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

package net.sourceforge.dionysus.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.regex.PatternSyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import net.sourceforge.dionysus.db.Database;

public class SAEDPanel<T extends DefaultTableModel> extends JPanel {

	/**
	 * A generic JPanel to hold the databases views.
	 * SAED stands for Search, Add, Edit, Delete which are the actions available in the first row of the panel.
	 */
	private static final long serialVersionUID = 178212460318145049L;
	private JTextField searchField;
	private JTable table;
	private TableRowSorter<T> sorter;
	
	public SAEDPanel(ActionListener addAL, ActionListener editAL, ActionListener deleteAL,
			T tModel, Database db){
		
		ImageIcon plus = new ImageIcon("images/list-add.png");
		ImageIcon minus = new ImageIcon("images/list-remove.png");
		ImageIcon edit = new ImageIcon("images/gtk-edit.png");
		
		GridBagLayout gbl_articlesP = new GridBagLayout();
		gbl_articlesP.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_articlesP.rowHeights = new int[]{0, 0, 0};
		gbl_articlesP.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_articlesP.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gbl_articlesP);
		
		JLabel lblNewLabel_1 = new JLabel("Search:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		this.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		searchField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		searchField.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        newFilter();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        newFilter();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        newFilter();
                    }
                });
		this.add(searchField, gbc_textField);
		searchField.setColumns(10);
		
		if(addAL != null){
			JButton btnAddArticle = new JButton("Add", plus);
			GridBagConstraints gbc_btnAddArticle = new GridBagConstraints();
			gbc_btnAddArticle.insets = new Insets(0, 0, 5, 5);
			gbc_btnAddArticle.gridx = 2;
			gbc_btnAddArticle.gridy = 0;
			btnAddArticle.addActionListener(addAL);
			this.add(btnAddArticle, gbc_btnAddArticle);
		}
		
		if(editAL != null){
			JButton btnEditArticle = new JButton("Edit", edit);
			GridBagConstraints gbc_btnEditArticle = new GridBagConstraints();
			gbc_btnEditArticle.anchor = GridBagConstraints.NORTHEAST;
			gbc_btnEditArticle.insets = new Insets(0, 0, 5, 5);
			gbc_btnEditArticle.gridx = 3;
			gbc_btnEditArticle.gridy = 0;
			btnEditArticle.addActionListener(editAL);
			this.add(btnEditArticle, gbc_btnEditArticle);
		}
		
		JButton btnDeleteArticle = new JButton("Delete", minus);
		GridBagConstraints gbc_btnDeleteArticle = new GridBagConstraints();
		gbc_btnDeleteArticle.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeleteArticle.gridx = 4;
		gbc_btnDeleteArticle.gridy = 0;
		btnDeleteArticle.addActionListener(deleteAL);
		this.add(btnDeleteArticle, gbc_btnDeleteArticle);
		
		table = new JTable();
		//T atModel = new T(db.getArrayForTables());
		table.setModel(tModel);
		sorter = new TableRowSorter<T>(tModel);
		table.setRowSorter(sorter);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		
		JScrollPane t1SP = new JScrollPane(table);
		
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.gridwidth = 8;
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 1;
		this.add(t1SP, gbc_table);
		
	}
	
	private void newFilter() {
        RowFilter<T, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
        	rf = RowFilter.regexFilter("(?i)(?u)" + searchField.getText());         
        } catch (PatternSyntaxException e) {
        	return;
        } catch (NullPointerException e) {
        	return;
        }
        sorter.setRowFilter(rf);
    }

}
