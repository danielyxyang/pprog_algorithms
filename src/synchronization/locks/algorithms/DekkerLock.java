package synchronization.locks.algorithms;

import java.util.concurrent.atomic.AtomicIntegerArray;

import synchronization.Lock;

public class DekkerLock implements Lock {
	volatile int turn = 0;
	AtomicIntegerArray flags = new AtomicIntegerArray(2);
	
	@Override
	public void acquire(int tid) {
		flags.set(tid, TRUE); 				// I am interested
		while(flags.get(1-tid) == TRUE) { 	// The other one is interested ...
			if(turn != tid) { 				// ... and it is his turn.
				flags.set(tid, FALSE); 			// I am not interested.
				while(turn != tid); 			// Wait until it is my turn.
				flags.set(tid, TRUE); 			// I am interested.
			}
		}
	}

	@Override
	public void release(int tid) {
		turn = 1-tid;
		flags.set(tid, FALSE);
	}

}
