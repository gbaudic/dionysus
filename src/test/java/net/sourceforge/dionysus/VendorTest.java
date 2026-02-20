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
import org.junit.Before;
import org.junit.Test;

/**
 * @author Gwilherm
 *
 */
public class VendorTest {

	private Vendor vendor;

	@Before
	public void setUp() {
		vendor = new Vendor("test", "t", "pass");
	}

	/**
	 * Test method for
	 * {@link net.sourceforge.dionysus.Vendor#Vendor(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testVendor() {
		Assert.assertEquals("test", vendor.getLogin());
		Assert.assertEquals("t", vendor.getName());
		Assert.assertEquals("pass", vendor.getcPassword());
	}

	/**
	 * Test method for
	 * {@link net.sourceforge.dionysus.Vendor#setName(java.lang.String)}.
	 */
	@Test
	public void testSetName() {
		vendor.setName("name");
		Assert.assertEquals("name", vendor.getName());
	}

	/**
	 * Test method for
	 * {@link net.sourceforge.dionysus.Vendor#setLogin(java.lang.String)}.
	 */
	@Test
	public void testSetLogin() {
		vendor.setLogin("newLogin");
		Assert.assertEquals("newLogin", vendor.getLogin());
	}

}
