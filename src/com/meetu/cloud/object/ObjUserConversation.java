package com.meetu.cloud.object;

import java.io.Serializable;
import java.util.List;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;
import com.meetu.common.Constants;
@AVClassName("ObjUserConversation")
public class ObjUserConversation extends AVObject implements Serializable{
	public static final Creator CREATOR = AVObjectCreator.instance;
	//会话ID
	public static String ID_CONVERSATION = "conversationId";
	//附加ID（活动ID，觅聊ID）
	public static String ID_APPEND = "appendId";
	//会话创建者
	public static String CONV_CREATOR = "creator";
	//会话关联者
	public static String CONV_USER = "user";
	//会话状态
	public static String STATUS = "status";
	//会话类型
	public static String TYPE = "type";
	//标题
	public static String TITLE = "title";
	//静音标记
	public static String MUTE = "mute";
	//是否拒绝接收消息
	public static String REFUSE_MSG = "refuseMsg";
	//会话结束时间
	public static String OVERTIME = "overTime";
	//会话成员
	public static final String MEMBETS = "member";
	public String getIdConversation() {
		return this.getString(ID_CONVERSATION);
	}
	public String getIdConvAppend() {
		return this.getString(ID_APPEND);
	}
	public ObjUser getConvCreator() {
		return this.getAVUser(CONV_CREATOR, ObjUser.class);
	}
	public ObjUser getConvUser() {
		return this.getAVUser(CONV_USER, ObjUser.class);
	}
	public int getStatus() {
		return this.getInt(STATUS);
	}
	public int getType() {
		return this.getInt(TYPE);
	}
	public String getTitle() {
		return this.getString(TITLE);
	}
	public int getMute() {
		if(this.getBoolean(MUTE)){
			return 1;
		}else{
			return 0;
		}
	}
	public int getRefuseMsg() {
		if(this.getBoolean(REFUSE_MSG)){
			return 1;
		}else{
			return 0;
		}
	}
	public long getOverTime() {
		return this.getLong(OVERTIME);
	}
	public List<String> getMembers() {
		return this.getList(MEMBETS);
	}
	
}
