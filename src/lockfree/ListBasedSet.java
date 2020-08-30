package lockfree;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ListBasedSet implements Set { // fine-grained locking
	private Node head = new Node(Integer.MIN_VALUE);

	public ListBasedSet() {
		head.next = new Node(Integer.MAX_VALUE);
	}
	
	@Override
	public boolean contains(int value) {
		head.lock();
		Node pred = head;
		try {
			Node curr = pred.next;
			curr.lock();
			try {
				while(curr.value < value) {
					pred.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock();
				}
				return curr.value == value;
			} finally {
				curr.unlock();
			}
		} finally {
			pred.unlock();
		}
	}
	
	@Override
	public boolean add(int value) { // fine-grained locking
		Node newNode = new Node(value);

		head.lock();
		Node pred = head;
		try {
			Node curr = pred.next;
			curr.lock();
			try {
				while(curr.value < value) { // hand-over-hand locking
					pred.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock();
				}
				if(curr.value == value) return false;
				else {
					pred.next = newNode;
					newNode.next = curr;
					return true;
				}
			} finally {
				curr.unlock();
			}
		} finally {
			pred.unlock();
		}
	}

	@Override
	public boolean remove(int value) {
		return false; // TODO
	}

	@Override
	public void print() {
		head.lock();
		Node pred = head;
		try {
			Node curr = pred.next;
			curr.lock();
			try {
				while(curr.next != null) {
					System.out.print(curr.value + " ");
					
					pred.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock();
				}
				System.out.println();
			} finally {
				curr.unlock();
			}
		} finally {
			pred.unlock();
		}
	}
	
	class Node {
		int value;
		Node next = null;
		
		private Lock lock = new ReentrantLock();
		public Node(int value) {
			this.value = value;
		}
		
		public void lock() {
			lock.lock();
		}
		
		public void unlock() {
			lock.unlock();
		}
	}
	
	public static void main(String[] args) {
		ListBasedSet set = new ListBasedSet();
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
