package org.example.PowerCursor;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

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

public class Receiver extends BTActivity implements BlueTooth {
	private static final int EXIT_LOCK_COUNT = 3;	
	private boolean onMenuControlled = true;
	private int onMenuControllCount = 0;
	
    /* ---------------------------------------------------------------------- */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		onMenuControlled = true;
		onMenuControllCount = 0;
	}

    /* ---------------------------------------------------------------------- */
	// option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Scan");
        menu.add("Discoverable");
        menu.add("Exit");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle() == "Scan"){
			scan();
            return true;
            
		} else if (item.getTitle() == "Discoverable"){
			discoverable();
            return true;
			
		} else if (item.getTitle() == "Exit"){
			exit();
        	return true;
			
		}
		
        return false;
    }

    /* ---------------------------------------------------------------------- */
    // receive a BT message and decode it
    @Override
    public void decodeMsg(byte[] buf){
    	float[] receiveData = new float[MESSAGE_ARRAY_SIZE-1];
    	int size = Integer.SIZE / Byte.SIZE;
    	
    	//read tag
    	int tag = (int)getFloat(buf, 0);
    	switch(tag){
    	case TAG_POS:
			for (int i = 1; i < MESSAGE_ARRAY_SIZE; i++) {
				receiveData[i - 1] = getFloat(buf, size * i);
			}
			handleCursor(receiveData);
        	break;
    		
    	case TAG_END:
    		finish();
    		System.exit(0);
    		break;
    		
    	default:
    		break;
    	}
    }
    
    public void handleCursor(float[] data){
    	//scripted in detail at override
    }
    
    /* ---------------------------------------------------------------------- */
    // send a BT message
    @Override
    public void sendMsg(float[] buf) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BTService.STATE_CONNECTED) {
        	showToast(R.string.not_connected);
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

    /* ---------------------------------------------------------------------- */
    // control key buttons(home, return, etc...)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
      if(keyCode==KeyEvent.KEYCODE_BACK){
    	  if(onMenuControlled){
    		  onMenuControllCount++;
    		  if(onMenuControllCount >= EXIT_LOCK_COUNT){
    			  onMenuControlled = false;
    		  }
    		  return false;
    	  } else {
    		  exit();
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
	
    /* ---------------------------------------------------------------------- */
    public void scan(){
        // Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }
    
    public void discoverable(){
        // Ensure this device is discoverable by others
        ensureDiscoverable();
    }
    
    public void exit(){
    	float[] exit = {TAG_END, 0f, 0f, 0f, 0f, 0f};
    	sendMsg(exit);
    	finish();
    	System.exit(0);
	}
}