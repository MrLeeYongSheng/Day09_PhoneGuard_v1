package com.itheima.day09_phoneguard_v1.domain;

import android.graphics.drawable.Drawable;

public class APKInfo {
	
	private Drawable icon;
	private String appName;
	private long size;
	private boolean isSd;//�Ƿ�װ��SD��,falseΪ��װ��ROM
	private boolean isSystem;//�Ƿ�ϵͳ��APK
	private String packName;
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	/**
	 * @return
	 * ����Ӧ�õĴ�С
	 */
	public long getSize() {
		return size;
	}
	/**
	 * @param size
	 * ����Ӧ�õĴ�С
	 */
	public void setSize(long size) {
		this.size = size;
	}
	/**
	 * @return
	 * �����Ƿ�װ��SD��
	 */
	public boolean isSd() {
		return isSd;
	}
	
	/**
	 * @param isSd
	 * ����Ӧ���Ƿ�װ��SD��
	 */
	public void setSd(boolean isSd) {
		this.isSd = isSd;
	}
	/**
	 * @return
	 * �����Ƿ�ΪϵͳӦ��
	 */
	public boolean isSystem() {
		return isSystem;
	}
	/**
	 * @param isSystem
	 * �����Ƿ�ΪϵͳӦ��
	 */
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	
}
