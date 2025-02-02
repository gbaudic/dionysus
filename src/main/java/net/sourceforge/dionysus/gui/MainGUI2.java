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

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sourceforge.dionysus.Article;
import net.sourceforge.dionysus.PaymentMethod;
import net.sourceforge.dionysus.Ticket;
import net.sourceforge.dionysus.TicketItem;
import net.sourceforge.dionysus.TicketState;
import net.sourceforge.dionysus.User;
import net.sourceforge.dionysus.Vendor;
import net.sourceforge.dionysus.db.ArticleDB;
import net.sourceforge.dionysus.db.Database;
import net.sourceforge.dionysus.db.TransactionDB;
import net.sourceforge.dionysus.db.UserDB;
import net.sourceforge.dionysus.gui.panels.ArticlesPanel;
import net.sourceforge.dionysus.gui.panels.TransactionsPanel;
import net.sourceforge.dionysus.gui.panels.UsersPanel;

public class MainGUI2 extends JFrame {

	private static final long serialVersionUID = -2332809325411708152L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				// First window is the login window, not the main one
				PasswordDialog pdiag = new PasswordDialog();
				pdiag.setVisible(true);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private JPanel contentPane;
	private UsersPanel accountsP;
	private TransactionsPanel transactionsP;
	private ArticlesPanel articlesP;
	/** THE textfield for everything */
	private JTextField saisieField;
	/** user name */
	private JLabel lblName;
	/** user balance */
	private JLabel lblBalance;
	/** name of cash assistant on duty */
	private JLabel lblVendorName;
	/** text of the ticket */
	private JTextArea ticketTextArea;
	private JLabel taskToDoLabel;
	private JLabel lblInProgress;

	/** Stock alerts */
	private JTextArea alertTextArea;
	private JLabel lblTotalTicket;
	private JLabel lblBalanceAfter;

	private JFileChooser fileChooser;

	public Currency currency;
	private Ticket currentTicket;
	private TicketItem currentItemAtDesk;
	private User currentUserAtDesk;
	private Article currentArticleAtDesk;
	private Vendor currentVendor;

	private TicketState currentState;
	// Databases
	private UserDB users;
	private ArticleDB catalogue;

	private TransactionDB journal;

