package com.demo.wtm.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.demo.wtm.R;
import com.demo.wtm.config.JSONTag;
import com.demo.wtm.model.User;

public class UserFragment extends Fragment implements OnClickListener{
	
	private static final String TAG = "UserFragment";
	
	private User mUser;
	
	private TextView tvUserName;
	private TextView tvUserEmail;
	private TextView tvUserProfile;
	private TextView tvUserGroup;
	private Button btUserModify;
	private Button btUserClose;
	
	public static final UserFragment newInstance(User user){
		UserFragment f = new UserFragment();
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
		
		View v = inflater.inflate(R.layout.fragment_user, container, false);
		
		tvUserName = (TextView)v.findViewById(R.id.tv_user_name);
		tvUserEmail = (TextView)v.findViewById(R.id.tv_user_email);
		tvUserGroup = (TextView)v.findViewById(R.id.tv_user_group);
		tvUserProfile = (TextView)v.findViewById(R.id.tv_user_profile);
		
		btUserModify = (Button)v.findViewById(R.id.bt_user_modify);
		btUserClose = (Button)v.findViewById(R.id.bt_user_close);
		
		tvUserName.setText(mUser.mUserName);
		tvUserEmail.setText(mUser.mUserEmail);
		tvUserGroup.setText(mUser.mUserGroup);
		tvUserProfile.setText(mUser.mUserProfile);
		
		btUserModify.setOnClickListener(this);
		btUserClose.setOnClickListener(this);
		
		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.bt_user_modify:{
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			UserModifyFragment f = UserModifyFragment.newInstance(mUser);
			ft.replace(R.id.framelayout_user, f);
			ft.addToBackStack(null);
			ft.commit();	
		}
		break;
		case R.id.bt_user_close:{
			getActivity().finish();
		}
		break;
		}
	}
	

}
