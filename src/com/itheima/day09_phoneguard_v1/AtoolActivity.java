package com.itheima.day09_phoneguard_v1;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.itheima.day09_phoneguard_v1.engine.SmsEngine;
import com.itheima.day09_phoneguard_v1.engine.SmsEngine.BlackupListener;

public class AtoolActivity extends Activity {

	private TextView tv_phonelocation;
	private TextView tv_sms_backup;
	private TextView tv_sms_getbackup;
	private TextView tv_applock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initEvent();
	}

	private OnClickListener mListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.tv_atool_phonelocation://查询归属地
			{
				Intent intent = new Intent(AtoolActivity.this, PhoneLocationActivity.class);
				startActivity(intent);
			}
				break;
			case R.id.tv_atool_sms_backup:
				pd.setTitle("短信备份进度");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				SmsEngine.blackup(AtoolActivity.this, new BlackupListener() {
					
					@Override
					public void show() {
						pd.show();
					}
					
					@Override
					public void setProgress(int value) {

						pd.setProgress(value);
					}
					
					@Override
					public void setMax(int max) {
						pd.setMax(max);
					}
					
					@Override
					public void dismiss() {
						pd.dismiss();
					}
				});
				break;
				
			case R.id.tv_atool_sms_getbackup:
				pd.setTitle("短信还原进度");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				SmsEngine.getBlackup(AtoolActivity.this, new BlackupListener() {
					
					@Override
					public void show() {
						pd.show();
					}
					
					@Override
					public void setProgress(int value) {

						pd.setProgress(value);
					}
					
					@Override
					public void setMax(int max) {
						pd.setMax(max);
					}
					
					@Override
					public void dismiss() {
						pd.dismiss();
					}
				});
				break;
				
			case R.id.tv_atool_applock:
			{
				Intent intent = new Intent(AtoolActivity.this, AppLockActivity.class);
				startActivity(intent);
			}
				break;
				
			default:
				break;
			}
		}
	};
	private ProgressDialog pd;
	private void initEvent() {

		tv_phonelocation.setOnClickListener(mListener);
		tv_sms_backup.setOnClickListener(mListener);
		tv_sms_getbackup.setOnClickListener(mListener);
		tv_applock.setOnClickListener(mListener);
	}

	private void initView() {
		setContentView(R.layout.activity_atool);
		tv_phonelocation = (TextView) findViewById(R.id.tv_atool_phonelocation);
		tv_sms_backup = (TextView) findViewById(R.id.tv_atool_sms_backup);
		tv_sms_getbackup = (TextView) findViewById(R.id.tv_atool_sms_getbackup);
		tv_applock = (TextView) findViewById(R.id.tv_atool_applock);
		pd = new ProgressDialog(AtoolActivity.this);
	}
}
