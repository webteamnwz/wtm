package kr.po1.webtechmobile.Http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import kr.po1.webtechmobile.WtmDataStore;
import kr.po1.webtechmobile.Activity.WtmActivityDataLoading;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.protocol.HTTP;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class WtmHttpCall
{
	public static String ACTION_FINISH = "kr.po1.webtechmobile.httpfinish";

	private ArrayList<WtmHttpCallHandler> handlerList;

	private int handlerCnt = 0;
	private int doneCnt = 0;

	public WtmHttpCall()
	{
		handlerList = new ArrayList<WtmHttpCallHandler>();
	}

	public void addHandler(WtmHttpCallHandler handler)
	{
		handlerList.add(handler);
	}

	public void execute()
	{
		handlerCnt = handlerList.size();

		start();

		for(int handlerIndex = 0; handlerIndex < handlerCnt; handlerIndex++)
		{
			final WtmHttpCallHandler handler = handlerList.get(handlerIndex);

			final String url = handler.getRequestUrl();
			final int method = handler.getRequestMethod();

			// final HttpClient httpclient = AndroidHttpClient.newInstance("");
			final HttpClient httpclient = WtmHttpClient.getHttpClient();

			class HttpTask extends AsyncTask<String, Void, String>
			{
				private int responseStatus;

				@Override
				protected String doInBackground(String... params)
				{
					InputStream content = null;

					switch(method)
					{
						case WtmHttpCallHandler.METHOD_GET:
							HttpGet httpget = new HttpGet(url);
							httpget.addHeader("X-ZUMO-AUTH", WtmDataStore.authKey);
							try
							{
								HttpResponse response = httpclient.execute(httpget);
								StatusLine status = response.getStatusLine();
								responseStatus = status.getStatusCode();
								content = response.getEntity().getContent();
							}
							catch(Exception e)
							{
								e.printStackTrace();
								Log.e("tag", e.toString());
							}
							break;

						case WtmHttpCallHandler.METHOD_POST:
							HttpPost httppost = new HttpPost(url);
							httppost.addHeader("X-ZUMO-AUTH", WtmDataStore.authKey);
							try
							{
								if(handler.getParams().size() > 0)
								{
									List<NameValuePair> nameValuePairs = handler.getPostParams();
									httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
								}

								HttpResponse response = httpclient.execute(httppost);
								StatusLine status = response.getStatusLine();
								responseStatus = status.getStatusCode();
								content = response.getEntity().getContent();
							}
							catch(Exception e)
							{
								e.printStackTrace();
								Log.e("tag", e.toString());
							}
							break;

						case WtmHttpCallHandler.METHOD_PUT:
							HttpPut httpput = new HttpPut(url);
							httpput.addHeader("X-ZUMO-AUTH", WtmDataStore.authKey);
							try
							{
								if(handler.getParams().size() > 0)
								{
									List<NameValuePair> nameValuePairs = handler.getPostParams();
									httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
								}

								HttpResponse response = httpclient.execute(httpput);
								StatusLine status = response.getStatusLine();
								responseStatus = status.getStatusCode();
								content = response.getEntity().getContent();
							}
							catch(Exception e)
							{
								e.printStackTrace();
								Log.e("tag", e.toString());
							}
							break;

						case WtmHttpCallHandler.METHOD_DELETE:
							HttpDelete httpdelete = new HttpDelete(url);
							httpdelete.addHeader("X-ZUMO-AUTH", WtmDataStore.authKey);
							try
							{
								HttpResponse response = httpclient.execute(httpdelete);
								StatusLine status = response.getStatusLine();
								responseStatus = status.getStatusCode();
								content = response.getEntity().getContent();
							}
							catch(Exception e)
							{
								e.printStackTrace();
								Log.e("tag", e.toString());
							}
							break;
					}

					String result = convertInputStreamToString(content);

					return result;
				}

				@Override
				protected void onPostExecute(String result)
				{
					handler.onCompleted(result, responseStatus);

					if(++doneCnt >= handlerCnt)
					{
						Intent httpFinishBroadCastIntent = new Intent(ACTION_FINISH);
						WtmDataStore.context.sendBroadcast(httpFinishBroadCastIntent);

						onComplete();
					}
				}
			}
			new HttpTask().execute(url);
		}
	}

	public void start()
	{
		Intent intentDataLoading = new Intent(WtmDataStore.context, WtmActivityDataLoading.class);
		intentDataLoading.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		WtmDataStore.context.startActivity(intentDataLoading);
	}

	public void onComplete()
	{
	}

	public String convertInputStreamToString(InputStream is)
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try
		{
			while((line = reader.readLine()) != null)
				sb.append(line + "\n");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		return sb.toString();
	};
}
