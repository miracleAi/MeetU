package com.meetu.activity.homepage;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.LogUtil.log;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.meetu.R;
import com.meetu.R.layout;
import com.meetu.R.menu;
import com.meetu.adapter.BookingListViewAdapter;
import com.meetu.bean.ActivityBean;
import com.meetu.cloud.callback.ObjTicketCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityTicket;
import com.meetu.cloud.wrap.ObjActivityWrap;
import com.meetu.entity.Booking;
import com.meetu.myapplication.MyApplication;
import com.meetu.tools.DateUtils;
import com.meetu.tools.DensityUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class JoinActivity extends Activity implements OnClickListener ,OnItemClickListener{
	private RelativeLayout backLayout;
	private ListView listView;
	private List<Booking> booklist=new ArrayList<Booking>();
	private BookingListViewAdapter adapter;
	private LinearLayout payLayout;
	private RelativeLayout paylRelativeLayout,payweixin,payzhifubao;
	private RelativeLayout visibleCenten4;
	
	//网络数据相关
	//某项活动的票种列表
	private ActivityBean activityBean=new ActivityBean();
	private ArrayList<ObjActivityTicket> tickets = new ArrayList<ObjActivityTicket>();
	private ObjActivity objActivity= null;
	private FinalBitmap fianlBitmap;
	
	//控件相关
	private ImageView photoHuodong;
	private TextView title,titleDesc,timeStart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_join);
		//取到全局的bitmap
		MyApplication app=(MyApplication) this.getApplicationContext();
		fianlBitmap=app.getFinalBitmap();
		
		
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		activityBean= (ActivityBean) bundle.getSerializable("activityBean");
		initLoadActivity(activityBean.getActyId());
		//数据加载
		loaddate();
				
		//事件绑定处理
		initView();
		
		
	}
	private void initLoadActivity(String activityId) {
		log.e("zcq", "activityId=="+activityId);
			try {
				 objActivity=AVObject.createWithoutData(ObjActivity.class, activityId);
//				ObjActivityCoverWrap.queryActivityCover(objActivity, new ObjActivityCoverCallback() {
//					
//					@Override
//					public void callback(List<ObjActivityCover> objects, AVException e) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
			} catch (AVException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	

	private void initView() {
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_join_homepager_rl);
		backLayout.setOnClickListener(this);
		listView=(ListView) super.findViewById(R.id.listview_piao_join_homepager);
		adapter=new BookingListViewAdapter(this, tickets);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
	
		payLayout=(LinearLayout) super.findViewById(R.id.pay_join_homepager_ll);
		payLayout.setOnClickListener(this);
		paylRelativeLayout=(RelativeLayout) super.findViewById(R.id.pay_join_homepager_rl);
		paylRelativeLayout.setOnClickListener(this);
		payweixin=(RelativeLayout) super.findViewById(R.id.pay_weixin_rl);
		payweixin.setOnClickListener(this);
		payzhifubao=(RelativeLayout) super.findViewById(R.id.pay_zhifubao_rl);
		payzhifubao.setOnClickListener(this);
		visibleCenten4=(RelativeLayout) super.findViewById(R.id.center4_join_rl);
		
		title=(TextView) super.findViewById(R.id.title_huodong_join_tv);
		titleDesc=(TextView) super.findViewById(R.id.title_tab_huodong_join_tv);
		timeStart=(TextView) super.findViewById(R.id.time_huodong_start_join_tv);
		title.setText(activityBean.getTitle());
		titleDesc.setText(activityBean.getTitleSub());
		timeStart.setText(DateUtils.getDateToString(activityBean.getTimeStart()));
		
		photoHuodong=(ImageView) super.findViewById(R.id.photo_join_homepager);
		
		fianlBitmap.display(photoHuodong, activityBean.getActivityCover());
	}
	
	private void loaddate() {
//		booklist=new ArrayList<Booking>();
//		booklist.add(new Booking(11,"在校生",0,"99"));
//		booklist.add(new Booking(22,"校友",10,"199"));
//		booklist.add(new Booking(33,"投资人士",10,"免费"));
//		booklist.add(new Booking(33,"其他",0,"999"));
		
		//获取票种信息
		ObjActivityWrap.queryTicket(objActivity, new ObjTicketCallback() {

			@Override
			public void callback(List<ObjActivityTicket> objects, AVException e) {
				// TODO Auto-generated method stub
				
				if(e != null){
					Toast.makeText(getApplicationContext(), e.getMessage(), 1000).show();
					return ;
				}
				if(objects != null && objects.size()>0){
					tickets.addAll(objects);
					String ss = tickets.get(0).getTicketTitle();
					log.e("zcq", "name=="+ss);
					handler.sendEmptyMessage(1);
			//		secondTv.setText(ss);
				}
			}
		});

	}
		
		
	

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back_join_homepager_rl :
				finish();

				break;
			case R.id.pay_join_homepager_rl:
				Toast.makeText(this, "点击了免费参加活动", Toast.LENGTH_SHORT).show();
				break;
			case R.id.pay_weixin_rl:
				Toast.makeText(this, "点击了微信支付", Toast.LENGTH_SHORT).show();
				break;
		 case R.id.pay_zhifubao_rl:
			 Toast.makeText(this, "点击了支付宝支付", Toast.LENGTH_SHORT).show();
				break;
			default :
				break;
		}
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		Toast.makeText(this,"position "+position+" id "+id, Toast.LENGTH_SHORT).show();
		adapter.setSelectedPosition(position);
		if(tickets.get(position).getPrice().equals("0")){
			payLayout.setVisibility(View.INVISIBLE);
			paylRelativeLayout.setVisibility(View.VISIBLE);
			visibleCenten4.setVisibility(View.INVISIBLE);
		}else{
			payLayout.setVisibility(View.VISIBLE);
			paylRelativeLayout.setVisibility(View.INVISIBLE);
			visibleCenten4.setVisibility(View.VISIBLE);
		}
		
		adapter.notifyDataSetChanged();
	
	}
	
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 1:
				//给listview动态设置高度
				RelativeLayout.LayoutParams params=(LayoutParams) listView.getLayoutParams();
				params.height=tickets.size()*DensityUtil.dip2px(JoinActivity.this, 70);
				listView.setLayoutParams(params);
				adapter.notifyDataSetChanged();
								
				break;
			}
		}
	
	};

}
