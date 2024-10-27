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

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.StringJoiner;

/**
 * Class to represent a specific article in the shop (for example "1 bottle of
 * Budweiser beer" or "Florida oranges")
 */
public class Article implements Serializable, CSVAble {

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = 4726480832864870735L;
	public static final int QUANTITY_DECIMALS = 3;
	/** Article name */
	private final String name;
	/** long code */
	private final long code;
	/**
	 * articles can have different prices (max 3): members of student union,
	 * non-members, visitors (for example)
	 */
	private final Price prices[];

	/** current quantity: units or (grams or milliliters) */
	private int stock;

	/** lower bound for alerts */
	private int limitStock;
	/** inventory management */
	private boolean hasStockMgmtEnabled;
	/** alert when low inventory */
	private boolean hasStockAlertEnabled;

	/**
	 * Is currently being used (i.e., is available in the pub at this very moment)
	 */
	private boolean isActive;

	/**
	 * Has already been used at least once and cannot be deleted permanently
	 * (otherwise corresponding transactions cannot be reverted without errors)
	 */
	private boolean hasBeenUsed;

	/**
	 * Allows to distinguish between countable (two bottles) and uncountable (531g
	 * of sugar). Useless in the case of a student pub, but useful for other shops
	 */
	private boolean isCountable;

	/**
	 * Constructor
	 *
	 * @param name   article name
	 * @param prices an array of prices
	 * @param stock  initial number of units
	 * @param code   the code to be used for recalling the article
	 */
	public Article(final String name, final Price[] prices, final int stock, final long code) {
		this(name, prices, code);
		this.stock = stock;
	}

	/**
	 * Constructor
	 *
	 * @param name        article name
	 * @param prices      an array of prices
	 * @param stock       initial number of units
	 * @param code        the code to be used for recalling the article
	 * @param isCountable flag to tell this article will be uncountable
	 */
	public Article(final String name, final Price[] prices, final int stock, final long code,
			final boolean isCountable) {
		this(name, prices, stock, code);
		this.isCountable = isCountable;
	}

	/**
	 * Simple constructor when no stock management is used
	 *
	 * @param name   article name
	 * @param prices an array of prices
	 * @param code   the code to be used for recalling this article
	 */
	public Article(final String name, final Price[] prices, final long code) {
		this.name = name;
		this.code = code;
		this.prices = prices;
		isCountable = true;
	}

	/**
	 * Add stock to this article, use a negative quantity to remove
	 *
	 * @param amount the INTEGER amount to add or remove
	 */
	public void addStock(final int amount) {
		stock += amount;
	}

	/** {@inheritDoc} */
	@Override
	public String csvHeader() {
		return "# Name;Code;Price0;Price1;Price2;Stock;Limit;Management;Alert;Active;Used;Countable";
	}

	public double getArticlePrice() {
		return prices[0].getPrice();
	}

	/**
	 *
	 * @param id price identifier. Must be 0, 1 or 2 in this implementation
	 * @return the corresponding price, or 0 if the price does not exist
	 */
	public double getArticlePrice(final int id) {
		if (id >= 0 && id < getNumberOfPrices()) {
			return prices[id].getPrice();
		} else {
			throw new IndexOutOfBoundsException("Price does not exist");
		}
	}

	/**
	 * @return the code
	 */
	public long getCode() {
		return code;
	}

	public int getLimitStock() {
		return limitStock;
	}

	public String getName() {
		return name;
	}

	public int getNumberOfPrices() {
		return prices.length;
	}

	public int getStock() {
		return stock;
	}

	/**
	 * Produces the tooltip text for the Cash desk view
	 *
	 * Use of HTML is a trick to achieve multiline tooltips
	 *
	 * @return the correct tooltip text
	 */
	public String getToolTipText() {
		final StringJoiner ttt = new StringJoiner("", "<html>", "</html>");
		ttt.add(name + " (" + String.valueOf(code) + ")");
		for (int i = 0; i < getNumberOfPrices(); i++) {
			ttt.add("Price " + String.valueOf(i) + ": "
					+ NumberFormat.getCurrencyInstance().format(getArticlePrice(i)));
		}
		return ttt.toString();
	}

	public boolean hasBeenUsed() {
		return hasBeenUsed;
	}

	public boolean hasStockAlertEnabled() {
		return hasStockAlertEnabled;
	}

	public boolean hasStockMgmtEnabled() {
		return hasStockMgmtEnabled;
	}

	public boolean isActive() {
		return isActive;
	}

	/**
	 * @return true if article is countable (1, 2...), false otherwise (0.731)
	 */
	public boolean isCountable() {
		return isCountable;
	}

	public void setActive(final boolean isActive) {
		this.isActive = isActive;
	}

	public void setLimitStock(final int limitStock) {
		if (limitStock >= 0) {
			this.limitStock = limitStock;
		}
	}

	public void setStock(final int newStock) {
		stock = newStock;
	}

	public void setStockAlertEnabled(final boolean hasStockAlertEnabled) {
		this.hasStockAlertEnabled = hasStockAlertEnabled;
	}

	public void setStockMgmt(final boolean hasStockMgmtEnabled) {
		this.hasStockMgmtEnabled = hasStockMgmtEnabled;
	}

	/**
	 * Generate the string for CSV serialization TODO
	 */
	@Override
	public String toCSV() {
		final StringJoiner csvline = new StringJoiner(";");
		csvline.add(name);
		csvline.add(String.valueOf(code));
		csvline.add(String.valueOf(getArticlePrice()));

		final double factor = isCountable ? 1. : 1000.; // Hide the underlying storage as integers

		if (getNumberOfPrices() > 1) {
			csvline.add(String.valueOf(getArticlePrice(1)));
		}
		if (getNumberOfPrices() > 2) {
			csvline.add(String.valueOf(getArticlePrice(2)));
		}
		if (hasStockMgmtEnabled) {
			csvline.add(String.valueOf(stock / factor));
		}
		if (hasStockAlertEnabled) {
			csvline.add(String.valueOf(limitStock / factor));
		}
		csvline.add(String.valueOf(hasStockMgmtEnabled));
		csvline.add(String.valueOf(hasStockAlertEnabled));
		csvline.add(String.valueOf(isActive));
		csvline.add(String.valueOf(hasBeenUsed));
		csvline.add(String.valueOf(isCountable));

		return csvline.toString();
	}

	/**
	 * Declare this article as having been used (read: sold) at least once
	 */
	public void use() {
		if (!hasBeenUsed) {
			this.hasBeenUsed = true;
		}
	}

}
