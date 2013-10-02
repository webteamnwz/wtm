package kr.po1.webtechmobile.List;

public class WtmCategoryListItem
{
	private int iconId;
	private String title;
	private int count;

	public WtmCategoryListItem(int iconId, String title, int count)
	{
		this.iconId = iconId;
		this.title = title;
		this.count = count;
	}

	public void setIconId(int iconId)
	{
		this.iconId = iconId;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public int getIconId()
	{
		return iconId;
	}

	public String getTitle()
	{
		return title;
	}

	public int getCount()
	{
		return count;
	}
}
