package com.meetu.activity.homepage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import cn.beecloud.BCPay;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCPayResult;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.meetu.R;
import com.meetu.R.layout;
import com.meetu.R.menu;
import com.meetu.adapter.BookingListViewAdapter;
import com.meetu.bean.ActivityBean;
import com.meetu.cloud.callback.ObjActivityOrderCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjTicketCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityOrder;
import com.meetu.cloud.object.ObjActivityTicket;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityOrderWrap;
import com.meetu.cloud.wrap.ObjActivityWrap;
import com.meetu.common.Constants;
import com.meetu.entity.Booking;
import com.meetu.myapplication.MyApplication;
import com.meetu.tools.DateUtils;
import com.meetu.tools.DensityUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class JoinActivity extends Activity implements OnClickListener,
OnItemClickListener {
	private RelativeLayout backLayout;
	private ListView listView;
	private List<Booking> booklist = new ArrayList<Booking>();
	private BookingListViewAdapter adapter;
	private LinearLayout payLayout;
	private RelativeLayout paylRelativeLayout, payweixin, payzhifubao;
	private RelativeLayout visibleCenten4;

	// 网络数据相关
	// 某项活动的票种列表
	private ActivityBean activityBean = new ActivityBean();
	private ArrayList<ObjActivityTicket> tickets = new ArrayList<ObjActivityTicket>();
	private ObjActivity objActivity = null;
	private FinalBitmap fianlBitmap;
	private int selectedPosition = -1;
	// 拿本地的 user
	private AVUser currentUser = AVUser.getCurrentUser();
	private ObjUser user;
	// 控件相关
	private ImageView photoHuodong;
	private TextView title, titleDesc, timeStart;
	private EditText nameEditText;

	// 获取活动报名信息
	boolean isFirstLoad = false;
	boolean isSecondLoad = false;
	private boolean isJoin = false;// 是否报名
	//订单实体
	ObjActivityOrder activityOrder = new ObjActivityOrder();
	//支付方式标记
	String payMethord = "";
	//支付结果返回入口
	BCCallback bcCallback = new BCCallback() {
		@Override
		public void done(final BCResult bcResult) {
			final BCPayResult bcPayResult = (BCPayResult)bcResult;
			//根据你自己的需求处理支付结果
			//需要注意的是，此处如果涉及到UI的更新，请在UI主进程或者Handler操作
			JoinActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String result = bcPayResult.getResult();
					/*
	                      注意！
	                      所有支付渠道建议以服务端的状态金额为准，此处返回的RESULT_SUCCESS仅仅代表手机端支付成功
					 */
					if (result.equals(BCPayResult.RESULT_SUCCESS)) {
						Toast.makeText(JoinActivity.this, "用户支付成功", Toast.LENGTH_LONG).show();
						updateOrder(Constants.OrderStatusPaySuccess);
					} else if (result.equals(BCPayResult.RESULT_CANCEL)){
						Toast.makeText(JoinActivity.this, "用户取消支付", Toast.LENGTH_LONG).show();
						updateOrder(Constants.OrderStatusPayFail);
					}else if (result.equals(BCPayResult.RESULT_FAIL)) {
						String toastMsg = "支付失败, 原因: " + bcPayResult.getErrCode() +
								" # " + bcPayResult.getErrMsg() +
								" # " + bcPayResult.getDetailInfo();
						/**
						 * 以下是正常流程，请按需处理失败信息
						 */

						Toast.makeText(JoinActivity.this, toastMsg, Toast.LENGTH_LONG).show();
						updateOrder(Constants.OrderStatusPayFail);
					} else if (result.equals(BCPayResult.RESULT_UNKNOWN)) {
						//可能出现在支付宝8000返回状态
						Toast.makeText(JoinActivity.this, "订单状态未知", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(JoinActivity.this, "invalid return", Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_join);
		// 取到全局的bitmap
		MyApplication app = (MyApplication) this.getApplicationContext();
		fianlBitmap = app.getFinalBitmap();

		if (currentUser != null) {
			// 强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);

		}
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		activityBean = (ActivityBean) bundle.getSerializable("activityBean");
		initLoadActivity(activityBean.getActyId());
		// 数据加载
		loaddate();

		// 事件绑定处理
		initView();

	}

	private void initLoadActivity(String activityId) {
		log.e("zcq", "activityId==" + activityId);
		try {
			objActivity = AVObject.createWithoutData(ObjActivity.class,
					activityId);
			// ObjActivityCoverWrap.queryActivityCover(objActivity, new
			// ObjActivityCoverCallback() {
			//
			// @Override
			// public void callback(List<ObjActivityCover> objects, AVException
			// e) {
			// // TODO Auto-generated method stub
			//
			// }
			// });
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initView() {
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_join_homepager_rl);
		backLayout.setOnClickListener(this);
		listView = (ListView) super
				.findViewById(R.id.listview_piao_join_homepager);
		adapter = new BookingListViewAdapter(this, tickets);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

		payLayout = (LinearLayout) super
				.findViewById(R.id.pay_join_homepager_ll);
		payLayout.setOnClickListener(this);
		paylRelativeLayout = (RelativeLayout) super
				.findViewById(R.id.pay_join_homepager_rl);
		paylRelativeLayout.setOnClickListener(this);
		payweixin = (RelativeLayout) super.findViewById(R.id.pay_weixin_rl);
		payweixin.setOnClickListener(this);
		payzhifubao = (RelativeLayout) super.findViewById(R.id.pay_zhifubao_rl);
		payzhifubao.setOnClickListener(this);
		visibleCenten4 = (RelativeLayout) super
				.findViewById(R.id.center4_join_rl);

		title = (TextView) super.findViewById(R.id.title_huodong_join_tv);
		titleDesc = (TextView) super
				.findViewById(R.id.title_tab_huodong_join_tv);
		timeStart = (TextView) super
				.findViewById(R.id.time_huodong_start_join_tv);
		title.setText(activityBean.getTitle());
		titleDesc.setText(activityBean.getTitleSub());
		timeStart
		.setText(DateUtils.getDateToString(activityBean.getTimeStart()));

		photoHuodong = (ImageView) super
				.findViewById(R.id.photo_join_homepager);

		fianlBitmap.display(photoHuodong, activityBean.getActivityCover());

		nameEditText = (EditText) super.findViewById(R.id.name_join_et);
	}

	private void loaddate() {
		// booklist=new ArrayList<Booking>();
		// booklist.add(new Booking(11,"在校生",0,"99"));
		// booklist.add(new Booking(22,"校友",10,"199"));
		// booklist.add(new Booking(33,"投资人士",10,"免费"));
		// booklist.add(new Booking(33,"其他",0,"999"));

		// 获取票种信息
		ObjActivityWrap.queryTicket(objActivity, new ObjTicketCallback() {

			@Override
			public void callback(List<ObjActivityTicket> objects, AVException e) {
				// TODO Auto-generated method stub

				if (e != null) {
					Toast.makeText(getApplicationContext(), e.getMessage(),
							1000).show();
					return;
				}
				if (objects != null && objects.size() > 0) {
					tickets.addAll(objects);
					String ss = tickets.get(0).getTicketTitle();
					log.e("zcq", "name==" + ss);
					handler.sendEmptyMessage(1);
					// secondTv.setText(ss);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_join_homepager_rl:
			finish();

			break;
		case R.id.pay_join_homepager_rl:
			// TODO 免费活动
			checkISjoinActivity(true);
			break;
		case R.id.pay_weixin_rl:
			payMethord = "weixin";
			checkISjoinActivity(false);
			Toast.makeText(this, "点击了微信支付", Toast.LENGTH_SHORT).show();
			break;
		case R.id.pay_zhifubao_rl:
			payMethord = "zhifubao";
			checkISjoinActivity(false);
			Toast.makeText(this, "点击了支付宝支付", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		// Toast.makeText(this,"position "+position+" id "+id,
		// Toast.LENGTH_SHORT).show();
		adapter.setSelectedPosition(position);
		selectedPosition = position;
		if (tickets.get(position).getPrice() == 0) {
			payLayout.setVisibility(View.INVISIBLE);
			paylRelativeLayout.setVisibility(View.VISIBLE);
			visibleCenten4.setVisibility(View.INVISIBLE);
		} else {
			payLayout.setVisibility(View.VISIBLE);
			paylRelativeLayout.setVisibility(View.INVISIBLE);
			visibleCenten4.setVisibility(View.VISIBLE);
		}

		adapter.notifyDataSetChanged();

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				// 给listview动态设置高度
				RelativeLayout.LayoutParams params = (LayoutParams) listView
				.getLayoutParams();
				params.height = tickets.size()
						* DensityUtil.dip2px(JoinActivity.this, 70);
				listView.setLayoutParams(params);
				adapter.notifyDataSetChanged();

				break;
			}
		}

	};

	/**
	 * 检查是否报名参加过活动
	 * 
	 * @author lucifer
	 * @date 2015-11-13
	 */
	private void checkISjoinActivity(final boolean isFree) {
		// 查询是否报名
		ObjActivityWrap.queryUserJoin(objActivity, user,
				new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				isFirstLoad = true;
				if (isFirstLoad && isSecondLoad) {

				}
				if (e != null) {
					return;
				} else if (result) {
					isJoin = true;
					Toast.makeText(getApplicationContext(),
							"您已经报名过此活动", Toast.LENGTH_SHORT).show();
				} else {
					isJoin = false;
					if(isFree){
						//支付状态为成功
						joinActivity(Constants.OrderStatusPaySuccess);
					}else{
						//支付状态为锁定
						joinActivity(Constants.OrderStatusLock);
					}
				}
			}
		});
	}
	/**
	 * 报名参加活动
	 * */
	public void joinActivity(final int orderState){
		if (nameEditText.getText().length() != 0) {
			ObjActivityOrderWrap.signUpActivity(
					objActivity, user,
					tickets.get(selectedPosition),
					orderState,
					nameEditText.getText().toString(),
					new ObjActivityOrderCallback() {

						@Override
						public void callback(ObjActivityOrder order, AVException e) {
							if (e != null) {
								Toast.makeText(
										getApplicationContext(),
										"报名失败，请检查网络",
										Toast.LENGTH_SHORT)
										.show();
							} else {
								isJoin = true;
								activityOrder = order;
								if(orderState == Constants.OrderStatusPaySuccess){
									Toast.makeText(
											getApplicationContext(),
											"报名成功",
											Toast.LENGTH_SHORT)
											.show();
									return;
								}
								if(payMethord.equals("weixin")){
									payMethord = "";
									payWeiXin(activityOrder.getObjectId(),tickets.get(selectedPosition).getPrice());
								}else if(payMethord.equals("zhifubao")){
									payMethord = "";
									payZfb(activityOrder.getObjectId(),tickets.get(selectedPosition).getPrice());
								}
							}

						}
					});
		} else {
			Toast.makeText(getApplicationContext(),
					"请填写你的真实姓名", Toast.LENGTH_SHORT).show();
		}

	}
	//修改订单状态
	public void updateOrder(int state){
		activityOrder.setOrderStatus(state);
		ObjActivityOrderWrap.updateOrder(activityOrder, new ObjActivityOrderCallback() {

			@Override
			public void callback(ObjActivityOrder order, AVException e) {
				// TODO Auto-generated method stub
				if(e == null){
					activityOrder = order;
				}
			}
		});
	}
	//支付宝支付
	protected void payZfb(String orderId,int price) {
		// TODO Auto-generated method stub
		Map<String, String> mapOptional = new HashMap<String, String>();
		mapOptional.put("phone", "android");

		BCPay.getInstance(JoinActivity.this).reqAliPaymentAsync(
				activityBean.getTitle()+"报名",
				1,
				orderId,
				mapOptional,
				bcCallback);
	}
	//微信支付
	protected void payWeiXin(String orderId,int price) {
		// TODO Auto-generated method stub
		//对于微信支付, 手机内存太小会有OutOfResourcesException造成的卡顿, 以致无法完成支付
		//这个是微信自身存在的问题
		Map<String, String> mapOptional = new HashMap<String, String>();

		mapOptional.put("phone", "android");

		if (BCPay.isWXAppInstalledAndSupported() &&
				BCPay.isWXPaySupported()) {

			BCPay.getInstance(JoinActivity.this).reqWXPaymentAsync(
					activityBean.getTitle()+"报名",               //订单标题
					1,                           //订单金额(分)
					orderId,  //订单流水号
					mapOptional,            //扩展参数(可以null)
					bcCallback);            //支付完成后回调入口

		} else {
			Toast.makeText(JoinActivity.this,
					"您尚未安装微信或者安装的微信版本不支持", Toast.LENGTH_LONG).show();
		}
	}

}
