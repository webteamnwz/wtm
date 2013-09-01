package com.demo.wtm.operation;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.demo.wtm.config.JSONTag;
import com.demo.wtm.config.WtmConfig;
import com.demo.wtm.fragment.CategoryFragment;
import com.demo.wtm.fragment.CategoryRoomFragment;
import com.demo.wtm.fragment.UserRoomFragment;
import com.demo.wtm.model.Category;
import com.demo.wtm.model.Room;
import com.demo.wtm.model.User;
import com.demo.wtm.request.Request;
import com.demo.wtm.request.RequestFactory;
import com.demo.wtm.request.RequestMethod;
import com.demo.wtm.service.RestService;
import com.demo.wtm.service.RequestService.Operation;
import com.demo.wtm.util.WtmPreference;

public class CategoryOperation implements Operation{
	
	private static final String TAG = "CategoryOperation";

	@Override
	public Bundle execute(Context context, Request request) {
		// TODO Auto-generated method stub
		
		JSONObject mJObj;
		JSONArray jArray;
        Bundle bundle = new Bundle();
				
		RestService client = new RestService(context);		
		RequestMethod method = RequestMethod.valueOf(request.getString(JSONTag.DELIVER_TAG_METHOD));
		
		try {
			
			switch(method){
			case GET:{
				client.execute(WtmConfig.REST_SERVICE_URI_CATEGORY, method, true);
				mJObj = new JSONObject(client.getResponse());
				jArray = mJObj.getJSONArray(JSONTag.REST_JSON_TAG_CATEGORY_CATEGORIES);
				ArrayList<Category> categoryList = new ArrayList<Category>();
				
		        for (int i = 0; i < jArray.length(); i++) {
		            JSONObject j = jArray.getJSONObject(i);
		            categoryList.add(new Category(j.getString(JSONTag.REST_JSON_TAG_CATEGORY_NO), j.getString(JSONTag.REST_JSON_TAG_CATEGORY_NAME), j.getString(JSONTag.REST_JSON_TAG_CATEGORY_ROOM_CNT)));
		        }
		        bundle.putParcelableArrayList(RequestFactory.REQUEST_BUNDLE_CATEGORY, categoryList);
				
			}
			break;
			}
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
        return bundle;
		
	}

}
