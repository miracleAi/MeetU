package com.meetu.activity;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.CloseJianpan;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ForgetPasswardActivity extends Activity implements OnClickListener {
	// 控件相关
	private ImageView back;
	private Button registerButton;
	private EditText uphone, upassward;
	private ImageView imgCloseBg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_forget_passward);
		initView();
	}

	private void initView() {
		back = (ImageView) super
				.findViewById(R.id.back_login_ForgetPassward_img);
		back.setOnClickListener(this);

		uphone = (EditText) super
				.findViewById(R.id.phone_login_ForgetPassward_et);
		upassward = (EditText) super
				.findViewById(R.id.passward_login_ForgetPassward_et);
		uphone.addTextChangedListener(watcher);
		upassward.addTextChangedListener(watcher);
		registerButton = (Button) super
				.findViewById(R.id.next_bt_forgetpassward);
		registerButton.setOnClickListener(this);
		imgCloseBg=(ImageView) findViewById(R.id.img_forget_bg);
		imgCloseBg.setOnClickListener(this);

	};

	/**
	 * Editview 输入框监听事件
	 */

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (uphone.getText().length() != 0
					&& upassward.getText().length() != 0) {
				registerButton
						.setBackgroundResource(R.drawable.register_login_720);
			} else {
				registerButton
						.setBackgroundResource(R.drawable.register_login_1_720);
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_login_ForgetPassward_img:
			finish();
		case R.id.img_forget_bg:
			CloseJianpan.closeKeyboard(this, uphone);
			break;
		case R.id.next_bt_forgetpassward:

			if (uphone.getText().length() != 11) {
				Toast.makeText(ForgetPasswardActivity.this, "手机号码格式错误",
						Toast.LENGTH_SHORT).show();
			} else if (upassward.getText().length() < 6) {
				Toast.makeText(ForgetPasswardActivity.this, "密码不能小于6位",
						Toast.LENGTH_SHORT).show();
			}else {
				// Toast.makeText(ForgetPasswardActivity.this, "可以发送数据了",
				// Toast.LENGTH_SHORT).show();

				initRegist(uphone.getText().toString());

			}

			break;

		default:
			break;
		}
	}

	/**
	 * 修改密码 验证手机号
	 * 
	 * @author lucifer
	 * @date 2015-11-5
	 */
	private void initRegist(String phone) {
		// TODO Auto-generated method stub
		log.e("lucifer", "phone=" + phone);
		ObjUserWrap.requestSmsCodeForResetPasswd(phone,
				new ObjFunBooleanCallback() {

					@Override
					public void callback(boolean result, AVException e) {
						// TODO Auto-generated method stub

						if (result == true) {
							Intent intent = new Intent(
									ForgetPasswardActivity.this,
									ForgetPasswordVerificationActivity.class);
							intent.putExtra("uphone", uphone.getText()
									.toString());
							intent.putExtra("upassward", upassward.getText()
									.toString());
							startActivity(intent);
						} else if (result == false) {
							log.e("lucifer", "充值密码异常==" + e);
							Toast.makeText(ForgetPasswardActivity.this,
									"手机号不存在", Toast.LENGTH_SHORT).show();
						}

					}
				});

	}

}
