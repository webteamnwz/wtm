package com.demo.wtm.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.demo.wtm.R;
import com.demo.wtm.config.JSONTag;
import com.demo.wtm.model.Category;
import com.demo.wtm.model.Room;
import com.demo.wtm.request.Request;
import com.demo.wtm.request.RequestFactory;
import com.demo.wtm.request.RequestManager;
import com.demo.wtm.request.RequestMethod;
import com.demo.wtm.request.RequestManager.RequestListener;
import com.demo.wtm.service.RequestService;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class RoomModifyFragment extends Fragment implements OnClickListener, RequestListener{
	
	private static final String TAG = "RoomModifyFragment";
	
	private Room mRoom;
	private String[] category;

	private EditText etRoomCategory;
	private EditText etRoomTitle;
	private EditText etRoomDesc;
	private EditText etStartDate;
	private EditText etEndDate;
	
	private int mStYear;
	private int mStMonth;
	private int mStDay;
	private int mEdYear;
	private int mEdMonth;
	private int mEdDay;
	
	private String mRoomCategoryNo;
	private ArrayList<Category> mCategoryList;
		
	private Button btRoomModify;
	private Button btRoomClose;	
	private Button btRoomCreate;
	
	private boolean mIsCreate = false;
	private boolean mIsModify = false;
	
	public static final RoomModifyFragment newInstance(Room room){
		RoomModifyFragment f = new RoomModifyFragment();
		
		Bundle bundle = new Bundle();
		
		bundle.putParcelable(JSONTag.DELIVER_TAG_ROOM, room);		
		f.setArguments(bundle);
	
		return f;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		mRoom = getArguments().getParcelable(JSONTag.DELIVER_TAG_ROOM);
		if(mRoom.mRoomNo == null)
			mIsCreate = true;
		else
			mIsModify = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_room, container, false);
		
		etRoomCategory = (EditText)v.findViewById(R.id.et_room_category);
		etRoomTitle = (EditText)v.findViewById(R.id.et_room_title);
		etRoomDesc = (EditText)v.findViewById(R.id.et_room_desc);
		etStartDate = (EditText)v.findViewById(R.id.et_room_start_date);
		etEndDate = (EditText)v.findViewById(R.id.et_room_end_date);
		
		btRoomCreate = (Button)v.findViewById(R.id.bt_room_create);
		btRoomModify = (Button)v.findViewById(R.id.bt_room_modify);
		btRoomClose = (Button)v.findViewById(R.id.bt_room_close);

		
		etRoomCategory.setFocusable(false);
		etStartDate.setFocusable(false);
		etEndDate.setFocusable(false);
		
		etRoomCategory.setOnClickListener(this);
		etStartDate.setOnClickListener(this);
		etEndDate.setOnClickListener(this);
		
		btRoomCreate.setOnClickListener(this);
		btRoomModify.setOnClickListener(this);
		btRoomClose.setOnClickListener(this);
		
		btRoomClose.setVisibility(View.VISIBLE);
		
		if(mIsCreate){
			
			btRoomCreate.setVisibility(View.VISIBLE);
			
			final Calendar c = Calendar.getInstance();
			mStYear = c.get(Calendar.YEAR);
			mStMonth = c.get(Calendar.MONTH);
			mStDay = c.get(Calendar.DAY_OF_MONTH);
			
			mEdYear = mStYear;
			mEdMonth = mStMonth;
			mEdDay = mStDay;
			
		}else if(mIsModify){
			
			btRoomModify.setVisibility(View.VISIBLE);
			
			mStYear = Integer.parseInt(mRoom.mRoomStartDate.substring(0,4));
			mStMonth = Integer.parseInt(mRoom.mRoomStartDate.substring(4,6))-1;
			mStDay = Integer.parseInt(mRoom.mRoomStartDate.substring(6,8));

			mEdYear = Integer.parseInt(mRoom.mRoomEndDate.substring(0,4));
			mEdMonth = Integer.parseInt(mRoom.mRoomEndDate.substring(4,6))-1;
			mEdDay = Integer.parseInt(mRoom.mRoomEndDate.substring(6,8));
			
			etRoomCategory.setText(mRoom.mRoomCategoryIds);
			etRoomTitle.setText(mRoom.mRoomTitle);
			etRoomDesc.setText(mRoom.mRoomDesc);

		}
		updateDisplay();

		return v;
	}
	
	private void updateDisplay(){
		etStartDate.setText(new StringBuilder().append(mStYear).append('-').append(mStMonth+1).append('-').append(mStDay));
		etEndDate.setText(new StringBuilder().append(mEdYear).append('-').append(mEdMonth+1).append('-').append(mEdDay));

	}
	
    private DatePickerDialog.OnDateSetListener mStDateSetListener = 
            new DatePickerDialog.OnDateSetListener() {
                 
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                    // TODO Auto-generated method stub
                    mStYear = year;
                    mStMonth = monthOfYear;
                    mStDay = dayOfMonth;
                    updateDisplay();
                }

            };

    private DatePickerDialog.OnDateSetListener mEdDateSetListener = 
            new DatePickerDialog.OnDateSetListener() {
                 
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                    // TODO Auto-generated method stub
                    mEdYear = year;
                    mEdMonth = monthOfYear;
                    mEdDay = dayOfMonth;
                    updateDisplay();
                }

            };

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()){
		case R.id.et_room_category:{
			
			RequestFactory requestFactory = new RequestFactory();
			Request request = requestFactory.getCategoryRequest();
			RequestManager requestManager = new RequestManager(getActivity(), RequestService.class);
			request.put(JSONTag.DELIVER_TAG_METHOD, RequestMethod.GET.name());
			
			requestManager.execute(this, request);		
			

		}
		break;
		case R.id.et_room_end_date:{
			DatePickerDialog d = new DatePickerDialog(getActivity(),
			        mStDateSetListener, mStYear, mStMonth, mStDay);
			d.show();			
		}
		break;
		case R.id.et_room_start_date:{
			DatePickerDialog d = new DatePickerDialog(getActivity(),
			        mStDateSetListener, mStYear, mStMonth, mStDay);
			d.show();	
		}
		break;
		case R.id.bt_room_close:{
			getActivity().finish();
		}
		break;
		case R.id.bt_room_modify:{
			RequestFactory requestFactory = new RequestFactory();
			Request request = requestFactory.getRoomRequest();
			RequestManager requestManager = new RequestManager(getActivity(), RequestService.class);
			request.put(JSONTag.DELIVER_TAG_METHOD, RequestMethod.PUT.name());
			
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

			cal.set(mStYear, mStMonth, mStDay);
			String startDate = format.format(cal.getTime()); 
			
			cal.set(mEdYear, mEdMonth, mEdDay);
			String endDate = format.format(cal.getTime()); 
			
			request.put(JSONTag.DELIVER_TAG_ROOM_NO, mRoom.mRoomNo);
			request.put(JSONTag.DELIVER_TAG_CATEGORY_NO, mRoomCategoryNo);
			request.put(JSONTag.DELIVER_TAG_ROOM_TITLE, etRoomTitle.getText().toString());
			request.put(JSONTag.DELIVER_TAG_ROOM_DESC, etRoomDesc.getText().toString());
			request.put(JSONTag.DELIVER_TAG_ROOM_STARTDATE, startDate);
			request.put(JSONTag.DELIVER_TAG_ROOM_ENDDATE, endDate);

			requestManager.execute(this, request);			
		}
		break;
		case R.id.bt_room_create:{
			RequestFactory requestFactory = new RequestFactory();
			Request request = requestFactory.getRoomRequest();
			RequestManager requestManager = new RequestManager(getActivity(), RequestService.class);
			request.put(JSONTag.DELIVER_TAG_METHOD, RequestMethod.POST.name());
			
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

			cal.set(mStYear, mStMonth, mStDay);
			String startDate = format.format(cal.getTime()); 
			
			cal.set(mEdYear, mEdMonth, mEdDay);
			String endDate = format.format(cal.getTime()); 
			
			request.put(JSONTag.DELIVER_TAG_CATEGORY_NO, mRoomCategoryNo);
			request.put(JSONTag.DELIVER_TAG_ROOM_TITLE, etRoomTitle.getText().toString());
			request.put(JSONTag.DELIVER_TAG_ROOM_DESC, etRoomDesc.getText().toString());
			request.put(JSONTag.DELIVER_TAG_ROOM_STARTDATE, startDate);
			request.put(JSONTag.DELIVER_TAG_ROOM_ENDDATE, endDate);

			requestManager.execute(this, request);
		}
		break;
			
		}
		
	}


	@Override
	public void onRequestFinished(Request request, Bundle bundle) {
		// TODO Auto-generated method stub
		
		if(request.getRequestType() == RequestFactory.ROOM_REQUEST){
			Toast toast = Toast.makeText(getActivity(), bundle.getString(RequestFactory.REQUEST_BUNDLE_ROOM), Toast.LENGTH_SHORT);
			toast.show();
			
			getActivity().finish();			
		}else if(request.getRequestType() == RequestFactory.CATEGORY_REQUEST){
			
			mCategoryList = bundle.getParcelableArrayList(RequestFactory.REQUEST_BUNDLE_CATEGORY);			
			ArrayList<String> stringList = new ArrayList<String>();
			
			for(int i=0;i<mCategoryList.size();i++){
				stringList.add(mCategoryList.get(i).mCategoryName);
			}
			
			Object[] objectList = stringList.toArray();
			category = Arrays.copyOf(objectList, objectList.length, String[].class);
			
			new AlertDialog.Builder(getActivity()).setTitle("Category").setItems(category, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int idx) {
					// TODO Auto-generated method stub
					etRoomCategory.setText(mCategoryList.get(idx).mCategoryName);
					mRoomCategoryNo = mCategoryList.get(idx).mCategoryNo;
					
				}
			}).setNeutralButton("Close", null).show();	
		}
	}
}
