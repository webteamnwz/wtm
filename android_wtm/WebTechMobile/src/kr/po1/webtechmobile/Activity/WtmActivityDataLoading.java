package kr.po1.webtechmobile.Activity;

import kr.po1.webtechmobile.R;
import kr.po1.webtechmobile.Http.WtmHttpCall;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class WtmActivityDataLoading extends Activity
{
	BroadcastReceiver br;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_loading);

		// LinearLayout layout = (LinearLayout)
		// findViewById(R.id.layout_http_loading);
		// layout.setVisibility(View.VISIBLE);

		br = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				finish();
			}
		};
		registerReceiver(br, new IntentFilter(WtmHttpCall.ACTION_FINISH));

		// Handler hdl = new Handler()
		// {
		// @Override
		// public void handleMessage(Message msg)
		// {
		// LinearLayout layout = (LinearLayout)
		// findViewById(R.id.layout_http_loading);
		// layout.setVisibility(View.VISIBLE);
		// }
		// };
		// hdl.sendEmptyMessageDelayed(0, 1000);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(br);
	}

	@Override
	public void onBackPressed()
	{
	}
}
