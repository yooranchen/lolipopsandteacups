package snowmada.main.view;

import snowmada.main.Enum.UrlCons;
import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

	public static final String SERVER_URL = UrlCons.GCM_REGITER.getUrl();
	public static final String SENDER_ID = "110551332890";
	public static final String TAG = "snomada";
	public static final String DISPLAY_MESSAGE_ACTION = "com.androidhive.pushnotifications1.DISPLAY_MESSAGE";
	public static final String EXTRA_MESSAGE = "message";

	public static void displayMessage(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}
}
