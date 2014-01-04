package org.rf.john.mclauncher;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
				System.out.println(RFInfo.SelectedLang.getString("NoNimbus"));
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception ex) {
			}
		}
		//---------------------------
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setResizable(false);
		ImageIcon icon;
		if(RFInfo.RunMode.equals(RunType.Debug)){
			icon = new ImageIcon("images/icon.png");  //Debug
		}else{
			icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icon.png")));  /******JAR******/
		}
		this.setIconImage(icon.getImage());
		//----------------------------
		RFInfo.Theme = new OakTheme();
		this.setBounds(300,200,RFInfo.Theme.FrameSize[0],RFInfo.Theme.FrameSize[1]);
		Save_LoadConfig config = new Save_LoadConfig();
		RFInfo.Theme.SetValue((config.Logined()?config.LoadUser():""),(config.Logined()?config.LoadPassword():""),RFInfo.Launcher.getInstalledProfiles(),config.Logined(),false);
		RFInfo.Theme.BuildFrame();
		this.InstalledLangs = new Languages().InstalledLang();
		if(this.InstalledLangs.isEmpty()){
			new JOptionPane().showMessageDialog(null,"<html><span style=\"color: red;\">沒有任何語系被安裝</span></html>","錯誤",JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
		for(Entry<String,String> entry:InstalledLangs.entrySet()){ //設定預設語系
			if(entry.getKey().equals(RFInfo.SelectedLang.LangFile)){
				SelectLang(RFInfo.SelectedLang.LangFile);
				break;
			}
		}
		new Languages().PrintInstalledLang(InstalledLangs);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		contentPane.add(RFInfo.Theme);
		setTitle(RFInfo.SelectedLang.getString("Title"));
		RFInfo.Theme.LoginBtn.setEnabled(false); //凍結登入按鈕
		RFInfo.Launcher.SetLastVersion();
		System.out.println("Minecraft Last Version: "+RFInfo.Launcher.LastVersion+"\n"); //LastVersion
		Login.checkConnect();
		RFInfo.Theme.ProfileSelectBox.setEnabled(Login.canConnect());
		RFInfo.Theme.LoginBtn.setText(RFInfo.Theme.TextStyles.get("LoginBtn").replace("${TEXT}",(Login.canConnect()?RFInfo.SelectedLang.getString("LoginBtn"):RFInfo.SelectedLang.getString("LaunchOffline"))));
		RFInfo.Theme.LoginBtn.setEnabled(true); //解凍
	}
	
	/**
	 * 設定/選擇語系
	 * @param LangFilePath 語系檔名稱
	 */
	private void SelectLang(String LangFilePath){
		RFInfo.SelectedLang = new Languages().SelectLanguages(LangFilePath);
		setTitle(RFInfo.SelectedLang.getString("Title"));
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu SittingsMenu = new JMenu(RFInfo.SelectedLang.getString("SittingTabTitle"));
		menuBar.add(SittingsMenu);
		
		JMenu SetJVMPath = new JMenu(RFInfo.SelectedLang.getString("JVMPathText"));
		SittingsMenu.add(SetJVMPath);
		
		Save_LoadConfig config = new Save_LoadConfig();
		JVMPathText = new JTextField(config.Saved()?config.getJVMPath():RFInfo.Launcher.JVMPath,1);
		SetJVMPath.add(JVMPathText);
		JVMPathText.setColumns(10);
		
		JMenu SetMinecraftPath = new JMenu(RFInfo.SelectedLang.getString("MinecraftDirText"));
		SittingsMenu.add(SetMinecraftPath);
		
		MinecraftPathText = new JTextField(config.Saved()?config.getMinecraftPath():RFInfo.Launcher.minecraftDir,1);
		SetMinecraftPath.add(MinecraftPathText);
		MinecraftPathText.setColumns(10);
		
		new Save_LoadConfig().saveConfig(JVMPathText.getText(),MinecraftPathText.getText(),LangFilePath,RFInfo.Launcher.LastVersion);
		
		JMenu SetLanguage = new JMenu(RFInfo.SelectedLang.getString("LangText"));
		menuBar.add(SetLanguage);
				
		/*JMenu ThemeSelectMenu = new JMenu(RFInfo.SelectedLang.getString("Theme"));
		menuBar.add(ThemeSelectMenu);*/
		
		JMenuItem About = new JMenuItem(RFInfo.SelectedLang.getString("About"));
		menuBar.add(About);
		About.addActionListener(new ActionListener(){
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new JOptionPane().showMessageDialog(null,
						"<html><span style=\"font-size: 18px;color: rgb(40%,40%,40%);font-weight:bold\">"+RFInfo.SelectedLang.getString("AboutText1")+"</span></html>\n"+
						"<html><span style=\"font-size: 12px;color: rgb(30%,30%,30%);\">"+RFInfo.SelectedLang.getString("AboutText2")+"</span></html>\n"+
						"<html><span style=\"font-size: 12px;color: rgb(30%,30%,30%);\">"+RFInfo.SelectedLang.getString("AboutText3")+"</span></html>\n"+
						"<html><span style=\"font-size: 10px;color: rgb(20%,20%,20%);\">version: "+RFInfo.Version+"</span></html>\n"+
						"----------------------------------------\n" +
						"<html><span style=\"font-size: 15px;color: rgb(40%,40%,40%);font-weight:bold\">"+RFInfo.SelectedLang.getString("About")+RFInfo.SelectedLang.getString("Theme")+":</span></html>\n"+
						"<html><span style=\"font-size: 12px;color: rgb(30%,30%,30%);\">"+RFInfo.SelectedLang.getString("Author")+": "+RFInfo.Theme.Author+"</span></html>\n"+
						"<html><span style=\"font-size: 12px;color: rgb(30%,30%,30%);\">"+RFInfo.SelectedLang.getString("ThemeName")+": "+RFInfo.Theme.ThemeName+"</span></html>\n"+
						"<html><span style=\"font-size: 12px;color: rgb(30%,30%,30%);\">"+RFInfo.SelectedLang.getString("Description")+": "+RFInfo.Theme.Description+"</span></html>\n"
						,RFInfo.SelectedLang.getString("About"),JOptionPane.INFORMATION_MESSAGE);
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
			OneLangBtn.setSelected(OneLang.getValue().equals(RFInfo.SelectedLang.getString("LangName")));
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
		RFInfo.Theme.ChangeLang();
	}
}