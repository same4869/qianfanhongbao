package com.xun.qianfanhongbao.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class PhoneUtil {

	public static String getPhoneIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	// 获得网络类型
	public static String getPhoneNetworkType(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conMan != null) {
			NetworkInfo info = conMan.getActiveNetworkInfo();
			if (info != null) {
				switch (info.getType()) {
				case ConnectivityManager.TYPE_WIFI:
					return "WIFI";
				case ConnectivityManager.TYPE_MOBILE:
					return "MOBILE";
				}
			}
		}
		return "UNKNOW";
	}
}
