package com.xun.qianfanhongbao.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xun.qianfanhongbao.R;

public class QianfanSoftActivity extends BaseActivity implements OnClickListener {
	private TextView downGuanfang, downWandoujia, down360;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qianfansoft);

		initView();
	}

	private void initView() {
		downGuanfang = (TextView) findViewById(R.id.download_guanwang);
		downGuanfang.setOnClickListener(this);
		downWandoujia = (TextView) findViewById(R.id.download_wandoujia);
		downWandoujia.setOnClickListener(this);
		down360 = (TextView) findViewById(R.id.download_360);
		down360.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.download_guanwang:
			Intent itguanfang = new Intent(Intent.ACTION_VIEW, Uri.parse("http://qianfanzhiche.bmob.cn/"));
			startActivity(itguanfang);
			break;
		case R.id.download_wandoujia:
			Intent itwandoujia = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.wandoujia.com/apps/com.xun.qianfanzhiche"));
			startActivity(itwandoujia);
			break;
		case R.id.download_360:
			Intent it360 = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://zhushou.360.cn/detail/index/soft_id/3172995?recrefer=SE_D_%E5%8D%83%E5%B8%86%E7%9F%A5%E8%BD%A6"));
			startActivity(it360);
			break;
		default:
			break;
		}
	}
}
