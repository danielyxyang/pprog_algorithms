package synchronization.locks.algorithms;

import java.util.concurrent.atomic.AtomicIntegerArray;

import synchronization.Lock;

public class BakeryLock implements Lock {
	final int nThreads;
	AtomicIntegerArray flags;
	AtomicIntegerArray tickets;
	
	public BakeryLock(int nThreads) {
		this.nThreads = nThreads;
		flags = new AtomicIntegerArray(nThreads);
		tickets = new AtomicIntegerArray(nThreads);
	}
	
	@Override
	public void acquire(int tid) {
		flags.set(tid, TRUE);
		int ticket = getTicket();
		tickets.set(tid, ticket);

		while(wait(tid, ticket));
	}
	private int getTicket() {
		int max = Integer.MIN_VALUE;
		for(int i = 0; i < nThreads; i++) {
			max = Math.max(max, tickets.get(i));
		}
		return max + 1;
	}
	private boolean wait(int tid, int ticket) {
		for(int i = 0; i < nThreads; i++) {
			if(flags.get(i) == TRUE && (tickets.get(i) < ticket || (tickets.get(i) == ticket && i < tid))) return true;
		}
		return false;
	}

	@Override
	public void release(int tid) {
		flags.set(tid, FALSE);
	}

}
