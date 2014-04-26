package org.rf.john.mclaunch.api;

import javax.swing.JProgressBar;

public interface Logger {
	JProgressBar progressBar=new JProgressBar();
	void Info(String Msg);
	void Warning(String Msg);
	void Error(String Msg);
	void Error(String Msg,Exception e);
	void DeadlyError(String Msg);
	void FinishCallback();  //啟動完成時呼叫的callback
}
