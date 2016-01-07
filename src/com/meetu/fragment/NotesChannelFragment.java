package com.meetu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.meetu.activity.messages.InputDialog;
import com.meetu.activity.messages.InputDialog.SendMessageCallback;
import com.meetu.activity.messages.NotesActivity;
import com.meetu.activity.miliao.FaceGVAdapter;
import com.meetu.activity.miliao.FaceVPAdapter;
import com.meetu.activity.miliao.ChatGroupActivity.OnCorpusSelectedListener;
import com.meetu.bean.UserBean;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjScripCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjScrip;
import com.meetu.cloud.object.ObjScripBox;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.utils.ChatMsgUtils;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjScriptWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.common.EmojisRelevantUtils;
import com.meetu.entity.ChatEmoji;
import com.meetu.entity.Chatmsgs;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.ChatmsgsDao;
import com.meetu.sqlite.EmojisDao;
import com.meetu.sqlite.UserDao;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;
import com.meetu.tools.StringToDrawbleId;
import com.tencent.open.cgireport.reportItem;

import android.R.integer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class NotesChannelFragment extends Fragment implements OnClickListener,
		OnItemClickListener {
	// 控件相关
	private RelativeLayout topLayout, bottomLayout, allLayout;
	private TextView contentTextView;

	private View view;
	private int windowWidth;
	// 表情键盘相关
	private RelativeLayout sendLayout, editviewLayout;
	private LinearLayout sendLinearLayout;
	int mVisibleHeight;

	public EditText mEditText;
	private RelativeLayout emojiLayout;
	private LinearLayout faceLayout;
	private ImageView emojiAll;
	private ScrollView mScrollView;
	private boolean mIsKeyboardShow = false;// 记录系统软键盘是否弹出
	private boolean isShowEmoji = false;// 记录表情键盘是否弹出
	private boolean isShowBottom = true;// 记录公共底部是否显示

	private boolean isWindow = true;// 记录屏幕点击的时候的状态 true 表示会弹出键盘 false 落下键盘
	private int windowFocusX, windowFocusY;
	private int noteHight;// 纸条的绝对高度
	private int emojiHight;// 表情键盘的高度
	private int ruanHight;// 软键盘的高度
	private int noteWight;// 纸条的最大宽度
	// 添加表情相关
	private EmojisDao emojisDao;
	private static List<ChatEmoji> chatEmojis;
	private ImageView image_face;// 表情图标
	// 7列3行
	private int columns = 7;// 表情的列数
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

	/**
	 * 发送小字条相关
	 */
	private List<String> notesMessageViewList;// 存储每个小字条消息
	private TextView content;
	private ImageView photoHead;
	private RelativeLayout contentLayout;
	private int layoutW = 0, layoutH = 0;// 小纸条的输入框的宽高
	private int viewX, viewY;

	// 网络数据相关
	ObjScripBox objScripBox = null;
	private FinalBitmap finalBitmap;
	private AVUser currentUser = AVUser.getCurrentUser();
	ObjScrip objScrip = null;
	AVIMConversation conv = null;
	private int numberId = 10000000;// 点击屏幕生成的view的id
	// 当前用户
	private ObjUser user = new ObjUser();
	private ChatmsgsDao chatmsgsDao;
	private UserDao userDao;

	private List<Chatmsgs> chatmsgsList = new ArrayList<Chatmsgs>();// 用来存放纸条消息
	private List<Chatmsgs> chatmsgsListNewTenNote = new ArrayList<Chatmsgs>();

	// public MessageHandler msgHandler;

	private int noteIdNow = 1;// 标记当前生成的view是第几个view 小纸条

	private List<Integer> noteIDList = new ArrayList<Integer>();

	private ImageView sendImageView;
	
	private Bitmap loadBitmap=null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_notes_channel, null);
			// 从本地数据库 获取表情数据
			emojisDao = new EmojisDao(getActivity());
			chatEmojis = emojisDao.getChatEmojisList();
			emojiHigh = DensityUtil.dip2px(getActivity(), 24);
			emojiWeight = DensityUtil.dip2px(getActivity(), 24);
			// msgHandler=new MessageHandler();
			chatmsgsDao = new ChatmsgsDao(getActivity());
			noteHight = DisplayUtils.getWindowHeight(getActivity())
					- DensityUtil.dip2px(getActivity(), 190);
			noteWight = DisplayUtils.getWindowWidth(getActivity());
			userDao = new UserDao(getActivity());
			loadBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.mine_likelist_profile_default);
			emojiHight = DensityUtil.dip2px(getActivity(), 275);
			ruanHight = DensityUtil.dip2px(getActivity(), 350);
			if (currentUser != null) {
				user = AVUser.cast(currentUser, ObjUser.class);
			}
			objScripBox = (ObjScripBox) getArguments().getSerializable(
					"ObjScripBox");
			objScrip = (ObjScrip) getArguments().getSerializable("ObjScrip");

			log.e("zcq duihua id", objScripBox.getConversationId());
			conv = MyApplication.chatClient.getConversation(objScripBox
					.getConversationId());

			MyApplication app = (MyApplication) getActivity()
					.getApplicationContext();
			finalBitmap = app.getFinalBitmap();

			getHight();

			intitView();
			// 加载表情
			InitViewPager();

			// chatmsgsDao.deleteAll();
			showScriptMsg();

		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	/**
	 * 取本地数据库中的小纸条并显示
	 * 
	 * @author lucifer
	 * @date 2015-12-2
	 */
	private void showScriptMsg() {
		// chatmsgsList=chatmsgsDao.getScriptChatmsgsList(objScripBox.getConversationId(),
		// user.getObjectId(), objScrip.getObjectId());
		if (chatmsgsDao.getScriptChatmsgsList(objScripBox.getConversationId(),
				user.getObjectId(), objScrip.getObjectId()) != null) {
			log.e("zcq 缓存消息", "不等于null");
			chatmsgsList.clear();
			chatmsgsList.addAll(chatmsgsDao.getScriptChatmsgsList(
					objScripBox.getConversationId(), user.getObjectId(),
					objScrip.getObjectId()));
		}

		log.e("zcq 缓存消息数量", "" + chatmsgsList.size());
		chatmsgsListNewTenNote.clear();
		if (chatmsgsList.size() > 10) {
			for (int i = (chatmsgsList.size() - 10); i < chatmsgsList.size(); i++) {
				chatmsgsListNewTenNote.add(chatmsgsList.get(i));
			}

		} else {
			chatmsgsListNewTenNote.addAll(chatmsgsList);
		}
		log.e("zcq chatmsgsListNewTenNote", "" + chatmsgsListNewTenNote.size());
		removeAllView();

		// for(int i=0;i<chatmsgsListNewTenNote.size();i++){
		// removeView((i+1));
		//
		// }
		for (int i = 0; i < chatmsgsListNewTenNote.size(); i++) {
			log.e("zcq ", "chatmsgsListNewTenNote.size()===" + i);

			allLayout.addView(getMessageLayout(chatmsgsListNewTenNote.get(i),
					i + 1));

		}
	}

	/**
	 * 获得弹出键盘的高度
	 */
	private void getHight() {
		// TODO Auto-generated method stub
		view.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						getKeyboardHeight();
					}

					private void getKeyboardHeight() {
						// TODO Auto-generated method stub
						Rect r = new Rect();
						view.getWindowVisibleDisplayFrame(r);

						int visibleHeight = r.height();

						if (mVisibleHeight == 0) {
							mVisibleHeight = visibleHeight;
							return;
						}

						if (mVisibleHeight == visibleHeight) {
							return;
						}
						mVisibleHeight = visibleHeight;

						// Magic is here
						// if (/* compare the visiable height to others */) {
						// mIsKeyboardShow = true;
						// } else {
						// mIsKeyboardShow = false;
						// }
					}
				});

	}

	private void intitView() {
		// TODO Auto-generated method stub
		mEditText = (EditText) view.findViewById(R.id.input_notes_fragment_et);
		// 输入框文字的变化监听
		mEditText.addTextChangedListener(mTextWatcher);

		// 动态设置高度
		topLayout = (RelativeLayout) view
				.findViewById(R.id.top_fragment_notes_channel_rl);
		bottomLayout = (RelativeLayout) view
				.findViewById(R.id.bottom_fragment_notes_channel_rl);
		windowWidth = DisplayUtils.getWindowWidth(getActivity());

		int topH = (int) (windowWidth / 1.2);
		int bottomH = (int) (windowWidth / 2.23);
		RelativeLayout.LayoutParams topparams = (LayoutParams) topLayout
				.getLayoutParams();
		topparams.height = topH;
		topLayout.setLayoutParams(topparams);
		RelativeLayout.LayoutParams bottomparams = (LayoutParams) bottomLayout
				.getLayoutParams();
		bottomparams.height = bottomH;
		bottomLayout.setLayoutParams(bottomparams);
		log.e("lucifer", "topH==" + topH + " bottomH==" + bottomH);

		mScrollView = (ScrollView) view
				.findViewById(R.id.scrollview_notes_channel);
		FrameLayout.LayoutParams scrollParams = (android.widget.FrameLayout.LayoutParams) mScrollView
				.getLayoutParams();
		scrollParams.height = topH + bottomH;
		mScrollView.setLayoutParams(scrollParams);

		allLayout = (RelativeLayout) view
				.findViewById(R.id.all_fragment_notes_channes_rl);
		allLayout.setOnClickListener(this);

		// 获取点击屏幕的坐标
		allLayout.setOnTouchListener(new TouchListenerImp());
		sendLayout = (RelativeLayout) view.findViewById(R.id.send_notes_rl);
		sendLayout.setOnClickListener(this);
		editviewLayout = (RelativeLayout) view
				.findViewById(R.id.bottom_emoji_send_rl);
		sendLinearLayout = (LinearLayout) view
				.findViewById(R.id.bottom_notes_send_ll);
		emojiLayout = (RelativeLayout) view
				.findViewById(R.id.emoji_notes_fragment_rl);
		emojiLayout.setOnClickListener(this);
		faceLayout = (LinearLayout) view.findViewById(R.id.chat_face_container);
		emojiAll = (ImageView) view.findViewById(R.id.emoji_notes_fragment_img);

		// 表情相关
		// 表情布局
		chat_face_container = (LinearLayout) view
				.findViewById(R.id.chat_face_container);
		mViewPager = (ViewPager) view.findViewById(R.id.face_viewpager);
		mViewPager.setOnPageChangeListener(new PageChange());
		// 表情下小圆点
		mDotsLayout = (LinearLayout) view
				.findViewById(R.id.face_dots_container);

		contentTextView = (TextView) view
				.findViewById(R.id.content_fragment_notechannel_tv);

		if (objScrip.getContentImage() != null) {
			finalBitmap.display(topLayout, objScrip.getContentImage().getUrl());
		}

		contentTextView.setText("" + objScrip.getContentText());
		sendImageView = (ImageView) view.findViewById(R.id.send_notes_img);

	}

	/**
	 * 获取当前 屏幕点击的位置
	 * 
	 * @author Administrator
	 * 
	 */
	private class TouchListenerImp implements OnTouchListener {

		public boolean onTouch(View v, MotionEvent event) {
			// MainActivity.this.info.setText("x="+event.getX()+"y="+event.getY());
			// log.e("lucifer", "x=="+event.getX() +" y=="+event.getY());
			windowFocusX = (int) event.getX();
			windowFocusY = (int) event.getY();
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.all_fragment_notes_channes_rl:
			// TODO
			log.e("lucifer", "触发页面");

			getHight();

			Boolean isShowView = NotesActivity.isShow;
			log.e("zcq", "isShowView" + isShowView);

			if (isShowView == true) {
				// 第一次点击屏幕
				if (isWindow == true) {
					sendLinearLayout.setVisibility(View.VISIBLE);
					if (isShowEmoji == true) {
						NotesActivity.visible();

						emojiAll.setImageResource(R.drawable.message_groupchat_btn_emoticon_hl);
						faceLayout.setVisibility(View.GONE);
						isShowEmoji = false;

					}
					mEditText.requestFocus();
					InputMethodManager inputManager = (InputMethodManager) mEditText
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					inputManager.showSoftInput(mEditText, -20);
					isWindow = false;
					mIsKeyboardShow = true;

					if ((noteHight - windowFocusY)
							+ DensityUtil.dip2px(getActivity(), 77) >= ruanHight) {

					} else {
						float moveHight = (noteHight - windowFocusY)
								+ DensityUtil.dip2px(getActivity(), 77)
								- ruanHight;
						log.e("zcq", "moveHight==" + moveHight);
						// allLayout.setY(moveHight);
						mScrollView.setY(moveHight);
					}

					NotesActivity.dismiss();
					// notesMessageViewList.add(getsendMessageLayout());

					allLayout.addView(getsendMessageLayout(windowFocusX,
							windowFocusY));

				} else {
					sendLinearLayout.setVisibility(View.GONE);
					faceLayout.setVisibility(View.GONE);
					isShowEmoji = false;
					InputMethodManager inputManager = (InputMethodManager) mEditText
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(view.getWindowToken(),
							0);
					isWindow = true;
					mIsKeyboardShow = false;

					allLayout.setY(0);
					mScrollView.setY(0);
					NotesActivity.visible();
					// allLayout.removeViewAt(1);
					// dismissAll();
					removeView(numberId);

				}
			}

			break;

		case R.id.emoji_notes_fragment_rl:

			// biaoqing
			if (isShowEmoji == false) {
				NotesActivity.dismiss();
				faceLayout.setVisibility(View.VISIBLE);
				emojiAll.setImageResource(R.drawable.massage_letters_show_reply_img_keybroad);

				isShowEmoji = true;
				// 落下软键盘
				InputMethodManager inputManager = (InputMethodManager) mEditText
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
				mIsKeyboardShow = false;

				if ((noteHight - windowFocusY)
						+ DensityUtil.dip2px(getActivity(), 77) < emojiHight) {
					float moveHight = (noteHight - windowFocusY)
							+ DensityUtil.dip2px(getActivity(), 77)
							- emojiHight;
					log.e("zcq", "moveHightemoji==" + moveHight);
					// allLayout.setY(moveHight);
					mScrollView.setY(moveHight);
				}

				// allLayout.addView(getsendMessageLayout());

				// allLayout.addView(getsendMessageLayout());

			} else {
				// 第二次点击表情

				NotesActivity.visible();
				emojiAll.setImageResource(R.drawable.message_groupchat_btn_emoticon_hl);
				faceLayout.setVisibility(View.GONE);
				isShowEmoji = false;
				// sendLinearLayout.setVisibility(View.GONE);
				// allLayout.setY(0);
				// mScrollView.setY(0);

				// allLayout.removeViewAt(100);
				mEditText.requestFocus();
				InputMethodManager inputManager = (InputMethodManager) mEditText
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(mEditText, -20);
				isWindow = false;
				mIsKeyboardShow = true;

				if ((noteHight - windowFocusY)
						+ DensityUtil.dip2px(getActivity(), 77) >= ruanHight) {

				} else {
					float moveHight = (noteHight - windowFocusY)
							+ DensityUtil.dip2px(getActivity(), 77) - ruanHight;
					log.e("zcq", "moveHight==" + moveHight);
					// allLayout.setY(moveHight);
					mScrollView.setY(moveHight);
				}

				NotesActivity.dismiss();

			}

			break;

		case R.id.send_notes_rl:
			// 发送纸条消息
			if (mEditText.getText().length() != 0) {
				int x = (viewX * 10000 / noteWight);
				int y = (viewY * 10000 / noteHight);
				log.e("zcq fasong", "x===" + x + " y===" + y);
				Chatmsgs chatmsgs = new Chatmsgs();
				chatmsgs.setConversationId(objScripBox.getConversationId());
				chatmsgs.setUid(user.getObjectId());
				chatmsgs.setSendTimeStamp("" + System.currentTimeMillis());
				chatmsgs.setClientId(user.getObjectId());
				chatmsgs.setContent("" + mEditText.getText());
				chatmsgs.setScriptId(objScrip.getObjectId());
				chatmsgs.setScripX(x);
				chatmsgs.setScripY(y);

				sendMsg(chatmsgs);

				chatmsgsDao.insert(chatmsgs);

				mEditText.setText("");
				sendLinearLayout.setVisibility(View.GONE);
				faceLayout.setVisibility(View.GONE);
				isShowEmoji = false;
				InputMethodManager inputManager = (InputMethodManager) mEditText
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
				isWindow = true;
				mIsKeyboardShow = false;

				allLayout.setY(0);
				mScrollView.setY(0);
				NotesActivity.visible();
				// allLayout.removeViewAt(1);
				// dismissAll();
				removeView(numberId);
				handler.sendEmptyMessage(1);
			}

			break;
		default:
			break;
		}

	}

	public static void dismissAll() {
		// sendLinearLayout.setVisibility(View.GONE);
		// faceLayout.setVisibility(View.GONE);
		// isShowEmoji=false;
		// InputMethodManager inputManager =
		// (InputMethodManager)mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		// inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		// isWindow=true;
		// mIsKeyboardShow=false;
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
		log.e("zcq", "count==" + count);
		return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
				: count / (columns * rows - 1) + 1;
	}

	private ImageView dotsItem(int position) {
		// TODO
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dot_image, null);
		ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
		iv.setId(position);
		return iv;
	}

	/*
	 * 初始表情 *
	 */
	private void InitViewPager() {
		log.e("zcq", "getPagerCount()==" + getPagerCount());
		// 获取页数 TODO
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
		// TODO
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		delEmoji.setId(StringToDrawbleId.getDrawableId(getActivity(),
				"massage_chat_btn_delete"));
		subList.add(delEmoji);
		/**
		 * 将添加过删除表情的表情列表存储到新的list里
		 */
		for (ChatEmoji emoji : subList) {
			subAllList.add(emoji);
		}

		FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, getActivity());
		gridview.setAdapter(mGvAdapter);
		gridview.setNumColumns(columns);
		// 单击表情执行的操作
		gridview.setOnItemClickListener(this);

		return gridview;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

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
			// TODO 暂时不显示表情
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
			SpannableString spannableString = addFace(getActivity(),
					emoji.getId(), emoji.getCharacter());
			mEditText.append(spannableString);
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
	 * 获取添加的视图 输入框
	 */
	private View getsendMessageLayout(int windowFocusX, int windowFocusY) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater
				.inflate(R.layout.item_note_channels_messages, null);
		contentLayout = (RelativeLayout) view
				.findViewById(R.id.input_note_channels_rl);
		content = (TextView) view.findViewById(R.id.content_note_channels_tv);

		content.setText(mEditText.getText().toString());
		photoHead = (ImageView) view
				.findViewById(R.id.photoHead_notes_channel_fragment_img);

		// 是我发的
		if (user.getProfileClip() != null) {
			finalBitmap.display(photoHead, user.getProfileClip().getThumbnailUrl(true, DensityUtil.dip2px(getActivity(), 40), DensityUtil.dip2px(getActivity(), 40)),loadBitmap);
		}

		viewX = windowFocusX - (DensityUtil.dip2px(getActivity(), 34) + 1);
		viewY = windowFocusY - (DensityUtil.dip2px(getActivity(), 54));
		// 在屏幕的最左边
		if (windowFocusX < ((DensityUtil.dip2px(getActivity(), 34) + 1))) {
			// x=((DensityUtil.dip2px(getActivity(), 34)+1));
			viewX = windowFocusX;
		}
		// 在你屏幕的最右边
		if ((windowWidth - windowFocusX) < ((DensityUtil.dip2px(getActivity(),
				34) + 1))) {
			viewX = windowFocusX - ((DensityUtil.dip2px(getActivity(), 69)));
		}
		// 在屏幕的最上边
		if (windowFocusY < (DensityUtil.dip2px(getActivity(), 54))) {
			// y=(DensityUtil.dip2px(getActivity(), 54));
			viewY = windowFocusY;
		}

		log.e("lucifer" + "x==" + viewX + "y==" + viewY);

		view.setX(viewX);
		view.setY(viewY);

		view.setId(numberId);

		// view.setTag(notesMessageViewList.size());
		view.setLayoutParams(params);
		return view;
	}

	/**
	 * 当输入框变化的时候的处理
	 */
	TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

			String contentString = mEditText.getText().toString();
			log.e("lucifer", "contentString" + contentString + " arg0==" + arg0);

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable arg0) {

			if (arg0.length() > 0) {
				sendImageView
						.setImageResource(R.drawable.message_groupchat_btn_send_hl);
			} else {
				sendImageView
						.setImageResource(R.drawable.message_groupchat_btn_send_nor);
			}

			if (sendLinearLayout.getVisibility() == View.VISIBLE) {
				SpannableString spannableString = EmojisRelevantUtils
						.getExpressionString(getActivity(), arg0.toString(),
								chatEmojis);

				content.setText("" + spannableString);

				
			}

			/*log.e("lucifer", "content.getWidth()===" + content.getWidth()
					+ "content.gethight()===" + content.getHeight());*/

			// 在你屏幕的最右边
			if ((windowWidth - windowFocusX) < ((DensityUtil.dip2px(
					getActivity(), 34) + 1))) {
				// 随着字体 textview 中的 字体宽度 让view内容不要出右屏幕
				if ((content.getWidth() + DensityUtil.dip2px(getActivity(), 4)) >= DensityUtil
						.dip2px(getActivity(), 69)) {
					viewX = windowWidth- (content.getWidth() + DensityUtil.dip2px(getActivity(), 30));
				}
			}
			// 随着字体 textview 中的 字体宽度 让view内容不要出下屏幕
//			log.e("lucifer","字条距离下表的距离"+ (DensityUtil.dip2px(getActivity(), 54)+ noteHight - windowFocusY)+ "文本view的高度"+ (content.getHeight() + DensityUtil.dip2px(getActivity(), 44 + 33)));
//			log.e("lucifer", "content.getHeight()===" + content.getHeight()+" content.getWidth()=="+content.getWidth());
			if ((content.getHeight() + DensityUtil.dip2px(getActivity(),
					44 + 33)) >= (DensityUtil.dip2px(getActivity(), 54)
					+ noteHight - windowFocusY)) {
				viewY = DensityUtil.dip2px(getActivity(), 54)- content.getHeight()+ DensityUtil.dip2px(getActivity(), 44);
			}

			View myView = view.findViewById(numberId);
			log.e("viewx===", "viewX=="+viewX+"  viewy=="+viewY);
			myView.setX(viewX);
			myView.setY(viewY);
			// log.e("lucifer", "xxx==="+view.getX()+"yyy==="+view.getY());
		}
	};

	//
	// private void getTextWH( String mString){
	// Paint pFont = new Paint();
	// Rect rect = new Rect();
	// pFont.getTextBounds(mString, 0, 1, rect);
	// Log.e("lucifer", "height:"+rect.height()+"width:"+rect.width());
	// }

	/**
	 * 删除添加的view id 是view 的id
	 */
	private void removeView(int id) {
		View hiddenView = allLayout.findViewById(id); // 在hidden_view.xml中hidden_layout是root
														// layout
		if (null != hiddenView) {

			ViewGroup parent = (ViewGroup) hiddenView.getParent();
			parent.removeView(hiddenView);
		}
	}

	/**
	 * fragment 中 隐藏 输入框布局
	 */
	public void isShowEditLayout() {

		sendLinearLayout.setVisibility(View.GONE);

		removeView(numberId);
		mScrollView.setY(0);
		allLayout.setY(0);
		mIsKeyboardShow = false;// 记录系统软键盘是否弹出
		isShowEmoji = false;// 记录表情键盘是否弹出
		isShowBottom = true;// 记录公共底部是否显示

		isWindow = true;// 记录屏幕点击的时候的状态 true 表示会弹出键盘 false 落下键盘
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case 1:
				log.e("zcq", "刷新了");
				showScriptMsg();
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 
	 * 
	 * @author lucifer
	 * @date 2015-12-1
	 */
	public void sendMsg(Chatmsgs chatmsgs) {

		log.e("zcq发送时候的位置" + "x==" + chatmsgs.getScripX() + " y=="
				+ chatmsgs.getScripY());
		AVIMTextMessage msg = new AVIMTextMessage();
		msg.setText("" + chatmsgs.getContent());
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(Constants.SCRIP_ID, objScrip.getObjectId());
		map.put(Constants.CHAT_MSG_TYPE, Constants.SHOW_SCRIPT_MSG);
		map.put(Constants.SCRIP_X, chatmsgs.getScripX());
		map.put(Constants.SCRIP_Y, chatmsgs.getScripY());

		msg.setAttrs(map);
		ObjChatMessage.sendChatMsg(conv, msg, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					log.e("zcq", e);
				//	Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT)
				//			.show();
					return;
				}
				if (result) {
					
					log.e("zcq", "发送成功");
//					Toast.makeText(getActivity(), "已发送", Toast.LENGTH_SHORT)
//							.show();
				} else {
					
					log.e("zcq", "发送失败");
//					Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT)
//							.show();
				}
			}
		});
	}

	/**
	 * 获取添加的视图 输入框 展示接收到的小纸条消息
	 */
	private View getMessageLayout(final Chatmsgs chatmsgs, int noteID) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater
				.inflate(R.layout.item_note_channels_messages, null);
		RelativeLayout contentLayout = (RelativeLayout) view
				.findViewById(R.id.input_note_channels_rl);
		TextView contentTextView = (TextView) view
				.findViewById(R.id.content_note_channels_tv);

		SpannableString spannableString = EmojisRelevantUtils
				.getExpressionString(getActivity(), chatmsgs.getContent(),
						chatEmojis);

		contentTextView.setText(spannableString);

		final ImageView photoHead = (ImageView) view
				.findViewById(R.id.photoHead_notes_channel_fragment_img);

		if (user.getObjectId().equals(chatmsgs.getClientId())) {
			// 是我发的
			if (user.getProfileClip() != null) {
				finalBitmap.display(photoHead, user.getProfileClip().getThumbnailUrl(true, DensityUtil.dip2px(getActivity(), 40), DensityUtil.dip2px(getActivity(), 40)),loadBitmap);
			}

		} else {
			// TODO 设置他人头像 需要封装个方法。多处使用
			if (chatmsgs.getClientId() != null
					&& !("").equals(chatmsgs.getClientId())) {
				ArrayList<UserBean> list = userDao.queryUser(chatmsgs
						.getClientId());
				if (null != list && list.size() > 0) {
					// barrage.setNickName(list.get(0).getNameNick());
					// barrage.setUserAvator(list.get(0).getProfileClip());
					if (!list.get(0).getProfileClip().equals("")) {
						finalBitmap.display(photoHead, list.get(0)
								.getProfileClip(),loadBitmap);
					}

				} else {
					
					ObjUserWrap.getObjUser(chatmsgs.getClientId(),
							new ObjUserInfoCallback() {

								@Override
								public void callback(ObjUser objuser,
										AVException e) {
									// TODO Auto-generated method stub
									if (e == null) {
										userDao.insertOrReplaceUser(objuser);
										ArrayList<UserBean> list2 = userDao
												.queryUser(chatmsgs
														.getClientId());
										if (null != list2 && list2.size() > 0) {
											// barrage.setNickName(list.get(0).getNameNick());
											// barrage.setUserAvator(list.get(0).getProfileClip());
											if (!list2.get(0).getProfileClip()
													.equals("")) {
												finalBitmap
														.display(
																photoHead,
																list2.get(0)
																		.getProfileClip(),loadBitmap);
											}

										}
									}
								}
							});
				}
			}
		}

		// viewX=windowFocusX-(DensityUtil.dip2px(getActivity(), 34)+1);
		// viewY=windowFocusY-(DensityUtil.dip2px(getActivity(), 54));
		// //在屏幕的最左边
		// if(windowFocusX<((DensityUtil.dip2px(getActivity(), 34)+1))){
		// //x=((DensityUtil.dip2px(getActivity(), 34)+1));
		// viewX=windowFocusX;
		// }
		// //在你屏幕的最右边
		// if((windowWidth-windowFocusX)<((DensityUtil.dip2px(getActivity(),
		// 34)+1))){
		// viewX=windowFocusX-((DensityUtil.dip2px(getActivity(), 69)));
		// }
		// //在屏幕的最上边
		// if(windowFocusY<(DensityUtil.dip2px(getActivity(), 54))){
		// //y=(DensityUtil.dip2px(getActivity(), 54));
		// viewY=windowFocusY;
		// }
		//
		// log.e("lucifer"+"x=="+viewX+"y=="+viewY);
		int x = (chatmsgs.getScripX() * noteWight / 10000);
		int y = (chatmsgs.getScripY() * noteHight / 10000);

		// log.e("zcq x y",
		// "x=="+x+" y=="+y+"  id=="+chatmsgs.getMessageCacheId());
		view.setX(x);
		view.setY(y);
		if (noteIdNow > 10) {
			// 删除第一条
		}

		view.setId(noteID);

		// view.setTag(notesMessageViewList.size());
		view.setLayoutParams(params);
		return view;
	}


	/**
	 * 移除所有的view
	 * 
	 * @author lucifer
	 * @date 2015-12-2
	 */
	public void removeAllView() {
		for (int i = 0; i < chatmsgsListNewTenNote.size(); i++) {
			removeView((i + 1));

		}
	}

	/**
	 * 
	 * 
	 * @author lucifer
	 * @date 2015-12-2
	 */
	public void showAllView() {
		handler.sendEmptyMessage(1);
	}

	/**
	 * 点击小飞机随机在屏幕上随机生成一个小纸条
	 * 
	 * @author lucifer
	 * @date 2015-12-12
	 */
	public void randowShowSendScript(int randowX, int randowY) {
		Boolean isShowView = NotesActivity.isShow;
		log.e("zcq", "isShowView" + isShowView);

		if (isShowView == true) {
			// 第一次点击屏幕
			if (isWindow == true) {
				sendLinearLayout.setVisibility(View.VISIBLE);
				if (isShowEmoji == true) {
					NotesActivity.visible();
					emojiAll.setImageResource(R.drawable.emotionsemojihl);
					faceLayout.setVisibility(View.GONE);
					isShowEmoji = false;

				}
				mEditText.requestFocus();
				InputMethodManager inputManager = (InputMethodManager) mEditText
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(mEditText, -20);
				isWindow = false;
				mIsKeyboardShow = true;
				// allLayout.setY(-100);

				if ((noteHight - randowY)
						+ DensityUtil.dip2px(getActivity(), 77) >= ruanHight) {

				} else {
					float moveHight = (noteHight - randowY)
							+ DensityUtil.dip2px(getActivity(), 77) - ruanHight;
					log.e("zcq", "moveHight==" + moveHight);
					// allLayout.setY(moveHight);
					mScrollView.setY(moveHight);
				}

				NotesActivity.dismiss();
				// notesMessageViewList.add(getsendMessageLayout());

				allLayout.addView(getsendMessageLayout(randowX, randowY));

			} else {
				sendLinearLayout.setVisibility(View.GONE);
				faceLayout.setVisibility(View.GONE);
				isShowEmoji = false;
				InputMethodManager inputManager = (InputMethodManager) mEditText
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
				isWindow = true;
				mIsKeyboardShow = false;

				allLayout.setY(0);
				mScrollView.setY(0);
				NotesActivity.visible();
				// allLayout.removeViewAt(1);
				// dismissAll();
				removeView(numberId);

			}
		}
	}

}
