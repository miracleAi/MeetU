package com.meetu.fragment;

import java.util.ArrayList;
import java.util.List;

import com.meetu.R;
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
import com.meetu.cloud.callback.ObjActivityCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityWrap;
import com.meetu.cloud.wrap.ObjPraiseWrap;
import com.meetu.common.Constants;
import com.meetu.entity.Huodong;




import com.meetu.sqlite.ActivityDao;
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
import android.widget.Toast;

public class HomePagefragment extends Fragment implements OnRefreshListener2<ListView> ,ScrollDistanceListener,OnItemClickListener,OnClickListener {
	private PullToRefreshListView lvNewsList;
	private NewsListViewAdapter adapter;
	private View view;
	private List<Huodong> data=new ArrayList<Huodong>();
	private RelativeLayout usernumber;

	private ListScrollDistanceCalculator listScrollDistanceCalculator;
	private double HightY;//item滑动的总高度
	private double HightYy;//item滑动的高度
	private double maginY;//标签移动的绝对总高度
	private double maginYy;//标签移动的距离
	private double pingHight;//屏幕的高度
	private int topHight;//屏幕上边标签栏的高度
	private int bottomHight;//屏幕下边taberbar的高度
	private int itemHight;//加载的item的高度
	private int number;//item的数量
	private int maginTop;//item 最初距离top的高度
	
	//网络数据相关
	AVUser currentUser = AVUser.getCurrentUser();
	private List<ObjActivity> objactyList=new ArrayList<ObjActivity>();
	//首页某一项活动
	private	ActivityBean bean = new ActivityBean();
	//网络活动表的一项
	ObjActivity  activityItem = new ObjActivity();
	//网络活动列表
	ArrayList<ActivityBean> actyList = new ArrayList<ActivityBean>();
	//当前用户
	ObjUser user = new ObjUser();
	ActivityDao actyDao;
	ActivityDao activityDao;
	private List<ActivityBean> actyListCache=new ArrayList<ActivityBean>();
	
