package forkjoin;

import java.util.concurrent.RecursiveTask;
import forkjoin.Map.MapFunction;

public class Pack extends RecursiveTask<int[]> {
	private int[] input;
	private MapFunction condition;
	
	public Pack(int[] input, MapFunction condition) {
		this.input = input;
		this.condition = condition;
	}

	@Override
	protected int[] compute() {
		int[] mask = new int[input.length];
		Map t1 = new Map(input, mask, condition); // computing bit mask
		t1.invoke();
		
		int[] index = new int[input.length];
		PrefixSum t2 = new PrefixSum(mask, index); // computing index vector
		t2.invoke();

		int resultLength = index[index.length-1]; // largest index
		int[] result = new int[resultLength];
		Map t3 = new Map(input, result, new MapFunction() { // mapping elements to result array
			@Override
			protected void run(int[] input, int[] output, int i) {
				if(mask[i] == 1) {
					int position = index[i] - 1;
					output[position] = input[i];
				}
			}
		});
		t3.invoke();
		
		return result;
	}
}
