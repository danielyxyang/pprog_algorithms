package synchronization.barrier;

import synchronization.Barrier;

public class MonitorBarrier implements Barrier {
	private static final int ENTER = 0;
	private static final int DRAIN = 1;
	final int nThreads;
	
	volatile int count = 0;
	volatile int phase = ENTER;

	public MonitorBarrier(int nThreads) {
		this.nThreads = nThreads;
	}
	
	public synchronized void await() throws InterruptedException {
		while(phase != ENTER) wait(); // waiting to enter barrier
		
		count++;
		if(count == nThreads) {
			phase = DRAIN; // enabling threads to leave barrier
			notifyAll();
		}
		
		while(phase != DRAIN) wait(); // waiting to leave barrier
		
		count--;
		if(count == 0) {
			phase = ENTER; // enabling threads to enter barrier
			notifyAll();
		}
	}
}
