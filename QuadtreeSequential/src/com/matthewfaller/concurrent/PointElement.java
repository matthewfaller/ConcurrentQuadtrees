package com.matthewfaller.concurrent;

public class PointElement {

	public long id;
	public Vec2f pos;
	
	public PointElement(Vec2f pos, long id){

		this.id = id;
		this.pos = pos;
	}
	
	@Override
	public String toString() {
		
		return "<" + pos.x + ", "+ pos.y + "> : "+id;
	}
}