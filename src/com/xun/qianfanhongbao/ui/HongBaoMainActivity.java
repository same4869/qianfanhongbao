package com.xun.qianfanhongbao.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xun.qianfanhongbao.R;
import com.xun.qianfanhongbao.service.HookService;
import com.xun.qianfanhongbao.view.BouncyBtnView;

public class HongBaoMainActivity extends BaseActivity implements OnClickListener {
	private BouncyBtnView bouncyBtnView;
	private TextView tipsTv;
	private TextView courseTv, aboutTv, qianfanSoftTv, payTv;

	private Boolean is2CallBack = false;// 是否双击退出

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startService(new Intent(getApplicationContext(), HookService.class));

		initView();
	}

	private void initView() {
		isShowBackBtn(false);
		setActionBarTitle("千帆抢红包");
		bouncyBtnView = (BouncyBtnView) findViewById(R.id.start_btn);
		bouncyBtnView.setOnClickListener(this);
		bouncyBtnView.setVisibility(View.INVISIBLE);

		tipsTv = (TextView) findViewById(R.id.tips);
		courseTv = (TextView) findViewById(R.id.sub_btn_1);
		courseTv.setOnClickListener(this);
		aboutTv = (TextView) findViewById(R.id.sub_btn_4);
		aboutTv.setOnClickListener(this);
		qianfanSoftTv = (TextView) findViewById(R.id.sub_btn_3);
		qianfanSoftTv.setOnClickListener(this);
		payTv = (TextView) findViewById(R.id.sub_btn_2);
		payTv.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isAccessibilitySettingsOn(getApplicationContext())) {
			tipsTv.setVisibility(View.VISIBLE);
			bouncyBtnView.setVisibility(View.INVISIBLE);
		} else {
			tipsTv.setVisibility(View.INVISIBLE);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					bouncyBtnView.setVisibility(View.VISIBLE);
					bouncyBtnView.popAnimation(true);
				}
			}, 300);
		}
	}

	// To check if service is enabled
	public boolean isAccessibilitySettingsOn(Context context) {
		int accessibilityEnabled = 0;
		final String service = context.getPackageName() + "/" + HookService.class.getName();
		boolean accessibilityFound = false;
		try {
			accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),
					android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
		if (accessibilityEnabled == 1) {
			String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
					Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
			if (settingValue != null) {
				TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
				splitter.setString(settingValue);
				while (splitter.hasNext()) {
					String accessabilityService = splitter.next();
					if (accessabilityService.equalsIgnoreCase(service)) {
						return true;
					}
				}
			}
		} else {
			// Log.v(TAG, "***ACCESSIBILIY IS DISABLED***");
		}

		return accessibilityFound;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_btn:
			startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
			break;
		case R.id.sub_btn_1:
			Intent courseIntent = new Intent(HongBaoMainActivity.this, CourseActivity.class);
			startActivity(courseIntent);
			break;
		case R.id.sub_btn_2:
			Toast.makeText(getApplicationContext(), "你(gai)的(gong)人(neng)品(zan)突(wei)然(kai)涨(fang)了10000", Toast.LENGTH_SHORT).show();
			break;
		case R.id.sub_btn_3:
			Intent qianfanIntent = new Intent(HongBaoMainActivity.this, QianfanSoftActivity.class);
			startActivity(qianfanIntent);
			break;
		case R.id.sub_btn_4:
			Intent aboutIntent = new Intent(HongBaoMainActivity.this, AboutActivity.class);
			startActivity(aboutIntent);
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!is2CallBack) {
				is2CallBack = true;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						is2CallBack = false;
					}
				}, 2500);

			} else {
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}
		return true;
	}
}
