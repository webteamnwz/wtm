package com.demo.wtm.fragment;

import java.util.ArrayList;

import com.demo.wtm.R;
import com.demo.wtm.RoomActivity;
import com.demo.wtm.config.JSONTag;
import com.demo.wtm.model.Room;
import com.demo.wtm.model.RoomArrayAdapter;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryRoomFragment extends Fragment{
	
	private static final String TAG = "CategoryRoomFragment";
	
	private static ArrayList<Room> mRoomList;
	
	public static final CategoryRoomFragment newInstance(ArrayList<Room> roomList){
		
		CategoryRoomFragment f = new CategoryRoomFragment();
		Bundle bundle = new Bundle();
	
		bundle.putParcelableArrayList(JSONTag.DELIVER_TAG_ROOM_LIST, roomList);
		f.setArguments(bundle);

		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		mRoomList = getArguments().getParcelableArrayList(JSONTag.DELIVER_TAG_ROOM_LIST);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_room_list, container, false);

		ListView lv = (ListView)v.findViewById(R.id.room_list);
		
		RoomArrayAdapter roomAdapter = new RoomArrayAdapter(getActivity(), android.R.layout.simple_list_item_activated_1, mRoomList);
		lv.setAdapter(roomAdapter);	
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				Room room = (Room) arg0.getAdapter().getItem(arg2);
				
				Intent intent = new Intent(getActivity().getBaseContext(), RoomActivity.class);
				intent.putExtra(JSONTag.DELIVER_TAG_ROOM_NO, room.mRoomNo);
				intent.putExtra(JSONTag.DELIVER_TAG_ROOM_JOINED, false);

				startActivity(intent);	
			}
			
		});
		
	
		return v;
	}

}
