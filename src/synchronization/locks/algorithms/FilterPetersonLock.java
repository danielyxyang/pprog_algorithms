package synchronization.locks.algorithms;

import synchronization.Lock;

public class FilterPetersonLock implements Lock {
	final int height; // height of lock tree
	PetersonLock[] locks;
	
	public FilterPetersonLock(int nThreads) {
		this.height = (int) Math.ceil(Math.log(nThreads) / Math.log(2));
		this.locks = new PetersonLock[(int) Math.pow(2, height)-1];
		for(int i = 0; i < locks.length; i++) locks[i] = new PetersonLock();
	}
	
	@Override
	public void acquire(int tid) {
		// N-thread lock
		for(int i = height - 1; i >= 0; i--) {
			int offset = (int) Math.pow(2, i) - 1;
			int k = (int) Math.pow(2, height - i);
			locks[offset + tid / k].acquire((tid % k) / (k/2));		
		}
		
		// 8-thread lock
//		locks[3 + tid / 2].acquire(tid % 2);
//		locks[1 + tid / 4].acquire((tid % 4) / 2);
//		locks[0 + tid / 8].acquire((tid % 8) / 4);
	}

	@Override
	public void release(int tid) {
		// N-thread lock
		for(int i = 0; i < height; i++) {
			int offset = (int) Math.pow(2, i) - 1;
			int k = (int) Math.pow(2, height - i);
			locks[offset + tid / k].release((tid % k) / (k/2));		
		}
		
		// 8-thread lock
//		locks[0 + tid / 8].release((tid % 8) / 4);
//		locks[1 + tid / 4].release((tid % 4) / 2);
//		locks[3 + tid / 2].release(tid % 2);
	}

}
