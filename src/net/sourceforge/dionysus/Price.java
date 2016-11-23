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
 * Abstraction class to deal with amounts using cents instead of usual currency units
 * This allows to use integers and to avoid occasional rounding errors
 */

public class Price implements Serializable{

	private static final long serialVersionUID = 1L;
	private int price;
	
	/**
	 * Constructor for amounts set as double
	 */
	public Price(double price) {
		this.price = (int)(price*100);
	}
	
	/**
	 * Constructor for amounts set as integers (already converted in cents)
	 */
	public Price(int price) {
		this.price = price;
	}
	
	public String toString() {
		return String.valueOf(price/100.);
	}
	
	public double getPrice() {
		return price/100.;
	}
}
