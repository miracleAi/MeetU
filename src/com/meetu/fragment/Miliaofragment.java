package com.meetu.fragment;




import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.meetu.R;
import com.meetu.activity.miliao.ApplyForMiLiaoActivity;
import com.meetu.activity.miliao.ChatGroupActivity;
import com.meetu.activity.miliao.CreationChatActivity;
import com.meetu.adapter.BoardPageFragmentAdapter;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.callback.ObjAuthoriseApplyCallback;
import com.meetu.cloud.callback.ObjAuthoriseCategoryCallback;
import com.meetu.cloud.callback.ObjAvimclientCallback;
import com.meetu.cloud.callback.ObjChatCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjAuthoriseApply;
import com.meetu.cloud.object.ObjAuthoriseCategory;
import com.meetu.cloud.object.ObjChat;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjAuthoriseWrap;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjChatWrap;
import com.meetu.common.Constants;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.UserAboutDao;
import com.meetu.tools.DepthPageTransformer;
import com.meetu.tools.MyZoomOutPageTransformer;
import com.meetu.tools.ZoomOutPageTransformer;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Miliaofragment extends Fragment implements OnPageChangeListener,OnClickListener{
	//viewpager相关
	private View view;
	private ViewPager viewPager;
	private BoardPageFragmentAdapter adapter=null;
	private List<Fragment> fragmentList=new ArrayList<Fragment>();
	
	//控件相关
	private TextView numberAll,numberPosition;
	private RelativeLayout addLayout,joinLayout;
	
	private int positonNow=0;//当前在第几个页面
	
	private AVIMConversation conv;
	
	//网络数据相关
	private List<ObjChat> objChatsList=new ArrayList<ObjChat>();
	AVUser currentUser = ObjUser.getCurrentUser();
	ObjUser user = new ObjUser();
	//权限
		ObjAuthoriseCategory category = new ObjAuthoriseCategory();
		ObjAuthoriseApply apply=new ObjAuthoriseApply();
	private UserAboutDao userAboutDao;
	ArrayList<UserAboutBean>	userAboutBeansList=new ArrayList<UserAboutBean>();
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view==null){
			view=inflater.inflate(R.layout.fragment_miliao, null);
			if(currentUser!=null){
				user = AVUser.cast(currentUser, ObjUser.class);
			}
			userAboutDao=new UserAboutDao(getActivity());
			//获取觅聊列表
			getObjChatList();
			
			viewPager=(ViewPager)view.findViewById(R.id.vpNewsList_miliao);	
			//设置viewpage的切换动画
			viewPager.setPageTransformer(true, new MyZoomOutPageTransformer());
			
			viewPager.setOnPageChangeListener(this);
	//		viewPager.setOffscreenPageLimit(3);
			numberAll=(TextView) view.findViewById(R.id.numberAll_miliao);	
			numberPosition=(TextView) view.findViewById(R.id.numberPosition_miliao);
			addLayout=(RelativeLayout) view.findViewById(R.id.add_miliao_rl);
			joinLayout=(RelativeLayout) view.findViewById(R.id.join_miliao_rl);
			addLayout.setOnClickListener(this);
			joinLayout.setOnClickListener(this);
			
			
			initView();
		}
		ViewGroup parent=(ViewGroup) view.getParent();
		if(parent!=null){
			parent.removeView(view);
		}
		return view;
	}

/**
 * 控件相关处理
 */
	private void initView() {
		
		
		addLayout.setOnClickListener(this);
		joinLayout.setOnClickListener(this);
		//TODO 此处应设置为卡片的总数量。
		
		
		numberPosition.setText(""+1);
	}
