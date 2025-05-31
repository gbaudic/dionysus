/* 	Dionysus, a student pub management software in Java
    Copyright (C) 2025 G. Baudic

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
package net.sourceforge.dionysus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public final class Settings {
	private static Settings instance;
	private static final String PROPERTIES_FILE = "dionysus.ini";

	// Property keys.
	private static final String USE_SCANNER = "useScanner";
	private static final String TICKET_HEADER = "ticketHeader";
	private static final String TICKET_FOOTER = "ticketFooter";

	// Thread-safe getter to obtain the unique instance.
	public static synchronized Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	private final Properties properties;

	private Settings() {
		properties = new Properties();
		loadProperties();
	}

	public String getTicketFooter() {
		return properties.getProperty(TICKET_FOOTER, "");
	}

	public String getTicketHeader() {
		return properties.getProperty(TICKET_HEADER, "");
	}

	public boolean getUseScanner() {
		return Boolean.parseBoolean(properties.getProperty(USE_SCANNER, "false"));
	}

	/**
	 * Public method to reload properties from disk. Only used from unit tests
	 *
	 * @throws IOException
	 */
	void load() throws IOException {
		load(PROPERTIES_FILE);
	}

	/**
	 * Public method to reload properties from disk. Only used from unit tests
	 *
	 * @param path path to the properties file
	 *
	 * @throws IOException
	 */
	void load(String path) throws IOException {
		properties.clear();
		loadProperties(path);
	}

	// Loads the properties from the file.
	// The file is created if it does not exist.
	private void loadProperties() {
		loadProperties(PROPERTIES_FILE);
	}

	// Loads the properties from the file.
	// The file is created if it does not exist.
	private void loadProperties(String path) {
		final File file = new File(path);
		if (!file.exists()) {
			try {
				// Create the file if it does not exist.
				file.createNewFile();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		try (InputStream in = new FileInputStream(file)) {
			properties.load(in);
		} catch (final IOException e) {
			// It is possible that the file is empty; in that case, we don't throw.
			e.printStackTrace();
		}
	}

	/**
	 * Saves the current properties to disk.
	 *
	 * @throws IOException
	 */
	public void save() throws IOException {
		try (OutputStream out = new FileOutputStream(PROPERTIES_FILE)) {
			properties.store(out, null);
		}
	}

	public void setTicketFooter(String value) {
		properties.setProperty(TICKET_FOOTER, value);
	}

	public void setTicketHeader(String value) {
		properties.setProperty(TICKET_HEADER, value);
	}

	public void setUseScanner(boolean value) {
		properties.setProperty(USE_SCANNER, Boolean.toString(value));
	}
}
