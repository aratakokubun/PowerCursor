package org.example.PowerCursor;

public class TouchLog {
	// excel で読み込める列数は最大で16,384なので，列限界は16000にしておく
	private static final int 	 LOG_PER_ROW 		= 16000;
	private static final String  LOG_DEFAULT_STRING = "\n";
	
	private static final String INFO_HEAD = "INFO :" + ",";
	private static final String INFO_END = ": INFO" + ",";
	private static final String HEADER = "\n" + "\n" + "HEADER" + ",";
	
	private static boolean isLogStarted = false;
	private static int 	logCount = 0;
	private static String 	logString = new String();
	
	/* -------------------------------------------------------------------- */
	public static void init(){
		isLogStarted = false;
		logCount = 0;
		logString = new String();
	}
	
	/* -------------------------------------------------------------------- */
	public static boolean getIsLogStarted(){
		return isLogStarted;
	}

	public static int getLogCount(){
		return logCount;
	}
	
	public static String getLogString(){
		return logString;
	}
	
	/* -------------------------------------------------------------------- */
	public static void start(String name){
		if(!isLogStarted){
			long startTime = System.currentTimeMillis();
			String top = "log start"+"," + name+"," + "@"+startTime+"," + "\n";
			FileReadWrite.WriteTouchLog(top);
			isLogStarted = true;
		}
	}
	
	public static void header(String str){
		if(isLogStarted){
			newLine();
			long logTime = System.currentTimeMillis();
			String temp = HEADER + str+"," + "@"+logTime+",";
			FileReadWrite.WriteTouchLog(temp);
		}
	}
	
	public static void info(String str, boolean isHead){
		if(isLogStarted){
			newLine();
			long logTime = System.currentTimeMillis();
			//String temp = isHead ? INFO_HEAD + str+"," + "@"+logTime+"," : str+"," + "@"+logTime+"," + INFO_END;
			String temp = "\n" + str+logTime + ",";
			FileReadWrite.WriteTouchLog(temp);
		}
	}
	
	public static void add(String str, int section){
		// section はメッセージ中の節数
		if(isLogStarted){
			logCount += section;
			logString += str+",";
			if(logCount >= LOG_PER_ROW){
				newLine();
			}
		}
	}
	
	public static void forceWriteToFile(){
		if(isLogStarted){
			newLine();
		}
	}
	
	/* -------------------------------------------------------------------- */
	private static void newLine(){
		FileReadWrite.WriteTouchLog(logString);
		logCount  = 0;
		logString = LOG_DEFAULT_STRING;
	}
	
	@Deprecated
	public static void reset(){
		if(isLogStarted){
			newLine();
			String temp = "\n,\n,\n, ALL RESET., ";
			FileReadWrite.WriteTouchLog(temp);
		}
	}	
}