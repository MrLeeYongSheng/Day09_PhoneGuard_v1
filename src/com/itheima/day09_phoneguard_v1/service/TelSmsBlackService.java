package com.itheima.day09_phoneguard_v1.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.itheima.day09_phoneguard_v1.dao.BlacklistDao;
import com.itheima.day09_phoneguard_v1.db.BlacklistTable;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class TelSmsBlackService extends Service {

	private SmsBlackReceiver receiver;
	private TelephonyManager tm;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		registerSmsBlackReceiver();
		startTelBlack();
	}
	
	private void startTelBlack() {

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	private PhoneStateListener mListener = new PhoneStateListener() {
		public void onCallStateChanged(int state, String incomingNumber) {
			if(state == TelephonyManager.CALL_STATE_RINGING) {
				BlacklistDao dao = new BlacklistDao(TelSmsBlackService.this);
				int mode = dao.getMode(incomingNumber);
				if((mode & BlacklistTable.TEL) != 0) {
					try {
						Class clazz = Class.forName("android.os.ServiceManager");
						Method method = clazz.getMethod("getService", String.class);
						IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
						ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
						iTelephony.endCall();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		};
	};

	private void registerSmsBlackReceiver() {

		receiver = new SmsBlackReceiver();
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver, filter);
	}
	
	private class SmsBlackReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object sms : objs) {
				SmsMessage mess = SmsMessage.createFromPdu((byte[]) sms);
				String phone = mess.getOriginatingAddress();
				BlacklistDao dao = new BlacklistDao(TelSmsBlackService.this);
				int mode = dao.getMode(phone);
				if((mode & BlacklistTable.SMS) != 0) {
					abortBroadcast();
				}
			}
		}
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		tm.listen(mListener, PhoneStateListener.LISTEN_NONE);
	}

}
