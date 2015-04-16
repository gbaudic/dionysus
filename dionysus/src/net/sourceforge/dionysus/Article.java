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

package net.sourceforge.dionysus;

import java.io.Serializable;

/**
 * Class to represent a specific article in the shop (for example "1 bottle of Budweiser beer" or "Florida oranges")
 *
 */
public class Article implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private long code; //long code
	private Price prices []; //articles can have different prices (max 3): members of student union, non-members, visitors (for example)
	private int stock; //current quantity
	private int limitStock; //lower bound for alerts
	private boolean hasStockMgmtEnabled; //inventory management
	private boolean hasStockAlertEnabled; //alert when low inventory
	private boolean isActive; //Is currently being used (ie., is available in the pub at this very moment)
	private boolean hasBeenUsed; /* Has already been used at least once and cannot be deleted permanently, 
	(otherwise corresponding transactions cannot be reverted without errors) */
	private boolean isCountable; /*Allows to distinguish between countable (two bottles) and uncountable (531g of sugar).
	Useless in the case of a student pub, but useful for other shops*/
	
	/**
	 * Simple constructor when no stock management is used
	 * @param name article name
	 * @param prices an array of prices
	 * @param code the code to be used for recalling this article
	 */
	public Article(String name, Price[] prices, long code) {
		super();
		this.name = name;
		this.code = code;
		this.prices = prices;
		isCountable = true;
	}
	
	/**
	 * 
	 * @param name article name
	 * @param prices an array of prices
	 * @param stock initial number of units
	 * @param code the code to be used for recalling the article
	 */
	public Article(String name, Price[] prices, int stock, long code) {
		this(name, prices, code);
		this.stock = stock;
	}

	public int getNumberOfPrices()
	{
		return prices.length;
	}

	public String getName() {
		return name;
	}

	public long getCode() {
		return code;
	}
	
	public double getArticlePrice()
	{
		return prices[0].getPrice();
	}
	
	/**
	 * 
	 * @param id price identifier. Must be 0, 1 or 2 in this implementation
	 * @return the corresponding price, or 0 if the price does not exist
	 */
	public double getArticlePrice(int id)
	{
		if(id >= 0 && id < getNumberOfPrices())
			{
				return prices[id].getPrice();
			} else {
				return 0;
			}
	}
	
	public int getStock()
	{
		return stock;
	}
	
	public void setStock(int newStock)
	{
		stock = newStock;
	}
	
	public void addStock(int amount)
	{
		stock += amount;
	}

	public int getLimitStock() {
		return limitStock;
	}

	public boolean hasStockMgmtEnabled() {
		return hasStockMgmtEnabled;
	}

	public boolean hasStockAlertEnabled() {
		return hasStockAlertEnabled;
	}

	public boolean isActive() {
		return isActive;
	}

	public boolean hasBeenUsed() {
		return hasBeenUsed;
	}

	public void setLimitStock(int limitStock) {
		this.limitStock = limitStock;
	}

	public void setStockMgmt(boolean hasStockMgmtEnabled) {
		this.hasStockMgmtEnabled = hasStockMgmtEnabled;
	}

	public void setStockAlertEnabled(boolean hasStockAlertEnabled) {
		this.hasStockAlertEnabled = hasStockAlertEnabled;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	/**
	 * Declare this article as having been used (read: sold) at least once
	 */
	public void use() {
		if(!hasBeenUsed)
			this.hasBeenUsed = true;
	}

}
