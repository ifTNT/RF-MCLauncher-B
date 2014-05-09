package org.rf.john.mclaunch.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

public class Launcher{
	public Logger logger;
	public static String minecraftDir;
	public static String JVMPath;
	public static char SplitChar;
	private HashMap<String,String> LauncherOptions;
	private static String LibrariesBaseDownloadURL="https://libraries.minecraft.net/";
	private static String AssetsIndexBaseDownloadURL="https://s3.amazonaws.com/Minecraft.Download/indexes/";
	private static String AssetsBaseDownloadURL="http://resources.download.minecraft.net/";
	private static String LoginServer="https://login.minecraft.net/";
	//public int LauncherVersion=1;
		
	public Launcher(Logger _logger){
		logger=_logger;
		SplitChar=File.separatorChar;
		if(getOS().equals("linux")){
			minecraftDir= System.getProperty("user.home")+"/.minecraft/";
			JVMPath = System.getProperty("java.home")+"/bin/java";
			
		}else if(getOS().equals("windows")){
			minecraftDir= (((System.getenv("APPDATA")!=null)?System.getenv("APPDATA"):System.getProperty("user.home", "."))+"\\.minecraft\\");
			JVMPath = "\""+System.getProperty("java.home")+"\\bin\\"+(new File("\""+System.getProperty("java.home")+"\\bin\\javaw.exe\"").isFile()?"javaw.exe":"java")+"\"";
		}else if(getOS().equals("osx")){
			minecraftDir=System.getProperty("user.home")+"/Library/Application Support/minecraft/";
			JVMPath=System.getProperty("java.home")+"/bin/java";
		}else if(getOS().equals("unknow")){
			logger.Error("Unknow os");
		}
	}
	
	/**
	 * 取得已定義的啟動檔
	 * @return 啟動檔
	 */
	@SuppressWarnings("static-access")
	public final HashSet<String> getInstalledProfiles(){
		HashSet<String> FinallyOut = new HashSet<String>();
		try {
			JSONObject launcher_profile = JSONFunction.CreateFromFile(minecraftDir+"launcher_profiles.json");
			JSONObject InstalledProfiles = launcher_profile.getJSONObject("profiles");
			for(String OneProfileName:new JSONObject().getNames(InstalledProfiles)){
				FinallyOut.add(OneProfileName);
			}
		} catch (IOException e) {
			logger.Error("Get profiles error",e);
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
			JSONObject launcher_profile = JSONFunction.CreateFromFile(minecraftDir+"launcher_profiles.json");
			FinallyOut = launcher_profile.getString("selectedProfile");
		} catch (IOException e) {
			logger.Error("Get selected profile error",e);
		}
		return FinallyOut;
	}
	
	/**
	 * 設定Minecraft Last Version
	 */
	public final String getLastVersion(){
		try {
			JSONObject VersionsJson=JSONFunction.CreateFromStream(
					getConnectObj("https://s3.amazonaws.com/Minecraft.Download/versions/versions.json").getInputStream());
			return (VersionsJson.getJSONObject("latest").has("release")?VersionsJson.getJSONObject("latest").getString("release"):null);
		} catch (IOException e) {
			logger.Error("Get last version error",e);
			return null;
		}
	}
	
	public boolean checkLoginServer(){
		try {
			return 	JSONFunction.CreateFromStream(
							DownloadThread.getConnectObj("http://status.mojang.com/check?service=login.minecraft.net").getInputStream())
							.getString("login.minecraft.net").equals("green");
		} catch (IOException e) {
			logger.Error("Connect to Internet Error",e);
			return false;
		}
	}
	
	public HashMap<String,String> Login(String user,String pwd){
		String Result="";
		HashMap<String,String> ReturnVal=new HashMap<>();
		try {
			logger.Info("Checking user...");
			InputStreamReader resules = new InputStreamReader(
					getConnectObj(LoginServer+"?user="+user+"&password="+pwd+"&version=14").getInputStream(),"UTF-8");
			int data = resules.read();
			while(data != -1){
				Result += (char) data;
				data = resules.read();
			}
		} catch (IOException e) {
			logger.Error("Check user error",e);
			ReturnVal.put("Status","Server error");
			return ReturnVal;
		}
		switch(Result){
			case "Bad login":
				logger.Error("Login Error: Bad login");
				ReturnVal.put("Status","Bad login");
			case "Old version":
				logger.Error("Login Error: Old version");
				ReturnVal.put("Status","Old version");
			default:
				logger.Info("Login success!");
				ReturnVal.put("Status","Success");
				String[] SplitedResult=Result.split(":");
				ReturnVal.put("UserName",SplitedResult[2]);
				ReturnVal.put("Session","token:"+SplitedResult[3]+":"+SplitedResult[4]);
				ReturnVal.put("UUID",SplitedResult[4]);
				ReturnVal.put("AccessToken",SplitedResult[3]);
		}
		return ReturnVal;
	}
	
