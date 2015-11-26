package com.meetu.fragment;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.R;
import com.meetu.activity.ChooseSchoolActivity;
import com.meetu.activity.mine.ChangeBirthdayActivity;
import com.meetu.activity.mine.ChangeCityActivity;
import com.meetu.activity.mine.ChangeMajorActivity;
import com.meetu.activity.mine.ChangeNameActivity;
import com.meetu.activity.mine.ChangeSchoolActivity;
import com.meetu.activity.mine.ChangexingzuoActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.common.SchoolDao;
import com.meetu.entity.Schools;
import com.meetu.sqlite.CityDao;
import com.meetu.tools.DateUtils;
import com.meetu.view.NotifyingScrollView;
import com.meetu.view.ScrollTabHolderMineupFragment;

import android.R.raw;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MinePersonalInformation extends ScrollTabHolderMineupFragment implements OnClickListener{
	//控件相关
	private View view;
	NotifyingScrollView mScrollView;
	private ImageView ivman_selector,ivwoman_selector;
	private TextView username,usersex,userbirthday,userConstellation;
	private TextView userschool,usergrade,usermajor,userhometown;
	private LinearLayout nameLayout,sexlLayout,birthdayLayout,constellationLayout;
	private LinearLayout schoolLayout,gradeLayout,majorLayout,hometownLayout;

	//网络数据相关
	//拿本地的  user 
	private AVUser currentUser = AVUser.getCurrentUser();
	private ObjUser user;
	private CityDao cityDao=new CityDao();
	private SchoolDao schoolDao=new SchoolDao();
	
	private static final String ARG_POSITION = "position";
	private int mPosition;
	public static Fragment newInstance(int position) {
		MinePersonalInformation fragment = new MinePersonalInformation();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }
	//滑动相关，调整view位置
		@Override
		public void adjustScroll(int scrollHeight, int headerHeight) {
			if (mScrollView == null) return;
			mScrollView.scrollTo(0, headerHeight - scrollHeight);
		}
		
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null){
			view=inflater.inflate(R.layout.fragment_mine_personal_information, null);

			initView();
		}
		ViewGroup parent=(ViewGroup)view.getParent();
		if(parent!=null){
			parent.removeView(view);
		}
		mScrollView.setOnScrollChangedListener(new NotifyingScrollView.OnScrollChangedListener() {
			@Override
			public void onScrollChanged(ScrollView view, int l, int t, int oldl, int oldt) {
				if (mScrollTabHolder != null) {
					mScrollTabHolder.onScrollViewScroll(view, l, t, oldl, oldt, mPosition);
				}
			}
		});
		mPosition = getArguments().getInt(ARG_POSITION);
		return view;
	}
	private void initView(){
		//拿到本地的缓存对象  user
		if(currentUser!=null){
			//强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		mScrollView = (NotifyingScrollView) view.findViewById(R.id.scrollview_mine_fragment);
		username=(TextView) view.findViewById(R.id.minesetting_username_tv);
		usersex=(TextView) view.findViewById(R.id.minesetting_sex_tv);
		userbirthday=(TextView) view.findViewById(R.id.minesetting_birthday_tv);
		userConstellation=(TextView) view.findViewById(R.id.minesetting_Constellation_tv);
		userschool=(TextView) view.findViewById(R.id.minesetting_school_tv);

		usermajor=(TextView) view.findViewById(R.id.minesetting_major_tv);
		userhometown=(TextView) view.findViewById(R.id.minesetting_hometown_tv);
		username.setOnClickListener(this);
		usersex.setOnClickListener(this);
		userbirthday.setOnClickListener(this);
		userConstellation.setOnClickListener(this);
		userschool.setOnClickListener(this);

		usermajor.setOnClickListener(this);
		userhometown.setOnClickListener(this);
		nameLayout=(LinearLayout) view.findViewById(R.id.minesetting_username_ll);
		birthdayLayout=(LinearLayout) view.findViewById(R.id.minesetting_birthday_ll);
		constellationLayout=(LinearLayout) view.findViewById(R.id.minesetting_Constellation_ll);
		schoolLayout=(LinearLayout) view.findViewById(R.id.minesetting_school_ll);

		sexlLayout=(LinearLayout) view.findViewById(R.id.minesetting_sex_ll);
		majorLayout=(LinearLayout) view.findViewById(R.id.minesetting_major_ll);
		hometownLayout=(LinearLayout) view.findViewById(R.id.minesetting_hometown_ll);
		nameLayout.setOnClickListener(this);
		birthdayLayout.setOnClickListener(this);
		constellationLayout.setOnClickListener(this);
		schoolLayout.setOnClickListener(this);
		sexlLayout.setOnClickListener(this);
		majorLayout.setOnClickListener(this);

		hometownLayout.setOnClickListener(this);

		//设置 属性数据
		username.setText(user.getNameNick());

		if(user.getGender()==1){
			usersex.setText("男生");
		}else if(user.getGender()==2){
			usersex.setText("女生");
		}else{
			usersex.setText("未知");
		}
		Long birthLong=user.getBirthday();
		String birthString=DateUtils.getDateToString(birthLong);
		log.e("zcq", "birthString"+birthString);
		userbirthday.setText(birthString);
		userConstellation.setText(user.getConstellation());
		userschool.setText(user.getSchool());
		usermajor.setText(user.getDepartment());
		log.d("mytest", ""+user.getHometown());
		if(user.getHometown()!=null && !user.getHometown().equals("")){
			String homeString=""+cityDao.getCity(user.getHometown()).get(0).getPrivance()+
					cityDao.getCity(user.getHometown()).get(0).getCity()+
					cityDao.getCity(user.getHometown()).get(0).getTown();
			userhometown.setText(homeString);
		}else{
			userhometown.setText("");
		}
		
	}

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub

		super.setArguments(args);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//修改名字
		case R.id.minesetting_username_ll :

			Intent intent=new Intent(getActivity(),ChangeNameActivity.class);
			intent.putExtra("name",username.getText().toString());
			startActivityForResult(intent,0);
			break;
			//修改生日
		case R.id.minesetting_birthday_ll:
			Intent intent5=new Intent(getActivity(),ChangeBirthdayActivity.class);
			intent5.putExtra("birthday",userbirthday.getText().toString());
			startActivityForResult(intent5,5);
			break;
			//修改 学校
		case R.id.minesetting_school_ll:
			Intent intent1=new Intent(getActivity(),ChangeSchoolActivity.class);
			intent1.putExtra("school",userschool.getText());
			startActivityForResult(intent1, 1);	
			break;
			//星座的修改	
		case R.id.minesetting_Constellation_ll:
			Intent intent2=new Intent(getActivity(),ChangexingzuoActivity.class);
			intent2.putExtra("Constellation", userConstellation.getText());
			startActivityForResult(intent2, 2);
			break;
			//专业的修改
		case R.id.minesetting_major_ll:
			//传个学校对象过去 和 完善信息保持一致
			Schools schools=new Schools();
			schools.setUnivsId(""+user.getSchoolNum());
			schools.setUnivsNameString(schoolDao.getschoolName(""+user.getSchoolNum()).get(0).getUnivsNameString());
			Intent intent3=new Intent(getActivity(),ChangeMajorActivity.class);
			Bundle bundle=new Bundle();
			bundle.putSerializable("schools",schools);
			intent3.putExtras(bundle);
			startActivityForResult(intent3, 3);
			break;
			//家乡的修改
		case R.id.minesetting_hometown_ll:
			Intent intent4=new Intent(getActivity(),ChangeCityActivity.class);
			intent4.putExtra("hometown", userhometown.getText());
			startActivityForResult(intent4,4);
			break;
		default :
			break;
		}

	}




	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data){

		switch(requestCode){
		case 0:
			if(resultCode==getActivity().RESULT_OK){
				/*取得来自SecondActivity页面的数据，并显示到画面*/	   
				/*获取Bundle中的数据，注意类型和key*/
				String name = data.getExtras().getString("name");			              
				// Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
				username.setText(name);			  
			}			  
			break;
		case 1:
			if(resultCode==getActivity().RESULT_OK){
				Toast.makeText(getActivity(), "学校 专业修改成功", Toast.LENGTH_SHORT).show();
				String schoolID=data.getExtras().getString("schools");
				String majorID=data.getExtras().getString("departments");
				String schoolName=schoolDao.getschoolName(schoolID).get(0).getUnivsNameString();
				String majorName=schoolDao.getDepartmentsName(schoolID, majorID).get(0).getDepartmentName();
				userschool.setText(schoolName);
				usermajor.setText(majorName);		  
			}
			break;
		case 2:
			if(resultCode==getActivity().RESULT_OK){
				String xingzuo=data.getExtras().getString("xingzuo");
				//	  Toast.makeText(getActivity(), xingzuo, Toast.LENGTH_SHORT).show();
				userConstellation.setText(xingzuo);
			}

			break;
		case 3:
			if(resultCode==getActivity().RESULT_OK){
				Toast.makeText(getActivity(), "专业修改成功", Toast.LENGTH_SHORT).show();
				String major=data.getExtras().getString("department");
				usermajor.setText(schoolDao.getDepartmentsName(""+user.getSchoolNum(), major).get(0).getDepartmentName());
			}

			break;
		case 4:
			if(resultCode==getActivity().RESULT_OK){
				Toast.makeText(getActivity(), "家乡修改成功", Toast.LENGTH_SHORT).show();
				String hometown=data.getExtras().getString("city");
				userhometown.setText(hometown);
			}

			break;
		case 5:
			if(resultCode==getActivity().RESULT_OK){
				String birthday=data.getExtras().getString("birthday");
				userbirthday.setText(birthday);
			}

			break;
		}

	}


}
