package com.meetu.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import android.R.string;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;



import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogUtil.log;

import com.meetu.R;
import com.meetu.activity.miliao.ChatGroupActivity;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.entity.Chatmsgs;
import com.meetu.entity.Huodong;
import com.meetu.myapplication.MyApplication;
import com.meetu.tools.BitmapCut;
import com.meetu.tools.DateUtils;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;



@SuppressLint("NewApi")
public class ChatmsgsListViewAdapter  extends BaseAdapter implements OnClickListener {

	private Context mContext;
	private List<Chatmsgs> chatmsgsList;
	private SimpleDateFormat sd;
	
	private int mMaxItemWidth;//item最大宽度
	private int mMinItemWidth;//item最小宽度
	
	//TODO 暂时只测试4种状态  ,但是要进一步判断消息发送的方向
	private final int TYPE_COUNT=13;
	
	//网络相关
	
	private ObjUser user=null;
	FinalBitmap finalBitmap;
	
	public ChatmsgsListViewAdapter(Context context,List<Chatmsgs> chatmsgsList){
		this.mContext=context;
		this.chatmsgsList=chatmsgsList;
		
		
		mMaxItemWidth=DisplayUtils.getWindowWidth(mContext)-DensityUtil.dip2px(mContext, 110);
		mMinItemWidth=DensityUtil.dip2px(mContext, 44);
		
		MyApplication app=(MyApplication) context.getApplicationContext();
		finalBitmap=app.getFinalBitmap();
	}
	/**
	 * 消息状态 style
	 *  10  我发的文本消息  12接收的文本消息  11 我发的图片的消息  13接收的图片消息   2新人加入消息 3普通通知消息  4活动咨询反馈通知消息 5
	 *  消息发送方向 
	 * 
	 */
	@Override
	public int getItemViewType(int position) {
		
		// TODO Auto-generated method stub
		return chatmsgsList!=null?chatmsgsList.get(position).getChatMsgType():-1;
	}
	/**
	 * 
	 */
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return TYPE_COUNT;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.d("lucifer","getCount()");
//		return newsList.size();
		return chatmsgsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Log.d("lucifer","getItem()");
		return chatmsgsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		Log.d("lucifer","getItemId()");
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		final ViewHolder holder;
		Chatmsgs item=chatmsgsList.get(position);
		
