package com.itheima.day09_phoneguard_v1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.day09_phoneguard_v1.service.ComingPhoneService;
import com.itheima.day09_phoneguard_v1.service.TelSmsBlackService;
import com.itheima.day09_phoneguard_v1.service.WatchdogService;
import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.ServiceUtil;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;
import com.itheima.day09_phoneguard_v1.view.ItemSettingCenterView;

public class SettingCenterActivity extends Activity {

	private ItemSettingCenterView iscv_autoUpdate;
	private ItemSettingCenterView iscv_blacklist;
	private ItemSettingCenterView iscv_comingphone;
	private ItemSettingCenterView iscv_watchdog;
	private RelativeLayout rl_style;
	private String[] style = new String[] { "ε����", "������", "ƻ����", "������", "͸��ɫ" };
	private TextView tv_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initEvent();
		initData();
	}

	private void initEvent() {

		iscv_autoUpdate.setOnItemClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iscv_autoUpdate.setChecked(!iscv_autoUpdate.isChecked());
				SharedPreferencesUtils.putBoolean(SettingCenterActivity.this,
						MyConstants.AUTO_UPDATE, iscv_autoUpdate.isChecked());
			}
		});

		iscv_blacklist.setOnItemClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (ServiceUtil
						.isServiceRunning(SettingCenterActivity.this,
								"com.itheima.day09_phoneguard_v1.service.TelSmsBlackService")) {
					// ���ط�����������
					// ֹͣ����
					Intent intent = new Intent(SettingCenterActivity.this,
							TelSmsBlackService.class);
					stopService(intent);
					iscv_blacklist.setChecked(false);
				} else {
					// ���ط���ֹͣ
					// ��������
					Intent intent = new Intent(SettingCenterActivity.this,
							TelSmsBlackService.class);
					startService(intent);
					iscv_blacklist.setChecked(true);
				}
			}
		});

		iscv_comingphone.setOnItemClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (ServiceUtil
						.isServiceRunning(SettingCenterActivity.this,
								"com.itheima.day09_phoneguard_v1.service.ComingPhoneService")) {
					// ������ʾ�����ط�����������
					// ֹͣ����
					Intent intent = new Intent(SettingCenterActivity.this,
							ComingPhoneService.class);
					stopService(intent);
					iscv_comingphone.setChecked(false);
				} else {
					// ������ʾ�����ط���ֹͣ
					// ��������
					Intent intent = new Intent(SettingCenterActivity.this,
							ComingPhoneService.class);
					startService(intent);
					iscv_comingphone.setChecked(true);
				}
			}
		});
		
		iscv_watchdog.setOnItemClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (ServiceUtil
						.isServiceRunning(SettingCenterActivity.this,
								"com.itheima.day09_phoneguard_v1.service.WatchdogService")) {
					// ���ط�����������
					// ֹͣ����
					Intent intent = new Intent(SettingCenterActivity.this,
							WatchdogService.class);
					stopService(intent);
					iscv_watchdog.setChecked(false);
				} else {
					// ���ط���ֹͣ
					// ��������
					Intent intent = new Intent(SettingCenterActivity.this,
							WatchdogService.class);
					startService(intent);
					iscv_watchdog.setChecked(true);
				}
			}
		});

		rl_style.setOnClickListener(new OnClickListener() {

			private AlertDialog dialog;

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SettingCenterActivity.this);
				builder.setTitle("������ʾ��ʽ")
				.setSingleChoiceItems(style, SharedPreferencesUtils.getInt(SettingCenterActivity.this, MyConstants.PHONE_LOCATION_STYLE, 0),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								tv_content.setText(style[which]);
								SharedPreferencesUtils.putInt(SettingCenterActivity.this, MyConstants.PHONE_LOCATION_STYLE, which);
								dialog.dismiss();
							}
						});
				dialog = builder.show();
			}
		});
	}

	private void initView() {
		setContentView(R.layout.activity_settingcenter);
		iscv_autoUpdate = (ItemSettingCenterView) findViewById(R.id.iscv_settingcenter_autoupdate);
		iscv_blacklist = (ItemSettingCenterView) findViewById(R.id.iscv_settingcenter_blacklist);
		iscv_comingphone = (ItemSettingCenterView) findViewById(R.id.iscv_settingcenter_comingphone);
		iscv_watchdog = (ItemSettingCenterView) findViewById(R.id.iscv_settingcenter_watchdog);
		rl_style = (RelativeLayout) findViewById(R.id.rl_settingcenter_phonelocation_style);
		tv_content = (TextView) findViewById(R.id.tv_settingcenter_phonelocation_content);
	}

	private void initData() {

		if (SharedPreferencesUtils.getBoolean(SettingCenterActivity.this,
				MyConstants.AUTO_UPDATE, false)) {
			iscv_autoUpdate.setChecked(true);
		}

		if (ServiceUtil.isServiceRunning(SettingCenterActivity.this,
				"com.itheima.day09_phoneguard_v1.service.TelSmsBlackService")) {
			// ���ط�����������
			iscv_blacklist.setChecked(true);
		} else {
			// ���ط���û������
			iscv_blacklist.setChecked(false);
		}

		if (ServiceUtil.isServiceRunning(SettingCenterActivity.this,
				"com.itheima.day09_phoneguard_v1.service.ComingPhoneService")) {
			// ������ʾ�����ط�����������
			iscv_comingphone.setChecked(true);
		} else {
			//������ʾ�����ط���û������
			iscv_comingphone.setChecked(false);
		}
		
		if (ServiceUtil.isServiceRunning(SettingCenterActivity.this,
				"com.itheima.day09_phoneguard_v1.service.WatchdogService")) {
			// ���Ź�������������
			iscv_watchdog.setChecked(true);
		} else {
			// ���Ź�����û������
			iscv_watchdog.setChecked(false);
		}
		
		tv_content.setText(style[SharedPreferencesUtils.getInt(SettingCenterActivity.this, MyConstants.PHONE_LOCATION_STYLE, 0)]);
	}
}
