package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.meetu.cloud.object.ObjActivityOrder;

public interface ObjActivityOrderCallback {
	void callback(List<ObjActivityOrder> objects, AVException e);
}
