package com.itheima.day09_phoneguard_v1;

import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LostFindActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(SharedPreferencesUtils.getBoolean(LostFindActivity.this, MyConstants.SP_ISSETUP, false)) {
			//�Ѿ����ù�
			initView();
		} else{
			//û�����ù�
			Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
			startActivity(intent);
			finish();
		}
		
	}

	private void initView() {

		setContentView(R.layout.activity_lostfind);
	}
}
