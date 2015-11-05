package com.meetu.cloud.object;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;

@AVClassName("ObjActivityPhoto")
public class ObjActivityPhoto extends AVObject{
	public static final Creator CREATOR = AVObjectCreator.instance;
	/**
	 *  照片所属活动（只读）
	 */
	public static final String ACTIVITY = "activity";
	/**
	 *  照片（文件）（只读）
	 */
	public static final String PHOTO = "photo";
	/**
	 *  点赞数量（只读）
	 */
	public static final String PRAISECOUNT = "praiseCount";
	/**
	 *  照片宽度（像素）（只读）
	 */
	public static final String PHOTOWIDTH = "photoWidth";
	/**
	 *  照片高度（像素）（只读）
	 */
	public static final String PHOTOHEIGHT = "photoHeight";
	public ObjActivityPhoto() {
		// TODO Auto-generated constructor stub
	}
	public ObjActivity getActivity() {
		ObjActivity ac = new ObjActivity();
		try {
			ac = this.getAVObject(ACTIVITY, ObjActivity.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ac;
	}
	public AVFile getPhoto() {
		return this.getAVFile(PHOTO);
	}
	public int getPraiseCount() {
		return this.getInt(PRAISECOUNT);
	}
	public int getPhotoWidth() {
		return this.getInt(PHOTOWIDTH);
	}
	public int getPhotoHeight() {
		return this.getInt(PHOTOHEIGHT);
	}
	
}
