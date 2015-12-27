package com.xun.qianfanhongbao.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.xun.qianfanhongbao.R;

public class AboutActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		initView();
	}

	private void initView() {
		setActionBarTitle("关于本软件");
	}

	@Override
	public void onClick(View v) {

	}
}
