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

package net.sourceforge.dionysus.db;

import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import net.sourceforge.dionysus.Article;

/**
 * A class implementing an article database
 *
 * TODO: exploit ArrayLists at their full potential ;) Even better: use a
 * dictionary indexed with article codes...
 */
public class ArticleDB extends Database<Article> {

	/**
	 * Constructor
	 */
	public ArticleDB() {
		data = new ArrayList<Article>(20);
		numberOfRecords = 0;
	}

	/** {@inheritDoc} */
	@Override
	public Article[] getArray() {
		return data.toArray(new Article[numberOfRecords]);
	}

	/**
	 * Allows finding an article with its integer code
	 *
	 * @param code the code to find
	 * @return first article found with this code, null if no article matches
	 */
	public Article getArticleByCode(long code) {
		for (int i = 0; i < numberOfRecords; i++) {
			if (data.get(i) != null && data.get(i).getCode() == code) {
				return data.get(i);
			}
		}

		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void makeArrayForTables() {
		foodForTable = new Object[numberOfRecords][8];
		for (int i = 0; i < numberOfRecords; i++) {
			foodForTable[i][0] = data.get(i).getName();
			foodForTable[i][1] = data.get(i).getCode();
			foodForTable[i][2] = data.get(i).isActive();
			foodForTable[i][3] = NumberFormat.getCurrencyInstance().format(data.get(i).getArticlePrice());

			double price1, price2;

			if (data.get(i).getNumberOfPrices() >= 1) {
				price1 = data.get(i).getArticlePrice(1);
			} else {
				price1 = 0.00;
			}
			foodForTable[i][4] = NumberFormat.getCurrencyInstance().format(price1);

			if (data.get(i).getNumberOfPrices() >= 2) {
				price2 = data.get(i).getArticlePrice(2);
			} else {
				price2 = 0.00;
			}
			foodForTable[i][5] = NumberFormat.getCurrencyInstance().format(price2);

			if (data.get(i).hasStockMgmtEnabled()) {
				foodForTable[i][6] = String.valueOf((data.get(i).getStock()));
			} else {
				foodForTable[i][6] = "NA";
			}

			if (data.get(i).hasStockAlertEnabled()) {
				foodForTable[i][7] = String.valueOf(data.get(i).getLimitStock());
			} else {
				foodForTable[i][7] = "NA";
			}
			// TODO: additional fields!
		}
	}

	/**
	 * Modifies a given article
	 *
	 * @param a     article to edit
	 * @param index index of article
	 */
	@Override
	public void modify(Article a, int index) {
		if (a != null) { // No reason to pass a null, there is a function for cleaning up...
			if (index >= 0 && index < numberOfRecords) {
				data.set(index, a);

				saveToTextFile();
				makeArrayForTables(); // TODO : optimize!
			}
		}
	}

	/**
	 * Removes an article from the list, and cleans up the array to remove any null
	 *
	 * @param a article to be removed
	 */
	@Override
	public void remove(Article a) {
		if (a != null) {
			if (a.hasBeenUsed()) {
				JOptionPane.showMessageDialog(null,
						"Article already used! \nDeleting it will prevent cancellation of all transactions involving this article.",
						"Info", JOptionPane.INFORMATION_MESSAGE);
			}
			// Merge these two dialogs in one?
			int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this article?",
					"Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (choice != JOptionPane.YES_OPTION) {
				return;
			}

			data.remove(a);
			numberOfRecords--;

		}
		saveToTextFile();
		makeArrayForTables();
	}
}
