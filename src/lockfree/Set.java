package lockfree;

public interface Set {
	public boolean contains(int value);
	public boolean add(int value);
	public boolean remove(int value);
	public void print();
}
