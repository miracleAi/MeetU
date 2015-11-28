package com.meetu.activity;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.R;
import com.meetu.R.layout;
import com.meetu.R.menu;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.myapplication.MyApplication;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
/**
 * 系统设置 页面
 * @author Administrator
 *
 */
public class SystemSettingsActivity extends Activity implements OnClickListener{
	private RelativeLayout sharelayout,evaluationlayout,clearlayout,checkForUpdatelayout,signOutlayout;
	private RelativeLayout backLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_system_settings);
		initView();
	}

	private void initView() {
		// TODO 
		sharelayout=(RelativeLayout) super.findViewById(R.id.share_system_settings_rl);
		evaluationlayout=(RelativeLayout) super.findViewById(R.id.evaluation_system_settings_rl);
		clearlayout=(RelativeLayout) super.findViewById(R.id.clear_system_settings_rl);
		checkForUpdatelayout=(RelativeLayout) super.findViewById(R.id.checkforupdates_system_settings_rl);
		signOutlayout=(RelativeLayout) super.findViewById(R.id.sign_out_system_settings_rl);
		sharelayout.setOnClickListener(this);
		evaluationlayout.setOnClickListener(this);
		clearlayout.setOnClickListener(this);
		checkForUpdatelayout.setOnClickListener(this);
		signOutlayout.setOnClickListener(this);
		backLayout=(RelativeLayout) super.findViewById(R.id.back_systemSettings_rl);
		backLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO
		switch (v.getId()) {
		case R.id.share_system_settings_rl:
			Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
			
			break;
		case R.id.evaluation_system_settings_rl:
			Toast.makeText(this, "评价", Toast.LENGTH_SHORT).show();
			
			break;
		case R.id.clear_system_settings_rl:
			Toast.makeText(this, "清除", Toast.LENGTH_SHORT).show();
			
			break;
		case R.id.checkforupdates_system_settings_rl:
			Toast.makeText(this, "检查", Toast.LENGTH_SHORT).show();
			
			break;
		case R.id.sign_out_system_settings_rl:
			//TODO 清缓存 删除本地 信息
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
