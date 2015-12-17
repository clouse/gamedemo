package com.atet.tvmarket.control.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.statistics.bases.StatisticsConstant;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.classify.detail.ThirdGameDetailActivity;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.mygame.activity.GameDetailActivity;
import com.atet.tvmarket.control.mygame.utils.QRUtil;
import com.atet.tvmarket.entity.dao.GameSearchPinyinInfo;
import com.atet.tvmarket.model.DataConfig;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.KeyboardUtils;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.LoadingView;
import com.google.zxing.WriterException;

public class SearchActivity extends BaseActivity {
	ALog alog = ALog.getLogger(SearchActivity.class);
	private Button btnA, btnB, btnC, btnD, btnE, btnF, btnG, btnH, btnI, btnJ,
			btnK, btnL, btnM, btnN, btnO, btnP, btnQ, btnR, btnS, btnT, btnU,
			btnV, btnW, btnX, btnY, btnZ, btn0, btn1, btn2, btn3, btn4, btn5,
			btn6, btn7, btn8, btn9, btnClear; // 键盘按键
	private ImageButton btnDelete; // "删除"按键
	private TextView tvLetters; // 用于显示输入的字母
	private TextView tvSearchShowTv;
	private RecyclerView searchRecyclerView;
	private Map<String, Button> keyboardButtons = new HashMap<String, Button>(); // 存储键盘按键
	private String letters = "";
	private Toast toast = null;
	private SearchResultAdapter searchResultAdapter;
	// private RadioGroup radGrpQrcode;
	private Button btnIOSQrcode, btnAndroidQrcode;
	// private TextView tvQrcodeTip;
	private ImageView ivQrcode;
	private KeyClick keyClick;
	// private BtnClickListener btnClickListener;
	private KeyOnTouchListener keyOnTouchListener;
	private KeyOnFocusChangeListener keyOnFocusChangeListener;
	private List<GameSearchPinyinInfo> gameSearchPinyinInfoList = new ArrayList<GameSearchPinyinInfo>();// 输入拼音字母后的搜索结果
	private List<GameSearchPinyinInfo> allGamePinyinInfoList = new ArrayList<GameSearchPinyinInfo>();// 获取到的所有游戏信息
	private LoadingView loadingView;
	private LinearLayout contentView;
	private static final int MSG_GAMEPINYIN_NET = 0x001; // 从网络获取游戏
	private static final int MSG_GAMEPINYIN_LOCAL = 0x002;// 从本地获取游戏
	private static final int MSG_GAMERECOMMEN = 0x003; // 推荐游戏
	private static final int MSG_LOAD_QRCODE = 0x004;// 加载二维码
	private boolean isFirstLoadPinyin = true; // 判断是否是刚进入到搜索界面时初次加载拼音游戏
	private ConnectivityManager mConnectivityManager;
	private NetworkInfo netInfo;
	private Bitmap bitmap = null;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_GAMEPINYIN_NET:

