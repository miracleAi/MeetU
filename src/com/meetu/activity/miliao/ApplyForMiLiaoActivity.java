package com.meetu.activity.miliao;

import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.bean.SeekChatInfoBean;
import com.meetu.cloud.callback.ObjAuthoriseApplyCallback;
import com.meetu.cloud.callback.ObjAuthoriseCategoryCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjAuthoriseApply;
import com.meetu.cloud.object.ObjAuthoriseCategory;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjAuthoriseWrap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ApplyForMiLiaoActivity extends Activity implements OnClickListener {
	private RelativeLayout backLayout;
	private ImageView applyImageView;
	private EditText content;// 内容

	private LinearLayout nonetipLayout;
	private TextView applyResultTextView;

	// 网络相关
	AVUser currentUser = ObjUser.getCurrentUser();
	ObjUser user = new ObjUser();
	// 权限
	ObjAuthoriseCategory categorys = new ObjAuthoriseCategory();
	// 权限申请
	ObjAuthoriseApply applys = new ObjAuthoriseApply();

	// SeekChatInfoBean chatBean = new SeekChatInfoBean();

	private boolean isApply;
	private String applyId;
	private String categoryId;
	private String applyResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_apply_for_mi_liao);
		if (currentUser != null) {
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		// chatBean = (SeekChatInfoBean) getIntent().getExtras().get(
		// "chatBean");
		Intent intent = getIntent();

		String number = intent.getStringExtra("isApply");
		if (number.equals("0")) {
			isApply = false;
		} else {
			isApply = true;
		}
		applyId = intent.getStringExtra("applyId");
		categoryId = intent.getStringExtra("CategoryId");
		applyResult = intent.getStringExtra("ApplyReply");

		log.e("categoryId", categoryId);
		log.e("applyId", applyId);
		initView();
	}

	private void initView() {
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_applyForMiLiao_rl);
		backLayout.setOnClickListener(this);
		applyImageView = (ImageView) super
				.findViewById(R.id.applyForMiLiao_img);
		applyImageView.setOnClickListener(this);
		content = (EditText) super.findViewById(R.id.content_applyForMiLiao_et);

		nonetipLayout = (LinearLayout) findViewById(R.id.apply_no_miliao_ll);
		applyResultTextView = (TextView) findViewById(R.id.apply_result_miliao_tv);

		if (isApply) {
			nonetipLayout.setVisibility(View.GONE);
			applyResultTextView.setVisibility(View.VISIBLE);
			applyResultTextView.setText(applyResult);
		} else {
			nonetipLayout.setVisibility(View.VISIBLE);
			applyResultTextView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_applyForMiLiao_rl:
			finish();

			break;
		case R.id.applyForMiLiao_img:

			// Intent intent=new
			// Intent(ApplyForMiLiaoActivity.this,CreationChatActivity.class);
			// startActivity(intent);
			if (content.getText().length() == 0) {
				Toast.makeText(getApplicationContext(), "请填写申请内容", 1000).show();
			} else {
				// queryIsApply(chatBean);

				if (isApply) {

					applys = getObjAuthoriseById(applyId);
					updateApplyAuthorise(applys, content.getText().toString());
				} else {
					// 没申请过
					categorys = getObjAuthoriseCategoryById("" + categoryId);
					log.e("zcq categoryId", categoryId);
					applyAuthorise(categorys, content.getText().toString());
				}
				finish();
			}

		default:
			break;
		}

	}


	// 发起申请
	/**
	 * 发起申请权限
	 * 
	 * @param caty
	 * @param argument
	 *            内容
	 * @author lucifer
	 * @date 2015-11-17
	 */
	public void applyAuthorise(ObjAuthoriseCategory caty, String argument) {
		ObjAuthoriseWrap.applyAuthorise(user, caty, argument,
				new ObjFunBooleanCallback() {

					@Override
					public void callback(boolean result, AVException e) {
						// TODO Auto-generated method stub
						if (e != null) {
							log.e("zcq", e);

							return;
						}
						if (result) {
							log.e("zcq", "发起申请成功");
							Toast.makeText(getApplicationContext(), "小U收到申请啦",
									Toast.LENGTH_SHORT).show();
						} else {
							log.e("zcq", "发起申请失败，请检查网络");
							Toast.makeText(getApplicationContext(),
									"申请发送失败", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	// 重新申请
	public void updateApplyAuthorise(ObjAuthoriseApply aply, String argument) {
		aply.setFreshStatus(false);
		ObjAuthoriseWrap.updateApplyAuthorise(aply, argument,
				new ObjFunBooleanCallback() {

					@Override
					public void callback(boolean result, AVException e) {
						// TODO Auto-generated method stub
						if (e != null) {
							log.e("zcq", e);
							return;
						}
						if (result) {
							log.e("zcq", "重新发起申请成功");
							Toast.makeText(getApplicationContext(), "小U收到申请啦",
									Toast.LENGTH_SHORT).show();
						} else {
							log.e("zcq", "重新发起申请失败，请检查网络");
							Toast.makeText(getApplicationContext(),
									"申请发送失败", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
	}

	/**
	 * 根据ID生成activity对象
	 * */
	public static ObjAuthoriseApply getObjAuthoriseById(String id) {
		ObjAuthoriseApply apply = null;
		try {
			apply = AVObject.createWithoutData(ObjAuthoriseApply.class, id);
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return apply;
	}

	/**
	 * 根据ID生成activity对象
	 * */
	public static ObjAuthoriseCategory getObjAuthoriseCategoryById(String id) {
		ObjAuthoriseCategory category = null;
		try {
			category = AVObject.createWithoutData(ObjAuthoriseCategory.class,
					id);
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return category;
	}

}
