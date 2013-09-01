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
import com.demo.wtm.fragment.CategoryRoomFragment;
import com.demo.wtm.fragment.UserRoomFragment;
import com.demo.wtm.model.Room;
import com.demo.wtm.model.User;
import com.demo.wtm.request.Request;
import com.demo.wtm.request.RequestFactory;
import com.demo.wtm.request.RequestMethod;
import com.demo.wtm.service.RestService;
import com.demo.wtm.service.RequestService.Operation;
import com.demo.wtm.util.WtmPreference;

public class CategoryRoomOperation implements Operation{
	
	private static final String TAG = "CategoryRoomOperation";

	@Override
	public Bundle execute(Context context, Request request) {
		// TODO Auto-generated method stub
		JSONObject mJObj;
		JSONArray jArray;
        Bundle bundle = new Bundle();
		
		RestService client = new RestService(context);
		RequestMethod method = RequestMethod.valueOf(request.getString(JSONTag.DELIVER_TAG_METHOD));

		String categoryNo = request.getString(JSONTag.DELIVER_TAG_CATEGORY_NO);

		
		try {
			
			switch(method){
			case GET:{
				client.addParam(JSONTag.DELIVER_TAG_CATEGORY_NO, categoryNo);
				client.execute(WtmConfig.REST_SERVICE_URI_CATEGORY_ROOM, method, true);
				
				mJObj = new JSONObject(client.getResponse());
				jArray = mJObj.getJSONArray(JSONTag.REST_JSON_TAG_CATEGORY_ROOM);
				ArrayList<Room> roomList = new ArrayList<Room>();
				
		        for (int i = 0; i < jArray.length(); i++) {
		            JSONObject jRoom = jArray.getJSONObject(i);
		            Room room = new Room(jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_NO)
		            		, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_TITLE)
		            		, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_MANAGER_ID)
		            		, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_DESC)
		            		, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_STARTDATE)
		            		, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_ENDDATE)
		            		, null
		            		, false);
		            roomList.add(room);
		        }
				
			    bundle.putParcelableArrayList(RequestFactory.REQUEST_BUNDLE_CATEGORY_ROOM, roomList);
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
