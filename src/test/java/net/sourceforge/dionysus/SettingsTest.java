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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.Test;

public class SettingsTest {
	// The properties file used by Settings.
	private final File file = new File("dionysus.ini");

	// Clean up after tests.
	@After
	public void tearDown() {
		if (file.exists()) {
			file.delete();
		}
	}

	@Test
	public void testLoadPartial() throws IOException {
		final Settings settings = Settings.getInstance();

		settings.load("src/test/resources/conf1.ini");

		assertTrue(settings.getUseScanner());
		assertEquals("", settings.getTicketFooter());
		assertEquals("My Shop, LLC", settings.getTicketHeader());
	}

	@Test
	public void testSaveLoad() throws IOException {
		final Settings settings = Settings.getInstance();

		// Set properties, including a multi line one.
		// This is necessary to make sure we can properly handle the ticket header and
		// footer.
		settings.setUseScanner(true);
		settings.setTicketHeader("Tom's Diner\nSunset Blvd 1");
		settings.setTicketFooter("Goodbye");

		// Save the settings to a file and explicitly reload it.
		// Otherwise it is only going to be loaded once at the creation of the settings
		// object.
		settings.save();
		settings.load();

		// Verify the loaded values match the saved ones.
		assertTrue(settings.getUseScanner());
		assertEquals("Tom's Diner\nSunset Blvd 1", settings.getTicketHeader());
		assertEquals("Goodbye", settings.getTicketFooter());
	}
}
