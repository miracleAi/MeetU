package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjUserPhoto;

public interface ObjUserPhotoCallback {
	void callback(List<ObjUserPhoto> objects, AVException e);
}
