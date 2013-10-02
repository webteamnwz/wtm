package kr.po1.webtechmobile;

import java.util.HashMap;
import java.util.Hashtable;

import kr.po1.webtechmobile.DataSet.WtmCategoryInfo;
import kr.po1.webtechmobile.DataSet.WtmFbUserInfo;
import kr.po1.webtechmobile.DataSet.WtmRoomInfo;
import kr.po1.webtechmobile.DataSet.WtmUserInfo;
import kr.po1.webtechmobile.DataSet.WtmUserRoomInfo;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.microsoft.windowsazure.mobileservices.MobileServiceUser;

public class WtmDataStore
{
	public static Context context;

	public static String authKey = null;

	public static String myRoom;

	public static MobileServiceUser authUserInfo;
	public static WtmUserInfo myInfo;
	public static WtmUserInfo userInfo;
	public static WtmRoomInfo roomInfo;
	public static WtmFbUserInfo fbUserInfo;

	public static WtmUserRoomInfo userRoomInfo = null;
	public static WtmCategoryInfo categoryInfo = null;

	public static HashMap<String, dialogButton> dialogButtons = new HashMap<String, dialogButton>();

	public static View dialogCustomView;

	public static int drawableResourceView;
	public static ImageView drawableResourceImageView;
	public static Drawable drawableResource;

	public static Hashtable<String, Drawable> imageCollector = new Hashtable<String, Drawable>();
}
