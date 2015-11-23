package com.meetu.activity.miliao;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;

import com.meetu.R;
import com.meetu.R.drawable;
import com.meetu.R.id;
import com.meetu.R.layout;
import com.meetu.adapter.GridRecycleMiLiaoInfoAdapter;
import com.meetu.adapter.GridRecycleMiLiaoInfoAdapter.OnMiLiaoInfoItemClickCallBack;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;

import com.meetu.entity.User;
import com.meetu.entity.UserAbout;
import com.meetu.sqlite.UserAboutDao;
import com.meetu.tools.DensityUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.GridLayoutManager;

import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MiLiaoInfoActivity extends Activity implements OnClickListener,OnMiLiaoInfoItemClickCallBack{
	//控件相关
	private TextView numberAll;
	private Switch switch1;
	private RelativeLayout backLayout;
	private RecyclerView mRecyclerView;
	private GridRecycleMiLiaoInfoAdapter mAdapter;
	private List<UserAbout> mlist=new ArrayList<UserAbout>();
	private List<UserAbout> mList2=new ArrayList<UserAbout>();//存储新的list
	private RecyclerView.LayoutManager mLayoutManager; 
	private int mHight;//reclycle 高度
	private Boolean detele=false;//记录是否是删除状态
	
	//网络数据相关
	
	private String conversationStyle;//对话类型，1 表示活动群聊  2表示觅聊    3表示单聊 暂时没有
	private String conversationId;//对话id
	private List<ObjUser> objUsersList=new ArrayList<ObjUser>();
	private UserAboutDao userAboutDao;
	//会话成员列表
	List<ObjUser> userList = new ArrayList<ObjUser>();
	private List<UserAboutBean> beanList=new ArrayList<UserAboutBean>();
	//当前用户
	private	ObjUser user = new ObjUser();
	AVUser currentUser = AVUser.getCurrentUser();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//全屏
		super.getWindow();
		setContentView(R.layout.activity_mi_liao_info);
		Intent intent=getIntent();
		conversationStyle=intent.getStringExtra("ConversationStyle");
		conversationId=intent.getStringExtra("ConversationId");
		userAboutDao=new UserAboutDao(this);
		if (currentUser != null) {
			//强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}else{
			return;
		}
		loadData();
		
//		loadData2();
		initView();
	}

	private void loadData2() {
		// TODO Auto-generated method stub
//		for(UserAbout user:mlist){
//			user.setIsDetele(false);
//			mList2.add(user);
//		}
		if(mlist!=null){
			mList2.addAll(mlist);
		}
		UserAbout item=new UserAbout();		
		item.setUserName("移除");
		item.setIsDetele(false);
		item.setDeleteImg(R.drawable.massage_groupchatmember_btn_remove);
		mList2.add(item);
		
		
	}

	private void loadData() {
		// TODO Auto-generated method stub
//		mlist=new ArrayList<User>();
//		User item=new User();
//		item.setName("张三");
//		item.setUserid("1");		
//		item.setHeadPhoto(R.drawable.chat_img_sponsor_profiles_default);
//		mlist.add(item);
//		
//		User item2=new User();
//		item2.setName("里四1");
//		item2.setHeadPhoto(R.drawable.chat_img_sponsor_profiles_default);
//		mlist.add(item2);
//		
//		User item3=new User();
//		item3.setName("里四2");
//		item3.setHeadPhoto(R.drawable.chat_img_sponsor_profiles_default);
//		mlist.add(item3);
//		
//		User item4=new User();
//		item4.setName("里四3");
//		item4.setHeadPhoto(R.drawable.chat_img_sponsor_profiles_default);
//		mlist.add(item4);
//		
//		User item5=new User();
//		item5.setName("里四4");
//		item5.setHeadPhoto(R.drawable.chat_img_sponsor_profiles_default);
//		mlist.add(item5);
//		
		// 加载网络数据
		objUsersList=new ArrayList<ObjUser>();
		if(conversationStyle.equals("1")){
			//活动群聊
			
			beanList=userAboutDao.queryUserAbout(user.getObjectId(), Constants.ACTIVITY_TYPE, conversationId);
		}else if(conversationStyle.equals("2")){
			//觅聊群聊
			beanList=userAboutDao.queryUserAbout(user.getObjectId(), Constants.CONVERSATION_TYPE, conversationId);
			
		}
		log.e("zcq", "beanList=="+beanList);
		
		if(beanList!=null){
			getUsersListInfo(beanList);
		}
		
		numberAll.setText(""+beanList.size());
		
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		backLayout=(RelativeLayout) super.findViewById(R.id.back_miliao_info_img_rl);
		backLayout.setOnClickListener(this);
		switch1=(Switch) super.findViewById(R.id.switch1_miliao_info);
		switch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
			
                if (isChecked) {  
                    log.e("lucifer"+"打开");//打开消息
                } else {  
                    //关闭消息
                	 log.e("lucifer"+"关闭");
                }  
				
			}
		});
		
		mRecyclerView=(RecyclerView) super.findViewById(R.id.recycleview_miliao_info);
		
