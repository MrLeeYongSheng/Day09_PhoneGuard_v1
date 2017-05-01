package com.itheima.day09_phoneguard_v1;

import com.itheima.day09_phoneguard_v1.utils.MD5Utils;
import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	private GridView gv_menu;
	private String[] names = {"�ֻ�����","ͨѶ��ʿ","����ܼ�","���̹���","����ͳ��",
			"������ɱ","��������","�߼�����","��������"};
	private int[] icons = {R.drawable.safe,R.drawable.callmsgsafe,R.drawable.selector_home_gd_app
			,R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan
			,R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings};

	private AlertDialog passwordDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initView();
		initData();
		initEvent();
	}

	private void initEvent() {

		gv_menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				switch (position) {
				case 0://�ֻ�����
					if(TextUtils.isEmpty(SharedPreferencesUtils.getString(HomeActivity.this, 
							MyConstants.SP_SAFE_PASSWORD_NAME, MyConstants.SP_SAFE_PASSWORD_KEY, ""))) {
						showSetPasswordDialog();
					} else {
						showEnterPasswordDialog();
					}
					break;

				case 1://ͨѶ��ʿ
					Intent teleSmsIntent = new Intent(HomeActivity.this, TeleSmsActivity.class);//��ת���Ƿ�ҳ
					//Intent teleSmsIntent = new Intent(HomeActivity.this, TeleSmsPagesActivity.class);//��ת����ҳ
					startActivity(teleSmsIntent);
					break;
					
				case 2://Ӧ�ùܼ�
					Intent appmanIntent = new Intent(HomeActivity.this, AppManagerActivity.class);
					startActivity(appmanIntent);
					break;
					
				case 3://���̹ܼ�
					Intent taskmanIntent = new Intent(HomeActivity.this, TaskManagerActivity.class);
					startActivity(taskmanIntent);
					break;	
					
				case 5://���̹ܼ�
					Intent antiVirusIntent = new Intent(HomeActivity.this, AntivirusActivity.class);
					startActivity(antiVirusIntent);
					break;		
					
				case 7://�߼�����
					Intent atoolIntent = new Intent(HomeActivity.this, AtoolActivity.class);
					startActivity(atoolIntent);
					break;
					
				case 8://��������
					Intent settingIntent = new Intent(HomeActivity.this, SettingCenterActivity.class);
					startActivity(settingIntent);
					break;
				default:
					break;
				}
			}
		});
	}

	protected void showEnterPasswordDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this, R.layout.dialog_home_gd_safe_enter_password, null);
		passwordDialog = builder.setView(view ).show();
		final EditText et_dialog_safe_enter_password = (EditText) view.findViewById(R.id.et_dialog_home_gd_safe_enter_password);

		Button btn_dialog_safe_enter_password = (Button) view.findViewById(R.id.btn_dialog_home_gd_safe_enter_password);
		Button btn_dialog_safe_enter_cancle = (Button) view.findViewById(R.id.btn_dialog_home_gd_safe_enter_cancel);
		
		btn_dialog_safe_enter_password.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String password = et_dialog_safe_enter_password.getText().toString().trim();
				if(TextUtils.isEmpty(password)) {
					Toast.makeText(HomeActivity.this, "��������벻��Ϊ��", Toast.LENGTH_SHORT).show();
				} else {
					//MD5���μ���
					password= MD5Utils.md5(MD5Utils.md5(password));
					String passwordBefore = SharedPreferencesUtils.getString(HomeActivity.this, MyConstants.SP_SAFE_PASSWORD_NAME, MyConstants.SP_SAFE_PASSWORD_KEY, "");
					if(password.equals(passwordBefore)) {
						passwordDialog.dismiss();
						Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
						startActivity(intent);
					} else {
						Toast.makeText(HomeActivity.this, "������������", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		btn_dialog_safe_enter_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				passwordDialog.dismiss();
			}
		});
		
	}

	protected void showSetPasswordDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this, R.layout.dialog_home_gd_safe_password, null);
		passwordDialog = builder.setView(view ).show();
		final EditText et_dialog_safe_passwordone = (EditText) view.findViewById(R.id.et_dialog_home_gd_safe_passwordone);
		final EditText et_dialog_safe_passwordtwo = (EditText) view.findViewById(R.id.et_dialog_home_gd_safe_passwordtwo);
		Button btn_dialog_safe_setpass = (Button) view.findViewById(R.id.btn_dialog_home_gd_safe_setpass);
		Button btn_dialog_safe_cancle = (Button) view.findViewById(R.id.btn_dialog_home_gd_safe_cancel);
		
		btn_dialog_safe_setpass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String passwordone = et_dialog_safe_passwordone.getText().toString().trim();
				String passwordtwo = et_dialog_safe_passwordtwo.getText().toString().trim();
				if(TextUtils.isEmpty(passwordone) || TextUtils.isEmpty(passwordtwo)) {
					Toast.makeText(HomeActivity.this, "��������벻��Ϊ��", Toast.LENGTH_SHORT).show();
				} else if(!passwordone.equals(passwordtwo)){
					Toast.makeText(HomeActivity.this, "������������벻һ��", Toast.LENGTH_SHORT).show();
				} else {
					//MD5���μ���
					passwordone = MD5Utils.md5(MD5Utils.md5(passwordone));
					SharedPreferencesUtils.putString(HomeActivity.this, MyConstants.SP_SAFE_PASSWORD_NAME, MyConstants.SP_SAFE_PASSWORD_KEY, passwordone);
					passwordDialog.dismiss();
				}
			}
		});
		btn_dialog_safe_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				passwordDialog.dismiss();
			}
		});
		
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return icons.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.item_home_gridview, null);
			ImageView iv_item_gd_icon = (ImageView) view.findViewById(R.id.iv_item_home_gd_icon);
			TextView tv_item_gd_icon_name = (TextView) view.findViewById(R.id.tv_item_home_gd_icon_name);
			iv_item_gd_icon.setImageResource(icons[position]);
			tv_item_gd_icon_name.setText(names[position]);
			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	private void initData() {
		MyAdapter mAdapter = new MyAdapter();
		gv_menu.setAdapter(mAdapter);
	}

	private void initView() {
		gv_menu = (GridView) findViewById(R.id.gv_home_menu);
	}
}
