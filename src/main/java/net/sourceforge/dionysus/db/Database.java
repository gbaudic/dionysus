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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import net.sourceforge.dionysus.*;

/**
 * Generic interface for databases
 *
 * @param <T> The class which will be held in the database (User, Article...)
 */
public abstract class Database<T> {

	protected ArrayList<T> data;
	protected Object [][] foodForTable;
	protected File targetF;
	protected int numberOfRecords;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	/**
	 * Initializes DB with the binary data from a given file
	 * @param filename path to the file to be used
	 */
	public void createFromTextFile(String filename) {

		targetF = new File(filename);
		if(!targetF.exists()){
			JOptionPane.showMessageDialog(null, "Error when trying to access database file: "+filename, "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//File exists, go on!
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(targetF)));
			
			//Read the total number of saved users
			int size = ois.readInt();
			
			data.ensureCapacity(size);
			numberOfRecords = size; //We assume no null object has been saved
			for(int i = 0 ; i < size ; i++){
				data.add((T) ois.readObject());
			}
			
			ois.close();
			
			makeArrayForTables();
		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null,
					"Error when trying to save database file: "+targetF.getName()+"\n"+e.getLocalizedMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Saves the database content to a binary (and not text, despite the name) file
	 */
	public void saveToTextFile() {
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(targetF)));
			
			oos.writeInt(numberOfRecords); //Writing the number of users
			for(int i=0 ; i < numberOfRecords ; i++){
				//if(data[i] != null) //Do not exclude nulls for coherence
					oos.writeObject(data.get(i));
			}
			oos.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Error when trying to access database file: "+targetF.getName()+"\n"+e.getLocalizedMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
		}	
	}
	
	/**
	 * Export the database in a human-friendly format (CSV, actually)
	 * @param file the File object to write to
	 */
	public void export(File file){
		if(file != null){
			try (BufferedWriter bw = new BufferedWriter(new PrintWriter(file))) {
				//Write header
				if(data.size() > 0)
					bw.write(((CSVAble) data.get(0)).csvHeader()+"\r\n");
				//Write data
				for(T obj : data){
					bw.write(((CSVAble) obj).toCSV()+"\r\n");
				}
			} catch (IOException | NullPointerException | ClassCastException e) {
				JOptionPane.showMessageDialog(null,
						"Error when exporting database file to "+file.getName()+"\n"+e.getLocalizedMessage(), 
						"Export error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Returns the data array which is at the heart of the database
	 * @return the content of the database as an array of the recorded objects (with the right type)
	 */
	public abstract T [] getArray();
	
	/**
	 * Generates an array compliant with a display in a JTable component
	 * @return an Object array
	 */
	public Object [][] getArrayForTables(){
		return foodForTable;
	}
	
	/**
	 * Generates internally the array required for display
	 */
	public abstract void makeArrayForTables();
	
	/**
	 * Adds an entry to the database
	 * @param t the entry to add
	 */
	public void add(T t) {
		if(t != null){

			data.add(t);
			numberOfRecords++;

			saveToTextFile();
			makeArrayForTables();
		}
	}
	
	/**
	 * Modifies an entry in the database
	 * @param t modified version of the entry
	 * @param index place to insert
	 */
	public abstract void modify(T t, int index);
	
	/**
	 * Deletes an entry in the database
	 * @param t entry to remove
	 */
	public abstract void remove(T t);
}