	public boolean LaunchGameOffline(String ProfileName,String LastVersion){
		HashMap<String,String> DefaultOption=new HashMap<>();
		DefaultOption.put("NoDeleteNatives","false");
		return LaunchGameOffline(ProfileName,LastVersion,DefaultOption);
	}
	
	/**
	 * 離線啟動遊戲
	 * @param ProfileName 啟動檔名稱
	 */
	public boolean LaunchGameOffline(String ProfileName,String LastVersion,HashMap<String,String> options){
		try {
			logger.Info("Launching Offline");
			
			JSONObject launcher_profile = JSONFunction.CreateFromFile(minecraftDir+"launcher_profiles.json");
			String ProfileSession = ("token:"+launcher_profile.getJSONObject("authenticationDatabase").getJSONObject(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("playerUUID")).getString("accessToken")+":"+launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("playerUUID"));
			String PlayerName = launcher_profile.getJSONObject("authenticationDatabase").getJSONObject(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("playerUUID")).getString("displayName");
			return this.LaunchGame(PlayerName,ProfileName, ProfileSession,LastVersion,options);
		} catch (IOException e) {
			logger.Error("Launch offline error",e);
			return false;
		}
	}
	
	public boolean LaunchGame(String PlayerName,String ProfileName,String session,String LastVersion){
		HashMap<String,String> DefaultOption=new HashMap<>();
		DefaultOption.put("NoDeleteNatives","false");
		return LaunchGame(PlayerName,ProfileName,session,LastVersion,DefaultOption);
	}
	
