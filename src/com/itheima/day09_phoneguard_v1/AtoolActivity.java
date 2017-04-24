package com.itheima.day09_phoneguard_v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

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
			case R.id.tv_atool_phonelocation://≤È—ØπÈ Ùµÿ
			{
				Intent intent = new Intent(AtoolActivity.this, PhoneLocationActivity.class);
				startActivity(intent);
			}
				break;

			default:
				break;
			}
		}
	};
	private void initEvent() {

		tv_phonelocation.setOnClickListener(mListener);
	}

	private void initView() {
		setContentView(R.layout.activity_atool);
		tv_phonelocation = (TextView) findViewById(R.id.tv_atool_phonelocation);
		tv_sms_backup = (TextView) findViewById(R.id.tv_atool_sms_backup);
		tv_sms_getbackup = (TextView) findViewById(R.id.tv_atool_sms_getbackup);
		tv_applock = (TextView) findViewById(R.id.tv_atool_applock);
	}
}
