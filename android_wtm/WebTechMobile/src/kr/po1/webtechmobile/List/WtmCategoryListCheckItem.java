package kr.po1.webtechmobile.List;

public class WtmCategoryListCheckItem
{
	private int iconId;
	private String title;
	private int count;
	private boolean isCheck = false;

	public WtmCategoryListCheckItem(int iconId, String title, int count,
			boolean isCheck)
	{
		this.iconId = iconId;
		this.title = title;
		this.count = count;
		this.isCheck = isCheck;
	}

	public WtmCategoryListCheckItem(int iconId, String title, int count)
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

	public boolean getIsCheck()
	{
		return isCheck;
	}
}
