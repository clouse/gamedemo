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

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.atet.tvmarket.model.DataTaskListener;
import com.atet.tvmarket.model.ReqConfig;
import com.atet.tvmarket.model.TaskListener;
import com.atet.tvmarket.model.netroid.NetworkResponse;
import com.atet.tvmarket.model.netroid.Request;
import com.atet.tvmarket.model.netroid.Response;
import com.atet.tvmarket.model.netroid.Exception.NetroidError;
import com.atet.tvmarket.model.task.TaskResult;

/**
 * A request for retrieving a {@link JSONObject} response body at a given URL, allowing for an
 * optional {@link JSONObject} to be passed in as part of the request body.
 */
public  class DataTaskRequest<T> extends Request<T> {
    private TaskListener mListener;

    public DataTaskRequest(TaskListener<T> listener) {
        super(null, listener);
        mListener=listener;
        
        if(listener==null){
            try {
                throw new NetroidError("parameters error:listener null");
            } catch (NetroidError e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        setSynchronized(true);
    }
    
    @Override
    public NetworkResponse perform() {
        // TODO Auto-generated method stub
        try {
            return new NetworkResponse(null, HTTP.UTF_8);
        } catch (Exception e) {
            return new NetworkResponse(new byte[1], HTTP.UTF_8);
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        TaskResult result=null;
        if(mListener!=null){
            result = mListener.doTaskInBackground();
        }
        
        return Response.success(null, response);
    }
    
    public void setReqConfig(ReqConfig reqConfig){
    	if(mListener != null && mListener instanceof DataTaskListener) {
    		DataTaskListener dataTaskListener = (DataTaskListener)mListener;
    		dataTaskListener.setReqConfig(reqConfig);
    	}
    }
}
