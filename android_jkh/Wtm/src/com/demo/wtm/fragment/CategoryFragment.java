package com.demo.wtm.fragment;

import java.util.ArrayList;

import com.demo.wtm.CategoryRoomActivity;
import com.demo.wtm.R;
import com.demo.wtm.config.JSONTag;
import com.demo.wtm.model.Category;
import com.demo.wtm.model.CategoryArrayAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryFragment extends Fragment{
	
	private static final String TAG = "CategoryFragment";
	
	private static ArrayList<Category> mCategoryList;
	
	public static final CategoryFragment newInstance(ArrayList<Category> categoryList){

		CategoryFragment f = new CategoryFragment();
		Bundle bundle = new Bundle();
			
		bundle.putParcelableArrayList(JSONTag.DELIVER_TAG_CATEGORY_LIST, categoryList);
		f.setArguments(bundle);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		mCategoryList = getArguments().getParcelableArrayList(JSONTag.DELIVER_TAG_CATEGORY_LIST);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_category_list, container, false);

		ListView lv = (ListView)v.findViewById(R.id.category_list);
		
		CategoryArrayAdapter categoryAdapter = new CategoryArrayAdapter(getActivity(), android.R.layout.simple_list_item_activated_1, mCategoryList);
		lv.setAdapter(categoryAdapter);	
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub				
				Category category = (Category) arg0.getAdapter().getItem(arg2);
	
				Intent intent = new Intent(getActivity().getBaseContext(), CategoryRoomActivity.class);
				intent.putExtra(JSONTag.DELIVER_TAG_CATEGORY_NO, category.mCategoryNo);
				startActivity(intent);		

			}
			
		});
		
		lv.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
	
		return v;
	}
}
