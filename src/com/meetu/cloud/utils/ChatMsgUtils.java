package com.meetu.cloud.utils;

import com.avos.avoscloud.im.v2.AVIMMessage.AVIMMessageIOType;
import com.avos.avoscloud.im.v2.AVIMMessage.AVIMMessageStatus;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.meetu.common.Constants;

public class ChatMsgUtils {
	//转换消息状态
	public static int getStatus(AVIMMessageStatus s){
		if(s == AVIMMessageStatus.AVIMMessageStatusNone){
			return Constants.STATUES_NONE;
		}
		if(s == AVIMMessageStatus.AVIMMessageStatusSending){
			return Constants.STATUES_SENDING;
		}
		if(s == AVIMMessageStatus.AVIMMessageStatusSent){
			return Constants.STATUES_SENT;
		}
		if(s == AVIMMessageStatus.AVIMMessageStatusReceipt){
			return Constants.STATUES_RECEIPT;
		}
		if(s == AVIMMessageStatus.AVIMMessageStatusFailed){
			return Constants.STATUES_FAILED;
		}
		return 0;
	}
	//转换消息方向
	public static int getDerection(AVIMMessageIOType io){
		if(io == AVIMMessageIOType.AVIMMessageIOTypeIn){
			return Constants.IOTYPE_IN;
		}
		if(io == AVIMMessageIOType.AVIMMessageIOTypeIn){
			return Constants.IOTYPE_OUT;
		}
		return 0;
	}
	//发送消息是否显示-转换
	public static boolean getsendTimeIsShow(int i){
		if(i == Constants.TIMESHOW){
			return true;
		}else{
			return false;
		}
	}
	//接收消息是否显示-转换
	public static int geRecvTimeIsShow(boolean b){
		if(b){
			return Constants.TIMESHOW;
		}else{
			return Constants.TIMESHOWNOT;
		}
	}
	//设置是否显示消息
	public static boolean isShowChatTime(long start){
		long t = System.currentTimeMillis() - start;
		if(t > 60000){
			return true;
		}else{
			return false;
		}
	}
}
