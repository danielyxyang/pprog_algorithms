package producerconsumer;

import java.util.ArrayList;

public class DataflowSquare {
	static final int[] X = new int[] {0, 0,1, 0,1,1, 0,1,1,1, 0,1,1,1,1};
	static final int[] Y = new int[] {1, 2,2, 3,3,3, 4,4,4,4, 5,5,5,5,5};
	static final int capacity = 10;
	
	public static void main(String[] args) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		Channel[] x = new Channel[1];
		Channel[] y = new Channel[2];
		Channel[] c = new Channel[23];
		for(int i = 0; i < x.length; i++) x[i] = new Channel(capacity);
		for(int i = 0; i < y.length; i++) y[i] = new Channel(capacity);
		for(int i = 0; i < c.length; i++) c[i] = new Channel(capacity);
		
		Node zero = new Node(0, c[0]); nodes.add(zero);
		Node inX = new Node(X, x); nodes.add(inX);
		Node inY = new Node(Y, y); nodes.add(inY);
		
		Node tf1 = new Node(new Channel[] {c[0], c[1], x[0]}, c[2], Node.TF); nodes.add(tf1);
		Node inv1 = new Node(c[2], c[3], Node.INV); nodes.add(inv1);
		Node sw1 = new Node(new Channel[] {y[0], c[3]}, c[4], Node.SWITCH); nodes.add(sw1);
		Node copy1 = new Node(c[4], new Channel[] {c[5], c[6]}, Node.COPY); nodes.add(copy1);
		Node add1 = new Node(new Channel[] {c[5], c[7]}, c[9], Node.ADD); nodes.add(add1);
		Node tf3 = new Node(new Channel[] {c[8], c[0], c[21]}, c[7], Node.TF); nodes.add(tf3);
		Node copy2 = new Node(c[9], new Channel[] {c[8], c[10], c[11]}, Node.COPY); nodes.add(copy2);
		Node compare1 = new Node(new Channel[] {c[11], c[6]}, c[12], Node.COMPARE); nodes.add(compare1);
		
		Node sw2 = new Node(new Channel[] {c[13], c[12]}, c[14], Node.SWITCH); nodes.add(sw2);
		Node inc1 = new Node(c[14], c[15], Node.INC); nodes.add(inc1);
		Node tf2 = new Node(new Channel[] {c[17], c[0], c[19]}, c[13], Node.TF); nodes.add(tf2);
		Node copy3 = new Node(c[15], new Channel[] {c[16], c[17]}, Node.COPY); nodes.add(copy3);
		Node cmp2 = new Node(new Channel[] {c[16], y[1]}, c[18], Node.COMPARE); nodes.add(cmp2);
		Node copy4 = new Node(c[18], new Channel[] {c[1], c[19], c[20], c[21]}, Node.COPY); nodes.add(copy4);
		
		Node sw3 = new Node(new Channel[] {c[10], c[20]}, c[22], Node.SWITCH); nodes.add(sw3);
		Node out = new Node(c[22]); nodes.add(out);
		
		c[1].enqueue(1);
		c[7].enqueue(0);
		c[13].enqueue(0);
		
		for(Node node : nodes) {
			new Thread(node).start();
		}
	}
}
