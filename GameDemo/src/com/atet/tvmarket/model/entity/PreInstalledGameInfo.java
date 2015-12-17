package com.atet.tvmarket.model.entity;

import java.io.Serializable;

import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.model.database.BaseModel;
import com.atet.tvmarket.model.database.TableDescription;


@TableDescription(name = "PreInstalledGameInfo")
public class PreInstalledGameInfo extends BaseModel implements AutoType,
		Serializable {
	private static final long serialVersionUID = 1L;
	private String packageName;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "PreInstalledGameInfo [packageName=" + packageName + "]";
	}
}
