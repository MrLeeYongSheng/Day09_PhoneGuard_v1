package com.itheima.day09_phoneguard_v1;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private static final int VERSON_OK = 0;
	private static final int VERSON_LOW = 1;
	private RelativeLayout rl_root;
	private int versionCode;
	private String packageName;
	private UrlBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		animation();
		startThreadTimeMillis = SystemClock.currentThreadTimeMillis();
		connection();

	}

	private void connection() {

		new Thread(){


			public void run() {
				String path = "http://10.0.2.2:8080/splash.json";
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					int responseCode = conn.getResponseCode();
					if(responseCode == 200) {
						InputStream is = conn.getInputStream();
						String result = StreamUtil.parser(is);
						bean = JsonParser2UrlBean.parser(result);
						isNewVerson(bean);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}.start();
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case VERSON_OK:
				gotoMain();
				break;
			case VERSON_LOW:
				showAlertDiaload();
				break;
			default:
				break;
			}
		};
	};
	private long startThreadTimeMillis;
	private long endThreadTimeMillis;
	/**
	 *  ���������߳���
	 */
	private void isNewVerson(UrlBean bean) {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			versionCode = packageInfo.versionCode;
			packageName = packageInfo.packageName;
			if(bean.getVerson() < versionCode) {
				Message msg = Message.obtain();
				msg.what = VERSON_LOW;
				mHandler.sendMessage(msg);
			} else {
				endThreadTimeMillis = SystemClock.currentThreadTimeMillis();
				if (startThreadTimeMillis-endThreadTimeMillis < 5000l) {
					SystemClock.sleep(3000l - (startThreadTimeMillis-endThreadTimeMillis));
				}
				Message msg = Message.obtain();
				msg.what = VERSON_OK;
				mHandler.sendMessage(msg);
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	protected void showAlertDiaload() {

		AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
		builder.setTitle("�������°汾")
		.setMessage("�汾���� : " + bean.getDesc())
		.setPositiveButton("����", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(SplashActivity.this, "׼������", Toast.LENGTH_SHORT).show();
			}
		}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				gotoMain();
			}
		}).show();
	}

	protected void gotoMain() {

		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
	}

	private void animation() {
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(3000);
		aa.setFillAfter(true);

		RotateAnimation ra = new RotateAnimation(0f, 360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(3000);
		ra.setFillAfter(true);

		ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa.setDuration(3000);
		sa.setFillAfter(true);
		
		AnimationSet as = new AnimationSet(true);
		as.addAnimation(aa);
		as.addAnimation(ra);
		as.addAnimation(sa);
		
		rl_root.setAnimation(as);
	}

	private void initView() {
		setContentView(R.layout.activity_splash);
		rl_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
	}
}
