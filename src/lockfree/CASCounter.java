package lockfree;

import java.util.concurrent.atomic.AtomicInteger;

public class CASCounter {
	private AtomicInteger counter = new AtomicInteger(0);
	
	public void increment() {
		int old;
		do {
			old = counter.get();
		} while(!counter.compareAndSet(old, old+1));
	}
	
	public void incrementWithBackoff() {
		while(true) {
			int old = counter.get();
			if(counter.compareAndSet(old, old+1)) return;
			else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {}
			}
		}
	}
	
	public int get() {
		return counter.get();
	}
}
