package com.matthewfaller.concurrent;

import java.util.ArrayList;

public class QTree {
	
	private static final int SPLIT = 1;
	
	private Box box;
	private boolean isLeaf = false;
	private QTree[] children = new QTree[4];	
	private QTree parent = null;
	private Vec2f[] content = new Vec2f[SPLIT];
	private int[] pointCount = new int[SPLIT];
	private int numEntries = 0;
	private int totalCount = 0;
	
	public QTree(Box box, boolean isLeaf, QTree parent){
		this.parent = parent;
		this.box = box;
		this.isLeaf = isLeaf;
	}
	
	public boolean insert(Vec2f point){
		boolean result = false;
				
		if(isLeaf){
			
			if(box.containsPoint(point)){
				
				for(int i = 0; i < numEntries; i++){
					
					if(content[i].equals(point)){
						pointCount[i]++;
						totalCount++;
						result = true;
						break;
					}
				}
				if(!result){
					
					if(numEntries < SPLIT){
						content[numEntries] = point;
						pointCount[numEntries++] += 1;
						totalCount++;
					}else{				
						split();
						insert(point);
					}
				}
				result = true;	
			}
		}
		else{			
			for(QTree child : children)
				result |= child.insert(point);
		}		
		return result;
	}
	
	public boolean delete(Vec2f point){
		boolean result = false;
		
		if(isLeaf){
			
			if(box.containsPoint(point)){
				
				for(int i = 0; i < numEntries; i++){
					
					if(content[i].equals(point)){
						
						pointCount[i]--;
						totalCount--;
						
						if(pointCount[i] == 0)
							numEntries--;
						
						merge();
						result = true;
						break;
					}
				}
			}
		}else{
			
			for(QTree child : children)
				result |= child.delete(point);
		}
		
		return result;
	}
	
	
	

	public ArrayList<Vec2f> getPoints() {
		ArrayList<Vec2f> result = new ArrayList<Vec2f>(SPLIT);
		
		if(isLeaf){
			
			for(int i = 0; i < numEntries; i++)				
				result.add(content[i]);					
		}else{
			for(QTree child : children)
				result.addAll(child.getPoints());
		}
		return result;
	}

	public ArrayList<Box> getBoxes(){
		
		ArrayList<Box> result = new ArrayList<Box>();
		
		result.add(box);
		
		if(!isLeaf)
			for(QTree child : children)
				result.addAll(child.getBoxes());
		
		return result;
	}
	private void split(){
		isLeaf = false;
		float halfWidth = box.getWidth()/4, halfHeight = box.getHeight()/4;
		float width = box.getWidth()/2, height = box.getHeight()/2;
		boolean isNewLeaf = true;
		//CCW Quadrant ordering.
		//TOP RIGHT
		children[0] = new QTree( new Box(new Vec2f(box.pos.x + halfWidth, box.pos.y + halfHeight),	width, height), 
				isNewLeaf, this);
		
		//TOP LEFT
		children[1] = new QTree( new Box(new Vec2f(box.pos.x - halfWidth, box.pos.y + halfHeight),	width, height), 
				isNewLeaf, this);
		
		//BOT LEFT
		children[2] = new QTree( new Box(new Vec2f(box.pos.x - halfWidth, box.pos.y - halfHeight),	width, height), 
				isNewLeaf, this);
		
		//BOT RIGHT
		children[3] = new QTree( new Box(new Vec2f(box.pos.x + halfWidth, box.pos.y - halfHeight),	width, height), 
				isNewLeaf, this);
		
		giveToChildren();
	}
	
	private void giveToChildren(){
		
		for(Vec2f p : content){			
			for(QTree child : children)
				if(child.box.containsPoint(p))
					insert(p);
		}
		
		pointCount = new int[SPLIT];
		numEntries = 0;
		totalCount = 0;		
	}
	
	private int count(){
		
		if(isLeaf)
			return totalCount;	
		
		int count = 0;
		
		for(QTree child : children)
			count += child.count();
		
		return count;
	}
	
	private void merge() {		
		
		if(parent == null){//Base Case 1	
			
			ArrayList<Vec2f> points = getPoints();
			children = new QTree[4];
			isLeaf = true;
			
			for(Vec2f p : points)
				insert(p);
			
			return;
		}
		if(parent.count() > SPLIT){//Base Case 2
			
			ArrayList<Vec2f> points = getPoints();
			children = new QTree[4];
			isLeaf = true;
			
			for(Vec2f p : points)
				insert(p);
			
			return;
		}
		
		parent.merge();		
	}

}
