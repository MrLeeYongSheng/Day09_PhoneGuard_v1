package com.itheima.day09_phoneguard_v1;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TeleSmsActivity extends Activity {

	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	private ListView lv_blacklist;
	private Button btn_add;
	private TextView tv_nodata;
	private ProgressBar pb_loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING:
				//正在加载数据
				//显示progressBar
				pb_loading.setVisibility(View.VISIBLE);
				//隐藏显示数据的ListView 
				lv_blacklist.setVisibility(View.GONE);
				//隐藏显示没数据的TextView 
				tv_nodata.setVisibility(View.GONE);
				break;
				
			case FINISH://加载数据完成后
				
				//有数据
				//隐藏progressBar
				pb_loading.setVisibility(View.GONE);
				//显示 显示数据的ListView
				lv_blacklist.setVisibility(View.VISIBLE);
				//隐藏显示没数据的TextView 
				tv_nodata.setVisibility(View.GONE);
				
				//没数据
				//隐藏progressBar
				pb_loading.setVisibility(View.GONE);
				//隐藏显示数据的ListView
				lv_blacklist.setVisibility(View.GONE);
				//隐藏显示没数据的TextView 
				tv_nodata.setVisibility(View.VISIBLE);
				
				break;

			default:
				break;
			}
		};
	};
	
	private void initView() {

		setContentView(R.layout.activity_telesms);
		//增加黑名单按钮
		btn_add = (Button) findViewById(R.id.btn_telesms_add);
		//显示数据的listView
		lv_blacklist = (ListView) findViewById(R.id.lv_telesms_blacklist);
		//显示没有数据的TextView
		tv_nodata = (TextView) findViewById(R.id.tv_telesms_nodata);
		//显示加载数据的Progressbar
		pb_loading = (ProgressBar) findViewById(R.id.pb_telesms_loading);
	}
}
