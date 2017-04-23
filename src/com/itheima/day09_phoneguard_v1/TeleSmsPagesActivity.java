package com.itheima.day09_phoneguard_v1;

import java.util.ArrayList;
import java.util.List;

import com.itheima.day09_phoneguard_v1.dao.BlacklistDao;
import com.itheima.day09_phoneguard_v1.db.BlacklistTable;
import com.itheima.day09_phoneguard_v1.domain.BlackBean;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TeleSmsPagesActivity extends Activity {

	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	private ListView lv_blacklist;
	private Button btn_add;
	private TextView tv_nodata;
	private ProgressBar pb_loading;
	private List<BlackBean> blacklist = new ArrayList<BlackBean>();//黑名单的数据列表
	private MyAdapter mAdapter; //listView 的适配器
	private ListViewItem lvi;	//用于优化ListView的缓存显示
	private int currentPage = 1;//初始化当前页为1
	private final int count = 10; //设定每页有十条数据 
	private int totalPages;
	private TextView tv_pages_currentpage; //获取跳转的页数
	private EditText et_pages_j2p; //显示当前页

	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
		initData();
	}

	public void preview(View v) {
		currentPage--;
		if(currentPage == 0) {
			currentPage = totalPages;
		}
		initData();
	}
	
	public void next(View v) {
		currentPage++;
		currentPage = currentPage % totalPages;
		if(currentPage == 0) {
			currentPage = totalPages;
		}
		initData();
	}
	
	public void jump(View v) {
		String currentPageStr = et_pages_j2p.getText().toString().trim();
		if(TextUtils.isEmpty(currentPageStr)) {
			Toast.makeText(TeleSmsPagesActivity.this, "跳转页不能为空", Toast.LENGTH_SHORT).show();
			return ;
		}
		int parseInt = Integer.parseInt(currentPageStr);
		if(parseInt<=0 || parseInt>totalPages) {
			Toast.makeText(TeleSmsPagesActivity.this, "数据无效!", Toast.LENGTH_SHORT).show();
			return ;
		}
		currentPage = parseInt;
		initData();
	}
	
	private void initData() {

		//耗时的动作在该方法里执行,如下
		timeConsuming();	
	}

	/**
	 * 耗时的事情在这里操作
	 * 		获取数据
	 */
	private void timeConsuming() {

		new Thread(){
			private BlacklistDao dao;

			public void run() {
				mHandler.obtainMessage(LOADING).sendToTarget();
				dao = new BlacklistDao(TeleSmsPagesActivity.this);
				//获取黑名单数据
//				blacklist = dao.getAllDatas();
				blacklist = dao.getCurrentPageDatas(currentPage, count);  //用来测试分页查询的查询某一页的数据
				totalPages = dao.getTotalPages(count); //获取总页数
				mHandler.obtainMessage(FINISH).sendToTarget();
			};
		}.start();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING:
				//正在加载数据
				//显示progressBar
				pb_loading.setVisibility(View.VISIBLE);
				//隐藏显示数据的ListView 
				lv_blacklist.setVisibility(View.GONE);
				//隐藏显示没数据的TextView 
				tv_nodata.setVisibility(View.GONE);
				break;
				
			case FINISH://加载数据完成后
				
				//有数据
				
				if (blacklist!=null && blacklist.size()!=0) {
					//隐藏progressBar
					pb_loading.setVisibility(View.GONE);
					//显示 显示数据的ListView
					lv_blacklist.setVisibility(View.VISIBLE);
					//隐藏显示没数据的TextView 
					tv_nodata.setVisibility(View.GONE);
					//通知更新数据的显示.
					mAdapter.notifyDataSetChanged();
					//显示当前页
					tv_pages_currentpage.setText(currentPage+"/"+totalPages);
				} else {
					//没数据
					//隐藏progressBar
					pb_loading.setVisibility(View.GONE);
					//隐藏显示数据的ListView
					lv_blacklist.setVisibility(View.GONE);
					//隐藏显示没数据的TextView 
					tv_nodata.setVisibility(View.VISIBLE);
				}
				break;

			default:
				break;
			}
		};
	};
	
	private class ListViewItem {
		TextView tv_item_phone;
		TextView tv_item_mode;
		ImageView iv_item_delete;
	}
	
	private class MyAdapter extends BaseAdapter {


		@Override
		public int getCount() {
			return blacklist.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = View.inflate(TeleSmsPagesActivity.this, R.layout.item_telesms_blacklist, null);
				lvi = new ListViewItem();
				lvi.tv_item_phone = (TextView) convertView.findViewById(R.id.tv_item_telesms_phone);
				lvi.tv_item_mode = (TextView) convertView.findViewById(R.id.tv_item_telesms_mode);
				lvi.iv_item_delete = (ImageView) convertView.findViewById(R.id.iv_item_btn_telesms_delete);
				convertView.setTag(lvi);
			} else {
				lvi = (ListViewItem) convertView.getTag();
			}
			BlackBean blackBean = blacklist.get(position);
			lvi.tv_item_phone.setText(blackBean.getPhone());
			int mode = blackBean.getMode();
			if(BlacklistTable.SMS == mode) {
				lvi.tv_item_mode.setText("拦截短信");
			} else if(BlacklistTable.TEL == mode){
				lvi.tv_item_mode.setText("拦截电话");
			} else if(BlacklistTable.ALL == mode){
				lvi.tv_item_mode.setText("全部拦截");
			}
			return convertView;
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

		setContentView(R.layout.activity_telesms_pages); //分页查看的界面
//		setContentView(R.layout.activity_telesms); //非分页查看的界面
		//增加黑名单按钮
		btn_add = (Button) findViewById(R.id.btn_telesms_add);
		//显示数据的listView
		lv_blacklist = (ListView) findViewById(R.id.lv_telesms_blacklist);
		//显示没有数据的TextView
		tv_nodata = (TextView) findViewById(R.id.tv_telesms_nodata);
		//显示加载数据的Progressbar
		pb_loading = (ProgressBar) findViewById(R.id.pb_telesms_loading);
		mAdapter = new MyAdapter();
		lv_blacklist.setAdapter(mAdapter);
		
		//显示当前页
		tv_pages_currentpage = (TextView) findViewById(R.id.tv_telesms_pages_currentpage);
		//跳转页的EditText
		et_pages_j2p = (EditText) findViewById(R.id.et_telesms_pages_j2p);
	}
}
