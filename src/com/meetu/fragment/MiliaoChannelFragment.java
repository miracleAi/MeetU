package com.meetu.fragment;

import net.tsz.afinal.FinalBitmap;

import com.avos.avoscloud.LogUtil.log;
import com.meetu.R;
import com.meetu.R.id;
import com.meetu.cloud.object.ObjChat;
import com.meetu.myapplication.MyApplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MiliaoChannelFragment extends Fragment {
	//控件相关
	private TextView dismissData;
	private ImageView photoManager,backgroud;
	private TextView titile;
	private TextView numberAll,numberFavor;
	
	//网络数据相关
	private ObjChat  objChat=new ObjChat();
	private FinalBitmap finalBitmap;
	
	
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(view==null){
			view=inflater.inflate(R.layout.fragment_miliao_channel, null);
			objChat=(ObjChat) getArguments().get("ObjChat");
			log.e("zcq","objChat=="+objChat.getObjectId());
			MyApplication app=(MyApplication) getActivity().getApplicationContext();
			finalBitmap=app.getFinalBitmap();
			
			initView();
			
		}
		ViewGroup parent=(ViewGroup) view.getParent();
		if(parent!=null){
			parent.removeView(view);
		}
		return view;
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		dismissData=(TextView) view.findViewById(R.id.date_top_miliao_channel_tv);
		photoManager=(ImageView) view.findViewById(R.id.photoHead_manage_miliao_channel_img);
		titile=(TextView) view.findViewById(R.id.content_center_miliao_channel_tv);
		numberAll=(TextView) view.findViewById(R.id.numberAll_miliao_channel);
		numberFavor=(TextView) view.findViewById(R.id.numberFavor_miliao_channel);
		backgroud=(ImageView) view.findViewById(R.id.backgroud_miliao_channel_img);
		finalBitmap.display(backgroud,objChat.getChatPicture().getUrl());
		titile.setText(""+objChat.getChatTitle());
		
	}

	

}
