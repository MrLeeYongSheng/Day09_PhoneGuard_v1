package com.itheima.day09_phoneguard_v1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.day09_phoneguard_v1.domain.ProcessBean;
import com.itheima.day09_phoneguard_v1.engine.ProcessEngine;
import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;

public class TaskManagerActivity extends Activity {
	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	private RelativeLayout rl_taskTotalInfo;// 顶上状态标题
	private TextView tv_procCount;// 顶上状态标题 的进程数目
	private TextView tv_freeMem;// 顶上状态标题 的空余内存/总内存
	private ProgressBar pb_loading;// 加载时的ProgressBar
	private ListView lv_procInfo;// 显示的数据的ListView
	private TextView tv_procType;// 总是显示在ListView的顶端的进程种类
	private List<ProcessBean> allNotSysProInfo = new CopyOnWriteArrayList<ProcessBean>();// 非系统进程
	private List<ProcessBean> allSysProInfo = new CopyOnWriteArrayList<ProcessBean>();// 系统进程
	private long totalMem;//记录总的进程内存
	private long freeMem;//记录可用进程内存
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initEvent();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initData();
		setInfoTitle();
	}
	
	private void initView() {

		setContentView(R.layout.activity_taskmanager);
		rl_taskTotalInfo = (RelativeLayout) findViewById(R.id.rl_taskman_taskTotalInfo);
		tv_procCount = (TextView) findViewById(R.id.tv_taskman_procCount);
		tv_freeMem = (TextView) findViewById(R.id.tv_taskman_freeMem);
		pb_loading = (ProgressBar) findViewById(R.id.pb_taskman_loading);
		lv_procInfo = (ListView) findViewById(R.id.lv_taskman_procInfo);
		tv_procType = (TextView) findViewById(R.id.tv_taskman_procType);

		btn_func_clear = (Button) findViewById(R.id.btn_taskman_func_clear);
		btn_func_all = (Button) findViewById(R.id.btn_taskman_func_all);
		btn_func_notall = (Button) findViewById(R.id.btn_taskman_func_notall);
		btn_func_setting = (Button) findViewById(R.id.btn_taskman_func_setting);

		mAdapter = new MyAdapter();
		lv_procInfo.setAdapter(mAdapter);

		mHandler.obtainMessage(LOADING).sendToTarget();

		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	}

	private void initData() {
		
		freeMem = ProcessEngine.getFreeMemory(TaskManagerActivity.this);
		totalMem = ProcessEngine.getTotalMemory();
		
		new Thread() {
			public void run() {
				List<ProcessBean> allProInfo = new ArrayList<ProcessBean>();// 全部进程
				allSysProInfo.clear();
				allNotSysProInfo.clear();
				allProInfo = ProcessEngine
						.getAllProcInfo(TaskManagerActivity.this);
				for (ProcessBean bean : allProInfo) {// 分类数据
					if (bean.isSystem()) {// 系统进程
						allSysProInfo.add(bean);
					} else {// 非系统进程
						allNotSysProInfo.add(bean);
					}
				}
				// 通知界面改变
				mHandler.obtainMessage(FINISH).sendToTarget();
			};
		}.start();
	}

	private void initEvent() {
		lv_procInfo.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (firstVisibleItem < (1 + allNotSysProInfo.size())) {
					// 显示的第一条数据为: 用户进程(123)
					tv_procType.setText("用户进程(" + allNotSysProInfo.size() + ")");
				} else if (firstVisibleItem == (1 + allNotSysProInfo.size())) {
					// 显示的第一条数据为: 系统进程(123)
					tv_procType.setText("系统进程(" + allSysProInfo.size() + ")");
				}
			}
		});

		btn_func_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (ProcessBean bean : allNotSysProInfo) {
					if (!bean.getPackName().equals(getPackageName())) {
						// 非本应用的包名
						bean.setChecked(true);
					} else {// 本应用的包名
						bean.setChecked(false);
					}
				}

				for (ProcessBean bean : allSysProInfo) {
					bean.setChecked(true);// 系统进程,绝不包含本应用包名
				}
				// 通知界面改变
				mAdapter.notifyDataSetChanged();
			}
		});

		btn_func_notall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (ProcessBean bean : allNotSysProInfo) {
					bean.setChecked(false);// 非系统进程
				}

				for (ProcessBean bean : allSysProInfo) {
					bean.setChecked(false);// 系统进程
				}
				// 通知界面改变
				mAdapter.notifyDataSetChanged();
			}
		});

		btn_func_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int killProc = 0;
				long clearMem = 0L;
				for (ProcessBean bean : allNotSysProInfo) {
					if (bean.isChecked()) {
						// 非系统进程
						am.killBackgroundProcesses(bean.getPackName());
						killProc++;
						clearMem += bean.getMemSize();
						allNotSysProInfo.remove(bean);
					}
				}

				for (ProcessBean bean : allSysProInfo) {
					// 系统进程
					if (bean.isChecked()) {
						am.killBackgroundProcesses(bean.getPackName());
						killProc++;
						clearMem += bean.getMemSize();
						allSysProInfo.remove(bean);
					}
				}

				Toast.makeText(
						TaskManagerActivity.this,
						"清理了"
								+ killProc
								+ "个进程,释放了"
								+ Formatter.formatFileSize(
										TaskManagerActivity.this, clearMem)
								+ "内存", Toast.LENGTH_SHORT).show();
				
				freeMem += clearMem;
				setInfoTitle();
				mAdapter.notifyDataSetChanged();

			}
		});
		
		btn_func_setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(TaskManagerActivity.this, TaskManagerSettingActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void setInfoTitle() {
		if(SharedPreferencesUtils.getBoolean(TaskManagerActivity.this, MyConstants.DISPLAY_SYS_PROC, false)) {
			//显示系统进程
			tv_procCount.setText("运行中的进程:" + (allNotSysProInfo.size() + allSysProInfo.size()));
		} else {
			tv_procCount.setText("运行中的进程:" + allNotSysProInfo.size());
		}
		String formatFreeMemSize = Formatter.formatFileSize(
				TaskManagerActivity.this,
				freeMem);
		String formatTotalMemize = Formatter.formatFileSize(
				TaskManagerActivity.this, totalMem);
		tv_freeMem.setText("可用/总内存:" + formatFreeMemSize + "/"
				+ formatTotalMemize);
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING:
				lv_procInfo.setVisibility(View.GONE);
				rl_taskTotalInfo.setVisibility(View.GONE);
				pb_loading.setVisibility(View.VISIBLE);
				tv_procType.setVisibility(View.GONE);
				break;

			case FINISH:
				lv_procInfo.setVisibility(View.VISIBLE);
				rl_taskTotalInfo.setVisibility(View.VISIBLE);
				pb_loading.setVisibility(View.GONE);
				tv_procType.setVisibility(View.VISIBLE);
				// 显示顶上状态标题 的进程数目
				setInfoTitle();
				mAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}

	};
	private MyAdapter mAdapter;
	private Button btn_func_clear;
	private Button btn_func_all;
	private Button btn_func_notall;
	private Button btn_func_setting;
	private ActivityManager am;

	private class Data {
		public ImageView iv_icon;
		public TextView tv_name;
		public TextView tv_procSize;
		public CheckBox cb_select;
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if(SharedPreferencesUtils.getBoolean(TaskManagerActivity.this, MyConstants.DISPLAY_SYS_PROC, false)) {
				//显示系统进程
				return allNotSysProInfo.size() + allSysProInfo.size() + 2; 
			} else {
				//不显示系统进程
				return allNotSysProInfo.size()+1;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (position == 0) {
				TextView tv_person = (TextView) View.inflate(
						TaskManagerActivity.this, R.layout.tv_soft, null);
				tv_person.setText("用户进程(" + allNotSysProInfo.size() + ")");
				return tv_person;
			} else if (position == 1 + allNotSysProInfo.size()) {
				TextView tv_system = (TextView) View.inflate(
						TaskManagerActivity.this, R.layout.tv_soft, null);
				tv_system.setText("系统进程(" + allSysProInfo.size() + ")");
				return tv_system;
			} else {

				View view = null;
				final Data data;

				if (convertView != null
						&& (convertView instanceof RelativeLayout)) {
					view = convertView;
					data = (Data) view.getTag();
				} else {
					view = View.inflate(TaskManagerActivity.this,
							R.layout.item_taskmanager, null);
					data = new Data();
					data.iv_icon = (ImageView) view
							.findViewById(R.id.iv_item_taskman_icon);
					data.tv_name = (TextView) view
							.findViewById(R.id.tv_item_taskman_name);
					data.tv_procSize = (TextView) view
							.findViewById(R.id.tv_item_taskman_procSize);
					data.cb_select = (CheckBox) view
							.findViewById(R.id.cb_item_taskman_select);
					view.setTag(data);
				}

				// 获取ProcessBean
				final ProcessBean proInfo = getItem(position);

				// 设置进程图标
				data.iv_icon.setImageDrawable(proInfo.getIcon());
				// 设置进程名字
				data.tv_name.setText(proInfo.getName());
				// 设置进程占用大小
				String formatSize = Formatter.formatFileSize(
						TaskManagerActivity.this, proInfo.getMemSize());
				data.tv_procSize.setText(formatSize);

				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (proInfo.getPackName().equals(getPackageName())) {// 本应用包名
							proInfo.setChecked(false);
						} else {
							// 非本应用包名
							proInfo.setChecked(!proInfo.isChecked());
							data.cb_select.setChecked(proInfo.isChecked());
						}
					}
				});
				data.cb_select.setChecked(proInfo.isChecked());
				if (proInfo.getPackName().equals(getPackageName())) {// 本应用包名
					// 阉割CheckBox
					data.cb_select.setVisibility(View.GONE);
				} else {
					data.cb_select.setVisibility(View.VISIBLE);
				}
				return view;
			}
		}

		@Override
		public ProcessBean getItem(int position) {
			if (position <= allNotSysProInfo.size()) {
				return allNotSysProInfo.get(position - 1);
			} else {
				return allSysProInfo
						.get(position - allNotSysProInfo.size() - 2);
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}
}
