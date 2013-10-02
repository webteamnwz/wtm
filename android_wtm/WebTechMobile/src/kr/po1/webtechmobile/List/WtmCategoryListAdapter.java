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

public class WtmCategoryListAdapter extends BaseAdapter
{
	Context context;
	int layoutId;
	ArrayList<WtmCategoryListItem> list;
	LayoutInflater inflater;

	public WtmCategoryListAdapter(Context context, int layoutId,
			ArrayList<WtmCategoryListItem> list)
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

		WtmCategoryListItem item = list.get(position);

		TextView textTitle = (TextView) convertView.findViewById(R.id.layout_list_style_1_textTitle);
		TextView textCount = (TextView) convertView.findViewById(R.id.layout_list_style_1_textCount);
		ImageView imageIcon = (ImageView) convertView.findViewById(R.id.layout_list_style_1_imageIcon);

		imageIcon.setImageResource(item.getIconId());
		textTitle.setText(item.getTitle());
		textCount.setText(item.getCount() + "");

		return convertView;
	}
}
