package com.itheima.day09_phoneguard_v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;

public class LostFindActivity extends Activity {

	private TextView tv_gotosetup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(SharedPreferencesUtils.getBoolean(LostFindActivity.this, MyConstants.SP_ISSETUP, false)) {
			//已经设置过
			initView();
			initEvent();
		} else{
			//没有设置过
			Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
			startActivity(intent);
			finish();
		}
		
	}

	private void initEvent() {
		tv_gotosetup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void initView() {

		setContentView(R.layout.activity_lostfind);
		tv_gotosetup = (TextView) findViewById(R.id.tv_lostfind_gotosetup);
	}
}
