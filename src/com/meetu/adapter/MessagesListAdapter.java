package com.meetu.adapter;

import java.util.ArrayList;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.activity.miliao.ChatGroupActivity;
import com.meetu.bean.CoversationUserBean;
import com.meetu.bean.MessageChatBean;
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
import com.meetu.sqlite.MessageChatDao;
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
	private List<CoversationUserBean> messagesList;
	private static List<ChatEmoji> chatEmojis;
	private MessageChatDao chatmsgsDao;
	private MessageChatBean chatmsgs;
	private UserDao userDao;
	// 网络相关
	ObjUser user = null;
	String nickName = "";

	public MessagesListAdapter(Context context, List<CoversationUserBean> messagesList,
			List<ChatEmoji> chatEmojis) {
		this.mContext = context;
		this.messagesList = messagesList;
		chatmsgsDao = new MessageChatDao(context);
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
		CoversationUserBean item = messagesList.get(position);
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
		if (chatmsgsDao.getChatmsgsList(item.getIdConversation(),
				user.getObjectId()).size() != 0) {
			chatmsgs = chatmsgsDao.getChatmsgsList(item.getIdConversation(),
					user.getObjectId()).get(
							chatmsgsDao.getChatmsgsList(item.getIdConversation(),
									user.getObjectId()).size() - 1);
			nickName = "";
			if(null != chatmsgs.getIdClient()){
				ArrayList<UserBean> list = userDao.queryUser(chatmsgs.getIdClient());
				if (null != list && list.size() > 0) {
					nickName = list.get(0).getNameNick();
				}else{
					ObjUserWrap.getObjUser(chatmsgs.getIdClient(),
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
			if (chatmsgs.getTypeMsg() == Constants.SHOW_SEND_TYPE_TEXT
					|| chatmsgs.getTypeMsg() == Constants.SHOW_RECEIVE_TYPE_TEXT) {
				SpannableString spannableString = EmojisRelevantUtils
						.getExpressionString(mContext, chatmsgs.getMsgText(),
								chatEmojis);
				if(!nickName.equals("")){
					holder.tvContent.setText(nickName+":"+spannableString);
				}else{
					holder.tvContent.setText(spannableString);
				}

			}
			if (chatmsgs.getTypeMsg() == Constants.SHOW_SEND_TYPE_IMG
					|| chatmsgs.getTypeMsg() == Constants.SHOW_RECEIVE_TYPE_IMG) {
				if(!nickName.equals("")){
					holder.tvContent.setText(nickName+":"+"[图片]");
				}else{
					holder.tvContent.setText("[图片]");
				}
			}
			if (chatmsgs.getTypeMsg() == Constants.SHOW_MEMBER_ADD) {
				holder.tvContent.setText("系统消息：新人加入了，打个招呼吧！");
			}
			if (chatmsgs.getTypeMsg() == Constants.SHOW_SELF_ADD) {
				holder.tvContent.setText("系统消息：欢迎加入觅聊");
			}
			if (chatmsgs.getTypeMsg() == Constants.SHOW_SELF_KICK) {
				holder.tvContent.setText("系统消息：您已被踢出觅聊");
			}

		} else {
			holder.tvContent.setText("");
			// holder.tvNoReadMessages.setText(item.getUnreadMsgCount());
		}
		if (item.getUnReadCount() > 99) {
			holder.noReadLayout.setVisibility(View.VISIBLE);
			holder.tvNoReadMessages.setText("99+");

			holder.noReadLayout.setBackgroundResource(R.drawable.massage_newslist_img_unreadtips_2_bg);
		} else if (item.getUnReadCount() <= 0) {
			holder.noReadLayout.setVisibility(View.INVISIBLE);

		} else {
			holder.noReadLayout.setVisibility(View.VISIBLE);
			holder.tvNoReadMessages.setText("" + item.getUnReadCount());
			if(item.getUnReadCount()>9){
				holder.noReadLayout.setBackgroundResource(R.drawable.massage_newslist_img_unreadtips_2_bg);			
			}else{
				holder.noReadLayout.setBackgroundResource(R.drawable.massage_newslist_img_unreadtips_1_bg);
			}
		}

		if (item.getType() == Constants.ACTYSG) {
			holder.tvName.setText(item.getTitle());
			holder.photpHead
			.setImageResource(R.drawable.massage_newslist_img_acty);
			if(item.getStatus() == Constants.CONV_STATUS_OPEN){
				if(chatmsgs!=null && chatmsgs.getSendTimeStamp()!=0){
					holder.tvTime.setText(""+DateUtils.getFormattedTimeInterval(chatmsgs.getSendTimeStamp()));
				}else{
					holder.tvTime.setText("");
				}
			}else{
				holder.tvTime.setText("活动群聊已消失");
			}
		} else if (item.getType() == Constants.SEEKMSG) {
			holder.tvName.setText(item.getTitle());
			holder.photpHead
			.setImageResource(R.drawable.massage_newslist_img_chat);
			if(item.getStatus() == Constants.CONV_STATUS_OPEN){
				if(dismissData.getDismissData(item.getOverTime())!=null){
					holder.tvTime.setText(""+dismissData.getDismissData(item.getOverTime())+"后消失");
				}
			}else{
				switch (item.getStatus()) {
				case Constants.CONV_STATUS_KICK:
					holder.tvTime.setText("您已被踢出觅聊");
					break;
				case Constants.CONV_STATUS_DISSOLVE:
					holder.tvTime.setText("觅聊已解散");
					break;
				case Constants.CONV_STATUS_DISMISS:
					holder.tvTime.setText("觅聊已消失");
					break;
				default:
					break;
				}
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
