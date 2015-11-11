package com.meetu.cloud.callback;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMConversation;

public interface ObjCoversationCallback {
	void callback(AVIMConversation conversation, AVException e);
}
