package org.example.PowerCursor;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


public class ShowPowerCursorActivity extends Receiver {
	
	public static final String SUBJECT_NAME = "Name";
	private DragView mDragView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		// get the instance of window manager
        WindowManager wm = (WindowManager)getSystemService(Activity.WINDOW_SERVICE);
        // generate the instance of display
        Display disp = wm.getDefaultDisplay();
        
        // delete title bar of application
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDragView = new DragView(this, disp.getWidth(), disp.getHeight());
		setContentView(mDragView);
        // delete status bar of application
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ExperimentPowerCursorController.setParent(this);
        ExperimentPowerCursorController.setDefaultParameters();
        
        // make random array of parameters
        MakeRandArray.initArray();
        
        // show caution toast
        Toast.makeText(this, "オブジェクトのタイプを指定！やり直せないので注意!", Toast.LENGTH_LONG).show();
        // set pseudo type on alert
        ExperimentPowerCursorController.setPseudoTypeDialog();
        
		// initialize touch log
		TouchLog.init();
		// このタイミングだとinitが間に合わずにログファイルが二つできるバグがあるが，基本的に問題はないのでこのままにしておく
		TouchLog.start(NAME);
    }
    
	/* -------------------------------------------------------------------- */
    /**
     * menu item setting
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("PowerCursor Parameters");
        menu.add("Next");
        menu.add("Switch Panel");
    	super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle() == "PowerCursor Parameters"){
			// TODO
			// アプリが強制終了した際に手動で設定できた方が良い
			//mDragView.intentPowerCursorObectDialog();
        	return true;
		} else if(item.getTitle() == "Next"){
			mDragView.switchParameter();
			return true;
		} else if(item.getTitle() == "Switch Panel"){
			mDragView.switchPanel();
			return true;
		}
    	super.onOptionsItemSelected(item);
		
        return false;
    }

	/* -------------------------------------------------------------------- */
    @Override
    public void handleCursor(float[] data){
		MotionEvent ev = MotionEvent.obtain(0, 0, (int)data[2], data[0], data[1], 0);
    	mDragView.onTouchBehind(ev);
    	ev.recycle();
    }

}