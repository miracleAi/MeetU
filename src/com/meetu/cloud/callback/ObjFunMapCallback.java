package com.meetu.cloud.callback;

import java.util.Map;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMException;

public interface ObjFunMapCallback {

	void callback(Map<String, Object> map,AVException e);
}
