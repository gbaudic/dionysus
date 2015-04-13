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

package net.sourceforge.dionysus;

/**
 * A ticket item, corresponding to a given number of a given article at a given price for a given total amount
 * (forgive me for giving such an explanation^^)
 */
public class TicketItem {

	private Article article;
	private int fee; // the price id (not the price itself)
	private int quantity;
	private double amount;
	
	public TicketItem(Article article, int fee, int quantity) {
		this.article = article;
		this.fee = fee;
		this.quantity = quantity;
	}

	public Article getArticle() {
		return article;
	}
	
	public void addArticles(int number)
	{
		quantity += number;
	}
	
	public void removeArticles(int number)
	{
		quantity -= number;
	}
	
	public int getFee()
	{
		return fee;
	}
	
	public int getQuantity()
	{
		return quantity;
	}
	
	/**
	 * Compute the amount corresponding to this item under usual circumstances
	 * (no rebates, special offers...)
	 */
	public void computeAmount(){
		amount = (article.getArticlePrice(fee))*quantity;
	}
	
	/**
	 * 
	 * @return price of this item in EUROS
	 */
	public double getAmount(){
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
	
	public String toString()
	{
		return " "+String.valueOf(quantity)+" x "+article.getName()+"  "+String.valueOf(getAmount())+" €";
	}
	
	public void setQuantity(int q){
		quantity = q;
	}
	
	public void setFee(int t){
		if(t < 0 || t >= article.getNumberOfPrices()){
			fee = 0;
		} else {
			fee = t;
		}
	}
}
