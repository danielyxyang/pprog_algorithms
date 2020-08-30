package producerconsumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Channel {
	private ReentrantLock lock = new ReentrantLock(true);
	private Condition consumer = lock.newCondition();
	private Condition producer = lock.newCondition();
	
	private int[] buffer;
	private int capacity;
	
	private int first;
	private int last;
	private int count;
	
	public Channel(int capacity) {
		this.capacity = capacity;
		this.buffer = new int[capacity];
		first = last = 0;
		count = 0;
	}
	
	public void enqueue(int data) {
		lock.lock();
		try {
			while(count == capacity) {
				try {
					producer.await();
				} catch (InterruptedException e) {}
			}
			
			buffer[last] = data;
			last = next(last);
			count++;
			consumer.signalAll();	
		} finally {
			lock.unlock();
		}
	}
	
	public int dequeue() {
		lock.lock();
		try {
			while(count == 0) {
				try {
					consumer.await();
				} catch (InterruptedException e) {}
			}
			
			int data = buffer[first];
			first = next(first);
			count--;
			producer.signalAll();
			
			return data;
		} finally {
			lock.unlock();
		}
	}
	
	private int next(int index) {
		return (index + 1) % capacity;
	}
}
