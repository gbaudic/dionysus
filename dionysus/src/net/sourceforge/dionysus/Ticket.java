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

package net.sourceforge.dionysus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;

import net.sourceforge.dionysus.db.*;

/**
 * Class representing a ticket, structure to represent purchase of different products at various prices at a single time
 */
public class Ticket {

	private ArrayList<TicketItem> items;
	private User customer; /**customer buying, null if not a registered user*/
	private double amount;
	private PaymentMethod pMethod; //Only one payment method per ticket
	private Vendor vendor;
	
	public Ticket(User u) {
		items = new ArrayList<TicketItem>();
		customer = u;
	}
	
	public void addArticle(TicketItem newItem) {
		//Check that article at this price does not exist yet
		//Otherwise add to existing Item
		for(TicketItem item : items) {
			if(item != null) {
				if(item.getArticle() == newItem.getArticle() && item.getFee() == newItem.getFee()){ //Check if comparison works
					item.addArticles(newItem.getQuantity());
					updateAmount();
					return;
				}
			}
		}
		
		items.add(newItem);
		
		updateAmount();
	}
	
	/**
	 * Removes an article at a given price in a given quantity
	 * @param a the corresponding TicketItem
	 */
	public void removeArticle(TicketItem a)
	{
		//Check that article exists
		for(TicketItem ti : items)
		{
			if(ti != null)
			{
				if(ti.getArticle().getName() == a.getArticle().getName() && ti.getFee() == a.getFee()){
					int remainder = ti.getQuantity() - a.getQuantity();
					if(remainder == 0){
						//If quantity drops to zero, delete article
						items.remove(ti);
					} else {
						if(remainder > 0){
							ti.removeArticles(a.getQuantity());
						} else {
							//TODO: exception if we remove more than we have
						}
					}
					return;
				}
			}
		}	
		
		updateAmount();
	}
	
	/**
	 * Recompute the total for the ticket
	 */
	public void updateAmount() {
		amount = 0;
		
		for(TicketItem ti : items) {
			if(ti != null) {
				amount += ti.getAmount();
			}
		}
	}
	
	/**
	 * 
	 * @return the ticket grand total
	 */
	public double getAmount() {
		return amount;
	}
	
	public double getBalanceAfterTicket() {
		if(customer != null){
			return customer.getBalance() - amount;
		} else {
			return 0.00;
		}
	}
	
	/**
	 * Prints ticket info to GUI components
	 * FIXME: THAT SHOULD NOT BE DONE BY THIS CLASS!!!!!
	 * @param target display TextArea
	 * @param total Label for the total
	 * @param after Label for balance after transaction 
	 */
	public void printTicketToScreen(JTextArea target, JLabel total, JLabel after) {
		target.setText(null);
		for(TicketItem ti : items) {
			if(ti != null){
				//Add to GUI component the lines for each article
				target.setText(target.getText() + ti.toString() + "\n");
			}
		}
		
		//Same for total amount
		total.setText(String.valueOf(getAmount())+ " �");
		
		//Same for balance after purchase
		if(customer != null){
			after.setText(String.valueOf(getBalanceAfterTicket()) + " �");
		} else {
			after.setText("--,-- �");
		}
	}
	
	/**
	 * Exports the ticket to a string
	 * The submit() method needs to have been called previously to set the vendor
	 * TODO: this may need to be rewritten.
	 */
	public StringBuilder printTicketToText(String date){
		
		StringBuilder accu = new StringBuilder();
		
		if(date != null){
			accu.append( date + "\r\n");
		} else {
			accu.append(DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM).format(new Date()) + "\r\n");
		}
		
		accu.append("From "+customer.getNameWithPromo()+"\r\n");
		accu.append("Sold by "+vendor.getName()+"\r\n");
		
		for(TicketItem ti : items)
		{
			if(ti != null){
				accu.append("\t" + ti.toString() + "\r\n");
			}
		}
		
		accu.append("Total: "+String.valueOf(amount)+" �\r\nPaid by ");
		
		if(pMethod != null){
			accu.append(pMethod.getName());
		} else {
			accu.append("user account");
		}
		
		return accu.append("\r\n\r\n");
	}
	
	public void saveTicketToText(){
		//Output the date and time of the ticket in a human readable localized form
		String date = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM).format(new Date());
		
		File f = new File("tickets/"+date+".txt");
		FileWriter fw;
		
		try {
			fw = new FileWriter(f);
			
			fw.append(printTicketToText(date));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Setter for the payment method
	 * @param method the payment method used
	 */
	public void pay(PaymentMethod method){
		pMethod = method;
	}
	
	public PaymentMethod getPaymentMethod(){
		return pMethod;
	}
	
	public int getNumberOfItems(){
		return items.size();
	}
	
	/**
	 * Converts a ticket with its TicketItem to transactions for saving,
	 * and saves the transactions file
	 * The Ticket object will then be able to be deleted
	 * 
	 */
	public void submit(User destUser, TransactionDB tdb, Vendor v) {
		vendor = v;
		for(TicketItem item : items){
			if(item != null){
				Transaction t = new Transaction((int)(item.getAmount())*100, customer, destUser, item.getArticle(), item.getQuantity(), pMethod,v);
				tdb.add(t);
				Article solde = item.getArticle();
				if(solde.hasStockMgmtEnabled())
					solde.setStock(solde.getStock() - item.getQuantity());
				
				solde.use();			
			}
		}
		
		//Debit customer account (sourceUser) and
		if(customer != null && pMethod == null){
			customer.debite(amount);
		}
		//Credit customer account (destUser) ??
		if(destUser != null && pMethod == null){
			destUser.credite(amount);
		}
	}
	
}
