package synchronization.locks;

public class ReadWriteLock {
	volatile int readers = 0;
	volatile int readerWaiting = 0;
	volatile int writers = 0;
	volatile int writerWaiting = 0;
	volatile int readerPassing = 0; // number of readers to pass before next writer
	
	public synchronized void acquireRead() throws InterruptedException {
		readerWaiting++;
		while((readerPassing <= 0 && writerWaiting > 0) || writers > 0) wait(); // wait if writers are waiting and readers are not allowed to pass
		readerWaiting--;
		
		readers++;
	}
	
	public synchronized void releaseRead() {
		readers--;
		readerPassing--;
		if(readers <= 0) notifyAll();
	}
	
	public synchronized void acquireWrite() throws InterruptedException {
		writerWaiting++;
		while(readerPassing > 0 || readers > 0 || writers > 0) wait(); // wait if readers are still allowed to pass
		writerWaiting--;
		
		writers++;
	}
	
	public synchronized void releaseWrite() {
		writers--;
		readerPassing = readerWaiting; // saving number of waiting readers
		notifyAll();
	}

}
