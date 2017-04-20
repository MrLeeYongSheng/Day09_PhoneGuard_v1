package com.itheima.day09_phoneguard_v1;

import com.itheima.day09_phoneguard_v1.service.LostFindService;
import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.ServiceUtil;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class Setup4Activity extends BaseSetupActivity {

	private CheckBox cb_protect;
	private TextView tv_protect;

	@Override
	public void preActivity() {

		startActivity(Setup3Activity.class);
	}

	@Override
	public void nextActivity() {

		startActivity(LostFindActivity.class);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup4);
		cb_protect = (CheckBox) findViewById(R.id.cb_setup4_protect);
		tv_protect = (TextView) findViewById(R.id.tv_setup4_protect);
	}
	
	@Override
	public void initData() {
		
		if(ServiceUtil.isServiceRunning(Setup4Activity.this,
				"com.itheima.day09_phoneguard_v1.service.LostFindService")) {
			tv_protect.setText("防盗保护开启了");
			tv_protect.setTextColor(Color.GREEN);
			cb_protect.setChecked(true);
		} else {
			tv_protect.setText("防盗保护还没开启");
			tv_protect.setTextColor(Color.GRAY);
			cb_protect.setChecked(false);
		}
	}
	
	@Override
	public void initEvent() {
		cb_protect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked) {
					Intent intent = new Intent(Setup4Activity.this, LostFindService.class);
					startService(intent);
					tv_protect.setText("防盗保护开启了");
					tv_protect.setTextColor(Color.GREEN);
				} else {
					Intent intent = new Intent(Setup4Activity.this, LostFindService.class);
					stopService(intent);
					tv_protect.setText("防盗保护还没开启");
					tv_protect.setTextColor(Color.GRAY);
				}
			}
		});
	}
	
	@Override
	public void next(View v) {
		if(!cb_protect.isChecked()) {
			Toast.makeText(Setup4Activity.this, "还没勾选呢", Toast.LENGTH_SHORT).show();
			return ;
		}
		SharedPreferencesUtils.putBoolean(Setup4Activity.this, MyConstants.SP_ISSETUP, true);
		super.next(v);
	}

}
