package com.demo.wtm;

import java.util.ArrayList;

import com.demo.wtm.config.JSONTag;
import com.demo.wtm.fragment.CategoryFragment;
import com.demo.wtm.fragment.UserRoomFragment;
import com.demo.wtm.model.Category;
import com.demo.wtm.model.User;
import com.demo.wtm.request.Request;
import com.demo.wtm.request.RequestFactory;
import com.demo.wtm.request.RequestManager;
import com.demo.wtm.request.RequestMethod;
import com.demo.wtm.request.RequestManager.RequestListener;
import com.demo.wtm.service.RequestService;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;


public class PageActivity extends BaseActivity implements RequestListener, OnClickListener {

	private static final String TAG = "PageActivity";
    //private FragmentTabHost mTabHost;
	//private RequestManager mRequestManager;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page);
		
		//View vPageActionBar = getLayoutInflater().inflate(R.layout.menu_actionbar_page, null);
		//vPageActionBar.findViewById(R.id.bt_make_room).setOnClickListener(this);
		//vPageActionBar.findViewById(R.id.bt_profile).setOnClickListener(this);
		
		//ActionBar actionBar = getActionBar();
		//actionBar.setCustomView(vPageActionBar);
		//actionBar.setDisplayShowCustomEnabled(true);
		
		TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
		tabHost.setup();
		
		TabSpec spec1 = tabHost.newTabSpec("tab1");
		spec1.setIndicator("My Room");
		spec1.setContent(R.id.framelayout_content);
		tabHost.addTab(spec1);
		
		TabSpec spec2 = tabHost.newTabSpec("tab2");
		spec2.setIndicator("Category");
		spec2.setContent(R.id.framelayout_content2);
		tabHost.addTab(spec2);
		
	    LinearLayout linearLayoutMain = (LinearLayout)findViewById(R.id.linearLayoutPage);
	    registerForContextMenu(linearLayoutMain);

	    displayFragment();
	}
	
	public void displayFragment(){
		RequestFactory requestFactory = new RequestFactory();
        RequestManager requestManager = new RequestManager(this, RequestService.class);
		
		Request request = requestFactory.getUserRoomRequest();
		request.put(JSONTag.DELIVER_TAG_METHOD, RequestMethod.GET.name());
		requestManager.execute(this, request);
				
		Request request2 = requestFactory.getCategoryRequest();
		request2.put(JSONTag.DELIVER_TAG_METHOD, RequestMethod.GET.name());
		requestManager.execute(this, request2);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		if(arg0.getId() == R.id.bt_make_room){
			Intent intent = new Intent(getBaseContext(), RoomActivity.class);
			startActivity(intent);	
		}else{
			Intent intent = new Intent(getBaseContext(), UserActivity.class);
			startActivity(intent);
		}

	}


	@Override
	public void onRequestFinished(Request request, Bundle bundle) {
		// TODO Auto-generated method stub
		switch(request.getRequestType()){
		case RequestFactory.USER_ROOM_REQUEST:{
			FragmentManager fm = getSupportFragmentManager();
			User user = bundle.getParcelable(RequestFactory.REQUEST_BUNDLE_USER_ROOM);
			UserRoomFragment f = UserRoomFragment.newInstance(user);
			
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.framelayout_content, f);
			ft.commit();
		}
		break;
		case RequestFactory.CATEGORY_REQUEST:{
			FragmentManager fm = getSupportFragmentManager();
			ArrayList<Category> categoryList = bundle.getParcelableArrayList(RequestFactory.REQUEST_BUNDLE_CATEGORY);
			CategoryFragment f = CategoryFragment.newInstance(categoryList);
			
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.framelayout_content2, f);
			ft.commit();

		
		}
		break;
		}

	}
	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.add("New Room");
        menu.add("Profile");
        menu.add("reload");
    } 	
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if ("New Room" == item.getTitle()){
			Intent intent = new Intent(getBaseContext(), RoomActivity.class);
			startActivity(intent);	
        }else if ("Profile" == item.getTitle()){
			Intent intent = new Intent(getBaseContext(), UserActivity.class);
			startActivity(intent);
        }else if ("reload" == item.getTitle()){
        	displayFragment();
        }
		return true;
    }    


}
