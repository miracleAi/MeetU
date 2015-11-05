package com.meetu.activity.mine;





import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.LogUtil.log;
import com.meetu.R;
import com.meetu.common.SchoolDao;
import com.meetu.entity.Schools;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ChangeSchoolActivity extends Activity implements OnClickListener {
	//控件相关
	private ImageView back;
	private String school;
	private RelativeLayout backLayout;
	private EditText mEditText;
	
	//listView 相关
	private ListView mListView,mListViewFind;
	private List<Schools> schoolsAlllList=new ArrayList<Schools>();
	private SchoolDao schoolDao=new SchoolDao();
	private SchoolListAllAdapter mListAllAdapter;
	private List<Schools> schoolsFindList=new ArrayList<Schools>();
	private SchoolListAllAdapter mListFindAdapter;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//全屏
		super.getWindow();
		setContentView(R.layout.activity_change_grade);
	
		school=super.getIntent().getStringExtra("school");
		
		
		loadData();
		
		initView();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		
		schoolsAlllList=schoolDao.getschoolAll();
		log.e("lucifer", ""+schoolsAlllList.size());
		
	}

	private void initView() {
		back=(ImageView) super.findViewById(R.id.back_changeschool_mine);
		back.setOnClickListener(this);
		backLayout=(RelativeLayout) super.findViewById(R.id.back_changeschool_mine_rl);
		backLayout.setOnClickListener(this);
	
		
		
		mListView=(ListView) super.findViewById(R.id.schoolList_changeschool_mine);
		mListAllAdapter=new SchoolListAllAdapter(this, schoolsAlllList);
		//给listview 加头
		LinearLayout  hearder =  (LinearLayout) LayoutInflater.from(this).inflate(R.layout.listview_schooll_all_head, null);
		mListView.addHeaderView(hearder);
		
		mListView.setAdapter(mListAllAdapter);
		
		//点击item 进行的操作
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				log.e("lucifer", ""+schoolsAlllList.get(position-1).getUnivsId()+" "+schoolsAlllList.get(position-1).getUnivsNameString());
				
				Intent intent=new Intent(ChangeSchoolActivity.this,ChangeMajorActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("schools", schoolsAlllList.get(position-1));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		mListViewFind=(ListView) super.findViewById(R.id.schoolList_find_changeschool_mine);
		//listView 加头
		LinearLayout  hearderViewLayout =  (LinearLayout) LayoutInflater.from(this).inflate(R.layout.listview_schooll_find_head, null);
		mListViewFind.addHeaderView(hearderViewLayout); 
//		mListFindAdapter=new SchoolListAllAdapter(this, list);
		
		mEditText=(EditText) super.findViewById(R.id.content_change_grade_et);
		
		mEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO chaxun
				if(arg0.length()!=0){
					mListViewFind.setVisibility(View.VISIBLE);	
				
				schoolsFindList=schoolDao.getSchoolsFind(arg0.toString());
				
//				if(schoolsFindList.size()!=0){
//					mListFindAdapter=new SchoolListAllAdapter(this, schoolsFindList);
//					mListViewFind.setAdapter(mListFindAdapter);
//				}
				addListview(schoolsFindList);
				}else{
					mListViewFind.setVisibility(View.GONE);
				}
				
			}
		});
		
		
	}

	public void addListview(List<Schools> list){
		if(list.size()!=0){
			mListFindAdapter=new SchoolListAllAdapter(this, list);
			    


			mListViewFind.setAdapter(mListFindAdapter);
			mListViewFind.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO 
					log.e("lucifer", ""+schoolsFindList.get(position-1).getUnivsId()+" "+schoolsFindList.get(position-1).getUnivsNameString());
					Intent intent=new Intent(ChangeSchoolActivity.this,ChangeMajorActivity.class);
					Bundle bundle=new Bundle();
					bundle.putSerializable("schools", schoolsFindList.get(position-1));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			
		}
		mListFindAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back_changeschool_mine_rl:
				Intent intent2=new Intent();	
				intent2.putExtra("school", school);
				ChangeSchoolActivity.this.setResult(1,intent2);
				finish();
				
				break;
			

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
		intent.putExtra("school", school);
		ChangeSchoolActivity.this.setResult(1,intent);
		finish();
	}

}
