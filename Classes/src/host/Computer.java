package host;

import java.io.Serializable;

public class Computer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4169965920142543236L;
	private String name;
	private String ip;

	public Computer(String computerName, String localIP) {
		setIP(localIP);
		setName(computerName);
	}

	public String getIP() {
		return ip;
	}

	private void setIP(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}
}
