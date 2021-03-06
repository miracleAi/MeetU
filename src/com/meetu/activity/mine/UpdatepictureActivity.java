package com.meetu.activity.mine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserPhotoWrap;
import com.meetu.common.Constants;
import com.meetu.entity.Middle;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UpdatepictureActivity extends Activity implements OnClickListener {
	private EditText updateText;
	private TextView textsize, quxiao, wancheng;
	private ImageView picture;
	private AVUser currentUser = AVUser.getCurrentUser();
	// 当前用户
	private ObjUser user = new ObjUser();
	// 照片存在本地的路径
	private String userUpPhotoUri;
	private Bitmap upPhoto;
	private Boolean isEnd = false;// 用来标示 照片是否上传完成
	private RelativeLayout okLayout,backLayout;
	private ProgressBar mineProgre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_updatepicture);
		if (currentUser != null) {
			// 强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		initView();
		
		update();
	}

	private void initView() {
		mineProgre = (ProgressBar) findViewById(R.id.mineup_progress_bar);
		updateText = (EditText) super.findViewById(R.id.update_text_mine);
		updateText.addTextChangedListener(watcher);
		textsize = (TextView) super.findViewById(R.id.textsize_update_mine);
		picture = (ImageView) super.findViewById(R.id.update_picture_mine);
	
		wancheng = (TextView) super.findViewById(R.id.wancheng_update_mine_tv);
		
		
		okLayout=(RelativeLayout) findViewById(R.id.wancheng_update_mine_rl);
		okLayout.setOnClickListener(this);
		backLayout=(RelativeLayout) findViewById(R.id.back_update_mine_rl);
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

	public void update() {
		ContentResolver resolver = getContentResolver();
		// headerPortait=BitmapCut.toRoundBitmap(headerPortait);
		if (Middle.bimaBitmap != null) {

			userUpPhotoUri = saveHeadImg(Middle.bimaBitmap);
			// 获得要上传的图片的bitmap
			upPhoto = Middle.bimaBitmap;
			picture.setImageBitmap(Middle.bimaBitmap);
			upLoadUserPhoto(user);

		}
	}

	/**
	 * 将bitmap 存到本地
	 * 
	 * @param head
	 * @author lucifer
	 * @date 2015-11-9
	 */
	public String saveHeadImg(Bitmap head) {
		FileOutputStream fos = null;
		String path = "";
		path = Environment.getExternalStorageDirectory() + "/user_upphoto.png";
		try {
			fos = new FileOutputStream(new File(
					Environment.getExternalStorageDirectory()
							+ "/user_upphoto.png"));
			head.compress(CompressFormat.PNG, 100, fos);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return path;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_update_mine_rl:
			finish();
			break;
		// 上传图片的相关信息
		case R.id.wancheng_update_mine_rl:
			// Toast.makeText(this, "进行上传", Toast.LENGTH_SHORT).show();
			if (isEnd == true) {
				if(updateText.getText().length()>0){
					upLoadUserPhotoInfo(user);
				}else{
					Toast.makeText(getApplicationContext(), "请填写描述文字", Toast.LENGTH_SHORT).show();
				}
				

				
			} else {
				Toast.makeText(this, "请等照片上传结束后再点击", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}

	}

	AVFile userf = null;

	// 上传用户照片
	public void upLoadUserPhoto(final ObjUser user) {
		try {
			userf = AVFile.withAbsoluteLocalPath(
					"ObjUserPhoto" + user.getObjectId() + Constants.IMG_TYPE,
					userUpPhotoUri);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mineProgre.setVisibility(View.VISIBLE);
		userf.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if(e == null){
					mineProgre.setProgress(0);
					mineProgre.setVisibility(View.GONE);
					isEnd = true;
				}else{
					mineProgre.setProgress(0);
					mineProgre.setVisibility(View.GONE);
					Toast.makeText(UpdatepictureActivity.this, "上传照片失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		}, new ProgressCallback() {
			
			@Override
			public void done(Integer progress) {
				// TODO Auto-generated method stub
				mineProgre.setProgress(progress);
			}
		});
	}

	/**
	 * 上传图片的相关信息
	 * 
	 * @param user
	 * @author lucifer
	 * @date 2015-11-9
	 */
	public void upLoadUserPhotoInfo(final ObjUser user) {
		int wedth = upPhoto.getWidth();
		int height = upPhoto.getHeight();

		ObjUserPhotoWrap.addUserPhoto(user, upPhoto, userf, updateText
				.getText().toString(), height, wedth,
				new ObjFunBooleanCallback() {

					@Override
					public void callback(boolean result, AVException e) {
						// TODO Auto-generated method stub
						if (e != null) {
							// clickBtn.setText(LOAD_FAIL);
							Toast.makeText(UpdatepictureActivity.this,
									"上传失败", Toast.LENGTH_SHORT).show();
							log.e("zcq", "上传照片失败");
						}
						if (result) {
							// clickBtn.setText(LOAD_SUC);
							Toast.makeText(UpdatepictureActivity.this,
									"照片上传成功", Toast.LENGTH_SHORT).show();
							log.e("zcq", "上传照片 信息 成功");
							Intent intent = getIntent();
							setResult(RESULT_OK, intent);
							finish();
						} else {
							// clickBtn.setText(LOAD_FAIL);
							Toast.makeText(UpdatepictureActivity.this,
									"发表失败", Toast.LENGTH_SHORT).show();
							log.e("zcq", "上传照片失败");
						}
					}
				});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		this.setResult(RESULT_CANCELED, intent);
		finish();
		super.onBackPressed();
	}

}
