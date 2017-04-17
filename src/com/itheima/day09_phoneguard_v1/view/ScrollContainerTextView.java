package com.itheima.day09_phoneguard_v1.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

public class ScrollContainerTextView extends TextView {

	public ScrollContainerTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	

	public ScrollContainerTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public ScrollContainerTextView(Context context) {
		super(context);
	}

	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return true;
	}
}
