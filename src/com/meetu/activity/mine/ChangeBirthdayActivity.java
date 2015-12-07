package com.meetu.activity.mine;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.tools.DateUtils;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeBirthdayActivity extends Activity implements OnClickListener {
	// 控件相关
	private TextView birthday;
	private TextView queding;
	private String birth;
	private ImageView backImageView;
	private RelativeLayout backlLayout, quedingLayout;

	// 网络 数据相关
	// 拿本地的 user
	private AVUser currentUser = AVUser.getCurrentUser();
	private ObjUser user;
	private long birthLong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_change_birthday);
		birth = super.getIntent().getStringExtra("birthday");
		// 拿到本地的缓存对象 user
		if (currentUser != null) {
			// 强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		intiView();
	}

	private void intiView() {

		birthday = (TextView) super.findViewById(R.id.name_changbirth_et);
		birthday.setText(birth);
		birthday.setOnClickListener(this);
		queding = (TextView) super
				.findViewById(R.id.mine_changebirth_wancheng_bt);
		// queding.setOnClickListener(this);
		backImageView = (ImageView) super
				.findViewById(R.id.back_changebirth_mine);
		// backImageView.setOnClickListener(this);
		backlLayout = (RelativeLayout) super
				.findViewById(R.id.back_changebirth_mine_rl);
		backlLayout.setOnClickListener(this);
		quedingLayout = (RelativeLayout) super
				.findViewById(R.id.mine_changebirth_wancheng_rl);
		quedingLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.name_changbirth_et:
			Time time = new Time("GMT+8");
			time.setToNow();
			int year = time.year;
			int month = time.month;
			int day = time.monthDay;

			this.showDialog(1);
			new DatePickerDialog(this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker arg0, int year, int month,
						int day) {
					// TODO Auto-generated method stub
					birthday.setText(year + "-" + (month + 1) + "-" + day);

					birthLong = DateUtils.getStringToDate(birthday.getText()
							.toString());
				}
			}, year, month, day).show();
			break;
		// 点击 完成 上传时间
		case R.id.mine_changebirth_wancheng_rl:

			completeInfo(user);
			break;
		// 返回
		case R.id.back_changebirth_mine_rl:
			Intent intent2 = new Intent();
			intent2.putExtra("birthday", birth);
			ChangeBirthdayActivity.this.setResult(RESULT_CANCELED, intent2);
			finish();
			break;
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
		intent.putExtra("birthday", birth);
		ChangeBirthdayActivity.this.setResult(RESULT_CANCELED, intent);
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
		birthLong = DateUtils.getStringToDate(birthday.getText().toString());
		user.setBirthday(birthLong);

		// 只上传信息
		ObjUserWrap.completeUserInfo(user, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				if (result) {
					Toast.makeText(getApplicationContext(), "save 名字修改成功", 1000)
							.show();
					Intent intent = new Intent();
					intent.putExtra("birthday", birthday.getText().toString());
					ChangeBirthdayActivity.this.setResult(RESULT_OK, intent);
					finish();

				} else {
					Toast.makeText(getApplicationContext(), "save 名字修改失败", 1000)
							.show();
				}
			}
		});
	}

}
