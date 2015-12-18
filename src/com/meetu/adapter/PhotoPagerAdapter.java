package com.meetu.adapter;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;

import com.avos.avoscloud.LogUtil.log;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.meetu.activity.homepage.HomePageDetialActivity;
import com.meetu.cloud.object.ObjActivityCover;
import com.meetu.cloud.object.ObjActivityPhoto;
import com.meetu.entity.PhotoWall;
import com.meetu.entity.Photolunbo;
import com.meetu.myapplication.MyApplication;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PhotoPagerAdapter extends PagerAdapter {
	private Context mContext;
	private List<ObjActivityCover> Newslist;
	private BitmapUtils bitmapUtils;
	private FinalBitmap finalBitmap;

	public PhotoPagerAdapter(Context context, List<ObjActivityCover> list) {
		super();
		this.mContext = context;
		this.Newslist = list;
		bitmapUtils = new BitmapUtils(mContext);
		HomePageDetialActivity activity = (HomePageDetialActivity) context;
		// NewsApplication
		// app=(NewsApplication)activity.getApplicationContext();
		// finalBitmap=app.getFinalBitmap();
		MyApplication app = (MyApplication) context.getApplicationContext();
		finalBitmap = app.getFinalBitmap();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int number = Integer.MAX_VALUE;
		return Newslist.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		// super.destroyItem(container, position, object);r
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		ObjActivityCover item = Newslist.get(position % Newslist.size());
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.item_photolunbo_detial, null);
		ImageView img = (ImageView) view
				.findViewById(R.id.photo_lunbo_item_homepage_img);
		RelativeLayout loadLayout = (RelativeLayout) view.findViewById(R.id.load_layout);
		img.setTag(loadLayout);
		loadLayout.setVisibility(View.VISIBLE);
		
		log.e("zcq url", item.getCover().getUrl());
		
		//finalBitmap.display(img, item.getCover().getUrl());
		bitmapUtils.display(img, item.getCover().getUrl(), new BitmapLoadCallBack<View>() {

			@Override
			public void onLoadCompleted(View view, String arg1, Bitmap bitmap,
					BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
				// TODO Auto-generated method stub
				((RelativeLayout)view.getTag()).setVisibility(View.GONE);
				((ImageView)view).setImageBitmap(bitmap);
			}

			@Override
			public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		container.addView(view);
		return view;
	}

}
