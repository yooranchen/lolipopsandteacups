package snowmada.main.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SkiPatrolAlertReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent i = new Intent(context,SKIDialogActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		
		}

}
