package com.meetu.cachedb;

import java.lang.ref.WeakReference;

import android.content.Context;


public class ActivityCacheDb {
	public static final String ACTIVITYCACHE_TABLE="activityCache_table";
	private static ActivityCacheDb instance;
	private WeakReference<Context> wr  = null;
	public ActivityCacheDb(Context context) {
		// TODO Auto-generated constructor stub
		wr = new WeakReference<Context>(context);
	}
	public static ActivityCacheDb  getInstance(Context context){
		if(instance==null){
			instance = new ActivityCacheDb(context);
		}
		return instance;
	}
}
