package synchronization.locks;

import synchronization.Lock;
import synchronization.Semaphore;

public class SemaphoreLock implements Lock {
	Semaphore lock = new Semaphore(1);

	@Override
	public void acquire(int tid) {
		try {
			lock.acquire();
		} catch (InterruptedException e) {}
	}

	@Override
	public void release(int tid) {
		lock.release();
	}
	
}
