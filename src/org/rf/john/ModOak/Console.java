package org.rf.john.ModOak;

public class Console {
	public static void info(String in,String ClientName){
		System.out.println(new java.text.SimpleDateFormat("[HH:mm:ss]").format(new java.util.Date())+"["+ClientName+"][info]"+in);
	}
	public static void Warning(String in,String ClientName){
		System.out.println(new java.text.SimpleDateFormat("[HH:mm:ss]").format(new java.util.Date())+"["+ClientName+"][Waring]"+in);
	}
	public static void Error(String in,String ClientName){
		System.out.println(new java.text.SimpleDateFormat("[HH:mm:ss]").format(new java.util.Date())+"["+ClientName+"][Error]"+in);
	}
	public static void info(int in,String ClientName){info(in+"",ClientName);}
	public static void Warning(int in,String ClientName){Warning(in+"",ClientName);}
	public static void Error(int in,String ClientName){Error(in+"",ClientName);}
	
	public static void info(short in,String ClientName){info(in+"",ClientName);}
	public static void Warning(short in,String ClientName){Warning(in+"",ClientName);}
	public static void Error(short in,String ClientName){Error(in+"",ClientName);}
	
	public static void info(char in,String ClientName){info(in+"",ClientName);}
	public static void Warning(char in,String ClientName){Warning(in+"",ClientName);}
	public static void Error(char in,String ClientName){Error(in+"",ClientName);}
	
	public static void info(boolean in,String ClientName){info(in+"",ClientName);}
	public static void Warning(boolean in,String ClientName){Warning(in+"",ClientName);}
	public static void Error(boolean in,String ClientName){Error(in+"",ClientName);}
	
	public static void info(float in,String ClientName){info(in+"",ClientName);}
	public static void Warning(float in,String ClientName){Warning(in+"",ClientName);}
	public static void Error(float in,String ClientName){Error(in+"",ClientName);}
	
	public static void info(double in,String ClientName){info(in+"",ClientName);}
	public static void Warning(double in,String ClientName){Warning(in+"",ClientName);}
	public static void Error(double in,String ClientName){Error(in+"",ClientName);}
	
	public static void info(byte in,String ClientName){info(in+"",ClientName);}
	public static void Warning(byte in,String ClientName){Warning(in+"",ClientName);}
	public static void Error(byte in,String ClientName){Error(in+"",ClientName);}
}
