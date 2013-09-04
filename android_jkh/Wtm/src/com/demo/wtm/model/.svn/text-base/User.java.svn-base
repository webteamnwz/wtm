package com.demo.wtm.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
	
	public String mUserId;
	public String mUserImg;
	public String mUserName;
	public String mUserGroup;
	public String mUserEmail;
	public String mUserProfile;
	
	public ArrayList<Room> mRoomList = new ArrayList<Room>();
	
	public User()
	{
	}
	
	public User(String userId, String userImg, String userName, String userGroup, String userEmail, String userProfile){
		mUserId = userId;
		mUserImg = userImg;
		mUserName = userName;
		mUserGroup = userGroup;
		mUserEmail = userEmail;
		mUserProfile = userProfile;
	}
	
	public void addRoom(Room room){
		mRoomList.add(room);
	}
	
	public boolean isJoinRoom(Room room){
		for(int ii=0;ii < mRoomList.size();ii++){
			if(mRoomList.get(ii).mRoomNo.equals(room.mRoomNo)){
				return true;
			}
		}
		return false;
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
