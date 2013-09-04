package com.demo.wtm.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Room implements Parcelable{
	public String mRoomNo;
	public String mRoomTitle;
	public String mRoomManagerId;
	public String mRoomDesc;
	public String mRoomStartDate;
	public String mRoomEndDate;
	public String mRoomJoinCnt;
	public String mRoomCategoryIds;
	public boolean mIsJoin;
	
	public Room()
	{
	}
	
	public Room(String no, String title, String id, String desc, String start, String end, String category_ids, boolean isJoin){
		mRoomNo = no;
		mRoomTitle = title;
		mRoomManagerId = id;
		mRoomDesc = desc;
		mRoomStartDate = start;
		mRoomEndDate = end;
		mRoomCategoryIds = category_ids;
		mIsJoin = isJoin;
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
