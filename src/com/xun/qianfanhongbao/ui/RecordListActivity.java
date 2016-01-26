package com.xun.qianfanhongbao.ui;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.xun.qianfanhongbao.R;
import com.xun.qianfanhongbao.bean.UserInfoBean;
import com.xun.qianfanhongbao.util.PhoneUtil;

public class RecordListActivity extends BaseActivity {
	private ListView recordListView;
	private RecordListAdapter recordListAdapter;

	private List<UserInfoBean> userInfoBeans;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_list);

		initView();
		initData(PhoneUtil.getPhoneIMEI(getApplicationContext()));
	}

	private void initView() {
		recordListView = (ListView) findViewById(R.id.record_list);
		recordListAdapter = new RecordListAdapter();
		recordListView.setAdapter(recordListAdapter);
	}

	int page = 0;
	Double sum = 0.0;

	private void initData(String phoneIMEI) {
		BmobQuery<UserInfoBean> query = new BmobQuery<UserInfoBean>();
		query.addWhereEqualTo("phoneIMEI", phoneIMEI);
		// 返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(50);
		// query.setSkip(1000 * page);
		// 执行查询方法
		query.findObjects(this, new FindListener<UserInfoBean>() {
			@Override
			public void onSuccess(List<UserInfoBean> object) {
				userInfoBeans = object;
				recordListAdapter.setData(userInfoBeans);

				// Log.d("kkkkkkkk", "onSuccess userInfoBeans.size() --> " + userInfoBeans.size());
				// if (userInfoBeans.size() < 1000) {
				// for (int i = 0; i < userInfoBeans.size(); i++) {
				// Double price = Double.parseDouble(userInfoBeans.get(i).getGetPrice());
				// sum = sum + price;
				// }
				// Log.d("kkkkkkkk", "sum --> " + sum);
				// } else {
				// Log.d("kkkkkkkk", "page --> " + page);
				// for (int i = 0; i < userInfoBeans.size(); i++) {
				// Double price = Double.parseDouble(userInfoBeans.get(i).getGetPrice());
				// sum = sum + price;
				// }
				// page++;
				// initData(PhoneUtil.getPhoneIMEI(getApplicationContext()));
				// }
			}

			@Override
			public void onError(int code, String msg) {
				// Log.d("kkkkkkkk", "msg ---> " + msg);
			}
		});
	}

	public class RecordListAdapter extends BaseAdapter {
		private List<UserInfoBean> datas;

		public void setData(List<UserInfoBean> datas) {
			this.datas = datas;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			if (datas == null) {
				return 0;
			} else {
				return datas.size();
			}
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_record_item, null);
				viewHolder.price = (TextView) convertView.findViewById(R.id.item_price);
				viewHolder.time = (TextView) convertView.findViewById(R.id.item_time);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.price.setText(datas.get(position).getGetPrice() + "元");
			viewHolder.time.setText(datas.get(position).getUpdatedAt());

			return convertView;
		}

	}

	public static class ViewHolder {
		public TextView price;
		public TextView time;
	}
}
