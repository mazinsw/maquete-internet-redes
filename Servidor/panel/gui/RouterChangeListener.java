package gui;

public interface RouterChangeListener {
	public void weightChanged(Object tagA, Object tagB, double weight);
	public void posChanged(Object tag, int x, int y);
	public void edgeAdded(Object tagA, Object tagB, double weight);
	public void edgeDeleted(Object tagA, Object tagB);
	public void nodeDeleted(Object tag);
	public void nodeSelected(Object tag);
	public void nodeUnSelected(Object tag);
}
