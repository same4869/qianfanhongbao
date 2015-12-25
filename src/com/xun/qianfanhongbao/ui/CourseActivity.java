package com.xun.qianfanhongbao.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xun.qianfanhongbao.R;

public class CourseActivity extends BaseActivity implements OnClickListener {
	private TextView backTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);

		initView();
	}

	private void initView() {
		backTv = (TextView) findViewById(R.id.imageview_above_menu_teacher);
		backTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_above_menu_teacher:
			finish();
			break;

		default:
			break;
		}

	}
}
