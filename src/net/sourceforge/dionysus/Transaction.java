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
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * The base class for transactions, which is the process of buying a single article
 * Consequently, there will be several transactions for one ticket, even if there is only
 * one payment operation.
 * Transactions are immutable. 
 */
public class Transaction implements Serializable,CSVAble {

	private static final long serialVersionUID = 1L;
	
	private Date date;
	private Price amount;
	private User sourceUser;
	private User destUser;
	private Article article;
	private int numberOfItems;
	private PaymentMethod pMethod;
	private Vendor vendor;
	
	public Transaction(int amount, User sourceUser, User destUser,
			Article article, int numberOfItems, PaymentMethod pMethod) {
		this.amount = new Price(amount);
		this.sourceUser = sourceUser;
		this.destUser = destUser;
		this.article = article;
		this.numberOfItems = numberOfItems;
		this.date = new Date();
		this.pMethod = pMethod;
	}
	
	public Transaction(int amount, User sourceUser, User destUser,
			Article article, int numberOfItems, PaymentMethod pMethod, Vendor v) {
		this(amount,sourceUser,destUser,article,numberOfItems,pMethod);
		this.vendor = v;
	}
	
	public Transaction(Transaction cancelledTransaction) {
		this.amount = new Price(cancelledTransaction.getAmount().getPrice());
		this.sourceUser = cancelledTransaction.getDestUser();
		this.destUser = cancelledTransaction.getSourceUser();
		this.article = cancelledTransaction.getArticle();
		this.numberOfItems = cancelledTransaction.getNumberOfItems();
		this.date = new Date();
		this.pMethod = cancelledTransaction.getPaymentMethod();
	}
	
	public Transaction(Transaction cancelledTransaction, Vendor v){
		this(cancelledTransaction);
		this.vendor = v;
	}
	
	/**
	 * 
	 * @return a nice String representing this Transaction
	 */
	public String print() {
		return "On "+DateFormat.getDateInstance().format(date)+", "+String.valueOf(numberOfItems)+" "+article.getName()+" ("+amount.toString()+")";
	}

	public Price getAmount() {
		return amount;
	}

	public User getSourceUser() {
		return sourceUser;
	}

	public User getDestUser() {
		return destUser;
	}

	public Article getArticle() {
		return article;
	}
	
	public Vendor getVendor() {
		return vendor;
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public PaymentMethod getPaymentMethod() {
		return pMethod;
	}

	public Date getDate() {
		return date;
	}
	
	/**
	 * Reverts all changes that were made by this transaction
	 */
	public void revert() {
		article.addStock(numberOfItems);
		if(sourceUser != null)
			sourceUser.credite(amount.getPrice());
		
		if(destUser != null)
			destUser.debite(amount.getPrice());
	}

	@Override
	public String toCSV() {
		StringBuilder sb = new StringBuilder();
		sb.append(DateFormat.getDateTimeInstance().format(date) + ";");
		sb.append(NumberFormat.getCurrencyInstance().format(amount.getPrice()) + ";");
		if(sourceUser != null)
			sb.append(sourceUser.getName());
		sb.append(';');
		if(destUser != null)
			sb.append(destUser.getName());
		sb.append(';');
		sb.append(article.getName() + ";");
		double factor = article.isCountable() ? 1 : 1000;
		sb.append(String.valueOf(numberOfItems/factor) + ";");
		String paymentName = pMethod == null ? "account" : pMethod.getName();
		sb.append(paymentName + ";");
		sb.append(vendor.getName());
		return sb.toString();
	}
	
	public String csvHeader() {
	    return "# Date;Amount;Source user;Destination user;Article;Number;Payment;Vendor";
	}
}
