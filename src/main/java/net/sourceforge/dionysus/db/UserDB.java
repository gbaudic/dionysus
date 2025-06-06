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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JOptionPane;

import net.sourceforge.dionysus.User;

public class UserDB extends Database<User> {

	/**
	 * Constructor
	 */
	public UserDB() {
		data = new ArrayList<>(20);
		numberOfRecords = 0;
	}

	/**
	 * Initializes user database from a text file originating from the "original"
	 * software
	 *
	 * @param filename the path to the database text file
	 */
	public void createFromLegacyTextFile(String filename) {
		targetF = new File(filename);
		if (!targetF.exists()) {
			JOptionPane.showMessageDialog(null, "Error when trying to access database!", Database.ERROR,
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// File exists, go on!
		try (LineNumberReader lnr = new LineNumberReader(new BufferedReader(new FileReader(targetF)))) {
			String s;

			while ((s = lnr.readLine()) != null) {
				String[] values = s.split("><|<|>");
				int promo = Integer.parseInt(values[1]);
				int balance = Math.round(Float.parseFloat(values[4]) * 100F); // Float because Double led to rounding
																				// issues

				data.add(new User(values[2], values[3], promo, balance));
				numberOfRecords++;
			}

			makeArrayForTables();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					String.format("Error with the file backing the User database: %s.", e.getLocalizedMessage()),
					Database.ERROR, JOptionPane.ERROR_MESSAGE);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
					String.format("Illegal numeric value encountered while loading legacy user database: %s.",
							e.getLocalizedMessage()),
					Database.ERROR, JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Saves the user database to a text format compliant with the "original"
	 * software This should not be used for normal operation
	 *
	 * @param filename full path to destination file
	 */
	public void exportToLegacyTextFile(String filename) {
		try (BufferedWriter bw = new BufferedWriter(new PrintWriter(filename))) {
			for (final User user : data) {
				if (Objects.nonNull(user)) {
					bw.write(user.getTextForLegacyFile() + "\r\n");
				} else {
					bw.write("null\r\n");
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, String
					.format("Error when exporting the User database to legacy format: %s.", e.getLocalizedMessage()),
					Database.ERROR, JOptionPane.ERROR_MESSAGE);
		}
	}

	/** {@inheritDoc} */
	@Override
	public User[] getArray() {
		return data.toArray(new User[numberOfRecords]);
	}

	/** {@inheritDoc} */
	@Override
	public Object[][] getArrayForTables() {
		return foodForTable;
	}

	/** {@inheritDoc} */
	@Override
	public void makeArrayForTables() {
		foodForTable = new Object[numberOfRecords][6];
		for (int i = 0; i < numberOfRecords; i++) {
			foodForTable[i][0] = data.get(i).getLastName();
			foodForTable[i][1] = data.get(i).getFirstName();
			foodForTable[i][2] = data.get(i).getID();
			foodForTable[i][3] = data.get(i).getPromo();
			foodForTable[i][4] = NumberFormat.getCurrencyInstance().format(data.get(i).getBalance());
			foodForTable[i][5] = data.get(i).hasPaidCaution();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void modify(User u, int index) {
		if (u != null && index >= 0 && index < numberOfRecords) {
			// No reason to pass a null argument, there is a function for cleaning up...
			data.set(index, u);

			saveToTextFile();
			makeArrayForTables();// TODO: optimize !
		}
	}

	/** {@inheritDoc} */
	@Override
	public void remove(User u) {
		if (u != null) {
			if (u.getBalance() != 0) {
				int choice = JOptionPane.showConfirmDialog(null,
						"This user has a nonzero balance.\nAre you sure to delete them?", "Confirmation",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				if (choice != JOptionPane.YES_OPTION) {
					return;
				}
			}

			if (data.remove(u)) {
				numberOfRecords--;

				saveToTextFile();
				makeArrayForTables();
			}

		}
	}

}
