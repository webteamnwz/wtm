package kr.po1.webtechmobile.List;

public class WtmRoomListItem
{
	private int iconId;
	private String title;

	public WtmRoomListItem(int iconId, String title)
	{
		this.iconId = iconId;
		this.title = title;
	}

	public void setIconId(int iconId)
	{
		this.iconId = iconId;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public int getIconId()
	{
		return iconId;
	}

	public String getTitle()
	{
		return title;
	}
}
