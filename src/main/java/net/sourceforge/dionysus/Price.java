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
import java.text.NumberFormat;

/**
 * Abstraction class to deal with amounts using cents instead of usual currency
 * units This allows to use integers and to avoid occasional rounding errors
 */

public class Price implements Serializable {

	/** The version UID */
	private static final long serialVersionUID = 492682353831884654L;

	/** Price stored as an integer. 12.45 would be 1245 */
	private int value;

	/**
	 * Constructor for amounts set as double
	 *
	 * @param price
	 *            price as a double
	 */
	public Price(double price) {
		this.value = (int) (price * 100);
	}

	/**
	 * Constructor for amounts set as integers (already converted in cents)
	 *
	 * @param price
	 *            integer price
	 */
	public Price(int price) {
		this.value = price;
	}

	/**
	 * Getter for price
	 *
	 * @return price, expressed in the usual human way ($12.45)
	 */
	public double getPrice() {
		return value / 100.;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return NumberFormat.getCurrencyInstance().format(value / 100.);
	}
}
