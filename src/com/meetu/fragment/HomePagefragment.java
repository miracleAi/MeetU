package com.meetu.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.meetu.activity.homepage.HomePageDetialActivity;
import com.meetu.activity.homepage.JoinUsersActivity;
import com.meetu.adapter.NewsListViewAdapter;
import com.meetu.adapter.NewsListViewAdapter.OnItemImageFavorClickCallBack;
import com.meetu.bean.ActivityBean;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.callback.ObjActivityCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjUserCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityOrderWrap;
import com.meetu.cloud.wrap.ObjActivityWrap;
import com.meetu.cloud.wrap.ObjPraiseWrap;
import com.meetu.common.Constants;
import com.meetu.entity.Huodong;
import com.meetu.sqlite.ActivityDao;
import com.meetu.sqlite.UserAboutDao;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;
import com.meetu.view.ListScrollDistanceCalculator;
import com.meetu.view.ListScrollDistanceCalculator.ScrollDistanceListener;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomePagefragment extends Fragment implements
		OnRefreshListener2<ListView>, ScrollDistanceListener,
		OnItemClickListener, OnClickListener {
	private PullToRefreshListView lvNewsList;
	private NewsListViewAdapter adapter;
	private View view;
	private List<Huodong> data = new ArrayList<Huodong>();
	private RelativeLayout usernumber;

	private ListScrollDistanceCalculator listScrollDistanceCalculator;
	private double HightY;// item滑动的总高度
	private double HightYy;// item滑动的高度
	private double maginY;// 标签移动的绝对总高度
	private double maginYy;// 标签移动的距离
	private double pingHight;// 屏幕的高度
	private int topHight;// 屏幕上边标签栏的高度
	private int bottomHight;// 屏幕下边taberbar的高度
	private int itemHight;// 加载的item的高度
	private int number;// item的数量
	private int maginTop;// item 最初距离top的高度

	private TextView numberAll, numberFavor;// 活动报名总人数
	private int itemNow = 0;// 现在标签在第几个item
	// 网络数据相关
	AVUser currentUser = AVUser.getCurrentUser();
	private List<ObjActivity> objactyList = new ArrayList<ObjActivity>();
	// 首页某一项活动
	private ActivityBean bean = new ActivityBean();
	// 网络活动表的一项
	ObjActivity activityItem = new ObjActivity();
	// 网络活动列表
	ArrayList<ActivityBean> actyList = new ArrayList<ActivityBean>();
	// 当前用户
	ObjUser user = new ObjUser();
	ActivityDao actyDao;
	ActivityDao activityDao;
	private List<ActivityBean> actyListCache = new ArrayList<ActivityBean>();

	private ObjActivity objActivity = null;
	private UserAboutDao userAboutDao;
	private ArrayList<UserAboutBean> userAboutBeanList = new ArrayList<UserAboutBean>();
	
	private RelativeLayout cityLayout;
	
	//网络操作点击 管理
	private boolean ischeckFavor=false;//当前操作网络是否正在请求中
	
	private int numberCache;//用来初始化cache中的关注数量
	
	//所有用户数量
	private List<String> userAllNumberList=new ArrayList<String>();
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		activityDao = new ActivityDao(getActivity());
		pingHight = DisplayUtils.getWindowHeight(getActivity());
		actyDao = new ActivityDao(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_homepage, null);
			if (currentUser != null) {
				// 强制类型转换
				user = AVUser.cast(currentUser, ObjUser.class);
			}
			userAboutDao = new UserAboutDao(getActivity());
			lvNewsList = (PullToRefreshListView) view
					.findViewById(R.id.newsList);
			// load(1,pageSize);

			actyListCache.addAll(actyDao.queryActys(user.getObjectId()));
			if (actyListCache.size() == 0) {
				loadData();
			}

			adapter = new NewsListViewAdapter(super.getActivity(),
					actyListCache);


			if(ischeckFavor==false){
				ischeckFavor=true;
				// 点赞回调
				adapter.setOnItemImageFavorClickCallBack(new OnItemImageFavorClickCallBack() {

					@Override
					public void onItemImageFavorClick(final int position) {
						// TODO Auto-generated method stub
						initLoadActivity(actyListCache.get(position).getActyId());
						// 修改云端
						ObjPraiseWrap.praiseActivity(user, objActivity,
								new ObjFunBooleanCallback() {

									@Override
									public void callback(boolean result,
											AVException e) {
										// TODO Auto-generated method stub
										if (e != null || result == false) {
											Toast.makeText(getActivity(),
													"点赞失败，请检查网络", 1000).show();
											ischeckFavor=false;
										} else {
											// 插入到本地数据库 成功
											activityDao.updateIsFavor(user
													.getObjectId(), actyListCache
													.get(position).getActyId(), 1);
											Toast.makeText(getActivity(), "点赞成功",
													1000).show();
											int number=actyListCache.get(position).getPraiseCount()+1;
											activityDao.updateFavourNumber(user.getObjectId(), actyListCache.get(position).getActyId(), number);

											actyListCache.clear();
											actyListCache.addAll(actyDao
													.queryActys(user.getObjectId()));
											adapter.notifyDataSetChanged();
											ischeckFavor=false;
										}

									}
								});
					}


					@Override
					public void onItemCancleImageFavorClick(final int position) {
						// 获取当前activity
						initLoadActivity(actyListCache.get(position).getActyId());
						// 修改云端
						ObjPraiseWrap.cancelPraiseActivity(user, objActivity,
								new ObjFunBooleanCallback() {

									@Override
									public void callback(boolean result,
											AVException e) {
										if (e != null || result == false) {
											Toast.makeText(getActivity(),
													"取消失败，请检查网络", 1000).show();
											ischeckFavor=false;
										} else {
											// 插入到本地数据库 成功
											activityDao.updateIsFavor(user
													.getObjectId(), actyListCache
													.get(position).getActyId(), 1);
											Toast.makeText(getActivity(), "取消点赞成功",
													1000).show();
											int number=actyListCache.get(position).getPraiseCount()-1;
											activityDao.updateFavourNumber(user.getObjectId(), actyListCache.get(position).getActyId(), number);
											
											// holder.favourImg.setImageResource(R.drawable.acty_cardimg_btn_like_hl);
											actyListCache.clear();
											actyListCache.addAll(actyDao
													.queryActys(user.getObjectId()));
											adapter.notifyDataSetChanged();
											ischeckFavor=false;
										}

									}
								});

					}
				});
			}
	

			lvNewsList.setAdapter(adapter);
			lvNewsList.setMode(Mode.BOTH);
			lvNewsList.setOnRefreshListener(this);
			// lvNewsList.setRefreshing(true);
			listScrollDistanceCalculator = new ListScrollDistanceCalculator();
			listScrollDistanceCalculator.setScrollDistanceListener(this);
			lvNewsList.setOnItemClickListener(this);
			// 调用计算高度的方法
			lvNewsList.setOnScrollListener(listScrollDistanceCalculator);
			usernumber = (RelativeLayout) view
					.findViewById(R.id.usernumber_homepage_rl);
			usernumber.setOnClickListener(this);
			numberAll = (TextView) view
					.findViewById(R.id.zongnumber_homepage_tv);
			numberFavor = (TextView) view
					.findViewById(R.id.woguanzhunumber_homepage_tv);
			// loadData(1,pageSize);

			initView();
			
			cityLayout=(RelativeLayout) view.findViewById(R.id.adress_homepage_fragment_rl);
			cityLayout.setOnClickListener(this);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	/**
	 * 获取活动首页相关 网络数据
	 * 
	 * @author lucifer
	 * @date 2015-11-10
	 */
	private void loadData() {
		// TODO Auto-generated method stub
		log.e("lucifer", "正在加载网络数据");
		actyList = new ArrayList<ActivityBean>();
		numberCache=0;
		ObjActivityWrap.queryAllActivitys(new ObjActivityCallback() {

			@Override
			public void callback(List<ObjActivity> objects, AVException e) {

				if (e != null) {
					return;
				}
				if (objects != null && objects.size() > 0) {
					log.e("lucifer", "信息拉取成功");

					for (ObjActivity activity : objects) {
						System.out.println(objects);
						activityItem = activity;
						objactyList.add(activity);
						log.e("zcq",
								"getLocationLongitude=="
										+ activity.getLocationLongitude()
										+ "  " + activity.getLocationLatitude());
						bean = new ActivityBean();
						bean.setActyId(activity.getObjectId());
						bean.setUserId(user.getObjectId());
						if (activity.getActivityContent() != null) {
							bean.setActivityContent(""
									+ activity.getActivityContent().getUrl());
						}
						if (activity.getActivityCover() != null) {
							bean.setActivityCover(""
									+ activity.getActivityCover().getUrl());
						}
						bean.setLocationAddress(activity.getLocationAddress());
						bean.setLocationPlace(activity.getLocationPlace());
						bean.setOrderCountBoy(activity.getOrderCountBoy());
						bean.setOrderCountGirl(activity.getOrderCountGirl());
						bean.setPraiseCount(activity.getPraiseCount());
						bean.setStatus(activity.getStatus());
						bean.setTimeStart(activity.getTimeStart());
						bean.setTitle(activity.getTitle());
						bean.setTitleSub(activity.getTitleSub());
						bean.setTimeStop(activity.getTimeStop());
						bean.setTimeChatStop(activity.getTimeChatStop());
						bean.setLocationGovernment(activity
								.getLocationGovernment());
						bean.setConversationId(activity.getConversationId());
						bean.setIndex(0);
						bean.setIsFavor(0);
						bean.setOrderAndFollow(0);
						bean.setLocationLatitude(""
								+ activity.getLocationLatitude());
						bean.setLocationLongtitude(""
								+ activity.getLocationLongitude());
						actyList.add(bean);
						log.e("zcq", "objects==" + objects.size() + " ");

						// 用户相关缓存

						initLoadActivity(activity.getObjectId());
						queryOrderUsers(objActivity,
								activity.getConversationId());

					}
					/**
					 * 此处执行活动信息保存
					 * */
					log.e("zcq", "actyList=" + actyList.size());
					actyDao.deleteByUser(user.getObjectId());
					log.e("zcq", "actyList=" + actyList.size());
					log.e("zcq", "actyList.url=="
							+ actyList.get(0).getActivityCover() + " =="
							+ actyList.get(1).getActivityCover());
					actyDao.saveActyList(actyList);

					log.e("zcq", "actyList==" + actyList.size());
					actyListCache.clear();
					actyListCache.addAll(actyDao.queryActys(user.getObjectId()));
					log.e("zcq", "actyListCache==" + actyListCache.size());
					
					

					handler.sendEmptyMessage(1);

				}
			}
		});

	}

	// 查询是否点赞 setFavor
	public void queryFavor(ObjActivity activity) {
		if (user == null) {
			return;
		}
		ObjPraiseWrap.queryActivityFavor(user, activity,
				new ObjFunBooleanCallback() {

					@Override
					public void callback(boolean result, AVException e) {
						// TODO Auto-generated method stub
						// 测试点赞 取消赞 获取点赞信息后执行
						if (result) {
							// actyList.get(0).setIsFavor(1);
							// favorImg.setVisibility(View.VISIBLE);
							// clickBtn.setText(CANCEL_PRAISE_ACTY);
						} else {
							// actyList.get(0).setIsFavor(0);
							// favorImg.setVisibility(View.GONE);
							// clickBtn.setText(PRAISE_ACTIVITY);
						}
						/**
						 * 此处执行更新活动信息点赞字段
						 * */
						// actyDao.updateIsFavor(user.getObjectId(), 0,
						// actyList.get(0).getIsFavor());
					}
				});
	}

	private void initView() {
		maginTop = DensityUtil.dip2px(getActivity(), 88);
		number = actyListCache.size();
		pingHight = DisplayUtils.getWindowHeight(getActivity());
		topHight = DensityUtil.dip2px(getActivity(), 44);
		bottomHight = DensityUtil.dip2px(getActivity(), 54);
		itemHight = DensityUtil.dip2px(getActivity(), 301) + 1;// item的高度（+1是加下划线）

		HightY = itemHight * number - pingHight + topHight + bottomHight;// item滑动的总高度
		maginY = pingHight - topHight - bottomHight - itemHight;// 标签移动的绝对总高度

		int allnumberUser=	actyListCache.get(itemNow).getOrderCountBoy()+actyListCache.get(itemNow).getOrderCountGirl();
		numberAll.setText(""+allnumberUser+"人报名");
		numberFavor.setText(""+actyListCache.get(itemNow).getOrderAndFollow()+"个我关注的");
	}

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
	}

	public void load(int pageNo, int pageSize) {

	}

	/**
	 * 点击进入详情页面
	 */

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(), HomePageDetialActivity.class);
		// intent.putExtra("style", ""+data.get(position-1).getStyle());
		Bundle bundle = new Bundle();
		bundle.putSerializable("activityBean", actyListCache.get(position - 1));
		// log.e("zcq", "objactyList=="+objactyList.size());
		// bundle.putSerializable("objActivity", objactyList.get(position-1));
		intent.putExtras(bundle);
		log.e("position", "position=" + position);
		startActivity(intent);

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();

				initView();
				refreshComplete();
				
				break;
			}
		}

	};

	private void refreshComplete() {
		lvNewsList.postDelayed(new Runnable() {

			@Override
			public void run() {
				lvNewsList.onRefreshComplete();
			}
		}, 500);
	}

	private int pageNo = 1;
	private int pageSize = 5;

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNo = 1;
		// load(1, pageSize);
		refreshComplete();
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//		pageNo++;
//		load(pageNo, pageSize);
		refreshComplete();
		Toast.makeText(getActivity(), "没有更多了呢", Toast.LENGTH_SHORT).show();

	}

	/**
	 * 调用计算滑动总高度的方法 total为滑动总高度
	 */
	@Override
	public void onScrollDistanceChanged(int delta, int total) {
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) usernumber
				.getLayoutParams();
		int a = lp.topMargin;
		lp.topMargin = (int) (-total * maginY / HightY) + maginTop;
		int i = lp.topMargin;
		// Log.e("e", "total "+total +" delta "+delta +" i  "+i
		// +" HightY "+HightY+" a="+a);
		usernumber.setLayoutParams(lp);

		// TODO 实时更新活动标签里的内容 滑动距离有点问题

		try {
			int ii = (int) ((maginY / (actyListCache.size() - 1)));// 标签滑动一个item 滑动的距离
			//log.e("lucifer" + "ii==" + ii + " i==" + i + " maginTop==" + maginTop);
			int aa = (i - maginTop) / ii;
			if (itemNow != aa) {
			//	log.e("lucifer", "total==" + total + " itemHight==" + itemHight);
				itemNow = aa;
				if (itemNow >= actyListCache.size()) {
					itemNow = actyListCache.size() - 1;
//					int number = userAboutDao.queryUserAbout(user.getObjectId(), 3,
//							actyListCache.get(itemNow).getConversationId()).size();
				int allnumberUser=	actyListCache.get(itemNow).getOrderCountBoy()+actyListCache.get(itemNow).getOrderCountGirl();
//				numberAll.setText("" + (actyListCache.get(itemNow).getOrderCountBoy()+actyListCache.get(itemNow).getOrderCountGirl())+"人报名");
					numberAll.setText(""+allnumberUser+"人报名");
					numberFavor.setText(""+actyListCache.get(itemNow).getOrderAndFollow()+"个我关注的");
				} else {

					if(itemNow<0){
						itemNow=0;
					}
					int allnumberUser=	actyListCache.get(itemNow).getOrderCountBoy()+actyListCache.get(itemNow).getOrderCountGirl();
					numberAll.setText(""+allnumberUser+"人报名");

					numberFavor.setText(""+actyListCache.get(itemNow).getOrderAndFollow()+"个我关注的");
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.usernumber_homepage_rl:
			// Toast.LENGTH_SHORT).show();
			// TODO 要传入实时滑动到的那个activity
			Intent intent = new Intent(getActivity(), JoinUsersActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("activityBean", actyListCache.get(itemNow));
			intent.putExtras(bundle);
			startActivity(intent);

			break;
		case R.id.adress_homepage_fragment_rl:
			Toast.makeText(getActivity(), " 亲爱的 暂时仅开放北京 小U正在飞往其他城市哦", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}

	}

	/**
	 * 获得活动的activity
	 * 
	 * @param activityId
	 * @author lucifer
	 * @date 2015-11-13
	 */
	public void initLoadActivity(String activityId) {
		log.e("zcq", "activityId==" + activityId);
		try {
			objActivity = AVObject.createWithoutData(ObjActivity.class,
					activityId);

		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

	}

	/**
	 * //查询参加活动列表 缓存活动成员
	 * 
	 * @param activity
	 * @param conversitionId 
	 * @author lucifer
	 * @date 2015-11-23
	 */
	public void queryOrderUsers(final ObjActivity activity,
			final String conversitionId) {
		

		ObjActivityOrderWrap.queryActivitySignUp(activity,
				new ObjUserCallback() {

					@Override
					public void callback(List<ObjUser> objects, AVException e) {
						if (e != null) {
							log.e("zcq", e);
							return;
						} else if (objects != null) {
							numberCache++;

							int numberFavor=0;
							userAboutBeanList = new ArrayList<UserAboutBean>();
							for (ObjUser objUser : objects) {
								UserAboutBean item = new UserAboutBean();
								item.setUserId(user.getObjectId());
								item.setAboutType(3);
								item.setAboutColetctionId(conversitionId);
								item.setAboutUserId(objUser.getObjectId());

								userAboutBeanList.add(item);
								
								List<UserAboutBean> lists=new ArrayList<UserAboutBean>();
								lists=userAboutDao.queryUserAbout(user.getObjectId(), 1, "");
								if(lists!=null&&lists.size()>0){
									for(int i=0;i<lists.size();i++){
										if(objUser.getObjectId().equals(lists.get(i).getAboutUserId())){
											numberFavor++;
											break;	
										}
										
									}			
									
								}else{
									//不存在我关注的人
									
								}

							}
							log.e("zcq", "numberFavor=="+numberFavor);
							activityDao.updateFavorUserNumber(user.getObjectId(), activity.getObjectId(), numberFavor);
							
							
							userAboutDao.deleteByType(user.getObjectId(),
									Constants.ACTIVITY_TYPE, conversitionId);

							log.e("zcq", "userAboutBeanList=="
									+ userAboutBeanList.size());
							userAboutDao.saveUserAboutList(userAboutBeanList);
							
							log.e("numberCache", ""+numberCache);
							if(numberCache==actyListCache.size()){
								log.e("zcq cahce", "重新加载数据");
								actyListCache.clear();
								actyListCache.addAll(actyDao.queryActys(user.getObjectId()));
								handler.sendEmptyMessage(1);
								
							}
						}

					}
				});
		
	}

}
