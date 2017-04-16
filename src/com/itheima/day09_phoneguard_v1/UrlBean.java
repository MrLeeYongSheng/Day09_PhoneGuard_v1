package com.itheima.day09_phoneguard_v1;

public class UrlBean {

	private int verson;
	private String url;
	private String desc;
	public int getVerson() {
		return verson;
	}
	public void setVerson(int verson) {
		this.verson = verson;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "UrlBean [verson=" + verson + ", url=" + url + ", desc=" + desc
				+ "]";
	}
	
}
