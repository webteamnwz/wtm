package kr.po1.webtechmobile.Http;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class WtmHttpCallHandler
{
	public static final int METHOD_GET = 1;
	public static final int METHOD_POST = 2;
	public static final int METHOD_PUT = 3;
	public static final int METHOD_DELETE = 4;

	private String requestUrl;
	private int requestMethod;
	private Hashtable<String, String> params = new Hashtable<String, String>();
	private boolean isGetParam = false;

	public WtmHttpCallHandler(String url, int method)
	{
		requestUrl = url;
		requestMethod = method;

		if(method == METHOD_GET)
			isGetParam = true;
	}

	public void isGetParam(Boolean yn)
	{
		isGetParam = yn;
	}

	public void addParam(String key, int value)
	{
		addParam(key, Integer.toString(value));
	}

	public void addParam(String key, String value)
	{
		params.put(key, value);
	}

	public String getRequestUrl()
	{
		if(isGetParam && params.size() > 0)
		{
			String query = "";
			Enumeration<String> em = params.keys();
			int paramCount = params.size();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(paramCount);

			while(em.hasMoreElements())
			{
				String key = em.nextElement().toString();
				nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));

				query += "&" + key + "=" + params.get(key);
			}

			requestUrl += "?" + query;
		}

		return requestUrl;
	}

	public int getRequestMethod()
	{
		return requestMethod;
	}

	public Hashtable<String, String> getParams()
	{
		return params;
	}

	public List<NameValuePair> getPostParams()
	{
		if(params.size() <= 0)
			return null;

		Enumeration<String> em = params.keys();
		int paramCount = params.size();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(paramCount);

		while(em.hasMoreElements())
		{
			String key = em.nextElement().toString();
			nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
		}

		return nameValuePairs;
	}

	public void onCompleted(String responseContents, int responseStatus)
	{
	}
}
