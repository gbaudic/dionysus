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

/**
 * Interface for all objects which may get exported with their database as
 * CSV<br/>
 *
 * CSV export is mainly here for interoperability
 */
public interface CSVAble {

	/**
	 * Produce a header describing contents of each field
	 *
	 * @return the header line for the CSV file
	 */
	public String csvHeader();

	/**
	 * Getter
	 *
	 * @return serialized representation of the object for CSV
	 */
	public String toCSV();
}
