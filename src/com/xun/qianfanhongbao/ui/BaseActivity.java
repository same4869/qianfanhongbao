package com.xun.qianfanhongbao.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.xun.qianfanhongbao.R;

public class BaseActivity extends Activity {
	private FrameLayout contentView;
	private TextView titleTv;
	private TextView backTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_base);
		UmengUpdateAgent.update(this);

		contentView = (FrameLayout) findViewById(R.id.base_content);
		titleTv = (TextView) findViewById(R.id.tv_above_title_teacher);
		backTv = (TextView) findViewById(R.id.imageview_above_menu_teacher);
		backTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	protected void setActionBarTitle(String string) {
		titleTv.setText(string);
	}

	protected void isShowBackBtn(boolean isShow) {
		if (isShow) {
			backTv.setVisibility(View.VISIBLE);
		} else {
			backTv.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void setContentView(int layoutResId) {
		LayoutInflater.from(this).inflate(layoutResId, contentView);
	}

	@Override
	public void setContentView(View view) {
		contentView.addView(view);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		contentView.addView(view, params);
	}
}
