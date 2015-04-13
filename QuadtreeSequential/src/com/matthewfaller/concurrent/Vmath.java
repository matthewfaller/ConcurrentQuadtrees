package com.matthewfaller.concurrent;

/**
 * This class performs a number of useful mathmatical operations. 
 * @author Matthew Faller
 * */
public abstract class Vmath {
	
	private final static byte TR = 0;
	private final static byte TL = 1;
	static private final byte BL = 2;
	static private final byte BR = 3;
	
	/**
	 * Calculates the dot product of two vectors
	 * @param a The first vector
	 * @param b The second vector
	 * @return a dot b
	 * */
	public static float dotProduct(Vec2f a, Vec2f b){
		return a.x*b.x + a.y*b.y;
	}
	
	/**
	 * Calculates a minus b
	 * @param a minuend 
	 * @param b subtrahend
	 * @return a - b (A new Vec2f object is created).
	 * */
	public static Vec2f a_minus_b(Vec2f a, Vec2f b){
		return new Vec2f(a.x - b.x, a.y - b.y);
	}
	
	/**
	 * Calculates a plus b
	 * @param a the first addend
	 * @param b the second addend
	 * @return a + b (A new Vec2f object is created).
	 * */
	public static Vec2f a_plus_b(Vec2f a, Vec2f b){
		return new Vec2f(a.x + b.x, a.y + b.y);
	}
	
	/**
	 * Calculates the cross product of the two vectors
	 * @param a The first vector
	 * @param b The second vector
	 * @return a x b
	 * */
	public static float crossProduct(Vec2f a, Vec2f b){
		return (a.x*b.y - b.x*a.y);
	}
	
	/**
	 * Subtracts b from a, storing the result in a.
	 * @param a The minuend.
	 * @param b The subtrahend.
	 * */
	public static void subtractB(Vec2f a, Vec2f b){
		a.x -= b.x;
		a.y -= b.y;
	}
	
	/**
	 * Adds b to a, storing the result in a.
	 * @param a The first addend.
	 * @param b The second addend.
	 * */
	public static void addB(Vec2f a, Vec2f b){
		a.x += b.x;
		a.y += b.y;
	}
	
	/**
	 * Calculates the magnitude of the vector.
	 * @param a The vector
	 * @return ||a||
	 * */
	public static double magnitude(Vec2f a){
		return Math.sqrt((a.x*a.x + a.y*a.y));
	}
	
	/**
	 * Scales the vector (storing the result in vector a)
	 * @param a The vector to scale.
	 * @param scaler The scaler.
	 * 
	 * */
	public static void scale(Vec2f a, float scaler){
		a.x *= scaler;
		a.y *= scaler;
	}
	
	//I could make other methods that would rotate by a fixed amount such as, 90 degrees.
	//That way, the sin / cos functions would not need to be called. 
	/**
	 * Rotates the vector A by theta radians. Stores result in A.
	 * 
	 * @theta the amount of rotation in radians. 
	 * @a the vector to rotate.
	 * */
	public static void rotate(double theta, Vec2f a){
		/*
		 * Rotates the vector using a rotation matrix:
		 * 
		 * [	cos0	-sin0		[x]
		 * 		sin0	 cos0 ] * 	[y]
		 * 
		 * ==
		 * 
		 * < xcos0 - ysin0, xsin0 + ycos0 >
		 * */
		
		a.x = (float) (a.x*Math.cos(theta) - a.y*Math.sin(theta));
		a.y = (float) (a.x*Math.sin(theta) + a.y*Math.cos(theta));
	}
	
	/**
	 * This method returns the angle between A and B.
	 * 
	 * This method WILL divide by Zero if |a| or |b| = 0
	 * Returns as radians. 
	 * */
	public static double angleBetweenVectors(Vec2f a, Vec2f b){
		assert magnitude(a) != 0 || magnitude(b) != 0;
		
		return Math.acos(  (crossProduct(a, b)) / (magnitude(a) * magnitude(b)) );	
	}
	
	/**
	 * 
	 * Converts the vector's direction to an angle between 0 radians and 2PI .
	 * 0 is the right most direction, PI is the left most. 
	 * 
	 * This method along with magnitude(Vec2f a) allows a vector to be represented as a polar coordinate.	 * 
	 * */
	public static float getAngle(Vec2f a){
		
		double angle =  Math.atan(a.y/a.x); //Opposite / Adjacent
		angle = Math.abs(angle);
		
		
		switch (whichQuad(a)) {

		case TR:
			break;

		case TL:
			angle = Math.PI - angle;
			break;

		case BL:
			angle = Math.PI + angle;
			break;
		case BR:
			angle = 2*Math.PI - angle;
			break;
		}
		
		//Logger.logCatMessage("math", "Angle:"+Math.toDegrees(angle));
		return (float) angle;
	}
	
	/**
	 * Calculates which Quadrant the vector lies in (if its tail were at the origin).
	 * */
	private static byte whichQuad(Vec2f v){
		
		byte q;
		
		if(v.x > 0){
			
			if(v.y > 0){
				q = TR;
			}else{
				q = BR;
			}
		}else{
			
			if(v.y > 0){
				q = TL;
			}else{
				q = BL;
			}
		}
		return q;
	}
	
	
	
}
