package kr.po1.webtechmobile.DataSet;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WtmRoomInfo
{
	private JSONObject jsonRoomInfo;

	private int roomNo;
	private String roomTitle;
	private String roomDesc;

	private String startDate;
	private String endDate;
	private String[] categoryIds = {};
	private String roomManager;

	private int joinCnt;
	private int checkCnt;

	private boolean isJoined = false;
	private boolean isChecked = false;

	private JSONArray checkedUsersArray;
	private ArrayList<HashMap<String, String>> checkedUsers = new ArrayList<HashMap<String, String>>();

	public WtmRoomInfo(String jsonContents)
	{
		try
		{
			jsonRoomInfo = new JSONObject(jsonContents);
			setRoomInfo();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public WtmRoomInfo(JSONObject jsonContents)
	{
		jsonRoomInfo = jsonContents;
		setRoomInfo();
	}

	public void setRoomInfo()
	{
		try
		{
			JSONObject tempJson = null;

			if(jsonRoomInfo.has("room"))
			{
				tempJson = jsonRoomInfo;
				jsonRoomInfo = jsonRoomInfo.getJSONObject("room");
			}

			if(jsonRoomInfo.has("room_no"))
				roomNo = jsonRoomInfo.getInt("room_no");

			if(jsonRoomInfo.has("room_title"))
				roomTitle = jsonRoomInfo.getString("room_title");

			if(jsonRoomInfo.has("room_desc"))
				roomDesc = jsonRoomInfo.getString("room_desc");

			if(jsonRoomInfo.has("start_date"))
				startDate = jsonRoomInfo.getString("start_date");
			if(jsonRoomInfo.has("end_date"))
				endDate = jsonRoomInfo.getString("end_date");

			if(jsonRoomInfo.has("cnt_join"))
				joinCnt = jsonRoomInfo.getInt("cnt_join");
			if(jsonRoomInfo.has("cnt_chk"))
				checkCnt = jsonRoomInfo.getInt("cnt_chk");

			if(jsonRoomInfo.has("room_manager_id"))
				roomManager = jsonRoomInfo.getString("room_manager_id");

			if(jsonRoomInfo.has("category_ids") && !jsonRoomInfo.isNull("category_ids"))
				categoryIds = jsonRoomInfo.getString("category_ids").split("\\|");

			if(jsonRoomInfo.has("checked_user"))
				checkedUsersArray = jsonRoomInfo.getJSONArray("checked_user");

			if(jsonRoomInfo.has("is_joined"))
				isJoined = jsonRoomInfo.getBoolean("is_joined");

			if(jsonRoomInfo.has("is_checked"))
				isChecked = jsonRoomInfo.getBoolean("is_checked");

			if(tempJson != null)
				if(tempJson.has("checked_user"))
				{
					checkedUsersArray = tempJson.getJSONArray("checked_user");

					for(int userIndex = 0; userIndex < checkedUsersArray.length(); userIndex++)
					{
						JSONObject jsonCheckedUserInfo = checkedUsersArray.getJSONObject(userIndex);
						HashMap<String, String> checkedUserInfo = new HashMap<String, String>();

						checkedUserInfo.put("user_id", jsonCheckedUserInfo.has("user_id") ? jsonCheckedUserInfo.getString("user_id") : null);
						checkedUserInfo.put("user_img", jsonCheckedUserInfo.has("user_img") ? jsonCheckedUserInfo.getString("user_img") : null);

						checkedUsers.add(checkedUserInfo);
					}
				}
		}
		catch(JSONException e)
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

	public String getStartDate()
	{
		return startDate;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public String[] getCategoryIds()
	{
		return categoryIds;
	}

	public String getManagerId()
	{
		return roomManager;
	}

	public int getJoinCnt()
	{
		return joinCnt;
	}

	public int getCheckCnt()
	{
		return checkCnt;
	}

	public ArrayList<HashMap<String, String>> getCheckedUsers()
	{
		return checkedUsers;
	}

	public boolean getIsJoined()
	{
		return isJoined;
	}

	public boolean getIsChecked()
	{
		return isChecked;
	}
}