/**
 * viewpager 相关处理
 */

	private void initViewPager() {
		// TODO 加载网络数据后设置 viewpager数据 
		fragmentList=new ArrayList<Fragment>();
		for(int i=0;i<objChatsList.size();i++){
		MiliaoChannelFragment frag=new MiliaoChannelFragment();
		Bundle bundle=new Bundle();
		bundle.putSerializable("ObjChat", objChatsList.get(i));
		frag.setArguments(bundle);
		fragmentList.add(frag);
		}
		
		adapter=new BoardPageFragmentAdapter(super.getActivity().getSupportFragmentManager(), fragmentList);
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setCurrentItem(0);
		 
		
	}


	@Override
	public void onPageScrollStateChanged(int position) {
		// TODO Auto-generated method stub
//		log.e("lucifer", "position="+position);		
	}


	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		log.e("lucifer", "arg0="+arg0 +" arg1="+arg1+ "arg2="+arg2);
	}


	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		log.e("lucifer", "position="+position);
		numberPosition.setText(""+(position+1));
		//传过去当前页面
		positonNow=position;
		
		conv = MyApplication.chatClient.getConversation(""+objChatsList.get(positonNow).getConversationId());
//		initViewPager();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//申请觅聊
		case R.id.add_miliao_rl:
			queryAuthoriseCategory(100);
			
			
			break;
		//加入觅聊
		case R.id.join_miliao_rl:
			//TODO 使用测试 clientId  ConversationId  进行对话
