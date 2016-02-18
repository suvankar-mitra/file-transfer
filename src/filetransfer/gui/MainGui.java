package filetransfer.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import filetransfer.bean.FTBean;
import filetransfer.core.client.Receiver;
import filetransfer.core.server.CheckIP;
import filetransfer.core.server.Sender;

@SuppressWarnings("serial")
public class MainGui extends JFrame {

	private JPanel contentPane;
	private JPanel panel;
	private JTextField txtHost;
	private JTextField txtPort;
	private JButton btnReceive;
	private JButton btnSend;
	private JButton btnConnect;
	private JLabel lblPublish;
	private JLabel lblDate;
	private JLabel lblMessage;

	private boolean send=false;
	private boolean receive=false;

	/**
	 * Launch the application.
	 */
	public static void init() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui frame = new MainGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainGui() {
		setBackground(Color.WHITE);
		setTitle("File Transfer 1.0");
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainGui.class.getResource("/res/icon.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 531, 386);
		//menu bar
		JMenuBar mnuBar=new Menu();
		setJMenuBar(mnuBar);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240,240,240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));

		JPanel pnlConnnect = new JPanel();
		pnlConnnect.setBackground(Color.WHITE);
		pnlConnnect.setEnabled(false);
		pnlConnnect.setBorder(new LineBorder(new Color(0, 0, 0)));

		JSeparator separator = new JSeparator();

