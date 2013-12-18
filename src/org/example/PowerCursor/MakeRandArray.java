package org.example.PowerCursor;

import android.util.Log;

public class MakeRandArray {
	private static final String TAG = "Make Random Array";
	
	// experimental settings
	private static final int ARRAY = 96;
	private static final int LOOP = 1000;
	
	// all set of parameters
	private static final int[][] SET = {
		{0,4}, {0,4}, {0,4}, {0,4}, {0,4}, {0,4}, {0,4}, {0,4}, {0,4}, {0,4}, {0,4}, {0,4},
		{4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, {4,0}, 
		{0,3}, {1,4}, {0,3}, {1,4}, {0,3}, {1,4}, {0,3}, {1,4}, {0,3}, {1,4}, {0,3}, {1,4}, 
		{3,0}, {4,1}, {3,0}, {4,1}, {3,0}, {4,1}, {3,0}, {4,1}, {3,0}, {4,1}, {3,0}, {4,1}, 
		{0,2}, {1,3}, {2,4}, {0,2}, {1,3}, {2,4}, {0,2}, {1,3}, {2,4}, {0,2}, {1,3}, {2,4}, 
		{2,0}, {3,1}, {4,2}, {2,0}, {3,1}, {4,2}, {2,0}, {3,1}, {4,2}, {2,0}, {3,1}, {4,2}, 
		{0,1}, {1,2}, {2,3}, {3,4}, {0,1}, {1,2}, {2,3}, {3,4}, {0,1}, {1,2}, {2,3}, {3,4}, 
		{1,0}, {2,1}, {3,2}, {4,3}, {1,0}, {2,1}, {3,2}, {4,3}, {1,0}, {2,1}, {3,2}, {4,3}, 
	};
	
	// TODO
	// もしアプリが強制終了してしまった場合，データをdropbox経由で取り出し，残り分だけSETに格納
	// 更にARRAY定数もそれに合わせて変更する
	// initArrayのswapをコメントアウト→実行
	// 書き換えた場合はその旨をコメントで残し，実験終了後にすぐ修正
	// TODO
	
	// param array
	private static int[][] paramArray = new int[ARRAY][2];
	// flag if array is initialized
	private static boolean isInit = false;
	// experimental count
	private static int count = 0;
	// switch parameter index
	private static int paramIndex = 0;
	// power cursor object type
	private static boolean isTypeSet = false;
	private static int type = 0;
	
	// flag is finished
	private static boolean isFinished = false;
	
	/* -------------------------------------------------------------------- */
	// must be called when the activity started
	public static void initArray(){
		// set default value
		paramArray = SET;
		
		// random swap
		for(int i = 0; i < ARRAY*LOOP; i++){
			// swap array in rate of 1/2
			if(Math.random()>0.5){
				int no1 = (int)(Math.random()*ARRAY);
				int no2 = (int)(Math.random()*ARRAY);
				swap(no1, no2);
			}
		}
		isInit = true;
	}
	
	// swap array
	private static void swap(int no1, int no2){
		int[] temp = paramArray[no1];
		paramArray[no1] = paramArray[no2];
		paramArray[no2] = temp;
	}
	
	// swap array child
	@Deprecated
	private static void swapChild(int no){
		int temp = paramArray[no][0];
		paramArray[no][0] = paramArray[no][1];
		paramArray[no][1] = temp;
	}
	
	/* -------------------------------------------------------------------- */
	// get parameters according to count
	public static float[] getParams(){
		float[] params = {paramArray[count][0]*getScaleFromType(type), paramArray[count][1]*getScaleFromType(type)};
		return params;
	}
	
	public static float[] getParams(int index){
		float[] params = {paramArray[index][0]*getScaleFromType(type), paramArray[index][1]*getScaleFromType(type)};
		return params;
	}
	
	public static float getParam(){
		Log.e(TAG, "switch to " + paramArray[count][paramIndex]*getScaleFromType(type));
		return paramArray[count][paramIndex]*getScaleFromType(type);
	}
	
	// get array according to count
	public static int[] getArray(){
		return paramArray[count];
	}
	
	public static int[] getArray(int index){
		return paramArray[index];
	}
	
	/* -------------------------------------------------------------------- */
	// go next count
	public static boolean goNext(){
		if(paramIndex > 0){
			if(count < ARRAY-1){
				count++;
			} else {
				isFinished = true;
			}
			paramIndex = 0;
			return true;
		} else {
			return false;
		}
	}
	public static void goBack(){
		count--;
		paramIndex = 0;
	}
	
	public static int getCount(){
		return count;
	}
	
	public static boolean getIsFinished(){
		return isFinished;
	}
	
	public static boolean switchParamIndex(){
		// if only the case of a mistake, go back to the first
		if(paramIndex > 0){
			paramIndex--;
			return false;
		} else {
			paramIndex++;
			return true;
		}
	}
	
	public static int getParamIndex(){
		return paramIndex;
	}
	
	/* -------------------------------------------------------------------- */
	// set and get type of pseudo
	public static final int getType(){
		return type;
	}
	
	public static final void setType(int pseudoType){
		isTypeSet = true;
		type = pseudoType;
	}
	
	// get if type is set
	public static boolean getIsTypeSet(){
		return isTypeSet;
	}
	
	/* -------------------------------------------------------------------- */
	// write data to backup file
	public static boolean writeArrayToBackupFile(){
		if(isInit){
			String data = 
					"object:"+POWERCURSOR.PSEUDO_NAME[type] + ",\n" +
					"param0" + "," +
					"param1" + "," +
					"param0_float" + "," +
					"param1_float" + "," +
					"diff" + "," +
					"rate" + "," +
					"count" + ",\n";
			for(int i = 0; i < ARRAY; i++){
				int a0 = paramArray[i][0];
				int a1 = paramArray[i][1];
				data += 
					a0 + "," +
					a1 + "," +
					a0*getScaleFromType(type) + "," +
					a1*getScaleFromType(type) + "," +
					(a0>a1 ? a0-a1 : a1-a0) + "," +
					((a0==0||a1==0) ? "n/0" : (a0>a1 ? (float)a0/(float)a1 : (float)a1/(float)a0)) + "," +
					i + ",\n";
			}
			FileReadWrite.WriteBackUp(data);
			return true;
		} else {
			
			return false;
		}
	}
	
	// make header in log file
	public static void makeLogHeader(){
		// make header in log file
		FileReadWrite.defineLocation(ShowPowerCursorActivity.SUBJECT_NAME);
		String header = 
				"object:"+POWERCURSOR.PSEUDO_NAME[type] + ",\n" +
				"count" + "," +
				"param0" + "," +
				"param1" + "," +
				"param0_float" + "," +
				"param1_float" + "," +
				"diff" + "," +
				"rate" + "," +
				"answer(0:before(red) 1:after(blue)" + "," +
				"time(0)" + "," +
				"time(1)" + ",\n";
		FileReadWrite.WriteData(header);
	}
	
	/* -------------------------------------------------------------------- */
	// get scale from pseudo type
	public static float getScaleFromType(int type){
		switch(type){
		case POWERCURSOR.HOLE:
			return POWERCURSOR.SCALE_PARAM_HOLE;
		case POWERCURSOR.PUSHGUTTER:
			return POWERCURSOR.SCALE_PARAM_PUSHGUTTER;
		case POWERCURSOR.SAND:
			return POWERCURSOR.SCALE_PARAM_SAND;
			default:
		return 0.0f;
		}
	}
}
