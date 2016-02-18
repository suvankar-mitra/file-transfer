package filetransfer.core.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import filetransfer.bean.FTBean;

public class CheckIP {
	public static String checkIp() throws Exception {
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine(); //you get the IP as a String
			return ip;
		} catch(IOException e) {
			throw new Exception("Internet connection is down.");
		}
	}
	public static FTBean checkIpBean() {
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine(); //you get the IP as a String
			FTBean bean=new FTBean();
			bean.setIp(ip);
			return bean;
		} catch(IOException e) {
			throw new RuntimeException("Internet connection is down.");
		}
	}
}
