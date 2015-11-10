package com.meetu.adapter;

import java.util.List;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.lidroid.xutils.BitmapUtils;
import com.meetu.R;
import com.meetu.MainActivity;
import com.meetu.activity.mine.FavorListActivity;
import com.meetu.activity.mine.MinephotoActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.entity.PhotoWall;
import com.meetu.myapplication.MyApplication;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;

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



public class MinePhotoAdapter extends PagerAdapter{
	private Context  mContext;
	private List<ObjUserPhoto> Newslist;
	
	//网络数据相关
	
		private BitmapUtils bitmapUtils; 
		private String photoUrl;
		
		private AVUser currentUser = AVUser.getCurrentUser();
		 //当前用户
		private ObjUser user = new ObjUser();	
	
	public MinePhotoAdapter(Context context, List<ObjUserPhoto> list) {
		super();
		this.mContext = context;
		this.Newslist = list;
		bitmapUtils=new BitmapUtils(mContext);
		MinephotoActivity activity=(MinephotoActivity)context;
//		NewsApplication app=(NewsApplication)activity.getApplicationContext();
//		finalBitmap=app.getFinalBitmap();
		if (currentUser != null) {
			//强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}
	}

	@Override
	public int getCount() {
		
		return Newslist.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		
		return view==object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		
		container.removeView((View)object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		ObjUserPhoto item=Newslist.get(position);
		
		log.e("zcq",""+photoUrl);
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_minephoto_viewpager,null);
		
		TextView name=(TextView) view.findViewById(R.id.name_mine_photoview_fullscreen);
		name.setText(""+user.getNameNick());
		TextView desc=(TextView) view.findViewById(R.id.desc_item_minephoto_tv);
		desc.setText(""+item.getPhotoDescription());
		TextView favorNumber=(TextView) view.findViewById(R.id.favorNumber_item_minephoto);
		favorNumber.setText(""+item.getPraiseCount());
		ImageView img=(ImageView)view.findViewById(R.id.photo_demail_mine);
		
		RelativeLayout.LayoutParams params=(LayoutParams) img.getLayoutParams();
	//	params.width=DisplayUtils.getWindowWidth(mContext);
		params.width=1200;
		img.setLayoutParams(params);
		
		photoUrl=item.getPhoto().getUrl();
		
		bitmapUtils.display(img, photoUrl);
		ImageView photoHead=(ImageView) view.findViewById(R.id.nameheader_mine_photoview_fullscreen_img);
		bitmapUtils.display(photoHead, user.getProfileClip().getUrl());
		/**
		 * viewpager 内部事件监听处理
		 */
		ImageView headphoto=(ImageView) view.findViewById(R.id.nameheader_mine_photoview_fullscreen_img);
		headphoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				Intent intent=new Intent(mContext,MainActivity.class);
//				intent.putExtra("page", 3);
//				mContext.startActivity(intent);
				((Activity) mContext).finish();
				
				
			}
		});
		RelativeLayout favorLayout=(RelativeLayout) view.findViewById(R.id.favor_minephoto_mine_rl);
		favorLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Log.e("1", "1111111");
				Intent intent=new Intent(mContext,FavorListActivity.class);
				mContext.startActivity(intent);
				
				
			}
		});
		
		
		//View view2=LayoutInflater.from(mContext).inflate(R.layout.photo_desc_item_layout, null);
		
//		TextView desc=(TextView)view.findViewById(R.id.tv_content_photo_item_desc);
//		desc.setText(news.getNote());
		container.addView(view);
		return view;
	}

	

}
