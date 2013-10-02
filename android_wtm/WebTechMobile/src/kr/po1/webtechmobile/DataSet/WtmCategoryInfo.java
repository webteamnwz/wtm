package kr.po1.webtechmobile.DataSet;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class WtmCategoryInfo
{
	JSONObject categoryInfo;

	JSONArray categoryList;
	ArrayList<WtmCategoryDetail> categoryListInfo = new ArrayList<WtmCategoryDetail>();

	public WtmCategoryInfo(String jsonContents)
	{
		try
		{
			categoryInfo = new JSONObject(jsonContents);
			setCategoryInfo();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.e("tag", e.toString());
		}
	}

	public WtmCategoryInfo(JSONObject jsonContents)
	{
		categoryInfo = jsonContents;
		setCategoryInfo();
	}

	private void setCategoryInfo()
	{
		try
		{
			if(categoryInfo.has("categories"))
			{
				categoryList = categoryInfo.getJSONArray("categories");

				for(int categoryIndex = 0; categoryIndex < categoryList.length(); categoryIndex++)
				{
					WtmCategoryDetail categoryDetail = new WtmCategoryDetail(categoryList.getJSONObject(categoryIndex));
					categoryListInfo.add(categoryDetail);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.e("tag", e.toString());
		}
	}

	public ArrayList<WtmCategoryDetail> getCategoryList()
	{
		return categoryListInfo;
	}
}
