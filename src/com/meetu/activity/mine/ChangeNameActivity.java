package com.meetu.activity.mine;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.meetu.MainActivity;
import com.meetu.activity.SetPersonalInformation2Activity;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeNameActivity extends Activity implements OnClickListener {
	// 控件相关
	private TextView queding;
	private EditText nameEditText;
	private String name;
	private ImageView backImageView;
	private RelativeLayout backLayout, quedingLayout;
	private TextView nameNumser;//名字数量

	// 网络数据 相关
	// 拿本地的 user
	private AVUser currentUser = AVUser.getCurrentUser();
	private ObjUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_change_name);

		// 拿到本地的缓存对象 user
		if (currentUser != null) {
			// 强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}

		name = super.getIntent().getStringExtra("name");

		queding = (TextView) super
				.findViewById(R.id.mine_changename_wancheng_bt);
		queding.setOnClickListener(this);

		nameEditText = (EditText) findViewById(R.id.name_changname_et);
		nameEditText.setText(name);
		
		nameEditText.setOnClickListener(this);

		backImageView = (ImageView) super
				.findViewById(R.id.back_changename_mine);
		backImageView.setOnClickListener(this);
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_changename_mine_rl);
		quedingLayout = (RelativeLayout) super
				.findViewById(R.id.mine_changename_wancheng_rl);
		backLayout.setOnClickListener(this);
		quedingLayout.setOnClickListener(this);
		nameNumser=(TextView) findViewById(R.id.textSi_change_name_tv);
		
		nameEditText.addTextChangedListener(textWatcher);
		nameNumser.setText(""+name.length());
	}
	public TextWatcher textWatcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			nameNumser.setText(""+arg0.toString().length());
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 点击确定
		case R.id.mine_changename_wancheng_rl:
			completeInfo(user);

			break;
		case R.id.back_changename_mine_rl:
			Intent intent2 = new Intent();
			intent2.putExtra("name", name);
			ChangeNameActivity.this.setResult(RESULT_CANCELED, intent2);
			finish();
		default:
			break;
		}

	}

	/**
	 * 设置点击返回键的状态
	 */
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("name", name);
		ChangeNameActivity.this.setResult(RESULT_CANCELED, intent);
		finish();
	}

	/**
	 * 上传 修改信息
	 * 
	 * @param user
	 * @author lucifer
	 * @date 2015-11-6
	 */
	public void completeInfo(final ObjUser user) {

		user.setNameNick(nameEditText.getText().toString());

		// 只上传信息
		ObjUserWrap.completeUserInfo(user, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				if (result) {
					Toast.makeText(getApplicationContext(), "已保存", 1000)
							.show();
					Intent intent = new Intent();
					intent.putExtra("name", nameEditText.getText().toString());
					ChangeNameActivity.this.setResult(RESULT_OK, intent);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "保存失败", 1000)
							.show();
				}
			}
		});
	}

}
