package com.meetu.cloud.wrap;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.meetu.cloud.callback.ObjChatBeanCallback;
import com.meetu.cloud.callback.ObjChatCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjChat;
import com.meetu.cloud.object.ObjUser;

public class ObjChatWrap {
	//保存觅聊信息
	public static void saveGroupInfo(ObjUser user,AVFile file,String title,final ObjChatBeanCallback callback){
		final ObjChat chat = new ObjChat();
		chat.setUser(user);
		chat.setChatPicture(file);
		chat.setChatTitle(title);
		long stopTime = System.currentTimeMillis()+24*3600*1000;
		chat.setTimeChatStop(stopTime);
		chat.setFetchWhenSave(true);
		chat.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if(e == null){
					callback.callback(chat, null);
				}else{
					callback.callback(null, e);
				}
			}
		});
	}
	//获取觅聊列表
	public static void queryChatList(final ObjChatCallback callback){
		AVQuery<ObjChat> query = AVObject.getQuery(ObjChat.class);
//	query.whereGreaterThan("timeChatStop", System.currentTimeMillis());
		query.findInBackground(new FindCallback<ObjChat>() {
			
			@Override
			public void done(List<ObjChat> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(null, e);
					return;
				}
				if(objects != null){
					callback.callback(objects, null);
				}else{
					callback.callback(null, new AVException(0, "获取觅聊列表失败"));
				}
			}
		});
	}
}
