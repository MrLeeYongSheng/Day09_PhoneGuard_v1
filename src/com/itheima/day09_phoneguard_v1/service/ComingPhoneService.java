package com.itheima.day09_phoneguard_v1.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.itheima.day09_phoneguard_v1.R;
import com.itheima.day09_phoneguard_v1.engine.PhoneLocationEngine;
import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;

public class ComingPhoneService extends Service {
	// "蔚空蓝", "金属灰", "苹果绿", "活力橙", "透明色"
	private int[] styles = new int[] { R.drawable.call_locate_blue,
			R.drawable.call_locate_gray, R.drawable.call_locate_green,
			R.drawable.call_locate_orange, R.drawable.call_locate_white };

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private PhoneStateListener mListener = new PhoneStateListener() {
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// 空闲状态
				hideLocationToast();
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// 接通状态
				hideLocationToast();
				break;
			case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
				showLocationToast(incomingNumber);
				break;

			default:
				break;
			}
		};
	};
	private TelephonyManager tm;
	private WindowManager.LayoutParams params;
	private WindowManager mWM;
	private View mView;

	@Override
	public void onCreate() {
		super.onCreate();
		mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		initToastParams();
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private void initToastParams() {
		// XXX This should be changed to use a Dialog, with a Theme.Toast
		// defined that sets up the layout params appropriately.
		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;// 优先于电话
		params.setTitle("Toast");
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		params.gravity = Gravity.LEFT | Gravity.TOP;
		params.x = SharedPreferencesUtils.getInt(ComingPhoneService.this,
				MyConstants.PHONE_LOCATION_X, 0);
		params.y = SharedPreferencesUtils.getInt(ComingPhoneService.this,
				MyConstants.PHONE_LOCATION_Y, 0);
	}

	private void showLocationToast(String incomingNumber) {
		mView = View.inflate(ComingPhoneService.this, R.layout.sys_toast, null);
		mView.setBackgroundResource(styles[SharedPreferencesUtils.getInt(
				ComingPhoneService.this, MyConstants.PHONE_LOCATION_STYLE, 0)]);
		mView.setOnTouchListener(new OnTouchListener() {

			private float startX;
			private float startY;
			private float moveX;
			private float moveY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getRawX();
					startY = event.getRawY();
					break;

				case MotionEvent.ACTION_MOVE:
					moveX = event.getRawX();
					moveY = event.getRawY();
					params.x += (int) (moveX - startX);
					params.y += (int) (moveY - startY);
					startX = moveX;
					startY = moveY;
					mWM.updateViewLayout(v, params);
					break;

				case MotionEvent.ACTION_UP:
					if (params.x < 0) {
						params.x = 0;
					} else if (params.x > (mWM.getDefaultDisplay().getWidth() - mView
							.getWidth())) {
						params.x = mWM.getDefaultDisplay().getWidth()
								- mView.getWidth();
					}
					if (params.y < 0) {
						params.y = 0;
					} else if (params.y > (mWM.getDefaultDisplay().getHeight() - mView
							.getHeight())) {
						params.y = mWM.getDefaultDisplay().getHeight()
								- mView.getHeight();
					}
					SharedPreferencesUtils.putInt(ComingPhoneService.this,
							MyConstants.PHONE_LOCATION_X, params.x);
					SharedPreferencesUtils.putInt(ComingPhoneService.this,
							MyConstants.PHONE_LOCATION_Y, params.y);
					break;

				default:
					break;
				}
				return false;
			}
		});
		TextView tv_toast = (TextView) mView.findViewById(R.id.tv_sys_toast);
		tv_toast.setText(PhoneLocationEngine.queryLocationByPhone(
				incomingNumber, ComingPhoneService.this));
		mWM.addView(mView, params);
	}

	protected void hideLocationToast() {
		if (mView != null) {
			mWM.removeView(mView);
			mView = null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(mListener, PhoneStateListener.LISTEN_NONE);
		hideLocationToast();
	}
}
