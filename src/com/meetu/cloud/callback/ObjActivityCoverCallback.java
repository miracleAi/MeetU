package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.meetu.cloud.object.ObjActivityCover;

public interface ObjActivityCoverCallback {
	void callback(List<ObjActivityCover> objects, AVException e);
}
