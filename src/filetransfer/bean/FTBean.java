package filetransfer.bean;

import java.io.File;

public class FTBean {
	private String ip="0.0.0.0";
	private int port=5000;
	private File fileName;
	private String password="";

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public File getFileName() {
		return fileName;
	}
	public void setFileName(File fileName) {
		this.fileName = fileName;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}	
	@Override
	public String toString() {
		return ip+" "+port;
	}
}
