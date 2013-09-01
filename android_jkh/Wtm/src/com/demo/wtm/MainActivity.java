package com.demo.wtm;

import com.demo.wtm.request.Request;
import com.demo.wtm.request.RequestFactory;
import com.demo.wtm.request.RequestManager;
import com.demo.wtm.request.RequestManager.RequestListener;
import com.demo.wtm.service.AuthService;
import com.demo.wtm.service.RequestService;
import com.demo.wtm.util.WtmPreference;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements RequestListener, OnClickListener{

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		UserAuth mUserAuth = new UserAuth();
		
		AuthService mAuth = new AuthService(this, mUserAuth);
		

		Button btnApp = (Button)findViewById(R.id.btnApp);
		btnApp.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		
	public class UserAuth implements UserAuthenticationCallback
	{	
		@Override
        public void onCompleted(MobileServiceUser user,
                Exception exception, ServiceFilterResponse response) {

            if (exception == null) {       
                WtmPreference wp = new WtmPreference(getBaseContext());
                wp.putString("authtoken", user.getAuthenticationToken());
                wp.putString("userid", user.getUserId());
                wp.commit();
                
                
        		Intent intent = new Intent(getBaseContext(), PageActivity.class);
        		startActivity(intent);
        		
            } else {
                Log.i("kh", "Auth error");
            }			
		}

	}

	@Override
	public void onRequestFinished(Request request, Bundle bundle) {
		// TODO Auto-generated method stub
		
		Toast toast = Toast.makeText(this, bundle.getString("USERINFO"), Toast.LENGTH_SHORT);
		toast.show();
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getBaseContext(), PageActivity.class);
		startActivity(intent);	
		this.finish();
	}
}
