package com.atet.tvmarket.control.mygame.update;

import java.lang.reflect.Field;

import com.atet.tvmarket.entity.AutoType;


public class HttpReqParams implements AutoType{
	// 游戏专题ID
	private String typeId;
	// 游戏ID
	private String gameId;
	// 游戏关键字
	private String keyWord;
	// 用户ID
	private Integer userId;
	// 游戏评论的内容
	private String commContent;
	// 广场板块ID
	private Integer plateId;
	// 广场帖子ID
	private Integer postId;
	// 意见反馈的内容
	private Integer suggestType;
	// 昵称
	private String nickName;
	// 用户名
	private String userName;
	// 用户头像
	private String avator;
	// 绑定的电话号，用于找回密码
	private String phtone;
	// 用户邮箱地址
	private String email;

	// 请求列表数据每页显示的数量
	private Integer pageSize;
	// 请求列表数据的页码
	private Integer currentPage;
	// 帖子回复
	private String content;
	// 意见反馈中反馈联系人的联系方式
	private String contact;
	// 设备型号标识，后台根据此数据获取改设备所属公司下的游戏专题
	private String deviceCode;
	// 图片的规格类型
	private String sizeTypeCode;
	// 图片的用途类型
	private String useTypeCode;
	// 要求的数量
	private int number;
	// 游戏的下载地址
	private String url;

	private String ids;// 第三方游戏通过id获取游戏信息

	// 中间下载地址
	private Integer downToken;
	private String packageNames;
	// 游戏ID 获取下载地址
	private String id;
	private String deviceId;
	private Integer deviceType;
	private Long startTime;
	private Long endTime;
	private Long duration;
	private String sdCard;
	private String blueToothMac;
	private String cpu;
	private String gpu;
	private String ram;
	private String rom;
	private String versionCode;
	private String sdkVersion;
	private String resolution;
	private Integer dpi;
	private Long installTime;
	private Integer isFirstUpload;

	private String loginName;
	private String password;
	private String newPassword;
	private String oldPassword;
	private String confirmPassword;
	private String chineseName;
	private String phone;
	private Integer sex;
	private String birthday;
	private String address;
	private String qq;
	private String wechat;

	private String gameType;
	private String videoType;// 视频类型

	/**
	 * 新增自段，服务器后台生成用于确保设备的唯一性
	 * 
	 * @add zhaominglai
	 * @date 2014/9/28
	 * */
	private String atetId;
	private String channelId;// 渠道ID，预留字段，值设置为0
	private String productId;// 终端设备的唯一标识。

	private String cpId;
	private Long enterTime;
	private Long exitTime;
	private String gameName;

	private String packageName;// 游戏包名

	private Long minTime;// 公告接口当中用来标志上次请求时间的标志

	private String modelId;// 广告模板id

	private String giftPackageid;

	private Integer type; // 平台类型 1为TV,2为手机,3为平板

	private Integer noticeType;

	public void setNoticeType(Integer noticeType) {
		this.noticeType = noticeType;
	}

