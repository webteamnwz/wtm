package com.demo.wtm.operation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.demo.wtm.config.JSONTag;
import com.demo.wtm.config.WtmConfig;
import com.demo.wtm.fragment.UserFragment;
import com.demo.wtm.model.Room;
import com.demo.wtm.model.User;
import com.demo.wtm.request.Request;
import com.demo.wtm.request.RequestFactory;
import com.demo.wtm.request.RequestMethod;
import com.demo.wtm.service.RestService;
import com.demo.wtm.service.RequestService.Operation;
import com.demo.wtm.util.WtmPreference;

public class UserOperation implements Operation{

	private static final String TAG = "UserOperation";
	
	@Override
	public Bundle execute(Context context, Request request) {
		// TODO Auto-generated method stub
		
		JSONObject jObj;
        Bundle bundle = new Bundle();
				
		RestService client = new RestService(context);
		RequestMethod method = RequestMethod.valueOf(request.getString(JSONTag.DELIVER_TAG_METHOD));
		
		try {
			
			switch(method){
			case GET:{
				client.execute(WtmConfig.REST_SERVICE_URI_USER, method, true);
				jObj = new JSONObject(client.getResponse());	
				
				JSONObject jUser = jObj.getJSONObject(JSONTag.REST_JSON_TAG_USER);
				User user = new User(jUser.getString(JSONTag.REST_JSON_TAG_USER_ID)
						, jUser.getString(JSONTag.REST_JSON_TAG_USER_IMG)
						, jUser.getString(JSONTag.REST_JSON_TAG_USER_NAME)
						, jUser.getString(JSONTag.REST_JSON_TAG_USER_GROUP)
						, null
						, jUser.getString(JSONTag.REST_JSON_TAG_USER_PROFILE));
				
		        bundle.putParcelable(RequestFactory.REQUEST_BUNDLE_USER, user);
			}
			break;
			case PUT:
			case POST:{
				
		        client.addParam("user_name", request.getString("user_name"));
		        client.addParam("user_profile", request.getString("user_profile"));
		        client.addParam("user_email", request.getString("user_email"));
		        client.addParam("user_img", request.getString("user_img"));
		        client.addParam("user_group", request.getString("user_group"));
		        client.addParam("category_no_1", "");
		        client.addParam("category_no_2", "");
		        client.addParam("category_no_3", "");
		        
				client.execute(WtmConfig.REST_SERVICE_URI_USER, method, true);
				jObj = new JSONObject(client.getResponse());	
				
				bundle.putString(RequestFactory.REQUEST_BUNDLE_USER, jObj.getString(JSONTag.REST_JSON_TAG_MSG));

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
