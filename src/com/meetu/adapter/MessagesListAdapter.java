package com.meetu.adapter;

import java.util.ArrayList;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.activity.miliao.ChatGroupActivity;
import com.meetu.bean.UserBean;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.utils.DateUtils;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.common.EmojisRelevantUtils;
import com.meetu.common.Spanning;
import com.meetu.common.dismissData;
import com.meetu.entity.ChatEmoji;
import com.meetu.entity.Chatmsgs;
import com.meetu.entity.Huodong;
import com.meetu.entity.Messages;
import com.meetu.entity.User;
import com.meetu.sqlite.ChatmsgsDao;
import com.meetu.sqlite.UserDao;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessagesListAdapter extends BaseAdapter {
	private Context mContext;
	private List<Messages> messagesList;
	private static List<ChatEmoji> chatEmojis;
	private ChatmsgsDao chatmsgsDao;
	private Chatmsgs chatmsgs;
	private UserDao userDao;
	// 网络相关
	ObjUser user = null;
	String nickName = "";

	public MessagesListAdapter(Context context, List<Messages> messagesList,
			List<ChatEmoji> chatEmojis) {
		this.mContext = context;
		this.messagesList = messagesList;
		chatmsgsDao = new ChatmsgsDao(context);
		userDao = new UserDao(context);

		this.chatEmojis = chatEmojis;
		if (ObjUser.getCurrentUser() != null) {
			AVUser currentUser = ObjUser.getCurrentUser();
			user = AVUser.cast(currentUser, ObjUser.class);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		log.e("zcq", "messagesList.size()==" + messagesList.size());
		return messagesList.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messagesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		chatmsgs=null;
		ViewHolder holder = null;
		Messages item = messagesList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_fragment_messages, null);
			holder.photpHead = (ImageView) convertView
					.findViewById(R.id.photoHead_item_fragment);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.name_item_fragment_messages_tv);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.content_item_fragment_messages_tv);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.time_item_fragment_messages_tv);
			holder.tvNoReadMessages = (TextView) convertView
					.findViewById(R.id.number_noRead_item_fragment_messages_tv);
			holder.noReadLayout = (RelativeLayout) convertView
					.findViewById(R.id.number_noRead_item_fragment_messages_rl);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 根据对话id拿到聊天对话的最后一条消息
		if (chatmsgsDao.getChatmsgsList(item.getConversationID(),
				user.getObjectId()).size() != 0) {
			chatmsgs = chatmsgsDao.getChatmsgsList(item.getConversationID(),
					user.getObjectId()).get(
							chatmsgsDao.getChatmsgsList(item.getConversationID(),
									user.getObjectId()).size() - 1);
			nickName = "";
			if(null != chatmsgs.getClientId()){
				ArrayList<UserBean> list = userDao.queryUser(chatmsgs.getClientId());
				if (null != list && list.size() > 0) {
					nickName = list.get(0).getNameNick();
				}else{
					ObjUserWrap.getObjUser(chatmsgs.getClientId(),
							new ObjUserInfoCallback() {

						@Override
						public void callback(ObjUser objuser, AVException e) {
							// TODO Auto-generated method stub
							if (e == null) {
								nickName = objuser.getNameNick();
								userDao.insertOrReplaceUser(objuser);
							}
						}
					});
				}
			}
			// 如果 是 文本消息 如果有表情的话显示表情
			if (chatmsgs.getChatMsgType() == Constants.SHOW_SEND_TEXT
					|| chatmsgs.getChatMsgType() == Constants.SHOW_RECV_TEXT) {
				SpannableString spannableString = EmojisRelevantUtils
						.getExpressionString(mContext, chatmsgs.getContent(),
								chatEmojis);
				if(!nickName.equals("")){
					holder.tvContent.setText(nickName+":"+spannableString);
				}else{
					holder.tvContent.setText(spannableString);
				}

			}
			if (chatmsgs.getChatMsgType() == Constants.SHOW_SEND_IMG
					|| chatmsgs.getChatMsgType() == Constants.SHOW_RECV_IMG) {
				if(!nickName.equals("")){
					holder.tvContent.setText(nickName+":"+"[图片]");
				}else{
					holder.tvContent.setText("[图片]");
				}
			}
			if (chatmsgs.getChatMsgType() == Constants.SHOW_MEMBERCHANGE) {
				holder.tvContent.setText("系统消息：新人加入了，打个招呼吧！");
			}
			if (chatmsgs.getChatMsgType() == Constants.SHOW_SELF_CHANGE) {
				holder.tvContent.setText("系统消息：欢迎加入觅聊");
			}
			if (chatmsgs.getChatMsgType() == Constants.SHOW_SELF_DEL) {
				holder.tvContent.setText("系统消息：您已被踢出觅聊");
			}

		} else {
			holder.tvContent.setText("");
			// holder.tvNoReadMessages.setText(item.getUnreadMsgCount());
		}
		if (item.getUnreadMsgCount() > 99) {
			holder.noReadLayout.setVisibility(View.VISIBLE);
			holder.tvNoReadMessages.setText("99+");
			
			holder.noReadLayout.setBackgroundResource(R.drawable.massage_newslist_img_unreadtips_2_bg);
		} else if (item.getUnreadMsgCount() <= 0) {
			holder.noReadLayout.setVisibility(View.INVISIBLE);

		} else {
			holder.noReadLayout.setVisibility(View.VISIBLE);
			holder.tvNoReadMessages.setText("" + item.getUnreadMsgCount());
			if(item.getUnreadMsgCount()>9){
				holder.noReadLayout.setBackgroundResource(R.drawable.massage_newslist_img_unreadtips_2_bg);			
			}else{
				holder.noReadLayout.setBackgroundResource(R.drawable.massage_newslist_img_unreadtips_1_bg);
			}
		}

		if (item.getConversationType() == Constants.ACTYSG) {
			holder.tvName.setText(item.getActyName());
			holder.photpHead
			.setImageResource(R.drawable.massage_newslist_img_acty);
			if(dismissData.getDismissData(item.getTimeOver())!=null){
				if(dismissData.getDismissData(item.getTimeOver()).equals("dismiss")){
					holder.tvTime.setText("活动群聊已消失");
				}else{
					holder.tvTime.setText(""+dismissData.getDismissData(item.getTimeOver())+"后消失");
				}
			}else{
				if(chatmsgs!=null && chatmsgs.getSendTimeStamp()!=null){
					holder.tvTime.setText(""+DateUtils.getFormattedTimeInterval(Long.valueOf(chatmsgs.getSendTimeStamp())));
				}else{
					holder.tvTime.setText("");
				}
			}
		} else if (item.getConversationType() == Constants.SEEKMSG) {
			holder.tvName.setText(item.getChatName());
			holder.photpHead
			.setImageResource(R.drawable.massage_newslist_img_chat);
			if(dismissData.getDismissData(item.getTimeOver())!=null && !dismissData.getDismissData(item.getTimeOver()).equals("dismiss")){
				holder.tvTime.setText(""+dismissData.getDismissData(item.getTimeOver())+"后消失");
			}else{
				holder.tvTime.setText("觅聊已消失");
			}
		}
		return convertView;
	}

	private class ViewHolder {
		private TextView tvName;
		private TextView tvContent;
		private TextView tvTime;
		private TextView tvNoReadMessages;
		private ImageView photpHead;
		private RelativeLayout noReadLayout;

	}

}
