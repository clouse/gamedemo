package com.atet.tvmarket.model.json;

import com.atet.tvmarket.entity.AutoType;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

/**
 * @author wsd
 * @Description:单个Json对象的解析
 * @date 2012-12-5 上午9:57:34
 */
public class JsonObjectParser<T extends AutoType> extends AbstractParser<T> {
	/**
	* @Fields clazz :要解析的类
	*/
	private Class clazz;
	
	public JsonObjectParser(Class clazz) {
		super();
		this.clazz = clazz;
	}
	/**
	 * 模板方法，用来负责解析具体的单个对象
	 */
	@Override
	public T parserInner(JsonReader reader) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		T obj = gson.fromJson(reader, clazz);
		return obj;
	}

}
