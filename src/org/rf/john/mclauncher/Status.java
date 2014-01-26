package org.rf.john.mclauncher;

import java.util.HashSet;

import org.json.JSONObject;
import org.rf.john.mclauncher.langs.Languages;
import org.rf.john.mclauncher.Themes.Theme;


public class Status{
	public static String Version="v2.0a"; //RF-MCLauncher-B的版本
	public static RunType RunMode=RunType.JAR; //運作模式(Debug/JAR)
	
	public static HashSet<String> Arguments=new HashSet<>(); //啟動參數
	public static boolean TestArguments(String arg){ //檢查是否有該參數
		if(Arguments.isEmpty()) return false;
		return (Arguments.contains(arg));
	}
	public static MainFrame2 MainFrameObj; //主視窗物件
	public static Theme Theme; //已選的佈景主題
	public static Languages SelectedLang; //已選的語系
	public static Launcher Launcher;  //啟動器物件
	public static JSONObject RFMCLB_InfoJSON;
	public static boolean Busy;
	
	public static String getOS(){
		if(System.getProperty("os.name").toLowerCase().indexOf("win")>=0){
			return "windows";
		}else if(System.getProperty("os.name").toLowerCase().indexOf("nix")>=0 || System.getProperty("os.name").toLowerCase().indexOf("nux")>=0){
			return "linux";
		}else if(System.getProperty("os.name").toLowerCase().indexOf("mac")>=0||System.getProperty("os.name").toLowerCase().replace(" ","").indexOf("osx")>=0){
			return "osx";
		}else{
			return "unknow";
		}
	}
}
