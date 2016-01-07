package com.meetu.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.meetu.activity.messages.FollowActivity;
import com.meetu.activity.messages.LitterNoteActivity;
import com.meetu.activity.messages.SystemMsgActivity;
import com.meetu.activity.miliao.ChatGroupActivity;
import com.meetu.activity.miliao.EmojiParser;
import com.meetu.adapter.MessagesListAdapter;
import com.meetu.bean.CoversationUserBean;
import com.meetu.bean.MemberActivityBean;
import com.meetu.bean.MemberSeekBean;
import com.meetu.bean.MessageChatBean;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.callback.ObjConvUserListCallback;
import com.meetu.cloud.callback.ObjConversationListCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjFunMapCallback;
import com.meetu.cloud.callback.ObjListCallback;
import com.meetu.cloud.callback.ObjSysMsgListCallback;
import com.meetu.cloud.object.ObjSysMsg;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.object.ObjUserConversation;
import com.meetu.cloud.utils.DateUtils;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjChatWrap;
import com.meetu.cloud.wrap.ObjSysMsgWrap;
import com.meetu.common.Constants;
import com.meetu.common.Log;
import com.meetu.common.PerfectInformation;
import com.meetu.common.SharepreferencesUtils;
import com.meetu.entity.ChatEmoji;
import com.meetu.entity.Messages;
import com.meetu.myapplication.DefaultMessageHandler;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.ConversationUserDao;
import com.meetu.sqlite.EmojisDao;
import com.meetu.sqlite.MemberActivityDao;
import com.meetu.sqlite.MemberSeekDao;
import com.meetu.sqlite.MessageChatDao;
import com.meetu.sqlite.MessagesDao;
import com.meetu.sqlite.UserAboutDao;
import com.meetu.view.ChatViewInterface;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Messagefragment extends Fragment implements OnItemClickListener,
OnClickListener,ChatViewInterface{
	private ListView mListView;
	private List<CoversationUserBean> mdataListCache = new ArrayList<CoversationUserBean>();
	private MessagesListAdapter mAdapter;
	private View view;
	private RelativeLayout attentionLayout;

	private ConversationUserDao convUserDao = null;
	private RelativeLayout littleNoteLayout;
	private RelativeLayout sysMsgLayout;
	private TextView sysMsgCountTv;
	private ArrayList<ObjSysMsg> msgList = new ArrayList<ObjSysMsg>();
	String conversationId = "";

	// 表情相关 xml解析
	private static EmojiParser parser;
	private static List<ChatEmoji> chatEmojis;
	private static EmojisDao emojisDao;

	// 网络相关
	ObjUser user = null;

	//
	//	private UserAboutDao userAboutDao;
	private ArrayList<MemberSeekBean> userAboutBeansList = new ArrayList<MemberSeekBean>();
	private List<MemberActivityBean> memberActivityBeans=new ArrayList<MemberActivityBean>();
	long scanTime = 0;
	public boolean isEnd = true;

	MemberSeekDao memberSeekDao;
	MemberActivityDao memberActivityDao;
	MessageChatDao msgChatDao ;
	
	boolean isDelete = false;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_messages, container, false);
		if (ObjUser.getCurrentUser() != null) {
			AVUser currentUser = ObjUser.getCurrentUser();
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		scanTime = SharepreferencesUtils.getInstance().getSystemScanTime(getActivity(), SharepreferencesUtils.SYS_MSG_SCAN, 0);
		convUserDao = new ConversationUserDao(getActivity());
		memberSeekDao=new MemberSeekDao(getActivity());
		memberActivityDao=new MemberActivityDao(getActivity());
		msgChatDao = new MessageChatDao(getActivity());

		loadEmoji(getActivity());

		loadData();
		initView();
		loadSysMsg();
		getConversation();
		return view;

	}
	private void initReceiveMsg() {
		// TODO Auto-generated method stub
		MyApplication.defaultMsgHandler.setUpdateBean(this);
	}

	private void loadSysMsg() {
		ObjSysMsgWrap.querySysMsgs(user, new ObjSysMsgListCallback() {

			@Override
			public void callback(List<ObjSysMsg> objects, AVException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					msgList.clear();
					msgList.addAll(objects);
					int count = 0;
					if(objects == null || objects.size()==0){
						return;
					}
					for (int i = 0; i < objects.size(); i++) {
						if(objects.get(i).getCreatedAt().getTime()>scanTime){
							count += 1;
						}
					}
					sysMsgCountTv.setTextColor(Color.parseColor("#ff6b6b"));
					sysMsgCountTv.setText(""+count);
				} else {
					// 查询出错
				}
			}
		});

	}
	private static void loadEmoji(Context context) {

		emojisDao = new EmojisDao(context);

		chatEmojis = emojisDao.getChatEmojisList();
	}

	private void loadData() {

		ArrayList<CoversationUserBean> list = convUserDao.getMessages(user.getObjectId());

		if (list != null && list.size() > 0) {
			mdataListCache.clear();
			mdataListCache.addAll(list);
		} else {
			// 重新加载
		}

	}

	private void initView() {
		attentionLayout = (RelativeLayout) view
				.findViewById(R.id.attention_messages_rl);
		mListView = (ListView) view
				.findViewById(R.id.listView_messages_fragment);
		mAdapter = new MessagesListAdapter(getActivity(), mdataListCache,
				chatEmojis);
		mListView.setAdapter(mAdapter);
		mListView.setDivider(null);
		mListView.setOnItemClickListener(this);

		littleNoteLayout = (RelativeLayout) view
				.findViewById(R.id.litter_notes_fragment_messages_rl);
		littleNoteLayout.setOnClickListener(this);
		sysMsgLayout = (RelativeLayout) view
				.findViewById(R.id.system_msg_layout);
		sysMsgLayout.setOnClickListener(this);
		sysMsgCountTv = (TextView) view.findViewById(R.id.system_msg_count);

		attentionLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), FollowActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void setArguments(Bundle args) {

		super.setArguments(args);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		//被踢出  退出 失效 解散 点击之后将失效会话删掉
		int convStatus = mdataListCache.get(position).getStatus();
		switch (convStatus) {
		case Constants.CONV_STATUS_KICK:
		case Constants.CONV_STATUS_QUIT:
		case Constants.CONV_STATUS_DISSOLVE:
		case Constants.CONV_STATUS_DISMISS:
			isDelete = true;
			convUserDao.deleteConv(user.getObjectId(),mdataListCache.get(position).getIdConversation());
			deleteObjUserConv(mdataListCache.get(position).getIdConversation());
			break;
		default:
			isDelete = false;
			break;
		}
		Intent intent = new Intent(getActivity(), ChatGroupActivity.class);
		conversationId = mdataListCache.get(position).getIdConversation();
		intent.putExtra("ConversationId", conversationId );
		intent.putExtra("ConversationStyle", 
				 mdataListCache.get(position).getType());
		startActivityForResult(intent, 1001);
		// 清空该项未读消息
		convUserDao.updateUnreadClear(user.getObjectId(),
				mdataListCache.get(position).getIdConversation());
	}
	//删掉后台记录
	private void deleteObjUserConv(String convId) {
		// TODO Auto-generated method stub
		ObjChatWrap.deleteUserConv(user, convId, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(result){

				}
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initReceiveMsg();
		handler.sendEmptyMessage(1);
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MyApplication.defaultMsgHandler.setUpdateBean(null);
	}
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case 1:
				mdataListCache.clear();
				ArrayList<CoversationUserBean> list = convUserDao.getMessages(user.getObjectId());
				log.d("mytest", "conv size:"+list.size());
				if (list != null && list.size() > 0) {
					for (CoversationUserBean messages : list) {
						mdataListCache.add(messages);
					}
				} else {
					// 重新加载
				}
				mAdapter.notifyDataSetChanged();
				break;

			}
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.litter_notes_fragment_messages_rl:
			if(user.isCompleteUserInfo()){
				Intent intent = new Intent(getActivity(), LitterNoteActivity.class);
				startActivity(intent);
			}else{
				PerfectInformation.showDiolagPerfertInformation(getActivity(), "亲爱的 完善个人信息后才能查看小纸条呢");
			}
			break;
		case R.id.system_msg_layout:
			sysMsgCountTv.setText("0");
			Intent intent2 = new Intent(getActivity(), SystemMsgActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}

	}

	/**
	 * 获取所有会话,保存至数据库(需要修改，通过api获得)
	 * 
	 * @author lucifer
	 * @date 2015-11-20
	 * update zlp 2015-12-30
	 */
	public void getConversation() {
		/*ObjChatMessage.getConversation(user.getObjectId(),
				MyApplication.chatClient, new ObjConversationListCallback() {

			@Override
			public void callback(List<AVIMConversation> objects,
					AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					log.e("zcq", e);
					return;
				}

				ArrayList<AVIMConversation> convList = new ArrayList<AVIMConversation>();
				for (AVIMConversation conversation : objects) {
					convList.add(conversation);
				}
				if (convList.size() > 0) {
					ArrayList<Messages> list = new ArrayList<Messages>();
					for (int i = 0; i < convList.size(); i++) {
						AVIMConversation conversation = convList.get(i);
						// 保存到本地
						getMember(conversation);
						Messages msg = new Messages();
						msg.setUpdateTime(System.currentTimeMillis());
						msg.setUserId(user.getObjectId());
						msg.setConversationID(conversation
								.getConversationId());
						int type = (Integer) conversation
								.getAttribute("cType");
						msg.setConversationType(type);
						String id = (String) conversation
								.getAttribute("appendId");
						String title = (String) conversation
								.getAttribute("title");
						long overTime = (Long) conversation
								.getAttribute("overTime");
						msg.setTimeOver(overTime);
						msg.setCreatorID(convList.get(i).getCreator());
						// 插入时所有标记为未踢出
						msg.setTiStatus(0);
						if (type == 1) {
							msg.setActyId(id);
							msg.setActyName(title);
							list.add(msg);
						} else {
							log.e("zcq", "觅聊觅聊");
							msg.setChatId(id);
							msg.setChatName(title);
							if(conversation.getMembers().size()>2){
								list.add(msg);
							}
						}

					}
					messagesDao.insertList(list);
				}
				handler.sendEmptyMessage(1);
			}
		});*/
		/*ObjChatWrap.getConvUserList(user,new ObjConvUserListCallback() {

			@Override
			public void callback(List<ObjUserConversation> objects, AVException e) {
				// TODO Auto-generated method stub
				if (e != null ) {
					return;
				}
				if(objects.size() == 0){
					return;
				}
				ArrayList<CoversationUserBean> convUserList = new ArrayList<CoversationUserBean>();
				for(int i=0;i<objects.size();i++){
					ObjUserConversation objBean = objects.get(i);
					CoversationUserBean bean =new CoversationUserBean();
					bean.setIdMine(objBean.getConvUser().getObjectId());
					bean.setIdConversation(objBean.getIdConversation());
					bean.setIdConvAppend(objBean.getIdConvAppend());
					bean.setMute(objBean.getMute());
					bean.setOverTime(objBean.getOverTime());
					bean.setRefuseMsg(objBean.getRefuseMsg());
					bean.setStatus(objBean.getStatus());
					bean.setTitle(objBean.getTitle());
					bean.setType(objBean.getType());
					bean.setUnReadCount(0);
					bean.setUpdateTime(System.currentTimeMillis());
					convUserList.add(bean);
					ArrayList<MemberSeekBean> seekMemList = new ArrayList<MemberSeekBean>();
					if(objBean.getMembers()!= null){
						seekMemList.addAll(objBean.getMembers());
						memberSeekDao.deleteByConv(user.getObjectId(), bean.getIdConversation());
						memberSeekDao.saveAllUserSeek(seekMemList);
					}
				}
				convUserDao.insertList(convUserList);
				handler.sendEmptyMessage(1);
			}
		});*/
		ObjChatWrap.getConvUserList(user, new ObjFunMapCallback() {

			@Override
			public void callback(Map<String, Object> map, AVException e) {
				log.d("mytest", ""+map);
				if(e != null){
					return;
				}
				ArrayList<HashMap<String, Object>> resultMap = (ArrayList<HashMap<String, Object>>)map.get("result");
				if(resultMap == null || resultMap.size() == 0){
					return ;
				}
				ArrayList<CoversationUserBean> convUserList = new ArrayList<CoversationUserBean>();
				for(HashMap<String, Object> resultBean : resultMap){
					CoversationUserBean bean =new CoversationUserBean();
					bean.setIdConvAppend((String)resultBean.get("appendId"));
					AVUser creator = (AVUser) resultBean.get("creator");
					bean.setIdConvCreator(creator.getObjectId());
					bean.setIdConversation((String)resultBean.get("conversationId"));
					bean.setIdMine(user.getObjectId());
					boolean mute = (Boolean) resultBean.get("mute");
					if(mute){
						bean.setMute(1);
					}else{
						bean.setMute(0);
					}
					bean.setOverTime((Long)resultBean.get("overTime"));
					boolean refuseMsg = (Boolean) resultBean.get("refuseMsg");
					if(refuseMsg){
						bean.setRefuseMsg(1);
					}else{
						bean.setRefuseMsg(0);
					}
					bean.setStatus((Integer)resultBean.get("status"));
					bean.setTitle((String)resultBean.get("title"));
					bean.setType((Integer)resultBean.get("type"));
					bean.setUpdateTime(System.currentTimeMillis());
					convUserList.add(bean);
					ArrayList<String> memberList = (ArrayList<String>) resultBean.get("member");
					if(memberList == null || memberList.size() == 0){
						return;
					}
					ArrayList<MemberSeekBean> seekMemList = new ArrayList<MemberSeekBean>();
					for(String memId:memberList){
						MemberSeekBean seekBean = new MemberSeekBean();
						seekBean.setConversationId((String)resultBean.get("conversationId"));
						seekBean.setConvStatus(0);
						seekBean.setMemberSeekId(memId);
						seekBean.setMineId(user.getObjectId());
						seekBean.setSeekId((String)resultBean.get("appendId"));
						seekMemList.add(seekBean);
					}
					memberSeekDao.deleteByConv(user.getObjectId(), bean.getIdConversation());
					memberSeekDao.saveAllUserSeek(seekMemList);
				}
				convUserDao.insertList(convUserList);
				handler.sendEmptyMessage(1);
			}
		});
	}
	/**
	 * 查询对话成员 插到本地
	 * 
	 * @param covn
	 * @author lucifer
	 * @date 2015-11-28
	 */
	/*public void getMember(AVIMConversation covn) {

		userAboutBeansList = new ArrayList<MemberSeekBean>();
		memberActivityBeans=new ArrayList<MemberActivityBean>();

		List<String> list = covn.getMembers();
		Object type= covn.getAttribute("cType");
		Log.e("type", ""+type );
		Object appendId= covn.getAttribute("appendId");
		Log.e("appendId", appendId.toString());

		if(type.toString().equals("1")){
			if (list != null) {
				for (String string : list) {
					MemberActivityBean item = new MemberActivityBean();
					item.setActivityId(""+appendId);
					item.setConversationId(covn.getConversationId());
					item.setConvStatus(Constants.NORMAL);
					item.setMemberId(string);
					item.setMineId(user.getObjectId());
					memberActivityBeans.add(item);
				}

			}
			memberActivityDao.deleteByConv(user.getObjectId(), covn.getConversationId());
			memberActivityDao.saveAllUserActivity(memberActivityBeans);
		}
		if(type.toString().equals("2")){
			if (list != null) {
				for (String string : list) {
					MemberSeekBean item = new MemberSeekBean();
					item.setConversationId(covn.getConversationId());
					item.setConvStatus(Constants.NORMAL);
					item.setMemberSeekId(string);
					item.setMineId(user.getObjectId());
					item.setSeekId(""+appendId);

					userAboutBeansList.add(item);
				}

			}
			memberSeekDao.deleteByConv(user.getObjectId(), covn.getConversationId());
			memberSeekDao.saveAllUserSeek(userAboutBeansList);
		}


	}*/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1001){
			log.d("mytest", "msg frgt delete");
			getConversation();
			if(isDelete){
				isDelete = false;
				msgChatDao.deleteByConv(user.getObjectId(), conversationId);
			}
		}
	}

	@Override
	public void updateView(MessageChatBean bean) {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(1);
	}
}
