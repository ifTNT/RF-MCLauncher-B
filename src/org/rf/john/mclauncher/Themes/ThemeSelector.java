package org.rf.john.mclauncher.Themes;

import org.rf.john.mclauncher.RFInfo;

public class ThemeSelector {
	public void SelectTheme(String SelectedTheme){
		Theme NewTheme = new Theme();
		RFInfo.Theme = NewTheme;
		RFInfo.MainFrameObj.setSize(NewTheme.FrameSize[0],NewTheme.FrameSize[1]);
		
	}
	public void getInstalledTheme(){
		
	}
	public String getThemeAuthor(){
		return null;
	}
}
