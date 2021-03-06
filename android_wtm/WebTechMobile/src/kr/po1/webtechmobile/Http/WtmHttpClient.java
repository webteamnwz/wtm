package kr.po1.webtechmobile.Http;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

@SuppressWarnings("deprecation")
public class WtmHttpClient
{
	public static HttpClient customHttpClient;

	private WtmHttpClient()
	{
	}

	public static synchronized HttpClient getHttpClient()
	{
		if(customHttpClient == null)
		{
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpProtocolParams.setUseExpectContinue(params, true);

			ConnManagerParams.setTimeout(params, 1000);

			HttpConnectionParams.setConnectionTimeout(params, 5000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			SchemeRegistry shcReg = new SchemeRegistry();
			shcReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			shcReg.register(new Scheme("https", org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory(), 443));
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, shcReg);
			customHttpClient = new DefaultHttpClient(conMgr, params);
		}

		return customHttpClient;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
