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
 * Enum to specify a payment method
 *
 * @author G. Baudic
 *
 */
public enum PaymentMethod implements Serializable {

	// ACCOUNT("Account"), //for 0.3 release
	CASH("Cash"), CHECK("Check"), CREDIT_CARD("Card"), MONEO_CARD("Izly"), COFFEE_CARD("Coffee card"), MISC("Misc.");
	/*
	 * "Coffee card" was a paper card paid in advance that entitled the buyer to
	 * drink 11 coffees at the price of 10 "Moneo" was a French card system which
	 * looks like a credit card, except that it is loaded with a given amount and
	 * can be refilled using a standard credit card. It was originally designed to
	 * pay small amounts, thus avoiding using small coins (0.01, 0.02, 0.05Euro...),
	 * for example at the butcher's or at the baker's. In reality, almost no one
	 * used it because only a few shops were equipped. But in our city it was used
	 * for student restaurants and vending machines in the university, and we had
	 * planned to be equipped in the student pub. This feature was basically
	 * implemented in Dionysus for future use. Note that the use of this system
	 * stopped in the summer of 2015. It was replaced by a new system called izly.
	 */

	/** User-friendly name of payment method */
	private String name;

	/**
	 * Constructor
	 *
	 * @param s name
	 */
	PaymentMethod(String s) {
		name = s;
	}

	/**
	 * Getter for the name
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