	/**
	 * Create the frame.
	 */
	public MainGUI2() {
		// Localization
		setLocale(Locale.getDefault());
		currency = Currency.getInstance(getLocale());

		// Database filling
		fillTheDB();

		// GUI
		setResizable(false);
		setTitle(Constants.SOFTWARE_NAME + " v" + Constants.SOFTWARE_VERSION + " " + Constants.SOFTWARE_VERSION_NICK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBounds(100, 100, 1024, 768);
		setSize(1024, 740); // TODO: adjust to screen resolution
		setLocationRelativeTo(null); // center on screen
		fileChooser = new JFileChooser();

		// *********************************************
		// ****************** MENU *********************
		// *********************************************
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmSettings = new JMenuItem("Settings (TBD)");
		mnFile.add(mntmSettings);

		JMenuItem mntmImportUsersLegacy = new JMenuItem("Import users from legacy (TBD)");
		mnFile.add(mntmImportUsersLegacy);

		JMenuItem mntmChangeVendor = new JMenuItem("Change vendor");
		mnFile.add(mntmChangeVendor);
		mntmChangeVendor.addActionListener(arg0 -> {
			if (currentTicket != null) {
				int choice = JOptionPane.showConfirmDialog(null,
						"There is a ticket currently being processed.\nAre you sure you want to quit?", "Confirmation",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				// If a ticket is started, we need to confirm the will to quit
				if (choice != JOptionPane.YES_OPTION) {
					return;
				}
			}
			setVisible(false);

			// Go back to password
			PasswordDialog pdiag = new PasswordDialog();
			pdiag.setVisible(true);
		});

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmQuit = new JMenuItem("Quit", new ImageIcon(getClass().getResource("/application-exit.png")));
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
		mntmQuit.addActionListener(arg0 -> {
			if (currentTicket != null) {
				int choice = JOptionPane.showConfirmDialog(null,
						"There is a ticket currently being processed.\nAre you sure you want to quit?", "Confirmation",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				// If a ticket is started, we need to confirm the will to quit
				if (choice != JOptionPane.YES_OPTION) {
					return;
				}
			}

			System.exit(0); // Everything is fine, so we stop here
		});
		mnFile.add(mntmQuit);

		JMenu mnExport = new JMenu("Export");
		menuBar.add(mnExport);

		ImageIcon convertIcon = new ImageIcon(getClass().getResource("/gtk-convert.png"));

		JMenuItem mntmExportUsersLegacy = new JMenuItem("Users to legacy (TBD)");
		mntmExportUsersLegacy.addActionListener(e -> {
			int result = fileChooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				File output = fileChooser.getSelectedFile();
				if (output != null) {
					// Export
				}
			}
		});
		mnExport.add(mntmExportUsersLegacy);

		JMenuItem mntmExportUsersCSV = new JMenuItem("Users to CSV", convertIcon);
		mntmExportUsersCSV.addActionListener(e -> exportCSV(users));
		mnExport.add(mntmExportUsersCSV);

		JMenuItem mntmExportArticlesCSV = new JMenuItem("Articles to CSV", convertIcon);
		mntmExportArticlesCSV.addActionListener(e -> exportCSV(catalogue));
		mnExport.add(mntmExportArticlesCSV);

		JMenuItem mntmExportTransCSV = new JMenuItem("Transactions to CSV", convertIcon);
		mntmExportTransCSV.addActionListener(e -> exportCSV(journal));
		mnExport.add(mntmExportTransCSV);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About", new ImageIcon(getClass().getResource("/dialog-information.png")));
		mntmAbout.addActionListener(e -> {
			AboutDialog dlg = new AboutDialog();
			dlg.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			dlg.setVisible(true);
		});
		mnHelp.add(mntmAbout);

		// ********************************************************************************************
		// ********************************************************************************************
		// ********************************************************************************************
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		gbc_tabbedPane.weighty = 0.85;
		contentPane.add(tabbedPane, gbc_tabbedPane);

		JPanel vueP = new JPanel();
		tabbedPane.addTab("Cash desk", null, vueP, "Main view");
		GridBagLayout gbl_vueP = new GridBagLayout();
		gbl_vueP.columnWidths = new int[] { 0, 0, 0 };
		gbl_vueP.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_vueP.columnWeights = new double[] { 0.6, 0.2, Double.MIN_VALUE };
		gbl_vueP.rowWeights = new double[] { 0.2, 0.2, 0.2, 1.0, 0.2, Double.MIN_VALUE };
		vueP.setLayout(gbl_vueP);

		JPanel panel = new JPanel();
		panel.setBorder(
				new TitledBorder(null, "Current user and vendor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		gbc_panel.gridheight = 2;
		vueP.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0 };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		panel.setLayout(gbl_panel);

		JLabel lblN = new JLabel("Name: ");
		GridBagConstraints gbc_lblN = new GridBagConstraints();
		gbc_lblN.anchor = GridBagConstraints.LINE_END;
		gbc_lblN.insets = new Insets(0, 0, 5, 5);
		gbc_lblN.gridx = 0;
		gbc_lblN.gridy = 0;
		panel.add(lblN, gbc_lblN);

		lblName = new JLabel("no user selected");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_nomLabel = new GridBagConstraints();
		gbc_nomLabel.insets = new Insets(0, 0, 5, 5);
		gbc_nomLabel.gridx = 1;
		gbc_nomLabel.gridy = 0;
		panel.add(lblName, gbc_nomLabel);

		JButton chooser = new JButton("Select");
		chooser.setToolTipText("Select user");
		GridBagConstraints gbc_chooser = new GridBagConstraints();
		gbc_chooser.fill = GridBagConstraints.HORIZONTAL;
		gbc_chooser.insets = new Insets(0, 0, 5, 5);
		gbc_chooser.gridx = 2;
		gbc_chooser.gridy = 0;
		chooser.addActionListener(arg0 -> {
			UserChoiceDialog choices = new UserChoiceDialog(users);
			choices.setVisible(true);
			User nextCUser = choices.getUser();
			currentUserAtDesk = nextCUser;

			if (currentUserAtDesk != null) {
				lblName.setText(currentUserAtDesk.getNameWithPromo());
				lblBalance.setText(NumberFormat.getCurrencyInstance().format(currentUserAtDesk.getBalance()));

				if (currentTicket == null) {
					currentTicket = new Ticket(currentUserAtDesk);
					ticketTextArea.setText(null);
				} else {
					currentTicket.setUser(currentUserAtDesk);
				}
				taskToDoLabel.setText("Choose article");
				currentState = TicketState.TICKET_IDLE;
			}
		});
		panel.add(chooser, gbc_chooser);

		JButton btnX = new JButton("Default");
		btnX.setToolTipText("Accountless users");
		GridBagConstraints gbc_btnX = new GridBagConstraints();
		gbc_btnX.insets = new Insets(0, 0, 5, 5);
		gbc_btnX.gridx = 2;
		gbc_btnX.gridy = 1;
		gbc_btnX.fill = GridBagConstraints.HORIZONTAL; // for esthetics
		btnX.addActionListener(arg0 -> {
			currentUserAtDesk = null;

			lblName.setText("default (00)");
			lblBalance.setText(NumberFormat.getCurrencyInstance().format(0.00));

			if (currentTicket == null) {
				currentTicket = new Ticket(currentUserAtDesk);
				ticketTextArea.setText(null);
			} else {
				currentTicket.setUser(currentUserAtDesk);
			}
			taskToDoLabel.setText("Choose article");
			currentState = TicketState.TICKET_IDLE;
		});
		panel.add(btnX, gbc_btnX);

		JLabel lblSolde = new JLabel("Balance:");
		GridBagConstraints gbc_lblSolde = new GridBagConstraints();
		gbc_lblSolde.anchor = GridBagConstraints.LINE_END;
		gbc_lblSolde.insets = new Insets(0, 0, 5, 5);
		gbc_lblSolde.gridx = 0;
		gbc_lblSolde.gridy = 1;
		panel.add(lblSolde, gbc_lblSolde);

		lblBalance = new JLabel("--");
		lblBalance.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_soldeLabel = new GridBagConstraints();
		gbc_soldeLabel.insets = new Insets(0, 0, 5, 5);
		gbc_soldeLabel.gridx = 1;
		gbc_soldeLabel.gridy = 1;
		panel.add(lblBalance, gbc_soldeLabel);

		JLabel lblVendor = new JLabel("Vendor:");
		GridBagConstraints gbc_lblVendor = new GridBagConstraints();
		gbc_lblVendor.insets = new Insets(0, 0, 0, 5);
		gbc_lblVendor.gridx = 0;
		gbc_lblVendor.gridy = 2;
		panel.add(lblVendor, gbc_lblVendor);

		lblVendorName = new JLabel();
		lblVendor.setLabelFor(lblVendorName);
		lblVendorName.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblVendorName = new GridBagConstraints();
		gbc_lblVendorName.insets = new Insets(0, 0, 0, 5);
		gbc_lblVendorName.gridx = 1;
		gbc_lblVendorName.gridy = 2;
		panel.add(lblVendorName, gbc_lblVendorName);

		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 0;
		gbc_panel_2.gridheight = 3;
		vueP.add(panel_2, gbc_panel_2);
		panel_2.setLayout(new GridLayout(3, 0, 0, 0));

		taskToDoLabel = new JLabel("Welcome!");
		taskToDoLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		taskToDoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(taskToDoLabel);

		lblInProgress = new JLabel("");
		lblInProgress.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblInProgress);

		saisieField = new JTextField();
		saisieField.setHorizontalAlignment(SwingConstants.RIGHT);
		saisieField.setFont(new Font("Tahoma", Font.BOLD, 20));
		saisieField.addActionListener(e -> {
			inputLogic();
			saisieField.setText(null);
		});
		saisieField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				// insertUpdate(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (saisieField.getText().length() == 13) { // barcode length
					inputLogic();
					// TODO: clear document after validation of input
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// Nothing to do here
			}
		});
		panel_2.add(saisieField);
		saisieField.setColumns(13);

