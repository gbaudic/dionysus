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
public class UserTest {

	/** Object being tested */
	private User testUser;

	@Before
	public void setUp() {
		testUser = new User("Test", "User", 62, 301);
	}

	/**
	 * Test method for {@link net.sourceforge.dionysus.User#credite(double)}.
	 */
	@Test
	public void testCredite() {
		testUser.credite(25.66);
		Assert.assertEquals(28.67, testUser.getBalance(), 0.0001);
	}

	/**
	 * Test method for {@link net.sourceforge.dionysus.User#debite(double)}.
	 */
	@Test
	public void testDebite() {
		testUser.debite(1.50);
		Assert.assertEquals(1.51, testUser.getBalance(), 0.0001);
	}

	/**
	 * Test method for {@link net.sourceforge.dionysus.User#getNameWithPromo()}.
	 */
	@Test
	public void testGetNameWithPromo() {
		Assert.assertEquals("Test User (62)", testUser.getNameWithPromo());
	}

	/**
	 * Test method for {@link net.sourceforge.dionysus.User#getTextForLegacyFile()}.
	 */
	@Test
	public void testGetTextForLegacyFile() {
		Assert.assertEquals("<62><Test><User><3.01>", testUser.getTextForLegacyFile());
	}

	/**
	 * Test method for {@link net.sourceforge.dionysus.User#toCSV()}.
	 */
	@Test
	public void testToCSV() {
		Assert.assertEquals("Test;User;;62;3.01;false", testUser.toCSV());
	}

}
