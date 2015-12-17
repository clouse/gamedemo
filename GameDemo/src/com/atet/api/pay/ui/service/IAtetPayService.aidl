package com.atet.api.pay.ui.service;

import com.atet.api.pay.ui.service.IRemoteServiceCallback;

interface IAtetPayService {
   String startPay(String params,IRemoteServiceCallback callback);
   String login(String params,IRemoteServiceCallback callback);
}