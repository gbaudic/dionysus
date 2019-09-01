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

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewCategoryDialog extends JDialog {
	
	/**
	 * A dialog box to edit the categories
	 * This dialog does not manage articles for the categories
	 */
	private static final long serialVersionUID = -3243725927349670922L;
	private JTextField nameField;
	private JTextField imageField;
	private JPanel contentPanel;
	private final JFileChooser fc;
	
	public NewCategoryDialog() {
		fc = new JFileChooser();
		contentPanel = new JPanel();
		setModal(true);
		setLocationRelativeTo(null);
		setTitle("Category record");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 142, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel = new JLabel("Name:");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.anchor = GridBagConstraints.LINE_END;
			gbc_lblNewLabel.gridx = 1;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			nameField = new JTextField();
			GridBagConstraints gbc_nameField = new GridBagConstraints();
			gbc_nameField.insets = new Insets(0, 0, 5, 0);
			gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
			gbc_nameField.gridx = 2;
			gbc_nameField.gridy = 0;
			contentPanel.add(nameField, gbc_nameField);
			nameField.setColumns(10);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Image:");
			GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
			gbc_lblNewLabel_1.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblNewLabel_1.anchor = GridBagConstraints.LINE_END;
			gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_1.gridx = 1;
			gbc_lblNewLabel_1.gridy = 1;
			contentPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		}
		{
			imageField = new JTextField();
			GridBagConstraints gbc_imageField = new GridBagConstraints();
			gbc_imageField.insets = new Insets(0, 0, 5, 0);
			gbc_imageField.fill = GridBagConstraints.HORIZONTAL;
			gbc_imageField.gridx = 2;
			gbc_imageField.gridy = 1;
			contentPanel.add(imageField, gbc_imageField);
			imageField.setColumns(10);
		}
		{
			JButton btnImage = new JButton("...");
			GridBagConstraints gbc_btnImage = new GridBagConstraints();
			gbc_btnImage.insets = new Insets(0, 0, 5, 0);
			gbc_btnImage.gridx = 3;
			gbc_btnImage.gridy = 1;
			btnImage.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int returnVal = fc.showOpenDialog(contentPanel);
					if(returnVal == JFileChooser.APPROVE_OPTION){
						//TODO: check image and initialize the corresponding field
					}
				}
			});
			contentPanel.add(btnImage, gbc_btnImage);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				ImageIcon okIcon = new ImageIcon(getClass().getResource("/gtk-apply.png"));
				JButton okButton = new JButton("OK", okIcon);
				okButton.setActionCommand("OK");

				okButton.addActionListener((arg0) -> {
					try {
						if(nameField.getText().isEmpty())
							throw new IllegalArgumentException("Name cannot be blank.");
					} catch (NumberFormatException e){
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						JOptionPane.showMessageDialog(null, "Wrong parameter: \n"+e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
					}
				});

				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				ImageIcon cancelIcon = new ImageIcon(getClass().getResource("/gtk-cancel.png"));
				JButton cancelButton = new JButton("Cancel", cancelIcon);
				cancelButton.setActionCommand("Cancel");

				cancelButton.addActionListener((arg0) -> setVisible(false));

				buttonPane.add(cancelButton);
			}
		}
	}

	
}
