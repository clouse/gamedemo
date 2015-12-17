package com.atet.tvmarket.model.database;
/**
 * @ClassName: BaseModel.java
 * @Description: 数据库表的基础类型
 * @author 吴绍东
 * @date 2012-12-12 下午12:47:24
 */
public class BaseModel {
	private String autoIncrementId;
	
	public String getAutoIncrementId()
	{
		return autoIncrementId;
	}
	
	public void setAutoIncrementId(String autoIncrementId)
	{
		this.autoIncrementId=autoIncrementId;
	}

}
