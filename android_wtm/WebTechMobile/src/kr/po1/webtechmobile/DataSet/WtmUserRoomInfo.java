package kr.po1.webtechmobile.DataSet;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class WtmUserRoomInfo
{
	private JSONObject jsonObject;

	private int createRoomCount = 0;
	private int joinRoomCount = 0;

	private JSONArray jsonRoomList;
	private ArrayList<WtmUserRoomDetail> roomList = new ArrayList<WtmUserRoomDetail>();

	public WtmUserRoomInfo(String jsonContents)
	{
		try
		{
			jsonObject = new JSONObject(jsonContents);
			setInfo();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.e("tag", e.toString());
		}
	}

	private void setInfo()
	{
		try
		{
			// 사용자가 개설한 공부방 갯수
			if(jsonObject.has("create_room_cnt"))
				createRoomCount = jsonObject.getInt("create_room_cnt");

			// 사용자가 참가한 공부방 갯수
			if(jsonObject.has("join_room_cnt"))
				joinRoomCount = jsonObject.getInt("join_room_cnt");

			// 공부방 정보
			if(jsonObject.has("roomList"))
			{
				jsonRoomList = jsonObject.getJSONArray("roomList");
				setRoomList();
			}
		}
		catch(Exception e)
		{
			Log.e("tag", e.toString());
			e.printStackTrace();
		}
	}

	private void setRoomList()
	{
		try
		{
			for(int roomIndex = 0; roomIndex < jsonRoomList.length(); roomIndex++)
			{
				JSONObject jsonRoomInfo = jsonRoomList.getJSONObject(roomIndex);
				String stringRoomInfo = jsonRoomList.getString(roomIndex);
				WtmUserRoomDetail roomDetail = new WtmUserRoomDetail(jsonRoomInfo);
				roomList.add(roomDetail);
			}
		}
		catch(Exception e)
		{
			Log.e("tag", e.toString());
			e.printStackTrace();
		}
	}

	public int getCreateRoomCount()
	{
		return createRoomCount;
	}

	public int getJoinRoomCount()
	{
		return joinRoomCount;
	}

	public ArrayList<WtmUserRoomDetail> getRoomList()
	{
		return roomList;
	}
}