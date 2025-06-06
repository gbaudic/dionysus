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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.sourceforge.dionysus.Vendor;

/**
 * The initial dialog box, which is intended to protect the software from
 * unauthorized users
 */
public class PasswordDialog extends JDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = -3434784729931468513L;
	private static final String loginFile = "logins.txt"; //$NON-NLS-1$
	private final JPanel contentPanel;
	private char[] correctPassword;
	private final JPasswordField passwordField;
	private HashMap<String, Vendor> database;
	private Vendor chosenVendor;

	private boolean result = false;
	/**
	 * Login field to allow multiple users (with each one having a login/password
	 * pair)
	 */
	private final JTextField loginField;

	/**
	 * Create the dialog.
	 */
	public PasswordDialog() {
		contentPanel = new JPanel();
		setResizable(false);
		setTitle(Messages.getString("PasswordDialog.2")); //$NON-NLS-1$
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setSize(450, 123);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		final GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 46, 377, 0 };
		gbl_contentPanel.rowHeights = new int[] { 20, 20, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);

		final JLabel lblLogin = new JLabel(Messages.getString("PasswordDialog.3")); //$NON-NLS-1$
		lblLogin.setHorizontalAlignment(SwingConstants.RIGHT);
		final GridBagConstraints gbc_lblLogin = new GridBagConstraints();
		gbc_lblLogin.anchor = GridBagConstraints.EAST;
		gbc_lblLogin.insets = new Insets(0, 0, 5, 5);
		gbc_lblLogin.gridx = 0;
		gbc_lblLogin.gridy = 0;
		contentPanel.add(lblLogin, gbc_lblLogin);

		loginField = new JTextField();
		final GridBagConstraints gbc_loginField = new GridBagConstraints();
		gbc_loginField.anchor = GridBagConstraints.NORTH;
		gbc_loginField.fill = GridBagConstraints.HORIZONTAL;
		gbc_loginField.insets = new Insets(0, 0, 5, 0);
		gbc_loginField.gridx = 1;
		gbc_loginField.gridy = 0;
		contentPanel.add(loginField, gbc_loginField);
		loginField.setColumns(10);

		final JLabel lblEnterLeWord = new JLabel(Messages.getString("PasswordDialog.4")); //$NON-NLS-1$
		lblEnterLeWord.setHorizontalAlignment(SwingConstants.RIGHT);
		final GridBagConstraints gbc_lblEnterLeWord = new GridBagConstraints();
		gbc_lblEnterLeWord.anchor = GridBagConstraints.EAST;
		gbc_lblEnterLeWord.insets = new Insets(0, 0, 0, 5);
		gbc_lblEnterLeWord.gridx = 0;
		gbc_lblEnterLeWord.gridy = 1;
		contentPanel.add(lblEnterLeWord, gbc_lblEnterLeWord);

		passwordField = new JPasswordField();
		final GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.anchor = GridBagConstraints.NORTH;
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 1;
		contentPanel.add(passwordField, gbc_passwordField);

		final JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		final JButton okButton = new JButton(Messages.getString("PasswordDialog.6"), Constants.ok); //$NON-NLS-1$
		okButton.setActionCommand(Messages.getString("PasswordDialog.7")); //$NON-NLS-1$
		okButton.addActionListener(this::onOK);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		final JButton cancelButton = new JButton(Messages.getString("PasswordDialog.13"), Constants.cancel); //$NON-NLS-1$
		cancelButton.setActionCommand(Messages.getString("PasswordDialog.14")); //$NON-NLS-1$
		cancelButton.addActionListener(arg0 -> {
			setVisible(false);
			System.exit(0);
		});
		buttonPane.add(cancelButton);

		loadDatabase();
	}

	/**
	 * Checks the existence of the login provided, and initializes passwords
	 *
	 * @return true if login exists, false otherwise
	 */
	private boolean checkIdentification() {
		final String targetLogin = loginField.getText();

		chosenVendor = database.get(targetLogin);

		if (chosenVendor == null) {
			return false;
		} else {
			correctPassword = chosenVendor.getcPassword().toCharArray();
			return true;
		}

	}

	/**
	 * Checks the provided password
	 *
	 * @param input a character array
	 * @return the result of the check
	 */
	private boolean checkPassword(char[] input) {
		boolean isCorrect = false;

		if (input.length != correctPassword.length) {
			isCorrect = false;
		} else {
			isCorrect = Arrays.equals(input, correctPassword);
		}

		if (isCorrect) {
			Arrays.fill(correctPassword, '0');
		}

		return isCorrect;
	}

	/**
	 * Getter for identification result
	 *
	 * @return true if identification succeeded, false otherwise
	 */
	public boolean getResult() {
		return result;
	}

	/**
	 * Load the contents of the logins.txt file
	 */
	private void loadDatabase() {
		database = new HashMap<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(loginFile)); Scanner s = new Scanner(reader)) {

			String line = s.nextLine();

			while (line != null) {
				if (!line.startsWith("#")) { //$NON-NLS-1$
					final String[] pieces = line.split(":"); //$NON-NLS-1$
					if (pieces.length == 3) {
						// Format is login:display_name:password
						database.put(pieces[0], new Vendor(pieces[0], pieces[1], pieces[2]));
					} else {
						JOptionPane.showMessageDialog(null, Messages.getString("PasswordDialog.21"), //$NON-NLS-1$
								Messages.getString("PasswordDialog.22"), //$NON-NLS-1$
								JOptionPane.WARNING_MESSAGE);
					}
				}

				line = s.nextLine();
			}

		} catch (final IOException e) {
			JOptionPane.showMessageDialog(null, Messages.getString("PasswordDialog.23"), //$NON-NLS-1$
					Messages.getString("PasswordDialog.24"), //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1); // We need to exit here, because there is no way the user can login.
		} catch (final NoSuchElementException e) {
			// do nothing, we simply have reached the end of the file
		}
	}

	/**
	 *
	 */
	private void onOK(ActionEvent evt) {
		if (checkIdentification()) {
			if (checkPassword(passwordField.getPassword())) {
				// Hide this dialog box and show the main window
				result = true;
				passwordField.setText(null);
				setVisible(false);
				final MainGUI2 frame = new MainGUI2();
				frame.setCurrentVendor(chosenVendor);
				frame.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(null, Messages.getString("PasswordDialog.8"), //$NON-NLS-1$
						Messages.getString("PasswordDialog.9"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, Messages.getString("PasswordDialog.10"), //$NON-NLS-1$
					Messages.getString("PasswordDialog.11"), //$NON-NLS-1$
					JOptionPane.WARNING_MESSAGE);
		}
	}
}
