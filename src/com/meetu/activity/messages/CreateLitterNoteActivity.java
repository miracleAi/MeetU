package com.meetu.activity.messages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.meetu.cloud.callback.ObjAvimclientCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjScripInfoCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjScrip;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjScriptWrap;
import com.meetu.cloud.wrap.ObjUserPhotoWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.myapplication.MyApplication;
import com.meetu.tools.UriToBitmap;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateLitterNoteActivity extends Activity implements
OnClickListener {

	// 控件相关
	private ImageView photoUpdate, uploadAgain, photo;
	private EditText updateText;
	private TextView textsize;// 输入的文字数量
	private RelativeLayout upLayout, backLayout, sendLayout;
	private RelativeLayout photoUpdateLayout, photoLayout;
	private ImageView userHeadPhoto;
	private TextView userName;
	// 网络数据相关
	private String photoPath = "";
	ObjUser user = new ObjUser();
	AVUser currentUser = ObjUser.getCurrentUser();

	private String userId;// 发给谁的

	private boolean isSend = false;// 用来记录是否在网络处理中 false 可以点击 true 不可点击

	private FinalBitmap finalBitmap;
	private ProgressBar noteProgre;
	boolean isUpEnd = true;
	public static final File FILE_SDCARD = Environment
			.getExternalStorageDirectory();
	public static final File FILE_LOCAL = new File(FILE_SDCARD, "meetu");
	Uri imageUri;//The Uri to store the big bitmap
	Bitmap headerPortait;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_create_litter_note);

		if (currentUser != null) {
			user = AVUser.cast(currentUser, ObjUser.class);
		}

		MyApplication app = (MyApplication) this.getApplicationContext();
		finalBitmap = app.getFinalBitmap();

		userId = getIntent().getStringExtra("userId");
		log.e("zcq", "userId==" + userId);
		initView();
		// 生成裁剪保存目录
		if (!FILE_LOCAL.exists()) {
			FILE_LOCAL.mkdirs();
		}
		getUserInfo(userId);
	}

	private void initView() {
		noteProgre = (ProgressBar) findViewById(R.id.noteup_progress_bar);
		photoUpdate = (ImageView) super
				.findViewById(R.id.photoUpdate_create_note_img);
		photoUpdate.setOnClickListener(this);
		sendLayout = (RelativeLayout) super
				.findViewById(R.id.send_notes_top_rl);
		sendLayout.setOnClickListener(this);
		uploadAgain = (ImageView) super
				.findViewById(R.id.uploadAgain_create_note_img);
		uploadAgain.setOnClickListener(this);
		photoUpdateLayout = (RelativeLayout) super
				.findViewById(R.id.photoUpdate_creation_note_rl);
		photoLayout = (RelativeLayout) findViewById(R.id.photo_creationNote_rl);
		photo = (ImageView) super.findViewById(R.id.photo_creation_note_img);
		updateText = (EditText) findViewById(R.id.Create_note_text_et);
		updateText.addTextChangedListener(watcher);
		textsize = (TextView) findViewById(R.id.textsize_CreationChat_tv);
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_notes_top_rl);
		backLayout.setOnClickListener(this);
		userHeadPhoto = (ImageView) findViewById(R.id.photoHead_notes_top_create_img);
		userName = (TextView) findViewById(R.id.user_name_create_note_tv);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.photoUpdate_create_note_img:
			//	showDialog();
			Intent intent1 = new Intent(Intent.ACTION_PICK, null);
			intent1.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent1, 11);
			break;
		case R.id.send_notes_top_rl:
			// fasong

			if (isSend == false) {
				if (updateText.getText().length() <= 0) {
					Toast.makeText(this, "请填写想说的话", Toast.LENGTH_SHORT).show();
				} else if (photoPath.equals("")) {
					Toast.makeText(this, "请上传背景图片", Toast.LENGTH_SHORT).show();
				} else {
					if(isUpEnd){
						isUpEnd = false;
						createGroup();
						isSend = true;
					}else{
						Toast.makeText(this, "图片还没有上传完成哦", Toast.LENGTH_SHORT).show();
					}
				}
			}
			break;
		case R.id.uploadAgain_create_note_img:
			// 重新上传
			Intent intent2 = new Intent(Intent.ACTION_PICK, null);
			intent2.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent2, 11);
			break;
		case R.id.back_notes_top_rl:
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
								.getExternalStorageDirectory(),
								"/photo_note.png")));
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case 11:
			if (resultCode == this.RESULT_OK) {
				File f = new File(FILE_LOCAL, String.valueOf(System.currentTimeMillis())+".png");
				imageUri = Uri.fromFile(f);
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		case 22:
			if (resultCode == this.RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/photo_note.png");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}

			break;
		case 33:
			log.e("图片处理", "aaa");
			/*if (data != null) {
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
			}*/
			if(imageUri != null){
				if(headerPortait != null && !headerPortait.isRecycled()){
					headerPortait.recycle();
					headerPortait = null;
				}
				log.e("图片处理", "bbb");
				try {
					headerPortait = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
					if (headerPortait != null) {
						photoPath = saveHeadImg(headerPortait);
						photo.setImageBitmap(headerPortait);
						photoUpdateLayout.setVisibility(View.GONE);
						photoUpdateLayout.setFocusable(false);
						photoLayout.setVisibility(View.VISIBLE);
						uploadAgain.setFocusable(true);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return;
				}catch (OutOfMemoryError e) {
					// TODO: handle exception
					return;
				}
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private Bitmap decodeUriAsBitmap(Uri uri){
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}catch (OutOfMemoryError e) {
			// TODO: handle exception
			return null;
		}
		return bitmap;
	}
	/**
	 * 把要上传的图片存到本地sd卡上
	 * 
	 * @param photo
	 */
	public String saveHeadImg(Bitmap photo) {
		FileOutputStream fos = null;
		String path = "";
		path = Environment.getExternalStorageDirectory() + "/photo_note.png";
		try {
			fos = new FileOutputStream(new File(
					Environment.getExternalStorageDirectory()
					+ "/photo_note.png"));
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
		intent.putExtra("aspectX", 1.2);
		intent.putExtra("aspectY", 1);
		// // outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 928);
		intent.putExtra("outputY", 775);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, 33);
	}

	// 创建小纸条
	public void createScript() {

		ObjUser receiver = null;
		try {
			receiver = ObjUser.createWithoutData(ObjUser.class, userId);
			ObjScrip scrip = new ObjScrip();
			scrip.setSender(user);
			scrip.setReceiver(receiver);
			scrip.setContentImage(groupf);
			scrip.setContentText("" + updateText.getText().toString());
			List<String> list = new ArrayList<String>();
			list.add(user.getObjectId());
			list.add(receiver.getObjectId());
			scrip.setM(list);
			ObjScriptWrap.createScrip(scrip, new ObjScripInfoCallback() {

				@Override
				public void callback(ObjScrip scrip, AVException e) {
					// TODO Auto-generated method stub
					if (e != null) {
						// clickBtn.setText(LOADFAIL);
						log.e("zcq", e);
						Toast.makeText(getApplicationContext(), "发送失败",
								Toast.LENGTH_SHORT).show();

						return;
					} else {
						// clickBtn.setText(LOADSUC);
						// scripCurrent = scrip;
						Toast.makeText(getApplicationContext(), "发送成功",
								Toast.LENGTH_SHORT).show();
						finish();
						// Intent intent=new
						// Intent(CreateLitterNoteActivity.this,LitterNoteActivity.class);
						// startActivity(intent);
					}
				}
			});
		} catch (AVException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// 创建群
	AVFile groupf = null;

	public void createGroup() {
		Toast.makeText(this, "正在创建，求稍等", Toast.LENGTH_SHORT).show();
		log.e("zcq", "正在创建，求稍等");
		try {
			groupf = AVFile.withAbsoluteLocalPath("script" + user.getObjectId()
					+ Constants.IMG_TYPE, photoPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 上传图片
		noteProgre.setVisibility(View.VISIBLE);
		groupf.saveInBackground(new SaveCallback() {

			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if(e == null){
					noteProgre.setProgress(0);
					noteProgre.setVisibility(View.GONE);
					createScript();
				}else{
					noteProgre.setProgress(0);
					noteProgre.setVisibility(View.GONE);
					Toast.makeText(getApplicationContext(), "上传失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		}, new ProgressCallback() {

			@Override
			public void done(Integer progress) {
				// TODO Auto-generated method stub
				noteProgre.setProgress(progress);
			}
		});
	}

	/**
	 * 根据创建者用户id 获取用户相关信息
	 * 
	 * @param objId
	 * @author lucifer
	 * @date 2015-11-17
	 */
	private void getUserInfo(String objId) {
		ObjUserWrap.getObjUser(objId, new ObjUserInfoCallback() {

			@Override
			public void callback(ObjUser user, AVException e) {

				if (user.getProfileClip() != null) {
					finalBitmap.display(userHeadPhoto, user.getProfileClip()
							.getUrl());
				}
				userName.setText(user.getNameNick());
				if (user.getGender() == 1) {

					// 根据性别设置图片
					Drawable nav_up = getResources().getDrawable(
							R.drawable.acty_joinlist_img_male);
					nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
							nav_up.getMinimumHeight());
					userName.setCompoundDrawables(null, null, nav_up, null);
				}

			}
		});
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(headerPortait != null){
			headerPortait.recycle();
			headerPortait = null;
		}
	}
}
