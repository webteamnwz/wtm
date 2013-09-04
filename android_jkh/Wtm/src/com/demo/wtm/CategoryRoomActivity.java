package com.demo.wtm;

import java.util.ArrayList;

import com.demo.wtm.config.JSONTag;
import com.demo.wtm.fragment.CategoryRoomFragment;
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


public class CategoryRoomActivity extends BaseActivity implements RequestListener {

	private static final String TAG = "CategoryRoomActivity";
	
	private RequestManager mRequestManager;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_room);
		
		RequestFactory requestFactory = new RequestFactory();
		Request request = requestFactory.getCategoryRoomRequest();
		request.put(JSONTag.DELIVER_TAG_CATEGORY_NO, getIntent().getStringExtra(JSONTag.DELIVER_TAG_CATEGORY_NO));
		request.put(JSONTag.DELIVER_TAG_METHOD, RequestMethod.GET.name());
        mRequestManager = new RequestManager(this, RequestService.class);
		
		mRequestManager.execute(this, request);
   
	}


	@Override
	public void onRequestFinished(Request request, Bundle bundle) {
		// TODO Auto-generated method stub
		
		FragmentManager fm = getSupportFragmentManager();
		ArrayList<Room> roomList = bundle.getParcelableArrayList(RequestFactory.REQUEST_BUNDLE_CATEGORY_ROOM);		
		CategoryRoomFragment f = CategoryRoomFragment.newInstance(roomList);
		
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.framelayout_category_room, f);
		ft.commit();
		
	}
	
}