		if(convertView==null){
		holder=new ViewHolder();
		log.e("zcq", "item.getChatMsgType()=="+item.getChatMsgType());
		log.e("zcq", "item.getClientId()=="+item.getClientId());
			switch (item.getChatMsgType()) {
			
			case 10:					
				convertView=LayoutInflater.from(mContext).inflate(R.layout.chat_item_text_right, null);
				holder.content=(TextView) convertView.findViewById(R.id.content_chat_item_right_tv);				
				holder.time=(TextView) convertView.findViewById(R.id.time_chat_item_right_tv);
				holder.timeLayout=(RelativeLayout) convertView.findViewById(R.id.time_chat_item_right_rl);
				holder.userHeadPhoto=(ImageView) convertView.findViewById(R.id.userHead_chat_item_right_img);
				holder.userName=(TextView) convertView.findViewById(R.id.userName_chat_item_tv);
				holder.failPhoto=(ImageView) convertView.findViewById(R.id.fail_chat_item_text_img);
				break;								
			case 12:
				convertView=LayoutInflater.from(mContext).inflate(R.layout.chat_item_text_left, null);
				holder.content=(TextView) convertView.findViewById(R.id.content_chat_item_left_tv);
				holder.time=(TextView) convertView.findViewById(R.id.time_chat_item_left_tv);
				holder.timeLayout=(RelativeLayout) convertView.findViewById(R.id.time_chat_item_left_rl);
				
				holder.userHeadPhoto=(ImageView) convertView.findViewById(R.id.userHead_chat_item_left_img);
				holder.userName=(TextView) convertView.findViewById(R.id.userName_chat_item_left_tv);
				holder.failPhoto=(ImageView) convertView.findViewById(R.id.fail_chat_item_text_left);
				break;
			case 11:
				convertView=LayoutInflater.from(mContext).inflate(R.layout.chat_item_photo_right, null);
				holder.photo=(ImageView) convertView.findViewById(R.id.photo_chat_item_right_img);
				holder.time=(TextView) convertView.findViewById(R.id.time_photochat_item_right_tv);
				holder.timeLayout=(RelativeLayout) convertView.findViewById(R.id.time_photochat_item_right_rl);
				break;
			case 13:
				convertView=LayoutInflater.from(mContext).inflate(R.layout.chat_item_photo_left, null);
				holder.photo=(ImageView) convertView.findViewById(R.id.photo_chat_item_left_img);
				holder.time=(TextView) convertView.findViewById(R.id.time_photochat_item_left_tv);
				holder.timeLayout=(RelativeLayout) convertView.findViewById(R.id.time_photochat_item_left_rl);
				break;
			default:
				break;
			}

			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		user=new ObjUser();
		
		
		switch (item.getChatMsgType()) {
			case 10:
			case 12:
				SpannableString spannableString = ChatGroupActivity.getExpressionString(mContext, item.getContent());
				holder.content.setMaxWidth(mMaxItemWidth);
				holder.content.setMinWidth(mMinItemWidth);
				holder.content.setText(spannableString);				
				if(item.getIsShowTime()==1){
					long time=Long.parseLong(item.getSendTimeStamp());
					Date date=new Date(time);
					sd=new SimpleDateFormat("MM-dd HH:mm");
					String string= sd.format(date);
//					log.e(""+date+", "+sd.format(date)+", time=="+item.getSendTimeStamp());
					holder.time.setText(string);
					holder.timeLayout.setVisibility(View.VISIBLE);
				}
				if(item.getIsShowTime()==0){
					holder.timeLayout.setVisibility(View.GONE);
				}
				ObjUserWrap.getObjUser(item.getClientId(), new ObjUserInfoCallback() {
					
					@Override
					public void callback(ObjUser user, AVException e) {
						// TODO Auto-generated method stub
						if(e!=null){
							log.e("zcq", e);
							
						}else if(user!=null){
							
							holder.userName.setText(user.getNameNick());
							finalBitmap.display(holder.userHeadPhoto, user.getProfileClip().getUrl());
							
						}else{
							log.e("zcq", "个人信息获取失败");
						}
					}
				});
					
				break;
		
				
			case 11:	
		
				String fileName=item.getImgMsgImageUrl();
				log.e("lucifer1",fileName.toString());

				BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
				Bitmap bmp = BitmapFactory.decodeFile(fileName, options);

				 Bitmap bbmp=BitmapCut.getRadiusBitmap(bmp);
				holder.photo.setImageBitmap(bbmp);			
				if(item.getIsShowTime()==1){
					long time=Long.parseLong(item.getSendTimeStamp());
					Date date=new Date(time);
					sd=new SimpleDateFormat("MM-dd HH:mm");
					String string= sd.format(date);
//					log.e(""+date+", "+sd.format(date)+", time=="+item.getSendTimeStamp());
					holder.time.setText(string);
					holder.timeLayout.setVisibility(View.VISIBLE);
				}
				if(item.getIsShowTime()==0){
					holder.timeLayout.setVisibility(View.GONE);
				}

				break;
			case 13:
				finalBitmap.display(holder.photo, item.getImgMsgImageUrl());
				if(item.getIsShowTime()==1){
					long time=Long.parseLong(item.getSendTimeStamp());
					Date date=new Date(time);
					sd=new SimpleDateFormat("MM-dd HH:mm");
					String string= sd.format(date);
//					log.e(""+date+", "+sd.format(date)+", time=="+item.getSendTimeStamp());
					holder.time.setText(string);
					holder.timeLayout.setVisibility(View.VISIBLE);
				}
				if(item.getIsShowTime()==0){
					holder.timeLayout.setVisibility(View.GONE);
				}
				
				
				break;
		
		}
		
		Log.d("lucifer","getView()");
		
		
		
	
		
		return convertView;
	}
	
	
	private class ViewHolder{
		private TextView content;
		private ImageView photo;
		
		private TextView time;
		private RelativeLayout timeLayout;
		
		private ImageView userHeadPhoto;
		private TextView userName;
		private ImageView failPhoto;
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	



}
