package com.meetu.cloud.wrap;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjFunEnumCallback;
import com.meetu.cloud.callback.ObjFunObjectCallback;
import com.meetu.cloud.callback.ObjUserCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjUser;


/**
 * Created by mac on 15/9/9.
 */
public class ObjUserWrap {
	/**检查此手机号码是否已经注册
	 * @param phone 检查的手机号码
	 * @param callback 回调对象
	 */
	public static void phoneIsAlreadyRegister(String phone, final ObjFunEnumCallback callback){
		if (callback != null){
			//查询用户，你需要使用特殊的用户查询对象来完成
			AVQuery<AVUser> query = AVUser.getQuery();
			query.whereEqualTo("mobilePhoneNumber", phone);
			query.getFirstInBackground(new GetCallback<AVUser>() {

				@Override
				public void done(AVUser user, AVException e) {

					if(user != null && !user.getObjectId().equals("")){
						callback.callback(ObjExecResult.EXEC_TRUE, null);
						return ;
					}
					if(e == null){
						callback.callback(ObjExecResult.EXEC_FALSE, null);
					}else{
						callback.callback(ObjExecResult.EXEC_EXCEPTION, e);
					}
				}
			});
		}
	}


	/**
	 * 用户登录
	 *
	 * @param phone 登录手机号
	 * @param passwd 登录密码
	 * @param callback 回调对象
	 */
	public static void login(String phone, String passwd, final ObjFunObjectCallback callback){
		if (callback != null){
			ObjUser.loginByMobilePhoneNumberInBackground(phone, passwd, new LogInCallback<AVUser>() {
				@Override
				public void done(AVUser avUser, AVException e) {
					if (avUser != null){
						callback.callback(avUser, null);
					}else {
						if (e.getCode() == 210) {
							callback.callback(null, new AVException(1, "密码不正确"));
						}else {
							callback.callback(null, new AVException(2, "用户不存在"));
						}
					}
				}
			});
		}
	}

	/**
	 * 用户注册
	 * @param phone 手机号码
	 * @param passwd 初始密码
	 * @param smsCode 短信验证码
	 * @param callback 回调对象
	 *
	 *
	 */
	public static void register(final String phone, final String passwd, String smsCode, final ObjFunBooleanCallback callback){
		if (callback != null){
			AVOSCloud.verifySMSCodeInBackground(smsCode, phone, new AVMobilePhoneVerifyCallback() {
				@Override
				public void done(AVException e) {
					if (e == null){
						ObjUser user = new ObjUser();

						user.setUsername(ObjWrap.createId());
						user.setMobilePhoneNumber(phone);
						user.setPassword(passwd);

						user.setUserType(0);
						user.setIsVipUser(false);
						user.setIsVerifyUser(false);
						user.setIsCompleteUserInfo(false);

						user.setFetchWhenSave(true);

						user.signUpInBackground(new SignUpCallback() {
							@Override
							public void done(AVException e) {
								if (e == null){
									callback.callback(true, null);
								}else {
									log.e("注册失败异常", e.toString() +" "+e.getCode());

									callback.callback(false, new AVException(1, "注册失败"));
								}
							}
						});
					}else {
						callback.callback(false, new AVException(0, "短信验证错误"));
					}
				}
			});
		}
	}


	/**
	 * 完善个人信息
	 *
	 * @param user 用户对象
	 * @param callback 回调对象
	 */
	public static void completeUserInfo(ObjUser user, final ObjFunBooleanCallback callback){
		if (callback != null){
			user.saveInBackground(new SaveCallback() {
				@Override
				public void done(AVException e) {
					if (e == null){
						callback.callback(true, null);
					}else {
						callback.callback(false, null);
					}
				}
			});
		}
	}

	/**
	 * 获取重置密码短信
	 * @param phone 手机号码
	 * @param callback 回调对象
	 */
	public static void requestSmsCodeForResetPasswd(String phone, final ObjFunBooleanCallback callback){
		log.e("zcq", "phone"+phone);
		if (callback != null){
			ObjUser.requestPasswordResetBySmsCodeInBackground(phone, new RequestMobileCodeCallback() {
				@Override
				public void done(AVException e) {
					if (e == null){
						log.e("zcq", "e1="+e);
						callback.callback(true, null);
					}else {
						log.e("zcq", "e2="+e);
						callback.callback(false, e);
					}
				}
			});
		}
	}

	/**
	 *
	 * @param smsCode 短信验证码
	 * @param newPasswd 新密码
	 * @param callback 回调对象
	 */
	public static void resetPasswd(String smsCode, String newPasswd, final ObjFunBooleanCallback callback){
		if (callback != null){
			ObjUser.resetPasswordBySmsCodeInBackground(smsCode, newPasswd, new UpdatePasswordCallback() {
				@Override
				public void done(AVException e) {
					if (e == null){
						callback.callback(true, null);
					}else {
						callback.callback(false, e);
					}
				}
			});
		}
	}
	/**
	 * 退出登录，清除缓存用户对象
	 */
	public static void logOut(){
		AVUser.logOut(); 
	}
	/**
	 * 根据用户ID获取用户信息
	 * */
	public static void getObjUser(String objId,final ObjUserInfoCallback callback){
		AVQuery<ObjUser> query = AVUser.getQuery(ObjUser.class);
		query.whereEqualTo("objectId", objId);
		query.setCachePolicy(AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
		//TimeUnit.DAYS.toMillis(1)
		query.setMaxCacheAge(10*60*1000);
		query.getFirstInBackground(new GetCallback<ObjUser>() {

			@Override
			public void done(ObjUser user, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(null, e);
					return;
				}
				if(user != null){
					callback.callback(user, null);
				}else{
					callback.callback(null, new AVException(0, "获取用户信息失败"));
				}
			}
		});
	}
}
