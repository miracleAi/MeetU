package com.meetu.activity;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.meetu.R;
import com.meetu.R.layout;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjChat;
import com.meetu.cloud.object.ObjReportChat;
import com.meetu.cloud.object.ObjReportUser;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjReportWrap;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
/**
 * 举报页面
 * @author Administrator
 *
 */

public class ReportActivity extends Activity implements OnClickListener{
	private RelativeLayout pornLayout,advertisingLayout,swearingLayout,othersLayout;
	private ImageView pornImg,advertisingImg,swearingImg,othersImg;

	private int selector=-1;//选中的举报类型0  1 2 3  记录

	//控件相关
	private RelativeLayout backLayout,defineLayout;
	EditText reportEt;
	
	ObjUser user;
	String flag = "";
	String otherId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_report);
		user = AVUser.cast(AVUser.getCurrentUser(), ObjUser.class);
		flag = getIntent().getStringExtra("flag");
		otherId = getIntent().getStringExtra("otherId");
		initView();
	}

	private void initView() {

		pornLayout=(RelativeLayout) super.findViewById(R.id.porn_report_rl);
		advertisingLayout=(RelativeLayout) super.findViewById(R.id.advertising_report_rl);
		swearingLayout=(RelativeLayout) super.findViewById(R.id.swearing_report_rl);
		othersLayout=(RelativeLayout) super.findViewById(R.id.others_report_rl);
		reportEt = (EditText) findViewById(R.id.content_report_tv);

		pornImg=(ImageView) super.findViewById(R.id.porn_report_img);
		advertisingImg=(ImageView) super.findViewById(R.id.advertising_report_img);
		swearingImg=(ImageView) super.findViewById(R.id.swearing_report_img);
		othersImg=(ImageView) super.findViewById(R.id.others_report_img);

		pornLayout.setOnClickListener(this);
		advertisingLayout.setOnClickListener(this);
		swearingLayout.setOnClickListener(this);
		othersLayout.setOnClickListener(this);

		//控件相关
		backLayout=(RelativeLayout) super.findViewById(R.id.back_report_rl);
		backLayout.setOnClickListener(this);
		defineLayout=(RelativeLayout) super.findViewById(R.id.define_report_rl);
		defineLayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.porn_report_rl:
			//0 表示色情
			selector=0;
			pornLayout.setBackgroundColor(this.getResources().getColor(R.color.buyticket_check));
			pornImg.setImageResource(R.drawable.acty_join_btn_point_hl);

			advertisingLayout.setBackgroundColor(Color.WHITE);
			advertisingImg.setImageResource(R.drawable.acty_join_btn_point_nor);
			swearingLayout.setBackgroundColor(Color.WHITE);
			swearingImg.setImageResource(R.drawable.acty_join_btn_point_nor);
			othersLayout.setBackgroundColor(Color.WHITE);
			othersImg.setImageResource(R.drawable.acty_join_btn_point_nor);

			break;
		case R.id.advertising_report_rl:
			//1表示广告
			selector=1;
			pornLayout.setBackgroundColor(Color.WHITE);
			pornImg.setImageResource(R.drawable.acty_join_btn_point_nor);

			advertisingLayout.setBackgroundColor(this.getResources().getColor(R.color.buyticket_check));
			advertisingImg.setImageResource(R.drawable.acty_join_btn_point_hl);
			swearingLayout.setBackgroundColor(Color.WHITE);
			swearingImg.setImageResource(R.drawable.acty_join_btn_point_nor);
			othersLayout.setBackgroundColor(Color.WHITE);
			othersImg.setImageResource(R.drawable.acty_join_btn_point_nor);

			break;
		case R.id.swearing_report_rl:
			//32表示侮辱谩骂
			selector=2;
			pornLayout.setBackgroundColor(Color.WHITE);
			pornImg.setImageResource(R.drawable.acty_join_btn_point_nor);

			advertisingLayout.setBackgroundColor(Color.WHITE);
			advertisingImg.setImageResource(R.drawable.acty_join_btn_point_nor);
			swearingLayout.setBackgroundColor(this.getResources().getColor(R.color.buyticket_check));
			swearingImg.setImageResource(R.drawable.acty_join_btn_point_hl);
			othersLayout.setBackgroundColor(Color.WHITE);
			othersImg.setImageResource(R.drawable.acty_join_btn_point_nor);

			break;
		case R.id.others_report_rl:
			//3 表示其他
			selector=3;
			pornLayout.setBackgroundColor(Color.WHITE);
			pornImg.setImageResource(R.drawable.acty_join_btn_point_nor);

			advertisingLayout.setBackgroundColor(Color.WHITE);
			advertisingImg.setImageResource(R.drawable.acty_join_btn_point_nor);
			swearingLayout.setBackgroundColor(Color.WHITE);
			swearingImg.setImageResource(R.drawable.acty_join_btn_point_nor);
			othersLayout.setBackgroundColor(this.getResources().getColor(R.color.buyticket_check));
			othersImg.setImageResource(R.drawable.acty_join_btn_point_hl);

			break;
		case R.id.back_report_rl:
			finish();
			break;
		case R.id.define_report_rl:
			//TODO 确定 需要做判断
			if(flag.equals("user")){
				reportUser(otherId);
			}else{
				reportChat(otherId);
			}
			break;


		default:
			break;
		}

	}
	/**
	 * 举报用户
	 * */
	public void reportUser(String usrId){
		ObjUser otherUser;
		try {
			otherUser = AVUser.createWithoutData(ObjUser.class, usrId);
			ObjReportUser  shieldUser = new ObjReportUser();
			shieldUser.setUser(user);
			shieldUser.setReportUser(otherUser);
			shieldUser.setReportCode(selector);
			shieldUser.setAppend(reportEt.getText().toString());
			ObjReportWrap.reportUser(shieldUser, new ObjFunBooleanCallback() {

				@Override
				public void callback(boolean result, AVException e) {
					// TODO Auto-generated method stub
					if(e != null){
						Toast.makeText(getApplicationContext(), "操作失败", 1000).show();
						return;
					}
					if(result){
						Toast.makeText(getApplicationContext(), "举报成功", 1000).show();
						finish();
					}else{
						Toast.makeText(getApplicationContext(), "操作失败", 1000).show();
					}
				}
			});
		} catch (AVException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * 举报觅聊
	 * */
	public void reportChat(String chatId){
		ObjReportChat reportChat = new ObjReportChat();
		try {
			ObjChat chat = AVObject.createWithoutData(ObjChat.class, chatId);
			reportChat.setChat(chat);
			reportChat.setUser(user);
			reportChat.setReportCode(selector);
			reportChat.setAppend(reportEt.getText().toString());
			ObjReportWrap.reportChat(reportChat, new ObjFunBooleanCallback() {
				
				@Override
				public void callback(boolean result, AVException e) {
					// TODO Auto-generated method stub
					if(e != null){
						Toast.makeText(getApplicationContext(), "操作失败", 1000).show();
						return;
					}
					if(result){
						Toast.makeText(getApplicationContext(), "举报成功", 1000).show();
						finish();
					}else{
						Toast.makeText(getApplicationContext(), "操作失败", 1000).show();
					}
				}
			});
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
