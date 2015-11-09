package com.meetu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class TestMsgActivity extends Activity{
	ImageView favorImag,upImg;
	Button clickBtn;
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
	}
}
