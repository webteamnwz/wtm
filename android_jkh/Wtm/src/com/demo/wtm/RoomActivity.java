package com.demo.wtm;

import com.demo.wtm.config.JSONTag;
import com.demo.wtm.fragment.RoomFragment;
import com.demo.wtm.fragment.RoomModifyFragment;
import com.demo.wtm.model.Room;
import com.demo.wtm.request.Request;
import com.demo.wtm.request.RequestFactory;
import com.demo.wtm.request.RequestManager;
import com.demo.wtm.request.RequestMethod;
import com.demo.wtm.request.RequestManager.RequestListener;
import com.demo.wtm.service.RequestService;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class RoomActivity extends BaseActivity implements RequestListener {

	private static final String TAG = "RoomActivity";
	
	private String mRoomNo;
	private boolean mRoomJoined;
	
	private RequestManager mRequestManager;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room);
		
		RequestFactory requestFactory = new RequestFactory();
		Request request = requestFactory.getRoomRequest();
		
		mRoomNo = getIntent().getStringExtra(JSONTag.DELIVER_TAG_ROOM_NO);
		mRoomJoined = getIntent().getBooleanExtra(JSONTag.DELIVER_TAG_ROOM_JOINED, false);
		
		if(mRoomNo == null){
			
			FragmentManager fm = getSupportFragmentManager();
			Room room = new Room();	
			RoomModifyFragment f = RoomModifyFragment.newInstance(room);
			
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.framelayout_room, f);
			ft.commit();			
			
		}else{
		
			request.put(JSONTag.DELIVER_TAG_METHOD, RequestMethod.GET.name());
			request.put(JSONTag.DELIVER_TAG_ROOM_NO, mRoomNo);
			request.put(JSONTag.DELIVER_TAG_ROOM_JOINED, mRoomJoined);
			
	        mRequestManager = new RequestManager(this, RequestService.class);
			
			mRequestManager.execute(this, request);
		}
	}


	@Override
	public void onRequestFinished(Request request, Bundle bundle) {
		// TODO Auto-generated method stub
		
		FragmentManager fm = getSupportFragmentManager();
		Room room = bundle.getParcelable(RequestFactory.REQUEST_BUNDLE_ROOM);		
		RoomFragment f = RoomFragment.newInstance(room);
		
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.framelayout_room, f);
		ft.commit();
		
	}
	
}
