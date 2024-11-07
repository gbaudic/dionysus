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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.dionysus.db.TransactionDB;

/**
 * Class representing a ticket, structure to represent purchase of different
 * products at various prices at a single time
 */
public class Ticket {

	/** Ticket contents */
	private List<TicketItem> items;
	/** customer buying, null if not a registered user */
	private User customer;
	/** Ticket amount */
	private double amount;
	/** Payment method. Only one payment method per ticket */
	private PaymentMethod pMethod;
	/** Shop assistant editing the ticket */
	private Vendor vendor;
	/** Formatter to display localized prices */
	private NumberFormat amountFormatter;

	/**
	 * Constructor
	 *
	 * @param u user making the purchase. Can be null.
	 */
	public Ticket(User u) {
		items = new ArrayList<TicketItem>();
		customer = u;
		amountFormatter = NumberFormat.getCurrencyInstance();
	}

	/**
	 * Add an article to the ticket
	 *
	 * @param newItem item to add
	 */
	public void addArticle(TicketItem newItem) {
		if (newItem != null && newItem.getQuantity() != 0) {
			items.add(newItem);

			updateAmount();
		}
	}

	/**
	 *
	 * @return the ticket grand total
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Generates an array compliant with a display in a JTable component
	 *
	 * @return an Object array
	 */
	public Object[][] getArrayForTables() {
		Object[][] result = new Object[items.size()][3];

		for (int i = 0; i < items.size(); i++) {
			TicketItem ti = items.get(i);
			if (ti != null) {
				result[i][0] = ti.getArticle().getName();
				DecimalFormat df = new DecimalFormat("###,###.###");
				if (ti.getArticle().isCountable()) {
					result[i][1] = df.format(ti.getQuantity());
				} else {
					result[i][1] = df.format(ti.getQuantity() / 1000.0);
				}

				result[i][2] = NumberFormat.getCurrencyInstance().format(ti.getAmount());
			}
		}

		return result;
	}

	/**
	 * Get the new balance of the user once this Ticket is processed
	 *
	 * @return the future balance
	 */
	public double getBalanceAfterTicket() {
		return customer != null ? customer.getBalance() - amount : 0.00;
	}

	/**
	 * Get the contents of the ticket
	 *
	 * @return the contents of the ticket as an unmodifiable list of items
	 */
	public List<TicketItem> getItems() {
		return Collections.unmodifiableList(items);
	}

	/**
	 * Get the number of items in the ticket
	 *
	 * @return the number of items
	 */
	public int getNumberOfItems() {
		return items.size();
	}

	/**
	 * Getter for the payment method
	 *
	 * @return the payment method used
	 */
	public PaymentMethod getPaymentMethod() {
		return pMethod;
	}

	/**
	 * Setter for the payment method
	 *
	 * @param method the payment method used
	 */
	public void pay(PaymentMethod method) {
		pMethod = method;
	}

	/**
	 * Exports the ticket to a string The submit() method needs to have been called
	 * previously to set the vendor TODO: this may need to be rewritten.
	 */
	public StringBuilder printTicketToText(String date) {
		StringBuilder accu = new StringBuilder();

		// Ticket date
		if (date != null) {
			accu.append(date + "%n");
		} else {
			accu.append(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(new Date()) + "%n");
		}

		// Users involved
		accu.append(String.format("From %s%n", customer.getNameWithPromo()));
		accu.append(String.format("Sold by %s%n", vendor.getName()));

		// Purchased items
		for (TicketItem ti : items) {
			if (ti != null) {
				accu.append("\t" + ti.toString() + "%n");
			}
		}

		// Grand total
		accu.append(String.format("Total: %s%nPaid by ", amountFormatter.format(amount)));

		// Payment method
		if (pMethod != null) {
			accu.append(pMethod.getName());
		} else {
			accu.append("user account");
		}

		// Goodbye message
		// TODO make this configurable
		accu.append("%nThanks for your purchase, welcome back");

		return accu.append("%n%n");
	}

	/**
	 * Removes an article at a given price in a given quantity
	 *
	 * @param a the corresponding TicketItem
	 */
	public void removeArticle(TicketItem a) {
		// Check that article exists
		for (TicketItem ti : items) {
			if (ti != null && ti.getArticle().getName().equals(a.getArticle().getName()) && ti.getFee() == a.getFee()) {
				int remainder = ti.getQuantity() - a.getQuantity();
				if (remainder == 0) {
					// If quantity drops to zero, delete article
					items.remove(ti);
				} else {
					if (remainder > 0) {
						ti.removeArticles(a.getQuantity());
					} else {
						throw new IllegalArgumentException("You cannot remove more articles than already present");
					}
				}
				return;
			}
		}

		updateAmount();
	}

	/**
	 * Convenience method to output ticket contents as a text file
	 */
	public void saveTicketToText() {
		// Output the date and time of the ticket in a human readable localized form
		String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(new Date());

		File f = new File("tickets/" + date + ".txt");

		try (FileWriter fw = new FileWriter(f)) {
			fw.append(printTicketToText(date));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set the user for which this Ticket is edited
	 *
	 * @param newUser user
	 */
	public void setUser(User newUser) {
		customer = newUser; // no check on null because it has a meaning
	}

	/**
	 * Converts a ticket with its TicketItem to transactions for saving, and saves
	 * the transactions file The Ticket object will then be able to be deleted
	 *
	 */
	public void submit(User destUser, TransactionDB tdb, Vendor v) {
		vendor = v;
		for (TicketItem item : items) {
			if (item != null) {
				Transaction t = new Transaction((int) (item.getAmount()) * 100, customer, destUser, item.getArticle(),
						item.getQuantity(), pMethod, v);
				tdb.add(t);
				Article solde = item.getArticle();
				if (solde.hasStockMgmtEnabled()) {
					solde.setStock(solde.getStock() - item.getQuantity());
				}

				solde.use();
			}
		}

		// Debit customer account (sourceUser) and
		if (customer != null && pMethod == null) {
			customer.debite(amount);
		}
		// Credit customer account (destUser) ??
		if (destUser != null && pMethod == null) {
			destUser.credite(amount);
		}
	}

	/**
	 * Recompute the total for the ticket
	 */
	public void updateAmount() {
		amount = items.stream().filter(item -> item != null).mapToDouble(item -> item.getAmount()).sum();
	}

}
