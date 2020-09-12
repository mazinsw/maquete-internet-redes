package gui;

public class Node {
	private int x;
	private int y;
	private int dx;
	private int dy;
	private Object tag;
	private String text;
	private boolean withComputer;

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

	public void setDrawX(int dx) {
		this.dx = dx;
	}

	public void setDrawY(int dy) {
		this.dy = dy;
	}
	
	public int getDrawX() {
		return dx;
	}

	public int getDrawY() {
		return dy;
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setComputer(boolean flag) {
		withComputer = flag;		
	}

	public boolean hasComputer() {
		return withComputer;
	}

	public Node copy() {
		Node node = new Node();
		
		node.dx = dx;
		node.dy =dy;
		node.tag = tag;
		node.text = text;
		node.withComputer = withComputer;
		node.x = x;
		node.y = y;
		return node;
	}
	
	@Override
	public boolean equals(Object arg0) {
		Node other =(Node)arg0;
		return other.text.equals(text);
	}

}
