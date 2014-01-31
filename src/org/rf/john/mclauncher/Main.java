package org.rf.john.mclauncher;

/*-----------引用的檔案---------------

Apache commons-codec1.8.jar
JSON.org json.jar(自行編譯)
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

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.codec.binary.Base64;
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
		if(Status.RunMode.equals(RunModeUtil.JAR)){
			try {
				PrintStream out = new PrintStream(new FileOutputStream(new File("launch.log"))); 
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
		System.out.println("\nRF-MCLauncher-B "+Status.Version);
		System.out.println("版權所有 (C) 2014 by曙@RasFun軟體工作室");
		System.out.println("本程式採用GPLv3授權 請詳閱相關條款\n");
		System.out.print("Arguments: ");
		for(String OneArg:args){
			System.out.print(OneArg+" ");
			Status.Arguments.add(OneArg);
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
		Status.SelectedLang = new Languages().SelectLanguages(langconfig.Saved()?langconfig.getSelectedLang():LocalLang);
		
		Status.Launcher=new Launcher(Status.SelectedLang);
		
		new Thread(){@Override public void run(){Login.checkConnect();}}.start();
		new Thread(){@Override public void run(){
			try {
				/*URL RFMCLB_InfoDownloadURL = new URL("https://raw2.github.com/ifTNT/RF-MCLauncher-B/master/RFMCLB_Info.json");
				URLConnection urlc = RFMCLB_InfoDownloadURL.openConnection();
				urlc.setConnectTimeout(5000);
				HttpURLConnection ConnectObj = (HttpURLConnection) urlc;
				ConnectObj.addRequestProperty("User-Agent", "Mozilla/5.0");
				ConnectObj.connect();*/
				Status.RFMCLB_InfoJSON=JSONFunction.CreateFromStream(
						DownloadThread.getConnectObj("https://raw2.github.com/ifTNT/RF-MCLauncher-B/master/RFMCLB_Info.json").getInputStream());
			} catch (IOException e) {
				System.out.println("--*Get RFMCLB_Info Error*--");
				e.printStackTrace();
				System.out.println("--*Use default sitting*--");
			}
		}}.start();
		/*try{
			Status.RFMCLB_InfoJSON=JSONFunction.CreateFromStream(
					DownloadThread.getConnectObj("https://raw2.github.com/ifTNT/RF-MCLauncher-B/master/RFMCLB_Info.json").getInputStream());
			
			//--------Start 產生物件-------
			//URL ClassSearchPath[]={new File("").getAbsoluteFile().toURL()}; //搜尋路徑
			ClassLoader cl = new URLClassLoader(new URL[] {new URL(System.getProperty("user.dir"))}); //產生ClassLoader
			Launcher OldLauncher=null;
			if(new File("org/rf/john/mclauncher/Launcher.class".replace("/",System.getProperty("file.separator"))).isFile()){ //Load From File
				Class<?> c = cl.loadClass("org.rf.john.mclauncher.Launcher");
				//Constructor constructor = c.getConstructor(Languages.class);
				OldLauncher = (Launcher)c.newInstance();
				System.out.println("Load Launcher From \" RFMCLB_Launcher.class\n"+OldLauncher.toString());
			}else{ //Load From JAR
				OldLauncher=new Launcher(Status.SelectedLang);
				System.out.println("Load Launcher From JAR\n"+OldLauncher.toString());
			}
			//-------End 產生物件---------
			
			if(Status.RFMCLB_InfoJSON.getInt("LauncherLastVersion") <= OldLauncher.LauncherVersion || Status.RunMode == RunModeUtil.Debug || Status.TestArguments("--NoUpdate")){
				Status.Launcher=OldLauncher; //No Update
				System.out.println(Status.RunMode == RunModeUtil.Debug?"Skip Download!":"No Newer Launcher Found!");
			}else{
				System.out.println("Found New Launcher(#"+Status.RFMCLB_InfoJSON.getInt("LauncherLastVersion")+"), Downloading...");
				
				BufferedInputStream InputStream = new BufferedInputStream(DownloadThread.getConnectObj("http://rf_mclauncher_b.000space.com/RFMCLB_Launcher_"+Status.RFMCLB_InfoJSON.getInt("LauncherLastVersion")+".class").getInputStream());
						
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
				WriteStream.write(DownloadThread.readFully(InputStream));
				InputStream.close();
				WriteStream.flush();
				WriteStream.close();
				//DisplayThread=null;
				System.out.println("Download Finish!");
						
				//--------Start 產生物件-------
				Class<?> c = cl.loadClass("org.rf.john.mclauncher.Launcher");
				//Constructor constructor = c.getConstructor(Languages.class);
				Status.Launcher = (Launcher)c.newInstance();
				//-------End 產生物件---------
			}
		}catch(IOException e){
			System.out.println("--*Get RFMCLB_Info Error*--");
			System.out.println("--*Use default sitting*--");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("--*Load Launcher Error*--");
			e.printStackTrace();
		} catch (SecurityException e) {
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
			
		//RFInfo.Launcher=new Launcher(RFInfo.SelectedLang);
		System.out.println("Default JVM path: "+Status.Launcher.JVMPath); //JVM預設路徑
		System.out.println("Default Minecraft path: "+Status.Launcher.minecraftDir+"\n"); //minecraft預設路徑
		
		System.out.println("Language: "+Status.SelectedLang.getString("LangName")); //設定語系
		
		//System.out.println("VM Name: "+System.getProperty("java.vm.name"));
		Status.MainFrameObj = new MainFrame2(); //實體化視窗
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
		if(Status.Theme.UserBox.getText()==""||Status.Theme.PwdBox.getPassword().length==0){
			new JOptionPane().showMessageDialog(null,"<html><span style=\"color: red;\">"+Status.SelectedLang.getString("LoginError")+": "+Status.SelectedLang.getString("PleaseEnterUserAndPw")+"</span></html>",Status.SelectedLang.getString("LoginError"),JOptionPane.ERROR_MESSAGE);;
		}else{
			TimeCounter.start();
			Status.Theme.MainProgressBar.setEnabled(true);
			Status.Theme.MainProgressBar.setIndeterminate(true);
			Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("Logining"));
			Status.Theme.MainProgressBar.setMaximum(1000);
			Status.Theme.LoginBtn.setEnabled(false);
			Status.Theme.UserBox.setEnabled(false);
			Status.Theme.PwdBox.setEnabled(false);
			Status.Theme.ProfileSelectBox.setEnabled(false);
			Status.Theme.RememberMe.setEnabled(false);
			System.out.println("Login!!");
			//String password = new String(RFInfo.MainFrameObj.PwdBox.getPassword());
			Save_LoadConfig config = new Save_LoadConfig();
			if(Status.Theme.RememberMe.isSelected()){
				config.SaveUser(new String(Status.Theme.PwdBox.getPassword()), Status.Theme.UserBox.getText());
			}else{
				config.DeleteData();
			}
			config.saveConfig(Status.MainFrameObj.JVMPathText.getText(),Status.MainFrameObj.MinecraftPathText.getText(),Status.SelectedLang.LangFile,Status.Launcher.LastVersion);
			//RFInfo.Launcher=new Launcher(RFInfo.SelectedLang);
			if(Login.canConnect()){
				if(Login.connect(Status.Theme.UserBox.getText(),new String(Status.Theme.PwdBox.getPassword()))){
					Status.Theme.MainProgressBar.setIndeterminate(false);
					Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("LoginSuccess"));
					Status.Launcher.minecraftDir=Status.MainFrameObj.MinecraftPathText.getText();
					Status.Launcher.JVMPath=Status.MainFrameObj.JVMPathText.getText();
					Status.Launcher.LaunchGame(Login.getUser(),Status.Launcher.getInstalledProfiles().get(Status.Theme.ProfileSelectBox.getSelectedIndex()),Login.getSession());
				}else{ //登入錯誤
					Status.Theme.LoginBtn.setEnabled(true);
					Status.Theme.UserBox.setEnabled(true);
					Status.Theme.PwdBox.setEnabled(true);
					Status.Theme.ProfileSelectBox.setEnabled(true);
					Status.Theme.RememberMe.setEnabled(true);
					Status.Theme.MainProgressBar.setIndeterminate(false);
					Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("LoginError")+"("+Login.Result+")");
					Status.Theme.MainProgressBar.setEnabled(false);
					new JOptionPane().showMessageDialog(null,"<html><span style=\"color: red;\">"+Status.SelectedLang.getString("LoginError")+"("+Login.Result+")</span></html>",Status.SelectedLang.getString("LoginError"),JOptionPane.ERROR_MESSAGE);
				}
			}else{ //離線啟動
				Status.Theme.MainProgressBar.setIndeterminate(false);
				Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("LoginSuccess"));
				Status.Launcher.minecraftDir=Status.MainFrameObj.MinecraftPathText.getText();
				Status.Launcher.JVMPath=Status.MainFrameObj.JVMPathText.getText();
				Status.Launcher.LaunchGameOffline(Status.Launcher.getInstalledProfiles().get(Status.Theme.ProfileSelectBox.getSelectedIndex()));
			}
		}
	}
}
class Save_LoadConfig{ //設定檔操作
	//private String MinecraftDir="";
	private String ConfigFile = "."+System.getProperty("file.separator")+"RFMCLB.config";
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
			return json.has("user")&&json.has("pwd")?new String(new Base64().decode(json.getString("pwd"))):"";
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
			return json.has("user")?json.getString("user"):"";
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
			return json.has("user");
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
			return json.has("JVMPath")?json.getString("JVMPath"):"";
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
			return json.has("MinecraftPath")?json.getString("MinecraftPath"):"";
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
			return json.has("SelectedLang")?json.getString("SelectedLang"):"";
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
			return json.has("LastVersion")?json.getString("LastVersion"):"";
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
			json.put("JVMPath",Status.Launcher.JVMPath);//JVMPath);
			json.put("MinecraftPath",Status.Launcher.minecraftDir);//MinecraftPath);
			json.put("SelectedLang",SelectedLang);
			json.put("LastVersion",Status.Launcher.LastVersion);
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
	private int JobCount=0;
	private int FinishedJobs=0;
	private int RunningJobs=0;
	//private MainFrame FrameObj;
	private String Type;
	private String JobName;
	private ThreadJob ThisJob;
	private int o0o=0;
	private int o0oDelay=0;
	/*public static enum ThreadJobType{
		Download,Extract
	}*/
	
	public void DownloadJob(String _jn,DownloadUtil Do){
		Status.Busy=true;
		this.Type="Download";
		this.JobCount=1;
		this.FinishedJobs=0;
		Status.Theme.MainProgressBar.setIndeterminate(true);
		Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("Downloading"));
		Status.Theme.MainProgressBar.setMaximum(0);
		//RFInfo.Theme.MainProgressBar.setValue(0);
		new DownloadThread(Do.DownloadPath,Do.FilePath,this,"Td1").start();
		while(this.FinishedJobs!=1){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("--*DownloadThreadJobError*--");
				e.printStackTrace();
			}
		}
		Status.Theme.MainProgressBar.setIndeterminate(false);
		Status.Busy=false;
		return;
	}
	/**
	 * 開始多執行緒下載
	 * @param _JobName 工做名稱
	 * @param DownloadBaseURL 下載物件
	 */
	public void DownloadJob(String _JobName,ArrayList<DownloadUtil> DownloadObjects){DownloadJob(_JobName,DownloadObjects.size(),DownloadObjects);}
	public void DownloadJob(String _JobName,int MaxThreads,ArrayList<DownloadUtil> DownloadObjects){
		Status.Busy=true;
		this.ThisJob=this;
		final ArrayList<DownloadUtil> NeedDownloads=new ArrayList<>();
		for(int i=0;i<=DownloadObjects.size()-1;i++/*int i=DownloadObjects.size()-1;i>=0;i--*/){
			DownloadUtil OneDownloadJob=DownloadObjects.get(i);
			if(!new File(OneDownloadJob.FilePath.replace("-universal","")).isFile()){NeedDownloads.add(OneDownloadJob);}
		}
		if(NeedDownloads.size()==1){
			DownloadJob(_JobName,NeedDownloads.get(0));
			return;
		}
		this.Type="Download";
		this.JobCount=NeedDownloads.size();
		this.JobName=_JobName;
		if(MaxThreads>this.JobCount) MaxThreads=this.JobCount;
		Status.Theme.MainProgressBar.setMaximum(this.JobCount);
		if(this.JobCount>1){
			System.out.print("\n---Starting job:["+this.JobName+"][Download]");
			Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("Downloading")+"("+this.FinishedJobs+"/"+this.JobCount+")");
			System.out.println(" max thread:"+MaxThreads);
			
			final int _MaxThreads=MaxThreads/2;
		
			new Thread(){ //DownloadHub1 (0~[1/2-1])
				@Override
				public void run(){
					Thread.currentThread().setName("DownloadHub1");
					for(int i=0;i<=(JobCount/2)-1;i++){
						DownloadUtil OneDownloadJob=NeedDownloads.get(i);
						if(RunningJobs>=_MaxThreads){
							try {
								while(RunningJobs>_MaxThreads/2){
									/*if(o0oDelay==0){
										String Showo0o=(o0o==0?"0":"o")+(o0o==1?"0":"o")+(o0o==2?"0":"o");
										RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("Downloading")+" ("+FinishedJobs+"/"+JobCount+")"+Showo0o);
										o0o=(o0o+1)%3;
									}
									o0oDelay=(o0oDelay+1)%80;*/
									Thread.sleep(1);
								}
								Thread.sleep(100);
							} catch (InterruptedException e) {
								System.out.println("--*DownloadThreadJobSleepError*--");
								e.printStackTrace();
							}
						}
						new DownloadThread(OneDownloadJob.DownloadPath,OneDownloadJob.FilePath,ThisJob,"Td"+i).start();
						RegisterThread();						
					}
				}
			}.start();
			
			if(JobCount>=2)
			new Thread(){ //DownloadHub2 (1/2~1)
				@Override
				public void run(){
					Thread.currentThread().setName("DownloadHub2");
					for(int j=JobCount/2;j<=JobCount-1;j++){
						DownloadUtil OneDownloadJob=NeedDownloads.get(j);
						if(RunningJobs>=_MaxThreads){
							try {
								while(RunningJobs>_MaxThreads/2){
									/*if(o0oDelay==0){
										String Showo0o=(o0o==0?"0":"o")+(o0o==1?"0":"o")+(o0o==2?"0":"o");
										RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("Downloading")+" ("+FinishedJobs+"/"+JobCount+")"+Showo0o);
										o0o=(o0o+1)%3;
									}
									o0oDelay=(o0oDelay+1)%80;*/
									Thread.sleep(1);
								}
								Thread.sleep(100);
							} catch (InterruptedException e) {
								System.out.println("--*DownloadThreadJobSleepError*--");
								e.printStackTrace();
							}
						}
						new DownloadThread(OneDownloadJob.DownloadPath,OneDownloadJob.FilePath,ThisJob,"Td"+j).start();
						RegisterThread();
						
					}
				}
			}.start();
			while(this.FinishedJobs!=this.JobCount){
				if(o0oDelay==0){
					String Showo0o=(o0o==0?"0":"o")+(o0o==1?"0":"o")+(o0o==2?"0":"o");
					Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("Downloading")+" ("+this.FinishedJobs+"/"+this.JobCount+")"+Showo0o);
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
			System.out.println("---Finished job:["+this.JobName+"][Download]");
		}
		Status.Busy=false;
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
		this.Type="Extract";
		this.JobCount=ExtractFile.size();
		this.JobName=_JobName;
		Status.Theme.MainProgressBar.setMaximum(this.JobCount);
		System.out.print("\n---Starting job:[Extract]");
		Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("LoadingLib")+"("+this.FinishedJobs+"/"+this.JobCount+")");
		System.out.println(" max thread:"+this.JobCount);
		for(int i=0;i<this.JobCount;i++){
			new ExtractThread(ExtractFile.get(i),TargetDir.get(i),exclude.get(i),this).start();
		}
		while(this.FinishedJobs!=this.JobCount){
			if(o0oDelay==0){
				String Showo0o=(o0o==0?"0":"o")+(o0o==1?"0":"o")+(o0o==2?"0":"o");
				Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("LoadingLib")+" ("+this.FinishedJobs+"/"+this.JobCount+")"+Showo0o);
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
		System.out.println("---Finished job:["+this.JobName+"][Extract]");
		return;
	}
	private synchronized void RegisterThread(){
		this.RunningJobs++;
	}
	/**
	 * 單一執行緒結束工作時,所觸發的方法
	 */
	public synchronized void FinishJob(){
		this.FinishedJobs++;
		this.RunningJobs--;
		Status.Theme.MainProgressBar.setValue(FinishedJobs); //完成進度
		String Showo0o=(o0o==0?"0":"o")+(o0o==1?"0":"o")+(o0o==2?"0":"o");
		if(this.Type.equals("Download")){
			Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("Downloading")+" ("+this.FinishedJobs+"/"+this.JobCount+")"+Showo0o);
		}else{
			Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("LoadingLib")+" ("+this.FinishedJobs+"/"+this.JobCount+")"+Showo0o);
		}
	}
}

