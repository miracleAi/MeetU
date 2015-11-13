package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjChat;

public interface ObjChatBeanCallback {
	void callback(ObjChat object, AVException e);
}
