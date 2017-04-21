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
				//���ڼ�������
				//��ʾprogressBar
				pb_loading.setVisibility(View.VISIBLE);
				//������ʾ���ݵ�ListView 
				lv_blacklist.setVisibility(View.GONE);
				//������ʾû���ݵ�TextView 
				tv_nodata.setVisibility(View.GONE);
				break;
				
			case FINISH://����������ɺ�
				
				//������
				//����progressBar
				pb_loading.setVisibility(View.GONE);
				//��ʾ ��ʾ���ݵ�ListView
				lv_blacklist.setVisibility(View.VISIBLE);
				//������ʾû���ݵ�TextView 
				tv_nodata.setVisibility(View.GONE);
				
				//û����
				//����progressBar
				pb_loading.setVisibility(View.GONE);
				//������ʾ���ݵ�ListView
				lv_blacklist.setVisibility(View.GONE);
				//������ʾû���ݵ�TextView 
				tv_nodata.setVisibility(View.VISIBLE);
				
				break;

			default:
				break;
			}
		};
	};
	
	private void initView() {

		setContentView(R.layout.activity_telesms);
		//���Ӻ�������ť
		btn_add = (Button) findViewById(R.id.btn_telesms_add);
		//��ʾ���ݵ�listView
		lv_blacklist = (ListView) findViewById(R.id.lv_telesms_blacklist);
		//��ʾû�����ݵ�TextView
		tv_nodata = (TextView) findViewById(R.id.tv_telesms_nodata);
		//��ʾ�������ݵ�Progressbar
		pb_loading = (ProgressBar) findViewById(R.id.pb_telesms_loading);
	}
}
