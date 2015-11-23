package com.meetu.entity;
/** 
 * @author  lucifer 
 * @date 2015-11-23
 * @return  
 */
public class UserAbout {
	private String userName;
	private String userId;
	private String userHeadPhotoUrl;
	private Boolean isDetele;
	private int deleteImg;
	
	public int getDeleteImg() {
		return deleteImg;
	}
	public void setDeleteImg(int deleteImg) {
		this.deleteImg = deleteImg;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserHeadPhotoUrl() {
		return userHeadPhotoUrl;
	}
	public void setUserHeadPhotoUrl(String userHeadPhotoUrl) {
		this.userHeadPhotoUrl = userHeadPhotoUrl;
	}
	public Boolean getIsDetele() {
		return isDetele;
	}
	public void setIsDetele(Boolean isDetele) {
		this.isDetele = isDetele;
	}
	

}
