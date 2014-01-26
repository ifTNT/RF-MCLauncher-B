package org.rf.john.mclauncher.langs;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.json.JSONObject;
import org.rf.john.mclauncher.JSONFunction;
import org.rf.john.mclauncher.Status;
import org.rf.john.mclauncher.RunModeUtil;

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
	public final Languages SelectLanguages(String SelectLang){
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
			System.out.println("--*Change Lang to: "+SelectLang+"*--");
		}
		
		JSONObject LangJSON=new JSONObject();
		try {
			if(Status.RunMode.equals(RunModeUtil.JAR)){	
				ClassLoader cl=this.getClass().getClassLoader();
				LangJSON=JSONFunction.CreateFromStream(cl.getResourceAsStream("org/rf/john/mclauncher/langs/"+SelectLang));
			}else{
				//------------取得工作資料夾-----------
				URL WorkDirPatht = this.getClass().getResource(""); //工作資料夾
				String WorkDirPath=WorkDirPatht.getFile();
				String LangDir="";
				if(!Status.getOS().equals("windows")){
					LangDir=WorkDirPath;
				}else{
					LangDir=WorkDirPath.substring(1,WorkDirPath.length()).replace("/","\\");
				}
				LangDir=URLDecoder.decode(LangDir,"UTF-8");
				//----------END 取得工作資料夾----------
				LangJSON=JSONFunction.CreateFromFile(LangDir+SelectLang);
			}
		} catch (IOException e) {
			System.out.println("--*Lang load Error!!*--");
			e.printStackTrace();
		}
		return new Languages(LangJSON,SelectLang);
	}
	
	/**
	 * 取得已安裝的語系
	 * @return 已安裝的語系(Key:LangFile ,Value:LangName)
	 */
	public final HashMap<String,String> InstalledLang(){
		HashMap<String, String> InstalledLang = new HashMap<>();
		try {
			if(Status.RunMode.equals(RunModeUtil.JAR)){
				String ThisJarPath=this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
				if(ThisJarPath.contains("!")){
					ThisJarPath=ThisJarPath.substring(0,ThisJarPath.indexOf("!"));
				}
				if(ThisJarPath.endsWith("/")){
					ThisJarPath=ThisJarPath.substring(0,ThisJarPath.length() - 1);
				}
				
				JarFile jarFile = new JarFile(URLDecoder.decode(ThisJarPath,"UTF-8"));
				Enumeration<JarEntry> JarFiles = jarFile.entries();
				while (JarFiles.hasMoreElements()) {
					JarEntry OneFile = (JarEntry)JarFiles.nextElement();
					if(OneFile.getName().matches(".+\\.lang")){ //取得所有.lang檔
						ClassLoader cl=this.getClass().getClassLoader();
						JSONObject LangJSON = JSONFunction.CreateFromStream(cl.getResourceAsStream(OneFile.getName()));
						String LangFile=OneFile.getName().substring(OneFile.getName().lastIndexOf("/")+1,OneFile.getName().length());
						InstalledLang.put(LangFile,LangJSON.getString("LangName"));
					}
				}
				jarFile.close();
				
			}else{
				//------------取得工作資料夾-----------
				URL WorkDirPatht = this.getClass().getResource(""); //工作資料夾
				String WorkDirPath=WorkDirPatht.getFile();
				//File dir=null;
				String LangDir="";
				if(!Status.getOS().equals("windows")){
					LangDir=WorkDirPath;
				}else{
					LangDir=WorkDirPath.substring(1,WorkDirPath.length()).replace("/","\\");
				}
				LangDir=URLDecoder.decode(LangDir,"UTF-8");
				//----------END 取得工作資料夾----------
				String[] InstalledLangs = new File(LangDir).list(new FilenameFilter() { //--┐
					public boolean accept(File directory, String fileName) { //				|
						return fileName.endsWith(".lang");  //								|
					} //																	|
				}); //取的lang資料夾下的所有.lang檔---------------------------------------------┘
				for(String OneLangFile:InstalledLangs){
					JSONObject LangJSON = JSONFunction.CreateFromFile(LangDir+OneLangFile); 
					InstalledLang.put(OneLangFile,LangJSON.getString("LangName"));
				}
			}
		} catch (IOException e) {
			System.out.println("--*Lang load Error!!*--");
			e.printStackTrace();
			System.out.println("--*Error: Maybe RunMode is wrong!*--");
			System.out.println("Exit with status 1");
			System.exit(1);
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
		if(!JSONFunction.Search(this.Lang,Path)) System.out.println("--*Undefined lang field: \""+Path+"\" *--");
		return JSONFunction.Search(this.Lang,Path)?this.Lang.getString(Path):"{Undefined}";
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