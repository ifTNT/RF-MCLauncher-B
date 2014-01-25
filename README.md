RF-MCLauncher-B
===============

一個非官方的開源Minecraft啟動器<br>
使用GPLv3進行授權 請詳閱相關條款<br>
<br>
程式設計: 曙@RasFun

##ChangLog
#####1.0.0---初版

#####1.0.1rc2
    *修正嚴重Bug--啟動SessionID錯誤(RFBug#000)

#####2.0a
    *改變語言引擎
    *將英文語系的Password改為P.W
    +允許自定義語言包
    *更改原始碼結構
      └ *將MainFrame,SelectedLang,Launcher定義為static以減少參數傳遞
      └ -移除空迴圈
      └ *展開原始碼
      └ *將mian改為Main ,以減少誤會
      └ -廢除MainFrame,ImagePaenl和LangType
      └ *將LangType改為使用HashMap實作
      └ +新增RFInfo來管理全域變數
      └ *將Launcher合併
      └ *將根套件改為org.rf.john
    +支援1.7
    +支援自定義參數
    *增加紀錄檔的可讀性
    -移除SessionID顯示 以保護帳號隱私
    *修正不支援中文路徑的Bug(RFBug#001)
    *修正不支援Forge的Bug(RFBug#002)
    *將介面改為OakTheme
    +加入o0o
    +可自動取得LastVersion
    +可自動判斷適合系統的語系
    +允許使用Apache Ant進行開發
    *將Forge的程式庫改為.pack.xz下載 以加快速度
    +引用tukaani的xz java-1.4程式庫
    *改進下載的執行緒分配
    *如果欲下載的檔案存在 則不下載
    +支援Assets下載
    -不再將程式庫刪除,重下載 只下載沒有的程式庫
    *將ant的jar壓縮等級變成9
    +將ant debug的錯誤導向到launch.err
    *更改Windows的NativesTail
    -清理部份無用的原始碼
    *改用ant作為jar佈署工具(之前是用Eclipsc)
    +下載檔案中關閉視窗時 會詢問
    * 精簡程式碼
    +遊戲主程式不存在時,會自動下載

##參數
- --NoDelNatives : 不要刪除Natives資料夾(除錯用)
- --NoUpdate : 不檢查更新(暫時棄用)

##未來
- 自訂義佈景(外觀) (製作中...)
- ModOak (模組安裝器)
- 錯誤Log分析
- 自動優化啟動參數
- 可編輯Profile
- 支援XP,Mac(OSX)
- 可完全取代官方啟動器

##Bugs
- RFBug#000 啟動SessionID錯誤(1.0-rc2已解決)
- RFBug#001 不支援中文路徑(2.0a已解決)
- RFBug#002 不支援Forge(2.0a已解決)