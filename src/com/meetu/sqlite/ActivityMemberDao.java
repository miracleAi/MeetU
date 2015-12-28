package com.meetu.sqlite;

import android.content.Context;

/** 
 * @author  lucifer 
 * @date 2015-12-26
 * @return  
 */
public class ActivityMemberDao {
	private MySqliteDBHelper dbHelper;

	public ActivityMemberDao(Context context) {
		// TODO Auto-generated constructor stub
		dbHelper = new MySqliteDBHelper(context);
	}
	

}
