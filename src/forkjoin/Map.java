package forkjoin;

import java.util.concurrent.RecursiveAction;

public class Map extends RecursiveAction {
	private final int SEQ_CUTOFF = 1000;
	
	private int[] input;
	private int[] output;
	private MapFunction function;
	
	private int start;
	private int length;
	
	public Map(int[] input, int[] output, MapFunction function) {
		this(input, output, function, 0, input.length);	
	}

	public Map(int[] input, int[] output, MapFunction function, int start, int length) {
		this.input = input;
		this.output = output;
		this.function = function;
		this.start = start;
		this.length = length;
	}
	
	@Override
	protected void compute() {
		if(length <= SEQ_CUTOFF) {
			for(int i = start; i < start+length; i++) function.run(input, output, i);
			return;
		}
		
		int half = length / 2;
		Map t1 = new Map(input, output, function, start, half);
		Map t2 = new Map(input, output, function, start + half, length - half);
		t1.fork();
		t2.compute();
		t1.join();
	}
	
	static abstract class MapFunction {
		abstract protected void run(int[] input, int[] output, int index);
	}
}
