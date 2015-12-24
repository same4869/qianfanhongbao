package com.xun.qianfanhongbao.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xun.qianfanhongbao.R;
import com.xun.qianfanhongbao.service.HookService;

public class HongBaoMainActivity extends Activity {

	Button qidong;
	TextView info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startService(new Intent(getApplicationContext(), HookService.class));

		qidong = (Button) findViewById(R.id.qidong);
		info = (TextView) findViewById(R.id.info);
		qidong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "请开启  疯狂抢红包BY Young", 1).show();
				startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (isAccessibilitySettingsOn(getApplicationContext())) {
			qidong.setText("已启动  疯狂抢红包BY Young");
			info.setText("已开启自动抢红包模式，您可以后台运行该APP，同时开启 微信接收新消息通知,并后台运行微信,同时屏幕保持常亮");
		} else {
			info.setText("");
			qidong.setText("点击开启   疯狂抢红包BY Young");
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

}
