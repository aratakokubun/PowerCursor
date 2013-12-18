package org.example.PowerCursor;

import android.graphics.Point;
import android.util.Log;

public class MakeInconsistency {
	private static final String TAG = "Make Inconsistency";
	
	private static float dx	, dy;
	private static int 	vx	, vy;
	private static int 	prex, prey;
	private static int 	nowx, nowy;
	
	// initializer
	public static void init(){
		dx = 0.0f;
		dy = 0.0f;
	}
	public static void init(float val_dx, float val_dy, int val_vx, int val_vy,
			int val_prex, int val_prey, int val_nowx, int val_nowy){
		dx = val_dx;
		dy = val_dy;
		vx = val_vx;
		vy = val_vy;
		prex = val_prex;
		prey = val_prey;
		nowx = val_nowx;
		nowy = val_nowy;
	}
	
	// setter
	public static void setDx(float value){
		dx = value;
	}
	public static void setDy(float value){
		dy = value;
	}
	public static void setVx(int value){
		vx = value;
	}
	public static void setVy(int value){
		vy = value;
	}
	public static void setPrex(int value){
		prex = value;
	}
	public static void setPrey(int value){
		prey = value;
	}
	public static void setNowx(int value){
		nowx = value;
	}
	public static void setNowy(int value){
		nowy = value;
	}
	
	// getter
	public static float getDx(){
		return dx;
	}
	public static float getDy(){
		return dy;
	}
	public static int getVx(){
		return vx;
	}
	public static int getVy(){
		return vy;
	}
	public static int getPrex(){
		return prex;
	}
	public static int getPrey(){
		return prey;
	}
	public static int getNowx(){
		return nowx;
	}
	public static int getNowy(){
		return nowy;
	}
	
