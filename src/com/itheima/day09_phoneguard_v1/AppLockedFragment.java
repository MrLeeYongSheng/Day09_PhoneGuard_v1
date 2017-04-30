package com.itheima.day09_phoneguard_v1;

import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.day09_phoneguard_v1.dao.LockedDao;

public class AppLockedFragment extends LockBaseFragment {

	@Override
	protected void setImageResourceOrEvent(ImageView iv_lock,
			final LockedDao ldao, final String packName, final View view) {
		iv_lock.setImageResource(R.drawable.selector_applocked_unlocked);
		iv_lock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ldao.remove(packName);
				TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, 0f);
				ta.setDuration(300L);
				view.setAnimation(ta);
				new Thread() {
					public void run() {

						SystemClock.sleep(300L);
						initData();

					};
				}.start();
			}
		});
	}

	@Override
	protected void setTopText(TextView tv_left_count, int totalSize) {
		tv_left_count.setText("¼ÓËøÈí¼þ(" + totalSize + ")");
	}

	@Override
	protected boolean isContainsLock(String packName) {
		return getAllLockedList().contains(packName);
	}
}
