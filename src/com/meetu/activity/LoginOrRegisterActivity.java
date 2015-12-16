package com.meetu.activity;

import com.meetu.common.Constants;

import cc.imeetu.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginOrRegisterActivity extends Activity implements
		OnClickListener {

	private Button denglu, zhuce;
	FinishReceiver fr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_login_or_register);
		initView();
		fr = new FinishReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.LOGIN_REGISTOR_FINISH);
		registerReceiver(fr, filter);
	}

	private void initView() {
		denglu = (Button) super.findViewById(R.id.bt_denglu_dengluorzhuce);
		zhuce = (Button) super.findViewById(R.id.bt_zhuce_dengluorzhuce);
		denglu.setOnClickListener(this);
		zhuce.setOnClickListener(this);
	}
	class FinishReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			LoginOrRegisterActivity.this.finish();
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_denglu_dengluorzhuce:
			Intent intent1 = new Intent(LoginOrRegisterActivity.this,
					LoginActivity.class);
			startActivity(intent1);

			break;
		case R.id.bt_zhuce_dengluorzhuce:
			Intent intent2 = new Intent(LoginOrRegisterActivity.this,
					RegisterActivity.class);
			startActivity(intent2);
			break;

		default:
			break;
		}

	}

}