//		LinearLayout.LayoutParams params=(LayoutParams) mRecyclerView.getLayoutParams();
//		if(mlist.size()!=0&&(mlist.size()+1)%4==0){
//			mHight=(((mlist.size()+1)/4))*DensityUtil.dip2px(this, 65);	
//		}else{
//			mHight=(((mlist.size()+1)/4)+1)*DensityUtil.dip2px(this, 65);
//		}
//		params.height=mHight;
//		mRecyclerView.setLayoutParams(params);
		setRecycleviewHight();
		
		mLayoutManager = new LinearLayoutManager(this);  
 //       mRecyclerView.setLayoutManager(mLayoutManager); 
		mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
		mAdapter=new GridRecycleMiLiaoInfoAdapter(this,mList2);
		
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setAdapter(mAdapter);
		
//		mAdapter.setOnItemClickLitenerBack(this);
		
		
		mAdapter.setOnItemClickLitenerBack(this);
		
		numberAll=(TextView) super.findViewById(R.id.numberAll_user_miliao_info);
		
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.back_miliao_info_img_rl:
			finish();
			break;
		
		
		}
		
	}

	@Override
	public void onItemClick(int position) {
		// TODO Auto-generated method stub
		if(detele==false){
			
			if(position==mList2.size()-1){
				Toast.makeText(this, "点击了删除。进入删除模式"+position, Toast.LENGTH_SHORT).show();
				detele=true;
				mList2.remove(mList2.size()-1);
				for(UserAbout user:mList2){
					user.setIsDetele(true);
				}
		
				UserAbout item=new UserAbout();				
				item.setUserName("取消");
				item.setIsDetele(false);
				item.setDeleteImg(R.drawable.massage_groupchatmember_btn_cancel);
				mList2.add(item);
				
				
			}else{
				
				Toast.makeText(this, "点击了位置"+position, Toast.LENGTH_SHORT).show();
			}
		}else{
			if(position==mList2.size()-1){
				//取消删除
				detele=false;
				
				mList2.remove(mList2.size()-1);
				for(UserAbout user:mList2){
					user.setIsDetele(false);
				}
				UserAbout item=new UserAbout();
				item.setUserName("移除");
				item.setIsDetele(false);
				item.setDeleteImg(R.drawable.massage_groupchatmember_btn_remove);
				mList2.add(item);
				
			}else{
				mList2.remove(position);
			}
			
		}
		
		
		
		handler.sendEmptyMessage(1);
	}

	@Override
	public void onItemLongClick(View view, int position) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 更新UI、
	 */
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			switch(msg.what){
			case 1:
				
				setRecycleviewHight();
				mAdapter.notifyDataSetChanged();
				
			
				break;
			
			}
		
		}
		
		
	};
	/**
	 * 设置 recycleview高度
	 */
	public void setRecycleviewHight(){
		RelativeLayout.LayoutParams params= (android.widget.RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams();
		if(mList2.size()!=0&&(mList2.size())%4==0){
			mHight=(((mList2.size())/4))*DensityUtil.dip2px(this, 65);	
		}else{
			mHight=(((mList2.size())/4)+1)*DensityUtil.dip2px(this, 65);
		}
		params.height=mHight;
		mRecyclerView.setLayoutParams(params);
	}

	/**
	 * 获得群员列表
	 * @param objId  
	 * @author lucifer
	 * @date 2015-11-17
	 */
	private void getUsersListInfo(final List<UserAboutBean> list){
		userList=new ArrayList<ObjUser>();
		
		for(int i=0;i<list.size();i++){
			ObjUserWrap.getObjUser(list.get(i).getAboutUserId(), new ObjUserInfoCallback() {
				
				@Override
				public void callback(ObjUser user, AVException e) {
					if(e!=null){
						log.e("zcq", e);
						return;
					}else if(user!=null){
						log.e("zcq","群员信息获取成功");
						
						UserAbout item=new UserAbout();
						item.setUserId(user.getObjectId());
						item.setUserName(user.getNameNick());
						item.setIsDetele(false);
						if(user.getProfileClip()!=null){
							item.setUserHeadPhotoUrl(user.getProfileClip().getUrl());
						}
						
						mlist.add(item);
						
						
						userList.add(user);	
						if(list.size()==mlist.size()){
							//所有的都加载过后刷新数据
							loadData2();
							handler.sendEmptyMessage(1);
						}
					}

				}
			});
		}

		
	}
	

}
