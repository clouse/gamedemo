package com.atet.api.pay.ui.service;

import android.os.Bundle;

interface IRemoteServiceCallback {
	void startActivity(String packageName, String className, int callingPid,in Bundle bundle);
}