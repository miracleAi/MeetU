package com.meetu;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.meetu.cloud.callback.ObjAuthoriseApplyCallback;
import com.meetu.cloud.callback.ObjAuthoriseCategoryCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjAuthoriseApply;
import com.meetu.cloud.object.ObjAuthoriseCategory;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjAuthoriseWrap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class TestMsgActivity extends Activity{
	private static final String AUTHORISECATEGORY = "AuthoriseCategory";
	private static final String ISAPPLY = "isApply";
	private static final String HAVEAUTHOREISE = "haveAuthorise";
	private static final String STARTAPPLY = "startApply";
	private static final String UPDATEAPPLY = "updateApply";
	private static final String LOADFAIL = "loadfail";
	private static final String LOADSUC = "loadsuc";
	private static final String LOADING = "loading";
	private static final String CREATEGROUP = "createGroup";
	ImageView favorImag,upImg;
	Button clickBtn;
	//权限
	ObjAuthoriseCategory category = new ObjAuthoriseCategory();
	//权限申请
	ObjAuthoriseApply apply = new ObjAuthoriseApply();
	ObjUser user = new ObjUser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_msg_layout);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		favorImag = (ImageView) findViewById(R.id.favor_img);
		upImg = (ImageView) findViewById(R.id.up_img);
		clickBtn = (Button) findViewById(R.id.click);
		AVUser currentUser = ObjUser.getCurrentUser();
		user = AVUser.cast(currentUser, ObjUser.class);
		clickBtn.setText(AUTHORISECATEGORY);
		clickBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(clickBtn.getText().toString().equals(AUTHORISECATEGORY)){
					//查询权限分类，判断是否需要权限
					clickBtn.setText(LOADING);
					queryAuthoriseCategory(100);
					return;
				}
				if(clickBtn.getText().toString().equals(HAVEAUTHOREISE)){
					//查询是否拥有权限
					clickBtn.setText(LOADING);
					queryHaveAuthorise(category);
					return;
				}
				if(clickBtn.getText().toString().equals(ISAPPLY)){
					//查询是否已申请
					clickBtn.setText(LOADING);
					queryIsApply(category);
				}
				if(clickBtn.getText().toString().equals(STARTAPPLY)){
					//申请权限
					clickBtn.setText(LOADING);
					applyAuthorise(category, "hello");
				}
				if(clickBtn.getText().toString().equals(UPDATEAPPLY)){
					//重新申请权限
					clickBtn.setText(LOADING);
					updateApplyAuthorise(apply);
				}
				if(clickBtn.getText().toString().equals(CREATEGROUP)){
					//创建群聊
					
				}
			}
		});
	}
	//查询是否需要权限
	public void queryAuthoriseCategory(int operationNum){
		ObjAuthoriseWrap.queryAuthoriseCatogory(operationNum, new ObjAuthoriseCategoryCallback() {

			@Override
			public void callback(List<ObjAuthoriseCategory> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return;
				}
				if(objects.size() ==  0){
					clickBtn.setText(LOADFAIL);
					return;
				}
				category = objects.get(0);
				if(category.isNeedAuthorise()){
					clickBtn.setText(HAVEAUTHOREISE);
				}else{
					clickBtn.setText(CREATEGROUP);
				}
			}
		});
	}
	//查询是否有权限
	public void queryHaveAuthorise(ObjAuthoriseCategory category){
		ObjAuthoriseWrap.queryUserAuthorise(category,user, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return;
				}
				if(result){
					clickBtn.setText(CREATEGROUP);
				}else{
					clickBtn.setText(ISAPPLY);
				}
			}
		});
	}
	//查询是否已申请
	public void queryIsApply(ObjAuthoriseCategory category){
		ObjAuthoriseWrap.queryApply(user, category, new ObjAuthoriseApplyCallback() {

			@Override
			public void callback(List<ObjAuthoriseApply> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return;
				}
				if(objects.size() == 0){
					clickBtn.setText(STARTAPPLY);
				}else{
					apply = objects.get(0);
					clickBtn.setText(UPDATEAPPLY);
				}
			}
		});
	}
	//发起申请
	public void applyAuthorise(ObjAuthoriseCategory caty,String argument){
		ObjAuthoriseWrap.applyAuthorise(user, caty, argument, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return;
				}
				if(result){
					clickBtn.setText(LOADSUC);
				}else{
					clickBtn.setText(LOADFAIL);
				}
			}
		});
	}
	//重新申请
	public void updateApplyAuthorise(ObjAuthoriseApply aply){
		aply.setLookStatus(0);
		ObjAuthoriseWrap.updateApplyAuthorise(aply, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return;
				}
				if(result){
					clickBtn.setText(LOADSUC);
				}else{
					clickBtn.setText(LOADFAIL);
				}
			}
		});
	}
}
