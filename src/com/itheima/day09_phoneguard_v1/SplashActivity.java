package com.itheima.day09_phoneguard_v1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.day09_phoneguard_v1.domain.UrlBean;
import com.itheima.day09_phoneguard_v1.utils.JsonParser2UrlBeanUtils;
import com.itheima.day09_phoneguard_v1.utils.MyConstants;
import com.itheima.day09_phoneguard_v1.utils.SharedPreferencesUtils;
import com.itheima.day09_phoneguard_v1.utils.StreamUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends Activity {

	private static final int VERSON_OK = 0;
	private static final int VERSON_LOW = 1;
	protected static final int ERROR = 2;
	private RelativeLayout rl_root;
	private TextView tv_version;
	private int versionCode;
	private UrlBean bean;
	private long startTimeMillis;
	private long endTimeMillis;
	private String versionName;
	private String fileName;
	private ProgressBar pb_downloading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
		animation();

		//耗时的操作防盗timeConsuming里,例如资源的获取,在动画播放开始时就使用了该方法
		//方法如下
	}

	/**
	 * 耗时的操作放在里面
	 */
	private void timeConsuming() {
		
		if(SharedPreferencesUtils.getBoolean(SplashActivity.this, MyConstants.AUTO_UPDATE, false)) {
			// startTimeMillis = System.currentTimeMillis();
			startTimeMillis = SystemClock.currentThreadTimeMillis();
			connection();
		}
		File file = new File(getFilesDir(), "address.db");
//		File file = new File("/data/data/"+getPackageName()+"/files", "address.db");
		if(!file.exists()) {
			//把assert文件夹里的归属地数据库拷贝到本地
			downPhoneLocationDB("address.db");
		}
		
		//antivirus.db
		File file2 = new File(getFilesDir(), "antivirus.db");
		if(!file2.exists()) {
			//把assert文件夹里的病毒数据库拷贝到本地
			downPhoneLocationDB("antivirus.db");
		}
	}
	
	/**
	 * 把assert文件夹里的归属地数据库拷贝到本地
	 */
	private void downPhoneLocationDB(final String file) {

		new Thread(){
			public void run() {
				AssetManager assets = getAssets();
				InputStream is = null;
				FileOutputStream fos = null;
				try {
					is = assets.open(file);
					fos = openFileOutput(file, MODE_PRIVATE);
					byte[] buf = new byte[10240];
					int len = 0;
					int i = 1;
					while((len=is.read(buf)) > 0) {
						i++;
						fos.write(buf, 0, len);
						if(i%10 == 0) {
							fos.flush();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if(is!=null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(fos!=null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
		}.start();
	}

	private void initData() {
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo;
			packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			versionCode = packageInfo.versionCode;
			versionName = packageInfo.versionName;
			if (TextUtils.isEmpty(versionName)) {
				tv_version.setText("通用");
			} else {
				tv_version.setText(versionName);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void connection() {

		new Thread() {

			public void run() {
				String path = "http://10.0.2.2:8080/splash.json";
				HttpURLConnection conn = null;
				InputStream is = null;
				int errorCode = -1;// -1表示正常
				try {
					URL url = new URL(path);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					int responseCode = conn.getResponseCode();
					if (responseCode == 200) {
						is = conn.getInputStream();
						String result = StreamUtil.parser(is);
						bean = JsonParser2UrlBeanUtils.parser(result);
					} else {
						// 404 找不到地址/资源
						System.out.println(404);
						errorCode = 404;
					}
				} catch (IOException e) {
					// 4001 连接网络失败
					errorCode = 4001;
					System.out.println(4001);
					e.printStackTrace();
				} catch (JSONException e) {
					// 4002 json格式解析错误
					errorCode = 4002;
					System.out.println(4002);
					e.printStackTrace();
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (conn != null) {
						conn.disconnect();
					}
					Message msg = Message.obtain();
					if (errorCode == -1) {
						msg.what = isNewVerson(bean);
					} else {
						msg.what = ERROR;
						msg.arg1 = errorCode;
					}

					endTimeMillis = SystemClock.currentThreadTimeMillis();
					if (endTimeMillis - startTimeMillis < 3000L) {
						SystemClock
								.sleep(3000L - (endTimeMillis - startTimeMillis));
					}
					mHandler.sendMessage(msg);
				}
			}

		}.start();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case VERSON_OK:
				gotoMain();
				break;
			case VERSON_LOW:
				showAlertDiaload();
				break;
			case ERROR:
				switch (msg.arg1) {
				case 404:// 资源找不到
					Toast.makeText(SplashActivity.this, "404 目标资源找不到",
							Toast.LENGTH_SHORT).show();
					break;
				case 4001:// 网络连接错误
					Toast.makeText(SplashActivity.this, "4001 网络连接错误",
							Toast.LENGTH_SHORT).show();
					break;
				case 4002:// 4002 json格式解析错误
					Toast.makeText(SplashActivity.this, "4002 json格式解析错误",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				gotoMain();
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 运行在子线程里
	 * 
	 * @param bean
	 */
	private int isNewVerson(UrlBean bean) {

		if (bean.getVerson() > versionCode) {
			return VERSON_LOW;
		} else {
			return VERSON_OK;
		}

	}

	protected void showAlertDiaload() {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				SplashActivity.this);
		builder.setTitle("更新最新版本")
				.setMessage("版本特性 : " + bean.getDesc())
				.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						gotoMain();
					}
				})
				.setPositiveButton("更新", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(SplashActivity.this, "准备下载",
								Toast.LENGTH_SHORT).show();
						downloadNewApk();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						gotoMain();
					}
				}).show();
	}

	protected void downloadNewApk() {

		HttpUtils httpUtils = new HttpUtils();
		System.out.println(bean.getUrl());
		fileName = getFileName(bean.getUrl());
		httpUtils.download(bean.getUrl(), Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/"
				+ fileName, new RequestCallBack<File>() {

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
				pb_downloading.setVisibility(ProgressBar.VISIBLE);
				pb_downloading.setMax((int) total);
				pb_downloading.setProgress((int) current);
			}

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				pb_downloading.setVisibility(ProgressBar.GONE);
				Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_SHORT)
						.show();
				installAPK();
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				pb_downloading.setVisibility(ProgressBar.GONE);
				Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_SHORT)
						.show();

			}
		});
	}

	private String getFileName(String url) {
		if (url != null) {
			return url.substring(url.lastIndexOf("/") + 1);
		}
		return null;
	}

	/**
	 * 安装APK
	 */
	protected void installAPK() {
		/*
		 * <intent-filter> <action android:name="android.intent.action.VIEW" />
		 * <category android:name="android.intent.category.DEFAULT" /> <data
		 * android:scheme="content" /> <data android:scheme="file" /> <data
		 * android:mimeType="application/vnd.android.package-archive" />
		 * </intent-filter>
		 */
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		Uri data = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory().getAbsolutePath(), fileName));
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(data, type);
		startActivityForResult(intent, 0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		gotoMain();
	}

	protected void gotoMain() {

		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	private void animation() {
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(3000);
		aa.setFillAfter(true);

		RotateAnimation ra = new RotateAnimation(0f, 360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(3000);
		ra.setFillAfter(true);

		ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa.setDuration(3000);
		sa.setFillAfter(true);

		AnimationSet as = new AnimationSet(true);
		as.addAnimation(aa);
		as.addAnimation(ra);
		as.addAnimation(sa);
		
		as.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {

				timeConsuming();
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				if(!SharedPreferencesUtils.getBoolean(SplashActivity.this,
						MyConstants.AUTO_UPDATE, false)){
					mHandler.obtainMessage(VERSON_OK).sendToTarget();
				}
			}
		});

		rl_root.setAnimation(as);
	}

	private void initView() {
		setContentView(R.layout.activity_splash);
		rl_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
		tv_version = (TextView) findViewById(R.id.tv_splash_version);
		pb_downloading = (ProgressBar) findViewById(R.id.pb_splash_donwloading);
	}
}
