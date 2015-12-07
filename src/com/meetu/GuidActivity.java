package com.meetu;


import cc.imeetu.R;

import com.meetu.activity.LoginOrRegisterActivity;
import com.meetu.adapter.GuildAdapter;
import com.meetu.fragment.GuildFragment;
import com.meetu.view.PageIndicator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class GuidActivity extends FragmentActivity{
	GuildFragment guildFragment;
	ViewPager guildPager;
	PageIndicator indicator;
	Button goMainBtn;
	int guildCount = 4;
	GuildAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guild_acty_layout);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		guildPager = (ViewPager) findViewById(R.id.guild_pager);
		indicator = (PageIndicator) findViewById(R.id.indicator);
		goMainBtn = (Button) findViewById(R.id.go_btn);
		
		adapter = new GuildAdapter(getSupportFragmentManager(),guildCount);
		guildPager.setAdapter(adapter);
		indicator.setViewPager(guildPager);
		
		indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(position == 3){
                	goMainBtn.setVisibility(View.VISIBLE);
                	goMainBtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							SharedPreferences sp=GuidActivity.this.getSharedPreferences("app_param", Context.MODE_PRIVATE);
							sp.edit().putInt("user", 1).commit();
							Intent intent = new Intent(GuidActivity.this,LoginOrRegisterActivity.class);
							startActivity(intent);
							finish();
						}
					});
                }else{
                	goMainBtn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
		
	}

}
