package forkjoin;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class PrefixSum extends RecursiveAction {
	private final int SEQ_CUTOFF = 100000;
	
	private int[] data;
	private int[] prefixSum;
	public PrefixSum(int[] data, int[] prefixSum) {
		this.data = data;
		this.prefixSum = prefixSum;
	}
	
	@Override
	public void compute() {
		int n = data.length;
		int[] result = new int[2*n-1];
		FirstPassTask t1 = new FirstPassTask(data, result, 0, n, 0);
		t1.invoke();
		SecondPassTask t2 = new SecondPassTask(data, result, prefixSum, 0, n, 0, 0);
		t2.invoke();
	}
	
	class FirstPassTask extends RecursiveTask<Integer> {
		private int[] input; // input array
		private int[] rangeSums; // binary tree as array
		
		private int start;
		private int length;
		private int index; // index in result array
		
		public FirstPassTask(int[] input, int[] rangeSums, int start, int length, int index) {
			this.input = input;
			this.rangeSums = rangeSums;
			this.start = start;
			this.length = length;
			this.index = index;
		}
		
		@Override
		protected Integer compute() { // compute range sum for [start, start+length)
			if(length <= SEQ_CUTOFF) {
				for(int i = start; i < start+length; i++) {
					rangeSums[index] += input[i];
				}
				return rangeSums[index];
			}
			
			int half = length / 2;
			FirstPassTask t1 = new FirstPassTask(input, rangeSums, start, half, 2*index + 1); // left subtree
			FirstPassTask t2 = new FirstPassTask(input, rangeSums, start + half, length - half, 2*index + 2); // right subtree
			
			t1.fork();
			rangeSums[index] = t2.compute() + t1.join();
			
			return rangeSums[index];
		}
	}

	class SecondPassTask extends RecursiveAction {
		private int[] input; // input array
		private int[] rangeSums; // binary tree as array
		private int[] result; // prefix sum array
		
		private int start;
		private int length;
		private int index; // index in result array
		private int leftSum; // left prefix sum up to index
		
		public SecondPassTask(int[] input, int[] rangeSums, int[] result, int start, int length, int index, int leftSum) {
			this.input = input;
			this.rangeSums = rangeSums;
			this.result = result;
			this.start = start;
			this.length = length;
			this.index = index;
			this.leftSum = leftSum;
		}
		@Override
		protected void compute() {
			if(length <= SEQ_CUTOFF) {
				for(int i = start; i < start+length; i++) {
					if(i == start) result[i] = leftSum + input[i];
					else result[i] = result[i-1] + input[i];
				}
				return;
			}
			
			int half = length / 2;
			SecondPassTask t1 = new SecondPassTask(input, rangeSums, result, start, half, 2*index + 1, leftSum); // left subtree
			SecondPassTask t2 = new SecondPassTask(input, rangeSums, result, start + half, length - half, 2*index + 2, leftSum + rangeSums[2*index+1]); // right subtree
			
			t1.fork();
			t2.compute();
			t1.join();
		}
	}
	
}
