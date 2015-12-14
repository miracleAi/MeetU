package com.meetu.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.meetu.activity.miliao.ApplyForMiLiaoActivity;
import com.meetu.activity.miliao.ChatGroupActivity;
import com.meetu.activity.miliao.CreationChatActivity;
import com.meetu.adapter.BoardPageFragmentAdapter;
import com.meetu.bean.SeekChatBean;
import com.meetu.bean.SeekChatInfoBean;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.callback.ObjAuthoriseApplyCallback;
import com.meetu.cloud.callback.ObjAuthoriseCategoryCallback;
import com.meetu.cloud.callback.ObjAvimclientCallback;
import com.meetu.cloud.callback.ObjChatCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjFunMapCallback;
import com.meetu.cloud.object.ObjAuthoriseApply;
import com.meetu.cloud.object.ObjAuthoriseCategory;
import com.meetu.cloud.object.ObjChat;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjAuthoriseWrap;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjChatWrap;
import com.meetu.common.Constants;
import com.meetu.common.PerfectInformation;
import com.meetu.entity.Chatmsgs;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.ChatmsgsDao;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Miliaofragment extends Fragment implements OnPageChangeListener,
OnClickListener {
	// viewpager相关
	private View view;
	private ViewPager viewPager;
	private BoardPageFragmentAdapter adapter = null;
	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	// 控件相关
	private TextView numberAll, numberPosition;
	private RelativeLayout addLayout, joinLayout;

	private int positonNow = 0;// 当前在第几个页面

	private AVIMConversation conv;



	// 网络数据相关
	private List<ObjChat> objChatsList = new ArrayList<ObjChat>();
	AVUser currentUser = ObjUser.getCurrentUser();
	ObjUser user = new ObjUser();
	// 权限
	ObjAuthoriseCategory category = new ObjAuthoriseCategory();
	ObjAuthoriseApply apply = new ObjAuthoriseApply();
	private UserAboutDao userAboutDao;
	ArrayList<UserAboutBean> userAboutBeansList = new ArrayList<UserAboutBean>();

	SeekChatInfoBean chatBean = null;

	private List<SeekChatBean> seekChatBeansList = new ArrayList<SeekChatBean>();

	private Boolean isAdd = false;

	ChatmsgsDao chatmsgsDao;
	
	List<Boolean> isAddList=new ArrayList<Boolean>();

	//空状态相关
	private RelativeLayout noneFailLayout;//空状态背景
	private TextView nonoTextView;//没有数据文字
	private TextView failTextView;//加载失败文字
	private ImageView miliaoImv;
	private TextView joinChatTv;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_miliao, null);
			if (currentUser != null) {
				user = AVUser.cast(currentUser, ObjUser.class);
			}
			chatmsgsDao=new ChatmsgsDao(getActivity());

			userAboutDao = new UserAboutDao(getActivity());
			// 获取觅聊列表
			// getObjChatList();

			noneFailLayout=(RelativeLayout) view.findViewById(R.id.none_or_fail_miliao_fragment_rl);
			nonoTextView=(TextView) view.findViewById(R.id.none_miliao_fragment_tv);
			failTextView=(TextView) view.findViewById(R.id.fail_miliao_fragment_tv);
			miliaoImv = (ImageView) view.findViewById(R.id.join_miliao_img);
			joinChatTv = (TextView) view.findViewById(R.id.join_chat_tv);

			loadData();

			viewPager = (ViewPager) view.findViewById(R.id.vpNewsList_miliao);
			// 设置viewpage的切换动画
			viewPager.setPageTransformer(true, new MyZoomOutPageTransformer());

			viewPager.setOnPageChangeListener(this);
			// viewPager.setOffscreenPageLimit(3);
			numberAll = (TextView) view.findViewById(R.id.numberAll_miliao);
			numberPosition = (TextView) view
					.findViewById(R.id.numberPosition_miliao);
			addLayout = (RelativeLayout) view.findViewById(R.id.add_miliao_rl);
			joinLayout = (RelativeLayout) view
					.findViewById(R.id.join_miliao_rl);
			addLayout.setOnClickListener(this);
			joinLayout.setOnClickListener(this);

			initView();
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
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
		// TODO 此处应设置为卡片的总数量。

		//		numberPosition.setText("" + 1);
	}

	/**
	 * viewpager 相关处理
	 */

	private void initViewPager() {
		// TODO 加载网络数据后设置 viewpager数据
		fragmentList.clear();
		fragmentList = new ArrayList<Fragment>();
		for (int i = 0; i < seekChatBeansList.size(); i++) {
			MiliaoChannelFragment frag = new MiliaoChannelFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("SeekChatBean", seekChatBeansList.get(i));
			frag.setArguments(bundle);
			fragmentList.add(frag);
		}

		adapter = new BoardPageFragmentAdapter(super.getActivity()
				.getSupportFragmentManager(), fragmentList);
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(0);
		viewPager.setCurrentItem(positonNow);
		isAddconvesition();
		conv = MyApplication.chatClient.getConversation(""
				+ seekChatBeansList.get(positonNow).getConversationId());
	}

	@Override
	public void onPageScrollStateChanged(int position) {
		// TODO Auto-generated method stub
		// log.e("lucifer", "position="+position);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		// log.e("lucifer", "arg0="+arg0 +" arg1="+arg1+ "arg2="+arg2);
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		log.e("lucifer", "position=" + position);
		numberPosition.setText("" + (position + 1));
		// 传过去当前页面
		positonNow = position;
		isAddconvesition();

		conv = MyApplication.chatClient.getConversation(""
				+ seekChatBeansList.get(positonNow).getConversationId());
		// initViewPager();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 申请觅聊
		case R.id.add_miliao_rl:
			// queryAuthoriseCategory(100);

			if(user.isCompleteUserInfo()){
				if (chatBean != null) {
					cteatMiliao();
				}
			}else{
				PerfectInformation.showDiolagPerfertInformation(getActivity(), "亲爱的 完善个人信息后才能和小伙伴们玩耍呢");
			}


			break;
			// 加入觅聊
		case R.id.join_miliao_rl:

			if(user.isCompleteUserInfo()){
				if (seekChatBeansList != null && seekChatBeansList.size() != 0) {
					if (isAdd) {
						miliaoImv.setImageResource(R.drawable.miliao_in);
						List<UserAboutBean> memList = userAboutDao.queryUserAbout(user.getObjectId(), 
								Constants.CONVERSATION_TYPE, seekChatBeansList.get(positonNow).getConversationId());
						if(memList.size()<5){
							joinChatTv.setText("等待开启");
							Toast.makeText(getActivity(), "觅聊尚未开启", 1000).show();
							return;
						}
						log.e("zcq", "已经加入过当前觅聊");
						joinChatTv.setText("进入觅聊");
						Intent intent2 = new Intent(getActivity(),
								ChatGroupActivity.class);
						intent2.putExtra("ConversationId", ""
								+ seekChatBeansList.get(positonNow)
								.getConversationId());
						// 传对话的类型 1 表示活动群聊 2 表示觅聊 3 表示单聊
						intent2.putExtra("ConversationStyle", "" + 2);
						intent2.putExtra("title",
								"" + seekChatBeansList.get(positonNow).getTitle());
						intent2.putExtra("number",
								""
										+ seekChatBeansList.get(positonNow)
										.getMembers().size());
						intent2.putExtra("objectId",
								seekChatBeansList.get(positonNow).getObjectId());// 觅聊id
						intent2.putExtra("TimeOver", ""+seekChatBeansList.get(positonNow).getTimeChatStop());

						log.e("zcq timeover", ""+seekChatBeansList.get(positonNow).getTimeChatStop());

						Bundle bundle = new Bundle();
						bundle.putSerializable("SeekChatBean",
								seekChatBeansList.get(positonNow));
						intent2.putExtras(bundle);
						startActivityForResult(intent2, 200);
					} else {
						log.e("zcq", "没加入过当前觅聊");
						miliaoImv.setImageResource(R.drawable.chat_navi_btn_joinchat);
						List<UserAboutBean> memList = userAboutDao.queryUserAbout(user.getObjectId(),
								Constants.CONVERSATION_TYPE,seekChatBeansList.get(positonNow).getConversationId());
						if(memList.size() >= 500){
							joinChatTv.setText("人数已满");
							Toast.makeText(getActivity(), "觅聊人数已满", 1000).show();
							return;
						}
						joinChatTv.setText("加入觅聊");
						joinGroup(conv);
					}
				}
			}else{
				PerfectInformation.showDiolagPerfertInformation(getActivity(), "亲爱的 完善个人信息后才能和小伙伴们玩耍呢");
			}

			break;

		default:
			break;
		}

	}

	Handler handler = new Handler() {

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

	// 加入当前觅聊
	public void joinGroup(final AVIMConversation conversation) {

		ObjChatMessage.joinChat(MyApplication.chatClient, conversation,
				new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					log.e("zcq", e);
					return;
				}
				if (result) {
					//TODO 更新本地聊天消息本人加入提醒    觅聊卡片中头像更新
					log.e("zcq", "加入觅聊成功");
					Chatmsgs chatmsgs=new Chatmsgs();
					chatmsgs.setContent("欢迎加入觅聊");
					chatmsgs.setClientId(user.getObjectId());
					chatmsgs.setSendTimeStamp(""+System.currentTimeMillis());
					chatmsgs.setChatMsgType(Constants.SHOW_SELF_CHANGE);
					chatmsgs.setConversationId(conversation.getConversationId());
					chatmsgs.setUid(user.getObjectId());
					chatmsgsDao.insert(chatmsgs);
					UserAboutBean aboutBean = new UserAboutBean();
					aboutBean.setUserId(user.getObjectId());
					aboutBean.setAboutUserId(user.getObjectId());
					aboutBean.setAboutType(Constants.CONVERSATION_TYPE);
					aboutBean.setAboutColetctionId(conversation.getConversationId());
					userAboutDao.saveUserAboutBean(aboutBean);

					List<UserAboutBean> memList = userAboutDao.queryUserAbout(user.getObjectId(), 
							Constants.CONVERSATION_TYPE, seekChatBeansList.get(positonNow).getConversationId());
					miliaoImv.setImageResource(R.drawable.miliao_in);
					
				//	((MiliaoChannelFragment)fragmentList.get(positonNow))).setu
					
					isAdd=true;
					((MiliaoChannelFragment) fragmentList.get(positonNow)).setUserInfo();
		
					if(memList.size()<5){
						joinChatTv.setText("等待开启");
						return;
					}else{
						joinChatTv.setText("进入觅聊");
					}
					Intent intent2 = new Intent(getActivity(),
							ChatGroupActivity.class);
					intent2.putExtra("ConversationId", ""
							+ seekChatBeansList.get(positonNow)
							.getConversationId());
					// 传对话的类型 1 表示活动群聊 2 表示觅聊 3 表示单聊
					intent2.putExtra("ConversationStyle", "" + 2);
					intent2.putExtra("title", ""
							+ seekChatBeansList.get(positonNow)
							.getTitle());
					intent2.putExtra("number", ""
							+ seekChatBeansList.get(positonNow)
							.getMembers().size() + 1);
					intent2.putExtra("objectId",
							seekChatBeansList.get(positonNow)
							.getObjectId());// 觅聊id
					intent2.putExtra("TimeOver",""+ seekChatBeansList.get(positonNow).getTimeChatStop());
					Bundle bundle = new Bundle();
					bundle.putSerializable("SeekChatBean",
							seekChatBeansList.get(positonNow));
					intent2.putExtras(bundle);
					startActivityForResult(intent2, 200);
					
				} else {
					log.e("zcq", "加入觅聊失败 ，请检查网络");
				}
			}
		});
	}

	/**
	 * 加载网络数据
	 *   
	 * @author lucifer
	 * @date 2015-12-10
	 */
	public void loadData() {
		log.e("zcq", "正在加载");
		Toast.makeText(getActivity(), "正在加载", Toast.LENGTH_SHORT).show();
		ObjChatWrap.queryChatInfo(user, System.currentTimeMillis(), 999,
				new ObjFunMapCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void callback(Map<String, Object> object,
					AVException e) {

				if (e != null) {
					//获取失败，点击屏幕重新加载

					log.e("zcq 加载失败异常", e);
					noneFailLayout.setVisibility(View.VISIBLE);
					nonoTextView.setVisibility(View.GONE);
					failTextView.setVisibility(View.VISIBLE);

					noneFailLayout.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							loadData();

						}
					});
					return;
				}
				if(object.get("seekChatCount")==null||(Integer)object.get("seekChatCount")<=0){
					log.e("zcq datanull", "没取到觅聊列表");
					noneFailLayout.setEnabled(false);
					//没获取到数据
					noneFailLayout.setVisibility(View.VISIBLE);
					nonoTextView.setVisibility(View.VISIBLE);
					failTextView.setVisibility(View.GONE);

				}else{
					noneFailLayout.setEnabled(false);
					log.e("zcq datanullaaa", "没取到觅聊列表"+"object=="+object.size());
					noneFailLayout.setVisibility(View.GONE);

					chatBean = new SeekChatInfoBean();
					log.d("mytest", "result=" + object);
					chatBean.setNeedAuthorise((Boolean) object
							.get("needAuthorise"));
					log.d("mytest",
							"NeedAuthorise=" + chatBean.getNeedAuthorise());
					if (chatBean.getNeedAuthorise()) {
						// 需要授权
						chatBean.setIsApply((Boolean) object.get("isApply"));
						log.d("mytest", "isApply=" + chatBean.getIsApply());
						if (chatBean.getIsApply()) {
							// 已经申请
							chatBean.setFreshStatus((Boolean) object
									.get("freshStatus"));
							log.d("mytest",
									"freshStatus="
											+ chatBean.getFreshStatus());
							chatBean.setApplyResult((Integer) object
									.get("applyResult"));
							log.d("mytest",
									"applyResult="
											+ chatBean.getApplyResult());
							chatBean.setApplyReply((String) object
									.get("applyReply"));
							log.d("mytest",
									"applyReply="
											+ chatBean.getApplyReply());
							chatBean.setArgument((String) object
									.get("argument"));
							log.d("mytest",
									"argument=" + chatBean.getArgument());
							chatBean.setApplyId((String) object
									.get("applyId"));
							log.d("mytest",
									"applyId=" + chatBean.getApplyId());
							chatBean.setAuthoriseCategoryId((String) object
									.get("authoriseCategoryId"));
							log.d("mytest",
									"applyId="
											+ chatBean
											.getAuthoriseCategoryId());
						} else {
							// 未申请
							chatBean.setAuthoriseCategoryId((String) object
									.get("authoriseCategoryId"));
							log.d("mytest",
									"applyId="
											+ chatBean
											.getAuthoriseCategoryId());
						}
					} else {
						// 不需要授权，直接创建
					}
					// 有觅聊
					chatBean.setSeekChatCount((Integer) object
							.get("seekChatCount"));
					log.d("mytest",
							"seekChatCount" + chatBean.getSeekChatCount());
					if (chatBean.getSeekChatCount() == 0) {
						return;
					}
					// createAt是Date类型，timeChatStop为long类型，取值是注意
					chatBean.setChatList((List<Map<String, Object>>) object
							.get("seekChats"));
					log.d("mytest", "seekChats=" + chatBean.getChatList());
					
					seekChatBeansList.clear();
					seekChatBeansList = new ArrayList<SeekChatBean>();

					for (int i = 0; i < chatBean.getSeekChatCount(); i++) {
						SeekChatBean bean = new SeekChatBean();
						List<Map<String, Object>> members = (List<Map<String, Object>>) chatBean
								.getChatList().get(i).get("members");
						bean.setMembers(members);
						log.d("mytest", "members" + members);

						bean.setConversationId((String) chatBean
								.getChatList().get(i).get("conversationId"));
						log.d("mytest",
								"conversationId" + bean.getConversationId());
						AVUser avUser = (AVUser) chatBean.getChatList()
								.get(i).get("creator");
						bean.setCreator(AVUser.cast(avUser, ObjUser.class));
						log.d("mytest", "creator" + bean.getCreator());
						bean.setFolloweeCount((Integer) chatBean
								.getChatList().get(i).get("followeeCount"));
						log.d("mytest",
								"followeeCount" + bean.getFolloweeCount());
						Date date = (Date) chatBean.getChatList().get(i)
								.get("createdAt");
						bean.setCreateAt(date.getTime());
						log.d("mytest", "createdAt" + bean.getCreateAt());
						bean.setObjectId((String) chatBean.getChatList()
								.get(i).get("objectId"));
						log.d("mytest", "objectId" + bean.getObjectId());
						bean.setPictureUrl((String) chatBean.getChatList()
								.get(i).get("pictureUrl"));
						log.d("mytest", "pictureUrl" + bean.getPictureUrl());
						bean.setTitle((String) chatBean.getChatList()
								.get(i).get("title"));
						log.d("mytest", "title" + bean.getTitle());
						bean.setTimeChatStop((Long) chatBean.getChatList()
								.get(i).get("timeChatStop"));
						log.d("mytest",
								"timeChatStop" + bean.getTimeChatStop());

						seekChatBeansList.add(bean);

						//缓存觅聊成员
						List<String> userList=new ArrayList<String>();
						for(int j=0;j<bean.getMembers().size();j++){
							userList.add(""+bean.getMembers().get(j).get("userId"));
						}
						getMember(userList,""+bean.getConversationId());

					}

					numberAll.setText("" + seekChatBeansList.size());
					numberPosition.setText("" + 1);
					handler.sendEmptyMessage(1);


				}

			}
		});
	}

	public void cteatMiliao() {
		if (chatBean.getNeedAuthorise()) {
			if (chatBean.getIsApply() == true) {
				if (chatBean.getApplyResult() == 2) {
					log.e("zcq", "有权限");
					Intent intent = new Intent(getActivity(),
							CreationChatActivity.class);
					startActivityForResult(intent, 100);
				} else {
					log.e("zcq", "申请还没通过");
					Intent intent = new Intent(getActivity(),
							ApplyForMiLiaoActivity.class);
					intent.putExtra("isApply", "" + 1);// 已经申请
					intent.putExtra("applyId", chatBean.getApplyId());
					intent.putExtra("CategoryId",chatBean.getAuthoriseCategoryId());
					intent.putExtra("ApplyReply", ""+chatBean.getApplyResult());
					intent.putExtra("argument", chatBean.getArgument());
					startActivity(intent);
				}
			} else {
				log.e("zcq", "没申请过");
				Intent intent = new Intent(getActivity(),
						ApplyForMiLiaoActivity.class);
				// Bundle bundle=new Bundle();
				// bundle.putSerializable("chatBean", chatBean);
				// intent.putExtras(bundle);
				intent.putExtra("isApply", "0");
				intent.putExtra("applyId", chatBean.getApplyId());
				intent.putExtra("CategoryId", chatBean.getAuthoriseCategoryId());

				log.e("zcq CategoryId"+""+chatBean.getAuthoriseCategoryId());
				intent.putExtra("ApplyReply", chatBean.getApplyReply());
				startActivity(intent);
			}

		} else {

			Intent intent = new Intent(getActivity(),
					CreationChatActivity.class);
			startActivityForResult(intent, 100);
		}

	}

	/**
	 * 是否已经加入了当前对话
	 * 
	 * @author lucifer
	 * @date 2015-11-27
	 */
	public void isAddconvesition() {
		List<String> list = new ArrayList<String>();
		SeekChatBean chatBean = seekChatBeansList.get(positonNow);
		List<UserAboutBean> aboutlist = userAboutDao.queryUserAbout(user.getObjectId(), Constants.CONVERSATION_TYPE, chatBean.getConversationId());

		for (int i = 0; i < aboutlist.size(); i++) {
			list.add(aboutlist.get(i).getAboutUserId());
		}
		for (String string : list) {
			if (user.getObjectId().equals(string)) {
				isAdd = true;
				break;
			}else{
				isAdd = false;
			}
		}
		if(isAdd){
			miliaoImv.setImageResource(R.drawable.miliao_in);
			List<UserAboutBean> memList = userAboutDao.queryUserAbout(user.getObjectId(), 
					Constants.CONVERSATION_TYPE, seekChatBeansList.get(positonNow).getConversationId());
			if(memList.size()<5){
				joinChatTv.setText("等待开启");
			}else{
				joinChatTv.setText("进入觅聊");
			}
		}else{
			miliaoImv.setImageResource(R.drawable.chat_navi_btn_joinchat);
			List<UserAboutBean> memList = userAboutDao.queryUserAbout(user.getObjectId(), 
					Constants.CONVERSATION_TYPE, seekChatBeansList.get(positonNow).getConversationId());
			if(memList.size()>=500){
				joinChatTv.setText("人数已满");
			}else{
				joinChatTv.setText("加入觅聊");
				isAdd=false;
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 100:
			if (resultCode == getActivity().RESULT_OK) {
				// 创建觅聊 成功 执行的操作
				log.e("lucifer", "需要重新加载数据");
				seekChatBeansList.clear();
				positonNow=0;
		//		handler.sendEmptyMessage(1);
				loadData();
			}
			break;
		case 200:
			if (resultCode == getActivity().RESULT_OK) {
				// 创建觅聊 成功 执行的操作
				log.e("lucifer", "需要重新加载头像");
				((MiliaoChannelFragment) fragmentList.get(positonNow)).setUserInfo();
				isAddconvesition();
			}
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * 查询对话成员 插到本地
	 * 
	 * @param covn
	 * @author lucifer
	 * @date 2015-11-28
	 */
	public void getMember(List<String> list,String conversationId) {
		log.d("mytest", "wode====="+list.size());
		userAboutBeansList = new ArrayList<UserAboutBean>();
		if (list != null) {
			for (String string : list) {
				UserAboutBean item = new UserAboutBean();
				item.setUserId(user.getObjectId());
				item.setAboutType(2);
				item.setAboutUserId(string);
				item.setAboutColetctionId(conversationId);
				userAboutBeansList.add(item);
			}

		}
		userAboutDao.deleteByType(user.getObjectId(), 2,
				conversationId);
		userAboutDao.saveUserAboutList(userAboutBeansList);

	}



}
