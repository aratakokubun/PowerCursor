package org.example.PowerCursor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


/**
 * Experiment of the power cursor with the changeable parameters of feedback
 * Displaying the power cursor objects, cursor (that shows the touching point of the subjects), and utility buttons
 * @author kkbnart
 */
public class DragView extends SurfaceView implements SurfaceHolder.Callback
{
	private static final String TAG = "Drag View";
	
	// context
	Context parent;
	
	// thread
	private int sleep 		 = 10;
	private Handler mHandler = new Handler();
	// calculate FPS to make a loop of adding inconsistency
	private FPSManager myManager;
	
	private int windowWidth;
	private int windowHeight;
	
	// if which face to use
	public boolean isUseBehind;
	
	// Button image
	BitmapDrawable buttonSwitch;
	BitmapDrawable buttonAnswer;
	BitmapDrawable buttonAnswerRed;
	BitmapDrawable buttonAnswerBlue;
	
	// objects
	PowerCursorObject mPowerCursorObject;
	Cursor mCursor;
	
	// answer
	private int answer = -1;
	// time
	private long nowTime;
	private long firstTime;
	private long secondTime;

	/* -------------------------------------------------------------------- */
	public DragView(Context context, int width, int height) {
		super(context);
		parent = context;
		
		init(context, width, height);

		// start measuring FPS
		myManager = new FPSManager();
		
		(new Thread(new Runnable(){
			@Override
			public void run(){
				while(true){
					try{
						long elapsedTime = myManager.getElapsedTimeAndMeasure();
						long sleepTime = (sleep > elapsedTime) ? sleep-elapsedTime : 0;
						Thread.sleep(sleepTime);
						//Thread.sleep(sleep);
					}	catch(InterruptedException e){
						Log.e("Error", "Sleep Execute Error!");
					}
					mHandler.post(new Runnable(){
						@Override
						public void run(){
							// proceed image loop count
							PowerCursorObject.addImageLoopCount();
							// Update touching point here
							int x = mCursor.getTouchX() - (int)(mCursor.getIsTouched() ? MakeInconsistency.getDx() : 0);
							int y = mCursor.getTouchY() - (int)(mCursor.getIsTouched() ? MakeInconsistency.getDy() : 0);
							mCursor.setPos(x, y);
							MakeInconsistency.setNowx(x);
							MakeInconsistency.setNowy(y);
							// Make inconsistency while touching
							if(mCursor.getIsTouched()){
								MakeInconsistency.pseudo(
										mPowerCursorObject.getPseudoType(),
										mPowerCursorObject.getPseudoParam(),
										mPowerCursorObject.getPseudoRange(),
										mCursor.getPoint(),
										mCursor.getDiffTouchPoint(),
										mPowerCursorObject.getPoint());
							}
							// set the past coordinates to calculate the speed of the cursor
							MakeInconsistency.setPrex(x);
							MakeInconsistency.setPrey(y);
							// re-draw view
							invalidate();
						}
					});
				}
			}
		})).start();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
	}
	
