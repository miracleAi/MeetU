package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjActivity;

public interface ObjActivityCallback {

	void callback(List<ObjActivity> objects, AVException e);

}
