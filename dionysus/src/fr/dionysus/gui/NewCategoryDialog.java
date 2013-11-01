package fr.dionysus.gui;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
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
	 * 
	 */
	private static final long serialVersionUID = -3243725927349670922L;
	private JTextField nameField;
	private JTextField imageField;
	private JPanel contentPanel = new JPanel();
	private final JFileChooser fc = new JFileChooser();
	
	public NewCategoryDialog() {
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
					// TODO Auto-generated method stub
					int returnVal = fc.showOpenDialog(contentPanel);
				}
			});
			contentPanel.add(btnImage, gbc_btnImage);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				ImageIcon okIcon = new ImageIcon("images/gtk-apply.png");
				JButton okButton = new JButton("OK", okIcon);
				okButton.setActionCommand("OK");

				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							
						} catch (NumberFormatException e){
							e.printStackTrace();
						}
					}
				});

				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				ImageIcon cancelIcon = new ImageIcon("images/gtk-cancel.png");
				JButton cancelButton = new JButton("Cancel", cancelIcon);
				cancelButton.setActionCommand("Cancel");

				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						
						setVisible(false);
					}			
				});

				buttonPane.add(cancelButton);
			}
		}
	}

	
}
