package org.example.PowerCursor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Send and Receive Message using BlueTooth.
 * Information in the message contains
 * 0, message tag
 * 1, touch x coordinate / or resist id, type id, etc...
 * 2, touch y coordinate
 * 3, touch action
 * 4, touch pressure
 * 5, touch time
 */

public class Sender extends BTActivity implements BlueTooth {
	private static final String TAG = "Sender Activity";
	
	private static final int EXIT_LOCK_COUNT = 10;	
	private boolean onMenuControlled = true;
	private int onMenuControllCount = 0;
		
	private Display disp;
	private int Width;
	private int Height;
		
	TextView val_x;
	TextView val_y;
	TextView val_p;
	TextView val_fps;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendview);
		
		disp = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).
				getDefaultDisplay();
		Width  = disp.getWidth();
		Height = disp.getHeight();
		
		// TouchLog.init();		
		// TouchLog.start(NAME);
		
		val_x 	= (TextView)findViewById(R.id.val_x);
		val_y 	= (TextView)findViewById(R.id.val_y);
		val_p 	= (TextView)findViewById(R.id.val_p);
		val_fps = (TextView)findViewById(R.id.val_fps);
		
		onMenuControlled = true;
		onMenuControllCount = 0;
	}

	/* -------------------------------------------------------------------- */
    /**
     * menu item setting
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.scan:
            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            return true;
            
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        	
        case R.id.exit:
        	float[] exit = {TAG_END, 0f, 0f, 0f, 0f, 0f};
        	sendMsg(exit);
        	finish();
        	System.exit(0);
        	return true;
        	
        default:
        	break;
        }
        return false;
    }

	/* -------------------------------------------------------------------- */
    /**
     * decode BT message
     */
    @Override
    public void decodeMsg(byte[] buf){
    	int size = Integer.SIZE / Byte.SIZE;
    	//read tag
    	int tag = (int)getFloat(buf, 0);
    	switch(tag){
    	case TAG_NEXT:
    		int _url 	= (int)getFloat(buf, size*1);
    		String _msg  = "URL:"+_url;
    		// TouchLog.info(_msg, true);
    		break;
    		
    	case TAG_END:
    		finish();
    		System.exit(0);
    		break;
    	}
    }
    
    @Override
    public void sendMsg(float[] buf) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BTService.STATE_CONNECTED) {
        	//showToast(R.string.not_connected);
            return;
        }

        // Get the message bytes and tell the BluetoothChatService to write
        int size = Integer.SIZE/Byte.SIZE;
        byte[] send = new byte[size * MESSAGE_ARRAY_SIZE];
        for(int i = 0; i < MESSAGE_ARRAY_SIZE; i++){
        	printFloat(send, size*i, buf[i]);
        }
        mChatService.writeOne(send);
        
        // Reset out string buffer to zero and clear the edit text field
        mOutStringBuffer.setLength(0);
    }
    
	/* -------------------------------------------------------------------- */
    /**
     * decode touch
     */
	@Override
	public boolean onTouchEvent(MotionEvent event){
		int x 	= (int) event.getX();
		int y 	= (int) event.getY();
		int action 	= event.getAction();
		float press = event.getPressure();
		long  time  = System.currentTimeMillis();
		
		// using landscape
		//float[] buf = {TAG_POS, (float)x, (float)(Height-y), action, press, time};
		// using portrait
		float[] buf = {TAG_POS, (float)(Width-x), (float)y, action, press, time};
		sendMsg(buf);

		if(action == MotionEvent.ACTION_DOWN){
			// TouchLog.info("Touch Down", true);
		} else if(action == MotionEvent.ACTION_UP){
			// TouchLog.info("Touch Up", false);
		}
		String touchInfo = x + ":" + y + ":" + press;
		// TouchLog.add(touchInfo, 1);
		
		val_x.setText(String.valueOf(x));
		val_y.setText(String.valueOf(y));
		val_p.setText(String.valueOf(press));
		
		return true;
	}
	
	/* -------------------------------------------------------------------- */
    /**
     * control key buttons(home, return, etc...)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
      if(keyCode==KeyEvent.KEYCODE_BACK){
    	  // exit from sender is prohibited basically.
    	  if(onMenuControlled){
    		  onMenuControllCount++;
    		  if(onMenuControllCount >= EXIT_LOCK_COUNT){
    			  onMenuControlled = false;
    		  }
    		  return false;
    	  } else {
    		  onExit();
    		  return true;
    	  }
      } else if(keyCode==KeyEvent.KEYCODE_HOME){
    	  if(onMenuControlled){
    		  return false;
    	  } else {
    		  moveTaskToBack(true);
    		  return true;
    	  }
      }
      return false;
    }

    public void onExit(){
    	float[] exit = {TAG_END, 0f, 0f, 0f, 0f, 0f};
    	sendMsg(exit);
    	finish();
    	System.exit(0);
	}
    
	/* -------------------------------------------------------------------- */
    @Override
    public void onDestroy(){
    	// TouchLog.forceWriteToFile();
    	super.onDestroy();
    }
    
}