package com.itheima.day09_phoneguard_v1.service;

import com.itheima.day09_phoneguard_v1.receiver.LockScreenClearReceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class LockScreenService extends Service {

	private LockScreenClearReceiver receiver;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		receiver = new LockScreenClearReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver, filter );
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//取消广播接受者的注册
		unregisterReceiver(receiver);
	}
}
