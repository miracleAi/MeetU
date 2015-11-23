package com.meetu.cloud.object;

import java.io.Serializable;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;
@AVClassName("ObjActivityTicket")
public class ObjActivityTicket extends AVObject{
	public static final Creator CREATOR = AVObjectCreator.instance;
	
	public ObjActivityTicket() {
		// TODO Auto-generated constructor stub
	}
	/**
	 *  票所属活动（只读）
	 */
	public static final String ACTIVITY= "activity";
	/**
	 *  票的标题（只读）
	 */
	public static final String TICKETTITLE= "ticketTitle";
	/**
	 *  票的基本描述（只读）
	 */
	public static final String TICKETDESCRIPTION= "ticketDescription";
	/**
	 *  标准价格（只读）
	 */
	public static final String PRICE= "price";
	/**
	 *  会员价格（只读）
	 */
	 public static final String PRICEVIP= "priceVip";
	/**
	 *  票的总张数（只读）
	 */
	public static final String TICKETCOUNT= "ticketCount";
	/**
	 *  已售出总数量（只读）
	 */
	public static final String TICKETSALECOUNT= "ticketSaleCount";

	public ObjActivity getActivity() {
		return this.getAVObject(ACTIVITY);
	}
	public String getTicketTitle() {
		return this.getString(TICKETTITLE);
	}
	public String getTicketDescription() {
		return this.getString(TICKETDESCRIPTION);
	}
	public int getPrice() {
		return this.getInt(PRICE);
	}
	public String getPriceVip() {
		return this.getString(PRICEVIP);
	}
	public int getTicketCount() {
		return this.getInt(TICKETCOUNT);
	}
	public int getTicketSaleCount() {
		return this.getInt(TICKETSALECOUNT);
	}
	
}
