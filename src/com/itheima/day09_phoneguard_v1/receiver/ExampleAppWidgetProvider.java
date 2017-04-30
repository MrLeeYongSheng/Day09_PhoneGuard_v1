package com.itheima.day09_phoneguard_v1.receiver;

import com.itheima.day09_phoneguard_v1.service.AppWidgetService;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class ExampleAppWidgetProvider extends AppWidgetProvider {

	

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Intent intent = new Intent(context, AppWidgetService.class);
		context.startService(intent);

	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		Intent intent = new Intent(context, AppWidgetService.class);
		context.stopService(intent);
	}

}
