package com.meetu.cloud.callback;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMException;
import com.meetu.cloud.object.ObjGlobalAndroid;

public interface ObjGlobalCallback {

	void callback(ObjGlobalAndroid object,AVException e);
}
