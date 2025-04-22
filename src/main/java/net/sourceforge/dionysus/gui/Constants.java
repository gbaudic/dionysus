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
package net.sourceforge.dionysus.gui;

import javax.swing.ImageIcon;

/**
 * Some constants for use across the software
 */
public final class Constants {

	/** Software name */
	public static final String SOFTWARE_NAME = "Dionysus";
	/** Version number as a string */
	public static final String SOFTWARE_VERSION = "0.4";
	/** Version nickname */
	public static final String SOFTWARE_VERSION_NICK = "\"Riesling\"";

	// IMAGES
	public static final ImageIcon plus = new ImageIcon(Constants.class.getResource("/list-add.png"));
	public static final ImageIcon minus = new ImageIcon(Constants.class.getResource("/list-remove.png"));
	public static final ImageIcon edit = new ImageIcon(Constants.class.getResource("/gtk-edit.png"));
	public static final ImageIcon ok = new ImageIcon(Constants.class.getResource("/gtk-ok.png"));
	public static final ImageIcon cancel = new ImageIcon(Constants.class.getResource("/gtk-cancel.png"));
	public static final ImageIcon exit = new ImageIcon(Constants.class.getResource("/application-exit.png"));
	public static final ImageIcon convert = new ImageIcon(Constants.class.getResource("/gtk-convert.png"));
	public static final ImageIcon about = new ImageIcon(Constants.class.getResource("/dialog-information.png"));

	/**
	 * Private constructor
	 */
	private Constants() {
	}

}
