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
import java.util.ArrayList;

import javax.swing.JOptionPane;
import fr.dionysus.*;

/**
 * A class implementing an article database
 * TODO: exploit ArrayLists at their full potential ;)
 */
public class ArticleDB implements Database<Article> {

	private ArrayList<Article> data;
	private Object [][] foodForTable;
	private File targetF;
	private int numberOfArticles;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	//Not necessarily in a text file
	
	public ArticleDB(){
		data = new ArrayList<Article>(20);
		numberOfArticles = 0;
	}
	
	@Override
	public void createFromTextFile(String filename) {
		targetF = new File(filename);
		if(!targetF.exists()){
			JOptionPane.showMessageDialog(null, "Error while accessing article database!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//File exists, go on!
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(targetF)));
			
			//Reading the number of saved articles
			int size = ois.readInt();
			
			data.ensureCapacity(size);
			numberOfArticles = size; //We assume no null has been stored
			for(int i = 0 ; i < size ; i++){
				data.add((Article) ois.readObject());
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
			
			oos.writeInt(numberOfArticles); //Write the number of articles
			for(int i=0 ; i < numberOfArticles ; i++){
				//if(data[i] != null) //Do not exclude nulls for coherence
					oos.writeObject(data.get(i));
			}
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Article[] getArray() {
		return (Article[]) data.toArray();
	}
	
	/**
	 * Adds an article to the database
	 * @param a the article to add
	 */
	public void add(Article a)
	{
		if(a != null){
						
			data.add(a);
			numberOfArticles++;
			
			saveToTextFile();
			makeArrayForTables();
		}
	}
	
	/**
	 * Removes an article from the list, and cleans up the array to remove any null
	 * @param a article to be removed
	 */
	public void remove(Article a)
	{
		if(a != null){
			if(a.hasBeenUsed()){
				JOptionPane.showMessageDialog(null, "Article already used! \nDeleting it will prevent cancellation of all transactions involving this article.", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
			//Merge these two dialogs in one?
			int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this article?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if(choice != JOptionPane.YES_OPTION)
				return;
			
			data.remove(a);
			numberOfArticles--;
			
		}
		saveToTextFile();
		makeArrayForTables();
	}

	/**
	 * Modifies a given article
	 * @param a
	 * @param indice
	 */
	public void modify(Article a, int index){
		if(a != null){ //No reason to pass a null, there is a function for cleaning up...
			if(index >= 0 && index < numberOfArticles){
				data.set(index, a);

				saveToTextFile();
				makeArrayForTables(); //TODO : optimize!
			}
		}
	}

	/**
	 * Allows finding an article with its integer code
	 * @param code the code to find
	 * @return first article found with this code, null if no article matches
	 */
	public Article getArticleByCode(long code){
		for(int i = 0 ; i < numberOfArticles ; i++){
			if(data.get(i) != null && data.get(i).getCode() == code){
				return data.get(i);
			}
		}
		
		return null;
	}
	
	public int getNumberOfArticles() {
		return numberOfArticles;
	}

	@Override
	public void makeArrayForTables() {
		foodForTable = new Object[numberOfArticles][8];
		for(int i = 0 ; i < numberOfArticles ; i++){
			foodForTable[i][0] = data.get(i).getName();
			foodForTable[i][1] = new Long(data.get(i).getCode());
			foodForTable[i][2] = new Boolean(data.get(i).isActive());
			foodForTable[i][3] = new Double(data.get(i).getArticlePrice());
			
			if(data.get(i).getNumberOfPrices() >= 1){
				foodForTable[i][4] = new Double(data.get(i).getArticlePrice(1));
			} else {
				foodForTable[i][4] = new Double(0.00);
			}
			
			if(data.get(i).getNumberOfPrices() >= 2){
				foodForTable[i][5] = new Double(data.get(i).getArticlePrice(2));
			} else {
				foodForTable[i][5] = new Double(0.00);
			}
			
			if(data.get(i).hasStockMgmtEnabled()){
				foodForTable[i][6] = String.valueOf((data.get(i).getStock()));
			} else {
				foodForTable[i][6] = "NA";
			}
			
			if(data.get(i).hasStockAlertEnabled()){
				foodForTable[i][7] = String.valueOf(data.get(i).getLimitStock());
			} else {
				foodForTable[i][7] = "NA";
			}
		}
	}

	@Override
	public Object [][] getArrayForTables() {
		return foodForTable;
		
	}
}
