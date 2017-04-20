package com.itheima.day09_phoneguard_v1.service;

import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class LocationService extends Service {

	private LocationManager lm;
	private LocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);
		String bestProvider = lm.getBestProvider(criteria, true);
		listener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {

				float accuracy = location.getAccuracy(); //精度
				double altitude = location.getAltitude(); //海拔
				double longitude = location.getLongitude();//经度
				double latitude = location.getLatitude();//纬度
				float speed = location.getSpeed();
				String msg = "accuracy:" + accuracy + "\naltitude:" + altitude + "\nspeed:" + speed
						+ "\nlongitude:" + longitude + "\nlatitude:" + latitude;
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage(SharedPreferencesUtils.getString(LocationService.this,
						MyConstants.SAFE_NUMBER, ""), null, msg, null, null);
				stopSelf();
			}
		};
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(lm != null) {
			lm.removeUpdates(listener);
			lm = null;
			listener = null;
		}
	}
}
