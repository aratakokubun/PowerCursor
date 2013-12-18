package org.example.PowerCursor;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class PseudoLaunch extends Activity implements OnClickListener{
	
	static final String TAG 	= "DemoKitLaunch";
	static final String[] LIST 	= {"male", "female"};
	
	static FileReadWrite mFileReadWrite; 
	
	Button run_sender 	= null;
	Button run_receiver = null;
	EditText your_name	= null;
	EditText your_age	= null;
	AutoCompleteTextView your_sex	= null;
	
	String name;
	String sex;
	String age;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		initView();
	}
	
	private void initView(){
		run_sender 	 = (Button)findViewById(R.id.RunSender);
		run_receiver = (Button)findViewById(R.id.RunReceiver);
		your_name	 = (EditText)findViewById(R.id.nameText);
		your_age	 = (EditText)findViewById(R.id.ageText);
		your_sex	 = (AutoCompleteTextView)findViewById(R.id.sexText);
		
		run_sender.setOnClickListener(this);
		run_receiver.setOnClickListener(this);
		
		//アダプターの生成
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, LIST);
	     
	    //AutoCompleteTextViewにアダプターをセット
	    your_sex.setAdapter(adapter);
	}
	
	//--------------------------------------------------------------------
	/**
	 * add menu items
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Launch as Sender");
		menu.add("Launch as Receiver");
		menu.add("Quit");
		
		return true;
	}

	//--------------------------------------------------------------------
	/**
	 * on menu item selected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle() == "Launch as Sender"){
			OnSenderPressed();
		} else if (item.getTitle() == "Launch as Receiver"){
			OnReceiverPressed();
		} else if (item.getTitle() == "Quit") {
			finish();
			System.exit(0);
		}
		
		return true;
	}
	
	//--------------------------------------------------------------------
	/**
	 * start intent activity
	 */
	private void startIntent(Intent intent){
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Log.e(TAG, "unable to start DemoKit activity", e);
		}
		
		finish();
	}

	//--------------------------------------------------------------------
	/**
	 * method on  button clicked
	 */
	@Override
	public void onClick(View v) {
		int vId = v.getId();
		
		switch (vId) {
		case R.id.RunSender:
			OnSenderPressed();
			break;
		case R.id.RunReceiver:
			OnReceiverPressed();
			break;
		}
		
	}
	
	//--------------------------------------------------------------------
	private void OnSenderPressed(){
		Log.i(TAG, "starting sender");
		Intent intent = new Intent(this, Sender.class);
		startIntent(intent);
	}
	
	private void OnReceiverPressed(){
		Log.i(TAG, "starting receiver");
		//Intent intent = new Intent(this, Receiver.class);
		Intent intent = new Intent(this, ShowPowerCursorActivity.class);
		startIntent(intent);
	}
}
