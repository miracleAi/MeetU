package com.meetu.cloud.utils;

import com.avos.avoscloud.im.v2.AVIMMessage.AVIMMessageIOType;
import com.avos.avoscloud.im.v2.AVIMMessage.AVIMMessageStatus;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.meetu.common.Constants;

public class ChatMsgUtils {
	//消息类型转换
	public static int getType(AVIMReservedMessageType t){
		if(t == AVIMReservedMessageType.TextMessageType){
			return Constants.TEXT_TYPE;
		}
		if(t == AVIMReservedMessageType.ImageMessageType){
			return Constants.IMAGE_TYPE;
		}
		if(t == AVIMReservedMessageType.AudioMessageType){
			return Constants.AUDIO_TYPE;
		}
		if(t == AVIMReservedMessageType.VideoMessageType){
			return Constants.VEDIO_TYPE;
		}
		if(t == AVIMReservedMessageType.LocationMessageType){
			return Constants.LOCATION_TYPE;
		}
		if(t == AVIMReservedMessageType.FileMessageType){
			return Constants.FILE_TYPE;
		}
		if(t == AVIMReservedMessageType.UnsupportedMessageType){
			return Constants.UNSUPPORT_TYPE;
		}
		return 0;
	}
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
	//发送消息是否显示转换
	public static boolean getsendTimeIsShow(int i){
		if(i == Constants.TIMESHOW){
			return true;
		}else{
			return false;
		}
	}
	//接收消息是否显示转换
	public static int geRecvTimeIsShow(boolean b){
		if(b){
			return Constants.TIMESHOW;
		}else{
			return Constants.TIMESHOWNOT;
		}
	}
	//设置是否显示消息
	public static int isShowChatTime(long start){
		long t = System.currentTimeMillis() - start;
		if(t > 60000){
			return Constants.TIMESHOW;
		}else{
			return Constants.TIMESHOWNOT;
		}
	}
}
