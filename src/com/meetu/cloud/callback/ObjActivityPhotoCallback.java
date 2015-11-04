package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjActivityPhoto;

public interface ObjActivityPhotoCallback {
	void callback(List<ObjActivityPhoto> objects, AVException e);
}
