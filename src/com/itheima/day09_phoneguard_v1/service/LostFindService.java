package com.itheima.day09_phoneguard_v1.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;

public class LostFindService extends Service {

	private SmsReveiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		receiver = new SmsReveiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver, filter );
	}
	
	private class SmsReveiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {

			Bundle extras = intent.getExtras();
			Object[] objects = (Object[]) extras.get("pdus");
			for (Object object : objects) {
				SmsMessage sm = SmsMessage.createFromPdu((byte[]) object);
				if("#*gps*#".equals(sm.getMessageBody())) {
					Intent locationIntent = new Intent(context, LocationService.class);
					startService(locationIntent);
				}
			}
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		System.out.println("服务销毁了");
	}

}