			case MSG_GAMEPINYIN_LOCAL:
				handleKeyboardView();
			case MSG_GAMERECOMMEN:
				if (searchResultAdapter == null) {
					searchResultAdapter = new SearchResultAdapter(
							gameSearchPinyinInfoList,
							new SearchResultAdapter.IGameiconClick() {
								@Override
								public void gameIconClick(
										GameSearchPinyinInfo gameSearchPinyinInfo) {
									// TODO Auto-generated method stub
									int type = gameSearchPinyinInfo
											.getType();
									// Umeng统计搜索结果
									UmengUtils
											.setOnEvent(
													SearchActivity.this,
													UmengUtils.GAMECENTER_SEARCH_RESULT_CLICK);
									if (type == DataConfig.GAME_TYPE_COPYRIGHT) {
										// 版权游戏
										Intent intent = new Intent(
												SearchActivity.this,
												GameDetailActivity.class);
										intent.putExtra("gameId",
												gameSearchPinyinInfo
														.getGameId());
										intent.putExtra(Constant.GAMECENTER, StatisticsConstant.FROM_GAME_SEARCH);
										startActivity(intent);
									}
									if (type == DataConfig.GAME_TYPE_THIRD) {
										// 第三方游戏
										Intent intent = new Intent(
												SearchActivity.this,
												ThirdGameDetailActivity.class);
										intent.putExtra("ThirdGameId",
												gameSearchPinyinInfo
														.getGameId());
										intent.putExtra(Constant.GAMECENTER, StatisticsConstant.FROM_GAME_SEARCH);
										startActivity(intent);
									}

								}
							}, searchRecyclerView);
					searchRecyclerView.setAdapter(searchResultAdapter);
				} else {
					searchResultAdapter.dataChange(gameSearchPinyinInfoList);
				}
				break;
			default:
				break;
			}
		}
	};;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		ScaleViewUtils.scaleView(this);
		initView();
		setBlackTitle(true);
		// 初次进入时，加载所有拼音游戏
		loadGameSearchPinyin();
		// // 加载推荐游戏
		// loadRecommendGameSearchInfo();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitleData();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//		registerReceiver(myNetReceiver, mFilter);
	}

	private void initView() {
		loadingView = (LoadingView) findViewById(R.id.search_loading);
		contentView = (LinearLayout) findViewById(R.id.layout_content);
		loadingView.setDataView(contentView);
		keyClick = new KeyClick();
		// btnClickListener = new BtnClickListener();
		keyOnTouchListener = new KeyOnTouchListener();
		keyOnFocusChangeListener = new KeyOnFocusChangeListener();
		initKeyboardView();
		initRecyclerView();
		tvSearchShowTv = (TextView) findViewById(R.id.search_result_show_tv);
		// tvQrcodeTip = (TextView) findViewById(R.id.search_qrcode_tip_tv);
		ivQrcode = (ImageView) findViewById(R.id.search_qrcode_iv);
		btnAndroidQrcode = (Button) findViewById(R.id.search_qrcode_android_btn);
		btnAndroidQrcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.requestFocus();
				v.requestFocusFromTouch();
			}
		});
		btnAndroidQrcode.setOnTouchListener(keyOnTouchListener);
		btnAndroidQrcode.setOnFocusChangeListener(keyOnFocusChangeListener);
		btnIOSQrcode = (Button) findViewById(R.id.search_qrcode_ios_btn);
		btnIOSQrcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.requestFocus();
				v.requestFocusFromTouch();
			}
		});
		btnIOSQrcode.setOnTouchListener(keyOnTouchListener);
		btnIOSQrcode.setOnFocusChangeListener(keyOnFocusChangeListener);
		try {
				bitmap = QRUtil.createQRCode(Constant.ATET_OFFICE_ADDRESS,300);
		} catch (WriterException e) {
		// TODO Auto-generated catch block
				e.printStackTrace();
		}
		ivQrcode.setImageBitmap(bitmap);
	}

	private void initKeyboardView() {
		tvLetters = (TextView) findViewById(R.id.search_input_letters_tv);
		btnA = (Button) findViewById(R.id.search_key_A_btn);
		btnB = (Button) findViewById(R.id.search_key_B_btn);
		btnC = (Button) findViewById(R.id.search_key_C_btn);
		btnD = (Button) findViewById(R.id.search_key_D_btn);
		btnE = (Button) findViewById(R.id.search_key_E_btn);
		btnF = (Button) findViewById(R.id.search_key_F_btn);
		btnG = (Button) findViewById(R.id.search_key_G_btn);
		btnH = (Button) findViewById(R.id.search_key_H_btn);
		btnI = (Button) findViewById(R.id.search_key_I_btn);
		btnJ = (Button) findViewById(R.id.search_key_J_btn);
		btnK = (Button) findViewById(R.id.search_key_K_btn);
		btnL = (Button) findViewById(R.id.search_key_L_btn);
		btnM = (Button) findViewById(R.id.search_key_M_btn);
		btnN = (Button) findViewById(R.id.search_key_N_btn);
		btnO = (Button) findViewById(R.id.search_key_O_btn);
		btnP = (Button) findViewById(R.id.search_key_P_btn);
		btnQ = (Button) findViewById(R.id.search_key_Q_btn);
		btnR = (Button) findViewById(R.id.search_key_R_btn);
		btnS = (Button) findViewById(R.id.search_key_S_btn);
		btnT = (Button) findViewById(R.id.search_key_T_btn);
		btnU = (Button) findViewById(R.id.search_key_U_btn);
		btnV = (Button) findViewById(R.id.search_key_V_btn);
		btnW = (Button) findViewById(R.id.search_key_W_btn);
		btnX = (Button) findViewById(R.id.search_key_X_btn);
		btnY = (Button) findViewById(R.id.search_key_Y_btn);
		btnZ = (Button) findViewById(R.id.search_key_Z_btn);
		btn0 = (Button) findViewById(R.id.search_key_0_btn);
		btn1 = (Button) findViewById(R.id.search_key_1_btn);
		btn2 = (Button) findViewById(R.id.search_key_2_btn);
		btn3 = (Button) findViewById(R.id.search_key_3_btn);
		btn4 = (Button) findViewById(R.id.search_key_4_btn);
		btn5 = (Button) findViewById(R.id.search_key_5_btn);
		btn6 = (Button) findViewById(R.id.search_key_6_btn);
		btn7 = (Button) findViewById(R.id.search_key_7_btn);
		btn8 = (Button) findViewById(R.id.search_key_8_btn);
		btn9 = (Button) findViewById(R.id.search_key_9_btn);
		btnClear = (Button) findViewById(R.id.search_key_clear_btn);
		btnDelete = (ImageButton) findViewById(R.id.search_key_delete_btn);
		btnDelete.setOnClickListener(keyClick);
		btnDelete.setOnTouchListener(keyOnTouchListener);
		btnDelete.setOnFocusChangeListener(keyOnFocusChangeListener);
		btnDelete.setNextFocusDownId(btnDelete.getId());
		btnClear.setOnClickListener(keyClick);
		btnClear.setOnTouchListener(keyOnTouchListener);
		btnClear.setOnFocusChangeListener(keyOnFocusChangeListener);
		btnClear.setNextFocusDownId(btnClear.getId());
		keyboardButtons.put("A", btnA);
		keyboardButtons.put("B", btnB);
		keyboardButtons.put("C", btnC);
		keyboardButtons.put("D", btnD);
		keyboardButtons.put("E", btnE);
		keyboardButtons.put("F", btnF);
		keyboardButtons.put("G", btnG);
		keyboardButtons.put("H", btnH);
		keyboardButtons.put("I", btnI);
		keyboardButtons.put("J", btnJ);
		keyboardButtons.put("K", btnK);
		keyboardButtons.put("L", btnL);
		keyboardButtons.put("M", btnM);
		keyboardButtons.put("N", btnN);
		keyboardButtons.put("O", btnO);
		keyboardButtons.put("P", btnP);
		keyboardButtons.put("Q", btnQ);
		keyboardButtons.put("R", btnR);
		keyboardButtons.put("S", btnS);
		keyboardButtons.put("T", btnT);
		keyboardButtons.put("U", btnU);
		keyboardButtons.put("V", btnV);
		keyboardButtons.put("W", btnW);
		keyboardButtons.put("X", btnX);
		keyboardButtons.put("Y", btnY);
		keyboardButtons.put("Z", btnZ);
		keyboardButtons.put("0", btn0);
		keyboardButtons.put("1", btn1);
		keyboardButtons.put("2", btn2);
		keyboardButtons.put("3", btn3);
		keyboardButtons.put("4", btn4);
		keyboardButtons.put("5", btn5);
		keyboardButtons.put("6", btn6);
		keyboardButtons.put("7", btn7);
		keyboardButtons.put("8", btn8);
		keyboardButtons.put("9", btn9);
		for (Button btn : keyboardButtons.values()) {
			btn.setOnClickListener(keyClick);
			btn.setOnTouchListener(keyOnTouchListener);
			btn.setOnFocusChangeListener(keyOnFocusChangeListener);
		}
	}

	private void initRecyclerView() {
		searchRecyclerView = (RecyclerView) findViewById(R.id.search_result_recycleview);
		searchRecyclerView.setOnFocusChangeListener(keyOnFocusChangeListener);
		LayoutManager layoutManager = new GridLayoutManager(this, 2);
		searchRecyclerView.setLayoutManager(layoutManager);
	}

	class KeyClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			v.requestFocus();
			v.requestFocusFromTouch();
			int id = v.getId();
			if (id == R.id.search_key_A_btn) {
				letters = letters + 'A';
			} else if (id == R.id.search_key_B_btn) {
				letters = letters + 'B';
			} else if (id == R.id.search_key_C_btn) {
				letters = letters + 'C';
			} else if (id == R.id.search_key_D_btn) {
				letters = letters + 'D';
			} else if (id == R.id.search_key_E_btn) {
				letters = letters + 'E';
			} else if (id == R.id.search_key_F_btn) {
				letters = letters + 'F';
			} else if (id == R.id.search_key_G_btn) {
				letters = letters + 'G';
			} else if (id == R.id.search_key_H_btn) {
				letters = letters + 'H';
			} else if (id == R.id.search_key_I_btn) {
				letters = letters + 'I';
			} else if (id == R.id.search_key_J_btn) {
				letters = letters + 'J';
			} else if (id == R.id.search_key_K_btn) {
				letters = letters + 'K';
			} else if (id == R.id.search_key_L_btn) {
				letters = letters + 'L';
			} else if (id == R.id.search_key_M_btn) {
				letters = letters + 'M';
			} else if (id == R.id.search_key_N_btn) {
				letters = letters + 'N';
			} else if (id == R.id.search_key_O_btn) {
				letters = letters + 'O';
			} else if (id == R.id.search_key_P_btn) {
				letters = letters + 'P';
			} else if (id == R.id.search_key_Q_btn) {
				letters = letters + 'Q';
			} else if (id == R.id.search_key_R_btn) {
				letters = letters + 'R';
			} else if (id == R.id.search_key_S_btn) {
				letters = letters + 'S';
			} else if (id == R.id.search_key_T_btn) {
				letters = letters + 'T';
			} else if (id == R.id.search_key_U_btn) {
				letters = letters + 'U';
			} else if (id == R.id.search_key_V_btn) {
				letters = letters + 'V';
			} else if (id == R.id.search_key_W_btn) {
				letters = letters + 'W';
			} else if (id == R.id.search_key_X_btn) {
				letters = letters + 'X';
			} else if (id == R.id.search_key_Y_btn) {
				letters = letters + 'Y';
			} else if (id == R.id.search_key_Z_btn) {
				letters = letters + 'Z';
			} else if (id == R.id.search_key_0_btn) {
				letters = letters + '0';
			} else if (id == R.id.search_key_1_btn) {
				letters = letters + '1';
			} else if (id == R.id.search_key_2_btn) {
				letters = letters + '2';
			} else if (id == R.id.search_key_3_btn) {
				letters = letters + '3';
			} else if (id == R.id.search_key_4_btn) {
				letters = letters + '4';
			} else if (id == R.id.search_key_5_btn) {
				letters = letters + '5';
			} else if (id == R.id.search_key_6_btn) {
				letters = letters + '6';
			} else if (id == R.id.search_key_7_btn) {
				letters = letters + '7';
			} else if (id == R.id.search_key_8_btn) {
				letters = letters + '8';
			} else if (id == R.id.search_key_9_btn) {
				letters = letters + '9';
			} else if (id == R.id.search_key_delete_btn) {
				if (letters.length() != 0) {
					letters = letters.substring(0, letters.length() - 1);
				} else {
					return;
				}
			} else if (id == R.id.search_key_clear_btn) {
				if (letters.length() == 0) {
					return;
				} else {
					letters = "";
				}
			} else {
			}
			tvLetters.setText(letters);
			KeyboardUtils.changeKeyboardView(keyboardButtons, KeyboardUtils
					.getClickableLetters(letters, allGamePinyinInfoList),
					btnDelete);
			// 如果当前输入的拼音为空
			if ("".equals(letters)) {
				tvSearchShowTv.setText(R.string.search_default_tip);
				loadRecommendGameSearchInfo();
			} else {
				tvSearchShowTv.setText(R.string.search_result_tip);
				loadGameSearchPinyin();
			}
		}
	}

	class KeyOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if (listener != null
						&& listener instanceof KeyOnFocusChangeListener) {
					((KeyOnFocusChangeListener) listener).setView(v);
				}
			}
			return false;
		}
	}

	class KeyOnFocusChangeListener implements OnFocusChangeListener {

		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (v.getId() == R.id.search_result_recycleview) {
					System.out.println("[onFocusChange]:"
							+ BaseApplication.position);
					// 用于处理焦点问题
					if (BaseApplication.position > gameSearchPinyinInfoList
							.size() - 1) {
						BaseApplication.position = gameSearchPinyinInfoList
								.size() - 1;
					}
					searchRecyclerView
							.smoothScrollToPosition(BaseApplication.position);
					View view = searchRecyclerView.getLayoutManager()
							.findViewByPosition(BaseApplication.position);
					if (view != null) {
						view.requestFocus();
					}
				}
				if(v instanceof Button){
					if(KeyboardUtils.hasUsableKeysBelow(keyboardButtons,(Button)v)){
						v.setNextFocusDownId(0);
					}else{
						v.setNextFocusDownId(btnClear.getId());
					};
				}
				
				if(v.getId() == btn3.getId()){
					if (KeyboardUtils.SearshUpKeyFocus(keyboardButtons, btn3)){
						btn3.setNextFocusUpId(btnX.getId());
					}
				}
				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
				}
			}
		}

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}
	}

	/**
	 * 
	 * @Title: loadGameSearchPinyin
	 * @Description: TODO(加载输入拼音后的搜索结果)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void loadGameSearchPinyin() {
		// 第一次进入，会显示加载界面
		if (isFirstLoadPinyin) {
			loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);
		}

		// 数据库是否为空
		boolean isNoData = DataFetcher
				.isGameSearchPinyinDataExist(SearchActivity.this);
		// 如果本地没有数据或者，如果是刚刚进入到该界面时
		if (isNoData || isFirstLoadPinyin) {
			if (isFirstLoadPinyin) {
				isFirstLoadPinyin = false;
			}
			// 从服务器取所有数据
			ReqCallback<List<GameSearchPinyinInfo>> reqCallback = new ReqCallback<List<GameSearchPinyinInfo>>() {
				@Override
				public void onResult(
						TaskResult<List<GameSearchPinyinInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					if (code == TaskResult.OK) {
						List<GameSearchPinyinInfo> infos = taskResult.getData();
						alog.info("[infos]1" + infos.toString());
						if (infos != null && !infos.isEmpty()) {
							alog.info("[loadGameSearchPinyin]1"
									+ infos.toString());
							allGamePinyinInfoList = infos;
							// 加载到拼音数据后，再加载推荐的游戏
							loadRecommendGameSearchInfo();
							loadingView.getmHandler().sendEmptyMessage(
									Constant.DISMISSLOADING);
						} else { // 如果没有请求到数据，显示没有数据
							loadingView.getmHandler().sendEmptyMessage(
									Constant.NULLDATA);
						}
					} else {
						// 网络正常
						if (NetUtil.isNetworkAvailable(SearchActivity.this,
								true)) {
							loadingView.getmHandler().sendEmptyMessage(
									Constant.NULLDATA);
						} else {
							loadingView.getmHandler().sendEmptyMessage(
									Constant.EXCEPTION);
						}
					}
					handler.sendEmptyMessage(MSG_GAMEPINYIN_NET);
				}

				@Override
				public void onUpdate(
						TaskResult<List<GameSearchPinyinInfo>> taskResult) {
					alog.info("onUpdate1");
				}
			};
			DataFetcher
					.getGameSearchPinyin(SearchActivity.this, reqCallback, true)
					.registerUpdateListener(reqCallback)
					.request(SearchActivity.this);
		} else {
			// 根据拼音搜索游戏
			String pinyin = letters;
			alog.info("pinyin:" + pinyin);
			if ("".equals(pinyin)) {
				return;
			}

			ReqCallback<List<GameSearchPinyinInfo>> reqCallback = new ReqCallback<List<GameSearchPinyinInfo>>() {
				@Override
				public void onResult(
						TaskResult<List<GameSearchPinyinInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("[loadGameSearchPinyin2]taskResult code:" + code);
					if (code == TaskResult.OK) {
						List<GameSearchPinyinInfo> infos = taskResult.getData();
						alog.info("[infos]2" + infos.toString());
						if (infos != null && !infos.isEmpty()) {
							alog.info(infos.toString());
							gameSearchPinyinInfoList = infos;
							handler.sendEmptyMessage(MSG_GAMEPINYIN_LOCAL);
						}
					}
				}

				@Override
				public void onUpdate(
						TaskResult<List<GameSearchPinyinInfo>> taskResult) {
					alog.info("onUpdate2");
				}
			};
			DataFetcher
					.getGameSearchPinyinByInput(SearchActivity.this, pinyin,
							reqCallback, false)
					.registerUpdateListener(reqCallback)
					.request(SearchActivity.this);
		}
	}

	/**
	 * 
	 * @Title: loadRecommendGameSearchInfo
	 * @Description: TODO(加载推荐游戏信息)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void loadRecommendGameSearchInfo() {
		ReqCallback<List<GameSearchPinyinInfo>> reqCallback = new ReqCallback<List<GameSearchPinyinInfo>>() {
			@Override
			public void onResult(
					TaskResult<List<GameSearchPinyinInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("code:" + code);
				if (code == TaskResult.OK) {
					List<GameSearchPinyinInfo> infos = taskResult.getData();
					alog.info("[loadRecommendGameSearchInfo]taskResult code:"
							+ code + " data:" + taskResult.getData().toString());
					if (infos != null && !infos.isEmpty()) {
						alog.info("[loadRecommendGameSearchInfo]"
								+ infos.toString());
						gameSearchPinyinInfoList = infos;
						handler.sendEmptyMessage(MSG_GAMERECOMMEN);
					}
				}
			}

			@Override
			public void onUpdate(
					TaskResult<List<GameSearchPinyinInfo>> taskResult) {

			}
		};
		DataFetcher.getRecommendGameSearchInfo(SearchActivity.this,
				reqCallback, true).request(SearchActivity.this);

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		if (action == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BUTTON_X
					|| keyCode == KeyEvent.KEYCODE_X
					|| keyCode == KeyEvent.KEYCODE_MENU) {
				if (contentView.getVisibility() == View.INVISIBLE) {
					alog.info("loadingView.VISIBLE == View.GONE");
					//loadMain();
					loadGameSearchPinyin();
				} else {
					// 如果当前输入的拼音为空
					if ("".equals(letters)) {
						loadRecommendGameSearchInfo();
					} else {
						loadGameSearchPinyin();
					}
				}
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	// 显示加载信息
	private void showLoading() {
		loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);
	}

	// 加载信息消失
	private void dismissLoading() {
		loadingView.getmHandler().sendEmptyMessage(Constant.DISMISSLOADING);
	}

	/**
	 * 
	 * @Title: handleKeyboardView
	 * @Description: TODO(键盘的显示情况)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void handleKeyboardView() {
		// 当前键盘上可点击的字母
		String clickableLetters = KeyboardUtils.getClickableLetters(letters,
				allGamePinyinInfoList);
		// 更新当前键盘的显示视图
		KeyboardUtils.changeKeyboardView(keyboardButtons, clickableLetters,
				btnDelete);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		if (myNetReceiver != null) {
//			unregisterReceiver(myNetReceiver);
//		}
		
		if(bitmap != null && !bitmap.isRecycled()){
			bitmap.recycle();
		}
	}

	/**
	 * 监听网络
	 */
//	private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			alog.info("onReceive");
//			String action = intent.getAction();
//			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//				mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//				netInfo = mConnectivityManager.getActiveNetworkInfo();
//				if (netInfo != null && netInfo.isAvailable()) {
//					alog.info("net conn");
//				//	loadMain();
//					loadGameSearchPinyin();
//				} else {
//				}
//			}
//
//		}
//	};
}
