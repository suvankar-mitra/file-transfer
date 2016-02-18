package filetransfer.core.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import filetransfer.bean.FTBean;

public final class Sender {
	private static ServerSocket serverSocket=null;
	private static Socket socket=null;
	private static volatile boolean isConnected=false;
	private static DataOutputStream dout=null;
	private static int totalByteSentPercentage=0;
	private static String connectionSpeed;
	private static String file=null;
	private Sender(){}
	
	public static String getFile() {
		return file;
	}

	public static boolean isConnected() {
		return isConnected;
	}

	public static String getConnectionSpeed() {
		return connectionSpeed;
	}

	public static int getTotalByteSentPercentage() {
		return totalByteSentPercentage;
	}

	public static void init(FTBean bean) throws Exception {
		if(serverSocket==null || !serverSocket.isBound())
			serverSocket=new ServerSocket(bean.getPort());

		System.out.println(" ***Waiting for receiver to join...");
		serverSocket.setSoTimeout(60*1000); 	//1 minute timeout
		socket = serverSocket.accept();
		System.out.println(" ***Receiver joined->"+socket.getInetAddress());
		isConnected=true;

		DataInputStream din = new DataInputStream(socket.getInputStream());
		dout = new DataOutputStream(socket.getOutputStream());


		String recvPswd = din.readUTF();
		if(bean.getPassword().equals(recvPswd)) {
			System.out.println("Password matches.");
		}
		else{
			dout.writeUTF("Password mismatch.");
			throw new Exception("Password mismatch.");
		}		
		dout.writeUTF(" Passwords match!");
	}

	public static void send(FTBean bean) throws IOException{
		if(!isConnected)	return;
		File file=bean.getFileName();
		if(file.isDirectory()) {
			try{		
				dout.writeBoolean(true); 	//sending a directory -> true
				dout.writeUTF(file.getName());
				File[] listOfFiles = file.listFiles();
				int count = listOfFiles.length;
				for(File f:listOfFiles)
					if(f.isDirectory() || !f.canRead())
						count--;
				dout.writeInt(count); 		// n ->> Its a directory , sending bunch of files
				for(File fileToSend : listOfFiles) {
					sendFile(dout, fileToSend);
				}
			}finally{
				dout.close();
				socket.close();
				System.out.println("\n");
				System.out.println(" ***File sent successfully!");
			}
		}
		else {
			try{
				dout.writeBoolean(false); 	//sending a directory -> false, i.e. sending a file
				dout.writeInt(1);		// 1 ->> Sending only one file
				sendFile(dout, file);
				System.out.println("\n");
				System.out.println(" ***File sent successfully!");
			}finally{
				dout.close();
				socket.close();
				System.out.println("\n");
				System.out.println(" ***File sent successfully!");
			}
		}
	}

