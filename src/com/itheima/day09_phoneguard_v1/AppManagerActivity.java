package com.itheima.day09_phoneguard_v1;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.itheima.day09_phoneguard_v1.domain.APKInfo;
import com.itheima.day09_phoneguard_v1.engine.APKEngine;

public class AppManagerActivity extends Activity {

	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	private TextView tv_romsize;
	private TextView tv_sdsize;
	private MyAdapter mAdapter;
	private List<APKInfo> allAPKInfo = new ArrayList<APKInfo>();
	private List<APKInfo> allNotSysAPKInfo = new ArrayList<APKInfo>();
	private List<APKInfo> allSysAPKInfo = new ArrayList<APKInfo>();
	private ListView lv_apkinfo;// ��ʾ���ݵ�ListView
	private RelativeLayout rl_storage; // ��ʾ�洢�ռ�ĸ�����
	private ProgressBar pb_loading; // ����ʱ��ʾ��progressbar
	private TextView tv_soft_title; // ����ʱ��ʾ��������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initData();
		initEvent();
	}

	private PopupWindow pw; // ��������,�ĸ����ܵ����

	/**
	 * @param parent
	 *            ��ʾ��������
	 * @param x
	 *            ��������Ͻ���Ҫ ��ʾ��xλ��
	 * @param y
	 *            ��������Ͻ���Ҫ��ʾ��yλ��
	 */
	private void showPopupWindow(View parent, int x, int y) {
		// ��ʾ֮ǰ�Ȱ�֮ǰ��ʾ�Ĵ���ر�
		closePopupWindow();
		pw.showAtLocation(parent, Gravity.TOP | Gravity.LEFT, x, y);
	}

	private void closePopupWindow() {
		if (pw != null && pw.isShowing()) {
			pw.dismiss();
			pw = null;
		}
	}

	private void initEvent() {

		lv_apkinfo.setOnItemClickListener(new OnItemClickListener() {

			private ImageView iv_remove;
			private ImageView iv_setting;
			private ImageView iv_startup;
			private ImageView iv_share;
			private View contentView;
			private APKInfo apkInfo;
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					 int position, long id) {

				apkInfo = (APKInfo) lv_apkinfo.getItemAtPosition(position);
				// ���ô��嶯��
				ScaleAnimation sa = new ScaleAnimation(0f, 1f, 0.5f, 1f,
						Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				sa.setDuration(500L);

				contentView = View.inflate(AppManagerActivity.this,
						R.layout.popup_appman, null);
				contentView.setAnimation(sa);
				initClickView();
				initClickEvent();
				pw = new PopupWindow(contentView, -2, -2);
				pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				// ��ʾ����
				int[] location = new int[2];
				view.getLocationInWindow(location);
				showPopupWindow(view, location[0] + 50, location[1]);
			}

			private void initClickEvent() {
				OnClickListener listener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						closePopupWindow();
						switch (v.getId()) {
						case R.id.iv_popup_appman_remove:
							removeAPK();
							break;

						case R.id.iv_popup_appman_setting:
							/*
							 * act=android.settings.APPLICATION_DETAILS_SETTINGS
							 * dat=package:com.itheima.day07_030_notificationnow
							 */
							Intent settingIntent = new Intent();
							settingIntent
									.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
							settingIntent.setData(Uri.parse("package:"
									+ apkInfo.getPackName()));
							startActivity(settingIntent);
							break;

						case R.id.iv_popup_appman_share:
							showShare();
							break;

						case R.id.iv_popup_appman_startup:
							Intent startupIntent = pm
									.getLaunchIntentForPackage(apkInfo
											.getPackName());
							startActivity(startupIntent);
							break;

						default:
							break;
						}
					}

					private void removeAPK() {

						/*
							<intent-filter>
				                <action android:name="android.intent.action.VIEW" />
				                <action android:name="android.intent.action.DELETE" />
				                <category android:name="android.intent.category.DEFAULT" />
				                <data android:scheme="package" />
				            </intent-filter>
						 */
						if(!apkInfo.isSystem()) { //�û�Ӧ��
							Intent intent = new Intent();
							intent.setAction("android.intent.action.DELETE");
							intent.addCategory("android.intent.category.DEFAULT");
							intent.setData(Uri.parse("package:" + apkInfo.getPackName()));
							startActivity(intent);
						} else { //ϵͳӦ��
							
						}
					}
				};
				iv_remove.setOnClickListener(listener);
				iv_setting.setOnClickListener(listener);
				iv_startup.setOnClickListener(listener);
				iv_share.setOnClickListener(listener);
			}

			private void initClickView() {
				iv_remove = (ImageView) contentView
						.findViewById(R.id.iv_popup_appman_remove);
				iv_setting = (ImageView) contentView
						.findViewById(R.id.iv_popup_appman_setting);
				iv_startup = (ImageView) contentView
						.findViewById(R.id.iv_popup_appman_startup);
				iv_share = (ImageView) contentView
						.findViewById(R.id.iv_popup_appman_share);
			}
		});

		lv_apkinfo.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				// ����ʱ�Ѵ���ر�
				closePopupWindow();

				if (firstVisibleItem < (1 + allNotSysAPKInfo.size())) {
					// ��ʾ�ĵ�һ������Ϊ: �������(123)
					tv_soft_title.setText("�������(" + allNotSysAPKInfo.size()
							+ ")");
				} else if (firstVisibleItem == (1 + allNotSysAPKInfo.size())) {
					// ��ʾ�ĵ�һ������Ϊ: ϵͳ���(123)
					tv_soft_title.setText("ϵͳ���(" + allSysAPKInfo.size() + ")");
				}
			}
		});
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING:
				lv_apkinfo.setVisibility(View.GONE);
				rl_storage.setVisibility(View.GONE);
				pb_loading.setVisibility(View.VISIBLE);
				tv_soft_title.setVisibility(View.GONE);
				break;

			case FINISH:
				lv_apkinfo.setVisibility(View.VISIBLE);
				rl_storage.setVisibility(View.VISIBLE);
				pb_loading.setVisibility(View.GONE);
				tv_soft_title.setVisibility(View.VISIBLE);
				// ��ʾ�ĵ�һ������Ϊ: �������(123)
				tv_soft_title.setText("ϵͳ���(" + allNotSysAPKInfo.size() + ")");
				// ֪ͨ���������ı�,��ʾ����
				mAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		};
	};
	private PackageManager pm;

	private void initData() {
		
		allNotSysAPKInfo.clear();
		allSysAPKInfo.clear();
		allAPKInfo.clear();
		
		// ����������ʾprogressbar
		mHandler.obtainMessage(LOADING).sendToTarget();

		// SD�����ÿռ�:123M
		long sdSize = APKEngine.getSDFreeSpace();
		String formatSDSize = Formatter.formatFileSize(AppManagerActivity.this,
				sdSize);
		tv_sdsize.setText("SD�����ÿռ�:" + formatSDSize);
		// ROM���ÿռ�:123M
		String formatROMSize = Formatter.formatFileSize(
				AppManagerActivity.this, APKEngine.getROMFreeSpace());
		tv_romsize.setText("ROM���ÿռ�:" + formatROMSize);

		new Thread() {
			public void run() {
				// ��ȡ����
				allAPKInfo = APKEngine.getAllAPKInfo(AppManagerActivity.this);
				for (APKInfo apkInfo : allAPKInfo) {
					if (apkInfo.isSystem()) {
						allSysAPKInfo.add(apkInfo);
					} else {
						allNotSysAPKInfo.add(apkInfo);
					}
				}
				// ���ݼ������,��ʾ����
				mHandler.obtainMessage(FINISH).sendToTarget();
			};
		}.start();
	}

	private class Data {
		public ImageView iv_icon;
		public TextView tv_name;
		public TextView tv_storage;
		public TextView tv_size;
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return allAPKInfo.size() + 2; // �ȼ���1+1+allNotSysAPKInfo.size()+allSysAPKInfo.size()
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (position == 0) {
				TextView tv_person = (TextView) View.inflate(
						AppManagerActivity.this, R.layout.tv_soft, null);
				tv_person.setText("�������(" + allNotSysAPKInfo.size() + ")");
				return tv_person;
			} else if (position == 1 + allNotSysAPKInfo.size()) {
				TextView tv_system = (TextView) View.inflate(
						AppManagerActivity.this, R.layout.tv_soft, null);
				tv_system.setText("ϵͳ���(" + allSysAPKInfo.size() + ")");
				return tv_system;
			} else {

				View view = null;
				Data data;

				if (convertView != null
						&& (convertView instanceof RelativeLayout)) {
					view = convertView;
					data = (Data) view.getTag();
				} else {
					view = View.inflate(AppManagerActivity.this,
							R.layout.item_appmanager, null);
					data = new Data();
					data.iv_icon = (ImageView) view
							.findViewById(R.id.iv_item_appman_icon);
					data.tv_name = (TextView) view
							.findViewById(R.id.tv_item_appman_name);
					data.tv_size = (TextView) view
							.findViewById(R.id.tv_item_appman_size);
					data.tv_storage = (TextView) view
							.findViewById(R.id.tv_item_appman_storage);
					view.setTag(data);
				}

				// ��ȡAPKInfo
				APKInfo apkInfo = getItem(position);

				// ����Ӧ��ͼ��
				data.iv_icon.setImageDrawable(apkInfo.getIcon());
				// ����Ӧ������
				data.tv_name.setText(apkInfo.getAppName());
				// ����Ӧ�ô�С
				String formatSize = Formatter.formatFileSize(
						AppManagerActivity.this, apkInfo.getSize());
				data.tv_size.setText(formatSize);
				// ���ô洢λ��
				if (apkInfo.isSd()) {
					data.tv_storage.setText("SD���洢");
				} else {
					data.tv_storage.setText("ROM�洢");
				}

				return view;
			}
		}

		@Override
		public APKInfo getItem(int position) {
			APKInfo apkInfo;
			if (position <= allNotSysAPKInfo.size()) { // �������
				apkInfo = allNotSysAPKInfo.get(position - 1);

			} else { // ϵͳ���
				// position-2-allNotSysAPKInfo.size()�ȼ�position-1-1-allNotSysAPKInfo.size()
				// ��0��ʼ��ȡallSysAPKInfo������
				apkInfo = allSysAPKInfo.get(position - 2
						- allNotSysAPKInfo.size());
			}
			return apkInfo;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {

			initData();
		}
	};
	
	private void initView() {
		setContentView(R.layout.activity_appmanager);
		lv_apkinfo = (ListView) findViewById(R.id.lv_appman_apkinfo);
		tv_romsize = (TextView) findViewById(R.id.tv_appman_romsize);
		tv_sdsize = (TextView) findViewById(R.id.tv_appman_sdsize);
		rl_storage = (RelativeLayout) findViewById(R.id.rl_appman_storage);// ��ʾ�洢�ռ�ĸ�����
		pb_loading = (ProgressBar) findViewById(R.id.pb_appman_loading);
		tv_soft_title = (TextView) findViewById(R.id.tv_appman_softtitle);// ����ʱ��ʾ��������
		mAdapter = new MyAdapter();
		lv_apkinfo.setAdapter(mAdapter);

		pm = getPackageManager();
		ShareSDK.initSDK(this);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(receiver, filter );
	}

	private void showShare() {
		OnekeyShare oks = new OnekeyShare();
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ��QQ�ռ�ʹ��
		oks.setTitle("���Ա���");
		// titleUrl�Ǳ�����������ӣ�����Linked-in,QQ��QQ�ռ�ʹ��
		oks.setTitleUrl("http://sharesdk.cn");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText("���Է����ı�");
		// ��������ͼƬ������΢����������ͼƬ��Ҫͨ����˺�����߼�д��ӿڣ�������ע�͵���������΢��
		oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		// oks.setImagePath(Environment.getExternalStorageDirectory()+"/test.jpg");//ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl("http://sharesdk.cn");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("���ǲ��������ı�");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite("ShareSDK");
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl("http://sharesdk.cn");

		// ��������GUI
		oks.show(this);
	}

	@Override
	protected void onDestroy() {
		closePopupWindow();
		unregisterReceiver(receiver);
		super.onDestroy();
	}
}
