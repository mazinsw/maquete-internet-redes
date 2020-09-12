package common;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

public class Util {
	public static String getComputerName() {
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			return addr.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getLocalIP() {
		Enumeration<NetworkInterface> nets;
		try {
			nets = NetworkInterface.getNetworkInterfaces();
	        for (NetworkInterface netint : Collections.list(nets)) {
	            if(!netint.isLoopback() && !netint.isVirtual() ) {
	            	Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
	                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
	                	if(inetAddress instanceof Inet4Address)
	                		return inetAddress.getHostAddress();
	                }
	            }
	        }
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "";
	}
}
