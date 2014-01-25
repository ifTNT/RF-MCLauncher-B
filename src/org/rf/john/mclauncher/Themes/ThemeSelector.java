package org.rf.john.mclauncher.Themes;

import org.rf.john.mclauncher.Status;

public class ThemeSelector {
	public void SelectTheme(String SelectedTheme){
		Theme NewTheme = new Theme();
		Status.Theme = NewTheme;
		Status.MainFrameObj.setSize(NewTheme.FrameSize[0],NewTheme.FrameSize[1]);
		
	}
	public void getInstalledTheme(){
		
	}
	public String getThemeAuthor(){
		return null;
	}
}
