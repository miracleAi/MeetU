package com.meetu.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.LogUtil.log;
import com.baidu.platform.comapi.map.y;
import com.meetu.MainActivity;
import com.meetu.activity.mine.ChangeSchoolActivity;
import com.meetu.activity.mine.UpdatepictureActivity;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.common.SchoolDao;
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
import com.meetu.tools.DateUtils;
import com.meetu.tools.StringToDate;

import android.R.integer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

public class SetPersonalInformation2Activity extends BaseActivity implements
		OnClickListener, OnWheelChangedListener {
	// 控件相关
	private ImageView ivTouxiang, shangyiye, submit;
	private TextView etSchool, tvHome;
	private TextView major;

	private ImageView ivman_selector, ivwoman_selector;
	protected TextView sex, birthday;
	protected EditText username;
	// 城市选择相关
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private TextView mBtnConfirm;

	private CityDao cityDao = new CityDao();
	private SchoolDao schoolDao = new SchoolDao();

	// private String uName,uSex,uBrith;

	// 上传 信息 头像相关
	private String fHeadPath = "";
	private String yHeadPath = "";
	// 家乡
	private String cityID;
	// 学校
	private String schoolId = "", departmentId = "";
	private String schoolName, departmentName;
	// 生日
	protected long birthLong;
	protected String birthString;// 时间戳字符串
	// 名字
	private String uName, uSex;

	// 性别
	//
	private Department department;

	private Boolean isShowTwo = false;// 是否显示第二个view 学校家乡那个
	private LinearLayout oneViewLayout, towViewLayout;
	private Boolean isUpHead = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_shezhigerenxinxi2);
		// 取得前边传过来的数据
		Intent intent = this.getIntent();
		//
		// uName=intent.getStringExtra("name");
		// uSex=intent.getStringExtra("sex");
		// uBrith=intent.getStringExtra("birth");

		// TODO YOUWENTI

		log.e("lucifer", "birthString==" + birthString);

		initView();
		// if(schoolId==null){
		// oneViewLayout.setVisibility(View.VISIBLE);
		// towViewLayout.setVisibility(View.GONE);
		// isShowTwo=false;
		// }else{
		// oneViewLayout.setVisibility(View.GONE);
		// towViewLayout.setVisibility(View.VISIBLE);
		// isShowTwo=true;
		//
		// }
	}

	private void initView() {
		ivTouxiang = (ImageView) super
				.findViewById(R.id.iv_touxiang_shezhigerenxinxi2);
		Bitmap head = readHead();
		if (head != null) {

			fHeadPath = Environment.getExternalStorageDirectory()
					+ "/f_user_header.png";
			yHeadPath = Environment.getExternalStorageDirectory()
					+ "/user_header.png";

			ivTouxiang.setImageBitmap(head);
		}
		ivTouxiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});

		shangyiye = (ImageView) super.findViewById(R.id.self2_back_img);
		shangyiye.setOnClickListener(this);
		etSchool = (TextView) super.findViewById(R.id.self2_school_et);
		etSchool.setOnClickListener(this);
		tvHome = (TextView) super.findViewById(R.id.self2_home_et);
		tvHome.setOnClickListener(this);
		submit = (ImageView) super.findViewById(R.id.self2_submit_img);
		submit.setOnClickListener(this);
		major = (TextView) super.findViewById(R.id.self2_major_et);
		major.setOnClickListener(this);

		// 第二个

		username = (EditText) super.findViewById(R.id.selfinfo1_nickname_et);
		sex = (TextView) super.findViewById(R.id.selfinfo1_sex_et);
		sex.setOnClickListener(this);
		birthday = (TextView) super.findViewById(R.id.selfinfo_birth_et);
		birthday.setOnClickListener(this);

		oneViewLayout = (LinearLayout) super
				.findViewById(R.id.one_settingInfo_ll);
		towViewLayout = (LinearLayout) super
				.findViewById(R.id.two_settingInfo_ll);

	}

	public Bitmap readHead() {
		String file = Environment.getExternalStorageDirectory()
				+ "/user_header.png";
		return BitmapFactory.decodeFile(file);
	}

	private void showDialog() {
		final AlertDialog portraidlg = new AlertDialog.Builder(this).create();
		portraidlg.show();
		Window win = portraidlg.getWindow();
		win.setContentView(R.layout.dialog_show_photo);
		RadioButton portrait_native = (RadioButton) win
				.findViewById(R.id.Portrait_native);
		portrait_native.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent1, 11);
				portraidlg.dismiss();
			}
		});
		RadioButton portrait_take = (RadioButton) win
				.findViewById(R.id.Portrait_take);
		portrait_take.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 调用摄像头
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory(),
								"/user_header.png")));
				startActivityForResult(intent2, 22);
				portraidlg.dismiss();
			}
		});
		View viewTop = win.findViewById(R.id.view_top_dialog_sethead);
		View viewBottom = win.findViewById(R.id.view_bottom_dialog_sethead);
		// 点击dialog外部，关闭dialog
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
			if (resultCode == this.RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		case 22:
			if (resultCode == this.RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/user_header.png");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片

			}

			break;
		case 33:
			if (data != null) {
				Bundle extras = data.getExtras();
				headerPortait = extras.getParcelable("data");
				if (headerPortait != null) {
					fHeadPath = saveHeadImg(headerPortait, false);
				}
				headerPortait = BitmapCut.toRoundBitmap(headerPortait);
				if (headerPortait != null) {
					yHeadPath = saveHeadImg(headerPortait, true);
					ivTouxiang.setImageBitmap(headerPortait);
				}
				isUpHead = true;
			}

			break;

		// 拿到返回的学校专业信息
		case 10:
			if (resultCode == RESULT_OK) {
				schoolId = data.getStringExtra("schools");
				departmentId = data.getStringExtra("departments");

				log.e("lucifer", "schoolId==" + schoolId + "departmentId="
						+ departmentId);

				schoolName = schoolDao.getschoolName(schoolId).get(0)
						.getUnivsNameString();
				departmentName = schoolDao
						.getDepartmentsName(schoolId, departmentId).get(0)
						.getDepartmentName();
				etSchool.setText(schoolName);
				major.setText(departmentName);
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public String saveHeadImg(Bitmap head, boolean isY) {
		FileOutputStream fos = null;
		String path = "";
		if (isY) {
			path = Environment.getExternalStorageDirectory()
					+ "/user_header.png";
		} else {
			path = Environment.getExternalStorageDirectory()
					+ "/f_user_header.png";
		}
		try {
			fos = new FileOutputStream(new File(path));
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
	 * 
	 * @param uri
	 */

	public void cropPhoto(Uri uri) {
		// 调用拍照的裁剪功能
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽和搞的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// // outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 33);
	}

	private PopupWindow popupWindowCity;

	private void initPopupWindowCity() {
		if (popupWindowCity == null) {
			View view = LayoutInflater.from(this).inflate(R.layout.shengshiqu,
					null);
			popupWindowCity = new PopupWindow(view,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			// 设置外观
			popupWindowCity.setFocusable(true);
			popupWindowCity.setOutsideTouchable(true);
			ColorDrawable colorDrawable = new ColorDrawable();
			popupWindowCity.setBackgroundDrawable(colorDrawable);
			// tvTitle=(TextView)view.findViewById(R.id.tvcolectList);

			mViewProvince = (WheelView) view.findViewById(R.id.id_province);
			mViewCity = (WheelView) view.findViewById(R.id.id_city);
			mViewDistrict = (WheelView) view.findViewById(R.id.id_district);
			mBtnConfirm = (TextView) view
					.findViewById(R.id.city_selector_shengshiqu_tv);

			// 添加change事件
			mViewProvince
					.addChangingListener(SetPersonalInformation2Activity.this);
			// 添加change事件
			mViewCity.addChangingListener(SetPersonalInformation2Activity.this);
			// 添加change事件
			mViewDistrict
					.addChangingListener(SetPersonalInformation2Activity.this);
			// 添加onclick事件
			mBtnConfirm.setOnClickListener(this);
			setUpData();
		}

	}

	private void setUpData() {
		// initProvinceDatas();
		initProvinceDatasNews();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				SetPersonalInformation2Activity.this, mProvinceDatas));
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
		City city = new City();
		List<City> provinceList = new ArrayList<City>();
		List<City> cityList = new ArrayList<City>();
		List<City> townList = new ArrayList<City>();

		int pCurrent = mViewCity.getCurrentItem();
		cityList = cityDao.getAllCity(mCurrentProviceName);
		// cityList=cityDao.getAllCity(mCurrentProviceName);
		String[] citys = new String[cityList.size()];

		for (int i = 0; i < cityList.size(); i++) {
			citys[i] = cityList.get(i).getCity();
		}
		mCurrentCityName = cityList.get(pCurrent).getCity();
		// mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];

		townList = cityDao.getAllTown(mCurrentProviceName, mCurrentCityName);
		Log.e("lucifer", "townList==" + townList.size());
		String[] areas = new String[townList.size()];

		for (int i = 0; i < townList.size(); i++) {
			areas[i] = townList.get(i).getTown();
		}

		// String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
		mCurrentDistrictName = areas[0];
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	@SuppressWarnings("unused")
	private void updateCities() {
		City city = new City();
		List<City> provinceList = new ArrayList<City>();
		List<City> cityList = new ArrayList<City>();
		List<City> townList = new ArrayList<City>();

		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		Log.e("lucifer", "mCurrentProviceName==" + mCurrentProviceName);

		cityList = cityDao.getAllCity(mCurrentProviceName);
		String[] cities = new String[cityList.size()];
		for (int i = 0; i < cityList.size(); i++) {
			cities[i] = cityList.get(i).getCity();
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
			if (isShowTwo == true) {
				towViewLayout.setVisibility(View.GONE);
				oneViewLayout.setVisibility(View.VISIBLE);
				isShowTwo = false;
			} else {
				// 销毁
				finish();
			}

			break;
		case R.id.self2_school_et:

			Intent intent = new Intent(SetPersonalInformation2Activity.this,
					ChangeSchoolActivity.class);
			startActivityForResult(intent, 10);
			break;
		// 点击城市
		case R.id.self2_home_et:
			initPopupWindowCity();
			popupWindowCity.showAsDropDown(tvHome, 0, 0);
			tvHome.setText("" + mCurrentProviceName + mCurrentCityName
					+ mCurrentDistrictName);

			break;
		/**
		 * 点击 下一页 右
		 */
		case R.id.self2_submit_img:
			if (isShowTwo == false) {
				uName = username.getText().toString();
				uSex = sex.getText().toString();
				birthLong = DateUtils.getStringToDate(birthday.getText()
						.toString());

				oneViewLayout.setVisibility(View.GONE);
				towViewLayout.setVisibility(View.VISIBLE);
				isShowTwo = true;
			} else {

				if (etSchool.getText().length() != 0
						&& tvHome.getText().length() != 0
						&& major.getText().length() != 0) {
					Toast.makeText(SetPersonalInformation2Activity.this,
							"正在上传信息", Toast.LENGTH_SHORT).show();

					// 拿本地的 user
					AVUser currentUser = AVUser.getCurrentUser();
					if (currentUser != null) {
						// 强制类型转换
						ObjUser user = AVUser.cast(currentUser, ObjUser.class);
						completeInfo(user);
					}
				}
			}

			break;
		case R.id.city_selector_shengshiqu_tv:
			popupWindowCity.dismiss();
			tvHome.setText(mCurrentProviceName + mCurrentCityName
					+ mCurrentDistrictName);

			cityID = cityDao
					.getID(mCurrentProviceName, mCurrentCityName,
							mCurrentDistrictName).get(0).getId();

			log.e("lucifer", "cityID==" + cityID);

			break;

		// 点击事件搬迁

		case R.id.selfinfo1_sex_et:
			initPopupWindowSex();
			popupWindowSex.showAsDropDown(v, 72, 400);
			break;
		case R.id.iv_man_sexselector:

			// ivman_selector.setImageResource(R.drawable.complete_gender_ms_720);
			popupWindowSex.dismiss();
			sex.setText("男生");
			break;
		case R.id.iv_woman_sexselector:
			// ivwoman_selector.setImageResource(R.drawable.complete_gender_fs_720);
			popupWindowSex.dismiss();
			sex.setText("女生");

			break;
		case R.id.selfinfo_birth_et:
			showDialog(1);
			break;
		default:
			break;
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
			tvHome.setText("" + mCurrentProviceName + mCurrentCityName
					+ mCurrentDistrictName);
		} else if (wheel == mViewCity) {
			updateAreas();
			tvHome.setText("" + mCurrentProviceName + mCurrentCityName
					+ mCurrentDistrictName);
		} else if (wheel == mViewDistrict) {
			// mCurrentDistrictName =
			// mDistrictDatasMap.get(mCurrentCityName)[newValue];
			// mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
			int pCurrent = mViewDistrict.getCurrentItem();
			List<City> townList = new ArrayList<City>();
			townList = cityDao
					.getAllTown(mCurrentProviceName, mCurrentCityName);
			String[] towns = new String[townList.size()];
			for (int i = 0; i < townList.size(); i++) {
				towns[i] = townList.get(i).getTown();
			}
			mCurrentDistrictName = towns[pCurrent];

			tvHome.setText("" + mCurrentProviceName + mCurrentCityName
					+ mCurrentDistrictName);
		}
	}

	private PopupWindow popupWindowSex;

	private void initPopupWindowSex() {
		if (popupWindowSex == null) {
			View view = LayoutInflater.from(this).inflate(
					R.layout.dialog_sex_selector, null);
			popupWindowSex = new PopupWindow(view,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			// 设置外观
			popupWindowSex.setFocusable(true);
			popupWindowSex.setOutsideTouchable(true);
			ColorDrawable colorDrawable = new ColorDrawable();
			popupWindowSex.setBackgroundDrawable(colorDrawable);
			// tvTitle=(TextView)view.findViewById(R.id.tvcolectList);
			ivman_selector = (ImageView) view
					.findViewById(R.id.iv_man_sexselector);
			ivwoman_selector = (ImageView) view
					.findViewById(R.id.iv_woman_sexselector);
			ivman_selector.setOnClickListener(this);
			ivwoman_selector.setOnClickListener(this);
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {

		Time time = new Time("GMT+8");
		time.setToNow();
		int year = time.year;
		int month = time.month;
		int day = time.monthDay;
		// TODO Auto-generated method stub

		return new DatePickerDialog(this, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker arg0, int year, int month, int day) {
				// TODO Auto-generated method stub
				log.e("lucifer", "DatePicker==" + arg0);
				birthday.setText(year + "-" + (month + 1) + "-" + day);

				// 转成时间戳

				birthLong = DateUtils.getStringToDate(birthday.getText()
						.toString());
				log.e("lucifer", "birthLong==" + birthLong);
			}
		}, year, month, day);

	}

	AVFile fFile = null;// 裁剪后图片
	AVFile yFile = null;// 原图

	/**
	 * 完善个人信息 上传信息 和头像
	 * 
	 * @param user
	 * @author lucifer
	 * @date 2015-11-6
	 */
	public void completeInfo(final ObjUser user) {
		File upF1 = new File(yHeadPath);
		File upF2 = new File(fHeadPath);

		log.e("lucifer", "uName==" + uName + " uSex==" + uSex + " birthday ="
				+ birthLong);
		log.e("lucifer", "schoolID==" + schoolId + " departmentId== "
				+ departmentId);
		try {
			user.setNameNick(uName);
			if (uSex.equals("男生")) {
				user.setGender(1);
			} else if (uSex.equals("女生")) {
				user.setGender(2);
			} else {
				user.setGender(0);
			}
			// 获取时间戳（单位毫秒）
			user.setBirthday(birthLong);
			log.e("lucifer", "birthLong==up==" + birthLong);
			// 根据生日计算
			user.setConstellation("白羊座");
			user.setSchool(schoolName);
			// 学校编码，查询数据库
			user.setSchoolNum(1001);
			// 学校所在地编码，查数据库
			user.setSchoolLocation(Long.valueOf(schoolId));
			// 此处为专业分类名称
			user.setDepartment(departmentName);
			// 专业分类编码，数据库查询
			user.setDepartmentId(Integer.valueOf(departmentId));
			user.setHometown(cityID);
			user.setIsCompleteUserInfo(true);
			yFile = AVFile.withAbsoluteLocalPath(Constants.HEAD_FILE_RECT
					+ user.getObjectId() + Constants.IMG_TYPE, fHeadPath);
			fFile = AVFile.withAbsoluteLocalPath(Constants.HEAD_FILE_CIRCLE
					+ user.getObjectId() + Constants.IMG_TYPE, yHeadPath);

			if (isUpHead == true) {
				// 上传头像 和 个人信息
				fFile.saveInBackground(new SaveCallback() {

					@Override
					public void done(AVException e) {
						// TODO Auto-generated method stub
						if (e != null) {
							return;
						}
						user.setProfileClip(fFile);
						yFile.saveInBackground(new SaveCallback() {

							@Override
							public void done(AVException e) {
								// TODO Auto-generated method stub
								if (e != null) {
									return;
								}
								user.setProfileOrign(yFile);
								ObjUserWrap.completeUserInfo(user,
										new ObjFunBooleanCallback() {

											@Override
											public void callback(
													boolean result,
													AVException e) {
												// TODO Auto-generated method
												// stub
												if (result) {
													// clickBtn.setText(LOAD_SUC);

													Toast.makeText(
															getApplicationContext(),
															"save 上传成功", 1000)
															.show();
													Intent intent = new Intent(
															SetPersonalInformation2Activity.this,
															MainActivity.class);
													startActivity(intent);
												} else {
													// clickBtn.setText(LOAD_FAIL);
													Toast.makeText(
															getApplicationContext(),
															"save 上传失败", 1000)
															.show();
												}
											}
										});

							}
						});
					}
				});

			} else {
				// 只上传信息
				ObjUserWrap.completeUserInfo(user, new ObjFunBooleanCallback() {

					@Override
					public void callback(boolean result, AVException e) {
						// TODO Auto-generated method stub
						if (result) {
							// clickBtn.setText(LOAD_SUC);

							Toast.makeText(getApplicationContext(),
									"save 上传成功", 1000).show();
							Intent intent = new Intent(
									SetPersonalInformation2Activity.this,
									MainActivity.class);
							startActivity(intent);
						} else {
							// clickBtn.setText(LOAD_FAIL);
							Toast.makeText(getApplicationContext(),
									"save 上传失败", 1000).show();
						}
					}
				});

			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