		lblDate = new JLabel("date");
		lblDate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblDate.setText(new Date().toString().split(" ")[0] +" - "+ new Date().toString().split(" ")[1] +
						" - "+new Date().toString().split(" ")[2]);
			}
		});

		new Thread(new Runnable() {			
			@Override
			public void run() {
				while(true){
					lblDate.setText(new Date().toString().split(" ")[3] +" - "+ new Date().toString().split(" ")[4]);
					try{
						Thread.sleep(1000);
					}catch(InterruptedException e){}
				}
			}
		}).start();

		lblMessage = new JLabel("");

		JButton btnNext = new JButton("Next >");
		btnNext.setEnabled(false);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//MainGui.this.setVisible(false);
				dispose();
				if(send)
					SendFilesGui.send();
				else if(receive)
					ReceiveFilesGui.receive();
			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(503, Short.MAX_VALUE)
					.addComponent(lblMessage)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(106)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(pnlConnnect, 0, 0, Short.MAX_VALUE)
						.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE))
					.addGap(104))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(433, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addContainerGap())
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(separator, GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDate)
					.addContainerGap(478, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(pnlConnnect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addGap(18)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(9)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblMessage)
						.addComponent(lblDate)))
		);

		JLabel lblHost = new JLabel("Host :");
		lblHost.setEnabled(false);

		JLabel lblPort = new JLabel("Port :");
		lblPort.setEnabled(false);

		txtHost = new JTextField();
		txtHost.setEditable(false);
		txtHost.setText("127.0.0.1");
		txtHost.setEnabled(false);
		txtHost.setColumns(10);

		txtPort = new JTextField();
		txtPort.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		txtPort.setText("5000");
		txtPort.setEnabled(false);
		txtPort.setColumns(10);

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FTBean bean=new FTBean();
				JPasswordField pf = new JPasswordField();
				String password="";
				int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (okCxl == JOptionPane.OK_OPTION) {
					password = new String(pf.getPassword());
					if(password.isEmpty()){
						JOptionPane.showMessageDialog(null, "Please note that you did not set a password.");
					}
					//System.err.println("You entered: " + password);
					else
						bean.setPassword(password);
				}
				else if(okCxl == JOptionPane.CANCEL_OPTION){
					JOptionPane.showMessageDialog(null, "Please note that you declined to set a password."
							+ "\nSetting a password is recommended.");
				}
				btnNext.setEnabled(false);
				/**
				 * Sender
				 */
				if(btnConnect.getText().equalsIgnoreCase("publish")){
					
					txtPort.setEditable(false);
					bean.setPort(Integer.parseInt(txtPort.getText()));
					/**
					 * integrate Sender class
					 */
					Thread senderThread=new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								Sender.init(bean);
							} catch (Exception e) {
								if(e.getMessage().equals("Password mismatch.")){	//passwords did not match!!
									JOptionPane.showMessageDialog(null, "Password entered by the receiver did not match!", "Warning", JOptionPane.ERROR_MESSAGE);
									//dispose();
									try {
										Sender.closeAllConnection();
									} catch (IOException e1) {
									}
									dispose();
									MainGui.init();
								}
							}
						}
					});
					senderThread.start();					
					
					new Thread(new Runnable() {						
						@Override
						public void run() {
							btnConnect.setEnabled(false);
							int t=59;
							try {
								while(!Sender.isConnected() && t>=0){
									lblPublish.setText("");
									lblMessage.setText("Waiting for receiver: ("+t+" sec)");
									t--;
									try{Thread.sleep(1000);}
									catch(InterruptedException e){}
								}
							} catch (Exception e1) {								
								e1.printStackTrace();
							}						
							if(t<0){
								senderThread.interrupt();
								lblPublish.setText("Publish sender  ->");
								lblMessage.setText("Connection time out.");
								btnConnect.setEnabled(true);
								txtPort.setEditable(true);
							}
							else if(t>=0 && Sender.isConnected()){
								lblMessage.setText("Connection successfull.");
								btnNext.setEnabled(true);
							}
						}
					}).start();
					
				}
				/**
				 * Receiver
				 */
				else if(btnConnect.getText().equalsIgnoreCase("connect")){
					btnConnect.setMnemonic(KeyEvent.VK_C);
					txtHost.setEditable(false);
					txtPort.setEditable(false);
					bean.setIp(txtHost.getText());
					bean.setPort(Integer.parseInt(txtPort.getText()));
					/**
					 * integrate with Receiver class
					 */
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try {
								btnConnect.setEnabled(false);
								Receiver.init(bean);
								lblPublish.setText("");
								lblMessage.setText("Connecton Successfull.");
								btnNext.setEnabled(true);
							} catch (Exception e1) {
								if(e1.getMessage().equals("Password mismatch.")){	//passwords did not match!!
									JOptionPane.showMessageDialog(null, "Password entered did not match!", "Warning", JOptionPane.ERROR_MESSAGE);
									dispose();
									try {
										Receiver.closeAllConnection();
									} catch (IOException e2) {
									}
									MainGui.init();
									//System.exit(0);
								}
								e1.printStackTrace();
								lblMessage.setText(e1.getMessage());
								txtHost.setEditable(true);
								txtPort.setEditable(true);
								btnConnect.setEnabled(true);
							}
						}
					}).start();
				}
			}
		});
		btnConnect.setEnabled(false);

		JLabel lblConnectionInformation = new JLabel("Connection Details");
		lblConnectionInformation.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblConnectionInformation.setEnabled(false);

		lblPublish = new JLabel("");
		lblPublish.setEnabled(false);
		GroupLayout gl_pnlConnnect = new GroupLayout(pnlConnnect);
		gl_pnlConnnect.setHorizontalGroup(
				gl_pnlConnnect.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlConnnect.createSequentialGroup()
						.addGap(21)
						.addGroup(gl_pnlConnnect.createParallelGroup(Alignment.LEADING)
								.addComponent(lblConnectionInformation)
								.addGroup(gl_pnlConnnect.createSequentialGroup()
										.addComponent(lblHost)
										.addGap(18)
										.addComponent(txtHost, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_pnlConnnect.createParallelGroup(Alignment.TRAILING, false)
										.addGroup(gl_pnlConnnect.createSequentialGroup()
												.addComponent(lblPublish, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(btnConnect))
										.addGroup(Alignment.LEADING, gl_pnlConnnect.createSequentialGroup()
												.addComponent(lblPort)
												.addGap(18)
												.addComponent(txtPort, 198, 198, 198))))
						.addContainerGap(27, Short.MAX_VALUE))
				);
		gl_pnlConnnect.setVerticalGroup(
				gl_pnlConnnect.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlConnnect.createSequentialGroup()
						.addGap(13)
						.addComponent(lblConnectionInformation)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_pnlConnnect.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblHost)
								.addComponent(txtHost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(18)
						.addGroup(gl_pnlConnnect.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPort)
								.addComponent(txtPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_pnlConnnect.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPublish)
								.addComponent(btnConnect))
						.addGap(10))
				);
		pnlConnnect.setLayout(gl_pnlConnnect);

		JLabel lblWhatDoYou = new JLabel("What do you want to do?");
		lblWhatDoYou.setFont(new Font("Tahoma", Font.BOLD, 12));

		btnSend = new JButton("Send");
		btnSend.setMnemonic(KeyEvent.VK_S);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				send=true;
				receive=false;

				btnSend.setEnabled(false);
				btnReceive.setEnabled(false);

				lblConnectionInformation.setEnabled(true);
				lblWhatDoYou.setEnabled(false);
				lblHost.setEnabled(true);
				lblPort.setEnabled(true);
				txtHost.setEnabled(true);
				txtPort.setEnabled(true);
				lblPublish.setText("Publish Sender  ->");
				btnConnect.setMnemonic(KeyEvent.VK_P);
				
				new Thread(new Runnable() {

					@Override
					public void run() {
						FTBean bean=CheckIP.checkIpBean();
						txtHost.setText(bean.getIp());
					}
				}).start();

				btnConnect.setText("Publish");
				btnConnect.setEnabled(true);
				btnConnect.setToolTipText("Let everyone know that you are ready to send file(s).");
			}
		});
		btnSend.setToolTipText("Send file(s) to someone.");

		btnReceive = new JButton("Receive");
		btnReceive.setMnemonic(KeyEvent.VK_R);
		btnReceive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send=false;
				receive=true;

				btnSend.setEnabled(false);
				btnReceive.setEnabled(false);
				btnConnect.setMnemonic(KeyEvent.VK_C);
				
				lblConnectionInformation.setEnabled(true);
				lblWhatDoYou.setEnabled(false);
				lblHost.setEnabled(true);
				lblPort.setEnabled(true);
				txtHost.setEnabled(true);
				txtHost.setEditable(true);
				txtPort.setEnabled(true);

				btnConnect.setEnabled(true);
				btnConnect.setToolTipText("Connect with sender to receive file(s).");
				lblPublish.setText("Connect with sender  ->");
			}
		});
		btnReceive.setToolTipText("Receive file(s) from someone.");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
						.addGap(36)
						.addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
						.addComponent(btnReceive)
						.addGap(28))
				.addGroup(gl_panel.createSequentialGroup()
						.addGap(65)
						.addComponent(lblWhatDoYou)
						.addContainerGap(65, Short.MAX_VALUE))
				);
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblWhatDoYou)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(btnReceive)
								.addComponent(btnSend))
						.addContainerGap())
				);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}
}
