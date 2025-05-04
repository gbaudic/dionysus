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
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import net.sourceforge.dionysus.User;

/**
 * The dialog box to add a new user to the system
 *
 * This class contains both the GUI code and the software logic
 *
 */
public class NewUserDialog extends JDialog {

	private static final long serialVersionUID = -6444446674902515180L;
	private final JPanel contentPanel = new JPanel();
	private User user;
	private JTextField lastNameField;
	private JTextField firstNameField;
	private JTextField idField;
	private JTextField balanceField;
	private JSpinner promoChooser;
	private JCheckBox chckbxCaution;

	/**
	 * Create the dialog. The dialog is intended for creation, so all fields will be
	 * empty
	 */
	public NewUserDialog() {
		setTitle(Messages.getString("NewUserDialog2.0")); //$NON-NLS-1$
		setModal(true);
		setSize(451, 254);
		setLocationRelativeTo(null); // centering
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		final GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);

		final JLabel lblNom = new JLabel(Messages.getString("NewUserDialog2.1")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblNom = new GridBagConstraints();
		gbc_lblNom.anchor = GridBagConstraints.EAST;
		gbc_lblNom.insets = new Insets(0, 0, 5, 5);
		gbc_lblNom.gridx = 0;
		gbc_lblNom.gridy = 0;
		contentPanel.add(lblNom, gbc_lblNom);

		lastNameField = new JTextField();
		final GridBagConstraints gbc_nomField = new GridBagConstraints();
		gbc_nomField.insets = new Insets(0, 0, 5, 0);
		gbc_nomField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nomField.gridx = 1;
		gbc_nomField.gridy = 0;
		contentPanel.add(lastNameField, gbc_nomField);
		lastNameField.setColumns(10);

		final JLabel lblPrenom = new JLabel(Messages.getString("NewUserDialog2.2")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblPrenom = new GridBagConstraints();
		gbc_lblPrenom.anchor = GridBagConstraints.EAST;
		gbc_lblPrenom.insets = new Insets(0, 0, 5, 5);
		gbc_lblPrenom.gridx = 0;
		gbc_lblPrenom.gridy = 1;
		contentPanel.add(lblPrenom, gbc_lblPrenom);

		firstNameField = new JTextField();
		final GridBagConstraints gbc_prenomField = new GridBagConstraints();
		gbc_prenomField.insets = new Insets(0, 0, 5, 0);
		gbc_prenomField.fill = GridBagConstraints.HORIZONTAL;
		gbc_prenomField.gridx = 1;
		gbc_prenomField.gridy = 1;
		contentPanel.add(firstNameField, gbc_prenomField);
		firstNameField.setColumns(10);

		final JLabel lblID = new JLabel(Messages.getString("NewUserDialog2.3")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblID = new GridBagConstraints();
		gbc_lblID.anchor = GridBagConstraints.EAST;
		gbc_lblID.insets = new Insets(0, 0, 5, 5);
		gbc_lblID.gridx = 0;
		gbc_lblID.gridy = 2;
		contentPanel.add(lblID, gbc_lblID);

		idField = new JTextField();
		idField.setToolTipText(Messages.getString("NewUserDialog2.4")); //$NON-NLS-1$
		final GridBagConstraints gbc_idField = new GridBagConstraints();
		gbc_idField.insets = new Insets(0, 0, 5, 0);
		gbc_idField.fill = GridBagConstraints.HORIZONTAL;
		gbc_idField.gridx = 1;
		gbc_idField.gridy = 2;
		contentPanel.add(idField, gbc_idField);
		idField.setColumns(13);

		final JLabel lblPromo = new JLabel(Messages.getString("NewUserDialog2.5")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblPromo = new GridBagConstraints();
		gbc_lblPromo.insets = new Insets(0, 0, 5, 5);
		gbc_lblPromo.gridx = 0;
		gbc_lblPromo.gridy = 3;
		contentPanel.add(lblPromo, gbc_lblPromo);

		// This part was specific to the university in question
		// The spinner is here to limit the range to realistic values
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 6);
		final int currentPromo = cal.get(Calendar.YEAR) - 1909;
		final SpinnerNumberModel m = new SpinnerNumberModel(currentPromo - 1, currentPromo - 4, currentPromo, 1);

		promoChooser = new JSpinner();
		promoChooser.setModel(m);
		final GridBagConstraints gbc_promoChooser = new GridBagConstraints();
		gbc_promoChooser.fill = GridBagConstraints.BOTH;
		gbc_promoChooser.insets = new Insets(0, 0, 5, 0);
		gbc_promoChooser.gridx = 1;
		gbc_promoChooser.gridy = 3;
		contentPanel.add(promoChooser, gbc_promoChooser);

		final JLabel lblSolde = new JLabel(Messages.getString("NewUserDialog2.6")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblSolde = new GridBagConstraints();
		gbc_lblSolde.anchor = GridBagConstraints.EAST;
		gbc_lblSolde.insets = new Insets(0, 0, 5, 5);
		gbc_lblSolde.gridx = 0;
		gbc_lblSolde.gridy = 4;
		contentPanel.add(lblSolde, gbc_lblSolde);

		balanceField = new JTextField();
		final GridBagConstraints gbc_soldeField = new GridBagConstraints();
		gbc_soldeField.insets = new Insets(0, 0, 5, 0);
		gbc_soldeField.fill = GridBagConstraints.HORIZONTAL;
		gbc_soldeField.gridx = 1;
		gbc_soldeField.gridy = 4;
		contentPanel.add(balanceField, gbc_soldeField);
		balanceField.setColumns(10);

		chckbxCaution = new JCheckBox(Messages.getString("NewUserDialog2.7")); //$NON-NLS-1$
		final GridBagConstraints gbc_chckbxCaution = new GridBagConstraints();
		gbc_chckbxCaution.gridx = 1;
		gbc_chckbxCaution.gridy = 5;
		contentPanel.add(chckbxCaution, gbc_chckbxCaution);

		final JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		final JButton okButton = new JButton(Messages.getString("NewUserDialog2.9"), Constants.ok); //$NON-NLS-1$
		okButton.setActionCommand(Messages.getString("NewUserDialog2.10")); //$NON-NLS-1$

		okButton.addActionListener(arg0 -> {
			if (!lastNameField.getText().isEmpty() && !firstNameField.getText().isEmpty()) {
				// TODO : Check
				final Integer i = (int) promoChooser.getValue();
				double menton = 0; // the variable name is here a pun (in French)

				try {
					menton = Double.parseDouble(balanceField.getText());
				} catch (final NumberFormatException e) {
					JOptionPane.showMessageDialog(null, Messages.getString("NewUserDialog2.11"), //$NON-NLS-1$
							Messages.getString("NewUserDialog2.12"), //$NON-NLS-1$
							JOptionPane.WARNING_MESSAGE);
					return; // do not create the user!
				}
				// Finally, we create the user
				user = new User(lastNameField.getText(), firstNameField.getText(), i.intValue(), (int) (menton * 100),
						chckbxCaution.isSelected());
				user.setID(idField.getText());
				setVisible(false);
			} else {
				JOptionPane.showMessageDialog(null, Messages.getString("NewUserDialog2.13"), //$NON-NLS-1$
						Messages.getString("NewUserDialog2.14"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
			}
		});

		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		final JButton cancelButton = new JButton(Messages.getString("NewUserDialog2.16"), Constants.cancel); //$NON-NLS-1$
		cancelButton.setActionCommand(Messages.getString("NewUserDialog2.17")); //$NON-NLS-1$

		cancelButton.addActionListener(arg0 -> {
			user = null;
			setVisible(false);
		});

		buttonPane.add(cancelButton);

	}

	/**
	 * Constructor for edition
	 *
	 * @param u the user to be edited
	 */
	public NewUserDialog(User u) {
		this();
		setUser(u);
	}

	/**
	 * Getter for the current user
	 *
	 * @return the user being edited/created
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Initializes the contents of the dialog with preexisting data
	 *
	 * This is required when using this dialog for edition instead of creation
	 *
	 * @param u the user to edit
	 */
	private void setUser(User u) {
		this.user = u;

		if (user != null) {
			lastNameField.setText(user.getLastName());
			firstNameField.setText(user.getFirstName());
			idField.setText(user.getID());
			promoChooser.setValue(user.getPromo());
			balanceField.setText(String.valueOf(user.getBalance()));
			chckbxCaution.setSelected(user.hasPaidCaution());
		}
	}
}
