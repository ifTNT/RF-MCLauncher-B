package org.rf.john.mclauncher.Themes;

import java.awt.Color;

import org.json.JSONObject;
import org.rf.john.mclauncher.LaunchPanel;


public class Theme extends LaunchPanel{
	public String Author="";
	public String ThemeName="";
	public String Description="";
	public JSONObject Detail=new JSONObject();
	
	/**
	 * 設定視窗大小
	 * @param w 寬
	 * @param h 長
	 */
	protected final void SetFrameSize(int w,int h){this.FrameSize[0]=w;this.FrameSize[1]=h;}
	/**
	 * 設定背景(圖片)
	 * @param imgurl 圖片路徑
	 * @param StartX 開始繪製的x座標
	 * @param StartY 開始繪製的y座標
	 * @param width  寬
	 * @param height 長
	 */
	protected final void SetBackground(String imgurl,int StartX,int StartY,int width,int height){
		this._SetBackground(imgurl,StartX,StartY,width, height);
	}
	
	/**
	 * 設定背景(圖片)
	 * @param imgurl 圖片路徑
	 * @param width  寬
	 * @param height 長
	 */
	protected final void SetBackground(String imgurl,int width,int height){
		this._SetBackground(imgurl,0,0,width, height);
	}
	
	/**
	 * 設定背景(純色)
	 * @param ColorCode 背景顏色代碼
	 */
	protected final void SetBackground(Color ColorCode){
		this._SetBackground(ColorCode,FrameSize[0],FrameSize[1]);
	}
	
	/**
	 * 佈景初始設定
	 * 1.作者名稱,佈景名稱,佈景介紹
	 * 2.視窗大小
	 * 3.背景設定
	 * 4.說明文字樣式設定
	 */
	public Theme(){}
	
	/**
	 * 元件排版
	 */
	public void BuildFrame(){}
}
