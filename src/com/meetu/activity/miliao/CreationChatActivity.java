package com.meetu.activity.miliao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.meetu.cloud.callback.ObjAvimclientCallback;
import com.meetu.cloud.callback.ObjChatBeanCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjScripInfoCallback;
import com.meetu.cloud.object.ObjChat;
import com.meetu.cloud.object.ObjScrip;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjChatWrap;
import com.meetu.cloud.wrap.ObjScriptWrap;
import com.meetu.cloud.wrap.ObjUserPhotoWrap;
import com.meetu.common.Constants;
import com.meetu.myapplication.MyApplication;
import com.meetu.tools.BitmapCut;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;

import android.R.string;
import android.annotation.SuppressLint;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class CreationChatActivity extends Activity implements OnClickListener {
	private ImageView photoUpdate, uploadAgain, photo;
	private int windowHight, windowWidth;
	private EditText updateText;
	private TextView textsize;// 输入的文字数量
	private RelativeLayout photoUpdateLayout, photoLayout;
	private int PhotoWidth, PhotoHight;
	private RelativeLayout upLayout, backLayout;
	private ProgressBar chatUpProgre;
	// 网络数据相关
	private String photoPath = "";
	ObjUser user = new ObjUser();
	AVUser currentUser = ObjUser.getCurrentUser();
	boolean isUpEnd = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.getWindow();
		setContentView(R.layout.activity_creation_chat);
		if (currentUser != null) {
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		initView();
	}

	private void initView() {
		chatUpProgre = (ProgressBar) findViewById(R.id.chatup_progress_bar);
		photoUpdateLayout = (RelativeLayout) super
				.findViewById(R.id.photoUpdate_creationChat_rl);
		// 设置上传主题图片在屏幕的1/3高度处
		RelativeLayout.LayoutParams ll = (LayoutParams) photoUpdateLayout
				.getLayoutParams();
		windowHight = DisplayUtils.getWindowHeight(this);
		// 状态栏加标题栏在本屏幕的高度
		int topHight = DensityUtil.dip2px(this, 64);
		// 图片的半径
		int imageViewRange = DensityUtil.dip2px(this, 45);
		ll.topMargin = windowHight / 3 - topHight - imageViewRange;
		photoUpdateLayout.setLayoutParams(ll);

		// 动态设置选择照片后布局的高度
		photoLayout = (RelativeLayout) super
				.findViewById(R.id.photo_creationChat_rl);
		RelativeLayout.LayoutParams params = (LayoutParams) photoLayout
				.getLayoutParams();
		// 屏幕宽度
		windowWidth = DisplayUtils.getWindowWidth(this);
		PhotoWidth = windowWidth - DensityUtil.dip2px(this, 50 + 50);
		PhotoHight = (int) (PhotoWidth / (275.00 / 258.00));
		params.height = PhotoHight;

		photoLayout.setLayoutParams(params);
		log.e("lucifer", "photoLayoutWidth=" + PhotoWidth + "params.height="
				+ params.height);

		photoUpdate = (ImageView) super
				.findViewById(R.id.photoUpdate_creationChat_img);

		photoUpdateLayout.setOnClickListener(this);

		photo = (ImageView) super.findViewById(R.id.photo_creationChat_img);

		uploadAgain = (ImageView) super
				.findViewById(R.id.uploadAgain_creationChat_img);
		uploadAgain.setOnClickListener(this);

		updateText = (EditText) super.findViewById(R.id.CreationChat_text_mine);
		updateText.addTextChangedListener(watcher);
		textsize = (TextView) super.findViewById(R.id.textsize_CreationChat_tv);

		// wancheng
		upLayout = (RelativeLayout) super
				.findViewById(R.id.wancheng_creationChat_rl);
		upLayout.setOnClickListener(this);
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_creationChat_rl);
		backLayout.setOnClickListener(this);
	}

	/**
	 * 监听editview 中输入的字符的数量 动态改变数量
	 */
	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

			textsize.setText("" + updateText.getText().length());

		}
	};

	/**
	 * 点击事件 处理
	 */

	@SuppressLint("ShowToast")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.photoUpdate_creationChat_rl:

		//	showDialog();
			Intent intent1 = new Intent(Intent.ACTION_PICK, null);
			intent1.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent1, 11);

			break;
		case R.id.uploadAgain_creationChat_img:
			// Toast.makeText(this, "重新上传", Toast.LENGTH_SHORT).show();
		//	showDialog();
			Intent intent2 = new Intent(Intent.ACTION_PICK, null);
			intent2.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent2, 11);
			break;
		case R.id.wancheng_creationChat_rl:
			if (updateText.getText().length() == 0) {
				Toast.makeText(getApplicationContext(), "请输入觅聊标题",
						Toast.LENGTH_SHORT).show();
			} else {
				// 开始创建操作
				if (photoPath.equals("")) {
					Toast.makeText(getApplicationContext(), "请选择照片",
							Toast.LENGTH_SHORT).show();
				} else {
					if(isUpEnd){
						isUpEnd = false;
						createGroup();
					}else{
						Toast.makeText(getApplicationContext(), "图片还没有上传成功哦",
								Toast.LENGTH_SHORT).show();
					}
				}

			}
			break;
		case R.id.back_creationChat_rl:
			Intent intent = getIntent();
			setResult(RESULT_CANCELED, intent);
			finish();
			break;

		default:
			break;
		}

	}

	private void showDialog() {
		final AlertDialog portraidlg = new AlertDialog.Builder(this).create();
		portraidlg.show();
		Window win = portraidlg.getWindow();
		win.setContentView(R.layout.dialog_show_photo);
		RadioButton portrait_native = (RadioButton) win
				.findViewById(R.id.Portrait_native);
		portrait_native.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent1, 11);
				portraidlg.dismiss();
			}
		});
		RadioButton portrait_take = (RadioButton) win
				.findViewById(R.id.Portrait_take);
		portrait_take.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// 调用摄像头
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory(), "/photo.png")));
				startActivityForResult(intent2, 22);
				portraidlg.dismiss();
			}
		});
		View viewTop = win.findViewById(R.id.view_top_dialog_sethead);
		View viewBottom = win.findViewById(R.id.view_bottom_dialog_sethead);
		// 点击dialog外部，关闭dialog
		viewTop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				portraidlg.dismiss();
			}
		});
		viewBottom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				portraidlg.dismiss();
			}
		});

	}

	private Bitmap headerPortait;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case 11:
			if (resultCode == this.RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		case 22:
			if (resultCode == this.RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/photo.png");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片

			}

			break;
		case 33:
			log.e("图片处理", "aaa");
			if (data != null) {
				Bundle extras = data.getExtras();
				headerPortait = extras.getParcelable("data");
				log.e("headerPortait", headerPortait.toString());
				if (headerPortait != null) {
					photoPath = saveHeadImg(headerPortait);
					photo.setImageBitmap(headerPortait);
					photoUpdateLayout.setVisibility(View.GONE);
					photoUpdateLayout.setFocusable(false);
					photoLayout.setVisibility(View.VISIBLE);
					uploadAgain.setFocusable(true);
				}
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 把要上传的图片存到本地sd卡上
	 * 
	 * @param photo
	 */
	public String saveHeadImg(Bitmap photo) {
		FileOutputStream fos = null;
		String path = "";
		path = Environment.getExternalStorageDirectory() + "/photo.png";
		try {
			fos = new FileOutputStream(new File(
					Environment.getExternalStorageDirectory() + "/photo.png"));
			photo.compress(CompressFormat.PNG, 100, fos);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} finally {

			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return path;

	}

	public Bitmap readHead() {
		String file = Environment.getExternalStorageDirectory() + "/photo.png";
		return BitmapFactory.decodeFile(file);
	}

	/**
	 * 调用拍照的裁剪功能
	 * 
	 * @param uri
	 */

	public void cropPhoto(Uri uri) {

		// 调用拍照的裁剪功能
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽和搞的比例
		intent.putExtra("aspectX", 275);
		intent.putExtra("aspectY", 258);
		// // outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 550);
		intent.putExtra("outputY", 516);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 33);
	}

	// 创建群
	AVFile groupf = null;

	public void createGroup() {
		try {
			groupf = AVFile.withAbsoluteLocalPath("chat" + user.getObjectId()
					+ Constants.IMG_TYPE, photoPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 上传图片
		chatUpProgre.setVisibility(View.VISIBLE);
		groupf.saveInBackground(new SaveCallback() {

			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					chatUpProgre.setProgress(0);
					chatUpProgre.setVisibility(View.GONE);
					if(MyApplication.isChatLogin){
						saveGroupInfo();
					}else{
						ObjChatMessage.connectToChatServer(
								MyApplication.chatClient,
								new ObjAvimclientCallback() {

									@Override
									public void callback(AVIMClient client,
											AVException e) {
										if (e != null) {
											log.e("zcq", e);
											return;
										}
										if (client != null) {
											MyApplication.chatClient = client;
											log.e("zcq", "连接聊天长连接成功");
											saveGroupInfo();
										} else {
											log.e("zcq", "连接聊天长连接失败");
										}
									}
								});
					}
				} else {
					chatUpProgre.setProgress(0);
					chatUpProgre.setVisibility(View.GONE);
					log.e("zcq", "照片上传失败");
				}
			}
		},new ProgressCallback() {
			
			@Override
			public void done(Integer pro) {
				// TODO Auto-generated method stub
				chatUpProgre.setProgress(pro);
			}
		});
	}

	// 保存群信息
	public void saveGroupInfo() {
		ObjChatWrap.saveGroupInfo(user, groupf,
				updateText.getText().toString(), new ObjChatBeanCallback() {

					@Override
					public void callback(ObjChat object, AVException e) {
						// TODO Auto-generated method stub
						if (e != null) {
							// clickBtn.setText(LOADFAIL);
							Toast.makeText(CreationChatActivity.this, "觅聊创建失败", 1000).show();
							return;
						} else {
							Toast.makeText(CreationChatActivity.this, "觅聊已创建", 1000).show();
							Intent intent = getIntent();
							setResult(RESULT_OK, intent);
							finish();
						}

					}
				});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		setResult(RESULT_CANCELED, intent);
		finish();
		super.onBackPressed();
	}

}
