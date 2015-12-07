package com.meetu.fragment;


import cc.imeetu.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GuildFragment extends Fragment{
	ImageView guildImv;
	int imgs[] = {R.drawable.guidepage_1,R.drawable.guidepage_2,R.drawable.guidepage_3,R.drawable.guidepage_4};
	public static GuildFragment newInstance(int posiiton){
		GuildFragment fragment = new GuildFragment();
		Bundle arg = new Bundle();  
		arg.putInt("position", posiiton);  
		fragment.setArguments(arg);  
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.guild_fragment_layout, null);
		guildImv = (ImageView) view.findViewById(R.id.guild_imv);
		int sectionNum = getArguments().getInt("position");
		guildImv.setImageResource(imgs[sectionNum]);
		return view;
	}

}
