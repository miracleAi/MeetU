package com.meetu.cloud.object;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;

@AVClassName("ObjGlobalAndroid")
public class ObjGlobalAndroid extends AVObject{
	public static final Creator CREATOR = AVObjectCreator.instance;
	public static  String VERSION = "version";
	public static String APK = "apk";
	public static String ISUPDATE = "isUpdate";
	public String getVersion() {
		return this.getString(VERSION);
	}
	public AVFile getApk() {
		return this.getAVFile(APK);
	}
	public boolean getIsupdate(){
		return this.getBoolean(ISUPDATE);
	}
}
