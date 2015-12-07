package com.meetu.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.lidroid.xutils.BitmapUtils;
import com.meetu.MainActivity;
import com.meetu.activity.mine.FavorListActivity;
import com.meetu.activity.mine.MinephotoActivity;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.entity.PhotoWall;
import com.meetu.myapplication.MyApplication;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MinePhotoAdapter extends PagerAdapter {
	private Context mContext;
	private List<ObjUserPhoto> Newslist;

	// 网络数据相关

	private BitmapUtils bitmapUtils;
	private String photoUrl;
	private FinalBitmap finalBitmap;
	private AVUser currentUser = AVUser.getCurrentUser();
	// 当前用户
	private ObjUser userMy = new ObjUser();

	private String userId;// 用户id
	private boolean isMyself = true;// 用来标记是否从我自己的页面跳过来的
	private ObjUser userObjUser = new ObjUser();// 他人的user对象

	public MinePhotoAdapter(Context context, List<ObjUserPhoto> list,
			String userID) {
		super();
		this.mContext = context;
		this.Newslist = list;
		this.userId = userID;
		bitmapUtils = new BitmapUtils(mContext);
		MinephotoActivity activity = (MinephotoActivity) context;
		MyApplication app = (MyApplication) activity.getApplicationContext();
		finalBitmap = app.getFinalBitmap();
		if (currentUser != null) {
			// 强制类型转换
			userMy = AVUser.cast(currentUser, ObjUser.class);
		}
		log.e("zcq", "userId==" + userId);
		if (userId != null) {
			isMyself = false;
		}
	}

	@Override
	public int getCount() {

		return Newslist.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {

		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		ObjUserPhoto item = Newslist.get(position);

		log.e("zcq", "" + photoUrl);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.item_minephoto_viewpager, null);

		TextView desc = (TextView) view
				.findViewById(R.id.desc_item_minephoto_tv);
		desc.setText("" + item.getPhotoDescription());
		TextView favorNumber = (TextView) view
				.findViewById(R.id.favorNumber_item_minephoto);
		favorNumber.setText("" + item.getPraiseCount());
		ImageView img = (ImageView) view.findViewById(R.id.photo_demail_mine);

		RelativeLayout.LayoutParams params = (LayoutParams) img
				.getLayoutParams();
		// params.width=DisplayUtils.getWindowWidth(mContext);
		params.width = 1200;
		img.setLayoutParams(params);

		if (item.getPhoto() != null) {
			photoUrl = item.getPhoto().getUrl();
			finalBitmap.display(img, photoUrl);
		}

		if (isMyself == false) {
			getUserInfo(userId, view);
		} else {
			TextView name = (TextView) view
					.findViewById(R.id.name_mine_photoview_fullscreen);
			name.setText("" + userMy.getNameNick());
			ImageView photoHead = (ImageView) view
					.findViewById(R.id.nameheader_mine_photoview_fullscreen_img);
			RelativeLayout favorLayout = (RelativeLayout) view
					.findViewById(R.id.favor_minephoto_mine_rl);
			if (userMy.getProfileClip() != null) {
				finalBitmap
						.display(photoHead, userMy.getProfileClip().getUrl());
			}
			favorLayout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(mContext,
							FavorListActivity.class);
					mContext.startActivity(intent);

				}
			});

		}

		container.addView(view);
		return view;
	}

	/**
	 * 根据创建者用户id 获取用户相关信息
	 * 
	 * @param objId
	 * @author lucifer
	 * @date 2015-11-17
	 */
	private void getUserInfo(String objId, final View view) {
		ObjUserWrap.getObjUser(objId, new ObjUserInfoCallback() {

			@Override
			public void callback(ObjUser user, AVException e) {
				if (e != null) {
					return;
				} else if (user != null) {
					userObjUser = user;

					TextView name = (TextView) view
							.findViewById(R.id.name_mine_photoview_fullscreen);
					name.setText("" + userMy.getNameNick());

					ImageView photoHead = (ImageView) view
							.findViewById(R.id.nameheader_mine_photoview_fullscreen_img);
					// bitmapUtils.display(photoHead,
					// user.getProfileClip().getUrl());

					log.e("zcq", "isMyself==" + isMyself);
					if (isMyself == true) {
						log.e("zcq", "wode" + "" + user.getProfileClip());
						if (userMy.getProfileClip() != null) {
							finalBitmap.display(photoHead, userMy
									.getProfileClip().getUrl());
						}
					} else {
						log.e("zcq", "nide");
						if (userObjUser.getProfileClip() != null) {
							finalBitmap.display(photoHead, userObjUser
									.getProfileClip().getUrl());
						}
					}

					/**
					 * viewpager 内部事件监听处理
					 */

					RelativeLayout favorLayout = (RelativeLayout) view
							.findViewById(R.id.favor_minephoto_mine_rl);
					favorLayout.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {

							// 图片点赞 给用户
							Toast.makeText(mContext, "dianzan", 1000).show();

						}
					});

				} else {
					return;
				}

			}
		});
	}

}
