package org.example.PowerCursor;

import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;

public class Cursor {
	public static final int CURSOR_IMAGE_ID = R.drawable.cursor;
	
	private static final int CURSOR_WIDTH = 30;
	private static final int CURSOR_HEIGHT = 30;

	private boolean isTouched;
	private int x;
	private int y;
	private int touchx;
	private int touchy;
	private int preTouchx;
	private int preTouchy;
	private BitmapDrawable bitmap;
	
	public Cursor(){
		isTouched = false;
		x = 0;
		y = 0;
		touchx = x;
		touchy = y;
		preTouchx = x;
		preTouchy = y;
	}
	
	// setter
	public void setIsTouched(boolean isTouched){
		this.isTouched = isTouched;
	}
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	public void setTouchX(int x){
		this.touchx = x;
	}
	public void setTouchY(int y){
		this.touchy = y;
	}
	public void setPreTouchX(int x){
		this.preTouchx = x;
	}
	public void setPreTouchY(int y){
		this.preTouchy = y;
	}
	public void setPos(int x, int y){
		this.x = x;
		this.y = y;
		setBitmapBounds();
	}
	public void setBitmap(BitmapDrawable bitmap){
		this.bitmap = bitmap;
		setBitmapBounds();
	}
	
	// getter
	public boolean getIsTouched(){
		return isTouched;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getTouchX(){
		return touchx;
	}
	public int getTouchY(){
		return touchy;
	}
	public int getPreTouchX(){
		return preTouchx;
	}
	public int getPreTouchY(){
		return preTouchy;
	}
	public Point getPoint(){
		return new Point(x, y);
	}
	public Point getDiffTouchPoint(){
		return new Point(touchx-preTouchx, touchy-preTouchy);
	}
	public BitmapDrawable getBitmap(){
		return bitmap;
	}
	
	public void setBitmapBounds(){
		bitmap.setBounds(x-CURSOR_WIDTH/2, y-CURSOR_HEIGHT/2, x+CURSOR_WIDTH/2, y+CURSOR_HEIGHT/2);
	}
	
}
