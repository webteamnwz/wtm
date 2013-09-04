package com.demo.wtm.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable{
	public String mCategoryNo;
	public String mCategoryName;
	public String mRoomCnt;

	
	public Category(String no, String name, String cnt){
		mCategoryNo = no;
		mCategoryName = name;
		mRoomCnt = cnt;

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