	/* -------------------------------------------------------------------- */
	public void init(Context context, int width, int height){		
		// get size of window
		windowWidth = width;
		windowHeight = height;
		
		// initialize power cursor objects
		BitmapDrawable[] bitmapArray = new BitmapDrawable[6];
		for(int i = 0; i < bitmapArray.length; i++){
			bitmapArray[i] = (BitmapDrawable) getResources().getDrawable(POWERCURSOR.BITMAP_ID[i]);
		}
		// TODO
		// y座標位置を変更しました -100
		mPowerCursorObject = new PowerCursorObject(
				windowWidth/2 - POWERCURSOR.WIDTH/2,
				windowHeight/2 - POWERCURSOR.HEIGHT/2 - 100,
				windowWidth/2 + POWERCURSOR.WIDTH/2,
				windowHeight/2 + POWERCURSOR.HEIGHT/2 - 100,
				POWERCURSOR.PSEUDO_PARAMS[0],
				POWERCURSOR.HILL);
		mPowerCursorObject.setBitmapDrawable(bitmapArray);
		
		// initialize cursor
		mCursor = new Cursor();
		mCursor.setBitmap((BitmapDrawable) getResources().getDrawable(Cursor.CURSOR_IMAGE_ID));
		
		// initialize inconsistency
		MakeInconsistency.init();
		
		isUseBehind = true;
		
		// view object
		buttonSwitch = (BitmapDrawable) getResources().getDrawable(R.drawable.buttonnext);
		buttonSwitch.setBounds(CANVAS_LAYOUT.BUTTON_SWITCH_RECT);
		buttonAnswer = (BitmapDrawable) getResources().getDrawable(R.drawable.button_answer);
		buttonAnswer.setBounds(CANVAS_LAYOUT.BUTTON_ANSWER_RECT);
		//buttonAnswerRed = (BitmapDrawable) getResources().getDrawable(R.drawable.button_answer_red);
		buttonAnswerRed = (BitmapDrawable) getResources().getDrawable(R.drawable.button_zero);
		buttonAnswerRed.setBounds(CANVAS_LAYOUT.BUTTON_ANSWER_RED_RECT);
		//buttonAnswerBlue = (BitmapDrawable) getResources().getDrawable(R.drawable.button_answer_blue);
		buttonAnswerBlue = (BitmapDrawable) getResources().getDrawable(R.drawable.button_one);
		buttonAnswerBlue.setBounds(CANVAS_LAYOUT.BUTTON_ANSWER_BLUE_RECT);
		setBackgroundColor(COLOR.DARK_GRAY);
	
		answer = -1;
		nowTime = System.currentTimeMillis();
	}
	
	/* -------------------------------------------------------------------- */
	@Override
	public void onDraw(Canvas c){
		Paint p = new Paint();
		
		// draw object
		// if type == push gutter, draw moving image
		if(PowerCursorObject.getPseudoType() == POWERCURSOR.PUSHGUTTER){
			Bitmap bmp = mPowerCursorObject.getBitmap().getBitmap();
			c.drawBitmap(bmp, mPowerCursorObject.getMovingImageSrcRect(), mPowerCursorObject.getMovingImageDstRect(), p);
		} else {
			mPowerCursorObject.getBitmap().draw(c);
		}
		
		// draw cursor
		mCursor.getBitmap().draw(c);
		
		// show count
		p.setColor(COLOR.WHITE);
		p.setTextSize(CANVAS_LAYOUT.TextSizeL);
		c.drawText("試行 : " + MakeRandArray.getCount(), CANVAS_LAYOUT.COUNT_TEXT[0], CANVAS_LAYOUT.COUNT_TEXT[1], p);
		
		// draw switch button
		buttonSwitch.draw(c);
		if(MakeRandArray.getParamIndex()>0){
			buttonAnswerRed.draw(c);
			buttonAnswerBlue.draw(c);
			if(answer!=-1){
				buttonAnswer.draw(c);
				c.drawText("回答 : " + answer, CANVAS_LAYOUT.ANSWER_TEXT[0], CANVAS_LAYOUT.ANSWER_TEXT[1], p);
			}
		}
		
		// show info to the subjects which one he/she is testing
//		switch(MakeRandArray.getParamIndex()){
//		case 0:
//			p.setColor(COLOR.RED);
//			break;
//		case 1:
//			p.setColor(COLOR.BLUE);
//			break;
//		}
//		c.drawCircle(CANVAS_LAYOUT.INFO_SWITCH_CIRCLE[0], CANVAS_LAYOUT.INFO_SWITCH_CIRCLE[1], CANVAS_LAYOUT.CircleSizeL, p);
		p.setColor(COLOR.WHITE);
		p.setTextSize(CANVAS_LAYOUT.TextSizeXL);
		c.drawText(String.valueOf(MakeRandArray.getParamIndex()), CANVAS_LAYOUT.INDEX_TEXT[0], CANVAS_LAYOUT.INDEX_TEXT[1], p);
		
		// if already the count is over, show finish
		if(MakeRandArray.getIsFinished()){
			p.setColor(COLOR.WHITE);
			p.setTextSize(CANVAS_LAYOUT.TextSizeXL);
			c.drawText("Finished!", CANVAS_LAYOUT.FINISH_TEXT[0], CANVAS_LAYOUT.FINISH_TEXT[1], p);
		}
	}

