package com.meetu.cloud.object;

import java.io.Serializable;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;

@AVClassName("ObjAuthoriseCategory")
public class ObjAuthoriseCategory extends AVObject implements Serializable {
	public static final Creator CREATOR = AVObjectCreator.instance;
	/**
	 * 操作编号
	 */
	public static final String OPERATIONNUM = "operationNum";
	/**
	 * 是否需要认证
	 */
	public static final String NEEDAUTHORISE = "needAuthorise";
	/**
	 * 操作描述
	 */
	public static final String OPRATIONDESCRIPTION = "operationDescription";

	public ObjAuthoriseCategory() {
		// TODO Auto-generated constructor stub
	}

	public int getOperationNum() {
		return this.getInt(OPERATIONNUM);
	}

	public void setOperationNum(int operationNum) {
		this.put(OPERATIONNUM, operationNum);
		;
	}

	public boolean isNeedAuthorise() {
		return this.getBoolean(NEEDAUTHORISE);
	}

	public void setNeedAuthorise(boolean needAuthorise) {
		this.put(NEEDAUTHORISE, needAuthorise);
	}

	public String getOperationDescription() {
		return this.getString(OPRATIONDESCRIPTION);
	}

	public void setOperationDescription(String operationDescription) {
		this.put(OPRATIONDESCRIPTION, operationDescription);
	}

}
