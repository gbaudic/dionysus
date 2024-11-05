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

package net.sourceforge.dionysus;

import java.text.NumberFormat;

/**
 * A ticket item, corresponding to a given number of a given article at a given
 * price for a given total amount (forgive me for giving such an explanation^^)
 */
public class TicketItem {

	/** Article associated with this item */
	private Article article;
	/** the price id (not the price itself) */
	private int fee;
	/** in units or in grams/milliliters depending on Article type */
	private int quantity; //
	private double amount; // TODO: fix this to stay in cents here too
	/** flag to indicate amount should not be recomputed */
	private boolean forcedAmount;

	/**
	 * Default constructor
	 *
	 * Uses fee #0 and quantity of 1 since this is considered the most frequent case
	 *
	 * @param article article being bought
	 */
	public TicketItem(Article article) {
		this(article, 0, 1);
	}

	/**
	 * Constructor
	 *
	 * @param article  article being bought
	 * @param fee      index of fee to use, starting at 0
	 * @param quantity quantity of the product, in units or grams/milliliters
	 */
	public TicketItem(Article article, int fee, int quantity) {
		this.article = article;
		this.fee = fee;
		this.quantity = quantity;
		forcedAmount = false;
	}

	/**
	 * Increase the number of articles
	 *
	 * @param number a positive number to add
	 */
	public void addArticles(int number) {
		if (number >= 0) {
			quantity += number;
			computeAmount();
		} else {
			throw new IllegalArgumentException("Adding a negative quantity is not allowed.");
		}
	}

	/**
	 * Compute the amount corresponding to this item under usual circumstances (no
	 * rebates, special offers...)
	 */
	public void computeAmount() {
		if (article != null) {
			if (!forcedAmount) {
				if (article.isCountable()) {
					amount = article.getArticlePrice(fee) * quantity;
				} else {
					amount = (article.getArticlePrice(fee) / 1000.0) * quantity;
				}
			}
		} else {
			amount = 0;
		}
	}

	/**
	 * Getter for the total price of this item
	 *
	 * @return price of this item in usual format (e.g., 12.99 and not 1299 cents)
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Getter for the article specified by this Item
	 *
	 * @return the article
	 */
	public Article getArticle() {
		return article;
	}

	/**
	 * Getter for the fee index
	 *
	 * @return the fee index
	 */
	public int getFee() {
		return fee;
	}

	/**
	 * Getter for the quantity
	 *
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Decrease the number of articles
	 *
	 * @param number a positive number to remove
	 */
	public void removeArticles(int number) {
		if (number >= 0 && number <= quantity) {
			quantity -= number;
			computeAmount();
		} else {
			throw new IllegalArgumentException("Illegal value when removing articles.");
		}
	}

	/**
	 * Forces a given amount Useful when selling 'meters' (12 units at the price of
	 * 10, especially with beer)
	 *
	 * @param nam amount to force
	 */
	public void setAmount(double nam) {
		amount = nam;
		forcedAmount = true;
	}

	/**
	 * Change the fee used
	 *
	 * @param newFee new fee ID to use, illegal values default to standard fee (ID
	 *               0)
	 */
	public void setFee(int newFee) {
		if (newFee < 0 || newFee >= article.getNumberOfPrices()) {
			fee = 0;
		} else {
			fee = newFee;
		}
		computeAmount();
	}

	/**
	 * Setter for quantity
	 *
	 * @param q the new quantity
	 */
	public void setQuantity(int q) {
		if (q > 0) {
			quantity = q;
			computeAmount();
		}
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		String name = article != null ? article.getName() : "null";

		return String.format(" %d x %s  %s", quantity, name, NumberFormat.getCurrencyInstance().format(getAmount()));
	}
}
