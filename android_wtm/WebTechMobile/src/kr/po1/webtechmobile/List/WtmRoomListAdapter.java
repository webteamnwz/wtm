package kr.po1.webtechmobile.List;

import java.util.ArrayList;

import kr.po1.webtechmobile.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WtmRoomListAdapter extends BaseAdapter
{
	Context context;
	int layoutId;
	ArrayList<WtmRoomListItem> list;
	LayoutInflater inflater;

	public WtmRoomListAdapter(Context context, int layoutId,
			ArrayList<WtmRoomListItem> list)
	{
		this.context = context;
		this.layoutId = layoutId;
		this.list = list;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{
		return this.list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(convertView == null)
			convertView = inflater.inflate(layoutId, parent, false);

		WtmRoomListItem item = list.get(position);

		TextView textTitle = (TextView) convertView.findViewById(R.id.layout_list_style_1_textTitle);
		ImageView imageIcon = (ImageView) convertView.findViewById(R.id.layout_list_style_1_imageIcon);

		imageIcon.setImageResource(item.getIconId());
		textTitle.setText(item.getTitle());

		return convertView;
	}
}
