package net.sourceforge.dionysus.gui.panels;

import java.util.regex.PatternSyntaxException;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import net.sourceforge.dionysus.User;
import net.sourceforge.dionysus.db.UserDB;
import net.sourceforge.dionysus.gui.UserTableModel;

/**
 * Class to host the users panel, with add/modify/remove/credit buttons
 *
 */
public class UsersPanel extends JPanel {

	private static final long serialVersionUID = 2128311951562983724L;
	private TableRowSorter<UserTableModel> userSorter;
	private JTextField userRechercheField;
	
	private User currentUser;
	
	private UserDB users;
	
	/**
	 * 
	 */
	public UsersPanel(UserDB users) {
		super();
		
		this.users = users;
		
		
	}
	
	
	private void newUserFilter() {
        RowFilter<UserTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
        	rf = RowFilter.regexFilter("(?i)(?u)" + userRechercheField.getText());         
        } catch (PatternSyntaxException e) {
        	return;
        } catch (NullPointerException e) {
        	return;
        }
        userSorter.setRowFilter(rf);
    }
}
