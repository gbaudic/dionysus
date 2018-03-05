/* 	Dionysus, a student pub management software in Java
    Copyright (C) 2011,2013,2015-2018  G. Baudic

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

/**
 * State enumeration to denote the current status of the software in Cash desk view
 */
public enum TicketState {
    IDLE,           // No user selected, no ticket
    TICKET_IDLE,    // Ticket being opened, waiting for new article or payment
    PAYMENT,        // Ticket finished and being paid
    EDIT_ITEM,      // Modification of a previously entered ticket item
    QUANTITY,       // Article selected
    PRICE;          // Article and quantity selected
}

