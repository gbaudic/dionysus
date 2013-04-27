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

import javax.swing.JDialog;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import java.awt.Insets;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

/**
 * The dialog box for properties and proper credits
 *
 */
public class AboutDialog extends JDialog {
	public AboutDialog() {
		setTitle("About");
		setResizable(false);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{444, 0};
		gridBagLayout.rowHeights = new int[]{249, 23, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblLogo = new JLabel("icon");
		GridBagConstraints gbc_lblLogo = new GridBagConstraints();
		gbc_lblLogo.insets = new Insets(0, 0, 5, 5);
		gbc_lblLogo.gridx = 0;
		gbc_lblLogo.gridy = 0;
		panel.add(lblLogo, gbc_lblLogo);
		
		JLabel lblSoftwareName = new JLabel("Dionysus v");
		lblSoftwareName.setHorizontalAlignment(SwingConstants.LEFT);
		lblSoftwareName.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblSoftwareName = new GridBagConstraints();
		gbc_lblSoftwareName.anchor = GridBagConstraints.WEST;
		gbc_lblSoftwareName.insets = new Insets(0, 0, 5, 0);
		gbc_lblSoftwareName.gridx = 1;
		gbc_lblSoftwareName.gridy = 0;
		panel.add(lblSoftwareName, gbc_lblSoftwareName);
		
		JLabel lblDesc = new JLabel("A shop management software written in Java");
		GridBagConstraints gbc_lblDesc = new GridBagConstraints();
		gbc_lblDesc.anchor = GridBagConstraints.WEST;
		gbc_lblDesc.insets = new Insets(0, 0, 5, 0);
		gbc_lblDesc.gridx = 1;
		gbc_lblDesc.gridy = 1;
		panel.add(lblDesc, gbc_lblDesc);
		
		JLabel lblCopyright = new JLabel("Copyright 2011-2013 podgy_piglet");
		lblCopyright.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblCopyright = new GridBagConstraints();
		gbc_lblCopyright.insets = new Insets(0, 0, 5, 0);
		gbc_lblCopyright.anchor = GridBagConstraints.WEST;
		gbc_lblCopyright.gridx = 1;
		gbc_lblCopyright.gridy = 2;
		panel.add(lblCopyright, gbc_lblCopyright);
		
		JLabel lblLicenseInfo = new JLabel("license goes here");
		GridBagConstraints gbc_lblLicenseInfo = new GridBagConstraints();
		gbc_lblLicenseInfo.gridx = 1;
		gbc_lblLicenseInfo.gridy = 4;
		panel.add(lblLicenseInfo, gbc_lblLicenseInfo);
		
		JButton btnOK = new JButton("OK");
		GridBagConstraints gbc_btnOK = new GridBagConstraints();
		gbc_btnOK.anchor = GridBagConstraints.NORTH;
		gbc_btnOK.gridx = 0;
		gbc_btnOK.gridy = 1;
		getContentPane().add(btnOK, gbc_btnOK);
	}

	private static final long serialVersionUID = 3797170463816470428L;

}
