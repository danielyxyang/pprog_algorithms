package synchronization.locks.algorithms;

import java.util.concurrent.atomic.AtomicIntegerArray;

import synchronization.Lock;

public class FilterLock implements Lock {
	final int nThreads;
	final int nLevels;
	AtomicIntegerArray levels;
	AtomicIntegerArray victims;
	
	public FilterLock(int nThreads) {
		this.nThreads = nThreads;
		this.nLevels = nThreads;
		levels = new AtomicIntegerArray(nThreads);
		victims = new AtomicIntegerArray(nLevels);
	}
	
	@Override
	public void acquire(int tid) {
		for(int i = 1; i < nLevels; i++) {
			levels.set(tid, i); 	// I want to go on level i.
			victims.set(i, tid); 	// But I will wait.
			while(wait(tid, i)); 	// Wait if there are others on higher levels AND I have to wait.
			
			// level i = i threads filtered out
		}
		// level n-1 = n-1 threads filtered out = 1 thread left
	}
	private boolean wait(int tid, int level) {
		for(int i = 0; i < nThreads; i++) {
			if(i != tid && levels.get(i) >= level && victims.get(level) == tid) return true;
		}
		return false;
	}

	@Override
	public void release(int tid) {
		levels.set(tid, 0);
	}

}