		JPanel panel_articles = new JPanel();
		GridBagConstraints gbc_panel_articles = new GridBagConstraints();
		gbc_panel_articles.gridheight = 4;
		gbc_panel_articles.insets = new Insets(0, 0, 5, 5);
		gbc_panel_articles.fill = GridBagConstraints.BOTH;
		gbc_panel_articles.gridx = 0;
		gbc_panel_articles.gridy = 2;
		vueP.add(panel_articles, gbc_panel_articles);
		panel_articles.setLayout(new GridLayout(5, 6, 0, 0));

		Article[] cat = catalogue.getArray();
		if (cat != null) {
			for (final Article c : cat) {
				if (c != null && c.isActive()) {
					JButton btn = new JButton(c.getName());
					btn.setToolTipText(c.getToolTipText());
					btn.addActionListener(arg0 -> {
						if (currentState == TicketState.TICKET_IDLE) {
							currentArticleAtDesk = c;
							currentItemAtDesk = new TicketItem(c);
							lblInProgress.setText("? x " + c.getName());

							if (c.getNumberOfPrices() > 1) {
								taskToDoLabel.setText("Select fee");
								currentState = TicketState.PRICE;
							} else if (!currentArticleAtDesk.isCountable()) {
								// Only 1 price id, quantity to set
								taskToDoLabel.setText("Select quantity");
								currentState = TicketState.QUANTITY;
								lblInProgress.setText("? x " + currentArticleAtDesk.getName());
							} else {
								// Only 1 price id, no quantity to set: add item to ticket
								currentItemAtDesk.computeAmount();
								finalizeTicketItem();
							}
						}
					});

					panel_articles.add(btn);
				}
			}
		}

