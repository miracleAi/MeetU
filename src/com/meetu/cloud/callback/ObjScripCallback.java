package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjScrip;

public interface ObjScripCallback {
	void callback(List<ObjScrip> objects, AVException e);
}
