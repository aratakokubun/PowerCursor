package org.example.PowerCursor;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

interface Experiment{
	public static final boolean DO				 = true;
	public static final int 	MAX_RESIST_COUNT = 5*5;
	public static final String 	NAME = "UNKNOWN";//TODO ”íŒ±ŽÒ–¼
}

interface BlueTooth {
    // Debugging
    public static final String TAG = "Pseudo using BlueTooth";
    public static final boolean D  = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ 		 = 2;
    public static final int MESSAGE_WRITE 		 = 3;
    public static final int MESSAGE_DEVICE_NAME  = 4;
    public static final int MESSAGE_TOAST 		 = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST 	   = "toast";

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE = 1;
    public static final int REQUEST_ENABLE_BT 	   = 2;
    
    //send message array size
    public static final int MESSAGE_ARRAY_SIZE = 6;

    //decode message tag
    public static final int TAG_POS  	=  0;
    public static final int TAG_INIT 	=  1;
    public static final int TAG_NEXT 	=  2;
    public static final int TAG_BACK 	=  3;
    public static final int TAG_END		=  4;
    public static final int TAG_TYPE 	=  5;
    public static final int TAG_PHASE  	=  6;
    public static final int TAG_RESIST	=  7;
    public static final int TAG_RESET	=  8;
    public static final int TAG_START	=  9;
    public static final int TAG_ZONE 	= 10;
    public static final int TAG_MIN_FPS	= 11;
}

class BTActivity extends Activity implements BlueTooth, Experiment {
    // Name of the connected device
    static String mConnectedDeviceName = null;
    // String buffer for outgoing messages
    static StringBuffer mOutStringBuffer = null;
    // Local Bluetooth adapter
    static BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    static BTService mChatService = null;
    // Write data to csv file
    static FileReadWrite mFileReadWrite = null;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");
		
		// fix display as landscape
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
		// use full display
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		// disable title bar
		//requestWindowFeature(Window.FEATURE_NO_TITLE); 
		// disable display sleep
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Declare using local folder
        mFileReadWrite = new FileReadWrite(NAME);

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
        	showToast("Bluetooth is not available");
            finish();
            return;
        }
	}
	
    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupConnection();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BTService.STATE_NONE) {
              // Start the Bluetooth chat services
              mChatService.start();
            }
        }
    }
    
    private void setupConnection(){
    	Log.d(TAG, "setupConnection()");
    	
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BTService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

    public void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
    
    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	switch (msg.what) {
        	case MESSAGE_STATE_CHANGE:
        		if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
        		switch (msg.arg1) {
        		case BTService.STATE_CONNECTED:
        			break;
        		case BTService.STATE_CONNECTING:
        			break;
        		case BTService.STATE_LISTEN:
        			break;
        		case BTService.STATE_NONE:
        			break;
        		}
        		break;
        	case MESSAGE_WRITE:
        		break;
        	case MESSAGE_READ:
        		byte[] readBuf = (byte[]) msg.obj;
        		decodeMsg(readBuf);
        		break;
        	case MESSAGE_DEVICE_NAME:
        		// save the connected device's name
        		mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
        		showToast(getApplicationContext(), "Connected to " + mConnectedDeviceName);
        		break;
        	case MESSAGE_TOAST:
        		showToast(getApplicationContext(), msg.getData().getString(TOAST));
        		break;
        	}
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // Get the BLuetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                // Attempt to connect to the device
                mChatService.connect(device);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                setupConnection();
            } else {
                // User did not enable Bluetooth or an error occured
                Log.d(TAG, "BT not enabled");
                showToast(R.string.bt_not_enabled_leaving);
                finish();
            }
        }
    }

	//--------------------------------------------------------------------
    /**
     * define method detail at override
     */
    public void decodeMsg(byte[] buf){}
    public void sendMsg(float[] buf) {}

	//--------------------------------------------------------------------
	/**
	 * static methods
	 * you can use these methods out side from this class.
	 */
	public static void printInteger(byte[] data,int offset,int value) throws IllegalArgumentException{
		int size=Integer.SIZE/Byte.SIZE;
		if(data==null||data.length<size||offset<0||data.length-size<offset)

			throw new IllegalArgumentException("Bat Param");
		else{
			for(int i=0;i<size;i++)

				data[offset+i]=Integer.valueOf(value>>(Byte.SIZE*(size-1-i))).byteValue();
		}
	}
	
	public static int getInteger(byte[] data,int offset) throws IllegalArgumentException{
		int result=0;
		int size=Integer.SIZE/Byte.SIZE;
		if(data==null||data.length<size||offset<0||data.length-size<offset)

			throw new IllegalArgumentException("Bat Param");
		else{
			for(int i=0;i<size;i++)

				result|=Integer.valueOf(data[offset+i]&0xff).intValue()<<(Byte.SIZE*(size-1-i));
		}
		return result;
	}
	
	public static void printFloat(byte[] data,int offset,float value) throws IllegalArgumentException{
		printInteger(data, offset, Float.floatToIntBits(value));
	}
	
	public static float getFloat(byte[] data,int offset) throws IllegalArgumentException{
		return Float.intBitsToFloat(getInteger(data, offset));
	}

	//--------------------------------------------------------------------
	/**
	 * show Toast
	 */
	protected void showToast(String message){
        Toast.makeText(
        		this,
        		message,
        		Toast.LENGTH_SHORT
        ).show();
	}
	protected void showToastLong(String message){
        Toast.makeText(
        		this,
        		message,
        		Toast.LENGTH_LONG
        ).show();
	}
	protected void showToast(int resId){
		Toast.makeText(
				this,
				resId,
				Toast.LENGTH_SHORT
		).show();
	}
	protected void showToast(Context context, String message){
		Toast.makeText(
				context,
				message,
				Toast.LENGTH_SHORT
		).show();
	}
}