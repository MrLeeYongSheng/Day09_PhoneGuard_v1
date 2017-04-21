package com.itheima.day09_phoneguard_v1.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.day09_phoneguard_v1.R;

public class ItemSettingCenterView extends LinearLayout {

	private TextView tv_content;
	private TextView tv_title;
	private CheckBox cb_select;
	private String title;
	private String content;
	private RelativeLayout rl_root;
	private String[] contentValues;
	private boolean isSelected = false;


	public ItemSettingCenterView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}


	public ItemSettingCenterView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ItemSettingCenterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itheima.day09_phoneguard_v1",
				"title");
		content = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itheima.day09_phoneguard_v1",
				"content");
		
		initView();
		initData();
		initEvent();
	}


	/**
	 * @param listener
	 * 设置点击事件的监听器
	 */
	public void setOnItemClickListener(OnClickListener listener) {
		rl_root.setOnClickListener(listener);
	}
	
	/**
	 * @param checked
	 * 设置点击的激活状态
	 */
	public void setChecked(boolean checked) {
		cb_select.setChecked(checked);
	}
	
	/**
	 * @return
	 * 获取是否点击的状态
	 */
	public boolean isChecked() {
		return cb_select.isChecked();
	}
	
	private void initEvent() {
		rl_root.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isSelected = !isSelected;
				cb_select.setChecked(isSelected);
			}
		});
		
		cb_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked) {
					tv_content.setText(contentValues[1]);
					tv_content.setTextColor(Color.GREEN);
				} else {
					tv_content.setText(contentValues[0]);
					tv_content.setTextColor(Color.RED);
				}
			}
		});
	}


	private void initData() {
		
		tv_title.setText(title);
		contentValues = content.split("-");
		if(!cb_select.isChecked()) {
			tv_content.setText(contentValues[0]);
			tv_content.setTextColor(Color.RED);
		}
	}


	private void initView() {
		View view = View.inflate(getContext(), R.layout.item_settingcenter, null);
		tv_title = (TextView) view.findViewById(R.id.tv_item_settingcenter_title);
		tv_content = (TextView) view.findViewById(R.id.tv_item_settingcenter_content);
		cb_select = (CheckBox) view.findViewById(R.id.cb_item_settingcenter_select);
		rl_root = (RelativeLayout) view.findViewById(R.id.rl_item_settingcenter_root);
		addView(view);
		
	}
}
