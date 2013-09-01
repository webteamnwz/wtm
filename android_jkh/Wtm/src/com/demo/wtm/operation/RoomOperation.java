package com.demo.wtm.operation;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.demo.wtm.config.JSONTag;
import com.demo.wtm.config.WtmConfig;
import com.demo.wtm.fragment.RoomFragment;
import com.demo.wtm.model.Room;
import com.demo.wtm.request.Request;
import com.demo.wtm.request.RequestFactory;
import com.demo.wtm.request.RequestMethod;
import com.demo.wtm.service.RestService;
import com.demo.wtm.service.RequestService.Operation;
import com.demo.wtm.util.WtmPreference;

public class RoomOperation implements Operation{
	
	private static final String TAG = "RoomOperation";

	@Override
	public Bundle execute(Context context, Request request) {
		// TODO Auto-generated method stub
		String roomNo;	
	
		JSONObject jObj;
		Room room;
        Bundle bundle = new Bundle();
		
		RestService client = new RestService(context);
		RequestMethod method = RequestMethod.valueOf(request.getString(JSONTag.DELIVER_TAG_METHOD));
		try {
			
			switch(method){
			case GET:{
				roomNo = request.getString(JSONTag.DELIVER_TAG_ROOM_NO);
				client.addParam(JSONTag.DELIVER_TAG_ROOM_NO, roomNo);
				client.execute(WtmConfig.REST_SERVICE_URI_ROOM, method, true);
				
				jObj = new JSONObject(client.getResponse());

				JSONObject jRoom = jObj.getJSONObject(JSONTag.REST_JSON_TAG_ROOM);
				room = new Room(roomNo, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_TITLE)
						, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_MANAGER_ID)
						, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_DESC)
						, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_STARTDATE)
						, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_ENDDATE)
						, jRoom.getString(JSONTag.REST_JSON_TAG_ROOM_CATEGORY_IDS)
						, request.getBoolean(JSONTag.DELIVER_TAG_ROOM_JOINED));
		        bundle.putParcelable(RequestFactory.REQUEST_BUNDLE_ROOM, room);
			}
			break;
			case POST:{
				client.addParam(JSONTag.DELIVER_TAG_CATEGORY_NO, request.getString(JSONTag.DELIVER_TAG_CATEGORY_NO));
				client.addParam(JSONTag.DELIVER_TAG_ROOM_TITLE, request.getString(JSONTag.DELIVER_TAG_ROOM_TITLE));
				client.addParam(JSONTag.DELIVER_TAG_ROOM_DESC, request.getString(JSONTag.DELIVER_TAG_ROOM_DESC));
				client.addParam(JSONTag.DELIVER_TAG_ROOM_STARTDATE, request.getString(JSONTag.DELIVER_TAG_ROOM_STARTDATE));
				client.addParam(JSONTag.DELIVER_TAG_ROOM_ENDDATE, request.getString(JSONTag.DELIVER_TAG_ROOM_ENDDATE));
				client.execute(WtmConfig.REST_SERVICE_URI_ROOM, method, true);
				jObj = new JSONObject(client.getResponse());

				bundle.putString(RequestFactory.REQUEST_BUNDLE_ROOM, jObj.getString(JSONTag.REST_JSON_TAG_MSG));

			}
			break;
			case PUT:{
				client.addParam(JSONTag.DELIVER_TAG_ROOM_NO, request.getString(JSONTag.DELIVER_TAG_ROOM_NO));
				client.addParam(JSONTag.DELIVER_TAG_CATEGORY_NO, request.getString(JSONTag.DELIVER_TAG_CATEGORY_NO));
				client.addParam(JSONTag.DELIVER_TAG_ROOM_TITLE, request.getString(JSONTag.DELIVER_TAG_ROOM_TITLE));
				client.addParam(JSONTag.DELIVER_TAG_ROOM_DESC, request.getString(JSONTag.DELIVER_TAG_ROOM_DESC));
				client.addParam(JSONTag.DELIVER_TAG_ROOM_STARTDATE, request.getString(JSONTag.DELIVER_TAG_ROOM_STARTDATE));
				client.addParam(JSONTag.DELIVER_TAG_ROOM_ENDDATE, request.getString(JSONTag.DELIVER_TAG_ROOM_ENDDATE));
				client.execute(WtmConfig.REST_SERVICE_URI_ROOM, method, true);
				jObj = new JSONObject(client.getResponse());

				bundle.putString(RequestFactory.REQUEST_BUNDLE_ROOM, jObj.getString(JSONTag.REST_JSON_TAG_MSG));

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
