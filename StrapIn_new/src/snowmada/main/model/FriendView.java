package snowmada.main.model;

import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.Enum.UrlCons;
import snowmada.main.dialog.DlgFriend;
import snowmada.main.dialog.DlgFriend.OnFriendDialogListener;
import snowmada.main.network.HttpClient;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FriendView implements OnClickListener{
	public View mView = null;
	private BaseActivity base;
	public ImageView iv_user_image;
	public TextView tv_name;
	public LinearLayout ll_main_row;
	public TextView tv_online_status;
	public CheckBox cb_istrack;
	public LinearLayout  ll_main;
	private  String fname,lname;

	private String id;
	public OnFriendDialogListener listener;
	public boolean online = true;
	public boolean track;
	
	public FriendView(BaseActivity b,final String id,  String fname, String lname, boolean track){
		
		base  = b;
		this.id = id;		
		this.track = track;
		this.fname = fname;
		this.lname = lname;
		mView = View.inflate(base, R.layout.friend_row1, null);
		iv_user_image = (ImageView)mView.findViewById(R.id.iv_user_image);
		tv_name = (TextView)mView.findViewById(R.id.tv_name);
		tv_online_status = (TextView)mView.findViewById(R.id.tv_online_status);
		cb_istrack = (CheckBox)mView.findViewById(R.id.cb_istrack);
		ll_main = (LinearLayout) mView.findViewById(R.id.ll_main);
		cb_istrack.setChecked(track);
		base.imageLoader.DisplayImage("https://graph.facebook.com/" + id + "/picture", iv_user_image);
		
		tv_name.setText(fname+" "+lname);
		tv_online_status.setText(online?"Online":"Offline");
		tv_online_status.setTextColor(online?Color.parseColor("#0be423"):Color.parseColor("#FF0000"));
		ll_main.setOnClickListener(this);
		
		cb_istrack.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Toast.makeText(base, ""+isChecked, 1000).show();
				if(isChecked){
					cb_istrack.setChecked(true);
				}else{
					cb_istrack.setChecked(false);
				}
				new CallWebServicve().execute(base.application.getUserinfo().userId,""+id,isChecked?"1":"0");
			}
		});
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_main:
			new DlgFriend(base,id ,fname+" "+lname, online).show();
			break;
		}	
	}
	
	public void setTrackStatus(String id, String friendid, String status){
		
		
	}
	
	
	public class CallWebServicve extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			
			
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("user_id", params[0]);
				jsonObject.put("friend_id", params[1]);
				jsonObject.put("status", params[2]);
				
				JSONObject response = HttpClient.SendHttpPost(UrlCons.STATUS_TRACK_TOOGLE.getUrl(), jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
	}
	
}
