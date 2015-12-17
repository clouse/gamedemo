package com.atet.tvmarket.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothInputDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.atet.common.logging.ALog;

public class GamepadConnectHelper {
	private ALog alog = ALog.getLogger(GamepadConnectHelper.class);
	private static final String TAG = "GamepadConnectService";
	private boolean mIsAutoEnableBT = true;
	private static final int TIME_OUT_HANDLER = 1;
	private static final int SCAN_TIME_OUT = 20;
	public static final int ATET_DEVICE_CLASS = 1480;
	public static final int SKYWORTH_DEVICE_CLASS = 1288;
	public static final int ATETBJ9_DEVICE_CLASS = 1288;
	public static int CURRENT_DEVICE_CLASS = 0;
	private Context mContext;
	private OpState mOpState = OpState.OP_INIT;
	private BluetoothAdapter mAdapter;
	private BluetoothInputDevice mService;
	// 是否初始化后移除配对但没连接的手柄
	private boolean mIsRemoveBondedBTgamepad = false;
	private boolean mIsInited = false;
	// 是否自动打开蓝牙
	private String mConnectingDeviceAddr = null;
	private OnGamepadConnectListener mListener;
	private boolean mIsGetProfile = false;
	public static List<Integer> deviceClasses = new ArrayList<Integer>();

	/**
	 * 操作状态
	 */
	private enum OpState {
		OP_INIT, OP_SCANNING_DEVICES, OP_PAIRING, OP_CONNECTING
	};

	public GamepadConnectHelper(Context context,
			OnGamepadConnectListener listener) {
		mContext = context;
		mListener = listener;
	}

	/**
	 * 初始化
	 * 
	 * @throws
	 * @author:LiuQin
	 * @date 2015-1-14
	 */
	public void init() {
		if (mIsInited) {
			alog.error("[initService] already init");
			return;
		}
		initBlueTooth();
		mIsInited = true;
	}

	/**
	 * 回收资源
	 * 
	 * @throws
	 * @author:LiuQin
	 * @date 2015-1-14
	 */
	public void recycle() {
		stopScanBTdevice();
		destroyBlueTooth();
		mContext = null;
	}

	public boolean isServiceConnected() {
		return mIsInited && mService != null;
	}

	/**
	 * 开户蓝牙扫描
	 * 
	 * @throws
	 * @author:LiuQin
	 * @date 2015-1-14
	 */
	public void startScanBTdevice() {
		if (!mIsInited) {
			alog.error("[scanBTdevice] Error:not init", null);
			return;
		}
		if (mOpState != OpState.OP_INIT && mService != null) {
			alog.warn("[startScanBTdevice] mOpState not init:" + mOpState);
			return;
		}

		startBtDiscovery();
	}

	/**
	 * 关闭蓝牙扫描
	 * 
	 * @throws
	 * @author:LiuQin
	 * @date 2015-1-14
	 */
	public void stopScanBTdevice() {
		mOpState = OpState.OP_INIT;
		mConnectingDeviceAddr = null;
		mHandler.removeMessages(TIME_OUT_HANDLER);
		cancelBtDiscovery();
	}

	public boolean isRemoveBondedBTgamepad() {
		return mIsRemoveBondedBTgamepad;
	}

	/**
	 * 连接蓝牙服务后是否取消未连接的配对手柄
	 * 
	 * @param isRemoveBondedBTgamepad
	 * @throws
	 * @author:LiuQin
	 * @date 2015-1-14
	 */
	public void setRemoveBondedBTgamepad(boolean isRemoveBondedBTgamepad) {
		this.mIsRemoveBondedBTgamepad = isRemoveBondedBTgamepad;
	}

	private void initBlueTooth() {
		// 初始化广播接收器
		initBroadcastReceiver();
		connectBlueToothService();
	}

