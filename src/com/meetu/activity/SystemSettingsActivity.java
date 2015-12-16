package com.meetu.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.MainActivity;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjGlobalCallback;
import com.meetu.cloud.object.ObjGlobalAndroid;
import com.meetu.cloud.utils.SystemUtils;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjGlobalAndroidWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.myapplication.MyApplication;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.R.string;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
			/*shareMsg("meetu", "测试", "测试内容", Environment.getExternalStorageDirectory()
					+ "/f_user_header.png");*/
			break;
		case R.id.clear_system_settings_rl:
			clearAllCache(getApplicationContext());
			break;
		case R.id.checkforupdates_system_settings_rl:
			//getVersion();
			checkUpdate();
			break;
		case R.id.sign_out_system_settings_rl:
			showSignOutDialog();
			break;
		case R.id.back_systemSettings_rl:
			finish();
			break;

		default:
			break;
		}
	}
	private void checkUpdate() {
		// TODO Auto-generated method stub
		log.d("mytest", "zhixinging");
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
				log.d("mytest", "zhixing");
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					log.d("mytest", "update2");
					UmengUpdateAgent.showUpdateDialog(getApplicationContext(), updateInfo);
					break;
				case UpdateStatus.No: // has no update
					log.d("mytest", "no update");
					Toast.makeText(SystemSettingsActivity.this, "已经是最新版本啦！", Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.NoneWifi: // none wifi
					log.d("mytest", "no wifi");
					Toast.makeText(getApplicationContext(), "no wifi ", Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.Timeout: // time out
					log.d("mytest", "time out");
					Toast.makeText(getApplicationContext(), "time out", Toast.LENGTH_SHORT).show();
					break;
				}
			}

		});
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		//UmengUpdateAgent.forceUpdate(SystemSettingsActivity.this);
		UmengUpdateAgent.update(SystemSettingsActivity.this);
	}

	//退出登录弹窗
	private void showSignOutDialog() {
		// TODO Auto-generated method stub
		Dialog dialog = new AlertDialog.Builder(this).setTitle("提示").setMessage("亲爱的，真的要离开小U么？")
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).setNeutralButton("退出登录", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						signOut();
					}
				}).create();
		dialog.show();
	}
	//版本更新弹窗
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
						//下载完成后自动弹出安装
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
			if(children.length == 0){
				return false;
			}
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
			return dir.delete();
		}else{
			return false;
		}
	} 
	/**
	 * 进入市场评分
	 * */

	public void commentApp() {
		final PackageManager packageManager = SystemSettingsActivity.this.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		if(pinfo == null || pinfo.size() ==0){
			Toast.makeText(SystemSettingsActivity.this, "您还未安装任何应用市场", 1000).show();
			return;
		}
		try {
			Uri uri = Uri.parse("market://details?id="+getPackageName());  
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);  
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			startActivity(intent);  
		} catch (ActivityNotFoundException e) {
			// TODO: handle exception
			Toast.makeText(SystemSettingsActivity.this, "您还未安装任何应用市场", 1000).show();
		}
		// 判断360市场是否存在
		/*if (isAvilible(SystemSettingsActivity.this, "com.qihoo.appstore")) {
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
		}*/
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
	//调用系统分享
	public void shareMsg(String activityTitle, String msgTitle, String msgText,
			String imgPath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imgPath == null || imgPath.equals("")) {
			intent.setType("text/plain"); // 纯文本
		} else {
			File f = new File(imgPath);
			if (f != null && f.exists() && f.isFile()) {
				intent.setType("image/png");
				Uri u = Uri.fromFile(f);
				intent.putExtra(Intent.EXTRA_STREAM, u);
			}
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, activityTitle));
	}
	/**
	 * 注销登录需要做的操作，删除本地的用户对象 断开application的长连接
	 * 
	 * @author lucifer
	 * @date 2015-11-25
	 */
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
					Intent finishIntent = new Intent(Constants.MAIN_FINISH);
					sendBroadcast(finishIntent);
					Toast.makeText(getApplicationContext(), "退出", Toast.LENGTH_SHORT).show();	
					/*Intent i = getBaseContext().getPackageManager() 
							.getLaunchIntentForPackage(getBaseContext().getPackageName()); 
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					startActivity(i);*/
					Intent intent=new Intent(SystemSettingsActivity.this,LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}else{
					log.e("zcq", "长连接注销失败");
				}

			}
		});


	}

}
