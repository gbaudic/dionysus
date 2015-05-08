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

package net.sourceforge.dionysus.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import net.sourceforge.dionysus.Transaction;
import net.sourceforge.dionysus.db.TransactionDB;
import net.sourceforge.dionysus.gui.TransactionTableModel;

/**
 * Class to hold the Transactions panel, with the revert button
 *
 */
public class TransactionsPanel extends JPanel {

	private static final long serialVersionUID = 280813945615261532L;
	private TableRowSorter<TransactionTableModel> transactionSorter;
	private TransactionTableModel ttModel;
	private JTextField transactionRechercheField;
	private JTable transactionTable;
	
	private TransactionDB journal;
	
	private Transaction currentTransaction;

	public TransactionsPanel(TransactionDB transactions){
		super();
		
		this.journal = transactions;
		
		GridBagLayout gbl_transactionsP = new GridBagLayout();
		gbl_transactionsP.columnWidths = new int[]{0, 0, 0, 0};
		gbl_transactionsP.rowHeights = new int[]{0, 0, 0};
		gbl_transactionsP.columnWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_transactionsP.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gbl_transactionsP);
		
		JLabel lblNewLabel_2 = new JLabel("Search: ");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 0;
		add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		transactionRechercheField = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 0;
		transactionRechercheField.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        newTransactionFilter();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        newTransactionFilter();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        newTransactionFilter();
                    }
                });
		add(transactionRechercheField, gbc_textField_1);
		transactionRechercheField.setColumns(10);
		
		ImageIcon cancel = new ImageIcon("images/gtk-cancel.png");
		//TODO: this is done at least 4 times in the code: factor!
		
		JButton btnNewButton_6 = new JButton("Cancel", cancel);
		GridBagConstraints gbc_btnNewButton_6 = new GridBagConstraints();
		gbc_btnNewButton_6.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_6.gridx = 2;
		gbc_btnNewButton_6.gridy = 0;
		btnNewButton_6.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = transactionTable.getSelectedRow();
				int realRow = -1;
				if(row >= 0){
					realRow = transactionTable.convertRowIndexToModel(transactionTable.getSelectedRow());
					currentTransaction = (Transaction)journal.getArray()[realRow];

					if(currentTransaction != null){
						int choice = JOptionPane.showConfirmDialog(null,"Are you sure to delete this transaction?","",JOptionPane.YES_NO_OPTION);
						if(choice == JOptionPane.YES_OPTION){
							journal.add(new Transaction(currentTransaction));
							//TODO : complete cancellation of effects (restore balances, stocks...) if asked

							choice = JOptionPane.showConfirmDialog(null,"Do you also want to revert its consequences?","",JOptionPane.YES_NO_OPTION);
							if(choice == JOptionPane.YES_OPTION)
								currentTransaction.revert();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "No transaction selected!", "Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		add(btnNewButton_6, gbc_btnNewButton_6);
		
		transactionTable = new JTable();
		TransactionTableModel ttModel = new TransactionTableModel(journal.getArrayForTables());
		transactionTable.setModel(ttModel);
		transactionSorter = new TableRowSorter<TransactionTableModel>(ttModel);
		transactionTable.setRowSorter(transactionSorter);
		transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		transactionTable.setFillsViewportHeight(true);
		//transactionTable.setAutoCreateRowSorter(true);
		
		JScrollPane t2SP = new JScrollPane(transactionTable);
		
		GridBagConstraints gbc_transactionTable = new GridBagConstraints();
		gbc_transactionTable.gridwidth = 6;
		gbc_transactionTable.fill = GridBagConstraints.BOTH;
		gbc_transactionTable.gridx = 0;
		gbc_transactionTable.gridy = 1;
		add(t2SP, gbc_transactionTable);
			
	}
	
	private void newTransactionFilter() {
        RowFilter<TransactionTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
        	rf = RowFilter.regexFilter("(?i)(?u)" + transactionRechercheField.getText());         
        } catch (PatternSyntaxException e) {
        	return;
        } catch (NullPointerException e) {
        	return;
        }
        transactionSorter.setRowFilter(rf);
    }
	
	public void refreshTable(){
		ttModel.fireTableDataChanged();
	}
}
