package com.itheima.day09_phoneguard_v1.domain;

import android.graphics.drawable.Drawable;

/**
 * 封装进程信息的Bean
 * @author Administrator
 *
 */
public class ProcessBean {

	private Drawable icon;//APK图标
	private String name;//APK名字
	private String packName;//APK包名
	private long memSize;//APK占用的内存大小
	private boolean isSystem;//是否系统的APK;
	private boolean isChecked;//是否被选中
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	public long getMemSize() {
		return memSize;
	}
	public void setMemSize(long memSize) {
		this.memSize = memSize;
	}
	public boolean isSystem() {
		return isSystem;
	}
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	@Override
	public String toString() {
		return "ProcessBean [icon=" + icon + ", name=" + name + ", packName="
				+ packName + ", memSize=" + memSize + ", isSystem=" + isSystem
				+ "]";
	}
	
	
}
