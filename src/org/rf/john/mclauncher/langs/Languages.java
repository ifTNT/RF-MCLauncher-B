package org.rf.john.mclauncher.langs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.json.JSONObject;
import org.rf.john.mclauncher.RFInfo;
import org.rf.john.mclauncher.RunType;


class OS{
	/**
	 * 取得作業系統
	 * @return 作業系統代碼(windows,linux,know)
	 */
	public static String getOS(){
		if(System.getProperty("os.name").toLowerCase().indexOf("win")>=0){
			return "windows";
		}else if(System.getProperty("os.name").toLowerCase().indexOf("nix")>=0 || System.getProperty("os.name").toLowerCase().indexOf("nux")>=0){
			return "linux";
		}else{
			return "unknow";
		}
	}
}

public class Languages{
	private JSONObject Lang; //語系
	public String LangFile; //語系檔名稱
	public Languages(){}
	public Languages(JSONObject json,String file){this.Lang=json;this.LangFile=file;}
	
	/**
	 * 取得語系物件
	 * @param SelectLang 語系檔名稱
	 * @return 語系物件
	 */
	@SuppressWarnings("static-access")
	public final Languages SelectLanguages(String SelectLang){
		BufferedReader LangReader=null;
		if(InstalledLang().containsValue(SelectLang)){
			System.out.println("--*Lang \""+SelectLang+"\" not found*--");
			switch(System.getProperty("user.country").toLowerCase()){
			case "tw":
				SelectLang="lang_tw.lang";
				break;
			case "cn":
				SelectLang="lang_cn_simple.lang";
				break;
			default:
				SelectLang="lang_en.lang";
				break;
				
			}
			System.out.println("--*Change Lang to: lang_tw.lang*--");
		}
		if(new RFInfo().RunMode.equals(RunType.JAR)){	
			ClassLoader cl=this.getClass().getClassLoader();
			InputStreamReader LangInputStream=null;
			try {
				LangInputStream = new InputStreamReader(cl.getResourceAsStream("org/rf/john/mclauncher/langs/"+SelectLang),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			LangReader=new BufferedReader(LangInputStream);
		}else{
			//------------取得工作資料夾-----------
			URL WorkDirPatht = this.getClass().getResource(""); //工作資料夾
			String WorkDirPath=WorkDirPatht.getFile();
			//File dir=null;
			String LangDir="";
			if(!OS.getOS().equals("windows")){
				LangDir=WorkDirPath;
			}else{
				LangDir=WorkDirPath.substring(1,WorkDirPath.length()).replace("/","\\");
			}
			try {
				LangDir=URLDecoder.decode(LangDir,"UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			//----------END 取得工作資料夾----------
			try {
				LangReader=new BufferedReader(new FileReader(LangDir+SelectLang));
			} catch (FileNotFoundException e) {
				System.out.println("--*Lang load Error!!*--");
				e.printStackTrace();
			}
		}
		String LangData="";
		String input="";
		do{
			try {
				input = LangReader.readLine();
				if(input==null){
					break;
				}
				LangData += input;
			} catch (IOException e) {
				System.out.println("--*Lang load Error!!*--");
				e.printStackTrace();
			}
		}while(true);
		//LangData=new String(LangData.replace("\n","").replace("\r","").getBytes("UTF-8"),"UTF-8");
		JSONObject LangJSON = new JSONObject(LangData);
		try {
			LangReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Languages(LangJSON,SelectLang);
	}
	
	/**
	 * 取得已安裝的語系
	 * @return 已安裝的語系(Key:LangFile ,Value:LangName)
	 */
	@SuppressWarnings({ "static-access", "resource" })
	public final HashMap<String,String> InstalledLang(){		
		HashMap<String, String> InstalledLang = new HashMap<>();
		if(new RFInfo().RunMode.equals(RunType.JAR)){
			String ThisJarPath=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			if(ThisJarPath.contains("!")){
				ThisJarPath=ThisJarPath.substring(0,ThisJarPath.indexOf("!"));
			}
			if(ThisJarPath.endsWith("/")){
				ThisJarPath=ThisJarPath.substring(0,ThisJarPath.length() - 1);
			}
			try {
				JarFile jarFile = new JarFile(URLDecoder.decode(ThisJarPath,"UTF-8"));
				Enumeration<JarEntry> JarFiles = jarFile.entries();
				while (JarFiles.hasMoreElements()) {
					JarEntry OneFile = (JarEntry)JarFiles.nextElement();
					if(OneFile.getName().matches(".+\\.lang")){ //取得所有.lang檔
						ClassLoader cl=this.getClass().getClassLoader();
						InputStreamReader LangInputStream=new InputStreamReader(cl.getResourceAsStream(OneFile.getName()),"UTF8");
						BufferedReader LangReader=new BufferedReader(LangInputStream);
						String LangData="";
						String input="";
						do{
							try {
								input = LangReader.readLine();
								if(input==null){
									break;
								}
								LangData += input;
							} catch (IOException e) {
								System.out.println("--*Lang load Error!!*--");
								e.printStackTrace();
							}
						}while(true);
						JSONObject LangJSON = new JSONObject(LangData);
						String LangFile=OneFile.getName().substring(OneFile.getName().lastIndexOf("/")+1,OneFile.getName().length());
						InstalledLang.put(LangFile,LangJSON.getString("LangName"));
					}
				}
			} catch (IOException e) {
				System.out.println("--*Lang load Error!!*--");
				e.printStackTrace();
			}
		}else{
			//------------取得工作資料夾-----------
			URL WorkDirPatht = this.getClass().getResource(""); //工作資料夾
			String WorkDirPath=WorkDirPatht.getFile();
			//File dir=null;
			String LangDir="";
			if(!OS.getOS().equals("windows")){
			LangDir=WorkDirPath;
			}else{
				LangDir=WorkDirPath.substring(1,WorkDirPath.length()).replace("/","\\");
			}
			try {
				LangDir=URLDecoder.decode(LangDir,"UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			//----------END 取得工作資料夾----------
			String[] InstalledLangs = new File(LangDir).list(new FilenameFilter() { //--┐
				public boolean accept(File directory, String fileName) { //				|
					return fileName.endsWith(".lang");  //								|
				} //																	|
			}); //取的lang資料夾下的所有.lang檔---------------------------------------------┘
			for(String OneLangFile:InstalledLangs){
				BufferedReader LangReader;
				try {
					LangReader = new BufferedReader(new FileReader(LangDir+OneLangFile));
					String LangData="";
					String input="";
					do{
						try {
							input = LangReader.readLine();
							if(input==null){
								break;
							}
							LangData += input;
						} catch (IOException e) {
							System.out.println("--*Lang load Error!!*--");
							e.printStackTrace();
						}
					}while(true);
					JSONObject LangJSON = new JSONObject(LangData); 
					InstalledLang.put(OneLangFile,LangJSON.getString("LangName"));
				} catch (FileNotFoundException e1) {
					System.out.println("--*Lang load Error!!*--");
					e1.printStackTrace();
				}
			}
		}
		return InstalledLang; //Key:LangFile ,Value:LangName
	}
	
	/**
	 * 印出已安裝的語系
	 * @param LangList 已安裝的語系列表
	 */
	public static void PrintInstalledLang(HashMap<String,String> LangList){
		System.out.println("-----------------Installed Languages-------------------");
		for(Entry<String,String> OneLang:LangList.entrySet()){
			System.out.println(OneLang.getKey()+": "+OneLang.getValue());
		}
		System.out.println("---------------------------------------------------------");
	}
	
	/**
	 * 取得語系內容
	 * @param Path 標籤
	 * @return 語系內容
	 */
	public final String getString(String Path){
		return this.Lang.getString(Path);
	}
}
/*class lang_no_en extends Languages {{
	/*-------------------------------------
	|       Language-鶯葛禮緒               |
	-------------------------------------
	LangName= "鶯葛禮緒";
	Title= "賣快夫特啦驅而";
	NoNimbus= "力思思賣ㄊ依思那特思播特Nimbus路克ㄟ恩的夫歐";
	AboutText1= "ㄜ爆特賣快夫特啦驅而:";
	AboutText2= "賣快夫特啦驅而依思ㄜㄤ喔付思ㄟㄜ賣快夫特啦驅而";
	AboutText3= "抓挖播思恩鎂的掰曙";
	MainTabTitle= "賣快夫特啦驅而";
	SittingTabTitle= "思町個思";
	LangText= "纜隔橘:";
	UserText= "由思而:";
	PwdText= "趴思渦的:";
	LoginBtm= "肉革殷";
	VersionText= "播思恩";
	RememberMe= "肉一賣播密!";
	MinecraftDirText= "Minecraft資料夾:";
	JVMPathText= "Java路徑:";
	
}}*/