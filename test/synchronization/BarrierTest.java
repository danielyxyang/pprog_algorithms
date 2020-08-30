package synchronization;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import synchronization.barrier.MonitorBarrier;
import synchronization.barrier.SemaphoreBarrier;

public class BarrierTest {
	private final int NUM_THREADS = 20;
	private final int NUM_ITERATIONS = 10;

	@Test
	public void testMonitorBarrier() throws InterruptedException {
		testBarrier(new MonitorBarrier(NUM_THREADS), NUM_THREADS);
	}
	
	@Test
	public void testSemaphoreBarrier() throws InterruptedException {
		testBarrier(new SemaphoreBarrier(NUM_THREADS), NUM_THREADS);
	}
	
	private void testBarrier(Barrier barrier, int nThreads) throws InterruptedException {
		AtomicInteger count = new AtomicInteger(0);
		
		WorkerThread[] threads = new WorkerThread[nThreads];
		for(int k = 0; k < NUM_ITERATIONS; k++) {
			for(int i = 0; i < nThreads; i++) {
				threads[i] = new WorkerThread(nThreads, barrier, count);
				threads[i].start();
			}
			for(int i = 0; i < nThreads; i++) {
				threads[i].join();
				assertFalse(threads[i].isFailed());
			}
			count.set(0);
		}
	}

	class WorkerThread extends Thread {
		private int nThreads;
		private Barrier barrier;
		private AtomicInteger count;
		private boolean failed = false;
		public WorkerThread(int nThreads, Barrier barrier, AtomicInteger count) {
			this.nThreads = nThreads;
			this.barrier = barrier;
			this.count = count;
		}
		@Override
		public void run() {
			try {
				Thread.sleep((long) (Math.random() * 100));

//				System.out.println("Enter: " + Thread.currentThread().getId());
				count.incrementAndGet();
				barrier.await();
				
//				System.out.println("Leave: " + Thread.currentThread().getId());
				failed = (count.get() != nThreads);
			} catch (InterruptedException e) {}
		}
		public boolean isFailed() {
			return this.failed;
		}
	}
}
