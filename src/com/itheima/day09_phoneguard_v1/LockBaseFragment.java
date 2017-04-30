package com.itheima.day09_phoneguard_v1;

import java.util.ArrayList;
import java.util.List;

import com.itheima.day09_phoneguard_v1.dao.LockedDao;
import com.itheima.day09_phoneguard_v1.db.LockedTable;
import com.itheima.day09_phoneguard_v1.domain.APKInfo;
import com.itheima.day09_phoneguard_v1.engine.APKEngine;

import android.app.Fragment;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class LockBaseFragment extends Fragment {

	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	private View view;
	private TextView tv_left_softtype_tag;
	private ListView lv_left_data;
	private ProgressBar pb_left_loading;
	private List<APKInfo> allUnlockSysAPKInfo = new ArrayList<APKInfo>();//��������û������ϵͳAPK��Ϣ
	private List<APKInfo> allUnlockNotSysAPKInfo = new ArrayList<APKInfo>();//��������û�������û�APK��Ϣ
	private LockedDao ldao;;
	private List<APKInfo> allAPKInfo;//��������Ӧ�õ�����
	
	private List<String> allLockedList;
	public List<String> getAllLockedList() {
		return allLockedList;
	}

	public void setAllLockedList(List<String> allLockedList) {
		this.allLockedList = allLockedList;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		view = View.inflate(getActivity(), R.layout.fragment_left_unlock, null);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initView();
		initData();
		initEvent();
	}


	/**
	 * �ڸ÷������ʼ����ͼ
	 */
	private void initView() {
		tv_left_softtype_tag = (TextView) view.findViewById(R.id.tv_fragm_left_unlock_softtype_tag);
		lv_left_data = (ListView) view.findViewById(R.id.lv_framgm_left_unlock_data);
		pb_left_loading = (ProgressBar) view.findViewById(R.id.pb_fragm_left_unlock_loading);
		tv_left_count = (TextView) view.findViewById(R.id.tv_fragm_left_unlock_totalcount);
		ldao = new LockedDao(getActivity());
		//��ʾ����ProgressBar
		mHandler.obtainMessage(LOADING).sendToTarget();
		mAdapter = new MyAdapter();
		lv_left_data.setAdapter(mAdapter);
		
		

		
	}

	/**
	 * �ڸ÷������ʼ������
	 */
	protected void initData() {

		
		

		synchronized (new Object()) {
			new Thread(){
				public void run() {
				
					allUnlockNotSysAPKInfo.clear();
					allUnlockSysAPKInfo.clear();
					allAPKInfo = APKEngine.getAllAPKInfo(getActivity());
					for (APKInfo apkInfo : allAPKInfo) {
						//!ldao.isLocked(apkInfo.getPackName())
						if(isContainsLock(apkInfo.getPackName())) {
							if(apkInfo.isSystem()) {
								//ϵͳӦ��
								allUnlockSysAPKInfo.add(apkInfo);
							} else {
								//��ϵͳӦ��
								allUnlockNotSysAPKInfo.add(apkInfo);
							}
						}//end if
					}//end for
					//���������Ѿ��������
					//��ʾ����
					mHandler.obtainMessage(FINISH).sendToTarget();
				};
			}.start();
		}

	}//end initData

	protected boolean isContainsLock(String packName) {
		return allLockedList.contains(packName);
	}

	/**
	 * �ڸ÷������ʼ���¼�
	 */
	private void initEvent() {
		lv_left_data.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (firstVisibleItem < (1 + allUnlockNotSysAPKInfo.size())) {
					// ��ʾ�ĵ�һ������Ϊ: �û�����(123)
					tv_left_softtype_tag.setText("�������(" + allUnlockNotSysAPKInfo.size() + ")");
				} else if (firstVisibleItem == (1 + allUnlockNotSysAPKInfo.size())) {
					// ��ʾ�ĵ�һ������Ϊ: ϵͳ����(123)
					tv_left_softtype_tag.setText("ϵͳ���(" + allUnlockSysAPKInfo.size() + ")");
				}
			}
		});
	}//end initEvent
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING:
				tv_left_softtype_tag.setVisibility(View.GONE);
				lv_left_data.setVisibility(View.GONE);
				pb_left_loading.setVisibility(View.VISIBLE);
				break;
				
			case FINISH:
				tv_left_softtype_tag.setVisibility(View.VISIBLE);
				lv_left_data.setVisibility(View.VISIBLE);
				pb_left_loading.setVisibility(View.GONE);
				
				/////////////////////////////////////////////////////////////
				
				//tv_left_count.setText("���������("+(allUnlockNotSysAPKInfo.size()+allUnlockSysAPKInfo.size())+")");
				setTopText(tv_left_count,(allUnlockNotSysAPKInfo.size()+allUnlockSysAPKInfo.size()));
				//////////////////////////////////////////////////////////////
				mAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		};
	};
	private class Data {
		public ImageView iv_icon;
		public TextView tv_name;
		public ImageView iv_lock;
	}
	private MyAdapter mAdapter;
	private TextView tv_left_count;
	
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 1+1+allUnlockNotSysAPKInfo.size()+allUnlockSysAPKInfo.size();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			if (position == 0) {
				TextView tv_person = (TextView) View.inflate(
						getActivity(), R.layout.tv_soft, null);
				tv_person.setText("�������(" + allUnlockNotSysAPKInfo.size() + ")");
				return tv_person;
			} else if (position == 1 + allUnlockNotSysAPKInfo.size()) {
				TextView tv_system = (TextView) View.inflate(
						getActivity(), R.layout.tv_soft, null);
				tv_system.setText("ϵͳ���(" + allUnlockSysAPKInfo.size() + ")");
				return tv_system;
			} else {

				View view = null;
				Data data;

				if (convertView != null
						&& (convertView instanceof RelativeLayout)) {
					view = convertView;
					data = (Data) view.getTag();
				} else {
					view = View.inflate(getActivity(),
							R.layout.item_applocked_unlock, null);
					data = new Data();
					data.iv_icon = (ImageView) view
							.findViewById(R.id.iv_item_applocked_unlocked_icon);
					data.tv_name = (TextView) view
							.findViewById(R.id.tv_item_applocked_unlocked_name);
					data.iv_lock = (ImageView) view.findViewById(R.id.iv_item_applocked_unlocked_lock);
					view.setTag(data);
				}

				// ��ȡAPKInfo
				APKInfo apkInfo = getItem(position);

				// ����Ӧ��ͼ��
				data.iv_icon.setImageDrawable(apkInfo.getIcon());
				// ����Ӧ������
				data.tv_name.setText(apkInfo.getAppName());
				//////////////////////////////////////////////////////////////////////////
				//����״̬ѡ�������ߵ���¼�
				//data.iv_lock.setImageResource(R.drawable.selector_applocked_unlocked);
				setImageResourceOrEvent(data.iv_lock,ldao,apkInfo.getPackName(),view);
				//////////////////////////////////////////////////////////////////////////
				
				return view;
			}
		}

		@Override
		public APKInfo getItem(int position) {
			APKInfo apkInfo;
			if (position <= allUnlockNotSysAPKInfo.size()) { // �������
				apkInfo = allUnlockNotSysAPKInfo.get(position - 1);

			} else { // ϵͳ���

				apkInfo = allUnlockSysAPKInfo.get(position - 2
						- allUnlockNotSysAPKInfo.size());
			}
			return apkInfo;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}

	protected void setImageResourceOrEvent(ImageView iv_lock, LockedDao ldao, String packName, View view) {
		iv_lock.setImageResource(R.drawable.selector_applocked_unlocked);
	}

	protected void setTopText(TextView tv_left_count, int totalSize) {
		tv_left_count.setText("���������("+totalSize+")");
	}
}
