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

package fr.dionysus.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import javax.swing.JTextArea;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.SwingConstants;

import fr.dionysus.*;
import fr.dionysus.database.*;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.JSeparator;

public class MainGUI2 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField userRechercheField;
	private JTable userTable;
	private JTextField textField;
	private JTable table_1;
	private JTextField textField_1;
	private JTable table_2;
	private JTextField saisieField;
	private JLabel nomLabel;
	private JLabel soldeLabel;
	private JTextArea ticketTextArea; 
	private JLabel taskToDoLabel;
	private JLabel enCours;
	private JTextArea alertTextArea;
	
	public static String SOFTWARE_NAME = "Dionysus";
	public static String SOFTWARE_VERSION = "0.1 \"Clairette\"";
	
	private Ticket currentTicket;
	private TicketItem currentItemAtDesk;
	private User currentUserAtDesk;
	private Article currentArticleAtDesk;
	
	private User currentUser;
	private Article currentArticle;
	private Transaction currentTransaction;
	
	private UserDB users;
	private ArticleDB catalogue;
	private TransactionDB journal;
	
	private JLabel lblTotalTicket;
	private JLabel lblSoldeApres;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					MainGUI2 frame = new MainGUI2();
//					frame.setVisible(true);
					
					PasswordDialog pdiag = new PasswordDialog();
					pdiag.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainGUI2() {
		fillTheDB();
		
		setResizable(false);
		setTitle(SOFTWARE_NAME + " v"+ SOFTWARE_VERSION);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 1024, 768);
		setSize(1024,740);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmSettings = new JMenuItem("Settings");
		mnFile.add(mntmSettings);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mnFile.add(mntmQuit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		contentPane.add(tabbedPane, gbc_tabbedPane);
		
		JPanel vueP = new JPanel();
		tabbedPane.addTab("Cash desk", null, vueP, null);
		GridBagLayout gbl_vueP = new GridBagLayout();
		gbl_vueP.columnWidths = new int[]{0, 0, 0, 0};
		gbl_vueP.rowHeights = new int[]{0, 0, 0, 0};
		gbl_vueP.columnWeights = new double[]{0.6, 0.2, 0.2, Double.MIN_VALUE};
		gbl_vueP.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		vueP.setLayout(gbl_vueP);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Current user", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		vueP.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblN = new JLabel("Name: ");
		GridBagConstraints gbc_lblN = new GridBagConstraints();
		gbc_lblN.anchor = GridBagConstraints.LINE_END;
		gbc_lblN.insets = new Insets(0, 0, 5, 5);
		gbc_lblN.gridx = 0;
		gbc_lblN.gridy = 0;
		panel.add(lblN, gbc_lblN);
		
		nomLabel = new JLabel("no user selected");
		nomLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_nomLabel = new GridBagConstraints();
		gbc_nomLabel.insets = new Insets(0, 0, 5, 5);
		gbc_nomLabel.gridx = 1;
		gbc_nomLabel.gridy = 0;
		panel.add(nomLabel, gbc_nomLabel);
		
		JButton chooser = new JButton("...");
		chooser.setToolTipText("Select user");
		GridBagConstraints gbc_chooser = new GridBagConstraints();
		gbc_chooser.insets = new Insets(0, 0, 5, 0);
		gbc_chooser.gridx = 3;
		gbc_chooser.gridy = 0;
		chooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				UserChoiceDialog2 choices = new UserChoiceDialog2(users);
				choices.setVisible(true);
				User nextCUser = choices.getUser();
				currentUserAtDesk = nextCUser;
				
				if(currentUserAtDesk != null){
					nomLabel.setText(currentUserAtDesk.getFullName());
					soldeLabel.setText(String.valueOf(currentUserAtDesk.getBalance()));
				} else {
					nomLabel.setText("default (00)"); 
					soldeLabel.setText(String.valueOf(0.00));
				}
				
				currentTicket = new Ticket(currentUserAtDesk);
				taskToDoLabel.setText("Choose article");
			}
		});
		panel.add(chooser, gbc_chooser);
		
		JLabel lblSolde = new JLabel("Balance:");
		GridBagConstraints gbc_lblSolde = new GridBagConstraints();
		gbc_lblSolde.anchor = GridBagConstraints.LINE_END;
		gbc_lblSolde.insets = new Insets(0, 0, 0, 5);
		gbc_lblSolde.gridx = 0;
		gbc_lblSolde.gridy = 1;
		panel.add(lblSolde, gbc_lblSolde);
		
		soldeLabel = new JLabel("-.--");
		soldeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_soldeLabel = new GridBagConstraints();
		gbc_soldeLabel.insets = new Insets(0, 0, 0, 5);
		gbc_soldeLabel.gridx = 2;
		gbc_soldeLabel.gridy = 1;
		panel.add(soldeLabel, gbc_soldeLabel);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 0;
		vueP.add(panel_2, gbc_panel_2);
		panel_2.setLayout(new GridLayout(3, 0, 0, 0));
		
		taskToDoLabel = new JLabel("Welcome!");
		taskToDoLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		taskToDoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(taskToDoLabel);
		
		enCours = new JLabel("");
		enCours.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(enCours);
		
		saisieField = new JTextField();
		saisieField.setHorizontalAlignment(SwingConstants.RIGHT);
		saisieField.setFont(new Font("Tahoma", Font.BOLD, 14));
		saisieField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					if(saisieField.getText().length() > 0){
						//Validate the content in the TextField
						int saisie = Integer.parseInt(saisieField.getText());
						saisieField.setText(null);

						if(currentItemAtDesk == null){
							//New TicketItem
							currentArticleAtDesk = catalogue.getArticleByCode(saisie);
							if(currentArticleAtDesk != null){
								currentItemAtDesk = new TicketItem(currentArticleAtDesk, -1, 0);

								if(currentArticleAtDesk.getNumberOfPrices() > 1){
									taskToDoLabel.setText("Select fee");
								} else {
									currentItemAtDesk.setFee(0);
									taskToDoLabel.setText("Select quantity");
								}
								enCours.setText("? x "+currentArticleAtDesk.getName());
								return;
							} else {
								JOptionPane.showMessageDialog(null,"Unknown article!", "Error", JOptionPane.WARNING_MESSAGE);
								return;
							}
						} else {
							//Complete the current TicketItem
							if(currentItemAtDesk.getFee() == -1){
								//Fee not entered
								if(saisie < 0 || saisie >= currentArticleAtDesk.getNumberOfPrices()){
									JOptionPane.showMessageDialog(null,"Invalid fee for this article!", "Error", JOptionPane.WARNING_MESSAGE);
									return;
								} else {
									currentItemAtDesk.setFee(saisie);
									taskToDoLabel.setText("Select quantity");
									return;
								}
							} else {
								//Quantity not entered
								if(saisie <= 0){
									JOptionPane.showMessageDialog(null,"Invalid quantity!", "Error", JOptionPane.WARNING_MESSAGE);
									return;
								} else {
									currentItemAtDesk.setQuantity(saisie);
									currentItemAtDesk.computeAmount();
									currentTicket.addArticle(currentItemAtDesk);
									enCours.setText(String.valueOf(saisie)+" x "+currentArticleAtDesk.getName());
									currentItemAtDesk = null;
									currentArticleAtDesk = null;
									currentTicket.printTicketToScreen(ticketTextArea, lblTotalTicket, lblSoldeApres);
									taskToDoLabel.setText("Other article or finish");
									return;
								}
							}
						}
					} else {
						//Validations without content in the TextField
						if(currentTicket.getNumberOfItems() >= 1){
							if(currentTicket.getPaymentMethod() == null){
								if(currentUserAtDesk == null){
									taskToDoLabel.setText("Choose payment method");
									return;
								} else {
									//Save ticket
									finalizeTicket();
									
									return;
								}
							} else {
								if(currentTicket.getPaymentMethod() == PaymentMethod.CASH){
									double paye = Double.parseDouble(JOptionPane.showInputDialog(null, "Change given: ", "Change", JOptionPane.QUESTION_MESSAGE));
									JOptionPane.showMessageDialog(null, "You owe "+String.valueOf(paye - currentTicket.getAmount())+"€.","", JOptionPane.INFORMATION_MESSAGE);
								}
								//Save the finished ticket
								finalizeTicket();
								return;
							}
							
						}
					}


				} catch (NumberFormatException nfe){
					nfe.printStackTrace();
				}
			}
		});
		panel_2.add(saisieField);
		saisieField.setColumns(10);
		
		JPanel panel_articles = new JPanel();
		GridBagConstraints gbc_panel_articles = new GridBagConstraints();
		gbc_panel_articles.gridheight = 2;
		gbc_panel_articles.insets = new Insets(0, 0, 5, 5);
		gbc_panel_articles.fill = GridBagConstraints.BOTH;
		gbc_panel_articles.gridx = 0;
		gbc_panel_articles.gridy = 1;
		vueP.add(panel_articles, gbc_panel_articles);
		panel_articles.setLayout(new GridLayout(5, 6, 0, 0));
		
		Article [] cat = (Article[]) catalogue.getArray();
		if(cat != null){
			for(final Article c : cat){
				if(c != null && c.isActive()){
					JButton btn = new JButton(c.getName());
					String ttt = "<html>"+ c.getName() + " ("+ String.valueOf(c.getCode()) + ")";
					for(int i = 0 ; i < c.getNumberOfPrices() ; i++){
						ttt += "<br/>Price "+String.valueOf(i)+": "+String.valueOf(c.getArticlePrice(i));
					}
					ttt += "</html>";
					btn.setToolTipText(ttt);
					btn.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							if(currentItemAtDesk == null){
								currentArticleAtDesk = c;
								currentItemAtDesk = new TicketItem(c, -1, 0);
								enCours.setText("? x "+c.getName());
								
								if(c.getNumberOfPrices() > 1){
									taskToDoLabel.setText("Choose fee");
								} else {
									taskToDoLabel.setText("Choose quantity");
									currentItemAtDesk.setFee(0);
								}
							}
						}
					});
					
					panel_articles.add(btn);
				}
			}
		}
		
		ticketTextArea = new JTextArea();
		ticketTextArea.setEditable(false);
		GridBagConstraints gbc_ticketTextArea = new GridBagConstraints();
		gbc_ticketTextArea.insets = new Insets(0, 0, 5, 5);
		gbc_ticketTextArea.fill = GridBagConstraints.BOTH;
		gbc_ticketTextArea.gridx = 2;
		gbc_ticketTextArea.gridy = 0;
		vueP.add(ticketTextArea, gbc_ticketTextArea);
		
		JPanel panel_pavenum = new JPanel();
		GridBagConstraints gbc_panel_pavenum = new GridBagConstraints();
		gbc_panel_pavenum.gridheight = 2;
		gbc_panel_pavenum.insets = new Insets(0, 0, 0, 5);
		gbc_panel_pavenum.fill = GridBagConstraints.BOTH;
		gbc_panel_pavenum.gridx = 1;
		gbc_panel_pavenum.gridy = 1;
		vueP.add(panel_pavenum, gbc_panel_pavenum);
		panel_pavenum.setLayout(new GridLayout(4, 3, 0, 0));
		
		JButton btn1 = new JButton("1");
		btn1.setMnemonic('1');
		btn1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saisieField.setText(saisieField.getText()+String.valueOf(1));
			}
		});
		panel_pavenum.add(btn1);
		
		JButton btn2 = new JButton("2");
		btn2.setMnemonic('2');
		btn2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saisieField.setText(saisieField.getText()+String.valueOf(2));
			}
		});
		panel_pavenum.add(btn2);
		
		JButton btn3 = new JButton("3");
		btn3.setMnemonic('3');
		btn3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saisieField.setText(saisieField.getText()+String.valueOf(3));
			}
		});
		panel_pavenum.add(btn3);
		
		JButton btn4 = new JButton("4");
		btn4.setMnemonic('4');
		btn4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saisieField.setText(saisieField.getText()+String.valueOf(4));
			}
		});
		panel_pavenum.add(btn4);
		
		JButton btn5 = new JButton("5");
		btn5.setMnemonic('5');
		btn5.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saisieField.setText(saisieField.getText()+String.valueOf(5));
			}
		});
		panel_pavenum.add(btn5);
		
		JButton btn6 = new JButton("6");
		btn6.setMnemonic('6');
		btn6.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saisieField.setText(saisieField.getText()+String.valueOf(6));
			}
		});
		panel_pavenum.add(btn6);
		
		JButton btn7 = new JButton("7");
		btn7.setMnemonic('7');
		btn7.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saisieField.setText(saisieField.getText()+String.valueOf(7));
			}
		});
		panel_pavenum.add(btn7);
		
		JButton btn8 = new JButton("8");
		btn8.setMnemonic('8');
		btn8.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saisieField.setText(saisieField.getText()+String.valueOf(8));
			}
		});
		panel_pavenum.add(btn8);
		
		JButton btn9 = new JButton("9");
		btn9.setMnemonic('9');
		btn9.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saisieField.setText(saisieField.getText()+String.valueOf(9));
			}
		});
		panel_pavenum.add(btn9);
		
		JButton btn0 = new JButton("0");
		btn0.setMnemonic('0');
		btn0.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saisieField.setText(saisieField.getText()+String.valueOf(0));
			}
		});
		panel_pavenum.add(btn0);
		
		JButton btnMetre = new JButton("Meter");
		btnMetre.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saisieField.setText(String.valueOf(12));
			}
		});
		panel_pavenum.add(btnMetre);
		
		JButton btnClear = new JButton("C");
		btnClear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saisieField.setText("");
			}
		});
		panel_pavenum.add(btnClear);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Total", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 5);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 2;
		gbc_panel_3.gridy = 1;
		vueP.add(panel_3, gbc_panel_3);
		panel_3.setLayout(new GridLayout(2, 0, 0, 0));
		
		lblTotalTicket = new JLabel("Ticket total: ");
		lblTotalTicket.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_3.add(lblTotalTicket);
		
		lblSoldeApres = new JLabel("Balance after ticket:");
		panel_3.add(lblSoldeApres);
		
		JPanel panel_PaymentMethods = new JPanel();
		panel_PaymentMethods.setBorder(new TitledBorder(null, "Payment method", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_PaymentMethods = new GridBagConstraints();
		gbc_panel_PaymentMethods.insets = new Insets(0, 0, 0, 5);
		gbc_panel_PaymentMethods.fill = GridBagConstraints.BOTH;
		gbc_panel_PaymentMethods.gridx = 2;
		gbc_panel_PaymentMethods.gridy = 2;
		vueP.add(panel_PaymentMethods, gbc_panel_PaymentMethods);
		panel_PaymentMethods.setLayout(new GridLayout(2, 3, 0, 0));
		
		for(final PaymentMethod p : PaymentMethod.values()){
			JButton btn = new JButton(p.getName());
			btn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(currentTicket != null){
						if(currentTicket.getPaymentMethod() == null){
							currentTicket.pay(p);
							
							if(currentTicket.getPaymentMethod() == PaymentMethod.CASH){
								double paye = Double.parseDouble(JOptionPane.showInputDialog(null, "Change given: ", "Change", JOptionPane.QUESTION_MESSAGE));
								JOptionPane.showMessageDialog(null, "You owe "+String.valueOf(paye - currentTicket.getAmount())+"€.","", JOptionPane.INFORMATION_MESSAGE);
							}
							//Save the finished ticket
							finalizeTicket();
							return;
						}
					}
				}
			});
			
			panel_PaymentMethods.add(btn);
		}
		
		JPanel comptesP = new JPanel();
		tabbedPane.addTab("Accounts", null, comptesP, null);
		GridBagLayout gbl_comptesP = new GridBagLayout();
		gbl_comptesP.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_comptesP.rowHeights = new int[]{0, 0, 0};
		gbl_comptesP.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 1.0};
		gbl_comptesP.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		comptesP.setLayout(gbl_comptesP);
		
		JLabel lblNewLabel = new JLabel("Search: ");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		comptesP.add(lblNewLabel, gbc_lblNewLabel);
		
		userRechercheField = new JTextField();
		GridBagConstraints gbc_userRechercheField = new GridBagConstraints();
		gbc_userRechercheField.insets = new Insets(0, 0, 5, 5);
		gbc_userRechercheField.fill = GridBagConstraints.HORIZONTAL;
		gbc_userRechercheField.gridx = 1;
		gbc_userRechercheField.gridy = 0;
		comptesP.add(userRechercheField, gbc_userRechercheField);
		userRechercheField.setColumns(10);
		
		ImageIcon plus = new ImageIcon("images/list-add.png");
		ImageIcon minus = new ImageIcon("images/list-remove.png");
		ImageIcon edit = new ImageIcon("images/gtk-edit.png");
		ImageIcon cancel = new ImageIcon("images/gtk-cancel.png");
		
		JButton btnAddUser = new JButton("Add", plus);
		GridBagConstraints gbc_btnAddUser = new GridBagConstraints();
		gbc_btnAddUser.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddUser.gridx = 2;
		gbc_btnAddUser.gridy = 0;
		btnAddUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewUserDialog2 newUser = new NewUserDialog2();
				newUser.setVisible(true);
				User nu = newUser.getUser();
				users.addUser(nu);
			}
		});
		comptesP.add(btnAddUser, gbc_btnAddUser);
		
		JButton btnEditUser = new JButton("Edit", edit);
		GridBagConstraints gbc_btnEditUser = new GridBagConstraints();
		gbc_btnEditUser.insets = new Insets(0, 0, 5, 5);
		gbc_btnEditUser.gridx = 3;
		gbc_btnEditUser.gridy = 0;
		btnEditUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = userTable.getSelectedRow();
				int realRow = -1;
				
				if(row >= 0){
					realRow = userTable.convertRowIndexToModel(userTable.getSelectedRow());
					currentUser = (User)users.getArray()[realRow];
					
					if(currentUser != null){
						NewUserDialog2 mud = new NewUserDialog2(currentUser);
						mud.setVisible(true);
						currentUser = mud.getUser();
						users.modifyUser(currentUser, realRow);
					} else {
						JOptionPane.showMessageDialog(null, "No user selected!", "Error", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		comptesP.add(btnEditUser, gbc_btnEditUser);
		
		JButton btnDeleteUser = new JButton("Delete", minus);
		GridBagConstraints gbc_btnDeleteUser = new GridBagConstraints();
		gbc_btnDeleteUser.anchor = GridBagConstraints.EAST;
		gbc_btnDeleteUser.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteUser.gridx = 4;
		gbc_btnDeleteUser.gridy = 0;
		btnDeleteUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = userTable.getSelectedRow();
				int realRow = -1;
				
				if(row >= 0){
					realRow = userTable.convertRowIndexToModel(userTable.getSelectedRow());
					currentUser = (User)users.getArray()[realRow];

					if(currentUser != null){
						int choice = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this account?","",JOptionPane.YES_NO_OPTION);
						if(choice == JOptionPane.YES_OPTION){
							users.removeUser(currentUser);
							//Keep original index to modify without adding
						}
					} else {
						JOptionPane.showMessageDialog(null, "No user selected!", "Error", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		comptesP.add(btnDeleteUser, gbc_btnDeleteUser);
		
		userTable = new JTable();
		userTable.setModel(new UserTableModel(users.getArrayForTables()));
		userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userTable.setFillsViewportHeight(true);
		userTable.setAutoCreateRowSorter(true);
		
		JScrollPane scrollPane = new JScrollPane(userTable);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		//gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridwidth = 8;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		comptesP.add(scrollPane, gbc_scrollPane);
		
		/*GridBagConstraints gbc_userTable = new GridBagConstraints();
		gbc_userTable.gridwidth = 8;
		gbc_userTable.fill = GridBagConstraints.BOTH;
		gbc_userTable.gridx = 0;
		gbc_userTable.gridy = 1;
		comptesP.add(userTable, gbc_userTable);*/
		
		JPanel articlesP = new JPanel();
		tabbedPane.addTab("Articles", null, articlesP, null);
		GridBagLayout gbl_articlesP = new GridBagLayout();
		gbl_articlesP.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_articlesP.rowHeights = new int[]{0, 0, 0};
		gbl_articlesP.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_articlesP.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		articlesP.setLayout(gbl_articlesP);
		
		JLabel lblNewLabel_1 = new JLabel("Search:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		articlesP.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		articlesP.add(textField, gbc_textField);
		textField.setColumns(10);
		
		
		JButton btnAddArticle = new JButton("Add", plus);
		GridBagConstraints gbc_btnAddArticle = new GridBagConstraints();
		gbc_btnAddArticle.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddArticle.gridx = 2;
		gbc_btnAddArticle.gridy = 0;
		btnAddArticle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewArticleDialog nad = new NewArticleDialog();
				nad.setVisible(true);
				Article a = nad.getArticle();
				catalogue.addArticle(a);
			}
		});
		articlesP.add(btnAddArticle, gbc_btnAddArticle);
		
		JButton btnEditArticle = new JButton("Edit", edit);
		GridBagConstraints gbc_btnEditArticle = new GridBagConstraints();
		gbc_btnEditArticle.anchor = GridBagConstraints.NORTHEAST;
		gbc_btnEditArticle.insets = new Insets(0, 0, 5, 5);
		gbc_btnEditArticle.gridx = 3;
		gbc_btnEditArticle.gridy = 0;
		btnEditArticle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table_1.getSelectedRow();
				int realRow = -1;
				if(row >= 0){
					realRow = table_1.convertRowIndexToModel(table_1.getSelectedRow());
					currentArticle = (Article)catalogue.getArray()[realRow];
					
					if(currentArticle != null){
					NewArticleDialog nad = new NewArticleDialog();
					nad.setArticle(currentArticle);
					nad.setVisible(true);
					currentArticle = nad.getArticle();
					//Keep original index to modify without adding
					catalogue.modifyArticle(currentArticle, realRow);
					} 
				} else {
					JOptionPane.showMessageDialog(null, "No article selected!", "Error", JOptionPane.WARNING_MESSAGE);
				}
				updateStockAlerts();
			}
		});
		articlesP.add(btnEditArticle, gbc_btnEditArticle);
		
		JButton btnDeleteArticle = new JButton("Delete", minus);
		GridBagConstraints gbc_btnDeleteArticle = new GridBagConstraints();
		gbc_btnDeleteArticle.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeleteArticle.gridx = 4;
		gbc_btnDeleteArticle.gridy = 0;
		btnDeleteArticle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table_1.getSelectedRow();
				int realRow = -1;
				if(row >= 0){
					realRow = table_1.convertRowIndexToModel(table_1.getSelectedRow());
					currentArticle = (Article)catalogue.getArray()[realRow];

					if(currentArticle != null){
						int choice = JOptionPane.showConfirmDialog(null,"Are you sure to delete this article?","",JOptionPane.YES_NO_OPTION);
						if(choice == JOptionPane.YES_OPTION){
							catalogue.removeArticle(currentArticle);
						}
					} else {
						JOptionPane.showMessageDialog(null, "No article selected!", "Error", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		articlesP.add(btnDeleteArticle, gbc_btnDeleteArticle);
		
		table_1 = new JTable();
		table_1.setModel(new DefaultTableModel(
			catalogue.getArrayForTables(),
			new String[] {
				"Name", "Code", "Active", "Price 0", "Price 1", "Price 2", "Current stock", "Alert from"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class, Boolean.class, Double.class, Double.class, Double.class, String.class, String.class
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
		});
		table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_1.setFillsViewportHeight(true);
		table_1.setAutoCreateRowSorter(true);
		
		JScrollPane t1SP = new JScrollPane(table_1);
		
		GridBagConstraints gbc_table_1 = new GridBagConstraints();
		gbc_table_1.gridwidth = 8;
		gbc_table_1.fill = GridBagConstraints.BOTH;
		gbc_table_1.gridx = 0;
		gbc_table_1.gridy = 1;
		articlesP.add(t1SP, gbc_table_1);
		
		JPanel transactionsP = new JPanel();
		tabbedPane.addTab("Transactions", null, transactionsP, null);
		GridBagLayout gbl_transactionsP = new GridBagLayout();
		gbl_transactionsP.columnWidths = new int[]{0, 0, 0, 0};
		gbl_transactionsP.rowHeights = new int[]{0, 0, 0};
		gbl_transactionsP.columnWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_transactionsP.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		transactionsP.setLayout(gbl_transactionsP);
		
		JLabel lblNewLabel_2 = new JLabel("Search: ");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 0;
		transactionsP.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 0;
		transactionsP.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton_6 = new JButton("Cancel", cancel);
		GridBagConstraints gbc_btnNewButton_6 = new GridBagConstraints();
		gbc_btnNewButton_6.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_6.gridx = 2;
		gbc_btnNewButton_6.gridy = 0;
		btnNewButton_6.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table_2.getSelectedRow();
				int realRow = -1;
				if(row >= 0){
					realRow = table_2.convertRowIndexToModel(table_2.getSelectedRow());
					currentTransaction = (Transaction)journal.getArray()[realRow];
					
					if(currentTransaction != null){
						int choice = JOptionPane.showConfirmDialog(null,"Are you sure to delete this transaction?","",JOptionPane.YES_NO_OPTION);
						if(choice == JOptionPane.YES_OPTION){
							journal.addTransaction(new Transaction(currentTransaction));
							//TODO : complete cancellation of effects (restore balances, stocks...) if asked
							
							choice = JOptionPane.showConfirmDialog(null,"Do you also want to revert its consequences?","",JOptionPane.YES_NO_OPTION);
							if(choice == JOptionPane.YES_OPTION)
								currentTransaction.revert();
						}
					} else {
						JOptionPane.showMessageDialog(null, "No transaction selected!", "Error", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		transactionsP.add(btnNewButton_6, gbc_btnNewButton_6);
		
		table_2 = new JTable();
		table_2.setModel(new DefaultTableModel(
			journal.getArrayForTables(),
			new String[] {
				"Date", "Article", "Quantity", "Amount", "Source account", "Dest. account", "Paid by"
			}
		) {
			Class[] columnTypes = new Class[] {
				Date.class, String.class, Integer.class, Double.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_2.setFillsViewportHeight(true);
		table_2.setAutoCreateRowSorter(true);
		
		JScrollPane t2SP = new JScrollPane(table_2);
		
		GridBagConstraints gbc_table_2 = new GridBagConstraints();
		gbc_table_2.gridwidth = 6;
		gbc_table_2.fill = GridBagConstraints.BOTH;
		gbc_table_2.gridx = 0;
		gbc_table_2.gridy = 1;
		transactionsP.add(t2SP, gbc_table_2);
		
		alertTextArea = new JTextArea();
		alertTextArea.setColumns(8);
		alertTextArea.setRows(50);
		alertTextArea.setEditable(false);
		GridBagConstraints gbc_alertTextArea = new GridBagConstraints();
		gbc_alertTextArea.fill = GridBagConstraints.BOTH;
		gbc_alertTextArea.gridx = 0;
		gbc_alertTextArea.gridy = 1;
		contentPane.add(alertTextArea, gbc_alertTextArea);
		
		updateStockAlerts();
	}
	
	/**
	 * Initializes and fills the required databases
	 */
	private void fillTheDB(){
		//Initialize
		users = new UserDB();
		catalogue = new ArticleDB();
		journal = new TransactionDB();
		
		//Fill
		users.createFromTextFile("comptes.dat");
		catalogue.createFromTextFile("articles.dat");
		journal.createFromTextFile("journal.dat");
	}

	/**
	 * Refreshes the component containing the stock alerts
	 * TODO: partly rewrite for optimization
	 */
	public void updateStockAlerts(){
		alertTextArea.setText(null); //Clean UI component
		if(catalogue != null){
			for(Article a : (Article [])catalogue.getArray()){
				if(a != null && a.isActive() && a.hasStockMgmtEnabled() && a.hasStockAlertEnabled()){
					int stock = a.getStock();
					if(stock <= a.getLimitStock()){
						alertTextArea.setText(alertTextArea.getText()+"\nThere are only "+String.valueOf(a.getStock())+" "+a.getName()+" left! Consider refilling");
						if(stock < 0){
							alertTextArea.setText(alertTextArea.getText()+" or disabling inventory management for this article.");
						} else {
							alertTextArea.setText(alertTextArea.getText()+"! ");
						}
					}
				}
			}
		}
	}
	
	public void finalizeTicket(){
		currentTicket.submit(null, journal);
		currentTicket.saveTicketToText();
		currentTicket = null;
		taskToDoLabel.setText("Choose user");
		enCours.setText(null);
		ticketTextArea.setText(null);
		users.saveToTextFile();
		catalogue.saveToTextFile();
		updateStockAlerts();
	}
	
}
