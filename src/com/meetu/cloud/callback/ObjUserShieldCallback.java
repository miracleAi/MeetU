package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjShieldUser;

public interface ObjUserShieldCallback {

	void callback(List<ObjShieldUser> objects,AVException e);
}
