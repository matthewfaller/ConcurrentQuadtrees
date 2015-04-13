package com.matthewfaller.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test{

	private static int TEST_SIZE = 1000000;
	private static int NUM_THREADS = 10;
	private static float SPACE_SIZE = 20000;
	private static ArrayList<Float2> list    = new ArrayList<Float2>(TEST_SIZE);
	private static ArrayList<Float2> treeRes = new ArrayList<Float2>(TEST_SIZE);
	private static ArrayList<Vec2f> seqRes  = new ArrayList<Vec2f>(TEST_SIZE);
	
	private static Box bounds = new Box(new Vec2f(SPACE_SIZE/2,SPACE_SIZE/2), SPACE_SIZE, SPACE_SIZE);
	private static QTree  sTree = new QTree(bounds, true, null);
	private static CQTree cTree = new CQTree(bounds, true, null);//CQTree(bounds, isLeaf, noParent).
	private static QuadtreeConcurrent qTree = new QuadtreeConcurrent(bounds, true, null);
	private static long start = 0, stop = 0;

	
	public static void main(String... args) throws InterruptedException {

		System.out.println("--------------------------------");
		System.out.println("Tree Bounds : " + sTree.getBoxes().get(0).getWidth());
		generatePoints();	
		
		System.out.println("--------------------------------");
		System.out.println("Building Single-Threaded Tree...");
		boolean testing = true;
		start = System.nanoTime();
		
//		while(testing){
//			sTree = new QTree(bounds, true, null);
//			qTree = new QuadtreeArray(bounds, true, null);
			
//			for(Float2 p : list)
//				sTree.insert(p);
			
			for(Float2 p : list)
				qTree.insert(p);
		
		
			
//		}
		stop = System.nanoTime();		
		System.out.println("Time : " + ((double)(stop - start)/1000000) + " ms.");
		
		start = System.currentTimeMillis();
		//buildConcurrentTree(list);
		
		//while(isBuilding){ Thread.sleep(20); }
		
		Box search = new Box(new Vec2f(25, 25), 50, 50);

		System.out.println("--------------------------------");
		System.out.println("Conducting Search...");
		
		start = System.currentTimeMillis();		
		treeRes =  searchCQTree(search);
		stop = System.currentTimeMillis();
		System.out.println("Time : " + (stop - start));
		seqRes  = searchLinear(search);
//		System.out.println("Linear Results:");
//		System.out.println("----------------------");
//		printList(seqRes);
//		System.out.println("Tree Results:");
//		System.out.println("----------------------");
//		printList(treeRes);
		
		compareResults(seqRes, treeRes);
		
	}

	private static void buildConcurrentTree(ArrayList<Vec2f> points) throws InterruptedException {
		final int THREAD_WORK = TEST_SIZE / NUM_THREADS;
		int workStart = 0, workEnd;
		Thread[] threads = new Thread[NUM_THREADS];
		
		for(int i = 0; i < NUM_THREADS; i++){
			workStart = i * THREAD_WORK;
			workEnd = workStart + THREAD_WORK;			
			threads[i] = new Thread(new AsyncIns(points.subList(workStart, workEnd), cTree));
		}
		
		System.out.println("--------------------------------");
		System.out.println("Building Multi-Threaded Tree...");
		
		start = System.currentTimeMillis();
		
		for(int i = 0; i < NUM_THREADS; i++)			
			threads[i].start();
		
		
		for(Thread t : threads)
			t.join();
		
		stop = System.currentTimeMillis();		
		System.out.println("Time : " + (stop - start));
		
	}
	

	private static void compareResults(ArrayList<Vec2f> seqRes, ArrayList<Float2> treeRes) {
		System.out.println("--------------------------------");
		System.out.println("Comparing Results: ");
		
		int failures = 0;
		for(Vec2f p : seqRes){
			
			if(!treeRes.contains(p)){
				System.out.println("Search Failure : " + p); 
				failures++;
			}
		}
		
		System.out.println("--------------------------------");
		System.out.println("Total Failures: " + failures);
	}

	private static ArrayList<Vec2f> searchLinear(Box search) {
		seqRes.clear();
		
		for(Vec2f p : list)
			if(search.containsPoint(p))
				seqRes.add(p);
		
		return seqRes;
	}

	private static ArrayList<Float2> searchCQTree(Box searchSpace) {
		
		return qTree.queryRange(searchSpace, new ArrayList<Float2>());
	}
	
	private static ArrayList<Vec2f> searchQTree(Box searchSpace){
		
		return sTree.queryRange(searchSpace);
	}

	private static void generatePoints() {
		Random rand = new Random();
		
		for(int i = 0; i < TEST_SIZE; i++){
		
			float rX = rand.nextFloat() * bounds.getWidth();
			float rY = rand.nextFloat() * bounds.getHeight();
			
			list.add(new Float2(rX, rY));
			//System.out.println(list.get(i));
		}		
	}
	
	private static <T> void printList(List<T> list){
		
		for(T t : list)
			System.out.println(t);		
	}
	
	private static class AsyncIns implements Runnable{

		private List<Vec2f> work;
		private CQTree root;
		
		public AsyncIns(List<Vec2f> work, CQTree root){
			
			this.work = work;
			this.root = root;
		}
		@Override
		public void run() {

			for(Vec2f p : work)
				root.insert(p);			
		}
		
	}
}
