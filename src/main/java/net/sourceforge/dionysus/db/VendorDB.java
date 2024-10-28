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

package net.sourceforge.dionysus.db;

import net.sourceforge.dionysus.Vendor;

public class VendorDB extends Database<Vendor> {

	/** {@inheritDoc} */
	@Override
	public Vendor[] getArray() {
		return data.toArray(new Vendor[numberOfRecords]);
	}

	/** {@inheritDoc} */
	@Override
	public void makeArrayForTables() {
		// No vendor tables for the moment

	}

	/** {@inheritDoc} */
	@Override
	public void modify(Vendor t, int index) {

		if (t != null && index >= 0 && index < numberOfRecords) {
			data.set(index, t);

			saveToTextFile();
			makeArrayForTables();
		}

	}

	/** {@inheritDoc} */
	@Override
	public void remove(Vendor t) {
		// Do nothing for the moment

	}

	/**
	 * Checks if a given login and password match a user in the database
	 *
	 * @param login    user login
	 * @param password user password
	 * @return true if the login/password pair matches, false otherwise
	 */
	public boolean validateIdentification(String login, String password) {

		if (login != null && password != null && !login.isEmpty()) { // yes, password can be empty...
			// Check that the login exists

			// If it does, verify the corresponding password
		} else {
			return false;
		}
		return false;
	}

}
