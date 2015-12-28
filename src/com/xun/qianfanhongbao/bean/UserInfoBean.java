package com.xun.qianfanhongbao.bean;

import cn.bmob.v3.BmobObject;

public class UserInfoBean extends BmobObject {
	private static final long serialVersionUID = 5565476961202605425L;
	private String phoneIMEI; // 用户的device ID
	private String networkType;
	private String getPrice; // 用户抢到的红包

	public String getPhoneIMEI() {
		return phoneIMEI;
	}

	public void setPhoneIMEI(String phoneIMEI) {
		this.phoneIMEI = phoneIMEI;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getGetPrice() {
		return getPrice;
	}

	public void setGetPrice(String getPrice) {
		this.getPrice = getPrice;
	}

}