	private ObjActivity objActivity= null;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		activityDao=new ActivityDao(getActivity());
		pingHight=DisplayUtils.getWindowHeight(getActivity());
		actyDao=new ActivityDao(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view==null){
			 view=inflater.inflate(R.layout.fragment_homepage, null);
			 if (currentUser != null) {
					//强制类型转换
					user = AVUser.cast(currentUser, ObjUser.class);
			 }
			 lvNewsList=(PullToRefreshListView)view.findViewById(R.id.newsList);
//			 load(1,pageSize);
			 
			 actyListCache.addAll(actyDao.queryActys(user.getObjectId()));
			 if(actyListCache.size()==0){
				 loadData();
			 }
			
			 adapter=new NewsListViewAdapter(super.getActivity(),actyListCache);
			 
			 //点赞回调   
			 adapter.setOnItemImageFavorClickCallBack(new OnItemImageFavorClickCallBack() {
				
				
				@Override
				public void onItemImageFavorClick(final int position) {
					// TODO Auto-generated method stub
					initLoadActivity(actyListCache.get(position).getActyId());
					//修改云端
					ObjPraiseWrap.praiseActivity(user, objActivity, new ObjFunBooleanCallback() {
						
						@Override
						public void callback(boolean result, AVException e) {
							// TODO Auto-generated method stub
							if(e!=null||result==false){
								Toast.makeText(getActivity(), "点赞失败，请检查网络", 1000).show();
							}else{
								//插入到本地数据库    成功
								activityDao.updateIsFavor(user.getObjectId(), actyListCache.get(position).getActyId(), 1);	
								Toast.makeText(getActivity(), "点赞成功", 1000).show();
								
								actyListCache.clear();
								actyListCache.addAll(actyDao.queryActys(user.getObjectId()));
								adapter.notifyDataSetChanged();
							}
						}
					});
				}
				
				@Override
				public void onItemCancleImageFavorClick(final int position) {
					//获取当前activity
					initLoadActivity(actyListCache.get(position).getActyId());
					//修改云端
					ObjPraiseWrap.cancelPraiseActivity(user, objActivity, new ObjFunBooleanCallback() {
						
						@Override
						public void callback(boolean result, AVException e) {
							if(e!=null||result==false){
								Toast.makeText(getActivity(), "取消失败，请检查网络", 1000).show();
							}else{
								//插入到本地数据库    成功
								activityDao.updateIsFavor(user.getObjectId(), actyListCache.get(position).getActyId(), 1);
								Toast.makeText(getActivity(), "取消点赞成功", 1000).show();
//								holder.favourImg.setImageResource(R.drawable.acty_cardimg_btn_like_hl);
								actyListCache.clear();
								actyListCache.addAll(actyDao.queryActys(user.getObjectId()));
								adapter.notifyDataSetChanged();
							}
						}
					});
					
				}
			});
			 
			 lvNewsList.setAdapter(adapter);
			 lvNewsList.setMode(Mode.BOTH);
			 lvNewsList.setOnRefreshListener(this);
			 //lvNewsList.setRefreshing(true);	
			 listScrollDistanceCalculator = new ListScrollDistanceCalculator();
			 listScrollDistanceCalculator.setScrollDistanceListener(this);
			 lvNewsList.setOnItemClickListener(this);
			 //调用计算高度的方法
			 lvNewsList.setOnScrollListener(listScrollDistanceCalculator);
			 usernumber=(RelativeLayout) view.findViewById(R.id.usernumber_homepage_rl);
			 usernumber.setOnClickListener(this);
			// loadData(1,pageSize);
			
			
			initView();
		}
		ViewGroup parent=(ViewGroup)view.getParent();
		if(parent!=null){
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
		actyList=new ArrayList<ActivityBean>();
		ObjActivityWrap.queryAllActivitys(new ObjActivityCallback() {

			@Override
			public void callback(List<ObjActivity> objects, AVException e) {
				
				if(e != null){
					return;
				}
				if(objects != null && objects.size()>0){
					log.e("lucifer", "信息拉取成功");
					
					for(ObjActivity activity : objects){
						
							activityItem = activity;
							objactyList.add(activity);
							log.e("zcq", "getLocationLongitude=="+activity.getLocationLongitude()+"  "+activity.getLocationLatitude());
							bean=new ActivityBean();
							bean.setActyId(activity.getObjectId());
							bean.setUserId(user.getObjectId());
							bean.setActivityContent(activity.getActivityContent().getUrl());
							bean.setActivityCover(activity.getActivityCover().getUrl());
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
							bean.setLocationGovernment(activity.getLocationGovernment());
							bean.setConversationId(activity.getConversationId());
							bean.setIndex(0);
							bean.setIsFavor(0);
							bean.setOrderAndFollow(0);
							bean.setLocationLatitude(""+activity.getLocationLatitude());
							bean.setLocationLongtitude(""+activity.getLocationLongitude());
							actyList.add(bean);
						
					}
					/**
					 * 此处执行活动信息保存
					 * */
					log.e("zcq", "actyList="+actyList.size());
					actyDao.deleteByUser(user.getObjectId());
					log.e("zcq", "actyList="+actyList.size());
					log.e("zcq","actyList.url=="+actyList.get(0).getActivityCover()+" =="+actyList.get(1).getActivityCover());
					actyDao.saveActyList(actyList);
									
					log.e("zcq", "actyList=="+actyList.size());
					actyListCache.addAll(actyDao.queryActys(user.getObjectId()));
					log.e("zcq", "actyListCache=="+actyListCache.size());
					handler.sendEmptyMessage(1);
//					SharedPreferences sp = getSharedPreferences(Constants.ACTIVITY_CACHE_SP,MODE_PRIVATE);
//					Editor editor = sp.edit();
//					editor.putString(Constants.ACTIVITY_CACHE_TIME, String.valueOf(System.currentTimeMillis()));
//					editor.commit();
					//查询是否点赞（因测试活动照片，注释此行）
//					queryFavor(activityItem);
//					//轮播封面展示图片列表
//					queryActyCovers(activityItem);
//					//参与用户列表 参与并且我关注的人数
//					queryOrderUsers(activityItem);
					
				}
			}
		});
		
	}
	//查询是否点赞  setFavor
	public void queryFavor(ObjActivity activity){
		if(user == null){
			return ;
		}
		ObjPraiseWrap.queryActivityFavor(user, activity, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				//测试点赞 取消赞  获取点赞信息后执行
				if(result){
//					actyList.get(0).setIsFavor(1);
//					favorImg.setVisibility(View.VISIBLE);
//					clickBtn.setText(CANCEL_PRAISE_ACTY);
				}else{
//					actyList.get(0).setIsFavor(0);
//					favorImg.setVisibility(View.GONE);
//					clickBtn.setText(PRAISE_ACTIVITY);
				}
				/**
				 * 此处执行更新活动信息点赞字段
				 * */
//				actyDao.updateIsFavor(user.getObjectId(), 0, actyList.get(0).getIsFavor());
			}
		});
	}

	private void initView() {
		maginTop=DensityUtil.dip2px(getActivity(), 88);
		number=actyListCache.size();
		pingHight=DisplayUtils.getWindowHeight(getActivity());
		topHight=DensityUtil.dip2px(getActivity(), 44);
		bottomHight=DensityUtil.dip2px(getActivity(), 54);
		itemHight=DensityUtil.dip2px(getActivity(), 301)+1;//item的高度（+1是加下划线）
		
		HightY=itemHight*number-pingHight+topHight+bottomHight;//item滑动的总高度
		maginY=pingHight-topHight-bottomHight-itemHight;//标签移动的绝对总高度
		
	}

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
	}
	public void load(int pageNo,int pageSize){


	}
	/**
	 * 点击进入详情页面
	 */

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		Intent intent=new Intent(getActivity(),HomePageDetialActivity.class);
//		intent.putExtra("style", ""+data.get(position-1).getStyle());
		Bundle bundle=new Bundle();
		bundle.putSerializable("activityBean", actyListCache.get(position-1));
