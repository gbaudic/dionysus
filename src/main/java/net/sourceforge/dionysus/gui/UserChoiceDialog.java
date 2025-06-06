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
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import net.sourceforge.dionysus.User;
import net.sourceforge.dionysus.db.UserDB;
import net.sourceforge.dionysus.gui.models.UserTableModel;

public class UserChoiceDialog extends JDialog {

	private static final long serialVersionUID = 3404606793383794213L;
	private final JPanel contentPanel;
	private final JTextField searchExpr;
	private final JTable resultsTable;
	private final JLabel searchExprCaption;
	private final TableRowSorter<UserTableModel> sorter;

	private User chosenUser;
	private UserDB theDB;
	private Object[][] foodForTable;

	/**
	 * Create the dialog.
	 */
	public UserChoiceDialog(UserDB udb) {
		contentPanel = new JPanel();
		setModal(true);
		setTitle(Messages.getString("UserChoiceDialog2.0")); //$NON-NLS-1$
		// setBounds(100, 100, 450, 300);
		setSize(450, 300);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		final GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);

		searchExprCaption = new JLabel(Messages.getString("UserChoiceDialog2.1")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPanel.add(searchExprCaption, gbc_lblNewLabel);

		searchExpr = new JTextField();
		searchExprCaption.setLabelFor(searchExpr);
		final GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		searchExpr.setColumns(10);

		searchExpr.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				newFilter();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				newFilter();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				newFilter();
			}
		});
		contentPanel.add(searchExpr, gbc_textField);

		if (udb != null) {
			theDB = udb;
			foodForTable = theDB.getArrayForTables();
		} else {
			foodForTable = new Object[][] {};
		}

		resultsTable = new JTable();

		final UserTableModel tModel = new UserTableModel(foodForTable);

		resultsTable.setModel(tModel);
		sorter = new TableRowSorter<>(tModel);
		resultsTable.setRowSorter(sorter);
		resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultsTable.setFillsViewportHeight(true);

		final JScrollPane tableSP = new JScrollPane(resultsTable);

		final GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.gridwidth = 2;
		gbc_table.insets = new Insets(0, 0, 0, 5);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 1;
		contentPanel.add(tableSP, gbc_table);

		final JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		final JButton okButton = new JButton(Messages.getString("UserChoiceDialog2.3"), Constants.ok); //$NON-NLS-1$
		okButton.setActionCommand(Messages.getString("UserChoiceDialog2.4")); //$NON-NLS-1$
		okButton.addActionListener(this::onOK);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		final JButton cancelButton = new JButton(Messages.getString("UserChoiceDialog2.6"), Constants.cancel); //$NON-NLS-1$
		cancelButton.setActionCommand(Messages.getString("UserChoiceDialog2.7")); //$NON-NLS-1$
		cancelButton.addActionListener(arg0 -> setVisible(false));

		buttonPane.add(cancelButton);

	}

	public User getUser() {
		return chosenUser;
	}

	private void newFilter() {
		RowFilter<UserTableModel, Object> rf = null;
		// If current expression doesn't parse, don't update.
		try {
			rf = RowFilter.regexFilter("(?i)(?u)" + searchExpr.getText()); //$NON-NLS-1$
		} catch (PatternSyntaxException | NullPointerException e) {
			return;
		}

		sorter.setRowFilter(rf);
	}

	/**
	 *
	 */
	private void onOK(ActionEvent evt) {
		// Choice of the user from the user which was selected in the table
		final int row = resultsTable.getSelectedRow();
		if (row >= 0) {
			final int realRow = resultsTable.convertRowIndexToModel(resultsTable.getSelectedRow());
			chosenUser = theDB.getArray()[realRow];
		}
		setVisible(false);
	}
}
