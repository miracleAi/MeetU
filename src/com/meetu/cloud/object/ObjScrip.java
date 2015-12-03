package com.meetu.cloud.object;

import java.io.Serializable;
import java.util.List;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;

@AVClassName("ObjScrip")
public class ObjScrip extends AVObject implements Serializable {
	public static final Creator CREATOR = AVObjectCreator.instance;
	/**
	 * 纸条所属的纸箱
	 */
	public static final String SCRIPBOX = "scripBox";
	/**
	 * 纸条的发送者
	 */
	ObjUser sender;
	public static final String SENDER = "sender";
	/**
	 * 纸条的接收者
	 */
	ObjUser receiver;
	public static final String RECEIVER = "receiver";
	List<String> m;
	public static final String M = "m";
	/**
	 * 纸条的文字内容
	 */
	String contentText;
	public static final String CONTENTTEXT = "contentText";
	/**
	 * 纸条的图片内容
	 */
	AVFile contentImage;
	public static final String CONTENTIMAGE = "contentImage";

	public ObjScrip() {
		// TODO Auto-generated constructor stub
	}

	public ObjScripBox getScripBox() {
		return this.getAVObject(SCRIPBOX);
	}

	public void setScripBox(ObjScripBox scripBox) {
		this.put(SCRIPBOX, scripBox);
	}

	public ObjUser getSender() {
		return this.getAVUser(SENDER, ObjUser.class);
	}

	public void setSender(ObjUser sender) {
		this.put(SENDER, sender);
	}

	public ObjUser getReceiver() {
		return this.getAVUser(RECEIVER, ObjUser.class);
	}

	public void setReceiver(ObjUser receiver) {
		this.put(RECEIVER, receiver);
	}

	public List<String> getM() {
		return this.getList(M);
	}

	public void setM(List<String> m) {
		this.put(M, m);
	}

	public String getContentText() {
		return this.getString(CONTENTTEXT);
	}

	public void setContentText(String contentText) {
		this.put(CONTENTTEXT, contentText);
	}

	public AVFile getContentImage() {
		return this.getAVFile(CONTENTIMAGE);
	}

	public void setContentImage(AVFile contentImage) {
		this.put(CONTENTIMAGE, contentImage);
	}

}
