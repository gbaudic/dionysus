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

package net.sourceforge.dionysus.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sourceforge.dionysus.Category;

/**
 * A dialog box to edit the categories
 *
 * This dialog does not manage articles for the categories
 */
public class NewCategoryDialog extends JDialog {

	/** The UID for serialization */
	private static final long serialVersionUID = -3243725927349670922L;

	private Category category;

	/** Widget for category name */
	private final JTextField nameField;

	/** Widget for category image */
	private final JTextField imageField;

	/** UI holder */
	private final JPanel contentPanel;

	/** Picker for image */
	private final JFileChooser fc;

	/**
	 * Constructor
	 */
	public NewCategoryDialog() {
		fc = new JFileChooser();
		fc.setMultiSelectionEnabled(false);
		fc.setFileFilter(new FileNameExtensionFilter(Messages.getString("NewCategoryDialog.4"), //$NON-NLS-1$
				ImageIO.getReaderFileSuffixes()));
		contentPanel = new JPanel();
		setModal(true);
		setLocationRelativeTo(null);
		setTitle(Messages.getString("NewCategoryDialog.0")); //$NON-NLS-1$
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		final GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 142, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);

		final JLabel lblNewLabel = new JLabel(Messages.getString("NewCategoryDialog.1")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.LINE_END;
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		contentPanel.add(lblNewLabel, gbc_lblNewLabel);

		nameField = new JTextField();
		final GridBagConstraints gbc_nameField = new GridBagConstraints();
		gbc_nameField.insets = new Insets(0, 0, 5, 0);
		gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameField.gridx = 2;
		gbc_nameField.gridy = 0;
		contentPanel.add(nameField, gbc_nameField);
		nameField.setColumns(10);

		final JLabel lblNewLabel_1 = new JLabel(Messages.getString("NewCategoryDialog.2")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_1.anchor = GridBagConstraints.LINE_END;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 1;
		contentPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);

		imageField = new JTextField();
		final GridBagConstraints gbc_imageField = new GridBagConstraints();
		gbc_imageField.insets = new Insets(0, 0, 5, 0);
		gbc_imageField.fill = GridBagConstraints.HORIZONTAL;
		gbc_imageField.gridx = 2;
		gbc_imageField.gridy = 1;
		contentPanel.add(imageField, gbc_imageField);
		imageField.setColumns(10);

		final JButton btnImage = new JButton(Messages.getString("NewCategoryDialog.3")); //$NON-NLS-1$
		final GridBagConstraints gbc_btnImage = new GridBagConstraints();
		gbc_btnImage.insets = new Insets(0, 0, 5, 0);
		gbc_btnImage.gridx = 3;
		gbc_btnImage.gridy = 1;
		btnImage.addActionListener(this::onChooseImage);
		contentPanel.add(btnImage, gbc_btnImage);

		final JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		final JButton okButton = new JButton(Messages.getString("NewCategoryDialog.5"), Constants.ok); //$NON-NLS-1$
		okButton.setActionCommand(Messages.getString("NewCategoryDialog.6")); //$NON-NLS-1$

		okButton.addActionListener(this::onOK);

		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		final JButton cancelButton = new JButton(Messages.getString("NewCategoryDialog.11"), Constants.cancel); //$NON-NLS-1$
		cancelButton.setActionCommand(Messages.getString("NewCategoryDialog.12")); //$NON-NLS-1$

		cancelButton.addActionListener(arg0 -> setVisible(false));

		buttonPane.add(cancelButton);

	}

	/**
	 *
	 */
	private void onChooseImage(ActionEvent e) {
		final int returnVal = fc.showOpenDialog(contentPanel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// Try to populate the UI widget with the path to the selected file
			final File img = fc.getSelectedFile();
			try {
				imageField.setText(img.getCanonicalPath());
			} catch (final IOException e1) {
				JOptionPane.showMessageDialog(null, Messages.getString("NewCategoryDialog.10"), //$NON-NLS-1$
						Messages.getString("NewCategoryDialog.9"), //$NON-NLS-1$
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	/**
	 *
	 */
	private void onOK(ActionEvent evt) {
		try {
			if (nameField.getText().isEmpty()) {
				throw new IllegalArgumentException(Messages.getString("NewCategoryDialog.7")); //$NON-NLS-1$
			}
		} catch (final IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, Messages.getString("NewCategoryDialog.8") + e.getMessage(), //$NON-NLS-1$
					Messages.getString("NewCategoryDialog.9"), //$NON-NLS-1$
					JOptionPane.WARNING_MESSAGE);
		}
	}

}