	public static synchronized void closeAllConnection() throws IOException{
		if(isConnected){
			if(serverSocket!=null){
				serverSocket.close();
				serverSocket=null;
			}
			if(socket!=null){
				socket.close();
				socket=null;
			}
			isConnected=false;
		}
	}
	/*
	public static void init(int port, String file) throws Exception{
		if(serverSocket==null){
			serverSocket = new ServerSocket(port);
		}
		else if(serverSocket!=null && !serverSocket.isBound())
			serverSocket = new ServerSocket(port);
		listen(file);
	}
	private static void listen(String fileName) throws Exception {
		File file = new File(fileName);
		if(!file.exists()){
			throw new FileNotFoundException(file.getName()+" file not found!");
		}

		System.out.println(" ***Waiting for receiver to join...");
		serverSocket.setSoTimeout(60*1000); 	//1 minute timeout
		Socket socket = serverSocket.accept();
		System.out.println(" ***Receiver joined->"+socket.getInetAddress());
		isConnected=true;

		System.out.println("\t------------");
		try{
			System.out.println("\tYour IP address is:: "+CheckIP.checkIp());
			System.out.println("\tShare your IP address with receiver.");
		} catch(Exception e) {
			System.out.println("\t!!! Problem:: "+e.getMessage());
			System.out.println("\tYou will not be able to send files over Internet,\n\t"
					+ " Though you still can send files inside your work/home network.");
		}
		System.out.println("\t------------");
		System.out.println(" Sending file:: "+file.getName());

		//password to authorize receiver
		System.out.print(" Enter a password [Press enter for no password]:: ");
		char[] pswd = System.console().readPassword();

		System.out.println(" ***Waiting for receiver to join...");
		serverSocket.setSoTimeout(60*1000); 	//1 minute timeout
		Socket socket = serverSocket.accept();
		System.out.println(" ***Receiver joined->"+socket.getInetAddress());

		DataInputStream din = new DataInputStream(socket.getInputStream());
		DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

		String recvPswd = din.readUTF();
		if(String.valueOf(pswd).equals(recvPswd)) {
			System.out.println(" ***Password matches.");
		}
		else{
			dout.writeUTF(" Passwords mismatch!");
			throw new Exception("Password mismatch.");
		}

		dout.writeUTF(" Passwords match!");
		if(file.isDirectory()) {
			System.out.print(" You chose a directory to send? Send all files in this directory? Y/N:: ");
			Scanner sc = new Scanner(System.in);
			String choice = sc.nextLine();
			sc.close();
			if(choice.equalsIgnoreCase("y")){
				try{		
					dout.writeBoolean(true); 	//sending a directory -> true
					dout.writeUTF(file.getName());
					File[] listOfFiles = file.listFiles();
					int count = listOfFiles.length;
					for(File f:listOfFiles)
						if(f.isDirectory() || !f.canRead())
							count--;
					dout.writeInt(count); 		// n ->> Its a directory , sending bunch of files
					for(File fileToSend : listOfFiles) {
						sendFile(dout, fileToSend);
					}
				}finally{
					dout.close();
					System.out.println("\n");
					System.out.println(" ***File sent successfully!");
				}
			}
		}
		else {
			try{
				dout.writeBoolean(false); 	//sending a directory -> false, i.e. sending a file
				dout.writeInt(1);		// 1 ->> Sending only one file
				sendFile(dout, file);
				System.out.println("\n");
				System.out.println(" ***File sent successfully!");
			}finally{
				dout.close();
			}
		}
	}*/

	private static void sendFile(DataOutputStream dout, File file) throws IOException {
		if(file.isDirectory()) {

		}
		else{
			Sender.file=file.getName();
			System.out.println("\n\n\tSending file:: "+file.getName());
			dout.writeUTF(file.getName());		//sending file name
			long fileSize=file.length();
			dout.writeLong(fileSize);		//sending file size
			int count;
			byte[] buffer = new byte[1024*512];	//512KB buffer size
			System.out.println();
			FileInputStream fis = new FileInputStream(file);
			double totalByteSent=0;
			long startTime = new Date().getTime();
			long time ;
			try {
				while((count=fis.read(buffer,0,(int)Math.min(buffer.length, fileSize)))>0) {
					totalByteSent += count;
					totalByteSentPercentage=(int)Math.ceil((100.0/fileSize) * totalByteSent);
					time = 1 + (new Date().getTime() - startTime)/1000;		//1 is added to overcome divide by zero error for first time
					dout.write(buffer,0,count);
					connectionSpeed=(((totalByteSent/time)/1024) > 1024 ? 
							((int)(((totalByteSent/time)/1024)/1024)+" MBps") : ((int)((totalByteSent/time)/1024))+" KBps");
					/*System.out.print("\r [[ Progress:: "+(int)Math.ceil((100.0/fileSize) * totalByteSent) + "% ]]");
					System.out.print(" [[ Time:: "+ (int)(time/60) + " min " + time%60 + " sec ]]");
					System.out.print(" [[ Speed::" + (((totalByteSent/time)/1024) > 1024 ? 
							((int)(((totalByteSent/time)/1024)/1024)+" MBps") : ((int)((totalByteSent/time)/1024))+" KBps") +
							" ]] ");
					System.out.print("          ");*/
				}
			}
			finally {
				if(fis!=null)
					fis.close();
			}
		}
	}
}
