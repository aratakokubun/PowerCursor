package org.example.PowerCursor;

import android.graphics.Rect;

public class CANVAS_LAYOUT {
	public static final float BASE_SCALE_FACTOR = 1.0f;
	
	public static int TextSizeS = 20;
	public static int TextSizeM = 40;
	public static int TextSizeL = 60;
	public static int TextSizeXL = 100;
	
	public static int CircleSizeS = 5;
	public static int CircleSizeM = 10;
	public static int CircleSizeL = 20;
	public static int CitcleSizeXL = 30;

	// layout of landscape
	// for the participants of right dominant
	public static final Rect BUTTON_SWITCH_RECT = new Rect(0, 860, 100, 960);
	public static final Rect BUTTON_ANSWER_RECT = new Rect(200, 900, 400, 980);
	public static final Rect BUTTON_ANSWER_RED_RECT = new Rect(100, 1000, 300, 1080);
	public static final Rect BUTTON_ANSWER_BLUE_RECT = new Rect(400, 1000, 600, 1080);
	public static final int[] FPS_TEXT = {100, 20};
	public static final int[] COUNT_TEXT = {200, 80};
	public static final int[] ANSWER_TEXT = {300, 1160};
	public static final int[] FINISH_TEXT = {200, 500};
	public static final int[] INDEX_TEXT = {50, 80};
	public static final int[] INFO_SWITCH_CIRCLE = {50,50};
	public static final int[] INFO_ON_TOUCHING_CIRCLE = {40, 80};
	
	// for the participants of left dominant
//	public static final Rect BUTTON_SWITCH_RECT = new Rect(800-0, 860, 800-100, 960);
//	public static final Rect BUTTON_ANSWER_RECT = new Rect(800-200, 900, 800-400, 980);
//	public static final Rect BUTTON_ANSWER_RED_RECT = new Rect(800-100, 1000, 800-300, 1080);
//	public static final Rect BUTTON_ANSWER_BLUE_RECT = new Rect(800-400, 1000, 800-600, 1080);
}