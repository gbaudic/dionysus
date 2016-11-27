/* 	Dionysus, a student pub management software in Java
    Copyright (C) 2011,2013,2015,2016  G. Baudic

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

/**
 * A ticket item, corresponding to a given number of a given article at a given price for a given total amount
 * (forgive me for giving such an explanation^^)
 */
public class TicketItem {

	private Article article;
	private int fee; // the price id (not the price itself)
	private int quantity;
	private double amount; //TODO: fix this to stay in cents here too
	
	public TicketItem(Article article) {
		this.article = article;
		this.fee = 0;
		this.quantity = 1;
	}
	
	public TicketItem(Article article, int fee, int quantity) {
		this.article = article;
		this.fee = fee;
		this.quantity = quantity;
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
		amount = (article.getArticlePrice(fee))*quantity;
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
	}
	
	public String toString() {
		return " "+String.valueOf(quantity)+" x "+article.getName()+"  "+String.valueOf(getAmount());
	}
	
	public void setQuantity(int q) {
		quantity = q;
		computeAmount();
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
