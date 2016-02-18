package filetransfer.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import filetransfer.bean.FTBean;
import filetransfer.core.server.Sender;

@SuppressWarnings("serial")
public class SendFilesGui extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JButton btnSelect;
	private JLabel lblProgress;
	private JProgressBar progressBar;
	private JLabel lblTime;
	private JLabel lblSpeed;
	private JButton btnCancel;
	private JButton btnSend;
	private JFileChooser chooser;
	private JLabel lblInformation;
	private JLabel lblSending;
	private JLabel lblPercentage;

	/**
	 * Launch the application.
	 */
	public static void send() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SendFilesGui frame = new SendFilesGui();
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
	public SendFilesGui() {
		setTitle("File Transfer 1.0");
		setIconImage(Toolkit.getDefaultToolkit().getImage(SendFilesGui.class.getResource("/res/icon.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 541, 386);
		
		JMenuBar menuBar=new Menu();
		setJMenuBar(menuBar);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JSeparator separator = new JSeparator();

		JLabel lblDate = new JLabel("date");
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

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);

		JLabel lblChooseAFile = new JLabel("Choose a File to Send");
		lblChooseAFile.setFont(new Font("Tahoma", Font.BOLD, 12));

		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);

		btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Send File");
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					//System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
					//System.out.println("getSelectedFile() : " + chooser.getSelectedFile());

					if(chooser.getSelectedFile().isDirectory()){
						Toolkit.getDefaultToolkit().beep();
						int dialogResult = JOptionPane.showConfirmDialog (null, 
								"You chose a directory to send.\nPlease note that all the files "
										+ "in this directory will be sent,"
										+ "\nany sub-directory will be excluded.\nYou want to proceed?","Warning",JOptionPane.YES_NO_OPTION);
						if(dialogResult==JOptionPane.YES_OPTION){
							textField.setText(chooser.getSelectedFile().toString());
							lblProgress.setEnabled(true);
							lblSpeed.setEnabled(true);
							lblTime.setEnabled(true);
							btnSend.setEnabled(true);
							FTBean bean=new FTBean();
							bean.setFileName(chooser.getSelectedFile());
						}
					}
					else{
						textField.setText(chooser.getSelectedFile().toString());
						lblProgress.setEnabled(true);
						lblSpeed.setEnabled(true);
						lblTime.setEnabled(true);
						btnSend.setEnabled(true);
						FTBean bean=new FTBean();
						bean.setFileName(chooser.getSelectedFile());
					}

				} else {
					System.out.println("No Selection ");
				}
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
										.addComponent(textField, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnSelect))
								.addComponent(lblChooseAFile))
						.addContainerGap())
				);
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblChooseAFile)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSelect))
						.addContainerGap(19, Short.MAX_VALUE))
				);
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBackground(Color.WHITE);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnCancel.getText().equalsIgnoreCase("Close")){
					dispose();
					System.exit(0);
				}
				dispose();
				System.exit(ABORT);
			}
		});
		btnCancel.setEnabled(false);

		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelect.setEnabled(false);
				btnCancel.setEnabled(true);
				btnSend.setEnabled(false);
				
				lblInformation.setText("Sending file(s)");
				/**
				 * time thread
				 */
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						int time=0;
						int min=0;
						int hr=0;
						while(!lblInformation.getText().equals("File(s) sent successfully.")){
							if(time<60 && min<1) lblTime.setText(time+" s");
							else if(min>=1 && hr<1) lblTime.setText(min+" m "+time+" s");
							else if(hr>=1) lblTime.setText(hr+" h "+min+" m "+time+" s");
							time++;
							if(time>=60){
								time=time%60;
								min++;
							}
							if(min>=60){
								min%=60;
								hr++;
							}
							try{
								Thread.sleep(1000);
							}catch(InterruptedException e){}
						}
					}
				}).start();
				/**
				 * Progress bar
				 */
				new Thread(new Runnable() {					
					@Override
					public void run() {
						while(Sender.getTotalByteSentPercentage()<=100) {
							lblSending.setText(Sender.getFile());
							lblPercentage.setText(Sender.getTotalByteSentPercentage()+" %");
							progressBar.setValue(Sender.getTotalByteSentPercentage());
							lblSpeed.setText(Sender.getConnectionSpeed());
							try {
								Thread.sleep(100);
							}catch(InterruptedException e){}
						}
					}
				}).start();				
				
				/**
				 * main thread to send file
				 */
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						FTBean bean=new FTBean();
						bean.setFileName(new File(textField.getText().trim()));
						try {
							Sender.send(bean);
							lblInformation.setText("File(s) sent successfully.");	//time thread is depending on this label
							btnCancel.setText("Close");
						} catch (IOException e1) {
							lblInformation.setText(e1.getMessage());
							e1.printStackTrace();
						}
					}
				}).start();
			}
		});
		btnSend.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnSend.setEnabled(false);
		
		lblInformation = new JLabel("");

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(separator, GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE))
						.addComponent(lblInformation, Alignment.TRAILING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addContainerGap(348, Short.MAX_VALUE)
							.addComponent(btnCancel)
							.addGap(18)
							.addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addContainerGap())
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(108, Short.MAX_VALUE)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 305, GroupLayout.PREFERRED_SIZE)
					.addGap(102))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDate, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(360, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(27)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSend)
						.addComponent(btnCancel))
					.addGap(18)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 3, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblInformation)
						.addComponent(lblDate)))
		);

		progressBar = new JProgressBar();
		progressBar.setEnabled(false);

		lblProgress = new JLabel("Progress");
		lblProgress.setEnabled(false);
		lblProgress.setFont(new Font("Tahoma", Font.BOLD, 12));

		lblSpeed = new JLabel("0.0Kbps");
		lblSpeed.setEnabled(false);

		lblTime = new JLabel("0 s");
		lblTime.setEnabled(false);
		
		lblSending = new JLabel("");
		
		lblPercentage = new JLabel("");
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(12)
					.addComponent(lblTime)
					.addPreferredGap(ComponentPlacement.RELATED, 430, Short.MAX_VALUE)
					.addComponent(lblSpeed)
					.addGap(8))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(11)
					.addComponent(lblSending, GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblPercentage)
					.addContainerGap())
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblProgress)
					.addContainerGap(440, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblProgress)
					.addGap(11)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(14)
							.addComponent(lblSending))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblPercentage)))
					.addPreferredGap(ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSpeed)
						.addComponent(lblTime))
					.addGap(6))
		);
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);
	}
}
