package filetransfer.gui;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import filetransfer.bean.FTBean;
import filetransfer.core.server.CheckIP;

@SuppressWarnings("serial")
public class Menu extends JMenuBar{
	
	public Menu() {
		JMenu mnFile = new JMenu("File");
		this.add(mnFile);

		JMenuItem mntmSaveHostport = new JMenuItem("Save Host/Port");
		mnFile.add(mntmSaveHostport);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//dispose();
				System.exit(0);
			}
		});
		
		JMenuItem mntmRestart = new JMenuItem("Restart");
		mntmRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*try {
					Sender.closeAllConnection();
					Receiver.closeAllConnection();
					dispose();
					MainGui.main(null);
				}catch(Exception e1){}*/
			}
		});
		mnFile.add(mntmRestart);
		mnFile.add(mntmExit);

		JMenu mnNetwork = new JMenu("Network");
		this.add(mnNetwork);

		JMenuItem mntmTestConnection = new JMenuItem("Test Connection");
		mntmTestConnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							FTBean bean=CheckIP.checkIpBean();
							bean.getIp();
							JOptionPane.showMessageDialog(null, "Your computer is connected with the Internet."
									+ "\nYour public IP is:   "+CheckIP.checkIp()
									+ "\nYour private IP is:  "+Inet4Address.getLocalHost().getHostAddress()
									,"Connection Test",JOptionPane.INFORMATION_MESSAGE);
						}
						catch(Exception e1){

							try {
								JOptionPane.showMessageDialog(null, "Your computer is not connected with the Internet."+
										"\nYour private IP is:  "+Inet4Address.getLocalHost().getHostAddress(),"Connection Test",JOptionPane.OK_OPTION);
							} catch (HeadlessException | UnknownHostException e) {
							}
						}
					}
				}).start();

			}
		});
		mnNetwork.add(mntmTestConnection);

		JMenu mnHelp = new JMenu("Help");
		this.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				About.about();
			}
		});

		JMenuItem mntmHelp = new JMenuItem("Help (?)");
		mnHelp.add(mntmHelp);
		mnHelp.add(mntmAbout);
	}
}
