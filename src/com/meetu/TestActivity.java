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
import com.meetu.cloud.callback.ObjActivityPhotoCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjFunObjectsCallback;
import com.meetu.cloud.callback.ObjTicketCallback;
import com.meetu.cloud.callback.ObjUserCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityCover;
import com.meetu.cloud.object.ObjActivityPhoto;
import com.meetu.cloud.object.ObjActivityPraise;
import com.meetu.cloud.object.ObjActivityTicket;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityCoverWrap;
import com.meetu.cloud.wrap.ObjActivityOrderWrap;
import com.meetu.cloud.wrap.ObjActivityPhotoWrap;
import com.meetu.cloud.wrap.ObjActivityWrap;
import com.meetu.cloud.wrap.ObjFollowWrap;
import com.meetu.cloud.wrap.ObjPraiseWrap;
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
	private static final String SMS_PSD = "password";
	private static final String COMPLETE_INFO = "completeInfo";
	private static final String LOADING = "loading";
	private static final String LOAD_FAIL = "file";
	private static final String LOAD_SUC = "suc";
	private static final String LOAD_ACTIVITY = "loadactivity";
	private static final String LOAD_SIGN_INFO = "loadSignInfo";
	private static final String SIGN_UP = "signup";
	private static final String PRAISE_ACTIVITY = "praiseActivity";
	private static final String CANCEL_PRAISE_ACTY = "cancelPraiseActy";
	private static final String LOAD_ACTIVITY_PHOTO = "loadActivityPhoto";
	private static final String PRAISE_ACTY_PHOTO = "praiseactivityphoto";
	private static final String CANCEL_PRAISE_ACTY_PHOTO = "cancelPraiseActyPhoto";
	private static final String USER_FEEDBACK = "userFeedback";

	private ImageView ivTouxiang;
	private Button clickBtn;
	private EditText ed;
	String fPath = "";
	String yPath = "";
	boolean isSms = true;
	//活动测试
	private ImageView favorImg,photoFavor;
	private TextView favrCout,followTv,joinTv,statusTv,titleTv,addressTv,timeTv,bigImg,contentTv,orderUserTv,actyCoverTv;
	private TextView firstTv,secondTv;
	//首页某一项活动
	ActivityBean bean = new ActivityBean();
	ObjActivity  activityItem = new ObjActivity();
	//参加活动用户列表
	ArrayList<ObjUser> userList = new ArrayList<ObjUser>();
	//是否已参加活动
	boolean isJoin = false;
	//某项活动的票种列表
	private ArrayList<ObjActivityTicket> tickets = new ArrayList<ObjActivityTicket>();
	//活动某张照片
	private ObjActivityPhoto photoBean = new ObjActivityPhoto();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//全屏
		super.getWindow(); 
		setContentView(R.layout.test_layout);
		initView();
	}
	private void initView(){
		ivTouxiang=(ImageView)super.findViewById(R.id.selfinfo1_userhead_img);
		clickBtn = (Button) findViewById(R.id.click);
		ed = (EditText) findViewById(R.id.ed);
		firstTv = (TextView) findViewById(R.id.first_tv);
		secondTv = (TextView) findViewById(R.id.second_tv);
		photoFavor = (ImageView) findViewById(R.id.photo_favor);

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
		/**
		 * 进入时，测试功能标记
		 * */
		//clickBtn.setText(SMS_PSD);
		//clickBtn.setText(COMPLETE_INFO);
		clickBtn.setText(LOAD_ACTIVITY);
		clickBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AVUser currentUser = AVUser.getCurrentUser();
				if (currentUser != null) {
					//强制类型转换
					ObjUser user = AVUser.cast(currentUser, ObjUser.class);
					if(clickBtn.getText().toString().equals(SMS_PSD)){
						clickBtn.setText(LOADING);
						//忘记密码，获取短信验证 & 重置密码
						if(isSms){
							forgetPsdSms("13061481781");
							Toast.makeText(getApplicationContext(), "send sms code", 1000).show();
							isSms = false;
						}else{
							resetSmsPsd(ed.getText().toString(), "123456");
							isSms = true;
						}
						return ;
					}
					if(clickBtn.getText().toString().equals(COMPLETE_INFO)){
						clickBtn.setText(LOADING);
						//完善个人信息
						completeInfo(user);
						return ;
					}
					if(clickBtn.getText().toString().equals(LOAD_SIGN_INFO)){
						clickBtn.setText(LOADING);
						//去报名，获得活动信息
						getInfo(activityItem);
						return ;
					}
					if(clickBtn.getText().toString().equals(SIGN_UP)){
						clickBtn.setText(LOADING);
						//报名
						sinUp(activityItem);
						return ;
					}
					if(clickBtn.getText().toString().equals(PRAISE_ACTIVITY)){
						clickBtn.setText(LOADING);
						//活动点赞
						praiseActivity(activityItem);
						return ;
					}
					if(clickBtn.getText().toString().equals(CANCEL_PRAISE_ACTY)){
						clickBtn.setText(LOADING);
						//活动取消赞
						cancelPraiseActivity(activityItem);
						return ;
					}
					if(clickBtn.getText().toString().equals(LOAD_ACTIVITY)){
						clickBtn.setText(LOADING);
						//获取活动
						getActivitys();
						return ;
					}
					if(clickBtn.getText().toString().equals(LOAD_ACTIVITY_PHOTO)){
						clickBtn.setText(LOADING);
						//获取活动照片
						loadActvityPhoto(activityItem);
						return ;
					}
					if(clickBtn.getText().toString().equals(PRAISE_ACTY_PHOTO)){
						clickBtn.setText(LOADING);
						//对活动照片点赞
						praiseActivityPhoto(photoBean);
						return ;
					}
					if(clickBtn.getText().toString().equals(CANCEL_PRAISE_ACTY_PHOTO)){
						clickBtn.setText(LOADING);
						//对活动照片取消赞
						cancelPraiseActtyPhoto(photoBean);
						return ;
					}
					if(clickBtn.getText().toString().equals(USER_FEEDBACK)){
						clickBtn.setText(LOADING);
						//活动反馈
						activityFeedback(activityItem);
						return ;
					}
				}else{
					Toast.makeText(getApplicationContext(), "not login", 1000).show();
					return ;
				}
			}
		});
	}
	//活动信息请求成功回调执行
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constants.QUER_FAVOR_OK:
				boolean isFavor = bean.isFavor();
				//测试点赞 取消赞  获取点赞信息后执行
				if(isFavor){
					favorImg.setVisibility(View.VISIBLE);
					clickBtn.setText(CANCEL_PRAISE_ACTY);
				}else{
					favorImg.setVisibility(View.GONE);
					clickBtn.setText(PRAISE_ACTIVITY);
				}
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
					//测试去报名（获取活动成功后执行）
					//clickBtn.setText(LOAD_SIGN_INFO);
					//测试活动图片（获取活动成功后运行）
					//clickBtn.setText(LOAD_ACTIVITY_PHOTO);
					//测试活动反馈（获取活动成功后运行）
					clickBtn.setText(USER_FEEDBACK);
					for(ObjActivity activity : objects){
						ActivityBean bean1 = new ActivityBean();
						bean1.setActivity(activity);
						if(activity.getObjectId().equals("55e2b25f60b2719ea5e22c96")){
							bean = bean1;
							activityItem = bean.getActivity();
						}
					}
					//查询是否点赞（因测试活动照片，注释此行）
					//bean.queryFavor(activityItem, handler);
					String imgUrl = activityItem.getActivityCover().getUrl();
					int praiseCount = activityItem.getPraiseCount();
					int boyAndGirl = activityItem.getOrderCountGirl();
					String statusStr = ObjActivity.getStatusStr(activityItem.getStatus());
					String title = activityItem.getTitle();
					String lacationTitle = activityItem.getLocationAddress();
					String timeStart = activityItem.getTimeStart()+"";
					//活动详情内容网页
					String actyContent = activityItem.getActivityContent().getUrl();
					//封面展示图片列表
					queryActyCovers(activityItem);
					//参与用户列表 参与并且我关注的人数
					queryOrderUsers(activityItem);

					bigImg.setText(imgUrl);
					favrCout.setText("favor  "+String.valueOf(praiseCount));
					joinTv.setText("join  "+String.valueOf(boyAndGirl));
					statusTv.setText("status  "+statusStr);
					titleTv.setText("title  "+title);
					addressTv.setText("address  "+lacationTitle);
					timeTv.setText("startTime  "+timeStart);
					contentTv.setText("content  "+actyContent);
				}
			}
		});
	}
	//查询活动封面图列表 setActyCovers
		public void queryActyCovers(ObjActivity activity){
			ObjActivityCoverWrap.queryActivityCover(activity, new ObjActivityCoverCallback() {

				@Override
				public void callback(List<ObjActivityCover> objects, AVException e) {
					// TODO Auto-generated method stub
					ArrayList<ObjActivityCover> coverList = new ArrayList<ObjActivityCover>();
					if(e != null){
						return ;
					}
					if(objects != null && objects.size()>0){
						coverList.addAll(objects);
						actyCoverTv.setText("cover:"+String.valueOf(coverList.size()));
					}
				}
			});
		}
		//查询参加活动列表  setOrderUsers
		public void queryOrderUsers(final ObjActivity activity){
			ObjActivityOrderWrap.queryActivitySignUp(activity, new ObjUserCallback() {

				@Override
				public void callback(List<ObjUser> objects, AVException e) {
					// TODO Auto-generated method stub
					if(e != null){
						return ;
					}
					if(objects != null){
						userList.addAll(objects);
						if(userList.size()>0){
							queryFollowAndOrder(activity, handler);
							if(userList.size()>0){
								orderUserTv.setText("users"+userList.size()+userList.get(0).getProfileClip().getUrl());
							}else{
								orderUserTv.setText("users"+userList.size());
							}
						}else{
							bean.setOrderAndFollow(0);
							int followCount = bean.getOrderAndFollow();
							followTv.setText("follow  "+String.valueOf(followCount));
						}
					}
				}
			});

		}
		//获取参加活动并且我关注的人  setOrderAndFollow
		public void queryFollowAndOrder(ObjActivity activity,final Handler handler){
			ArrayList<ObjUser> followUsers = new ArrayList<ObjUser>();
			ObjFollowWrap.myFollow(userList, userSign, new ObjFunObjectsCallback() {
				
				@Override
				public void callback(List<AVObject> objects, AVException e) {
					// TODO Auto-generated method stub
					if(e != null){
						return ;
					}
					if(objects == null){
						Log.d("mytest", "obj null");
						bean.setOrderAndFollow(0);
					}else{
						bean.setOrderAndFollow(objects.size());
					}
					int followCount = bean.getOrderAndFollow();
					followTv.setText("follow  "+String.valueOf(followCount));
				}
			});
		}
	//获取活动报名信息
	boolean isFirstLoad = false;
	boolean isSecondLoad = false;
	ObjUser userSign = AVUser.cast(ObjUser.getCurrentUser(), ObjUser.class);
	private void getInfo(ObjActivity activitySign) {
		//查询是否报名
		ObjActivityWrap.queryUserJoin(activitySign, userSign, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				isFirstLoad = true;
				if(isFirstLoad && isSecondLoad){
					clickBtn.setText(SIGN_UP);
				}
				if(e != null){
					return ;
				}
				if(result){
					firstTv.setText("已参加");
					isJoin = true;
				}else{
					firstTv.setText("未参加");
					isJoin = false;
				}
			}
		});
		//获取票种信息
		ObjActivityWrap.queryTicket(activitySign, new ObjTicketCallback() {

			@Override
			public void callback(List<ObjActivityTicket> objects, AVException e) {
				// TODO Auto-generated method stub
				isSecondLoad = true;
				if(isFirstLoad && isSecondLoad){
					clickBtn.setText(SIGN_UP);
				}
				if(e != null){
					Toast.makeText(getApplicationContext(), e.getMessage(), 1000).show();
					return ;
				}
				if(objects != null && objects.size()>0){
					tickets.addAll(objects);
					String ss = tickets.get(0).getTicketTitle();
					secondTv.setText(ss);
				}
			}
		});

	}
	//活动报名
	protected void sinUp(ObjActivity activitySign) {
		// TODO Auto-generated method stub
		if(isJoin){
			Toast.makeText(TestActivity.this, "您已参加此活动", 1000).show();
			return ;
		}
		ObjActivityOrderWrap.signUpActivity(activitySign, userSign, tickets.get(0), Constants.OrderStatusPaySuccess, "hello", new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOAD_SUC);
					Toast.makeText(TestActivity.this, "报名失败", 1000).show();
					return;
				}else{
					clickBtn.setText(LOAD_FAIL);
				}
			}
		});
	}
	//活动点赞
	public void praiseActivity(ObjActivity acty){
		ObjPraiseWrap.praiseActivity(userSign, acty, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOAD_FAIL);
					return ;
				}
				if(result){
					clickBtn.setText(LOAD_SUC);
					favorImg.setVisibility(View.VISIBLE);
					clickBtn.setText(CANCEL_PRAISE_ACTY);
				}else{
					clickBtn.setText(LOAD_FAIL);
				}
			}
		});
	}
	//活动取消点赞
	public void cancelPraiseActivity(ObjActivity acty){
		ObjPraiseWrap.cancelPraiseActivity(userSign, acty, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOAD_FAIL);
					return ;
				}
				if(result){
					clickBtn.setText(LOAD_SUC);
					favorImg.setVisibility(View.GONE);
					clickBtn.setText(PRAISE_ACTIVITY);
				}else{
					clickBtn.setText(LOAD_FAIL);
				}
			}
		});
	}
	//获取活动照片
	public void loadActvityPhoto(ObjActivity acty){
		ObjActivityPhotoWrap.queryActivityPhotos(acty, new ObjActivityPhotoCallback() {

			@Override
			public void callback(List<ObjActivityPhoto> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOAD_FAIL);
					return ;
				}
				if(objects != null && objects.size()>0){
					photoBean = objects.get(0);
					firstTv.setText(photoBean.getPhoto().getUrl());
					//查询是否对照片点赞
					ObjActivityPhotoWrap.queryPhotoPraise(photoBean, userSign, new ObjFunBooleanCallback() {

						@Override
						public void callback(boolean result, AVException e) {
							// TODO Auto-generated method stub
							if(e != null){
								clickBtn.setText(LOAD_FAIL);
								return ;
							}
							if(result){
								photoFavor.setVisibility(View.VISIBLE);
								clickBtn.setText(CANCEL_PRAISE_ACTY_PHOTO);
							}else{
								photoFavor.setVisibility(View.GONE);
								clickBtn.setText(PRAISE_ACTY_PHOTO);
							}
						}
					});
				}
			}
		});
	}
	//对活动照片点赞
	public void praiseActivityPhoto(ObjActivityPhoto photo){
		ObjActivityPhotoWrap.praiseActivityPhoto(photo, userSign, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOAD_FAIL);
					return ;
				}
				if(result){
					photoFavor.setVisibility(View.VISIBLE);
					clickBtn.setText(CANCEL_PRAISE_ACTY_PHOTO);
				}else{
					clickBtn.setText(LOAD_FAIL);
				}
			}
		});
	}
	//对活动照片取消赞
	public void cancelPraiseActtyPhoto(ObjActivityPhoto photo){
		ObjActivityPhotoWrap.cancelPraiseActivityPhoto(userSign, photo, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOAD_FAIL);
					return ;
				}
				if(result){
					photoFavor.setVisibility(View.GONE);
					clickBtn.setText(PRAISE_ACTY_PHOTO);
				}else{
					clickBtn.setText(LOAD_FAIL);
				}
			}
		});
	}
	//活动反馈
	public void activityFeedback(ObjActivity acty){
		ObjActivityWrap.activityFeedBack(acty, userSign, "hello error", new ObjFunBooleanCallback() {
			
			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOAD_FAIL);
					return ;
				}else{
					clickBtn.setText(LOAD_SUC);
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
					clickBtn.setText(LOAD_SUC);
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
					clickBtn.setText(LOAD_SUC);
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
										clickBtn.setText(LOAD_SUC);
										Toast.makeText(getApplicationContext(), "save suc", 1000).show();
									}else{
										clickBtn.setText(LOAD_FAIL);
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
