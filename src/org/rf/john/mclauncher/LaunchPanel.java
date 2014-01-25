package org.rf.john.mclauncher;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;

import javax.swing.SwingConstants;

public class LaunchPanel extends JPanel {
	
	public int[] FrameSize=new int[2];  //視窗大小[w,h]
	public JButton LoginBtn=new JButton();  //登入按鈕
	public JTextField UserBox=new JTextField(); //帳號輸入框
	public JPasswordField PwdBox=new JPasswordField(); //密碼輸入框
	public JCheckBox RememberMe=new JCheckBox(); //記住我
	public JComboBox<String> ProfileSelectBox=new JComboBox<>(); //啟動檔選擇
	public JLabel UserText=new JLabel();  //帳號輸入框說明文字
	public JLabel PwdText=new JLabel();   //密碼輸入框說明文字
	public JLabel ProfileText=new JLabel();  //啟動檔選擇說明文字
	public JProgressBar MainProgressBar=new JProgressBar(); //進度條
	public HashMap<String,String> TextStyles=new HashMap<String,String>();  //說明文字樣式設定
	
	public LaunchPanel() {} //建構式
	
	/**
	 * 設定初始值
	 * @param User 帳號
	 * @param Pwd 密碼
	 * @param Profiles 已選的啟動檔
	 * @param rememberme 記住我
	 * @param launcheOffline 是否離線啟動
	 */
	public final void SetValue(String User,String Pwd,ArrayList<String> Profiles,boolean rememberme,boolean launcheOffline){
		this.UserBox = new JTextField(User,1);
		this.PwdBox = new JPasswordField(Pwd,1);
		String ProfilesName[]=new String[Profiles.size()];
		System.out.println("Loaded "+Status.Launcher.getInstalledProfiles().size()+" profiles from launcher_profiles.json");
		if(!TextStyles.containsKey("ProfileFormat")){System.out.println("--*Theme Error:Undefined TextStyle*--");return;}
		for(int i=0;i < Profiles.size();i++){
			ProfilesName[i] = TextStyles.get("ProfileFormat").replace("${TEXT}",Profiles.get(i));
		}
		this.ProfileSelectBox = new JComboBox<>(ProfilesName);
		this.ProfileSelectBox.setSelectedItem(TextStyles.get("ProfileFormat").replace("${TEXT}",Status.Launcher.selectedProfile()));
		this.RememberMe.setSelected(rememberme);
		this.ProfileSelectBox.setEnabled(launcheOffline);
		this.MainProgressBar.setStringPainted(true);
		this.MainProgressBar.setString(Status.SelectedLang.getString("MainProgressBarIdleText"));
		this.MainProgressBar.setEnabled(false);
		this.LoginBtn.addActionListener(new ActionListener(){ //登入按鈕監聽
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource().equals(LoginBtn)){
					launcheThread lt = new launcheThread(); //實體化啟動執行緒
					lt.start();
				}
			}
		});
		UserText.setHorizontalAlignment(SwingConstants.RIGHT);
		PwdText.setHorizontalAlignment(SwingConstants.RIGHT);
		ProfileText.setHorizontalAlignment(SwingConstants.RIGHT);
	}
	
	/**
	 * 變更語系(需先設定RFInfo.SelectedLang)
	 */
	public final void ChangeLang(){
		if(!(TextStyles.containsKey("UserText")&&
			TextStyles.containsKey("PwdText")&&
			TextStyles.containsKey("ProfilesText")&&
			TextStyles.containsKey("RememberMe")&&
			TextStyles.containsKey("LoginBtn")&&
			TextStyles.containsKey("ProfileFormat"))
		){System.out.println("--*Theme Error:Undefined TestStyle*--");return;}
		
		UserText.setText(TextStyles.get("UserText").replace("${TEXT}",Status.SelectedLang.getString("UserText")));
		PwdText.setText(TextStyles.get("PwdText").replace("${TEXT}",Status.SelectedLang.getString("PwdText")));
		ProfileText.setText(TextStyles.get("ProfilesText").replace("${TEXT}",Status.SelectedLang.getString("VersionText")));
		RememberMe.setText(TextStyles.get("RememberMe").replace("${TEXT}",Status.SelectedLang.getString("RememberMe")));
		LoginBtn.setText(TextStyles.get("LoginBtn").replace("${TEXT}",(Login.canConnect()?Status.SelectedLang.getString("LoginBtn"):Status.SelectedLang.getString("LaunchOffline"))));
		MainProgressBar.setString(Status.SelectedLang.getString("MainProgressBarIdleText"));
		
		if(Status.getOS().equals("windows")){
			String FontList[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			Arrays.sort(FontList);
			if(Arrays.binarySearch(FontList,"微軟正黑體")>-1){
				System.out.println("Set Font to: 微軟正黑體");
				Font font = new Font("微軟正黑體",Font.BOLD,12);
				UserText.setFont(font);
				PwdText.setFont(font);
				ProfileText.setFont(font);
				RememberMe.setFont(font);
				LoginBtn.setFont(font);
				MainProgressBar.setFont(font);
			}
		}
	}
	
	//----Image Panel-------
	private Image image;
    private int imgwidth;
    private int imgheight;
    private int startx;
    private int starty;
    private boolean DrawImage=true;
    private Color color=Color.WHITE;
    
	public void _SetBackground(String imgurl,int width,int height) {
        try {
        	if(Status.RunMode.equals(RunType.Debug)){
        		image = ImageIO.read(new File(imgurl));  //Debug
        	}else{
        		this.getClass().getClassLoader();
				image = ImageIO.read(ClassLoader.getSystemResourceAsStream(imgurl));  //***JAR***
        	}
            imgwidth = width;
            imgheight = height;
            startx=0;
            starty=0;
            DrawImage=true;
        } catch (IOException ex) {
        	System.out.println("--*Theme Error: SetBackgroundError*--");
            ex.printStackTrace();
        }
    }
    //@SuppressWarnings("static-access")
    public void _SetBackground(String imgurl,int StartX,int StartY,int width,int height) {
        try {
        	if(Status.RunMode.equals(RunType.Debug)){
        		image = ImageIO.read(new File(imgurl));  //Debug
        	}else{
        		this.getClass().getClassLoader();
        		image = ImageIO.read(ClassLoader.getSystemResourceAsStream(imgurl));  //***JAR***
        	}
            imgwidth = width;
            imgheight = height;
            startx=StartX;
            starty=StartY;
            DrawImage=true;
        } catch (IOException ex) {
        	System.out.println("--*Theme Error: SetBackgroundError*--");
            ex.printStackTrace();
        }
    }
    
    public void _SetBackground(Color colorcode,int width,int height) {
        DrawImage=false;
        imgwidth=width;
        imgheight=height;
        color=colorcode;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(DrawImage){
        	g.setColor(color);
        	g.fillRect(0,0,FrameSize[0],FrameSize[1]);
        	g.drawImage(image,startx,starty, imgwidth, imgheight, this);
        }else{
        	g.setColor(color);
        	g.fillRect(0,0,imgwidth,imgheight);
        }
    }
}