package com.intravita.android.rss.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class BebasneueTextView extends TextView {
private static Typeface mTypeface;
	
	public BebasneueTextView(Context context) {
		super(context);
		init();
	}
	
	public BebasneueTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public BebasneueTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		init();
	}
	
	private Typeface getCustomTypeface() {
		if (mTypeface == null)
			mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/bebasneue.ttf");
		return mTypeface;
	}
	
	private void init() {
		setTypeface(getCustomTypeface());
	}
}
