/* 	Dionysus, a student pub management software in Java
    Copyright (C) 2011,2013,2015-2019  G. Baudic

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package net.sourceforge.dionysus.gui.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

/**
 * The scanner frame, to use the webcam as a barcode scanner
 */
public class ScannerFrame extends JFrame implements Runnable, ThreadFactory {

	private static final long serialVersionUID = 6441489157408381878L;
	private static final long INTERVAL_BETWEEN_SCANS_MS = 1000;

	private Executor executor = Executors.newSingleThreadExecutor(this);

	private Webcam webcam;
	private WebcamPanel panel;
	/** Result of last scan, to avoid unwanted multiple scans of the same product */
	private String lastParsedText;
	/** Date of last scan, to avoid unwanted multiple scans of the same product */
	private Instant lastScan;
	/** Observers for parsed barcodes */
	private List<Consumer<String>> observers;

	/**
	 * Constructor
	 */
	public ScannerFrame() {
		super();

		setLayout(new FlowLayout());
		setTitle("Scan preview");

		Dimension size = WebcamResolution.VGA.getSize();

		webcam = Webcam.getWebcams().get(0);
		// TODO gracefully manage case of no webcam or multiple ones
		webcam.setViewSize(size);

		panel = new WebcamPanel(webcam);
		panel.setPreferredSize(size);
		panel.setFPSDisplayed(false);

		add(panel);

		pack();
		setVisible(true);

		lastScan = Instant.now();
		observers = new ArrayList<>();

		executor.execute(this);
	}

	/**
	 * Add an observer for scanned barcodes
	 *
	 * @param observer the observer to add
	 */
	public void addObserver(Consumer<String> observer) {
		observers.add(observer);
	}

	/** {@inheritDoc} */
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "scanner");
		t.setDaemon(true);
		return t;
	}

	/** {@inheritDoc} */
	@Override
	public void run() {

		do {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Result result = null;
			BufferedImage image = null;

			if (webcam.isOpen()) {

				if ((image = webcam.getImage()) == null) {
					continue;
				}

				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

				try {
					result = new MultiFormatReader().decode(bitmap);
				} catch (NotFoundException e) {
					// fall thru, it means there is no QR code in image
				}
			}

			if (result != null) {
				String contents = result.getText();
				Instant now = Instant.now();

				// Secure against multiple reads of the same article
				if (!contents.equals(lastParsedText) || now.isAfter(lastScan.plusMillis(INTERVAL_BETWEEN_SCANS_MS))) {
					lastParsedText = contents;
					lastScan = now;

					// Submit data to consumers
					for (Consumer<String> c : observers) {
						c.accept(lastParsedText);
					}
				}
			}

		} while (true);
	}
}
