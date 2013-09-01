package com.demo.wtm;

import org.json.JSONException;
import org.json.JSONObject;

import com.demo.wtm.config.JSONTag;
import com.demo.wtm.fragment.RoomFragment;
import com.demo.wtm.fragment.UserFragment;
import com.demo.wtm.model.Room;
import com.demo.wtm.model.User;
import com.demo.wtm.request.Request;
import com.demo.wtm.request.RequestFactory;
import com.demo.wtm.request.RequestManager;
import com.demo.wtm.request.RequestMethod;
import com.demo.wtm.request.RequestManager.RequestListener;
import com.demo.wtm.service.RequestService;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


public class UserActivity extends BaseActivity implements RequestListener {

	private static final String TAG = "UserActivity";
	
	private RequestManager mRequestManager;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);

		RequestFactory requestFactory = new RequestFactory();
		Request request = requestFactory.getUserRequest();
        mRequestManager = new RequestManager(this, RequestService.class);
        
        request.put(JSONTag.DELIVER_TAG_METHOD, RequestMethod.GET.name());
		
		mRequestManager.execute(this, request);
        
	}

	@Override
	public void onRequestFinished(Request request, Bundle bundle) {
		// TODO Auto-generated method stub
		FragmentManager fm = getSupportFragmentManager();
		User user = bundle.getParcelable(RequestFactory.REQUEST_BUNDLE_USER);		
		UserFragment f = UserFragment.newInstance(user);
		
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.framelayout_user, f);
		ft.commit();
		
	}
	
}
