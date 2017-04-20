package com.itheima.day09_phoneguard_v1;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.day09_phoneguard_v1.domain.ContactBean;
import com.itheima.day09_phoneguard_v1.engine.ReadContactsEngine;
import com.itheima.day09_phoneguard_v1.utils.MyConstants;

public class ContactsActivity extends ListActivity {

	protected static final int LOADING = 0;
	protected static final int FINISH = 1;
	private List<ContactBean> contacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
		initData();
		initEvent();
	}

	private void initEvent() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent();
				intent.putExtra(MyConstants.SAFE_NUMBER, contacts.get(position).getPhone());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private Handler mHandler = new Handler(){
		private ProgressDialog pd;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LOADING:
				pd = new ProgressDialog(ContactsActivity.this);
				pd.setTitle("注意");
				pd.setMessage("正在玩命加载中...");
				pd.show();
				break;
 
			case FINISH:
				if(pd != null) {
					pd.dismiss();
					pd = null;
				}
				adapter.notifyDataSetChanged();
				break;
				
			default:
				break;
			}
		}
	};
	private MyAdapter adapter;
	private ListView listView;
	
	private void initData() {
		new Thread(){


			public void run() {
				contacts = ReadContactsEngine.getContacts(ContactsActivity.this);
				Message msg = Message.obtain();
				msg.what = FINISH;
				mHandler.sendMessage(msg);
			};
		}.start();
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if(contacts == null) {
				return 0;
			}
			return contacts.size();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ContactBean contactBean = contacts.get(position);
			View view;
			if(convertView == null) {
				view = View.inflate(ContactsActivity.this, R.layout.activity_contacts, null);
			} else {
				view = convertView;
			}
			TextView tv_name = (TextView) view.findViewById(R.id.tv_contacts_name);
			TextView tv_phone = (TextView) view.findViewById(R.id.tv_contacts_phone);
			tv_name.setText(contactBean.getName());
			tv_phone.setText(contactBean.getPhone());
			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	private void initView() {
		listView = getListView();
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		Message msg = Message.obtain();
		msg.what = LOADING;
		mHandler.sendMessage(msg);
		
	}
}
