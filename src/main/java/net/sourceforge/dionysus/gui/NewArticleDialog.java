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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.sourceforge.dionysus.Article;
import net.sourceforge.dionysus.Price;

/**
 * Dialog box to add or edit articles
 *
 */
public class NewArticleDialog extends JDialog {

	private static final long serialVersionUID = 3336738625857841805L;
	private final JPanel contentPanel;
	private final JTextField nameField;
	private final JTextField codeField;
	private final JTextField stockField;
	private final JTextField alertField;
	private final JTextField price0Field;
	private final JTextField price1Field;
	private final JTextField price2Field;
	private final JCheckBox chckbxActive;
	private final JCheckBox chckbxStockEnabled;
	private final JCheckBox chckbxAlertEnabled;
	private final JCheckBox chckbxCountable;

	private Article article;

	/**
	 * Create the dialog. Default constructor without argument for new articles
	 */
	public NewArticleDialog() {
		contentPanel = new JPanel();
		setTitle(Messages.getString("NewArticleDialog.0")); //$NON-NLS-1$
		setModal(true);
		setSize(369, 290);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		final GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 142, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);

		final JLabel lblNewLabel = new JLabel(Messages.getString("NewArticleDialog.1")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.LINE_END;
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		contentPanel.add(lblNewLabel, gbc_lblNewLabel);

		nameField = new JTextField();
		final GridBagConstraints gbc_nomField = new GridBagConstraints();
		gbc_nomField.insets = new Insets(0, 0, 5, 0);
		gbc_nomField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nomField.gridx = 2;
		gbc_nomField.gridy = 0;
		contentPanel.add(nameField, gbc_nomField);
		nameField.setColumns(10);

		final JLabel lblNewLabel_1 = new JLabel(Messages.getString("NewArticleDialog.2")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_1.anchor = GridBagConstraints.LINE_END;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 1;
		contentPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);

		codeField = new JTextField();
		codeField.setToolTipText(Messages.getString("NewArticleDialog.3")); //$NON-NLS-1$
		final GridBagConstraints gbc_codeField = new GridBagConstraints();
		gbc_codeField.insets = new Insets(0, 0, 5, 0);
		gbc_codeField.fill = GridBagConstraints.HORIZONTAL;
		gbc_codeField.gridx = 2;
		gbc_codeField.gridy = 1;
		contentPanel.add(codeField, gbc_codeField);
		codeField.setColumns(13); // Barcodes have a length of 13

		chckbxStockEnabled = new JCheckBox(Messages.getString("NewArticleDialog.4")); //$NON-NLS-1$
		final GridBagConstraints gbc_chckbxStockEnabled = new GridBagConstraints();
		gbc_chckbxStockEnabled.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxStockEnabled.gridx = 0;
		gbc_chckbxStockEnabled.gridy = 2;
		contentPanel.add(chckbxStockEnabled, gbc_chckbxStockEnabled);

		final JLabel lblStock = new JLabel(Messages.getString("NewArticleDialog.5")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblStock = new GridBagConstraints();
		gbc_lblStock.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblStock.anchor = GridBagConstraints.EAST;
		gbc_lblStock.insets = new Insets(0, 0, 5, 5);
		gbc_lblStock.gridx = 1;
		gbc_lblStock.gridy = 2;
		contentPanel.add(lblStock, gbc_lblStock);

		stockField = new JTextField();
		stockField.setToolTipText(Messages.getString("NewArticleDialog.6")); //$NON-NLS-1$
		final GridBagConstraints gbc_stockField = new GridBagConstraints();
		gbc_stockField.insets = new Insets(0, 0, 5, 0);
		gbc_stockField.fill = GridBagConstraints.HORIZONTAL;
		gbc_stockField.gridx = 2;
		gbc_stockField.gridy = 2;
		contentPanel.add(stockField, gbc_stockField);
		stockField.setColumns(10);

		chckbxAlertEnabled = new JCheckBox(Messages.getString("NewArticleDialog.7")); //$NON-NLS-1$
		final GridBagConstraints gbc_chckbxAlertEnabled = new GridBagConstraints();
		gbc_chckbxAlertEnabled.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxAlertEnabled.gridx = 0;
		gbc_chckbxAlertEnabled.gridy = 3;
		contentPanel.add(chckbxAlertEnabled, gbc_chckbxAlertEnabled);

		final JLabel lblNewLabel_2 = new JLabel(Messages.getString("NewArticleDialog.8")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_2.anchor = GridBagConstraints.LINE_END;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 1;
		gbc_lblNewLabel_2.gridy = 3;
		contentPanel.add(lblNewLabel_2, gbc_lblNewLabel_2);

		alertField = new JTextField();
		final GridBagConstraints gbc_alertField = new GridBagConstraints();
		gbc_alertField.insets = new Insets(0, 0, 5, 0);
		gbc_alertField.fill = GridBagConstraints.HORIZONTAL;
		gbc_alertField.gridx = 2;
		gbc_alertField.gridy = 3;
		contentPanel.add(alertField, gbc_alertField);
		alertField.setColumns(10);

		final JLabel lblNewLabel_3 = new JLabel(Messages.getString("NewArticleDialog.9")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_3.anchor = GridBagConstraints.LINE_END;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 4;
		contentPanel.add(lblNewLabel_3, gbc_lblNewLabel_3);

		price0Field = new JTextField();
		final GridBagConstraints gbc_price0Field = new GridBagConstraints();
		gbc_price0Field.insets = new Insets(0, 0, 5, 0);
		gbc_price0Field.fill = GridBagConstraints.HORIZONTAL;
		gbc_price0Field.gridx = 2;
		gbc_price0Field.gridy = 4;
		contentPanel.add(price0Field, gbc_price0Field);
		price0Field.setColumns(10);

		final JLabel lblNewLabel_4 = new JLabel(Messages.getString("NewArticleDialog.10")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 1;
		gbc_lblNewLabel_4.gridy = 5;
		contentPanel.add(lblNewLabel_4, gbc_lblNewLabel_4);

		price1Field = new JTextField();
		final GridBagConstraints gbc_price1Field = new GridBagConstraints();
		gbc_price1Field.insets = new Insets(0, 0, 5, 0);
		gbc_price1Field.fill = GridBagConstraints.HORIZONTAL;
		gbc_price1Field.gridx = 2;
		gbc_price1Field.gridy = 5;
		contentPanel.add(price1Field, gbc_price1Field);
		price1Field.setColumns(10);

		final JLabel lblNewLabel_5 = new JLabel(Messages.getString("NewArticleDialog.11")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_5.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 1;
		gbc_lblNewLabel_5.gridy = 6;
		contentPanel.add(lblNewLabel_5, gbc_lblNewLabel_5);

		price2Field = new JTextField();
		final GridBagConstraints gbc_price2Field = new GridBagConstraints();
		gbc_price2Field.insets = new Insets(0, 0, 5, 0);
		gbc_price2Field.fill = GridBagConstraints.HORIZONTAL;
		gbc_price2Field.gridx = 2;
		gbc_price2Field.gridy = 6;
		contentPanel.add(price2Field, gbc_price2Field);
		price2Field.setColumns(10);

		chckbxActive = new JCheckBox(Messages.getString("NewArticleDialog.12")); //$NON-NLS-1$
		chckbxActive.setToolTipText(Messages.getString("NewArticleDialog.13")); //$NON-NLS-1$
		final GridBagConstraints gbc_chckbxActive = new GridBagConstraints();
		gbc_chckbxActive.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxActive.anchor = GridBagConstraints.WEST;
		gbc_chckbxActive.gridx = 1;
		gbc_chckbxActive.gridy = 7;
		contentPanel.add(chckbxActive, gbc_chckbxActive);

		chckbxCountable = new JCheckBox(Messages.getString("NewArticleDialog.14")); //$NON-NLS-1$
		chckbxCountable.setToolTipText(Messages.getString("NewArticleDialog.15")); //$NON-NLS-1$
		chckbxCountable.setSelected(true);
		// chckbxCountable.setEnabled(false); //for 0.3 release
		final GridBagConstraints gbc_chckbxCountable = new GridBagConstraints();
		gbc_chckbxCountable.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxCountable.anchor = GridBagConstraints.WEST;
		gbc_chckbxCountable.gridx = 1;
		gbc_chckbxCountable.gridy = 8;
		contentPanel.add(chckbxCountable, gbc_chckbxCountable);

		final JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		final JButton okButton = new JButton(Messages.getString("NewArticleDialog.17"), Constants.ok); //$NON-NLS-1$
		okButton.setActionCommand(Messages.getString("NewArticleDialog.18")); //$NON-NLS-1$

		okButton.addActionListener(this::okClicked);

		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		final JButton cancelButton = new JButton(Messages.getString("NewArticleDialog.20"), Constants.cancel); //$NON-NLS-1$
		cancelButton.setActionCommand(Messages.getString("NewArticleDialog.21")); //$NON-NLS-1$

		cancelButton.addActionListener(arg0 -> {
			article = null;
			setVisible(false);
		});

		buttonPane.add(cancelButton);

	}

	/**
	 * Constructor with argument for further editing
	 *
	 * @param a article to use
	 */
	public NewArticleDialog(Article a) {
		this();
		setArticle(a);
	}

	/**
	 * Simple check for validity of price
	 *
	 * @param price string to test
	 * @throws IllegalArgumentException if the value is negative
	 * @throws NumberFormatException    if the string is not a valid number
	 */
	private void checkPrice(final String price) {
		final double value = Double.valueOf(price);
		if (value < 0) {
			throw new IllegalArgumentException(Messages.getString("NewArticleDialog.32")); //$NON-NLS-1$
		}
	}

	/**
	 * Getter for article
	 *
	 * @return the Article being edited by this dialog
	 */
	public Article getArticle() {
		return this.article;
	}

	private void okClicked(ActionEvent arg0) {
		try {
			if (nameField.getText().isEmpty()) {
				throw new IllegalArgumentException(Messages.getString("NewArticleDialog.22")); //$NON-NLS-1$
			}
			if (codeField.getText().isEmpty()) {
				throw new IllegalArgumentException(Messages.getString("NewArticleDialog.23")); //$NON-NLS-1$
				// TODO: check that code is numeric only
			} else {
				if (!codeField.getText().matches(Messages.getString("NewArticleDialog.24"))) { //$NON-NLS-1$
					throw new IllegalArgumentException(Messages.getString("NewArticleDialog.25")); //$NON-NLS-1$
				}
			}

			validatePrices();

			final Price p0 = new Price(Double.parseDouble(price0Field.getText()));
			Price p1 = null;
			if (!price1Field.getText().isEmpty()) {
				p1 = new Price(Double.parseDouble(price1Field.getText()));
			}
			Price p2 = null;
			if (!price2Field.getText().isEmpty()) {
				p2 = new Price(Double.parseDouble(price2Field.getText()));
			}
			Price[] prices;
			if (p2 == null) {
				if (p1 == null) {
					prices = new Price[1];
					prices[0] = p0;
				} else {
					prices = new Price[2];
					prices[0] = p0;
					prices[1] = p1;
				}
			} else {
				prices = new Price[3];
				prices[0] = p0;
				prices[1] = p1;
				prices[2] = p2;
			}

			// Checking for code duplicates will be done afterwards

			article = new Article(nameField.getText(), prices, 0, Long.parseLong(codeField.getText()),
					chckbxCountable.isSelected());

			article.setActive(chckbxActive.isSelected());

			final int qtyFactor = article.isCountable() ? 1 : 1000; // Multiplicative factor for uncountable articles

			// Activate stock alerts if requested
			if (chckbxAlertEnabled.isSelected()) {
				article.setStockAlertEnabled(true);
				article.setLimitStock(Integer.parseInt(alertField.getText()) * qtyFactor);
			} else {
				article.setStockAlertEnabled(false);
			}

			// Activate stock management if requested
			if (chckbxStockEnabled.isSelected()) {
				article.setStockMgmt(true);
				article.setStock(Integer.parseInt(stockField.getText()) * qtyFactor);
			} else {
				article.setStockMgmt(false);
			}

			setVisible(false);
		} catch (final NumberFormatException e) {
			JOptionPane.showMessageDialog(null, Messages.getString("NewArticleDialog.26"), //$NON-NLS-1$
					Messages.getString("NewArticleDialog.27"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
		} catch (final IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, Messages.getString("NewArticleDialog.28") + e.getMessage(), //$NON-NLS-1$
					Messages.getString("NewArticleDialog.29"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
		}
	}

	/**
	 * Fills in UI components with data from the DB record
	 *
	 * @param a existing Article to use when filling the dialog
	 */
	public void setArticle(Article a) {
		article = a;

		if (a != null) {
			final double qtyFactor = a.isCountable() ? 1. : 1000.;

			nameField.setText(a.getName());
			codeField.setText(String.valueOf(a.getCode()));
			stockField.setText(String.valueOf(a.getStock() / qtyFactor));
			alertField.setText(String.valueOf(a.getLimitStock() / qtyFactor));

			chckbxActive.setSelected(a.isActive());

			chckbxCountable.setSelected(a.isCountable());

			chckbxStockEnabled.setSelected(a.hasStockMgmtEnabled());

			chckbxAlertEnabled.setSelected(a.hasStockAlertEnabled());

			price0Field.setText(String.valueOf(a.getArticlePrice()));
			final int n = a.getNumberOfPrices();
			if (n > 1) {
				if (n > 2) {
					price2Field.setText(String.valueOf(a.getArticlePrice(2)));
				}

				price1Field.setText(String.valueOf(a.getArticlePrice(1)));
			}
		} else {
			throw new IllegalArgumentException(Messages.getString("NewArticleDialog.30")); //$NON-NLS-1$
		}
	}

	/**
	 * Check that prices have meaningful values and are correctly filled
	 */
	private void validatePrices() {
		final JTextField fields[] = { price0Field, price1Field, price2Field };

		// Check order of filled prices
		int result = 0;
		int flag = 1;
		for (final JTextField field : fields) {
			final String text = field.getText();
			if (text.isEmpty()) {
				result += flag;
			}
			flag *= 2;
			checkPrice(text); // Check that values are positive
		}

		if (result != 1 && result != 3 && result != 7) {
			throw new IllegalArgumentException(Messages.getString("NewArticleDialog.31")); //$NON-NLS-1$
		}
	}

}
