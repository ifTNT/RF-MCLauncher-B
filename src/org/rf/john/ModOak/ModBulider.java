package org.rf.john.ModOak;

import java.net.URL;

public class ModBulider{
	public boolean Bulid(){return false;} //安裝模組的方法
	public String getOS(){ //取得作業系統
		if(System.getProperty("os.name").toLowerCase().indexOf("win")>=0){
			return "win";
		}else if(System.getProperty("os.name").toLowerCase().indexOf("nix")>=0 || System.getProperty("os.name").toLowerCase().indexOf("nux")>=0){
			return "unix";
		}else{
			return "unknow";
		}
	}
	public String DownloadFile(URL DownloadURL){ //下載檔案
		return null;
	}
	public boolean Extract(String SourceFile,String TargetDir){ //解壓縮檔案
		
		return false;
	}
	public boolean FileToZip(String TargetFile,String SourceFile,String BasePath){ //移動檔案進壓縮檔
		return false;
	}
	public boolean MoveFile(String Source,String Target){ //移動檔案
		return false;
	}
	public boolean MoveFile(String Source,String Target,boolean mkdir){ //移動檔案(資料夾不存在時是否建立資料夾)
		return false;
	}
	public String GetMinecraftDir(){ //取的Minecraft根目錄
		return null;
	}
	public void SetMaxProgress(int x){ //設定最大進度
		
	}
	public void SetProgress(int x){ //設定進度
		
	}
	public void SetProgressString(String s){ //設定進度條上的字
		
	}
	public void LaunchAndCloseGame(){ //啟動遊戲後立刻關閉遊戲
		
	}
}
