package com.meetu.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.R;
import com.meetu.R.layout;
import com.meetu.R.menu;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjGlobalCallback;
import com.meetu.cloud.object.ObjGlobalAndroid;
import com.meetu.cloud.utils.SystemUtils;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjGlobalAndroidWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.myapplication.MyApplication;

import android.R.string;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 系统设置 页面
 * 
 * @author Administrator
 * 
 */
public class SystemSettingsActivity extends Activity implements OnClickListener {
	private RelativeLayout sharelayout, evaluationlayout, clearlayout,
			checkForUpdatelayout, signOutlayout;
	private RelativeLayout backLayout;

	private ObjGlobalAndroid globalObject;
	private TextView progressTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_system_settings);
		globalObject = new ObjGlobalAndroid();
		initView();
	}

	private void initView() {
		// TODO
		sharelayout = (RelativeLayout) super
				.findViewById(R.id.share_system_settings_rl);
		evaluationlayout = (RelativeLayout) super
				.findViewById(R.id.evaluation_system_settings_rl);
		clearlayout = (RelativeLayout) super
				.findViewById(R.id.clear_system_settings_rl);
		checkForUpdatelayout = (RelativeLayout) super
				.findViewById(R.id.checkforupdates_system_settings_rl);
		signOutlayout = (RelativeLayout) super
				.findViewById(R.id.sign_out_system_settings_rl);
		sharelayout.setOnClickListener(this);
		evaluationlayout.setOnClickListener(this);
		clearlayout.setOnClickListener(this);
		checkForUpdatelayout.setOnClickListener(this);
		signOutlayout.setOnClickListener(this);
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_systemSettings_rl);
		backLayout.setOnClickListener(this);
		sharelayout.setVisibility(View.GONE);
		progressTv = (TextView) findViewById(R.id.progress_tv);
		progressTv.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO
		switch (v.getId()) {
		case R.id.share_system_settings_rl:
			Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
			break;
		case R.id.evaluation_system_settings_rl:
			commentApp();
			break;
		case R.id.clear_system_settings_rl:
			clearAllCache(getApplicationContext());
			break;
		case R.id.checkforupdates_system_settings_rl:
			getVersion();
			break;
		case R.id.sign_out_system_settings_rl:
			signOut();
			finish();

			break;
		case R.id.back_systemSettings_rl:
			finish();
			break;

		default:
			break;
		}


	}
	public void showDialog(){
		Dialog dialog = new AlertDialog.Builder(this).setTitle("有新版本").setMessage("是否安装新版本？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						downloadApk();
					}
				}).setNeutralButton("暂不更新", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).create();
		dialog.show();
	}
	//检查版本
	public void getVersion(){
		ObjGlobalAndroidWrap.checkVersion(new ObjGlobalCallback() {
			
			@Override
			public void callback(ObjGlobalAndroid object, AVException e) {
				// TODO Auto-generated method stub
				if(e == null){
					globalObject = object;
					if(!globalObject.getVersion().equals(SystemUtils.getVersion(getApplicationContext()))){
						//Toast.makeText(getApplicationContext(), "有新版本", 1000).show();
						showDialog();
					}
				}
			}
		});
	}
	//下载apk文件
	public void downloadApk(){
		AVFile avfile = globalObject.getApk();
		avfile.getDataInBackground(new GetDataCallback() {

			@Override
			public void done(byte[] data, AVException e) {
				// TODO Auto-generated method stub
				if(e == null){
					File file=new File(Environment .getExternalStorageDirectory()+"/meetu","MeetU.apk");
					if(!file.exists()){
						file.getParentFile().mkdirs();
					}
					try {
						FileOutputStream fos = new FileOutputStream(file);
						fos.write(data);
						Intent intent = new Intent(Intent.ACTION_VIEW);  
				        intent.setDataAndType(Uri.fromFile(new File(Environment  
				                .getExternalStorageDirectory()+"/meetu", "MeetU.apk")),  
				                "application/vnd.android.package-archive");  
				        startActivity(intent);  
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else{
					Toast.makeText(getApplicationContext(), "下载安装包出错", 1000).show();
				}
			}
		}, new ProgressCallback() {

			@Override
			public void done(Integer progress) {
				// TODO Auto-generated method stub
				log.d("mytest", Integer.toString(progress));
				progressTv.setText(String.valueOf(progress));
			}
		}); 
	}
	/**
	 * 清除缓存
	 * */
	public static void clearAllCache(Context context) {
		//内部缓存
		deleteDir(context.getCacheDir());
		//外部缓存
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
			deleteDir(context.getExternalCacheDir());
		}  
		Toast.makeText(context, "缓存已清空", 1000).show();
	}
	//清除操作
	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	} 
	/**
	 * 进入360市场评分
	 * */

	public void commentApp() {
		// 判断360市场是否存在
		if (isAvilible(SystemSettingsActivity.this, "com.qihoo.appstore")) {
			// 市场存在
			Intent intent = new Intent(Intent.ACTION_VIEW);
			// 跳转到360市场评分
			ComponentName cn = new ComponentName("com.qihoo.appstore",
					"com.qihoo.appstore.activities.SearchDistributionActivity");
			intent.setComponent(cn);
			//此处替换ID为自己APP的ID
			intent.setData(Uri
					.parse("market://details?id=com.paopaobeauty.meinv"));
			startActivity(intent);
		} else {
			// 市场不存在
			Toast.makeText(getApplicationContext(), "请下载360手机助手", 0).show();
		}
	}

	// 判断市场是否存在的方法
	public static boolean isAvilible(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
		// 从pinfo中将包名字逐一取出，压入pName list中
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				pName.add(pn);
			}
		}
		return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE

	}

	/**
	 * 注销登录需要做的操作，删除本地的用户对象 断开application的长连接
	 * 
	 * @author lucifer
	 * @date 2015-11-25
	 */
//<<<<<<< HEAD
//	public void signOut() {
//		ObjChatMessage.logOutChat(MyApplication.chatClient,
//				new ObjFunBooleanCallback() {
//
//					@Override
//					public void callback(boolean result, AVException e) {
//						// TODO Auto-generated method stub
//						if (e != null) {
//							log.e("zcq", "退出" + e);
//							return;
//						} else if (result) {
//							log.e("zcq", "长连接注销成功");
//							ObjUserWrap.logOut();
//							Toast.makeText(getApplicationContext(), "退出",
//									Toast.LENGTH_SHORT).show();
//							Intent intent = new Intent(
//									SystemSettingsActivity.this,
//									LoginActivity.class);
//							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//							startActivity(intent);
//						} else {
//							log.e("zcq", "长连接注销失败");
//						}
//
//					}
//				});
//=======
	public void signOut(){
		ObjChatMessage.logOutChat(MyApplication.chatClient, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e!=null){
					log.e("zcq", "退出"+e);
					return;
				}else if(result){
					log.e("zcq", "长连接注销成功");
					ObjUserWrap.logOut();
					Toast.makeText(getApplicationContext(), "退出", Toast.LENGTH_SHORT).show();	
					Intent intent=new Intent(SystemSettingsActivity.this,LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}else{
					log.e("zcq", "长连接注销失败");
				}

			}
		});


	}

}
