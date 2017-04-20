package com.itheima.day09_phoneguard_v1;

import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends BaseSetupActivity {

	private Button btn_save_safenumber;
	private EditText et_safenumber;

	@Override
	public void preActivity() {
		startActivity(Setup2Activity.class);

	}

	@Override
	public void nextActivity() {
		startActivity(Setup4Activity.class);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup3);
		btn_save_safenumber = (Button) findViewById(R.id.btn_setup3_save_safenumber);
		et_safenumber = (EditText) findViewById(R.id.et_setup3_safenumber);
	}

	@Override
	public void initData() {
		String lastSafeNumber = SharedPreferencesUtils.getString(Setup3Activity.this, MyConstants.SAFE_NUMBER, "");
		if(!TextUtils.isEmpty(lastSafeNumber)) {
			et_safenumber.setText(lastSafeNumber);
		}
	}
	
	@Override
	public void initEvent() {
		btn_save_safenumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Setup3Activity.this, ContactsActivity.class);
				startActivityForResult(intent, 1);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(RESULT_OK == resultCode) {
		String phoneNumber = data.getStringExtra(MyConstants.SAFE_NUMBER);
			if(!TextUtils.isEmpty(phoneNumber)) {
				et_safenumber.setText(phoneNumber);
			}
		}
	}
	
	@Override
	public void next(View v) {
		String safeNumber = et_safenumber.getText().toString().trim();
		if (TextUtils.isEmpty(safeNumber)) {
			Toast.makeText(Setup3Activity.this, "ºÅÂë²»ÄÜÎª¿Õ", Toast.LENGTH_SHORT)
					.show();
		} else {
			SharedPreferencesUtils.putString(Setup3Activity.this,
					MyConstants.SAFE_NUMBER, safeNumber);
			super.next(v);
		}
	}

}
