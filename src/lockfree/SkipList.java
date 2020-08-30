package lockfree;

public class SkipList implements Set { // TODO: making thread-safe
	private static final int MAX_LEVEL = 10;
	
	private Node head = new Node(Integer.MIN_VALUE);

	public SkipList() {
		Node tail = new Node(Integer.MAX_VALUE);
		for(int i = 0; i < MAX_LEVEL; i++) {
			head.next[i] = tail;
		}
	}
	
	@Override
	public boolean contains(int value) {
		return find(value) == null;
	}
	
	private Node[] find(int value) {
		Node[] preds = new Node[MAX_LEVEL];
		
		Node pred = head;
		for(int i = MAX_LEVEL - 1; i >= 0; i--) {
			Node curr = pred.next[i];
			while(curr.value < value) {
				pred = curr;
				curr = curr.next[i];
			}
			
			if(curr.value == value) return null;
			else preds[i] = pred;
		}
		return preds;
	}

	@Override
	public boolean add(int value) {
		Node newNode = new Node(value);
		
		int level = getLevel();
		Node[] preds = find(value);

		if(preds == null) return false;
		else {
			for(int i = level; i >= 0; i--) {
				newNode.next[i] = preds[i].next[i];
				preds[i].next[i] = newNode;
			}
		}
		
		return true;
	}
	
	private int getLevel() {
		int level = 0;
		while(Math.random() < 0.5 && level < MAX_LEVEL - 1) {
			level++;
		}
		return level;
	}

	@Override
	public boolean remove(int value) {
		return false; // TODO
	}

	@Override
	public void print() {
		System.out.println("Printing set (per element)");
		Node curr = head;
		while(curr != null) {
			for(int i = 0; i < MAX_LEVEL; i++) {
				if(curr.next[i] != null) System.out.printf("%2d ", curr.value);
			}
			curr = curr.next[0];
			System.out.println();
		}
		
		System.out.println();
		System.out.println("Printing set (per level)");
		for(int i = MAX_LEVEL - 1; i >= 0; i--) {
			curr = head;
			while(curr != null) {
				if(curr.next[i] != null) System.out.printf("%2d ", curr.value);
				curr = curr.next[i];
			}
			System.out.println();
		}
	}

	class Node {
		int value;
		Node[] next = new Node[MAX_LEVEL];
		
		public Node(int value) {
			this.value = value;
		}
	}
	
	public static void main(String[] args) {
		SkipList set = new SkipList();
		for(int i = 10; i < 20; i++) {
			set.add(i);
		}
		set.add(4);
		set.add(2);
		set.add(6);
		set.add(7);
		set.add(3);
		set.add(1);
		set.add(9);
		set.add(5);
		set.add(5);
		set.add(5);
//		set.add(8);
		set.print();
		System.out.println(set.contains(16));
		System.out.println(set.contains(8));
	}
}
