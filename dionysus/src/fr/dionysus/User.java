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

public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Structure representing a user
	 * It has roughly the same functionalities as the one in the 'original software'
	 */

	private String lastName;
	private String firstName;
	private int promo;
	private int balance; //Balance in cents
	private boolean paidCaution; 
	/* To prevent students for staying for too long with a negative balance (and eventually leaving university without paying...),
	 * they had to give an amount of money that would be only used if they get in such a situation. This flag tells if they have paid it or not.
	*/
	
	public User(String lastName, String firstName, int promo, int balance) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.promo = promo;
		this.balance = balance;
		paidCaution = false;
	}

	public User(String lastName, String firstName, int promo, int balance,
			boolean paidCaution) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.promo = promo;
		this.balance = balance;
		this.paidCaution = paidCaution;
	}

	/**
	 * Returns the balance in euros
	 * @return the balance in euros
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
	public void debite(double montant)
	{
		this.balance -= (int)(montant*100);
	}
	
	/**
	 * Returns the whole name
	 * @return the whole name
	 */
	public String getName()
	{
		return lastName+" "+firstName;
	}
	
	/**
	 * Returns the full name with the year
	 * @return full name (year)
	 */
	public String getFullName()
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
	
	/**
	 * Returns a string for the database file
	 * The format used is the one for the so-called 'original software' for backwards compatibility
	 * @return
	 */
	public String getTextForFile(){
		return "<"+String.valueOf(promo)+"><"+lastName+"><"+firstName+"><"+String.valueOf(balance)+">";
	}

	public boolean hasPaidCaution() {
		return paidCaution;
	}

	public void setPaidCaution(boolean paidCaution) {
		this.paidCaution = paidCaution;
	}
	
}
