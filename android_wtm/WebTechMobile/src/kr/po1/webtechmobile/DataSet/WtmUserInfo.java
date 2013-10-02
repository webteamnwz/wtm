package kr.po1.webtechmobile.DataSet;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WtmUserInfo
{
	private String userId;
	private String userName;
	private String userEmail;
	private String userImg;
	private String userProfile;
	private String[] userGroup;
	private String fbToken;

	private boolean isFirst;

	private JSONArray userCategoryArray;
	private ArrayList<HashMap<String, String>> userCategory;

	public WtmUserInfo(String jsonContents)
	{
		try
		{
			JSONObject jsonObnect = new JSONObject(jsonContents);
			setUserInfo(jsonObnect);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public WtmUserInfo(JSONObject jsonContents)
	{
		setUserInfo(jsonContents);
	}

	public void setUserInfo(JSONObject userInfoObject)
	{
		try
		{
			JSONObject userInfo = userInfoObject.getJSONObject("user");
			if(userInfo.has("user_id"))
				userId = userInfo.getString("user_id");
			if(userInfo.has("user_name"))
				userName = userInfo.getString("user_name");
			if(userInfo.has("user_email"))
				userEmail = userInfo.getString("user_email");
			if(userInfo.has("user_img"))
				userImg = userInfo.getString("user_img");
			if(userInfo.has("user_profile"))
				userProfile = userInfo.getString("user_profile");
			if(userInfo.has("user_group"))
				userGroup = userInfo.getString("user_group").split("|");
			if(userInfo.has("fb_token"))
				fbToken = userInfo.getString("fb_token");
			isFirst = userInfo.getString("is_first").equals("true");

			userCategoryArray = userInfo.getJSONArray("user_category");

			for(int i = 0; i < userCategoryArray.length(); i++)
			{
				JSONObject userInfoJson = userCategoryArray.getJSONObject(i);
				HashMap<String, String> categotyInfo = new HashMap<String, String>();
				categotyInfo.put("user_id", userInfoJson.getString("user_id"));
				categotyInfo.put("user_img", userInfoJson.getString("user_img"));

				userCategory.add(categotyInfo);
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
	}

	// GET

	public String getUserId()
	{
		return userId;
	}

	public String getUserName()
	{
		return userName;
	}

	public String getUserEmail()
	{
		return userEmail;
	}

	public String getUserImage()
	{
		return userImg;
	}

	public String getUserProfile()
	{
		return userProfile;
	}

	public String[] getUserGruop()
	{
		return userGroup;
	}

	public String getFbToken()
	{
		return fbToken;
	}

	public Boolean getIsFirst()
	{
		return isFirst;
	}

	public ArrayList<HashMap<String, String>> getUserCategory()
	{
		return userCategory;
	}

	// SET

	public void setUserProfile(String inputUserProfile)
	{
		userProfile = inputUserProfile;
	}

	public void setUserImage(String inputUserImage)
	{
		userImg = inputUserImage;
	}
}
