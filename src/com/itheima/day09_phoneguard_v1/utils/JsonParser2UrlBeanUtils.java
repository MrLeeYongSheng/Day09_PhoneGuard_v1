package com.itheima.day09_phoneguard_v1.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.itheima.day09_phoneguard_v1.domain.UrlBean;


public class JsonParser2UrlBeanUtils {

	public static UrlBean parser(String result) throws JSONException {
		JSONObject jObj = new JSONObject(result);
		UrlBean bean = new UrlBean();
		bean.setVerson(jObj.getInt("verson"));
		bean.setUrl(jObj.getString("url"));
		bean.setDesc(jObj.getString("desc"));
		return bean;
	}

}
