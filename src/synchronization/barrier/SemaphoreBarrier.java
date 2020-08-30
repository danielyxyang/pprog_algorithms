package synchronization.barrier;

import synchronization.Barrier;
import synchronization.Semaphore;

public class SemaphoreBarrier implements Barrier {
	final int nThreads;
	
	volatile int count = 0;
	Object lock = new Object();
	Semaphore barrier1 = new Semaphore(0); // entry-turnstile (initial: closed)
	Semaphore barrier2 = new Semaphore(1); // exit-turnstile (initial: open)

	public SemaphoreBarrier(int nThreads) {
		this.nThreads = nThreads;
	}

	@Override
	public void await() throws InterruptedException {
		synchronized(lock) {
			count++;
			if(count == nThreads) {
				barrier2.acquire(); // close exit-tunstile
				barrier1.release(); // open entry-turnstile
			}
		}
		barrier1.acquire(); // waiting at entry-turnstile
		barrier1.release();
		
		synchronized(lock) {
			count--;
			if(count == 0) {
				barrier1.acquire(); // close entry-turnstile
				barrier2.release(); // open exit-turnstile
			}
		}
		barrier2.acquire(); // waiting at exit-turnstile
		barrier2.release();
	}
	
}
