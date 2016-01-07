package com.meetu.sqlite;

import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.im.v2.Conversation;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.common.Constants;
import com.meetu.common.DbConstents;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.StaticLayout;

public class MySqliteDBHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;

	private static final String TBL_CHANNEL = "channel";
	private static final String TBL_CHANNEL_COLUMN_ID = "_id";
	private static final String TBL_CHANNEL_COLUMN_CODE = "_code";
	private static final String TBL_CHANNEL_COLUMN_NAME = "_name";
	private static final String TBL_CHANNEL_COLUMN_URL = "_url";
	private static final String TBL_CHANNEL_COLUMN_STATE = "_state";

	private static final String TBL_USERS = "users";
	private static final String TBL_USERS_COLUMN_ID = "_id";
	private static final String TBL_USERS_COLUMN_NAME = "_name";
	private static final String TBL_USERS_COLUMN_PWD = "_pwd";

	// 聊天消息表
	private static final String TBL_CHATMSGS = "chatmsgs";// 表名
	private static final String TBL_CHATMSGS_MESSAGE_CACHE_ID = "_message_cache_id";// 本地消息缓存id
	private static final String TBL_CHATMSGS_MESSAGE_ID = "_message_id";//
	private static final String TBL_CHATMSGS_CLIENT_ID = "_client_id";//
	private static final String TBL_CHATMSGS_CONVERSATION_ID = "_conversation_id";// 属于哪个对话id
	private static final String TBL_CHATMSGS_CHAT_MSG_TYPE = "_chat_msg_type";// 消息类型
	private static final String TBL_CHATMSGS_CHAT_MSG_DIRECTION = "_chat_msg_direction";//
	private static final String TBL_CHATMSGS_CHAT_MSG_STATUS = "_chat_msg_status";//
	private static final String TBL_CHATMSGS_IS_SHOW_TIME = "_is_show_time";// 是否显示时间
	private static final String TBL_CHATMSGS_SEND_TIME_STAMP = "_send_time_stamp";//
	private static final String TBL_CHATMSGS_DELIVERED_TIME_STAMP = "_delivered_time_stamp";//
	private static final String TBL_CHATMSGS_CONTENT = "_content";// 消息内容
	private static final String TBL_CHATMSGS_IMG_MSG_IMAGE_URL = "_img_msg_image_url";//
	private static final String TBL_CHATMSGS_IMG_MSG_IMAGE_WIDTH = "_img_msg_image_width";// 图片宽
	private static final String TBL_CHATMSGS_IMG_MSG_IMAGE_HIGHT = "_img_msg_image_height";// 图片高
	private static final String TBL_CHATMSGS_NOW_JOIN_USER_ID = "_now_join_user_id";// 新加入成员的id
	private static final String TBL_CHATMSGS_NOTIFICATION_BASE_CONTENT = "_notification_base_content";//
	private static final String TBL_CHATMSGS_NOTIFICATION_ACTY_CONTENT = "_notification_acty_content";//
	private static final String TBL_CHATMSGS_NOTIFICATION_ACTY_TITLE = "_notification_acty_title";//
	private static final String TBL_CHATMSGS_NOTIFICATION_aCTY_TITLE_SUB = "_notification_acty_title_sub";//
	private static final String TBL_CHATMSGS_SCRIPID = "_scrip_id";// 消息内容
	private static final String TBL_CHATMSGS_SCRIPX = "_scrip_x";// 消息内容
	private static final String TBL_CHATMSGS_SCRIPY = "_scrip_y";// 消息内容

	// 消息总表
	private static final String TBL_MESSAGES = "messages";// 表名

	private static final String TBL_MESSAGES_CACHEID = "_messages_cache_id";
	private static final String TBL_MESSAGES_CONVERSATION_ID = "_conversation_id";
	private static final String TBL_MESSAGES_CONVERSATION_TYPE = "_conversation_type";
	private static final String TBL_MESSAGES_CREATOR_ID = "_creator_id";
	private static final String TBL_MESSAGES_TIME_OVER = "_time_over";
	private static final String TBL_MESSAGES_ACTY_ID = "_acty_id";
	private static final String TBL_MESSAGES_ACTY_NAME = "_acty_name";
	private static final String TBL_MESSAGES_CHAT_ID = "_chat_id";
	private static final String TBL_MESSAGES_CHAT_NAME = "_chat_name";
	private static final String TBL_MESSAGES_TI_STATUS = "_ti_status";
	private static final String TBL_MESSAGES_UNREAD_COUNT = "_unread_count";
	private static final String TBL_MESSAGES_UPDATE_TIME = "_update_time";

	// 表情
	private static final String TBL_EMOJIS = "emojis";// 表名
	private static final String TBL_EMOJIS_CACHE_ID = "_emojis_cache_id";// 缓存id
	private static final String TBL_EMOJIS_ID = "_emojis_id";// 表情对应的本子资源图片的id
	private static final String TBL_EMOJIS_CHARACTER = "_character";// 表情对应的文字表述
	// 例如【微笑】
	private static final String TBL_EMOJIS_FACE_NAME = "_face_name";// 表情资源文件名

	public MySqliteDBHelper(Context context) {
		super(context, DbConstents.DBNAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		StringBuffer sb = new StringBuffer();
		sb.append("create table if not exists ");
		sb.append(TBL_CHANNEL + "(");
		sb.append(TBL_CHANNEL_COLUMN_ID
				+ " integer primary key autoincrement ,");
		sb.append(TBL_CHANNEL_COLUMN_CODE + " varchar(100) ,");
		sb.append(TBL_CHANNEL_COLUMN_NAME + " varchar(100) ,");
		sb.append(TBL_CHANNEL_COLUMN_URL + " varchar(100),");
		sb.append(TBL_CHANNEL_COLUMN_STATE + " varchar(1000) ");
		sb.append(")");
		db.execSQL(sb.toString());

		StringBuffer sb2 = new StringBuffer();
		sb2.append("create table if not exists ");
		sb2.append(TBL_USERS + "(");
		sb2.append(TBL_USERS_COLUMN_ID + " integer primary key autoincrement ,");
		sb2.append(TBL_USERS_COLUMN_NAME + " varchar(100) ,");
		sb2.append(TBL_USERS_COLUMN_PWD + " varchar(100) ");
		sb2.append(")");
		db.execSQL(sb2.toString());

		/**
		 * 聊天消息表
		 */
		StringBuffer sb3 = new StringBuffer();
		sb3.append("create table if not exists ");
		sb3.append(TBL_CHATMSGS + "(");
		sb3.append(TBL_CHATMSGS_MESSAGE_CACHE_ID
				+ " varchar(100) primary key ,");
		sb3.append(Constants.USERID + " varchar(100) ,");
		sb3.append(TBL_CHATMSGS_MESSAGE_ID + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_CLIENT_ID + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_CONVERSATION_ID + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_CHAT_MSG_TYPE + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_CHAT_MSG_DIRECTION + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_CHAT_MSG_STATUS + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_IS_SHOW_TIME + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_SEND_TIME_STAMP + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_DELIVERED_TIME_STAMP + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_CONTENT + " varchar(10000) , ");
		sb3.append(TBL_CHATMSGS_IMG_MSG_IMAGE_URL + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_IMG_MSG_IMAGE_WIDTH + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_IMG_MSG_IMAGE_HIGHT + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_NOW_JOIN_USER_ID + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_NOTIFICATION_BASE_CONTENT + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_NOTIFICATION_ACTY_CONTENT + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_NOTIFICATION_ACTY_TITLE + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_NOTIFICATION_aCTY_TITLE_SUB + " varchar(100) ,");
		sb3.append(TBL_CHATMSGS_SCRIPID + " varchar(100) , ");
		sb3.append(TBL_CHATMSGS_SCRIPX + " Integer , ");
		sb3.append(TBL_CHATMSGS_SCRIPY + " Integer ");
		sb3.append(")");
		db.execSQL(sb3.toString());

		/**
		 * 消息总表,会话表
		 */
		StringBuffer sb4 = new StringBuffer();
		sb4.append("create table if not exists ");
		sb4.append(TBL_MESSAGES + "(");
		// sb4.append(TBL_MESSAGES_CACHEID+
		// " integer primary key autoincrement ,");
		sb4.append(Constants.USERID + " varchar(100) ,");
		sb4.append(TBL_MESSAGES_CONVERSATION_ID + " varchar(100) ,");
		sb4.append(TBL_MESSAGES_CONVERSATION_TYPE + " Integer ,");
		sb4.append(TBL_MESSAGES_TI_STATUS + " Integer ,");
		sb4.append(TBL_MESSAGES_CREATOR_ID + " varchar(100) ,");
		sb4.append(TBL_MESSAGES_TIME_OVER + " varchar(100) ,");
		sb4.append(TBL_MESSAGES_ACTY_ID + " varchar(100) ,");
		sb4.append(TBL_MESSAGES_ACTY_NAME + " varchar(100) ,");
		sb4.append(TBL_MESSAGES_CHAT_ID + " varchar(100) ,");
		sb4.append(TBL_MESSAGES_CHAT_NAME + " varchar(100) ,");
		sb4.append(TBL_MESSAGES_UNREAD_COUNT + " Integer ,");
		sb4.append(TBL_MESSAGES_UPDATE_TIME + " Integer ,");
		sb4.append("constraint " + TBL_MESSAGES_CACHEID + " primary key ("
				+ Constants.USERID + "," + TBL_MESSAGES_CONVERSATION_ID + ") ");
		sb4.append(")");
		db.execSQL(sb4.toString());

		/**
		 * emoji 聊天表情 表
		 */
		StringBuffer sb5 = new StringBuffer();
		sb5.append("create table if not exists ");
		sb5.append(TBL_EMOJIS + "(");
		sb5.append(TBL_EMOJIS_CACHE_ID + " integer primary key autoincrement ,");
		sb5.append(TBL_EMOJIS_ID + " varchar(100) ,");
		sb5.append(TBL_EMOJIS_CHARACTER + " varchar(100) ,");
		sb5.append(TBL_EMOJIS_FACE_NAME + " varchar(100) ");

		sb5.append(")");
		db.execSQL(sb5.toString());
		/**
		 * 首页活动缓存表
		 * */
		StringBuffer activitySb = new StringBuffer();
		activitySb.append("create table if not exists ");
		activitySb.append(DbConstents.ACTIVITY_CACHE_TB + "(");
		activitySb.append("id integer primary key autoincrement ,");
		activitySb.append(DbConstents.ID_MINE + " varchar(100) ,");
		activitySb.append(DbConstents.ACTIVITYID + " varchar(100) ,");
		activitySb.append(DbConstents.ACTYCREATORID + " varchar(100) ,");
		activitySb.append(DbConstents.ISACTIVITYPRAISE + " integer ,");
		activitySb.append(DbConstents.ACTIVITYFOLLOWCOUNT + " integer ,");
		activitySb.append(DbConstents.ACTIVITYCOVER + " varchar(100) ,");
		activitySb.append(DbConstents.TIMESTART + " Integer ,");
		activitySb.append(DbConstents.STATUS + " Integer ,");
		activitySb.append(DbConstents.ACTIVITYCONTENT + " varchar(100) ,");
		activitySb.append(DbConstents.PRAISECOUNT + " Integer ,");
		activitySb.append(DbConstents.ORDERCOUNTBOY + " Integer ,");
		activitySb.append(DbConstents.ORDERCOUNTGIRL + " Integer ,");
		activitySb.append(DbConstents.TITLE + " varchar(100) ,");
		activitySb.append(DbConstents.TITLESUB + " varchar(100) ,");
		activitySb.append(DbConstents.LOCATIONADDRESS + " varchar(100) ,");
		activitySb.append(DbConstents.LOCATIONPLACE + " varchar(100) ,");
		activitySb.append(DbConstents.LOCATIONGOVERNMENT + " varchar(100) ,");
		activitySb.append(DbConstents.TIMESTOP + " Integer ,");
		activitySb.append(DbConstents.TIMECHATSTOP + " Integer ,");
		activitySb.append(DbConstents.ACTIVITYINDEX + " Integer ,");
		activitySb.append(DbConstents.LOCATIONLONGTITUDE + " varchar(100) ,");
		activitySb.append(DbConstents.LOCATIONLATITUDE + " varchar(100) ,");
		activitySb.append(DbConstents.CONVERSATIONID + " varchar(100) ,");
		activitySb.append(DbConstents.ISCACHEFLAG + " varchar(100) ");
		activitySb.append(")");
		db.execSQL(activitySb.toString());
		/**
		 * 用户集合缓存表
		 * */
		StringBuffer aboutSb = new StringBuffer();
		aboutSb.append("create table if not exists ");
		aboutSb.append(DbConstents.USERABOUT_CACHE_TB + "(");
		aboutSb.append(DbConstents.ID_MINE + " varchar(100) ,");
		aboutSb.append(DbConstents.ABOUTTYPE + " Integer ,");
		aboutSb.append(DbConstents.ABOUTUSERID + " varchar(100) ,");
		aboutSb.append(DbConstents.ABOUTCOLECTIONID + " varchar(100) ,");
		aboutSb.append("constraint " + DbConstents.ABOUTCACHEID + " primary key ("
				+ DbConstents.ID_MINE + ","+ DbConstents.ABOUTUSERID + "," + DbConstents.ABOUTCOLECTIONID + ") ");
		aboutSb.append(")");
		db.execSQL(aboutSb.toString());
		/**
		 * 用户缓存表
		 * */
		StringBuffer userSb = new StringBuffer();
		userSb.append("create table if not exists ");
		userSb.append(DbConstents.USERINFO_TB + "(");
		userSb.append(DbConstents.ID_MINE + " varchar(100) primary key,");
		userSb.append(DbConstents.USERTYPE + " Integer ,");
		userSb.append(DbConstents.ISVIP + " Integer ,");
		userSb.append(DbConstents.ISCOMPLETE + " Integer ,");
		userSb.append(DbConstents.ISVERIFY + " Integer ,");
		userSb.append(DbConstents.GENDER + " Integer ,");
		userSb.append(DbConstents.NICKNAME + " varchar(100) ,");
		userSb.append(DbConstents.REALNAME + " varchar(100) ,");
		userSb.append(DbConstents.CONSTELLATION + " varchar(100) ,");
		userSb.append(DbConstents.BIRTHDAY + " Integer ,");
		userSb.append(DbConstents.SCHOOL + " varchar(100) ,");
		userSb.append(DbConstents.SCHOOLLOCATION + " Integer ,");
		userSb.append(DbConstents.SCHOOLNUM + " Integer ,");
		userSb.append(DbConstents.DEPARTMENT + " varchar(100) ,");
		userSb.append(DbConstents.DEPARTMENTID + " Integer ,");
		userSb.append(DbConstents.HOMETOWN + " varchar(100) ,");
		userSb.append(DbConstents.PROFILECLIP + " varchar(100) ,");
		userSb.append(DbConstents.PROFILEORIGN + " varchar(100) ,");
		userSb.append(DbConstents.USER_CACHE_TIME + " varchar(100) ");
		userSb.append(")");
		db.execSQL(userSb.toString());
		/**
		 * 屏蔽我的人缓存表
		 * */
		StringBuffer userShieldTb = new StringBuffer();
		userShieldTb.append("create table if not exists ");
		userShieldTb.append(DbConstents.USER_SHIELD_TB + "(");
		userShieldTb.append("id integer primary key autoincrement ,");
		userShieldTb.append(DbConstents.ID_MINE + " varchar(100) ,");
		userShieldTb.append(DbConstents.SHIELD_USER_ID + " varchar(100) ");
		userShieldTb.append(")");
		db.execSQL(userShieldTb.toString());

		/**
		 * 活动成员表
		 * */
		StringBuffer actyMemSb = new StringBuffer();
		actyMemSb.append("create table if not exists ");
		actyMemSb.append(DbConstents.MEMBER_ACTY_TB + "(");
		actyMemSb.append(DbConstents.ID_MINE + " varchar(100) ,");
		actyMemSb.append(DbConstents.ID_ACTY + " varchar(100) ,");
		actyMemSb.append(DbConstents.ID_ACTY_MEMBER + " varchar(100) ,");
		actyMemSb.append(DbConstents.ID_ACTY_CONVERSATION + " varchar(100) ,");
		actyMemSb.append(DbConstents.STATUS_ACTY_CONV + " Integer ,");
		actyMemSb.append("constraint " + DbConstents.ID_MINE_MEMBER_ACTY + " primary key ("
				+ DbConstents.ID_MINE + "," +DbConstents.ID_ACTY+","+ DbConstents.ID_ACTY_MEMBER + ") ");
		actyMemSb.append(")");
		db.execSQL(actyMemSb.toString());
		/**
		 * 觅聊成员表
		 * */
		StringBuffer seekMemSb = new StringBuffer();
		seekMemSb.append("create table if not exists ");
		seekMemSb.append(DbConstents.MEMBER_SEEK_TB + "(");
		seekMemSb.append(DbConstents.ID_MINE + " varchar(100) ,");
		seekMemSb.append(DbConstents.ID_SEEK_MEMBER + " varchar(100) ,");
		seekMemSb.append(DbConstents.ID_SEEK + " varchar(100) ,");
		seekMemSb.append(DbConstents.ID_SEEK_CONVERSATION + " varchar(100) ,");
		seekMemSb.append(DbConstents.STATUS_SEEK_CONV + " Integer ,");
		seekMemSb.append("constraint " + DbConstents.ID_MINE_MEMBER_SEEK + " primary key ("
				+ DbConstents.ID_MINE + "," +DbConstents.ID_SEEK+","+ DbConstents.ID_SEEK_MEMBER + ") ");
		seekMemSb.append(")");
		db.execSQL(seekMemSb.toString());
		/**
		 * 用户-会话表
		 */
		StringBuffer convSb = new StringBuffer();
		convSb.append("create table if not exists ");
		convSb.append(DbConstents.CONVERSATION_USER_TB + "(");
		convSb.append(DbConstents.ID_MINE + " varchar(100) ,");
		convSb.append(DbConstents.ID_CONVERSATION + " varchar(100) ,");
		convSb.append(DbConstents.ID_CONV_APPEND + " varchar(100) ,");
		convSb.append(DbConstents.ID_CONV_CREATOR + " varchar(100) ,");
		convSb.append(DbConstents.STATUS_CONV + " Integer ,");
		convSb.append(DbConstents.TYPE_CONV + " Integer ,");
		convSb.append(DbConstents.CONV_MUTE + " Integer ,");
		convSb.append(DbConstents.TITLE_CONV + " varchar(100) ,");
		convSb.append(DbConstents.CONV_OVER_TIME + " Integer ,");
		convSb.append(DbConstents.CONV_UPDATE_TIME + " Integer ,");
		convSb.append(DbConstents.UNREAD_COUNT + " Integer ,");
		convSb.append("constraint " + DbConstents.ID_MINE_CONVERSATION + " primary key ("
				+ DbConstents.ID_MINE + "," + DbConstents.ID_CONVERSATION + ") ");
		convSb.append(")");
		db.execSQL(convSb.toString());
		
		/**
		 * 聊天消息表
		 * */
		StringBuffer chatMsgSb = new StringBuffer();
		chatMsgSb.append("create table if not exists ");
		chatMsgSb.append(DbConstents.MSG_CHAT_TB + "(");
		chatMsgSb.append(DbConstents.ID_CACHE_MSG
				+ " varchar(100) primary key,");
		chatMsgSb.append(DbConstents.ID_MINE + " varchar(100) ,");
		chatMsgSb.append(DbConstents.ID_MESSAGE + " varchar(100) , ");
		chatMsgSb.append(DbConstents.ID_CLIENT + " varchar(100) , ");
		chatMsgSb.append(DbConstents.SEND_TIME_STAMP + " Integer , ");
		chatMsgSb.append(DbConstents.MSG_TEXT + " varchar(100) , ");
		chatMsgSb.append(DbConstents.MSG_FILE_URL + " varchar(100) , ");
		chatMsgSb.append(DbConstents.MSG_IMG_HEIGH + " varchar(100) , ");
		chatMsgSb.append(DbConstents.MSG_IMG_WIDTH + " varchar(100) , ");
		chatMsgSb.append(DbConstents.ID_MSG_CONVERSATION + " varchar(100) , ");
		chatMsgSb.append(DbConstents.ID_OPERATED + " varchar(100) , ");
		chatMsgSb.append(DbConstents.TYPE_MSG + " Integer , ");
		chatMsgSb.append(DbConstents.STATUS_MSG + " Integer , ");
		chatMsgSb.append(DbConstents.DIRECTION_MSG + " varchar(100) , ");
		chatMsgSb.append(DbConstents.IS_SHOW_TIME + " varchar(100)");
		/*chatMsgSb.append("constraint " + DbConstents.ID_CACHE_MSG + " primary key ("
				+ DbConstents.ID_MINE + "," + DbConstents.ID_MESSAGE + ") ");*/
		chatMsgSb.append(")");
		db.execSQL(chatMsgSb.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub

		onCreate(db);
	}

}
