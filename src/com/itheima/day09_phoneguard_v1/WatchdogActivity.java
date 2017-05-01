package com.itheima.day09_phoneguard_v1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class WatchdogActivity extends Activity {

	private Button btn_ok;
	private EditText et_pass;
	private PackageManager pm;
	private ImageView iv_icon;
	private WABroadcastReceiver receiver;
	private String packName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		receiver = new WABroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		registerReceiver(receiver, filter);
		
		
		initView();
		initData();
		initEvent();
	}

	//点击返回键的事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK) {
			gotoHomePage();
		}
		
		return super.onKeyDown(keyCode, event);
	}

	private class WABroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent == null) {
				return ;
			}
			if(Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction() )) {
				//Home键处理
				gotoHomePage();
			}
		}
		
	}
	
	private void gotoHomePage() {
		/*
			  	<action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.HOME" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.MONKEY"/>
		 */
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addCategory("android.intent.category.MONKEY");
		startActivity(intent);
		finish();
	}
	
	private void initEvent() {
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String pass = et_pass.getText().toString().trim();
				if(TextUtils.isEmpty(pass)) {
					Toast.makeText(WatchdogActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
					return ;
				}
				if("123456".equals(pass)) {
					//密码正确
					Intent intent = new Intent();
					intent.setAction("com.itheima.password.ok");
					intent.putExtra("packName", packName);
					sendBroadcast(intent);
					finish();
				} else {
					//密码错误
					Toast.makeText(WatchdogActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
					return ;
				}
			}
		});
	}

	private void initData() {
		Intent intent = getIntent();
		packName = intent.getStringExtra("packName");
		try {
			ApplicationInfo appInfo = pm.getApplicationInfo(packName, 0);
			iv_icon.setImageDrawable(appInfo.loadIcon(pm));
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	private void initView() {
		setContentView(R.layout.activity_watchdao_enterypass);
		et_pass = (EditText) findViewById(R.id.et_watchdog_pass);
		btn_ok = (Button) findViewById(R.id.btn_watchdog_ok);
		iv_icon = (ImageView) findViewById(R.id.iv_watchdog_icon);
		pm = getPackageManager();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(receiver!=null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
	}
}
