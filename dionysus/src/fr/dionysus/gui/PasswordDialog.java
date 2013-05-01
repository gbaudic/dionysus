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
import java.awt.GridLayout;
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

public class PasswordDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private char[] correctPassword;
	private JPasswordField passwordField;
	
	private boolean result = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PasswordDialog dialog = new PasswordDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PasswordDialog() {
		setResizable(false);
		setTitle("Password required");
		setModal(true);
		setSize(450,123);
		setLocationRelativeTo(null);
		//setBounds(100, 100, 450, 123);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(2, 1, 0, 0));
		{
			JLabel lblEnterLeWord = new JLabel("Enter password: ");
			lblEnterLeWord.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblEnterLeWord);
		}
		{
			passwordField = new JPasswordField();
			contentPanel.add(passwordField);
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
	 * In the original software, the file was hidden
	 */
	private void getCorrectPassword(){
		//Filename: passe.txt is set here
		File pass = new File("passe.txt");
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
	
	public boolean getResult(){
		return result;
	}
}
