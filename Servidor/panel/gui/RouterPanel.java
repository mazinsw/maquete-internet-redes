package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class RouterPanel extends JPanel implements MouseListener,
		MouseMotionListener, MouseWheelListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4283803484510880016L;
	private ImageIcon imgIcon;
	private Image img;
	private ImageIcon imgIconSel;
	private Image imgSel;
	private ImageIcon imgIconComp;
	private Image imgComp;
	private ImageIcon imgIconCompSel;
	private Image imgCompSel;
	private List<Node> nodes;
	private List<Edge> edges;
	private boolean editMode;
	private Node editingNode;
	private boolean creatingEdge;
	private Node linkingNode;
	private Edge linkingEdge;
	private Edge selectedEdge;
	private double zoom;
	private int offsetX;
	private int offsetY;
	private boolean shifting;
	private int shiftingX;
	private int shiftingY;
	private int movingX;
	private int movingY;
	private List<RouterChangeListener> listener;
	private int editingX;
	private int editingY;
	private boolean readOnly;
	private Node selectedNode;

	public RouterPanel() {
		imgIcon = getImageIcon("router");
		img = imgIcon.getImage();
		imgIconComp = getImageIcon("computer");
		imgComp = imgIconComp.getImage();

		imgIconSel = getImageIcon("router_selected");
		imgSel = imgIconSel.getImage();
		imgIconCompSel = getImageIcon("computer_selected");
		imgCompSel = imgIconCompSel.getImage();
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		linkingNode = new Node();
		editMode = false;
		zoom = 1;
		listener = new ArrayList<RouterChangeListener>();
		scale(1);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		setFocusable(true);
		setRequestFocusEnabled(true);
	}

	public void addNode(String text, Object tag) throws Exception {
		Node node = new Node();
		node.setTag(tag);
		node.setText(text);
		if (nodeExists(node))
			throw new Exception("Node " + text + " already exists");
		synchronized (nodes) {
			nodes.add(node);
		}

		node.setDrawX(new Random().nextInt(getWidth() - img.getWidth(null))
				- offsetX);
		node.setDrawY(new Random().nextInt(getHeight() - img.getHeight(null))
				- offsetY);
		applyZoom(node);
		repaint();
	}

	public Object getSelected() {
		if (selectedNode == null)
			return null;
		return selectedNode.getTag();
	}

	private Node addNode(Node node) {
		for (Node n : nodes) {
			if (node.equals(n))
				return n;
		}
		synchronized (nodes) {
			nodes.add(node);
		}
		return node;
	}

	private boolean nodeExists(Node node) {
		for (Node n : nodes) {
			if (node.equals(n))
				return true;
		}
		return false;
	}

	private boolean edgeExists(Edge edge) {
		for (Edge e : edges) {
			if (edge.equals(e))
				return true;
		}
		return false;
	}

	public void addEdge(Node a, Node b, double weight) throws Exception {
		// TODO Auto-generated method stub
		Edge edge = new Edge();
		edge.setA(addNode(a));
		edge.setB(addNode(b));
		edge.setWeight(weight);
		if (edgeExists(edge) || a.equals(b))
			throw new Exception("Edge " + a.getText() + " -> " + b.getText()
					+ " already exists");
		scaleNode(a);
		scaleNode(b);
		synchronized (edges) {
			edges.add(edge);
		}
		repaint();
	}

	public void removeNode(Object tag) throws Exception {
		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if (tag.equals(node.getTag())) {
				nodes.remove(i);
				for (int j = edges.size() - 1; j >= 0; j--) {
					Edge edge = edges.get(j);
					if (tag.equals(edge.getA().getTag())
							|| tag.equals(edge.getB().getTag())) {
						edges.remove(j);
					}
				}
				repaint();
				for (RouterChangeListener rcl : listener) {
					rcl.nodeDeleted(node.getTag());
				}
				return;
			}
		}
		throw new Exception("Node not found");
	}

	public void clear() {
		nodes.clear();
		edges.clear();
		repaint();
	}

	public void setComputer(String text, Object tag, boolean hasComputer)
			throws Exception {
		for (Node node : nodes) {
			if (tag.equals(node.getTag())) {
				node.setText(text);
				node.setComputer(hasComputer);
				repaint();
				return;
			}
		}
		//throw new Exception("Node " + text + " not found");
	}

	public void addChangeListener(RouterChangeListener l) {
		listener.add(l);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		synchronized (nodes) {

			for (Node node : nodes) {
				int x = node.getDrawX() - img.getWidth(null) / 2;
				int y = node.getDrawY() - img.getHeight(null) / 2;
				int xM = node.getDrawX();
				int yM = node.getDrawY() + img.getHeight(null) / 2;
				if (node == selectedNode)
					g.drawImage(imgSel, x + offsetX, y + offsetY, this);
				else
					g.drawImage(img, x + offsetX, y + offsetY, this);
				if (node.hasComputer()) {
					if (node == selectedNode)
						g.drawImage(imgCompSel, x + offsetX,
								y + offsetY - imgComp.getHeight(null)
										+ (int) (img.getHeight(null) * 0.5),
								this);
					else
						g.drawImage(imgComp, x + offsetX,
								y + offsetY - imgComp.getHeight(null)
										+ (int) (img.getHeight(null) * 0.5),
								this);
				}
				g2d.setColor(Color.BLUE);
				if (node.getText() != null) {
					int tw = g2d.getFontMetrics().stringWidth(node.getText());
					int th = g2d.getFontMetrics().getHeight();
					g2d.drawString(node.getText(), xM + offsetX - tw / 2, yM
							+ offsetY + th);
				}
			}

		}

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);
		synchronized (edges) {

			for (Edge edge : edges) {
				int xA = edge.getA().getDrawX();
				int yA = edge.getA().getDrawY();
				int xB = edge.getB().getDrawX();
				int yB = edge.getB().getDrawY();
				int xM = (xA + xB) / 2;
				int yM = (yA + yB) / 2;
				// draw weight
				String str = String.valueOf(edge.getWeight());
				int tw = g2d.getFontMetrics().stringWidth(str);
				int th = g2d.getFontMetrics().getHeight();
				g2d.setColor(Color.WHITE);
				g2d.fillRoundRect(xM + offsetX - 4, yM + offsetY - th, tw + 8,
						th + 4, 6, 6);
				g2d.setColor(Color.BLACK);
				g2d.drawRoundRect(xM + offsetX - 4, yM + offsetY - th, tw + 8,
						th + 4, 6, 6);
				g2d.setColor(Color.BLUE);
				g2d.drawString(str, xM + offsetX, yM + offsetY);
				// draw line
				if (edge == selectedEdge)
					g2d.setColor(Color.ORANGE);
				else
					g2d.setColor(Color.BLACK);
				g2d.drawLine(xA + offsetX, yA + offsetY, xB + offsetX, yB
						+ offsetY);
			}

		}

	}

	private void applyZoom(Node node) {
		int x = node.getDrawX() - getWidth() / 2;
		int y = node.getDrawY() - getHeight() / 2;
		x = (int) (x / zoom);
		y = (int) (y / zoom);
		x = x + getWidth() / 2;
		y = y + getHeight() / 2;
		node.setX(x);
		node.setY(y);
	}

	private void scaleNode(Node node) {
		int x = node.getX() - getWidth() / 2;
		int y = node.getY() - getHeight() / 2;
		x = (int) (x * zoom);
		y = (int) (y * zoom);
		x = x + getWidth() / 2;
		y = y + getHeight() / 2;
		node.setDrawX(x);
		node.setDrawY(y);
	}

	private void scale(double zoom) {
		img = imgIcon.getImage().getScaledInstance(
				(int) (imgIcon.getIconWidth() * zoom * 0.2),
				(int) (imgIcon.getIconHeight() * zoom * 0.2),
				Image.SCALE_SMOOTH);
		imgSel = imgIconSel.getImage().getScaledInstance(
				(int) (imgIconSel.getIconWidth() * zoom * 0.2),
				(int) (imgIconSel.getIconHeight() * zoom * 0.2),
				Image.SCALE_SMOOTH);
		imgComp = imgIconComp.getImage().getScaledInstance(
				(int) (imgIconComp.getIconWidth() * zoom * 0.8),
				(int) (imgIconComp.getIconHeight() * zoom * 0.8),
				Image.SCALE_SMOOTH);
		imgCompSel = imgIconCompSel.getImage().getScaledInstance(
				(int) (imgIconCompSel.getIconWidth() * zoom * 0.8),
				(int) (imgIconCompSel.getIconHeight() * zoom * 0.8),
				Image.SCALE_SMOOTH);
		// img = resizeImage(imgIcon, zoom * 0.2);
		// imgComp = resizeImage(imgIconComp, zoom * 0.8);
		for (Node node : nodes) {
			scaleNode(node);
		}
		repaint();
	}

	public ImageIcon getImageIcon(String name) {
		try {
			return new ImageIcon(getClass()
					.getResource("/img/" + name + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (readOnly)
			return;
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
			Edge edge = getEdgeAt(e.getX(), e.getY());
			if (edge != null) {
				// read in the second number from user as a string
				String number = JOptionPane
						.showInputDialog("Digite o peso da aresta:");
				// convert numbers from type String to type int
				try {
					double weight = Double.parseDouble(number);
					double oldWeight = edge.getWeight();
					edge.setWeight(weight);
					if (oldWeight != weight && !creatingEdge) {
						for (RouterChangeListener rcl : listener) {
							rcl.weightChanged(edge.getA().getTag(), edge.getB()
									.getTag(), weight);
						}
					}
					repaint();
				} catch (Exception e1) {
					// TODO: handle exception
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON2) {
			shifting = true;
			shiftingX = e.getX();
			shiftingY = e.getY();
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			if (readOnly)
				return;
			Node node = getNodeAt(e.getX(), e.getY());
			if (node != null) {
				editMode = true;
				editingNode = node;
				movingX = e.getX();
				movingY = e.getY();
				editingX = e.getX();
				editingY = e.getY();
			}
		} else if (e.getButton() == MouseEvent.BUTTON1) {
			grabFocus();
			Node node = getNodeAt(e.getX(), e.getY());
			if (node != null) {
				if (readOnly) {
					if (selectedNode != node) {
						selectedNode = node;
						repaint();
						for (RouterChangeListener rcl : listener) {
							rcl.nodeSelected(selectedNode.getTag());
						}
					}
					return;
				}
				if (creatingEdge == false) {
					creatingEdge = true;
					linkingEdge = new Edge();
					linkingEdge.setA(node);
					linkingNode.setDrawX(e.getX() - offsetX);
					linkingNode.setDrawY(e.getY() - offsetY);
					applyZoom(linkingNode);
					linkingEdge.setB(linkingNode);
					edges.add(linkingEdge);
					selectedNode = node;
					repaint();
				} else {
					if (node != linkingEdge.getA()) {
						creatingEdge = false;
						linkingEdge.setB(node);
						boolean canAdd = true;
						// check if already exits an edge between these two
						// nodes
						for (int i = 0; i < edges.size() - 1; i++) {
							Edge edge = edges.get(i);
							if (edge.equals(linkingEdge)) {
								edges.remove(edges.size() - 1);
								canAdd = false;
								break;
							}
						}
						if (selectedNode != node) {
							selectedNode = node;
							for (RouterChangeListener rcl : listener) {
								rcl.nodeSelected(selectedNode.getTag());
							}
						}
						repaint();
						if (canAdd) {
							for (RouterChangeListener rcl : listener) {
								rcl.edgeAdded(linkingEdge.getA().getTag(),
										linkingEdge.getB().getTag(),
										linkingEdge.getWeight());
							}
						}
					}
				}
			} else {
				Edge edge = getEdgeAt(e.getX(), e.getY());
				if (edge != selectedEdge) {
					selectedEdge = edge;
					repaint();
				} else if (selectedNode != null) {
					for (RouterChangeListener rcl : listener) {
						rcl.nodeUnSelected(selectedNode.getTag());
					}
					selectedNode = null;
					repaint();
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (editMode) {
			editMode = false;
			if (e.getX() == editingX && e.getY() == editingY)
				return;
			for (RouterChangeListener rcl : listener) {
				rcl.posChanged(editingNode.getTag(), editingNode.getX(),
						editingNode.getY());
			}
		}
		if (e.getButton() == MouseEvent.BUTTON2 && shifting) {
			shifting = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (editMode) {
			editingNode.setDrawX(editingNode.getDrawX() + e.getX() - movingX);
			editingNode.setDrawY(editingNode.getDrawY() + e.getY() - movingY);
			movingX = e.getX();
			movingY = e.getY();
			applyZoom(editingNode);
			repaint();
		} else if (creatingEdge) {
			linkingNode.setDrawX(e.getX() - offsetX);
			linkingNode.setDrawY(e.getY() - offsetY);
			applyZoom(linkingNode);
			repaint();
		} else if (shifting) {
			offsetX += e.getX() - shiftingX;
			offsetY += e.getY() - shiftingY;
			shiftingX = e.getX();
			shiftingY = e.getY();
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (creatingEdge) {
			linkingNode.setDrawX(e.getX() - offsetX);
			linkingNode.setDrawY(e.getY() - offsetY);
			applyZoom(linkingNode);
			repaint();
		}
	}

	private Node getNodeAt(int x, int y) {
		for (int i = nodes.size() - 1; i >= 0; i--) {
			Node node = nodes.get(i);
			if ((node.getDrawX() + offsetX - img.getWidth(null) / 2 <= x)
					&& (node.getDrawX() + offsetX + img.getWidth(null) / 2 >= x))
				if ((node.getDrawY() + offsetY - img.getHeight(null) / 2 <= y)
						&& (node.getDrawY() + offsetY + img.getHeight(null) / 2 >= y))
					return node;
		}
		return null;
	}

	private Edge getEdgeAt(int x2, int y2) {
		for (int i = edges.size() - 1; i >= 0; i--) {
			Edge edge = edges.get(i);
			int x1 = edge.getA().getDrawX() + offsetX;
			int y1 = edge.getA().getDrawY() + offsetY;
			int x3 = edge.getB().getDrawX() + offsetX;
			int y3 = edge.getB().getDrawY() + offsetY;
			double d;
			int dist = (x1 - x3) * (x1 - x3) + (y1 - y3) * (y1 - y3);
			if (dist == 0) {
				dist = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
				d = Math.sqrt(dist);
			} else {
				double t = ((x2 - x1) * (x3 - x1) + (y2 - y1) * (y3 - y1))
						/ (double) dist;
				if (t < 0) {
					dist = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
					d = Math.sqrt(dist);
				} else if (t > 1) {
					dist = (x2 - x3) * (x2 - x3) + (y2 - y3) * (y2 - y3);
					d = Math.sqrt(dist);
				} else {
					double x = x1 + t * (x3 - x1);
					double y = y1 + t * (y3 - y1);
					d = Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
				}
			}
			if (Math.abs(d) < 4)
				return edge;
		}
		return null;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		int notches = e.getWheelRotation();
		if (notches < 0) {
			zoom += 0.04;
			scale(zoom);
		} else {
			if (zoom >= 0.08) {
				zoom -= 0.04;
				scale(zoom);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (readOnly)
			return;
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (creatingEdge) {
				edges.remove(edges.size() - 1);
				creatingEdge = false;
				repaint();
			} else if (selectedEdge != null) {
				selectedEdge = null;
				repaint();
			}
		}
		if (selectedEdge != null && e.getKeyCode() == KeyEvent.VK_DELETE) {
			edges.remove(selectedEdge);
			Edge temp = selectedEdge;
			selectedEdge = null;
			repaint();
			for (RouterChangeListener rcl : listener) {
				rcl.edgeDeleted(temp.getA().getTag(), temp.getB().getTag());
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public List<Edge> getEdges() {
		List<Edge> list = new ArrayList<Edge>();
		for (Edge edge : edges) {
			list.add(edge.copy());
		}
		return list;
	}

}
