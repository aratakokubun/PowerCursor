package org.example.PowerCursor;

import android.util.Log;

public class FPSManager {
	private static final String TAG = "FPS Manager";
	
	private double fps;
	private long elapsed_time;
	private long prev_time;
	private long now_time;
	
	private boolean onMeasureMin;
	private double min_fps;

	public FPSManager(){
		fps			 = 0;
		prev_time 	 = System.currentTimeMillis();
		now_time  	 = System.currentTimeMillis();
		elapsed_time = now_time - prev_time;
		onMeasureMin = false;
	}
	
	public long getElapsedTime(){
		now_time  = System.currentTimeMillis();
		elapsed_time = now_time - prev_time;
		return elapsed_time;
	}
	
	public long getElapsedTimeAndMeasure(){
		now_time  = System.currentTimeMillis();
		elapsed_time = now_time - prev_time;
		fps			 = elapsed_time>0 ? 1000.0 / elapsed_time : 1000.0;
		prev_time	 = now_time;
		return elapsed_time;
	}
	
	public double measure(){
		now_time  	 = System.currentTimeMillis();
		elapsed_time = now_time - prev_time;
		fps			 = elapsed_time>0 ? 1000.0 / elapsed_time : 1000.0;
		prev_time	 = now_time;
		return fps;
	}
	
	public boolean startMeasureMin(){return onMeasureMin = !onMeasureMin;}
	
	public double measureMinFps(){
		if(onMeasureMin){
			if(min_fps>fps) min_fps = fps;
		} else {
			min_fps = 100L;
		}
		return min_fps;
	}
	
	public double getNowFPS(){
		return fps;
	}
}