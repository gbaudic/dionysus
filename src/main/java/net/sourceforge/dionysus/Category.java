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

import java.awt.Color;
import java.io.Serializable;
import java.util.HashSet;

/**
 * A category to sort articles
 *
 * An article can belong to several categories at the same time (useful for
 * discounts)
 */
public class Category implements Serializable {

	/** UID for serialization */
	private static final long serialVersionUID = -4335349707097715750L;

	/** Name of the category */
	private String name;

	/** Absolute path to an image */
	private String imagePath;

	/** List of articles belonging to this category */
	private HashSet<Article> articlesList;

	/** Background color of button for this category */
	private Color color;

	/**
	 * Constructor
	 *
	 * @param name      name of the category
	 * @param imagePath full path to the image to be used
	 */
	public Category(String name, String imagePath) {
		this(name, imagePath, new HashSet<Article>());
	}

	/**
	 * Constructor
	 *
	 * @param name         name of the category
	 * @param imagePath    full path to the image to be used
	 * @param articlesList list of articles to include
	 */
	public Category(String name, String imagePath, HashSet<Article> articlesList) {
		this(name, imagePath, articlesList, null);
	}

	/**
	 * Constructor
	 *
	 * @param name         name of the category
	 * @param imagePath    full path to the image to be used
	 * @param articlesList list of articles to include
	 * @param color        color to use
	 */
	public Category(String name, String imagePath, HashSet<Article> articlesList, Color color) {
		this.name = name;
		this.imagePath = imagePath;
		this.articlesList = articlesList;
		this.color = color;
	}

	/**
	 * Adder method
	 *
	 * @param a article to add to this category
	 */
	public void addArticle(Article a) {
		if (a != null) {
			articlesList.add(a);
		}
	}

	/**
	 * Remove all articles from this category
	 *
	 * Useful for deletion
	 */
	public void clear() {
		articlesList.clear();
	}

	/**
	 * @return the articlesList
	 */
	public HashSet<Article> getArticlesList() {
		return articlesList;
	}

	/**
	 * @return the background color for buttons
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Remove an article from the category
	 *
	 * @param a article to remove
	 */
	public void removeArticle(Article a) {
		articlesList.remove(a);
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		if (imagePath != null && !imagePath.isEmpty()) {
			this.imagePath = imagePath;
		}
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
