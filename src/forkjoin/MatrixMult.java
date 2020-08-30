package forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MatrixMult {
	private int[][] a;
	private int[][] b;
	public MatrixMult(int[][] a, int[][] b) {
		this.a = a;
		this.b = b;
	}
	
	public int[][] compute() {
		ForkJoinPool fjp = new ForkJoinPool();

		int[][] result = new int[a.length][b.length];
		List<MAC> tasks = new ArrayList<MAC>();
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < b.length; j++) {
				tasks.add(new MAC(result, i, j));
			}
		}
		
		for(MAC task : tasks) fjp.submit(task);
		for(MAC task : tasks) task.join();
		return result;
	}
	
	class MAC extends RecursiveAction {
		private int[][] result;
		private int i, j;
		public MAC(int[][] result, int i, int j) {
			this.result = result;
			this.i = i;
			this.j = j;
		}
		@Override
		protected void compute() {
			for(int k = 0; k < a[0].length; k++) {
				result[i][j] += a[i][k] * b[k][j];
			}
		}
	}
}
