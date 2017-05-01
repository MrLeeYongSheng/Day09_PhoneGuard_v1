package com.itheima.day09_phoneguard_v1;

import java.util.List;

import com.itheima.day09_phoneguard_v1.dao.AntivirusDao;
import com.itheima.day09_phoneguard_v1.domain.APKInfo;
import com.itheima.day09_phoneguard_v1.engine.APKEngine;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AntivirusActivity extends Activity {

	protected static final int START = 1;
	protected static final int MESSAGE = 2;
	protected static final int FINISH = 3;
	private ImageView iv_scanning;
	private TextView tv_desc;
	private ProgressBar pb_progress;
	private LinearLayout ll_app;
	private List<APKInfo> allAPKInfo;
	private RotateAnimation ra;
	private AntivirusDao adao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		setContentView(R.layout.activity_antivirus);
		
		iv_scanning = (ImageView) findViewById(R.id.iv_antivirus_scanning);
		tv_desc = (TextView) findViewById(R.id.tv_antivirus_desc);
		pb_progress = (ProgressBar) findViewById(R.id.pb_antivirus_progress);
		ll_app = (LinearLayout) findViewById(R.id.ll_antivirus_app);
		
		adao = new AntivirusDao(AntivirusActivity.this);
	}

	private void initData() {
		//准备动画
		ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(2000);
		ra.setRepeatCount(Animation.INFINITE);
		ra.setInterpolator(new Interpolator() {
			
			@Override
			public float getInterpolation(float x) {
				return 2*x;
			}
		});
		iv_scanning.setAnimation(ra);
		
		new Thread(){
			public void run() {
				
				allAPKInfo = APKEngine.getAllAPKInfo(AntivirusActivity.this);
				mHandler.obtainMessage(START).sendToTarget();
				int i = 0;
				for (APKInfo apkInfo : allAPKInfo) {
					i++;
					int virusState = -1;//1:是病毒,-1:不是病毒
					if(adao.isVirus(apkInfo.getApkPath())) {//是病毒
						virusState = 1;
					} else {
						//不是病毒,什么也不做
					}
					mHandler.obtainMessage(MESSAGE,i,virusState,apkInfo).sendToTarget();
					//////////////////////////////////////////////////////
					SystemClock.sleep(80); //显示效果
					//////////////////////////////////////////////////////
				}
				mHandler.obtainMessage(FINISH).sendToTarget();
			};
		}.start();
	}

	private void initEvent() {
		
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case START:
				pb_progress.setMax(allAPKInfo.size());
				break;
				
			case MESSAGE:
				int virusState = msg.arg2;
				APKInfo apkInfo = (APKInfo) msg.obj;
				pb_progress.setProgress(msg.arg1);
				TextView tv = new TextView(AntivirusActivity.this);
				tv.setText(apkInfo.getAppName());
				tv.setSingleLine(true);
				tv.setEllipsize(TruncateAt.END);
				if(virusState == -1) {
					tv.setTextColor(Color.BLACK);
				} else {
					tv.setTextColor(Color.RED);
				}
				ll_app.addView(tv, 0);
				break;
				
			case FINISH:
				ra.cancel();
				tv_desc.setText("扫描完成!");
				break;

			default:
				break;
			}
		};
	};


}
