package com.demo.wtm.fragment;

import com.demo.wtm.R;
import com.demo.wtm.RoomActivity;
import com.demo.wtm.UserActivity;
import com.demo.wtm.config.JSONTag;
import com.demo.wtm.model.Room;
import com.demo.wtm.model.RoomArrayAdapter;
import com.demo.wtm.model.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class UserRoomFragment extends Fragment{
	
	private static final String TAG = "UserRoomFragment";
	private static User mUser;
	
	public static final UserRoomFragment newInstance(User user){
		
		UserRoomFragment f = new UserRoomFragment();
		Bundle args = new Bundle();
			
		args.putParcelable(JSONTag.DELIVER_TAG_USER, user);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mUser = getArguments().getParcelable(JSONTag.DELIVER_TAG_USER);


	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_room_list, container, false);
		ListView lv = (ListView)v.findViewById(R.id.room_list);
		
		RoomArrayAdapter roomAdapter = new RoomArrayAdapter(getActivity(), android.R.layout.simple_list_item_activated_1, mUser.mRoomList);
		lv.setAdapter(roomAdapter);	
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub				
				Room room = (Room) arg0.getAdapter().getItem(arg2);

				Intent intent = new Intent(getActivity().getBaseContext(), RoomActivity.class);
				intent.putExtra(JSONTag.DELIVER_TAG_ROOM_NO, room.mRoomNo);
				intent.putExtra(JSONTag.DELIVER_TAG_ROOM_JOINED, true);
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
