package com.itheima.day09_phoneguard_v1.receiver;

import com.itheima.day09_phoneguard_v1.service.LostFindService;
import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		//开机启动防盗服务
		if(SharedPreferencesUtils.getBoolean(context, MyConstants.SP_ISSETUP, false)) {
			Intent lostFindIntent = new Intent(context, LostFindService.class);
			context.startService(lostFindIntent);
		}
		
		if(SharedPreferencesUtils.getBoolean(context, MyConstants.SP_ISSETUP, false)) {
			String oldSimSerialNumber = SharedPreferencesUtils.getString(context, MyConstants.SIM_SERIALNUMBER, "");
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String newSimSerialNumber = tm.getSimSerialNumber();
			
//			newSimSerialNumber = newSimSerialNumber + "1";//注意:+"1"是为了模拟SIM卡的更换
			if(!oldSimSerialNumber.equals(newSimSerialNumber)) {
				//更换SIM卡
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage(SharedPreferencesUtils.getString(context, MyConstants.SAFE_NUMBER, ""),
						null, "wo shi xiao tou", null, null);
			}
		}
	}

}
