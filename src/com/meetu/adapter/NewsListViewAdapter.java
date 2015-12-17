package com.meetu.adapter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.lidroid.xutils.BitmapUtils;
import com.meetu.bean.ActivityBean;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityPhotoWrap;
import com.meetu.cloud.wrap.ObjPraiseWrap;
import com.meetu.common.ImageLoader;
import com.meetu.entity.Huodong;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.ActivityDao;
import com.meetu.tools.DateUtils;
import com.meetu.tools.DensityUtil;

@SuppressLint("NewApi")
public class NewsListViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<ActivityBean> newsList;
	private ImageLoader mImageLoader;

	private final int TYPE_COUNT = 4;
	private FinalBitmap finalBitmap;
	// 网络相关
	private BitmapUtils bitmapUtils;
	// 网络数据相关
	AVUser currentUser = AVUser.getCurrentUser();
	// 当前用户
	ObjUser user = new ObjUser();
	ActivityDao activityDao;
	private ObjActivity objActivity = null;

	public NewsListViewAdapter(Context context, List<ActivityBean> newsList) {
		this.mContext = context;
		this.newsList = newsList;

		bitmapUtils = new BitmapUtils(mContext);
		MyApplication app = (MyApplication) context.getApplicationContext();
		finalBitmap = app.getFinalBitmap();
		activityDao = new ActivityDao(mContext);

		if (currentUser != null) {
			// 强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}
	}

	// 设置点赞回调
	public interface OnItemImageFavorClickCallBack {
		// 点赞
		void onItemImageFavorClick(int position);

		// 取消点赞
		void onItemCancleImageFavorClick(int position);

	}

	private OnItemImageFavorClickCallBack onItemImageFavorClickCallBack;

	public void setOnItemImageFavorClickCallBack(
			OnItemImageFavorClickCallBack onItemImageFavorClickCallBack) {
		this.onItemImageFavorClickCallBack = onItemImageFavorClickCallBack;
	}

	/**
	 * 
	 */
	@Override
	public int getViewTypeCount() {

		return TYPE_COUNT;
	}

	@Override
	public int getCount() {

		Log.d("lucifer", "getCount()");

		return newsList.size();
	}

	@Override
	public Object getItem(int position) {

		Log.d("lucifer", "getItem()");
		return newsList.get(position);
	}

	@Override
	public long getItemId(int position) {

		Log.d("lucifer", "getItemId()");
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		final ActivityBean item = newsList.get(position);
		log.e("lucifer", "getIsFavor==" + item.getIsFavor());
		initLoadActivity(item.getActyId());
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_huodong_fragment, null);
			holder.ivImgUrl = (ImageView) convertView
					.findViewById(R.id.img_huodong_homepage);
			holder.tvTilte = (TextView) convertView
					.findViewById(R.id.title_huodong_homepage);
			holder.styleTextView = (TextView) convertView
					.findViewById(R.id.style_homepage_tv);
			holder.topRl = (RelativeLayout) convertView
					.findViewById(R.id.top_homepage_item);
			holder.tvAdress = (TextView) convertView
					.findViewById(R.id.address_huodong_homepage_tv);
			holder.tvStarTime = (TextView) convertView
					.findViewById(R.id.starttime_huodong_homepager_tv);
			holder.favourImg = (ImageView) convertView
					.findViewById(R.id.favour_hongdong_fragment_img);
			holder.praiseCount = (TextView) convertView
					.findViewById(R.id.praiseCount_huodong_fragment_tv);
			holder.favorLayout=(RelativeLayout) convertView.findViewById(R.id.favour_hongdong_fragment_rl);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Log.d("lucifer", "getView()");
		// RelativeLayout.LayoutParams params=(LayoutParams)
		// holder.topRl.getLayoutParams();
		// if(position==0){
		// params.topMargin=DensityUtil.dip2px(mContext, 44);
		// holder.topRl.setLayoutParams(params);
		// }

		// holder.ivImgUrl.setImageResource(item.getImg());
		// bitmapUtils.display(holder.ivImgUrl, item.getActivityCover());
		if (item.getActivityCover() != null) {
			finalBitmap.display(holder.ivImgUrl, item.getActivityCover());
		}

		// new ImageLoader().showImageByThread(holder.ivImgUrl, item.getImg());
		// mImageLoader.showImageByAsyncTask(holder.ivImgUrl, item.getImg());
		holder.tvTilte.setText(item.getTitle()+"---"+item.getTitleSub());
		holder.tvAdress.setText( "地址: "
				+ item.getLocationPlace());
		holder.tvStarTime
				.setText(DateUtils.getDateToString(item.getTimeStart()));
		holder.praiseCount.setText("" + item.getPraiseCount());

		// long nowTime=System.currentTimeMillis();
		holder.styleTextView
				.setText(ObjActivity.getStatusStr(item.getStatus()));
		if (item.getStatus() == 70) {
			holder.topRl
					.setBackgroundResource(R.drawable.acty_cover_card_img_mask);
		}

		/**
		 * 查询是否对活动点赞
		 */
		ObjPraiseWrap.queryActivityFavor(user, objActivity,
				new ObjFunBooleanCallback() {

					@Override
					public void callback(boolean result, AVException e) {
						if (e != null) {
							log.e("zcq", e);
							return;
						} else if (result) {
							// 表示已经点赞
							holder.favourImg
									.setImageResource(R.drawable.acty_cardimg_btn_like_hl);
							holder.favorLayout
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											onItemImageFavorClickCallBack
													.onItemCancleImageFavorClick(position);
											holder.favourImg
													.setImageResource(R.drawable.acty_show_navi_btn_like_line_nor);
										}
									});
						} else {
							// 表示未点赞
							holder.favourImg
									.setImageResource(R.drawable.acty_show_navi_btn_like_line_nor);
							holder.favorLayout
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {

											onItemImageFavorClickCallBack
													.onItemImageFavorClick(position);
											holder.favourImg
													.setImageResource(R.drawable.acty_cardimg_btn_like_hl);
										}
									});
						}

					}
				});



		return convertView;
	}

	public class ViewHolder {
		private TextView tvTilte;
		private TextView tvDigest;
		private TextView tvReply;
		private ImageView ivImgUrl;
		private ImageView ivExtImg1;
		private ImageView ivExtImg2;
		private TextView styleTextView;
		private RelativeLayout topRl;
		private TextView tvAdress;// 活动地址
		private TextView tvStarTime;
		private ImageView favourImg;// 点赞图片
		private TextView praiseCount;// 点赞数量
		private RelativeLayout favorLayout;//点赞布局
	}

	/**
	 * 获得活动的activity
	 * 
	 * @param activityId
	 * @author lucifer
	 * @date 2015-11-13
	 */
	private void initLoadActivity(String activityId) {
		log.e("zcq", "activityId==" + activityId);
		try {
			objActivity = AVObject.createWithoutData(ObjActivity.class,
					activityId);

		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 查询是否对活动点赞
	 * 
	 * @author lucifer
	 * @date 2015-11-16
	 */
	private void isFavor() {
		ObjPraiseWrap.queryActivityFavor(user, objActivity,
				new ObjFunBooleanCallback() {

					@Override
					public void callback(boolean result, AVException e) {
						// TODO Auto-generated method stub

					}
				});
	}

}
