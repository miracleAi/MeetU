package com.meetu.activity.homepage;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;

import com.lidroid.xutils.BitmapUtils;
import com.meetu.adapter.JoinUserAdapter;
import com.meetu.adapter.JoinUserFavorAdapter;
import com.meetu.bean.ActivityBean;
import com.meetu.cloud.callback.ObjFunObjectsCallback;
import com.meetu.cloud.callback.ObjUserCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityOrderWrap;
import com.meetu.cloud.wrap.ObjFollowWrap;
import com.meetu.entity.User;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.ActivityDao;
import com.meetu.tools.DensityUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class JoinUsersActivity extends Activity implements OnItemClickListener,
		OnClickListener {
	// 控件相关
	private ListView mlistViewAll, mListViewFavor;
	private JoinUserAdapter adapter;
	private List<User> joinUsersList = new ArrayList<User>();
	// private List<User> joinUsersFavorList=new ArrayList<User>();
	private JoinUserFavorAdapter adapterfavor;
	private float itemHight = (float) 60.5;// item高度加下划线高度
	private TextView numberFavor, numberAll;
	private RelativeLayout backLayout, signLayout;
	private ImageView signImageView;
	private ImageView myPHotoHead, sexPhoto;
	private TextView userName, userSchool, numberJoin;

	// 网络数据相关
	private ActivityBean activityBean = new ActivityBean();
	private ObjActivity objActivity = null;
	private BitmapUtils bitmapUtils;
	private FinalBitmap finalBitmap;
	// 当前用户
	ObjUser user = new ObjUser();
	AVUser currentUser = AVUser.getCurrentUser();
	// 参加活动用户列表
	ArrayList<ObjUser> userList = new ArrayList<ObjUser>();
	private ArrayList<ObjUser> userFavorList = new ArrayList<ObjUser>();

	private ActivityDao actyDao;
	
	//空状态
	private RelativeLayout noneOrFailLayout;
	private TextView noneTextView;
	private TextView failTextView;
	private RelativeLayout allLayout;
	
	private RelativeLayout MYtopLayout,myLayout;
	private boolean isJoin=false;//是否加入了当前活动

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_join_users);
		if (currentUser != null) {
			// 强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		bitmapUtils = new BitmapUtils(this);
		MyApplication app = (MyApplication) this.getApplication();
		finalBitmap = app.getFinalBitmap();
		actyDao = new ActivityDao(this);
		// 接收传过来的数据
		activityBean = (ActivityBean) getIntent().getExtras().getSerializable(
				"activityBean");

		log.e("lucifer",
				"activityBean.getActyId()==" + activityBean.getActyId());

		initLoadActivity(activityBean.getActyId());
		
	
		
		
		initView();
		loadData();
		loadData2();
		mlistViewAll = (ListView) super
				.findViewById(R.id.listview_all_joinUsers);
		mListViewFavor = (ListView) super
				.findViewById(R.id.listview_favor_joinUser);
		adapter = new JoinUserAdapter(this, userList);

		mlistViewAll.setAdapter(adapter);
		mlistViewAll.setOnItemClickListener(this);
		adapterfavor = new JoinUserFavorAdapter(this, userFavorList);
		mListViewFavor.setAdapter(adapterfavor);

	//	isJoin();
		
		initalize();
		
	}
	
	/**
	 * 初始化加载内容
	 *   
	 * @author lucifer
	 * @date 2015-12-8
	 */
	private void initalize() {
		// TODO Auto-generated method stub
		// 动态设置listview 高度
		RelativeLayout.LayoutParams rlAll = (LayoutParams) mlistViewAll
				.getLayoutParams();
		rlAll.height = DensityUtil.dip2px(this, itemHight) * userList.size();
		mlistViewAll.setLayoutParams(rlAll);

		RelativeLayout.LayoutParams rlfavor = (LayoutParams) mListViewFavor
				.getLayoutParams();
		rlfavor.height = DensityUtil.dip2px(this, itemHight)
				* userFavorList.size() + 40;
		mListViewFavor.setLayoutParams(rlfavor);

		log.e("lucifer", "itemHight=" + itemHight);
		numberFavor = (TextView) super.findViewById(R.id.number_favor_joinUser);
		numberFavor.setText("(" + userFavorList.size() + ")");
		numberAll = (TextView) super.findViewById(R.id.number_all_joinUser);
		numberAll.setText("(" + userList.size() + ")");
		
		// TODO 此处应该是报名后的真实名字
		userName.setText(user.getNameNick());
		
		userSchool.setText(user.getSchool());
		sexPhoto = (ImageView) super
				.findViewById(R.id.user_sex_huodong_join_img);
		if (user.getGender() == 2) {
			sexPhoto.setImageResource(R.drawable.acty_joinlist_img_female);
		} else {

		}
		
		if(user.getProfileClip()!=null){
			finalBitmap.display(myPHotoHead, user.getProfileClip().getUrl());
		}
	}

	private void loadData2() {
		
	

	}