	/* -------------------------------------------------------------------- */
	@Override
	public boolean onTouchEvent(MotionEvent ev){
		int action 	= (int)ev.getAction();
		float x = ev.getX() - (mCursor.getIsTouched() ? MakeInconsistency.getDx() : 0);
		float y = ev.getY() - (mCursor.getIsTouched() ? MakeInconsistency.getDy() : 0);

		//always enabled touch button
		if(action == MotionEvent.ACTION_DOWN){
			if(atSwitchButton(x, y)){
				TouchLog.info("Switch to " + MakeRandArray.getParam(), true);
				return true;
			} else if(atAnswerButton(x, y)){
				TouchLog.header("Start next trial " + MakeRandArray.getParam());
				return true;
			} else if(atSwitchAnswerButton(x, y)){
				return true;
			}
		}
		
		// TODO
		// Is it good to make disable to touch from front touch panel without the buttons?
		if(!isUseBehind){
			//mCursor.setPos((int)x, (int)y);
			// preserve previous touch point
			mCursor.setPreTouchX(mCursor.getTouchX());
			mCursor.setPreTouchY(mCursor.getTouchY());
			mCursor.setTouchX((int)ev.getX());
			mCursor.setTouchY((int)ev.getY());
	
			if(action == MotionEvent.ACTION_DOWN){
				MakeInconsistency.setPrex((int)x);
				MakeInconsistency.setPrey((int)y);
				mCursor.setIsTouched(true);
			} else if (action == MotionEvent.ACTION_UP) {
				mCursor.setIsTouched(false);
				MakeInconsistency.init();
			} else {
				mCursor.setIsTouched(true);
			}
		}

		return true;
	}
	
	//to divide touch from behind and touch from front, make touch
	public boolean onTouchBehind(MotionEvent ev){
		// TODO
		// 受信データにタグをつけて二回描画することを防ぐ
		int action 	= (int)ev.getAction();
		float x = ev.getX() - (mCursor.getIsTouched() ? MakeInconsistency.getDx() : 0);
		float y = ev.getY() - (mCursor.getIsTouched() ? MakeInconsistency.getDy() : 0);
		float press = ev.getPressure();
		
		//mCursor.setPos((int)x, (int)y);
		// preserve previous touch point
		mCursor.setPreTouchX(mCursor.getTouchX());
		mCursor.setPreTouchY(mCursor.getTouchY());
		mCursor.setTouchX((int)ev.getX());
		mCursor.setTouchY((int)ev.getY());

		if(action == MotionEvent.ACTION_DOWN){
			MakeInconsistency.setPrex((int)x);
			MakeInconsistency.setPrey((int)y);
			mCursor.setIsTouched(true);
			TouchLog.info("Touch Down", true);
		} else if(action == MotionEvent.ACTION_UP){
			mCursor.setIsTouched(false);
			MakeInconsistency.init();
			TouchLog.info("Touch Up", true);
		} else {
			mCursor.setIsTouched(true);
		}
		
		// write touch information to log file
		String touchInfo = x + ":" + y + ":" + press;
		TouchLog.add(touchInfo, 1);
		
		return true;
	}
	
	private boolean atSwitchButton(float x, float y){
		if(
				x > CANVAS_LAYOUT.BUTTON_SWITCH_RECT.left &&
				y > CANVAS_LAYOUT.BUTTON_SWITCH_RECT.top &&
				x < CANVAS_LAYOUT.BUTTON_SWITCH_RECT.right &&
				y < CANVAS_LAYOUT.BUTTON_SWITCH_RECT.bottom
		){
			switchParameter();
			return true;
		}
		return false;
	}
		
