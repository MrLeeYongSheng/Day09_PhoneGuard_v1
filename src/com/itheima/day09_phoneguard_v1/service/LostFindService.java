package com.itheima.day09_phoneguard_v1.service;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;

import com.itheima.day09_phoneguard_v1.R;

public class LostFindService extends Service {

	private SmsReveiver receiver;
	private boolean isPlay = false;

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
				String sms = sm.getMessageBody();
				if("#*gps*#".equals(sms)) {
					Intent locationIntent = new Intent(context, LocationService.class);
					startService(locationIntent);
					abortBroadcast();
				} else if("#*lockscreen*#".equals(sms)) {
					DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
					dpm.lockNow();
					dpm.resetPassword("123456", 0);
					abortBroadcast();
				} else if("#*wipedata*#".equals(sms)){
					DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
					dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
					abortBroadcast();
				} else if("#*music*#".equals(sms)) {
					abortBroadcast();
					if(isPlay) {
						return ;
					}
					MediaPlayer mp = MediaPlayer.create(LostFindService.this, R.raw.music1);
					mp.setVolume(1f, 1f);
					mp.start();
					isPlay = true;
					mp.setOnCompletionListener(new OnCompletionListener() {
						
						@Override
						public void onCompletion(MediaPlayer mp) {

							isPlay = false;
						}
					});
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
