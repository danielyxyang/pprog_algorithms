package synchronization;

import static org.junit.Assert.*;

import org.junit.Test;

import synchronization.Lock;
import synchronization.locks.AtomicLock;
import synchronization.locks.MonitorLock;
import synchronization.locks.SemaphoreLock;
import synchronization.locks.algorithms.BakeryLock;
import synchronization.locks.algorithms.DekkerLock;
import synchronization.locks.algorithms.FilterLock;
import synchronization.locks.algorithms.FilterPetersonLock;
import synchronization.locks.algorithms.PetersonLock;

public class LockTest {
	private final int NUM_THREADS = 8;
	private final int NUM_ITERATIONS = 1000;

//	@Test
	public void testDekkerLock() {
		testLock(new DekkerLock(), 2, 10000);
	}
	
//	@Test
	public void testPetersonLock() {
		testLock(new PetersonLock(), 2, 10000);
	}

	@Test
	public void testFilterLock() {
		testLock(new FilterLock(NUM_THREADS), NUM_THREADS, NUM_ITERATIONS);
	}
	
	@Test
	public void testFilterPetersonLock() {
		testLock(new FilterPetersonLock(NUM_THREADS), NUM_THREADS, NUM_ITERATIONS);
	}
	
	@Test
	public void testBakeryLock() {
		testLock(new BakeryLock(NUM_THREADS), NUM_THREADS, NUM_ITERATIONS);
	}
	
	@Test
	public void testAtomicLock() {
		testLock(new AtomicLock(NUM_THREADS), NUM_THREADS, NUM_ITERATIONS);
	}
	
	@Test
	public void testMonitorLock() {
		testLock(new MonitorLock(), NUM_THREADS, NUM_ITERATIONS);
	}
	
	@Test
	public void testSemaphoreLock() {
		testLock(new SemaphoreLock(), NUM_THREADS, NUM_ITERATIONS);
	}
	
	private void testLock(Lock lock, int nThreads, int nIterations) {
		for(int i = 0; i < 100; i++) {
			assertEquals(nThreads * nIterations, count(lock, nThreads, nIterations));
		}
	}
	private int count(Lock lock, int nThreads, int nIterations) {
		Counter counter = new Counter();
		Thread[] threads = new Thread[nThreads];
		for(int tid = 0; tid < nThreads; tid++) {
			threads[tid] = new CounterThread(tid, lock, counter, nIterations);
			threads[tid].start();
		}
		for(int tid = 0; tid < nThreads; tid++) {
			try {
				threads[tid].join();
			} catch (InterruptedException e) {}
		}
		return counter.count;
	}
	
	class CounterThread extends Thread {
		private int tid;
		private Lock lock;
		private Counter counter;
		private int nIterations;
		public CounterThread(int tid, Lock lock, Counter counter, int nIterations) {
			this.tid = tid;
			this.lock = lock;
			this.counter = counter;
			this.nIterations = nIterations;
		}
		@Override
		public void run() {
			for(int i = 0; i < nIterations; i++) {
				lock.acquire(tid);
				counter.increment();
				lock.release(tid);
			}
		}
	}
	class Counter {
		volatile int count = 0;
		public void increment() {
			count++;
		}
	}
}
