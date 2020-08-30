package synchronization.locks;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import synchronization.Lock;

public class AtomicLock implements Lock {
	AtomicBoolean lock = new AtomicBoolean();
	private Backoff[] backoffs;
	
	public AtomicLock(int nThreads) {
		backoffs = new Backoff[nThreads];
	}

	@Override
	public void acquire(int tid) {
		// TAS with backoff
		while(!lock.compareAndSet(false, true)) {
			if(backoffs[tid] == null) backoffs[tid] = new Backoff();
			backoffs[tid].backoff();
		}
		
		// TATAS with backoff
//		while(true) { 
//			while(lock.get());
//			if(lock.compareAndSet(false, true)) break;
//			else {
//				if(backoffs[tid] == null) backoffs[tid] = new Backoff();
//				backoffs[tid].backoff();
//			}
//		}
		
		if(backoffs[tid] != null) backoffs[tid].reset();		
	}

	@Override
	public void release(int tid) {
		lock.set(false);
	}
	
	class Backoff {
		private static final int MAX_DELAY = 1000;
		private int delay = 1;
		private Random random;
		public Backoff() {
			random = new Random();
		}
		
		public void backoff() {
			try {
				Thread.sleep(random.nextInt(delay));
			} catch (InterruptedException e) {}
			if(delay < MAX_DELAY) delay *= 2;
		}
		
		public void reset() {
			delay = 1;
		}
	}
}
