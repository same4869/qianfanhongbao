package com.xun.qianfanhongbao.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.nineoldandroids.view.ViewHelper;
import com.xun.qianfanhongbao.R;

public class BouncyBtnView extends FrameLayout {

	private final SpringSystem springSystem;
	private final Spring popAnimationSpring;
	private final View layer;

	public BouncyBtnView(Context context) {
		this(context, null);
	}

	public BouncyBtnView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BouncyBtnView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		View rootView = LayoutInflater.from(context).inflate(R.layout.view_button, this);
		layer = rootView.findViewById(R.id.skin_home_btn_camera);

		springSystem = SpringSystem.create();

		popAnimationSpring = springSystem.createSpring().setSpringConfig(SpringConfig.fromBouncinessAndSpeed(5, 5)).addListener(new SimpleSpringListener() {
			@Override
			public void onSpringUpdate(Spring spring) {
				setPopAnimationProgress((float) spring.getCurrentValue());
			}
		});
	}

	public void popAnimation(boolean on) {
		popAnimationSpring.setCurrentValue(0);
		popAnimationSpring.setEndValue(on ? 1 : 0);
	}

	public void setPopAnimationProgress(float progress) {
		ViewHelper.setScaleX(layer, progress);
		ViewHelper.setScaleY(layer, progress);
		ViewHelper.setAlpha(layer, progress);
	}

	public float transition(float progress, float startValue, float endValue) {
		return (float) SpringUtil.mapValueFromRangeToRange(progress, 0, 1, startValue, endValue);
	}

}
