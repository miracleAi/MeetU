package com.meetu.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

public class BoardPageFragmentAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragmentList;
	private FragmentManager fm;

	public BoardPageFragmentAdapter(FragmentManager fm,
			List<Fragment> fragmentList) {
		super(fm);
		this.fm = fm;
		// TODO Auto-generated constructor stub
		this.fragmentList = fragmentList;
	}


	@Override
	public Fragment getItem(int idx) {
		// TODO Auto-generated method stub
		return fragmentList.get(idx);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragmentList.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE; // 没有找到child要求重新加载
	}

	public void setFragments(List<Fragment> fragments) {
		if (this.fragmentList != null) {
			FragmentTransaction ft = fm.beginTransaction();
			for (Fragment f : this.fragmentList) {
				ft.remove(f);
			}
			ft.commit();
			ft = null;
			fm.executePendingTransactions();
		}
		this.fragmentList = fragments;
		notifyDataSetChanged();
	}
}
