RF-MCLauncher-B
===============

一個非官方的開源Minecraft啟動器api  
使用GPLv3進行授權 請詳閱相關條款  
  
程式設計: 曙@RasFun

##ChangLog
請到Master看

##Buliding
#####請先安裝Apache ant
* 編譯 `ant build`
* 包成jar檔 `ant jar`
* 看範例 `ant demo`

##How to use
####Method
使用範例打在`HowToUse.java`裡,在這裡我先將我們所提供的method講一下:  
* `Launcher Launcher(Logger)` 建構式,需傳入一個實作Logger的類別  
* `HashSet<String> getInstalledProfiles()` 取得已安裝的Profile  
* `String selectedProfile()` 取得最後一次所選取的Profile  
* `String getLastVersion()` 取得Minecraft的最後版本,離線時不可用(回傳null)  
* `boolean checkLoginServer()` 檢查登入伺服器是否上線  
* `HashMap<String,String> Login(String user,String pw)` 取得正版的帳號資訊,傳入帳號,密碼,回傳驗證結果  
    * `Status` 驗證結果  
      ("Server error"=資料取得錯誤,"Bad login"=帳密錯誤,"Old version"=啟動器版本過舊,"Susses"=驗證成功)

    `(接下來的value只有Status="Succes"時會出現)`
    * `UserName` 玩家的顯示名稱
    * `Session` 啟動時會用到
    * `UUID` 沒有用
    * `AccessToken` 沒有用
  
* `boolean LaunchGameOffline(String Profile,String LastVersion)`  
離線啟動遊戲,傳入:選取的啟動檔,LastVersion,回傳:啟動成功=true,失敗=false  
* `boolean LaunchGameOffline(String Profile,String LastVersion,HashMap<String,String> options)`  
同上,多出來的那個參數可以定義啟動器(定義的參數請參考下方)  
* `boolean LaunchGame(String PlayerName,String Profile,String Session,String LastVersion)`  
正常的啟動遊戲,傳入:玩家的顯示名稱,選取的啟動檔,Session,LastVersion,回傳:啟動成功=true,失敗=false  
* `boolean LaunchGame(String PlayerName,String Profile,String Session,String LastVersion,HashMap<String,String> options)`  
同上,多出來的那個參數可以定義啟動器(定義的參數請參考下方)  
  
*******
####Logger
Logger是一個被我們特別設計,用來傳遞訊的一個介面,您必須實作他:  
* `void Info(String Msg)` 一般訊息的顯示  
* `void Warning(String Msg)` 警告訊息的顯示  
* `void Error(String Msg)` 錯誤訊息的顯示  
* `void Error(String Msg,Exception e)` 錯誤訊息的顯示(帶有Exception)  
* `void DeadlyError(String Msg)` 會導致啟動器不能繼續的錯誤訊息  
* `void FinishCallback()` 啟動結束,遊戲準備開始時所呼叫的callback  
* `void TheEndCallback()` 遊戲結束時所呼叫的callback  
* `JProgressBar progressBar` 請傳入主視窗的進度條元件,如果沒有使用,請將他設成`JProgressBar progressBar=new JProgressBar()` 
 
##Options
<table>
  <tbody>
    <tr>
      <td>&lt;key></td>
      <td>&lt;value></td>
      <td>&lt;description></td>
    </tr>
    <tr>
      <td>NoDeleteNatives</td>
      <td>true/false</td>
      <td>啟動完成後是否刪除Natives資料夾(true=不刪除,false=刪除)</td>
    </tr>
  </tbody>
</table>