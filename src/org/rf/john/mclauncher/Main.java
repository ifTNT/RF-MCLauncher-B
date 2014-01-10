package org.rf.john.mclauncher;

/*-----------引用的檔案---------------

Apache commons-codec1.8.jar
JSON.org json.jar(自行編譯)
Apache apache-ant-1.9.2/ant.jar
tukaani.org xz-java-1.4.zip

-----------END 引用的檔案-------------*/

/*------------------------------------GPLv3授權---------------------------------------

    -------------English------------------
    RF-MCLauncher-B is a unoffical Minecraft launcher.
    Copyright (C) 2013 by曙@RasFun軟體工作室
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    ----------------中文------------------
    RF-MCLauncher-B是一個非官方的Minecraft啟動器
    
    版權所有 (C) 2013 by曙@RasFun軟體工作室
    
    
    本程式爲自由軟體；您可依據自由軟體基金會所發表的GNU通用公共授權條款，對本程式再次發佈和/或修改；無論您依據的是本授權的第三版，或（您可選的）任一日後發行的版本。

	本程式是基於使用目的而加以發佈，然而不負任何擔保責任；亦無對適售性或特定目的適用性所爲的默示性擔保。詳情請參照GNU通用公共授權。

	您應已收到附隨於本程式的GNU通用公共授權的副本；如果沒有，請參照

	<http://www.gnu.org/licenses/>.

-------------------------------------END GPLv3授權---------------------------------------*/

/*============================================
 | **特別規定: 請勿將此啟動器變更為非正版的啟動器** |
 ============================================*/


import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;

import org.apache.commons.codec.binary.Base64;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.json.JSONObject;
import org.rf.john.mclauncher.langs.*;

import org.tukaani.xz.XZInputStream;

/**
 * 
 * @author john
 * @version 2.0a
 * @since 2013/08/25
 * 
 */

