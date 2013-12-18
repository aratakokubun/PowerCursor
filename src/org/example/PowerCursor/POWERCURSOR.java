package org.example.PowerCursor;

public class POWERCURSOR {
	// the size of the power cursor objects
	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;
	
	// type of the power cursor effect
	public static final int NONE		= 0;
	public static final int HOLE		= 1;
	public static final int HILL		= 2;
	public static final int PUSHGUTTER	= 3;
	public static final int SAND		= 4;
	public static final int GAUSSIAN	= 5;
	public static final int RAMP		= 6;
	
	// name of the power cursor objects
	public static final String[] PSEUDO_NAME = {
		"None", "Hole", "Hill", "PushGutter", "Sand", "Gaussian", "Ramp"
	};
	
	// TODO
	// bitmapの差し替え
	public static final int[] BITMAP_ID = {
		R.drawable.buttonnext, R.drawable.hole, R.drawable.hill,
		R.drawable.pushgutter_long, R.drawable.sand, R.drawable.buttonnext, R.drawable.buttonnext
	};
	
	// TODO
	// 等比的にするか，等差的にするか
	// ここは文献を調べるべし
	// 今のところ，等差(Feeling Bump and Holeから)で
	// 0.005f, 0.015f, 0.025f, 0.035f, 0.045f
	// もしくは
	// 0.005f, 0.02f, 0.035f, 0.05f, 0.065f
	// あたりが有力
	public static final float[] PSEUDO_PARAMS = {
		// 0.005f, 0.015f, 0.025f, 0.035f, 0.045f
		// 0.005f, 0.02f, 0.035f, 0.05f, 0.065f
		// 0.005f, 0.025f, 0.045f, 0.065f, 0.08f
		// 0.005f, 0.03f, 0.055f, 0.08f, 0.105f
		0.0f, 0.005f, 0.01f, 0.015f, 0.02f, 0.025f, 0.03f, 0.035f, 0.04f, 0.045f, 0.05f, 0.055f, 0.06f, 0.065f, 0.07f, 0.075f, 0.08f, 0.085f, 0.09f, 0.095f, 0.1f, 0.11f, 0.12f, 0.13f, 0.14f, 0.15f, 0.16f
	};
	
	public static final int[] PUSHGUTTER_FLOW_OF_VECTOR = {POWERCURSOR.WIDTH/10, 0};
	public static final float[] PUSHGUTTER_SLOPE_DIVISION = {-2.0f/4.0f, -1.0f/4.0f, 0.0f, 1.0f/4.0f, 2.0f};
	public static final float PUSHGUTTER_SLOPE_STRENGTH = 0.01f;
	public static final int SAND_MAG = 10;
	
	public static final float SCALE_PARAM_HOLE = 0.0150f;
	public static final float SCALE_PARAM_PUSHGUTTER = 0.020f;
	public static final float SCALE_PARAM_SAND = 0.020f;
}