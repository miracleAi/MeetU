package com.meetu.activity.mine;

import java.io.Serializable;
import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.avos.avoscloud.AVException;
import com.meetu.bean.UserBean;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.cloud.utils.DateUtils;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.UserDao;

import cc.imeetu.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FavorPhotoScanActivity extends Activity implements OnClickListener{
	private RelativeLayout backLayout, deletLayout;
	private TextView dateTv;
	TextView descTv;
	TextView favorNumberTv;
	ImageView photoImv;
	TextView nameTv;
	ImageView userAvatorImv;
	RelativeLayout favorLayout;
	ImageView favorImv;
	ObjUserPhoto photo = new ObjUserPhoto();
	private UserDao userDao;
	private FinalBitmap finalBitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favor_photo_scan_layout);
		userDao = new UserDao(this);
		MyApplication app = (MyApplication) getApplicationContext();
		finalBitmap = app.getFinalBitmap();
		initView();
		if(getIntent().getSerializableExtra("photo")!=null){
			photo = (ObjUserPhoto) getIntent().getSerializableExtra("photo");
			exeData();
		}
		
	}
	private void exeData() {
		// TODO Auto-generated method stub
		if(photo.getPraiseCount() == 0){
			favorImv.setImageResource(R.drawable.mine_photoview_fullscreen_btn_like_nor);
		}else{
			favorImv.setImageResource(R.drawable.mine_photoview_fullscreen_btn_like_hl);
		}
		finalBitmap.display(photoImv, photo.getPhoto().getUrl());
		dateTv.setText(DateUtils.format(photo.getCreatedAt().getTime(), DateUtils.DateFormat_Date));
		favorNumberTv.setText(""+photo.getPraiseCount());
		descTv.setText(photo.getPhotoDescription());
		ArrayList<UserBean> list = userDao.queryUser(photo.getUser().getObjectId());
		if (null != list && list.size() > 0) {
			nameTv.setText(list.get(0).getNameNick());
			finalBitmap.display(userAvatorImv, list.get(0).getProfileClip());
		}else{
			ObjUserWrap.getObjUser(photo.getUser().getObjectId(),
					new ObjUserInfoCallback() {

				@Override
				public void callback(ObjUser objuser, AVException e) {
					// TODO Auto-generated method stub
					if (e == null) {
						nameTv.setText(objuser.getNameNick());
						finalBitmap.display(userAvatorImv, objuser.getProfileClip().getUrl());
						userDao.insertOrReplaceUser(objuser);
					}
				}
			});
		}
	}
	private void initView() {
		// TODO Auto-generated method stub
		backLayout = (RelativeLayout) findViewById(R.id.back_mine_photoview_fullscreen_rl);
		backLayout.setOnClickListener(this);
		deletLayout = (RelativeLayout) findViewById(R.id.deldect_mine_photoview_fullscreen_rl);
		deletLayout.setVisibility(View.GONE);
		deletLayout.setOnClickListener(this);
		favorLayout = (RelativeLayout) findViewById(R.id.favor_minephoto_mine_rl);
		favorLayout.setOnClickListener(this);
		dateTv = (TextView) findViewById(R.id.date_tv);
		descTv = (TextView) findViewById(R.id.desc_item_minephoto_tv);
		favorNumberTv = (TextView) findViewById(R.id.favorNumber_item_minephoto);
		photoImv = (ImageView) findViewById(R.id.photo_demail_mine);
		nameTv = (TextView) findViewById(R.id.name_mine_photoview_fullscreen);
		userAvatorImv = (ImageView) findViewById(R.id.nameheader_mine_photoview_fullscreen_img);
		favorImv = (ImageView) findViewById(R.id.favor_minephoto_mine);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_mine_photoview_fullscreen_rl:
			finish();
			break;
		case R.id.deldect_mine_photoview_fullscreen_rl:
			
			break;
		case R.id.favor_minephoto_mine_rl:
			Intent intent = new Intent(FavorPhotoScanActivity.this,
					FavorListActivity.class);
			intent.putExtra("photo", (Serializable) photo);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
