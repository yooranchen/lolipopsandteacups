package snowmada.main.view;

import static snowmada.main.view.CommonUtilities.CHAT_MESSAGE;
import static snowmada.main.view.CommonUtilities.DISPLAY_CHAT_MESSAGE_ACTION;

import java.util.StringTokenizer;

import snowmada.main.Enum.PrefCons;
import snowmada.main.application.MyApplication;
import snowmada.main.db.SnowmadaDbAdapter;
import snowmada.main.model.OnMessageProcess;
import snowmada.main.socket.ConnectionProcess;
import snowmada.main.socket.PingChatServer;
import snowmada.main.socket.RefreshUsers;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SocketService extends Service implements OnMessageProcess {
	public ConnectionProcess cp;

	public PingChatServer ping;
	public RefreshUsers refreshUsers;
	//public MyApplication application;
	private final IBinder mBinder = new SocketBinder();
	private SnowmadaDbAdapter dbAdapter;
	private StringTokenizer tokens;
	public SharedPreferences sharedPreferences;
	public String first_name = null;
	public String last_name = null;
	public String userId = null;

	public class SocketBinder extends Binder {
		SocketService getService() {
			return SocketService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;

	}

	@Override
	public void onCreate() {
		super.onCreate();
		sharedPreferences = getSharedPreferences("GLOBAL_SETTINGS", Context.MODE_PRIVATE);
		first_name = sharedPreferences.getString("FIRST_NAME", first_name);
		last_name = sharedPreferences.getString("LAST_NAME", last_name);
		userId = sharedPreferences.getString("USER_ID", userId);
		
		
		dbAdapter = SnowmadaDbAdapter.databaseHelperInstance(getApplicationContext());
		//application = (MyApplication) getApplication();
		cp = new ConnectionProcess(this);
		
		cp.start();
		
		ping = new PingChatServer(cp);
		ping.start();
		refreshUsers = new RefreshUsers(cp);
		refreshUsers.start();
		Log.e("userId", userId);
		cp.sendData("n" + ":" + userId + "~" + first_name + "~" + last_name + ":" + "" + userId);
		
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;

	}

	@Override
	public void processValue(String val) {

		Log.e("SSSSSSocket", val);
		tokens = new StringTokenizer(val, ":");
		String firstToken = tokens.nextToken();
		if (firstToken.equals("$")) {
			receiveMsg(tokens);
		}

		Intent intent = new Intent(DISPLAY_CHAT_MESSAGE_ACTION);
		intent.putExtra(CHAT_MESSAGE, val);
		sendBroadcast(intent);
	}
/*
	@Override
	public void socketConnected() {
		// Toast.makeText(getApplicationContext(), "Connected", 1000).show();
		ping = new PingChatServer(cp);
		ping.start();
		refreshUsers = new RefreshUsers(cp);
		refreshUsers.start();
		Log.e("userId", userId);
		cp.sendData("n" + ":" + userId + "~" + first_name + "~" + last_name + ":" + "" + userId);
	}*/

	public void receiveMsg(StringTokenizer st) {
		String ss = "";
		final String sendername = new String(tokens.nextToken());
		final String message = new String(tokens.nextToken());
		String[] s1 = sendername.split("~");
		dbAdapter.inserValue(s1[0], userId, s1[1] + " " + s1[2], message, "" + System.currentTimeMillis());

	}

	

}