	/**
	 * 啟動遊戲
	 * @param PlayerName 玩家名稱
	 * @param ProfileName 啟動檔名稱
	 * @param session 玩家的Session
	 */
	@SuppressWarnings("static-access")
	public boolean LaunchGame(String PlayerName,String ProfileName,String session,String LastVersion,HashMap<String,String> options){
		this.LauncherOptions=options;
		String GameDir="";
		String AssetsDir=minecraftDir+"assets";
		String MainClass="";
		String MainJar="";
		String LaunchCmd="";
		String NativesDir="";
		String LaunchArgs="";
		String Version="";
		String AssetsIndex="";
		String LibrariesDir=minecraftDir+"libraries"+SplitChar;
		logger.Info("\n--Launching Game...");
		LaunchCmd += this.JVMPath;
		logger.Info("Free Memory: "+Runtime.getRuntime().totalMemory()+" Bytes");
		try {
			//-------------讀取必要資訊--------------
			JSONObject launcher_profile = JSONFunction.CreateFromFile(minecraftDir+"launcher_profiles.json");
			if(!launcher_profile.getString("selectedProfile").equals(ProfileName) &&  //if change selected profile in offline
					!JSONFunction.CreateFromStream(getConnectObj("http://status.mojang.com/check?service=login.minecraft.net").getInputStream())
					.getString("login.minecraft.net").equals("green")){
				logger.DeadlyError("Offline download");
				return false;
			}
			LaunchCmd += " "+(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).has("javaArgs")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("javaArgs"):"-Xmx"+(System.getProperty("sun.arch.data.model").equals("32")?"512M":"1G"));
			GameDir = launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).has("gameDir")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("gameDir"):minecraftDir;
			String LastVersionID = launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).has("lastVersionId")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("lastVersionId"):LastVersion;
			NativesDir = (minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+"-natives-" + System.nanoTime());
			MainJar = minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".jar";
			Version=LastVersionID;
			
			logger.Info("GameDir="+GameDir);
			logger.Info("PlayerDisplayName="+PlayerName);
			logger.Info("Session=******");
			logger.Info("BaseVersion="+Version);
			if(!new File(minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".json").isFile()||  //如果Profile or jar不存在
					!new File(minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".jar").isFile()){
				HashSet<String> Versions=new HashSet<>();
				JSONObject VersionsJson=JSONFunction.CreateFromStream(
						getConnectObj("https://s3.amazonaws.com/Minecraft.Download/versions/versions.json").getInputStream());
				for(int i=0;i<=VersionsJson.getJSONArray("versions").length()-1;i++){
					Versions.add(VersionsJson.getJSONArray("versions").getJSONObject(i).getString("id"));
				}
				if(Versions.contains(LastVersionID)){
					ArrayList<DownloadUtil> LastVersionIdFiles=new ArrayList<>();
					LastVersionIdFiles.add(new DownloadUtil("http://s3.amazonaws.com/Minecraft.Download/versions/"+LastVersionID+"/"+LastVersionID+".json",
																minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".json"));
					LastVersionIdFiles.add(new DownloadUtil("http://s3.amazonaws.com/Minecraft.Download/versions/"+LastVersionID+"/"+LastVersionID+".jar",
																minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".jar"));
					new ThreadJob().DownloadJob(logger,"Profile and Jar",LastVersionIdFiles);
				}else{
					logger.DeadlyError("Can not download");
					return false;
				}
			}
			launcher_profile.put("selectedProfile",ProfileName);
			JSONFunction.WriteToFile(launcher_profile,minecraftDir+"launcher_profiles.json");
			JSONObject LaunchFile = JSONFunction.CreateFromFile(minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".json");
			MainClass = LaunchFile.getString("mainClass");
			LaunchArgs = LaunchFile.getString("minecraftArguments");
			AssetsIndex=(LaunchFile.has("assets")?/*(Version.matches("[0-9.]+")?Version:*/LaunchFile.getString("assets")/*)*/:"legacy");
			JSONArray Libraries = LaunchFile.getJSONArray("libraries");
			
			
			//---------------------刪除,重下載程式庫---------------
			
			logger.progressBar.setIndeterminate(true);
			if(!new File(LibrariesDir).isDirectory()) new File(LibrariesDir).mkdir();
			logger.progressBar.setIndeterminate(false);
			logger.Info("Downloading libraries");
			logger.progressBar.setMaximum(Libraries.length());
			logger.progressBar.setValue(0);
			ArrayList<DownloadUtil> DownloadJobs=new ArrayList<>();
			for(int i=0;i<Libraries.length();i++){
				String LibrariesPath = (Libraries.getJSONObject(i).getString("name"));
				String[] SplitedPath = LibrariesPath.split(":");
				LibrariesPath = SplitedPath[0].replace('.', SplitChar);
				for(int j=1;j<SplitedPath.length;j++){
					LibrariesPath += (SplitChar+SplitedPath[j]);
				}
				String FileURL="";
				String NativesTail=(Libraries.getJSONObject(i).has("natives")&&Libraries.getJSONObject(i).getJSONObject("natives").has(getOS())?
											"-"+Libraries.getJSONObject(i).getJSONObject("natives").getString(getOS()).replace("${arch}",System.getProperty("sun.arch.data.model")):"");
				FileURL = (LibrariesPath+SplitChar+SplitedPath[SplitedPath.length-2]+"-"+SplitedPath[SplitedPath.length-1]+NativesTail+".jar");
				if(!SplitedPath[SplitedPath.length-2].matches("[a-zA-z]*forge.*")&&CheckLibPermission(Libraries.getJSONObject(i))){
					logger.Info("-- "+FileURL/*.replace("-universal","")*/);
					DownloadUtil OneLibrary = new DownloadUtil();
					OneLibrary.DownloadPath=((Libraries.getJSONObject(i).has("url")?Libraries.getJSONObject(i).getString("url"):this.LibrariesBaseDownloadURL)+FileURL).replace(SplitChar,'/')+(Libraries.getJSONObject(i).has("clientreq")&&Libraries.getJSONObject(i).getBoolean("clientreq")?".pack.xz":"");
					OneLibrary.FilePath=this.minecraftDir+"libraries"+SplitChar+FileURL/*.replace("-universal","")*/;
					DownloadJobs.add(OneLibrary);
				}else{
					logger.Info("Skip download: "+LibrariesDir+FileURL.replace("-universal",""));
				}
			}
			new ThreadJob().DownloadJob(logger,"Libraries",DownloadJobs);
			
			//------------------下載assets-------------------
			
			logger.Info("\n");
			logger.progressBar.setIndeterminate(false);
			logger.progressBar.setString("LoadingAssets");
			
			new ThreadJob().DownloadJob(logger,"AssetsIndex",new DownloadUtil(AssetsIndexBaseDownloadURL+AssetsIndex+".json",AssetsDir+SplitChar+"indexes"+SplitChar+AssetsIndex+".json"));
			logger.progressBar.setValue(0);
			logger.progressBar.setIndeterminate(true);
			logger.progressBar.setString("LoadingAssets");
			JSONObject AssetsIndexJSON = JSONFunction.CreateFromFile(this.minecraftDir+SplitChar+"assets"+SplitChar+"indexes"+SplitChar+AssetsIndex+".json");
			boolean Virtual = AssetsIndexJSON.has("virtual")&&AssetsIndexJSON.getBoolean("virtual");
			HashMap<DownloadUtil,Integer> DownloadAssets = new HashMap<>();
			for(String OneAssetName:JSONObject.getNames(AssetsIndexJSON.getJSONObject("objects"))){				
				String Hash = AssetsIndexJSON.getJSONObject("objects").getJSONObject(OneAssetName).getString("hash");
				
				//Objects->Virtual
				if(Virtual&&new File(AssetsDir+SplitChar+"objects"+SplitChar+Hash.substring(0,2)+SplitChar+Hash).isFile()){
					String OldFile=AssetsDir+SplitChar+"objects"+SplitChar+Hash.substring(0,2)+SplitChar+Hash;
					String NewFile=AssetsDir+SplitChar+"virtual"+SplitChar+AssetsIndex+SplitChar+OneAssetName;
					if(!new File(NewFile).isFile()){
						if(OneAssetName.lastIndexOf("/")>0) new File(AssetsDir+SplitChar+"virtual"+SplitChar+AssetsIndex+SplitChar+OneAssetName.substring(0,OneAssetName.lastIndexOf("/")).replace('/',SplitChar)).mkdirs();
						BufferedInputStream ReadStream = new BufferedInputStream(new FileInputStream(OldFile));
						new File(NewFile).createNewFile();
						BufferedOutputStream WriteStream = new BufferedOutputStream(new FileOutputStream(NewFile));
						WriteStream.write(DownloadThread.readFully(ReadStream));
						WriteStream.flush();
						ReadStream.close();
						WriteStream.close();
					}
				}
				/*//Virtual->Objects
				if(!Virtual&&new File(AssetsDir+SplitChar+"virtual"+SplitChar+AssetsIndex+SplitChar+OneAssetName).isFile()){
					String OldFile=AssetsDir+SplitChar+"virtual"+SplitChar+AssetsIndex+SplitChar+OneAssetName;
					String NewFile=AssetsDir+SplitChar+"objects"+SplitChar+Hash.substring(0,2)+SplitChar+Hash;
					if(!new File(NewFile).isFile()){
						if(OneAssetName.lastIndexOf("/")>0) makeDir(AssetsDir+SplitChar+"virtual"+SplitChar+AssetsIndex+SplitChar+OneAssetName.substring(0,OneAssetName.lastIndexOf("/")).replace("/",SplitChar));
						BufferedInputStream ReadStream = new BufferedInputStream(new FileInputStream(OldFile));
						new File(NewFile).createNewFile();
						BufferedOutputStream WriteStream = new BufferedOutputStream(new FileOutputStream(NewFile));
						WriteStream.write(DownloadThread.readFully(ReadStream));
						WriteStream.flush();
						ReadStream.close();
						WriteStream.close();
					}
				}*/
				
				DownloadUtil OneDownloadAsset=new DownloadUtil();
				OneDownloadAsset.DownloadPath=(this.AssetsBaseDownloadURL+Hash.substring(0,2)+SplitChar+Hash).replace(SplitChar,'/');
				OneDownloadAsset.FilePath=
						(Virtual?AssetsDir+SplitChar+"virtual"+SplitChar+AssetsIndex+SplitChar:AssetsDir+SplitChar+"objects"+SplitChar)+
						(Virtual?OneAssetName.replace('/',SplitChar):Hash.substring(0,2)+SplitChar+Hash);
				DownloadAssets.put(OneDownloadAsset,AssetsIndexJSON.getJSONObject("objects").getJSONObject(OneAssetName).getInt("size"));
			}
			logger.progressBar.setIndeterminate(false);
			
			//----------按檔案大小排序檔案-------------
			ArrayList<Entry<DownloadUtil, Integer>> Sorter = new ArrayList<>(DownloadAssets.entrySet());
			Collections.sort(Sorter,new Comparator<Entry<DownloadUtil,Integer>>(){
				@Override
				public int compare(Entry<DownloadUtil, Integer> o1,Entry<DownloadUtil, Integer> o2){
					return (o1.getValue()-o2.getValue());
				}	
			});
			
			ArrayList<DownloadUtil> SortedAssets = new ArrayList<>();
			for(Entry<DownloadUtil,Integer> OneDownloadObject:Sorter){
				SortedAssets.add(OneDownloadObject.getKey());
			}
			//-------------END 排序-----------------
			
			new ThreadJob().DownloadJob(logger,"Assets",20,SortedAssets);
			logger.progressBar.setIndeterminate(true);
			AssetsDir+=(Virtual?SplitChar+"virtual"+SplitChar+AssetsIndex+SplitChar:"");
			
			/*if(Virtual){
				makeDir(AssetsDir+SplitChar+"virtual"+SplitChar+AssetsIndex+SplitChar);
				for(String OneAssetName:JSONObject.getNames(AssetsIndexJSON.getJSONObject("objects"))){
					String Hash = AssetsIndexJSON.getJSONObject("objects").getJSONObject(OneAssetName).getString("hash");
					
					String OldFile=AssetsDir+SplitChar+"virtual"+SplitChar+"temp"+SplitChar+Hash.substring(0,2)+SplitChar+Hash;
					String NewFile=AssetsDir+SplitChar+"virtual"+SplitChar+AssetsIndex+SplitChar+OneAssetName.replace("/",SplitChar);
					if(!new File(NewFile).isFile()){
						if(OneAssetName.lastIndexOf("/")>0) makeDir(AssetsDir+SplitChar+"virtual"+SplitChar+AssetsIndex+SplitChar+OneAssetName.substring(0,OneAssetName.lastIndexOf("/")).replace("/",SplitChar));
						BufferedInputStream ReadStream = new BufferedInputStream(new FileInputStream(OldFile));
						new File(NewFile).createNewFile();
						BufferedOutputStream WriteStream = new BufferedOutputStream(new FileOutputStream(NewFile));
						WriteStream.write(DownloadThread.readFully(ReadStream));
						WriteStream.flush();
						ReadStream.close();
						WriteStream.close();
						//new File(OldFile).delete();
					}
				}
				//deleteDir(new File(AssetsDir+SplitChar+"virtual"+SplitChar+"temp"+SplitChar));
				AssetsDir+=SplitChar+"virtual"+SplitChar+AssetsIndex;
			}*/
			logger.progressBar.setIndeterminate(false);
			//------------------解壓,載入程式庫----------------
			File ExtractDir = new File(NativesDir+SplitChar);
			logger.progressBar.setValue(0);
			logger.progressBar.setString("LoadingLib");
			logger.progressBar.setIndeterminate(true);
			if(!ExtractDir.exists() || !ExtractDir.isDirectory()){
				new File(NativesDir+SplitChar+(getOS().equals("windows")?"\\":"")).mkdirs();
				logger.Info("-Natives dir was created!");
			}
			LaunchCmd += " -Djava.library.path="+ProgressDirSpace(NativesDir);
			LaunchCmd += " -cp ";
			ArrayList<String> extractFiles = new ArrayList<String>();
			ArrayList<String> extractTargetDir = new ArrayList<String>();
			ArrayList<ArrayList<String>> extractExclude = new ArrayList<ArrayList<String>>();
			logger.progressBar.setMaximum(Libraries.length());
			logger.Info("\n----------");
			for(int i=0;i<Libraries.length();i++){
				if(CheckLibPermission(Libraries.getJSONObject(i))){
					String LibrariesPath = (Libraries.getJSONObject(i).getString("name"));
					String[] temp = LibrariesPath.split(":");
					LibrariesPath = temp[0].replace('.', SplitChar);
					for(int j=1;j<temp.length;j++){
						LibrariesPath += (SplitChar+temp[j]);
					}
					String NativesTail=(Libraries.getJSONObject(i).has("natives")&&Libraries.getJSONObject(i).getJSONObject("natives").has(getOS())?
												"-"+Libraries.getJSONObject(i).getJSONObject("natives").getString(getOS()).replace("${arch}",System.getProperty("sun.arch.data.model")):"");
					if(Libraries.getJSONObject(i).has("extract")){
						logger.Info("This library will be extracted: "+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+NativesTail);
						extractFiles.add(LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+NativesTail+".jar");
						extractTargetDir.add(NativesDir+SplitChar);
						ArrayList<String> excludeTemp=new ArrayList<String>();
						for(String OneExclude:JSONFunction.readArray(Libraries.getJSONObject(i).getJSONObject("extract").getJSONArray("exclude"))){
							excludeTemp.add(OneExclude);
						}
						extractExclude.add(excludeTemp);
					}else{
						logger.Info("Loading library: "+LibrariesPath+SplitChar+(temp[temp.length-2]+"-"+temp[temp.length-1])+NativesTail);
						String OneLibraryPath=LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+NativesTail+".jar";
						LaunchCmd += ProgressDirSpace(OneLibraryPath)+System.getProperty("path.separator");
					}
				}
			}
			logger.Info("----------");
			new ThreadJob().ExtractJob(logger,"Libraries",extractFiles,extractTargetDir,extractExclude);
			LaunchCmd += ProgressDirSpace(MainJar)+" "+MainClass;
			
			logger.progressBar.setString("LaunchingGame");
			logger.progressBar.setIndeterminate(false);
			logger.progressBar.setValue(0);
			logger.progressBar.setEnabled(false);
			GameDir=ProgressDirSpace(GameDir);
			AssetsDir=ProgressDirSpace(AssetsDir);
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
			LaunchCmd=LaunchCmd.replace("\\","\\\\");

			logger.Info("\nRunning Command: "+LaunchCmd);
			final Process LaunchProcess = Runtime.getRuntime().exec(LaunchCmd);
			final BufferedReader StdOut = new BufferedReader(new InputStreamReader(LaunchProcess.getInputStream(),"UTF-8"));
			final BufferedReader ErrOut = new BufferedReader(new InputStreamReader(LaunchProcess.getErrorStream(),"UTF-8"));
			new Thread(){
				@Override public void run(){
					try {
			            String line="";
			            while((line = StdOut.readLine()) != null){
			            	logger.Info("【Minecraft Console】> "+line);
			            }
			            StdOut.close();
					} catch (IOException e) {
						logger.Error("Launch Error",e);
					}
				}
			}.start();
			new Thread(){
				@Override public void run(){
					try {
			            String line="";
			            while((line = ErrOut.readLine()) != null){
			            	logger.Info("【Minecraft Error】> "+line);
			            }
			            ErrOut.close();
					} catch (IOException e) {
						logger.Error("Launch Error",e);
					}
				}
			}.start();
			logger.Info("--Launch Finished!");
			logger.FinishCallback();
			logger.Info("\nGame is Running");
			
            int exitVal = LaunchProcess.waitFor();
            logger.Info("Launch over with return value:"+exitVal);
            if(!(LauncherOptions.containsKey("NoDeleteNatives")&&LauncherOptions.get("NoDeleteNatives").equalsIgnoreCase("true"))){
            	deleteDir(new File(NativesDir+SplitChar));
            	logger.Info("Deleted Natives Dir!");
            }
            if(exitVal!=0){
            	logger.DeadlyError("Minecraft returnd wrong status");
            	return false;
            }
            logger.TheEndCallback();
            return true;
		} catch (IOException e) {
			logger.Error("Launch Error",e);
		}catch (InterruptedException e) {
			logger.Error("Get Return Value Error",e);
		}
		return false;
	}
	private static String ProgressDirSpace(String in){
		return in.indexOf(' ')>0?"\""+in+"\"":in;
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
		if(in.has("rules")){
			JSONArray Rules=in.getJSONArray("rules");
			for(int i=0;i<Rules.length();i++){
				JSONObject Action=Rules.getJSONObject(i);
				if(Action.getString("action").equals("allow")){ //allow
					if(Action.has("os")){
						if(Action.getJSONObject("os").getString("name").equals(getOS())){
							FinalAction=true;
							if(Action.getJSONObject("os").has("version")&&getOS().equals("osx")&&
									!System.getProperty("os.version").matches(Action.getJSONObject("os").getString("version"))){
								FinalAction=false;
							}	
						}
					}else{
						FinalAction=true;
					}
				}else{ //disallow
					if(Action.has("os")){
						if(Action.getJSONObject("os").getString("name").equals(getOS())){
							FinalAction=false;
							if(Action.getJSONObject("os").has("version")&&getOS().equals("osx")&&
									!System.getProperty("os.version").matches(Action.getJSONObject("os").getString("version"))){
								FinalAction=true;
							}
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
