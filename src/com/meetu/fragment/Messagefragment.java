package com.meetu.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Test;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.meetu.R;
import com.meetu.activity.messages.LitterNoteActivity;
import com.meetu.activity.miliao.ChatGroupActivity;
import com.meetu.activity.miliao.EmojiParser;
import com.meetu.activity.miliao.XmlEmojifPullHelper;
import com.meetu.adapter.MessagesListAdapter;
import com.meetu.cloud.callback.ObjConversationListCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.common.Constants;
import com.meetu.entity.ChatEmoji;
import com.meetu.entity.Messages;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.EmojisDao;
import com.meetu.sqlite.MessagesDao;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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


public class Messagefragment extends Fragment implements OnItemClickListener,OnClickListener{
	private ListView mListView;
	private List<Messages> mdataList=new ArrayList<Messages>();
	private List<Messages> mdataListCache=new ArrayList<Messages>();
	private MessagesListAdapter mAdapter;
	private View view;
	
	private MessagesDao messagesDao;
	private RelativeLayout littleNoteLayout;
	
	//表情相关 xml解析    
	private static EmojiParser parser;  
    private static List<ChatEmoji> chatEmojis; 
	private static EmojisDao emojisDao;
	
	
	//网络相关
	ObjUser user = null;
	MyReceiver mr = null;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    view = inflater.inflate(R.layout.fragment_messages, container, false);
	    if(ObjUser.getCurrentUser() != null){
			AVUser currentUser = ObjUser.getCurrentUser();
			user = AVUser.cast(currentUser, ObjUser.class);
		}
	    messagesDao=new MessagesDao(getActivity());
//	    //TODO 测试	   
//	    testMessages();
	    
	    loadEmoji(getActivity());
	    
	    loadData();
	    initView();
	    
	    getConversation();
	    return view;
	    
	  }
	class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(1);
		}
		
	}
	private static void loadEmoji(Context context) {
		 

		
		emojisDao=new EmojisDao(context);
         
		chatEmojis=emojisDao.getChatEmojisList();
	}

	/**
	 * 测试
	 */
	private void testMessages() {

	}

	private void loadData() {

		
		ArrayList<Messages> list = messagesDao.getMessages(user.getObjectId());
		log.e("zcq", "user.getObjectId()======"+user.getObjectId());
		if(list != null && list.size()>0){
			mdataListCache.clear();
			mdataListCache.addAll(list);		
		}else{
			//重新加载
		}
		
	}

	private void initView() {
		mListView=(ListView) view.findViewById(R.id.listView_messages_fragment);
		mAdapter=new MessagesListAdapter(getActivity(), mdataListCache,chatEmojis);
		mListView.setAdapter(mAdapter);
		mListView.setDivider(null);
		mListView.setOnItemClickListener(this);
		
		
		littleNoteLayout=(RelativeLayout) view.findViewById(R.id.litter_notes_fragment_messages_rl);
		littleNoteLayout.setOnClickListener(this);
		
		mr = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.RECEIVE_MSG);
		getActivity().registerReceiver(mr, filter);
		
	}

	@Override
	public void setArguments(Bundle args) {
		
		super.setArguments(args);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(getActivity(),ChatGroupActivity.class);
		intent.putExtra("ConversationId", ""+mdataListCache.get(position).getConversationID());
		//传对话的类型   1 表示活动群聊 2 表示觅聊  3 表示单聊
		intent.putExtra("ConversationStyle", ""+mdataListCache.get(position).getConversationType());
		if(mdataListCache.get(position).getConversationType()==1){
			intent.putExtra("title",mdataListCache.get(position).getActyName());
		}else{
			intent.putExtra("title",mdataListCache.get(position).getChatName());
		}
		Bundle bundle=new Bundle();
		bundle.putSerializable("Messages", mdataListCache.get(position));
		startActivity(intent);
		//清空该项未读消息
		messagesDao.updateUnreadClear(user.getObjectId(), mdataListCache.get(position).getConversationID());
		handler.sendEmptyMessage(1);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		handler.sendEmptyMessage(1);
	}
	
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			
			switch(msg.what){
			case 1:	
				log.e("zcq", "刷新了");
				ArrayList<Messages> list = messagesDao.getMessages(user.getObjectId());
				
				if(list != null && list.size()>0){
					mdataListCache.clear();
					mdataListCache.addAll(list);		
				}else{
					//重新加载
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
			Intent intent =new Intent(getActivity(),LitterNoteActivity.class);
			startActivity(intent);
			
			break;

		default:
			break;
		}
		
	}
	
	
	/**
	 * 获取所有会话,保存至数据库
	 *   
	 * @author lucifer
	 * @date 2015-11-20
	 */
	public void getConversation(){
		ObjChatMessage.getConversation(MyApplication.chatClient, new ObjConversationListCallback() {

			@Override
			public void callback(List<AVIMConversation> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					log.e("zcq", e);
					return;  
				}

				ArrayList<AVIMConversation> convList = new ArrayList<AVIMConversation>();
				for(AVIMConversation conversation:objects){
					convList.add(conversation);
				}
				if(convList.size()>0){

					ArrayList<Messages> list = new ArrayList<Messages>();
					log.e("zcq", "user.getObjectId()=="+user.getObjectId());
					for(int i=0;i<convList.size();i++){
						AVIMConversation conversation = convList.get(i);
						Messages msg = new Messages();
						msg.setUserId(user.getObjectId());
						msg.setConversationID(conversation.getConversationId());
						int type = (Integer) conversation.getAttribute("cType");
						msg.setConversationType(type);
						String id = (String) conversation.getAttribute("appendId");
						String title = (String) conversation.getAttribute("title");
						long overTime = (Long) conversation.getAttribute("overTime");
						if(type == 1){
							msg.setActyId(id);
							msg.setActyName(title);
						}else{
							log.e("zcq", "觅聊觅聊");
							msg.setChatId(id);
							msg.setChatName(title);
						}
						msg.setTimeOver(overTime);
						msg.setCreatorID(convList.get(i).getCreator());
						//插入时所有标记为未踢出
						msg.setTiStatus(0);
						list.add(msg);
					}
					log.e("zcq", "list.size()=="+list.size());
					messagesDao.insertList(list);
				}
				log.e("zcq", "list.size()=="+convList.size());
				handler.sendEmptyMessage(1);
			}
		});

	}
	
	//查询数据库会话
	public void queryLocalCinv(){
		ArrayList<Messages> list = messagesDao.getMessages(user.getObjectId());
		if(list != null && list.size()>0){
			mdataListCache.clear();
			mdataListCache.addAll(list);		
		}

	}
	
	
	
	

}
