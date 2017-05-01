package com.itheima.day09_phoneguard_v1.service;

import java.util.List;

import com.itheima.day09_phoneguard_v1.WatchdogActivity;
import com.itheima.day09_phoneguard_v1.dao.LockedDao;
import com.itheima.day09_phoneguard_v1.db.LockedTable;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

public class WatchdogService extends Service {

	private LockedDao ldao;
	private List<String> allDatas;
	private boolean isWatching = false;
	private String packName = "";
	private ActivityManager am;
	private WSBroadcastReceiver receiver;
	private ContentObserver observer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		observer = new ContentObserver(new Handler()) {
			@Override
			public void onChange(boolean selfChange) {
				super.onChange(selfChange);
				new Thread() {
					public void run() {

						allDatas = ldao.getAllDatas();
					};
				}.start();

			}
		};
		getContentResolver().registerContentObserver(LockedTable.URI, true,
				observer);
		
		receiver = new WSBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction("com.itheima.password.ok");
		filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		registerReceiver(receiver, filter );
		
		ldao = new LockedDao(WatchdogService.this);
		allDatas = ldao.getAllDatas();
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		watching();
	}
	private class WSBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent == null) {
				return ;
			}
			if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction() )) {
				//屏幕锁屏处理
				isWatching = false;
				packName = "";
			} else if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
				watching();
			} else if(Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
				packName = "";
			}
			
			packName = intent.getStringExtra("packName");
			
		}
		
	}
	private void watching() {

		new Thread(){
			public void run() {
				isWatching = true;
				while(isWatching) {
					
					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
					RunningTaskInfo info = runningTasks.get(0);
					String packageName = info.topActivity.getPackageName();
					if(allDatas.contains(packageName)) {
						if(packageName.equals(packName)) {
							//已经验证,什么事也不干
						} else {
							//应用上锁
							Intent intent = new Intent(WatchdogService.this, WatchdogActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra("packName", packageName);
							startActivity(intent);
						}
					}
					SystemClock.sleep(50);
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isWatching = false;
		if(receiver!=null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
		if(observer!=null) {
			getContentResolver().unregisterContentObserver(observer);
			observer = null;
		}
	}
}
