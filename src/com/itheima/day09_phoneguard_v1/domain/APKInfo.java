package com.itheima.day09_phoneguard_v1.domain;

import android.graphics.drawable.Drawable;

public class APKInfo {
	
	private String apkPath;
	
	private Drawable icon;
	private String appName;
	private long size;
	private boolean isSd;//�Ƿ�װ��SD��,falseΪ��װ��ROM
	private boolean isSystem;//�Ƿ�ϵͳ��APK
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
	 * ����Ӧ�õĴ�С
	 */
	public long getSize() {
		return size;
	}
	/**
	 * @return
	 * �����Ƿ�װ��SD��
	 */
	public boolean isSd() {
		return isSd;
	}
	/**
	 * @return
	 * �����Ƿ�ΪϵͳӦ��
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
	 * ����Ӧ���Ƿ�װ��SD��
	 */
	public void setSd(boolean isSd) {
		this.isSd = isSd;
	}
	/**
	 * @param size
	 * ����Ӧ�õĴ�С
	 */
	public void setSize(long size) {
		this.size = size;
	}
	/**
	 * @param isSystem
	 * �����Ƿ�ΪϵͳӦ��
	 */
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	
}
