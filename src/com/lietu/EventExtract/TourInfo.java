package com.lietu.EventExtract;

import java.util.Date;

/**
 * 搜索信息的JavaBean
 * @author wolf
 *
 */
public class TourInfo {
	private static final long serialVersionUID = 8888L;
	private String title;
	private String url;
	private Date date;
	private String body = "";
	private String destination = "";
	private String qq = "";
	private String mobile = "";
	private String leader = "";
	private String counter = "";
	private String email = "";
	private String city = "";
	private String detailAddress = "";
	private String msninfo = "";
	private String telephone = "";
	private String category = "";
	private String fromwebsite = "";
	private String image = "";
	private String price = "";
	private String days = "";
	private String startTime = "";
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getFromwebsite() {
		return fromwebsite;
	}
	public void setFromwebsite(String fromwebsite) {
		this.fromwebsite = fromwebsite;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public String getCounter() {
		return counter;
	}
	public void setCounter(String counter) {
		this.counter = counter;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}	
	public void setCity(String city) {
		this.city = city;
	}
	public String getCity() {
		return city;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	public String getDetailAddress() {
		return detailAddress;
	}	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMsninfo() {
		return msninfo;
	}
	public void setMsninfo(String msninfo) {
		this.msninfo = msninfo;
	}	
	
	
	/**
	 * 覆盖toString()方法
	 */
	public String toString() {
		 String contract = "TEST  ";
		
		 if (qq.trim().length() > 0)
		 contract += "QQ:" + qq.trim();
		 
		 if (mobile.trim().length() > 0)
		 contract += " 手机:" + mobile.trim();
		 
		 if(telephone.trim().length() > 0)
		 contract += "固话：" + telephone.trim(); 
		
		 if (leader.trim().length() > 0)
		 contract += "  召集人:" + leader.trim();		 
		 
		 if (counter.trim().length() > 0)
		 contract += "  参与人数:" + counter.trim();
						
		 if (email.trim().length() > 0)
		 contract += "  email:" + email.trim();
						
		 if (city.trim().length() > 0)
		 contract += "  城市:" + city.trim();
		 
		 if (detailAddress.trim().length() > 0)
		 contract += "  详细地址:" + detailAddress.trim();	
		 
		 if(msninfo.trim().length() > 0)
			 contract += " MSN:" + msninfo.trim();

		 return contract;
	}
	
}
