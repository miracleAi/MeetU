package com.meetu.activity;

import java.util.Timer;
import java.util.TimerTask;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.wrap.ObjUserWrap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPasswordVerificationActivity extends Activity implements
		OnClickListener {
	private TimerTask mTimerTask;
	private Timer mTimer = new Timer(true);
	private Button sent;
	private int i = 59;
	private Boolean running;
	private TextView number1, number2, number3, number4, number5, number6;
	private ImageView back;
	private String uphone, upassward, number;
	private TextView fasongphone, userPhoneNumber;
	private RelativeLayout quedingLayout;

	private EditText allEditText;
	private LinearLayout allLayout;
	private RelativeLayout saveLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_forget_password__verification);
		uphone = getIntent().getStringExtra("uphone");
		upassward = getIntent().getStringExtra("upassward");
		initView();
		initLoad();
	}

	/**
	 * 向发送手机验证码
	 * 
	 * @author lucifer
	 * @date 2015-11-5
	 */
	private void initLoad() {
		ObjUserWrap.requestSmsCodeForResetPasswd(uphone,
				new ObjFunBooleanCallback() {

					@Override
					public void callback(boolean result, AVException e) {
						if (result == true) {

						} else {
							log.e("failure", e);
						}

					}
				});

	}

	private void initView() {
		saveLayout=(RelativeLayout) findViewById(R.id.forget_pw_rl);
		saveLayout.setOnClickListener(this);
		sent = (Button) findViewById(R.id.sent_forgetPassword_bt);
		sent.setOnClickListener(this);

		mTimerTask = new TimerTask() {
			public void run() {

				Message message = new Message();
				message.what = 1;
				doActionHandler.sendMessage(message);
			}

		};
		mTimer.schedule(mTimerTask, 1000, 1000); // 在1秒后每1秒执行一次定时器中的方法，比如本文为调用log.v打印输出。

		number1 = (TextView) super.findViewById(R.id.one_forgetPassword_et);
		number2 = (TextView) super.findViewById(R.id.two_forgetPassword_et);
		number3 = (TextView) super.findViewById(R.id.three_forgetPassword_et);
		number4 = (TextView) super.findViewById(R.id.four_forgetPassword_et);
		number5 = (TextView) super.findViewById(R.id.five_forgetPassword_et);
		number6 = (TextView) super.findViewById(R.id.six_forgetPassword_et);

		// number1.addTextChangedListener(watcher);
		// number2.addTextChangedListener(watcher);
		// number3.addTextChangedListener(watcher);
		// number4.addTextChangedListener(watcher);
		// number5.addTextChangedListener(watcher);
		// number6.addTextChangedListener(watcher);

		back = (ImageView) super
				.findViewById(R.id.activity_forgetPassword_back_img);
		back.setOnClickListener(this);

		quedingLayout = (RelativeLayout) super
				.findViewById(R.id.activity_forgetPassword_to_forgetPasswordVerification_rl);
		quedingLayout.setOnClickListener(this);
		userPhoneNumber = (TextView) super
				.findViewById(R.id.toPhoneNumber_forgetPassword_verification_tv);

		userPhoneNumber.setText(uphone);

		allEditText = (EditText) super
				.findViewById(R.id.all_forget_Verification_et);
		allEditText.addTextChangedListener(watcher);
		allLayout = (LinearLayout) super
				.findViewById(R.id.all_number_forget_verfication_ll);
		allLayout.setOnClickListener(this);
	}

	/**
	 * 输入框的监听事件
	 */

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			// if(number1.getText().length()==1){
			//
			// number1.setBackgroundResource(R.drawable.register_ver_s_720);
			// number2.setFocusable(true);
			// number2.setFocusableInTouchMode(true);
			// number2.requestFocus();
			// number2.requestFocusFromTouch();
			// }
			// if(number2.getText().length()==1){
			//
			// number2.setBackgroundResource(R.drawable.register_ver_s_720);
			// number3.setFocusable(true);
			// number3.setFocusableInTouchMode(true);
			// number3.requestFocus();
			// number3.requestFocusFromTouch();
			// }
			// if(number3.getText().length()==1){
			//
			// number3.setBackgroundResource(R.drawable.register_ver_s_720);
			// number4.setFocusable(true);
			// number4.setFocusableInTouchMode(true);
			// number4.requestFocus();
			// number4.requestFocusFromTouch();
			// }
			// if(number4.getText().length()==1){
			//
			// number4.setBackgroundResource(R.drawable.register_ver_s_720);
			// number5.setFocusable(true);
			// number5.setFocusableInTouchMode(true);
			// number5.requestFocus();
			// number5.requestFocusFromTouch();
			// }
			// if(number5.getText().length()==1){
			//
			// number5.setBackgroundResource(R.drawable.register_ver_s_720);
			// number6.setFocusable(true);
			// number6.setFocusableInTouchMode(true);
			// number6.requestFocus();
			// number6.requestFocusFromTouch();
			// }
			// if(number6.getText().length()==1){
			//
			// number6.setBackgroundResource(R.drawable.register_ver_s_720);
			//
			// }
			// if(number1.getText().length()==1&&number2.getText().length()==1&&number3.getText().length()==1
			// &&number4.getText().length()==1&&number5.getText().length()==1&&number6.getText().length()==1){
			// number=""+number1.getText().toString()+number2.getText().toString()+number3.getText().toString()+number4.getText().toString()+number5.getText().toString()+number6.getText().toString();
			// }

			if (allEditText.length() > 0) {
				if (allEditText.length() == 1) {
					number1.setBackgroundResource(R.drawable.register_ver_s_720);
					String num2 = (allEditText.getText()).toString().substring(
							0);
					log.e("lucifer",
							"num1==" + num2 + "  aaa" + allEditText.getText());
					number1.setText((allEditText.getText()).toString()
							.substring(0));

				} else if (allEditText.length() == 2) {
					number2.setBackgroundResource(R.drawable.register_ver_s_720);
					String num2 = (allEditText.getText()).toString().substring(
							1);
					log.e("lucifer",
							"num2==" + num2 + "  aaa" + allEditText.getText());
					number2.setText((allEditText.getText()).toString()
							.substring(1));
				} else if (allEditText.length() == 3) {
					number3.setBackgroundResource(R.drawable.register_ver_s_720);
					String num3 = (allEditText.getText()).toString().substring(
							2);
					log.e("lucifer",
							"num2==" + num3 + "  aaa" + allEditText.getText());
					number3.setText((allEditText.getText()).toString()
							.substring(2));
				} else if (allEditText.length() == 4) {
					number4.setBackgroundResource(R.drawable.register_ver_s_720);
					String num4 = (allEditText.getText()).toString().substring(
							3);
					log.e("lucifer",
							"num4==" + num4 + "  aaa" + allEditText.getText());
					number4.setText((allEditText.getText()).toString()
							.substring(3));
				} else if (allEditText.length() == 5) {
					number5.setBackgroundResource(R.drawable.register_ver_s_720);
					String num2 = (allEditText.getText()).toString().substring(
							4);
					log.e("lucifer",
							"num5==" + num2 + "  aaa" + allEditText.getText());
					number5.setText((allEditText.getText()).toString()
							.substring(4));

				} else if (allEditText.length() == 6) {
					number6.setBackgroundResource(R.drawable.register_ver_s_720);
					String num2 = (allEditText.getText()).toString().substring(
							5);
					log.e("lucifer",
							"num6==" + num2 + "  aaa" + allEditText.getText());
					number6.setText((allEditText.getText()).toString()
							.substring(5));
					saveLayout.setBackgroundResource(R.drawable.register_login_720);
				}

				if (allEditText.length() < 6) {
					number6.setText("");
					number6.setBackgroundResource(R.drawable.register_ver_h_720);
					saveLayout.setBackgroundResource(R.drawable.register_login_1_720);
				}
				if (allEditText.length() < 5) {
					number5.setText("");
					number5.setBackgroundResource(R.drawable.register_ver_h_720);
				}
				if (allEditText.length() < 4) {
					number4.setText("");
					number4.setBackgroundResource(R.drawable.register_ver_h_720);

				}
				if (allEditText.length() < 3) {
					number3.setText("");
					number3.setBackgroundResource(R.drawable.register_ver_h_720);
				}
				if (allEditText.length() < 2) {
					number2.setText("");
					number2.setBackgroundResource(R.drawable.register_ver_h_720);
				}
				if (allEditText.length() < 1) {
					number1.setText("");
					number1.setBackgroundResource(R.drawable.register_ver_h_720);
				}

			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			log.e("lucifer", " changdu==" + allEditText.length());
			if (allEditText.length() == 0) {
				number1.setText("");
				number1.setBackgroundResource(R.drawable.register_ver_h_720);
			}

		}
	};

	/**
	 * do some action
	 */
	private Handler doActionHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int msgId = msg.what;
			switch (msgId) {
			case 1:
				// do some action

				sent.setEnabled(false);
				sent.setText("重新发送" + "("+i+"s)");
				i--;
				if (i < 0) {
					sent.setText("重新发送");
					sent.setEnabled(true);
					sent.setBackgroundResource(R.drawable.register_sent_dark_720);
					break;
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// 从新发送验证码
		case R.id.sent_forgetPassword_bt:
			// Toast.makeText(ForgetPasswordVerificationActivity.this, "可点击测试",
			// Toast.LENGTH_SHORT).show();
			initLoad();
			i = 59;
			sent.setBackgroundResource(R.drawable.register_sent_grey_720);
			

			break;
		/**
		 * 进行提交修改密码
		 */
		case R.id.forget_pw_rl:

			number = allEditText.getText().toString();
			log.e("yanzhengma", number.toString());

			ObjUserWrap.resetPasswd(number, upassward,
					new ObjFunBooleanCallback() {

						@Override
						public void callback(boolean result, AVException e) {
							if (result == true) {
								Toast.makeText(
										ForgetPasswordVerificationActivity.this,
										"重置密码成功", Toast.LENGTH_SHORT).show();

								Intent intent = new Intent(
										ForgetPasswordVerificationActivity.this,
										LoginActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);

							} else {
								log.e("forgetPassword", e);
								Toast.makeText(
										ForgetPasswordVerificationActivity.this,
										"验证码不正确", Toast.LENGTH_SHORT).show();
							}

						}
					});
			break;

		case R.id.activity_forgetPassword_back_img:
			finish();
			break;
		case R.id.all_number_forget_verfication_ll:
			allEditText.requestFocus();
			InputMethodManager inputManager = (InputMethodManager) allEditText
					.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.showSoftInput(allEditText, 0);
			break;
		default:
			break;
		}
	}

}
