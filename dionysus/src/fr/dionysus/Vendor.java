/* 	Dionysus, a student pub management software in Java
    Copyright (C) 2011,2013  podgy_piglet

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

package fr.dionysus;

import java.io.Serializable;

public class Vendor implements Serializable {

	private static final long serialVersionUID = 7579644578907922526L;
	/**
	 * Structure representing a vendor, a person who is part of the team running the shop and has therefore
	 * the right to use the software
	 * It will help tracking which transactions have been made under the supervision of a given person.
	 * Useful for a student pub which may be run by different teams at different days, or a shop with several vendors
	 */

	private String name; //Name appearing on the screen and on tickets
	private String login; //Login for the identification process
	private char[] cPassword;//There is also a password
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	public static boolean validateIdentification(String login, String password) {
		
		//Check that the login exists
		
		//If it does, verify the corresponding password
		return false;
	}
	
	private boolean checkPassword(String password) {
		return false;
	}
}
