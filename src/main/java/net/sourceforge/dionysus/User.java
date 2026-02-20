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

/**
 * Structure representing a user It has roughly the same functionalities as the
 * one in the 'original software'
 */

public class User implements Serializable, CSVAble {

	private static final long serialVersionUID = 728813682202079442L;

	private final String lastName;
	private final String firstName;
	private final int promo;
	/** Balance in cents */
	private int balance;
	/**
	 * To prevent students from staying for too long with a negative balance (and
	 * eventually leaving university without paying...), they had to give an amount
	 * of money that would be only used if they get in such a situation. This flag
	 * tells if they have paid it or not.
	 */
	private boolean paidDeposit;
	/** unique identifier, such as a barcode or QR code on a member card */
	private String id;

	public User(final String lastName, final String firstName, final int promo, final int balance) {
		this(lastName, firstName, promo, balance, false);
	}

	public User(final String lastName, final String firstName, final int promo, final int balance,
			final boolean paidDeposit) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.promo = promo;
		this.balance = balance;
		this.paidDeposit = paidDeposit;
		id = "";
	}

	/**
	 * Adds an amount to the current balance
	 *
	 * @param montant
	 *            amount to credit
	 */
	public void credite(final double montant) {
		this.balance += (int) (montant * 100);
	}

	@Override
	public String csvHeader() {
		return "# Last name;First name;ID;Promo;Balance;Paid deposit";
	}

	/**
	 * Deduces an amount from current balance
	 *
	 * @param montant
	 *            amount to debit
	 */
	public void debite(final double montant) {
		this.balance -= (int) (montant * 100);
	}

	/**
	 * Returns the balance
	 *
	 * @return the balance TODO: check the result of the division when balance is
	 *         negative
	 */
	public double getBalance() {
		return balance / 100.;
	}

	/**
	 * Returns the first name (for editing)
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns the ID
	 *
	 * @return ID
	 */
	public String getID() {
		return id;
	}

	/**
	 * Returns the last name (for editing)
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns the whole name
	 *
	 * @return the whole name
	 */
	public String getName() {
		return lastName + " " + firstName;
	}

	/**
	 * Returns the full name with the year
	 *
	 * @return full name (year)
	 */
	public String getNameWithPromo() {
		return this.getName() + " (" + promo + ")";
	}

	/**
	 * Returns the "promo" (for display)
	 *
	 * @return the "promo" number
	 */
	public int getPromo() {
		return promo;
	}

	/**
	 * Returns a string for the database file The format used is the one for the
	 * so-called 'original software' for backwards compatibility
	 *
	 * @return promo, lastName, firstName and balance, each field surrounded by
	 *         <...>
	 */
	public String getTextForLegacyFile() {
		final String bal = String.valueOf(balance / 100.0);
		return String.format("<%s><%s><%s><%s>", String.valueOf(promo), lastName, firstName, bal);
	}

	public boolean hasPaidCaution() {
		return paidDeposit;
	}

	public void setID(final String id) {
		if (id != null && !id.matches("^\\s*$")) {
			this.id = id;
		}
	}

	public void setPaidCaution(final boolean paidCaution) {
		this.paidDeposit = paidCaution;
	}

	@Override
	public String toCSV() {
		final String bal = String.valueOf(balance / 100.0);
		return lastName + ";" + firstName + ";" + id + ";" + promo + ";" + bal + ";" + paidDeposit;
	}

}
