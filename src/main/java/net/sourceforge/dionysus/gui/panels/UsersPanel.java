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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.regex.PatternSyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import net.sourceforge.dionysus.User;
import net.sourceforge.dionysus.db.UserDB;
import net.sourceforge.dionysus.gui.NewUserDialog;
import net.sourceforge.dionysus.gui.models.UserTableModel;

/**
 * Class to host the users panel, with add/modify/remove/credit buttons
 *
 */
public class UsersPanel extends JPanel {

	private static final long serialVersionUID = 2128311951562983724L;
	private TableRowSorter<UserTableModel> userSorter;
	private UserTableModel utModel;
	private JTextField userRechercheField;
	private JTable userTable;

	private User currentUser;

	private UserDB users;

	/**
	 *
	 */
	public UsersPanel(UserDB usersDB) {
		super();

		this.users = usersDB;

		GridBagLayout gbl_comptesP = new GridBagLayout();
		gbl_comptesP.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_comptesP.rowHeights = new int[] { 0, 0, 0 };
		gbl_comptesP.columnWeights = new double[] { 1.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_comptesP.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gbl_comptesP);

		JLabel lblNewLabel = new JLabel("Search:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		userRechercheField = new JTextField();
		GridBagConstraints gbc_userRechercheField = new GridBagConstraints();
		gbc_userRechercheField.insets = new Insets(0, 0, 5, 5);
		gbc_userRechercheField.fill = GridBagConstraints.HORIZONTAL;
		gbc_userRechercheField.gridx = 1;
		gbc_userRechercheField.gridy = 0;
		lblNewLabel.setLabelFor(userRechercheField);
		userRechercheField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				newUserFilter();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				newUserFilter();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				newUserFilter();
			}
		});
		add(userRechercheField, gbc_userRechercheField);
		userRechercheField.setColumns(10);

		ImageIcon plus = new ImageIcon(getClass().getResource("/list-add.png"));
		ImageIcon minus = new ImageIcon(getClass().getResource("/list-remove.png"));
		ImageIcon edit = new ImageIcon(getClass().getResource("/gtk-edit.png"));
		JButton btnAddUser = new JButton("Add", plus);
		GridBagConstraints gbc_btnAddUser = new GridBagConstraints();
		gbc_btnAddUser.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddUser.gridx = 2;
		gbc_btnAddUser.gridy = 0;
		btnAddUser.addActionListener(this::addUser);
		add(btnAddUser, gbc_btnAddUser);

		JButton btnEditUser = new JButton("Edit", edit);
		GridBagConstraints gbc_btnEditUser = new GridBagConstraints();
		gbc_btnEditUser.insets = new Insets(0, 0, 5, 5);
		gbc_btnEditUser.gridx = 3;
		gbc_btnEditUser.gridy = 0;
		btnEditUser.addActionListener(this::editUser);
		add(btnEditUser, gbc_btnEditUser);

		JButton btnDeleteUser = new JButton("Delete", minus);
		GridBagConstraints gbc_btnDeleteUser = new GridBagConstraints();
//		gbc_btnDeleteUser.anchor = GridBagConstraints.EAST;
		gbc_btnDeleteUser.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteUser.gridx = 4;
		gbc_btnDeleteUser.gridy = 0;
		btnDeleteUser.addActionListener(this::deleteUser);
		add(btnDeleteUser, gbc_btnDeleteUser);

		JButton btnCredit = new JButton("Credit");
		btnCredit.setEnabled(false); // for 0.3 release
		GridBagConstraints gbc_btnCredit = new GridBagConstraints();
		gbc_btnCredit.fill = GridBagConstraints.BOTH; // make it taller for homogeneity
		gbc_btnCredit.insets = new Insets(0, 0, 5, 5);
		gbc_btnCredit.gridx = 5;
		gbc_btnCredit.gridy = 0;
		btnCredit.addActionListener((ActionEvent arg0) -> {
			int row = userTable.getSelectedRow();
			int realRow = -1;

			if (row >= 0) {
				realRow = userTable.convertRowIndexToModel(userTable.getSelectedRow());
				currentUser = users.getArray()[realRow];

				if (currentUser != null) {
					double credit = Double.parseDouble(JOptionPane.showInputDialog(null, "Credit: ", "Select amount",
							JOptionPane.QUESTION_MESSAGE));
					// TODO: open a window for credit, ask for amount and payment method
					// Perform changes, and register a transaction
				}
			} else {
				JOptionPane.showMessageDialog(null, "No user selected!", "Error", JOptionPane.WARNING_MESSAGE);
			}
		});
		add(btnCredit, gbc_btnCredit);

		userTable = new JTable();
		utModel = new UserTableModel(users.getArrayForTables());
		userTable.setModel(utModel);
		userSorter = new TableRowSorter<UserTableModel>(utModel);
		userTable.setRowSorter(userSorter);
		userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userTable.setFillsViewportHeight(true);
		// userTable.setAutoCreateRowSorter(true);

		JScrollPane scrollPane = new JScrollPane(userTable);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		// gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridwidth = 8;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);
	}

	/**
	 * ActionListener for add action
	 */
	private void addUser(ActionEvent e) {
		NewUserDialog newUser = new NewUserDialog();
		newUser.setVisible(true);
		User nu = newUser.getUser();
		users.add(nu);
		refreshTable();
	}

	/**
	 * ActionListener for delete action
	 *
	 */
	private void deleteUser(ActionEvent e) {
		int row = userTable.getSelectedRow();
		int realRow = -1;

		if (row >= 0) {
			realRow = userTable.convertRowIndexToModel(userTable.getSelectedRow());
			currentUser = users.getArray()[realRow];

			if (currentUser != null) {
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this account?", "",
						JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					users.remove(currentUser);
					utModel.fireTableRowsDeleted(realRow, realRow);
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "No user selected!", "Error", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * ActionListener for edit action
	 */
	private void editUser(ActionEvent arg0) {
		int row = userTable.getSelectedRow();
		int realRow = -1;

		if (row >= 0) {
			realRow = userTable.convertRowIndexToModel(userTable.getSelectedRow());
			currentUser = users.getArray()[realRow];

			if (currentUser != null) {
				NewUserDialog mud = new NewUserDialog(currentUser);
				mud.setVisible(true);
				currentUser = mud.getUser();
				users.modify(currentUser, realRow);
				refreshTable();
			}
		} else {
			JOptionPane.showMessageDialog(null, "No user selected!", "Error", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Filter for search box
	 */
	private void newUserFilter() {
		RowFilter<UserTableModel, Object> rf = null;
		// If current expression doesn't parse, don't update.
		try {
			rf = RowFilter.regexFilter("(?i)(?u)" + userRechercheField.getText());
		} catch (PatternSyntaxException e) {
			return;
		} catch (NullPointerException e) {
			return;
		}
		userSorter.setRowFilter(rf);
	}

	/**
	 * Regenerate data for the JTable
	 */
	public void refreshTable() {
		utModel.refreshData(users.getArrayForTables()); // TODO: improve
		utModel.fireTableDataChanged();
	}
}
