package com.meetu.activity;

import com.meetu.R;
import com.meetu.R.layout;

import android.os.Bundle;

import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
/**
 * 举报页面
 * @author Administrator
 *
 */

public class ReportActivity extends Activity implements OnClickListener{
	private RelativeLayout pornLayout,advertisingLayout,swearingLayout,othersLayout;
	private ImageView pornImg,advertisingImg,swearingImg,othersImg;
	
	private int selector=-1;//选中的举报类型 1 2 3 4  记录
	
	//控件相关
	private RelativeLayout backLayout,defineLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_report);
		initView();
	}

	private void initView() {
		
		pornLayout=(RelativeLayout) super.findViewById(R.id.porn_report_rl);
		advertisingLayout=(RelativeLayout) super.findViewById(R.id.advertising_report_rl);
		swearingLayout=(RelativeLayout) super.findViewById(R.id.swearing_report_rl);
		othersLayout=(RelativeLayout) super.findViewById(R.id.others_report_rl);
		
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
			//1 表示色情
			selector=1;
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
			//2表示广告
			selector=2;
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
			//3 表示侮辱谩骂
			selector=3;
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
			//4 表示其他
			selector=4;
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
			break;
			

		default:
			break;
		}
		
	}



}