class DownloadUtil{
	public String DownloadPath;
	//public String DownloadServer;
	//public String FileRootDir;
	public String FilePath;
	public DownloadUtil(){}
	public DownloadUtil(String _DownloadPath,String _FilePath){
		this.DownloadPath=_DownloadPath;
		//this.DownloadServer=Ds;
		//this.FileRootDir=Frd;
		this.FilePath=_FilePath;
	}
}
/**
 * 多執行緒下載實作類別
 * @author john
 *
 */
class DownloadThread extends Thread{
	private String FileURL="";
	private ThreadJob JobTable;
	private String DownloadURL="";
	private String ThreadName="";
	//private String RootDir;
	
	/**
	 * 單一執行緒工作設定
	 * @param DownloadHost 下載主機
	 * @param DownloadURL 下載路徑
	 * @param rootjob 工作分配器
	 */
	public DownloadThread(String _DownloadURL,String _FileURL,ThreadJob rootjob,String _ThreadName){
		this.FileURL=_FileURL;
		this.JobTable=rootjob;
		this.DownloadURL=_DownloadURL;
		this.ThreadName=_ThreadName;
		//this.RootDir=rootDir;
	}
	public static HttpURLConnection getConnectObj(String targetURL) throws IOException{
		URL targetUrlObj=new URL(targetURL);
		URLConnection urlc = targetUrlObj.openConnection();
		urlc.setConnectTimeout(5000);
		HttpURLConnection ConnectObj = (HttpURLConnection) urlc;
		ConnectObj.addRequestProperty("User-Agent", "Mozilla/5.0");
		ConnectObj.connect();
		return ConnectObj;
	}
	@Override
	public void run(){
		Thread.currentThread().setName(this.ThreadName);
		//String downloadURL=this.DownloadBase+FileURL.replace("\\","/"); //replace是給windows用的
		//String LibrariesDir=RFInfo.Launcher.minecraftDir+"libraries"+RFInfo.Launcher.SplitChar;
		/*String[] LibrariesPathTemp=this.FileURL.split(RFInfo.Launcher.SplitChar.equals("\\")?"\\\\":RFInfo.Launcher.SplitChar); //windows要特別加"\\"
		String LibrariesPath="";
		for(int i=0;i<LibrariesPathTemp.length-1;i++){
			LibrariesPath += LibrariesPathTemp[i]+RFInfo.Launcher.SplitChar;
		}
		System.out.println(this.FileURL+" , "+LibrariesPath);*/
		
		if(this.FileURL.lastIndexOf(File.separatorChar)>0){
			String TargetDir=this.FileURL.substring(0,this.FileURL.lastIndexOf(File.separatorChar));
			//System.out.println("Starting make dir: "+TargetDir);
			new File(TargetDir).mkdirs();
		}
		System.out.println("Starting download: "+this.FileURL.substring(this.FileURL.lastIndexOf(".minecraft")));
		try{
			if(!new File(FileURL).isFile()){
				URL DownloadUrlObj = new URL(this.DownloadURL);
				URLConnection urlc = DownloadUrlObj.openConnection();
				urlc.setConnectTimeout(5000);
				HttpURLConnection ConnectObj = (HttpURLConnection) urlc;
				ConnectObj.addRequestProperty("User-Agent", "Mozilla/5.0");
				ConnectObj.connect();
				BufferedInputStream DownloadStream = new BufferedInputStream(ConnectObj.getInputStream());
				if(DownloadURL.matches(".*\\.pack\\.xz")){
					unpackLibrary(new File(FileURL),readFully(DownloadStream));
				}else{
					new File(FileURL).createNewFile();
					BufferedOutputStream WriteStream = new BufferedOutputStream(new FileOutputStream(FileURL));
					WriteStream.write(readFully(DownloadStream));
					/*byte[] data = new byte[1024]; //緩衝區大小1024byte
					int len = DownloadStream.read(data); //讀檔
					while(len > 0) {
						WriteStream.write(data,0,len);
						len = DownloadStream.read(data);
					}*/
					WriteStream.flush();
					DownloadStream.close();
					WriteStream.close();
				}
			}
			System.out.println("Finish download: "+this.FileURL.substring(this.FileURL.lastIndexOf(".minecraft")));
		}catch(IOException e){
			System.out.println("--*Download Error( "+FileURL+" )*--");
			e.printStackTrace();
			
		}
		this.JobTable.FinishJob();
	}
	//------START 從ForgeInstaller複製過來的方法------
	public static void unpackLibrary(File output, byte[] data) throws IOException {
		byte[] decompressed = readFully(new XZInputStream(new ByteArrayInputStream(data)));
		System.out.println("Unpacking: "+output.getName()+".pack.xz");
		
		String end = new String(decompressed, decompressed.length - 4, 4);
		if (!end.equals("SIGN")){
			System.out.println("--*Unpacking failed, signature missing " + end+"*--");
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
		System.out.println("Finish unpack: "+output.getName()+".pack.xz => "+output.getName());
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
	public static byte[] readFully(ZipInputStream stream) throws IOException{
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
    //private Object[] Exclude;
    private HashSet<String> Exclude;
    private ThreadJob JobTable;
    /**
     * 單一執行續工作設定
     * @param inputfile 原始檔案
     * @param targetDir 目標資料夾
     * @param exclude 排除的檔案
     * @param rootjob 工作分配器
     */
	public ExtractThread(String inputfile,String targetDir,ArrayList<String> exclude,ThreadJob rootjob){
		this.InputFile=inputfile;
		this.TargetDir=targetDir;
		this.Exclude=new HashSet<String>(exclude);
		this.JobTable=rootjob;
	}
	@Override
	public void run(){
		if(!new File(this.TargetDir).isDirectory()){
			new File(this.TargetDir).mkdirs();
		}/*
		FileOutputStream fileOut; 
        File file; 
        InputStream inputStream; 
		Arrays.sort(this.Exclude);*/
        try{
        	ZipInputStream ZipInput = new ZipInputStream(new FileInputStream(new File(this.InputFile)));
        	ZipEntry OneEntry = ZipInput.getNextEntry();
        	
        	while(OneEntry!=null){
        		String TargetFileName=OneEntry.getName();
        		if(this.Exclude.contains(TargetFileName)||TargetFileName.matches(".*/.*")){
        			System.out.println("Skip: "+TargetFileName);
        			OneEntry=ZipInput.getNextEntry();
        			continue;
        		}
        		System.out.println("Extracting: "+TargetFileName);
        		if(!new File(this.TargetDir+File.separatorChar+TargetFileName).isFile())
        			new File(this.TargetDir+File.separatorChar+TargetFileName).createNewFile();
        		FileOutputStream TargetFileStream=new FileOutputStream(new File(this.TargetDir+File.separatorChar+TargetFileName));
        		TargetFileStream.write(DownloadThread.readFully(ZipInput));
        		TargetFileStream.close();
        		OneEntry=ZipInput.getNextEntry();
        	}
        	ZipInput.closeEntry();
        	ZipInput.close();
        	
        	/*
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
                    fileOut.write(DownloadThread.readFully(inputStream));
                    /*byte[] buf=new byte[1024];
                    int len=inputStream.read(buf);
                    while(len > 0){ 
                        fileOut.write(buf,0,len);
                        len=inputStream.read(buf);
                    } *
                    fileOut.close();

                    inputStream.close(); 
                }    
            } 
            zipFile.close(); */
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
			InputStreamReader resules = new InputStreamReader(
					DownloadThread.getConnectObj(LoginServer+"?user="+user+"&password="+pwd+"&version="+(Status.RFMCLB_InfoJSON!=null?Status.RFMCLB_InfoJSON.getInt("LoginVersion"):"14")).getInputStream(),"UTF-8");
			int data = resules.read();
			while(data != -1){
				Result += (char) data;
				data = resules.read();
			}
		} catch (IOException e) {
			System.out.println("--*Check user Error*---");
			e.printStackTrace();
			Result="Server Error";
			return false;
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
		try {
			CanConnect = /*ConnectObj.getResponseCode()==HttpURLConnection.HTTP_OK;*/
					JSONFunction.CreateFromStream(
							DownloadThread.getConnectObj("http://status.mojang.com/check?service=login.minecraft.net").getInputStream())
							.getString("login.minecraft.net").equals("green");
		} catch (IOException e) {
			System.out.println("--*Connect to Internet Error");
			e.printStackTrace();
		}
	}
	
	/**
	 * 檢查連線(快速)
	 * @return 是否可連線
	 */
	public static boolean canConnect(){
		return CanConnect;
	}
}