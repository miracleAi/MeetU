package com.meetu.activity.homepage;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.lidroid.xutils.BitmapUtils;
import com.meetu.R;
import com.meetu.R.layout;
import com.meetu.R.menu;
import com.meetu.bean.ActivityBean;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityWrap;
import com.meetu.tools.DateUtils;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityFeedbackActivity extends Activity implements OnClickListener{
	//控件相关
	private TextView copy,number,title,time;
	private RelativeLayout copylLayout,backLayout,quedingLayout;
	private EditText content;
	private ImageView photoHead;
	//网络数据相关
	private ActivityBean activityBean=new ActivityBean();
	private ObjActivity objActivity= null;
	private ObjUser user;
	private BitmapUtils bitmapUtils;
	//拿本地的  user 
	private AVUser currentUser = AVUser.getCurrentUser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_activity_feedback);
		if(currentUser!=null){
			//强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		bitmapUtils=new BitmapUtils(this);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		activityBean= (ActivityBean) bundle.getSerializable("activityBean");	
		log.e("zcq", "activityBean"+activityBean.getLocationLongtitude()+"   "+activityBean.getLocationLatitude());
		log.e("zcq", "TimeStart()=="+activityBean.getTimeStart()+"  =="+activityBean.getTimeStop());
		initLoadActivity(activityBean.getActyId());
		initView();
	}
	/**
	 * 根据用户id 生成 objacitivity
	 * @param activityId  
	 * @author lucifer
	 * @date 2015-11-13
	 */
	private void initLoadActivity(String activityId) {
		log.e("zcq", "activityId=="+activityId);
			try {
				 objActivity=AVObject.createWithoutData(ObjActivity.class, activityId);

			} catch (AVException e) {
				
				e.printStackTrace();
			}
			
		}

	private void initView() {
		copy=(TextView) super.findViewById(R.id.copy_feedback_tv);
		copy.setOnClickListener(this);
		number=(TextView) findViewById(R.id.number_feedback_tv);
		copylLayout=(RelativeLayout) super.findViewById(R.id.copy_feedback_rl);
		copylLayout.setOnClickListener(this);
		backLayout=(RelativeLayout) super.findViewById(R.id.back_feedback_homepager_rl);
		backLayout.setOnClickListener(this);
		quedingLayout=(RelativeLayout) super.findViewById(R.id.queding_feedback_homepager_rl);
		quedingLayout.setOnClickListener(this);
		content=(EditText) super.findViewById(R.id.content_feedback_et);
		time=(TextView) super.findViewById(R.id.time_feedback_tv);
		time.setText(DateUtils.getDateToString(System.currentTimeMillis()));
		title=(TextView) super.findViewById(R.id.title_feedback_tv);
		title.setText(""+activityBean.getTitle());
		photoHead=(ImageView) super.findViewById(R.id.photo_head_feedback_img);
		
		bitmapUtils.display(photoHead, user.getProfileClip().getUrl());
	}

	/**
	 * 
	 *    提交活动反馈 信息
	 * @author lucifer
	 * @date 2015-11-13
	 */
	private void saveFeedBack(){
		ObjActivityWrap.activityFeedBack(objActivity, user, content.getText().toString(), new ObjFunBooleanCallback() {
			
			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e!=null||result==false){
					Toast.makeText(ActivityFeedbackActivity.this, "提交失败哦", 1000).show();
				}else{
					Toast.makeText(ActivityFeedbackActivity.this, "提交成功", 1000).show();
					finish();
				}
				
			}
		});
	}
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
			case R.id.copy_feedback_rl :
			//实现复制 黏贴
			ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
			//复制
			clip.setText(number.getText());
			//黏贴.
			//clip.getText(); // 粘贴
			Toast.makeText(this, "已复制", Toast.LENGTH_SHORT).show();				
			break;
		case R.id.back_feedback_homepager_rl:
			finish();
			break;
		case R.id.queding_feedback_homepager_rl:
		//	Toast.makeText(this, "提交数据", Toast.LENGTH_SHORT).show();
			if(content.getText().length()==0){
				Toast.makeText(this, "请填写内容", 1000).show();
			}else{
				saveFeedBack();
			}
			break;

default :
	break;

	}
	
}
	

}
