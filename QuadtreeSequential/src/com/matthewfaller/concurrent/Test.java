package com.matthewfaller.concurrent;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;


public class Test extends JFrame{

	private static int TEST_SIZE = 100;
	private static ArrayList<PointElement> list    = new ArrayList<PointElement>(TEST_SIZE);
	private static ArrayList<PointElement> treeRes = new ArrayList<PointElement>(TEST_SIZE);
	private static ArrayList<PointElement> seqRes  = new ArrayList<PointElement>(TEST_SIZE);
	
	private static Box bounds = new Box(new Vec2f(0,0), 200f, 200f);
	private static QuadTree tree = new QuadTree(1, (short)20, bounds);
	
	public static void main(String... args) {

		Test t = new Test();
		t.setSize((int)bounds.getWidth(), (int)bounds.getHeight());
		t.setDefaultCloseOperation(EXIT_ON_CLOSE);
		t.setVisible(true);
		
		generatePoints();						
		
		for(PointElement e : list)
			tree.root.insert(e);
		
		Box search = new Box(new Vec2f(25, 25), 50, 50);
		
		seqRes  = searchLinear(search);
		treeRes = searchQuadTr(search);
		
		System.out.println("Linear Results:");
		System.out.println("----------------------");
		printList(seqRes);
		System.out.println("Tree Results:");
		System.out.println("----------------------");
		printList(treeRes);
		
		
	}

	private static ArrayList<PointElement> searchLinear(Box search) {
		seqRes.clear();
		
		for(PointElement e : list)
			if(search.containsPoint(e.pos))
				seqRes.add(e);
		
		return seqRes;
	}

	private static ArrayList<PointElement> searchQuadTr(Box search) {
		
		return tree.root.regionSearch(search);
	}

	private static void generatePoints() {
		Random rand = new Random();
		
		for(int i = 0; i < TEST_SIZE; i++){
		
			float rX = rand.nextFloat() * bounds.getWidth();
			float rY = rand.nextFloat() * bounds.getHeight();
			
			list.add(new PointElement(new Vec2f(rX, rY), i));
			//System.out.println(list.get(i));
		}		
	}
	
	private static <T> void printList(List<T> list){
		
		for(T t : list)
			System.out.println(t);		
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		//super.paint(g);
	}
}
