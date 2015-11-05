package com.meetu.activity.mine;





import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.LogUtil.log;
import com.meetu.R;
import com.meetu.activity.SetPersonalInformation2Activity;
import com.meetu.common.SchoolDao;
import com.meetu.entity.Department;
import com.meetu.entity.Schools;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChangeMajorActivity extends Activity implements OnClickListener,OnItemClickListener{
	//控件相关
	private TextView queding;
	private TextView majorEditText;
	private String major;
	private ImageView backImageView;
	private RelativeLayout backLayout,quedingLayout;
	
	//listview相关
	private ListView mListView;
	private List<Department> mList=new ArrayList<Department>();
	private DepartmentListAllAdapter mAdapter;
	private SchoolDao schoolDao=new SchoolDao();
	
	private Schools schools=new Schools();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//全屏
		super.getWindow();
		setContentView(R.layout.activity_change_major);
		major=super.getIntent().getStringExtra("major");
		//接收传递过来的对象
		Intent intent = this.getIntent(); 
		schools=(Schools)intent.getSerializableExtra("schools");
		loadData();
		initView();
	}


	private void loadData() {
		//TODO 测试数据
		mList=schoolDao.getDepartments(schools.getUnivsId());
	}


	private void initView() {
		// TODO 
		queding=(TextView) super.findViewById(R.id.mine_changemajor_majorwancheng_bt);
		queding.setOnClickListener(this);
		
		majorEditText=(TextView) findViewById(R.id.name_changmajor_et);
		majorEditText.setText(schools.getUnivsNameString());
		majorEditText.setOnClickListener(this);
		backImageView=(ImageView) super.findViewById(R.id.back_changemajor_mine);
		backImageView.setOnClickListener(this);
		backLayout=(RelativeLayout) super.findViewById(R.id.back_changemajor_mine_rl);
		quedingLayout=(RelativeLayout) super.findViewById(R.id.mine_changemajor_majorwancheng_rl);
		backLayout.setOnClickListener(this);
		quedingLayout.setOnClickListener(this);
		
		//listview
		mListView=(ListView) super.findViewById(R.id.listview_changeMajor);
		mAdapter=new DepartmentListAllAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.mine_changemajor_majorwancheng_rl:

				Intent intent=new Intent();				
				intent.putExtra("major", majorEditText.getText().toString());
				ChangeMajorActivity.this.setResult(3,intent);				
				finish();
				
				break;
			case R.id.back_changemajor_mine_rl:
				Intent intent2=new Intent();	
				intent2.putExtra("major", major);
				ChangeMajorActivity.this.setResult(3,intent2);
				finish();
				
			default :
				break;
		}
		
	}
	/**
	 * 设置点击返回键的状态
	 */
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent=new Intent();	
		intent.putExtra("major", major);
		ChangeMajorActivity.this.setResult(3,intent);
		finish();
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int positon, long arg3) {
		// TODO text
		log.e("lucifer", ""+mList.get(positon).getDepartmentName());
		Intent intent=new Intent(ChangeMajorActivity.this,SetPersonalInformation2Activity.class);
//		Bundle bundle=new Bundle();
//		bundle.putSerializable("department", mList.get(positon));
//		intent.putExtras(bundle);
		intent.putExtra("school", schools.getUnivsId());
		intent.putExtra("department", mList.get(positon).getId());
		startActivity(intent);
		
	}
}
