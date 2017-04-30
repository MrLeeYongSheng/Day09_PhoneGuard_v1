package com.itheima.day09_phoneguard_v1.service;

import java.util.Timer;
import java.util.TimerTask;

import com.itheima.day09_phoneguard_v1.R;
import com.itheima.day09_phoneguard_v1.engine.ProcessEngine;
import com.itheima.day09_phoneguard_v1.receiver.ExampleAppWidgetProvider;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

public class AppWidgetService extends Service {

	private ActivityManager am;
	private Timer timer;
	private TimerTask task;
	private AppWidgetManager awm;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		
		awm = AppWidgetManager.getInstance(AppWidgetService.this);
		
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
			
				ComponentName provider = new ComponentName(AppWidgetService.this, ExampleAppWidgetProvider.class);
				RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
				int proc_count = ProcessEngine.getAllProcInfo(AppWidgetService.this).size();
				//显示正在运行进程
				views.setTextViewText(R.id.process_count, "正在运行进程:"+proc_count );
				//显示可用内存
				long freeSize = ProcessEngine.getFreeMemory(AppWidgetService.this);
				views.setTextViewText(R.id.process_memory, "可用内存:"+Formatter.formatFileSize(AppWidgetService.this, freeSize ));
				//设置监听点击事件
				Intent intent = new Intent();
				intent.setAction("com.itheima.appwidget.clear");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(AppWidgetService.this, 0, intent, 0);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				awm.updateAppWidget(provider, views );
			}
		};
		timer.schedule(task , 0, 5000);
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task!=null) {
			task.cancel();
			task = null;
		}
		if(timer!=null) {
			timer.cancel();
			timer = null;
		}
	}
}
