package com.xun.qianfanhongbao.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.xun.qianfanhongbao.R;

public class CourseActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);

		initView();
	}

	private void initView() {
		setActionBarTitle("操作教程");
	}

	@Override
	public void onClick(View v) {

	}
}
