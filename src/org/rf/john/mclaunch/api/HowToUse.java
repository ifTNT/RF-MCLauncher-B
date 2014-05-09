package org.rf.john.mclaunch.api;

import java.util.HashMap;

import javax.swing.JProgressBar;

public class HowToUse {
	public static void main(String[] args){
		//建構式必須傳入一個實作Logger類別(在這裡是CustomLogger)
		Launcher launcher = new Launcher(new CustomLogger());
		for(String OneProfile:launcher.getInstalledProfiles()){ //getInstalledProfiles()可以取得已安裝的Profile,回傳值是HashSet<String>
			System.out.println(OneProfile);
		}
		System.out.println("\n"+launcher.selectedProfile()); //selectedProfile()可以取得最後一次選取的Profile名稱,回傳值是String
		//離線啟動遊戲,第一個參數是選擇的Profile名稱(String),
		//			 第二個是Minecraft的LastVersion(String,您可以用任何方式取得,或用我們提供的getLastVersion(),這個method會取得線上資料,所以離線時不能使用)
		//回傳值:遊戲啟動成功=true,失敗=false
		launcher.LaunchGameOffline(launcher.selectedProfile(),launcher.getLastVersion());
		
		//Login這個method很明顯的是在提供帳戶登入用的,傳入帳號,密碼,他就會回傳一個結果的HashMap<String,String>裡面有很多有用的東西
		HashMap<String,String> AuthResult = launcher.Login("user","password");
		
		//Status這個key會對應到一個驗證結果的value,這個value的值可能是"Server error","Bad login","Old version","Susses"
		//"Server error": 取得資料時發生莫名的錯誤
		//"Bad login": 帳號,密碼錯誤
		//"Old version": 啟動器版本過舊
		//"Susses": 登入成功(後面的所有值只有在Status="Success"時才會存在)
		System.out.println(AuthResult.get("Status"));
		if(AuthResult.get("Status").equals("Success")){
			System.out.println("UserName= "+AuthResult.get("UserName"));	//UserName: 玩家的顯示名稱
			System.out.println("Session= "+AuthResult.get("Session"));		//Session: 登入的token,等一下會用到
			System.out.println("UUID= "+AuthResult.get("UUID"));			//UUID： 我不知道這要幹嘛,但是官方登入器有,我就加上去了
			System.out.println("AccessToken= "+AuthResult.get("AccessToken")); //AccessToken: 同上
			
			//啟動遊戲(非離線),參數: 玩家名稱,選擇的Profile名稱,Session,LastVersion
			//回傳值:遊戲啟動成功=true,失敗=false
			launcher.LaunchGame(AuthResult.get("UserName"),launcher.selectedProfile(),AuthResult.get("Session"),launcher.getLastVersion());
		}
	}
}
class CustomLogger implements Logger{ //自訂的訊息紀錄器,必須在Launcher的建構式傳入
	JProgressBar progressBar=new CustomProgressBar(); //進度條,您可以傳入主視窗上的進度條,
	//如果不想用的話請將他設成
	//JProgressBar progressBar=new JProgressBar();
	
	@Override
	public void Info(String Msg) { //正常訊息顯示
		System.out.println(Msg);
	}

	@Override
	public void Warning(String Msg) { //警告訊息顯示
		System.out.println("*"+Msg+"*");
	}

	@Override
	public void Error(String Msg) { //錯誤訊息顯示
		System.out.println("--*"+Msg+"*--");
	}

	@Override
	public void Error(String Msg, Exception e) { //錯誤訊息顯示
		System.out.println("--*"+Msg+"*--");
		e.printStackTrace();
	}

	@Override
	public void DeadlyError(String Msg) { //致命錯誤訊息顯示(必要時需停止程式)
		System.out.println("Deadly error:");
		System.out.println(Msg);
		System.exit(0);
	}

	@Override
	public void FinishCallback() { //啟動完成,遊戲準備啟動時所呼叫的callback(可以再這裡將主視窗隱藏)
		System.out.println("Got it!");
	}
	
	@Override
	public void TheEndCallback() { //啟動完成,遊戲準備啟動時所呼叫的callback(可以再這裡將主視窗隱藏)
		System.out.println("Game Finished");
	}
}
class CustomProgressBar extends JProgressBar{ //自定義進度條
	@Override
	public void setString(String msg){ //因為沒有視窗,所以只好將進度打在console上
		System.out.println(msg);
	}
}