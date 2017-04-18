package com.itheima.day09_phoneguard_v1;

import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Setup2Activity extends BaseSetupActivity {

	private TextView tv_btn_setup2_bindSIM;
	private ImageView iv_setup2_lockSIM;

	@Override
	public void preActivity() {
		startActivity(Setup1Activity.class);
	}

	@Override
	public void nextActivity() {
		startActivity(Setup3Activity.class);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup2);
		tv_btn_setup2_bindSIM = (TextView) findViewById(R.id.tv_btn_setup2_bindSIM);
		iv_setup2_lockSIM = (ImageView) findViewById(R.id.iv_setup2_lockSIM);
	}
	
	@Override
	public void initEvent() {
		tv_btn_setup2_bindSIM.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(SharedPreferencesUtils.getString(Setup2Activity.this,
						MyConstants.SIM_SERIALNUMBER, ""))) {
					//û�а�SIM��, ȥ��
					
					//��ȡSIM����Ϣ
					TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();
					//�洢SIM����Ϣ SIM_SERIALNUMBER
					SharedPreferencesUtils.putString(Setup2Activity.this,
							MyConstants.SIM_SERIALNUMBER, simSerialNumber);
					iv_setup2_lockSIM.setImageResource(R.drawable.lock);
					tv_btn_setup2_bindSIM.setText("���SIM��");
				} else {
					//�Ѿ��󶨹�SIM��, ȥ���
					SharedPreferencesUtils.putString(Setup2Activity.this,
							MyConstants.SIM_SERIALNUMBER, "");
					iv_setup2_lockSIM.setImageResource(R.drawable.unlock);
					tv_btn_setup2_bindSIM.setText("��SIM��");
				}
			}
		});
	}
	
	@Override
	public void initData() {
		if (TextUtils.isEmpty(SharedPreferencesUtils.getString(Setup2Activity.this,
				MyConstants.SIM_SERIALNUMBER, ""))) {
			iv_setup2_lockSIM.setImageResource(R.drawable.unlock);
			tv_btn_setup2_bindSIM.setText("��SIM��");
		} else {
			iv_setup2_lockSIM.setImageResource(R.drawable.lock);
			tv_btn_setup2_bindSIM.setText("���SIM��");
		}
	}
	
	@Override
	public void next(View v) {
		if (TextUtils.isEmpty(SharedPreferencesUtils.getString(Setup2Activity.this,
				MyConstants.SIM_SERIALNUMBER, ""))) {
			Toast.makeText(Setup2Activity.this, "����û�а�SIM��", Toast.LENGTH_SHORT).show();
			return ;
		}
		super.next(v);
	}

}
