package com.itheima.day09_phoneguard_v1.domain;

import android.graphics.drawable.Drawable;

/**
 * ��װ������Ϣ��Bean
 * @author Administrator
 *
 */
public class ProcessBean {

	private Drawable icon;//APKͼ��
	private String name;//APK����
	private String packName;//APK����
	private long memSize;//APKռ�õ��ڴ��С
	private boolean isSystem;//�Ƿ�ϵͳ��APK;
	private boolean isChecked;//�Ƿ�ѡ��
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
