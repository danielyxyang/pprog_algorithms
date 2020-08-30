package synchronization.locks.algorithms;

import java.util.concurrent.atomic.AtomicIntegerArray;

import synchronization.Lock;

public class PetersonLock implements Lock {
	volatile int victim = 0;
	AtomicIntegerArray flags = new AtomicIntegerArray(2);
	
	@Override
	public void acquire(int tid) {
		flags.set(tid, TRUE); 								// I'm interested.
		victim = tid; 										// But I will wait.
		while(flags.get(1-tid) == TRUE && victim == tid); 	// Wait if the other one is interested AND I have to wait.
	}

	@Override
	public void release(int tid) {
		flags.set(tid, FALSE);
	}

}
