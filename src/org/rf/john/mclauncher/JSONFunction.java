package org.rf.john.mclauncher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;


public class JSONFunction{
	/**
	 * 從檔案建立JSONObject
	 * @param File 原始檔案
	 * @return 該檔案所建立的JSONObject
	 * @throws IOException
	 */
	public static JSONObject CreateFromFile(String File) throws IOException{
		return CreateFromBufferedReader(new BufferedReader(new FileReader(URLDecoder.decode(File,"UTF8"))));
	}
	public static JSONObject CreateFromStream(InputStream stream) throws IOException{
		return CreateFromBufferedReader(new BufferedReader(new InputStreamReader(stream,"UTF-8")));
	}
	private static JSONObject CreateFromBufferedReader(BufferedReader bufReader) throws IOException{
		String input;
		String jsonTxt="";
		do{
			input = bufReader.readLine();
			if(input==null){
				break;
			}
			jsonTxt += input;
		}while(true);
		bufReader.close();
		return (!jsonTxt.equals(""))?new JSONObject(jsonTxt):null;
	}
	
	/**
	 * 將JSON寫入檔案,並格式化
	 * @param in JSON
	 * @param TargetFile 目標檔案
	 * @throws IOException
	 */
	public static void WriteToFile(JSONObject in,String TargetFile) throws IOException{
		BufferedWriter WriteStream = new BufferedWriter(new FileWriter(TargetFile));
		WriteStream.write(Format(in));
		WriteStream.flush();
		WriteStream.close();
	}
	
	/**
	 * 將JSON寫入檔案,並格式化
	 * @param in JSON
	 * @param TargetFile 目標檔案
	 * @throws IOException
	 */
	public static void WriteToFile(String in,String TargetFile) throws IOException{
		BufferedWriter WriteStream = new BufferedWriter(new FileWriter(TargetFile));
		WriteStream.write(Format(in));
		WriteStream.flush();
		WriteStream.close();
	}
	
	/**
	 * 搜尋該標籤是否在該JSON內(防止產生錯誤)
	 * @param input JSON
	 * @param key 欲搜索的標籤
	 * @return 該標籤是否存在
	 */
	@SuppressWarnings("static-access")
	public static boolean Search(JSONObject input,String key){
		//String[] SortedArray = new JSONObject().getNames(input);
		//Arrays.sort(SortedArray);
		//return ((Arrays.binarySearch(SortedArray,key) > -1)?true:false);
		return new HashSet<String>(Arrays.asList(new JSONObject().getNames(input))).contains(key);
	}
	
	/*
	 * 取得JSONArray的第key項
	 * @param in
	 * @param key
	 * @return
	 */
	/*public static JSONObject getItem(JSONArray in,int key){
		if(key>=in.length()||key<0)
			return null;
		return in.getJSONObject(key);
	}*/
	
	
	/*@SuppressWarnings({ "static-access", "unused"})
	public static JSONObject getItem(JSONArray in,String key){
		for(int i=0;i<in.length();i++){
			for(String OneKey:new JSONObject().getNames(in.getJSONObject(i))){
				if(in.getJSONObject(i).getString("name").equals(key)){
					return in.getJSONObject(i);
				}
			}	
		}
		return null;
	}*/
	
	/**
	 * 將JSONArray轉成一般陣列
	 * @param in JSONArray輸入
	 * @return 字串陣列
	 */
	public static String[] readArray(JSONArray in){
		ArrayList<String> out = new ArrayList<String>();
		for(int i=0;i<in.length();i++){
			out.add(in.getString(i));
		}
		String[] StringOut = new String[out.size()];
		for(int i=0;i<out.size();i++){
			StringOut[i]=out.get(i);
		}
		return StringOut;
	}
	
	/**
	 * 將JSON格式化
	 * @param JSONin JSON
	 * @return 已格式化的字串
	 */
	public static String Format(String JSONin){
		String Tab="  ";
		String nl=System.getProperty("line.separator");
		int TABs=0;
		String OutTemp=JSONin;
		String Out="";
		OutTemp=OutTemp.replace("{",nl+"{"+nl); //處理換行
		OutTemp=OutTemp.replace("}",nl+"}");
		OutTemp=OutTemp.replace("[",nl+"["+nl);
		OutTemp=OutTemp.replace("]",nl+"]");
		OutTemp=OutTemp.replace(",",","+nl);
		String[] OutTempArray=OutTemp.split(nl); //消除開頭空行
		for(int i=1;i<=OutTempArray.length-1;i++){ //處理縮排
			String OneLine=OutTempArray[i];
			if(OneLine.equals("]")) TABs-=1;
			if(OneLine.equals("}")) TABs-=1;
			if(OneLine.equals("],")) TABs-=1;
			if(OneLine.equals("},")) TABs-=1;
			for(int j=0;j<=TABs-1;j++){Out+=Tab;}
			if(OneLine.equals("{")) TABs+=1;
			if(OneLine.equals("[")) TABs+=1;
			
			Out+=OneLine;
			Out+=nl;
		}
		//System.out.println(Out);
		return Out;
	}
	public static String Format(JSONObject JSONin){
		return Format(JSONin.toString());
	}
}
