package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjAuthoriseCategory;

public interface ObjAuthoriseCategoryCallback {
	void callback(List<ObjAuthoriseCategory> objects, AVException e);
}
