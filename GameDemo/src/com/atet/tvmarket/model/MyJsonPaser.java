package com.atet.tvmarket.model;

import com.atet.api.utils.EncryptUtils;
import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.entity.SignVerifyReq;
import com.google.gson.Gson;

public class MyJsonPaser {

    public static <T extends AutoType> String toJson(T t) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(t);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return json;
    }

    public static <T extends AutoType> T fromJson(String json, Class clazz) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = (T) gson.fromJson(json, clazz);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return t;
    }
    
    public static <T extends AutoType> String getVerifyPostData(T t) {
    	String postData = null;
    	try {
    		Gson gson = new Gson();
    		String data = gson.toJson(t);
    		String sign = DataHelper.signPostData(data);

    		SignVerifyReq signVerifyReq = new SignVerifyReq();
    		signVerifyReq.setData(data);
    		signVerifyReq.setSign(sign);

    		postData = gson.toJson(signVerifyReq);
    	} catch (Exception e) {
    		// TODO: handle exception
    		e.printStackTrace();
    	}
    	return postData;
    }
}
