package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.meetu.cloud.object.ObjUser;

public interface ObjUserCallback {
	void callback(List<ObjUser> objects, AVException e);
}
