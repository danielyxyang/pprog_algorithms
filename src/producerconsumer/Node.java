package producerconsumer;

public class Node implements Runnable {
	public static final Computation COPY = new Computation() {
		@Override
		public void compute(Integer[] inputs, Integer[] outputs) { // 0: data input, 1: control input
			for(int i = 0; i < outputs.length; i++) outputs[i] = inputs[0];
		}
	};
	public static final Computation SWITCH = new Computation() {
		@Override
		public void compute(Integer[] inputs, Integer[] outputs) { // 0: data input, 1: control input
			outputs[0] = (inputs[1] == 1) ? inputs[0] : null;
		}
	};
	public static final Computation TF = new Computation() { // 0: false input, 1: true input, 2: control input
		@Override
		public void compute(Integer[] inputs, Integer[] outputs) {
			outputs[0] = (inputs[2] == 1) ? inputs[1] : inputs[0];
		}
	};
	public static final Computation INC = new Computation() {
		@Override
		public void compute(Integer[] inputs, Integer[] outputs) {
			outputs[0] = inputs[0] + 1;
		}
	};
	public static final Computation ADD = new Computation() {
		@Override
		public void compute(Integer[] inputs, Integer[] outputs) {
			outputs[0] = inputs[0] + inputs[1];
		}
	};
	public static final Computation COMPARE = new Computation() {
		@Override
		public void compute(Integer[] inputs, Integer[] outputs) {
			outputs[0] = (inputs[0] >= inputs[1]) ? 1 : 0;
		}
	};
	public static final Computation INV = new Computation() { // 0: false input, 1: true input, 2: control input
		@Override
		public void compute(Integer[] inputs, Integer[] outputs) {
			outputs[0] = 1 - inputs[0];
		}
	};
	public static final Computation ZERO = new Computation() {
		@Override
		public void compute(Integer[] inputs, Integer[] outputs) {
			outputs[0] = 0;
		}
	};
	public static final Computation PRINT = new Computation() {
		@Override
		public void compute(Integer[] inputs, Integer[] outputs) {
			System.out.println(inputs[0]);
			outputs[0] = inputs[0];
		}
	};
	
	
	private Channel[] inputs;
	private Channel[] outputs;
	private Computation computation;
	
	public Node(Channel[] inputs, Channel[] outputs, Computation computation) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.computation = computation;
	}
	public Node(Channel[] inputs, Channel output, Computation computation) {
		this(inputs, new Channel[] {output}, computation);
	}
	public Node(Channel input, Channel[] outputs, Computation computation) {
		this(new Channel[] {input}, outputs, computation);
	}
	public Node(Channel input, Channel output, Computation computation) {
		this(new Channel[] {input}, new Channel[] {output}, computation);
	}

	// input node
	public Node(int[] data, Channel[] outputs) {
		this.inputs = null;
		this.outputs = outputs;
		this.computation = new Computation() {
			int n = 0;
			@Override
			public void compute(Integer[] inputs, Integer[] outputs) {
				if(n < data.length) {
					for(int i = 0; i < outputs.length; i++) outputs[i] = data[n];
					n++;
				}
				else {
					for(int i = 0; i < outputs.length; i++) outputs[i] = null;
				}
			}
		};
	}
	public Node(int[] data, Channel output) {
		this(data, new Channel[] {output});
	}
	// constant input node
	public Node(int data, Channel[] outputs) {
		this.inputs = null;
		this.outputs = outputs;
		this.computation = new Computation() {
			@Override
			public void compute(Integer[] inputs, Integer[] outputs) {
				for(int i = 0; i < outputs.length; i++) outputs[i] = data;
			}
		};
	}
	public Node(int data, Channel output) {
		this(data, new Channel[] {output});
	}
	// output node
	public Node(Channel input) {
		this.inputs = new Channel[] {input};
		this.outputs = null;
		this.computation = new Computation() {
			@Override
			public void compute(Integer[] inputs, Integer[] outputs) {
				System.out.println(inputs[0]);
			}
		};
	}
	
	@Override
	public void run() {
		while(true) {
			Integer[] input = (inputs != null) ? new Integer[inputs.length] : null;
			Integer[] output = (outputs != null) ? new Integer[outputs.length] : null;
			
			if(inputs != null) for(int i = 0; i < inputs.length; i++) {
				input[i] = inputs[i].dequeue();
			}
			computation.compute(input, output);
			if(outputs != null) for(int i = 0; i < outputs.length; i++) {
				if(output[i] != null) outputs[i].enqueue(output[i]);
			}
		}
	}
	
	static abstract class Computation {
		abstract public void compute(Integer[] inputs, Integer[] outputs);
	}
}
