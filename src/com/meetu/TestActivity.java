package com.meetu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.activity.SetPersonalInformation2Activity;
import com.meetu.activity.SetPersonalInformationActivity;
import com.meetu.bean.ActivityBean;
import com.meetu.cloud.callback.ObjActivityCallback;
import com.meetu.cloud.callback.ObjActivityCoverCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityCover;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityCoverWrap;
import com.meetu.cloud.wrap.ObjActivityWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.cloud.wrap.ObjWrap;
import com.meetu.common.Constants;
import com.meetu.tools.BitmapCut;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends Activity{
	private ImageView ivTouxiang;
	private Button clickBtn;
	private EditText ed;
	String fPath = "";
	String yPath = "";
	boolean isSms = true;
	//活动测试
	private ImageView favorImg;
	private TextView favrCout,followTv,joinTv,statusTv,titleTv,addressTv,timeTv,bigImg,contentTv,orderUserTv,actyCoverTv;
	ArrayList<ActivityBean> activitys = new ArrayList<ActivityBean>();
	ActivityBean bean = new ActivityBean();
	ArrayList<ObjActivityCover> coverList = new ArrayList<ObjActivityCover>();
	ArrayList<AVUser> userList = new ArrayList<AVUser>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//全屏
		super.getWindow(); 
		setContentView(R.layout.test_layout);
		initView();
		initActivity();
	}

	private void initActivity() {
		// TODO Auto-generated method stub
		bigImg =  (TextView) findViewById(R.id.big_img);
		favorImg = (ImageView) findViewById(R.id.favor_img);
		favrCout = (TextView) findViewById(R.id.favor_count_tv);
		followTv = (TextView) findViewById(R.id.follow_tv);
		joinTv = (TextView) findViewById(R.id.join_tv);
		statusTv = (TextView) findViewById(R.id.status_tv);
		titleTv = (TextView) findViewById(R.id.title_tv);
		addressTv = (TextView) findViewById(R.id.address_tv);
		timeTv = (TextView) findViewById(R.id.time_tv);
		contentTv = (TextView) findViewById(R.id.content_tv);
		orderUserTv = (TextView) findViewById(R.id.user_order_tv);
		actyCoverTv = (TextView) findViewById(R.id.acty_cover_tv);
		getActivitys();
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constants.QUER_FAVOR_OK:
				boolean isFavor = bean.isFavor();
				if(isFavor){
					favorImg.setVisibility(View.VISIBLE);
				}else{
					favorImg.setVisibility(View.GONE);
				}
				break; 
			case Constants.QUER_ORDERFOLLOW_OK:
				int followCount = bean.getOrderAndFollow();
				followTv.setText("follow  "+String.valueOf(followCount));
				userList = bean.getOrderUsers();
				orderUserTv.setText("users"+userList.size());
				break;
			case Constants.QUER_ACTIVITYCOVER_OK:
				coverList = bean.getActyCovers();
				actyCoverTv.setText("cover:"+coverList.size());
				break;
			default:
				break;
			}
		};
	};
	//获取活动信息
	private void getActivitys() {
		// TODO Auto-generated method stub
		ObjActivityWrap.queryAllActivitys(new ObjActivityCallback() {

			@Override
			public void callback(List<ObjActivity> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					return;
				}
				if(objects != null && objects.size()>0){
					for(ObjActivity activity : objects){
						ActivityBean bean1 = new ActivityBean();
						bean1.setActivity(activity);
						activitys.add(bean1);
						if(activity.getObjectId().equals("55e2b25700b0ded317c48f3b")){
							bean = bean1;
						}
					}
					//查询是否点赞
					bean.queryFavor(bean.getActivity(), handler);
					String imgUrl = bean.getActivity().getActivityCover().getUrl();
					int praiseCount = bean.getActivity().getPraiseCount();
					int boyAndGirl = bean.getActivity().getOrderCountGirl();
					String statusStr = ObjActivity.getStatusStr(bean.getActivity().getStatus());
					String title = bean.getActivity().getTitle();
					String lacationTitle = bean.getActivity().getLocationAddress();
					String timeStart = bean.getActivity().getTimeStart()+"";
					//活动详情内容网页
					String actyContent = bean.getActivity().getActivityContent().getUrl();
					//封面展示图片列表
					bean.queryActyCovers(bean.getActivity(), handler);
					//参与用户列表 参与并且我关注的人数
					bean.queryOrderUsers(bean.getActivity(), handler);

					bigImg.setText(imgUrl);
					favrCout.setText("favor  "+String.valueOf(praiseCount));
					joinTv.setText("join  "+String.valueOf(boyAndGirl));
					statusTv.setText("status  "+statusStr);
					titleTv.setText("title  "+title);
					addressTv.setText("address  "+lacationTitle);
					timeTv.setText("startTime  "+timeStart);
					contentTv.setText("content  "+actyContent);
					actyCoverTv.setText("cover  "+String.valueOf(coverList.size()));
				}
			}
		});
	}
	private void startTwo(){
		ActivityBean bean = activitys.get(0);
		Intent intent = new Intent(TestActivity.this,TestActivityTwo.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("activity", bean);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	private void initView(){
		ivTouxiang=(ImageView)super.findViewById(R.id.selfinfo1_userhead_img);
		clickBtn = (Button) findViewById(R.id.click);
		ed = (EditText) findViewById(R.id.ed);
		Bitmap head=readHead();
		if(head!=null){
			fPath = Environment.getExternalStorageDirectory()+"/f_user_header.png";
			yPath = Environment.getExternalStorageDirectory()+"/user_header.png";
			ivTouxiang.setImageBitmap(head);
		}
		ivTouxiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
		clickBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AVUser currentUser = AVUser.getCurrentUser();
				if (currentUser != null) {
					try {
						ObjUser user = AVObject.createWithoutData(ObjUser.class, currentUser.getObjectId());
						//完善个人信息
						/**
						 * completeInfo(user);
						 * */
					} catch (AVException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//忘记密码，获取短信验证 & 重置密码
					/**
					 * if(isSms){
						forgetPsdSms("13061481781");
						Toast.makeText(getApplicationContext(), "send sms code", 1000).show();
						isSms = false;
					}else{
						resetSmsPsd(ed.getText().toString(), "123456");
						isSms = true;
					}
					 */
				}else{
					Toast.makeText(getApplicationContext(), "not login", 1000).show();
					return ;
				}
			}
		});
	}
	//忘记密码短信验证
	public void forgetPsdSms(String phoneNum){
		ObjUserWrap.requestSmsCodeForResetPasswd(phoneNum, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(result){
					//发送成功
				}
			}
		});
	}
	//重置密码
	public void resetSmsPsd(String smsNum,String newPsd){
		ObjUserWrap.resetPasswd(smsNum, newPsd, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(result){
					//修改成功
					Toast.makeText(getApplicationContext(), "reset suc", 1000).show();
				}
			}
		});
	}
	AVFile fFile = null;
	AVFile yFile = null;
	//完善个人信息
	public void completeInfo(final ObjUser user){
		File upF1 = new File(yPath);
		File upF2 = new File(fPath);
		try {
			user.setNameNick("tom");
			user.setGender(1);
			//获取时间戳（单位毫秒）
			user.setBirthday(36985214745L);
			//根据生日计算
			user.setConstellation("天枰座");
			user.setSchool("university");
			//学校编码，查询数据库
			user.setSchoolNum(1001);
			//学校所在地编码，查数据库
			user.setSchoolLocation(1001);
			//此处为专业分类名称
			user.setDepartment("computer");
			//专业分类编码，数据库查询
			user.setDepartmentId(1);
			user.setHometown("china");
			fFile = AVFile.withAbsoluteLocalPath(Constants.HEAD_FILE_RECT+user.getObjectId()+Constants.IMG_TYPE, fPath);
			yFile = AVFile.withAbsoluteLocalPath(Constants.HEAD_FILE_CIRCLE+user.getObjectId()+Constants.IMG_TYPE, yPath);
			fFile.saveInBackground(new SaveCallback() {

				@Override
				public void done(AVException e) {
					// TODO Auto-generated method stub
					if(e != null){
						return ;
					}
					user.setProfileClip(fFile);
					yFile.saveInBackground(new SaveCallback() {

						@Override
						public void done(AVException e) {
							// TODO Auto-generated method stub
							if(e != null){
								return ;
							}
							user.setProfileOrign(yFile);
							ObjUserWrap.completeUserInfo(user,new ObjFunBooleanCallback() {

								@Override
								public void callback(boolean result, AVException e) {
									// TODO Auto-generated method stub
									if(result){
										Toast.makeText(getApplicationContext(), "save suc", 1000).show();
									}else{
										Toast.makeText(getApplicationContext(), "save fail", 1000).show();
									}
								}
							});

						}
					});
				}
			});
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
						"/user_header.png")));
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
						+ "/user_header.png");
				cropPhoto(Uri.fromFile(temp));//裁剪图片

			}

			break;
		case 33:
			if(data!=null){
				Bundle extras=data.getExtras();
				//裁剪后图片
				headerPortait=extras.getParcelable("data");
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
			path = Environment.getExternalStorageDirectory()+"/user_header.png";
		}else{
			path = Environment.getExternalStorageDirectory()+"/f_user_header.png";
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
		String file=Environment.getExternalStorageDirectory()+"/user_header.png";
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
