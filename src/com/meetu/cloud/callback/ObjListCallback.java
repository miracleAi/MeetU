package com.meetu.cloud.callback;

import java.util.ArrayList;

import com.avos.avoscloud.AVException;

public interface ObjListCallback {
	void callback(ArrayList<String> list, AVException e);
}