/*class OS{
	/**
	 * 取得作業系統
	 * @return 作業系統代碼(windows,linux,know)
	 *
}*/
class TimeCounter{ //計算程式執行時間
	private static double startTime,stopTime;
	/**
	 * 開始計時
	 */
	public static void start(){
		startTime=System.currentTimeMillis();
	}
	/**
	 * 停止計時
	 */
	public static void stop(){
		stopTime=System.currentTimeMillis();
	}
	/**
	 * 計算執行時間,並印出
	 */
	public static void count(){
		System.out.println("\n---Run Time---");
		System.out.println(((stopTime-startTime)/1000)+" sec");
		System.out.println((stopTime-startTime)+" ms");
		System.out.println("--------------");
	}
}
public class Main {
	@SuppressWarnings("deprecation")
	public static void main(String args[]) {
		/*
		 * 啟動參數:
		 * --NoDelNatives :不要刪除Natives資料夾(除錯用)
		 * --NoUpdate	  :不檢查更新
		 * 
		 */
		//-----------------------------------------------------------
		System.out.println("");
		System.out.println("   _________________         ________________");
		System.out.println("  |                  \\      |                |");
		System.out.println("  |    _________     |      |     ___________|");
		System.out.println("  |   |         |    |      |    |");
		System.out.println("  |   |_________|    |      |    |___________");
		System.out.println("  |                  |      |                |");
		System.out.println("  |    ___    ______ /      |    ___________ |");
		System.out.println("  |   |   \\   \\             |    |");
		System.out.println("  |   |    \\   \\            |    |");
		System.out.println("  |   |     \\   \\           |    |");
		System.out.println("  |   |      \\   \\          |    |");
		System.out.println("  |   |       \\   \\         |    |");
		System.out.println("  |___|        \\___\\ as     |____| un軟體工作室");
		//-------------------------------------------------------------
		if(RFInfo.RunMode.equals(RunType.JAR)){
			File file = new File("launch.log");  
			FileOutputStream fis;
			try {
				fis = new FileOutputStream(file);
				PrintStream out = new PrintStream(fis); 
				System.setOut(out);
				System.setErr(out);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		System.out.println("");
		System.out.println("   _________________         ________________");
		System.out.println("  |                                  \\       |                               |");
		System.out.println("  |        _________         |      |         ___________|");
		System.out.println("  |       |                  |        |      |        |");
		System.out.println("  |       |_________|        |      |        |___________");
		System.out.println("  |                                   |      |                                |");
		System.out.println("  |       ___        ______ /       |        ___________ |");
		System.out.println("  |      |      \\      \\                    |        |");
		System.out.println("  |      |        \\      \\                  |        |");
		System.out.println("  |      |          \\      \\                |        |");
		System.out.println("  |      |            \\      \\              |        |");
		System.out.println("  |      |              \\      \\            |        |");
		System.out.println("  |___|                \\___\\   as   |____|   un軟體工作室");
		System.out.println("\nRF-MCLauncher-B "+RFInfo.Version);
		System.out.println("版權所有 (C) 2013 by曙@RasFun軟體工作室");
		System.out.println("本程式採用GPLv3授權 請詳閱相關條款\n");
		System.out.print("Arguments: ");
		for(String OneArg:args){
			System.out.print(OneArg+" ");
			RFInfo.Arguments.add(OneArg);
		}
		System.out.println("");
		System.out.println("os.name= "+System.getProperty("os.name"));
		System.out.println("os.arch= "+System.getProperty("os.arch"));
		System.out.println("os.version= "+System.getProperty("os.version"));
		System.out.println("java.version= "+System.getProperty("java.version"));
		System.out.println("Running at \" "+System.getProperty("user.dir")+" \"");
		
		Save_LoadConfig langconfig = new Save_LoadConfig();
		String LocalLang="";
		switch(System.getProperty("user.country").toLowerCase()){
		case "tw":
			LocalLang="lang_tw.lang";
			break;
		case "cn":
			LocalLang="lang_cn_simple.lang";
			break;
		default:
			LocalLang="lang_en.lang";
			break;
			
		}
		RFInfo.SelectedLang = new Languages().SelectLanguages(langconfig.Saved()?langconfig.getSelectedLang():LocalLang);
		
		try{
			URL RFMCLB_InfoDownloadURL = new URL("http://rf_mclauncher_b.000space.com/RFMCLB_Info.json");
			URLConnection urlc = RFMCLB_InfoDownloadURL.openConnection();
			urlc.setConnectTimeout(5000);
			HttpURLConnection ConnectObj = (HttpURLConnection) urlc;
			ConnectObj.addRequestProperty("User-Agent", "Mozilla/5.0");
			ConnectObj.connect();
			BufferedInputStream InputStream = new BufferedInputStream(ConnectObj.getInputStream());
			byte[] buf=new byte[128];
			String RFMCLB_Info="";
            int len=InputStream.read(buf);
            while(len > 0){ 
            	RFMCLB_Info = RFMCLB_Info+new String(buf);
                len=InputStream.read(buf);
            }
			InputStream.close();
			RFInfo.RFMCLB_InfoJSON=(RFMCLB_Info.equals("")?null:new JSONObject(RFMCLB_Info));
			
			RFInfo.Launcher=new Launcher();
			//JSONObject RFMCLB_InfoJSON = new JSONObject(RFMCLB_Info);
			/*
			//--------Start 產生物件-------
			URL ClassSearchPath[]={new File("").getAbsoluteFile().toURL()}; //搜尋路徑
			ClassLoader cl = new URLClassLoader(ClassSearchPath); //產生ClassLoader
			Launcher OldLauncher=null;
			if(new File("org/rf/john/mclauncher/Launcher.class".replace("/",System.getProperty("file.separator"))).isFile()){ //Load From File
				Class c = cl.loadClass("org.rf.john.mclauncher.Launcher");
				//Constructor constructor = c.getConstructor(Languages.class);
				OldLauncher = (Launcher)c.newInstance();
				System.out.println("Load Launcher From \" RFMCLB_Launcher.class \"(#"+OldLauncher.PrintLauncherVersion()+")");
			}else{ //Load From JAR
				OldLauncher=new Launcher(RFInfo.SelectedLang);
				System.out.println("Load Launcher From JAR(#"+OldLauncher.PrintLauncherVersion()+")");
			}
			//-------End 產生物件---------
			
			if(RFInfo.RFMCLB_InfoJSON.getInt("LauncherLastVersion") <= OldLauncher.PrintLauncherVersion() || RFInfo.RunMode == RunType.Debug || RFInfo.TestArguments("--NoUpdate")){
				RFInfo.Launcher=OldLauncher; //No Update
				System.out.println(RFInfo.RunMode == RunType.Debug?"Skip Download!":"No Newer Launcher Found!");
			}else{
				System.out.println("Found New Launcher(#"+RFInfo.RFMCLB_InfoJSON.getInt("LauncherLastVersion")+"), Downloading...");
				URL NewLauncherURL = new URL("http://rf_mclauncher_b.000space.com/RFMCLB_Launcher_"+RFInfo.RFMCLB_InfoJSON.getInt("LauncherLastVersion")+".class");
				//DownloadLauncherDisplayThread DisplayThread=new DownloadLauncherDisplayThread();
				urlc = NewLauncherURL.openConnection();
				urlc.setConnectTimeout(5000);
				ConnectObj = (HttpURLConnection) urlc;
				ConnectObj.addRequestProperty("User-Agent", "Mozilla/5.0");
				ConnectObj.connect();
				//long FileSize=ConnectObj.getContentLengthLong();
				InputStream = new BufferedInputStream(ConnectObj.getInputStream());
				
				String Dirs[]="org/rf/john/mclauncher/".split("/");
				System.out.println("Creating Dir: org/rf/john/mclauncher/");
				String temp="";
				for(String OneDir:Dirs){
					temp += OneDir+System.getProperty("file.separator");
					if(!new File(temp).isDirectory())
						if(!new File(temp).mkdir()){
							System.out.println("Creating Dir Error: "+temp);
						}
				}
				new File("org/rf/john/mclauncher/Launcher.class".replace("/",System.getProperty("file.separator"))).createNewFile();
				BufferedOutputStream WriteStream = new BufferedOutputStream(new FileOutputStream("org/rf/john/mclauncher/Launcher.class".replace("/",System.getProperty("file.separator"))));
				buf=new byte[1024];
				//long ReadBytes=0;
				//DisplayThread.start();
	            len=InputStream.read(buf);
	            while(len > 0){
	            	WriteStream.write(buf,0, len);
	            	//ReadBytes+=len;
	            	//int Progress=(int)((ReadBytes/FileSize)*100);
	            	//DisplayThread.DisplayBar.setValue(Progress);
	            	//DisplayThread.DisplayBar.setString(Progress+"%");
	                len=InputStream.read(buf);
	            }
				InputStream.close();
				WriteStream.flush();
				WriteStream.close();
				//DisplayThread=null;
				System.out.println("Download Finish!");
				
				//--------Start 產生物件-------
				Class c = cl.loadClass("org.rf.john.mclauncher.Launcher");
				//Constructor constructor = c.getConstructor(Languages.class);
				RFInfo.Launcher = (Launcher)c.newInstance();
				//-------End 產生物件---------
			}
		}catch(IOException e){
			System.out.println("--*Get RFMCLB_Info Error*--");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("--*Load Launcher Error*--");
			e.printStackTrace();
		} /*catch (NoSuchMethodException e) {
			System.out.println("--*Load Launcher Error*--");
			e.printStackTrace();
		}/ catch (SecurityException e) {
			System.out.println("--*Load Launcher Error*--");
			e.printStackTrace();
		} catch (InstantiationException e) {
			System.out.println("--*Load Launcher Error*--");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("--*Load Launcher Error*--");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.out.println("--*Load Launcher Error*--");
			e.printStackTrace();
		}*/
		}catch(IOException e){
			System.out.println("--*Get RFMCLB_Info Error*--");
			e.printStackTrace();
		}
			
		System.out.println("Launcher Version: #"+RFInfo.Launcher.PrintLauncherVersion());
		//RFInfo.Launcher=new Launcher(RFInfo.SelectedLang);
		System.out.println("Default JVM path: "+RFInfo.Launcher.JVMPath); //JVM預設路徑
		System.out.println("Default Minecraft path: "+RFInfo.Launcher.minecraftDir+"\n"); //minecraft預設路徑
		
		System.out.println("Language: "+RFInfo.SelectedLang.getString("LangName")); //設定語系
		
		//System.out.println("VM Name: "+System.getProperty("java.vm.name"));
		RFInfo.MainFrameObj = new MainFrame2(); //實體化視窗
	}
}/*
class DownloadLauncherDisplayThread extends Thread{
	public JProgressBar DisplayBar;
	public DownloadLauncherDisplayThread(){
		Thread.currentThread().setName("DownloadLauncherDisplayThread");
		DisplayBar=new JProgressBar();
		DisplayBar.setEnabled(false);
		DisplayBar.setIndeterminate(true);
		DisplayBar.setString("正在下載啟動器...");
		DisplayBar.setMaximum(100);
	}
	public void run(){
		DisplayBar.setEnabled(true);
		DisplayBar.setIndeterminate(false);
		Object Message[]=new Object[2];
		Message[0]="<html><span style='color:red;font-weight:bold;'>請勿關閉此視窗</span></html>\n正在下載新版啟動器...";
		Message[1]=DisplayBar;
		new JOptionPane().showMessageDialog(null,Message,"正在下載新版啟動器",JOptionPane.INFORMATION_MESSAGE);
	}
}*/
class launcheThread extends Thread{ //啟動執行緒
	@SuppressWarnings("static-access")
	@Override
	public void run(){
		Thread.currentThread().setName("LaunchThread");
		TimeCounter.start();
		RFInfo.Theme.MainProgressBar.setEnabled(true);
		RFInfo.Theme.MainProgressBar.setIndeterminate(true);
		RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("Logining"));
		RFInfo.Theme.MainProgressBar.setMaximum(1000);
		RFInfo.Theme.LoginBtn.setEnabled(false);
		RFInfo.Theme.UserBox.setEnabled(false);
		RFInfo.Theme.PwdBox.setEnabled(false);
		RFInfo.Theme.ProfileSelectBox.setEnabled(false);
		RFInfo.Theme.RememberMe.setEnabled(false);
		System.out.println("Login!!");
		//String password = new String(RFInfo.MainFrameObj.PwdBox.getPassword());
		Save_LoadConfig config = new Save_LoadConfig();
		if(RFInfo.Theme.RememberMe.isSelected()){
			config.SaveUser(new String(RFInfo.Theme.PwdBox.getPassword()), RFInfo.Theme.UserBox.getText());
		}else{
			config.DeleteData();
		}
		config.saveConfig(RFInfo.MainFrameObj.JVMPathText.getText(),RFInfo.MainFrameObj.MinecraftPathText.getText(),RFInfo.SelectedLang.LangFile,RFInfo.Launcher.LastVersion);
		//RFInfo.Launcher=new Launcher(RFInfo.SelectedLang);
		if(Login.canConnect()){
			if(Login.connect(RFInfo.Theme.UserBox.getText(),new String(RFInfo.Theme.PwdBox.getPassword()))){
				RFInfo.Theme.MainProgressBar.setIndeterminate(false);
				RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("LoginSuccess"));
				RFInfo.Launcher.minecraftDir=RFInfo.MainFrameObj.MinecraftPathText.getText();
				RFInfo.Launcher.JVMPath=RFInfo.MainFrameObj.JVMPathText.getText();
				RFInfo.Launcher.LaunchGame(Login.getUser(),RFInfo.Launcher.getInstalledProfiles().get(RFInfo.Theme.ProfileSelectBox.getSelectedIndex()),Login.getSession());
			}else{ //登入錯誤
				RFInfo.Theme.LoginBtn.setEnabled(true);
				RFInfo.Theme.UserBox.setEnabled(true);
				RFInfo.Theme.PwdBox.setEnabled(true);
				RFInfo.Theme.ProfileSelectBox.setEnabled(true);
				RFInfo.Theme.RememberMe.setEnabled(true);
				RFInfo.Theme.MainProgressBar.setIndeterminate(false);
				RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("LoginError")+"("+Login.Result+")");
				RFInfo.Theme.MainProgressBar.setEnabled(false);
				new JOptionPane().showMessageDialog(null,"<html><span style=\"color: red;\">"+RFInfo.SelectedLang.getString("LoginError")+"("+Login.Result+")</span></html>",RFInfo.SelectedLang.getString("LoginError"),JOptionPane.ERROR_MESSAGE);
			}
		}else{ //離線啟動
			RFInfo.Theme.MainProgressBar.setIndeterminate(false);
			RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("LoginSuccess"));
			RFInfo.Launcher.minecraftDir=RFInfo.MainFrameObj.MinecraftPathText.getText();
			RFInfo.Launcher.JVMPath=RFInfo.MainFrameObj.JVMPathText.getText();
			RFInfo.Launcher.LaunchGameOffline(RFInfo.Launcher.getInstalledProfiles().get(RFInfo.Theme.ProfileSelectBox.getSelectedIndex()));
		}
	}
}
class Save_LoadConfig{ //設定檔操作
	//private String MinecraftDir="";
	private String ConfigFile = "."+System.getProperty("file.separator")+"RFMCLB.config";
	//private File PasswordPath = new File("./lastlogin.pwd");
	//private File UserPath = new File("./lastlogin");
	public Save_LoadConfig(){}
	/**
	 * 儲存玩家帳號
	 * @param pwd 密碼
	 * @param user 帳號名稱
	 */
	public void SaveUser(String pwd,String user){
		pwd = new String(new Base64().encode(pwd.getBytes()));  //使用base64加密
		try{
			JSONObject json;
			if(new File(this.ConfigFile).exists()){
				json = JSONFunction.CreateFromFile(this.ConfigFile);
			}else{
				json = new JSONObject();
			}
			json.put("user",user);
			json.put("pwd",pwd);
			JSONFunction.WriteToFile(json,this.ConfigFile);
		}catch(IOException e){
			System.out.println("--*Saving User Error*--");
			e.printStackTrace();
		}
	}
	/**
	 * 載入玩家密碼
	 * @return 密碼
	 */
	public String LoadPassword(){
		try{
		if(!new File(this.ConfigFile).exists()) return null;
			JSONObject json = JSONFunction.CreateFromFile(this.ConfigFile);
			return new String(new Base64().decode(json.getString("pwd")));
		}catch(IOException e){
			System.out.println("--*Load Password Error*--");
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 載入玩家帳號
	 * @return 帳號
	 */
	public String LoadUser(){
		try{
			if(!new File(this.ConfigFile).exists()) return null;
			JSONObject json = JSONFunction.CreateFromFile(this.ConfigFile);
			return json.getString("user");
		}catch(IOException e){
			System.out.println("--*Load User Error*--");
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 檢查設定檔是否已儲存
	 * @return 是否已儲存設定檔
	 */
	public boolean Saved(){
		return new File(this.ConfigFile).exists();
	}
	/**
	 * 檢查玩家帳號是否已儲存
	 * @return 玩家帳號
	 */
	public boolean Logined(){
		try{
			if(!new File(this.ConfigFile).exists()) return false;
			JSONObject json = JSONFunction.CreateFromFile(this.ConfigFile);
			return JSONFunction.Search(json,"user");
		}catch(IOException e){
			System.out.println("--*Check Logined Error*--");
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 刪除設定檔
	 */
	public void DeleteData(){
		new File(this.ConfigFile).delete();
	}
	/**
	 * 取得已儲存的JVM路徑
	 * @return JVM路徑
	 */
	public String getJVMPath(){
		try{
			if(!new File(this.ConfigFile).exists()) return null;
			JSONObject json = JSONFunction.CreateFromFile(this.ConfigFile);
			return JSONFunction.Search(json,"JVMPath")?json.getString("JVMPath"):"";
		}catch(IOException e){
			System.out.println("--*Load JVMPath Error*--");
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 取得已儲存的Minecraft路徑
	 * @return Minecraft路徑
	 */
	public String getMinecraftPath(){
		try{
			if(!new File(this.ConfigFile).exists()) return null;
			JSONObject json = JSONFunction.CreateFromFile(this.ConfigFile);
			return JSONFunction.Search(json,"MinecraftPath")?json.getString("MinecraftPath"):"";
		}catch(IOException e){
			System.out.println("--*Load MinecraftPath Error*--");
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 取得已選擇的語系
	 * @return 語系檔名稱
	 */
	public String getSelectedLang(){
		try{
			if(!new File(this.ConfigFile).exists()) return null;
			JSONObject json = JSONFunction.CreateFromFile(this.ConfigFile);
			return JSONFunction.Search(json,"SelectedLang")?json.getString("SelectedLang"):"";
		}catch(IOException e){
			System.out.println("--*Load SelectedLang Error*--");
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 取得Minecraft Last Version(離線時使用)
	 * @return LastVersion
	 */
	public String getLastVersion(){
		try{
			if(!new File(this.ConfigFile).exists()) return null;
			JSONObject json = JSONFunction.CreateFromFile(this.ConfigFile);
			return JSONFunction.Search(json,"LastVersion")?json.getString("LastVersion"):"";
		}catch(IOException e){
			System.out.println("--*Load LastVersion Error*--");
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 儲存設定檔
	 * @param JVMPath JVM路徑
	 * @param MinecraftPath Minecraft路徑
	 * @param SelectedLang 已選擇的語系
	 * @param LastVersion MinecraftLastVersion
	 */
	public void saveConfig(String JVMPath,String MinecraftPath,String SelectedLang,String LastVersion){
		try{
			JSONObject json;
			if(new File(this.ConfigFile).exists()){
				json = JSONFunction.CreateFromFile(this.ConfigFile);
			}else{
				json = new JSONObject();
			}
			json.put("JVMPath",RFInfo.Launcher.JVMPath);//JVMPath);
			json.put("MinecraftPath",RFInfo.Launcher.minecraftDir);//MinecraftPath);
			json.put("SelectedLang",SelectedLang);
			json.put("LastVersion",RFInfo.Launcher.LastVersion);
			JSONFunction.WriteToFile(json,this.ConfigFile);
		}catch(IOException e){
			System.out.println("--*Saving Config Error*--");
			e.printStackTrace();
		}
	}
}
/**
 * 多執行緒工作分配器
 * @author john
 *
 */
class ThreadJob{
	private int JobCount;
	private int FinishedJobs;
	//private MainFrame FrameObj;
	private ThreadJobType Type;
	private String JobName;
	private int o0o=0;
	private int o0oDelay=0;
	/**
	 * 開始多執行緒下載
	 * @param _JobName 工做名稱
	 * @param DownloadBaseURL 下載路徑(Key=URL,Value=DownloadHost)
	 */
	public void DownloadJob(String _JobName,HashMap<String,String> DownloadBaseURL){
		this.Type=ThreadJobType.Download;
		this.JobCount=DownloadBaseURL.size();
		this.JobName=_JobName;
		RFInfo.Theme.MainProgressBar.setMaximum(this.JobCount);
		System.out.print("\n---Starting job:[Download]");
		RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("Downloading")+"("+this.FinishedJobs+"/"+this.JobCount+")");
		System.out.println(" max thread:"+this.JobCount);
		for(Entry<String,String> OneDownloadJob:DownloadBaseURL.entrySet()){
			new DownloadThread(OneDownloadJob.getValue(),OneDownloadJob.getKey(),this).start();
		}
		while(this.FinishedJobs!=this.JobCount){
			if(o0oDelay==0){
				String Showo0o=(o0o==0?"0":"o")+(o0o==1?"0":"o")+(o0o==2?"0":"o");
				RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("Downloading")+" ("+this.FinishedJobs+"/"+this.JobCount+")"+Showo0o);
				o0o=(o0o+1)%3;
			}
			o0oDelay=(o0oDelay+1)%80;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("--*DownloadThreadJobError*--");
				e.printStackTrace();
			}
		}
		RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("LoadingLib"));
		System.out.println("---Finished job:["+this.JobName+"][Download]");
		return;
	}
	
	/**
	 * 開始多執行緒解壓縮
	 * @param _JobName 工作名稱
	 * @param ExtractFile 欲解壓縮的檔案
	 * @param TargetDir 目標資料夾
	 * @param exclude 排除的檔案
	 */
	public void ExtractJob(String _JobName,ArrayList<String> ExtractFile,ArrayList<String> TargetDir,ArrayList<ArrayList<String>> exclude){ //Extract
		this.Type=ThreadJobType.Extract;
		this.JobCount=ExtractFile.size();
		this.JobName=_JobName;
		RFInfo.Theme.MainProgressBar.setMaximum(this.JobCount);
		System.out.print("\n---Starting job:[Extract]");
		RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("LoadingLib")+"("+this.FinishedJobs+"/"+this.JobCount+")");
		System.out.println(" max thread:"+this.JobCount);
		for(int i=0;i<this.JobCount;i++){
			new ExtractThread(ExtractFile.get(i),TargetDir.get(i),exclude.get(i).toArray(),this).start();
		}
		while(this.FinishedJobs!=this.JobCount){
			if(o0oDelay==0){
				String Showo0o=(o0o==0?"0":"o")+(o0o==1?"0":"o")+(o0o==2?"0":"o");
				RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("LoadingLib")+" ("+this.FinishedJobs+"/"+this.JobCount+")"+Showo0o);
				o0o=(o0o+1)%3;
			}
			o0oDelay=(o0oDelay+1)%80;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("--*ExtractThreadJobError*--");
				e.printStackTrace();
			}
		}
		RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("LoadingLib"));
		System.out.println("---Finished job:["+this.JobName+"][Extract]");
		return;
	}
	/**
	 * 單一執行緒結束工作時,所觸發的方法
	 */
	public synchronized void FinishJob(){
		this.FinishedJobs++;
		RFInfo.Theme.MainProgressBar.setValue(FinishedJobs); //完成進度
		String Showo0o=(o0o==0?"0":"o")+(o0o==1?"0":"o")+(o0o==2?"0":"o");
		switch(this.Type){
		case Download:
			RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("Downloading")+" ("+this.FinishedJobs+"/"+this.JobCount+")"+Showo0o);
			break;
		case Extract:
			RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("LoadingLib")+" ("+this.FinishedJobs+"/"+this.JobCount+")"+Showo0o);
			break;
		}
	}
}
enum ThreadJobType{
	Download,Extract
}
/**
 * 多執行緒下載實作類別
 * @author john
 *
 */
class DownloadThread extends Thread{
	private String FileURL="";
	private ThreadJob JobTable;
	private String DownloadBase;
	
	/**
	 * 單一執行緒工作設定
	 * @param DownloadHost 下載主機
	 * @param DownloadURL 下載路徑
	 * @param rootjob 工作分配器
	 */
	public DownloadThread(String DownloadHost,String DownloadURL,ThreadJob rootjob){
		this.FileURL=DownloadURL;
		this.JobTable=rootjob;
		this.DownloadBase=DownloadHost;
	}
	@Override
	public void run(){
		String downloadURL=this.DownloadBase+FileURL.replace("\\","/"); //replace是給windows用的
		String LibrariesDir=RFInfo.Launcher.minecraftDir+"libraries"+RFInfo.Launcher.SplitChar;
		String[] LibrariesPathTemp=this.FileURL.split(RFInfo.Launcher.SplitChar.equals("\\")?"\\\\":RFInfo.Launcher.SplitChar); //windows要特別加"\\"
		String LibrariesPath="";
		for(int i=0;i<LibrariesPathTemp.length-1;i++){
			LibrariesPath += LibrariesPathTemp[i]+RFInfo.Launcher.SplitChar;
		}
		
		System.out.println("Starting make dir: "+LibrariesDir+LibrariesPath);
		RFInfo.Launcher.makeDir(LibrariesDir+LibrariesPath);
		System.out.println("Starting download: "+LibrariesDir+FileURL);
		try{
			URL DownloadUrlObj = new URL(downloadURL);
			URLConnection urlc = DownloadUrlObj.openConnection();
			urlc.setConnectTimeout(5000);
			HttpURLConnection ConnectObj = (HttpURLConnection) urlc;
			ConnectObj.addRequestProperty("User-Agent", "Mozilla/5.0");
			ConnectObj.connect();
			BufferedInputStream DownloadStream = new BufferedInputStream(ConnectObj.getInputStream());
			new File(LibrariesDir+FileURL.replace("-universal","")).createNewFile();
			if(FileURL.matches(".*\\.pack\\.xz")){
				unpackLibrary(new File(LibrariesDir+FileURL),readFully(DownloadStream));
			}else{
				BufferedOutputStream WriteStream = new BufferedOutputStream(new FileOutputStream(LibrariesDir+FileURL.replace("-universal","")));
				byte[] data = new byte[1024]; //緩衝區大小1024byte
				int len = DownloadStream.read(data); //讀檔
				while(len > 0) {
					WriteStream.write(data,0,len);
					len = DownloadStream.read(data);
				}
				WriteStream.flush();
				DownloadStream.close();
				WriteStream.close();
			}
			System.out.println("Finish download:"+LibrariesDir+FileURL);
		}catch(IOException e){
			System.out.println("--*Download Error("+FileURL+")*--");
			e.printStackTrace();
			
		}
		this.JobTable.FinishJob();
	}
	//------START 從ForgeInstaller複製過來的方法------
	public static void unpackLibrary(File output, byte[] data) throws IOException {
		byte[] decompressed = readFully(new XZInputStream(new ByteArrayInputStream(data)));
	
		String end = new String(decompressed, decompressed.length - 4, 4);
		if (!end.equals("SIGN")){
			System.out.println("Unpacking failed, signature missing " + end);
			return;
		}
	
		int x = decompressed.length;
		int len = decompressed[(x - 8)] & 0xFF | (decompressed[(x - 7)] & 0xFF) << 8 | (decompressed[(x - 6)] & 0xFF) << 16 | (decompressed[(x - 5)] & 0xFF) << 24;
	
		byte[] checksums = Arrays.copyOfRange(decompressed, decompressed.length - len - 8, decompressed.length - 8);
	
		FileOutputStream jarBytes;
			
		jarBytes = new FileOutputStream(output);
		JarOutputStream jos = new JarOutputStream(jarBytes);
	
		Pack200.newUnpacker().unpack(new ByteArrayInputStream(decompressed), jos);
	
		jos.putNextEntry(new JarEntry("checksums.sha1"));
		jos.write(checksums);
		jos.closeEntry();
	
		jos.close();
		jarBytes.close();
	}
	public static byte[] readFully(InputStream stream) throws IOException{
		byte[] data = new byte[4096];
		ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();
		int len;
		do {
			len = stream.read(data);
			if (len > 0){
				entryBuffer.write(data, 0, len);
			}
		}while (len != -1);

		return entryBuffer.toByteArray();
	}
	//------END 從ForgeInstaller複製過來的方法------
}
/**
 * 多執行續解壓縮實作類別
 * @author john
 *
 */
class ExtractThread extends Thread{ 
    private String InputFile;
    private String TargetDir;
    private Object[] Exclude;
    private ThreadJob JobTable;
    /**
     * 單一執行續工作設定
     * @param inputfile 原始檔案
     * @param targetDir 目標資料夾
     * @param exclude 排除的檔案
     * @param rootjob 工作分配器
     */
	public ExtractThread(String inputfile,String targetDir,Object[] exclude,ThreadJob rootjob){
		this.InputFile=inputfile;
		this.TargetDir=targetDir;
		this.Exclude=exclude;
		this.JobTable=rootjob;
	}
	@Override
	public void run(){
		if(!new File(this.TargetDir).isDirectory()){
			new File(this.TargetDir).mkdir();
		}
		FileOutputStream fileOut; 
        File file; 
        InputStream inputStream; 
		Arrays.sort(this.Exclude);
        try{
            ZipFile zipFile = new ZipFile(this.InputFile);
            
            for(Enumeration<ZipEntry> entries = zipFile.getEntries(); entries.hasMoreElements();){ 
                ZipEntry entry = (ZipEntry)entries.nextElement(); 
                if(Arrays.binarySearch(this.Exclude,entry.getName()) > -1){
                	System.out.println("Skip: "+entry.getName());
                	continue;
                }
                file = new File(this.TargetDir+entry.getName()); 
                
                if(entry.isDirectory()){ 
                    file.mkdirs();
                }
                else{
                    if(!file.getParentFile().exists()){
                    	System.out.println("Skip: "+entry.getName());
                    	continue; 
                    }
                    System.out.println("Extracting: "+entry.getName());

                    inputStream = zipFile.getInputStream(entry); 

                    fileOut = new FileOutputStream(file);
                    byte[] buf=new byte[1024];
                    int len=inputStream.read(buf);
                    while(len > 0){ 
                        fileOut.write(buf,0,len);
                        len=inputStream.read(buf);
                    } 
                    fileOut.close();

                    inputStream.close(); 
                }    
            } 
            zipFile.close(); 
        }catch(IOException e){
        	System.out.println("--*Extract Error:*--");
            e.printStackTrace(); 
        }
        this.JobTable.FinishJob();
	}
}

/**
 * 帳號驗證
 * @author john
 */
class Login{
	private static String LoginServer="https://login.minecraft.net/";
	public static String Result="";
	private static boolean CanConnect=false;
	
	/**
	 * 驗證帳號
	 * @param user 帳號名稱
	 * @param pwd 密碼
	 * @return 是否登入成功
	 */
	public static boolean connect(String user,String pwd){
		try {
			System.out.println("Checking user...");
			URL loginurl = new URL(LoginServer+"?user="+user+"&password="+pwd+"&version="+(RFInfo.RFMCLB_InfoJSON!=null?RFInfo.RFMCLB_InfoJSON.getInt("LoginVersion"):"14"));
			URLConnection urlc = loginurl.openConnection();
			urlc.setConnectTimeout(5000);
			HttpURLConnection ConnectObj = (HttpURLConnection) urlc;
			ConnectObj.connect();
			InputStreamReader resules = new InputStreamReader(ConnectObj.getInputStream(),"UTF-8");
			int data = resules.read();
			while(data != -1){
				Result += (char) data;
				data = resules.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		switch(Result){
			case "Bad login":
				System.out.println("--*Login Error: Bad login!*--");
				return false;
			case "Old version":
				System.out.println("--*Login Error: Old version!*--");
				return false;
			default:
				System.out.println("Login success!");
				return true;
			
		}
	}
	
	/**
	 * 取得玩家名稱(登入後)
	 * @return 玩家名稱
	 */
	public static String getUser(){
		String[] splitResult = Result.split(":");
		return splitResult[2];
	}
	
	/**
	 * 取得Session(適用1.7以前)
	 * @return Session
	 */
	public static String getSession(){
		String[] splitResult = Result.split(":");
		return ("token:"+splitResult[3]+":"+splitResult[4]);
	}
	
	/**
	 * 取得玩家的UUID(適用1.7以後)
	 * @return UUID
	 */
	public static String getUUID(){
		String[] splitResult = Result.split(":");
		return (splitResult[4]);
	}
	
	/**
	 * 取得登入令牌(適用1.7以後)
	 * @return 登入令牌
	 */
	public static String getAccessToken(){
		String[] splitResult = Result.split(":");
		return (splitResult[3]);
	}
	
	/**
	 * 檢查連線
	 */
	public static void checkConnect(){
		boolean ReturnValues = false;
		try {
			URL loginurl = new URL(LoginServer);
			URLConnection urlc = loginurl.openConnection();
			urlc.setConnectTimeout(5000);
			HttpURLConnection ConnectObj = (HttpURLConnection) urlc;
			ConnectObj.connect();
			ReturnValues = ((ConnectObj.getResponseCode()==HttpURLConnection.HTTP_OK)?true:false);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		CanConnect=ReturnValues;
	}
	
	/**
	 * 檢查連線(快速)
	 * @return 是否可連線
	 */
	public static boolean canConnect(){
		return CanConnect;
	}
}