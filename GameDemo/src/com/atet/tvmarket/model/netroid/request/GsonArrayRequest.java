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
import java.security.acl.Group;
import java.util.List;

import org.json.JSONObject;

import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.model.netroid.Listener;
import com.atet.tvmarket.model.netroid.NetworkResponse;
import com.atet.tvmarket.model.netroid.Request;
import com.atet.tvmarket.model.netroid.Response;
import com.atet.tvmarket.model.netroid.Exception.ParseError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * A request for retrieving a {@link JSONObject} response body at a given URL, allowing for an
 * optional {@link JSONObject} to be passed in as part of the request body.
 */
public  class GsonArrayRequest<T extends AutoType> extends JsonRequest<List<T>> {
    private Class mClazz;
    
    /**
     * Creates a new request.
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
     *   indicates no parameters will be posted along with request.
     * @param listener Listener to receive the JSON response or error message
     */
    public GsonArrayRequest(int method, String url, String postData, Class clazz, Listener<List<T>> listener) {
        super(method, url, postData, listener);
    }
    
    public GsonArrayRequest(String url, String postData, Class clazz, Listener<List<T>> listener) {
        this(Request.Method.POST, url, postData, clazz, listener);
    }

    @Override
    protected Response<List<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, response.charset);
//            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            Gson gson = new Gson();
            
//            T info=(T)gson.fromJson(jsonString, mClazz);
//            Group<AutoType> info=gson.fromJson(jsonString, new TypeToken<T>(){}.getType());
//            return Response.success(info, response);
            
            List<T> list=gson.fromJson(jsonString, new TypeToken<List<T>>(){}.getType());
            return Response.success(list, response);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            // TODO: handle exception
            return Response.error(new ParseError(e));
        }
    }
}
