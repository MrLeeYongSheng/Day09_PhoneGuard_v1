package com.itheima.day09_phoneguard_v1;

import java.util.List;

import com.itheima.day09_phoneguard_v1.dao.LockedDao;
import com.itheima.day09_phoneguard_v1.db.LockedTable;
import com.itheima.day09_phoneguard_v1.domain.APKInfo;
import com.itheima.day09_phoneguard_v1.engine.APKEngine;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AppLockActivity extends FragmentActivity {

	private TextView tv_left_unlocked;
	private TextView tv_right_locked;
	private FragmentManager fm;
	private AppUnlockedFragment unlockedFragm;
	private AppLockedFragment lockedFragm;
	private List<String> allLockedList;
	private FragmentTransaction transaction;
	private ContentObserver observer;
	private LockedDao ldao;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initView();
		initData();
		initEvent();
	}

	private void initEvent() {
		OnClickListener mListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (fm == null) {
					fm = getFragmentManager();
				}
				transaction = fm.beginTransaction();
				if (v.getId() == R.id.tv_applock_left_unlocked) {
					// 点击未加锁(左边按钮)
					transaction.replace(R.id.fl_applock, unlockedFragm);
					tv_right_locked
							.setBackgroundResource(R.drawable.tab_right_default);
					tv_left_unlocked
							.setBackgroundResource(R.drawable.tab_left_pressed);
				} else if (v.getId() == R.id.tv_applock_right_locked) {
					// 点击已上锁(右边按钮)
					transaction.replace(R.id.fl_applock, lockedFragm);
					tv_right_locked
							.setBackgroundResource(R.drawable.tab_right_pressed);
					tv_left_unlocked
							.setBackgroundResource(R.drawable.tab_left_default);
				}// end if
				transaction.commit();
			}
		};
		tv_right_locked.setOnClickListener(mListener);
		tv_left_unlocked.setOnClickListener(mListener);
	}

	private void initData() {
		synchronized (AppLockActivity.this) {
			new Thread() {

				public void run() {
					allLockedList = ldao.getAllDatas();
					unlockedFragm.setAllLockedList(allLockedList);
					lockedFragm.setAllLockedList(allLockedList);
					transaction.commit();
				};
			}.start();
			transaction = fm.beginTransaction();
			transaction.replace(R.id.fl_applock, unlockedFragm);
			tv_right_locked.setBackgroundResource(R.drawable.tab_right_default);
			tv_left_unlocked.setBackgroundResource(R.drawable.tab_left_pressed);
		}
	}

	private void initView() {

		setContentView(R.layout.activity_applock);
		tv_right_locked = (TextView) findViewById(R.id.tv_applock_right_locked);
		tv_left_unlocked = (TextView) findViewById(R.id.tv_applock_left_unlocked);
		unlockedFragm = new AppUnlockedFragment();
		lockedFragm = new AppLockedFragment();
		fm = getFragmentManager();

		ldao = new LockedDao(AppLockActivity.this);

		observer = new ContentObserver(new Handler()) {
			@Override
			public void onChange(boolean selfChange) {
				super.onChange(selfChange);
				new Thread() {
					public void run() {

						allLockedList = ldao.getAllDatas();
						unlockedFragm.setAllLockedList(allLockedList);
						lockedFragm.setAllLockedList(allLockedList);

					};
				}.start();

			}
		};
		getContentResolver().registerContentObserver(LockedTable.URI, true,
				observer);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (observer != null) {
			getContentResolver().unregisterContentObserver(observer);
			observer = null;
		}
	}

}
