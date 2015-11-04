package com.meetu.cloud.object;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;
@AVClassName("ObjActivityCover")
public class ObjActivityCover extends AVObject{
	public static final Creator CREATOR = AVObjectCreator.instance;
	/**
	 *  封面所属活动（只读）
	 */
	//ObjActivity activity;
	public static final String ACTIVITY = "activity";
	/**
	 *  封面索引（只读）
	 */
	public static final String COVERINDEX = "coverIndex";
	/**
	 *  封面文件（图片）（只读）
	 */
	public static final String COVER = "cover";
	
	public ObjActivityCover() {
		// TODO Auto-generated constructor stub
	}
	
	public ObjActivity getActivity() {
		return this.getAVObject(ACTIVITY);
	}
	public int getCoverIndex() {
		return this.getInt(COVERINDEX);
	}
	public AVFile getCover() {
		return this.getAVFile(COVER);
	}
}
