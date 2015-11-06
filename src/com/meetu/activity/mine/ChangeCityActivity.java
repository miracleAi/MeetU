package com.meetu.activity.mine;



import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.baidu.location.e.n;
import com.meetu.R;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;
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
	
	//控件相关
	private TextView cityName;
	
	//网络数据 相关
		//拿本地的  user 
		private AVUser currentUser = AVUser.getCurrentUser();
		private ObjUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//全屏
		super.getWindow();
		setContentView(R.layout.activity_change_city);
		initView();
		city=super.getIntent().getStringExtra("hometown");
		
		//拿到本地的缓存对象  user
		if(currentUser!=null){
			//强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		
		setUpViews();
		setUpListener();
		setUpData();
		cityName.setText(""+mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
	}

	private void initView() {
		// TODO Auto-generated method stub
		cityName=(TextView) super.findViewById(R.id.cityName_change_city_tv);
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
//		initProvinceDatas();
		initProvinceDatasNews();
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
			cityName.setText(""+mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
		} else if (wheel == mViewCity) {
			updateAreas();
			cityName.setText(""+mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
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
			
			cityName.setText(""+mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
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


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.city_selector_shengshiqu_rl:
			completeInfo(user);

			break;
		case R.id.back_changecity_mine_rl:
			Intent intent2=new Intent();	
			intent2.putExtra("city", city);
			ChangeCityActivity.this.setResult(RESULT_CANCELED,intent2);
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
		ChangeCityActivity.this.setResult(RESULT_CANCELED,intent);
		finish();
	}
	
	/**
	 * 上传 修改信息 
	 * @param user  
	 * @author lucifer
	 * @date 2015-11-6
	 */
	public void completeInfo(final ObjUser user){
		
		String cityID=cityDao.getID(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName).get(0).getId();
		if(cityID!=null){
			user.setHometown(cityID);

			//只上传信息
			ObjUserWrap.completeUserInfo(user,new ObjFunBooleanCallback() {

				@Override
				public void callback(boolean result, AVException e) {			
					if(result){					
						Toast.makeText(getApplicationContext(), "家乡修改成功", 1000).show();
						Intent intent=new Intent();
						intent.putExtra("city", mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
						setResult(RESULT_OK, intent);
						finish();
						log.e("lucifer", ""+mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
									
					}else{
						Toast.makeText(getApplicationContext(), "家乡修改失败", 1000).show();
					}
				}
			});
		}else{
			Toast.makeText(getApplicationContext(), "修改不成功", 1000).show();
		}
	
	}
}



