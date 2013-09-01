package com.demo.wtm.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.wtm.R;
import com.demo.wtm.config.JSONTag;
import com.demo.wtm.model.User;
import com.demo.wtm.request.Request;
import com.demo.wtm.request.RequestFactory;
import com.demo.wtm.request.RequestManager;
import com.demo.wtm.request.RequestMethod;
import com.demo.wtm.request.RequestManager.RequestListener;
import com.demo.wtm.service.RequestService;

public class UserModifyFragment extends Fragment implements RequestListener, OnClickListener{
	
	private static final String TAG = "UserModifyFragment";
	
	private User mUser;
	
	private EditText etUserName;
	private EditText etUserEmail;
	private EditText etUserProfile;
	private EditText etUserGroup;
	private EditText etUserImg;
	
	private Button btUserSave;
	private Button btUserCancel;
	
	public static final UserModifyFragment newInstance(User user){
		UserModifyFragment f = new UserModifyFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(JSONTag.DELIVER_TAG_USER, user);
		f.setArguments(bundle);
		
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
		
		View v = inflater.inflate(R.layout.fragment_user_modify, container, false);
		
		etUserName = (EditText)v.findViewById(R.id.et_user_name);
		etUserEmail = (EditText)v.findViewById(R.id.et_user_email);
		etUserGroup = (EditText)v.findViewById(R.id.et_user_group);
		etUserProfile = (EditText)v.findViewById(R.id.et_user_profile);
		etUserImg = (EditText)v.findViewById(R.id.et_user_img);
		
		btUserSave = (Button)v.findViewById(R.id.bt_user_save);
		btUserCancel = (Button)v.findViewById(R.id.bt_user_cancel);

		etUserName.setText(mUser.mUserName);
		etUserEmail.setText(mUser.mUserEmail);
		etUserGroup.setText(mUser.mUserGroup);
		etUserImg.setText(mUser.mUserImg);
		etUserProfile.setText(mUser.mUserProfile);
		
		btUserSave.setOnClickListener(this);
		btUserCancel.setOnClickListener(this);
		
		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.bt_user_cancel:{
			getActivity().finish();
		}
		break;
		case R.id.bt_user_save:{
			RequestFactory requestFactory = new RequestFactory();
			Request request = requestFactory.getUserRequest();
			RequestManager requestManager = new RequestManager(getActivity(), RequestService.class);
			request.put(JSONTag.DELIVER_TAG_METHOD, RequestMethod.PUT.name());
			
			
			request.put("user_img", etUserImg.getText().toString());
			request.put("user_name", etUserName.getText().toString());
			request.put("user_group", etUserGroup.getText().toString());
			request.put("user_email", etUserEmail.getText().toString());
			request.put("user_profile", etUserProfile.getText().toString());

			requestManager.execute(this, request);
		}
		break;
		}

		
	}

	@Override
	public void onRequestFinished(Request request, Bundle bundle) {
		// TODO Auto-generated method stub
		Toast toast = Toast.makeText(getActivity(), bundle.getString(RequestFactory.REQUEST_BUNDLE_USER), Toast.LENGTH_SHORT);
		toast.show();
		
		getActivity().finish();
	}


	

}
