package kr.po1.webtechmobile.DataSet;

import org.json.JSONObject;

import android.util.Log;

public class WtmFbUserInfo
{
	private String fbId;
	private String fbName;
	private String fbFirstName;
	private String fbLastName;
	private String fbLink;
	private String fbGender;
	private String fbLocale;
	private String fbProfileImageUrl;

	public void setFbUserInfo(String jsonContents)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(jsonContents);
			setFbUserInfo(jsonObject);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setFbUserInfo(JSONObject fbUserInfoObject)
	{
		try
		{
			fbId = fbUserInfoObject.getString("id");
			fbName = fbUserInfoObject.getString("name");
			fbFirstName = fbUserInfoObject.getString("first_name");
			fbLastName = fbUserInfoObject.getString("last_name");
			if(fbUserInfoObject.has("link"))
				fbLink = fbUserInfoObject.getString("link");
			fbGender = fbUserInfoObject.getString("gender");
			fbLocale = fbUserInfoObject.getString("locale");
			fbProfileImageUrl = "https://graph.facebook.com/" + fbId + "/picture?type=large";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.e("tag", e.toString());
		}
	}

	public String getId()
	{
		return fbId;
	}

	public String getName()
	{
		return fbName;
	}

	public String getFirstName()
	{
		return fbFirstName;
	}

	public String getLastName()
	{
		return fbLastName;
	}

	public String getLink()
	{
		return fbLink;
	}

	public String getGender()
	{
		return fbGender;
	}

	public String getLocale()
	{
		return fbLocale;
	}

	public String getProfileImageUrl()
	{
		return fbProfileImageUrl;
	}
}
