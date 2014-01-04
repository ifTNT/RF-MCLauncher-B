package org.rf.john.mclauncher.Themes;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class OakTheme extends Theme{
	public OakTheme(){
		this.Author="曙";
		this.ThemeName="Oak";
		this.Description="RF-MCLauncher-B的預設佈景";
		//-----------
		SetFrameSize(300,400);
		SetBackground("images/OakBG.png",5,2,280,300);
		//-----------
		TextStyles.put("UserText","<html><span style=\"font-size: 15px;color: rgb(100%,100%,100%);font-weight:bold;\">${TEXT}</span></html>");
		TextStyles.put("PwdText","<html><span style=\"font-size: 15px;color: rgb(100%,100%,100%);font-weight:bold;\">${TEXT}</span></html>");
		TextStyles.put("ProfilesText","<html><span style=\"font-size: 15px;color: rgb(100%,100%,100%);font-weight:bold;\">${TEXT}</span></html>");
		TextStyles.put("RememberMe","<html><span style=\"font-size: 12px;color: rgb(100%,100%,100%);font-weight:bold;\">${TEXT}</span></html>");
		TextStyles.put("LoginBtn","<html><span style=\"font-size: 12px;color: rgb(0%,0%,0%);font-weight:bold;\">${TEXT}</span></html>");
		TextStyles.put("ProfileFormat","<html><span style=\"font-size:10px;font-weight:bold;\">${TEXT}</span></html");
	}
	public void BuildFrame(){
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(LoginBtn, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(RememberMe, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(UserText, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
								.addComponent(PwdText, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(ProfileText, Alignment.TRAILING))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(UserBox)
								.addComponent(PwdBox)
								.addComponent(ProfileSelectBox, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)))
						.addComponent(MainProgressBar, GroupLayout.PREFERRED_SIZE, 276, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(152, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(50)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(UserBox, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addComponent(UserText))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(PwdBox, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addComponent(PwdText))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(ProfileSelectBox, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(ProfileText))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(RememberMe)
						.addComponent(LoginBtn, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
					.addComponent(MainProgressBar, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		setLayout(groupLayout);
	}
}
