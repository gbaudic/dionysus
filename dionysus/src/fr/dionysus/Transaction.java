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

package fr.dionysus;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.*;

public class Transaction implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Date date;
	private Price amount;
	private User sourceUser;
	private User destUser;
	private Article article;
	private int numberOfItems;
	private PaymentMethod pMethod;
	
	public Transaction(int amount, User sourceUser, User destUser,
			Article article, int numberOfItems, PaymentMethod pMethod) {
		super();
		this.amount = new Price(amount);
		this.sourceUser = sourceUser;
		this.destUser = destUser;
		this.article = article;
		this.numberOfItems = numberOfItems;
		this.date = new Date();
		this.pMethod = pMethod;
	}
	
	public Transaction(Transaction cancelledTransaction)
	{
		this.amount = new Price(cancelledTransaction.getAmount().getPrice());
		this.sourceUser = cancelledTransaction.getDestUser();
		this.destUser = cancelledTransaction.getSourceUser();
		this.article = cancelledTransaction.getArticle();
		this.numberOfItems = cancelledTransaction.getNumberOfItems();
		this.date = new Date();
		this.pMethod = cancelledTransaction.getPaymentMethod();
	}
	
	public String print()
	{
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

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public PaymentMethod getPaymentMethod() {
		return pMethod;
	}

	public Date getDate() {
		return date;
	}
	
	public void revert() {
		article.addStock(numberOfItems);
		if(sourceUser != null)
			sourceUser.credite(amount.getPrice());
		
		if(destUser != null)
			destUser.debite(amount.getPrice());
	}
}
