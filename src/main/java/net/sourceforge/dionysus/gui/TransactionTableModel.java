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

import java.util.Date;

import javax.swing.table.DefaultTableModel;

public class TransactionTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 7595571828972009182L;
	private static String [] colNames =  new String[] {
		"Date", "Article", "Quantity", "Amount", "Source account", "Dest. account", "Paid by", "Vendor"
	};

	
	public TransactionTableModel(Object[][] data){
		super(data,colNames);
	}
	
	
	Class[] columnTypes = new Class[] {
			Date.class, String.class, Integer.class, String.class, String.class, String.class, String.class, String.class
	};
	public Class getColumnClass(int columnIndex) {
		return columnTypes[columnIndex];
	}
	boolean[] columnEditables = new boolean[] {
			false, false, false, false, false, false, false, false
	};
	public boolean isCellEditable(int row, int column) {
		return columnEditables[column];
	}
	
	public void refreshData(Object[][] newData){
		setDataVector(newData, colNames);
	}
}
