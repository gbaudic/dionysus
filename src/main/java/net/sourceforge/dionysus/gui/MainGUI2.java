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
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
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
import javax.swing.WindowConstants;
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
				final PasswordDialog pdiag = new PasswordDialog();
				pdiag.setVisible(true);

			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	private final JPanel contentPane;
	private final UsersPanel accountsP;
	private final TransactionsPanel transactionsP;
	private final ArticlesPanel articlesP;
	/** THE textfield for everything */
	private final JTextField inputField;
	/** user name */
	private final JLabel lblName;
	/** user balance */
	private final JLabel lblBalance;
	/** name of cash assistant on duty */
	private final JLabel lblVendorName;
	/** text of the ticket */
	private final JTextArea ticketTextArea;
	private final JLabel taskToDoLabel;
	private final JLabel lblInProgress;

	/** Stock alerts */
	private final JTextArea alertTextArea;
	private final JLabel lblTotalTicket;
	private final JLabel lblBalanceAfter;

	private final JFileChooser fileChooser;

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
		setTitle(Constants.SOFTWARE_NAME + " v" + Constants.SOFTWARE_VERSION + " " + Constants.SOFTWARE_VERSION_NICK); //$NON-NLS-1$ //$NON-NLS-2$
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(1024, 740); // TODO: adjust to screen resolution
		setLocationRelativeTo(null); // center on screen
		fileChooser = new JFileChooser();

		// *********************************************
		// ****************** MENU *********************
		// *********************************************
		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		final JMenu mnFile = new JMenu(Messages.getString("MainGUI2.2")); //$NON-NLS-1$
		menuBar.add(mnFile);

		final JMenuItem mntmSettings = new JMenuItem(Messages.getString("MainGUI2.3")); //$NON-NLS-1$
		mnFile.add(mntmSettings);

		final JMenuItem mntmImportUsersLegacy = new JMenuItem(Messages.getString("MainGUI2.4")); //$NON-NLS-1$
		mnFile.add(mntmImportUsersLegacy);

		final JMenuItem mntmChangeVendor = new JMenuItem(Messages.getString("MainGUI2.5")); //$NON-NLS-1$
		mnFile.add(mntmChangeVendor);
		mntmChangeVendor.addActionListener(this::onChangeVendor);

		final JSeparator separator = new JSeparator();
		mnFile.add(separator);

		final JMenuItem mntmQuit = new JMenuItem(Messages.getString("MainGUI2.6"), Constants.exit); //$NON-NLS-1$
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
		mntmQuit.addActionListener(this::onQuit);
		mnFile.add(mntmQuit);

		final JMenu mnExport = new JMenu(Messages.getString("MainGUI2.7")); //$NON-NLS-1$
		menuBar.add(mnExport);

		final JMenuItem mntmExportUsersLegacy = new JMenuItem(Messages.getString("MainGUI2.8")); //$NON-NLS-1$
		mntmExportUsersLegacy.addActionListener(e -> {
			final int result = fileChooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				final File output = fileChooser.getSelectedFile();
				if (output != null) {
					// Export
				}
			}
		});
		mnExport.add(mntmExportUsersLegacy);

		final JMenuItem mntmExportUsersCSV = new JMenuItem(Messages.getString("MainGUI2.9"), Constants.convert); //$NON-NLS-1$
		mntmExportUsersCSV.addActionListener(e -> exportCSV(users));
		mnExport.add(mntmExportUsersCSV);

		final JMenuItem mntmExportArticlesCSV = new JMenuItem(Messages.getString("MainGUI2.10"), Constants.convert); //$NON-NLS-1$
		mntmExportArticlesCSV.addActionListener(e -> exportCSV(catalogue));
		mnExport.add(mntmExportArticlesCSV);

		final JMenuItem mntmExportTransCSV = new JMenuItem(Messages.getString("MainGUI2.11"), Constants.convert); //$NON-NLS-1$
		mntmExportTransCSV.addActionListener(e -> exportCSV(journal));
		mnExport.add(mntmExportTransCSV);

		final JMenu mnHelp = new JMenu(Messages.getString("MainGUI2.12")); //$NON-NLS-1$
		menuBar.add(mnHelp);

		final JMenuItem mntmAbout = new JMenuItem(Messages.getString("MainGUI2.13"), Constants.about); //$NON-NLS-1$
		mntmAbout.addActionListener(this::onAbout);
		mnHelp.add(mntmAbout);

		// ********************************************************************************************
		// ********************************************************************************************
		// ********************************************************************************************
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		final GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		final GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		gbc_tabbedPane.weighty = 0.85;
		contentPane.add(tabbedPane, gbc_tabbedPane);

		final JPanel vueP = new JPanel();
		tabbedPane.addTab(Messages.getString("MainGUI2.14"), null, vueP, Messages.getString("MainGUI2.15")); //$NON-NLS-1$ //$NON-NLS-2$
		final GridBagLayout gbl_vueP = new GridBagLayout();
		gbl_vueP.columnWidths = new int[]{0, 0, 0};
		gbl_vueP.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_vueP.columnWeights = new double[]{0.6, 0.2, Double.MIN_VALUE};
		gbl_vueP.rowWeights = new double[]{0.2, 0.2, 0.2, 1.0, 0.2, Double.MIN_VALUE};
		vueP.setLayout(gbl_vueP);

		final JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, Messages.getString("MainGUI2.16"), TitledBorder.LEADING, //$NON-NLS-1$
				TitledBorder.TOP, null, null));
		final GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		gbc_panel.gridheight = 2;
		vueP.add(panel, gbc_panel);
		final GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0};
		panel.setLayout(gbl_panel);

		final JLabel lblN = new JLabel(Messages.getString("MainGUI2.17")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblN = new GridBagConstraints();
		gbc_lblN.anchor = GridBagConstraints.LINE_END;
		gbc_lblN.insets = new Insets(0, 0, 5, 5);
		gbc_lblN.gridx = 0;
		gbc_lblN.gridy = 0;
		panel.add(lblN, gbc_lblN);

		lblName = new JLabel(Messages.getString("MainGUI2.18")); //$NON-NLS-1$
		lblName.setFont(new Font("Tahoma", Font.BOLD, 14)); //$NON-NLS-1$
		final GridBagConstraints gbc_nomLabel = new GridBagConstraints();
		gbc_nomLabel.insets = new Insets(0, 0, 5, 5);
		gbc_nomLabel.gridx = 1;
		gbc_nomLabel.gridy = 0;
		panel.add(lblName, gbc_nomLabel);

		final JButton chooser = new JButton(Messages.getString("MainGUI2.20")); //$NON-NLS-1$
		chooser.setToolTipText(Messages.getString("MainGUI2.21")); //$NON-NLS-1$
		final GridBagConstraints gbc_chooser = new GridBagConstraints();
		gbc_chooser.fill = GridBagConstraints.HORIZONTAL;
		gbc_chooser.insets = new Insets(0, 0, 5, 5);
		gbc_chooser.gridx = 2;
		gbc_chooser.gridy = 0;
		chooser.addActionListener(this::onSelectUser);
		panel.add(chooser, gbc_chooser);

		final JButton btnX = new JButton(Messages.getString("MainGUI2.22")); //$NON-NLS-1$
		btnX.setToolTipText(Messages.getString("MainGUI2.23")); //$NON-NLS-1$
		final GridBagConstraints gbc_btnX = new GridBagConstraints();
		gbc_btnX.insets = new Insets(0, 0, 5, 5);
		gbc_btnX.gridx = 2;
		gbc_btnX.gridy = 1;
		gbc_btnX.fill = GridBagConstraints.HORIZONTAL; // for esthetics
		btnX.addActionListener(this::onSelectDefaultUser);
		panel.add(btnX, gbc_btnX);

		final JLabel lblSolde = new JLabel(Messages.getString("MainGUI2.24")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblSolde = new GridBagConstraints();
		gbc_lblSolde.anchor = GridBagConstraints.LINE_END;
		gbc_lblSolde.insets = new Insets(0, 0, 5, 5);
		gbc_lblSolde.gridx = 0;
		gbc_lblSolde.gridy = 1;
		panel.add(lblSolde, gbc_lblSolde);

		lblBalance = new JLabel(Messages.getString("MainGUI2.25")); //$NON-NLS-1$
		lblBalance.setFont(new Font("Tahoma", Font.BOLD, 14)); //$NON-NLS-1$
		final GridBagConstraints gbc_soldeLabel = new GridBagConstraints();
		gbc_soldeLabel.insets = new Insets(0, 0, 5, 5);
		gbc_soldeLabel.gridx = 1;
		gbc_soldeLabel.gridy = 1;
		panel.add(lblBalance, gbc_soldeLabel);

		final JLabel lblVendor = new JLabel(Messages.getString("MainGUI2.27")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblVendor = new GridBagConstraints();
		gbc_lblVendor.insets = new Insets(0, 0, 0, 5);
		gbc_lblVendor.gridx = 0;
		gbc_lblVendor.gridy = 2;
		panel.add(lblVendor, gbc_lblVendor);

		lblVendorName = new JLabel();
		lblVendor.setLabelFor(lblVendorName);
		lblVendorName.setHorizontalAlignment(SwingConstants.LEFT);
		final GridBagConstraints gbc_lblVendorName = new GridBagConstraints();
		gbc_lblVendorName.insets = new Insets(0, 0, 0, 5);
		gbc_lblVendorName.gridx = 1;
		gbc_lblVendorName.gridy = 2;
		panel.add(lblVendorName, gbc_lblVendorName);

		final JPanel panel_2 = new JPanel();
		final GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 0;
		gbc_panel_2.gridheight = 3;
		vueP.add(panel_2, gbc_panel_2);
		panel_2.setLayout(new GridLayout(3, 0, 0, 0));

		taskToDoLabel = new JLabel(Messages.getString("MainGUI2.28")); //$NON-NLS-1$
		taskToDoLabel.setFont(new Font("Tahoma", Font.PLAIN, 16)); //$NON-NLS-1$
		taskToDoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(taskToDoLabel);

		lblInProgress = new JLabel(""); //$NON-NLS-1$
		lblInProgress.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblInProgress);

		inputField = new JTextField();
		inputField.setHorizontalAlignment(SwingConstants.RIGHT);
		inputField.setFont(new Font("Tahoma", Font.BOLD, 20)); //$NON-NLS-1$
		inputField.addActionListener(e -> {
			inputLogic();
			inputField.setText(null);
		});
		inputField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				// insertUpdate(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (inputField.getText().length() == 13) { // barcode length
					inputLogic();
					// TODO: clear document after validation of input
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// Nothing to do here
			}
		});
		panel_2.add(inputField);
		inputField.setColumns(13);

		final JPanel panel_articles = new JPanel();
		final GridBagConstraints gbc_panel_articles = new GridBagConstraints();
		gbc_panel_articles.gridheight = 4;
		gbc_panel_articles.insets = new Insets(0, 0, 5, 5);
		gbc_panel_articles.fill = GridBagConstraints.BOTH;
		gbc_panel_articles.gridx = 0;
		gbc_panel_articles.gridy = 2;
		vueP.add(panel_articles, gbc_panel_articles);
		panel_articles.setLayout(new GridLayout(5, 6, 0, 0));

		final Article[] cat = catalogue.getArray();
		if (cat != null) {
			for (final Article c : cat) {
				if (c != null && c.isActive()) {
					final JButton btn = new JButton(c.getName());
					btn.setToolTipText(c.getToolTipText());
					btn.addActionListener(arg0 -> onSelectArticle(c));

					panel_articles.add(btn);
				}
			}
		}

		ticketTextArea = new JTextArea();
		ticketTextArea.setEditable(false);
		final JScrollPane sp_ticketTextArea = new JScrollPane(ticketTextArea);
		final GridBagConstraints gbc_ticketTextArea = new GridBagConstraints();
		gbc_ticketTextArea.insets = new Insets(0, 0, 5, 5);
		gbc_ticketTextArea.fill = GridBagConstraints.BOTH;
		gbc_ticketTextArea.gridx = 2;
		gbc_ticketTextArea.gridy = 0;
		gbc_ticketTextArea.gridheight = 4;
		vueP.add(sp_ticketTextArea, gbc_ticketTextArea);

		final JPanel panel_pavenum = new JPanel();
		final GridBagConstraints gbc_panel_pavenum = new GridBagConstraints();
		gbc_panel_pavenum.gridheight = 3;
		gbc_panel_pavenum.insets = new Insets(0, 0, 0, 5);
		gbc_panel_pavenum.fill = GridBagConstraints.BOTH;
		gbc_panel_pavenum.gridx = 1;
		gbc_panel_pavenum.gridy = 3;
		vueP.add(panel_pavenum, gbc_panel_pavenum);
		panel_pavenum.setLayout(new GridLayout(4, 3, 0, 0));

		// Numbers from 1 to 9 then 0
		for (int i = 1; i < 11; i++) {
			final String nbAsText = String.valueOf(i % 10);
			final JButton btn = new JButton(nbAsText);
			btn.setMnemonic(nbAsText.charAt(0));
			btn.addActionListener(arg0 -> inputField.setText(inputField.getText() + nbAsText));
			panel_pavenum.add(btn);
		}

		final JButton btnMetre = new JButton(Messages.getString("MainGUI2.32")); //$NON-NLS-1$
		btnMetre.setToolTipText(Messages.getString("MainGUI2.33")); //$NON-NLS-1$
		btnMetre.addActionListener(arg0 -> inputField.setText(String.valueOf(12)));
		panel_pavenum.add(btnMetre);

		final JButton btnClear = new JButton(Messages.getString("MainGUI2.34")); //$NON-NLS-1$
		btnClear.setToolTipText(Messages.getString("MainGUI2.35")); //$NON-NLS-1$
		btnClear.addActionListener(arg0 -> inputField.setText(null));
		panel_pavenum.add(btnClear);

		final JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, Messages.getString("MainGUI2.36"), TitledBorder.LEADING, //$NON-NLS-1$
				TitledBorder.TOP, null, null));
		final GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 5);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 2;
		gbc_panel_3.gridy = 4;
		gbc_panel_3.gridheight = 1;
		vueP.add(panel_3, gbc_panel_3);
		panel_3.setLayout(new GridLayout(2, 2, 0, 0));

		final JLabel lblTotalTicketText = new JLabel(Messages.getString("MainGUI2.37")); //$NON-NLS-1$
		lblTotalTicketText.setFont(new Font("Tahoma", Font.BOLD, 14)); //$NON-NLS-1$
		final GridBagConstraints gbc_lblTotalTicketText = new GridBagConstraints();
		gbc_lblTotalTicketText.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalTicketText.fill = GridBagConstraints.BOTH;
		gbc_lblTotalTicketText.gridx = 0;
		gbc_lblTotalTicketText.gridy = 0;
		panel_3.add(lblTotalTicketText, gbc_lblTotalTicketText);

		lblTotalTicket = new JLabel();
		lblTotalTicket.setFont(new Font("Tahoma", Font.BOLD, 14)); //$NON-NLS-1$
		final GridBagConstraints gbc_lblTotalTicket = new GridBagConstraints();
		gbc_lblTotalTicket.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalTicket.fill = GridBagConstraints.BOTH;
		gbc_lblTotalTicket.gridx = 1;
		gbc_lblTotalTicket.gridy = 0;
		panel_3.add(lblTotalTicket, gbc_lblTotalTicket);

		final JLabel lblSoldeApresText = new JLabel(Messages.getString("MainGUI2.40")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblSoldeApresText = new GridBagConstraints();
		gbc_lblSoldeApresText.insets = new Insets(0, 0, 5, 5);
		gbc_lblSoldeApresText.fill = GridBagConstraints.BOTH;
		gbc_lblSoldeApresText.gridx = 0;
		gbc_lblSoldeApresText.gridy = 1;
		panel_3.add(lblSoldeApresText, gbc_lblSoldeApresText);

		lblBalanceAfter = new JLabel();
		final GridBagConstraints gbc_lblSoldeApres = new GridBagConstraints();
		gbc_lblSoldeApres.insets = new Insets(0, 0, 5, 5);
		gbc_lblSoldeApres.fill = GridBagConstraints.BOTH;
		gbc_lblSoldeApres.gridx = 1;
		gbc_lblSoldeApres.gridy = 1;
		panel_3.add(lblBalanceAfter, gbc_lblSoldeApres);

		final JPanel panel_PaymentMethods = new JPanel();
		panel_PaymentMethods.setBorder(new TitledBorder(null, Messages.getString("MainGUI2.41"), TitledBorder.LEADING, //$NON-NLS-1$
				TitledBorder.TOP, null, null));
		final GridBagConstraints gbc_panel_PaymentMethods = new GridBagConstraints();
		gbc_panel_PaymentMethods.insets = new Insets(0, 0, 0, 5);
		gbc_panel_PaymentMethods.fill = GridBagConstraints.BOTH;
		gbc_panel_PaymentMethods.gridx = 2;
		gbc_panel_PaymentMethods.gridy = 5;
		gbc_panel_PaymentMethods.gridheight = 1;
		vueP.add(panel_PaymentMethods, gbc_panel_PaymentMethods);
		panel_PaymentMethods.setLayout(new GridLayout(2, 3, 0, 0));

		for (final PaymentMethod p : PaymentMethod.values()) {
			final JButton btn = new JButton(p.getName());
			btn.addActionListener(arg0 -> onPay(p));

			panel_PaymentMethods.add(btn);
		}

		currentState = TicketState.IDLE;

		// ********************************************************************************************
		// ********************************************************************************************
		// ********************************************************************************************
		accountsP = new UsersPanel(users);
		tabbedPane.addTab(Messages.getString("MainGUI2.42"), null, accountsP, Messages.getString("MainGUI2.43")); //$NON-NLS-1$ //$NON-NLS-2$

		// ********************************************************************************************
		// ********************************************************************************************
		// ********************************************************************************************

		articlesP = new ArticlesPanel(catalogue);
		tabbedPane.addTab(Messages.getString("MainGUI2.44"), null, articlesP, Messages.getString("MainGUI2.45")); //$NON-NLS-1$ //$NON-NLS-2$

		// *******************************************************************************************
		// *******************************************************************************************
		// *******************************************************************************************

		transactionsP = new TransactionsPanel(journal);
		tabbedPane.addTab(Messages.getString("MainGUI2.46"), null, transactionsP, Messages.getString("MainGUI2.47")); //$NON-NLS-1$ //$NON-NLS-2$

		// ********************************************
		alertTextArea = new JTextArea();
		alertTextArea.setColumns(8);
		alertTextArea.setRows(10);
		alertTextArea.setEditable(false);
		final JScrollPane alertScrollPane = new JScrollPane(alertTextArea);
		final GridBagConstraints gbc_alertTextArea = new GridBagConstraints();
		gbc_alertTextArea.fill = GridBagConstraints.BOTH;
		gbc_alertTextArea.gridx = 0;
		gbc_alertTextArea.gridy = 1;
		gbc_alertTextArea.weighty = 0.15;
		contentPane.add(alertScrollPane, gbc_alertTextArea);

		updateStockAlerts();
	}

	/**
	 * Export data to CSV file
	 *
	 * @param db
	 *            database to export
	 */
	private void exportCSV(Database db) {
		final int result = fileChooser.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			final File output = fileChooser.getSelectedFile();
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
		users.createFromTextFile("accounts.dat"); //$NON-NLS-1$
		catalogue.createFromTextFile("articles.dat"); //$NON-NLS-1$
		journal.createFromTextFile("log.dat"); //$NON-NLS-1$
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
		taskToDoLabel.setText(Messages.getString("MainGUI2.51")); //$NON-NLS-1$
		lblInProgress.setText(null);
		ticketTextArea.setText(null);
		lblTotalTicket.setText(null);
		lblBalanceAfter.setText(null);
		currentUserAtDesk = null;
		currentItemAtDesk = null;
		lblName.setText(Messages.getString("MainGUI2.52")); //$NON-NLS-1$
		lblBalance.setText(Messages.getString("MainGUI2.53")); //$NON-NLS-1$

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
		lblInProgress.setText(String.valueOf(currentItemAtDesk.getQuantity()) + " x " + currentArticleAtDesk.getName()); //$NON-NLS-1$
		currentItemAtDesk = null;
		currentArticleAtDesk = null;
		printTicketToScreen(currentTicket);
		taskToDoLabel.setText(Messages.getString("MainGUI2.55")); //$NON-NLS-1$
		currentState = TicketState.TICKET_IDLE;
	}

	/**
	 * Contains the logic for input field validation
	 */
	private void inputLogic() {
		try {
			if (inputField.getText().length() > 0) {
				// Validate the content in the TextField
				// TODO: change this so we can accommodate non-integer numbers in a
				// locale-independent way
				switch (currentState) {
					case TICKET_IDLE : // article being chosen
						final long input = Long.parseLong(inputField.getText());
						currentArticleAtDesk = catalogue.getArticleByCode(input);
						if (currentArticleAtDesk != null) {
							currentItemAtDesk = new TicketItem(currentArticleAtDesk);

							if (currentArticleAtDesk.getNumberOfPrices() > 1) {
								// More than 1 price id
								taskToDoLabel.setText(Messages.getString("MainGUI2.56")); //$NON-NLS-1$
								currentState = TicketState.PRICE;
								lblInProgress.setText("? x " + currentArticleAtDesk.getName()); //$NON-NLS-1$
							} else if (!currentArticleAtDesk.isCountable()) {
								// Only 1 price id, quantity to set
								taskToDoLabel.setText(Messages.getString("MainGUI2.58")); //$NON-NLS-1$
								currentState = TicketState.QUANTITY;
								lblInProgress.setText("? x " + currentArticleAtDesk.getName()); //$NON-NLS-1$
							} else {
								// Only 1 price id, no quantity to set: add item to ticket
								currentItemAtDesk.computeAmount();
								finalizeTicketItem();
							}
						} else {
							JOptionPane.showMessageDialog(null, Messages.getString("MainGUI2.60"), //$NON-NLS-1$
									Messages.getString("MainGUI2.61"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
						}
						break;
					case QUANTITY : // quantity being chosen
						final double input2 = Double.parseDouble(inputField.getText());
						if (input2 == 0) {
							JOptionPane.showMessageDialog(null, Messages.getString("MainGUI2.62"), //$NON-NLS-1$
									Messages.getString("MainGUI2.63"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
						} else {
							// Negative quantities are allowed so that corrections are possible
							if (currentArticleAtDesk.isCountable()) {
								currentItemAtDesk.setQuantity((int) input2);
							} else {
								currentItemAtDesk.setQuantity((int) (input2 * 1000));
							}
							currentItemAtDesk.computeAmount();
							finalizeTicketItem();
						}
						break;
					case PRICE : // price being chosen
						final long input3 = Long.parseLong(inputField.getText());
						if (input3 < 0 || input3 >= currentArticleAtDesk.getNumberOfPrices()) {
							JOptionPane.showMessageDialog(null, Messages.getString("MainGUI2.64"), //$NON-NLS-1$
									Messages.getString("MainGUI2.65"), //$NON-NLS-1$
									JOptionPane.WARNING_MESSAGE);
						} else {
							currentItemAtDesk.setFee((int) input3);
							if (!currentArticleAtDesk.isCountable()) {
								taskToDoLabel.setText(Messages.getString("MainGUI2.66")); //$NON-NLS-1$
								currentState = TicketState.QUANTITY;
							} else {
								currentItemAtDesk.computeAmount();
								finalizeTicketItem();
							}
						}
						break;
					default :
						break;
				}
			} else {
				// Validations without content in the TextField
				if (currentTicket.getNumberOfItems() >= 1) {
					if (currentTicket.getPaymentMethod() == null) {
						if (currentUserAtDesk == null) {
							taskToDoLabel.setText(Messages.getString("MainGUI2.67")); //$NON-NLS-1$
							return;
						} else {
							// Save ticket -- pay with current account balance
							finalizeTicket();
							return;
						}
					} else {
						if (currentTicket.getPaymentMethod() == PaymentMethod.CASH) {
							final double paye = Double
									.parseDouble(JOptionPane.showInputDialog(null, Messages.getString("MainGUI2.68"), //$NON-NLS-1$
											Messages.getString("MainGUI2.69"), JOptionPane.QUESTION_MESSAGE)); //$NON-NLS-1$
							JOptionPane.showMessageDialog(null, "You owe " + NumberFormat.getCurrencyInstance() //$NON-NLS-1$
									.format(paye - currentTicket.getAmount()) + ".", //$NON-NLS-1$
									"", JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
						}
						// Save the finished ticket
						finalizeTicket();
						return;
					}
				}
			}
		} catch (final NumberFormatException nfe) {
			JOptionPane.showMessageDialog(null, Messages.getString("MainGUI2.73"), Messages.getString("MainGUI2.74"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.ERROR_MESSAGE);
			nfe.printStackTrace();
		}
	}

	/**
	 *
	 */
	private void onAbout(ActionEvent e) {
		final AboutDialog dlg = new AboutDialog();
		dlg.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		dlg.setVisible(true);
	}

	/**
	 *
	 */
	private void onChangeVendor(ActionEvent e) {
		if (currentTicket != null) {
			final int choice = JOptionPane.showConfirmDialog(null, Messages.getString("MainGUI2.75"), //$NON-NLS-1$
					Messages.getString("MainGUI2.76"), //$NON-NLS-1$
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			// If a ticket is started, we need to confirm the will to quit
			if (choice != JOptionPane.YES_OPTION) {
				return;
			}
		}
		setVisible(false);

		// Go back to password
		final PasswordDialog pdiag = new PasswordDialog();
		pdiag.setVisible(true);
	}

	/**
	 * @param p
	 *            payment method
	 */
	private void onPay(final PaymentMethod p) {
		if (currentTicket != null && currentState == TicketState.TICKET_IDLE) {
			currentTicket.pay(p);

			if (p == PaymentMethod.CASH) {
				final double paye = Double
						.parseDouble(JOptionPane.showInputDialog(null, Messages.getString("MainGUI2.77"), //$NON-NLS-1$
								Messages.getString("MainGUI2.78"), JOptionPane.QUESTION_MESSAGE)); //$NON-NLS-1$

				JOptionPane.showMessageDialog(null, String.format(Messages.getString("MainGUI2.79"), //$NON-NLS-1$
						NumberFormat.getCurrencyInstance().format(paye - currentTicket.getAmount())),
						Messages.getString("MainGUI2.80"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
			} else {
				JOptionPane.showMessageDialog(null, String.format(Messages.getString("MainGUI2.81"), //$NON-NLS-1$
						String.valueOf(currentTicket.getAmount()), p.getName()), Messages.getString("MainGUI2.82"), //$NON-NLS-1$
						JOptionPane.PLAIN_MESSAGE);
			}
			// Save the finished ticket
			finalizeTicket();
			return;
		}
	}

	/**
	 *
	 */
	private void onQuit(ActionEvent e) {
		if (currentTicket != null) {
			final int choice = JOptionPane.showConfirmDialog(null, Messages.getString("MainGUI2.83"), //$NON-NLS-1$
					Messages.getString("MainGUI2.84"), //$NON-NLS-1$
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			// If a ticket is started, we need to confirm the will to quit
			if (choice != JOptionPane.YES_OPTION) {
				return;
			}
		}

		System.exit(0); // Everything is fine, so we stop here
	}

	/**
	 * @param c
	 */
	private void onSelectArticle(final Article c) {
		if (currentState == TicketState.TICKET_IDLE) {
			currentArticleAtDesk = c;
			currentItemAtDesk = new TicketItem(c);
			lblInProgress.setText(String.format(Messages.getString("MainGUI2.85"), c.getName())); //$NON-NLS-1$

			if (c.getNumberOfPrices() > 1) {
				taskToDoLabel.setText(Messages.getString("MainGUI2.86")); //$NON-NLS-1$
				currentState = TicketState.PRICE;
			} else if (!currentArticleAtDesk.isCountable()) {
				// Only 1 price id, quantity to set
				taskToDoLabel.setText(Messages.getString("MainGUI2.87")); //$NON-NLS-1$
				currentState = TicketState.QUANTITY;
				lblInProgress.setText(String.format(Messages.getString("MainGUI2.88"), currentArticleAtDesk.getName())); //$NON-NLS-1$
			} else {
				// Only 1 price id, no quantity to set: add item to ticket
				currentItemAtDesk.computeAmount();
				finalizeTicketItem();
			}
		}
	}

	/**
	 *
	 */
	private void onSelectDefaultUser(ActionEvent e) {
		currentUserAtDesk = null;

		lblName.setText(Messages.getString("MainGUI2.89")); //$NON-NLS-1$
		lblBalance.setText(NumberFormat.getCurrencyInstance().format(0.00));

		if (currentTicket == null) {
			currentTicket = new Ticket(currentUserAtDesk);
			ticketTextArea.setText(null);
		} else {
			currentTicket.setUser(currentUserAtDesk);
		}
		taskToDoLabel.setText(Messages.getString("MainGUI2.90")); //$NON-NLS-1$
		currentState = TicketState.TICKET_IDLE;
	}

	/**
	 *
	 */
	private void onSelectUser(ActionEvent e) {
		final UserChoiceDialog choices = new UserChoiceDialog(users);
		choices.setVisible(true);
		final User nextCUser = choices.getUser();
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
			taskToDoLabel.setText(Messages.getString("MainGUI2.91")); //$NON-NLS-1$
			currentState = TicketState.TICKET_IDLE;
		}
	}

	/**
	 * Display ticket information at the appropriate places in GUI
	 *
	 * @param t
	 *            the ticket to display
	 */
	private void printTicketToScreen(Ticket t) {
		if (t != null) {
			ticketTextArea.setText(null);
			for (final TicketItem ti : t.getItems()) {
				if (ti != null) {
					// Add to GUI component the lines for each article
					ticketTextArea.setText(ticketTextArea.getText() + ti.toString() + "\n"); //$NON-NLS-1$
				}
			}

			// Same for total amount
			lblTotalTicket.setText(NumberFormat.getCurrencyInstance().format(t.getAmount()));

			// Same for balance after purchase
			if (currentUserAtDesk != null) {
				lblBalanceAfter.setText(NumberFormat.getCurrencyInstance().format(t.getBalanceAfterTicket()));
			} else {
				lblBalanceAfter.setText(Messages.getString("MainGUI2.93")); //$NON-NLS-1$
			}
		}
	}

	/**
	 * @param currentVendor
	 *            the currentVendor to set
	 */
	public void setCurrentVendor(Vendor currentVendor) {
		this.currentVendor = currentVendor;
		lblVendorName.setText(currentVendor != null ? currentVendor.getName() : Messages.getString("MainGUI2.94")); //$NON-NLS-1$
	}

	/**
	 * Refreshes the component containing the stock alerts
	 */
	public void updateStockAlerts() {
		alertTextArea.setText(null); // Clean UI component
		final StringBuilder bob = new StringBuilder();

		if (Objects.nonNull(catalogue)) {
			for (final Article a : catalogue.getArray()) {
				if (a != null && a.hasStockMgmtEnabled() && a.hasStockAlertEnabled()) {
					final int stock = a.getStock();
					if (stock <= a.getLimitStock()) {
						bob.append(String.format(Messages.getString("MainGUI2.95"), stock, a.getName())); //$NON-NLS-1$
						if (stock < 0) {
							bob.append(Messages.getString("MainGUI2.96")); //$NON-NLS-1$
						} else {
							bob.append(Messages.getString("MainGUI2.97")); //$NON-NLS-1$
						}
					}
				}
			}
			alertTextArea.setText(bob.toString());
		}
	}

}
