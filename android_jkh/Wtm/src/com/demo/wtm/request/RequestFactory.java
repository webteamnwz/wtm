package com.demo.wtm.request;

public class RequestFactory {
	
	public static final int USER_REQUEST = 1;
	public static final int USER_ROOM_REQUEST = 2;
	public static final int ROOM_REQUEST = 3;
	public static final int CATEGORY_ROOM_REQUEST = 4;
	public static final int CATEGORY_REQUEST = 5;
	
	public static final String REQUEST_BUNDLE_USER = "com.demo.wtm.extra.user";
	public static final String REQUEST_BUNDLE_USER_ROOM = "com.demo.wtm.extra.user_room";
	public static final String REQUEST_BUNDLE_ROOM = "com.demo.wtm.extra.room";
	public static final String REQUEST_BUNDLE_CATEGORY_ROOM = "com.demo.wtm.extra.category_room";
	public static final String REQUEST_BUNDLE_CATEGORY = "com.demo.wtm.extra.category";
	
	public RequestFactory(){}
	
	
	public Request getUserRequest(){
		return new Request(USER_REQUEST);
	}
	
	public Request getUserRoomRequest(){
		return new Request(USER_ROOM_REQUEST);
	}
	
	public Request getRoomRequest(){
		return new Request(ROOM_REQUEST);
	}
	
	public Request getCategoryRoomRequest(){
		return new Request(CATEGORY_ROOM_REQUEST);
	}
	
	public Request getCategoryRequest(){
		return new Request(CATEGORY_REQUEST);
	}
}
