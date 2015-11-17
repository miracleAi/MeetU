package com.meetu.activity.miliao;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.R;
import com.meetu.R.layout;
import com.meetu.R.menu;
import com.meetu.cloud.callback.ObjAuthoriseApplyCallback;
import com.meetu.cloud.callback.ObjAuthoriseCategoryCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjAuthoriseApply;
import com.meetu.cloud.object.ObjAuthoriseCategory;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjAuthoriseWrap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ApplyForMiLiaoActivity extends Activity implements OnClickListener{
	private RelativeLayout backLayout;
	private ImageView applyImageView;
	private EditText content;//内容
	
	//网络相关
	AVUser currentUser = ObjUser.getCurrentUser();
	ObjUser user = new ObjUser();
	//权限
	ObjAuthoriseCategory category = new ObjAuthoriseCategory();
	//权限申请
		ObjAuthoriseApply apply = new ObjAuthoriseApply();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_apply_for_mi_liao);
		if(currentUser!=null){
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		category=(ObjAuthoriseCategory) getIntent().getExtras().get("category");
		initView();
	}

	private void initView() {
		backLayout=(RelativeLayout) super.findViewById(R.id.back_applyForMiLiao_rl);
		backLayout.setOnClickListener(this);
		applyImageView=(ImageView) super.findViewById(R.id.applyForMiLiao_img);
		applyImageView.setOnClickListener(this);
		content=(EditText) super.findViewById(R.id.content_applyForMiLiao_et);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_applyForMiLiao_rl:
			finish();
			
			break;
		case R.id.applyForMiLiao_img:
			
			
//			Intent intent=new Intent(ApplyForMiLiaoActivity.this,CreationChatActivity.class);
//			startActivity(intent);
			if(content.getText().length()==0){
				Toast.makeText(getApplicationContext(), "请填写申请内容", 1000).show();
			}else{
				queryIsApply(category);
				finish();
			}
			

		default:
			break;
		}
		
	}
	
	//查询是否已申请
		public void queryIsApply(final ObjAuthoriseCategory category){
			ObjAuthoriseWrap.queryApply(user, category, new ObjAuthoriseApplyCallback() {

				@Override
				public void callback(List<ObjAuthoriseApply> objects, AVException e) {
					// TODO Auto-generated method stub
					if(e != null){
					log.e("zcq", e);
						return;
					}
					if(objects.size() == 0){
						//没申请过
						applyAuthorise(category,content.getText().toString());
					}else{
						//申请过
						apply = objects.get(0);
						updateApplyAuthorise(apply,content.getText().toString());
					}
				}
			});
		}
		//发起申请
		/**
		 * 发起申请权限
		 * @param caty 
		 * @param argument  内容
		 * @author lucifer
		 * @date 2015-11-17
		 */
		public void applyAuthorise(ObjAuthoriseCategory caty,String argument){
			ObjAuthoriseWrap.applyAuthorise(user, caty, argument, new ObjFunBooleanCallback() {

				@Override
				public void callback(boolean result, AVException e) {
					// TODO Auto-generated method stub
					if(e != null){
						log.e("zcq", e);
			
						return;
					}
					if(result){
						log.e("zcq", "发起申请成功");
						Toast.makeText(getApplicationContext(), "发起申请成功", Toast.LENGTH_SHORT).show();
					}else{
						log.e("zcq", "发起申请失败，请检查网络");
						Toast.makeText(getApplicationContext(), "发起申请失败，请检查网络", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		//重新申请
		public void updateApplyAuthorise(ObjAuthoriseApply aply,String argument){
			aply.setLookStatus(0);
			ObjAuthoriseWrap.updateApplyAuthorise(aply, argument,new ObjFunBooleanCallback() {

				@Override
				public void callback(boolean result, AVException e) {
					// TODO Auto-generated method stub
					if(e != null){
						log.e("zcq", e);
						return;
					}
					if(result){
						log.e("zcq", "重新发起申请成功");
						Toast.makeText(getApplicationContext(), "重新发起申请成功", Toast.LENGTH_SHORT).show();
					}else{
						log.e("zcq", "重新发起申请失败，请检查网络");
						Toast.makeText(getApplicationContext(), "重新发起申请失败，请检查网络", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	
	

}
