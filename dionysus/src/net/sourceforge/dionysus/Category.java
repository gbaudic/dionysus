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

package net.sourceforge.dionysus;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * A category to sort articles
 * An article can belong to several categories at the same time (useful for discounts)
 */
public class Category implements Serializable {

	private static final long serialVersionUID = -4335349707097715750L;
	
	private String name; //Name of the category
	private String imagePath; //Absolute path to an image
	private ArrayList<Article> articlesList;
	
	/**
	 * @param name name of the category
	 * @param imagePath 
	 * @param articlesList
	 */
	public Category(String name, String imagePath, ArrayList<Article> articlesList) {
		this.name = name;
		this.imagePath = imagePath;
		this.articlesList = articlesList;
	}
	
	
	/**
	 * @param name
	 * @param imagePath
	 */
	public Category(String name, String imagePath) {
		this.name = name;
		this.imagePath = imagePath;
		articlesList = new ArrayList<Article>();
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}
	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		if(imagePath != null && !imagePath.isEmpty()){
			this.imagePath = imagePath;
		}
		
	}
	/**
	 * @return the articlesList
	 */
	public ArrayList<Article> getArticlesList() {
		return articlesList;
	}
	
	public void addArticle(Article a){
		if(a != null){
			articlesList.add(a);
		}
		
	}
}
