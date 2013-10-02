package kr.po1.webtechmobile.Activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kr.po1.webtechmobile.R;
import kr.po1.webtechmobile.Stack;
import kr.po1.webtechmobile.WtmDataStore;
import kr.po1.webtechmobile.WtmUtil;
import kr.po1.webtechmobile.dialogButton;
import kr.po1.webtechmobile.dialogButtonClickListener;
import kr.po1.webtechmobile.DataSet.WtmCategoryDetail;
import kr.po1.webtechmobile.DataSet.WtmCategoryInfo;
import kr.po1.webtechmobile.DataSet.WtmCategoryRoomInfo;
import kr.po1.webtechmobile.DataSet.WtmRoomInfo;
import kr.po1.webtechmobile.DataSet.WtmUserInfo;
import kr.po1.webtechmobile.DataSet.WtmUserRoomDetail;
import kr.po1.webtechmobile.DataSet.WtmUserRoomInfo;
import kr.po1.webtechmobile.Effect.WtmAnimation;
import kr.po1.webtechmobile.Http.WtmHttpCall;
import kr.po1.webtechmobile.Http.WtmHttpCallHandler;
import kr.po1.webtechmobile.List.WtmCategoryListAdapter;
import kr.po1.webtechmobile.List.WtmCategoryListCheckAdapter;
import kr.po1.webtechmobile.List.WtmCategoryListCheckItem;
import kr.po1.webtechmobile.List.WtmCategoryListItem;
import kr.po1.webtechmobile.List.WtmRoomListAdapter;
import kr.po1.webtechmobile.List.WtmRoomListItem;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class WtmActivityHome extends WtmActivity implements OnClickListener,
		OnTouchListener, OnItemClickListener
{
	// 뷰 인덱스
	static int INDEX_MYROOM_LIST = 0;
	static int INDEX_CATEGORY_LIST = 1;
	static int INDEX_CATEGORY_ROOM_LIST = 2;
	static int INDEX_ROOM_INFO = 3;
	static int INDEX_ROOM_MODIFY = 4;
	static int INDEX_MY_INFO = 5;
	static int INDEX_MY_INFO_MODIFY = 6;

	// 탭 인덱스
	static int TAB_MYROOM = 0;
	static int TAB_CATEGORY = 1;

	// 애니메이션
	static int ANIM_NEXT = 1;
	static int ANIM_PREV = 2;

	// 방 수정 모드
	static int ROOM_MODIFY_MODE_ADD = 0;
	static int ROOM_MODIFY_MODE_MOD = 1;

	private Context thisContext = this;
	private ViewFlipper vf;
	private float positionX = 0f;

	private int tabIndex = TAB_MYROOM;

	private int roomModifyMode = -1;
	private WtmRoomInfo roomModifyInfo;

	private ArrayList<String> roomIndexList = new ArrayList<String>();
	private ArrayList<String> categoryIndexList = new ArrayList<String>();
	private WtmCategoryRoomInfo categoryRoomInfo;
	private ArrayList<String> categoryRoomIndexList = new ArrayList<String>();

	ArrayList<Integer> checkCate = new ArrayList<Integer>();
	ArrayList<Integer> selectedCate = new ArrayList<Integer>();
	ArrayList<Integer> tempCate;

	String DatePickerSelectDate;

	private WtmUserInfo roomMasterInfo;

	private Stack viewIndexStack = new Stack();

	private WtmRoomInfo CurrentRoomInfo;

	private int selectedRoomNo;

	// 선택된 카테고리 정보
	private WtmCategoryListItem selectCategoryListItem;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		setViewIndex();
		setViewIndex(INDEX_MYROOM_LIST);
		setTabButtonStyle(tabIndex);

		WtmAnimation.init();

		isLastActivity = true;

		// refreshCategoryInfo();
		setCategoryInfo();
		// refreshJoinedRoomInfo();
		setUserRoomInfo();

		setMyInfo();

		// 최초가입인 경우 유저정보 띄우기
		if(WtmDataStore.myInfo.getIsFirst())
			setViewIndex(INDEX_MY_INFO, ANIM_NEXT);

		Button btnMyRoom = (Button) findViewById(R.id.buttonMyRoom);
		btnMyRoom.setOnClickListener(this);

		Button btnCategory = (Button) findViewById(R.id.buttonCategoryList);
		btnCategory.setOnClickListener(this);

		vf = (ViewFlipper) findViewById(R.id.viewFlipper1);
		// vf.setOnTouchListener(this);
		//
		// for(int layoutIndex = 0; layoutIndex < vf.getChildCount();
		// layoutIndex++)
		// {
		// View childLayout = vf.getChildAt(layoutIndex);
		// childLayout.setOnTouchListener(this);
		// }

		// ------------------------------------------------
		// 각종 버튼들 초기 이벤트 생성
		// ------------------------------------------------

		// 마이 룸
		Button btnCreateRoom = (Button) findViewById(R.id.buttonCategoryRoomList_createRoom);
		btnCreateRoom.setOnClickListener(this);
		Button btnMoveCategory = (Button) findViewById(R.id.buttonUserRoomList_moveCategory);
		btnMoveCategory.setOnClickListener(this);
		Button btnCreateRoom2 = (Button) findViewById(R.id.buttonUserRoomList_createRoom);
		btnCreateRoom2.setOnClickListener(this);

		// 방 정보
		Button buttonJoin = (Button) findViewById(R.id.buttonRoomInfo_Join);
		Button buttonOut = (Button) findViewById(R.id.buttonRoomInfo_Out);
		Button buttonCheckin = (Button) findViewById(R.id.buttonRoomInfo_Checkin);
		Button buttonModify = (Button) findViewById(R.id.buttonRoomInfo_Modify);
		Button buttonDelete = (Button) findViewById(R.id.buttonRoomInfo_Delete);
		buttonJoin.setOnClickListener(this);
		buttonOut.setOnClickListener(this);
		buttonCheckin.setOnClickListener(this);
		buttonModify.setOnClickListener(this);
		buttonDelete.setOnClickListener(this);

		// 방 만들기(수정)
		Button btnModify = (Button) findViewById(R.id.buttonRoomModify_Modify);
		Button btnCancel = (Button) findViewById(R.id.buttonRoomModify_Cancel);
		btnModify.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		EditText editRoomModifyStartDate = (EditText) findViewById(R.id.editRoomModify_Startdate);
		EditText editRoomModifyEndDate = (EditText) findViewById(R.id.editRoomModify_Enddate);
		editRoomModifyStartDate.setOnClickListener(this);
		editRoomModifyEndDate.setOnClickListener(this);

		// ------------------------------------------------
	}

	private void setViewIndex()
	{
		vf = (ViewFlipper) findViewById(R.id.viewFlipper1);
		int viewCount = vf.getChildCount();

		for(int viewIndex = 0; viewIndex < viewCount; viewIndex++)
		{
			View layoutView = vf.getChildAt(viewIndex);

			switch(layoutView.getId())
			{
				case R.id.layoutUserRoomList:
					INDEX_MYROOM_LIST = viewIndex;
					Log.d("tag", "INDEX_MYROOM_LIST");
					break;

				case R.id.layoutCategoryListInfo:
					INDEX_CATEGORY_LIST = viewIndex;
					Log.d("tag", "INDEX_CATEGORY_LIST");
					break;

				case R.id.layoutCategoryRoomList:
					INDEX_CATEGORY_ROOM_LIST = viewIndex;
					Log.d("tag", "INDEX_CATEGORY_ROOM_LIST");
					break;

				case R.id.layoutRoomInfo:
					INDEX_ROOM_INFO = viewIndex;
					Log.d("tag", "INDEX_ROOM_INFO");
					break;

				case R.id.layoutRoomModify:
					INDEX_ROOM_MODIFY = viewIndex;
					Log.d("tag", "INDEX_ROOM_MODIFY");
					break;

				case R.id.layoutMyInfo:
					INDEX_MY_INFO = viewIndex;
					Log.d("tag", "INDEX_MY_INFO");
					break;

				case R.id.layoutMyInfoModify:
					INDEX_MY_INFO_MODIFY = viewIndex;
					Log.d("tag", "INDEX_MY_INFO_MODIFY");
					break;
			}
		}
	}

	public void setViewIndex(int index)
	{
		vf = (ViewFlipper) findViewById(R.id.viewFlipper1);
		vf.setDisplayedChild(index);
	}

	public void setViewIndex(int index, int animationType)
	{
		setViewIndex(index, animationType, true);
	}

	public void setViewIndex(int index, int animationType, boolean isSaveStack)
	{
		// 방 추가/수정인 경우 키보드 숨기기
		if(getViewIndex() == INDEX_ROOM_MODIFY)
		{
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(findViewById(R.id.editRoomModify_Title).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(findViewById(R.id.editRoomModify_Desc).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(findViewById(R.id.editRoomModify_Startdate).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(findViewById(R.id.editRoomModify_Enddate).getWindowToken(), 0);
		}

		vf = (ViewFlipper) findViewById(R.id.viewFlipper1);

		if(animationType == ANIM_NEXT)
		{
			vf.setInAnimation(WtmAnimation.rightIn);
			vf.setOutAnimation(WtmAnimation.leftOut);
		}
		else if(animationType == ANIM_PREV)
		{
			vf.setInAnimation(WtmAnimation.leftIn);
			vf.setOutAnimation(WtmAnimation.rightOut);
		}

		if(isSaveStack)
			viewIndexStack.push(getViewIndex());

		vf.setDisplayedChild(index);
	}

	public int getViewIndex()
	{
		vf = (ViewFlipper) findViewById(R.id.viewFlipper1);

		return vf.getDisplayedChild();
	}

	// 가입된 방 리스트 출력
	public boolean setUserRoomInfo()
	{
		WtmUserRoomInfo roomInfo = WtmDataStore.userRoomInfo;
		ArrayList<WtmRoomListItem> listContent = new ArrayList<WtmRoomListItem>();

		roomIndexList = new ArrayList<String>();

		ArrayList<WtmUserRoomDetail> roomList = roomInfo.getRoomList();
		for(int roomIndex = 0; roomIndex < roomList.size(); roomIndex++)
		{
			WtmUserRoomDetail roomDetail = roomList.get(roomIndex);
			roomIndexList.add(Integer.toString(roomDetail.getRoomNo()));

			listContent.add(new WtmRoomListItem(R.drawable.ic_action_home, roomDetail.getRoomTitle()));
		}

		View noRoom = findViewById(R.id.layoutUserRoomList_noRoom);
		if(roomList.size() < 1)
			noRoom.setVisibility(View.VISIBLE);
		else
			noRoom.setVisibility(View.GONE);

		WtmRoomListAdapter categoryRoomListAdapter = new WtmRoomListAdapter(this, R.layout.layout_list_style_2, listContent);
		try
		{
			ListView list = (ListView) findViewById(R.id.listUserRoomList);
			list.setAdapter(categoryRoomListAdapter);
			list.setOnItemClickListener(this);
		}
		catch(Exception e)
		{
			Log.e("tag", e.toString());
		}

		return true;
	}

	// 카테고리 리스트 출력
	private boolean setCategoryInfo()
	{
		ArrayList<WtmCategoryDetail> categoryList = WtmDataStore.categoryInfo.getCategoryList();
		ArrayList<WtmCategoryListItem> listContent = new ArrayList<WtmCategoryListItem>();

		categoryIndexList = new ArrayList<String>();

		for(int categoryIndex = 0; categoryIndex < categoryList.size(); categoryIndex++)
		{
			WtmCategoryDetail categoryDetail = categoryList.get(categoryIndex);
			categoryIndexList.add(Integer.toString(categoryDetail.getCategoryNo()));

			listContent.add(new WtmCategoryListItem(R.drawable.ic_action_database, categoryDetail.getCategoryName(), categoryDetail.getRoomCount()));
		}

		WtmCategoryListAdapter categoryListAdapter = new WtmCategoryListAdapter(this, R.layout.layout_list_style_1, listContent);
		try
		{
			ListView lv = (ListView) findViewById(R.id.listCategoryList);
			lv.setAdapter(categoryListAdapter);
			lv.setOnItemClickListener(this);
		}
		catch(Exception e)
		{
			Log.e("tag", e.toString());
		}

		return true;
	}

	// 카테고리 방 리스트 출력
	private boolean setCategoryRoomListInfo()
	{
		ArrayList<WtmRoomInfo> roomList = categoryRoomInfo.getRoomList();
		ArrayList<WtmRoomListItem> listContent = new ArrayList<WtmRoomListItem>();

		categoryRoomIndexList = new ArrayList<String>();

		for(int categoryRoomIndex = 0; categoryRoomIndex < roomList.size(); categoryRoomIndex++)
		{
			WtmRoomInfo roomInfo = roomList.get(categoryRoomIndex);
			categoryRoomIndexList.add(Integer.toString(roomInfo.getRoomNo()));

			listContent.add(new WtmRoomListItem(R.drawable.ic_action_home, roomInfo.getRoomTitle()));
		}

		View noRoom = findViewById(R.id.layoutCategoryRoomList_noRoom);
		View roomExists = findViewById(R.id.layoutCategoryRoomList_roomExists);

		if(roomList.size() < 1)
		{
			noRoom.setVisibility(View.VISIBLE);
			roomExists.setVisibility(View.GONE);
			setString(R.id.textCategoryRoomList_noRoom, String.format(getString(R.string.categoryRoomList_noRoom), selectCategoryListItem.getTitle()));
		}
		else
		{
			noRoom.setVisibility(View.GONE);
			roomExists.setVisibility(View.VISIBLE);
		}

		WtmRoomListAdapter roomListAdapter = new WtmRoomListAdapter(this, R.layout.layout_list_style_2, listContent);
		ListView list = (ListView) findViewById(R.id.listCategoryRoomList);
		list.setAdapter(roomListAdapter);
		list.setOnItemClickListener(this);

		return true;
	}

	// 방 정보
	private boolean setRoomInfo()
	{
		return setRoomInfo(true);
	}

	private boolean setRoomInfo(boolean isRefreshImg)
	{
		TextView textTitle = (TextView) findViewById(R.id.textRoomInfo_roomTitle);
		TextView textDesc = (TextView) findViewById(R.id.textRoomInfo_roomDesc);
		TextView textStartdate = (TextView) findViewById(R.id.textRoomInfo_roomStartdate);
		TextView textEnddate = (TextView) findViewById(R.id.textRoomInfo_roomEnddate);
		TextView textMemberCount = (TextView) findViewById(R.id.textRoomInfo_roomMemberCount);
		TextView textCheckinCount = (TextView) findViewById(R.id.textRoomInfo_roomCheckinCount);

		textTitle.setText(CurrentRoomInfo.getRoomTitle());
		textDesc.setText(CurrentRoomInfo.getRoomDesc());
		textStartdate.setText(WtmUtil.convDate(CurrentRoomInfo.getStartDate()));
		textEnddate.setText(WtmUtil.convDate(CurrentRoomInfo.getEndDate()));
		textMemberCount.setText(String.format(getString(R.string.userRoomInfo_MemberCount), CurrentRoomInfo.getJoinCnt()));
		textCheckinCount.setText(String.format(getString(R.string.userRoomInfoLabel_Checkin), CurrentRoomInfo.getCheckCnt()));

		// ------------------------------------------------
		// 방장 정보
		// ------------------------------------------------
		TextView textMasterName = (TextView) findViewById(R.id.textRoomInfo_roomMasterName);
		textMasterName.setText(roomMasterInfo.getUserName());

		ImageView imageMasterImage = (ImageView) findViewById(R.id.imageRoomInfo_roomMasterImage);
		imageMasterImage.setContentDescription(roomMasterInfo.getUserId());
		imageMasterImage.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View view, MotionEvent arg1)
			{
				ImageView img = (ImageView) view;
				WtmDataStore.drawableResourceImageView = img;
				WtmDataStore.drawableResource = img.getDrawable();
				showFacebookUserInfo((String) view.getContentDescription());
				return false;
			}
		});

		if(isRefreshImg)
		{
			int h = imageMasterImage.getHeight(), w = imageMasterImage.getHeight();
			imageMasterImage.setImageResource(R.drawable.ic_action_user);
			WtmUtil.getImageToUrl(imageMasterImage, WtmUtil.getFbProfileImageUrl(roomMasterInfo.getUserId()));
			// Bitmap bm = WtmUtil.DrawToBitmap(imageMasterImage.getDrawable());
			// imageMasterImage.setImageBitmap(WtmEffect.getRoundedCornerBitmap(bm,
			// 5));
		}
		// ------------------------------------------------

		// ------------------------------------------------
		// 카테고리 정보
		// ------------------------------------------------
		LinearLayout categoriesLayout = (LinearLayout) findViewById(R.id.layoutRoomInfo_roomCategories);
		categoriesLayout.removeAllViews();

		String[] categories = CurrentRoomInfo.getCategoryIds();
		Log.i("tag", categories.length + "");

		if(categories.length > 0)
			for(int cateIndex = 0; cateIndex < categories.length; cateIndex++)
			{
				int categoryNo = Integer.parseInt(categories[cateIndex]);
				TextView textCategoryName = new TextView(thisContext);
				String categoryName = WtmUtil.getCategoryName(categoryNo);
				textCategoryName.setText(categoryName);
				categoriesLayout.addView(textCategoryName);
				textCategoryName.setTextAppearance(this, R.style.TagCategroyName);
				textCategoryName.setBackgroundResource(R.drawable.tag_category);

				ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) textCategoryName.getLayoutParams();
				margin.setMargins(0, 0, WtmUtil.dpTopx(5, thisContext), 0);

			}

		// ------------------------------------------------
		// 버튼 분기처리
		// ------------------------------------------------
		Button buttonJoin = (Button) findViewById(R.id.buttonRoomInfo_Join);
		Button buttonOut = (Button) findViewById(R.id.buttonRoomInfo_Out);
		Button buttonCheckin = (Button) findViewById(R.id.buttonRoomInfo_Checkin);
		Button buttonModify = (Button) findViewById(R.id.buttonRoomInfo_Modify);
		Button buttonDelete = (Button) findViewById(R.id.buttonRoomInfo_Delete);

		if(isJoindRoom(this.selectedRoomNo))
		{
			buttonJoin.setVisibility(View.GONE);
			buttonOut.setVisibility(View.VISIBLE);
			buttonCheckin.setVisibility(View.VISIBLE);

			if(CurrentRoomInfo.getManagerId().equals(WtmDataStore.myInfo.getUserId()))
			{
				buttonModify.setVisibility(View.VISIBLE);
				buttonDelete.setVisibility(View.VISIBLE);
				buttonOut.setVisibility(View.GONE);
			}
			else
			{
				buttonModify.setVisibility(View.GONE);
				buttonDelete.setVisibility(View.GONE);
			}
		}
		else
		{
			buttonJoin.setVisibility(View.VISIBLE);
			buttonCheckin.setVisibility(View.GONE);
			buttonOut.setVisibility(View.GONE);
			buttonModify.setVisibility(View.GONE);
			buttonDelete.setVisibility(View.GONE);
		}
		// ------------------------------------------------

		// ------------------------------------------------
		// 체크인 한 사람 정보
		// ------------------------------------------------
		LinearLayout layoutCheckedList = (LinearLayout) findViewById(R.id.layoutRoomInfo_roomCheckInList);
		layoutCheckedList.removeAllViews();
		ArrayList<HashMap<String, String>> checkedUser = CurrentRoomInfo.getCheckedUsers();
		for(int userIndex = 0; userIndex < checkedUser.size(); userIndex++)
		{
			HashMap<String, String> checkUserInfo = checkedUser.get(userIndex);
			ImageView imgIcon = new ImageView(thisContext);
			imgIcon.setBackgroundResource(R.drawable.icon_round);
			imgIcon.setImageResource(R.drawable.fb_icon_default);
			imgIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imgIcon.setLayoutParams(new LayoutParams(WtmUtil.dpTopx(40, thisContext), WtmUtil.dpTopx(40, thisContext)));
			imgIcon.setContentDescription(checkUserInfo.get("user_id"));
			// WtmUtil.getImageToUrl(imgIcon, checkUserInfo.get("user_img"));
			WtmUtil.getImageToUrl(imgIcon, WtmUtil.getFbProfileImageUrl(checkUserInfo.get("user_id")));
			layoutCheckedList.addView(imgIcon);

			ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) imgIcon.getLayoutParams();
			margin.setMargins(0, 0, WtmUtil.dpTopx(5, thisContext), WtmUtil.dpTopx(5, thisContext));

			if(checkUserInfo.get("user_id").equals(WtmDataStore.myInfo.getUserId()))
				buttonCheckin.setVisibility(View.GONE);

			imgIcon.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View view, MotionEvent arg1)
				{
					ImageView img = (ImageView) view;
					WtmDataStore.drawableResourceImageView = img;
					WtmDataStore.drawableResource = img.getDrawable();
					showFacebookUserInfo((String) view.getContentDescription());
					return false;
				}
			});
		}
		// ------------------------------------------------

		return true;
	}

	private void setMyInfo()
	{
		TextView textUserInfolTitle = (TextView) findViewById(R.id.textUserInfo_Title);
		TextView textUserInfoUserName = (TextView) findViewById(R.id.textUserInfo_userName);
		TextView textUserInfoUserEmail = (TextView) findViewById(R.id.textUserInfo_userEmail);
		TextView textUserInfoUserProfile = (TextView) findViewById(R.id.textUserInfo_userProfile);
		TextView textUserInfoUserGroup = (TextView) findViewById(R.id.textUserInfo_userGroup);

		WtmUserInfo myInfo = WtmDataStore.myInfo;

		textUserInfolTitle.setText("내 정보");

		textUserInfoUserName.setText(myInfo.getUserName());
		textUserInfoUserEmail.setText(myInfo.getUserEmail());
		textUserInfoUserProfile.setText(myInfo.getUserProfile());
		textUserInfoUserGroup.setText(myInfo.getUserGruop().toString());

		ImageView fbIcon = (ImageView) findViewById(R.id.imageUserInfo_profileImage);
		// WtmUtil.getImageToUrl(fbIcon, myInfo.getUserProfile());
		WtmUtil.getImageToUrl(fbIcon, WtmUtil.getFbProfileImageUrl(myInfo.getUserId()));
	}

	public void setRoomModify()
	{
		TextView labelTitle = (TextView) findViewById(R.id.textRoomModifyLabel_Title);

		LinearLayout categoryGroup = (LinearLayout) findViewById(R.id.layoutRoomModifyLabel_categoryGroup);
		if(roomModifyMode == ROOM_MODIFY_MODE_ADD)
		{
			categoryGroup.setVisibility(View.GONE);
			labelTitle.setText("방 만들기");
		}
		else
		{
			categoryGroup.setVisibility(View.VISIBLE);
			labelTitle.setText("방 수정");
		}

		Calendar calendar = Calendar.getInstance();
		String today = String.format("%d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));

		EditText editTitle = (EditText) findViewById(R.id.editRoomModify_Title);
		EditText editDesc = (EditText) findViewById(R.id.editRoomModify_Desc);
		EditText editStartdate = (EditText) findViewById(R.id.editRoomModify_Startdate);
		EditText editEnddate = (EditText) findViewById(R.id.editRoomModify_Enddate);

		// 데이터 초기화
		editTitle.setText("");
		editDesc.setText("");
		editStartdate.setText(today);
		editEnddate.setText(today);

		// 방 수정인 경우 데이터 가져오기
		if(roomModifyMode == ROOM_MODIFY_MODE_MOD)
		{
			editTitle.setText(CurrentRoomInfo.getRoomTitle());
			editDesc.setText(CurrentRoomInfo.getRoomDesc());
			editStartdate.setText(WtmUtil.convDate(CurrentRoomInfo.getStartDate()));
			editEnddate.setText(WtmUtil.convDate(CurrentRoomInfo.getEndDate()));
		}

		final LinearLayout layoutCategories = (LinearLayout) findViewById(R.id.layoutRoomModify_Categories);
		layoutCategories.removeAllViews();
		layoutCategories.setOnTouchListener(this);

		final TextView textDefaultCategory = new TextView(thisContext);
		textDefaultCategory.setText("터치해서 카테고리 선택");
		layoutCategories.addView(textDefaultCategory);

		final ArrayList<String> categoryListIndex = new ArrayList<String>();
		final ArrayList<String> categoryListIndexName = new ArrayList<String>();
		final ArrayList<WtmCategoryDetail> categoryList = WtmDataStore.categoryInfo.getCategoryList();
		checkCate = new ArrayList<Integer>();
		selectedCate = new ArrayList<Integer>();

		if(roomModifyMode == ROOM_MODIFY_MODE_MOD)
		{
			String[] categories = CurrentRoomInfo.getCategoryIds();
			if(categories.length > 0)
				layoutCategories.removeAllViews();
			for(int ci = 0; ci < categories.length; ci++)
			{
				selectedCate.add(Integer.parseInt(categories[ci]));
				String cateName = WtmUtil.getCategoryName(categories[ci]);

				TextView textCategoryName = new TextView(thisContext);
				textCategoryName.setText(cateName);
				layoutCategories.addView(textCategoryName);
				textCategoryName.setTextAppearance(thisContext, R.style.TagCategroyName);
				textCategoryName.setBackgroundResource(R.drawable.tag_category);

				ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) textCategoryName.getLayoutParams();
				margin.setMargins(0, 0, WtmUtil.dpTopx(5, thisContext), 0);
			}
		}

		layoutCategories.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				// 취소할 때 복구할 임시 지정
				tempCate = (ArrayList<Integer>) checkCate.clone();
				Log.d("tag", "checkCate count: " + checkCate.size());
				Log.d("tag", "tempCate count: " + tempCate.size());

				// 카테고리 목록
				ArrayList<WtmCategoryListCheckItem> listContent = new ArrayList<WtmCategoryListCheckItem>();

				for(int cateIndex = 0; cateIndex < categoryList.size(); cateIndex++)
				{
					WtmCategoryDetail categoryDetail = categoryList.get(cateIndex);
					categoryListIndex.add(categoryDetail.getCategoryNo() + "");
					categoryListIndexName.add(categoryDetail.getCategoryName());
					boolean checked = checkCate.contains(cateIndex);

					if(selectedCate.contains(categoryDetail.getCategoryNo()))
					{
						checked = true;
						if(!checkCate.contains(cateIndex))
							checkCate.add(cateIndex);
					}

					listContent.add(new WtmCategoryListCheckItem(R.drawable.ic_action_database, categoryDetail.getCategoryName(), categoryDetail.getRoomCount(), checked));

				}

				WtmCategoryListCheckAdapter categoryListAdapter = new WtmCategoryListCheckAdapter(thisContext, R.layout.layout_list_style_3, listContent);

				ListView listCategory = new ListView(thisContext);
				listCategory.setAdapter(categoryListAdapter);
				listCategory.setOnItemClickListener(new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3)
					{
						CheckBox check = (CheckBox) view.findViewById(R.id.layout_list_style_1_check);

						if(check.isChecked())
						{
							if(checkCate.remove((Integer) position))
								check.setChecked(!check.isChecked());
							else
								check.setChecked(false);
						}
						else if(checkCate.size() >= 3)
							Toast.makeText(thisContext, "카테고리는 3개까지 선택이 가능합니다.", Toast.LENGTH_SHORT).show();
						else if(checkCate.add(position))
							check.setChecked(!check.isChecked());
					}
				});

				dialog("카테고리 선택", listCategory, new dialogButton().setCancelButton(getString(R.string.dialogButton_Cancel), new dialogButtonClickListener()
				{
					@Override
					public void onClick()
					{
						// 취소 선택 시 백업해 놓은 걸로 복구
						checkCate = (ArrayList<Integer>) tempCate.clone();
					}
				}).setConfirmButton(getString(R.string.dialogButton_Confirm), new dialogButtonClickListener()
				{
					@Override
					public void onClick()
					{
						layoutCategories.removeAllViews();
						selectedCate = new ArrayList<Integer>();

						if(checkCate.size() < 1)
							layoutCategories.addView(textDefaultCategory);
						else
							for(int i = 0; i < checkCate.size(); i++)
							{
								int catePosition = checkCate.get(i);
								String cateName = categoryListIndexName.get(catePosition);
								TextView textCategoryName = new TextView(thisContext);
								textCategoryName.setText(cateName);
								layoutCategories.addView(textCategoryName);
								textCategoryName.setTextAppearance(thisContext, R.style.TagCategroyName);
								textCategoryName.setBackgroundResource(R.drawable.tag_category);

								ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) textCategoryName.getLayoutParams();
								margin.setMargins(0, 0, WtmUtil.dpTopx(5, thisContext), 0);

								selectedCate.add(Integer.parseInt(categoryListIndex.get(catePosition)));
							}
					}
				}));

				return false;
			}
		});
	}

	public boolean checkRoomModify()
	{
		EditText editTitle = (EditText) findViewById(R.id.editRoomModify_Title);
		EditText editDesc = (EditText) findViewById(R.id.editRoomModify_Desc);
		EditText editStartdate = (EditText) findViewById(R.id.editRoomModify_Startdate);
		EditText editEnddate = (EditText) findViewById(R.id.editRoomModify_Enddate);

		if(roomModifyMode == ROOM_MODIFY_MODE_MOD && selectedCate.size() < 1)
		{
			Toast.makeText(thisContext, "카테고리를 선택해 주세요.", Toast.LENGTH_SHORT).show();
			return false;
		}

		if(editTitle.getText().length() < 1)
		{
			Toast.makeText(thisContext, "방 제목을 입력해 주세요.", Toast.LENGTH_SHORT).show();
			return false;
		}

		if(editDesc.getText().length() < 1)
		{
			Toast.makeText(thisContext, "방 설명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	// 공용 버튼 클릭 이벤트 처리
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.buttonUserRoomList_moveCategory:
			case R.id.buttonCategoryList:
				if(tabIndex != TAB_CATEGORY)
				{
					setViewIndex(INDEX_CATEGORY_LIST, ANIM_NEXT, false);
					viewIndexStack.clean();
					tabIndex = TAB_CATEGORY;
					setTabButtonStyle(tabIndex);
					ListView listCategory = (ListView) findViewById(R.id.listCategoryList);
					listCategory.setSelection(listCategory.getFirstVisiblePosition());
					listCategory.setScrollX(0);
				}
				else if(getViewIndex() != INDEX_CATEGORY_LIST)
				{
					setViewIndex(INDEX_CATEGORY_LIST, ANIM_PREV, false);
					viewIndexStack.clean();
					tabIndex = TAB_CATEGORY;
					setTabButtonStyle(tabIndex);
					ListView listCategory = (ListView) findViewById(R.id.listCategoryList);
					listCategory.setSelection(listCategory.getFirstVisiblePosition());
					listCategory.setScrollX(0);
				}
				break;

			case R.id.buttonMyRoom:
				if(tabIndex != TAB_MYROOM)
				{
					setViewIndex(INDEX_MYROOM_LIST, ANIM_PREV, false);
					viewIndexStack.clean();
					tabIndex = TAB_MYROOM;
					setTabButtonStyle(tabIndex);
					ListView listMyRoom = (ListView) findViewById(R.id.listUserRoomList);
					listMyRoom.setSelection(listMyRoom.getFirstVisiblePosition());
					listMyRoom.setScrollX(0);
				}
				else if(getViewIndex() != INDEX_MYROOM_LIST)
				{
					setViewIndex(INDEX_MYROOM_LIST, ANIM_PREV, false);
					viewIndexStack.clean();
					tabIndex = TAB_MYROOM;
					setTabButtonStyle(tabIndex);
					ListView listMyRoom = (ListView) findViewById(R.id.listUserRoomList);
					listMyRoom.setSelection(listMyRoom.getFirstVisiblePosition());
					listMyRoom.setScrollX(0);
				}
				break;

			case R.id.listCategoryList:
				break;

			// ------------------------------------------------
			case R.id.buttonRoomInfo_Join: // 가입
				dialog(getString(R.string.dialogTitle_Confirm), "해당 방에 가입하시겠습니까?", new dialogButton().setPositiveButton(getString(R.string.dialogButton_Yes), new dialogButtonClickListener()
				{
					@Override
					public void onClick()
					{
						WtmHttpCallHandler handlerRoomJoin = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/user_room", WtmHttpCallHandler.METHOD_POST)
						{
							@Override
							public void onCompleted(String responseContents, int responseStatus)
							{
								Log.d("tag", responseContents);

								if(responseStatus == 200 && !WtmUtil.isError(responseContents))
								{
									refreshRoomInfo(selectedRoomNo, false);
									refreshJoinedRoomInfo();
									Toast.makeText(thisContext, getString(R.string.userRoomInfoJoinMsg_Success), Toast.LENGTH_SHORT).show();
								}
								else
									Toast.makeText(thisContext, getString(R.string.error_type_1), Toast.LENGTH_SHORT).show();
							}
						};
						handlerRoomJoin.addParam("room_no", selectedRoomNo);
						WtmHttpCall httpRoomJoin = new WtmHttpCall()
						{
							@Override
							public void start()
							{
								super.start();
							}
						};
						httpRoomJoin.addHandler(handlerRoomJoin);
						httpRoomJoin.execute();
					}
				}).setNegativeButton(getString(R.string.dialogButton_No), new dialogButtonClickListener()
				{
					@Override
					public void onClick()
					{
					};
				}));

				break;
			case R.id.buttonRoomInfo_Checkin: // 체크인
				WtmHttpCallHandler handlerRoomCheckin = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/user_room", WtmHttpCallHandler.METHOD_PUT)
				{
					@Override
					public void onCompleted(String responseContents, int responseStatus)
					{
						if(responseStatus == 200 && !WtmUtil.isError(responseContents))
						{
							refreshRoomInfo(selectedRoomNo, false);
							Toast.makeText(thisContext, getString(R.string.userRoomInfoCheckMsg_Success), Toast.LENGTH_SHORT).show();
						}
						else
							Toast.makeText(thisContext, getString(R.string.error_type_1), Toast.LENGTH_SHORT).show();
					}
				};
				handlerRoomCheckin.addParam("room_no", this.selectedRoomNo);
				handlerRoomCheckin.isGetParam(true);
				WtmHttpCall httpRoomCheckin = new WtmHttpCall()
				{
					@Override
					public void start()
					{
						super.start();
					}
				};
				httpRoomCheckin.addHandler(handlerRoomCheckin);
				httpRoomCheckin.execute();
				break;
			case R.id.buttonRoomInfo_Out: // 방 탈퇴
				dialog(getString(R.string.dialogTitle_Confirm), "이 방을 탈퇴하시겠습니까?", new dialogButton().setPositiveButton(getString(R.string.dialogButton_Yes), new dialogButtonClickListener()
				{
					@Override
					public void onClick()
					{
						WtmHttpCallHandler handlerRoomOut = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/user_room", WtmHttpCallHandler.METHOD_DELETE)
						{
							@Override
							public void onCompleted(String responseContents, int responseStatus)
							{
								if(responseStatus == 200 && !WtmUtil.isError(responseContents))
								{
									refreshRoomInfo(selectedRoomNo, false);
									refreshJoinedRoomInfo();
									Toast.makeText(thisContext, getString(R.string.userRoomInfoOutMsg_Success), Toast.LENGTH_SHORT).show();
								}
								else
									Toast.makeText(thisContext, getString(R.string.error_type_1), Toast.LENGTH_SHORT).show();
							}
						};
						handlerRoomOut.addParam("room_no", selectedRoomNo);
						handlerRoomOut.isGetParam(true);
						WtmHttpCall httpRoomOut = new WtmHttpCall()
						{
							@Override
							public void start()
							{
								super.start();
							}
						};
						httpRoomOut.addHandler(handlerRoomOut);
						httpRoomOut.execute();
					}
				}).setNegativeButton(getString(R.string.dialogButton_No), new dialogButtonClickListener()
				{
					@Override
					public void onClick()
					{
					};
				}));
				break;
			case R.id.buttonRoomInfo_Modify: // 방 수정
				roomModifyMode = ROOM_MODIFY_MODE_MOD;
				Log.d("tag", "setRoomModify();");
				setRoomModify();
				setViewIndex(INDEX_ROOM_MODIFY, ANIM_NEXT);
				break;
			case R.id.buttonRoomInfo_Delete: // 방 삭제
				dialog(getString(R.string.dialogTitle_Confirm), "이 방을 삭제하시겠습니까?", new dialogButton().setPositiveButton(getString(R.string.dialogButton_Yes), new dialogButtonClickListener()
				{
					@Override
					public void onClick()
					{
						WtmHttpCallHandler handlerRoomDelete = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/room", WtmHttpCallHandler.METHOD_DELETE)
						{
							@Override
							public void onCompleted(String responseContents, int responseStatus)
							{
								if(responseStatus == 200 && !WtmUtil.isError(responseContents))
								{
									refreshJoinedRoomInfo(true);
									refreshCategoryInfo();
									Toast.makeText(thisContext, getString(R.string.userRoomInfoDeleteMsg_Success), Toast.LENGTH_SHORT).show();
								}
								else
									Toast.makeText(thisContext, getString(R.string.error_type_1), Toast.LENGTH_SHORT).show();
							}
						};
						handlerRoomDelete.addParam("room_no", selectedRoomNo);
						handlerRoomDelete.isGetParam(true);
						WtmHttpCall httpRoomDelete = new WtmHttpCall()
						{
							@Override
							public void start()
							{
								super.start();
							}
						};
						httpRoomDelete.addHandler(handlerRoomDelete);
						httpRoomDelete.execute();
					}
				}).setNegativeButton(getString(R.string.dialogButton_No), new dialogButtonClickListener()
				{
					@Override
					public void onClick()
					{
					};
				}));
				break;
			// ------------------------------------------------

			// 카테고리 룸 - 방 만들기
			case R.id.buttonCategoryRoomList_createRoom:
			case R.id.buttonUserRoomList_createRoom:
				roomModifyMode = ROOM_MODIFY_MODE_ADD;
				setRoomModify();
				setViewIndex(INDEX_ROOM_MODIFY, ANIM_NEXT);
				break;
			// ------------------------------------------------
			// 방 수정
			// ------------------------------------------------
			case R.id.buttonRoomModify_Modify:
				if(checkRoomModify())
				{
					EditText editTitle = (EditText) findViewById(R.id.editRoomModify_Title);
					EditText editDesc = (EditText) findViewById(R.id.editRoomModify_Desc);
					EditText editStartdate = (EditText) findViewById(R.id.editRoomModify_Startdate);
					EditText editEnddate = (EditText) findViewById(R.id.editRoomModify_Enddate);

					String url = "https://wtm.azure-mobile.net/api/room";
					final String strRoomMode;
					int roomMode;

					if(this.roomModifyMode == ROOM_MODIFY_MODE_ADD)
					{
						strRoomMode = "추가";
						roomMode = WtmHttpCallHandler.METHOD_POST;
					}
					else
					{
						strRoomMode = "수정";
						roomMode = WtmHttpCallHandler.METHOD_PUT;
						url += "?room_no=" + selectedRoomNo;
					}

					// 방 만들기
					WtmHttpCallHandler handlerRoomModify = new WtmHttpCallHandler(url, roomMode)
					{
						@Override
						public void onCompleted(String responseContents, int responseStatus)
						{
							Log.d("tag", responseContents);

							if(responseStatus == 200 && !WtmUtil.isError(responseContents))
							{
								Toast.makeText(thisContext, String.format("방 %s 성공", strRoomMode), Toast.LENGTH_SHORT).show();
								refreshJoinedRoomInfo(true);
							}
							else
							{
								Toast.makeText(thisContext, String.format("방 %s 실패", strRoomMode), Toast.LENGTH_SHORT).show();
								Log.e("tag", responseContents);
							}
						}
					};
					handlerRoomModify.addParam("room_title", editTitle.getText().toString());
					handlerRoomModify.addParam("room_desc", editDesc.getText().toString());
					handlerRoomModify.addParam("start_date", editStartdate.getText().toString().replace("-", ""));
					handlerRoomModify.addParam("end_date", editEnddate.getText().toString().replace("-", ""));

					WtmHttpCall httpRoomModifyCall = new WtmHttpCall()
					{
						@Override
						public void onComplete()
						{
							if(roomModifyMode == ROOM_MODIFY_MODE_MOD)
								refreshCategoryInfo();
						}

						@Override
						public void start()
						{
							super.start();
						}
					};

					httpRoomModifyCall.addHandler(handlerRoomModify);

					if(this.roomModifyMode == ROOM_MODIFY_MODE_MOD)
					{
						Log.i("tag", "length: " + CurrentRoomInfo.getCategoryIds().length);
						String categoryUrl;
						categoryUrl = "https://wtm.azure-mobile.net/api/category_room?room_no=" + selectedRoomNo;

						if(CurrentRoomInfo.getCategoryIds().length > 0)
						{
							categoryUrl = "https://wtm.azure-mobile.net/api/category_room?room_no=" + selectedRoomNo;
							roomMode = WtmHttpCallHandler.METHOD_PUT;
						}
						else
						{
							categoryUrl = "https://wtm.azure-mobile.net/api/category_room";
							roomMode = WtmHttpCallHandler.METHOD_POST;
						}

						WtmHttpCallHandler handlerRoomCategory = new WtmHttpCallHandler(categoryUrl, roomMode)
						{
							@Override
							public void onCompleted(String responseContents, int responseStatus)
							{
								if(responseStatus != 200 || WtmUtil.isError(responseContents))
								{
									dialog(getString(R.string.dialogTitle_Error), "카테고리 수정 실패\n\n" + responseContents, new dialogButton().setCloseButton(getString(R.string.dialogButton_Confirm), new dialogButtonClickListener()));
									Log.e("tag", responseContents);
								}
							}
						};

						handlerRoomCategory.addParam("room_no", selectedRoomNo);

						for(int selectCateIndex = 0; selectCateIndex < selectedCate.size(); selectCateIndex++)
						{
							if(selectCateIndex >= 3)
								break;
							handlerRoomCategory.addParam("category_no_" + (selectCateIndex + 1), selectedCate.get(selectCateIndex));
							Log.d("tag", "category_no_" + (selectCateIndex + 1) + ": " + selectedCate.get(selectCateIndex));
						}

						httpRoomModifyCall.addHandler(handlerRoomCategory);
					}

					httpRoomModifyCall.execute();
				}
				break;
			case R.id.buttonRoomModify_Cancel:
				Object popObj = viewIndexStack.pop();
				if(popObj != null)
					setViewIndex(Integer.parseInt(popObj.toString()), ANIM_PREV, false);
				break;
			// ------------------------------------------------

			case R.id.editRoomModify_Startdate: // 시작일 클릭
				final EditText editStartdate = (EditText) findViewById(R.id.editRoomModify_Startdate);
				String getStartDate = editStartdate.getText().toString().replace("-", "");

				final DatePicker dpStartdate = new DatePicker(thisContext);
				dpStartdate.setCalendarViewShown(false);
				dpStartdate.init(WtmUtil.getDateYear(getStartDate), WtmUtil.getDateMonth(getStartDate) - 1, WtmUtil.getDateDay(getStartDate), new OnDateChangedListener()
				{
					@Override
					public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
					{
					}
				});
				dialog("시작일", dpStartdate, new dialogButton().setConfirmButton("확인", new dialogButtonClickListener()
				{
					@Override
					public void onClick()
					{
						editStartdate.setText(String.format("%04d-%02d-%02d", dpStartdate.getYear(), dpStartdate.getMonth() + 1, dpStartdate.getDayOfMonth()));
					}
				}).setCancelButton(getString(R.string.dialogButton_Cancel), new dialogButtonClickListener()));
				break;
			case R.id.editRoomModify_Enddate: // 종료일 클릭
				final EditText editEnddate = (EditText) findViewById(R.id.editRoomModify_Enddate);
				String getEndDate = editEnddate.getText().toString().replace("-", "");

				final DatePicker dpEnddate = new DatePicker(thisContext);
				dpEnddate.setCalendarViewShown(false);
				dpEnddate.init(WtmUtil.getDateYear(getEndDate), WtmUtil.getDateMonth(getEndDate) - 1, WtmUtil.getDateDay(getEndDate), new OnDateChangedListener()
				{
					@Override
					public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
					{
					}
				});
				dialog("종료일", dpEnddate, new dialogButton().setConfirmButton("확인", new dialogButtonClickListener()
				{
					@Override
					public void onClick()
					{
						editEnddate.setText(String.format("%04d-%02d-%02d", dpEnddate.getYear(), dpEnddate.getMonth() + 1, dpEnddate.getDayOfMonth()));
					}
				}).setCancelButton(getString(R.string.dialogButton_Cancel), new dialogButtonClickListener()));
				break;
		}
	}

	// ------------------------------------------------
	// 리스트 클릭
	// ------------------------------------------------
	@Override
	public void onItemClick(AdapterView<?> avd, View v, final int position, long id)
	{
		switch(avd.getId())
		{
		// ------------------------------------------------
		// 마이룸 클릭
		// ------------------------------------------------
			case R.id.listUserRoomList:
				String selectRoomNo = roomIndexList.get(position);
				selectedRoomNo = Integer.parseInt(selectRoomNo);

				WtmHttpCallHandler handlerMyRoomInfo = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/room", WtmHttpCallHandler.METHOD_GET)
				{
					@Override
					public void onCompleted(String responseContents, int responseStatus)
					{
						Log.d("tag", responseContents);

						if(responseStatus == 200)
						{
							CurrentRoomInfo = new WtmRoomInfo(responseContents);

							// 방장 유저정보 받아오기
							WtmHttpCallHandler handlerRoomMasterInfo = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/user", WtmHttpCallHandler.METHOD_GET)
							{
								@Override
								public void onCompleted(String responseContents, int responseStatus)
								{
									Log.d("tag", responseContents);

									if(responseStatus == 200)
										roomMasterInfo = new WtmUserInfo(responseContents);
								}
							};
							handlerRoomMasterInfo.addParam("user_id", CurrentRoomInfo.getManagerId());
							WtmHttpCall httpRoomMasterInfoCall = new WtmHttpCall()
							{
								@Override
								public void onComplete()
								{
									setRoomInfo();

									Handler hdl = new Handler()
									{
										@Override
										public void handleMessage(Message msg)
										{
											setViewIndex(INDEX_ROOM_INFO, ANIM_NEXT);
										}
									};
									hdl.sendEmptyMessageDelayed(0, 10);
								}

								@Override
								public void start()
								{
									super.start();
								}
							};
							httpRoomMasterInfoCall.addHandler(handlerRoomMasterInfo);
							httpRoomMasterInfoCall.execute();
						}
					}
				};
				handlerMyRoomInfo.addParam("room_no", selectRoomNo);
				WtmHttpCall httpMyRoomInfoCall = new WtmHttpCall()
				{
					@Override
					public void onComplete()
					{
					}

					@Override
					public void start()
					{
						super.start();
					}
				};
				httpMyRoomInfoCall.addHandler(handlerMyRoomInfo);
				httpMyRoomInfoCall.execute();
				break;
			// ------------------------------------------------

			// ------------------------------------------------
			// 카테고리 클릭
			// ------------------------------------------------
			case R.id.listCategoryList:
				String selectCategoryNo = categoryIndexList.get(position);

				WtmHttpCallHandler handlerCategoryRoom = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/category_room", WtmHttpCallHandler.METHOD_GET)
				{
					@Override
					public void onCompleted(String responseContents, int responseStatus)
					{
						Log.d("tag", responseContents);

						if(responseStatus == 200)
							categoryRoomInfo = new WtmCategoryRoomInfo(responseContents);
					}
				};
				handlerCategoryRoom.addParam("category_no", selectCategoryNo);
				WtmHttpCall httpCall = new WtmHttpCall()
				{
					@Override
					public void onComplete()
					{
						// 선택된 카테고리
						ListView lv = (ListView) findViewById(R.id.listCategoryList);
						TextView textLabel = (TextView) findViewById(R.id.textCategoryRoomListLabel);

						selectCategoryListItem = (WtmCategoryListItem) lv.getItemAtPosition(position);
						textLabel.setText(selectCategoryListItem.getTitle());

						setCategoryRoomListInfo();

						Handler hdl = new Handler()
						{
							@Override
							public void handleMessage(Message msg)
							{
								setViewIndex(INDEX_CATEGORY_ROOM_LIST, ANIM_NEXT);
							}
						};
						hdl.sendEmptyMessageDelayed(0, 10);
					}

					@Override
					public void start()
					{
						super.start();
					}
				};
				httpCall.addHandler(handlerCategoryRoom);
				httpCall.execute();
				break;
			// ------------------------------------------------

			// ------------------------------------------------
			// 카테고리 방 클릭
			// ------------------------------------------------
			case R.id.listCategoryRoomList:
				String selectCategoryRoomNo = categoryRoomIndexList.get(position);
				selectedRoomNo = Integer.parseInt(selectCategoryRoomNo);

				WtmHttpCallHandler handlerCategoryRoomInfo = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/room", WtmHttpCallHandler.METHOD_GET)
				{
					@Override
					public void onCompleted(String responseContents, int responseStatus)
					{
						Log.d("tag", responseContents);

						if(responseStatus == 200)
						{
							CurrentRoomInfo = new WtmRoomInfo(responseContents);

							// 방장 유저정보 받아오기
							WtmHttpCallHandler handlerCategoryRoomMasterInfo = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/user", WtmHttpCallHandler.METHOD_GET)
							{
								@Override
								public void onCompleted(String responseContents, int responseStatus)
								{
									Log.d("tag", responseContents);

									if(responseStatus == 200)
										roomMasterInfo = new WtmUserInfo(responseContents);
								}
							};
							handlerCategoryRoomMasterInfo.addParam("user_id", CurrentRoomInfo.getManagerId());
							WtmHttpCall httpCategoryRoomMasterInfoCall = new WtmHttpCall()
							{
								@Override
								public void onComplete()
								{
									setRoomInfo();

									Handler hdl = new Handler()
									{
										@Override
										public void handleMessage(Message msg)
										{
											setViewIndex(INDEX_ROOM_INFO, ANIM_NEXT);
										}
									};
									hdl.sendEmptyMessageDelayed(0, 10);
								}

								@Override
								public void start()
								{
									super.start();
								}
							};
							httpCategoryRoomMasterInfoCall.addHandler(handlerCategoryRoomMasterInfo);
							httpCategoryRoomMasterInfoCall.execute();
						}
					}
				};
				handlerCategoryRoomInfo.addParam("room_no", selectCategoryRoomNo);
				WtmHttpCall httpCategoryRoomInfoCall = new WtmHttpCall()
				{
					@Override
					public void onComplete()
					{
					}

					@Override
					public void start()
					{
						super.start();
					}
				};
				httpCategoryRoomInfoCall.addHandler(handlerCategoryRoomInfo);
				httpCategoryRoomInfoCall.execute();
				break;
		// ------------------------------------------------
		}
	}

	// ------------------------------------------------
	// 객체 터치
	// ------------------------------------------------
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		switch(event.getAction())
		{
			case R.id.editRoomModify_Startdate:
				EditText edit = (EditText) v;
				Toast.makeText(thisContext, edit.getText().toString(), Toast.LENGTH_SHORT).show();
				break;
			case MotionEvent.ACTION_MOVE:
				event.setLocation(positionX, event.getY());
				break;

			case MotionEvent.ACTION_DOWN:
				positionX = event.getX();
				break;

			case MotionEvent.ACTION_UP:
				float currentPointerX = event.getX();

				if(WtmUtil.pxTodp((int) currentPointerX - (int) positionX, this) > 70)
				{
					vf.setInAnimation(WtmAnimation.leftIn);
					vf.setOutAnimation(WtmAnimation.rightOut);
					vf.showPrevious();
				}
				else if(WtmUtil.pxTodp((int) positionX - (int) currentPointerX, this) > 70)
				{
					vf.setInAnimation(WtmAnimation.rightIn);
					vf.setOutAnimation(WtmAnimation.leftOut);
					vf.showNext();
				}

				event.setLocation(currentPointerX, event.getY());
				break;
		}

		return true;
	}

	// ------------------------------------------------
	// 뒤로가기 버튼
	// ------------------------------------------------
	@Override
	public void onBackPressed()
	{
		Object popObj = viewIndexStack.pop();

		if(popObj == null)
			super.onBackPressed();
		else
			setViewIndex(Integer.parseInt(popObj.toString()), ANIM_PREV, false);
	}

	// ------------------------------------------------
	// 메뉴 생성
	// ------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_home, menu);
		return true;
	}

	// ------------------------------------------------
	// 메뉴버튼 선택 시 호출
	// ------------------------------------------------
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// 뷰에 따른 메뉴 분기
		MenuItem actionCreateRoom = menu.findItem(R.id.menu_createRoom);
		actionCreateRoom.setVisible(getViewIndex() == INDEX_CATEGORY_ROOM_LIST || getViewIndex() == INDEX_MYROOM_LIST);

		return true;
	}

	private void refreshJoinedRoomInfo()
	{
		refreshJoinedRoomInfo(false);
	}

	// 가입된 방 정보 조회
	private void refreshJoinedRoomInfo(final boolean isMove)
	{
		WtmHttpCallHandler roomListHandler = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/user_room", WtmHttpCallHandler.METHOD_GET)
		{
			@Override
			public void onCompleted(String responseContents, int responseStatus)
			{
				if(responseStatus == 200 && !WtmUtil.isError(responseContents))
				{
					WtmDataStore.userRoomInfo = new WtmUserRoomInfo(responseContents);
					setUserRoomInfo();

					if(isMove)
					{
						Button btnMyRoom = (Button) findViewById(R.id.buttonMyRoom);
						btnMyRoom.performClick();
					}
				}
				else
					Toast.makeText(thisContext, getString(R.string.error_type_1), Toast.LENGTH_SHORT).show();
			}
		};
		WtmHttpCall httpCallJoinedRoom = new WtmHttpCall()
		{
			@Override
			public void onComplete()
			{
			}

			@Override
			public void start()
			{
				super.start();
			}
		};
		httpCallJoinedRoom.addHandler(roomListHandler);
		httpCallJoinedRoom.execute();
	}

	// 카테고리 정보 조회
	private void refreshCategoryInfo()
	{
		WtmHttpCallHandler categoryInfoHandler = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/category", WtmHttpCallHandler.METHOD_GET)
		{
			@Override
			public void onCompleted(String responseContents, int responseStatus)
			{
				if(responseStatus == 200)
					WtmDataStore.categoryInfo = new WtmCategoryInfo(responseContents);
			}
		};
		WtmHttpCall httpCall = new WtmHttpCall()
		{
			@Override
			public void onComplete()
			{
				setCategoryInfo();
			}

			@Override
			public void start()
			{
				super.start();
			}
		};
		httpCall.addHandler(categoryInfoHandler);
		httpCall.execute();
	}

	private void refreshRoomInfo(int roomNo)
	{
		refreshRoomInfo(roomNo, true);
	}

	private void refreshRoomInfo(int roomNo, final boolean isRefreshImg)
	{
		WtmHttpCallHandler handlerMyRoomInfo = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/room", WtmHttpCallHandler.METHOD_GET)
		{
			@Override
			public void onCompleted(String responseContents, int responseStatus)
			{
				if(responseStatus == 200)
				{
					CurrentRoomInfo = new WtmRoomInfo(responseContents);

					// 방장 유저정보 받아오기
					WtmHttpCallHandler handlerRoomMasterInfo = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/user", WtmHttpCallHandler.METHOD_GET)
					{
						@Override
						public void onCompleted(String responseContents, int responseStatus)
						{
							Log.d("tag", responseContents);

							if(responseStatus == 200)
								roomMasterInfo = new WtmUserInfo(responseContents);
						}
					};
					handlerRoomMasterInfo.addParam("user_id", CurrentRoomInfo.getManagerId());
					WtmHttpCall httpRoomMasterInfoCall = new WtmHttpCall()
					{
						@Override
						public void onComplete()
						{
							setRoomInfo(isRefreshImg);
						}

						@Override
						public void start()
						{
							super.start();
						}
					};
					httpRoomMasterInfoCall.addHandler(handlerRoomMasterInfo);
					httpRoomMasterInfoCall.execute();
				}
			}
		};
		handlerMyRoomInfo.addParam("room_no", roomNo);
		WtmHttpCall httpMyRoomInfoCall = new WtmHttpCall()
		{
			@Override
			public void onComplete()
			{
			}

			@Override
			public void start()
			{
				super.start();
			}
		};
		httpMyRoomInfoCall.addHandler(handlerMyRoomInfo);
		httpMyRoomInfoCall.execute();
	}

	// ------------------------------------------------
	// 메뉴 선택
	// ------------------------------------------------
	@SuppressLint("NewApi")
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		super.onMenuItemSelected(featureId, item);

		switch(item.getItemId())
		{
		// 내 정보 설정
			case R.id.menu_userInfo:
				if(getViewIndex() != INDEX_MY_INFO)
					setViewIndex(INDEX_MY_INFO, ANIM_NEXT);
				break;

			// 방 만들기
			case R.id.menu_createRoom:
				roomModifyMode = ROOM_MODIFY_MODE_ADD;
				setRoomModify();
				setViewIndex(INDEX_ROOM_MODIFY, ANIM_NEXT);
				break;

			case R.id.menu_test1:
				WtmHttpCallHandler handlerUserDelete = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/user", WtmHttpCallHandler.METHOD_DELETE)
				{
					@Override
					public void onCompleted(String responseContents, int responseStatus)
					{
						Log.d("tag", responseContents);

						if(responseStatus == 200)
							Toast.makeText(thisContext, "완료", Toast.LENGTH_SHORT).show();
					}
				};
				WtmHttpCall httpUserDeleteCall = new WtmHttpCall()
				{
					@Override
					public void onComplete()
					{
					}

					@Override
					public void start()
					{
						super.start();
					}
				};
				httpUserDeleteCall.addHandler(handlerUserDelete);
				httpUserDeleteCall.execute();
				break;
		}

		return true;
	}

	// ------------------------------------------------

	public boolean isJoindRoom(int roomNo)
	{
		ArrayList<WtmUserRoomDetail> roomList = WtmDataStore.userRoomInfo.getRoomList();
		for(int roomIndex = 0; roomIndex < roomList.size(); roomIndex++)
		{
			WtmUserRoomDetail roomInfo = roomList.get(roomIndex);

			if(roomInfo.getRoomNo() == roomNo)
				return true;
		}

		return false;
	}

	private void setTabButtonStyle(int selectedTab)
	{
		Button tabMyRoom = (Button) findViewById(R.id.buttonMyRoom);
		View tabBarMyroom = findViewById(R.id.tabBarMyRoom);
		Button tabCategoryList = (Button) findViewById(R.id.buttonCategoryList);
		View tabBarCategoryList = findViewById(R.id.tabBarCategoryList);

		if(selectedTab == TAB_MYROOM)
		{
			tabBarMyroom.setBackgroundColor(Color.parseColor("#0099cc"));
			tabBarCategoryList.setBackgroundColor(Color.parseColor("#9ae2fc"));

			tabMyRoom.setTextColor(Color.parseColor("#0099cc"));
			tabCategoryList.setTextColor(Color.parseColor("#9ae2fc"));
		}
		else
		{
			tabBarMyroom.setBackgroundColor(Color.parseColor("#9ae2fc"));
			tabBarCategoryList.setBackgroundColor(Color.parseColor("#0099cc"));

			tabMyRoom.setTextColor(Color.parseColor("#9ae2fc"));
			tabCategoryList.setTextColor(Color.parseColor("#0099cc"));
		}
	}

	// 이미지 클릭 시 유저정보 가져오기
	private void showFacebookUserInfo(String fbId)
	{
		WtmHttpCallHandler handlerUserDelete = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/user", WtmHttpCallHandler.METHOD_GET)
		{
			@Override
			public void onCompleted(String responseContents, int responseStatus)
			{
				Log.d("tag", responseContents);

				if(responseStatus == 200)
				{
					WtmUserInfo getUserInfo = new WtmUserInfo(responseContents);
					WtmDataStore.userInfo = getUserInfo;

					Intent intentUserInfo = new Intent(thisContext, WtmActivityUserInfo.class);
					intentUserInfo.putExtra("FB_ID", getUserInfo.getUserId());
					startActivity(intentUserInfo);
				}
			}
		};
		WtmHttpCall httpUserDeleteCall = new WtmHttpCall()
		{
			@Override
			public void onComplete()
			{
			}

			@Override
			public void start()
			{
				super.start();
			}
		};
		handlerUserDelete.addParam("user_id", fbId);
		httpUserDeleteCall.addHandler(handlerUserDelete);
		httpUserDeleteCall.execute();
		// WtmDataStore.userInfo;
	}
}
