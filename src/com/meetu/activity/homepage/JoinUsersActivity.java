package com.meetu.activity.homepage;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.lidroid.xutils.BitmapUtils;
import com.meetu.R;
import com.meetu.R.layout;
import com.meetu.R.menu;
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

public class JoinUsersActivity extends Activity implements OnItemClickListener,OnClickListener{
	//控件相关
	private ListView mlistViewAll,mListViewFavor;
	private JoinUserAdapter adapter;
	private List<User> joinUsersList=new ArrayList<User>();
//	private List<User> joinUsersFavorList=new ArrayList<User>();
	private JoinUserFavorAdapter adapterfavor;
	private float itemHight=(float) 60.5;//item高度加下划线高度
	private TextView numberFavor,numberAll;
	private RelativeLayout backLayout,signLayout;
	private ImageView signImageView;
	private ImageView myPHotoHead,sexPhoto;
	private TextView userName,userSchool,numberJoin;
	
	//网络数据相关
	private ActivityBean activityBean=new ActivityBean();
	private ObjActivity objActivity= null;
	private BitmapUtils bitmapUtils;
	private FinalBitmap finalBitmap;
	//当前用户
	ObjUser user = new ObjUser();
	AVUser currentUser = AVUser.getCurrentUser();
	//参加活动用户列表
	ArrayList<ObjUser> userList = new ArrayList<ObjUser>();
	private ArrayList<ObjUser> userFavorList= new ArrayList<ObjUser>();
	
	private ActivityDao actyDao;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//全屏
		super.getWindow();
		setContentView(R.layout.activity_join_users);
		if (currentUser != null) {
			//强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		bitmapUtils=new BitmapUtils(this);
		MyApplication app=(MyApplication) this.getApplication();
		finalBitmap=app.getFinalBitmap();
		actyDao=new ActivityDao(this);
		//接收传过来的数据
		activityBean=(ActivityBean) getIntent().getExtras().getSerializable("activityBean");
		
		log.e("lucifer", "activityBean.getActyId()=="+activityBean.getActyId());
		
		initLoadActivity(activityBean.getActyId());
		
		
		loadData();
		loadData2();
		mlistViewAll=(ListView) super.findViewById(R.id.listview_all_joinUsers);
		mListViewFavor=(ListView) super.findViewById(R.id.listview_favor_joinUser);
		adapter=new JoinUserAdapter(this, userList);
		
		mlistViewAll.setAdapter(adapter);
		mlistViewAll.setOnItemClickListener(this);
		adapterfavor=new JoinUserFavorAdapter(this, userFavorList);
		mListViewFavor.setAdapter(adapterfavor);
		
		
		initView();
	}

	

	private void loadData2() {
//		joinUsersFavorList=new ArrayList<User>();
//		User item=new User();
//		item.setName("刘亦菲");
//		item.setSchool("北京大学");
//		item.setSex("女");
//		item.setHeadPhoto(R.drawable.mine_likelist_profile_default);
//		joinUsersFavorList.add(item);
//		
//		User item1=new User();
//		item1.setName("李连杰");
//		item1.setSchool("清华大学");
//		item1.setSex("男");
//		item1.setHeadPhoto(R.drawable.mine_likelist_profile_default);
//		joinUsersFavorList.add(item1);
		
	}



	private void initView() {
		//动态设置listview 高度
		RelativeLayout.LayoutParams rlAll=(LayoutParams) mlistViewAll.getLayoutParams();
		rlAll.height=DensityUtil.dip2px(this,itemHight)*userList.size();
		mlistViewAll.setLayoutParams(rlAll);
		
		RelativeLayout.LayoutParams rlfavor=(LayoutParams) mListViewFavor.getLayoutParams();
		rlfavor.height=DensityUtil.dip2px(this, itemHight)*userFavorList.size()+40;
		mListViewFavor.setLayoutParams(rlfavor);
		
		log.e("lucifer", "itemHight="+itemHight);
		numberFavor=(TextView) super.findViewById(R.id.number_favor_joinUser);
		numberFavor.setText("("+userFavorList.size()+")");
		numberAll=(TextView) super.findViewById(R.id.number_all_joinUser);
		numberAll.setText("("+userList.size()+")");
		//点击事件处理
		backLayout=(RelativeLayout) super.findViewById(R.id.back_joinUsers_homepager_rl);
		backLayout.setOnClickListener(this);
		signLayout=(RelativeLayout) super.findViewById(R.id.sign_joinUsers_homepager_rl);
		signLayout.setOnClickListener(this);
		signImageView=(ImageView) super.findViewById(R.id.sign_joinUsers_homepager_img);
		myPHotoHead=(ImageView) super.findViewById(R.id.photo_head_my_join_users_huodong_tv);
		
		finalBitmap.display(myPHotoHead, user.getProfileClip().getUrl());
		
		userName=(TextView) super.findViewById(R.id.name_user_huodong_join_tv);
		//TODO 此处应该是报名后的真实名字
		userName.setText(user.getNameNick());
		userSchool=(TextView) super.findViewById(R.id.school_user_huodong_join_tv);
		userSchool.setText(user.getSchool());
		sexPhoto=(ImageView) super.findViewById(R.id.user_sex_huodong_join_img);
		if(user.getGender()==2){
			sexPhoto.setImageResource(R.drawable.acty_joinlist_img_female);
		}else{
			
		}
		numberJoin=(TextView) super.findViewById(R.id.number_join_huodong_tv);
		
	}
	
