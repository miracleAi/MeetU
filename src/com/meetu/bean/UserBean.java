package com.meetu.bean;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;

public class UserBean {
	private String userId;
	//用户类型
	private int userType;
	//用户是否是Vip用户
	private boolean  isVipUser;
	//用户是否已完成验证
	private boolean  isVerifyUser;
	//用户是否已完善个人信息
	private boolean  isCompleteUserInfo;
	//昵称
	private String nameNick;
	//真实名称
	private String nameReal;
	//性别
	private int gender;
	//生日
	private long birthday;
	//星座
	private String constellation;
	//学校
	private String school;
	//学校所在地编码
	private long schoolLocation;
	//学校编码
	private int schoolNum;
	//专业
	private String department;
	//专业编码
	private int departmentId;
	//家乡
	private String hometown;
	//切圆后的头像
	private String profileClip;
	//原始头像
	private String profileOrign;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public boolean isVipUser() {
		return isVipUser;
	}
	public void setVipUser(boolean isVipUser) {
		this.isVipUser = isVipUser;
	}
	public boolean isVerifyUser() {
		return isVerifyUser;
	}
	public void setVerifyUser(boolean isVerifyUser) {
		this.isVerifyUser = isVerifyUser;
	}
	public boolean isCompleteUserInfo() {
		return isCompleteUserInfo;
	}
	public void setCompleteUserInfo(boolean isCompleteUserInfo) {
		this.isCompleteUserInfo = isCompleteUserInfo;
	}
	public String getNameNick() {
		return nameNick;
	}
	public void setNameNick(String nameNick) {
		this.nameNick = nameNick;
	}
	public String getNameReal() {
		return nameReal;
	}
	public void setNameReal(String nameReal) {
		this.nameReal = nameReal;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public long getBirthday() {
		return birthday;
	}
	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}
	public String getConstellation() {
		return constellation;
	}
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public long getSchoolLocation() {
		return schoolLocation;
	}
	public void setSchoolLocation(long schoolLocation) {
		this.schoolLocation = schoolLocation;
	}
	public int getSchoolNum() {
		return schoolNum;
	}
	public void setSchoolNum(int schoolNum) {
		this.schoolNum = schoolNum;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public String getHometown() {
		return hometown;
	}
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	public String getProfileClip() {
		return profileClip;
	}
	public void setProfileClip(String profileClip) {
		this.profileClip = profileClip;
	}
	public String getProfileOrign() {
		return profileOrign;
	}
	public void setProfileOrign(String profileOrign) {
		this.profileOrign = profileOrign;
	}
	
}
