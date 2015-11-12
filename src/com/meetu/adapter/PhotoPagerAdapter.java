package com.meetu.adapter;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.tsz.afinal.FinalBitmap;



import com.lidroid.xutils.BitmapUtils;
import com.meetu.R;
import com.meetu.activity.homepage.HomePageDetialActivity;
import com.meetu.cloud.object.ObjActivityCover;
import com.meetu.cloud.object.ObjActivityPhoto;
import com.meetu.entity.PhotoWall;
import com.meetu.entity.Photolunbo;
import com.meetu.myapplication.MyApplication;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoPagerAdapter extends PagerAdapter {
	private Context  mContext;
	private List<ObjActivityCover> Newslist;
	private BitmapUtils bitmapUtils;
	private FinalBitmap finalBitmap;
	
	public PhotoPagerAdapter(Context context, List<ObjActivityCover> list) {
		super();
		this.mContext = context;
		this.Newslist = list;
		bitmapUtils=new BitmapUtils(mContext);
		HomePageDetialActivity activity=(HomePageDetialActivity)context;
//		NewsApplication app=(NewsApplication)activity.getApplicationContext();
//		finalBitmap=app.getFinalBitmap();
		MyApplication app=(MyApplication) context.getApplicationContext();
		finalBitmap=app.getFinalBitmap();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int number=Integer.MAX_VALUE;
		return  Newslist.size() ;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view==object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		//super.destroyItem(container, position, object);r
		container.removeView((View)object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		ObjActivityCover item=Newslist.get(position % Newslist.size());
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_photolunbo_detial,null);
		ImageView img=(ImageView)view.findViewById(R.id.photo_lunbo_item_homepage_img);
//		finalBitmap.display(img,news.getImgurl());
//		img.setImageResource(item.getImg());
		
//		bitmapUtils.display(img, item.getCover().getUrl());
		finalBitmap.display(img, item.getCover().getUrl());
		//View view2=LayoutInflater.from(mContext).inflate(R.layout.photo_desc_item_layout, null);
		
//		TextView title=(TextView)view.findViewById(R.id.title_lunbo_item_homepage_tv);
//		title.setText(item.getTitle());
		container.addView(view);
		return view;
	}
	

	

}
