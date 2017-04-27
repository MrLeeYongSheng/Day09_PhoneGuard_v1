package com.itheima.day09_phoneguard_v1.domain;

import java.util.List;

public class SmsesJsonBean {

	public List<Smses> smses;
	public class Smses {
		public String address;
		public String body;
		public String date;
		public String type;
	}
}
