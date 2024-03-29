package snowmada.main.dialog;

import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.Enum.UrlCons;
import snowmada.main.network.HttpClient;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.HomeView;
import snowmada.main.view.R;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DlgFriend extends Dialog implements android.view.View.OnClickListener {

	public interface OnFriendDialogListener {
		public void onTack(String id);

		public void onViewProfile(String id);

		public void onChat(String id);

		public void onDelete(String id);
	}

	private BaseActivity base;
	private TextView tv_name, tv_online_status;
	private ImageView iv_image;
	private Button btn_chat, btn_view_frofile, btn_track, btn_delete;
	private String name, id;
	private boolean online;
	public OnFriendDialogListener listener;
	public HomeView home;

	public DlgFriend(BaseActivity b,HomeView home, String id, String name, boolean online) {
		super(b);
		base = b;
		listener = (OnFriendDialogListener) base;
		this.id = id;
		this.home = home;

		this.name = name;
		this.online = online;

		base.slidingmenu.toggle();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.track_dialog);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		tv_name = (TextView) findViewById(R.id.tv_name);
		iv_image = (ImageView) findViewById(R.id.iv_image);
		btn_chat = (Button) findViewById(R.id.btn_chat);
		btn_view_frofile = (Button) findViewById(R.id.btn_view_frofile);
		btn_track = (Button) findViewById(R.id.btn_track);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		tv_online_status = (TextView) findViewById(R.id.tv_online_status);
		// btn_chat.setText(Html.fromHtml("<font color=\"#ffffff\">CH</font><font color=\"#28b6ff\">AT</font>"));
		// btn_view_frofile.setText(Html.fromHtml("<font color=\"#ffffff\">PROF</font><font color=\"#28b6ff\">ILE</font>"));
		/*
		 * btn_track.setText(Html.fromHtml(
		 * "<font color=\"#ffffff\">TRA</font><font color=\"#ffffff\">CK</font>"
		 * )); btn_delete.setText(Html .fromHtml(
		 * "<font color=\"#ffffff\">DEL</font><font color=\"#ffffff\">ETE</font>"
		 * ));
		 */btn_track.setTypeface(base.setFont());
		btn_delete.setTypeface(base.setFont());
		base.imageLoader.DisplayImage("https://graph.facebook.com/" + id + "/picture", iv_image);
		base.imageLoader.DisplayImage("https://graph.facebook.com/" + id + "/picture", iv_image);// Dont
																									// delete
																									// the
																									// second
																									// line.
		tv_online_status.setText(online ? "Online" : "Offline");
		tv_online_status.setTextColor(online ? Color.parseColor("#0be423") : Color.parseColor("#FF0000"));
		tv_name.setText(name);
		btn_chat.setOnClickListener(this);
		btn_view_frofile.setOnClickListener(this);
		btn_track.setOnClickListener(this);
		btn_delete.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_chat:
			dismiss();
			// listener.onChat(id);
			break;
		case R.id.btn_view_frofile:
			dismiss();
			listener.onViewProfile(id);
			break;
		case R.id.btn_track:
			dismiss();
			home.progressBar.setVisibility(View.VISIBLE);
			isTrackEnable(id);
			/*listener.onTack(id);*/
			break;
		case R.id.btn_delete:
			dismiss();
			listener.onDelete(id);
			break;
		}
	}

	public void isTrackEnable(final String trackuserId) {
		Thread t = new Thread() {
			public void run() {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("to_id", base.application.getUserinfo().userId);
					jsonObject.put("from_id", trackuserId);
					JSONObject json = HttpClient.SendHttpPost(UrlCons.CHECK_TRACK_STATUS.getUrl(), jsonObject);

					if (json != null) {
						if (json.getBoolean("status")) {
							base.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									//Toast.makeText(base, "You can Track "+name, Toast.LENGTH_LONG).show();
									home.progressBar.setVisibility(View.GONE);
									listener.onTack(id);

								}
							});

						} else {
							base.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									home.progressBar.setVisibility(View.GONE);
									Toast.makeText(base, "You cant Track "+name, Toast.LENGTH_LONG).show();

								}
							});
						}

					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
}
