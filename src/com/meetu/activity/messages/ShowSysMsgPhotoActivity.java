package com.meetu.activity.messages;

import cc.imeetu.R;

import com.lidroid.xutils.BitmapUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class ShowSysMsgPhotoActivity extends Activity {
	ImageView photoImv;
	BitmapUtils bitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sys_msg_photo_layout);
		bitmapUtils = new BitmapUtils(getApplicationContext());
		photoImv = (ImageView) findViewById(R.id.photo_demail_mine);
		String imgUrl = getIntent().getStringExtra("photoUrl");
		if (imgUrl != null) {
			bitmapUtils.display(photoImv, imgUrl);
		}
		photoImv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.zoom_exit, R.anim.zoom_exit);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.zoom_exit, R.anim.zoom_exit);
	}
	

}
