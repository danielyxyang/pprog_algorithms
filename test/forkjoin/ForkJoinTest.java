package forkjoin;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

import org.junit.Test;

import forkjoin.Map.MapFunction;
import forkjoin.MatrixMult;
import forkjoin.ReduceSum;

public class ForkJoinTest {

	@Test
	public void testReduceSum() {
		int[] data = new int[10000000];
		int sum = 0;
		for(int i = 0; i < data.length; i++) {
			data[i] = (int) (Math.random() * 100);
			sum += data[i];
		}
		
		ForkJoinPool fjp = new ForkJoinPool();
		int result = fjp.invoke(new ReduceSum(data));
		assertEquals(sum, result);
		
		// PERFORMANCE ANALYSIS
//		System.out.println("REDUCE SUM");
//		int nIterations = 100;
//		double time = 0;
//		
//		for(int k = 0; k < nIterations; k++) {
//			long time1 = System.currentTimeMillis();
//			int resultPar = fjp.invoke(new ReduceSum(data));
//			long time2 = System.currentTimeMillis();
//			time += time2 - time1;
//		}
//		System.out.printf("Parallel:   %.2fms%n", time / nIterations);
//		
//		time = 0;
//		for(int k = 0; k < nIterations; k++) {
//			long time1 = System.currentTimeMillis();
//			int resultSeq = 0;
//			for(int j = 0; j < data.length; j++) resultSeq += data[j];
//			long time2 = System.currentTimeMillis();
//			time += time2 - time1;
//		}
//		System.out.printf("Sequential: %.2fms%n", time / nIterations);
	}
	
	@Test
	public void testPrefixSum() {
		int n = (int) Math.pow(2, 20);
		int[] data = new int[n];
		int[] expected = new int[n];
		int[] result = new int[n];
		for(int i = 0; i < data.length; i++) {
			data[i] = (int) (Math.random() * 100);
			expected[i] += data[i] + (i > 0 ? expected[i-1] : 0);
		}
		
		ForkJoinPool fjp = new ForkJoinPool();
		fjp.invoke(new PrefixSum(data, result));
		
		assertArrayEquals(expected, result);
		
		// PERFORMANCE ANALYSIS
//		System.out.println("PREFIX SUM");
//		int nIterations = 100;
//		double time = 0;
//		
//		for(int k = 0; k < nIterations; k++) {
//			long time1 = System.currentTimeMillis();
//			int[] resultPar = new int[n];
//			fjp.invoke(new PrefixSum(data, resultPar));
//			long time2 = System.currentTimeMillis();
//			time += time2 - time1;
//		}
//		System.out.printf("Parallel:   %.2fms%n", time / nIterations);
//		
//		time = 0;
//		for(int k = 0; k < nIterations; k++) {
//			long time1 = System.currentTimeMillis();
//			int[] resultSeq = new int[n];
//			for(int i = 0; i < data.length; i++) resultSeq[i] += data[i] + (i > 0 ? resultSeq[i-1] : 0);
//			long time2 = System.currentTimeMillis();
//			time += time2 - time1;
//		}
//		System.out.printf("Sequential: %.2fms%n", time / nIterations);
	}
	
	@Test
	public void testMap() {
		int n = 100;
		int[] data = new int[n];
		int[] expected = new int[n];
		int[] result = new int[n];
		for(int i = 0; i < data.length; i++) {
			data[i] = (int) (Math.random() * 10);
			expected[i] = (int) Math.pow(2, data[i]);
		}
		
		ForkJoinPool fjp = new ForkJoinPool();
		fjp.invoke(new Map(data, result, new MapFunction() {
			@Override
			protected void run(int[] input, int[] output, int index) {
				output[index] = (int) Math.pow(2, input[index]);
			}
		}));
		
		assertArrayEquals(expected, result);
	}
	
	@Test
	public void testPack() {
		int n = 100;
		int[] data = new int[n];
		ArrayList<Integer> expected = new ArrayList<Integer>();
		for(int i = 0; i < data.length; i++) {
			data[i] = (int) (Math.random() * 100);
			if(data[i] > 50) expected.add(data[i]);
		}
		
		ForkJoinPool fjp = new ForkJoinPool();
		int[] result = fjp.invoke(new Pack(data, new MapFunction() {
			@Override
			protected void run(int[] input, int[] output, int i) {
				output[i] = (input[i] > 50) ? 1 : 0;
			}
		}));
		
		assertEquals(expected.size(), result.length);
		for(int i = 0; i < expected.size(); i++) {
			assertEquals((int) expected.get(i), result[i]);
		}
	}
	
	@Test
	public void testMatrixMult() {
		int[][] a = new int[][] {
			{1,2,3,4,5,6},
			{1,2,3,4,5,6},
			{1,2,3,4,5,6},
			{1,2,3,4,5,6}
		};
		int[][] b = new int[][] {
			{1,2,3,4,5,6},
			{1,2,3,4,5,6},
			{1,2,3,4,5,6},
			{1,2,3,4,5,6},
			{1,2,3,4,5,6},
			{1,2,3,4,5,6}
		};
		int[][] c = new int[][] {
			{21, 42, 63, 84, 105, 126},
			{21, 42, 63, 84, 105, 126},
			{21, 42, 63, 84, 105, 126},
			{21, 42, 63, 84, 105, 126}
		};
		MatrixMult mult = new MatrixMult(a, b);
		int[][] result = mult.compute();
//		System.out.println(Arrays.deepToString(result).replace("],", "],\n").replace("]", "}").replace("[", "{"));
		assertEquals(Arrays.deepToString(c), Arrays.deepToString(result));
	}

}
