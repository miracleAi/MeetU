package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjScripBox;

public interface ObjScripBoxCallback {
	void callback(List<ObjScripBox> objects, AVException e);
}
