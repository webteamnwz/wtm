package kr.po1.webtechmobile.Activity;

import kr.po1.webtechmobile.R;
import kr.po1.webtechmobile.WtmDataStore;
import kr.po1.webtechmobile.WtmUtil;
import android.app.Activity;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WtmActivityUserInfo extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);

		String fbId = (String) getIntent().getExtras().get("FB_ID");

		TextView textUserName = (TextView) findViewById(R.id.textUserInfo_userName);
		textUserName.setText(WtmDataStore.userInfo.getUserName());

		TextView textUserProfile = (TextView) findViewById(R.id.activityUserInfo_textUserProfile);
		textUserProfile.setText(WtmDataStore.userInfo.getUserProfile());

		TextView textUserEmail = (TextView) findViewById(R.id.activityUserInfo_textUserEmail);
		textUserEmail.setText(WtmDataStore.userInfo.getUserEmail());

		TextView textUserGroup = (TextView) findViewById(R.id.activityUserInfo_textUserGroup);
		String[] getUserGroup = WtmDataStore.userInfo.getUserGruop();
		String userGroup = "";
		// userGroup += getUserGroup[gi] + (gi + 1 == getUserGroup.length ? "" :
		// ", ");
		for(int gi = 0; gi < getUserGroup.length; gi++)
			userGroup += getUserGroup[gi];
		textUserGroup.setText(userGroup);

		ImageView imageBackgroundIcon = (ImageView) findViewById(R.id.imageUserInfo_backgroundIcon);
		// imageBackgroundIcon.setImageDrawable(WtmDataStore.drawableResource);
		// imageBackgroundIcon.setImageDrawable(WtmDataStore.drawableResourceImageView.getDrawable());
		WtmUtil.getImageToUrl(imageBackgroundIcon, WtmUtil.getFbProfileImageUrl(fbId));

		ImageView imageMyEdit = (ImageView) findViewById(R.id.activityUserInfo_imageMyEdit);
		if(WtmUtil.convFbId(fbId).equals(WtmUtil.convFbId(WtmDataStore.myInfo.getUserId())))
		{
			Log.i("tag", "myEdit");
			imageMyEdit.setVisibility(View.VISIBLE);
		}
		else
			imageMyEdit.setVisibility(View.GONE);

		LinearLayout boxLayout = (LinearLayout) findViewById(R.id.imageUserInfo_boxLayout);
		boxLayout.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return true;
			}
		});

		LinearLayout bgLayout = (LinearLayout) findViewById(R.id.imageUserInfo_backgroundLayout);
		bgLayout.setBackgroundResource(R.drawable.background_transition);
		TransitionDrawable background = (TransitionDrawable) bgLayout.getBackground();
		background.startTransition(300);

		bgLayout.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				finish();
				return false;
			}
		});
	}
}
