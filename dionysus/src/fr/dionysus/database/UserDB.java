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

package fr.dionysus.database;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import fr.dionysus.*;

public class UserDB extends Database<User> {	
	
	public UserDB(){
		data = new ArrayList<User>(20);
		numberOfRecords = 0;
	}
	
	public void remove(User u)
	{
		if(u != null){
			if(u.getBalance() != 0){
				int choice = JOptionPane.showConfirmDialog(null, "This user has a nonzero balance.\nAre you sure to delete him/her?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				if(choice != JOptionPane.YES_OPTION)
					return;
			}
			
			data.remove(u);
			numberOfRecords--;

			saveToTextFile();
			makeArrayForTables();
		}
	}
	
	public void modify(User u, int indice)
	{
		if(u != null){ //No reason to pass a null argument, there is a function for cleaning up...
			data.set(indice, u);
			
			saveToTextFile();
			makeArrayForTables();//TODO: optimize !
		}
	}

	@Override
	public void makeArrayForTables() {
		foodForTable = new Object[numberOfRecords][5];
		for(int i = 0 ; i < numberOfRecords ; i++){
			foodForTable[i][0] = data.get(i).getLastName();
			foodForTable[i][1] = data.get(i).getFirstName();
			foodForTable[i][2] = new Integer(data.get(i).getPromo());
			foodForTable[i][3] = new Double(data.get(i).getBalance());
			foodForTable[i][4] = new Boolean(data.get(i).hasPaidCaution());
		}
	}

	@Override
	public Object[][] getArrayForTables() {
		return foodForTable;
	}

	@Override
	public User[] getArray() {
		return (User []) data.toArray(new User[numberOfRecords]);
	}
	
}
