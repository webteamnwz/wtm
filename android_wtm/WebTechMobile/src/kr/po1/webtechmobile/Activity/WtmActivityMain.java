package kr.po1.webtechmobile.Activity;

import kr.po1.webtechmobile.R;
import kr.po1.webtechmobile.WtmDataStore;
import kr.po1.webtechmobile.WtmUserAuth;
import kr.po1.webtechmobile.WtmUtil;
import kr.po1.webtechmobile.dialogButton;
import kr.po1.webtechmobile.dialogButtonClickListener;
import kr.po1.webtechmobile.DataSet.WtmCategoryInfo;
import kr.po1.webtechmobile.DataSet.WtmUserInfo;
import kr.po1.webtechmobile.DataSet.WtmUserRoomInfo;
import kr.po1.webtechmobile.Http.WtmHttpCall;
import kr.po1.webtechmobile.Http.WtmHttpCallHandler;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("ShowToast")
public class WtmActivityMain extends WtmActivity implements OnClickListener
{
	private ProgressBar mProgressBar;
	Context thisContext = this;
	TextView txt;
	boolean isError = false;

	// 로그인 완료 브로드캐스트 리시버
	private BroadcastReceiver authBR = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String authKey = intent.getExtras().getString("AUTH_TOKEN");
			WtmDataStore.authKey = authKey;

			String action = intent.getAction();
			// txt.setText(getString(R.string.auth_complete));

			Button btnLoginCancel = (Button) findViewById(R.id.buttonFacebookLoginCancel);
			btnLoginCancel.setVisibility(View.GONE);

			LinearLayout testLayout = (LinearLayout) findViewById(R.id.layoutTest);
			testLayout.setVisibility(View.VISIBLE);

			authDone();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// getCacheDir().listFiles()
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txt = (TextView) findViewById(R.id.textStatus);

		// 인증 완료
		registerReceiver(authBR, new IntentFilter(WtmUserAuth.ACTION_AUTH_COMPLETE));

		// 로그인 버튼
		Button btnLogin = (Button) findViewById(R.id.buttonFacebookLogin);
		btnLogin.setOnClickListener((OnClickListener) this);

		Button btnLoginCancel = (Button) findViewById(R.id.buttonFacebookLoginCancel);
		btnLoginCancel.setOnClickListener((OnClickListener) this);

		mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

		onClick(btnLogin);
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
		// 로그인 버튼
			case R.id.buttonFacebookLogin:
				TextView txt = (TextView) findViewById(R.id.textStatus);
				txt.setText(getString(R.string.auth_begin));

				Button btnLogin = (Button) findViewById(R.id.buttonFacebookLogin);
				btnLogin.setVisibility(View.GONE);

				// Button btnLoginCancel = (Button)
				// findViewById(R.id.buttonFacebookLoginCancel);
				// btnLoginCancel.setVisibility(View.VISIBLE);

				// 로그인 인증 시작
				WtmUserAuth auth = new WtmUserAuth(thisContext);
				auth.doAuth();

				mProgressBar.setVisibility(View.VISIBLE);
				break;

			case R.id.buttonFacebookLoginCancel:
				break;
		}
	}

	// 인증 완료 후 호출
	private void authDone()
	{
		// 처음 가입했는지 여부
		final boolean isFirst = false;

		// 필요한 정보 미리 로드
		WtmHttpCallHandler userInfoHandler = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/user", WtmHttpCallHandler.METHOD_GET)
		{
			@Override
			public void onCompleted(String responseContents, int responseStatus)
			{
				if(responseStatus == 200 && !WtmUtil.isError(responseContents))
				{
					WtmUserInfo userInfo = new WtmUserInfo(responseContents);
					WtmDataStore.myInfo = userInfo;
					txt.setText("유저정보 조회 완료");
				}
				else
					isError = true;
			}
		};
		WtmHttpCallHandler roomListHandler = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/user_room", WtmHttpCallHandler.METHOD_GET)
		{
			@Override
			public void onCompleted(String responseContents, int responseStatus)
			{
				if(responseStatus == 200 && !WtmUtil.isError(responseContents))
				{
					WtmDataStore.userRoomInfo = new WtmUserRoomInfo(responseContents);
					txt.setText("가입된 방 조회 완료");
				}
				else
					isError = true;
			}
		};
		WtmHttpCallHandler categoryInfoHandler = new WtmHttpCallHandler("https://wtm.azure-mobile.net/api/category", WtmHttpCallHandler.METHOD_GET)
		{
			@Override
			public void onCompleted(String responseContents, int responseStatus)
			{
				if(responseStatus == 200 && !WtmUtil.isError(responseContents))
				{
					WtmDataStore.categoryInfo = new WtmCategoryInfo(responseContents);
					txt.setText("카테고리 조회 완료");
				}
				else
					isError = true;
			}
		};

		WtmHttpCall httpCall = new WtmHttpCall()
		{
			@Override
			public void onComplete()
			{
				if(WtmDataStore.authKey == null)
					isError = true;

				if(!isError)
				{
					// 처음 가입한 경우 페이스북 정보를 가져와서 유저정보를 업데이트 한다.
					if(isFirst)
					{
					}
					else
					{
						mProgressBar.setVisibility(View.INVISIBLE);
						Intent intentActivityHome = new Intent(thisContext, WtmActivityHome.class);
						startActivity(intentActivityHome);

						finish();
					}
				}
				else
					dialog(getString(R.string.dialogTitle_Error), getString(R.string.error_start_load_fail), new dialogButton().setConfirmButton(getString(R.string.dialogButton_Confirm), new dialogButtonClickListener()
					{
						@Override
						public void onClick()
						{
							finish();
							System.exit(0);
							android.os.Process.killProcess(android.os.Process.myPid());
						}
					}));
			}

			@Override
			public void start()
			{
			}
		};

		httpCall.addHandler(userInfoHandler);
		httpCall.addHandler(roomListHandler);
		httpCall.addHandler(categoryInfoHandler);
		httpCall.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// 메뉴 없음

		return true;
	}
}
