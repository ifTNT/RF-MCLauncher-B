package org.rf.john.ModOak;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import org.rf.john.mclauncher.langs.Languages;


import java.awt.Component;
import javax.swing.JToggleButton;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField OptionsField;
	private int FrameLong=540;
	private JFrame FrameObj;
	private Languages SelectedLang;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("rawtypes")
	public MainFrame() {
		SelectedLang=new Languages().SelectLanguages("lang_tw.lang");
		FrameObj=this;
		setTitle("ModOak");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100,100,450,FrameLong);
		
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
				System.out.println(this.SelectedLang.getString("NoNimbus"));
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception ex) {
			}
		}
		//---------------------------
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JLabel lblNewLabel_1 = new JLabel("ToolBar");
		menuBar.add(lblNewLabel_1);
		
		JMenu MenuServer = new JMenu(SelectedLang.getString("ServerMenu"));
		menuBar.add(MenuServer);
		
		JMenuItem AddServer = new JMenuItem(SelectedLang.getString("AddServer"));
		MenuServer.add(AddServer);
		
		JMenuItem RemoveServer = new JMenuItem(SelectedLang.getString("RemoveServer"));
		MenuServer.add(RemoveServer);
		
		JMenuItem ServerInfo = new JMenuItem(SelectedLang.getString("ServerInfo"));
		MenuServer.add(ServerInfo);
		
		JMenu MenuMod = new JMenu(SelectedLang.getString("ModMenu"));
		menuBar.add(MenuMod);
		
		JMenuItem ModInfo = new JMenuItem(SelectedLang.getString("ModInfo"));
		MenuMod.add(ModInfo);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JComboBox ProfileSelecter = new JComboBox();
		
		JComboBox ApiSelecter = new JComboBox();
		
		JTabbedPane ModsTree = new JTabbedPane(JTabbedPane.BOTTOM);
		
		JButton AddMod = new JButton("<html><span style='font-size:15px;font-weight:400;'>"+SelectedLang.getString("AddModBtn")+"</span></html>");
		
		JButton RemoveMod = new JButton("<html><span style='font-size:15px;font-weight:400;'>"+SelectedLang.getString("RemoveModBtn")+"</span></html>");
		
		JButton SaveList = new JButton("<html><span style='font-size:15px;font-weight:400;'>"+SelectedLang.getString("SaveListBtn")+"</span></html>");
		
		JButton LoadList = new JButton("<html><span style='font-size:15px;font-weight:400;'>"+SelectedLang.getString("LoadListBtn")+"</span></html>");
		
		JButton InstallBtn = new JButton("<html><span style='font-size:15px;font-weight:400;'>"+SelectedLang.getString("InstallBtn")+"</span></html>");
		
		JProgressBar ProgressBar = new JProgressBar();
		ProgressBar.setString("安裝中...");
		ProgressBar.setIndeterminate(true);
		
		JToggleButton ShowHideOptions = new JToggleButton("<html><span style='font-size:12px;font-weight:400;'>"+SelectedLang.getString("Options")+"</span></html>");
		ShowHideOptions.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				FrameLong=(FrameLong==700?540:700);
				FrameObj.setSize(450,FrameLong);
			}
		});
		
		JTextPane ConsolePane = new JTextPane();
		ConsolePane.setEditable(false);
		
		JTextPane OptionsDescription = new JTextPane();
		OptionsDescription.setEditable(false);
		
		OptionsField = new JTextField();
		OptionsField.setColumns(10);
		
		JComboBox<?> VersionSelecter = new JComboBox();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(ProgressBar, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
						.addComponent(OptionsDescription, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(OptionsField, GroupLayout.PREFERRED_SIZE, 289, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(VersionSelecter, 0, 113, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(ProfileSelecter, 0, 195, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(ApiSelecter, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE))
						.addComponent(ConsolePane, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(ModsTree, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(RemoveMod, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
								.addComponent(SaveList, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
								.addComponent(LoadList, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
								.addComponent(AddMod, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
								.addComponent(InstallBtn, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)))
						.addComponent(ShowHideOptions, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(ProfileSelecter, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
						.addComponent(ApiSelecter, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ProgressBar, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(AddMod, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(RemoveMod, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(SaveList, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(LoadList, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(InstallBtn, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
						.addComponent(ModsTree, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(ConsolePane, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(ShowHideOptions)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(OptionsDescription, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(OptionsField, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addComponent(VersionSelecter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
		);
		contentPane.setLayout(gl_contentPane);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{ModsTree, ProgressBar, ProfileSelecter, ApiSelecter, InstallBtn, LoadList, SaveList, RemoveMod, AddMod}));
	}
	/*private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}*/
}