/**
 * 控件
 *   
 * @author lucifer
 * @date 2015-12-8
 */
	private void initView() {

		// 点击事件处理
		
		allLayout=(RelativeLayout) findViewById(R.id.all_join_users_activity_rl);
		noneOrFailLayout=(RelativeLayout) findViewById(R.id.none_or_fail_join_users_rl);
		noneTextView=(TextView) findViewById(R.id.none_join_users_tv);
		failTextView=(TextView) findViewById(R.id.fail_join_users_tv);
		
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_joinUsers_homepager_rl);
		backLayout.setOnClickListener(this);
		signLayout = (RelativeLayout) super
				.findViewById(R.id.sign_joinUsers_homepager_rl);
		signLayout.setOnClickListener(this);
		signImageView = (ImageView) super
				.findViewById(R.id.sign_joinUsers_homepager_img);
		myPHotoHead = (ImageView) super
				.findViewById(R.id.photo_head_my_join_users_huodong_tv);
		
		userName = (TextView) super
				.findViewById(R.id.name_user_huodong_join_tv);
		userSchool = (TextView) super
				.findViewById(R.id.school_user_huodong_join_tv);
		numberJoin = (TextView) super.findViewById(R.id.number_join_huodong_tv);
		
		myLayout=(RelativeLayout) findViewById(R.id.center2_joinUsers_rl);
		MYtopLayout=(RelativeLayout) findViewById(R.id.top_join_user_rl);
		

	}

	private void loadData() {


		queryOrderUsers(objActivity);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_joinUsers_homepager_rl:
			finish();

			break;
		case R.id.sign_joinUsers_homepager_rl:
			signImageView
					.setImageResource(R.drawable.acty_joinlist_img_checkedin);
			break;

		default:
			break;
		}
	}

	// 查询参加活动列表 setOrderUsers
	public void queryOrderUsers(final ObjActivity activity) {

		ObjActivityOrderWrap.queryActivitySignUp(activity,
				new ObjUserCallback() {

					@Override
					public void callback(List<ObjUser> objects, AVException e) {
						if (e != null) {
							log.e("zcq", e);
							noneOrFailLayout.setVisibility(View.VISIBLE);
							noneTextView.setVisibility(View.GONE);
							failTextView.setVisibility(View.VISIBLE);
							noneOrFailLayout.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									loadData();
								}
							});
							return;
						} 
						if (objects != null&&objects.size()>0) {
							userList.addAll(objects);
							log.e("zcq 有人", "userList==" + userList.size());
							queryFollowAndOrder(activity);
							for(int i=0;i<objects.size();i++){
								if(user.getObjectId().equals(objects.get(i).getObjectId())){
									isJoin=true;
									break;
								}
								
							}
							
							noneOrFailLayout.setVisibility(View.GONE);
							allLayout.setVisibility(View.VISIBLE);
							if(isJoin==true){
								signLayout.setVisibility(View.VISIBLE);
								myLayout.setVisibility(View.VISIBLE);
								MYtopLayout.setVisibility(View.VISIBLE);
							}
							
							handler.sendEmptyMessage(1);
							log.e("zcq", "objects==" + objects.size());
						}else{
							log.e("zcq 没人", "没有成员啊");
							noneOrFailLayout.setVisibility(View.VISIBLE);
							noneTextView.setVisibility(View.VISIBLE);
							failTextView.setVisibility(View.GONE);
							
						}

					}
				});

	}

	// 获取参加活动并且我关注的人 setOrderAndFollow
	public void queryFollowAndOrder(ObjActivity activity) {
		ArrayList<ObjUser> followUsers = new ArrayList<ObjUser>();
		ObjFollowWrap.myFollow(userList, user, new ObjUserCallback() {

			@Override
			public void callback(List<ObjUser> objects, AVException e) {
				if (e != null) {
					log.e("zcq", e);
				} else if (objects != null) {
					userFavorList.addAll(objects);
					log.e("zcq", "userFavorList==" + userList.size());
					handler.sendEmptyMessage(1);
				}

			}
		});
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				log.e("lucifer", "shuaxin");
				adapterfavor.notifyDataSetChanged();
				initalize();
				break;

			default:
				break;
			}

		}

	};

	/**
	 * 获得活动的activity
	 * 
	 * @param activityId
	 * @author lucifer
	 * @date 2015-11-13
	 */
	public void initLoadActivity(String activityId) {
		log.e("zcq", "activityId==" + activityId);
		try {
			objActivity = AVObject.createWithoutData(ObjActivity.class,
					activityId);

		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void isJoin(){
		ObjActivityOrderWrap.queryActivitySignUp(objActivity, new ObjUserCallback() {
			
			@Override
			public void callback(List<ObjUser> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e!=null){
					log.e("zcq 请求失败", e);
					return;
				}
				if(objects!=null&&objects.size()>0){
					for(int i=0;i<objects.size();i++){
						if(user.getObjectId().equals(objects.get(i).getObjectId())){
							isJoin=true;
							break;
						}
						
					}
					
				}else{
					log.e("zcq","活动参加的人获取失败");
				}
			}
		});
	}

}
