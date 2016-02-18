package filetransfer.core.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import filetransfer.bean.FTBean;

public final class Receiver {
	private static Socket socket = null;
	private static boolean isConnected=false;
	private static DataOutputStream dout=null;
	private static DataInputStream din=null;
	private static int totalByteReadPercentage=0;
	private static String connectionSpeed;
	private static String file;

	public static String getFile() {
		return file;
	}

	public static String getConnectionSpeed() {
		return connectionSpeed;
	}

	public static int getTotalByteReadPercentage() {
		return totalByteReadPercentage;
	}

	public boolean isConnected() {
		return isConnected;
	}

	private Receiver(){}
	
	public static void init(FTBean bean) throws Exception{
		Receiver.socket = new Socket(bean.getIp(), bean.getPort());
		System.out.println(" ***connection successfull with sender "+socket.getInetAddress());
		isConnected=true;
		dout = new DataOutputStream(socket.getOutputStream());
		din = new DataInputStream(socket.getInputStream());	
		
		dout.writeUTF(bean.getPassword());
		dout.flush();
		
		//debug
		String match=din.readUTF();	//print->password matches
		if(match.equals("Password mismatch.")){
			throw new Exception(match);
		}
	}
	
	public static void closeAllConnection() throws IOException{
		if(isConnected){
			if(socket!=null){
				socket.close();
				socket=null;
			}
			isConnected=false;
		}
	}
	/*public static void init(String host, int port) throws Exception {
		Receiver.socket = new Socket(host, port);
		System.out.println(" ***connection successfull with sender "+socket.getInetAddress());
		
		System.out.print(" Enter password provided by sender:: ");
		char[] pswd = System.console().readPassword();
		DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
		dout.writeUTF(String.valueOf(pswd));
		dout.flush();
		
		DataInputStream din = new DataInputStream(socket.getInputStream());		
		
		//debug
		System.out.println(din.readUTF());
		
		boolean isReceivingDirectory = din.readBoolean();
		String dirName = "";
		if(isReceivingDirectory){
			dirName = din.readUTF();
		}
		int noOfFiles = din.readInt();	//number of files going to receive
		System.out.println(" Number of files to receive:: "+noOfFiles+"\n");
		int count = 0;
		while(noOfFiles>count){
			try {
				receive(din, count+1, dirName);
			}catch(Exception e){
				throw e;
			}
			count++;
		}
	}*/
	public static void receive(FTBean bean) throws IOException{
		boolean isReceivingDirectory = din.readBoolean();
		String dirName = bean.getFileName().getAbsolutePath()+"/";	//where to save
		if(isReceivingDirectory){
			dirName += din.readUTF();
		}
		int noOfFiles = din.readInt();	//number of files going to receive
		System.out.println(" Number of files to receive:: "+noOfFiles+"\n");
		int count = 0;
		while(noOfFiles>count){
			try {
				receiveFile(din, count+1, dirName);
			}catch(Exception e){
				throw e;
			}
			count++;
		}
	}
	private static void receiveFile(DataInputStream din,int fileNumber, String dirName) throws IOException{
		String file = din.readUTF();	//received file name
		Receiver.file=file;
		file = file.substring(file.lastIndexOf("\\")+1);
		long fileSize = din.readLong();		//received file size
		System.out.println(" "+fileNumber+". Receiving file:: "+file);
		if(new File(file).exists()){			
			new File(file).delete();			
		}
		int count=0;
		byte[] buffer = new byte[1024*128];		//128KB buffer size
		if(!dirName.isEmpty()) {
			new File(dirName).mkdir();
			file = dirName + "/" + file;
		}
		FileOutputStream fos = new FileOutputStream(new File(file));
		double totalByteRead=0;
		final long size = fileSize;
		long start = new Date().getTime();
		try{
			System.out.println();			
			while(fileSize>0 && (count=din.read(buffer,0,(int)Math.min(buffer.length, fileSize)))>0){
				totalByteRead += count;
				totalByteReadPercentage=(int)Math.ceil((100.0/size) * totalByteRead);
				long elapsed = (new Date().getTime() - start)/1000;
				connectionSpeed=(((totalByteRead/elapsed)/1024) > 1024 ? 
						((int)(((totalByteRead/elapsed)/1024)/1024)+" MBps") : ((int)((totalByteRead/elapsed)/1024))+" KBps");
				/*System.out.print("\r [[ Progress:: "+(int)Math.ceil((100.0/size) * totalByteRead) + "% ]]");
				System.out.print(" [[ Time:: "+ (int)(elapsed/60) + " min " + elapsed%60 + " sec ]]");
				System.out.print(" [[ Speed::" + (((totalByteRead/elapsed)/1024) > 1024 ? 
						((int)(((totalByteRead/elapsed)/1024)/1024)+" MBps") : ((int)((totalByteRead/elapsed)/1024))+" KBps") +
						" ]] ");
				System.out.print("          ");*/
				fos.write(buffer,0,count);
				fileSize -= count;
			}
		}
		catch(Exception e){
			throw e;
		} finally{
			fos.close();
		}
		System.out.println("\n");
		if(size==new File(file).length())
			System.out.println(" ***File received successfully!\n");
		else
			System.out.println(" ***File receive Unsuccessful!\n");
	}
}
