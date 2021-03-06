package org.rf.john.mclauncher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rf.john.mclauncher.langs.Languages;
import org.rf.john.mclauncher.Status;


public class Launcher{
	public String minecraftDir;
	public String JVMPath;
	public String LastVersion="";
	public static String SplitChar;
	protected String LibrariesBaseDownloadURL="https://libraries.minecraft.net/";
	protected String AssetsIndexBaseDownloadURL="https://s3.amazonaws.com/Minecraft.Download/indexes/";
	protected String AssetsBaseDownloadURL="http://resources.download.minecraft.net/";
	
	@Override
	public String toString(){
		return "  ClassName: "+this.getClass().toString().substring(6)+"\n"+
				"  Version: #1\n"+
				"  RunMode: "+(Status.RunMode.equals(RunType.JAR)?"JAR":"Debug")+"\n"+
				"  Online: "+Login.canConnect();
	}
	
	public Launcher(){
		SplitChar=System.getProperty("file.separator");
		if(Status.getOS().equals("linux")){
			minecraftDir= System.getProperty("user.home")+"/.minecraft/";
			JVMPath = System.getProperty("java.home")+"/bin/java";
			
		}else if(Status.getOS().equals("windows")){
			minecraftDir= (((System.getenv("APPDATA")!=null)?System.getenv("APPDATA"):System.getProperty("user.home", "."))+"\\.minecraft\\");
			JVMPath = "\""+System.getProperty("java.home")+"\\bin\\"+(new File("\""+System.getProperty("java.home")+"\\bin\\javaw.exe\"").isFile()?"javaw.exe":"java")+"\"";
		}
	}
	
	/**
	 * 初始化啟動器
	 * @param Lang 語系
	 */
	@SuppressWarnings("static-access")
	public Launcher(Languages Lang){
		this();
		if(Status.getOS().equals("unknow")){
			new JOptionPane().showMessageDialog(null,"<html><span style=\"color: RED;\">"+Lang.getString("NotSupportSystem")+"</span></html>",Lang.getString("Error"),JOptionPane.ERROR_MESSAGE);
			System.exit(1);
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
			JSONObject launcher_profile = JSONFunction.CreateFromFile(minecraftDir+"launcher_profiles.json");
			JSONObject InstalledProfiles = launcher_profile.getJSONObject("profiles");
			for(String OneProfileName:new JSONObject().getNames(InstalledProfiles)){
				FinallyOut.add(OneProfileName);
			}
			//in.close();
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
			JSONObject launcher_profile = JSONFunction.CreateFromFile(minecraftDir+"launcher_profiles.json");
			FinallyOut = launcher_profile.getString("selectedProfile");
			//in.close();
		} catch (IOException e) {
			System.out.println("--*Get Selected Profile Error*--");
			e.printStackTrace();
		}
		return FinallyOut;
	}
	
