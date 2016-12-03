/* 	Dionysus, a student pub management software in Java
    Copyright (C) 2011,2013,2015,2016  G. Baudic

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

import java.util.ArrayList;
import java.text.NumberFormat;

import net.sourceforge.dionysus.*;

public class TransactionDB extends Database<Transaction> {
	
	//Not necessarily in a text file
	
	public TransactionDB(){
		data = new ArrayList<Transaction>(20);
		numberOfRecords = 0;
	}

	public void makeArrayForTables() {
		foodForTable = new Object[numberOfRecords][8];
		
		for(int i = 0 ; i < numberOfRecords ; i++){
			foodForTable[i][0] = data.get(i).getDate();
			foodForTable[i][1] = data.get(i).getArticle().getName();
			foodForTable[i][2] = new Integer(data.get(i).getNumberOfItems());
			foodForTable[i][3] = NumberFormat.getCurrencyInstance().format(data.get(i).getAmount().getPrice()); //do not try to understand...
			
			if(data.get(i).getSourceUser() != null){
				foodForTable[i][4] = data.get(i).getSourceUser().getNameWithPromo();
			} else {
				foodForTable[i][4] = "none";
			}
			
			if(data.get(i).getDestUser() != null){
				foodForTable[i][5] = data.get(i).getDestUser().getNameWithPromo();
			} else {
				foodForTable[i][5] = "none";
			}
			
			if(data.get(i).getPaymentMethod() != null){
				foodForTable[i][6] = data.get(i).getPaymentMethod().getName();
			} else {
				foodForTable[i][6] = "Account";
			}
			
			if(data.get(i).getVendor() != null){
				foodForTable[i][7] = data.get(i).getVendor().getName();
			} else {
				foodForTable[i][7] = "null";
			}
			
		}	
	}

	@Override
	public void modify(Transaction t, int index) {
		// Transactions cannot be modified
		
	}

	@Override
	public void remove(Transaction t) {
		// Transactions cannot be removed, only reverted
		
	}

	@Override
	public Transaction[] getArray() {
		return (Transaction []) data.toArray(new Transaction[numberOfRecords]);
	}

	
}
