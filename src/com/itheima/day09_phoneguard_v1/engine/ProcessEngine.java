package com.itheima.day09_phoneguard_v1.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itheima.day09_phoneguard_v1.domain.ProcessBean;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ProcessEngine {

	public static List<ProcessBean> getAllProcInfo(Context context){
		List<ProcessBean> list = new ArrayList<ProcessBean>();
		PackageManager pm = context.getPackageManager();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
	
			for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
				ProcessBean bean = new ProcessBean();
				//获取包名
				String processName = runningAppProcessInfo.processName;
				bean.setPackName(processName);
				//获取应用信息
			
					PackageInfo packageInfo;
					try {
						packageInfo = pm.getPackageInfo(processName, 0);
					} catch (NameNotFoundException e) {
						e.printStackTrace();
						continue;
					}
					ApplicationInfo applicationInfo = packageInfo.applicationInfo;
					
					//获取图标
					bean.setIcon(applicationInfo.loadIcon(pm));
					//获取APK名字
					bean.setName(applicationInfo.loadLabel(pm).toString());
					//是否系统APK
					if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
						//不是系统的APK
						bean.setSystem(false);
					} else {
						//是系统的APK
						bean.setSystem(true);
					}
					//获取APK占用内存的大小
					android.os.Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
					int totalPrivateDirty = processMemoryInfo[0].getTotalPrivateDirty();
					bean.setMemSize(totalPrivateDirty * 1024);
					
					list.add(bean);

			}//end for

		return list;
	}
	
	/**
	 * @return
	 * 获取当前总内存的大小
	 */
	public static long getTotalMemory() {
		BufferedReader br = null;
		try {
			//通过读取配置文件来完成
			File file = new File("/proc/meminfo");
			br = new BufferedReader(new FileReader(file));
			String readLine = br.readLine();
			int startIndex = readLine.indexOf(":");
			int endIndex = readLine.indexOf("k");
			readLine = readLine.substring(startIndex+1, endIndex).trim();
			return Long.parseLong(readLine)*1024;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		} finally{
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @return
	 * 获取当前可用内存的大小
	 */
	public static long getFreeMemory(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo );
		long availMem = outInfo.availMem;
		return availMem;
	}
}
