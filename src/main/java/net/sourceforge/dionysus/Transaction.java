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
import java.util.Date;
import java.util.StringJoiner;

/**
 * The base class for transactions, which is the process of buying a single
 * article Consequently, there will be several transactions for one ticket, even
 * if there is only one payment operation. Transactions are immutable.
 */
public class Transaction implements Serializable, CSVAble {

	/**
	 * The version UID
	 */
	private static final long serialVersionUID = -2690513207608604236L;
	private Date date;
	private Price amount;
	private User sourceUser;
	private User destUser;
	private Article article;
	private int numberOfItems;
	private PaymentMethod pMethod;
	private Vendor vendor;

	/**
	 * Constructor
	 *
	 * @param amount
	 * @param sourceUser
	 * @param destUser
	 * @param article
	 * @param numberOfItems
	 * @param pMethod
	 */
	public Transaction(int amount, User sourceUser, User destUser, Article article, int numberOfItems,
			PaymentMethod pMethod) {
		this.amount = new Price(amount);
		this.sourceUser = sourceUser;
		this.destUser = destUser;
		this.article = article;
		this.numberOfItems = numberOfItems;
		this.date = new Date();
		this.pMethod = pMethod;
	}

	public Transaction(int amount, User sourceUser, User destUser, Article article, int numberOfItems,
			PaymentMethod pMethod, Vendor v) {
		this(amount, sourceUser, destUser, article, numberOfItems, pMethod);
		this.vendor = v;
	}

	/**
	 * Constructor for cancellation
	 *
	 * @param cancelledTransaction
	 *            transaction to revert
	 */
	public Transaction(Transaction cancelledTransaction) {
		this((int) (cancelledTransaction.getAmount().getPrice() * 100), cancelledTransaction.getDestUser(),
				cancelledTransaction.getSourceUser(), cancelledTransaction.getArticle(),
				cancelledTransaction.getNumberOfItems(), cancelledTransaction.getPaymentMethod());
	}

	/**
	 * Constructor for cancellation
	 *
	 * @param cancelledTransaction
	 *            transaction to revert
	 * @param v
	 *            vendor
	 */
	public Transaction(Transaction cancelledTransaction, Vendor v) {
		this(cancelledTransaction);
		this.vendor = v;
	}

	/** {@inheritDoc} */
	@Override
	public String csvHeader() {
		return "# Date;Amount;Source user;Destination user;Article;Number;Payment;Vendor";
	}

	public Price getAmount() {
		return amount;
	}

	public Article getArticle() {
		return article;
	}

	public Date getDate() {
		return date;
	}

	public User getDestUser() {
		return destUser;
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public PaymentMethod getPaymentMethod() {
		return pMethod;
	}

	public User getSourceUser() {
		return sourceUser;
	}

	public Vendor getVendor() {
		return vendor;
	}

	/**
	 *
	 * @return a nice String representing this Transaction
	 */
	public String print() {
		return String.format("On %s, %d %s (%s)", DateFormat.getDateInstance().format(date), numberOfItems,
				article.getName(), amount.toString());
	}

	/**
	 * Reverts all changes that were made by this transaction
	 */
	public void revert() {
		article.addStock(numberOfItems);
		if (sourceUser != null) {
			sourceUser.credite(amount.getPrice());
		}

		if (destUser != null) {
			destUser.debite(amount.getPrice());
		}
	}

	/** {@inheritDoc} */
	@Override
	public String toCSV() {
		StringJoiner sb = new StringJoiner(";");
		sb.add(DateFormat.getDateTimeInstance().format(date));
		sb.add(NumberFormat.getCurrencyInstance().format(amount.getPrice()));
		if (sourceUser != null) {
			sb.add(sourceUser.getName());
		} else {
			sb.add("");
		}
		if (destUser != null) {
			sb.add(destUser.getName());
		} else {
			sb.add("");
		}
		sb.add(article.getName());
		double factor = article.isCountable() ? 1 : 1000;
		sb.add(String.valueOf(numberOfItems / factor));
		String paymentName = pMethod == null ? "account" : pMethod.getName();
		sb.add(paymentName);
		sb.add(vendor.getName());
		return sb.toString();
	}
}