	public Integer getNoticeType() {
		return noticeType;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public HttpReqParams() {

	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setCommContent(String commContent) {
		this.commContent = commContent;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public void setPlateId(Integer plateId) {
		this.plateId = plateId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public void setSuggestType(Integer suggestType) {
		this.suggestType = suggestType;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setAvator(String avator) {
		this.avator = avator;
	}

	public void setPhtone(String phtone) {
		this.phtone = phtone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String getSizeTypeCode() {
		return sizeTypeCode;
	}

	public void setSizeTypeCode(String sizeTypeCode) {
		this.sizeTypeCode = sizeTypeCode;
	}

	public String getUseTypeCode() {
		return useTypeCode;
	}

	public void setUseTypeCode(String useTypeCode) {
		this.useTypeCode = useTypeCode;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getDownToken() {
		return downToken;
	}

	public void setDownToken(Integer downToken) {
		this.downToken = downToken;
	}

	public String getPackageNames() {
		return packageNames;
	}

	public void setPackageNames(String packageNames) {
		this.packageNames = packageNames;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWechat() {
		return wechat;
	}

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getNickName() {
		return nickName;
	}

	public String getUserName() {
		return userName;
	}

	public String getEmail() {
		return email;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStartTime() {
		return this.startTime;
	}

	public long getEndTime() {
		return this.endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getGiftPackageid() {
		return giftPackageid;
	}

	public void setGiftPackageid(String giftPackageid) {
		this.giftPackageid = giftPackageid;
	}

	public byte[] toJsonParam() {
		Field[] fields = this.getClass().getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		final int length = fields.length;
		for (int i = 0; i < length; i++) {
			Field field = fields[i];

			Object value = null;
			try {
				value = field.get(this);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			if (value != null) {
				// 把属性名先拼接进去
				sb.append(field.getName()).append(":");
				if (isInteger(field)) {
					sb.append((Integer) value);
				} else if (isLong(field)) {
					sb.append((Long) value);
				} else {

					sb.append("\"").append((String) value).append("\"");
				}
				// 多个属性之间用“，”号来连接
				sb.append(",");
			}
		}
		// 删除最后一个逗号
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("}");
//		if (Configuration.IS_DEBUG_ENABLE) {
//			DebugTool.debug(Configuration.DEBUG_TAG, "请求参数为：" + sb.toString());
//		}
		return sb.toString().getBytes();
	}

	/**
	 * 判断是否为整形
	 * 
	 * @param item
	 * @return
	 */
	private static boolean isInteger(Field item) {
		if (item.getType().getName().toString().equals("int")
				|| item.getType().getName().toString()
						.equals("java.lang.Integer")) {
			return true;
		}
		return false;
	}

	private static boolean isLong(Field item) {
		if (item.getType().getName().toString().equals("long")
				|| item.getType().getName().toString().equals("java.lang.Long")) {
			return true;
		}
		return false;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public long getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(long enterTime) {
		this.enterTime = enterTime;
	}

	public long getExitTime() {
		return exitTime;
	}

	public void setExitTime(long exitTime) {
		this.exitTime = exitTime;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Long getMinTime() {
		return minTime;
	}

	public void setMinTime(Long minTime) {
		this.minTime = minTime;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getSdCard() {
		return sdCard;
	}

	public void setSdCard(String sdCard) {
		this.sdCard = sdCard;
	}

	public String getBlueToothMac() {
		return blueToothMac;
	}

	public void setBlueToothMac(String blueToothMac) {
		this.blueToothMac = blueToothMac;
	}

	public String getGpu() {
		return gpu;
	}

	public void setGpu(String gpu) {
		this.gpu = gpu;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public String getRom() {
		return rom;
	}

	public void setRom(String rom) {
		this.rom = rom;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public int getDpi() {
		return dpi;
	}

	public void setDpi(Integer dpi) {
		this.dpi = dpi;
	}

	public Long getInstallTime() {
		return installTime;
	}

	public void setInstallTime(Long installTime) {
		this.installTime = installTime;
	}

	public String getAtetId() {
		return atetId;
	}

	public void setAtetId(String atetId) {
		this.atetId = atetId;
	}

	public Integer getIsFirstUpload() {
		return isFirstUpload;
	}

	public void setIsFirstUpload(Integer isFirstUpload) {
		this.isFirstUpload = isFirstUpload;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	@Override
	public String toString() {
		return "HttpReqParams [typeId=" + typeId + ", gameId=" + gameId
				+ ", keyWord=" + keyWord + ", userId=" + userId
				+ ", commContent=" + commContent + ", plateId=" + plateId
				+ ", postId=" + postId + ", suggestType=" + suggestType
				+ ", nickName=" + nickName + ", userName=" + userName
				+ ", avator=" + avator + ", phtone=" + phtone + ", email="
				+ email + ", pageSize=" + pageSize + ", currentPage="
				+ currentPage + ", content=" + content + ", contact=" + contact
				+ ", deviceCode=" + deviceCode + ", sizeTypeCode="
				+ sizeTypeCode + ", useTypeCode=" + useTypeCode + ", number="
				+ number + ", url=" + url + ", ids=" + ids + ", downToken="
				+ downToken + ", packageNames=" + packageNames + ", id=" + id
				+ ", deviceId=" + deviceId + ", deviceType=" + deviceType
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", duration=" + duration + ", sdCard=" + sdCard
				+ ", blueToothMac=" + blueToothMac + ", cpu=" + cpu + ", gpu="
				+ gpu + ", ram=" + ram + ", rom=" + rom + ", versionCode="
				+ versionCode + ", sdkVersion=" + sdkVersion + ", resolution="
				+ resolution + ", dpi=" + dpi + ", installTime=" + installTime
				+ ", isFirstUpload=" + isFirstUpload + ", loginName="
				+ loginName + ", password=" + password + ", newPassword="
				+ newPassword + ", oldPassword=" + oldPassword
				+ ", confirmPassword=" + confirmPassword + ", chineseName="
				+ chineseName + ", phone=" + phone + ", sex=" + sex
				+ ", birthday=" + birthday + ", address=" + address + ", qq="
				+ qq + ", wechat=" + wechat + ", gameType=" + gameType
				+ ", videoType=" + videoType + ", atetId=" + atetId
				+ ", channelId=" + channelId + ", productId=" + productId
				+ ", cpId=" + cpId + ", enterTime=" + enterTime + ", exitTime="
				+ exitTime + ", gameName=" + gameName + ", packageName="
				+ packageName + ", minTime=" + minTime + ", modelId=" + modelId
				+ ", giftPackageid=" + giftPackageid + ", type=" + type + "]";
	}

}
