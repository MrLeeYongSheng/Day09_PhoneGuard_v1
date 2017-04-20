package com.itheima.day09_phoneguard_v1.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtil {

	/**
	 * @param context
	 * @param service
	 * 		完整的名字
	 * @return
	 * 		服务是否正在运行
	 */
	public static boolean isServiceRunning(Context context, String service) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> services = am.getRunningServices(50);
		for (RunningServiceInfo runningServiceInfo : services) {
			if(runningServiceInfo.service.getClassName().equals(service)) {
				return true;
			}
		}
		return false;
	}
}
