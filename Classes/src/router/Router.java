package router;

import host.Computer;

import java.io.Serializable;

public class Router implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 882600327580940045L;
	private String ip;
	private Computer computer;
	private int x;
	private int y;
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Router(String ip) {
		setIP(ip);
	}

	public String getIP() {
		return ip;
	}

	private void setIP(String name) {
		this.ip = name;
	}

	public void setComputer(Computer computer) {
		this.computer = computer;
	}

	public Computer getComputer() {
		return computer;
	}

	@Override
	public boolean equals(Object obj) {
		Router other = (Router) obj;
		return ip.equals(other.ip);
	}
}
