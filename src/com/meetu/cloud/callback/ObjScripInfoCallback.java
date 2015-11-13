package com.meetu.cloud.callback;

import java.util.ArrayList;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjScrip;

public interface ObjScripInfoCallback {
	void callback(ObjScrip scrip, AVException e);
}