	/**
	 * 注册广播，接收蓝牙事件
	 * 
	 * @throws
	 */
	private void initBroadcastReceiver() {
		// 设置广播信息过滤
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND); // 远程蓝牙设备被发现
		// intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); // 远程蓝牙设备bond状态发生改变
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); // 蓝牙适配器状态发生改变
		intentFilter
				.addAction(BluetoothInputDevice.ACTION_CONNECTION_STATE_CHANGED);
		// intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		// intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		mContext.registerReceiver(receiver, intentFilter);
	}

	@SuppressLint("NewApi")
	private void destroyBlueTooth() {
		try {
			if (receiver != null) {
				mContext.unregisterReceiver(receiver);
			}
			if (mService != null && mAdapter != null) {
				mAdapter.closeProfileProxy(4, mService);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 连接到蓝牙服务
	 * 
	 * @throws
	 */
	@SuppressLint("NewApi")
	private void connectBlueToothService() {
		try {
			if (mAdapter == null) {
				mAdapter = BluetoothAdapter.getDefaultAdapter();
			}

			boolean isBTenable = mAdapter.isEnabled();
			alog.info("[connectBlueToothService] isBTenable:" + isBTenable
					+ " mIsAutoEnableBT:" + mIsAutoEnableBT);
			if (isBTenable && !mIsGetProfile) {
				mIsGetProfile = true;
				mAdapter.getProfileProxy(mContext,
						new InputDeviceServiceListener(), 4);
			}
			if (mIsAutoEnableBT && !isBTenable) {
				// 打开蓝牙
				alog.info("[connectBlueToothService] enable bt");
				mAdapter.enable();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			notSupportBThandle();
		}
	}

	/**
	 * These callbacks run on the main thread
	 */
	@SuppressLint("NewApi")
	private final class InputDeviceServiceListener implements
			BluetoothProfile.ServiceListener {

		// service连接时回调
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			alog.info("[onServiceConnected] Bluetooth service connected");
			mService = (BluetoothInputDevice) proxy;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (mIsRemoveBondedBTgamepad) {
						rmAllBondedButNotConnectedGamepads();
					}

					if (mListener != null) {
						mListener.onServiceConnected();
					}
				}
			}, 300);
		}

		@Override
		public void onServiceDisconnected(int profile) {
			// TODO Auto-generated method stub
			alog.warn("[onServiceDisconnected] ");
			mService = null;
			mIsGetProfile = false;
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			if (what == TIME_OUT_HANDLER) {
				if (!mIsInited || mOpState == OpState.OP_INIT) {
					return;
				}
				alog.warn("[handleMessage] scanning time out:" + SCAN_TIME_OUT);

				stopScanBTdevice();
				if (mListener != null) {
					mListener.onScanTimeout();
				}
				// cancelBtDiscovery();
				// startBtDiscovery();
			}
		}
	};

	/**
	 * 此广播用来监听手柄的连接状态(即蓝牙连接状态)
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				String action = intent.getAction();
				alog.debug("[onReceive]action:" + action);
				// 如果正在操作手柄并且蓝牙设备被找到
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// 找到新设备
					if (mOpState != OpState.OP_SCANNING_DEVICES) {
						return;
					}

					final BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (isAtetGamepad(device)) {
						alog.info("[onReceive] gamepad found,name:"
								+ device.getName() + " addr:"
								+ device.getAddress());
						if (isConnecting()) {
							if (isConnectingDevice(device)) {
								alog.warn("[onReceive] the same device found again");
								return;
							}
							alog.warn("[onReceive] wait to previous connecting");
							return;
						}

						// 取消搜索
						cancelBtDiscovery();

						mOpState = OpState.OP_PAIRING;
						mConnectingDeviceAddr = device.getAddress();
						// 当前的绑定状态
						int bondState = device.getBondState();
						// 如果已经绑定
						if (bondState == BluetoothDevice.BOND_BONDED) {
							// 取消配对
							alog.info("[unpairIfDisconnected] unpair it before connect");
							unpairDevice(device);
						} else if (bondState == BluetoothDevice.BOND_NONE) {
							alog.info("[unpairIfDisconnected] bound none");
							pairDevice(device);
						} else if (bondState == BluetoothDevice.BOND_BONDING) {
							// 如果正在绑定
							alog.info("[unpairIfDisconnected] bonding");
						} else {
							alog.info("[unpairIfDisconnected] bond state:"
									+ bondState);
						}
					} else {
						alog.debug("[onReceive] bt device found,name:"
								+ device.getName() + " addr:"
								+ device.getAddress());
					}
				} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED
						.equals(action)) {
					if (mOpState != OpState.OP_PAIRING) {
						return;
					}

					final BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (isAtetGamepad(device)) {
						alog.info("[onReceive] gamepad bond state change,name:"
								+ device.getName() + " addr:"
								+ device.getAddress());

						if (!isConnectingDevice(device)) {
							alog.warn("[onReceive] wait to previous device connecting");
							return;
						}

						// 确保搜索已取消
						cancelBtDiscovery();

						// 当前的绑定状态
						int bondState = device.getBondState();
						// 如果已经绑定
						if (bondState == BluetoothDevice.BOND_BONDED) {
							alog.info("[onReceive] target device bonded");
							mOpState = OpState.OP_CONNECTING;
							connect(device);
						} else if (bondState == BluetoothDevice.BOND_NONE) {
							alog.info("[onReceive] target device bond none");
							mHandler.postDelayed(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									// 绑定
									pairDevice(device);
								}
							}, 100);
						} else if (bondState == BluetoothDevice.BOND_BONDING) {
							alog.info("[onReceive] target device bonding");
						}
						// else if (bondState == BluetoothDevice.BOND_SUCCESS) {
						// alog.info("[onReceive] target device bond success");
						// }
					}
					// 如果蓝牙状态发生改变
				} else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
					// 蓝牙打开或关闭
					int state = intent.getIntExtra(
							BluetoothAdapter.EXTRA_STATE, 0);
					int preState = intent.getIntExtra(
							BluetoothAdapter.EXTRA_PREVIOUS_STATE, 0);
					if (state == BluetoothAdapter.STATE_ON) {
						// 如果蓝牙适配器处于开启状态
						alog.info("[onReceive] bluetooth state change->turn on");
						if (mService == null) {
							connectBlueToothService();
						} else if (mOpState == OpState.OP_SCANNING_DEVICES) {
							startBtDiscovery();
						}
					} else if (state == BluetoothAdapter.STATE_OFF) {
						// 蓝牙状态属于关闭状态
						alog.info("[onReceive] bluetooth state chnage->turn off");
						if (mOpState == OpState.OP_SCANNING_DEVICES) {
							startBtDiscovery();
						}
					}
				} else if (BluetoothInputDevice.ACTION_CONNECTION_STATE_CHANGED
						.equals(action)) {
					final BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (!isAtetGamepad(device)) {
						return;
					}
					int newState = intent.getIntExtra(
							BluetoothProfile.EXTRA_STATE, 0);
					int oldState = intent.getIntExtra(
							BluetoothProfile.EXTRA_PREVIOUS_STATE, 0);
					alog.info("connect state change,newState:" + newState
							+ " oldState:" + oldState);
					if (newState == BluetoothProfile.STATE_CONNECTED) {
						alog.warn("Success to connect bluetooth device:"
								+ device);
						deviceConnectedHandle(device);
					} else if (newState == BluetoothProfile.STATE_CONNECTING) {
						// 如果是正在连接状态
						alog.warn("[onReceive] connection state chnage->STATE_CONNECTING,device:"
								+ device);
						mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								if (isBTdeviceConnected(device)) {
									deviceConnectedHandle(device);
								} else {
									// 开始蓝牙搜索
									// startBtDiscovery();
									deviceDisconnectedHandle(device);
								}
							}
						}, 3000);
					} else if (newState == BluetoothProfile.STATE_DISCONNECTED
							&& oldState == BluetoothProfile.STATE_CONNECTING) {
						alog.error("[onReceive] Failed to connect device:"
								+ device, null);
						deviceDisconnectedHandle(device);
					} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
						alog.warn("[onReceive] device disconnected:" + device);
						deviceDisconnectedHandle(device);
					} else {
						alog.debug("[onReceive] not handle");
					}
				} else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
					final BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (!isAtetGamepad(device)) {
						return;
					}
					alog.info("[onReceive] ACTION_ACL_CONNECTED,device:"
							+ device);
					deviceConnectedHandle(device);
				} else if (BluetoothDevice.ACTION_ACL_DISCONNECTED
						.equals(action)) {
					final BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (!isAtetGamepad(device)) {
						return;
					}
					alog.info("[onReceive] ACTION_ACL_DISCONNECTED,device:"
							+ device);
					deviceDisconnectedHandle(device);
				} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
						.equals(action)) {
					alog.warn("[onReceive] ACTION_DISCOVERY_FINISHED");
					if (mOpState == OpState.OP_SCANNING_DEVICES) {
						alog.info("[onReceive] timeout handle");
						mHandler.removeMessages(TIME_OUT_HANDLER);
						mHandler.sendEmptyMessageDelayed(TIME_OUT_HANDLER, 800);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	};

	/**
	 * 
	 * @Title: rmAllBondedButNotConnectedGamepads
	 * @Description: TODO删除所有已经绑定但没有连接的手柄
	 * @param
	 * @return void
	 * @throws
	 */
	public void rmAllBondedButNotConnectedGamepads() {
		alog.info("[rmAllBondedButNotConnectedGamepads] ");
		if (mAdapter == null) {
			return;
		}

		Set<BluetoothDevice> bondedDevices = mAdapter.getBondedDevices(); // 所有已经绑定的蓝牙设备
		alog.info("[rmAllBondedButNotConnectedGamepads] size:"
				+ bondedDevices.size());
		if (bondedDevices.size() > 0) {
			Method removeBondMethod = null;
			try {
				removeBondMethod = BluetoothDevice.class
						.getMethod("removeBond");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			List<BluetoothDevice> connectedDevices = getConnectedBluetoothDevice(); // 获取已经连接的蓝牙设备

			for (Iterator iterator = bondedDevices.iterator(); iterator
					.hasNext();) {
				BluetoothDevice device = (BluetoothDevice) iterator.next();
				if (connectedDevices.contains(device)) {
					continue;
				}
				if (isAtetGamepad(device)) {
					boolean result = false;
					// result=device.removeBond();
					try {
						removeBondMethod.invoke(device); // 在所有已经绑定，但没有连接的蓝牙设备中，如果是手柄，则移除掉
					} catch (Exception e) {
						e.printStackTrace();
					}
					alog.info("[rmAllBondedButNotConnectedGamepads] remove device:"
							+ device.getName()
							+ " addr:"
							+ device.getAddress()
							+ " result:" + result);
				}
			}
		}
	}

	public int getConnectedDeviceCount() {
		List<BluetoothDevice> devices = getConnectedBluetoothDevice();
		deviceClasses.clear();
		if (devices == null) {
			return 0;
		}
		if (deviceClasses == null || deviceClasses.size() == 0) {
			for (int i = 0; i < devices.size(); i++) {
				alog.info("device" + i + ":"
						+ devices.get(i).getBluetoothClass().getDeviceClass());
				deviceClasses.add(devices.get(i).getBluetoothClass()
						.getDeviceClass());
			}
		}
		return (devices == null) ? 0 : devices.size();
	}

	/**
	 * 
	 * @Title: getConnectedBluetoothDevice
	 * @Description: TODO 获取已经连接的手柄
	 * @param @return
	 * @return List<BluetoothDevice>
	 * @throws
	 */
	public List<BluetoothDevice> getConnectedBluetoothDevice() {
		if (mService == null) {
			alog.info("[getConnectedBluetoothDevice] mService null");
			return null;
		}

		List<BluetoothDevice> devices = mService.getConnectedDevices();
		List<BluetoothDevice> connectedDevices = new ArrayList<BluetoothDevice>();
		for (BluetoothDevice bluetoothDevice : devices) {
			if (isAtetGamepad(bluetoothDevice)) { // 如果已经连接的蓝牙设备是手柄则添加到已连接列表中
				alog.info("[getConnectedBluetoothDevice] add device:"
						+ bluetoothDevice.getName() + " addr:"
						+ bluetoothDevice.getAddress());
				connectedDevices.add(bluetoothDevice);
			}
		}
		return connectedDevices;
	}

	/**
	 * 
	 * @Title: isGamePad
	 * @Description: TODO 判断是否为蓝牙设备
	 * @param @param device
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	@SuppressLint("NewApi")
	private boolean isAtetGamepad(BluetoothDevice device) {
		if (device == null) {
			return false;
		}
		boolean result = false;
		int deviceClass = device.getBluetoothClass().getDeviceClass();// 设备类对象
		if (deviceClass == ATET_DEVICE_CLASS) {
			CURRENT_DEVICE_CLASS = ATET_DEVICE_CLASS;
			result = true;
		} else if (deviceClass == SKYWORTH_DEVICE_CLASS) {
			CURRENT_DEVICE_CLASS = SKYWORTH_DEVICE_CLASS;
			alog.info("[isAtetGamepad] found skyworth device");
			result = true;
		}

		alog.info("[isAtetGamepad] " + result + " device name:"
				+ device.getName() + " addr:" + device.getAddress()
				+ " deviceClass:" + deviceClass);
		return result;
	}

	private boolean isConnecting() {
		return mConnectingDeviceAddr != null;
	}

	private boolean isConnectingDevice(BluetoothDevice device) {
		String connectingDeviceAddr = mConnectingDeviceAddr.toString();
		if (device == null || TextUtils.isEmpty(connectingDeviceAddr)) {
			return false;
		}
		if (connectingDeviceAddr.equals(device.getAddress())) {
			return true;
		}

		return false;
	}

	private void deviceConnectedHandle(BluetoothDevice device) {
		stopScanBTdevice();
		if (mListener != null) {
			mListener.onAtetBTgamepadConnected(device);
		}
	}

	private void deviceDisconnectedHandle(BluetoothDevice device) {
		if (mListener != null) {
			mListener.onAtetBTgamepadDisconnected(device);
		}
		// startBtDiscovery();
	}

	/**
	 * 
	 * @Title: startBtDiscovery
	 * @Description: TODO开始搜索蓝牙设备
	 * @param
	 * @return void
	 * @throws
	 */
	private void startBtDiscovery() {
		boolean result = false;
		mConnectingDeviceAddr = null;
		mOpState = OpState.OP_SCANNING_DEVICES;

		if (mAdapter != null && !mAdapter.isEnabled()) {
			alog.info("[startBtDiscovery] bluetooth disabled,enable it now");
			if (mAdapter.enable()) {
				stopScanBTdevice();
				if (mListener != null) {
					mListener.onNotSupportBT();
				}
				return;
			}
			mHandler.removeMessages(TIME_OUT_HANDLER);
			mHandler.sendEmptyMessageDelayed(TIME_OUT_HANDLER,
					SCAN_TIME_OUT * 1000);
			alog.warn("[startBtDiscovery] set timeout");
			return;
		}

		if (mAdapter != null && !mAdapter.isDiscovering()) {
			mHandler.removeMessages(TIME_OUT_HANDLER);
			mHandler.sendEmptyMessageDelayed(TIME_OUT_HANDLER,
					SCAN_TIME_OUT * 1000);
			alog.warn("[startBtDiscovery] set timeout");
			mAdapter.startDiscovery();
			result = true;
		}
		alog.info("[startBtDiscovery] " + result);
	}

	private void cancelBtDiscovery() {
		boolean result = false;
		if (mAdapter != null && mAdapter.isDiscovering()) {
			mAdapter.cancelDiscovery();
			result = true;
		}
		alog.info("[cancelDiscovery] " + result);
	}

	/**
	 * 
	 * @Title: unpairDevice
	 * @Description: TODO 取消蓝牙配对
	 * @param @param device
	 * @return void
	 * @throws
	 */
	private void unpairDevice(BluetoothDevice device) {
		if (device == null) {
			return;
		}
		Method removeBondMethod = null;
		try {
			removeBondMethod = BluetoothDevice.class.getMethod("removeBond");
			removeBondMethod.invoke(device); // 删除已经绑定的蓝牙设备
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private boolean isBTdeviceConnected(BluetoothDevice device) {
		if (device == null || mService == null) {
			return false;
		}
		int connectedState = mService.getConnectionState(device);
		// 如果已经连接或者正在连接
		if (connectedState == BluetoothProfile.STATE_CONNECTED
				|| connectedState == BluetoothProfile.STATE_CONNECTING) {
			alog.info("[isBTdeviceConnected] already connected");
			return true;
		}

		return false;
	}

	/**
	 * 配对蓝牙
	 * 
	 * @param device
	 * @throws
	 * @author:LiuQin
	 * @date 2015-1-14
	 */
	private void pairDevice(BluetoothDevice device) {
		if (device == null) {
			return;
		}
		// 配对
		try {
			alog.warn("[pairDevice] pair to " + device.getName());
			Method createBondMethod = BluetoothDevice.class
					.getMethod("createBond");
			createBondMethod.invoke(device);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 连接蓝牙设备
	 * 
	 * @param device
	 * @return
	 * @throws
	 */
	public boolean connect(BluetoothDevice device) {
		if (mService == null || device == null) {
			alog.error("[connect] error:mService null", null);
			return false;
		}
		alog.info("[connect] to " + device.getName());
		return mService.connect(device);
	}

	/**
	 * 不支持蓝牙处理
	 * 
	 * @author:LiuQin
	 * @date 2015-1-14
	 */
	private void notSupportBThandle() {
		// NewToast.makeToast(mContext, "Error:This device not support bluetooth",
		// Toast.LENGTH_LONG).show();
		if (mListener != null) {
			mListener.onNotSupportBT();
		}
	}

	public boolean ismAutoEnableBT() {
		return mIsAutoEnableBT;
	}

	public void setIsAutoEnableBT(boolean mIsAutoEnableBT) {
		this.mIsAutoEnableBT = mIsAutoEnableBT;
	}

	// 添加新设备
	public static void addNewDevice(Integer deviceClass) {
		if (deviceClasses.contains(deviceClass)) {
			deviceClasses.remove(deviceClass);
		}
		deviceClasses.add(deviceClass);
	}

	// 删除设备
	public static void removeDevice(Integer deviceClass) {
		if (deviceClasses.contains(deviceClass)) {
			deviceClasses.remove(deviceClass);
		}
	}

	public static interface OnGamepadConnectListener {
		/**
		 * 服务连接回调
		 * 
		 * @throws
		 * @author:LiuQin
		 * @date 2015-1-15
		 */
		public void onServiceConnected();

		/**
		 * 使用蓝牙服务失败
		 * 
		 * @throws
		 * @author:LiuQin
		 * @date 2015-1-15
		 */
		public void onNotSupportBT();

		/**
		 * 手柄连上的回调
		 * 
		 * @param device
		 * @throws
		 * @author:LiuQin
		 * @date 2015-1-15
		 */
		public void onAtetBTgamepadConnected(BluetoothDevice device);

		/**
		 * 手柄断开后的回调
		 * 
		 * @param device
		 * @throws
		 * @author:LiuQin
		 * @date 2015-1-15
		 */
		public void onAtetBTgamepadDisconnected(BluetoothDevice device);

		/**
		 * 蓝牙搜索超时回调
		 * 
		 * @throws
		 * @author:LiuQin
		 * @date 2015-1-15
		 */
		public void onScanTimeout();
	}
}
