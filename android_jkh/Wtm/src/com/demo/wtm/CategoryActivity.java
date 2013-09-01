package com.demo.wtm;

import java.util.ArrayList;

import com.demo.wtm.config.JSONTag;
import com.demo.wtm.fragment.CategoryFragment;
import com.demo.wtm.model.Category;
import com.demo.wtm.request.Request;
import com.demo.wtm.request.RequestFactory;
import com.demo.wtm.request.RequestManager;
import com.demo.wtm.request.RequestMethod;
import com.demo.wtm.request.RequestManager.RequestListener;
import com.demo.wtm.service.RequestService;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class CategoryActivity extends BaseActivity implements RequestListener {

	private static final String TAG = "CategoryActivity";
	
	private RequestManager mRequestManager;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		
		RequestFactory requestFactory = new RequestFactory();
		Request request = requestFactory.getCategoryRequest();
        mRequestManager = new RequestManager(this, RequestService.class);
        
        request.put(JSONTag.DELIVER_TAG_METHOD, RequestMethod.GET.name());
		
		mRequestManager.execute(this, request);
   
	}


	@Override
	public void onRequestFinished(Request request, Bundle bundle) {
		// TODO Auto-generated method stub
		
		FragmentManager fm = getSupportFragmentManager();
		ArrayList<Category> categoryList = bundle.getParcelableArrayList(RequestFactory.REQUEST_BUNDLE_CATEGORY);		
		CategoryFragment f = CategoryFragment.newInstance(categoryList);
		
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.framelayout_category, f);
		ft.commit();
		
	}
	
}
