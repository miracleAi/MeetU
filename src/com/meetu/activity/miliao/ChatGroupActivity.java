package com.meetu.activity.miliao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.baidu.location.h.m;
import com.meetu.TestReceiveMsg.MemberChangeHandler;
import com.meetu.activity.messages.ShowSysMsgPhotoActivity;
import com.meetu.adapter.ChatmsgsListViewAdapter;
import com.meetu.bean.CoversationUserBean;
import com.meetu.bean.MessageChatBean;
import com.meetu.bean.SeekChatBean;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.callback.ObjConvUserListCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjChat;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.object.ObjUserConversation;
import com.meetu.cloud.utils.ChatMsgUtils;
import com.meetu.cloud.wrap.ObjActivityWrap;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjChatWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.common.dismissData;
import com.meetu.entity.ChatEmoji;
import com.meetu.entity.Chatmsgs;
import com.meetu.entity.Messages;
import com.meetu.fragment.ChatFragment;
import com.meetu.fragment.HomePagefragment;
import com.meetu.myapplication.DefaultMemberHandler;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.ChatmsgsDao;
import com.meetu.sqlite.ConversationUserDao;
import com.meetu.sqlite.EmojisDao;
import com.meetu.sqlite.MemberSeekDao;
import com.meetu.sqlite.MessageChatDao;
import com.meetu.sqlite.MessagesDao;
import com.meetu.sqlite.UserAboutDao;
import com.meetu.tools.BitmapCut;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;
import com.meetu.tools.StringToDrawbleId;
import com.meetu.tools.UriToimagePath;
import com.meetu.tools.UrlLocationToBitmap;
import com.meetu.view.ChatViewInterface;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChatGroupActivity extends Activity implements OnClickListener,
OnItemClickListener,ChatViewInterface {
	private Context context;
	private RelativeLayout backLayout;
	private ImageView face;
	private LinearLayout faceLayout;
	private RelativeLayout listviewLayout;
	private Boolean faceBoolean = false;// 表情键盘的显示状态
	private EditText mEditText;
	private RelativeLayout userlayout;

	// 解析xml相关
	private EmojiParser parser;
	private EmojisDao emojisDao;
	private static List<ChatEmoji> chatEmojis;
	private List<String> staticFacesList;

	// 添加表情相关

	private ImageView image_face;// 表情图标
	// 7列3行
	private int columns;// 表情的列数
	private int rows = 3;// 表情的行数
	private int chatEmojisNumber;// 表情的总数
	private List<View> views = new ArrayList<View>();
	private LinearLayout chat_face_container;// 表情布局
	private ViewPager mViewPager;
	private LinearLayout mDotsLayout;
	private int current = 0;// 表情第几页
	private List<ChatEmoji> subList;
	private List<ChatEmoji> subAllList = new ArrayList<ChatEmoji>();
	private FaceGVAdapter faceGVAdapter;
	private String emojiKey;

	/** 表情页的监听事件 */
	private OnCorpusSelectedListener mListener;
	/**
	 * 输入框内显示表情的宽高
	 */
	private static int emojiHigh;
	private static int emojiWeight;

	// 聊天列表 相关
	private List<MessageChatBean> chatmsgsCacheList = new ArrayList<MessageChatBean>();
	private CoversationUserBean convUserBean = new CoversationUserBean();
	private ListView mChatmsgsListView;
	private ChatmsgsListViewAdapter mChatmsgsAdapter;
	private RelativeLayout sendlLayout;
	private ImageView send;

	private MessageChatDao msgChatDao = null;
	private ConversationUserDao convUserDao = null;
	private MemberSeekDao memberSeekDao = null;
	private ImageView photo, camera;// 点击发图片
	/**
	 * 聊天列表内显示表情的宽高
	 */
	private static int emojiChatHight;
	private static int emojiChatWeight;

	// 网络数据相关
	AVUser currentUser = ObjUser.getCurrentUser();
	ObjUser user = new ObjUser();

	private int conversationStyle;// 对话类型
	private String conversationId;// 对话id
	
	private AVIMConversation conversation;

	//private MessageHandler msgHandler;

	private AVFile chatPhoto = null;// 用来发送照片

	private String jstitle;// 群聊标题标题title
	private String objectID;// 群聊id
	private long timeOver;//群聊结束时间

	private TextView timeOverTextView;
	private LinearLayout timeLayout;
	private TextView userNumber;
	private TextView title;// 标题

	private RelativeLayout emojiLayout,pictureLayout,cameraLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.fragment_chat);
		if (currentUser != null) {
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		//msgHandler = new MessageHandler();
		emojisDao = new EmojisDao(this);
		//messagesDao = new MessagesDao(this);
		msgChatDao = new MessageChatDao(this);
		convUserDao = new ConversationUserDao(this);
		memberSeekDao = new MemberSeekDao(this);
		chatEmojis = emojisDao.getChatEmojisList();
		columns = DisplayUtils.getWindowWidth(this)
				/ DensityUtil.dip2px(this, 45);

		Intent intent = getIntent();
		conversationStyle = intent.getIntExtra("ConversationStyle",0);
		conversationId = intent.getStringExtra("ConversationId");
		
		conversation = MyApplication.chatClient.getConversation(""
				+ conversationId);
		loadData();
		initView();
		InitViewPager();
		getConvUserInfo();

	}
	private void getConvUserInfo() {
		// TODO Auto-generated method stub
		ArrayList<CoversationUserBean> list = convUserDao.getMessage(user.getObjectId(), conversationId);
		if(list != null && list.size()>0){
			convUserBean = list.get(0);
			objectID = convUserBean.getIdConvAppend();
			timeOver=convUserBean.getOverTime();
			jstitle = convUserBean.getTitle();
			int number = memberSeekDao.queryUserAbout("" + user.getObjectId(),conversationId).size();
			title.setText("" + jstitle);
			userNumber.setText("" + "(" + number + ")");
			if(dismissData.getDismissData(timeOver)==null){
				timeLayout.setVisibility(View.GONE);
			}else{
				timeOverTextView.setText(""+dismissData.getDismissData(timeOver));
			}
			//被踢出  退出 失效 解散 点击之后将失效会话删掉
			int convStatus = convUserBean.getStatus();
			switch (convStatus) {
			case Constants.CONV_STATUS_KICK:
			case Constants.CONV_STATUS_QUIT:
			case Constants.CONV_STATUS_DISSOLVE:
			case Constants.CONV_STATUS_DISMISS:
				deleteConv();
				break;
			default:
				break;
			}
		}else{
			//查询conversationUser表
			ObjChatWrap.getConvUserBean(user, conversationId, new ObjConvUserListCallback() {
				
				@Override
				public void callback(ObjUserConversation object, AVException e) {

					if(e != null){
						log.e("chatgroup", ""+e);
						return;
					}

					objectID = object.getIdConvAppend();
					timeOver=object.getOverTime();
					jstitle = object.getTitle();
					int number = memberSeekDao.queryUserAbout("" + user.getObjectId(),conversationId).size();
					title.setText("" + jstitle);
					userNumber.setText("" + "(" + number + ")");
					if(dismissData.getDismissData(timeOver)==null){
						timeLayout.setVisibility(View.GONE);
					}else{
						timeOverTextView.setText(""+dismissData.getDismissData(timeOver));
					}
					//被踢出  退出 失效 解散 点击之后将失效会话删掉
					int convStatus = object.getStatus();
					switch (convStatus) {
					case Constants.CONV_STATUS_KICK:
					case Constants.CONV_STATUS_QUIT:
					case Constants.CONV_STATUS_DISSOLVE:
					case Constants.CONV_STATUS_DISMISS:
						deleteConv();
						break;
					default:
						break;
					}
				}
			});
		}
	}
	private void initReceiveMsg() {
		// TODO Auto-generated method stub
		MyApplication.defaultMsgHandler.setUpdateBean((ChatViewInterface)this);
		MyApplication.defaultMsgHandler.setConversationId(conversationId);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initReceiveMsg();
		/*AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class,
				msgHandler);
		AVIMMessageManager
		.setConversationEventHandler(new MemberChangeHandler(getApplicationContext()));*/

	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MyApplication.defaultMsgHandler.setUpdateBean(null);
		/*AVIMMessageManager.unregisterMessageHandler(AVIMTypedMessage.class,
				msgHandler);
		AVIMMessageManager
		.setConversationEventHandler(new DefaultMemberHandler(
				getApplicationContext()));*/
	}

	private void loadData() {
		// 根据对话id取出相应的缓存消息
		chatmsgsCacheList = msgChatDao.getChatmsgsList(conversationId,
				user.getObjectId());
	}

	private void initView() {
		userlayout = (RelativeLayout) super
				.findViewById(R.id.userList_miliao_chat_rl);
		userlayout.setOnClickListener(this);
		emojiHigh = DensityUtil.dip2px(this, 24);
		emojiWeight = DensityUtil.dip2px(this, 24);
		emojiChatHight = DensityUtil.dip2px(this, 60);
		emojiChatWeight = DensityUtil.dip2px(this, 60);
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_miliao_chat_rl);
		backLayout.setOnClickListener(this);
		face = (ImageView) super.findViewById(R.id.chat_face_container_img);
		emojiLayout=(RelativeLayout) findViewById(R.id.chat_face_container_rl);
		emojiLayout.setOnClickListener(this);
		faceLayout = (LinearLayout) super
				.findViewById(R.id.chat_face_container);
		listviewLayout = (RelativeLayout) super
				.findViewById(R.id.listView_chatFragment_rl);
		listviewLayout.setOnClickListener(this);
		mEditText = (EditText) super.findViewById(R.id.input_chat_fragment_et);

		mEditText.setOnClickListener(this);
		/**
		 * 监听输入框内文字的变化
		 */
		mEditText.addTextChangedListener(textWatcher);

		// 表情相关
		// 表情布局
		chat_face_container = (LinearLayout) findViewById(R.id.chat_face_container);
		mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
		mViewPager.setOnPageChangeListener(new PageChange());
		// 表情下小圆点
		mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);

		// 聊天列表 相关
		mChatmsgsListView = (ListView) super
				.findViewById(R.id.listView_chatFragment);

		// mChatmsgsAdapter=new ChatmsgsListViewAdapter(this, chatmsgsList);

		mChatmsgsAdapter = new ChatmsgsListViewAdapter(this, chatmsgsCacheList,handler);

		mChatmsgsListView.setAdapter(mChatmsgsAdapter);
		mChatmsgsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (faceBoolean == true) {
					faceLayout.setVisibility(View.GONE);
					faceBoolean = false;
				}
				/**
				 * 隐藏默认输入软键盘
				 */
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(ChatGroupActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

				MessageChatBean item = chatmsgsCacheList.get(position);
				if(item.getTypeMsg() == Constants.SHOW_SEND_TYPE_IMG|| item.getTypeMsg() == Constants.SHOW_RECEIVE_TYPE_IMG){
					Intent intent=new Intent(ChatGroupActivity.this,ShowSysMsgPhotoActivity.class);
					intent.putExtra("photoUrl", ""+item.getFileUrl());
					startActivity(intent);
					overridePendingTransition(R.anim.zoom_inda, R.anim.zoom_inda);

				}

			}
		});

		mChatmsgsListView
		.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0,
					View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				MessageChatBean item = chatmsgsCacheList.get(position);
				showDialog(item);
				return false;
			}

		});

		mChatmsgsListView
		.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		mChatmsgsListView.setStackFromBottom(true);

		sendlLayout = (RelativeLayout) super
				.findViewById(R.id.send_chat_fragment_rl);
		sendlLayout.setOnClickListener(this);
		send = (ImageView) super.findViewById(R.id.send_chat_fragment_img);

		photo = (ImageView) super.findViewById(R.id.chat_photo_container_img);
		pictureLayout=(RelativeLayout) findViewById(R.id.chat_photo_container_rl);
		pictureLayout.setOnClickListener(this);
		camera = (ImageView) super.findViewById(R.id.chat_camera_container_img);
		cameraLayout=(RelativeLayout) findViewById(R.id.chat_camera_container_rl);
		cameraLayout.setOnClickListener(this);
		timeLayout=(LinearLayout) findViewById(R.id.time_remind_miliao_ll);
		title = (TextView) super.findViewById(R.id.title_fragment_chat_tv);
		userNumber = (TextView) super
				.findViewById(R.id.number_user_fragment_chat_tv);
		timeOverTextView=(TextView) findViewById(R.id.time_remind_miliao_tv);
	}

	private void showDialog(final MessageChatBean item) {
		final AlertDialog portraidlg = new AlertDialog.Builder(this).create();
		portraidlg.show();
		Window win = portraidlg.getWindow();
		win.setContentView(R.layout.item_chatmessage_selector);
		TextView copy = (TextView) win
				.findViewById(R.id.copy_item_chatmessage_tv);
		copy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				log.e("复制");
				portraidlg.dismiss();
				if (item.getTypeMsg() == Constants.SHOW_SEND_TYPE_TEXT || item.getTypeMsg() == Constants.SHOW_RECEIVE_TYPE_TEXT) {

					CopyContent(item.getMsgText());
				}
			}
		});

		TextView delete = (TextView) win
				.findViewById(R.id.delete_item_chatmessage_tv);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				log.e("删除");
				portraidlg.dismiss();
				deleteChatMessageCache(user.getObjectId(),
						"" + item.getIdCacheMsg());
			}

		});
		// 点击item的上下区域 dralog消失
		View topView = win.findViewById(R.id.top_item_chatmessage_dialog_view);
		topView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				portraidlg.dismiss();

			}
		});
		View bottomView = win
				.findViewById(R.id.bottom_item_chatmessage_dialog_view);
		bottomView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				portraidlg.dismiss();
			}
		});

	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void CopyContent(String content) {
		// TODO Auto-generated method stub
		// Copy.copy(content,this);
		log.e("lucifer", "content==" + content);
		ClipboardManager clip = (ClipboardManager) getSystemService(this.CLIPBOARD_SERVICE);

		clip.setText(content); // 复制

	}

	/**
	 * 删除本地缓存中本条信息
	 * 
	 * @param messageCacheId
	 *            消息缓存的id
	 */
	private void deleteChatMessageCache(String userID, String messageCacheId) {
		// TODO Auto-generated method stub
		msgChatDao.delete(userID, messageCacheId);
		handler.sendEmptyMessage(2);
	}

	/**
	 * 获取表情a
	 */
	private void loadEmoji() {


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_miliao_chat_rl:
			Intent dataIntent=getIntent();
			setResult(RESULT_CANCELED, dataIntent);
			finish();
			break;
		case R.id.chat_face_container_rl:
			if (faceBoolean == false) {
				/**
				 * 隐藏默认输入软键盘
				 */
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(ChatGroupActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

				faceLayout.setVisibility(View.VISIBLE);
				faceBoolean = true;

			} else {
				faceLayout.setVisibility(View.GONE);
				faceBoolean = false;
			}

			break;

		case R.id.listView_chatFragment_rl:
			faceLayout.setVisibility(View.GONE);
			faceBoolean = false;
			break;
		case R.id.input_chat_fragment_et:
			if (faceBoolean = true) {
				faceLayout.setVisibility(View.GONE);
				faceBoolean = false;
			}
			break;
			/**
			 * 点击发送按钮
			 */
		case R.id.send_chat_fragment_rl:
			// 发送消息
			sendChatmessage();
			break;
			// 图片消息
		case R.id.chat_photo_container_rl:
			sendChatPhotoMessage();
			break;
		case R.id.chat_camera_container_rl:
			sendChatCameraMessage();
			break;
		case R.id.userList_miliao_chat_rl:

			Intent intent = new Intent(this, MiLiaoInfoActivity.class);
			intent.putExtra("ConversationStyle", conversationStyle);
			intent.putExtra("ConversationId", conversationId);
			intent.putExtra("chatId", objectID);
			//	startActivity(intent);
			startActivityForResult(intent, 100);

		default:
			break;
		}
	}

	private void sendChatCameraMessage() {
		// TODO Auto-generated method stub
		Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent2.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(
						Environment.getExternalStorageDirectory(),
						"/chat_picture.png")));

		startActivityForResult(intent2, 22);

	}

	private void sendChatPhotoMessage() {
		// TODO Auto-generated method stub
		Intent intent1 = new Intent(Intent.ACTION_PICK, null);
		intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent1, 11);

	}

	private Bitmap headerPortait;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 11:
			if (resultCode == this.RESULT_OK) {

				Uri uri = data.getData();
				log.e("lucifer", uri.toString() + " ,filepath=="
						+ UriToimagePath.getImageAbsolutePath(this, uri));
				String fileName = UriToimagePath
						.getImageAbsolutePath(this, uri);
				// headerPortait=UrlLocationToBitmap.convertToBitmap(url.toString(),
				// 160, 160);
				// headerPortait=UriToBitmap.getBitmapFromUri(this, uri);
				sendChatPhoto(fileName);
			}
			break;
		case 22:
			if (resultCode == this.RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/chat_picture.png");

				String fileName = temp.toString();
				log.e("lucifer1", fileName.toString());

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				Bitmap bmp = BitmapFactory.decodeFile(fileName, options);

				saveHeadImg(bmp);
			}
			break;
		case 100:
			if(resultCode==this.RESULT_OK){
				setResult(RESULT_OK, getIntent());
				finish();
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 把要上传的图片存到本地sd卡上
	 * 
	 * @param photo
	 */
	public void saveHeadImg(Bitmap photo) {
		String uuid = UUID.randomUUID().toString();
		String picName = "/" + uuid + ".jpg";
		FileOutputStream fos = null;
		String path = "";
		try {
			fos = new FileOutputStream(new File(
					Environment.getExternalStorageDirectory() + picName));
			photo.compress(CompressFormat.PNG, 100, fos);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} finally {

			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		File temp = new File(Environment.getExternalStorageDirectory()
				+ picName);
		log.e("lucifer xin", "temp==" + temp.toString());
		// 本地图片路径
		sendChatPhoto(temp.toString());

	}

	private void sendChatPhoto(String uir) {
		// TODO Auto-generated method stub
		MessageChatBean mchatmsgs = new MessageChatBean();
		mchatmsgs.setIdCacheMsg(System.currentTimeMillis()+"");
		mchatmsgs.setIdMine(user.getObjectId());
		mchatmsgs.setIdClient(user.getObjectId());
		mchatmsgs.setSendTimeStamp( System.currentTimeMillis());
		mchatmsgs.setFileUrl(uir.toString());
		mchatmsgs.setIdConversation(conversationId);
		mchatmsgs.setTypeMsg(Constants.SHOW_SEND_TYPE_IMG);
		mchatmsgs.setDirectionMsg(Constants.IOTYPE_OUT);
		mchatmsgs.setStatusMsg(Constants.STATUES_SENDING);// 发送中

		isShowTime(mchatmsgs);
		log.e("lucifer time", "" + mchatmsgs.getIsShowTime());
		msgChatDao.insert(mchatmsgs);
		handler.sendEmptyMessage(1);
		sendPictureMessage(mchatmsgs);
	}

	/**
	 * 发送普通文本消息 然后通知线程更新UI
	 * 
	 */
	private void sendChatmessage() {
		// TODO 发送成功失败状态没有判断
		MessageChatBean mchatmsgs = new MessageChatBean();
		if (mEditText.getText().length() != 0) {
			String mcontentString = mEditText.getText().toString();
			mchatmsgs.setIdCacheMsg(System.currentTimeMillis()+"");
			mchatmsgs.setIdMine(user.getObjectId());
			mchatmsgs.setIdClient(user.getObjectId());
			mchatmsgs.setSendTimeStamp(System.currentTimeMillis());
			mchatmsgs.setMsgText(mcontentString);
			mchatmsgs.setIdConversation(conversationId);
			mchatmsgs.setTypeMsg(Constants.SHOW_SEND_TYPE_TEXT);
			mchatmsgs.setDirectionMsg(Constants.IOTYPE_OUT);
			mchatmsgs.setStatusMsg(Constants.STATUES_SENDING);// 发送中

			isShowTime(mchatmsgs);

			msgChatDao.insert(mchatmsgs);
			handler.sendEmptyMessage(1);
			sendTextMessage(mchatmsgs);
			mEditText.setText("");

		} else {
			Toast.makeText(this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 简单封装 添加时间戳 和是否显示时间
	 * 
	 * @param chatmsgs
	 */
	private void isShowTime(MessageChatBean chatmsgs) {
		chatmsgsCacheList.clear();
		chatmsgsCacheList.addAll(msgChatDao.getChatmsgsList(conversationId,
				user.getObjectId()));
		long time = (new Date()).getTime();
		chatmsgs.setSendTimeStamp(time);
		if (chatmsgsCacheList.size() == 0) {
			// 显示时间
			chatmsgs.setIsShowTime(Constants.TIMESHOW);
		} else {
			Long timeLang = chatmsgsCacheList.get(
					chatmsgsCacheList.size() - 1).getSendTimeStamp();
			if (time - timeLang >= 60000) {
				chatmsgs.setIsShowTime(Constants.TIMESHOW);
			} else {
				chatmsgs.setIsShowTime(Constants.TIMESHOWNOT);
			}
		}
		;

	}

	/*
	 * 初始表情 *
	 */
	private void InitViewPager() {
		// 获取页数
		for (int i = 0; i < getPagerCount(); i++) {
			views.add(viewPagerItem(i));
			LayoutParams params = new LayoutParams(16, 16);
			mDotsLayout.addView(dotsItem(i), params);
		}
		FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
		mViewPager.setAdapter(mVpAdapter);
		mDotsLayout.getChildAt(0).setSelected(true);
	}

	private View viewPagerItem(int position) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.face_gridview, null);// 表情布局
		GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
		/**
		 * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
		 * */
		subList = new ArrayList<ChatEmoji>();
		// TODO
		subList.addAll(chatEmojis
				.subList(position * (columns * rows - 1),
						(columns * rows - 1) * (position + 1) > chatEmojis
						.size() ? chatEmojis.size()
								: (columns * rows - 1) * (position + 1)));

		/**
		 * 末尾添加删除图标 添加了一个实体
		 * */
		// subList.add("massage_chat_btn_delete.png");

		ChatEmoji delEmoji = new ChatEmoji();
		delEmoji.setCharacter("[删除]");
		delEmoji.setFaceName("massage_chat_btn_delete");
		delEmoji.setId(StringToDrawbleId.getDrawableId(this,
				"massage_chat_btn_delete"));
		subList.add(delEmoji);
		/**
		 * 将添加过删除表情的表情列表存储到新的list里
		 */
		for (ChatEmoji emoji : subList) {
			subAllList.add(emoji);
		}

		FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, this);
		gridview.setAdapter(mGvAdapter);
		gridview.setNumColumns(columns);
		// 单击表情执行的操作
		gridview.setOnItemClickListener(this);

		return gridview;
	}

	/**
	 * 表情页改变时，dots效果也要跟着改变
	 * */
	class PageChange implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// log.e("lucifer", "arg0="+arg0+" ,arg1="+arg1+",arg2="+arg2);
		}

		@Override
		public void onPageSelected(int arg0) {
			current = arg0;
			log.e("lucifer", "position==" + arg0);
			for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
				mDotsLayout.getChildAt(i).setSelected(false);
			}
			mDotsLayout.getChildAt(arg0).setSelected(true);
		}

	}

	/**
	 * 根据表情数量以及GridView设置的行数和列数计算Pager数量
	 * 
	 * @return
	 */
	private int getPagerCount() {
		// TODO
		int count = chatEmojis.size();

		return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
				: count / (columns * rows - 1) + 1;
	}

	private ImageView dotsItem(int position) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dot_image, null);
		ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
		iv.setId(position);
		return iv;
	}

	/**
	 * 点击表情
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// 拿到第几页的第几个表情
		ChatEmoji emoji = subAllList.get(position + (current * columns * rows));
		log.e("lucifer", "current==" + current);
		// Toast.makeText(this, ""+emoji.getCharacter(),
		// Toast.LENGTH_SHORT).show();
		log.e("lucifer", "position=" + position + "  id=" + id);
		emojiKey = emoji.getCharacter();
		if (emoji.getCharacter().equals("[删除]")) {
			EditViewDelect();
		} else {
			EditViewInsert(emoji);
		}

	}

	/**
	 * 向输入框插入表情
	 */
	private void EditViewInsert(ChatEmoji emoji) {
		// // TODO Auto-generated method stub
		// mEditText.setText(mEditText.getText()+emojiKey);
		//
		// //光标设置到文本末尾
		// CharSequence text = mEditText.getText();
		// //Debug.asserts(text instanceof Spannable);
		// if (text instanceof Spannable) {
		// Spannable spanText = (Spannable)text;
		// Selection.setSelection(spanText, text.length());
		// }
		if (!TextUtils.isEmpty(emoji.getCharacter())) {
			if (mListener != null)
				mListener.onCorpusSelected(emoji);
			SpannableString spannableString = addFace(this, emoji.getId(),
					emoji.getCharacter());
			mEditText.append(spannableString);
		}

	}

	/**
	 * 删除输入框的内容，如果是表情，则删除整个表情字符串
	 */
	private void EditViewDelect() {
		// TODO Auto-generated method stub

		int selection = mEditText.getSelectionStart();
		String text = mEditText.getText().toString();
		if (selection > 0) {
			String text2 = text.substring(selection - 1);
			if ("]".equals(text2)) {
				int start = text.lastIndexOf("[");
				int end = selection;
				mEditText.getText().delete(start, end);
				return;
			}
			mEditText.getText().delete(selection - 1, selection);
		}

	}

	/**
	 * 使用handle 更新ui
	 */
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				// 刷新数据时 要先清空数据 再添加。不然 不刷新 亲测。。。
				chatmsgsCacheList.clear();
				mChatmsgsAdapter.notifyDataSetChanged();
				chatmsgsCacheList.addAll(msgChatDao.getChatmsgsList(conversationId, user.getObjectId()));
				mChatmsgsAdapter.notifyDataSetChanged();

				// ListView数据更新后，自动滚动到底部
				mChatmsgsListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				break;
			case 2:
				// 刷新数据时 要先清空数据 再添加。不然 不刷新 亲测。。。
				chatmsgsCacheList.clear();
				chatmsgsCacheList.addAll(msgChatDao.getChatmsgsList(
						conversationId, user.getObjectId()));
				mChatmsgsAdapter.notifyDataSetChanged();
				break;
			case 3:
				MessageChatBean chatMsgText = (MessageChatBean) msg.obj;
				sendTextMessage(chatMsgText);
				break;
			case 4:
				MessageChatBean chatMsgPhoto = (MessageChatBean) msg.obj;
				sendPictureMessage(chatMsgPhoto);
				break;
				//自己被踢出时
			case 5:
				Log.e("SHOW_SELF_KICK", "踢出刷新");
				// 刷新数据时 要先清空数据 再添加。不然 不刷新 亲测。。。
				chatmsgsCacheList.clear();
				chatmsgsCacheList.addAll(msgChatDao.getChatmsgsList(conversationId, user.getObjectId()));
				mChatmsgsAdapter.notifyDataSetChanged();
				// ListView数据更新后，自动滚动到底部
				mChatmsgsListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				//删除此会话相关信息
				deleteConv();
				break;
			}
		}

	};


	protected void deleteConv() {
		convUserDao.deleteConv(user.getObjectId(), conversationId);
		msgChatDao.deleteByConv(user.getObjectId(), conversationId);
		deleteObjUserConv(conversationId);
	}
	//删掉后台记录
		private void deleteObjUserConv(String convId) {
			// TODO Auto-generated method stub
			ObjChatWrap.deleteUserConv(user, convId, new ObjFunBooleanCallback() {

				@Override
				public void callback(boolean result, AVException e) {
					// TODO Auto-generated method stub
					if(result){

					}
				}
			});
		}
	/**
	 * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
	 * 
	 * @param context
	 * @param str
	 * @return
	 */
	public static SpannableString getExpressionString(Context context,
			String str) {
		SpannableString spannableString = new SpannableString(str);
		// 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
		String zhengze = "\\[[^\\]]+\\]";
		// 通过传入的正则表达式来生成一个pattern
		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString, sinaPatten, 0);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
		return spannableString;
	}

	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 * 
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws Exception
	 */
	private static void dealExpression(Context context,
			SpannableString spannableString, Pattern patten, int start)
					throws Exception {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			// 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
			if (matcher.start() < start) {
				continue;
			}
			log.e("lucifer", "" + key);
			// TODO 测试一个表情
			String value = null;
			for (ChatEmoji chatEmoji : chatEmojis) {
				if (chatEmoji.getCharacter().equals(key)) {
					value = chatEmoji.getFaceName();
					break;
				}

			}
			log.e("lucifer111", value);
			// String value="expression_1";
			if (TextUtils.isEmpty(value)) {
				continue;
			}
			int resId = context.getResources().getIdentifier(value, "drawable",
					context.getPackageName());
			// 通过上面匹配得到的字符串来生成图片资源id
			// Field field=R.drawable.class.getDeclaredField(value);
			// int resId=Integer.parseInt(field.get(null).toString());
			if (resId != 0) {
				Bitmap bitmap = BitmapFactory.decodeResource(
						context.getResources(), resId);
				bitmap = Bitmap.createScaledBitmap(bitmap, emojiChatHight,
						emojiChatWeight, true);
				// 通过图片资源id来得到bitmap，用一个ImageSpan来包装
				ImageSpan imageSpan = new ImageSpan(bitmap);
				// 计算该图片名字的长度，也就是要替换的字符串的长度
				int end = matcher.start() + key.length();
				// 将该图片替换字符串中规定的位置中
				spannableString.setSpan(imageSpan, matcher.start(), end,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				if (end < spannableString.length()) {
					// 如果整个字符串还未验证完，则继续。。
					dealExpression(context, spannableString, patten, end);
				}
				break;
			}
		}
	}

	/**
	 * 添加表情
	 * 
	 * @param context
	 * @param imgId
	 * @param spannableString
	 * @return
	 */
	public SpannableString addFace(Context context, int imgId,
			String spannableString) {
		if (TextUtils.isEmpty(spannableString)) {
			return null;
		}
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				imgId);
		bitmap = Bitmap
				.createScaledBitmap(bitmap, emojiHigh, emojiWeight, true);
		ImageSpan imageSpan = new ImageSpan(context, bitmap);
		SpannableString spannable = new SpannableString(spannableString);
		spannable.setSpan(imageSpan, 0, spannableString.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;

	}

	/**
	 * 表情的点击监听
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnCorpusSelectedListener {

		void onCorpusSelected(ChatEmoji emoji);

		void onCorpusDeleted();
	}

	/**
	 * 监听输入框内文字的变化
	 */
	public  TextWatcher textWatcher=new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable arg0) {

			if(arg0.length()>0){
				send.setImageResource(R.drawable.message_groupchat_btn_send_hl);
			}else{
				send.setImageResource(R.drawable.message_groupchat_btn_send_nor);
			}

		}
	};

	/**
	 * 用来接收消息的handle
	 * 
	 * @author lucifer
	 * 
	 */
	/*public class MessageHandler extends
	AVIMTypedMessageHandler<AVIMTypedMessage> {

		@Override
		public void onMessage(AVIMTypedMessage message,
				AVIMConversation conversation, AVIMClient client) {
			super.onMessage(message, conversation, client);
			// 请按自己需求改写
			switch (message.getMessageType()) {

			case -1:
				log.e("zcq", "接收到一条文本消息");
				//createChatMsg(conversation, message);

				break;
			case -2:
				log.e("zcq", "接收到一条图片消息");
				//createChatPicMsg(conversation, message);
				break;
			default:
				break;
			}

		}

		@Override
		public void onMessageReceipt(AVIMTypedMessage message,
				AVIMConversation conversation, AVIMClient client) {
			// TODO Auto-generated method stub
			super.onMessageReceipt(message, conversation, client);
		}

	}*/

	/**
	 * 文本 接收信息发送
	 * 
	 * @param conversation
	 * @param message
	 * @author lucifer
	 * @date 2015-11-19
	 */
	/*public void createChatMsg(AVIMConversation conversation,
			AVIMTypedMessage message) {
		// TODO Auto-generated method stub
		AVIMTextMessage msg = ((AVIMTextMessage) message);
		Chatmsgs chatBean = new Chatmsgs();
		int direction = 0;
		if(msg.getFrom().equals(user.getObjectId())){
			direction =Constants.IOTYPE_OUT;
		}else{
			direction =Constants.IOTYPE_IN;
		}
		chatBean.setUid(user.getObjectId());
		chatBean.setMessageCacheId(String.valueOf(System.currentTimeMillis()));
		chatBean.setClientId(msg.getFrom());
		chatBean.setMessageId(msg.getMessageId());
		chatBean.setConversationId(msg.getConversationId());
		chatBean.setChatMsgDirection(direction);
		chatBean.setChatMsgStatus(ChatMsgUtils.getStatus(msg.getMessageStatus()));
		boolean b = (Boolean) msg.getAttrs().get(Constants.IS_SHOW_TIME);
		chatBean.setIsShowTime(ChatMsgUtils.geRecvTimeIsShow(b));
		chatBean.setSendTimeStamp(String.valueOf(msg.getTimestamp()));
		chatBean.setDeliveredTimeStamp(String.valueOf(msg.getReceiptTimestamp()));
		chatBean.setContent(msg.getText());
		int style = (Integer) msg.getAttrs().get(Constants.CHAT_MSG_TYPE);
		// 我接受别人的消息
		if (style == Constants.SHOW_TEXT&& direction == Constants.IOTYPE_IN) {
			chatBean.setChatMsgType(Constants.SHOW_RECV_TEXT);
		} else {
			// 接收到自己发的消息
			chatBean.setChatMsgType(Constants.SHOW_SEND_TEXT);
		}
		// 接收到本人id 发送的消息 不插入到本地消息数据库
		if (!user.getObjectId().equals(msg.getFrom())) {
			chatmsgsDao.insert(chatBean);
			log.e("lucifer", "插入成功");
		}

		if (conversation.getConversationId().equals(conversationId)) {
			// 测试显示 刷新adapter

			handler.sendEmptyMessage(1);
			messagesDao.updateTime(user.getObjectId(),
					conversation.getConversationId());
		} else {
			// 未读消息加1
			messagesDao.updateUnread(user.getObjectId(),
					msg.getConversationId());
		}

	}
	 */
	/**
	 * 图片信息接收 发送
	 * 
	 * @param conversation
	 * @param message
	 * @author lucifer
	 * @date 2015-11-19
	 */
	/*public void createChatPicMsg(AVIMConversation conversation,
			AVIMTypedMessage message) {
		// TODO Auto-generated method stub
		AVIMImageMessage msg = ((AVIMImageMessage) message);
		Chatmsgs chatBean = new Chatmsgs();
		int direction = 0;
		if(msg.getFrom().equals(user.getObjectId())){
			direction =Constants.IOTYPE_OUT;
		}else{
			direction =Constants.IOTYPE_IN;
		}
		chatBean.setUid(user.getObjectId());
		chatBean.setMessageCacheId(String.valueOf(System.currentTimeMillis()));
		chatBean.setClientId(msg.getFrom());
		chatBean.setMessageId(msg.getMessageId());
		chatBean.setConversationId(msg.getConversationId());
		chatBean.setChatMsgDirection(direction);
		chatBean.setChatMsgStatus(ChatMsgUtils.getStatus(msg.getMessageStatus()));
		boolean b = (Boolean) msg.getAttrs().get(Constants.IS_SHOW_TIME);
		chatBean.setIsShowTime(ChatMsgUtils.geRecvTimeIsShow(b));
		chatBean.setSendTimeStamp(String.valueOf(msg.getTimestamp()));
		chatBean.setDeliveredTimeStamp(String.valueOf(msg.getReceiptTimestamp()));
		chatBean.setImgMsgImageUrl(msg.getAVFile().getThumbnailUrl(true, DensityUtil.dip2px(this, 160), DensityUtil.dip2px(this, 160),100,"jpg"));
		chatBean.setImgMsgImageHeight(msg.getHeight());
		chatBean.setImgMsgImageWidth(msg.getWidth());

		int style = (Integer) msg.getAttrs().get(Constants.CHAT_MSG_TYPE);
		if (style == Constants.SHOW_IMG
				&& direction == Constants.IOTYPE_IN) {
			// TODO 方便展示数据
			chatBean.setChatMsgType(Constants.SHOW_RECV_IMG);
		} else {
			chatBean.setChatMsgType(Constants.SHOW_SEND_IMG);
		}

		chatmsgsDao.insert(chatBean);
		if (conversation.getConversationId().equals(conversationId)) {
			// 测试显示
			handler.sendEmptyMessage(1);
			messagesDao.updateTime(user.getObjectId(),
					conversation.getConversationId());
		} else {
			// 未读消息加1
			messagesDao.updateUnread(user.getObjectId(),
					msg.getConversationId());
		}

	}
	 */
	/**
	 * 发送文本消息
	 * 
	 * @param mchatmsgs
	 * @author lucifer
	 * @date 2015-11-19
	 */

	public void sendTextMessage(final MessageChatBean mchatmsgs) {
		AVIMTextMessage msg = new AVIMTextMessage();
		msg.setText("" + mchatmsgs.getMsgText());

		Map<String, Object> map = new HashMap<String, Object>();

		if (mchatmsgs.getIsShowTime() == 1) {
			map.put(Constants.IS_SHOW_TIME, true);
		} else {
			map.put(Constants.IS_SHOW_TIME, false);
		}

		map.put(Constants.CHAT_MSG_TYPE, Constants.TYPE_TEXT);

		msg.setAttrs(map);

		ObjChatMessage.sendChatMsg(conversation, msg,
				new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				if (e != null) {
					log.e("zcq-------", "文本消息发送失败====="+e);
					msgChatDao.updateStatus(user.getObjectId(),mchatmsgs.getIdCacheMsg(), Constants.STATUES_FAILED);
					handler.sendEmptyMessage(1);
				} else if (result) {
					log.e("zcq", "文本消息发送成功");
					msgChatDao.updateStatus(user.getObjectId(),mchatmsgs.getIdCacheMsg(),Constants.STATUES_SENT);
					handler.sendEmptyMessage(1);
					convUserDao.updateTime(user.getObjectId(),
							conversation.getConversationId());
				} else {
					log.e("zcq", "文本消息发送失败");
					msgChatDao.updateStatus(user.getObjectId(),mchatmsgs.getIdCacheMsg(),Constants.STATUES_FAILED);
					handler.sendEmptyMessage(1);
				}

			}
		});
	}

	// 发送图片消息
	public void sendPictureMessage(final MessageChatBean mchatmsgs) {
		AVIMImageMessage msg;
		try {
			AVFile f = AVFile.withAbsoluteLocalPath("msg", mchatmsgs.getFileUrl());
			f.addMetaData("conversationId", conversationId);
			f.addMetaData("clientId", user.getObjectId());
			//type文件类型  1 代表图片
			f.addMetaData("type", "1");
			msg = new AVIMImageMessage(f);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constants.CHAT_MSG_TYPE, Constants.TYPE_IMG);
			if (mchatmsgs.getIsShowTime() == 1) {
				map.put(Constants.IS_SHOW_TIME, true);
			} else {
				map.put(Constants.IS_SHOW_TIME, false);
			}
			msg.setAttrs(map);
			ObjChatMessage.sendPicMsg(conversation, msg,
					new ObjFunBooleanCallback() {
				@Override
				public void callback(boolean result, AVException e) {
					// TODO Auto-generated method stub
					if (e != null) {
						msgChatDao.updateStatus(user.getObjectId(),mchatmsgs.getIdCacheMsg(), Constants.STATUES_FAILED);
						handler.sendEmptyMessage(1);
						return;
					}
					if (result) {
						log.e("zcq", "图片消息发送成功");
						msgChatDao.updateStatus(user.getObjectId(),mchatmsgs.getIdCacheMsg(), Constants.STATUES_SENT);
						handler.sendEmptyMessage(1);
						convUserDao.updateTime(user.getObjectId(),
								conversation.getConversationId());
					} else {
						log.e("zcq", "图片消息发送失败");
						msgChatDao.updateStatus(user.getObjectId(),mchatmsgs.getIdCacheMsg(), Constants.STATUES_FAILED);
						handler.sendEmptyMessage(1);
					}
				}
			});
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 群成员变动消息 接收 handle  
	 * @author lucifer
	 *
	 */
	/*public class MemberChangeHandler extends AVIMConversationEventHandler {

		public MemberChangeHandler(Context context) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onInvited(AVIMClient client, AVIMConversation conversation,
				String str) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onKicked(AVIMClient client, AVIMConversation conversation,
				String str) {
			// TODO Auto-generated method stub
			log.e("zcq", "进入被踢出回调");
			//handleMemberRemove(client, conversation,str);
		}

		@Override
		public void onMemberJoined(AVIMClient client,
				AVIMConversation conversation, List<String> array, String str) {
			// 参与者 ，邀请人
			// 在当前聊天--》1.活动群，判断参与者是否参加活动，不参加不提示。2.普通群：提示
			// 不在当前聊天--》1.参加，插入数据库。2.插入数据库
			log.e("zcq", "接收到新人加入消息");
			//handleMemberAdd(client, conversation, array, str);
		}

		@Override
		public void onMemberLeft(AVIMClient client, AVIMConversation conversation,
				List<String> array, String str) {
			log.e("zcq", "接收到其他成员被踢出消息");
		}

	}*/

	// 成员加入消息处理
	/*public void handleMemberAdd(final AVIMClient client,
			final AVIMConversation conversation, List<String> array, String str) {
		for (String userId : array) {
			Chatmsgs chatBean = new Chatmsgs();
			chatBean.setChatMsgType(Constants.SHOW_MEMBERCHANGE);
			chatBean.setClientId(client.getClientId());
			chatBean.setNowJoinUserId(client.getClientId());
			chatBean.setUid(user.getObjectId());
			chatBean.setNowJoinUserId(userId);
			chatBean.setMessageCacheId(String.valueOf(System
					.currentTimeMillis()));
			chatBean.setConversationId(conversation.getConversationId());
			chatBean.setSendTimeStamp(String.valueOf(System
					.currentTimeMillis()));
			chatmsgsDao.insert(chatBean);
			log.e("zcq", "插入a 数据库成功");
			messagesDao.updateTime(user.getObjectId(),
					conversation.getConversationId());
			//插入成员
			UserAboutBean aboutBean = new UserAboutBean();
			aboutBean.setUserId(user.getObjectId());
			aboutBean.setAboutType(Constants.CONVERSATION_TYPE);
			aboutBean.setAboutUserId(client.getClientId());
			aboutBean.setAboutColetctionId(conversation.getConversationId());
			userAboutDao.saveUserAboutBean(aboutBean);
			if (conversation.getConversationId().equals(conversationId)) {
				handler.sendEmptyMessage(1);
			} else {
				// 未读消息加1,保存未读
				messagesDao.updateUnread(user.getObjectId(),
						conversation.getConversationId());
			}
		}
	}*/


	// 被踢出
	/*public void handleMemberRemove(AVIMClient client,
			AVIMConversation conversation, String str) {
		//删除成员
		userAboutDao.deleteUserTypeUserId(user.getObjectId(), Constants.CONVERSATION_TYPE, conversation.getConversationId(), client.getClientId());
		if (conversation.getConversationId().equals(conversationId)) {
			log.e("zcq", "进入被踢出回调 在当前回话");
			// 删除消息缓存
			chatmsgsDao.deleteConversationId(user.getObjectId(),
					conversation.getConversationId());

			Chatmsgs chatmsgs=new Chatmsgs();				
			chatmsgs.setContent("您已被踢出觅聊");
			chatmsgs.setNowJoinUserId(client.getClientId());
			chatmsgs.setClientId(client.getClientId());
			chatmsgs.setSendTimeStamp(""+System.currentTimeMillis());
			chatmsgs.setChatMsgType(Constants.SHOW_SELF_DEL);		
			chatmsgs.setConversationId(conversation.getConversationId());
			chatmsgs.setUid(user.getObjectId());
			chatmsgs.setMessageCacheId(String.valueOf(System
					.currentTimeMillis()));
			chatmsgsDao.insert(chatmsgs);
			messagesDao.updateTime(user.getObjectId(),
					conversation.getConversationId());
			handler.sendEmptyMessage(1);

		} else {
			// 未读消息加1,保存未读
			messagesDao.updateUnread(user.getObjectId(),
					conversation.getConversationId());

			Chatmsgs chatBean = new Chatmsgs();
			chatBean.setChatMsgType(Constants.SHOW_SELF_DEL);//
			chatBean.setNowJoinUserId(client.getClientId());
			chatBean.setClientId(client.getClientId());
			chatBean.setUid(user.getObjectId());
			chatBean.setMessageCacheId(String.valueOf(System
					.currentTimeMillis()));
			chatBean.setConversationId(conversation.getConversationId());
			chatBean.setContent("您被踢出群聊");
			chatmsgsDao.insert(chatBean);
		}
	}
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent=getIntent();
		setResult(RESULT_CANCELED, intent);
		finish();
	}

	@Override
	public void updateView(MessageChatBean bean) {
		Log.e("chatgroup", "receive");
		if(bean.getTypeMsg() == Constants.SHOW_SELF_KICK){
			Log.e("SHOW_SELF_KICK", "踢出刷新2");
			handler.sendEmptyMessage(5);
		}else{
			handler.sendEmptyMessage(1);
		}
	}


}
