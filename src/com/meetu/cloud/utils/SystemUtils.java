package com.meetu.cloud.utils;

import java.io.File;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class SystemUtils {
	/**
	 * 获取当前app的版本号
	 * @param context
	 * @return
	 */
	public static String getVersion(Context context) {
		String versionname;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionname = pi.versionName;// 获取在AndroidManifest.xml中配置的版本号
		} catch (PackageManager.NameNotFoundException e) {
			versionname = "";
		}
		return versionname;
	}
	/**
	 * 获取文件大小
	 * 
	 * @param file
	 * @return
	 */
	public static double getDirSize(File file)
	{
		// 判断文件是否存在
		if (file.exists())
		{
			// 如果是目录则递归计算其内容的总大小
			if (file.isDirectory())
			{
				File[] children = file.listFiles();
				double size = 0;
				for (File f : children)
					size += getDirSize(f);
				return size;
			}
			else
			{// 如果是文件则直接返回其大小,以“兆”为单位
				double size = (double) file.length() / 1024 / 1024;
				return size;
			}
		}
		else
		{
			return 0.0;
		}
	}
}
