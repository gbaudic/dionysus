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
 * A ticket item, corresponding to a given number of a given article at a given price for a given total amount
 * (forgive me for giving such an explanation^^)
 */
public class TicketItem {

	private Article article;
	private int fee; // the price id (not the price itself)
	private int quantity; // in units or in grams/milliliters depending on Article type
	private double amount; //TODO: fix this to stay in cents here too
	private boolean forcedAmount; //flag to indicate amount should not be recomputed
	
	/**
	 * Constructor
	 * @param article article being bought
	 */
	public TicketItem(Article article) {
		this(article, 0, 1);
	}
	
	/**
	 * Constructor
	 * @param article article being bought
	 * @param fee index of fee to use, starting at 0
	 * @param quantity quantity of the product, in units or grams/milliliters
	 */
	public TicketItem(Article article, int fee, int quantity) {
		this.article = article;
		this.fee = fee;
		this.quantity = quantity;
		forcedAmount = false;
	}

	public Article getArticle() {
		return article;
	}
	
	public void addArticles(int number) {
		quantity += number;
		computeAmount();
	}
	
	public void removeArticles(int number) {
		quantity -= number;
		computeAmount();
	}
	
	public int getFee() {
		return fee;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * Compute the amount corresponding to this item under usual circumstances
	 * (no rebates, special offers...)
	 */
	public void computeAmount() {
		if(article != null){
			if(!forcedAmount){
				if(article.isCountable()) {
					amount = article.getArticlePrice(fee)*quantity;
				} else {
					amount = (article.getArticlePrice(fee)/1000.0)*quantity;
				}
			}
		} else {
			amount = 0;
		}
	}
	
	/**
	 * 
	 * @return price of this item in usual format (e.g., 12.99 and not 1299 cents)
	 */
	public double getAmount() {
		return amount;
	}
	
	/**
	 * Forces a given amount
	 * Useful when selling 'meters' (12 units at the price of 10, especially with beer)
	 * @param nam amount to force
	 */
	public void setAmount(double nam){
		amount = nam;
		forcedAmount = true;
	}
	
	@Override
	public String toString() {
	    String name = article != null ? article.getName() : "null";
	    
	    return String.format(" {} x {}  {}", quantity, name, NumberFormat.getCurrencyInstance().format(getAmount()));
	}
	
	public void setQuantity(int q) {
	    if(q > 0) {
	        quantity = q;
		    computeAmount();
	    }
	}
	
	/**
	 * Change the fee used
	 * @param newFee new fee ID to use, illegal values default to standard fee (ID 0)
	 */
	public void setFee(int newFee) {
		if(newFee < 0 || newFee >= article.getNumberOfPrices()){
			fee = 0;
		} else {
			fee = newFee;
		}
		computeAmount();
	}
}
