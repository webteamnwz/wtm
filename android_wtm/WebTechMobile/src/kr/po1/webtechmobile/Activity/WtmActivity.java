package kr.po1.webtechmobile.Activity;

import java.util.Calendar;

import kr.po1.webtechmobile.R;
import kr.po1.webtechmobile.WtmDataStore;
import kr.po1.webtechmobile.dialogButton;
import kr.po1.webtechmobile.Http.WtmHttp;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WtmActivity extends Activity
{
	private ActionBar actionBar;

	private long m_startTime;
	private long m_endTime;
	private boolean m_isPressedBackButton = false;

	protected Boolean isLastActivity = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		m_startTime = System.currentTimeMillis();

		WtmDataStore.context = this;

		try
		{
			actionBar = (ActionBar) getActionBar();
			actionBar.hide();
		}
		catch(Exception e)
		{
		}

		registerReceiver(new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				dataReceiver(context, intent);
			}
		}, new IntentFilter(WtmHttp.ACTION_HTTP_CALL_DONE));
	}

	protected void setString(int viewId, String str)
	{
		TextView textView = (TextView) findViewById(viewId);
		setString(textView, str);
	}

	protected void setString(TextView view, String str)
	{
		view.setText(str);
	}

	protected void dataReceiver(Context context, Intent intent)
	{
	}

	protected void dialog(String title, String contents, dialogButton clickListener)
	{
		Calendar now = Calendar.getInstance();

		int h = now.get(Calendar.HOUR_OF_DAY);
		int m = now.get(Calendar.MINUTE);
		int mili = now.get(Calendar.MILLISECOND);

		Long dialogId = Long.parseLong(Integer.toString(h) + Integer.toString(m) + Integer.toString(mili));

		Intent dialogIntent = new Intent(WtmActivity.this, WtmActivityDialog.class);
		dialogIntent.putExtra("DIALOG_TITLE", title);
		dialogIntent.putExtra("DIALOG_CONETNTS", contents);
		dialogIntent.putExtra("DIALOG_ID", dialogId.toString());

		WtmDataStore.dialogButtons.put(dialogId.toString(), clickListener);

		startActivity(dialogIntent);
	}

	protected void dialog(String title, View contentsView, dialogButton clickListener)
	{
		Calendar now = Calendar.getInstance();

		int h = now.get(Calendar.HOUR_OF_DAY);
		int m = now.get(Calendar.MINUTE);
		int mili = now.get(Calendar.MILLISECOND);

		Long dialogId = Long.parseLong(Integer.toString(h) + Integer.toString(m) + Integer.toString(mili));

		Intent dialogIntent = new Intent(WtmActivity.this, WtmActivityDialog.class);
		dialogIntent.putExtra("DIALOG_TITLE", title);
		dialogIntent.putExtra("DIALOG_CONETNTS", "CUSTOM_DIALOG");
		dialogIntent.putExtra("DIALOG_ID", dialogId.toString());

		WtmDataStore.dialogCustomView = contentsView;
		WtmDataStore.dialogButtons.put(dialogId.toString(), clickListener);

		startActivity(dialogIntent);
	}

	@Override
	public void onBackPressed()
	{
		if(isLastActivity)
		{
			m_endTime = System.currentTimeMillis();

			if(m_endTime - m_startTime > 2000)
				m_isPressedBackButton = false;

			if(m_isPressedBackButton == false)
			{
				m_isPressedBackButton = true;
				m_startTime = System.currentTimeMillis();
				Toast.makeText(this, getString(R.string.ui_exit_message), Toast.LENGTH_SHORT).show();
			}
			else
			{
				finish();
				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}
		else
			finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_home, menu);
		return true;
	}
}
