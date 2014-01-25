package org.rf.john.mclauncher;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;

import javax.swing.BoxLayout;

import org.rf.john.mclauncher.Themes.OakTheme;
import org.rf.john.mclauncher.langs.Languages;


@SuppressWarnings("serial")
public class MainFrame2 extends JFrame {

	private JPanel contentPane;
	public JTextField JVMPathText;
	public JTextField MinecraftPathText;
	public HashMap<String,String> InstalledLangs;
	public ButtonGroup SelectLangBtnGroup;
	
	@SuppressWarnings("static-access")
	public MainFrame2() {
		//------Set Nimbus L&F--------
		try {
			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			try {
				System.out.println(Status.SelectedLang.getString("NoNimbus"));
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception ex) {}
		}
		//---------------------------
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		ImageIcon icon;
		if(Status.RunMode.equals(RunType.Debug)){
			icon = new ImageIcon("images/icon.png");  //Debug
		}else{
			icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icon.png")));  /******JAR******/
		}
		this.setIconImage(icon.getImage());
		//----------------------------
		Status.Theme = new OakTheme();
		this.setBounds(300,200,Status.Theme.FrameSize[0],Status.Theme.FrameSize[1]);
		Save_LoadConfig config = new Save_LoadConfig();
		Status.Theme.SetValue((config.Logined()?config.LoadUser():""),(config.Logined()?config.LoadPassword():""),Status.Launcher.getInstalledProfiles(),config.Logined(),false);
		Status.Theme.BuildFrame();
		this.InstalledLangs = new Languages().InstalledLang();
		if(this.InstalledLangs.isEmpty()){
			new JOptionPane().showMessageDialog(null,"<html><span style=\"color: red;\">沒有任何語系被安裝</span></html>","錯誤",JOptionPane.ERROR_MESSAGE);
			this.dispose();
			System.exit(1);
		}
		/*for(Entry<String,String> entry:InstalledLangs.entrySet()){ //設定預設語系
			if(entry.getKey().equals(RFInfo.SelectedLang.LangFile)){
				SelectLang(RFInfo.SelectedLang.LangFile);
				break;
			}
		}*/
		if(InstalledLangs.containsKey(Status.SelectedLang.LangFile)) SelectLang(Status.SelectedLang.LangFile); //設定預設語系
		new Languages().PrintInstalledLang(InstalledLangs);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		contentPane.add(Status.Theme);
		setTitle(Status.SelectedLang.getString("Title"));
		Status.Theme.LoginBtn.setEnabled(false); //凍結登入按鈕
		Status.Launcher.SetLastVersion();
		System.out.println("Minecraft Last Version: "+Status.Launcher.LastVersion+"\n"); //LastVersion
		//Login.checkConnect();
		System.out.println("Launcher detail: \n"+Status.Launcher.toString()+"\n");
		Status.Theme.ProfileSelectBox.setEnabled(Login.canConnect());
		Status.Theme.LoginBtn.setText(Status.Theme.TextStyles.get("LoginBtn").replace("${TEXT}",(Login.canConnect()?Status.SelectedLang.getString("LoginBtn"):Status.SelectedLang.getString("LaunchOffline"))));
		Status.Theme.LoginBtn.setEnabled(true); //解凍
		
		this.addWindowListener(new WindowAdapter(){
			@Override
		    public void windowClosing(WindowEvent e){
		        if(Status.Busy){
		        	if(new JOptionPane().showConfirmDialog(null,"<html><span style=\"color: RED;\">"+Status.SelectedLang.getString("CloseWindowOnBusy")+"</span></html>",Status.SelectedLang.getString("Error"),JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION){
		        		e.getWindow().dispose();
		        		System.exit(0);
		        	}
		        }else{
		        	e.getWindow().dispose();
	        		System.exit(0);
		        }
		    }
		});
	}
	
	/**
	 * 設定/選擇語系
	 * @param LangFilePath 語系檔名稱
	 */
	private void SelectLang(String LangFilePath){
		Status.SelectedLang = new Languages().SelectLanguages(LangFilePath);
		setTitle(Status.SelectedLang.getString("Title"));
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu SittingsMenu = new JMenu(Status.SelectedLang.getString("SittingTabTitle"));
		menuBar.add(SittingsMenu);
		
		JMenu SetJVMPath = new JMenu(Status.SelectedLang.getString("JVMPathText"));
		SittingsMenu.add(SetJVMPath);
		
		Save_LoadConfig config = new Save_LoadConfig();
		JVMPathText = new JTextField(config.Saved()?config.getJVMPath():Status.Launcher.JVMPath,1);
		SetJVMPath.add(JVMPathText);
		JVMPathText.setColumns(10);
		
		JMenu SetMinecraftPath = new JMenu(Status.SelectedLang.getString("MinecraftDirText"));
		SittingsMenu.add(SetMinecraftPath);
		
		MinecraftPathText = new JTextField(config.Saved()?config.getMinecraftPath():Status.Launcher.minecraftDir,1);
		SetMinecraftPath.add(MinecraftPathText);
		MinecraftPathText.setColumns(10);
		
		new Save_LoadConfig().saveConfig(JVMPathText.getText(),MinecraftPathText.getText(),LangFilePath,Status.Launcher.LastVersion);
		
		JMenu SetLanguage = new JMenu(Status.SelectedLang.getString("LangText"));
		menuBar.add(SetLanguage);
				
		/*JMenu ThemeSelectMenu = new JMenu(RFInfo.SelectedLang.getString("Theme"));
		menuBar.add(ThemeSelectMenu);*/
		
		JMenuItem About = new JMenuItem(Status.SelectedLang.getString("About"));
		menuBar.add(About);
		About.addActionListener(new ActionListener(){
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new JOptionPane().showMessageDialog(null,
						"<html><span style=\"font-size: 18px;color: rgb(40%,40%,40%);font-weight:bold\">"+Status.SelectedLang.getString("AboutText1")+"</span></html>\n"+
						"<html><span style=\"font-size: 12px;color: rgb(30%,30%,30%);\">"+Status.SelectedLang.getString("AboutText2")+"</span></html>\n"+
						"<html><span style=\"font-size: 12px;color: rgb(30%,30%,30%);\">"+Status.SelectedLang.getString("AboutText3")+"</span></html>\n"+
						"<html><span style=\"font-size: 10px;color: rgb(20%,20%,20%);\">version: "+Status.Version+"</span></html>\n"+
						"----------------------------------------\n" +
						"<html><span style=\"font-size: 15px;color: rgb(40%,40%,40%);font-weight:bold\">"+Status.SelectedLang.getString("About")+Status.SelectedLang.getString("Theme")+":</span></html>\n"+
						"<html><span style=\"font-size: 12px;color: rgb(30%,30%,30%);\">"+Status.SelectedLang.getString("Author")+": "+Status.Theme.Author+"</span></html>\n"+
						"<html><span style=\"font-size: 12px;color: rgb(30%,30%,30%);\">"+Status.SelectedLang.getString("ThemeName")+": "+Status.Theme.ThemeName+"</span></html>\n"+
						"<html><span style=\"font-size: 12px;color: rgb(30%,30%,30%);\">"+Status.SelectedLang.getString("Description")+": "+Status.Theme.Description+"</span></html>\n"
						,Status.SelectedLang.getString("About"),JOptionPane.INFORMATION_MESSAGE);
			}
		});
		this.InstalledLangs = new Languages().InstalledLang();
		SelectLangBtnGroup=new ButtonGroup();
		if(this.InstalledLangs.isEmpty()){
			new JOptionPane().showMessageDialog(null,"<html><span style=\"color: red;\">沒有任何語系被安裝</span></html>","錯誤",JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
		for(final Entry<String,String> OneLang:InstalledLangs.entrySet()){
			JRadioButtonMenuItem OneLangBtn=new JRadioButtonMenuItem(OneLang.getValue());
			OneLangBtn.setSelected(OneLang.getKey().equals(Status.SelectedLang.LangFile));
			OneLangBtn.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.out.println("Chang language to "+OneLang.getValue());
					SelectLang(OneLang.getKey());
				}
			});
			SetLanguage.add(OneLangBtn);
			SelectLangBtnGroup.add(OneLangBtn);
		}
		Status.Theme.ChangeLang();
	}
}
