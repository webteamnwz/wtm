package kr.po1.webtechmobile.DataSet;

import org.json.JSONObject;

import android.util.Log;

public class WtmUserRoomDetail
{
	private int roomNo;
	private String roomTitle;
	private String roomDesc;
	private String roomManagerId;
	private int startDate = 0;
	private int endDate = 0;
	private int joinUserCount = 0;
	private int checkUserCount = 0;
	private boolean isChecked = false;

	private JSONObject jsonObject;

	public WtmUserRoomDetail(String jsonContents)
	{
		try
		{
			jsonObject = new JSONObject(jsonContents);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.e("tag", e.toString());
		}

		setUserRoomDetail();
	}

	public WtmUserRoomDetail(JSONObject jsonContents)
	{
		jsonObject = jsonContents;

		setUserRoomDetail();
	}

	private void setUserRoomDetail()
	{
		try
		{
			if(jsonObject.has("room_no"))
				roomNo = jsonObject.getInt("room_no");

			if(jsonObject.has("room_title"))
				roomTitle = jsonObject.getString("room_title");

			if(jsonObject.has("room_manager_id"))
				roomManagerId = jsonObject.getString("room_manager_id");

			if(jsonObject.has("start_date"))
				startDate = jsonObject.getInt("start_date");

			if(jsonObject.has("end_date"))
				endDate = jsonObject.getInt("end_date");

			if(jsonObject.has("cnt_join_user"))
				joinUserCount = jsonObject.getInt("cnt_join_user");

			if(jsonObject.has("cnt_chk_user"))
				checkUserCount = jsonObject.getInt("cnt_chk_user");

			if(jsonObject.has("is_checked"))
				isChecked = jsonObject.getString("is_checked").equals("true");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.e("tag", e.toString());
		}
	}

	public int getRoomNo()
	{
		return roomNo;
	}

	public String getRoomTitle()
	{
		return roomTitle;
	}

	public String getRoomDesc()
	{
		return roomDesc;
	}

	public String getRoomManagerId()
	{
		return roomManagerId;
	}

	public int getStartDate()
	{
		return startDate;
	}

	public int getEndDate()
	{
		return endDate;
	}

	public int getJoinUserCount()
	{
		return joinUserCount;
	}

	public int getCheckUserCount()
	{
		return checkUserCount;
	}

	public boolean isChecked()
	{
		return isChecked;
	}
}
