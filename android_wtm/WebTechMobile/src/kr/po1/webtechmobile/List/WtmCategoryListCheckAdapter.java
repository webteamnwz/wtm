package kr.po1.webtechmobile.List;

import java.util.ArrayList;

import kr.po1.webtechmobile.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class WtmCategoryListCheckAdapter extends BaseAdapter
{
	Context context;
	int layoutId;
	ArrayList<WtmCategoryListCheckItem> list;
	LayoutInflater inflater;
	ArrayList<Integer> listItem = new ArrayList<Integer>();

	public WtmCategoryListCheckAdapter(Context context, int layoutId,
			ArrayList<WtmCategoryListCheckItem> list)
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

		final int checkBoxPosition = position;

		WtmCategoryListCheckItem item = list.get(position);

		TextView textTitle = (TextView) convertView.findViewById(R.id.layout_list_style_1_textTitle);
		CheckBox check = (CheckBox) convertView.findViewById(R.id.layout_list_style_1_check);
		ImageView imageIcon = (ImageView) convertView.findViewById(R.id.layout_list_style_1_imageIcon);

		imageIcon.setImageResource(item.getIconId());
		textTitle.setText(item.getTitle());

		if(check != null)
		{
			check.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if(isChecked)
					{
						for(int i = 0; i < listItem.size(); i++)
							if(listItem.get(i) == checkBoxPosition)
								return;

						listItem.add(checkBoxPosition);
					}
					else
						for(int i = 0; i < listItem.size(); i++)
							if(listItem.get(i) == checkBoxPosition)
							{
								listItem.remove(i);
								break;
							}
				}
			});

			boolean isChecked = false;
			for(int i = 0; i < listItem.size(); i++)
				if(listItem.get(i) == checkBoxPosition)
				{
					check.setChecked(true);
					isChecked = true;
					break;
				}

			if(!isChecked)
				check.setChecked(false);
		}

		if(item.getIsCheck())
		{
			check.setChecked(true);
			listItem.add(position);
		}

		return convertView;
	}
}
