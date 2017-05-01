package com.itheima.day09_phoneguard_v1.domain;

import android.graphics.drawable.Drawable;

public class APKInfo {
	
	private String apkPath;
	
	private Drawable icon;
	private String appName;
	private long size;
	private boolean isSd;//是否安装在SD卡,false为安装在ROM
	private boolean isSystem;//是否系统的APK
	private String packName;
	public String getApkPath() {
		return apkPath;
	}
	public String getAppName() {
		return appName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public String getPackName() {
		return packName;
	}
	/**
	 * @return
	 * 返回应用的大小
	 */
	public long getSize() {
		return size;
	}
	/**
	 * @return
	 * 返回是否安装在SD卡
	 */
	public boolean isSd() {
		return isSd;
	}
	/**
	 * @return
	 * 返回是否为系统应用
	 */
	public boolean isSystem() {
		return isSystem;
	}
	public void setApkPath(String apkPath) {
		this.apkPath = apkPath;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	/**
	 * @param isSd
	 * 设置应用是否安装在SD卡
	 */
	public void setSd(boolean isSd) {
		this.isSd = isSd;
	}
	/**
	 * @param size
	 * 设置应用的大小
	 */
	public void setSize(long size) {
		this.size = size;
	}
	/**
	 * @param isSystem
	 * 设置是否为系统应用
	 */
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	
}
