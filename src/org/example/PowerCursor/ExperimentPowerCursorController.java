package org.example.PowerCursor;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExperimentPowerCursorController {
	/* ----------------------------------------------------------- */
	public static class ItemObject
	{
		private int id;
		private String name;
		public ItemObject(int id, String name){
			this.id = id;
			this.name = name;
		}
		public int getId(){
			return id;
		}
		public String getName(){
			return name;
		}
	}
	
	/* ----------------------------------------------------------- */
	private static final ItemObject[] ITEM = {
//		new ItemObject(POWERCURSOR.HILL, POWERCURSOR.PSEUDO_NAME[POWERCURSOR.HILL]),
		new ItemObject(POWERCURSOR.HOLE, POWERCURSOR.PSEUDO_NAME[POWERCURSOR.HOLE]),
		new ItemObject(POWERCURSOR.PUSHGUTTER, POWERCURSOR.PSEUDO_NAME[POWERCURSOR.PUSHGUTTER]),
		new ItemObject(POWERCURSOR.SAND, POWERCURSOR.PSEUDO_NAME[POWERCURSOR.SAND]),
	};
	
	private static ItemObject item;
	private static float param;
	
	// 実験中の切り替え用
	private static int paramIndex;
	private static float firstParam;
	private static float secondParam;
	
	private static Activity parent;
	private static Dialog dialog;
	
	/* ----------------------------------------------------------- */
	public static void setParent(Activity activity)
	{
		parent = activity;
	}
	
	/* ----------------------------------------------------------- */
	public static void setDefaultParameters()
	{
		item = new ItemObject(POWERCURSOR.HILL, POWERCURSOR.PSEUDO_NAME[POWERCURSOR.HILL]);
		param = POWERCURSOR.PSEUDO_PARAMS[0];
		paramIndex = 0;
		firstParam = POWERCURSOR.PSEUDO_PARAMS[0];
		secondParam = POWERCURSOR.PSEUDO_PARAMS[0];
	}
	
	/* ----------------------------------------------------------- */
	public static ItemObject getItem()
	{
		return item;
	}
	
	public static float getParam()
	{
		switch(paramIndex){
		case 0:
			return firstParam;
		case 1:
			return secondParam;
		default:
			return param;
		}
	}
	
	public static float getParam(int index)
	{
		switch(index){
		case 0:
			return firstParam;
		case 1:
			return secondParam;
		default:
			return param;
		}
	}
	
	public static int getParamIndex()
	{
		return paramIndex;
	}
	
	public static int getNextParamIndex()
	{
		int nextIndex = (paramIndex+1)%2;
		paramIndex = nextIndex;
		return nextIndex;
	}

	/* ----------------------------------------------------------- */
	public static void setPowerCursorParameters(ItemObject pc_item, float pc_param)
	{
		item = pc_item;
		param = pc_param;
	}
	
	public static void setItem(ItemObject pc_item)
	{
		item = pc_item;
	}
	
	public static void setParam(float pc_param){
		param = pc_param;
	}
	
	public static void setParamIndex(int index){
		paramIndex = index;
	}

	public static void setParam(float pc_param, int index) {
		switch (index) {
		case 0:
			firstParam = pc_param;
			return;
		case 1:
			secondParam = pc_param;
			return;
		default:
			param = pc_param;
			return;
		}
	}

	/* ----------------------------------------------------------- */
	// dialog for setting power cursor types and parameters
	public static void setExperimentSwitchPowerCursorDialog() {
		showDialog("set power cursor params.", parent.getLayoutInflater().inflate(R.layout.experiment_powercursor_paramset, null));

		final TextView typeName = (TextView)dialog.findViewById(R.id.type_name);
		typeName.setText(String.valueOf(item.getName()));
		
		final TextView firstParamText = (TextView) dialog.findViewById(R.id.first_param_value);
		firstParamText.setText(String.valueOf(getParam(0)));

		final TextView secondParamText = (TextView) dialog.findViewById(R.id.second_param_value);
		secondParamText.setText(String.valueOf(getParam(1)));

		final Button ok = (Button) dialog.findViewById(R.id.button_ok);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// if parameters are same, re-do setting
				if(firstParam == secondParam){
					Toast.makeText(parent, "Invalid parameters. Parameters are same.", Toast.LENGTH_SHORT).show();
				} else {
					paramIndex = 0;
					PowerCursorObject.setPseudoParam(firstParam);
					PowerCursorObject.setPseudoType(item.getId());
					dialog.dismiss();
				}
			}
		});

		ArrayAdapter<ItemObject> itemAdapter = new ArrayAdapter<ItemObject>(parent, android.R.layout.simple_spinner_item);
		itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// アイテムを追加します
		for (int i = 0; i < ITEM.length; i++) {
			itemAdapter.add(ITEM[i]);
		}
		final Spinner itemSpinner = (Spinner) dialog.findViewById(R.id.item_spinner);
		// アダプターを設定します
		itemSpinner.setAdapter(itemAdapter);
		// スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
		itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Spinner spinner = (Spinner) parent;
				// 選択されたアイテムを取得します
				ItemObject item = (ItemObject) spinner.getSelectedItem();
				setItem(item);
				typeName.setText(item.getName());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		ArrayAdapter<String> firstAdapter = new ArrayAdapter<String>(parent, android.R.layout.simple_spinner_item);
		firstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// アイテムを追加します
		for (int i = 0; i < POWERCURSOR.PSEUDO_PARAMS.length; i++) {
			firstAdapter.add(String.valueOf(POWERCURSOR.PSEUDO_PARAMS[i]));
		}
		final Spinner firstSpinner = (Spinner) dialog.findViewById(R.id.first_param_spinner);
		// アダプターを設定します
		firstSpinner.setAdapter(firstAdapter);
		// スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
		firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Spinner spinner = (Spinner) parent;
				// 選択されたアイテムを取得します
				String item = (String) spinner.getSelectedItem();
				firstParam = Float.valueOf(item);
				firstParamText.setText(item);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		ArrayAdapter<String> secondAdapter = new ArrayAdapter<String>(parent, android.R.layout.simple_spinner_item);
		secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// アイテムを追加します
		for (int i = 0; i < POWERCURSOR.PSEUDO_PARAMS.length; i++) {
			secondAdapter.add(String.valueOf(POWERCURSOR.PSEUDO_PARAMS[i]));
		}
		final Spinner secondSpinner = (Spinner) dialog.findViewById(R.id.second_param_spinner);
		// アダプターを設定します
		secondSpinner.setAdapter(secondAdapter);
		// スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
		secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Spinner spinner = (Spinner) parent;
				// 選択されたアイテムを取得します
				String item = (String) spinner.getSelectedItem();
				secondParam = Float.valueOf(item);
				secondParamText.setText(item);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	public static void setPseudoTypeDialog(){
		showDialog("set power cursor params.", parent.getLayoutInflater().inflate(R.layout.experiment_powercursor_paramset, null));

		final TextView typeName = (TextView)dialog.findViewById(R.id.type_name);
		typeName.setText(String.valueOf(item.getName()));

		final Button ok = (Button) dialog.findViewById(R.id.button_ok);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PowerCursorObject.setPseudoType(item.getId());
				MakeRandArray.setType(item.getId());
				MakeRandArray.writeArrayToBackupFile();
				MakeRandArray.makeLogHeader();
				PowerCursorObject.setPseudoParam(MakeRandArray.getParam());
				dialog.dismiss();
			}
		});

		ArrayAdapter<ItemObject> itemAdapter = new ArrayAdapter<ItemObject>(parent, android.R.layout.simple_spinner_item);
		itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// アイテムを追加します
		for (int i = 0; i < ITEM.length; i++) {
			itemAdapter.add(ITEM[i]);
		}
		final Spinner itemSpinner = (Spinner) dialog.findViewById(R.id.item_spinner);
		// アダプターを設定します
		itemSpinner.setAdapter(itemAdapter);
		// スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
		itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Spinner spinner = (Spinner) parent;
				// 選択されたアイテムを取得します
				ItemObject item = (ItemObject) spinner.getSelectedItem();
				setItem(item);
				typeName.setText(item.getName());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	/* ----------------------------------------------------------- */
	//dialogの表示
	private static void showDialog(String title, View content) {
		dialog = new Dialog(parent);
		dialog.setTitle(title);
		dialog.setContentView(content);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.show();
	}
}