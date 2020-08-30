package synchronization.locks;

import synchronization.Lock;

public class MonitorLock implements Lock {

	volatile boolean locked = false;
	
	@Override
	public synchronized void acquire(int tid) {
		while(locked) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		locked = true;
	}

	@Override
	public synchronized void release(int tid) {
		locked = false;
		notify();
	}
	
}
