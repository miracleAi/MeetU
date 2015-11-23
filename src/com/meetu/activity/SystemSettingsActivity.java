package com.meetu.activity;

import com.meetu.R;
import com.meetu.R.layout;
import com.meetu.R.menu;
import com.meetu.cloud.wrap.ObjUserWrap;

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
			Toast.makeText(this, "退出", Toast.LENGTH_SHORT).show();
			
			ObjUserWrap.logOut();
			Intent intent=new Intent(this,LoginActivity.class);
			startActivity(intent);
			
			break;
		case R.id.back_systemSettings_rl:
			finish();
			break;

		default:
			break;
		}
		
	}

}
