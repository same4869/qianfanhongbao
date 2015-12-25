package com.xun.qianfanhongbao.ui;

import android.app.Activity;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UmengUpdateAgent.update(this);
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
}