		ticketTextArea = new JTextArea();
		ticketTextArea.setEditable(false);
		JScrollPane sp_ticketTextArea = new JScrollPane(ticketTextArea);
		GridBagConstraints gbc_ticketTextArea = new GridBagConstraints();
		gbc_ticketTextArea.insets = new Insets(0, 0, 5, 5);
		gbc_ticketTextArea.fill = GridBagConstraints.BOTH;
		gbc_ticketTextArea.gridx = 2;
		gbc_ticketTextArea.gridy = 0;
		gbc_ticketTextArea.gridheight = 4;
		vueP.add(sp_ticketTextArea, gbc_ticketTextArea);

		JPanel panel_pavenum = new JPanel();
		GridBagConstraints gbc_panel_pavenum = new GridBagConstraints();
		gbc_panel_pavenum.gridheight = 3;
		gbc_panel_pavenum.insets = new Insets(0, 0, 0, 5);
		gbc_panel_pavenum.fill = GridBagConstraints.BOTH;
		gbc_panel_pavenum.gridx = 1;
		gbc_panel_pavenum.gridy = 3;
		vueP.add(panel_pavenum, gbc_panel_pavenum);
		panel_pavenum.setLayout(new GridLayout(4, 3, 0, 0));

		// Numbers from 1 to 9 then 0
		for (int i = 1; i < 11; i++) {
			String nbAsText = String.valueOf(i % 10);
			JButton btn = new JButton(nbAsText);
			btn.setMnemonic(nbAsText.charAt(0));
			btn.addActionListener(arg0 -> saisieField.setText(saisieField.getText() + nbAsText));
			panel_pavenum.add(btn);
		}

		JButton btnMetre = new JButton("Dozen");
		btnMetre.setToolTipText("12 units");
		btnMetre.addActionListener(arg0 -> saisieField.setText(String.valueOf(12)));
		panel_pavenum.add(btnMetre);

		JButton btnClear = new JButton("Clear");
		btnClear.setToolTipText("Clear any input (not the ticket!)");
		btnClear.addActionListener(arg0 -> saisieField.setText(null));
		panel_pavenum.add(btnClear);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Total", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 5);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 2;
		gbc_panel_3.gridy = 4;
		gbc_panel_3.gridheight = 1;
		vueP.add(panel_3, gbc_panel_3);
		panel_3.setLayout(new GridLayout(2, 2, 0, 0));

		JLabel lblTotalTicketText = new JLabel("Ticket total: ");
		lblTotalTicketText.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblTotalTicketText = new GridBagConstraints();
		gbc_lblTotalTicketText.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalTicketText.fill = GridBagConstraints.BOTH;
		gbc_lblTotalTicketText.gridx = 0;
		gbc_lblTotalTicketText.gridy = 0;
		panel_3.add(lblTotalTicketText, gbc_lblTotalTicketText);

