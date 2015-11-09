package com.meetu.cloud.object;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;
@AVClassName("ObjUserPhoto")
public class ObjUserPhoto extends AVObject{
	public static final Creator CREATOR = AVObjectCreator.instance;
	/**
	 *  照片所属用户
	 */
	public static final String USER = "user";
	/**
	 *  照片文件
	 */
	public static final String PHOTO = "photo";
	/**
	 *  照片描述
	 */
	public static final String PHOTODESCRIPTION = "photoDescription";
	/**
	 *  照片浏览量
	 */
	public static final String BROWSECOUNT = "browseCount";
	/**
	 *  照片点赞量
	 */
	public static final String  PRAISECOUNT = "praiseCount";
	/**
	 *  照片宽度（像素）
	 */
	public static final String IMAGEWIDTH = "imageWidth";
	/**
	 *  照片高度
	 */
	public static final String IMAGEHEIGHT = "imageHeight";
	/**
	 *  照片最后浏览时间
	 */
	public static final String  FINALLYBROWSETIME = "finallyBrowseTime";
	
	public ObjUserPhoto() {
		// TODO Auto-generated constructor stub
		
	}
	
	public ObjUser getUser() {
		return this.getAVUser(USER, ObjUser.class);
	}
	public void setUser(ObjUser user) {
		this.put(USER, user);
	}
	public AVFile getPhoto() {
		return this.getAVFile(PHOTO);
	}
	public void setPhoto(AVFile photo) {
		this.put(PHOTO, photo);
	}
	public String getPhotoDescription() {
		return this.getString(PHOTODESCRIPTION);
	}
	public void setPhotoDescription(String photoDescription) {
		this.put(PHOTODESCRIPTION, photoDescription);
	}
	public int getBrowseCount() {
		return this.getInt(BROWSECOUNT);
	}
	public void setBrowseCount(int browseCount) {
		this.put(BROWSECOUNT, browseCount);
	}
	public int getPraiseCount() {
		return this.getInt(PRAISECOUNT);
	}
	public void setPraiseCount(int praiseCount) {
		this.put(PRAISECOUNT, praiseCount);;
	}
	public int getImageWidth() {
		return this.getInt(IMAGEWIDTH);
	}
	public void setImageWidth(int imageWidth) {
		this.put(IMAGEWIDTH, imageWidth);
	}
	public int getImageHeight() {
		return this.getInt(IMAGEHEIGHT);
	}
	public void setImageHeight(int imageHeight) {
		this.put(IMAGEHEIGHT, imageHeight);;
	}
	public long getFinallyBrowseTime() {
		return this.getLong(FINALLYBROWSETIME);
	}
	public void setFinallyBrowseTime(long finallyBrowseTime) {
		this.put(FINALLYBROWSETIME, finallyBrowseTime);;
	}
	
}
