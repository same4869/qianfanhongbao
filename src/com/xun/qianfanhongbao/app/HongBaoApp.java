package com.xun.qianfanhongbao.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import cn.bmob.v3.Bmob;

import com.xun.qianfanhongbao.common.Constant;

public class HongBaoApp extends Application {
	private static HongBaoApp mApplication = null;

	public static HongBaoApp getInstance() {
		return mApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (!checkSignature()) {
			System.exit(-1);
			return;
		}
		Log.d("kkkkkkkk", "dev1");
		Bmob.initialize(getApplicationContext(), Constant.BMOB_APP_ID);
		mApplication = this;
		// setConstants();
		// getConstants();
	}

	@SuppressLint("DefaultLocale")
	private boolean checkSignature() {
		byte[] sig = a.getSign(this);
		Log.d("kkkkkkkk", "dev1 2");
		String hash = a.digest(sig, "MD5").toUpperCase();

		if (hash.equals("AAA224F4C8A3567941A6F4ACAE0B2C93") || hash.equals("03A80264EC83F16F6AB52461C83B1AA7")
				|| hash.equals("8BAEDA5F5CE0C32E67E6166AE61CA3F0")) {
			return true;
		}

		return false;
	}
}
