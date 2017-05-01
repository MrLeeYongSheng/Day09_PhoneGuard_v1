package com.itheima.day09_phoneguard_v1.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.itheima.day09_phoneguard_v1.domain.APKInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

public class APKEngine {

	public static List<APKInfo> getAllAPKInfo(Context context) {
		List<APKInfo> lists = new ArrayList<APKInfo>();
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> infos = pm.getInstalledPackages(0);
		for (PackageInfo packageInfo : infos) {
			APKInfo apkInfo = new APKInfo();
			apkInfo.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
			apkInfo.setIcon(packageInfo.applicationInfo.loadIcon(pm));
			apkInfo.setPackName(packageInfo.packageName);
			int flag = packageInfo.applicationInfo.flags;
			//Ӧ���Ƿ�ΪϵͳӦ��
			if((flag & ApplicationInfo.FLAG_SYSTEM) == 0) {
				//����ϵͳӦ��
				apkInfo.setSystem(false);
			} else {
				//��ϵͳӦ��
				apkInfo.setSystem(true);
			}
			//Ӧ���Ƿ�װ��SD��
			if((flag & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
				apkInfo.setSd(false);
			} else {
				apkInfo.setSd(true);
			}
			//����Ӧ�ô�С
			String sourceDir = packageInfo.applicationInfo.sourceDir;
			apkInfo.setApkPath(sourceDir);
			File file = new File(sourceDir);
			apkInfo.setSize(file.length());
			lists.add(apkInfo);
		}
		return lists;
	}

	/**
	 * @return
	 * SD�����ÿռ�
	 */
	public static long getSDFreeSpace() {
		return Environment.getExternalStorageDirectory().getFreeSpace();
	}

	/**
	 * @return
	 * ROM���ÿռ�
	 */
	public static long getROMFreeSpace() {
		return Environment.getDataDirectory().getFreeSpace();
	}
}
