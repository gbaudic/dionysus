/* 	Dionysus, a student pub management software in Java
    Copyright (C) 2011,2013  podgy_piglet

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

package fr.dionysus.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import fr.dionysus.*;
import javax.swing.JCheckBox;

public class NewUserDialog2 extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private User user;
	private JTextField nomField;
	private JTextField prenomField;
	private JTextField soldeField;
	private JSpinner promoChooser;
	private JCheckBox chckbxCaution;

	/**
	 * Create the dialog.
	 */
	public NewUserDialog2() {
		setTitle("User record");
		setModal(true);
		//setBounds(100, 100, 451, 190);
		setSize(451, 204);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNom = new JLabel("Last name:");
			GridBagConstraints gbc_lblNom = new GridBagConstraints();
			gbc_lblNom.anchor = GridBagConstraints.EAST;
			gbc_lblNom.insets = new Insets(0, 0, 5, 5);
			gbc_lblNom.gridx = 0;
			gbc_lblNom.gridy = 0;
			contentPanel.add(lblNom, gbc_lblNom);
		}
		{
			nomField = new JTextField();
			GridBagConstraints gbc_nomField = new GridBagConstraints();
			gbc_nomField.insets = new Insets(0, 0, 5, 0);
			gbc_nomField.fill = GridBagConstraints.HORIZONTAL;
			gbc_nomField.gridx = 1;
			gbc_nomField.gridy = 0;
			contentPanel.add(nomField, gbc_nomField);
			nomField.setColumns(10);
		}
		{
			JLabel lblPrenom = new JLabel("First name:");
			GridBagConstraints gbc_lblPrenom = new GridBagConstraints();
			gbc_lblPrenom.anchor = GridBagConstraints.EAST;
			gbc_lblPrenom.insets = new Insets(0, 0, 5, 5);
			gbc_lblPrenom.gridx = 0;
			gbc_lblPrenom.gridy = 1;
			contentPanel.add(lblPrenom, gbc_lblPrenom);
		}
		{
			prenomField = new JTextField();
			GridBagConstraints gbc_prenomField = new GridBagConstraints();
			gbc_prenomField.insets = new Insets(0, 0, 5, 0);
			gbc_prenomField.fill = GridBagConstraints.HORIZONTAL;
			gbc_prenomField.gridx = 1;
			gbc_prenomField.gridy = 1;
			contentPanel.add(prenomField, gbc_prenomField);
			prenomField.setColumns(10);
		}
		{
			JLabel lblPromo = new JLabel("Year: ");
			GridBagConstraints gbc_lblPromo = new GridBagConstraints();
			gbc_lblPromo.insets = new Insets(0, 0, 5, 5);
			gbc_lblPromo.gridx = 0;
			gbc_lblPromo.gridy = 2;
			contentPanel.add(lblPromo, gbc_lblPromo);
		}
		{
			//This part was specific to the university in question
			//The spinner is here to limit the range to realistic values
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 6);
			int currentPromo = cal.get(Calendar.YEAR) - 1909;
			SpinnerNumberModel m = new SpinnerNumberModel(currentPromo -1, currentPromo -4, currentPromo, 1);
			
			promoChooser = new JSpinner();
			promoChooser.setModel(m);
			GridBagConstraints gbc_promoChooser = new GridBagConstraints();
			gbc_promoChooser.fill = GridBagConstraints.BOTH;
			gbc_promoChooser.insets = new Insets(0, 0, 5, 0);
			gbc_promoChooser.gridx = 1;
			gbc_promoChooser.gridy = 2;
			contentPanel.add(promoChooser, gbc_promoChooser);
		}
		{
			JLabel lblSolde = new JLabel("Balance: ");
			GridBagConstraints gbc_lblSolde = new GridBagConstraints();
			gbc_lblSolde.anchor = GridBagConstraints.EAST;
			gbc_lblSolde.insets = new Insets(0, 0, 5, 5);
			gbc_lblSolde.gridx = 0;
			gbc_lblSolde.gridy = 3;
			contentPanel.add(lblSolde, gbc_lblSolde);
		}
		{
			soldeField = new JTextField();
			GridBagConstraints gbc_soldeField = new GridBagConstraints();
			gbc_soldeField.insets = new Insets(0, 0, 5, 0);
			gbc_soldeField.fill = GridBagConstraints.HORIZONTAL;
			gbc_soldeField.gridx = 1;
			gbc_soldeField.gridy = 3;
			contentPanel.add(soldeField, gbc_soldeField);
			soldeField.setColumns(10);
		}
		{
			chckbxCaution = new JCheckBox("Paid caution");
			GridBagConstraints gbc_chckbxCaution = new GridBagConstraints();
			gbc_chckbxCaution.gridx = 1;
			gbc_chckbxCaution.gridy = 4;
			contentPanel.add(chckbxCaution, gbc_chckbxCaution);
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
						if(!nomField.getText().isEmpty() && !prenomField.getText().isEmpty()){
							//TODO : Check
							Integer i = new Integer((Integer) promoChooser.getValue());
							double menton = 0; //the variable name is here a pun (in French)
							try{
								menton = Double.parseDouble(soldeField.getText());
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Failed to retrieve user balance!", "Error", JOptionPane.ERROR_MESSAGE);
							}
							user = new User(nomField.getText(), prenomField.getText(), i.intValue(), (int)(menton*100), chckbxCaution.isSelected());
							setVisible(false);
						} else {
							JOptionPane.showMessageDialog(null, "Incomplete name!", "Error", JOptionPane.WARNING_MESSAGE);
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
						user = null;
						setVisible(false);
					}			
				});
				
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public NewUserDialog2(User u){
		this();
		setUser(u);
	}
	
	public User getUser()
	{
		//this.setVisible(true);
		return this.user;
	}

	public void setUser(User u){
		this.user = u;
		
		if(user != null) {
			nomField.setText(user.getLastName());
			prenomField.setText(user.getFirstName());
			promoChooser.setValue(new Integer(user.getPromo()));
			soldeField.setText(String.valueOf(user.getBalance()));
			chckbxCaution.setSelected(user.hasPaidCaution());
		}
	}
}
