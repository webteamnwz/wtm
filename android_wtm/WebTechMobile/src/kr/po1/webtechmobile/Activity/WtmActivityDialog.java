package kr.po1.webtechmobile.Activity;

import kr.po1.webtechmobile.R;
import kr.po1.webtechmobile.WtmDataStore;
import kr.po1.webtechmobile.dialogButton;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class WtmActivityDialog extends WtmActivity implements OnClickListener
{
	dialogButton listener;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_style_2);

		LinearLayout bg = (LinearLayout) findViewById(R.id.layoutDialogCommon);
		bg.setBackgroundResource(R.drawable.background_transition);
		TransitionDrawable background = (TransitionDrawable) bg.getBackground();
		background.startTransition(300);

		try
		{
			Intent intent = getIntent();

			String dialogTitle = intent.getExtras().getString("DIALOG_TITLE");
			String dialogContents = intent.getExtras().getString("DIALOG_CONETNTS");

			View scrollView = findViewById(R.id.scrollDialogContents);
			LinearLayout layoutCustom = (LinearLayout) findViewById(R.id.layoutDialogCustom);

			setString(R.id.textDialogCommonTitle, dialogTitle);
			if(dialogContents.equals("CUSTOM_DIALOG"))
			{
				scrollView.setVisibility(View.GONE);
				layoutCustom.setVisibility(View.VISIBLE);
				layoutCustom.addView(WtmDataStore.dialogCustomView);
			}
			else
			{
				scrollView.setVisibility(View.VISIBLE);
				layoutCustom.setVisibility(View.GONE);
				setString(R.id.textDialogCommonContents, dialogContents);
			}

			String dialogId = intent.getExtras().getString("DIALOG_ID");
			listener = WtmDataStore.dialogButtons.get(dialogId);
			WtmDataStore.dialogButtons.remove(dialogId);

			Button buttonClose = (Button) findViewById(R.id.buttonClose);
			buttonClose.setOnClickListener(this);
			if(!listener.isUseClose())
			{
				buttonClose.setText(listener.getCloseText());
				buttonClose.setVisibility(View.GONE);
			}

			Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
			buttonCancel.setOnClickListener(this);
			if(!listener.isUseCancel())
			{
				buttonCancel.setText(listener.getCancelText());
				buttonCancel.setVisibility(View.GONE);
			}

			Button buttonNegative = (Button) findViewById(R.id.buttonNegative);
			buttonNegative.setOnClickListener(this);
			if(!listener.isUseNegative())
			{
				buttonNegative.setText(listener.getNegativeText());
				buttonNegative.setVisibility(View.GONE);
			}

			Button buttonPositive = (Button) findViewById(R.id.buttonPositive);
			buttonPositive.setOnClickListener(this);
			if(!listener.isUsePositive())
			{
				buttonPositive.setText(listener.getPositiveText());
				buttonPositive.setVisibility(View.GONE);
			}

			Button buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
			buttonConfirm.setOnClickListener(this);
			if(!listener.isUseConfirm())
			{
				buttonConfirm.setText(listener.getConfirmText());
				buttonConfirm.setVisibility(View.GONE);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.e("tag", e.toString());
		}
	};

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.buttonClose:
				listener.closeListener.onClick();
				break;

			case R.id.buttonCancel:
				listener.cancelListener.onClick();
				break;

			case R.id.buttonNegative:
				listener.negativeListener.onClick();
				break;

			case R.id.buttonPositive:
				listener.positiveListener.onClick();
				break;

			case R.id.buttonConfirm:
				listener.confirmListener.onClick();
				break;
		}

		finish();
	}

	@Override
	public void onBackPressed()
	{
		if(listener.isUseCancel())
		{
			listener.cancelListener.onClick();
			finish();
		}
		else if(listener.isUseClose())
		{
			listener.closeListener.onClick();
			finish();
		}
		else if(listener.isUseNegative())
		{
			listener.negativeListener.onClick();
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}
}