	/* ----------------------------------------------------------------------------------------------------------------------- */
	// make inconsistent situation with changing position and speed
	public static Point pseudo(int pseudoType, float pseudoParam, float pseudoRange, Point cursor, Point diffTouch, Point target){
		float old_dx = dx;
		float old_dy = dy;
		
		float[] vector = {
				target.x-cursor.x,
				target.y-cursor.y
		};
		float distance = (float)Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2));

		if(isInRange(pseudoType, vector, pseudoRange)){
			switch(pseudoType){
			case POWERCURSOR.NONE:
				break;
				
			case POWERCURSOR.HOLE:
				// force of V-shaped hole
				// dx -= const * pseudoParam;
				// dy -= const * pseudoParam;
				
				// force of U-shaped hole
				dx -= vector[0] * pseudoParam;
				dy -= vector[1] * pseudoParam;
				
				// force of Sine-shaped
				// float slope = Math.sin(distance);
				// dx -= slope * vector[0]/distance * pseudoParam;
				// dy -= slope * vector[1]/distance * pseudoParam;
				break;
					
			case POWERCURSOR.HILL:
				// force of V-shaped hole
				// dx += const * pseudoParam;
				// dy += const * pseudoParam;
				
				// force of U-shaped hole
				dx += vector[0] * pseudoParam;
				dy += vector[1] * pseudoParam;
				
				// force of Sine-shaped
				// float slope = Math.sin(distance);
				// dx += slope * vector[0]/distance * pseudoParam;
				// dy += slope * vector[1]/distance * pseudoParam;
				break;
				
			case POWERCURSOR.PUSHGUTTER:
				/*
				// flow of hole effect of each pipe
				// U-shaped
				float slope = 0;
				for(int i = 0; i < POWERCURSOR.PUSHGUTTER_SLOPE_DIVISION.length; i++){
					if(POWERCURSOR.PUSHGUTTER_SLOPE_DIVISION[i]*POWERCURSOR.HEIGHT<-vector[1] &&
							POWERCURSOR.PUSHGUTTER_SLOPE_DIVISION[i+1]*POWERCURSOR.HEIGHT>-vector[1]){
						slope = (POWERCURSOR.PUSHGUTTER_SLOPE_DIVISION[i]+POWERCURSOR.PUSHGUTTER_SLOPE_DIVISION[i+1])/2.0f*POWERCURSOR.HEIGHT - (-vector[1]);
						break;
					}
				}
				slope *= POWERCURSOR.PUSHGUTTER_SLOPE_STRENGTH;
				*/
				// the flow of right(width) to left(height)
				dx += POWERCURSOR.PUSHGUTTER_FLOW_OF_VECTOR[0] * pseudoParam;
				dy += POWERCURSOR.PUSHGUTTER_FLOW_OF_VECTOR[1] * pseudoParam;
				//dy += POWERCURSOR.PUSHGUTTER_FLOW_OF_VECTOR[1] * pseudoParam - slope;
				break;
					
			case POWERCURSOR.SAND:
				// this parameter is not sufficient and have to be converted to a better one
				vx = nowx - prex;
				vy = nowy - prey;
				
				// the feedback of decreasing speed of the cursor
				 double addDx = vx * pseudoParam * POWERCURSOR.SAND_MAG;
				 double addDy = vy * pseudoParam * POWERCURSOR.SAND_MAG;
				// dx += addDx;
				// dy += addDy;
				
				// TODO
				// パラメータをぶれが少なくなるように変更
				// the feedback on the sand floor two-way
				double angle = vy!=0 ? Math.atan(vx/-vy) : Math.atan(Integer.MAX_VALUE);
				angle = (angle/(2*Math.PI))*360;
				if(vy>0){angle+=180;}
				double dirDepAngle = 0;//what does this parameter means?
				double angleDiff = Math.abs(angle - (dirDepAngle));
				if(angleDiff>180){angleDiff -= 180;}
				if(angleDiff>90){angleDiff = 180 - angleDiff;}
				// scale to 0-1
				angleDiff *= 1/90;
				double alignment = 1 - angleDiff;
				alignment = Math.sqrt(alignment);
				// make random value near to 0.8
				// double addDx = pseudoParam * (vx+vx+vx+vy)/8.0 * (Math.random()-0.8) * alignment * POWERCURSOR.SAND_MAG;
				// double addDy = pseudoParam * (vx+vy+vy+vy)/8.0 * (Math.random()-0.8) * alignment * POWERCURSOR.SAND_MAG;
				// double makeRandom = 0.8 + 0.1*Math.random() - 0.8*Math.random();
				// addDx += pseudoParam * (vx+vx+vx+vy)/8.0 * (makeRandom-0.8) * alignment * POWERCURSOR.SAND_MAG * (Math.abs(vx) > Math.abs(vy) ? 1.0f : 1.0f/3.0f);
				// addDy += pseudoParam * (vx+vy+vy+vy)/8.0 * (makeRandom-0.8) * alignment * POWERCURSOR.SAND_MAG * (Math.abs(vy) > Math.abs(vx) ? 1.0f : 1.0f/3.0f);
				addDx -= pseudoParam * (vx+vx+vx+vy)/8.0 * (Math.random()-0.8) * alignment * POWERCURSOR.SAND_MAG;
				addDy -= pseudoParam * (vx+vy+vy+vy)/8.0 * (Math.random()-0.8) * alignment * POWERCURSOR.SAND_MAG;
				dx += addDx;
				dy += addDy;
				break;
				
			case POWERCURSOR.GAUSSIAN:
				if(Math.abs(distance) <= pseudoRange){
					dx -= (pseudoRange/2-Math.abs(Math.abs(vector[0])-pseudoRange/2)) * 2 * (vector[0]>0 ? +1 : -1) * pseudoParam;
					dy -= (pseudoRange/2-Math.abs(Math.abs(vector[1])-pseudoRange/2)) * 2 * (vector[1]>0 ? +1 : -1) * pseudoParam;
				}
				break;
				
			case POWERCURSOR.RAMP:
				dx -= pseudoRange/2 * ((double)vector[0]/(double)distance) * pseudoParam;
				dy -= pseudoRange/2 * ((double)vector[1]/(double)distance) * pseudoParam;
				break;

			default:
				break;
			}
		}
		
		// return deformed point
		Point showPoint = new Point(cursor.x+(int)(old_dx-dx), cursor.y+(int)(old_dy-dy));
		return showPoint;
	}
	
	private static boolean isInRange(int pseudoType, float[] vector, float pseudoRange){

		switch(pseudoType){
		case POWERCURSOR.HOLE:
		case POWERCURSOR.HILL:
		case POWERCURSOR.GAUSSIAN:
			float distance = (float)Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2));
			if(distance < pseudoRange){
				return true;
			} else {
				return false;
			}
			
		case POWERCURSOR.PUSHGUTTER:
		case POWERCURSOR.SAND:
		case POWERCURSOR.RAMP:
			if(Math.abs(vector[0]) < pseudoRange && Math.abs(vector[1]) < pseudoRange){
				return true;				
			} else {
				return false;
			}
			
		default:
			break;
		}
		
		return false;
	}
}