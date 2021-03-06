package com.meetu;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cc.imeetu.R;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.meetu.activity.LoginActivity;
import com.meetu.activity.LoginOrRegisterActivity;
import com.meetu.activity.RegisterActivity;
import com.meetu.activity.RegisterVerificationActivity;
import com.meetu.activity.ReportActivity;
import com.meetu.activity.SetPersonalInformation2Activity;
import com.meetu.activity.SetPersonalInformationActivity;
import com.meetu.activity.SystemSettingsActivity;
import com.meetu.activity.homepage.ActivityFeedbackActivity;
import com.meetu.activity.homepage.BarrageActivity;
import com.meetu.activity.homepage.HomePageDetialActivity;
import com.meetu.activity.homepage.JoinActivity;
import com.meetu.activity.homepage.JoinUsersActivity;
import com.meetu.activity.homepage.MemoryWallActivity;

import com.meetu.activity.messages.CreateLitterNoteActivity;
import com.meetu.activity.messages.NotesActivity;
import com.meetu.activity.miliao.ApplyForMiLiaoActivity;
import com.meetu.activity.miliao.ChatGroupActivity;
import com.meetu.activity.miliao.CreationChatActivity;
import com.meetu.activity.miliao.EmojiParser;
import com.meetu.activity.miliao.MiLiaoInfoActivity;
import com.meetu.activity.miliao.MiLiaoUsersListActivity;
import com.meetu.activity.miliao.XmlEmojifPullHelper;
import com.meetu.activity.mine.UserPagerActivity;
import com.meetu.adapter.JoinUserAdapter;
import com.meetu.adapter.MiLiaoUsersListAdapter;
import com.meetu.baidumapdemo.BaiduMapMainActivity;
import com.meetu.common.DBManager;
import com.meetu.common.city.ShengshiquActivity;
import com.meetu.entity.ChatEmoji;
import com.meetu.sqlite.DBManagerCity;
import com.meetu.sqlite.EmojisDao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.test.suitebuilder.TestMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class WelcomeActivity extends Activity {

	//导入学校数据库到本地
	private  DBManager dbHelper;

	//导入城市数据库到本地
	private  DBManagerCity dbHelperCity;
	Handler handler ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);

		handler = new Handler();
		// 导入数据库
		dbHelper = new DBManager(this);
		dbHelper.openDatabase(); 
		dbHelper.closeDatabase();

		dbHelperCity = new DBManagerCity(this);
		dbHelperCity.openDatabase();
		dbHelperCity.closeDatabase();
		next();
	}


	private SharedPreferences sp;

	private void next() {
		sp = super.getSharedPreferences("app_param", Context.MODE_PRIVATE);
		int user = sp.getInt("user", 0);
		if (user == 0) {
			goIndex();
		} else {
			goHome();
		}
	}

	/**
	 * 首次进入app 进行的操作
	 */
	private void goIndex() {
		//解析xml  开子线程处理任务
		new MyAsyncTask().execute();
	}

	private void goHome() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				AVUser currentUser = AVUser.getCurrentUser();
				if (currentUser != null) {
					// 允许用户使用应用
					Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
					WelcomeActivity.this.startActivity(intent);
					finish();
				} else {
					//缓存用户对象为空时， 可打开用户注册界面…
					Intent intent = new Intent(WelcomeActivity.this,LoginOrRegisterActivity.class);
					startActivity(intent);
					finish();
				}
			}
		}, 2000);
	}

	class MyAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			loadEmoji();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// 加载结束，进入引导页
			Intent intent = new Intent(WelcomeActivity.this,GuidActivity.class);
			startActivity(intent);
			finish();
		}

	}

	// 解析 xml 返回 emoji list
	private static EmojiParser parser;
	private static List<ChatEmoji> chatEmojis;
	private EmojisDao emojisDao;

	private void loadEmoji() {

		// TODO 删除以前插入的数据 测试需要
		// emojisDao.deleteAll();

		try {
			emojisDao = new EmojisDao(this);

			InputStream is = this.getAssets()
					.open("expressionImage_custom.xml");
			parser = new XmlEmojifPullHelper();
			// parser=new XmlEmojiSaxBookParser();
			chatEmojis = parser.parse(is);
			for (ChatEmoji emoji : chatEmojis) {
				// 根据String类型id获取对应资源id
				int resID = this.getResources().getIdentifier(
						emoji.getFaceName(), "drawable", this.getPackageName());
//				log.e("lucifer222222", "name" + emoji.getFaceName()
//						+ " resID==" + resID);
				emoji.setId(resID);
			}

			// 插入到sqlite 数据库
			for (ChatEmoji emoji : chatEmojis) {
				emojisDao.insert(emoji);
			}

		} catch (IOException e1) {
			log.e("2", e1);
			e1.printStackTrace();
		} catch (Exception e) {

			log.e("3", e);
			e.printStackTrace();
		}

	}

}