		lblTotalTicket = new JLabel();
		lblTotalTicket.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblTotalTicket = new GridBagConstraints();
		gbc_lblTotalTicket.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalTicket.fill = GridBagConstraints.BOTH;
		gbc_lblTotalTicket.gridx = 1;
		gbc_lblTotalTicket.gridy = 0;
		panel_3.add(lblTotalTicket, gbc_lblTotalTicket);

		JLabel lblSoldeApresText = new JLabel("Balance after ticket:");
		GridBagConstraints gbc_lblSoldeApresText = new GridBagConstraints();
		gbc_lblSoldeApresText.insets = new Insets(0, 0, 5, 5);
		gbc_lblSoldeApresText.fill = GridBagConstraints.BOTH;
		gbc_lblSoldeApresText.gridx = 0;
		gbc_lblSoldeApresText.gridy = 1;
		panel_3.add(lblSoldeApresText, gbc_lblSoldeApresText);

		lblBalanceAfter = new JLabel();
		GridBagConstraints gbc_lblSoldeApres = new GridBagConstraints();
		gbc_lblSoldeApres.insets = new Insets(0, 0, 5, 5);
		gbc_lblSoldeApres.fill = GridBagConstraints.BOTH;
		gbc_lblSoldeApres.gridx = 1;
		gbc_lblSoldeApres.gridy = 1;
		panel_3.add(lblBalanceAfter, gbc_lblSoldeApres);

