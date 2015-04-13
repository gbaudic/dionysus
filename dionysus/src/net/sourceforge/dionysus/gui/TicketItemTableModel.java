/* 	Dionysus, a student pub management software in Java
    Copyright (C) 2011,2013,2015  podgy_piglet

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

import javax.swing.table.DefaultTableModel;

/**
 * Table model for the redesigned ticket view
 *
 */
public class TicketItemTableModel extends DefaultTableModel {
	private static final long serialVersionUID = -2023864553780594289L;
	
	public TicketItemTableModel(Object[][] data){
		super(data, new String[] {
				"Name", "Quantity", "Price"
			});
	}
	
	Class[] columnTypes = new Class[] {
			String.class, Integer.class, Double.class
	};
	public Class getColumnClass(int columnIndex) {
		return columnTypes[columnIndex];
	}
	boolean[] columnEditables = new boolean[] {
			false, false, false
	};
	public boolean isCellEditable(int row, int column) {
		return columnEditables[column];
	}
}
