/* 	Dionysus, a student pub management software in Java
    Copyright (C) 2011,2013,2015  podgy_piglet

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * The initial dialog box, which is intended to protect the software from unauthorized users
 *
 */
public class PasswordDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final String passFile = "passe.txt";
	private final JPanel contentPanel = new JPanel();
	private char[] correctPassword;
	private JPasswordField passwordField;
	
	private boolean result = false;
	private JTextField loginField; //Login field to allow multiple users (with each one having a login/password pair)

	/**
	 * Create the dialog.
	 */
	public PasswordDialog() {
		setResizable(false);
		setTitle("Identification required");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setSize(450,123);
		setLocationRelativeTo(null);
		//setBounds(100, 100, 450, 123);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{46, 377, 0};
		gbl_contentPanel.rowHeights = new int[]{20, 20, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblLogin = new JLabel("Login");
			lblLogin.setHorizontalAlignment(SwingConstants.RIGHT);
			GridBagConstraints gbc_lblLogin = new GridBagConstraints();
			gbc_lblLogin.anchor = GridBagConstraints.EAST;
			gbc_lblLogin.insets = new Insets(0, 0, 5, 5);
			gbc_lblLogin.gridx = 0;
			gbc_lblLogin.gridy = 0;
			contentPanel.add(lblLogin, gbc_lblLogin);
		}
		{
			loginField = new JTextField();
			loginField.setText("default");
			loginField.setEnabled(false);
			GridBagConstraints gbc_loginField = new GridBagConstraints();
			gbc_loginField.anchor = GridBagConstraints.NORTH;
			gbc_loginField.fill = GridBagConstraints.HORIZONTAL;
			gbc_loginField.insets = new Insets(0, 0, 5, 0);
			gbc_loginField.gridx = 1;
			gbc_loginField.gridy = 0;
			contentPanel.add(loginField, gbc_loginField);
			loginField.setColumns(10);
		}
		{
			JLabel lblEnterLeWord = new JLabel("Password");
			lblEnterLeWord.setHorizontalAlignment(SwingConstants.RIGHT);
			GridBagConstraints gbc_lblEnterLeWord = new GridBagConstraints();
			gbc_lblEnterLeWord.anchor = GridBagConstraints.EAST;
			gbc_lblEnterLeWord.insets = new Insets(0, 0, 0, 5);
			gbc_lblEnterLeWord.gridx = 0;
			gbc_lblEnterLeWord.gridy = 1;
			contentPanel.add(lblEnterLeWord, gbc_lblEnterLeWord);
		}
		{
			passwordField = new JPasswordField();
			GridBagConstraints gbc_passwordField = new GridBagConstraints();
			gbc_passwordField.anchor = GridBagConstraints.NORTH;
			gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
			gbc_passwordField.gridx = 1;
			gbc_passwordField.gridy = 1;
			contentPanel.add(passwordField, gbc_passwordField);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				ImageIcon okIcon = new ImageIcon("images/gtk-apply.png");
				JButton okButton = new JButton("OK", okIcon);
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						if(checkPassword(passwordField.getPassword())){
							//Hide this dialog box and show the main window
							result = true;
							setVisible(false);
							MainGUI2 frame = new MainGUI2();
							frame.setVisible(true);
						} else {
							JOptionPane.showMessageDialog(null, "Wrong password.\nPlease retry.", "Error", JOptionPane.ERROR_MESSAGE);
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
						System.exit(0);
					}			
				});
				buttonPane.add(cancelButton);
			}
		}
		
		getCorrectPassword();
	}

	/**
	 * Loads the password from a text file (stored in clear...)
	 * In the original software, the file was hidden - but the data was plain text
	 */
	private void getCorrectPassword(){
		//Filename: passe.txt is set here
		File pass = new File(passFile);
		try {
			FileReader fr = new FileReader(pass);
			String str = "";
			int i = 0;
			while((i = fr.read()) != -1)
				str += (char)i;
			correctPassword = str.toCharArray();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Checks the provided password
	 * @param input a character array
	 * @return the result of the check
	 */
	private boolean checkPassword(char[] input){
		boolean isCorrect = false;
		
		if(input.length != correctPassword.length){
			isCorrect = false;
		} else {
			isCorrect = Arrays.equals(input, correctPassword);
		}
		
		if(isCorrect)
			Arrays.fill(correctPassword, '0');
		
		return isCorrect;
	}	
	
	/**
	 * Getter for identification result
	 * @return true if identification succeeded, false otherwise
	 */
	public boolean getResult(){
		return result;
	}
}
