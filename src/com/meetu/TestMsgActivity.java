package com.meetu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.meetu.cloud.callback.ObjAuthoriseApplyCallback;
import com.meetu.cloud.callback.ObjAuthoriseCategoryCallback;
import com.meetu.cloud.callback.ObjAvimclientCallback;
import com.meetu.cloud.callback.ObjChatBeanCallback;
import com.meetu.cloud.callback.ObjChatCallback;
import com.meetu.cloud.callback.ObjCoversationCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjFunCountCallback;
import com.meetu.cloud.callback.ObjListCallback;
import com.meetu.cloud.callback.ObjScripBoxCallback;
import com.meetu.cloud.callback.ObjScripCallback;
import com.meetu.cloud.callback.ObjScripInfoCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjAuthoriseApply;
import com.meetu.cloud.object.ObjAuthoriseCategory;
import com.meetu.cloud.object.ObjChat;
import com.meetu.cloud.object.ObjScrip;
import com.meetu.cloud.object.ObjScripBox;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.utils.ChatMsgUtils;
import com.meetu.cloud.wrap.ObjAuthoriseWrap;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjChatWrap;
import com.meetu.cloud.wrap.ObjScriptWrap;
import com.meetu.cloud.wrap.ObjUserPhotoWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.myapplication.MyApplication;
import com.meetu.tools.BitmapCut;

