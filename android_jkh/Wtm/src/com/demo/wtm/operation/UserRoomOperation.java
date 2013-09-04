package com.demo.wtm.operation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.demo.wtm.config.JSONTag;
import com.demo.wtm.config.WtmConfig;
import com.demo.wtm.fragment.UserRoomFragment;
import com.demo.wtm.model.Room;
import com.demo.wtm.model.User;
import com.demo.wtm.request.Request;
import com.demo.wtm.request.RequestFactory;
import com.demo.wtm.request.RequestMethod;
import com.demo.wtm.service.RestService;
import com.demo.wtm.service.RequestService.Operation;
import com.demo.wtm.util.WtmPreference;

public class UserRoomOperation implements Operation{
	
	private static final String TAG = "UserRoomOperation";

	@Override
	public Bundle execute(Context context, Request request) {
		// TODO Auto-generated method stub
		JSONObject jObj;
        Bundle bundle = new Bundle();
		RestService client = new RestService(context);	
		RequestMethod method = RequestMethod.valueOf(request.getString(JSONTag.DELIVER_TAG_METHOD));
		
		try{
			
			switch(method){
			case GET:{
				User user = new User();
				
				client.execute(WtmConfig.REST_SERVICE_URI_USER_ROOM, method, true);
				jObj = new JSONObject(client.getResponse());
				
				JSONArray jArray = jObj.getJSONArray(JSONTag.REST_JSON_TAG_USER_ROOM_LIST);				
		        for (int i = 0; i < jArray.length(); i++) {
		            JSONObject jRoom = jArray.getJSONObject(i);
		            Room room = new Room(jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_NO)
		            		, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_TITLE)
		            		, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_MANAGER_ID)
		            		, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_DESC)
		            		, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_STARTDATE)
		            		, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_ENDDATE)
		            		, null
		            		, true);
		            room.mRoomJoinCnt = jRoom.getString(JSONTag.REST_JSON_TAG_USER_ROOM_CNT_JOIN_USER);
		            user.addRoom(room);
		        }
		        
		        bundle.putParcelable(RequestFactory.REQUEST_BUNDLE_USER_ROOM, user);
			}
			break;
			case PUT:
			case POST:
			case DELETE:{
				String roomNo = request.getString(JSONTag.DELIVER_TAG_ROOM_NO);
				client.addParam(JSONTag.DELIVER_TAG_ROOM_NO, roomNo);				
				client.execute(WtmConfig.REST_SERVICE_URI_USER_ROOM, method, true);
				jObj = new JSONObject(client.getResponse());
				
				bundle.putString(RequestFactory.REQUEST_BUNDLE_USER_ROOM, jObj.getString(JSONTag.REST_JSON_TAG_MSG));
			}
			break;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
        return bundle;
	}
}
