package com.meetu.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharepreferencesUtils {
	public static String SYS_MSG_SCAN = "systemMsg_scan";
	public static String PHOTO_FAVOR_SCAN = "favorHisPos";

	public static SharepreferencesUtils shareUtils ;
	public static SharepreferencesUtils getInstance(){
		if(shareUtils == null){
			shareUtils = new SharepreferencesUtils();
		}
		return shareUtils;
	}
	//获取系统消息最后浏览时间
	public long getSystemScanTime(Context context,String prefKey,long defValue){
		return getShared(context, prefKey, defValue);
	}
	//获取照片点赞列表最后浏览时间
	public long getPhotoFavorScanTime(Context context,String prefKey,long defValue){
		return getShared(context, prefKey, defValue);
	}
	public  String getShared(Context ctx, String prefKey, String defValue) {
		if(ctx != null){
			return PreferenceManager.getDefaultSharedPreferences(ctx).getString(prefKey, defValue);
		}else{
			return "";
		}
	}

	public  boolean getShared(Context ctx, String prefKey, boolean defValue) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(prefKey, defValue);
	}

	public  float getShared(Context ctx, String prefKey, float defValue) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getFloat(prefKey, defValue);
	}

	public  long getShared(Context ctx, String prefKey, long defValue) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getLong(prefKey, defValue);
	}

	public  int getShared(Context ctx, String prefKey, int defValue) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getInt(prefKey, defValue);
	}
}
