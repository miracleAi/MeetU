package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjActivityPhoto;
import com.meetu.cloud.object.ObjAuthoriseApply;

public interface ObjAuthoriseApplyCallback {
	void callback(List<ObjAuthoriseApply> objects, AVException e);
}
