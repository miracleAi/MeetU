package com.meetu.cloud.callback;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMClient;

public interface ObjAvimclientCallback {
	void callback(AVIMClient client, AVException e);
}
