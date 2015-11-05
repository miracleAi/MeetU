package com.meetu.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.R;
import com.meetu.activity.mine.ChangeSchoolActivity;
import com.meetu.activity.mine.UpdatepictureActivity;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.common.city.ArrayWheelAdapter;
import com.meetu.common.city.BaseActivity;
import com.meetu.common.city.OnWheelChangedListener;
import com.meetu.common.city.ShengshiquActivity;
import com.meetu.common.city.WheelView;
import com.meetu.entity.City;
import com.meetu.entity.Department;
import com.meetu.entity.Middle;
import com.meetu.entity.Schools;
import com.meetu.sqlite.CityDao;
import com.meetu.tools.BitmapCut;
import com.meetu.tools.StringToDate;

import android.R.integer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SetPersonalInformation2Activity extends BaseActivity implements OnClickListener,OnWheelChangedListener{
	private ImageView ivTouxiang,shangyiye,submit;
	private TextView etSchool,tvHome;
	private TextView major;
	//城市选择相关
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private TextView mBtnConfirm;

	private CityDao cityDao=new CityDao();
	
	private String uName,uSex,uBrith;
	
	// 上传 信息   头像相关
	private	String fHeadPath = "";
    private String yHeadPath = "";
    
    private String cityID;
    
    private String schoolId,departmentId;
    //生日
    private long birthLong;
    private String birthString;//时间戳字符串
    
    //
    private Department department;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//全屏
		super.getWindow();
		setContentView(R.layout.activity_shezhigerenxinxi2);
		//取得前边传过来的数据
		Intent intent = this.getIntent(); 
		
		uName=intent.getStringExtra("name");
		uSex=intent.getStringExtra("sex");
		uBrith=intent.getStringExtra("birth");
		//TODO  YOUWENTI
//		birthString=StringToDate.getTimea(uBrith);
		log.e("lucifer", "birthString=="+birthString);
		
		log.e("lucifer", "取到数据=uName"+uName+"uSex"+uSex+"uBrith"+uBrith);
		
//		//接收传递过来的对象
//				Intent intent2 = this.getIntent(); 
//				department=(Department) intent.getSerializableExtra("department");
		
		initView();
	}
	private void initView(){
		ivTouxiang=(ImageView) super.findViewById(R.id.iv_touxiang_shezhigerenxinxi2);
		Bitmap head=readHead();
		if(head!=null){
			
			fHeadPath = Environment.getExternalStorageDirectory()+"/f_user_header.png";
			yHeadPath = Environment.getExternalStorageDirectory()+"/user_header.png";
			
			ivTouxiang.setImageBitmap(head);
		}
		ivTouxiang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
		
		shangyiye=(ImageView) super.findViewById(R.id.self2_back_img);
		shangyiye.setOnClickListener(this);
		etSchool=(TextView) super.findViewById(R.id.self2_school_et);
		etSchool.setOnClickListener(this);
		tvHome=(TextView) super.findViewById(R.id.self2_home_et);
		tvHome.setOnClickListener(this);
		submit=(ImageView) super.findViewById(R.id.self2_submit_img);
		submit.setOnClickListener(this);
		major=(TextView) super.findViewById(R.id.self2_major_et);
		major.setOnClickListener(this);
		
	}
	

	
	public Bitmap readHead(){
		String file=Environment.getExternalStorageDirectory()+"/user_header.png";
		return BitmapFactory.decodeFile(file);
	}
	
	private void showDialog(){
		final  AlertDialog portraidlg=new AlertDialog.Builder(this).create();
		portraidlg.show();
		Window win=portraidlg.getWindow();
		win.setContentView(R.layout.dialog_show_photo);
		RadioButton portrait_native=(RadioButton)win.findViewById(R.id.Portrait_native);
		portrait_native.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent1=new Intent(Intent.ACTION_PICK,null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent1, 11);
				portraidlg.dismiss();
			}
		});
		RadioButton portrait_take=(RadioButton)win.findViewById(R.id.Portrait_take);
		portrait_take.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//调用摄像头
				Intent intent2=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
						"/user_header.png")));
				startActivityForResult(intent2, 22);
				portraidlg.dismiss();
			}
		});
		View viewTop=win.findViewById(R.id.view_top_dialog_sethead);
		View viewBottom=win.findViewById(R.id.view_bottom_dialog_sethead);
		//点击dialog外部，关闭dialog
				viewTop.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						portraidlg.dismiss();
					}
				});
				viewBottom.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						portraidlg.dismiss();
					}
				});
	
	}
	
	private Bitmap headerPortait;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 11:
			if(resultCode==this.RESULT_OK){
				cropPhoto(data.getData());//裁剪图片
			}
			break ;
		case 22:
			if(resultCode==this.RESULT_OK){
				File temp=new File(Environment.getExternalStorageDirectory()
						+ "/user_header.png");
				cropPhoto(Uri.fromFile(temp));//裁剪图片
				
			}
			
			break;
		case 33:
			if(data!=null){
				Bundle extras=data.getExtras();
				headerPortait=extras.getParcelable("data");
				if(headerPortait!=null){
					fHeadPath = saveHeadImg(headerPortait,false);
				}
				headerPortait=BitmapCut.toRoundBitmap(headerPortait);
				if(headerPortait!=null){
					yHeadPath = saveHeadImg(headerPortait,true);
					ivTouxiang.setImageBitmap(headerPortait);
				}
			}
			
			break;
			
		//接收传递回来的学校和专业信息
		case 10:
			Intent intent=getIntent();
			schoolId=intent.getStringExtra("school");
			departmentId=intent.getStringExtra("department");
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
//	public void saveHeadImg(Bitmap head){
//		FileOutputStream fos=null;
//		try {
//			fos=new FileOutputStream(new File(Environment.getExternalStorageDirectory()+"/user_header.png"));
//			head.compress(CompressFormat.PNG, 100, fos);
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			
//				try {
//					if(fos!=null)fos.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		}
//		
//		
//	}
	public String saveHeadImg(Bitmap head,boolean isY){
		FileOutputStream fos=null;
		String path = "";
		if(isY){
			path = Environment.getExternalStorageDirectory()+"/user_header.png";
		}else{
			path = Environment.getExternalStorageDirectory()+"/f_user_header.png";
		}
		try {
			fos=new FileOutputStream(new File(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		head.compress(CompressFormat.PNG, 100, fos);
		try {
			fos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;


	}
	
	/**
     * 调用拍照的裁剪功能
     * @param uri
     */
	
	public void cropPhoto(Uri uri){
		//调用拍照的裁剪功能
		Intent intent=new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		//aspectX aspectY 是宽和搞的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// // outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		intent.putExtra("return-data",true);
		startActivityForResult(intent,33);
	}
	
	private PopupWindow popupWindow;
	private void initPopupWindow() {
		if (popupWindow == null) {
			View view = LayoutInflater.from(this).inflate(R.layout.shengshiqu,
					null);		
			popupWindow = new PopupWindow(view,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			// 设置外观
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			ColorDrawable colorDrawable = new ColorDrawable();
			popupWindow.setBackgroundDrawable(colorDrawable);
		//	tvTitle=(TextView)view.findViewById(R.id.tvcolectList);
			
			
			mViewProvince = (WheelView) view.findViewById(R.id.id_province);
			mViewCity = (WheelView) view.findViewById(R.id.id_city);
			mViewDistrict = (WheelView) view.findViewById(R.id.id_district);
			mBtnConfirm = (TextView) view.findViewById(R.id.city_selector_shengshiqu_tv);
			
			// 添加change事件
	    	mViewProvince.addChangingListener(SetPersonalInformation2Activity.this);
	    	// 添加change事件
	    	mViewCity.addChangingListener(SetPersonalInformation2Activity.this);
	    	// 添加change事件
	    	mViewDistrict.addChangingListener(SetPersonalInformation2Activity.this);
	    	// 添加onclick事件
	    	mBtnConfirm.setOnClickListener(this);
	    	setUpData();
		}
		
	}
	
	private void setUpData() {
//		initProvinceDatas();
		initProvinceDatasNews();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(SetPersonalInformation2Activity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
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
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_touxiang_shezhigerenxinxi2:
			
			break;
		case R.id.self2_back_img:
			finish();
			break;
		case R.id.self2_school_et:
			
			Intent intent=new Intent(SetPersonalInformation2Activity.this,ChangeSchoolActivity.class);
			startActivityForResult(intent,10);	
			break;
		//点击城市
		case R.id.self2_home_et:
			initPopupWindow();
			popupWindow.showAsDropDown(tvHome, 0, 0);
			tvHome.setText(""+mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
			
			/**
			 * 提交数据
			 */
		case R.id.self2_submit_img:
			if(etSchool.getText().length()!=0&&tvHome.getText().length()!=0&&major.getText().length()!=0){
				Toast.makeText(SetPersonalInformation2Activity.this, "可以提交传送数据了", Toast.LENGTH_SHORT).show();
				
				//拿本地的  user 
				AVUser currentUser = AVUser.getCurrentUser();
				if (currentUser != null) {
					//强制类型转换
					ObjUser user = AVUser.cast(currentUser, ObjUser.class);
					completeInfo(user);
				}
			}
			break;
		case R.id.city_selector_shengshiqu_tv:
			popupWindow.dismiss();
			tvHome.setText(mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
			
			cityID=cityDao.getID(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName).get(0).getId();
			
			log.e("lucifer", "cityID=="+cityID);
			
			break;
		default:
			break;
		}
	}
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
			tvHome.setText(""+mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
		} else if (wheel == mViewCity) {
			updateAreas();
			tvHome.setText(""+mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
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
			
			tvHome.setText(""+mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
		}
	}
	
	AVFile fFile = null;
	AVFile yFile = null;
	
	//完善个人信息
		public void completeInfo(final ObjUser user){
			File upF1 = new File(yHeadPath);
			File upF2 = new File(fHeadPath);
			try {
				user.setNameNick(uName);
				if(uSex.equals("男生")){
					user.setGender(1);
				}else if(uSex.equals("女生")){
					user.setGender(2);
				}else{
					user.setGender(0);
				}
				//获取时间戳（单位毫秒）
//				user.setBirthday(Long.valueOf(birthString));
				//根据生日计算
				user.setConstellation("天枰座");
				user.setSchool("university");
				//学校编码，查询数据库
				user.setSchoolNum(1001);
				//学校所在地编码，查数据库
				user.setSchoolLocation(Long.valueOf(schoolId));
				//此处为专业分类名称
				user.setDepartment("computer");
				//专业分类编码，数据库查询
				user.setDepartmentId(Integer.valueOf(departmentId));
				user.setHometown(cityID);
				fFile = AVFile.withAbsoluteLocalPath(Constants.HEAD_FILE_RECT+user.getObjectId()+Constants.IMG_TYPE, fHeadPath);
				yFile = AVFile.withAbsoluteLocalPath(Constants.HEAD_FILE_CIRCLE+user.getObjectId()+Constants.IMG_TYPE, yHeadPath);
				fFile.saveInBackground(new SaveCallback() {

					@Override
					public void done(AVException e) {
						// TODO Auto-generated method stub
						if(e != null){
							return ;
						}
						user.setProfileClip(fFile);
						yFile.saveInBackground(new SaveCallback() {

							@Override
							public void done(AVException e) {
								// TODO Auto-generated method stub
								if(e != null){
									return ;
								}
								user.setProfileOrign(yFile);
								ObjUserWrap.completeUserInfo(user,new ObjFunBooleanCallback() {

									@Override
									public void callback(boolean result, AVException e) {
										// TODO Auto-generated method stub
										if(result){
								//			clickBtn.setText(LOAD_SUC);
											Toast.makeText(getApplicationContext(), "save suc", 1000).show();
										}else{
								//			clickBtn.setText(LOAD_FAIL);
											Toast.makeText(getApplicationContext(), "save fail", 1000).show();
										}
									}
								});

							}
						});
					}
				});
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	

}
