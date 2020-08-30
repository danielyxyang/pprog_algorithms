package synchronization;

public class Semaphore {
	volatile int count;
	
	public Semaphore(int init) {
		this.count = init;
	}
	
	public synchronized void acquire() throws InterruptedException {
		while(count <= 0) wait();
		count--;
	}
	
	public synchronized void release() {
		count++;
		notifyAll();
	}
}