//			
			if(isJoin(objChatsList.get(positonNow).getConversationId())==true){
				log.e("zcq","已经加入过当前觅聊");
				
				Intent intent2=new Intent(getActivity(),ChatGroupActivity.class);
				intent2.putExtra("ConversationId", ""+objChatsList.get(positonNow).getConversationId());
				//传对话的类型   1 表示活动群聊 2 表示觅聊  3 表示单聊
				intent2.putExtra("ConversationStyle", ""+2);
				startActivity(intent2);
				
			}else{
		
				
				if(MyApplication.isChatLogin){
					//已经建立了长连接
					log.e("zcq", "已经建立过长连接");
					joinGroup(conv);
				}else{
					ObjChatMessage.connectToChatServer(MyApplication.chatClient, new ObjAvimclientCallback() {
	
						@Override
						public void callback(AVIMClient client, AVException e) {							
							if(e != null){
								log.e("zcq", e);
								return ;
							}
							if(client != null){
								MyApplication.chatClient = client;
								log.e("zcq", "连接聊天长连接成功");
								joinGroup(conv);
							}else{
								log.e("zcq", "连接聊天长连接失败");
								Toast.makeText(getActivity(), "请检查网络重新加入", 1000).show();
							}
						}
					});
				}
			}

			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 获取觅聊列表
	 *   
	 * @author lucifer
	 * @date 2015-11-16
	 */
	private void getObjChatList(){
		ObjChatWrap.queryChatList(new ObjChatCallback() {
			
			@Override
			public void callback(List<ObjChat> objects, AVException e) {
				if(e!=null){
					log.e("zcq", e);
					return;
				}else if(objects!=null){
					objChatsList.clear();
					objChatsList.addAll(objects);
					log.e("zcq", "objChatsList=="+objChatsList.size()+" objects=="+objects.size());
					numberAll.setText(""+objChatsList.size());
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
				initViewPager();
				break;

			default:
				break;
			}
		}
	
	};
	//查询是否有权限
			public void queryHaveAuthorise(final ObjAuthoriseCategory category){
				ObjAuthoriseWrap.queryUserAuthorise(category,user, new ObjFunBooleanCallback() {

					@Override
					public void callback(boolean result, AVException e) {
						// TODO Auto-generated method stub
						if(e != null){
							log.e("zcq", e);
							return;
						}
						if(result){
						//	clickBtn.setText(UPLOADPIC);
							log.e("zcq", "有权限");
							
						}else{
						//	clickBtn.setText(ISAPPLY);
							log.e("zcq", "木有权限");

							Intent intent=new Intent(getActivity(),ApplyForMiLiaoActivity.class);
							Bundle bundle=new Bundle();
							bundle.putSerializable("category", category);
							intent.putExtras(bundle);
							startActivity(intent);
						}
					}
				});
			}
			
			//查询是否需要权限
			public void queryAuthoriseCategory(int operationNum){
				ObjAuthoriseWrap.queryAuthoriseCatogory(operationNum, new ObjAuthoriseCategoryCallback() {
					@Override
					public void callback(List<ObjAuthoriseCategory> objects, AVException e) {
						// TODO Auto-generated method stub
						if(e != null){
							log.e("zcq", e);
							return;
						}else if(objects.size() ==  0){
							log.e("zcq", "数据加载失败");
							return;
						}else{
							category = objects.get(0);
							if(category.isNeedAuthorise()){
					
								//queryHaveAuthorise(category);
								queryIsApply(category);
							}else{
						
								
							}
						}
						
					}
				});
			}
			//查询是否已申请
			public void queryIsApply(final ObjAuthoriseCategory category){
				ObjAuthoriseWrap.queryApply(user, category, new ObjAuthoriseApplyCallback() {

					@Override
					public void callback(List<ObjAuthoriseApply> objects, AVException e) {
						// TODO Auto-generated method stub
						if(e != null){
						log.e("zcq", e);
							return;
						}
						if(objects.size() == 0){
							//没申请过
							log.e("zcq", "木有权限");
							Intent intent=new Intent(getActivity(),ApplyForMiLiaoActivity.class);
							startActivity(intent);
						}else{
							//申请过
							apply = objects.get(0);
						if(apply.getApplyResult()==2){
							//表示有权限创建觅聊
							log.e("zcq", "有权限");
							
							Intent intent=new Intent(getActivity(),CreationChatActivity.class);
							startActivityForResult(intent, 100);
						}else{
							log.e("zcq", "木有权限");
							Intent intent=new Intent(getActivity(),ApplyForMiLiaoActivity.class);
							startActivity(intent);
						}
						}
					}
				});
			}


			@Override
			public void onActivityResult(int requestCode, int resultCode,
					Intent data) {
				switch (requestCode) {
				case 100:
					if(resultCode==getActivity().RESULT_OK){
						//TODO 创建 觅聊成功  应刷新viewpager中数据
						log.e("zcq", "回传成功，该刷新viewpager数据了");
					}
					
					break;

				default:
					break;
				}
			}
			
			//加入当前觅聊
			public void joinGroup(AVIMConversation conversation){
				ObjChatMessage.joinChat(MyApplication.chatClient, conversation, new ObjFunBooleanCallback() {

					@Override
					public void callback(boolean result, AVException e) {
						// TODO Auto-generated method stub
						if(e != null){
							log.e("zcq",e);
							return ;
						}
						if(result){
							log.e("zcq","加入觅聊成功");
							
							Intent intent2=new Intent(getActivity(),ChatGroupActivity.class);
							intent2.putExtra("ConversationId", ""+objChatsList.get(positonNow).getConversationId());
							//传对话的类型   1 表示活动群聊 2 表示觅聊  3 表示单聊
							intent2.putExtra("ConversationStyle", ""+2);
							startActivity(intent2);
							//TODO 应该只刷新成员 省流量 
							handler.sendEmptyMessage(1);
						}else{
							log.e("zcq","加入觅聊失败 ，请检查网络");
						}
					}
				});
			}
		/**
		 * 判断是否加入过当前觅聊	
		 * @return  
		 * @author lucifer
		 * @date 2015-11-18
		 */
		private boolean isJoin(String colectionId){
			
			userAboutBeansList=new ArrayList<UserAboutBean>();
			userAboutBeansList.addAll(userAboutDao.queryUserAbout(user.getObjectId(), Constants.CONVERSATION_TYPE, colectionId));
			for(UserAboutBean userAboutBean :userAboutBeansList){
				if(user.getObjectId().equals(userAboutBean.getAboutUserId())){
					
					return true;
				}
			}
			
			return false;
			
		}
			

}
