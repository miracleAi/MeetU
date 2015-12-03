package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;

public interface ObjConversationListCallback {
	void callback(List<AVIMConversation> objects, AVException e);
}
