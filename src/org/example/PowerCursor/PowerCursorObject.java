package org.example.PowerCursor;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class PowerCursorObject extends ObjectWrapper{
	private static final String TAG = "Power Cursor Object";
	
	private static float pseudoParam;
	private static int pseudoType;
	private BitmapDrawable[] bitmapDrawable = new BitmapDrawable[6];
	private static float imageLoopCount;// to loop the moving object(ex. push gutter, twister, etc...)
	
	public PowerCursorObject(){
		super();
		pseudoParam = POWERCURSOR.PSEUDO_PARAMS[0];
		pseudoType = POWERCURSOR.HILL;
		imageLoopCount = 0f;
	}
	
	public PowerCursorObject(float param, int type){
		super();
		pseudoParam = param;
		pseudoType = type;
		imageLoopCount = 0f;
	}
	
	public PowerCursorObject(int left, int top, int right, int bottom, float param, int type){
		super(left, top, right, bottom);
		pseudoParam = param;
		pseudoType = type;
		imageLoopCount = 0f;
	}
	
	public void setBitmapDrawable(BitmapDrawable[] bitmap){
		if(bitmapDrawable.length != bitmap.length){
			Log.e(TAG, "Error. Bitmap array does not fit to Original Bitmap array");
			return;
		}
		for(int i = 0; i < bitmapDrawable.length; i++){
			bitmapDrawable[i] = bitmap[i];
		}
		setBitmapScale();
	}

	// Setter
	public static void setPseudoParam(float param){
		pseudoParam = param;
		imageLoopCount = 0;
	}
	public static void setPseudoType(int type){
		pseudoType = type;
		imageLoopCount = 0;
	}
	public static void setImageLoopCount(float count){
		imageLoopCount = count;
	}
	
	// Getter
	public static float getPseudoParam(){
		return pseudoParam;
	}
	public static int getPseudoType(){
		return pseudoType;
	}
	public int getPseudoRange(){
		return getWidth()/2;
	}
	public Point getPoint(){
		return new Point((getRight()+getLeft())/2, (getTop()+getBottom())/2);
	}
	public static float getImageLoopCount(){
		return imageLoopCount;
	}
	
	// create moving image rect
	public static final int IMAGE_LOOP_START = 0;
	public static final int IMAGE_LOOP_END = 860;
	public Rect getMovingImageSrcRect(){
		return new Rect(
				(int)(IMAGE_LOOP_START+imageLoopCount), 0 ,
				(int)(IMAGE_LOOP_START+imageLoopCount+POWERCURSOR.WIDTH), 256);
	}
	public Rect getMovingImageDstRect(){
		return new Rect(getLeft(), getTop(), getRight(), getBottom());
	}
	public static void addImageLoopCount(){
		//imageLoopCount += POWERCURSOR.PUSHGUTTER_FLOW_OF_VECTOR[0] * pseudoParam;
		imageLoopCount += POWERCURSOR.PUSHGUTTER_FLOW_OF_VECTOR[0] * 0.02f;
		while(IMAGE_LOOP_START+imageLoopCount+POWERCURSOR.WIDTH > IMAGE_LOOP_END){
			imageLoopCount -= (IMAGE_LOOP_END - IMAGE_LOOP_START - POWERCURSOR.WIDTH);
		}
	}
	
	@Override
	public void setWrapper(int left, int top, int right, int bottom){
		super.setWrapper(left, top, right, bottom);
		setBitmapScale();
	}
	
	public void setBitmapScale(){
		for(int i = 0; i < bitmapDrawable.length; i++){
			bitmapDrawable[i].setBounds(getLeft(), getTop(), getRight(), getBottom());
		}
	}
	
	// TODO
	// on displaying an object of push gutter (pseudoType = POWERCURSOR.PUSHGUTTER), the bitmap image has to be changed according to time
	public BitmapDrawable getBitmap(){
		return bitmapDrawable[pseudoType];
	}
	public BitmapDrawable getBitamp(int index){
		return bitmapDrawable[index];
	}
	
	public void showPowerCursorParamDialog(){
		// show dialog
		ExperimentPowerCursorController.setExperimentSwitchPowerCursorDialog();
	}
}