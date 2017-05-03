package com.itheima.day09_phoneguard_v1;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itheima.day09_phoneguard_v1.domain.APKInfo;
import com.itheima.day09_phoneguard_v1.engine.APKEngine;

public class CacheActivity extends Activity {

	protected static final int START = 1;
	protected static final int MESSAGE = 2;
	protected static final int FINISH = 3;
	public static final int FINISHONE = 4;
	public static final int CLEARALL = 5;
	private List<APKInfo> allAPKInfo;//所有应用信息
	private List<CacheData> allCacheData = new ArrayList<CacheActivity.CacheData>();//所有的缓存信息
	private Button btn_clearall;
	private ProgressBar pb_scanning;
	private TextView tv_scanning;
	private TextView tv_nodata;
	private LinearLayout ll_datas;
	private PackageManager pm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initEvent();
	}

	private void initView() {

		setContentView(R.layout.activity_cache);
		
		btn_clearall = (Button) findViewById(R.id.btn_cache_clearall);
		pb_scanning = (ProgressBar) findViewById(R.id.pb_cache_scanning);
		tv_scanning = (TextView) findViewById(R.id.tv_cache_scanning);
		tv_nodata = (TextView) findViewById(R.id.tv_cache_nodata);
		ll_datas = (LinearLayout) findViewById(R.id.ll_cache_datas);

		pm = getPackageManager();
		
		
	}

	private void initData() {

		new Thread() {

			public void run() {
				allCacheData.clear();
				ll_datas.removeAllViews();
				allAPKInfo = APKEngine.getAllAPKInfo(CacheActivity.this);
				mHandler.obtainMessage(START).sendToTarget();
				int i = 0;
				for (APKInfo apkInfo : allAPKInfo) {
					////////////////////////////////////////////
					SystemClock.sleep(50);///显示效果
					////////////////////////////////////////////
					i++;
					mHandler.obtainMessage(MESSAGE, i, 0, apkInfo.getAppName()).sendToTarget();
					getAPKCache(apkInfo);
				}
				mHandler.obtainMessage(FINISH).sendToTarget();
			};
		}.start();
	}

	private void initEvent() {
		btn_clearall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteAllCache();
			}
		});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case START:
				tv_nodata.setVisibility(View.GONE);
				ll_datas.setVisibility(View.GONE);
				tv_scanning.setText("准备中...");
				pb_scanning.setMax(allAPKInfo.size());
				break;
			case MESSAGE:
				tv_nodata.setVisibility(View.GONE);
				ll_datas.setVisibility(View.VISIBLE);
				pb_scanning.setProgress(msg.arg1);
				tv_scanning.setText("正在扫描:"+msg.obj);
				break;

			case FINISHONE:
				CacheData data = (CacheData) msg.obj;
				View view = View.inflate(CacheActivity.this, R.layout.item_cache, null);
				ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_item_cache_icon);
				TextView tv_name = (TextView) view.findViewById(R.id.tv_item_cache_name);
				TextView tv_size = (TextView) view.findViewById(R.id.iv_item_cache_size);
				iv_icon.setImageDrawable(data.icon);
				tv_name.setText(data.name);
				tv_size.setText(data.size);
				ll_datas.addView(view,0);
				break;

			case FINISH:
				if(allCacheData.size()==0) {
					//没有有缓存的应用
					tv_nodata.setVisibility(View.VISIBLE);
					ll_datas.setVisibility(View.GONE);
				}
				tv_scanning.setText("扫描完成!!");
				break;
				
			case CLEARALL:
				allCacheData.clear();
				ll_datas.removeAllViews();
				ll_datas.setVisibility(View.GONE);
				tv_nodata.setVisibility(View.VISIBLE);
				tv_nodata.setText((String)msg.obj);
				tv_scanning.setText("清理完成!!");
				break;	
				
			default:
				break;
			}
		};
	};


	
	private class CacheData {
		Drawable icon;
		String name;
		String size;//已经格式化好的Size
		long sizeLong;//没有格式化的Size
	}
	
	/**
	 * 通过反射来执行pm的getPackageSizeInfo方法
	 * @param apkInfo
	 * 
	 */
	private void getAPKCache(APKInfo apkInfo) {
		try {
			Class clazz = pm.getClass();
			MStatsObserver mStatsObserver = new MStatsObserver();
			mStatsObserver.apkInfo = apkInfo;
			Method method = clazz.getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
			method.invoke(pm, apkInfo.getPackName(),mStatsObserver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class MStatsObserver extends IPackageStatsObserver.Stub {

		APKInfo apkInfo;
		
		/* (non-Javadoc)
		 *  通过反射来执行pm的getPackageSizeInfo方法的回调
		 * @see android.content.pm.IPackageStatsObserver#onGetStatsCompleted(android.content.pm.PackageStats, boolean)
		 */
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {

			long cacheSize = pStats.cacheSize;
			if(cacheSize > 0) {
				//有缓存
					CacheData data = new CacheData();
					data.sizeLong = pStats.cacheSize;
					data.size = Formatter.formatFileSize(CacheActivity.this, cacheSize);
					data.icon = apkInfo.getIcon();
					data.name = apkInfo.getAppName();
					allCacheData.add(data);
					mHandler.obtainMessage(FINISHONE, data).sendToTarget();

			}else {
				//没有缓存,什么也不做
			}//end if else
			
		}
		
	}

	private void deleteAllCache() {
		//  public void freeStorageAndNotify(long freeStorageSize, IPackageDataObserver observer) 
		try {
			Class clazz = pm.getClass();
			Method method = clazz.getMethod("freeStorageAndNotify", long.class, IPackageDataObserver.class);
			method.invoke(pm, Long.MAX_VALUE, new MDataObserver());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class MDataObserver extends IPackageDataObserver.Stub {

		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)
				throws RemoteException {
			long size = 0;
			if(allCacheData.size()==0) {
				String obj = "恭喜您,本次一共节省了0B空间";
				mHandler.obtainMessage(CLEARALL, obj).sendToTarget();
				return ;
			}
			for (CacheData data : allCacheData) {
				size += data.sizeLong;
			}
			String obj = "恭喜您,本次一共节省了"+Formatter.formatFileSize(CacheActivity.this, size)+"空间";
			mHandler.obtainMessage(CLEARALL, obj).sendToTarget();
		}
		
	}
}
