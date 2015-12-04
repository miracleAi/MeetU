package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjSysMsg;

public interface ObjSysMsgListCallback {
	void callback(List<ObjSysMsg> objects, AVException e);
}
