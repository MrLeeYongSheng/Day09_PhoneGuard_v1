package com.itheima.day09_phoneguard_v1;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.day09_phoneguard_v1.dao.BlacklistDao;
import com.itheima.day09_phoneguard_v1.db.BlacklistTable;
import com.itheima.day09_phoneguard_v1.domain.BlackBean;
import com.itheima.day09_phoneguard_v1.utils.MyConstants;

public class TeleSmsActivity extends Activity {

	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	private ListView lv_blacklist;
	private Button btn_add;
	private TextView tv_nodata;
	private ProgressBar pb_loading;
	private List<BlackBean> blacklist = new ArrayList<BlackBean>();//黑名单的数据列表
	private MyAdapter mAdapter; //listView 的适配器
	private ListViewItem lvi;	//用于优化ListView的缓存显示
	private int totalCounts; //数据库数据总数
	
	private List<BlackBean> moreDatas; //存储分批查询的数据
	private final int COUNT = 10;//分批查询数据的条数

	private BlacklistDao dao; //用来操作数据库的dao

	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			if(data!=null) {
				String phone = data.getStringExtra(MyConstants.SAFE_NUMBER);
				if(!TextUtils.isEmpty(phone)) {
					showAddBlacklistHandwriteDialog(phone);
				}
			}
			
		}
	}
	
	private PopupWindow pw; //弹出窗体:添加黑名单功能
	private void initEvent() {

		lv_blacklist.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
					//获取ListView显示最后一个Item的下标位置
					int lastVisiblePosition = lv_blacklist.getLastVisiblePosition();
					if(lastVisiblePosition == blacklist.size()-1) {
						initData();
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btn_add.setOnClickListener(new View.OnClickListener() {
			
			private View contentView;
			private ScaleAnimation sa;
			private View.OnClickListener listener = new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.tv_item_blacklist_add_handwrite:
						showAddBlacklistHandwriteDialog("");
						dismissPW();
						break;
					case R.id.tv_item_blacklist_add_contacts:
					{
						Intent intent = new Intent(TeleSmsActivity.this, ContactsActivity.class);
						startActivityForResult(intent, 1);
						dismissPW();
						break;
					}
					case R.id.tv_item_blacklist_add_phonelog:
					{
						Intent intent = new Intent(TeleSmsActivity.this, CallLogActivity.class);
						startActivityForResult(intent, 1);
						dismissPW();
						break;
					}
					case R.id.tv_item_blacklist_add_smslog:
					{
						Intent intent = new Intent(TeleSmsActivity.this, SmsLogActivity.class);
						startActivityForResult(intent, 1);
						dismissPW();
						break;
					}

					default:
						break;
					}
				}
			};
			@Override
			public void onClick(View v) {
				
				if(contentView == null) {
					contentView = View.inflate(TeleSmsActivity.this, R.layout.item_blacklist_add, null);
					contentView.setAnimation(sa);
				}
				TextView tv_add_handwrite = (TextView) contentView.findViewById(R.id.tv_item_blacklist_add_handwrite);
				TextView tv_add_contacts = (TextView) contentView.findViewById(R.id.tv_item_blacklist_add_contacts);
				TextView tv_add_phonelog = (TextView) contentView.findViewById(R.id.tv_item_blacklist_add_phonelog);
				TextView tv_add_smslog = (TextView) contentView.findViewById(R.id.tv_item_blacklist_add_smslog);
				tv_add_handwrite.setOnClickListener(listener);
				tv_add_contacts.setOnClickListener(listener);
				tv_add_phonelog.setOnClickListener(listener);
				tv_add_smslog.setOnClickListener(listener);
				if(sa == null) {
					sa = new ScaleAnimation(1f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
					sa.setDuration(500L);
				}
				if(pw == null) {
					pw = new PopupWindow(contentView, -2, -2);//-2为包裹内容
				}
				if(pw != null) {
					if(pw.isShowing()) {
						dismissPW();
					} else {
						int[] location = new int[2];
						btn_add.getLocationInWindow(location);
						pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
						pw.showAtLocation(btn_add, Gravity.RIGHT|Gravity.TOP, 
								getWindowManager().getDefaultDisplay().getWidth()-(location[0]+btn_add.getWidth()),
								btn_add.getHeight()+location[1]);
					}
				}
			}

			private void dismissPW() {
				if(pw!=null && pw.isShowing()) {
					pw.dismiss();
					pw = null;
					sa = null;
					contentView = null;
				}
			}

			
		});
	}

	private void showAddBlacklistHandwriteDialog(String phone) {
		AlertDialog.Builder builder = new AlertDialog.Builder(TeleSmsActivity.this);
		View addBlacklistView = View.inflate(TeleSmsActivity.this, R.layout.dialog_telesms_blacklist_add, null);
		builder.setView(addBlacklistView);
		final AlertDialog addBlacklistDialog = builder.show();
		final EditText et_blacklist_phone = (EditText) addBlacklistView.findViewById(R.id.et_dialog_telesms_blacklist_phone);
		et_blacklist_phone.setText(phone);
		final CheckBox cb_blacklist_phone = (CheckBox) addBlacklistView.findViewById(R.id.cb_dialog_telesms_blacklist_phone);
		final CheckBox cb_blacklist_sms = (CheckBox) addBlacklistView.findViewById(R.id.cb_dialog_telesms_blacklist_sms);
		Button btn_blacklist_add = (Button) addBlacklistView.findViewById(R.id.btn_dialog_telesms_blacklist_add);
		Button btn_blacklist_cancle = (Button) addBlacklistView.findViewById(R.id.btn_dialog_telesms_blacklist_cancel);
		btn_blacklist_cancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addBlacklistDialog.dismiss();
			}
		});
		btn_blacklist_add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phone = et_blacklist_phone.getText().toString().trim();
				if(TextUtils.isEmpty(phone)) {
					Toast.makeText(TeleSmsActivity.this, "拦截号码不能为空", Toast.LENGTH_SHORT).show();
					return ;
				}
				//号码不为空
				if(!cb_blacklist_phone.isChecked() && !cb_blacklist_sms.isChecked()) {
					Toast.makeText(TeleSmsActivity.this, "拦截电话或者信息必须至少选择一个", Toast.LENGTH_SHORT).show();
					return ;
				}
				//有选择
				int mode = 0;
				if(cb_blacklist_phone.isChecked()) {
					mode |= BlacklistTable.TEL;
				}
				if(cb_blacklist_sms.isChecked()) {
					mode |= BlacklistTable.SMS;
				}
				//添加到数据库
				BlackBean bean = new BlackBean(phone, mode);
				//把原来存在的删除
				dao.delete(phone);
				blacklist.remove(bean);
				addBlacklistDialog.dismiss();
				//把新增的加入
				dao.add(bean);
				blacklist.add(0, bean);
				//通知把新增的显示
				mAdapter.notifyDataSetChanged();
			}
		});
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

			public void run() {
				mHandler.obtainMessage(LOADING).sendToTarget();
				dao = new BlacklistDao(TeleSmsActivity.this);
				/*//获取黑名单数据
				blacklist = dao.getAllDatas();*/
				totalCounts = dao.getTotalRows(); //获取总条数
				moreDatas = dao.getMoreDatas(blacklist.size(), COUNT);
				blacklist.addAll(moreDatas);
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
				
				if (moreDatas!=null && moreDatas.size()!=0) {
					//隐藏progressBar
					pb_loading.setVisibility(View.GONE);
					//显示 显示数据的ListView
					lv_blacklist.setVisibility(View.VISIBLE);
					//隐藏显示没数据的TextView 
					tv_nodata.setVisibility(View.GONE);
					//通知更新数据的显示.
					mAdapter.notifyDataSetChanged();
				} else {
					//没数据
					if(blacklist.size() == totalCounts) {
						Toast.makeText(TeleSmsActivity.this, "已经没有更多数据了", Toast.LENGTH_SHORT).show();
						//隐藏progressBar
						pb_loading.setVisibility(View.GONE);
						//显示 显示数据的ListView
						lv_blacklist.setVisibility(View.VISIBLE);
						break;
					}
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
				convertView = View.inflate(TeleSmsActivity.this, R.layout.item_telesms_blacklist, null);
				lvi = new ListViewItem();
				lvi.tv_item_phone = (TextView) convertView.findViewById(R.id.tv_item_telesms_phone);
				lvi.tv_item_mode = (TextView) convertView.findViewById(R.id.tv_item_telesms_mode);
				lvi.iv_item_delete = (ImageView) convertView.findViewById(R.id.iv_item_btn_telesms_delete);
				convertView.setTag(lvi);
			} else {
				lvi = (ListViewItem) convertView.getTag();
			}
			final BlackBean blackBean = blacklist.get(position);
			lvi.tv_item_phone.setText(blackBean.getPhone());
			int mode = blackBean.getMode();
			if(BlacklistTable.SMS == mode) {
				lvi.tv_item_mode.setText("拦截短信");
			} else if(BlacklistTable.TEL == mode){
				lvi.tv_item_mode.setText("拦截电话");
			} else if(BlacklistTable.ALL == mode){
				lvi.tv_item_mode.setText("全部拦截");
			}
			final int mposition = position;
			lvi.iv_item_delete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					AlertDialog.Builder builder = new AlertDialog.Builder(TeleSmsActivity.this);
					builder.setTitle("删除")
					.setMessage("您真的确定要删除?")
					.setPositiveButton("真删", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dao.delete(blackBean.getPhone());
							blacklist.remove(mposition);
							mAdapter.notifyDataSetChanged();
						}
					})
					.setNegativeButton("点错了", null).show();
				}
			});
			
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

//		setContentView(R.layout.activity_telesms_pages); //分页查看的界面
		setContentView(R.layout.activity_telesms); //非分页查看的界面
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
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(pw != null) {
			pw.dismiss();
			pw = null;
		}
	}
}
