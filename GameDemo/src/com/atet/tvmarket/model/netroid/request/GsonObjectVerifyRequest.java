/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.atet.tvmarket.model.netroid.request;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.MyJsonPaser;
import com.atet.tvmarket.model.netroid.Listener;
import com.atet.tvmarket.model.netroid.NetworkResponse;
import com.atet.tvmarket.model.netroid.Request;
import com.atet.tvmarket.model.netroid.Response;
import com.atet.tvmarket.model.netroid.Exception.ParseError;
import com.atet.tvmarket.model.netroid.Exception.SignVerifyError;

/**
 * A request for retrieving a {@link JSONObject} response body at a given URL, allowing for an
 * optional {@link JSONObject} to be passed in as part of the request body.
 */
public  class GsonObjectVerifyRequest<T extends AutoType> extends JsonRequest<T> {
    private static final String TAG = "GsonObjectVerifyRequest";
    private Class mClazz;
    private String mAppKey=null;
    private static ALog alog = ALog.getLogger(GsonObjectVerifyRequest.class);
    
    public String getAppKey() {
        return mAppKey;
    }

    public void setAppKey(String appKey) {
        this.mAppKey = appKey;
    }

    /**
     * Creates a new request.
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
     *   indicates no parameters will be posted along with request.
     * @param listener Listener to receive the JSON response or error message
     */
    public GsonObjectVerifyRequest(int method, String url, String postData, Listener<T> listener) {
        super(method, url, postData, listener);
    }
    
    public GsonObjectVerifyRequest(String url, String postData, Class clazz, Listener<T> listener) {
        this(Request.Method.POST, url, postData, listener);
        mClazz=clazz;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, response.charset);
            alog.debug("response data:"+jsonString);
            
            String data = getContent(jsonString, "data=", "&sign=");
            String sign = getContent(jsonString, "&sign=", null);
            boolean signValidate = DataHelper.checkSign(data, sign);
            if(!signValidate){
            	alog.equals("sign invalid");
            	return Response.error(new SignVerifyError());
            }
            T info = MyJsonPaser.fromJson(data, mClazz);
            return Response.success(info, response);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            // TODO: handle exception
            return Response.error(new ParseError(e));
        }
    }
    
	private static String getContent(String src, String startTag, String endTag) {
		String content = src;
		int start = src.indexOf(startTag);
		start += startTag.length();

		try {
			if (endTag != null) {
				int end = src.indexOf(endTag);
				content = src.substring(start, end);
			} else {
				content = src.substring(start);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return content;
	}
}
