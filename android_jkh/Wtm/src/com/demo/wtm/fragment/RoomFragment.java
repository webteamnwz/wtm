package com.demo.wtm.fragment;

import java.util.Calendar;

import com.demo.wtm.R;
import com.demo.wtm.RoomActivity;
import com.demo.wtm.config.JSONTag;
import com.demo.wtm.model.Room;
import com.demo.wtm.request.Request;
import com.demo.wtm.request.RequestFactory;
import com.demo.wtm.request.RequestManager;
import com.demo.wtm.request.RequestMethod;
import com.demo.wtm.request.RequestManager.RequestListener;
import com.demo.wtm.service.RequestService;
import com.demo.wtm.util.WtmPreference;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RoomFragment extends Fragment implements RequestListener, OnClickListener{
	
	private static final String TAG = "RoomFragment";
	
	private RequestManager mRequestManager;
	
	//private Room room;
	private EditText etRoomCategory;
	private EditText etRoomTitle;
	private EditText etRoomDesc;
	private EditText etStartDate;
	private EditText etEndDate;
	private Room mRoom;
	
	private int mStYear;
	private int mStMonth;
	private int mStDay;
	private int mEdYear;
	private int mEdMonth;
	private int mEdDay;
	
	private Button btRoomIn;
	private Button btRoomOut;
	private Button btRoomCheck;
	private Button btRoomModify;
	private Button btRoomClose;	
	private Button btRoomCreate;
	
	private boolean mIsMasterRoom = false;
	private boolean mIsJoinedRoom = false;
	private boolean mIsCheckedRoom = false;
	
	private String mUserId;
	
	public static final RoomFragment newInstance(Room room){
		
		RoomFragment f = new RoomFragment();
		Bundle bundle = new Bundle();
		
		bundle.putParcelable(JSONTag.DELIVER_TAG_ROOM, room);
		f.setArguments(bundle);
		return f;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		mRoom = getArguments().getParcelable(JSONTag.DELIVER_TAG_ROOM);
		
		WtmPreference wp = new WtmPreference(getActivity());
		mUserId = wp.getString("userid", "");
		
		if(mRoom.mRoomManagerId.equals(mUserId))
			mIsMasterRoom = true;
		
		mIsJoinedRoom = mRoom.mIsJoin;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		
		View v = inflater.inflate(R.layout.fragment_room, container, false);
		
		etRoomCategory = (EditText)v.findViewById(R.id.et_room_category);
		etRoomTitle = (EditText)v.findViewById(R.id.et_room_title);
		etRoomDesc = (EditText)v.findViewById(R.id.et_room_desc);
		etStartDate = (EditText)v.findViewById(R.id.et_room_start_date);
		etEndDate = (EditText)v.findViewById(R.id.et_room_end_date);
		
		btRoomIn = (Button)v.findViewById(R.id.bt_room_insert);
		btRoomCheck = (Button)v.findViewById(R.id.bt_room_update);
		btRoomOut = (Button)v.findViewById(R.id.bt_room_delete);
		btRoomModify = (Button)v.findViewById(R.id.bt_room_modify);
		btRoomClose = (Button)v.findViewById(R.id.bt_room_close);
		btRoomCreate = (Button)v.findViewById(R.id.bt_room_create);
		
		etRoomCategory.setFocusable(false);
		etRoomTitle.setFocusable(false);
		etRoomDesc.setFocusable(false);
		etStartDate.setFocusable(false);
		etEndDate.setFocusable(false);

		btRoomIn.setOnClickListener(this);
		btRoomCheck.setOnClickListener(this);
		btRoomOut.setOnClickListener(this);
		btRoomModify.setOnClickListener(this);
		btRoomClose.setOnClickListener(this);
		btRoomCreate.setOnClickListener(this);
						
		mStYear = Integer.parseInt(mRoom.mRoomStartDate.substring(0,4));
		mStMonth = Integer.parseInt(mRoom.mRoomStartDate.substring(4,6));
		mStDay = Integer.parseInt(mRoom.mRoomStartDate.substring(6,8));	
		mEdYear = Integer.parseInt(mRoom.mRoomEndDate.substring(0,4));
		mEdMonth = Integer.parseInt(mRoom.mRoomEndDate.substring(4,6));
		mEdDay = Integer.parseInt(mRoom.mRoomEndDate.substring(6,8));
		

		btRoomClose.setVisibility(View.VISIBLE);

		// master room
		if(mIsMasterRoom){
			btRoomModify.setVisibility(View.VISIBLE);
		}	
	
		// joined room
		if(mIsJoinedRoom){
			btRoomOut.setVisibility(View.VISIBLE);
		}else{
			btRoomIn.setVisibility(View.VISIBLE);
		}

		// checked room
		if(!mIsCheckedRoom){
			btRoomCheck.setVisibility(View.VISIBLE);
		}
		
		
		//etCategoryIdx.setText()
		etRoomCategory.setText(mRoom.mRoomCategoryIds);
		etRoomTitle.setText(mRoom.mRoomTitle);
		etRoomDesc.setText(mRoom.mRoomDesc);
		etStartDate.setText(new StringBuilder().append(mStYear).append('-').append(mStMonth).append('-').append(mStDay));
		etEndDate.setText(new StringBuilder().append(mEdYear).append('-').append(mEdMonth).append('-').append(mEdDay));
	
		return v;
	}


	@Override
	public void onRequestFinished(Request request, Bundle bundle) {
		// TODO Auto-generated method stub
		Toast toast = Toast.makeText(getActivity(), bundle.getString(RequestFactory.REQUEST_BUNDLE_USER_ROOM), Toast.LENGTH_SHORT);
		toast.show();
		
		getActivity().finish();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Room room = getArguments().getParcelable(JSONTag.DELIVER_TAG_ROOM);
	
		RequestFactory requestFactory = new RequestFactory();
		Request request = requestFactory.getUserRoomRequest();
        mRequestManager = new RequestManager(getActivity(), RequestService.class);
		
		switch(v.getId()){
		case R.id.bt_room_insert:{
			request.put("method", RequestMethod.POST.name());
			request.put(JSONTag.DELIVER_TAG_ROOM_NO, room.mRoomNo);
			mRequestManager.execute(this, request);
		}
		break;
		case R.id.bt_room_update:{
			request.put("method", RequestMethod.PUT.name());
			request.put(JSONTag.DELIVER_TAG_ROOM_NO, room.mRoomNo);
			mRequestManager.execute(this, request);
		}
		break;
		case R.id.bt_room_delete:{
			request.put("method", RequestMethod.DELETE.name());
			request.put(JSONTag.DELIVER_TAG_ROOM_NO, room.mRoomNo);
			mRequestManager.execute(this, request);
		}
		break;
		case R.id.bt_room_modify:{
			FragmentManager fm = getFragmentManager();
			RoomModifyFragment f = RoomModifyFragment.newInstance(room);
			
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.framelayout_room, f);
			ft.commit();	
		}
		break;
		case R.id.bt_room_close:{
			getActivity().finish();
		}
		break;
		}
		



	}


}
