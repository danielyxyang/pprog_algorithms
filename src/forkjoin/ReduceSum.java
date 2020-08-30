package forkjoin;

import java.util.concurrent.RecursiveTask;

public class ReduceSum extends RecursiveTask<Integer> {
	private final int SEQ_CUTOFF = 100000;
	
	private int[] data;
	private int start;
	private int length;
	
	public ReduceSum(int[] data) {
		this(data, 0, data.length);
	}
	public ReduceSum(int[] data, int start, int length) {
		this.data = data;
		this.start = start;
		this.length = length;
	}

	@Override
	protected Integer compute() {
		if(length <= SEQ_CUTOFF) {
			int sum = 0;
			for(int i = start; i < start + length; i++) sum += data[i];
			return sum;
		}
		int half = length / 2;
		ReduceSum t1 = new ReduceSum(data, start, half);
		ReduceSum t2 = new ReduceSum(data, start + half, length - half);
		
		t1.fork();
		return t2.compute() + t1.join();
	}
}
