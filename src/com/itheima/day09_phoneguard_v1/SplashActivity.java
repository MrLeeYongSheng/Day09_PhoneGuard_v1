package com.itheima.day09_phoneguard_v1;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private static final int VERSON_OK = 0;
	private static final int VERSON_LOW = 1;
	private RelativeLayout rl_root;
	private TextView tv_version;
	private int versionCode;
	private UrlBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
		animation();
		startThreadTimeMillis = SystemClock.currentThreadTimeMillis();
		connection();

	}

	private void initData() {
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo;
			packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			versionCode = packageInfo.versionCode;
			versionName = packageInfo.versionName;
			if(TextUtils.isEmpty(versionName)) {
				tv_version.setText("通用");
			} else {
				tv_version.setText(versionName);
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	private String versionName;
	/**
	 *  运行在子线程里
	 */
	private void isNewVerson(UrlBean bean) {

		if(bean.getVerson() > versionCode) {
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

	}
	
	protected void showAlertDiaload() {

		AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
		builder.setTitle("更新最新版本")
		.setMessage("版本特性 : " + bean.getDesc())
		.setPositiveButton("更新", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(SplashActivity.this, "准备下载", Toast.LENGTH_SHORT).show();
				downloadNewApk();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				gotoMain();
			}
		}).show();
	}

	protected void downloadNewApk() {
		
		HttpUtils httpUtils = new HttpUtils();
		System.out.println(bean.getUrl());
		httpUtils.download(bean.getUrl(), Environment.getExternalStorageDirectory().getAbsolutePath()+"/2.apk", new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
				
			}
		});
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
		tv_version = (TextView) findViewById(R.id.tv_splash_version);
	}
}
