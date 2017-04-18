package com.itheima.day09_phoneguard_v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSetupActivity extends Activity {

	private GestureDetector gd;

	public abstract void preActivity();

	public abstract void nextActivity();

	public abstract void initView();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initEvent();
		initData();
		listenGesture();
	}

	public void initData() {}

	public void initEvent() {}

	private void listenGesture() {

		gd = new GestureDetector(BaseSetupActivity.this, new OnGestureListener() {
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				if(Math.abs(velocityX) > 100f) {
					float distant = e2.getX() - e1.getX();
					float absDistant = Math.abs(distant);
					if(absDistant > 80f) {
						if(distant > 0) {
							preview(null);
						} else{
							next(null);
						}
						return true;
					}
				} 
				return false;
			}
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			
			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gd.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	public void next(View v) {
		nextActivity();
		nextAnimation();
	}

	public void preview(View v) {
		preActivity();
		preAnimation();
	}

	private void nextAnimation() {

		overridePendingTransition(R.anim.next_in, R.anim.next_out);
	}

	private void preAnimation() {

		overridePendingTransition(R.anim.preview_in, R.anim.preview_out);
	}

	public void startActivity(Class type) {
		Intent intent = new Intent(BaseSetupActivity.this, type);
		startActivity(intent);
		finish();
	}
}
