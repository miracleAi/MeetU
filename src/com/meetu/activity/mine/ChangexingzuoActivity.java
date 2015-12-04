package com.meetu.activity.mine;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.meetu.R;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;

import android.R.string;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChangexingzuoActivity extends Activity implements OnClickListener {
	// 控件相关
	private TextView text1, text2, text3, text4, text5, text6;
	private TextView text7, text8, text9, text10, text11, text12;
	private LinearLayout layout1, layout2, layout3, layout4, layout5, layout6;
	private LinearLayout layout7, layout8, layout9, layout10, layout11,
			layout12;
	private String xingzuo;
	private ImageView backImageView;
	private RelativeLayout backLayout, quedingLayout;

	// 网络数据相关
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
		setContentView(R.layout.activity_changexingzuo);
		xingzuo = super.getIntent().getStringExtra("Constellation");

		// 拿到本地的缓存对象 user
		if (currentUser != null) {
			// 强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}

		initView();
	}

	private void initView() {
		text1 = (TextView) super.findViewById(R.id.change_xingzuo1_tv);
		text2 = (TextView) super.findViewById(R.id.change_xingzuo2_tv);
		text3 = (TextView) super.findViewById(R.id.change_xingzuo3_tv);
		text4 = (TextView) super.findViewById(R.id.change_xingzuo4_tv);
		text5 = (TextView) super.findViewById(R.id.change_xingzuo5_tv);
		text6 = (TextView) super.findViewById(R.id.change_xingzuo6_tv);
		text7 = (TextView) super.findViewById(R.id.change_xingzuo7_tv);
		text8 = (TextView) super.findViewById(R.id.change_xingzuo8_tv);
		text9 = (TextView) super.findViewById(R.id.change_xingzuo9_tv);
		text10 = (TextView) super.findViewById(R.id.change_xingzuo10_tv);
		text11 = (TextView) super.findViewById(R.id.change_xingzuo11_tv);
		text12 = (TextView) super.findViewById(R.id.change_xingzuo12_tv);
		text1.setOnClickListener(this);
		layout1 = (LinearLayout) super.findViewById(R.id.change_xingzuo1_ll);
		layout2 = (LinearLayout) super.findViewById(R.id.change_xingzuo2_ll);
		layout3 = (LinearLayout) super.findViewById(R.id.change_xingzuo3_ll);
		layout4 = (LinearLayout) super.findViewById(R.id.change_xingzuo4_ll);
		layout5 = (LinearLayout) super.findViewById(R.id.change_xingzuo5_ll);
		layout6 = (LinearLayout) super.findViewById(R.id.change_xingzuo6_ll);
		layout7 = (LinearLayout) super.findViewById(R.id.change_xingzuo7_ll);
		layout8 = (LinearLayout) super.findViewById(R.id.change_xingzuo8_ll);
		layout9 = (LinearLayout) super.findViewById(R.id.change_xingzuo9_ll);
		layout10 = (LinearLayout) super.findViewById(R.id.change_xingzuo10_ll);
		layout11 = (LinearLayout) super.findViewById(R.id.change_xingzuo11_ll);
		layout12 = (LinearLayout) super.findViewById(R.id.change_xingzuo12_ll);
		layout1.setOnClickListener(this);
		layout2.setOnClickListener(this);
		layout3.setOnClickListener(this);
		layout4.setOnClickListener(this);
		layout5.setOnClickListener(this);
		layout6.setOnClickListener(this);
		layout7.setOnClickListener(this);
		layout8.setOnClickListener(this);
		layout9.setOnClickListener(this);
		layout10.setOnClickListener(this);
		layout11.setOnClickListener(this);
		layout12.setOnClickListener(this);
		backImageView = (ImageView) super
				.findViewById(R.id.back_changexingzuo_mine);
		backImageView.setOnClickListener(this);
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_changexingzuo_mine_rl);
		backLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.change_xingzuo1_ll:
			xingzuo = text1.getText().toString();
			completeInfo(user);
			Intent intent = new Intent();
			intent.putExtra("xingzuo", text1.getText().toString());
			setResult(RESULT_OK, intent);
			finish();

			break;
		case R.id.change_xingzuo2_ll:
			xingzuo = text2.getText().toString();
			completeInfo(user);
			Intent intent2 = new Intent();
			intent2.putExtra("xingzuo", text2.getText());
			setResult(RESULT_OK, intent2);
			finish();

			break;
		case R.id.change_xingzuo3_ll:
			xingzuo = text3.getText().toString();
			completeInfo(user);
			Intent intent3 = new Intent();
			intent3.putExtra("xingzuo", text3.getText());
			setResult(RESULT_OK, intent3);
			finish();

			break;
		case R.id.change_xingzuo4_ll:
			xingzuo = text4.getText().toString();
			completeInfo(user);
			Intent intent4 = new Intent();
			intent4.putExtra("xingzuo", text4.getText());
			setResult(RESULT_OK, intent4);
			finish();

			break;
		case R.id.change_xingzuo5_ll:
			xingzuo = text5.getText().toString();
			completeInfo(user);
			Intent intent5 = new Intent();
			intent5.putExtra("xingzuo", text5.getText());
			setResult(RESULT_OK, intent5);
			finish();

			break;
		case R.id.change_xingzuo6_ll:
			xingzuo = text6.getText().toString();
			completeInfo(user);
			Intent intent6 = new Intent();
			intent6.putExtra("xingzuo", text6.getText());
			setResult(RESULT_OK, intent6);
			finish();

			break;
		case R.id.change_xingzuo7_ll:
			xingzuo = text7.getText().toString();
			completeInfo(user);
			Intent intent7 = new Intent();
			intent7.putExtra("xingzuo", text7.getText());
			setResult(RESULT_OK, intent7);
			finish();
			break;
		case R.id.change_xingzuo8_ll:
			xingzuo = text8.getText().toString();
			completeInfo(user);
			Intent intent8 = new Intent();
			intent8.putExtra("xingzuo", text8.getText());
			setResult(RESULT_OK, intent8);
			finish();
			break;
		case R.id.change_xingzuo9_ll:
			xingzuo = text9.getText().toString();
			completeInfo(user);
			Intent intent9 = new Intent();
			intent9.putExtra("xingzuo", text9.getText());
			setResult(RESULT_OK, intent9);
			finish();

			break;
		case R.id.change_xingzuo10_ll:
			xingzuo = text10.getText().toString();
			completeInfo(user);
			Intent intent10 = new Intent();
			intent10.putExtra("xingzuo", text10.getText());
			setResult(RESULT_OK, intent10);
			finish();

			break;
		case R.id.change_xingzuo11_ll:
			xingzuo = text11.getText().toString();
			completeInfo(user);
			Intent intent11 = new Intent();
			intent11.putExtra("xingzuo", text11.getText());
			setResult(RESULT_OK, intent11);
			finish();
			break;
		case R.id.change_xingzuo12_ll:
			xingzuo = text12.getText().toString();
			completeInfo(user);
			Intent intent12 = new Intent();
			intent12.putExtra("xingzuo", text12.getText());
			setResult(RESULT_OK, intent12);
			finish();

			break;

		case R.id.back_changexingzuo_mine_rl:

			Intent intent13 = new Intent();
			intent13.putExtra("xingzuo", xingzuo);
			setResult(RESULT_CANCELED, intent13);
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
		intent.putExtra("xingzuo", xingzuo);
		setResult(RESULT_CANCELED, intent);
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

		user.setNameNick(xingzuo);

		// 只上传信息
		ObjUserWrap.completeUserInfo(user, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				if (result) {
					Toast.makeText(getApplicationContext(), "星座修改成功", 1000)
							.show();

					finish();
				} else {
					Toast.makeText(getApplicationContext(), "星座修改失败", 1000)
							.show();
				}
			}
		});
	}

}
