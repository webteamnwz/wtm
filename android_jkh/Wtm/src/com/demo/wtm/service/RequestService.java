package com.demo.wtm.service;

import com.demo.wtm.operation.CategoryOperation;
import com.demo.wtm.operation.CategoryRoomOperation;
import com.demo.wtm.operation.RoomOperation;
import com.demo.wtm.operation.UserOperation;
import com.demo.wtm.operation.UserRoomOperation;
import com.demo.wtm.request.Request;
import com.demo.wtm.request.RequestFactory;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class RequestService extends IntentService {
	
	private static final String TAG = "RequestService";
	
	public static final String INTENT_EXTRA_RECEIVER = "com.demo.wtm.extra.receiver";
	public static final String INTENT_EXTRA_REQUEST = "com.demo.wtm.extra.request";

    public static final int SUCCESS_CODE = 0;
    public static final int ERROR_CODE = -1;
    
	public RequestService() {
		super(TAG);
		// TODO Auto-generated constructor stub
		Log.i("kh",TAG);
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate called");
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
        Request request = intent.getParcelableExtra(INTENT_EXTRA_REQUEST);
        request.setClassLoader(getClassLoader());
        
        ResultReceiver receiver = intent.getParcelableExtra(INTENT_EXTRA_RECEIVER);
        Operation operation = getOperationForType(request.getRequestType());
        
        sendSuccess(receiver, operation.execute(this, request));

	}
	
    private void sendSuccess(ResultReceiver receiver, Bundle data) {
        sendResult(receiver, data, SUCCESS_CODE);
    }
    
    private void sendResult(ResultReceiver receiver, Bundle data, int code) {

        if (receiver != null) {
            if (data == null) {
                data = new Bundle();
            }

            receiver.send(code, data);
        }
    }
	
    public Operation getOperationForType(int requestType){
    	
    	switch(requestType){
    	case RequestFactory.USER_REQUEST:{
    		return new UserOperation();
    	}
    	case RequestFactory.USER_ROOM_REQUEST:{
    		return new UserRoomOperation();
    	}
    	case RequestFactory.ROOM_REQUEST:{
    		return new RoomOperation();
    	}
    	case RequestFactory.CATEGORY_ROOM_REQUEST:{
    		return new CategoryRoomOperation();
    	}
    	case RequestFactory.CATEGORY_REQUEST:{
    		return new CategoryOperation();
    	}
    	}
    	
    	return new UserOperation();
    }
	
	
    public interface Operation {

        public Bundle execute(Context context, Request request);
    }

}