	/**
	 * 設定Minecraft Last Version
	 */
	public final void SetLastVersion(){
		try {
			JSONObject VersionsJson=JSONFunction.CreateFromStream(
					DownloadThread.getConnectObj("https://s3.amazonaws.com/Minecraft.Download/versions/versions.json").getInputStream());
			this.LastVersion=(JSONFunction.Search(VersionsJson.getJSONObject("latest"),"release")?
					VersionsJson.getJSONObject("latest").getString("release"):"");
		} catch (IOException e) {
			this.LastVersion="";
			System.out.println("--*Get Last Version Error!*--");
			e.printStackTrace();
		}
		//this.LastVersion=(RFInfo.RFMCLB_InfoJSON!=null?RFInfo.RFMCLB_InfoJSON.getString("MinecraftLastVersion"):"");
		if(this.LastVersion.equals("")){ //從設定檔讀取
			this.LastVersion=new Save_LoadConfig().getLastVersion();
		}
		if(this.LastVersion.equals("")){
			this.LastVersion="1.7.4"; //預設值
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
		//boolean NeedDownload=false;
		String LibrariesDir=minecraftDir+"libraries"+SplitChar;
		System.out.println("\n--Launching Game...");
		LaunchCmd += this.JVMPath;
		System.out.println("Free Memory: "+Runtime.getRuntime().totalMemory()+" Bytes");
		try {
			//-------------讀取必要資訊--------------
			JSONObject launcher_profile = JSONFunction.CreateFromFile(minecraftDir+"launcher_profiles.json");
			if(!launcher_profile.getString("selectedProfile").equals(ProfileName) && !Login.canConnect()){
				Status.Theme.LoginBtn.setEnabled(true);
				Status.Theme.UserBox.setEnabled(true);
				Status.Theme.PwdBox.setEnabled(true);
				Status.Theme.ProfileSelectBox.setEnabled(true);
				Status.Theme.RememberMe.setEnabled(true);
				Status.Theme.MainProgressBar.setIndeterminate(false);
				Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("OfflineDownload"));
				Status.Theme.MainProgressBar.setEnabled(false);
				new JOptionPane().showMessageDialog(null,"<html><span style=\"color: RED;\">"+Status.SelectedLang.getString("OfflineDownload")+"</span></html>",Status.SelectedLang.getString("Error"),JOptionPane.ERROR_MESSAGE);
				return;
			}
			LaunchCmd += " "+(JSONFunction.Search(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName),"javaArgs")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("javaArgs"):"-Xmx"+(System.getProperty("sun.arch.data.model").equals("32")?"512M":"1G"));
			GameDir = JSONFunction.Search(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName),"gameDir")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("gameDir"):minecraftDir;
			String LastVersionID = JSONFunction.Search(launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName),"lastVersionId")?launcher_profile.getJSONObject("profiles").getJSONObject(ProfileName).getString("lastVersionId"):this.LastVersion;
			NativesDir = (minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+"-natives-" + System.nanoTime());
			//NeedDownload = !launcher_profile.getString("selectedProfile").equals(ProfileName) && Login.canConnect();
			MainJar = minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".jar";
			Version=LastVersionID;
			
			System.out.println("GameDir="+GameDir);
			System.out.println("PlayerDisplayName="+PlayerName);
			System.out.println("Session=******");
			System.out.println("BaseVersion="+Version);
			if(!new File(minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".json").isFile()||  //如果Profile or jar不存在
					!new File(minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".jar").isFile()){
				//Status.Theme.MainProgressBar.setIndeterminate(true);
				HashSet<String> Versions=new HashSet<>();
				JSONObject VersionsJson=JSONFunction.CreateFromStream(
						DownloadThread.getConnectObj("https://s3.amazonaws.com/Minecraft.Download/versions/versions.json").getInputStream());
				for(int i=0;i<=VersionsJson.getJSONArray("versions").length()-1;i++){
					Versions.add(VersionsJson.getJSONArray("versions").getJSONObject(i).getString("id"));
				}
				if(Versions.contains(LastVersionID)){
					ArrayList<DownloadObject> LastVersionIdFiles=new ArrayList<>();
					LastVersionIdFiles.add(new DownloadObject("http://s3.amazonaws.com/Minecraft.Download/versions/"+LastVersionID+"/"+LastVersionID+".json",
																minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".json"));
					LastVersionIdFiles.add(new DownloadObject("http://s3.amazonaws.com/Minecraft.Download/versions/"+LastVersionID+"/"+LastVersionID+".jar",
																minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".jar"));
					new ThreadJob().DownloadJob("Profile and Jar",LastVersionIdFiles);
				}else{
					Status.Theme.LoginBtn.setEnabled(true);
					Status.Theme.UserBox.setEnabled(true);
					Status.Theme.PwdBox.setEnabled(true);
					Status.Theme.ProfileSelectBox.setEnabled(true);
					Status.Theme.RememberMe.setEnabled(true);
					Status.Theme.MainProgressBar.setIndeterminate(false);
					Status.Theme.MainProgressBar.setValue(0);
					Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("CantDownload"));
					Status.Theme.MainProgressBar.setEnabled(false);
					new JOptionPane().showMessageDialog(null,"<html><span style=\"color: RED;\">"+Status.SelectedLang.getString("CantDownload")+"</span></html>",Status.SelectedLang.getString("Error"),JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			launcher_profile.put("selectedProfile",ProfileName);
			JSONFunction.WriteToFile(launcher_profile,minecraftDir+"launcher_profiles.json");
			JSONObject LaunchFile = JSONFunction.CreateFromFile(minecraftDir+"versions"+SplitChar+LastVersionID+SplitChar+LastVersionID+".json");
			MainClass = LaunchFile.getString("mainClass");
			LaunchArgs = LaunchFile.getString("minecraftArguments");
			AssetsIndex=(JSONFunction.Search(LaunchFile,"assets")?(Version.matches("[0-9.]+")?Version:LaunchFile.getString("assets")):"legacy");
			JSONArray Libraries = LaunchFile.getJSONArray("libraries");
			
			
			//---------------------刪除,重下載程式庫---------------
			
			//String NativesTail="-"+(Status.getOS().equals("linux")?"natives-linux":"natives-windows"/*-"+System.getProperty("os.arch")*/);
		
			Status.Theme.MainProgressBar.setIndeterminate(true);
			if(!new File(LibrariesDir).isDirectory()) new File(LibrariesDir).mkdir();
			Status.Theme.MainProgressBar.setIndeterminate(false);
			System.out.println("Downloading libraries");
			Status.Theme.MainProgressBar.setMaximum(Libraries.length());
			Status.Theme.MainProgressBar.setValue(0);
			ArrayList<DownloadObject> DownloadJobs=new ArrayList<>();
			for(int i=0;i<Libraries.length();i++){
				String LibrariesPath = (Libraries.getJSONObject(i).getString("name"));
				//LibrariesPath=LibrariesPath.replace(":",".");
				//LibrariesPath=LibrariesPath.substring(0,LibrariesPath.lastIndexOf(".")-1).replace(".",SplitChar);
				String[] SplitedPath = LibrariesPath.split(":");
				LibrariesPath = SplitedPath[0].replace(".", SplitChar);
				for(int j=1;j<SplitedPath.length;j++){
					LibrariesPath += (SplitChar+SplitedPath[j]);
				}
				//String[] SplitedPath = LibrariesPath.split(SplitChar);
				String FileURL="";
				//String downloadURL="";
				//NativesTail="-"+Status.getOS()
				String NativesTail=(JSONFunction.Search(Libraries.getJSONObject(i),"natives")&&
									JSONFunction.Search(Libraries.getJSONObject(i).getJSONObject("natives"),Status.getOS())?
											"-"+Libraries.getJSONObject(i).getJSONObject("natives").getString(Status.getOS()).replace("${arch}",System.getProperty("sun.arch.data.model")):"");
				FileURL = (LibrariesPath+SplitChar+SplitedPath[SplitedPath.length-2]+"-"+SplitedPath[SplitedPath.length-1]);
				FileURL += NativesTail;
				FileURL += (SplitedPath[SplitedPath.length-2].matches("[a-zA-z]*forge.*")?"-universal":"")+".jar"; //For Forge
				//FileURL += (JSONFunction.Search(Libraries.getJSONObject(i),"clientreq")&&Libraries.getJSONObject(i).getBoolean("clientreq")?".pack.xz":"");
				
				if(CheckLibPermission(Libraries.getJSONObject(i))){
					System.out.println("-- "+FileURL.replace("-universal",""));
					DownloadObject OneLibrary = new DownloadObject();
					//OneLibrary.DownloadServer=JSONFunction.Search(Libraries.getJSONObject(i),"url")?Libraries.getJSONObject(i).getString("url"):this.LibrariesBaseDownloadURL;
					OneLibrary.DownloadPath=((JSONFunction.Search(Libraries.getJSONObject(i),"url")?Libraries.getJSONObject(i).getString("url"):this.LibrariesBaseDownloadURL)+FileURL).replace(SplitChar,"/")+(JSONFunction.Search(Libraries.getJSONObject(i),"clientreq")&&Libraries.getJSONObject(i).getBoolean("clientreq")?".pack.xz":"");
					OneLibrary.FilePath=this.minecraftDir+"libraries"+SplitChar+FileURL.replace("-universal","");
					DownloadJobs.add(OneLibrary);
					//DownloadJobs.put(FileURL,JSONFunction.Search(Libraries.getJSONObject(i),"url")?Libraries.getJSONObject(i).getString("url"):this.LibrariesBaseDownloadURL);
					}else{
					System.out.println("Skip download: "+LibrariesDir+FileURL.replace("-universal",""));
				}
			}
			new ThreadJob().DownloadJob("Libraries",DownloadJobs);
			
			//------------------下載assets-------------------
			
			System.out.print("\n");
			Status.Theme.MainProgressBar.setIndeterminate(false);
			Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("LoadingAssets"));
			
			new ThreadJob().DownloadJob("AssetsIndex",new DownloadObject(AssetsIndexBaseDownloadURL+AssetsIndex+".json",AssetsDir+SplitChar+"indexes"+SplitChar+AssetsIndex+".json"));
			Status.Theme.MainProgressBar.setValue(0);
			Status.Theme.MainProgressBar.setIndeterminate(true);
			Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("LoadingAssets"));
			JSONObject AssetsIndexJSON = JSONFunction.CreateFromFile(this.minecraftDir+SplitChar+"assets"+SplitChar+"indexes"+SplitChar+AssetsIndex+".json");
			boolean Virtual = JSONFunction.Search(AssetsIndexJSON,"virtual")&&AssetsIndexJSON.getBoolean("virtual");
			HashMap<DownloadObject,Integer> DownloadAssets = new HashMap<>();
			for(String OneAssetName:JSONObject.getNames(AssetsIndexJSON.getJSONObject("objects"))){				
				String Hash = AssetsIndexJSON.getJSONObject("objects").getJSONObject(OneAssetName).getString("hash");
				
				//Objects->Virtual
				if(Virtual&&new File(AssetsDir+SplitChar+"objects"+SplitChar+Hash.substring(0,2)+SplitChar+Hash).isFile()){
					String OldFile=AssetsDir+SplitChar+"objects"+SplitChar+Hash.substring(0,2)+SplitChar+Hash;
					String NewFile=AssetsDir+SplitChar+"virtual"+SplitChar+AssetsIndex+SplitChar+OneAssetName;
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
				
				DownloadObject OneDownloadAsset=new DownloadObject();
				OneDownloadAsset.DownloadPath=(this.AssetsBaseDownloadURL+Hash.substring(0,2)+SplitChar+Hash).replace(SplitChar,"/");
				OneDownloadAsset.FilePath=
						(Virtual?AssetsDir+SplitChar+"virtual"+SplitChar+AssetsIndex+SplitChar:AssetsDir+SplitChar+"objects"+SplitChar)+
						(Virtual?OneAssetName.replace("/",SplitChar):Hash.substring(0,2)+SplitChar+Hash);
				DownloadAssets.put(OneDownloadAsset,AssetsIndexJSON.getJSONObject("objects").getJSONObject(OneAssetName).getInt("size"));
			}
			Status.Theme.MainProgressBar.setIndeterminate(false);
			
			//----------按檔案大小排序檔案-------------
			ArrayList<Entry<DownloadObject, Integer>> Sorter = new ArrayList<>(DownloadAssets.entrySet());
			Collections.sort(Sorter,new Comparator<Entry<DownloadObject,Integer>>(){
				@Override
				public int compare(Entry<DownloadObject, Integer> o1,Entry<DownloadObject, Integer> o2){
					return (o1.getValue()-o2.getValue());
				}	
			});
			
			ArrayList<DownloadObject> SortedAssets = new ArrayList<>();
			for(Entry<DownloadObject,Integer> OneDownloadObject:Sorter){
				SortedAssets.add(OneDownloadObject.getKey());
			}
			//-------------END 排序-----------------
			
			new ThreadJob().DownloadJob("Assets",20,SortedAssets);
			Status.Theme.MainProgressBar.setIndeterminate(true);
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
			Status.Theme.MainProgressBar.setIndeterminate(false);
			//------------------解壓,載入程式庫----------------
			File ExtractDir = new File(NativesDir+SplitChar);
			Status.Theme.MainProgressBar.setValue(0);
			Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("LoadingLib"));
			Status.Theme.MainProgressBar.setIndeterminate(true);
			if(!ExtractDir.exists() || !ExtractDir.isDirectory()){
				makeDir(NativesDir+SplitChar+(Status.getOS().equals("windows")?"\\":""));
				System.out.println("-Natives dir was created!");
			}
			LaunchCmd += " -Djava.library.path="+(NativesDir.indexOf(' ')>0?"\""+NativesDir+"\"":NativesDir);
			LaunchCmd += " -cp ";
			ArrayList<String> extractFiles = new ArrayList<String>();
			ArrayList<String> extractTargetDir = new ArrayList<String>();
			ArrayList<ArrayList<String>> extractExclude = new ArrayList<ArrayList<String>>();
			Status.Theme.MainProgressBar.setMaximum(Libraries.length());
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
					String NativesTail=(JSONFunction.Search(Libraries.getJSONObject(i),"natives")&&
										JSONFunction.Search(Libraries.getJSONObject(i).getJSONObject("natives"),Status.getOS())?
												"-"+Libraries.getJSONObject(i).getJSONObject("natives").getString(Status.getOS()).replace("${arch}",System.getProperty("sun.arch.data.model")):"");
					if(JSONFunction.Search(Libraries.getJSONObject(i), "extract")){
						//String[] FileList = (new File(minecraftDir+"libraries"+SplitChar+LibrariesPath).list());
						//System.out.println(FileList[0]);
						System.out.println("This library will be extracted: "+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+NativesTail);
						extractFiles.add(LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+NativesTail+".jar");
						extractTargetDir.add(NativesDir+SplitChar);
						ArrayList<String> excludeTemp=new ArrayList<String>();
						for(String OneExclude:JSONFunction.readArray(Libraries.getJSONObject(i).getJSONObject("extract").getJSONArray("exclude"))){
							excludeTemp.add(OneExclude);
						}
						extractExclude.add(excludeTemp);
					}else{
						System.out.println("Loading library: "+LibrariesPath+SplitChar+(temp[temp.length-2]+"-"+temp[temp.length-1])+NativesTail);
						String OneLibraryPath=LibrariesDir+(LibrariesPath+SplitChar+temp[temp.length-2]+"-"+temp[temp.length-1])+NativesTail+".jar";
						LaunchCmd += (OneLibraryPath.indexOf(' ')>0?"\""+OneLibraryPath+"\"":OneLibraryPath)+System.getProperty("path.separator");
					}
				}
			}
			System.out.println("----------");
			new ThreadJob().ExtractJob("Libraries",extractFiles,extractTargetDir,extractExclude);
			LaunchCmd += (MainJar.indexOf(' ')>0?"\""+MainJar+"\"":MainJar)+" "+MainClass;
		} catch (IOException e) {
			System.out.println("Launch Error!");
			e.printStackTrace();
		}
		
		Status.Theme.MainProgressBar.setString(Status.SelectedLang.getString("LaunchingGame"));
		Status.Theme.MainProgressBar.setIndeterminate(false);
		Status.Theme.MainProgressBar.setEnabled(false);
		GameDir=(GameDir.indexOf(' ')>0?"\""+GameDir+"\"":GameDir);
		AssetsDir=(AssetsDir.indexOf(' ')>0?"\""+AssetsDir+"\"":AssetsDir);
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
		try {
			System.out.println("\nRunning Command: "+LaunchCmd);
			System.out.println("--Launch Finished!");
			TimeCounter.stop();
			TimeCounter.count();
			Status.MainFrameObj.setVisible(false);
			//System.out.println(this.JVMPath+" -jar \""+URLEncoder.encode(System.getProperty("user.dir")+SplitChar+"RF-MCLauncher-B_v2.0a.jar","UTF-8")+"\"");
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
            		new JOptionPane().showMessageDialog(null,"<html><span style=\"color: red;\">"+Status.SelectedLang.getString("GameError")+"</span></html>",Status.SelectedLang.getString("Error"),JOptionPane.ERROR_MESSAGE);
            		break;
            	}
            }
            p_in.close();
            if(!Status.TestArguments("--NoDelNatives")){
            	deleteDir(new File(NativesDir+SplitChar));
            	System.out.println("Deleted Natives Dir!");
            }
            Status.MainFrameObj.dispose(); //結束程式
            System.exit(0);
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
		//System.out.println("Creating Dir: "+dir);
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
						if(Action.getJSONObject("os").getString("name").equals(Status.getOS())){
							FinalAction=true;
						}
					}else{
						FinalAction=true;
					}
				}else{ //disallow
					if(JSONFunction.Search(Action,"os")){
						if(Action.getJSONObject("os").getString("name").equals(Status.getOS())){
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
}