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

public class TransactionDB implements Database<Transaction> {

	private Transaction [] data;
	private Object[][] foodForTable;
	private File targetF;
	private int numberOfTransactions;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	//Not necessarily in a text file
	
	public TransactionDB(){
		data = new Transaction[20];
		numberOfTransactions = 0;
	}
	
	@Override
	public void createFromTextFile(String filename) {
		targetF = new File(filename);
		if(!targetF.exists()){
			JOptionPane.showMessageDialog(null, "Error while accessing transaction database!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//File exists, go on!
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(targetF)));
			
			//Read the number of saved articles
			int size = ois.readInt();
			
			data = new Transaction[size];
			numberOfTransactions = size; //We assume no null has been stored
			for(int i = 0 ; i < size ; i++){
				data[i] = (Transaction) ois.readObject();
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
			
			oos.writeInt(numberOfTransactions); //Write the number of transactions
			for(int i=0 ; i < numberOfTransactions ; i++){
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

	@Override
	public Transaction[] getArray() {
		return data;
	}
	
	//It is only permitted to add transactions, they can be cancelled afterwards but not deleted
	public void add(Transaction t)
	{
		if(t != null){
			if(numberOfTransactions >= data.length){
				Transaction[] newData = new Transaction[numberOfTransactions + 1];
				for(int i = 0 ; i < data.length ; i++){
					newData[i] = data[i];
				}
				data = newData;
			}
			
			data[numberOfTransactions] = t;
			numberOfTransactions++;
			
			saveToTextFile();
			makeArrayForTables();
		}
	}

	public void makeArrayForTables() {
		foodForTable = new Object[numberOfTransactions][7];
		
		for(int i = 0 ; i < numberOfTransactions ; i++){
			foodForTable[i][0] = data[i].getDate();
			foodForTable[i][1] = data[i].getArticle().getName();
			foodForTable[i][2] = new Integer(data[i].getNumberOfItems());
			foodForTable[i][3] = new Double(data[i].getAmount().getPrice()); //do not try to understand...
			
			if(data[i].getSourceUser() != null){
				foodForTable[i][4] = data[i].getSourceUser().getFullName();
			} else {
				foodForTable[i][4] = "none";
			}
			
			if(data[i].getDestUser() != null){
				foodForTable[i][5] = data[i].getDestUser().getFullName();
			} else {
				foodForTable[i][5] = "none";
			}
			
			if(data[i].getPaymentMethod() != null){
				foodForTable[i][6] = data[i].getPaymentMethod().getName();
			} else {
				foodForTable[i][6] = "Account";
			}	
		}	
	}
	
	public Object[][] getArrayForTables(){
		return foodForTable;
	}

	@Override
	public void modify(Transaction t, int index) {
		// Transactions cannot be modified
		
	}

	@Override
	public void remove(Transaction t) {
		// Transactions cannot be removed, only reverted
		
	}

	
}
