package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjUser;

public interface ObjUserInfoCallback {
	void callback(ObjUser user, AVException e);
}
