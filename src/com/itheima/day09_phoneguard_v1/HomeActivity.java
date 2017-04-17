package com.itheima.day09_phoneguard_v1;

import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;

import android.app.Activity;
import android.app.AlertDialog;
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
	private String[] names = {"手机防盗","通讯卫士","软件管家","进程管理","流量统计",
			"病毒查杀","缓存清理","高级工具","设置中心"};
	private int[] icons = {R.drawable.safe,R.drawable.callmsgsafe,R.drawable.selector_home_gd_app
			,R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan
			,R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings};
	private EditText et_dialog_safe_passwordone;
	private EditText et_dialog_safe_passwordtwo;
	private AlertDialog passwordDialog;
	private Button btn_dialog_safe_setpass;
	private Button btn_dialog_safe_cancle;
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
				case 0:
					showSetPasswordDialog();
					break;

				default:
					break;
				}
			}
		});
	}

	protected void showSetPasswordDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this, R.layout.dialog_home_gd_safe_password, null);
		passwordDialog = builder.setView(view ).show();
		et_dialog_safe_passwordone = (EditText) view.findViewById(R.id.et_dialog_home_gd_safe_passwordone);
		et_dialog_safe_passwordtwo = (EditText) view.findViewById(R.id.et_dialog_home_gd_safe_passwordtwo);
		btn_dialog_safe_setpass = (Button) view.findViewById(R.id.btn_dialog_home_gd_safe_setpass);
		btn_dialog_safe_cancle = (Button) view.findViewById(R.id.btn_dialog_home_gd_safe_cancel);
		
		btn_dialog_safe_setpass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String passwordone = et_dialog_safe_passwordone.getText().toString().trim();
				String passwordtwo = et_dialog_safe_passwordtwo.getText().toString().trim();
				if(TextUtils.isEmpty(passwordone) || TextUtils.isEmpty(passwordtwo)) {
					Toast.makeText(HomeActivity.this, "密码的输入不能为空", Toast.LENGTH_SHORT).show();
				} else if(!passwordone.equals(passwordtwo)){
					Toast.makeText(HomeActivity.this, "两次密码的输入不一致", Toast.LENGTH_SHORT).show();
				} else {
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
