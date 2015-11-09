package com.meetu.activity;






import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.MainActivity;
import com.meetu.R;
import com.meetu.cloud.callback.ObjFunEnumCallback;
import com.meetu.cloud.callback.ObjFunObjectCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjExecResult;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.cloud.wrap.ObjWrap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	//控件相关
	private ImageView back;
	private Button dengluButton;
	private EditText uphone,upassward;
	private TextView forgert;
	private TextView forgetPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_login);
		initView();
	}
	private void initView(){
		back=(ImageView) super.findViewById(R.id.iv_back_denglu);
		back.setOnClickListener(this);
		uphone=(EditText) super.findViewById(R.id.activity_register_phone_et);
		upassward=(EditText) super.findViewById(R.id.activity_register_mima_et);
		uphone.addTextChangedListener(watcher);
		upassward.addTextChangedListener(watcher);
		dengluButton=(Button) super.findViewById(R.id.denglu_bt_denglu);
		dengluButton.setOnClickListener(this);
		forgert=(TextView)super.findViewById(R.id.forget_to_torgetactivty_tv);
		forgert.setOnClickListener(this);
		
	}

	/**
	 * Editview 输入框监听事件
	 */
	
	private TextWatcher watcher = new TextWatcher() {
	    
	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
	        // TODO Auto-generated method stub
	    	if(uphone.getText().length()!=0&&upassward.getText().length()!=0){
	    		dengluButton.setBackgroundResource(R.drawable.register_login_720);
	    	}else{
	    		dengluButton.setBackgroundResource(R.drawable.register_login_1_720);
	    	}
	        
	    }
	    
	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
	            int after) {
	        // TODO Auto-generated method stub
	        
	    }
	    
	    @Override
	    public void afterTextChanged(Editable s) {
	        // TODO Auto-generated method stub
	        
	    }
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back_denglu:
			finish();
			break;
		case R.id.denglu_bt_denglu:
			if(uphone.getText().length()!=11){
				Toast.makeText(LoginActivity.this, "手机格式不正确", Toast.LENGTH_SHORT).show();
			}else if(upassward.getText().length()<6){
				Toast.makeText(LoginActivity.this, "密码不能小于6位", Toast.LENGTH_SHORT).show();
			}
			else{
//				Toast.makeText(LoginActivity.this, "可以发送数据了", Toast.LENGTH_SHORT).show();
				
				ObjUserWrap.login(uphone.getText().toString(), upassward.getText().toString(), new ObjFunObjectCallback() {
					
					@Override
					public void callback(AVObject object, AVException e) {
						if(object!=null){
							//登陆成功
							log.e("object", object.getClassName());
							Intent intent=new Intent(LoginActivity.this,MainActivity.class);
							startActivity(intent);
							finish();
							
						}else {
							if(e.getCode()==1){
								Toast.makeText(LoginActivity.this, "密码不正确", Toast.LENGTH_SHORT).show();
							}
							if(e.getCode()==2){
								Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
							}
						}
						
					}
				});
				
			}
			break;
		//忘记密码 操作
		case R.id.forget_to_torgetactivty_tv:
			Intent intent=new Intent(LoginActivity.this,ForgetPasswardActivity.class);
			intent.putExtra("userphone",uphone.getText());
			startActivity(intent);
			
//			if(uphone.getText().length()!=11){
//				Toast.makeText(LoginActivity.this, "手机号码格式错误", Toast.LENGTH_SHORT).show();			
//			}
//			else{
//				String phoneString=uphone.getText().toString();
//				ObjUserWrap.phoneIsAlreadyRegister(phoneString, new ObjFunEnumCallback() {
//					
//					@Override
//					public void callback(ObjExecResult result, AVException e) {
//						log.e("cunzai", "123");
//						if(result==ObjExecResult.EXEC_TRUE){
//							//表述此手机号注册过
//							
//							Intent intent=new Intent(LoginActivity.this,ForgetPasswardActivity.class);
//							intent.putExtra("userphone",uphone.getText());
//							startActivity(intent);
//							
//							//Toast.makeText(RegisterActivity.this, "此手机号 已经注册", Toast.LENGTH_SHORT).show();
//						}else if(result==ObjExecResult.EXEC_FALSE){
//	
//							Toast.makeText(LoginActivity.this, "手机号未注册", Toast.LENGTH_SHORT);
//						}else{
//							log.e("register", e);
//						}
//						
//					}
//				});
//
//				
//			}
			
			break;
		default:
			break;
		}
		
	}

}
