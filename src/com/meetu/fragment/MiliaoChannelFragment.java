package com.meetu.fragment;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.meetu.R;
import com.meetu.R.id;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.callback.ObjListCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjChat;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.UserAboutDao;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MiliaoChannelFragment extends Fragment {
	//控件相关
	private TextView dismissData;
	private ImageView photoManager,backgroud;
	private TextView titile;
	private TextView numberAll,numberFavor,numberUserAll;
	private View view;
	private ImageView oneUser,twoUser,threeUser,fourUser,fiveUser;
	
	//网络数据相关
	private ObjChat  objChat=new ObjChat();
	private FinalBitmap finalBitmap;
	private AVIMConversation conv;
	//会话成员列表
	ArrayList<ObjUser> memberUserList = new ArrayList<ObjUser>();
	private UserAboutDao userAboutDao;//成员缓存
	AVUser currentUser = ObjUser.getCurrentUser();
	ObjUser user = new ObjUser();
	private ArrayList<UserAboutBean> userAboutBeanList=new ArrayList<UserAboutBean>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(view==null){
			view=inflater.inflate(R.layout.fragment_miliao_channel, null);
			if(currentUser!=null){
				user = AVUser.cast(currentUser, ObjUser.class);
			}
			
			userAboutDao=new UserAboutDao(getActivity());
			objChat=(ObjChat) getArguments().get("ObjChat");
			log.e("zcq","objChat=="+objChat.getObjectId()+"  objChat.getConversationId=="+objChat.getConversationId());
			MyApplication app=(MyApplication) getActivity().getApplicationContext();
			finalBitmap=app.getFinalBitmap();
			conv = MyApplication.chatClient.getConversation(objChat.getConversationId());
			initView();
			getUserInfo(""+objChat.getUser().getObjectId());
			getMembers(conv);
		}
		ViewGroup parent=(ViewGroup) view.getParent();
		if(parent!=null){
			parent.removeView(view);
		}
		return view;
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		dismissData=(TextView) view.findViewById(R.id.date_top_miliao_channel_tv);
		photoManager=(ImageView) view.findViewById(R.id.photoHead_manage_miliao_channel_img);
		titile=(TextView) view.findViewById(R.id.content_center_miliao_channel_tv);
		numberAll=(TextView) view.findViewById(R.id.numberAll_miliao_channel);
		numberFavor=(TextView) view.findViewById(R.id.numberFavor_miliao_channel);
		backgroud=(ImageView) view.findViewById(R.id.backgroud_miliao_channel_img);
		if(objChat.getChatPicture()!=null){
			finalBitmap.display(backgroud,objChat.getChatPicture().getUrl());
		}
		
		titile.setText(""+objChat.getChatTitle());
		
		//参加觅聊的人
		oneUser=(ImageView) view.findViewById(R.id.one_photoHead_channel_img);
		twoUser=(ImageView) view.findViewById(R.id.two_photoHead_channel_img);
		threeUser=(ImageView) view.findViewById(R.id.three_photoHead_channel_img);
		fourUser=(ImageView) view.findViewById(R.id.four_photoHead_channel_img);
		fiveUser=(ImageView) view.findViewById(R.id.five_photoHead_channel_img);
		//觅聊总人数头像
		numberUserAll=(TextView) view.findViewById(R.id.number_user_fragment_miliao_channel_tv);
		
		
	}
	/**
	 * 根据创建者用户id 获取用户相关信息
	 * @param objId  
	 * @author lucifer
	 * @date 2015-11-17
	 */
	private void getUserInfo(String objId){
		ObjUserWrap.getObjUser(objId, new ObjUserInfoCallback() {
			
			@Override
			public void callback(ObjUser user, AVException e) {
				
				finalBitmap.display(photoManager, user.getProfileClip().getUrl());
			}
		});
	}
	
	/**
	 * 获取会话成员
	 */
		public void getMembers(AVIMConversation conv){
			ObjChatMessage.getChatMembers(conv,new ObjListCallback() {

				@Override
				public void callback(ArrayList<String> list, AVException e) {
					// TODO Auto-generated method stub
					
					if(e!=null){
					
						log.e("zcq", e);
						return;
					}else if(list.size()!=0){
						log.e("zcq","群员列表获取成功");
						log.e("zcq","list.size()=="+list.size());
						numberUserAll.setText(""+list.size());
						
						userAboutBeanList=new ArrayList<UserAboutBean>();
						for(String userId:list){
							
							getUsersListInfo(list);
							
							UserAboutBean userAboutBean=new UserAboutBean();
							userAboutBean.setUserId(""+user.getObjectId());
							userAboutBean.setAboutType(Constants.CONVERSATION_TYPE);
							userAboutBean.setAboutUserId(""+userId);
							userAboutBean.setAboutColetctionId(objChat.getConversationId());
							userAboutBeanList.add(userAboutBean);
						}
						
						userAboutDao.deleteByType(user.getObjectId(), Constants.CONVERSATION_TYPE, objChat.getConversationId());
						userAboutDao.saveUserAboutList(userAboutBeanList);  
						
				
					}
				}
			});
		}
		/**
		 * 获得群员列表
		 * @param objId  
		 * @author lucifer
		 * @date 2015-11-17
		 */
		private void getUsersListInfo(final ArrayList<String> list){
			memberUserList=new ArrayList<ObjUser>();
			
			for(int i=0;i<list.size();i++){
				ObjUserWrap.getObjUser(list.get(i), new ObjUserInfoCallback() {
					
					@Override
					public void callback(ObjUser user, AVException e) {
						if(e!=null){
							log.e("zcq", e);
							return;
						}else if(user!=null){
							log.e("zcq","群员信息获取成功");
							memberUserList.add(user);
							
							
							if(memberUserList.size()==list.size()){
								log.e("zcq", "设置头像");
								if(list.size()>=5){
									finalBitmap.display(fiveUser, memberUserList.get(4).getProfileClip().getUrl());
								} if(list.size()>=4){
									finalBitmap.display(fourUser, memberUserList.get(3).getProfileClip().getUrl());
								}if(list.size()>=3){
									finalBitmap.display(threeUser, memberUserList.get(2).getProfileClip().getUrl());
								}if(list.size()>=2){
									finalBitmap.display(twoUser, memberUserList.get(1).getProfileClip().getUrl());
								}if(list.size()>=1){
									finalBitmap.display(oneUser, memberUserList.get(0).getProfileClip().getUrl());
								}
							}
							
						}

					}
				});
			}

			
		}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				
				
				break;

			default:
				break;
			}
		}
		
	};
	
	

	

}
