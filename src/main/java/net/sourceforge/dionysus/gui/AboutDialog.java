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

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The dialog box for properties and proper credits
 */
public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 3797170463816470428L;

	/**
	 * Constructor
	 */
	public AboutDialog() {
		setTitle(Messages.getString("AboutDialog.0")); //$NON-NLS-1$
		setResizable(false);
		setModal(true);
		setSize(400, 420);
		setLocationRelativeTo(null);
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 444, 0 };
		gridBagLayout.rowHeights = new int[] { 249, 23, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		final JPanel panel = new JPanel();
		final GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		getContentPane().add(panel, gbc_panel);
		final GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		final JLabel lblLogo = new JLabel(Messages.getString("AboutDialog.1")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblLogo = new GridBagConstraints();
		gbc_lblLogo.insets = new Insets(0, 0, 5, 5);
		gbc_lblLogo.gridx = 0;
		gbc_lblLogo.gridy = 0;
		panel.add(lblLogo, gbc_lblLogo);

		final JLabel lblSoftwareName = new JLabel(Constants.SOFTWARE_NAME + Messages.getString("AboutDialog.2") //$NON-NLS-1$
				+ Constants.SOFTWARE_VERSION + Messages.getString("AboutDialog.3") + Constants.SOFTWARE_VERSION_NICK); //$NON-NLS-1$
		lblSoftwareName.setHorizontalAlignment(SwingConstants.LEFT);
		lblSoftwareName.setFont(new Font("Tahoma", Font.BOLD, 20)); //$NON-NLS-1$
		final GridBagConstraints gbc_lblSoftwareName = new GridBagConstraints();
		gbc_lblSoftwareName.anchor = GridBagConstraints.WEST;
		gbc_lblSoftwareName.insets = new Insets(0, 0, 5, 0);
		gbc_lblSoftwareName.gridx = 1;
		gbc_lblSoftwareName.gridy = 0;
		panel.add(lblSoftwareName, gbc_lblSoftwareName);

		final JLabel lblDesc = new JLabel(Messages.getString("AboutDialog.5")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblDesc = new GridBagConstraints();
		gbc_lblDesc.anchor = GridBagConstraints.WEST;
		gbc_lblDesc.insets = new Insets(0, 0, 5, 0);
		gbc_lblDesc.gridx = 1;
		gbc_lblDesc.gridy = 1;
		panel.add(lblDesc, gbc_lblDesc);

		final JLabel lblCopyright = new JLabel(Messages.getString("AboutDialog.6") //$NON-NLS-1$
				+ Messages.getString("AboutDialog.7")); //$NON-NLS-1$
		lblCopyright.setHorizontalAlignment(SwingConstants.LEFT);
		final GridBagConstraints gbc_lblCopyright = new GridBagConstraints();
		gbc_lblCopyright.insets = new Insets(0, 0, 5, 0);
		gbc_lblCopyright.anchor = GridBagConstraints.WEST;
		gbc_lblCopyright.gridx = 1;
		gbc_lblCopyright.gridy = 2;
		panel.add(lblCopyright, gbc_lblCopyright);

		final JLabel lblIconCopyright = new JLabel(Messages.getString("AboutDialog.8")); //$NON-NLS-1$
		lblIconCopyright.setHorizontalAlignment(SwingConstants.LEFT);
		final GridBagConstraints gbc_lblIconCopyright = new GridBagConstraints();
		gbc_lblIconCopyright.anchor = GridBagConstraints.WEST;
		gbc_lblIconCopyright.insets = new Insets(0, 0, 5, 0);
		gbc_lblIconCopyright.gridx = 1;
		gbc_lblIconCopyright.gridy = 3;
		panel.add(lblIconCopyright, gbc_lblIconCopyright);

		final Component verticalStrut = Box.createVerticalStrut(20);
		final GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut.gridx = 1;
		gbc_verticalStrut.gridy = 4;
		panel.add(verticalStrut, gbc_verticalStrut);

		final JLabel lblLicenseInfo = new JLabel(Messages.getString("AboutDialog.9")); //$NON-NLS-1$
		final GridBagConstraints gbc_lblLicenseInfo = new GridBagConstraints();
		gbc_lblLicenseInfo.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblLicenseInfo.gridx = 1;
		gbc_lblLicenseInfo.gridy = 5;
		panel.add(lblLicenseInfo, gbc_lblLicenseInfo);

		final JButton btnOK = new JButton(Messages.getString("AboutDialog.11"), Constants.ok); //$NON-NLS-1$
		final GridBagConstraints gbc_btnOK = new GridBagConstraints();
		gbc_btnOK.anchor = GridBagConstraints.NORTH;
		gbc_btnOK.gridx = 0;
		gbc_btnOK.gridy = 1;
		btnOK.addActionListener(arg0 -> setVisible(false));
		getContentPane().add(btnOK, gbc_btnOK);
	}

}
