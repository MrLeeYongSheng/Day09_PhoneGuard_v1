package com.itheima.day09_phoneguard_v1.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.itheima.day09_phoneguard_v1.domain.SmsesJsonBean;
import com.itheima.day09_phoneguard_v1.utils.GsonUtils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class SmsEngine {

	public interface BlackupListener {
		public void setMax(int max);

		public void setProgress(int value);

		public void show();

		public void dismiss();
	}

	private static class Data {
		int progress;
	}

	protected static final int SHOW = 1;
	protected static final int SETMAX = 2;
	protected static final int SETPROGRESS = 3;
	protected static final int DISMISS = 4;
	protected static final int FILENOTFOUND = 5;

	/**
	 * @param context
	 *            显示主界面的活动窗口
	 * @param blistener
	 *            用来执行进度的回调显示
	 */
	public static void blackup(final Context context, final BlackupListener bl) {

		final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SHOW:
					bl.show();
					break;

				case SETMAX:
					bl.setMax(msg.arg1);
					break;

				case SETPROGRESS:
					bl.setProgress(msg.arg1);
					break;
					
				case DISMISS:
					bl.dismiss();
					break;	

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};

		new Thread() {
			private Message msg;

			public void run() {
				ContentResolver resolver = context.getContentResolver();
				final Cursor cursor = resolver.query(
						Uri.parse("content://sms"), new String[] { "address",
								"date", "type", "body" }, null, null, null);

				final Data data = new Data();
				data.progress = 0;
				File file = new File(Environment.getExternalStorageDirectory(),
						"sms.json");
				try {
					FileOutputStream fos = new FileOutputStream(file);
					PrintWriter pw = new PrintWriter(fos);
					// show
					mHandler.obtainMessage(SHOW).sendToTarget();
					msg = Message.obtain();
					msg.what = SETMAX;
					msg.arg1 = cursor.getCount();
					mHandler.sendMessage(msg);
					pw.println("{\"smses\":[");
					while (cursor.moveToNext()) {
						data.progress++;
						pw.println("{");
						pw.println("\"address\":\"" + cursor.getString(0)
								+ "\",");// 电话号码
						pw.println("\"date\":\"" + cursor.getString(1) + "\",");// 时间
						pw.println("\"type\":\"" + cursor.getString(2) + "\",");// 类型
						pw.println("\"body\":\"" + cursor.getString(3) + "\"}");// 内容
						if (cursor.getPosition() != cursor.getCount() - 1) {
							pw.print(",");
						}

						// setProgress
						msg = Message.obtain();
						msg.what = SETPROGRESS;
						msg.arg1 = data.progress;
						mHandler.sendMessage(msg);
					}

					pw.println("]}");// 结束
					pw.close();
					cursor.close();
					// Desmiss
					msg = Message.obtain();
					msg.what = DISMISS;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	public static void getBlackup(final Context context, final BlackupListener bl) {

		final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SHOW:
					bl.show();
					break;

				case SETMAX:
					bl.setMax(msg.arg1);
					break;

				case SETPROGRESS:
					bl.setProgress(msg.arg1);
					break;
					
				case DISMISS:
					bl.dismiss();
					break;
					
				case FILENOTFOUND:
					Toast.makeText(context, "备份文件找不到", Toast.LENGTH_SHORT).show();
					break;	

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		
		new Thread(){
			public void run() {
				
				File file = new File(Environment.getExternalStorageDirectory(), "sms.json");
				if(!file.exists()) {
					mHandler.obtainMessage(FILENOTFOUND).sendToTarget();
				}
				try {
					SmsesJsonBean jsonBean = GsonUtils.getRootJsonParserObj(SmsesJsonBean.class, file);
					if(jsonBean!=null) {
						mHandler.obtainMessage(SETMAX, jsonBean.smses.size(), -1).sendToTarget();
						mHandler.obtainMessage(SHOW).sendToTarget();
						ContentResolver resolver = context.getContentResolver();
						for(int i=0; i<jsonBean.smses.size(); i++) {
							ContentValues values = new ContentValues();
							values.put("address", jsonBean.smses.get(i).address);
							values.put("body", jsonBean.smses.get(i).body);
							values.put("date", jsonBean.smses.get(i).date);
							values.put("type", jsonBean.smses.get(i).type);
							resolver.insert(Uri.parse("content://sms"), values );
							mHandler.obtainMessage(SETPROGRESS, i+1, -1).sendToTarget();
						}
						mHandler.obtainMessage(DISMISS).sendToTarget();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
}
