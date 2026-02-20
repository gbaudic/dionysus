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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.NumberFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Article
 */
public class ArticleTest {

	/** Object under test */
	private Article article;

	private Price p0 = new Price(123);
	private Price p1 = new Price(234);
	private Price p2 = new Price(345);

	@Before
	public void setUp() {

		article = new Article("test", new Price[]{p0, p1, p2}, 123456789L);
	}

	/**
	 * Test method for {@link net.sourceforge.dionysus.Article#addStock(int)}.
	 */
	@Test
	public void testAddStock() {
		Assert.assertEquals(0, article.getStock());
		article.addStock(15);
		Assert.assertEquals(15, article.getStock());
	}

	/**
	 * Test method for {@link net.sourceforge.dionysus.Article#getNumberOfPrices()}.
	 */
	@Test
	public void testGetNumberOfPrices() {
		assertEquals(3, article.getNumberOfPrices());
	}

	/**
	 * Test method for {@link net.sourceforge.dionysus.Article#getToolTipText()}.
	 */
	@Test
	public void testGetToolTipText() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		assertEquals(
				"<html>test (123456789)<br/>" + String.format("Price 0: %s<br/>", formatter.format(p0.getPrice()))
						+ String.format("Price 1: %s<br/>", formatter.format(p1.getPrice()))
						+ String.format("Price 2: %s", formatter.format(p2.getPrice())) + "</html>",
				article.getToolTipText());
	}

	/**
	 * Test method for {@link net.sourceforge.dionysus.Article#toCSV()}.
	 */
	@Test
	public void testToCSV() {
		assertEquals("test;123456789;1.23;2.34;3.45;;;false;false;false;false;true", article.toCSV());
	}

	/**
	 * Test method for {@link net.sourceforge.dionysus.Article#use()}.
	 */
	@Test
	public void testUse() {
		assertFalse(article.hasBeenUsed());
		article.use();
		assertTrue(article.hasBeenUsed());
	}

}
