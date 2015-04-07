package com.matthewfaller.concurrent;

public class Box {
	
	public Vec2f pos;
	private float width;
	private float height;
	private float halfWidth;
	private float halfHeight;
	
	/**
	 * Constucts a new box. 
	 * @param pos -The position of the box. This should be a newly allocated position, unique to this box.
	 * @param width -How wide this box is, from one side to the other. 
	 * @param height -How tall this box is, from bottom to top.
	 * */
	public Box(Vec2f pos, float width, float height){
		this.pos = pos;
		this.width = width;
		this.height = height;
		halfWidth = width/2;
		halfHeight = height/2;
	}
	
	/**
	 * Tests if this box intersects with box b.
	 * @return true if they intersect
	 * */
	public boolean intersects(Box b){
		/*
		 * The first line of the return statement:
		 * If the top right corner of this box is greater then the bottom left corner of b, there is an intersection.
		 * 
		 * The second line:
		 * Like wise, if the top right corner of b is greater then the bottom left corner of this, an intersection has occurred.
		 */		
		return 		( pos.x + halfWidth() >= b.pos.x - b.halfWidth()  && pos.y + halfHeight() >= b.pos.y - b.halfHeight() )
				
				&&	( b.pos.x + b.halfWidth() >= pos.x - halfWidth() && b.pos.y + b.halfHeight() >= pos.y - halfHeight()  );
	}
	
	/**
	 * Returns true if an only if this box entirely contains box b
	 * @untested
	 * */
	public boolean contains(Box b){		
		return 		( pos.x + halfWidth() > b.pos.x + b.halfWidth()  && pos.y + halfHeight() > b.pos.y + b.halfHeight() )
				
				&&	( b.pos.x - b.halfWidth() > pos.x - halfWidth() && b.pos.y - b.halfHeight() > pos.y - halfHeight()  );
	}
	
	/**
	 * Returns true if box b lies within this boxes x range or this boxes y range.
	 * @untested
	 * */
	public boolean partialContainment(Box b){
		
		return  (pos.x + halfWidth() > b.pos.x + b.halfWidth() && b.pos.x - b.halfWidth() > pos.x - halfWidth())
		 
		 || (pos.y + halfHeight() > b.pos.y + b.halfHeight() && b.pos.y - b.halfHeight() > pos.y - halfHeight());
	}
	
	/**
	 *Tests if the X-Axis projections of this box intersect
	 *@return True if they intersect
	 **/
	public boolean xAxisIntersection(Box b){		
		return pos.x + halfWidth() >= b.pos.x - b.halfWidth() && b.pos.x + b.halfWidth() >= pos.x - halfWidth();
	}
	
	/**
	 *Tests if the Y-Axis projections of this box intersect
	 *@return True if they intersect
	 **/
	public boolean yAxisIntersection(Box b){		
		return pos.y + halfHeight() > b.pos.y + b.halfHeight() && b.pos.y + b.halfHeight() >= pos.y - halfHeight();
	}
	
	
	public float getWidth() {
		return width;
	}
	
	public void setWidth(float width) {
		this.width = width;
		halfWidth = width/2;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float height) {
		this.height = height;
		halfHeight = height/2;
	}
	
	public float halfHeight(){
		return this.halfHeight;
	}
	
	public float halfWidth(){
		return this.halfWidth;
	}
	
	public float topRightX(){
		return pos.x + halfWidth();
	}
	
	public float topRightY(){
		return pos.y + halfHeight();
	}
		
	public float botLeftX(){
		return pos.x - halfWidth();
	}
	
	public float botLeftY(){
		return pos.y - halfHeight();
	}
	
	public boolean containsPoint(Vec2f point){		
		
		return  ( point.x > botLeftX() && point.y > botLeftY() )
				&&
				( point.x <= topRightX() && point.y <= topRightY() );
	}
}
