package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjChat;

public interface ObjChatCallback {
	void callback(List<ObjChat> objects, AVException e);
}
