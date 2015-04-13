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
	private User customer;
	private double amount;
	private PaymentMethod pMethod; //Only one payment method per ticket
	
	public Ticket(User u)
	{
		items = new ArrayList<TicketItem>();
		customer = u;
	}
	
	public void addArticle(TicketItem newItem)
	{
		//Check that article at this price does not exist yet
		//Otherwise add to existing Item
		for(int i = 0 ; i < items.size() ; i++)
		{
			if(items.get(i) != null)
			{
				if(items.get(i).getArticle() == newItem.getArticle() && items.get(i).getFee() == newItem.getFee()){ //Check if comparison works
					items.get(i).addArticles(newItem.getQuantity());
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
		for(int i = 0 ; i < items.size() ; i++)
		{
			if(items.get(i) != null)
			{
				if(items.get(i).getArticle().getName() == a.getArticle().getName() && items.get(i).getFee() == a.getFee()){
					int remainder = items.get(i).getQuantity() - a.getQuantity();
					if(remainder == 0){
						//If quantity drops to zero, delete article
						items.set(i,null);
					} else {
						if(remainder > 0){
							items.get(i).removeArticles(a.getQuantity());
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
	
	public void updateAmount()
	{
		amount = 0;
		
		for(int i = 0 ; i < items.size() ; i++)
		{
			if(items.get(i) != null)
			{
				amount += items.get(i).getAmount();
			}
		}
	}
	
	/**
	 * 
	 * @return the ticket grand total in EUROS
	 */
	public double getAmount()
	{
		return amount;
	}
	
	public double getBalanceAfterTicket()
	{
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
	 * @param apres Label balance after transaction 
	 */
	public void printTicketToScreen(JTextArea target, JLabel total, JLabel apres)
	{
		target.setText(null);
		for(int i=0 ; i < items.size() ; i++)
		{
			if(items.get(i) != null){
				//Add to GUI component the lines for each article
				target.setText(target.getText() + items.get(i).toString() + "\n");
			}
		}
		
		//Same for total amount
		total.setText("Total ticket: "+String.valueOf(getAmount())+ " €");
		
		//Same for balance after purchase
		if(customer != null){
			apres.setText("Balance after ticket: "+String.valueOf(getBalanceAfterTicket()) + " €");
		} else {
			apres.setText("Balance after ticket: --,-- €");
		}
	}
	
	/**
	 * Exports the ticket to a string
	 * TODO: this may need to be rewritten.
	 */
	public StringBuilder printTicketToText(String date){
		
		StringBuilder accu = new StringBuilder();
		
		if(date != null){
			accu.append( date + "\r\n");
		} else {
			accu.append(DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM).format(new Date()) + "\r\n");
		}
		
		accu.append("From "+customer.getFullName()+"\r\n");
		
		for(int i=0 ; i < items.size() ; i++)
		{
			if(items.get(i) != null){
				accu.append("\t" + items.get(i).toString() + "\r\n");
			}
		}
		
		accu.append("Total: "+String.valueOf(amount)+" €\r\nPaid by ");
		
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
	 * Converts a ticket with its TicketItem to transactions for saving
	 * The Ticket object will then be able to be deleted
	 * 
	 */
	public void submit(User destUser, TransactionDB tdb)
	{
		for(int i = 0 ; i < items.size() ; i++)
		{
			if(items.get(i) != null){
				Transaction t = new Transaction((int)(items.get(i).getAmount())*100, customer, destUser, items.get(i).getArticle(), items.get(i).getQuantity(), pMethod);
				tdb.add(t);
				Article solde = items.get(i).getArticle();
				if(solde.hasStockMgmtEnabled())
					solde.setStock(solde.getStock() - items.get(i).getQuantity());
				
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
