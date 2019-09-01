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

import net.sourceforge.dionysus.Article;
import net.sourceforge.dionysus.db.ArticleDB;
import net.sourceforge.dionysus.gui.ArticleTableModel;
import net.sourceforge.dionysus.gui.NewArticleDialog;

/**
 * 
 * Class to hold the Articles panel, with add/edit/delete buttons
 *
 */
public class ArticlesPanel extends JPanel {

	private static final long serialVersionUID = 748075565665356634L;
	private TableRowSorter<ArticleTableModel> articleSorter;
	private ArticleTableModel atModel;
	private JTextField articleRechercheField;
	private JTable articleTable;
	
	private Article currentArticle;
	
	private ArticleDB catalogue;
	
	/**
	 * 
	 */
	public ArticlesPanel(ArticleDB articleDB) {
		super();
		
		catalogue = articleDB;
		
		GridBagLayout gbl_articlesP = new GridBagLayout();
		gbl_articlesP.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_articlesP.rowHeights = new int[]{0, 0, 0};
		gbl_articlesP.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_articlesP.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gbl_articlesP);
		
		JLabel lblNewLabel_1 = new JLabel("Search:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		articleRechercheField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		articleRechercheField.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        newArticleFilter();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        newArticleFilter();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        newArticleFilter();
                    }
                });
		add(articleRechercheField, gbc_textField);
		articleRechercheField.setColumns(10);
		
		ImageIcon plus = new ImageIcon(getClass().getResource("/list-add.png"));
		ImageIcon minus = new ImageIcon(getClass().getResource("/list-remove.png"));
		ImageIcon edit = new ImageIcon(getClass().getResource("/gtk-edit.png"));		
		
		JButton btnAddArticle = new JButton("Add", plus);
		GridBagConstraints gbc_btnAddArticle = new GridBagConstraints();
		gbc_btnAddArticle.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddArticle.gridx = 2;
		gbc_btnAddArticle.gridy = 0;
		btnAddArticle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewArticleDialog nad = new NewArticleDialog();
				nad.setVisible(true);
				Article a = nad.getArticle();
				if(a != null) {
					Article b = catalogue.getArticleByCode(a.getCode());
					if(b == null) {
						catalogue.add(a);
						refreshTable();
					} else {
						JOptionPane.showMessageDialog(null, "An article with this code already exists.", "Error", JOptionPane.WARNING_MESSAGE);
						nad.setVisible(true); //let the user edit to fix the error
					}
				}
			}
		});
		add(btnAddArticle, gbc_btnAddArticle);
		
		JButton btnEditArticle = new JButton("Edit", edit);
		GridBagConstraints gbc_btnEditArticle = new GridBagConstraints();
		gbc_btnEditArticle.anchor = GridBagConstraints.NORTHEAST;
		gbc_btnEditArticle.insets = new Insets(0, 0, 5, 5);
		gbc_btnEditArticle.gridx = 3;
		gbc_btnEditArticle.gridy = 0;
		btnEditArticle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = articleTable.getSelectedRow();
				int realRow = -1;
				if(row >= 0){
					realRow = articleTable.convertRowIndexToModel(articleTable.getSelectedRow());
					currentArticle = (Article)catalogue.getArray()[realRow];
					
					if(currentArticle != null){
						NewArticleDialog nad = new NewArticleDialog();
						nad.setArticle(currentArticle);
						nad.setVisible(true);
						currentArticle = nad.getArticle();
						//Keep original index to modify without adding
						catalogue.modify(currentArticle, realRow);
						refreshTable();
					} 
				} else {
					JOptionPane.showMessageDialog(null, "No article selected!", "Error", JOptionPane.WARNING_MESSAGE);
				}
				//updateStockAlerts(); //TODO: reactivate me!
			}
		});
		add(btnEditArticle, gbc_btnEditArticle);
		
		JButton btnDeleteArticle = new JButton("Delete", minus);
		GridBagConstraints gbc_btnDeleteArticle = new GridBagConstraints();
		gbc_btnDeleteArticle.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeleteArticle.gridx = 4;
		gbc_btnDeleteArticle.gridy = 0;
		btnDeleteArticle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = articleTable.getSelectedRow();
				int realRow = -1;
				if(row >= 0){
					realRow = articleTable.convertRowIndexToModel(articleTable.getSelectedRow());
					currentArticle = (Article)catalogue.getArray()[realRow];

					if(currentArticle != null){
						int choice = JOptionPane.showConfirmDialog(null,"Are you sure to delete this article?","",JOptionPane.YES_NO_OPTION);
						if(choice == JOptionPane.YES_OPTION){
							catalogue.remove(currentArticle);
							refreshTable();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "No article selected!", "Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		add(btnDeleteArticle, gbc_btnDeleteArticle);
		
		articleTable = new JTable();
		atModel = new ArticleTableModel(catalogue.getArrayForTables());
		articleTable.setModel(atModel);
		articleSorter = new TableRowSorter<ArticleTableModel>(atModel);
		articleTable.setRowSorter(articleSorter);
		articleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		articleTable.setFillsViewportHeight(true);
		//articleTable.setAutoCreateRowSorter(true);
		
		JScrollPane t1SP = new JScrollPane(articleTable);
		
		GridBagConstraints gbc_articleTable = new GridBagConstraints();
		gbc_articleTable.gridwidth = 8;
		gbc_articleTable.fill = GridBagConstraints.BOTH;
		gbc_articleTable.gridx = 0;
		gbc_articleTable.gridy = 1;
		add(t1SP, gbc_articleTable);
	}
	
	/**
	 * Filter for search box
	 */
	private void newArticleFilter() {
        RowFilter<ArticleTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
        	rf = RowFilter.regexFilter("(?i)(?u)" + articleRechercheField.getText());         
        } catch (PatternSyntaxException e) {
        	return;
        } catch (NullPointerException e) {
        	return;
        }
        articleSorter.setRowFilter(rf);
    }
	
	public void refreshTable(){
		atModel.refreshData(catalogue.getArrayForTables() );
		atModel.fireTableDataChanged();
	}
}
