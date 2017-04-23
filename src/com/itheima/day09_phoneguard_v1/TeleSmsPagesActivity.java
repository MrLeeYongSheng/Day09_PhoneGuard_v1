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
	private List<BlackBean> blacklist = new ArrayList<BlackBean>();//�������������б�
	private MyAdapter mAdapter; //listView ��������
	private ListViewItem lvi;	//�����Ż�ListView�Ļ�����ʾ
	private int currentPage = 1;//��ʼ����ǰҳΪ1
	private final int count = 10; //�趨ÿҳ��ʮ������ 
	private int totalPages;
	private TextView tv_pages_currentpage; //��ȡ��ת��ҳ��
	private EditText et_pages_j2p; //��ʾ��ǰҳ

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
			Toast.makeText(TeleSmsPagesActivity.this, "��תҳ����Ϊ��", Toast.LENGTH_SHORT).show();
			return ;
		}
		int parseInt = Integer.parseInt(currentPageStr);
		if(parseInt<=0 || parseInt>totalPages) {
			Toast.makeText(TeleSmsPagesActivity.this, "������Ч!", Toast.LENGTH_SHORT).show();
			return ;
		}
		currentPage = parseInt;
		initData();
	}
	
	private void initData() {

		//��ʱ�Ķ����ڸ÷�����ִ��,����
		timeConsuming();	
	}

	/**
	 * ��ʱ���������������
	 * 		��ȡ����
	 */
	private void timeConsuming() {

		new Thread(){
			private BlacklistDao dao;

			public void run() {
				mHandler.obtainMessage(LOADING).sendToTarget();
				dao = new BlacklistDao(TeleSmsPagesActivity.this);
				//��ȡ����������
//				blacklist = dao.getAllDatas();
				blacklist = dao.getCurrentPageDatas(currentPage, count);  //�������Է�ҳ��ѯ�Ĳ�ѯĳһҳ������
				totalPages = dao.getTotalPages(count); //��ȡ��ҳ��
				mHandler.obtainMessage(FINISH).sendToTarget();
			};
		}.start();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING:
				//���ڼ�������
				//��ʾprogressBar
				pb_loading.setVisibility(View.VISIBLE);
				//������ʾ���ݵ�ListView 
				lv_blacklist.setVisibility(View.GONE);
				//������ʾû���ݵ�TextView 
				tv_nodata.setVisibility(View.GONE);
				break;
				
			case FINISH://����������ɺ�
				
				//������
				
				if (blacklist!=null && blacklist.size()!=0) {
					//����progressBar
					pb_loading.setVisibility(View.GONE);
					//��ʾ ��ʾ���ݵ�ListView
					lv_blacklist.setVisibility(View.VISIBLE);
					//������ʾû���ݵ�TextView 
					tv_nodata.setVisibility(View.GONE);
					//֪ͨ�������ݵ���ʾ.
					mAdapter.notifyDataSetChanged();
					//��ʾ��ǰҳ
					tv_pages_currentpage.setText(currentPage+"/"+totalPages);
				} else {
					//û����
					//����progressBar
					pb_loading.setVisibility(View.GONE);
					//������ʾ���ݵ�ListView
					lv_blacklist.setVisibility(View.GONE);
					//������ʾû���ݵ�TextView 
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
				lvi.tv_item_mode.setText("���ض���");
			} else if(BlacklistTable.TEL == mode){
				lvi.tv_item_mode.setText("���ص绰");
			} else if(BlacklistTable.ALL == mode){
				lvi.tv_item_mode.setText("ȫ������");
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

		setContentView(R.layout.activity_telesms_pages); //��ҳ�鿴�Ľ���
//		setContentView(R.layout.activity_telesms); //�Ƿ�ҳ�鿴�Ľ���
		//���Ӻ�������ť
		btn_add = (Button) findViewById(R.id.btn_telesms_add);
		//��ʾ���ݵ�listView
		lv_blacklist = (ListView) findViewById(R.id.lv_telesms_blacklist);
		//��ʾû�����ݵ�TextView
		tv_nodata = (TextView) findViewById(R.id.tv_telesms_nodata);
		//��ʾ�������ݵ�Progressbar
		pb_loading = (ProgressBar) findViewById(R.id.pb_telesms_loading);
		mAdapter = new MyAdapter();
		lv_blacklist.setAdapter(mAdapter);
		
		//��ʾ��ǰҳ
		tv_pages_currentpage = (TextView) findViewById(R.id.tv_telesms_pages_currentpage);
		//��תҳ��EditText
		et_pages_j2p = (EditText) findViewById(R.id.et_telesms_pages_j2p);
	}
}
