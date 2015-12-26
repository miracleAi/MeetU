package com.meetu.common;

/**
 * @author lucifer
 * @date 2015-12-26
 * @return
 */
public class ConversationuserBean {
	/**
	 * 用户本人ID
	 */
	public static String mineId;

	/**
	 * 会话ID
	 */
	public static String conversationId;
	/**
	 * 附加ID（活动ID，觅聊ID）
	 */
	public static String appendId;
	/**
	 * 会话创建者ID
	 */
	public static String convCreatorId;
	/**
	 * 会话状态
	 */
	public static String covnStatus;
	/**
	 * 会话类型
	 */
	public static String convType;
	/**
	 * 标题
	 */
	public static String convTitle;
	/**
	 * 静音标记
	 */
	public static String convMute;
	/**
	 * 会话结束时间
	 */
	public static String overTime;
	/**
	 * 最后更新时间
	 */
	public static String updateTime;
	public static String getMineId() {
		return mineId;
	}
	public static void setMineId(String mineId) {
		ConversationuserBean.mineId = mineId;
	}
	public static String getConversationId() {
		return conversationId;
	}
	public static void setConversationId(String conversationId) {
		ConversationuserBean.conversationId = conversationId;
	}
	public static String getAppendId() {
		return appendId;
	}
	public static void setAppendId(String appendId) {
		ConversationuserBean.appendId = appendId;
	}
	public static String getConvCreatorId() {
		return convCreatorId;
	}
	public static void setConvCreatorId(String convCreatorId) {
		ConversationuserBean.convCreatorId = convCreatorId;
	}
	public static String getCovnStatus() {
		return covnStatus;
	}
	public static void setCovnStatus(String covnStatus) {
		ConversationuserBean.covnStatus = covnStatus;
	}
	public static String getConvType() {
		return convType;
	}
	public static void setConvType(String convType) {
		ConversationuserBean.convType = convType;
	}
	public static String getConvTitle() {
		return convTitle;
	}
	public static void setConvTitle(String convTitle) {
		ConversationuserBean.convTitle = convTitle;
	}
	public static String getConvMute() {
		return convMute;
	}
	public static void setConvMute(String convMute) {
		ConversationuserBean.convMute = convMute;
	}
	public static String getOverTime() {
		return overTime;
	}
	public static void setOverTime(String overTime) {
		ConversationuserBean.overTime = overTime;
	}
	public static String getUpdateTime() {
		return updateTime;
	}
	public static void setUpdateTime(String updateTime) {
		ConversationuserBean.updateTime = updateTime;
	}
	

}
