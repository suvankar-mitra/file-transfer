package filetransfer.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
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
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import filetransfer.bean.FTBean;
import filetransfer.core.client.Receiver;

@SuppressWarnings("serial")
public class ReceiveFilesGui extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JFileChooser chooser;
	private JButton btnCancel;
	private JButton btnReceive;
	private JLabel lblInformation;
	private JProgressBar progressBar;
	private JLabel lblSpeed;
	private JLabel lblReceiving;
	private JLabel label;
	private JLabel lblProgress;
	private JLabel lblPercentage;

	/**
	 * Launch the application.
	 */
	public static void receive() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReceiveFilesGui frame = new ReceiveFilesGui();
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
	public ReceiveFilesGui() {
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

		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);

		JButton btnSelect = new JButton("select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Save file into...");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					textField.setText(chooser.getSelectedFile().toString());
					btnSelect.setEnabled(false);
					btnCancel.setEnabled(false);
					btnReceive.setEnabled(true);
					lblProgress.setEnabled(true);
					lblSpeed.setEnabled(true);
					label.setEnabled(true);
				}
			}
		});

		JLabel lblChooseALocation = new JLabel("Choose a location to save the file");
		lblChooseALocation.setFont(new Font("Tahoma", Font.BOLD, 12));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
										.addComponent(textField, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnSelect))
								.addComponent(lblChooseALocation))
						.addContainerGap())
				);
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblChooseALocation)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSelect))
						.addContainerGap(19, Short.MAX_VALUE))
				);
		panel.setLayout(gl_panel);

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

		btnReceive = new JButton("Receive");
		btnReceive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnReceive.setEnabled(false);
				btnCancel.setEnabled(true);
				lblInformation.setText("Receiving file(s)");
				
				/**
				 * time thread
				 */
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						int time=0;
						int min=0;
						int hr=0;
						while(!lblInformation.getText().equals("File(s) received successfully.")){
							if(time<60 && min<1) label.setText(time+" s");
							else if(min>=1 && hr<1) label.setText(min+" m "+time+" s");
							else if(hr>=1) label.setText(hr+" h "+min+" m "+time+" s");
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
						while(Receiver.getTotalByteReadPercentage()<=100) {
							lblReceiving.setText(Receiver.getFile());
							lblPercentage.setText(Receiver.getTotalByteReadPercentage()+" %");
							progressBar.setValue(Receiver.getTotalByteReadPercentage());
							lblSpeed.setText(Receiver.getConnectionSpeed());
							try {
								Thread.sleep(100);
							}catch(InterruptedException e){}
						}
					}
				}).start();

				/**
				 * main thread to receive files
				 */
				new Thread(new Runnable() {

					@Override
					public void run() {
						FTBean bean=new FTBean();
						bean.setFileName(new File(textField.getText()));
						try {
							Receiver.receive(bean);
							lblInformation.setText("File(s) received successfully."); ////time thread is depending on this label
							btnCancel.setText("Close");
						} catch (IOException e1) {
							lblInformation.setText(e1.getMessage());
							e1.printStackTrace();
						}
					}
				}).start();
			}
		});
		btnReceive.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnReceive.setEnabled(false);

		lblInformation = new JLabel("");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBackground(Color.WHITE);
		
		lblReceiving = new JLabel("");
		
		lblProgress = new JLabel("Progress");
		lblProgress.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblProgress.setEnabled(false);
		
		label = new JLabel("0 s");
		label.setEnabled(false);
		
		lblSpeed = new JLabel("0.0Kbps");
		lblSpeed.setEnabled(false);
		
		progressBar = new JProgressBar();
		progressBar.setEnabled(false);
		
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
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.RELATED, 430, Short.MAX_VALUE)
					.addComponent(lblSpeed)
					.addGap(8))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(11)
					.addComponent(lblReceiving, GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
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
							.addComponent(lblReceiving))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblPercentage)))
					.addPreferredGap(ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSpeed)
						.addComponent(label))
					.addGap(6))
		);
		panel_1.setLayout(gl_panel_1);

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
									.addComponent(lblDate, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, 357, Short.MAX_VALUE)
									.addComponent(lblInformation))
								.addComponent(separator, GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE))
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btnReceive, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 305, GroupLayout.PREFERRED_SIZE)
							.addGap(104))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 505, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(27)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnReceive)
						.addComponent(btnCancel))
					.addGap(18)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDate)
						.addComponent(lblInformation)))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
