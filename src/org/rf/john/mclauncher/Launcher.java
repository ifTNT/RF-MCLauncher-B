package org.rf.john.mclauncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rf.john.mclauncher.langs.Languages;
import org.rf.john.mclauncher.RFInfo;


public class Launcher{
	public String minecraftDir;
	public String JVMPath;
	public String LastVersion="";
	protected static String SplitChar;
	protected String LibrariesBaseDownloadURL="https://libraries.minecraft.net/";
	
	public int PrintLauncherVersion(){
		//System.out.println(this);
		return 1;
	}
	
	public Launcher(){
		SplitChar=System.getProperty("file.separator");
		if(RFInfo.getOS().equals("linux")){
			minecraftDir= System.getProperty("user.home")+"/.minecraft/";
			JVMPath = System.getProperty("java.home")+"/bin/java";
			
		}else if(RFInfo.getOS().equals("windows")){
			minecraftDir= (((System.getenv("APPDATA")!=null)?System.getenv("APPDATA"):System.getProperty("user.home", "."))+"\\.minecraft\\");
			JVMPath = "\""+System.getProperty("java.home")+"\\bin\\"+(new File("\""+System.getProperty("java.home")+"\\bin\\javaw.exe\"").isFile()?"javaw.exe":"java")+"\"";
		}else if(RFInfo.getOS().equals("unknow")){
			//new JOptionPane().showMessageDialog(null,"<html><span style=\"color: RED;\">"+Lang.getString("NotSupportSystem")+"</span></html>",Lang.getString("Error"),JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * 初始化啟動器
	 * @param Lang 語系
	 */
	@SuppressWarnings("static-access")
	public Launcher(Languages Lang){
		SplitChar=System.getProperty("file.separator");
		if(RFInfo.getOS().equals("linux")){
			minecraftDir= System.getProperty("user.home")+"/.minecraft/";
			JVMPath = System.getProperty("java.home")+"/bin/java";
			
		}else if(RFInfo.getOS().equals("windows")){
			minecraftDir= (((System.getenv("APPDATA")!=null)?System.getenv("APPDATA"):System.getProperty("user.home", "."))+"\\.minecraft\\");
			JVMPath = "\""+System.getProperty("java.home")+"\\bin\\"+(new File("\""+System.getProperty("java.home")+"\\bin\\javaw.exe\"").isFile()?"javaw.exe":"java")+"\"";
		}else if(RFInfo.getOS().equals("unknow")){
			new JOptionPane().showMessageDialog(null,"<html><span style=\"color: RED;\">"+Lang.getString("NotSupportSystem")+"</span></html>",Lang.getString("Error"),JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	/**
	 * 取得已定義的啟動檔
	 * @return 啟動檔
	 */
	@SuppressWarnings("static-access")
	public final ArrayList<String> getInstalledProfiles(){
		ArrayList<String> FinallyOut = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(minecraftDir+"launcher_profiles.json"));
			String input;
			String jsonTxt="";
			do{
				input = in.readLine();
				if(input==null){
					break;
				}
				jsonTxt += input;
			}while(true);
			JSONObject launcher_profile = new JSONObject(jsonTxt);
			JSONObject InstalledProfiles = launcher_profile.getJSONObject("profiles");
			for(String OneProfileName:new JSONObject().getNames(InstalledProfiles)){
				FinallyOut.add(OneProfileName);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return FinallyOut;
	}
	
	/**
	 * 取的已選擇的啟動檔
	 * @return 已選擇的啟動檔
	 */
	public final String selectedProfile(){
		String FinallyOut = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(minecraftDir+"launcher_profiles.json"));
			String input;
			String jsonTxt="";
			do{
				input = in.readLine();
				if(input==null){
					break;
				}
				jsonTxt += input;
			}while(true);
			JSONObject launcher_profile = new JSONObject(jsonTxt);
			FinallyOut = launcher_profile.getString("selectedProfile");
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return FinallyOut;
	}
	
	/**
	 * 設定Minecraft Last Version
	 */
	public final void SetLastVersion(){
		this.LastVersion=(RFInfo.RFMCLB_InfoJSON!=null?RFInfo.RFMCLB_InfoJSON.getString("MinecraftLastVersion"):"");
		if(this.LastVersion.equals("")){ //從設定檔讀取
			this.LastVersion=new Save_LoadConfig().getLastVersion();
		}
		if(this.LastVersion.equals("")){
			this.LastVersion="1.7.2"; //預設值
		}
		/*try {
			URL UpdateURL = new URL("http://rf_mclauncher_b.000space.com/RFMCLB_Info.json"); //從網站讀取
			URLConnection urlc = UpdateURL.openConnection();
			urlc.setConnectTimeout(5000);
			HttpURLConnection ConnectObj = (HttpURLConnection) urlc;
			ConnectObj.addRequestProperty("User-Agent", "Mozilla/5.0");
			ConnectObj.connect();
			BufferedInputStream InputStream = new BufferedInputStream(ConnectObj.getInputStream());
			String UpdateInfo="";
			byte[] buf=new byte[1024];
            int len=InputStream.read(buf);
            while(len > 0){ 
            	UpdateInfo = UpdateInfo+new String(buf);
                len=InputStream.read(buf);
            }
			InputStream.close();
			if(!(UpdateInfo.equals("") || !JSONFunction.Search(new JSONObject(UpdateInfo),"MinecraftLastVersion"))){
				this.LastVersion=new JSONObject(UpdateInfo).getString("MinecraftLastVersion");
			}
			if(this.LastVersion.equals("")){ //從設定檔讀取
				this.LastVersion=new Save_LoadConfig().getLastVersion();
			}
			if(this.LastVersion.equals("")){
				this.LastVersion="1.7.4"; //預設值
			}
			/*
			URL UpdateURL = new URL("http://mcupdate.tumblr.com/"); //從網站讀取
			URLConnection urlc = UpdateURL.openConnection();
			urlc.setConnectTimeout(5000);
			HttpURLConnection ConnectObj = (HttpURLConnection) urlc;
			ConnectObj.addRequestProperty("User-Agent", "Mozilla/5.0");
			ConnectObj.connect();
			BufferedInputStream InputStream = new BufferedInputStream(ConnectObj.getInputStream());
			String UpdatePage="";
			byte[] buf=new byte[1024];
            int len=InputStream.read(buf);
            while(len > 0){ 
                UpdatePage = UpdatePage+new String(buf);
                len=InputStream.read(buf);
            }
			InputStream.close();
			Pattern Regex=Pattern.compile(".*<h3><a href=\"http://mcupdate\\.tumblr\\.com/post/[0-9]+/minecraft[-0-9]+\">minecraft[ 0-9.]+</a></h3>.*");
			Matcher matcher=Regex.matcher(UpdatePage.toLowerCase());
			if(matcher.find()){
				String LastVersionHtml=matcher.group().trim();
				Pattern VersionRegex=Pattern.compile("minecraft [0-9.]+");
				Matcher VersionMatcher = VersionRegex.matcher(LastVersionHtml);
				if(VersionMatcher.find()){
					this.LastVersion=VersionMatcher.group().substring(10);
				}
			}
			if(this.LastVersion.equals("")){ //從設定檔讀取
				this.LastVersion=new Save_LoadConfig().getLastVersion();
			}
			if(this.LastVersion.equals("")){
				this.LastVersion="1.7.2"; //預設值
			}
			*
		} catch (IOException e) {
			System.out.println("--*GetLastVersionError*--");
			e.printStackTrace();
		}*/
	}
	
	/**
	 * 離線啟動遊戲
	 * @param ProfileName 啟動檔名稱
	 */
	public void LaunchGameOffline(String ProfileName){
		try {
			System.out.println("--Launching Offline!");
			JSONObject launcher_profile = JSONFunction.CreateFromFile(minecraftDir+"launcher_profiles.json");
			String ProfileSession = ("token:"+launcher_profile.getJSONObject("authenticationDatabase").getJSONObject(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("playerUUID")).getString("accessToken")+":"+launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("playerUUID"));
			String PlayerName = launcher_profile.getJSONObject("authenticationDatabase").getJSONObject(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("playerUUID")).getString("displayName");
			this.LaunchGame(PlayerName,ProfileName, ProfileSession);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 啟動遊戲
	 * @param PlayerName 玩家名稱
	 * @param ProfileName 啟動檔名稱
	 * @param session 玩家的Session
	 */
	@SuppressWarnings("static-access")
	public void LaunchGame(String PlayerName,String ProfileName,String session){
		//String VersionName="";
		String GameDir="";
		String AssetsDir=minecraftDir+"assets";
		String MainClass="";
		String MainJar="";
		String LaunchCmd="";
		String NativesDir="";
		String LaunchArgs="";
		String Version="";
		String AssetsIndex="";
		boolean NeedDownload=false;
		String LibrariesDir=minecraftDir+"libraries"+SplitChar;
		System.out.println("\n--Launching Game...");
		LaunchCmd += this.JVMPath;
		System.out.println("Free Memory: "+Runtime.getRuntime().totalMemory()+" Bytes");
		try {
			//-------------讀取必要資訊--------------
			JSONObject launcher_profile = JSONFunction.CreateFromFile(minecraftDir+"launcher_profiles.json");
			if(!launcher_profile.getString("selectedProfile").equals(ProfileName) && !Login.canConnect()){
				RFInfo.Theme.LoginBtn.setEnabled(true);
				RFInfo.Theme.UserBox.setEnabled(true);
				RFInfo.Theme.PwdBox.setEnabled(true);
				RFInfo.Theme.ProfileSelectBox.setEnabled(true);
				RFInfo.Theme.RememberMe.setEnabled(true);
				RFInfo.Theme.MainProgressBar.setIndeterminate(false);
				RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("OfflineDownload"));
				RFInfo.Theme.MainProgressBar.setEnabled(false);
				new JOptionPane().showMessageDialog(null,"<html><span style=\"color: RED;\">"+RFInfo.SelectedLang.getString("OfflineDownload")+"</span></html>",RFInfo.SelectedLang.getString("Error"),JOptionPane.ERROR_MESSAGE);
				return;
			}
			LaunchCmd += " "+(JSONFunction.Search(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName),"javaArgs")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("javaArgs"):"-Xmx"+(System.getProperty("sun.arch.data.model").equals("32")?"512M":"1G"));
			GameDir = JSONFunction.Search(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName),"gameDir")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("gameDir"):minecraftDir;
			String LastVersionID = JSONFunction.Search(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName),"lastVersionId")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("lastVersionId"):this.LastVersion;
			NativesDir = (minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+"-natives-" + System.nanoTime());
			NeedDownload = !launcher_profile.getString("selectedProfile").equals(ProfileName) && Login.canConnect();
			MainJar = minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".jar";
			Version=LastVersionID;
			launcher_profile.put("selectedProfile",ProfileName);
			JSONFunction.WriteToFile(launcher_profile,minecraftDir+"launcher_profiles.json");
			System.out.println("GameDir="+GameDir);
			System.out.println("PlayerDisplayName="+PlayerName);
			System.out.println("Session=******");
			System.out.println("BaseVersion="+Version);
			JSONObject LaunchFile = JSONFunction.CreateFromFile(minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".json");
			MainClass = LaunchFile.getString("mainClass");
			LaunchArgs = LaunchFile.getString("minecraftArguments");
			AssetsIndex=(JSONFunction.Search(LaunchFile,"assets")?LaunchFile.getString("assets"):"");
			JSONArray Libraries = LaunchFile.getJSONArray("libraries");
			
			
			//---------------------刪除,重下載程式庫---------------
			
			String NativesTail="-"+(RFInfo.getOS().equals("linux")?"natives-linux":"natives-windows-"+System.getProperty("os.arch"));
			
			if(NeedDownload){
				RFInfo.Theme.MainProgressBar.setIndeterminate(true);
				//RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("DeletingLib"));
				//System.out.println("Deleting libaries...");
				//System.out.println(deleteDir(new File(LibrariesDir))?"Delete success!":"Delete unsuccess!");
				new File(LibrariesDir).mkdir();
				//System.out.println("Deleted libraries!");
				RFInfo.Theme.MainProgressBar.setIndeterminate(false);
				System.out.println("Downloading libraries");
				RFInfo.Theme.MainProgressBar.setMaximum(Libraries.length());
				RFInfo.Theme.MainProgressBar.setValue(0);
				HashMap<String,String> DownloadJobs=new HashMap<>();
				for(int i=0;i<Libraries.length();i++){
					String LibrariesPath = (Libraries.getJSONObject(i).getString("name"));
					String[] temp = LibrariesPath.split(":");
					LibrariesPath = temp[0].replace(".", SplitChar);
					for(int j=1;j<temp.length;j++){
						LibrariesPath += (SplitChar+temp[j]);
					}
					if(CheckLibPermission(Libraries.getJSONObject(i))){
						String FileURL="";
						//String downloadURL="";
						FileURL = (LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1]);
						FileURL += (JSONFunction.Search(Libraries.getJSONObject(i), "natives")?NativesTail:"");
						FileURL += (temp[temp.length-2].matches("[a-zA-z]*forge.*")?"-universal":"")+".jar"; //For Forge
						FileURL += (temp[temp.length-2].matches("scala-[a-zA-z0-9.]+")?".pack.xz":"");
						//http://repo1.maven.org/maven2/org/scala-lang/scala-library/2.10.2/scala-library-2.10.2.jar
						System.out.println("-- "+FileURL);
						DownloadJobs.put(FileURL,JSONFunction.Search(Libraries.getJSONObject(i),"url")?Libraries.getJSONObject(i).getString("url"):this.LibrariesBaseDownloadURL);
						//Key=DownloadURL(FileURL),Value=DownloadHost
						}else{
						System.out.println("Skip download: "+LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?NativesTail:"")+".jar");
					}
				}
				new ThreadJob().DownloadJob("Libraries",DownloadJobs);
			}
			
			
			//------------------解壓,載入程式庫----------------
			File ExtractDir = new File(NativesDir+SplitChar);
			RFInfo.Theme.MainProgressBar.setValue(0);
			RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("LoadingLib"));
			RFInfo.Theme.MainProgressBar.setIndeterminate(true);
			if(!ExtractDir.exists() || !ExtractDir.isDirectory()){
				makeDir(NativesDir+SplitChar+(RFInfo.getOS().equals("windows")?"\\":""));
				System.out.println("-Natives dir was created!");
			}
			LaunchCmd += " -Djava.library.path="+NativesDir;
			LaunchCmd += " -cp ";
			ArrayList<String> extractFiles = new ArrayList<String>();
			ArrayList<String> extractTargetDir = new ArrayList<String>();
			ArrayList<ArrayList<String>> extractExclude = new ArrayList<ArrayList<String>>();
			RFInfo.Theme.MainProgressBar.setMaximum(Libraries.length());
			System.out.println("\n----------");
			for(int i=0;i<Libraries.length();i++){
				if(CheckLibPermission(Libraries.getJSONObject(i))){
					String LibrariesPath = (Libraries.getJSONObject(i).getString("name"));
					String[] temp = LibrariesPath.split(":");
					LibrariesPath = temp[0].replace(".", SplitChar);
					for(int j=1;j<temp.length;j++){
						LibrariesPath += (SplitChar+temp[j]);
					}
					//String[] SortedArray = new JSONObject().getNames(Libraries.getJSONObject(i));
					//Arrays.sort(SortedArray);
					if(JSONFunction.Search(Libraries.getJSONObject(i), "extract")){
						//String[] FileList = (new File(minecraftDir+"libraries"+SplitChar+LibrariesPath).list());
						//System.out.println(FileList[0]);
						System.out.println("This library will be extracted: "+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?NativesTail:"")+".jar");
						extractFiles.add(LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?NativesTail:"")+".jar");
						extractTargetDir.add(NativesDir+SplitChar);
						ArrayList<String> excludeTemp=new ArrayList<String>();
						for(String OneExclude:JSONFunction.readArray(Libraries.getJSONObject(i).getJSONObject("extract").getJSONArray("exclude"))){
							excludeTemp.add(OneExclude);
						}
						extractExclude.add(excludeTemp);
					}else{
						System.out.println("Loading library: "+LibrariesPath+SplitChar+(temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?NativesTail:"")+".jar");
						LaunchCmd += LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?NativesTail:"")+".jar"+System.getProperty("path.separator");
					}
				}
			}
			System.out.println("----------");
			new ThreadJob().ExtractJob("Libraries",extractFiles,extractTargetDir,extractExclude);
			LaunchCmd += MainJar+" "+MainClass;
		} catch (IOException e) {
			System.out.println("Launch Error!");
			e.printStackTrace();
		}
		
		RFInfo.Theme.MainProgressBar.setString(RFInfo.SelectedLang.getString("LaunchingGame"));
		RFInfo.Theme.MainProgressBar.setIndeterminate(false);
		RFInfo.Theme.MainProgressBar.setEnabled(false);
		LaunchArgs=LaunchArgs.replace("${auth_player_name}",PlayerName);
		LaunchArgs=LaunchArgs.replace("${auth_uuid}",session.split(":")[2]);
		LaunchArgs=LaunchArgs.replace("${auth_access_token}",session.split(":")[1]);
		LaunchArgs=LaunchArgs.replace("${auth_session}",session);
		LaunchArgs=LaunchArgs.replace("${game_directory}",GameDir);
		LaunchArgs=LaunchArgs.replace("${game_assets}",AssetsDir);
		LaunchArgs=LaunchArgs.replace("${version_name}",Version);
		LaunchArgs=LaunchArgs.replace("${assets_root}",AssetsDir);
		LaunchArgs=LaunchArgs.replace("${assets_index_name}",AssetsIndex);
		LaunchArgs=LaunchArgs.replace("${user_properties}","{}");
		LaunchArgs=LaunchArgs.replace("${user_type}","mojang");
		LaunchCmd += " "+LaunchArgs;
		try {
			System.out.println("\nRunning Command: "+LaunchCmd);
			System.out.println("--Launch Finished!");
			TimeCounter.stop();
			TimeCounter.count();
			RFInfo.MainFrameObj.setVisible(false);
			Process LaunchProcess = Runtime.getRuntime().exec(LaunchCmd);
			BufferedReader p_in = new BufferedReader(new InputStreamReader(LaunchProcess.getInputStream(),"UTF-8"));
            String line="";
            try{
            	LaunchProcess.exitValue();
            	System.out.println("Launch Over Return:"+LaunchProcess.exitValue());
            }catch(IllegalThreadStateException e){
            	System.out.println("\nGame is Running");
            }
            while((line = p_in.readLine()) != null){
            	System.out.println("【Minecraft Console】> "+line);
            	if(line.matches("^\\W{3,}Game crashed! Crash report saved to: \\W{3,}.*")){
            		new JOptionPane().showMessageDialog(null,"<html><span style=\"color: red;\">"+RFInfo.SelectedLang.getString("GameError")+"</span></html>",RFInfo.SelectedLang.getString("Error"),JOptionPane.ERROR_MESSAGE);
            		break;
            	}
            }
            p_in.close();
            if(!RFInfo.TestArguments("--NoDelNatives")){
            	deleteDir(new File(NativesDir+SplitChar));
            	System.out.println("Deleted Natives Dir!");
            }
            RFInfo.MainFrameObj.dispose(); //結束程式
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 製作資料夾
	 * @param dir 欲製作的資料夾
	 * @param splitChar 路徑分隔符號
	 * @return 是否成功
	 */
	public synchronized final boolean makeDir(String dir){
		String splitChar=SplitChar;
		String[] Dirs = dir.split(splitChar.equals("\\")?"\\\\":splitChar);
		String temp="";
		System.out.println("Creating Dir: "+dir);
		for(String OneDir:Dirs){
			temp += OneDir+SplitChar;
			if(!new File(temp).isDirectory())
				if(!new File(temp).mkdir()){
					System.out.println("Creating Dir Error: "+temp);
					return false;
				}
		}
		return true;
	}
	
	/**
	 * 刪除資料夾
	 * @param dir 欲刪除的資料夾
	 * @return 是否成功
	 */
	public static final boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			for (File OneFile:dir.listFiles()) { 
				if (!deleteDir(OneFile))
					return false;
			}
		}
		dir.delete();
		return true;
	}
	
	/**
	 * 檢查程式庫是否允許在這個作業系統上下載/解壓縮
	 * @param in 程式庫的JSONObject
	 * @return 是否允許
	 */
	public boolean CheckLibPermission(JSONObject in){
		boolean FinalAction=false;
		if(JSONFunction.Search(in,"rules")){
			JSONArray Rules=in.getJSONArray("rules");
			for(int i=0;i<Rules.length();i++){
				JSONObject Action=Rules.getJSONObject(i);
				if(Action.getString("action").equals("allow")){ //allow
					if(JSONFunction.Search(Action,"os")){
						if(Action.getJSONObject("os").getString("name").equals(RFInfo.getOS())){
							FinalAction=true;
						}
					}else{
						FinalAction=true;
					}
				}else{ //disallow
					if(JSONFunction.Search(Action,"os")){
						if(Action.getJSONObject("os").getString("name").equals(RFInfo.getOS())){
							FinalAction=false;
						}
					}else{
						FinalAction=false;
					}
				}
			}
		}else{
			FinalAction=true;
		}
		return FinalAction;
		/*
		if(JSONFunction.Search(in,"rules")){
			JSONArray Rules=in.getJSONArray("rules");
			if(Rules.length()==1){
				if(JSONFunction.getItem(Rules,0).getString("action").equals("allow")){ //allow
					if(JSONFunction.Search(JSONFunction.getItem(Rules,0),"os")){
						if(JSONFunction.getItem(Rules,0).getJSONObject("os").getString("name").equals(System.getProperty("os.name"))){
							return true; //allow:true;
						}else{
							return false;
						}
					}else{ //allow only
						return true;
					}
				}else{ //disallow
					if(JSONFunction.Search(JSONFunction.getItem(Rules,0),"os")){
						if(JSONFunction.getItem(Rules,0).getJSONObject("os").getString("name").equals(System.getProperty("os.name"))){
							return false; //disallow:true
						}else{
							return true;
						}
					}else{ //disallow only
						return false;
					}
				}
			}else if(in.getJSONArray("rules").length()==2){
				JSONObject AllowObj=null;
				JSONObject DisallowObj=null;
				if(JSONFunction.getItem(Rules,0).getString("action").equals("allow")&&JSONFunction.getItem(Rules,1).getString("action").equals("disallow")){ //allow disallow
					AllowObj = JSONFunction.getItem(Rules,0);
					DisallowObj = JSONFunction.getItem(Rules,1);
				}else
				if(JSONFunction.getItem(Rules,1).getString("action").equals("allow")&&JSONFunction.getItem(Rules,0).getString("action").equals("disallow")){ //disallow allow
					AllowObj = JSONFunction.getItem(Rules,1);
					DisallowObj = JSONFunction.getItem(Rules,0);
				}
				if((!JSONFunction.Search(AllowObj,"os"))&&(!JSONFunction.Search(DisallowObj,"os"))){
					return true; //allow:null disallow:null
				}
				if((!JSONFunction.Search(AllowObj,"os"))&&(DisallowObj.getJSONObject("os").getString("name").equals(System.getProperty("os.name")))){
					return false; //allow:null disallow:true
				}
				if((!JSONFunction.Search(DisallowObj,"os"))&&(AllowObj.getJSONObject("os").getString("name").equals(System.getProperty("os.name")))){
					return true; //allow:true disallow:null
				}
			}
		}
		return true; //default(no rules)
		*/
	}
}/*
class Launche_linux extends Launcher{{
	SplitChar="/";
	minecraftDir= System.getProperty("user.home")+"/.minecraft/";
	JVMPath = System.getProperty("java.home")+"/bin/java";
}
	@Override
	public void LauncheGame(String PlayerName,String ProfileName,String session){
		String VersionName="";
		String GameDir="";
		String AssetsDir=minecraftDir+"assets";
		String MainClass="";
		String MainJar="";
		String LauncheCmd="";
		String NativesDir="";
		String LauncheArgs="";
		String Version="";
		boolean NeedDownload=false;
		String LibrariesDir=minecraftDir+"libraries"+SplitChar;
		System.out.println("\n--Launching Game...");
		//Long LauncheTime = System.currentTimeMillis();
		LauncheCmd += this.JVMPath;
		System.out.println("Free Memory: "+Runtime.getRuntime().totalMemory());
		try {
			//-------------讀取必要資訊--------------
			JSONObject launcher_profile = JSONFunction.CreateFromFile(minecraftDir+"launcher_profiles.json");
			if(!launcher_profile.getString("selectedProfile").equals(ProfileName) && !Login.canConnect()){
				//System.out.println("dassddasd");
				RFInfo.MainFrameObj.LoginBtm.setEnabled(true);
				RFInfo.MainFrameObj.UserBox.setEnabled(true);
				RFInfo.MainFrameObj.PwdBox.setEnabled(true);
				RFInfo.MainFrameObj.ProfileSelectBox.setEnabled(true);
				RFInfo.MainFrameObj.RememberMe.setEnabled(true);
				RFInfo.MainFrameObj.MainProgressBar.setIndeterminate(false);
				RFInfo.MainFrameObj.MainProgressBar.setString(RFInfo.SelectedLang.getString("OfflineDownload"));
				RFInfo.MainFrameObj.MainProgressBar.setEnabled(false);
				new JOptionPane().showMessageDialog(null,"<html><span style=\"color: RED;\">"+RFInfo.SelectedLang.getString("OfflineDownload")+"</span></html>",RFInfo.SelectedLang.getString("Error"),JOptionPane.ERROR_MESSAGE);
				return;
			}
			LauncheCmd += " "+(JSONFunction.Search(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName),"javaArgs")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("javaArgs"):"-Xmx"+(System.getProperty("sun.arch.data.model").equals("32")?"512M":"1G"));
			GameDir = JSONFunction.Search(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName),"gameDir")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("gameDir"):minecraftDir;
			//PlayerName = launcher_profile.getJSONObject("authenticationDatabase").getJSONObject(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("playerUUID")).getString("displayName");
			//String Token = ("token:"+launcher_profile.getJSONObject("authenticationDatabase").getJSONObject(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("playerUUID")).getString("accessToken")+":"+launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("playerUUID"));
			String LastVersionID = JSONFunction.Search(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName),"lastVersionId")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("lastVersionId"):"1.6.2";
			NativesDir = (minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+"-natives-" + System.nanoTime());
			NeedDownload = !launcher_profile.getString("selectedProfile").equals(ProfileName) && Login.canConnect();
			MainJar = minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".jar";
			Version=LastVersionID;
			launcher_profile.put("selectedProfile",ProfileName);
			JSONFunction.WriteToFile(launcher_profile,minecraftDir+"launcher_profiles.json");
			System.out.println("GameDir="+GameDir);
			System.out.println("PlayerDisplayName="+PlayerName);
			System.out.println("Session=******");
			System.out.println("BaseVersion="+Version);
			JSONObject LauncheFile = JSONFunction.CreateFromFile(minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".json");
			MainClass = LauncheFile.getString("mainClass");
			LauncheArgs = LauncheFile.getString("minecraftArguments");
			JSONArray Libraries = LauncheFile.getJSONArray("libraries");
			
			
			//---------------------刪除,重下載程式庫---------------
			if(NeedDownload){
				RFInfo.MainFrameObj.MainProgressBar.setIndeterminate(true);
				RFInfo.MainFrameObj.MainProgressBar.setString(RFInfo.SelectedLang.getString("DeletingLib"));
				System.out.println("Deleting libaries...");
				System.out.println(FileFunction.deleteDir(new File(LibrariesDir))?"Delete success!":"Delete unsuccess!");
				new File(LibrariesDir).mkdir();
				System.out.println("Deleted libraries!");
				RFInfo.MainFrameObj.MainProgressBar.setIndeterminate(false);
				System.out.println("Downloading libraries");
				RFInfo.MainFrameObj.MainProgressBar.setMaximum(Libraries.length());
				RFInfo.MainFrameObj.MainProgressBar.setValue(0);
				ArrayList<String> DownloadURLs=new ArrayList<String>();
				ArrayList<String> DownloadBases=new ArrayList<String>();
				for(int i=0;i<Libraries.length();i++){
					String LibrariesPath = (Libraries.getJSONObject(i).getString("name"));
					String[] temp = LibrariesPath.split(":");
					LibrariesPath = temp[0].replace(".", SplitChar);
					for(int j=1;j<temp.length;j++){
						LibrariesPath += (SplitChar+temp[j]);
					}
					//this.MainFrameObj.MainProgressBar.setString(this.MainFrameObj.SelectedLang.Downloading+temp[temp.length-2]+(new JSONFounction().Search(Libraries.getJSONObject(i), "natives")?"-natives-linux.jar":".jar")+"-"+temp[temp.length-1]);
					if(CheckLibPermission(Libraries.getJSONObject(i))){
						String FileURL="";
						String downloadURL="";
						FileURL = (LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1]);
						FileURL += (JSONFunction.Search(Libraries.getJSONObject(i), "natives")?"-natives-linux.jar":".jar");
						System.out.println("-- "+FileURL);
						DownloadURLs.add(FileURL);
						DownloadBases.add(JSONFunction.Search(Libraries.getJSONObject(i),"url")?Libraries.getJSONObject(i).getString("url"):this.LibrariesBaseDownloadURL);
					}else{
						System.out.println("Skip download: "+LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?"-natives-linux.jar":".jar"));
					}
				}
				new ThreadJob().DownloadJob(DownloadBases,DownloadURLs);
			}
			
			
			//------------------解壓,載入程式庫----------------
			File ExtractDir = new File(NativesDir+SplitChar);
			RFInfo.MainFrameObj.MainProgressBar.setValue(0);
			RFInfo.MainFrameObj.MainProgressBar.setString(RFInfo.SelectedLang.getString("LoadingLib"));
			RFInfo.MainFrameObj.MainProgressBar.setIndeterminate(true);
			if(!ExtractDir.exists() || !ExtractDir.isDirectory()){
				makeDir(NativesDir+SplitChar,this.SplitChar);
				System.out.println("-Created natives dir!");
			}
			LauncheCmd += " -Djava.library.path="+NativesDir;
			LauncheCmd += " -cp ";
			ArrayList<String> extractFiles = new ArrayList<String>();
			ArrayList<String> extractTargetDir = new ArrayList<String>();
			ArrayList<ArrayList<String>> extractExclude = new ArrayList<ArrayList<String>>();
			RFInfo.MainFrameObj.MainProgressBar.setMaximum(Libraries.length());
			System.out.println("\n----------");
			for(int i=0;i<Libraries.length();i++){
				if(CheckLibPermission(Libraries.getJSONObject(i))){
					String LibrariesPath = (Libraries.getJSONObject(i).getString("name"));
					String[] temp = LibrariesPath.split(":");
					LibrariesPath = temp[0].replace(".", SplitChar);
					for(int j=1;j<temp.length;j++){
						LibrariesPath += (SplitChar+temp[j]);
					}
					//String[] SortedArray = new JSONObject().getNames(Libraries.getJSONObject(i));
					//Arrays.sort(SortedArray);
					if(JSONFunction.Search(Libraries.getJSONObject(i), "extract")){
						String[] FileList = (new File(minecraftDir+"libraries"+SplitChar+LibrariesPath).list());
						//System.out.println(FileList[0]);
						System.out.println("Will Extract library: "+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?"-natives-linux.jar":".jar"));
						//new unZip(LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+(new JSONFounction().Search(Libraries.getJSONObject(i), "natives")?"-natives-linux.jar":".jar"),NativesDir+SplitChar,new JSONFounction().readArray(Libraries.getJSONObject(i).getJSONObject("extract").getJSONArray("exclude")));
						extractFiles.add(LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?"-natives-linux.jar":".jar"));
						extractTargetDir.add(NativesDir+SplitChar);
						ArrayList<String> excludeTemp=new ArrayList<String>();
						for(String OneExclude:JSONFunction.readArray(Libraries.getJSONObject(i).getJSONObject("extract").getJSONArray("exclude"))){
							excludeTemp.add(OneExclude);
						}
						extractExclude.add(excludeTemp);
					}else{
						System.out.println("Loading library: "+LibrariesPath+SplitChar+(temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?"-natives-linux.jar":".jar"));
						LauncheCmd += LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?"-natives-linux.jar":".jar")+":";
					}
				}
			}
			System.out.println("----------");
			new ThreadJob().ExtractJob(extractFiles,extractTargetDir,extractExclude);
			LauncheCmd += MainJar+" "+MainClass;
			//System.out.println(LauncheClassPath);
		} catch (IOException e) {
			System.out.println("Launche Error!");
			e.printStackTrace();
		}
		
		RFInfo.MainFrameObj.MainProgressBar.setString(RFInfo.SelectedLang.getString("LaunchingGame"));
		RFInfo.MainFrameObj.MainProgressBar.setIndeterminate(false);
		RFInfo.MainFrameObj.MainProgressBar.setEnabled(false);
		LauncheArgs=LauncheArgs.replace("${auth_player_name}",PlayerName);
		LauncheArgs=LauncheArgs.replace("${auth_uuid}",session.split(":")[2]);
		LauncheArgs=LauncheArgs.replace("${auth_access_token}",session.split(":")[1]);
		LauncheArgs=LauncheArgs.replace("${auth_session}",session);
		LauncheArgs=LauncheArgs.replace("${game_directory}",GameDir);
		LauncheArgs=LauncheArgs.replace("${game_assets}",AssetsDir);
		LauncheArgs=LauncheArgs.replace("${version_name}",Version);
		LauncheCmd += " "+LauncheArgs;
		try {
			System.out.println("\nRunning Command: "+LauncheCmd);
			System.out.println("--Launche Finish!");
			TimeCounter.stop();
			TimeCounter.count();
			RFInfo.MainFrameObj.setVisible(false);
			Process LauncheProcess = Runtime.getRuntime().exec(LauncheCmd);
			BufferedReader p_in = new BufferedReader(new InputStreamReader(LauncheProcess.getInputStream()));
            String line="";
            try{
            	LauncheProcess.exitValue();
            	System.out.println("Launche Over Return:"+LauncheProcess.exitValue());
            }catch(IllegalThreadStateException e){
            	System.out.println("\nGame is Running");
            }
            while((line = p_in.readLine()) != null){
            	System.out.println("【Minecraft Console】> "+line);
            	if(line.indexOf("#@?@# ")>=0){
            		new JOptionPane().showMessageDialog(null,"<html><span style=\"color: red;\">"+RFInfo.SelectedLang.getString("GameError")+"</span></html>",RFInfo.SelectedLang.getString("Error"),JOptionPane.ERROR_MESSAGE);
            		break;
            	}
            }
            p_in.close();
            FileFunction.deleteDir(new File(NativesDir+SplitChar));
            System.out.println("Deleted Natives Dir!");
            RFInfo.MainFrameObj.dispose(); //結束程式
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
}
class Launche_windows extends Launcher{{
	SplitChar="\\";
	minecraftDir= (((System.getenv("APPDATA")!=null)?System.getenv("APPDATA"):System.getProperty("user.home", "."))+"\\.minecraft\\");
	JVMPath = "\""+System.getProperty("java.home")+"\\bin\\"+(new File("\""+System.getProperty("java.home")+"\\bin\\javaw.exe\"").isFile()?"javaw.exe":"java")+"\"";
}	
	@Override
	public void LauncheGame(String PlayerName,String ProfileName,String session){
		String VersionName="";
		String GameDir="";
		String AssetsDir=minecraftDir+"assets";
		String MainClass="";
		String MainJar="";
		String LauncheCmd="";
		String NativesDir="";
		String LauncheArgs="";
		String Version="";
		boolean NeedDownload=false;
		String LibrariesDir=minecraftDir+"libraries"+SplitChar;
		System.out.println("\n--Launching Game...");
		//Long LauncheTime = System.currentTimeMillis();
		LauncheCmd += this.JVMPath;
		System.out.println("Free Memory: "+Runtime.getRuntime().totalMemory());
		try {
			//-----------載入必要資訊----------
			JSONObject launcher_profile = JSONFunction.CreateFromFile(minecraftDir+"launcher_profiles.json");
			if(!launcher_profile.getString("selectedProfile").equals(ProfileName) && !Login.canConnect()){
				//System.out.println("dassddasd");
				RFInfo.MainFrameObj.LoginBtm.setEnabled(true);
				RFInfo.MainFrameObj.UserBox.setEnabled(true);
				RFInfo.MainFrameObj.PwdBox.setEnabled(true);
				RFInfo.MainFrameObj.ProfileSelectBox.setEnabled(true);
				RFInfo.MainFrameObj.RememberMe.setEnabled(true);
				RFInfo.MainFrameObj.MainProgressBar.setIndeterminate(false);
				RFInfo.MainFrameObj.MainProgressBar.setString(RFInfo.SelectedLang.getString("OfflineDownload"));
				RFInfo.MainFrameObj.MainProgressBar.setEnabled(false);
				new JOptionPane().showMessageDialog(null,"<html><span style=\"color: RED;\">"+RFInfo.SelectedLang.getString("OfflineDownload")+"</span></html>",RFInfo.SelectedLang.getString("Error"),JOptionPane.ERROR_MESSAGE);
				return;
			}
			LauncheCmd += " "+(JSONFunction.Search(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName),"javaArgs")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("javaArgs"):"-Xmx"+(System.getProperty("sun.arch.data.model").equals("32")?"512M":"1G"));
			GameDir = (JSONFunction.Search(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName),"gameDir")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("gameDir"):minecraftDir);
			//PlayerName = launcher_profile.getJSONObject("authenticationDatabase").getJSONObject(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("playerUUID")).getString("displayName");
			//String Token = ("token:"+launcher_profile.getJSONObject("authenticationDatabase").getJSONObject(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("playerUUID")).getString("accessToken")+":"+launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("playerUUID"));
			String LastVersionID = JSONFunction.Search(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName),"lastVersionId")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("lastVersionId"):"1.6.2";
			NativesDir = (minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+"-natives-" + System.nanoTime());
			NeedDownload = !launcher_profile.getString("selectedProfile").equals(ProfileName) && Login.canConnect();
			MainJar = minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".jar";
			Version=LastVersionID;
			launcher_profile.put("selectedProfile",ProfileName);
			JSONFunction.WriteToFile(launcher_profile,minecraftDir+"launcher_profiles.json");
			System.out.println("GameDir="+GameDir);
			System.out.println("PlayerDisplayName="+PlayerName);
			//System.out.println("Session=******");
			System.out.println("BaseVersion="+Version);
			JSONObject LauncheFile = JSONFunction.CreateFromFile(minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".json");
			MainClass = LauncheFile.getString("mainClass");
			LauncheArgs = LauncheFile.getString("minecraftArguments");
			JSONArray Libraries = LauncheFile.getJSONArray("libraries");
			
			
			//---------------------刪除,重下載程式庫---------------
			if(NeedDownload){
				RFInfo.MainFrameObj.MainProgressBar.setIndeterminate(true);
				RFInfo.MainFrameObj.MainProgressBar.setString(RFInfo.SelectedLang.getString("DeletingLib"));
				System.out.println("Deleting libaries...");
				System.out.println(FileFunction.deleteDir(new File(LibrariesDir))?"Delete success!":"Delete unsuccess!");
				new File(LibrariesDir).mkdir();
				System.out.println("Deleted libraries!");
				RFInfo.MainFrameObj.MainProgressBar.setIndeterminate(false);
				System.out.println("Downloading libraries");
				ArrayList<String> DownloadURLs=new ArrayList<String>();
				ArrayList<String> DownloadBases=new ArrayList<String>();
				for(int i=0;i<Libraries.length();i++){
					String LibrariesPath = (Libraries.getJSONObject(i).getString("name"));
					String[] temp = LibrariesPath.split(":");
					LibrariesPath = temp[0].replace(".", SplitChar);
					for(int j=1;j<temp.length;j++){
						LibrariesPath += (SplitChar+temp[j]);
					}
					RFInfo.MainFrameObj.MainProgressBar.setString(RFInfo.SelectedLang.getString("Downloading")+temp[temp.length-2]+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?"-natives-windows.jar":".jar")+"-"+temp[temp.length-1]);
					if(CheckLibPermission(Libraries.getJSONObject(i))){
						String FileURL="";
						String downloadURL="";
						FileURL = (LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1]);
						FileURL += (JSONFunction.Search(Libraries.getJSONObject(i), "natives")?"-natives-windows.jar":".jar");
						System.out.println("-- "+FileURL);
						DownloadURLs.add(FileURL);
						DownloadBases.add(JSONFunction.Search(Libraries.getJSONObject(i),"url")?Libraries.getJSONObject(i).getString("url"):this.LibrariesBaseDownloadURL);
					}else{
						System.out.println("Skip download: "+LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?"-natives-windows.jar":".jar"));
					}
				}
				new ThreadJob().DownloadJob(DownloadBases,DownloadURLs);
			}
			
			
			//------------------解壓,載入程式庫----------------
			File ExtractDir = new File(NativesDir+SplitChar);
			RFInfo.MainFrameObj.MainProgressBar.setValue(0);
			RFInfo.MainFrameObj.MainProgressBar.setString(RFInfo.SelectedLang.getString("LoadingLib"));
			RFInfo.MainFrameObj.MainProgressBar.setIndeterminate(true);
			System.out.println(NativesDir+SplitChar);
			if(!ExtractDir.exists() || !ExtractDir.isDirectory()){
				makeDir(NativesDir+SplitChar,"\\"+this.SplitChar);
				System.out.println("-Created natives dir!");
			}
			LauncheCmd += " -Djava.library.path="+NativesDir;
			LauncheCmd += " -cp ";
			RFInfo.MainFrameObj.MainProgressBar.setMaximum(Libraries.length());
			ArrayList<String> extractFiles = new ArrayList<String>();
			ArrayList<String> extractTargetDir = new ArrayList<String>();
			ArrayList<ArrayList<String>> extractExclude = new ArrayList<ArrayList<String>>();
			System.out.println("\n----------");
			for(int i=0;i<Libraries.length();i++){
				if(CheckLibPermission(Libraries.getJSONObject(i))){
					String LibrariesPath = (Libraries.getJSONObject(i).getString("name"));
					String[] temp = LibrariesPath.split(":");
					LibrariesPath = temp[0].replace(".", SplitChar);
					for(int j=1;j<temp.length;j++){
						LibrariesPath += (SplitChar+temp[j]);
					}
					//String[] SortedArray = new JSONObject().getNames(Libraries.getJSONObject(i));
					//Arrays.sort(SortedArray);
					if(JSONFunction.Search(Libraries.getJSONObject(i), "extract")){
						String[] FileList = (new File(minecraftDir+"libraries"+SplitChar+LibrariesPath).list());
						//System.out.println(FileList[0]);
						System.out.println("Will Extract library: "+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?"-natives-windows.jar":".jar"));
						//new unZip(LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+(new JSONFounction().Search(Libraries.getJSONObject(i), "natives")?"-natives-windows.jar":".jar"),NativesDir+SplitChar,new JSONFounction().readArray(Libraries.getJSONObject(i).getJSONObject("extract").getJSONArray("exclude")));
						extractFiles.add(LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?"-natives-windows.jar":".jar"));
						extractTargetDir.add(NativesDir+SplitChar);
						ArrayList<String> excludeTemp=new ArrayList<String>();
						for(String OneExclude:JSONFunction.readArray(Libraries.getJSONObject(i).getJSONObject("extract").getJSONArray("exclude"))){
							excludeTemp.add(OneExclude);
						}
						extractExclude.add(excludeTemp);
					}else{
						System.out.println("Loading library: "+LibrariesPath+SplitChar+(temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?"-natives-windows.jar":".jar"));
						LauncheCmd += LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+(JSONFunction.Search(Libraries.getJSONObject(i), "natives")?"-natives-windows.jar":".jar")+";";
					}
				}
			}
			System.out.println("----------");
			new ThreadJob().ExtractJob(extractFiles,extractTargetDir,extractExclude);
			LauncheCmd += MainJar+" "+MainClass;
			//System.out.println(LauncheClassPath);
		} catch (IOException e) {
			System.out.println("Launche Error!");
			e.printStackTrace();
		}
		RFInfo.MainFrameObj.MainProgressBar.setString(RFInfo.SelectedLang.getString("LaunchingGame"));
		RFInfo.MainFrameObj.MainProgressBar.setIndeterminate(false);
		RFInfo.MainFrameObj.MainProgressBar.setEnabled(false);
		LauncheArgs=LauncheArgs.replace("${auth_player_name}",PlayerName);
		LauncheArgs=LauncheArgs.replace("${auth_uuid}",session.split(":")[2]);
		LauncheArgs=LauncheArgs.replace("${auth_access_token}",session.split(":")[1]);
		LauncheArgs=LauncheArgs.replace("${auth_session}",session);
		LauncheArgs=LauncheArgs.replace("${game_directory}",GameDir);
		LauncheArgs=LauncheArgs.replace("${game_assets}",AssetsDir);
		LauncheArgs=LauncheArgs.replace("${version_name}",Version);
		LauncheCmd += " "+LauncheArgs;
		try {
			System.out.println("\nRunning Command: "+LauncheCmd);
			System.out.println("--Launche Finish!");
			TimeCounter.stop();
			TimeCounter.count();
			RFInfo.MainFrameObj.setVisible(false);
			Process LauncheProcess = Runtime.getRuntime().exec(LauncheCmd);
			BufferedReader p_in = new BufferedReader(new InputStreamReader(LauncheProcess.getInputStream()));
            String line="";
            try{
            	LauncheProcess.exitValue();
            	System.out.println("Launche Over Return:"+LauncheProcess.exitValue());
            }catch(IllegalThreadStateException e){
            	System.out.println("\nGame is Running");
            }
            while((line = p_in.readLine()) != null){
            	System.out.println("【Minecraft Console】> "+line);
            	if(line.matches(".*#@.@#.*")){
            		new JOptionPane().showMessageDialog(null,"<html><span style=\"color: red;\">"+RFInfo.SelectedLang.getString("GameError")+"</span></html>",RFInfo.SelectedLang.getString("Error"),JOptionPane.ERROR_MESSAGE);
            		break;
            	}
            }
            p_in.close();
            FileFunction.deleteDir(new File(NativesDir+SplitChar));
            System.out.println("Deleted Natives Dir!");
            RFInfo.MainFrameObj.dispose(); //結束程式
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
}*/
