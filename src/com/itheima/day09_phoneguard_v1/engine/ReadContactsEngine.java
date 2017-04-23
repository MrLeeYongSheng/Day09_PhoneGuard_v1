package com.itheima.day09_phoneguard_v1.engine;

import java.util.ArrayList;
import java.util.List;

import com.itheima.day09_phoneguard_v1.domain.ContactBean;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ReadContactsEngine {
	
	
	/**
	 * @param context
	 * 	上下文
	 * @return
	 * 	封装了电话号码的通短信记录
	 */
	public static List<ContactBean> getSmsLogs(Context context){
		List<ContactBean> smsLogs = new ArrayList<ContactBean>();
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms");
		Cursor cursor = resolver.query(uri , new String[]{"address"}, null, null, "_id desc");
		while(cursor.moveToNext()) {
			ContactBean bean = new ContactBean();
			bean.setName("");
			bean.setPhone(cursor.getString(cursor.getColumnIndex("address")));
			smsLogs.add(bean);
		}
		cursor.close();
		return smsLogs;
	}
	
	/**
	 * @param context
	 * 	上下文
	 * @return
	 * 	封装了姓名和电话号码的通话记录
	 */
	public static List<ContactBean> getCallLogs(Context context){
		List<ContactBean> callLogs = new ArrayList<ContactBean>();
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://call_log/calls");
		Cursor cursor = resolver.query(uri , new String[]{"name","number"}, null, null, "_id desc");
		while(cursor.moveToNext()) {
			ContactBean bean = new ContactBean();
			String name = cursor.getString(cursor.getColumnIndex("name"));
			if(name == null) {
				name = "";
			}
			bean.setName(name);
			bean.setPhone(cursor.getString(cursor.getColumnIndex("number")));
			callLogs.add(bean);
		}
		cursor.close();
		return callLogs;
	}

	/**
	 * 读取联系人
	 */
	public static List<ContactBean> getContacts(Context context) {
		List<ContactBean> contacts = new ArrayList<ContactBean>();
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://com.android.contacts/contacts");
		Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);
		while(cursor.moveToNext()) {
			ContactBean bean = new ContactBean();
			String id = cursor.getString(cursor.getColumnIndex("_id"));
			bean.setId(id);
			Uri uri2 = Uri.parse("content://com.android.contacts/data");
			Cursor cursor2 = resolver.query(uri2, new String[]{"data1","mimetype"},"contact_id=?" , new String[]{id}, null);
			while(cursor2.moveToNext()) {
				String data = cursor2.getString(cursor2.getColumnIndex("data1"));
				if("vnd.android.cursor.item/phone_v2".equals(cursor2.getString(cursor2.getColumnIndex("mimetype")))) {
					bean.setPhone(data);
				} else if("vnd.android.cursor.item/name".equals(cursor2.getString(cursor2.getColumnIndex("mimetype")))){
					bean.setName(data);
				}
			}
			contacts.add(bean);
			cursor2.close();
		}
		cursor.close();
		return contacts;
	}
}
