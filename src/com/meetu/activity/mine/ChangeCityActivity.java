package com.meetu.activity.mine;



import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.LogUtil.log;
import com.meetu.R;
import com.meetu.common.city.ArrayWheelAdapter;
import com.meetu.common.city.BaseActivity;
import com.meetu.common.city.OnWheelChangedListener;
import com.meetu.common.city.ShengshiquActivity;
import com.meetu.common.city.WheelView;
import com.meetu.entity.City;
import com.meetu.sqlite.CityDao;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeCityActivity extends BaseActivity implements OnClickListener, OnWheelChangedListener {
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private TextView mBtnConfirm;
	private String city;
	private RelativeLayout wanchLayout,backLayout;
	
	private CityDao cityDao=new CityDao();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
				super.requestWindowFeature(Window.FEATURE_NO_TITLE);
				//全屏
				super.getWindow();
		setContentView(R.layout.activity_change_city);
		city=super.getIntent().getStringExtra("hometown");
		
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
		mBtnConfirm = (TextView) findViewById(R.id.city_selector_shengshiqu_tv);
		wanchLayout=(RelativeLayout) super.findViewById(R.id.city_selector_shengshiqu_rl);
		wanchLayout.setOnClickListener(this);
		backLayout=(RelativeLayout) super.findViewById(R.id.back_changecity_mine_rl);
		backLayout.setOnClickListener(this);
	}
	
	private void setUpListener() {
		// 添加change事件
    	mViewProvince.addChangingListener(this);
    	// ���change�¼�
    	mViewCity.addChangingListener(this);
    	// ���change�¼�
    	mViewDistrict.addChangingListener(this);
    	// ���onclick�¼�
    	mBtnConfirm.setOnClickListener(this);
    }
	
	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(ChangeCityActivity.this, mProvinceDatas));
		
		Log.e("lucifer", "mProvinceDatas.length=="+mProvinceDatas.length);
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
//			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
//			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
			int pCurrent = mViewDistrict.getCurrentItem();
			List<City> townList=new ArrayList<City>();
			townList=cityDao.getAllTown(mCurrentProviceName, mCurrentCityName);
			String[] towns=new String[townList.size()];
			for(int i=0;i<townList.size();i++){
				towns[i]=townList.get(i).getTown();
			}
			mCurrentDistrictName=towns[pCurrent];
		}
	}
	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	@SuppressWarnings("unused")
	private void updateAreas() {
		City city=new City();
		List<City> provinceList = new ArrayList<City>();
		List<City> cityList=new ArrayList<City>();
		List<City> townList=new ArrayList<City>();
		
		int pCurrent = mViewCity.getCurrentItem();
		cityList=cityDao.getAllCity(mCurrentProviceName);
//		cityList=cityDao.getAllCity(mCurrentProviceName);
		String[] citys = new String[cityList.size()];
		
		for(int i=0;i<cityList.size();i++){
			citys[i]=cityList.get(i).getCity();
		}
		mCurrentCityName=cityList.get(pCurrent).getCity();
//		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		
		townList=cityDao.getAllTown(mCurrentProviceName, mCurrentCityName);
		Log.e("lucifer", "townList=="+townList.size());
		String[] areas = new String[townList.size()];
		
		for(int i=0;i<townList.size();i++){
			areas[i]=townList.get(i).getTown();
		}
		
//		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
		mCurrentDistrictName=areas[0];
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	@SuppressWarnings("unused")
	private void updateCities() {
		City city=new City();
		List<City> provinceList = new ArrayList<City>();
		List<City> cityList=new ArrayList<City>();
		List<City> townList=new ArrayList<City>();
		
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		Log.e("lucifer", "mCurrentProviceName=="+mCurrentProviceName);

		cityList=cityDao.getAllCity(mCurrentProviceName);
		String[] cities = new String[cityList.size()];
		for(int i=0;i<cityList.size();i++){
			cities[i]=cityList.get(i).getCity();
		}		
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}
//
//	/**
//	 * 根据当前的市，更新区WheelView的信息
//	 */
//	private void updateAreas() {
//		int pCurrent = mViewCity.getCurrentItem();
//		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
//		String[] areas = mDistrictDatasMap.get(mCurrentCityName);
//
//		if (areas == null) {
//			areas = new String[] { "" };
//		}
//		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
//		mViewDistrict.setCurrentItem(0);
//	}
//
//	/**
//	 * 根据当前的省，更新市WheelView的信息
//	 */
//	private void updateCities() {
//		int pCurrent = mViewProvince.getCurrentItem();
//		mCurrentProviceName = mProvinceDatas[pCurrent];
//		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
//		if (cities == null) {
//			cities = new String[] { "" };
//		}
//		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
//		mViewCity.setCurrentItem(0);
//		updateAreas();
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.city_selector_shengshiqu_rl:
			Intent intent=new Intent();
			intent.putExtra("city", mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
			setResult(4, intent);
			finish();
			log.e("lucifer", ""+mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
	//		showSelectedResult();
			break;
		case R.id.back_changecity_mine_rl:
			Intent intent2=new Intent();	
			intent2.putExtra("city", city);
			ChangeCityActivity.this.setResult(4,intent2);
			finish();
				break;
		default:
			break;
		}
	}

	/**
	 * 设置点击返回键的状态
	 */
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent=new Intent();	
		intent.putExtra("city", city);
		ChangeCityActivity.this.setResult(4,intent);
		finish();
	}
}



