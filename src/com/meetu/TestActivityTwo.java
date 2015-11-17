package com.meetu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.meetu.bean.ActivityBean;
import com.meetu.cloud.callback.ObjActivityCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjTicketCallback;
import com.meetu.cloud.callback.ObjUserPhotoCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityCover;
import com.meetu.cloud.object.ObjActivityTicket;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.cloud.object.ObjUserPhotoPraise;
import com.meetu.cloud.wrap.ObjActivityOrderWrap;
import com.meetu.cloud.wrap.ObjActivityWrap;
import com.meetu.cloud.wrap.ObjUserPhotoWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.tools.BitmapCut;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivityTwo extends Activity{
	private static final String LOADUSERPHOTO = "loadUserPhoto";
	private static final String LOADING = "loading";
	private static final String LOAD_FAIL = "fail";
	private static final String LOAD_SUC = "suc";
	private static final String PRAISERUSERPHOTO = "praiseUserPhoto";
	private static final String CANCELPRAISEUSERPHOTO = "cancelPraiseUserPhoto";
	private static final String ADDUSERPHOTO = "addUserPhoto";
	private static final String DELETEUSERPHOTO = "deleteUserPhoto";
	
	private ImageView ivTouxiang;
	private Button clickBtn;
	private EditText ed;
	String fPath = "";
	String yPath = "";
	boolean isSms = true;
	//活动测试
	private ImageView favorImg;
	private TextView favrCout,followTv,joinTv,statusTv,titleTv,addressTv,timeTv,bigImg,contentTv,orderUserTv,actyCoverTv;
	private TextView firstTv,secondTv;
	//当前用户
	ObjUser user = new ObjUser();
	//需上传的用户照片
	Bitmap userphoto;
	
	ObjUserPhoto photoBean = new ObjUserPhoto();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//全屏
		super.getWindow(); 
		setContentView(R.layout.testtwo_layout);
		initView();
	}
	private void initView(){
		ivTouxiang=(ImageView)super.findViewById(R.id.selfinfo1_userhead_img);
		clickBtn = (Button) findViewById(R.id.click);
		ed = (EditText) findViewById(R.id.ed);
		firstTv = (TextView) findViewById(R.id.first_tv);
		secondTv = (TextView) findViewById(R.id.second_tv);
		favorImg = (ImageView) findViewById(R.id.favor_img);
		Bitmap head=readHead();
		if(head!=null){
			userphoto = head;
			fPath = Environment.getExternalStorageDirectory()+"/f_user_photo.png";
			yPath = Environment.getExternalStorageDirectory()+"/user_photo.png";
		}
		ivTouxiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
		//clickBtn.setText(LOADUSERPHOTO);
		//clickBtn.setText(ADDUSERPHOTO);
		clickBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AVUser currentUser = AVUser.getCurrentUser();
				if (currentUser != null) {
					//强制类型转换
					user = AVUser.cast(currentUser, ObjUser.class);
					if(clickBtn.getText().toString().equals(LOADUSERPHOTO)){
						clickBtn.setText(LOADING);
						getUserPhoto(user);
						return ;
					}
					if(clickBtn.getText().toString().equals(PRAISERUSERPHOTO)){
						//对用户照片点赞
						clickBtn.setText(LOADING);
						praiseUserPhoto(photoBean, user);
						return ;
					}
					if(clickBtn.getText().toString().equals(CANCELPRAISEUSERPHOTO)){
						//对用户照片取消点赞
						clickBtn.setText(LOADING);
						cancelPraiseUserPhoto(photoBean, user);
						return ;
					}
					if(clickBtn.getText().toString().equals(ADDUSERPHOTO)){
						//上传用户照片
						clickBtn.setText(LOADING);
						upLoadUserPhoto(user);
						return ;
					}
					if(clickBtn.getText().toString().equals(DELETEUSERPHOTO)){
						clickBtn.setText(LOADING);
						deleteUserPhoto(photoBean);
						return ;
					}
				}else{
					Toast.makeText(getApplicationContext(), "not login", 1000).show();
					return ;
				}
			}
		});
	}
	AVFile userf =null;
	//上传用户照片
	public void upLoadUserPhoto(final ObjUser user){
		try {
			userf = AVFile.withAbsoluteLocalPath("ObjUserPhoto"+user.getObjectId()+Constants.IMG_TYPE, fPath);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ObjUserPhotoWrap.savePhoto(userf, new ObjFunBooleanCallback() {
			
			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOAD_FAIL);
					return ;
				}
				if(result){
					int wedth = userphoto.getWidth();
					int height = userphoto.getHeight();
					
					ObjUserPhotoWrap.addUserPhoto(user, userphoto, userf, "first", height, wedth, new ObjFunBooleanCallback() {
						
						@Override
						public void callback(boolean result, AVException e) {
							// TODO Auto-generated method stub
							if(e != null){
								clickBtn.setText(LOAD_FAIL);
							}
							if(result){
								clickBtn.setText(LOAD_SUC);
							}else{
								clickBtn.setText(LOAD_FAIL);
							}
						}
					});
				}else{
					clickBtn.setText(LOAD_FAIL);
				}
			}
		});
	}
	//获取用户照片
	public void getUserPhoto(ObjUser phuser){
		ObjUserPhotoWrap.queryUserPhoto(phuser, new ObjUserPhotoCallback() {
			
			@Override
			public void callback(List<ObjUserPhoto> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOAD_FAIL);
					return ;
				}
				if(objects != null && objects.size()>0){
					photoBean = objects.get(0);
					clickBtn.setText(DELETEUSERPHOTO);
					//queryIsPraiseUserPhoto(photoBean, user);
				}else{
					clickBtn.setText(LOAD_FAIL);
				}
			}
		});
	}
	//删除用户照片
	public void deleteUserPhoto(ObjUserPhoto photo){
		ObjUserPhotoWrap.deleteUserPhoto(photo, new ObjFunBooleanCallback() {
			
			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOAD_FAIL);
					return ;
				}
				if(result){
					clickBtn.setText(LOAD_SUC);
				}else{
					clickBtn.setText(LOAD_FAIL);
				}
			}
		});
	}
	//查询是否对用户照片点赞
	public void queryIsPraiseUserPhoto(ObjUserPhoto photo,ObjUser user){
		ObjUserPhotoWrap.queryUserPhotoPraise(photo, user, new ObjFunBooleanCallback() {
			
			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOAD_FAIL);
					return ;
				}
				if(result){
					clickBtn.setText(CANCELPRAISEUSERPHOTO);
					favorImg.setVisibility(View.VISIBLE);
				}else{
					clickBtn.setText(PRAISERUSERPHOTO);
					favorImg.setVisibility(View.GONE);
				}
			}
		});
	}
	//对用户照片点赞
	public void praiseUserPhoto(ObjUserPhoto photo,ObjUser user){
		ObjUserPhotoWrap.praiseUserPhoto(photoBean, user, new ObjFunBooleanCallback() {
			
			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOAD_FAIL);
					return ;
				}
				if(result){
					clickBtn.setText(CANCELPRAISEUSERPHOTO);
					favorImg.setVisibility(View.VISIBLE);
				}else{
					clickBtn.setText(LOAD_FAIL);
				}
			}
		});
	}
	//对用户照片取消点赞
	public void cancelPraiseUserPhoto(ObjUserPhoto photo,ObjUser user){
		ObjUserPhotoWrap.cancelPraiseUserPhoto(photo, user, new ObjFunBooleanCallback() {
			
			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOAD_FAIL);
					return ;
				}
				if(result){
					clickBtn.setText(PRAISERUSERPHOTO);
					favorImg.setVisibility(View.GONE);
				}else{
					clickBtn.setText(LOAD_FAIL);
				}
			}
		});
	}
	/**
	 * 
	 * 一下为测试界面相关部分
	 */

	private void showDialog(){
		final  AlertDialog portraidlg=new AlertDialog.Builder(this).create();
		portraidlg.show();
		Window win=portraidlg.getWindow();
		win.setContentView(R.layout.dialog_show_photo);
		RadioButton portrait_native=(RadioButton)win.findViewById(R.id.Portrait_native);
		portrait_native.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent1=new Intent(Intent.ACTION_PICK,null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent1, 11);
				portraidlg.dismiss();
			}
		});
		RadioButton portrait_take=(RadioButton)win.findViewById(R.id.Portrait_take);
		portrait_take.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//调用摄像头
				Intent intent2=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
						"/user_photo.png")));
				startActivityForResult(intent2, 22);
				portraidlg.dismiss();
			}
		});
		View viewTop=win.findViewById(R.id.view_top_dialog_sethead);
		View viewBottom=win.findViewById(R.id.view_bottom_dialog_sethead);
		//点击dialog外部，关闭dialog
		viewTop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				portraidlg.dismiss();
			}
		});
		viewBottom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				portraidlg.dismiss();
			}
		});



	}
	private Bitmap headerPortait;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 11:
			if(resultCode==this.RESULT_OK){
				cropPhoto(data.getData());//裁剪图片
			}
			break ;
		case 22:
			if(resultCode==this.RESULT_OK){
				File temp=new File(Environment.getExternalStorageDirectory()
						+ "/user_photo.png");
				cropPhoto(Uri.fromFile(temp));//裁剪图片

			}

			break;
		case 33:
			if(data!=null){
				Bundle extras=data.getExtras();
				//裁剪后图片
				headerPortait=extras.getParcelable("data");
				userphoto = headerPortait;
				if(headerPortait!=null){
					fPath = saveHeadImg(headerPortait,false);
				}
				//切圆图片
				headerPortait=BitmapCut.toRoundBitmap(headerPortait);
				if(headerPortait!=null){
					yPath = saveHeadImg(headerPortait,true);
					ivTouxiang.setImageBitmap(headerPortait);
				}
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	public String saveHeadImg(Bitmap head,boolean isY){
		FileOutputStream fos=null;
		String path = "";
		if(isY){
			path = Environment.getExternalStorageDirectory()+"/user_photo.png";
		}else{
			path = Environment.getExternalStorageDirectory()+"/f_user_photo.png";
		}
		try {
			fos=new FileOutputStream(new File(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		head.compress(CompressFormat.PNG, 100, fos);
		try {
			fos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shezhigerenxinxi, menu);
		return true;
	}


	public Bitmap readHead(){
		String file=Environment.getExternalStorageDirectory()+"/user_photo.png";
		return BitmapFactory.decodeFile(file);
	}

	/**
	 * 调用拍照的裁剪功能
	 * @param uri
	 */

	public void cropPhoto(Uri uri){
		//调用拍照的裁剪功能
		Intent intent=new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		//aspectX aspectY 是宽和搞的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// // outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		intent.putExtra("return-data",true);
		startActivityForResult(intent,33);
	}

}
