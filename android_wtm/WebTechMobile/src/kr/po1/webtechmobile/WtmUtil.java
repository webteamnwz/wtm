package kr.po1.webtechmobile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import kr.po1.webtechmobile.DataSet.WtmCategoryDetail;
import kr.po1.webtechmobile.Http.WtmHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class WtmUtil
{
	@SuppressWarnings("deprecation")
	public static String encodeUrl(Bundle parameters)
	{
		if(parameters == null)
			return "";

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(String key : parameters.keySet())
		{
			Object parameter = parameters.get(key);
			if(!(parameter instanceof String))
				continue;

			if(first)
				first = false;
			else
				sb.append("&");
			sb.append(URLEncoder.encode(key) + "=" + URLEncoder.encode(parameters.getString(key)));
		}
		return sb.toString();
	}

	public static String encodePostBody(Bundle parameters, String boundary)
	{
		if(parameters == null)
			return "";
		StringBuilder sb = new StringBuilder();

		for(String key : parameters.keySet())
		{
			Object parameter = parameters.get(key);
			if(!(parameter instanceof String))
				continue;

			sb.append("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n" + (String) parameter);
			sb.append("\r\n" + "--" + boundary + "\r\n");
		}

		return sb.toString();
	}

	public static void getImageToUrl(ImageView view, String url)
	{
		class ImageViewUrl extends AsyncTask<String, Void, Drawable>
		{
			private ImageView imgView;
			private HttpClient httpclient;
			private String url;
			InputStream thisIs = null;

			public ImageViewUrl(ImageView view)
			{
				imgView = view;
				httpclient = WtmHttpClient.getHttpClient();
			}

			@Override
			protected Drawable doInBackground(String... params)
			{
				this.url = params[0];
				Drawable draw = null;

				try
				{
					HttpGet httpget = new HttpGet(params[0]);
					HttpResponse response = httpclient.execute(httpget);
					thisIs = (InputStream) response.getEntity().getContent();
					draw = Drawable.createFromStream(thisIs, "src name");
					WtmDataStore.imageCollector.put(this.url, draw);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

				return draw;
			}

			@Override
			protected void onPostExecute(Drawable result)
			{
				imgView.setImageDrawable(result);
			}
		}

		if(WtmDataStore.imageCollector.containsKey(url))
		{
			Drawable dw = WtmDataStore.imageCollector.get(url);
			view.setImageDrawable(dw);
		}
		else
			new ImageViewUrl(view).execute(url);
	}

	public static String getFbProfileImageUrl(String fbId)
	{
		return "https://graph.facebook.com/" + convFbId(fbId) + "/picture?type=large";
	}

	public static int pxTodp(int px, Context context)
	{
		DisplayMetrics met = context.getResources().getDisplayMetrics();
		float pxtodp = px / (met.densityDpi / 160f);

		return (int) pxtodp;
	}

	public static int dpTopx(int dp, Context context)
	{
		DisplayMetrics met = context.getResources().getDisplayMetrics();
		float dptopx = dp * (met.densityDpi / 160f);

		return (int) dptopx;
	}

	public static String convFbId(String fbId)
	{
		return fbId.replace("Facebook:", "");
	}

	public static String convDate(String date)
	{
		String ret = null;

		switch(date.length())
		{
			case 8:
				String y = date.substring(0, 4);
				String m = date.substring(4, 6);
				String d = date.substring(6, 8);
				ret = y + "-" + m + "-" + d;
				break;

			default:
				ret = date;
				break;
		}

		return ret;
	}

	public static int getDateYear(String date)
	{
		switch(date.length())
		{
			case 8:
				String y = date.substring(0, 4);
				return Integer.parseInt(y);

			default:
				break;
		}

		return 0;
	}

	public static int getDateMonth(String date)
	{
		switch(date.length())
		{
			case 8:
				String m = date.substring(4, 6);
				return Integer.parseInt(m);

			default:
				break;
		}

		return 0;
	}

	public static int getDateDay(String date)
	{
		switch(date.length())
		{
			case 8:
				String d = date.substring(6, 8);
				return Integer.parseInt(d);

			default:
				break;
		}

		return 0;
	}

	public static boolean isError(String jsonContents)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(jsonContents);
			return isError(jsonObject);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return true;
	}

	public static boolean isError(JSONObject jsonContents)
	{
		try
		{
			if(jsonContents.has("code"))
				return !(jsonContents.getInt("code") == 100);

			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	public static CharSequence setTextStyleBold(CharSequence text)
	{
		final StyleSpan style = new StyleSpan(Typeface.BOLD);
		final SpannableString str = new SpannableString(text);
		str.setSpan(style, 0, text.length(), 0);

		return str;
	}

	public static CharSequence setTextStyleItalic(CharSequence text)
	{
		final StyleSpan style = new StyleSpan(Typeface.ITALIC);
		final SpannableString str = new SpannableString(text);
		str.setSpan(style, 0, text.length(), 0);

		return str;
	}

	public static CharSequence setTextStyleNormal(CharSequence text)
	{
		final StyleSpan style = new StyleSpan(Typeface.NORMAL);
		final SpannableString str = new SpannableString(text);
		str.setSpan(style, 0, text.length(), 0);

		return str;
	}

	public static String getCategoryName(String categoryNo)
	{
		return getCategoryName(Integer.parseInt(categoryNo));
	}

	public static String getCategoryName(int categoryNo)
	{
		ArrayList<WtmCategoryDetail> categoryList = WtmDataStore.categoryInfo.getCategoryList();

		for(int index = 0; index < categoryList.size(); index++)
		{
			WtmCategoryDetail categoryInfo = categoryList.get(index);

			if(categoryInfo.getCategoryNo() == categoryNo)
				return categoryInfo.getCategoryName();
		}

		return null;
	}

	public static Bitmap DrawToBitmap(Drawable dw)
	{
		Bitmap bitmap = Bitmap.createBitmap(dw.getMinimumWidth(), dw.getMinimumHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		dw.setBounds(0, 0, dw.getMinimumWidth(), dw.getMinimumHeight());
		dw.draw(canvas);

		return bitmap;
	}
}
