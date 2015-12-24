package com.xun.qianfanhongbao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xun.qianfanhongbao.service.HookService;

/**
 * 监听解锁等事件，启动服务
 */
public class Receiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context, HookService.class));
	}

}
