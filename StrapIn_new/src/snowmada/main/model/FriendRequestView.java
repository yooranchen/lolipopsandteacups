package snowmada.main.model;

import org.json.JSONObject;

import snowmada.main.Enum.UrlCons;
import snowmada.main.network.HttpClient;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FriendRequestView implements OnClickListener{
	
	private BaseActivity base;
	public View mView;
	public ImageView iv_image;
	public TextView tv_name;
	public LinearLayout mYesButton;
	public LinearLayout mNoButton;
	
	
	public int pos = 0;	
	public String from_id,to_id, name;
	public String responseMsg;
	public int track_status = 0;
	
	public FriendRequestView(BaseActivity b, String from_id, String to_id, String name) {
		base = b;	
		this.name = name;
		this.from_id = from_id;
		this.to_id = to_id;
		
		mView = View.inflate(base, R.layout.request_notification_row, null);
		iv_image = (ImageView)mView.findViewById(R.id.user_friend_image);
		tv_name = (TextView)mView.findViewById(R.id.facebook_friend_name);
		mYesButton = (LinearLayout)mView.findViewById(R.id.yes_button);
		mNoButton = (LinearLayout)mView.findViewById(R.id.no_button);
	
		
		base.imageLoader.DisplayImage("https://graph.facebook.com/" +from_id + "/picture", iv_image);
		tv_name.setText(name+"  has sent you a friend request");
		//a friend request
		
		mYesButton.setOnClickListener(this);
		mNoButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.yes_button:
			String action = "accept";
			new AddFriendStatus().execute();
			break;
			
		case R.id.no_button:
			String action1 = "deny";
			//new AddFriendStatus().execute();
			break;
		}
	}	
	public class AddFriendStatus extends AsyncTask<String, Void, Boolean>{		
		protected void onPreExecute() {
			base.showProgressBar();
		}		
		@Override
		protected Boolean doInBackground(String... params) {
			JSONObject response;
			boolean flag = false;
		  	try {
				JSONObject request = new JSONObject();
				request.put("from_id", from_id);
				request.put("to_id", to_id);
				request.put("first_name", base.application.getUserinfo().first_name);
				request.put("last_name", base.application.getUserinfo().last_name);
				response = HttpClient.SendHttpPost(UrlCons.ACCEPT_FRIEND_REQUEST.getUrl(), request);
				if(response!=null){
					flag = response.getBoolean("status");
				}
				
		  	}catch (Exception e) {
		  		base.dismissProgressBar();
				return false;
			}
			return flag;
		  	
		}
		
		@Override
		protected void onPostExecute(Boolean result) {							
			base.dismissProgressBar();		
				if(result){
					base.application.getUserinfo().setFriendWeb(true);
					//Toast.makeText(base, "hello", 1000).show();
					mView.setVisibility(View.GONE);
					//list.reloadList(pos);
					/*activity.myApp.isWebServiceCallForRefreshFriendList = true;
					activity.presenter.ackAfterFriendRequest(pos);*/
				}							
			}
		}
}
