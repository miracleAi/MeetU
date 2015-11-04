package com.meetu.cloud.callback;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.meetu.cloud.object.ObjActivityTicket;
import com.meetu.cloud.object.ObjUser;

public interface ObjTicketCallback {
	public void callback(List<ObjActivityTicket> objects, AVException e);
}
