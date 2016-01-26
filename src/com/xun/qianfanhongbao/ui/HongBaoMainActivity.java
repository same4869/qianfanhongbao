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
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.xun.qianfanhongbao.R;
import com.xun.qianfanhongbao.bean.UserInfoBean;
import com.xun.qianfanhongbao.service.HookService;
import com.xun.qianfanhongbao.view.BouncyBtnView;

public class HongBaoMainActivity extends BaseActivity implements OnClickListener {
	private BouncyBtnView bouncyBtnView;
	private TextView tipsTv;
	private TextView courseTv, aboutTv, qianfanSoftTv, payTv;
	private TextView recordTv, recordCountTv;

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
		recordTv = (TextView) findViewById(R.id.record_tv);
		recordTv.setOnClickListener(this);
		recordCountTv = (TextView) findViewById(R.id.record_count_tv);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getHongBaoCount();
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
			startShare();
			// Toast.makeText(getApplicationContext(), "你(gai)的(gong)人(neng)品(zan)突(wei)然(kai)涨(fang)了10000", Toast.LENGTH_SHORT).show();
			break;
		case R.id.sub_btn_3:
			Intent qianfanIntent = new Intent(HongBaoMainActivity.this, QianfanSoftActivity.class);
			startActivity(qianfanIntent);
			break;
		case R.id.sub_btn_4:
			Intent aboutIntent = new Intent(HongBaoMainActivity.this, AboutActivity.class);
			startActivity(aboutIntent);
			break;
		case R.id.record_tv:
			Intent recordIntent = new Intent(HongBaoMainActivity.this, RecordListActivity.class);
			startActivity(recordIntent);
			break;
		default:
			break;
		}

	}

	private void startShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.app_name));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://www.wandoujia.com/apps/com.xun.qianfanhongbao");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("无人值守无广告,能让你人品爆表的抢红包辅助软件");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImageUrl("http://img.wdjimg.com/mms/icon/v1/7/e5/34a4fbce173e5e95db745441687e5e57_256_256.png");
		// oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://www.wandoujia.com/apps/com.xun.qianfanhongbao");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("千帆抢红包就是好");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://www.wandoujia.com/apps/com.xun.qianfanhongbao");

		// 启动分享GUI
		oks.show(this);
	}

	private void getHongBaoCount() {
		BmobQuery<UserInfoBean> query = new BmobQuery<UserInfoBean>();
		query.count(this, UserInfoBean.class, new CountListener() {
			@Override
			public void onSuccess(int count) {
				recordCountTv.setText("已为大家抢到\n" + count + "个红包");
			}

			@Override
			public void onFailure(int code, String msg) {
			}
		});
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
