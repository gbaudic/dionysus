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

import org.junit.Assert;
import org.junit.Test;

public class PriceTest {

	/**
	 * Test method for {@link net.sourceforge.dionysus.Price#getPrice()}.
	 */
	@Test
	public void testGetPrice() {
		Price fisher = new Price(1000);
		Assert.assertEquals(10.00, fisher.getPrice(), 0.001);
	}

	@Test
	public void testGetPriceWithDouble() {
		Price fisher = new Price(12.45);
		Assert.assertEquals(12.45, fisher.getPrice(), 0.001);
	}

}
