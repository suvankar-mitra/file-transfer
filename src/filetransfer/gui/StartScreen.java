package filetransfer.gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class StartScreen extends JFrame {

	private JPanel contentPane;
	private JButton btnAccept;
	private static boolean accept;
	
	public static boolean isAccept() {
		return accept;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					/*StartScreen frame = new StartScreen();
					frame.setVisible(true);*/
					MainGui.init();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StartScreen() {
		setResizable(false);
		setTitle("File Transfer 1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 649, 460);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		btnAccept = new JButton("Accept");
		btnAccept.setMnemonic(KeyEvent.VK_A);
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				accept=true;
				dispose();
				MainGui.init();
			}
		});
		
		JButton btnDecline = new JButton("Decline");
		btnDecline.setMnemonic(KeyEvent.VK_D);
		btnDecline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}
		});
		
		JLabel lblLicenseAgreement = new JLabel("License Agreement");
		lblLicenseAgreement.setFont(new Font("Tahoma", Font.BOLD, 12));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createParallelGroup(Alignment.TRAILING)
							.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
								.addGap(12)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addContainerGap(458, Short.MAX_VALUE)
								.addComponent(btnDecline)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(btnAccept)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblLicenseAgreement)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGap(7)
					.addComponent(lblLicenseAgreement)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAccept)
						.addComponent(btnDecline))
					.addContainerGap())
		);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		BufferedReader br=null;
		try {
			InputStream is = this.getClass().getResourceAsStream("/res/LICENSE");
			if(is.available()<=0){
				throw new IOException();
			}
			br = new BufferedReader(new InputStreamReader(is));
			String line;
			while((line=br.readLine())!=null){
				textArea.append(" "+line+" "+"\n");
			}
			textArea.setCaretPosition(0);
			try {
				br.close();
			} catch (IOException e1) {}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		contentPane.setLayout(gl_contentPane);
	}
}
