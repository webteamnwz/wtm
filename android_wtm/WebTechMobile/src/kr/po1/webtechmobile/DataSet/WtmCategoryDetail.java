package kr.po1.webtechmobile.DataSet;

import org.json.JSONObject;

import android.util.Log;

public class WtmCategoryDetail
{
	private JSONObject categoryInfo;

	private int categoryNo;
	private String categoryName;
	private int roomCount = 0;

	public WtmCategoryDetail(JSONObject jsonContents)
	{
		categoryInfo = jsonContents;
		setCategoryDetail();
	}

	public WtmCategoryDetail(String jsonContents)
	{
		try
		{
			categoryInfo = new JSONObject(jsonContents);
			setCategoryDetail();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.e("tag", e.toString());
		}
	}

	private void setCategoryDetail()
	{
		try
		{
			if(categoryInfo.has("category_no"))
				categoryNo = categoryInfo.getInt("category_no");

			if(categoryInfo.has("category_name"))
				categoryName = categoryInfo.getString("category_name");

			if(categoryInfo.has("room_cnt"))
				roomCount = categoryInfo.getInt("room_cnt");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.e("tag", e.toString());
		}
	}

	public int getCategoryNo()
	{
		return categoryNo;
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public int getRoomCount()
	{
		return roomCount;
	}
}