		JPanel panel_PaymentMethods = new JPanel();
		panel_PaymentMethods.setBorder(
				new TitledBorder(null, "Payment method", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_PaymentMethods = new GridBagConstraints();
		gbc_panel_PaymentMethods.insets = new Insets(0, 0, 0, 5);
		gbc_panel_PaymentMethods.fill = GridBagConstraints.BOTH;
		gbc_panel_PaymentMethods.gridx = 2;
		gbc_panel_PaymentMethods.gridy = 5;
		gbc_panel_PaymentMethods.gridheight = 1;
		vueP.add(panel_PaymentMethods, gbc_panel_PaymentMethods);
		panel_PaymentMethods.setLayout(new GridLayout(2, 3, 0, 0));

		for (final PaymentMethod p : PaymentMethod.values()) {
			JButton btn = new JButton(p.getName());
			btn.addActionListener(arg0 -> {
				if (currentTicket != null && currentState == TicketState.TICKET_IDLE) {
					currentTicket.pay(p);

					if (p == PaymentMethod.CASH) {
						double paye = Double.parseDouble(JOptionPane.showInputDialog(null, "Change given: ", "Change",
								JOptionPane.QUESTION_MESSAGE));
						JOptionPane.showMessageDialog(null, "You owe "
								+ NumberFormat.getCurrencyInstance().format(paye - currentTicket.getAmount()) + ".", "",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane
								.showMessageDialog(
										null, String.valueOf(currentTicket.getAmount()) + " to be paid using "
												+ p.getName() + ".\nClick OK when done.",
										"Payment", JOptionPane.PLAIN_MESSAGE);
					}
					// Save the finished ticket
					finalizeTicket();
					return;
				}
			});

			panel_PaymentMethods.add(btn);
		}

		currentState = TicketState.IDLE;

		// ********************************************************************************************
		// ********************************************************************************************
		// ********************************************************************************************
		accountsP = new UsersPanel(users);
		tabbedPane.addTab("Accounts", null, accountsP, "Accounts management");

		// ********************************************************************************************
		// ********************************************************************************************
		// ********************************************************************************************

		articlesP = new ArticlesPanel(catalogue);
		tabbedPane.addTab("Articles", null, articlesP, "Manage articles in store");

		// *******************************************************************************************
		// *******************************************************************************************
		// *******************************************************************************************

		transactionsP = new TransactionsPanel(journal);
		tabbedPane.addTab("Transactions", null, transactionsP, "Log of all transactions");

		// ********************************************
		alertTextArea = new JTextArea();
		alertTextArea.setColumns(8);
		alertTextArea.setRows(10);
		alertTextArea.setEditable(false);
		JScrollPane alertScrollPane = new JScrollPane(alertTextArea);
		GridBagConstraints gbc_alertTextArea = new GridBagConstraints();
		gbc_alertTextArea.fill = GridBagConstraints.BOTH;
		gbc_alertTextArea.gridx = 0;
		gbc_alertTextArea.gridy = 1;
		gbc_alertTextArea.weighty = 0.15;
		contentPane.add(alertScrollPane, gbc_alertTextArea);

		updateStockAlerts();
	}

	private void exportCSV(Database db) {
		int result = fileChooser.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File output = fileChooser.getSelectedFile();
			if (output != null && db != null) {
				db.export(output);
			}
		}
	}

	/**
	 * Initializes and fills the required databases
	 */
	private void fillTheDB() {
		// Initialize
		users = new UserDB();
		catalogue = new ArticleDB();
		journal = new TransactionDB();

		// Fill
		users.createFromTextFile("accounts.dat");
		catalogue.createFromTextFile("articles.dat");
		journal.createFromTextFile("log.dat");
	}

	/**
	 * Called when a ticket is finished to trigger saving in databases and refresh
	 * of the labels and objects
	 */
	public void finalizeTicket() {
		// Save transactions
		currentTicket.submit(null, journal, currentVendor);
		currentTicket.saveTicketToText();

		// Save updated databases
		users.saveToTextFile();
		catalogue.saveToTextFile();
		updateStockAlerts();

		// Clear GUI
		currentTicket = null;
		taskToDoLabel.setText("Choose user");
		lblInProgress.setText(null);
		ticketTextArea.setText(null);
		lblTotalTicket.setText(null);
		lblBalanceAfter.setText(null);
		currentUserAtDesk = null;
		currentItemAtDesk = null;
		lblName.setText("no user selected");
		lblBalance.setText("--");

		// Change current state
		currentState = TicketState.IDLE;

		// Refresh JTables
		accountsP.refreshTable();
		transactionsP.refreshTable();
		articlesP.refreshTable();
	}

	/**
	 * Called when a ticket item is finished to store it in the current ticket
	 */
	private void finalizeTicketItem() {
		currentTicket.addArticle(currentItemAtDesk);
		lblInProgress.setText(String.valueOf(currentItemAtDesk.getQuantity()) + " x " + currentArticleAtDesk.getName());
		currentItemAtDesk = null;
		currentArticleAtDesk = null;
		printTicketToScreen(currentTicket);
		taskToDoLabel.setText("Other article or finish");
		currentState = TicketState.TICKET_IDLE;
	}

	/**
	 * Contains the logic for input field validation
	 */
	private void inputLogic() {
		try {
			if (saisieField.getText().length() > 0) {
				// Validate the content in the TextField
				// TODO: change this so we can accommodate non-integer numbers in a
				// locale-independent way
				switch (currentState) {
				case TICKET_IDLE: // article being chosen
					long saisie = Long.parseLong(saisieField.getText());
					currentArticleAtDesk = catalogue.getArticleByCode(saisie);
					if (currentArticleAtDesk != null) {
						currentItemAtDesk = new TicketItem(currentArticleAtDesk);

						if (currentArticleAtDesk.getNumberOfPrices() > 1) {
							// More than 1 price id
							taskToDoLabel.setText("Select fee");
							currentState = TicketState.PRICE;
							lblInProgress.setText("? x " + currentArticleAtDesk.getName());
						} else if (!currentArticleAtDesk.isCountable()) {
							// Only 1 price id, quantity to set
							taskToDoLabel.setText("Select quantity");
							currentState = TicketState.QUANTITY;
							lblInProgress.setText("? x " + currentArticleAtDesk.getName());
						} else {
							// Only 1 price id, no quantity to set: add item to ticket
							currentItemAtDesk.computeAmount();
							finalizeTicketItem();
						}
					} else {
						JOptionPane.showMessageDialog(null, "Unknown article!", "Error", JOptionPane.WARNING_MESSAGE);
					}
					break;
				case QUANTITY: // quantity being chosen
					double saisie2 = Double.parseDouble(saisieField.getText());
					if (saisie2 == 0) {
						JOptionPane.showMessageDialog(null, "Invalid quantity!", "Error", JOptionPane.WARNING_MESSAGE);
					} else {
						// Negative quantities are allowed so that corrections are possible
						if (currentArticleAtDesk.isCountable()) {
							currentItemAtDesk.setQuantity((int) saisie2);
						} else {
							currentItemAtDesk.setQuantity((int) (saisie2 * 1000));
						}
						currentItemAtDesk.computeAmount();
						finalizeTicketItem();
					}
					break;
				case PRICE: // price being chosen
					long saisie3 = Long.parseLong(saisieField.getText());
					if (saisie3 < 0 || saisie3 >= currentArticleAtDesk.getNumberOfPrices()) {
						JOptionPane.showMessageDialog(null, "Invalid fee for this article!", "Error",
								JOptionPane.WARNING_MESSAGE);
					} else {
						currentItemAtDesk.setFee((int) saisie3);
						if (!currentArticleAtDesk.isCountable()) {
							taskToDoLabel.setText("Select quantity");
							currentState = TicketState.QUANTITY;
						} else {
							currentItemAtDesk.computeAmount();
							finalizeTicketItem();
						}
					}
					break;
				default:
					break;
				}
			} else {
				// Validations without content in the TextField
				if (currentTicket.getNumberOfItems() >= 1) {
					if (currentTicket.getPaymentMethod() == null) {
						if (currentUserAtDesk == null) {
							taskToDoLabel.setText("Choose payment method");
							return;
						} else {
							// Save ticket -- pay with current account balance
							finalizeTicket();
							return;
						}
					} else {
						if (currentTicket.getPaymentMethod() == PaymentMethod.CASH) {
							double paye = Double.parseDouble(JOptionPane.showInputDialog(null, "Change given: ",
									"Change", JOptionPane.QUESTION_MESSAGE));
							JOptionPane.showMessageDialog(null,
									"You owe " + NumberFormat.getCurrencyInstance()
											.format(paye - currentTicket.getAmount()) + ".",
									"", JOptionPane.INFORMATION_MESSAGE);
						}
						// Save the finished ticket
						finalizeTicket();
						return;
					}
				}
			}
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(null, "Invalid input! Cannot parse a number.", "Error",
					JOptionPane.ERROR_MESSAGE);
			nfe.printStackTrace();
		}
	}

	/**
	 * Display ticket information at the appropriate places in GUI
	 *
	 * @param t the ticket to display
	 */
	private void printTicketToScreen(Ticket t) {
		if (t != null) {
			ticketTextArea.setText(null);
			for (TicketItem ti : t.getItems()) {
				if (ti != null) {
					// Add to GUI component the lines for each article
					ticketTextArea.setText(ticketTextArea.getText() + ti.toString() + "\n");
				}
			}

			// Same for total amount
			lblTotalTicket.setText(NumberFormat.getCurrencyInstance().format(t.getAmount()));

			// Same for balance after purchase
			if (currentUserAtDesk != null) {
				lblBalanceAfter.setText(NumberFormat.getCurrencyInstance().format(t.getBalanceAfterTicket()));
			} else {
				lblBalanceAfter.setText("--");
			}
		}
	}

	/**
	 * @param currentVendor the currentVendor to set
	 */
	public void setCurrentVendor(Vendor currentVendor) {
		this.currentVendor = currentVendor;
		lblVendorName.setText(currentVendor != null ? currentVendor.getName() : "unregistered");
	}

	/**
	 * Refreshes the component containing the stock alerts
	 */
	public void updateStockAlerts() {
		alertTextArea.setText(null); // Clean UI component
		StringBuilder bob = new StringBuilder();

		if (Objects.nonNull(catalogue)) {
			for (Article a : catalogue.getArray()) {
				if (a != null && a.hasStockMgmtEnabled() && a.hasStockAlertEnabled()) {
					int stock = a.getStock();
					if (stock <= a.getLimitStock()) {
						bob.append(
								String.format("\nThere are only %d %s left! Consider refilling", stock, a.getName()));
						if (stock < 0) {
							bob.append(" or disabling inventory management for this article.");
						} else {
							bob.append("!");
						}
					}
				}
			}
			alertTextArea.setText(bob.toString());
		}
	}

}
