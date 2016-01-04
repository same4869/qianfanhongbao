package com.xun.qianfanhongbao.service;

import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.PowerManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import cn.bmob.v3.listener.SaveListener;

import com.xun.qianfanhongbao.bean.UserInfoBean;
import com.xun.qianfanhongbao.util.PhoneUtil;

@SuppressLint("NewApi")
public class HookService extends AccessibilityService {
	private boolean isNew; // 是否是新红包，避免重复统计

	/**
	 * 当通知栏改变或界面改变时会触发该方法
	 */
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		if (event == null)
			return;

		if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
			unlockScreen();

			List<CharSequence> texts = event.getText();

			for (CharSequence t : texts) {

				if (t.toString().contains("[微信红包]")) {// 获取通知栏字符，若包含 [微信红包]
														// 则模拟手指点击事件
					handleNotificationChange(event);
					break;
				}
			}

		}

		/**
		 * 下面内容可以借助 Dump View Hierarchy For UI Automator 去分析微信UI结构
		 */
		if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

			// System.out.println("TYPE_WINDOW_STATE_CHANGED --> "+event.getClassName());
			if ("com.tencent.mm.ui.LauncherUI".equals(event.getClassName())) {
				// 在聊天界面,去点中红包
				checkKey2();
			} else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI".equals(event.getClassName())) {
				// 点中了红包，下一步就是去拆红包
				checkKey1();
			} else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(event.getClassName())) {
				// 拆完红包后看详细的纪录界面
				checkKey1();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

				// ((Vibrator)getSystemService(Context.VIBRATOR_SERVICE)).vibrate(500);
			}

		}
	}

	private void checkKey1() {
		AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
		if (nodeInfo == null) {
			return;
		}
		List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("拆红包"); // 获取包含 拆红包
																								// 文字的控件，模拟点击事件，拆开红包
		for (AccessibilityNodeInfo n : list) {
			n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			isNew = true;
		}

		List<AccessibilityNodeInfo> list2 = nodeInfo.findAccessibilityNodeInfosByText("元");
		for (int i = list2.size() - 1; i >= 0; i--) {
			AccessibilityNodeInfo parent = list2.get(i).getParent();
			for (int j = 0; j < parent.getChildCount(); j++) {
				if (parent.getChild(j) != null && parent.getChild(j).getText() != null) {
					try {
						Double blance = Double.parseDouble(parent.getChild(j).getText().toString());
						if (isNew) {
							storageInfoInLocalAndRemote(String.valueOf(blance));
						}
						isNew = false;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	// 将抢红包的信息存下来
	private void storageInfoInLocalAndRemote(String price) {
		UserInfoBean userInfoBean = new UserInfoBean();
		userInfoBean.setPhoneIMEI(PhoneUtil.getPhoneIMEI(getApplicationContext()));
		userInfoBean.setNetworkType(PhoneUtil.getPhoneNetworkType(getApplicationContext()));
		userInfoBean.setGetPrice(price);
		userInfoBean.save(getApplicationContext(), new SaveListener() {

			@Override
			public void onSuccess() {
			}

			@Override
			public void onFailure(int code, String arg0) {
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void checkKey2() {
		AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
		if (nodeInfo == null) {

			return;
		}
		List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("领取红包"); // 找到聊天界面中包含 领取红包
																								// 字符的控件
		if (list.isEmpty()) {
			list = nodeInfo.findAccessibilityNodeInfosByText("[微信红包]");
			for (AccessibilityNodeInfo n : list) {

				n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				break;
			}
		} else {
			// 最新的红包领起
			for (int i = list.size() - 1; i >= 0; i--) {
				AccessibilityNodeInfo parent = list.get(i);

				try {
					// 调用performAction(AccessibilityNodeInfo.ACTION_CLICK)触发点击事件
					parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
					Rect rect = new Rect();
					parent.getBoundsInScreen(rect);
					while (parent.getParent() != null) {
						parent = parent.getParent();
						parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
					}

					return;
				} catch (Exception e) {

					e.printStackTrace();
				}

			}
		}

	}

	private void unlockScreen() {
		KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		final KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("MyKeyguardLock");
		keyguardLock.disableKeyguard();

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
				"MyWakeLock");

		wakeLock.acquire();
	}

	public void handleNotificationChange(AccessibilityEvent event) {
		try {

			Notification notification = (Notification) event.getParcelableData();

			PendingIntent pendingIntent = notification.contentIntent;

			pendingIntent.send(); // 点击通知栏消息

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, START_STICKY, startId);
	}
}
