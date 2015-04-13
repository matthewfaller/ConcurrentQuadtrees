package com.matthewfaller.concurrent;
/**
 * This is the class the is used to represent things such as locations, forces, etc...
 * @author Matthew Faller
 * */
public class Vec2f {
	public float x;
	public float y;
	
	public Vec2f (float x, float y){
		this.x = x; this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {

		if(obj instanceof Vec2f){
			Vec2f p2 = (Vec2f)obj;
			
			return x == p2.x && y == p2.y;
		}else
			return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return "<" + x + ", " + y + ">";
	}
}
