package kr.po1.webtechmobile.DataSet;

import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WtmCategoryRoomInfo
{
	JSONObject jsonCategoryRoomInfo;

	private int roomCount = 0;

	private JSONArray arrayRoomList;
	private ArrayList<WtmRoomInfo> roomListInfo = new ArrayList<WtmRoomInfo>();

	public WtmCategoryRoomInfo(String jsonContents)
	{
		try
		{
			jsonCategoryRoomInfo = new JSONObject(jsonContents);
			setCatgoryRoomInfo();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public WtmCategoryRoomInfo(JSONObject jsonContents)
	{
		jsonCategoryRoomInfo = jsonContents;
		setCatgoryRoomInfo();
	}

	private void setCatgoryRoomInfo()
	{
		try
		{
			if(has("room_cnt"))
				roomCount = getInt("room_cnt");

			if(has("room"))
			{
				arrayRoomList = getArray("room");
				for(int roomIndex = 0; roomIndex < arrayRoomList.length(); roomIndex++)
				{
					WtmRoomInfo roomInfo = new WtmRoomInfo(arrayRoomList.getJSONObject(roomIndex));
					roomListInfo.add(roomInfo);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public int getRoomCount()
	{
		return roomCount;
	}

	public ArrayList<WtmRoomInfo> getRoomList()
	{
		return roomListInfo;
	}

	private boolean has(String key)
	{
		return jsonCategoryRoomInfo.has(key);
	}

	private String getString(String key)
	{
		try
		{
			return jsonCategoryRoomInfo.getString(key);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private int getInt(String key)
	{
		try
		{
			return jsonCategoryRoomInfo.getInt(key);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	private JSONArray getArray(String key)
	{
		try
		{
			return jsonCategoryRoomInfo.getJSONArray(key);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