	private void loadData() {
//		joinUsersList=new ArrayList<User>();
//		User item=new User();
//		item.setName("刘亦菲");
//		item.setSchool("北京大学");
//		item.setSex("女");
//		item.setHeadPhoto(R.drawable.mine_likelist_profile_default);
//		joinUsersList.add(item);
//		
//		User item1=new User();
//		item1.setName("李连杰");
//		item1.setSchool("清华大学");
//		item1.setSex("男");
//		item1.setHeadPhoto(R.drawable.mine_likelist_profile_default);
//		joinUsersList.add(item1);
//		
//		User item2=new User();
//		item2.setName("赵子龙");
//		item2.setSchool("北京大学");
//		item2.setSex("男");
//		item2.setHeadPhoto(R.drawable.mine_likelist_profile_default);
//		joinUsersList.add(item2);
//		
//		User item3=new User();
//		item3.setName("赵子龙");
//		item3.setSchool("北京大学");
//		item3.setSex("男");
//		item3.setHeadPhoto(R.drawable.mine_likelist_profile_default);
//		joinUsersList.add(item3);
//		
//		User item4=new User();
//		item4.setName("赵子龙");
//		item4.setSchool("北京大学");
//		item4.setSex("男");
//		item4.setHeadPhoto(R.drawable.mine_likelist_profile_default);
//		joinUsersList.add(item4);
//		
//		
//		User item5=new User();
//		item5.setName("赵子龙");
//		item5.setSchool("北京大学");
//		item5.setSex("男");
//		item5.setHeadPhoto(R.drawable.mine_likelist_profile_default);
//		joinUsersList.add(item5);
		
		queryOrderUsers(objActivity);
		
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back_joinUsers_homepager_rl :
				finish();
				
				break;
			case R.id.sign_joinUsers_homepager_rl:
				signImageView.setImageResource(R.drawable.acty_joinlist_img_checkedin);
				break;

			default :
				break;
		}
	}
	
	//查询参加活动列表  setOrderUsers
	public void queryOrderUsers(final ObjActivity activity){
		 
				ObjActivityOrderWrap.queryActivitySignUp(activity, new ObjUserCallback() {
					
					@Override
					public void callback(List<ObjUser> objects, AVException e) {
						if(e!=null){
							log.e("zcq", e);
							return ;
						}else if(objects!=null){
							userList.addAll(objects);
							log.e("zcq", "userList=="+userList.size());
							queryFollowAndOrder(activity);
							
							handler.sendEmptyMessage(1);
							log.e("zcq", "objects=="+objects.size());
						}
						
					}
				});

			}
			
			//获取参加活动并且我关注的人  setOrderAndFollow
			public void queryFollowAndOrder(ObjActivity activity){
				ArrayList<ObjUser> followUsers = new ArrayList<ObjUser>();
				ObjFollowWrap.myFollow(userList, user, new ObjUserCallback() {
					
					@Override
					public void callback(List<ObjUser> objects, AVException e) {
						if(e!=null){
							log.e("zcq",e);
						}else if(objects!=null){
							userFavorList.addAll(objects);
							log.e("zcq", "userFavorList=="+userList.size());
							handler.sendEmptyMessage(1);
						}
						
					}
				});
			}

	
			
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				log.e("lucifer", "shuaxin");
				adapterfavor.notifyDataSetChanged();
				initView();
				break;

			default:
				break;
			}
			
		}
	
			};
			/**
			 * 获得活动的activity
			 * @param activityId  
			 * @author lucifer
			 * @date 2015-11-13
			 */
			public void initLoadActivity(String activityId) {
				log.e("zcq", "activityId=="+activityId);
					try {
						 objActivity=AVObject.createWithoutData(ObjActivity.class, activityId);

					} catch (AVException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}



}
