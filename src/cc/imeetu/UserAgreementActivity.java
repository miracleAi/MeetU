package cc.imeetu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.RelativeLayout;

public class UserAgreementActivity extends Activity {
	private RelativeLayout backLayout;
	WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_agreement);
		backLayout=(RelativeLayout) findViewById(R.id.back_useragreement);
		webView=(WebView) findViewById(R.id.webview_useragreement);
		backLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		webView.loadUrl("http://ac-tcd4rj3s.clouddn.com/5007f6332ea4396d.html");
	}

	

}
