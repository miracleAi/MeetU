package com.meetu.adapter;

import java.util.List;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.R;

import com.meetu.activity.miliao.ChatGroupActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.common.EmojisRelevantUtils;
import com.meetu.common.Spanning;
import com.meetu.entity.ChatEmoji;
import com.meetu.entity.Chatmsgs;
import com.meetu.entity.Huodong;
import com.meetu.entity.Messages;
import com.meetu.entity.User;
import com.meetu.sqlite.ChatmsgsDao;

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
	// 网络相关
	ObjUser user = null;

	public MessagesListAdapter(Context context, List<Messages> messagesList,
			List<ChatEmoji> chatEmojis) {
		this.mContext = context;
		this.messagesList = messagesList;
		chatmsgsDao = new ChatmsgsDao(context);

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
			// TODO
			// 如果 是 文本消息 如果有表情的话显示表情
			if (chatmsgs.getChatMsgType() == 10
					|| chatmsgs.getChatMsgType() == 12) {
				SpannableString spannableString = EmojisRelevantUtils
						.getExpressionString(mContext, chatmsgs.getContent(),
								chatEmojis);

				holder.tvContent.setText(spannableString);

				// holder.tvContent.setText(chatmsgs.getContent());

				new Spanning().TextViewShowEmoji(mContext, chatEmojis,
						holder.tvContent, chatmsgs.getContent());

				// holder.tvNoReadMessages.setText(item.getUnreadMsgCount());
			}
			if (chatmsgs.getChatMsgType() == 11
					|| chatmsgs.getChatMsgType() == 13) {
				holder.tvContent.setText("[图片]");
			}

		} else {
			holder.tvContent.setText("");
			// holder.tvNoReadMessages.setText(item.getUnreadMsgCount());
		}
		if (item.getUnreadMsgCount() > 99) {
			holder.noReadLayout.setVisibility(View.VISIBLE);
			holder.tvNoReadMessages.setText("99+");
		} else if (item.getUnreadMsgCount() <= 0) {
			holder.noReadLayout.setVisibility(View.GONE);

		} else {
			holder.noReadLayout.setVisibility(View.VISIBLE);
			holder.tvNoReadMessages.setText("" + item.getUnreadMsgCount());
		}

		if (item.getConversationType() == 1) {

			holder.tvName.setText(item.getActyName());
			holder.photpHead
					.setImageResource(R.drawable.massage_newslist_img_acty);

		} else if (item.getConversationType() == 2) {
			holder.tvName.setText(item.getChatName());
			holder.photpHead
					.setImageResource(R.drawable.massage_newslist_img_chat);
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
