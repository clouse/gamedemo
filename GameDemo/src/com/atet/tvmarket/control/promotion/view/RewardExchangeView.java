package com.atet.tvmarket.control.promotion.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;

public class RewardExchangeView extends LinearLayout {
	//200积分的概率
	public static double rate0 = 0.17;  
	//6s的概率
	public static double rate1 = 0.01;  
	//100积分的概率
	public static double rate2 = 0.27;  
	//手柄的概率
	public static double rate3 = 0.02;  
	//50积分的概率
	public static double rate4 = 0.37;  
	//500积分的概率
	public static double rate5 = 0.08; 
	//100话费的概率
	public static double rate6 = 0.03;
	//iwatch的概率
	public static double rate7 = 0.01;
	//50话费的概率
	public static double rate8 = 0.04;  
	
	//是否设置概率
	private boolean hasPercentage;
	private static final int DEFAULT_TEXTSIZE = 25;
	private int textColor;
	private float textSize;
	private int lines;
	private float linesSpace;
	
	private LinearLayout mLinearLayout;
	private TextView tv_info;
	
	private TextView tv_gift;
	private int count;
	private String sysTime;
	private SimpleDateFormat formatters;
	private String desc;
	private int firstShowCount;
	
	private int[] phone = {134, 135, 136, 137, 138, 139, 150, 151, 152, 157, 158, 159, 187, 188,
			130, 131, 132, 155, 156, 185, 186, 133,153,180,189}; 
	
	private String[] address = {"黑龙江","吉林省","辽宁省","河北省","河南省","山东省","江苏省","山西省","陕西省",
			"甘肃省","四川省","青海省","湖南省","湖北省","江西省","安徽省","浙江省","福建省","广东省","广西省",
			"贵州省","云南省","海南省","内蒙古","新疆省","西藏省","宁夏省","北京省","天津省","上海省","重庆省"};
	
	private List<String> goods = new ArrayList<String>();
	
	private String userInfo;
	private String product;
	private String phoneNumber;
	private String province;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			addLinearLayout();
		}
	};
	
	public RewardExchangeView(Context context) {
		this(context, null);

	}

	public RewardExchangeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		ScaleViewUtils.init(context);
		TypedArray arr = context.obtainStyledAttributes(attrs,R.styleable.luckyview);
		
		textSize = arr.getDimension(R.styleable.luckyview_textSize, DEFAULT_TEXTSIZE);
		textColor = arr.getColor(R.styleable.luckyview_textColor, Color.BLACK);
		lines = arr.getInt(R.styleable.luckyview_lines, 11);
		linesSpace = arr.getDimension(R.styleable.luckyview_linesSpace, 0);
		// TODO Auto-generated constructor stub
		arr.recycle();
	}

	public RewardExchangeView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
		ScaleViewUtils.init(getContext());

	}
	
	private void initView() {
		firstShowCount = (int)(Math.random()  * 5 + 3);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
							Thread.sleep(getDelayTime());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mHandler.sendEmptyMessage(0);
				
				}
			}
		}).start();
			
	}
	
	/**
	 * 
	 * @description: 添加数据
	 * 
	 * @throws: 
	 * @author: LiJie
	 * @date: 2015年8月20日 下午5:31:27
	 */
	private void addLinearLayout() {
		
		mLinearLayout = new LinearLayout(getContext());
		
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.bottomMargin = (int)ScaleViewUtils.resetTextSize(linesSpace) ;
		
		tv_info = new TextView(getContext());
		tv_gift = new TextView(getContext());
		product = getReward();
		
		tv_info.setText(getUserinfo());
		tv_info.setTextColor(textColor);
		
		tv_info.setTextSize(ScaleViewUtils.resetTextSize(textSize));
		tv_gift.setTextSize(ScaleViewUtils.resetTextSize(textSize));
		tv_gift.setText(product);
		tv_gift.setSingleLine();
		tv_gift.setEllipsize(TruncateAt.END);
		tv_gift.setTextColor(textColor);
		
		if (mLinearLayout != null && getReward() != null) {
			if(NetUtil.isNetworkAvailable(getContext(), true)){
				mLinearLayout.addView(tv_info,params);
				mLinearLayout.addView(tv_gift,params);
			};
		}
		
		count = this.getChildCount();
		if(count >= lines){
			this.removeAllViews();
		}
		this.addView(mLinearLayout);
	}
	
	/**
	 * 
	 * @description: 获取时间的延迟
	 *
	 * @return 
	 * @throws: 
	 * @author: LiJie
	 * @date: 2015年8月20日 下午8:04:48
	 */
	private long getDelayTime(){
		formatters= new SimpleDateFormat("HH:mm");
		Date curDates = new Date(System.currentTimeMillis());// 获取当前时间
	    sysTime = formatters.format(curDates);
		String[] dds = new String[] {};
        // 分取系统时间 小时分
        dds = sysTime.split(":");
        int dhs = Integer.parseInt(dds[0]);
        if(firstShowCount > 0){
        	firstShowCount --;
        	return 10;
        }else{
        	if(dhs > 18 && dhs < 24){
        		return (long)(Math.random() * 30 * 1000 + 60 * 1000);
        	}else{
        		return (long)(Math.random() * 60 * 1000 + 5*60*1000);
        	}
        }
      // return 3000;
	}
	
	
	private String getUserinfo(){
		String secondPart = ((int)(Math.random() * 10000) + 10000 + "").substring(1);
		phoneNumber = phone[(int)(Math.random() * phone.length)] + "****" + secondPart;
		province = address[(int)(Math.random() * address.length)];
		
		return sysTime + "   " + province + "玩家  " + phoneNumber + desc;
	}
	
	public String getReward(){
		if(hasPercentage){
			 double randomNumber = Math.random();  
			  if (randomNumber >= 0 && randomNumber < rate0) {  
				  return goods.get(0);  
			  }else if (randomNumber >= rate0 && randomNumber < rate0 + rate1){  
				  return goods.get(1);  
			  }else if (randomNumber >= rate0 + rate1  && randomNumber < rate0 + rate1 + rate2){  
				  return goods.get(2);  
			  }else if (randomNumber >= rate0 + rate1 + rate2   
					  && randomNumber < rate0 + rate1 + rate2 + rate3){  
				  return goods.get(3);  
			  }else if (randomNumber >= rate0 + rate1 + rate2 + rate3  
					  && randomNumber < rate0 + rate1 + rate2 + rate3 + rate4){  
				  return goods.get(4);  
			  } else if (randomNumber >= rate0 + rate1 + rate2 + rate3 + rate4  
					  && randomNumber < rate0 + rate1 + rate2 + rate3 + rate4  + rate5){  
				  return goods.get(5);  
			  }else if (randomNumber >= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 
					    && randomNumber < rate0 + rate1 + rate2 + rate3 + rate4  + rate5 + rate6){  
				  return goods.get(6);  
			  }else if (randomNumber >= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 +rate6
					    && randomNumber < rate0 + rate1 + rate2 + rate3 + rate4  + rate5 + rate6 + rate7){  
				  return goods.get(7);  
			  }else if (randomNumber >= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 + rate6 + rate7
					    && randomNumber < rate0 + rate1 + rate2 + rate3 + rate4  + rate5 + rate6 + rate7 + rate8){  
				  return goods.get(8);  
			  }   
		}else{
			if(goods != null && goods.size() != 0){
				return goods.get((int)(Math.random() * goods.size()));
			}
		}
		return null;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public void setGoods(List<String> goods,boolean hasPercentage) {
		this.goods = goods;
		this.hasPercentage = hasPercentage;
	}
	
	
}
