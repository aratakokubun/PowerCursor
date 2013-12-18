package org.example.PowerCursor;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import android.os.Environment;
import android.util.Log;

public class FileReadWrite {
	public static final String DEFAULT_LOCATION = "/mnt/sdcard/Experiment/PowerCursor/";
	
	public static boolean bk;
	
	public static String fName			= "";
	public static String bkFileName		= "";
	public static String touchFileName	= "";
	public static String ENCODE			= "UTF-8";
	private final Calendar calendar 	= Calendar.getInstance();
	public static final int START		= 0;
	public static final int HEADER		= 1;
	public static final int DATA		= 2;
	public static final int RETURN		= 3;
	public static final int END			= 4;
	public static final int ARRAYDATA	= 5;
	
	public FileReadWrite(String name){
		//ファイル名(年月日時分、被験者名)
		String date = "" 
					+ calendar.get(Calendar.YEAR)
					+ (int)(calendar.get(Calendar.MONTH)+1)
					+ calendar.get(Calendar.DAY_OF_MONTH)
					+ calendar.get(Calendar.HOUR_OF_DAY)
					+ calendar.get(Calendar.MINUTE);
		
		fName	   	 = DEFAULT_LOCATION + date + "_" + name + ".csv";
		bkFileName 	 = DEFAULT_LOCATION + "BackUp" + date + "_" + name + ".csv";
		touchFileName = DEFAULT_LOCATION + "TouchLog" + date + "_" + name + ".csv";
	}
	
	public static void defineLocation(String name){
		Calendar c 	= Calendar.getInstance();
		//ファイル名(年月日時分、被験者名)
		String date = "" 
					+ c.get(Calendar.YEAR)
					+ (int)(c.get(Calendar.MONTH)+1)
					+ c.get(Calendar.DAY_OF_MONTH)
					+ c.get(Calendar.HOUR_OF_DAY)
					+ c.get(Calendar.MINUTE);
	
		fName	   	 = DEFAULT_LOCATION + date + "_" + name + ".csv";
		bkFileName 	 = DEFAULT_LOCATION + "BackUp" + date + "_" + name + ".csv";
		touchFileName = DEFAULT_LOCATION + "TouchLog" + date + "_" + name + ".csv";
	}
	
	public static void WriteFile(String sData, int sMode){
		String writeData;
		
		switch(sMode){
		case START:		writeData = "Log Start ," + "\n Name : ," + sData + ",";
						break;
		case HEADER:	writeData = "\n,\n Model : ," + sData + ",";
						break;
		case DATA:		writeData = sData + ",";
						break;
		case RETURN:	writeData = "\n" + sData + ",";
						break;
		case END:		writeData = "\n,\n Log End ,";
						break;
		case ARRAYDATA:	writeData = "\n Mode Array," + sData + ",\n";
						break;
		default:		writeData = ",";
						break;
		}
		
		BufferedWriter bufferedWriterObj = null;
		try {
			//ファイル出力ストリームの作成
			bufferedWriterObj = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fName, true), ENCODE));
	
			bufferedWriterObj.write(writeData);
			bufferedWriterObj.flush();

		} catch (Exception e) {
			Log.d("CommonFile.writeFile", e.getMessage());
		} finally {
			try {
				if( bufferedWriterObj != null) bufferedWriterObj.close();
			} catch (IOException e2) {
				Log.d("CommonFile.writeFile", e2.getMessage());
			}
		}
	}
	
	public static void WriteData(String sData){
		BufferedWriter bufferedWriterObj = null;
		try {
			//ファイル出力ストリームの作成
			bufferedWriterObj = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fName, true), ENCODE));
	
			bufferedWriterObj.write(sData);
			bufferedWriterObj.flush();

		} catch (Exception e) {
			Log.d("CommonFile.writeFile", e.getMessage());
		} finally {
			try {
				if( bufferedWriterObj != null) bufferedWriterObj.close();
			} catch (IOException e2) {
				Log.d("CommonFile.writeFile", e2.getMessage());
			}
		}
	}
	
	public static void WriteBackUp(String sData){
		BufferedWriter bufferedWriterObj = null;
		try {
			//ファイル出力ストリームの作成
			bufferedWriterObj = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(bkFileName, true), ENCODE));
	
			bufferedWriterObj.write(sData);
			bufferedWriterObj.flush();

		} catch (Exception e) {
			Log.d("CommonFile.writeBackUpFile", e.getMessage());
		} finally {
			try {
				if( bufferedWriterObj != null) bufferedWriterObj.close();
			} catch (IOException e2) {
				Log.d("CommonFile.writeBackUpFile", e2.getMessage());
			}
		}
	}
	
	public static void WriteTouchLog(String sData){
		BufferedWriter bufferedWriterObj = null;
		try {
			//ファイル出力ストリームの作成
			bufferedWriterObj = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(touchFileName, true), ENCODE));
			bufferedWriterObj.write(sData);
			bufferedWriterObj.flush();

		} catch (Exception e) {
			Log.d("CommonFile.writeBackUpFile", e.getMessage());
		} finally {
			try {
				if( bufferedWriterObj != null) bufferedWriterObj.close();
			} catch (IOException e2) {
				Log.d("CommonFile.writeTime", e2.getMessage());
			}
		}
	}
	
}