import android.R.array;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class TestMsgActivity extends Activity{
	private static final String AUTHORISECATEGORY = "AuthoriseCategory";
	private static final String ISAPPLY = "isApply";
	private static final String HAVEAUTHOREISE = "haveAuthorise";
	private static final String STARTAPPLY = "startApply";
	private static final String UPDATEAPPLY = "updateApply";
	private static final String LOADFAIL = "loadfail";
	private static final String LOADSUC = "loadsuc";
	private static final String LOADING = "loading";
	private static final String UPLOADPIC = "uploadPic";
	private static final String SAVEGROUP = "saveGroup";
	private static final String GETCHAT = "getChat";
	private static final String JOINGROUP = "joinGroup";
	private static final String CHATLOGOUT = "chatLogout";
	private static final String MEMBERCOUNT = "memberCount";
	private static final String MEMEBERS = "getMembers";
	private static final String MEMBERINFO = "memberInfo";
	private static final String SENDMSG = "sendmsg";
	private static final String SENDPICMSG = "sendPicmsg";
	private static final String CREATESCRIP = "createScrip";
	private static final String GETSCRIPS = "getScrips";
	private static final String GETSCRIPBOXS = "getScripBoxs";
	ImageView upImg;
	Button clickBtn;
	TextView countTv,infoTv;
	AVIMConversation chatConversation;
	//当前觅聊会话ID
	String conversationId;
	//会话成员列表
	ArrayList<String> memberList = new ArrayList<String>();
	//权限
	ObjAuthoriseCategory category = new ObjAuthoriseCategory();
	//权限申请
	ObjAuthoriseApply apply = new ObjAuthoriseApply();
	ObjUser user = new ObjUser();
	String fPath = "";
	String yPath = "";
	//需上传的用户照片
	Bitmap groupPhoto;
	//当前小纸条
	ObjScrip scripCurrent = null;
	//纸条箱
	ObjScripBox box = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_msg_layout);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		upImg = (ImageView) findViewById(R.id.up_img);
		countTv = (TextView) findViewById(R.id.count_tv);
		infoTv = (TextView) findViewById(R.id.info_tv);
		clickBtn = (Button) findViewById(R.id.click);
		AVUser currentUser = ObjUser.getCurrentUser();
		user = AVUser.cast(currentUser, ObjUser.class);
		Bitmap head=readHead();
		if(head!=null){
			groupPhoto = head;
			fPath = Environment.getExternalStorageDirectory()+"/f_user_photo.png";
			yPath = Environment.getExternalStorageDirectory()+"/user_photo.png";
			upImg.setImageBitmap(head);
		}
		upImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
		//clickBtn.setText(AUTHORISECATEGORY);
		//clickBtn.setText(CHATLOGOUT);
		//clickBtn.setText(JOINGROUP);
		//clickBtn.setText(MEMBERCOUNT);
		//clickBtn.setText(SENDPICMSG);
		//clickBtn.setText(UPLOADPIC);
		clickBtn.setText(GETSCRIPBOXS);
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
				if(clickBtn.getText().toString().equals(UPLOADPIC)){
					//创建群聊：上传群照片，创建群，群信息插入觅聊表
					clickBtn.setText(LOADING);
					createGroup();
				}
				/*if(clickBtn.getText().toString().equals(CREATEGROUP)){
					//创建群聊：上传群照片，创建群，群信息插入觅聊表
					clickBtn.setText(LOADING);
					createChat();
				}*/
				if(clickBtn.getText().toString().equals(SAVEGROUP)){
					//创建群聊：上传群照片，群信息插入觅聊表
					clickBtn.setText(LOADING);
					saveGroupInfo();
				}
				if(clickBtn.getText().toString().equals(GETCHAT)){
					//创建群聊：上传群照片群信息插入觅聊表
					clickBtn.setText(LOADING);
					getChatGroup();
				}
				if(clickBtn.getText().toString().equals(JOINGROUP)){
					//加入当前觅聊
					clickBtn.setText(LOADING);
					joinGroup();
				}
				if(clickBtn.getText().toString().equals(MEMBERCOUNT)){
					//会话成员数
					clickBtn.setText(LOADING);
					getChatMembCount();
				}
				if(clickBtn.getText().toString().equals(MEMEBERS)){
					//会话成员
					clickBtn.setText(LOADING);
					getMembers();
				}
				if(clickBtn.getText().toString().equals(MEMBERINFO)){
					//会话成员信息
					clickBtn.setText(LOADING);
					getMemberInfo();
				}
				if(clickBtn.getText().toString().equals(SENDMSG)){
					//发送信息
					clickBtn.setText(LOADING);
					sendMsg(false);
				}
				if(clickBtn.getText().toString().equals(SENDPICMSG)){
					//发送图片信息
					clickBtn.setText(LOADING);
					sendPicmsg(false);
				}
				if(clickBtn.getText().toString().equals(CHATLOGOUT)){
					clickBtn.setText(LOADING);
					//退出聊天登录
					logoutChat();
				}
				if(clickBtn.getText().toString().equals(CREATESCRIP)){
					//创建小纸条
					clickBtn.setText(LOADING);
					createScript();
				}
				if(clickBtn.getText().toString().equals(GETSCRIPBOXS)){
					//获取纸条箱
					clickBtn.setText(LOADING);
					getScripBoxs();
				}
				if(clickBtn.getText().toString().equals(GETSCRIPS)){
					//获取纸条箱内小纸条
					clickBtn.setText(LOADING);
					getScrips(box);
				}
			}
		});
	}
	//创建小纸条
	public void createScript(){
		ObjUser receiver = null;
		try {
			receiver = ObjUser.createWithoutData(ObjUser.class, "55d050e600b0de09f8a0ab89");
			ObjScrip scrip = new ObjScrip();
			scrip.setSender(user);
			scrip.setReceiver(receiver);
			scrip.setContentImage(groupf);
			scrip.setContentText("123");
			List<String> list = new ArrayList<String>();
			list.add(user.getObjectId());
			list.add(receiver.getObjectId());
			scrip.setM(list);
			ObjScriptWrap.createScrip(scrip, new ObjScripInfoCallback() {
				
				@Override
				public void callback(ObjScrip scrip, AVException e) {
					// TODO Auto-generated method stub
					if( e != null){
						clickBtn.setText(LOADFAIL);
					}else{
						clickBtn.setText(LOADSUC);
						scripCurrent = scrip;
					}
				}
			});
		} catch (AVException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	//获取所有纸条箱
	public void getScripBoxs(){
		ObjScriptWrap.queryScripBox(user, new ObjScripBoxCallback() {
			
			@Override
			public void callback(List<ObjScripBox> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return ;
				}
				if(objects.size()>0){
					box = objects.get(0);
					clickBtn.setText(GETSCRIPS);
				}else{
					clickBtn.setText(LOADFAIL);
				}
			}
		});
	}
	//获取纸条箱内所有小纸条
	public void getScrips(ObjScripBox box){
		ObjScriptWrap.queryAllScrip(box, new ObjScripCallback() {
			
			@Override
			public void callback(List<ObjScrip> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return ;
				}
				if(objects.size()>0){
					clickBtn.setText(LOADSUC);
				}else{
					clickBtn.setText(LOADFAIL);
				}
			}
		});
	}
	AVIMConversation conv = MyApplication.chatClient.getConversation("5644076600b0c060f9704638");
	//发送信息
	public void sendMsg(boolean isScrip){
		AVIMTextMessage msg = new AVIMTextMessage();
		msg.setText("你好");
		Map<String, Object> map = null;
		if(isScrip){
			map.put(Constants.SCRIP_ID,scripCurrent.getObjectId());
			map.put(Constants.SCRIP_TYPE, Constants.SCRIPT_MSG);
			map.put(Constants.SCRIP_X, 500);
			map.put(Constants.SCRIP_Y, 100);
		}else{
			//测试数据，实际为最新一条消息发送时间
			long l = System.currentTimeMillis() - 10000;
			map.put(Constants.IS_SHOW_TIME, ChatMsgUtils.isShowChatTime(l));
		}
		msg.setAttrs(map);
		ObjChatMessage.sendChatMsg(conv,msg , new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return ;
				}
				if(result){
					clickBtn.setText(LOADSUC);
				}else{
					clickBtn.setText(LOADFAIL);
				}
			}
		});
	}
	//发送图片消息
	public void sendPicmsg(boolean isScrip){
		AVIMImageMessage msg;
		try {
			msg = new AVIMImageMessage(fPath);
			Map<String, Object> map = null;
			if(isScrip){
				map.put(Constants.SCRIP_ID,scripCurrent.getObjectId());
				map.put(Constants.SCRIP_TYPE, Constants.SCRIPT_MSG);
				map.put(Constants.SCRIP_X, 500);
				map.put(Constants.SCRIP_Y, 100);
			}else{
				//测试数据，实际为最新一条消息发送时间
				long l = System.currentTimeMillis() - 10000;
				map.put(Constants.IS_SHOW_TIME, ChatMsgUtils.isShowChatTime(l));
			}
			msg.setAttrs(map);
			ObjChatMessage.sendPicMsg(conv, msg, new ObjFunBooleanCallback() {

				@Override
				public void callback(boolean result, AVException e) {
					// TODO Auto-generated method stub
					if(e != null){
						clickBtn.setText(LOADFAIL);
						return ;
					}
					if(result){
						clickBtn.setText(LOADSUC);
					}else{
						clickBtn.setText(LOADFAIL);
					}
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
	AVFile groupf = null;
	//创建群
	public void createGroup(){
		try {
			groupf = AVFile.withAbsoluteLocalPath("chat"+user.getObjectId()+Constants.IMG_TYPE, fPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//上传图片
		ObjUserPhotoWrap.savePhoto(groupf, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return ;
				}
				if(!result){
					clickBtn.setText(LOADFAIL);
					return;

				}
				if(MyApplication.isChatLogin){
					//clickBtn.setText(SAVEGROUP);
					clickBtn.setText(CREATESCRIP);
				}else{
					ObjChatMessage.connectToChatServer(MyApplication.chatClient, new ObjAvimclientCallback() {

						@Override
						public void callback(AVIMClient client, AVException e) {
							// TODO Auto-generated method stub
							if(e != null){
								clickBtn.setText(LOADFAIL);
								return ;
							}
							if(client != null){
								MyApplication.chatClient = client;
								//clickBtn.setText(SAVEGROUP);
								clickBtn.setText(CREATESCRIP);
							}else{
								clickBtn.setText(LOADFAIL);
							}
						}
					});
				}
			}
		});
	}
	//创建会话,已改为后台创建
	/*public void createChat(){
		ObjChatMessage.createChat(MyApplication.chatClient, Arrays.asList(user.getObjectId()), "chat", new ObjCoversationCallback() {

			@Override
			public void callback(AVIMConversation conversation, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return ;
				}
				if(conversation != null){
					chatConversation = conversation;
					clickBtn.setText(SAVEGROUP);
				}else{
					clickBtn.setText(LOADFAIL);
				}
			}
		});
	}*/
	//保存群信息
	public void saveGroupInfo(){
		if(chatConversation == null){
			clickBtn.setText(LOADFAIL);
			return;
		}
		ObjChatWrap.saveGroupInfo(user, groupf, "zlp_hello", new ObjChatBeanCallback() {

			@Override
			public void callback(ObjChat object, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return ;
				}
				clickBtn.setText(GETCHAT);
			}
		});
	}
	//获取觅聊群
	public void getChatGroup(){
		ObjChatWrap.queryChatList(new ObjChatCallback() {

			@Override
			public void callback(List<ObjChat> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return ;
				}
				if(objects.size()>0){
					conversationId = objects.get(0).getConversationId();
					if(MyApplication.isChatLogin){
						clickBtn.setText(JOINGROUP);
					}else{
						ObjChatMessage.connectToChatServer(MyApplication.chatClient, new ObjAvimclientCallback() {

							@Override
							public void callback(AVIMClient client, AVException e) {
								// TODO Auto-generated method stub
								if(e != null){
									clickBtn.setText(LOADFAIL);
									return ;
								}
								if(client != null){
									MyApplication.chatClient = client;
									clickBtn.setText(JOINGROUP);
								}else{
									clickBtn.setText(LOADFAIL);
								}
							}
						});
					}
				}else{
					clickBtn.setText("no chat");
				}
			}
		});
	}
	//加入当前觅聊
	public void joinGroup(){
		ObjChatMessage.joinChat(MyApplication.chatClient, conv, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return ;
				}
				if(result){
					clickBtn.setText(LOADSUC);
				}else{
					clickBtn.setText(LOADFAIL);
				}
			}
		});
	}
	//获取会话成员数
	public void getChatMembCount(){
		ObjChatMessage.getChatCount(conv , new ObjFunCountCallback() {

			@Override
			public void callback(int count, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return ;
				}
				countTv.setText(String.valueOf(count));
				clickBtn.setText(MEMEBERS);
			}
		});
	}
	//获取会话成员
	public void getMembers(){
		ObjChatMessage.getChatMembers(conv,new ObjListCallback() {

			@Override
			public void callback(ArrayList<String> list, AVException e) {
				// TODO Auto-generated method stub
				if(list.size() == 0){
					clickBtn.setText(LOADFAIL);
					return;
				}
				for(String s:list){
					memberList.add(s);
				}
				clickBtn.setText(MEMBERINFO);
			}
		});
	}
	//获取成员信息
	public void getMemberInfo(){
		ObjUserWrap.getObjUser(memberList.get(0),new ObjUserInfoCallback() {

			@Override
			public void callback(ObjUser user, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return ;
				}
				infoTv.setText(user.getProfileClip().getUrl());
				clickBtn.setText(LOADSUC);
			}
		});
	}
	//退出聊天登录
	public void logoutChat(){
		ObjChatMessage.connectToChatServer(MyApplication.chatClient, new ObjAvimclientCallback() {

			@Override
			public void callback(AVIMClient client, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return ;
				}
				if(client != null){
					clickBtn.setText(LOADSUC);
				}else{
					clickBtn.setText(LOADFAIL);
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
					clickBtn.setText(UPLOADPIC);
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
					clickBtn.setText(UPLOADPIC);
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
				groupPhoto = headerPortait;
				if(headerPortait!=null){
					fPath = saveHeadImg(headerPortait,false);
				}
				//切圆图片
				headerPortait=BitmapCut.toRoundBitmap(headerPortait);
				if(headerPortait!=null){
					yPath = saveHeadImg(headerPortait,true);
					upImg.setImageBitmap(headerPortait);
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