	private boolean atAnswerButton(float x, float y){
		if(
				x > CANVAS_LAYOUT.BUTTON_ANSWER_RECT.left &&
				y > CANVAS_LAYOUT.BUTTON_ANSWER_RECT.top &&
				x < CANVAS_LAYOUT.BUTTON_ANSWER_RECT.right &&
				y < CANVAS_LAYOUT.BUTTON_ANSWER_RECT.bottom
		){
			if(answer >= 0){
				writeAnswerToLogFile();
				setNextParameter();
				if(parent != null)
					Toast.makeText(parent, "試行番号:"+MakeRandArray.getCount(), Toast.LENGTH_SHORT).show();
				return true;
			}
		}
		return false;
	}
	
	private boolean atSwitchAnswerButton(float x, float y){
		if(MakeRandArray.getParamIndex() > 0){
			if(
					x > CANVAS_LAYOUT.BUTTON_ANSWER_RED_RECT.left &&
					y > CANVAS_LAYOUT.BUTTON_ANSWER_RED_RECT.top &&
					x < CANVAS_LAYOUT.BUTTON_ANSWER_RED_RECT.right &&
					y < CANVAS_LAYOUT.BUTTON_ANSWER_RED_RECT.bottom
			){
				answer = 0;
				return true;
			} else if(
					x > CANVAS_LAYOUT.BUTTON_ANSWER_BLUE_RECT.left &&
					y > CANVAS_LAYOUT.BUTTON_ANSWER_BLUE_RECT.top &&
					x < CANVAS_LAYOUT.BUTTON_ANSWER_BLUE_RECT.right &&
					y < CANVAS_LAYOUT.BUTTON_ANSWER_BLUE_RECT.bottom
			){
				answer = 1;
				return true;
			}
		}
		return false;
	}
	
	/* -------------------------------------------------------------------- */
	public void switchPanel(){
		isUseBehind = !isUseBehind;
	}
	
	public void switchParameter(){
		if(!MakeRandArray.switchParamIndex()){
			Toast.makeText(parent, "例外的に最初のパラメータに戻りました．意図的に戻した場合はこの試行の最初からやり直してください．", Toast.LENGTH_SHORT).show();
		} else {
			// calculate first time
			firstTime = System.currentTimeMillis() - nowTime;
			// timer start
			nowTime = System.currentTimeMillis();
		}
		PowerCursorObject.setPseudoParam(MakeRandArray.getParam());
	}
	
	public void setNextParameter(){
		if(MakeRandArray.goNext()){
			// calculate second time
			secondTime = System.currentTimeMillis() - nowTime;
			PowerCursorObject.setPseudoParam(MakeRandArray.getParam());
			// timer start
			nowTime = System.currentTimeMillis();
		} else {
			Toast.makeText(parent, "回答が指定されていません．", Toast.LENGTH_SHORT).show();
		}
	}
	
	/* -------------------------------------------------------------------- */
	// write answer to log file
	public void writeAnswerToLogFile(){
		int[] pair = MakeRandArray.getArray();
		int type = MakeRandArray.getType();
		String data = 
			MakeRandArray.getCount() + "," +
			pair[0] + "," +
			pair[1] + "," +
			pair[0]*MakeRandArray.getScaleFromType(type) + "," +
			pair[1]*MakeRandArray.getScaleFromType(type) + "," +
			(pair[0]>pair[1] ? pair[0]-pair[1] : pair[1]-pair[0]) + "," +
			((pair[0]==0||pair[1]==0) ? "n/0" : (pair[0]>pair[1] ? (float)pair[0]/(float)pair[1] : (float)pair[1]/(float)pair[0])) + "," +
			answer + "," +
			firstTime + "," +
			secondTime + ",\n";
		FileReadWrite.WriteData(data);
		
		// reset answer
		answer = -1;
	}
	
	/* -------------------------------------------------------------------- */
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		TouchLog.forceWriteToFile();
	}

}