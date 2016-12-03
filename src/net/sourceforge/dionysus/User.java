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

import java.io.Serializable;

/**
 * Structure representing a user
 * It has roughly the same functionalities as the one in the 'original software'
 */

public class User implements Serializable,CSVAble {
	
	private static final long serialVersionUID = 1L;
	
	private String lastName;
	private String firstName;
	private int promo;
	private int balance; //Balance in cents
	private boolean paidDeposit; 
	/* To prevent students from staying for too long with a negative balance (and eventually leaving university without paying...),
	 * they had to give an amount of money that would be only used if they get in such a situation. This flag tells if they have paid it or not.
	*/
	
	public User(String lastName, String firstName, int promo, int balance) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.promo = promo;
		this.balance = balance;
		paidDeposit = false;
	}

	public User(String lastName, String firstName, int promo, int balance,
			boolean paidDeposit) {
		this(lastName,firstName,promo,balance);
		this.paidDeposit = paidDeposit;
	}

	/**
	 * Returns the balance
	 * @return the balance
	 * TODO: check the result of the division when balance is negative
	 */
	public double getBalance() {
		return balance/100.;
	}

	/**
	 * Adds an amount to the current balance
	 * @param montant amount to credit
	 */
	public void credite(double montant) {
		this.balance += (int)(montant*100);
	}
	
	/**
	 * Deduces an amount from current balance
	 * @param montant amount to debit
	 */
	public void debite(double montant) {
		this.balance -= (int)(montant*100);
	}
	
	/**
	 * Returns the whole name
	 * @return the whole name
	 */
	public String getName() {
		return lastName+" "+firstName;
	}
	
	/**
	 * Returns the full name with the year
	 * @return full name (year)
	 */
	public String getNameWithPromo()
	{
		return this.getName()+" ("+String.valueOf(promo)+")";
	}

	/**
	 * Returns the last name (for editing)
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns the first name (for editing)
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns the "promo" (for display)
	 * @return the "promo" number
	 */
	public int getPromo() {
		return promo;
	}
	
	public boolean hasPaidCaution() {
		return paidDeposit;
	}

	public void setPaidCaution(boolean paidCaution) {
		this.paidDeposit = paidDeposit;
	}
	
	/**
	 * Returns a string for the database file
	 * The format used is the one for the so-called 'original software' for backwards compatibility
	 * @return promo, lastName, firstName and balance, each field surrounded by <...>
	 */
	public String getTextForLegacyFile(){
		String bal = String.valueOf(balance / 100.0);
		return "<"+String.valueOf(promo)+"><"+lastName+"><"+firstName+"><"+bal+">";
	}

	@Override
	public String toCSV() {
		String bal = String.valueOf(balance / 100.0);
		return lastName+";"+firstName+";"+promo+";"+bal+";"+paidDeposit;
	}
	
	public String csvHeader() {
	    return "# Last name;First name;Promo;Balance;Paid deposit";
	}
	
}
