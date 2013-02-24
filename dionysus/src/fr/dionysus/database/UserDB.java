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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import fr.dionysus.*;

public class UserDB implements Database {
	
	private User [] data;
	private Object [][] foodForTable;
	private File targetF;
	private int numberOfUsers;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public UserDB(){
		data = new User[20];
		numberOfUsers = 0;
	}

	@Override
	public void createFromTextFile(String filename) {
		targetF = new File(filename);
		if(!targetF.exists()){
			JOptionPane.showMessageDialog(null, "Error when trying to access user database!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//File exists, go on!
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(targetF)));
			
			//Read the total number of saved users
			int size = ois.readInt();
			
			data = new User[size];
			numberOfUsers = size; //We assume no null object has been saved
			for(int i = 0 ; i < size ; i++){
				data[i] = (User) ois.readObject();
			}
			
			ois.close();
			
			makeArrayForTables();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveToTextFile() {
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(targetF)));
			
			oos.writeInt(numberOfUsers); //Writing the number of users
			for(int i=0 ; i < numberOfUsers ; i++){
				//if(data[i] != null) //Do not exclude nulls for coherence
					oos.writeObject(data[i]);
			}
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void addUser(User u)
	{
		if(u != null){
			if(numberOfUsers >= data.length){
				User[] newData = new User[numberOfUsers + 1];
				for(int i = 0 ; i < data.length ; i++){
					newData[i] = data[i];
				}
				data = newData;
			}
			
			data[numberOfUsers] = u;
			numberOfUsers++;
			
			saveToTextFile();
			makeArrayForTables();
		}
	}
	
	public void removeUser(User u)
	{
		if(u != null){
			if(u.getBalance() != 0){
				int choice = JOptionPane.showConfirmDialog(null, "This user has a nonzero balance.\nAre you sure to delete him/her?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				
				if(choice != JOptionPane.YES_OPTION)
					return;
			}
				for(int i = 0 ; i < numberOfUsers ; i++){
					if(data[i].equals(u)){
						data[i] = null;
						for(int j = i+1 ; j < numberOfUsers ; j++){
							data[j-1] = data[j];
						}
						numberOfUsers--;
						break;
					}
				}
			saveToTextFile();
			makeArrayForTables();
		}
	}
	
	public void modifyUser(User u, int indice)
	{
		if(u != null){ //No reason to pass a null argument, there is a function for cleaning up...
			data[indice] = u;
			
			saveToTextFile();
			makeArrayForTables();//TODO: optimize !
		}
	}

	@Override
	public Object[] getArray() {
		return data;
	}

	@Override
	public void makeArrayForTables() {
		foodForTable = new Object[numberOfUsers][5];
		for(int i = 0 ; i < numberOfUsers ; i++){
			foodForTable[i][0] = data[i].getLastName();
			foodForTable[i][1] = data[i].getFirstName();
			foodForTable[i][2] = new Integer(data[i].getPromo());
			foodForTable[i][3] = new Double(data[i].getBalance());
			foodForTable[i][4] = new Boolean(data[i].hasPaidCaution());
		}
	}

	@Override
	public Object[][] getArrayForTables() {
		return foodForTable;
	}
	
}