//		log.e("zcq", "objactyList=="+objactyList.size());
//		bundle.putSerializable("objActivity", objactyList.get(position-1));
		intent.putExtras(bundle);
		log.e("position", "position="+position);
		startActivity(intent);
//		Toast.makeText(getActivity(), ""+id, Toast.LENGTH_SHORT).show();
		
	}
	
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 1:
				adapter.notifyDataSetChanged();
				
				initView();
				refreshComplete();
				break;
			}
		}
	
	};
	private void refreshComplete(){
		lvNewsList.postDelayed(new Runnable() {
	
	            @Override
	            public void run() {
	           	 lvNewsList.onRefreshComplete();
	            }
	    }, 500);
	}
	
	private int pageNo=1;
	private int pageSize=3;

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//		Toast.makeText(getActivity(), "下拉加载数据", Toast.LENGTH_SHORT).show();
		pageNo=1;
//		load(1, pageSize);
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
	//	Toast.makeText(getActivity(), "上拉加载数据", Toast.LENGTH_SHORT).show();
		pageNo++;
		load(pageNo,pageSize);
		
	}

	
	/**
	 * 调用计算滑动总高度的方法 total为滑动总高度
	 */
	@Override
	public void onScrollDistanceChanged(int delta, int total) {		
		RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)usernumber.getLayoutParams();
		int a=lp.topMargin;			
		lp.topMargin=(int) (-total*maginY/HightY)+maginTop;		
        int i=lp.topMargin;
        Log.e("e", "total "+total +" delta "+delta +" i  "+i +" HightY "+HightY+" a="+a);
        usernumber.setLayoutParams(lp);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.usernumber_homepage_rl :
//				Toast.makeText(getActivity(), "进入报名列表", Toast.LENGTH_SHORT).show();
				//TODO 要传入实时滑动到的那个activity
				Intent intent=new Intent(getActivity(),JoinUsersActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("activityBean", actyListCache.get(2));
				intent.putExtras(bundle);
				startActivity(intent);
				
				break;

			default :
				break;
		}
		
	}

	/**
	 * 获得活动的activity
	 * @param activityId  
	 * @author lucifer
	 * @date 2015-11-13
	 */
	public void initLoadActivity(String activityId) {
		log.e("zcq", "activityId=="+activityId);
			try {
				 objActivity=AVObject.createWithoutData(ObjActivity.class, activityId);

			} catch (AVException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	 

}
