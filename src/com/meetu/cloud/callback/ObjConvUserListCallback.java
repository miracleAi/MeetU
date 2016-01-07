package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjUserConversation;

public interface ObjConvUserListCallback {
void callback(ObjUserConversation object,AVException e);
}
