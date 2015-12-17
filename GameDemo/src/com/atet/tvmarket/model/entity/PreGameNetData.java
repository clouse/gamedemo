package com.atet.tvmarket.model.entity;

import java.io.Serializable;

import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.model.database.BaseModel;
import com.atet.tvmarket.model.database.TableDescription;


@TableDescription(name = "PreGameNetData")
public class PreGameNetData extends BaseModel implements AutoType, Serializable {
	private String data;
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "PreInstalledGameInfo [data=" + data + ", code=" + code + "]";
	}

}
