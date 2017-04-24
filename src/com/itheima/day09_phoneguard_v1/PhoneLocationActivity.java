package com.itheima.day09_phoneguard_v1;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.itheima.day09_phoneguard_v1.engine.PhoneLocationEngine;

public class PhoneLocationActivity extends Activity {

	private EditText et_phone;
	private Button btn_querry;
	private TextView tv_result;
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		initEvent();
	}

	private void initEvent() {

		et_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				querry();
			}
		});

		btn_querry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				querry();
			}

		});
	}

	private void querry() {
		String phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {

			// ¶¶¶―
			Animation shake = AnimationUtils.loadAnimation(
					PhoneLocationActivity.this, R.anim.shake);
			et_phone.startAnimation(shake);
			// Υπ¶―
			vibrator.vibrate(new long[] { 800L, 500L, 500L, 500L, 800L, 800L },
					2);

		} else {
			String location = PhoneLocationEngine.queryLocationByPhone(phone,
					PhoneLocationActivity.this);
			tv_result.setText(location);
		}
	}

	private void initView() {
		setContentView(R.layout.activity_phonelocation);
		et_phone = (EditText) findViewById(R.id.et_phonelocation_phone);
		btn_querry = (Button) findViewById(R.id.btn_phonelocation_querry);
		tv_result = (TextView) findViewById(R.id.tv_phonelocation_result);
	}
}
