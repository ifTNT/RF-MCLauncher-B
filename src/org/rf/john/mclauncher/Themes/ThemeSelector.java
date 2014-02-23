package org.rf.john.mclauncher.Themes;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.json.JSONException;
import org.json.JSONObject;
import org.rf.john.mclauncher.JSONFunction;
import org.rf.john.mclauncher.RunModeUtil;
import org.rf.john.mclauncher.Status;

public class ThemeSelector {
	/*public void SelectTheme(String SelectedTheme){
		Theme NewTheme = new Theme();
		Status.Theme = NewTheme;
		Status.MainFrameObj.setSize(NewTheme.FrameSize[0],NewTheme.FrameSize[1]);
		
	}*/
	/*public HashMap<String,Theme> getInstalledTheme(){
		HashMap<String,Theme> ReturnVal = new HashMap<>();
		try {
			HashSet<JarFile> ThemeJars = new HashSet<>();
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
					System.out.println(OneFile.getName());
					if(OneFile.getName().matches("Themes/.+\\.jar")){ //取得所有Theme的jar檔
						System.out.println(this.getClass().getClassLoader().getResource(OneFile.getName()).getFile());
						ThemeJars.add(new JarFile(this.getClass().getClassLoader().getResource(OneFile.getName()).getFile()));
					}
				}
				jarFile.close();	
			}else{
				String ThemeDir= System.getProperty("user.dir")+File.separator+"Themes"+File.separator;
				ThemeDir=URLDecoder.decode(ThemeDir,"UTF-8");
				
				String[] InstalledThemes = new File(ThemeDir).list(new FilenameFilter() { //--┐
					public boolean accept(File directory, String fileName) { //				  |
						return fileName.endsWith(".jar");  //							      |
					} //							  										  |
				}); //取的Theme資料夾下的所有Theme-----------------------------------------------┘
				for(String OneThemeJar:InstalledThemes){
					ThemeJars.add(new JarFile(ThemeDir+OneThemeJar));
				}
			}
			Iterator<JarFile> Jars = ThemeJars.iterator();
			while(Jars.hasNext()){
				JarFile OneThemeJar=Jars.next();
				if(OneThemeJar.getEntry("Theme.json")==null){
					System.out.println("--*Get Theme.json error*--");
					return null;
				}
				JSONObject ThemeJson = JSONFunction.CreateFromStream(OneThemeJar.getInputStream(OneThemeJar.getEntry("Theme.json")));
				System.out.println(ThemeJson.getString("name"));
				//System.out.println("jar:file:"+OneThemeJar.getName()+"!/");
				ClassLoader cl = URLClassLoader.newInstance(new URL[]{new URL("jar:file:"+OneThemeJar.getName()+"!/")});
				Class<?> clazz = cl.loadClass(ThemeJson.getString("main"));
				Theme OneThemeObj=(Theme)clazz.newInstance();
				OneThemeObj.Detail=ThemeJson;
				ReturnVal.put(ThemeJson.getString("name"),OneThemeObj);
			}
		} catch (IOException e) {
			System.out.println("--*Get installed themes error!!*--");
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			System.out.println("--*Load Theme's Class Error!*--");
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("--*JSON Error!*--");
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ReturnVal;
	}
	/*public HashMap<String,String> getThemeDetail(Object TargetTheme){
		if(!(TargetTheme instanceof Theme)){
			System.out.println("--*Get theme detail error*--");
			System.out.println("TargetTheme not instanceof Theme");
			return null;
		}
		HashMap<String,String> ThemeDetail=new HashMap<>();
		ThemeDetail.put("","");
		return ThemeDetail;
	}
	public String getThemeName(Object TargetTheme){
		if(!(TargetTheme instanceof Theme)){
			System.out.println("--*Get theme name error*--");
			System.out.println("TargetTheme not instanceof Theme");
			return null;
		}
		
		return ((Theme)TargetTheme).ThemeName;
	}*/
}
