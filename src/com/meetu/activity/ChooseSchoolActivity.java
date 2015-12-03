package com.meetu.activity;

import com.meetu.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class ChooseSchoolActivity extends Activity implements OnClickListener {

	private ImageView ivBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_school);
		initView();
	}

	private void initView() {
		ivBack = (ImageView) super.findViewById(R.id.ivBack_xuanzhexuexiao);
		ivBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ivBack_xuanzhexuexiao:
			finish();

			break;

		default:
			break;
		}

	}

}
