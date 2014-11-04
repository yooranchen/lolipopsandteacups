package snowmada.main.view;

import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.application.MyApplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;
import static snowmada.main.view.CommonUtilities.SENDER_ID;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "snomada";
	public static MyApplication app = null;
	public static String weiw_message = null;
	private static Runnable runnable;
	private static Handler handler = new Handler();;
	private static long lastUsed = 0;
	private static long idle = 0;

	public GCMIntentService() {
		super(SENDER_ID);
	}

	protected void onRegistered(Context context, String registrationId) {
		app = (MyApplication) getApplication();
		ServerUtilities.register(context, registrationId, app.getUserinfo().userId);
	}

	protected void onUnregistered(Context context, String registrationId) {
		ServerUtilities.unregister(context, registrationId);
	}

	protected void onMessage(Context context, Intent intent) {
		String message = intent.getExtras().getString("price");
		generateNotification(context, message);
	}

	protected void onDeletedMessages(Context context, int total) {
		String message = getString(R.string.gcm_deleted, total);
		generateNotification(context, message);
	}

	public void onError(Context context, String errorId) {
	}

	protected boolean onRecoverableError(Context context, String errorId) {
		Log.i(TAG, "Received recoverable error: " + errorId);
		return super.onRecoverableError(context, errorId);
	}

	private static void generateNotification(final Context context, String message) {
		Log.e(TAG, message);

		try {
			JSONObject json = new JSONObject(message);

			if (json.getInt("status") == 10) { // Friend request send
				PendingIntent intent = PendingIntent.getActivity(context, 0, new Intent(), 0);
				showNotification(context, json.getString("message"), intent);
			} else if (json.getInt("status") == 11) {// Friend request accept
				app.getUserinfo().setFriendWeb(true);
				PendingIntent intent = PendingIntent.getActivity(context, 0, new Intent(), 0);
				showNotification(context, json.getString("message"), intent);
			} else if (json.getInt("status") == 12) { // Track Notification
				Intent notiintent = new Intent(context, TempActivity.class);
				notiintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				notiintent.putExtra("value", true);
				context.startActivity(notiintent);
				PendingIntent intent = PendingIntent.getActivity(context, 0, new Intent(), 0);
				showNotification(context, json.getString("message"), intent);
				// TrackLocation.createInstance(context);

				lastUsed = System.currentTimeMillis();
				idle = 0;
				runnable = new Runnable() {
					public void run() {

						handler.postDelayed(runnable, 100);
						idle = System.currentTimeMillis() - lastUsed;
						// Log.i("idle", "" + idle);
						if (idle >= 2 * 60 * 1000) {
							Intent notiintent1 = new Intent(context, TempActivity.class);
							notiintent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							notiintent1.putExtra("value", false);
							context.startActivity(notiintent1);
							handler.removeCallbacks(runnable);

						}
					}
				};
				handler.postDelayed(runnable, 1000);

			} else if (json.getInt("status") == 13) { // Ski patrol alert

				String info[] = json.getString("message").split("-");
				String name = info[0];
				String ski_patroler_id = info[1];
				app.getUserinfo().setSKiLaunch(true);
				app.getUserinfo().setSkiName(name);
				app.getUserinfo().setSkiId(ski_patroler_id);
				Intent intent = new Intent("SNOMADA_SKI_PATROL_ALERT_DIALOG");
				context.sendBroadcast(intent);

				// showNotification(context, json.getString("message"),intent);
			} else if (json.getInt("status") == 14) { // Meet UP
				Intent inte = new Intent(context, HomeView.class);
				inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |   Intent.FLAG_ACTIVITY_SINGLE_TOP);
				inte.putExtra("ski", true);
				inte.putExtra("lat", json.getString("lat"));
				inte.putExtra("lng", json.getString("lng"));
				PendingIntent intent = PendingIntent.getActivity(context, 0, inte, 0);
				showNotification(context, json.getString("message"), intent);

				// showNotification(context, json.getString("message"),intent);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public static void showNotification(Context context, String noti_msg, PendingIntent intent) {
		int icon = R.drawable.app_logo_small;
		long when = System.currentTimeMillis();
		String title = context.getString(R.string.app_name);
		Notification notification = new Notification(icon, noti_msg, when);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notification.setLatestEventInfo(context, title, noti_msg, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);
	}

}