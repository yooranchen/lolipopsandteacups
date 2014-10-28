package snowmada.main.model;

import org.json.JSONObject;

import snowmada.main.Enum.UrlCons;
import snowmada.main.network.HttpClient;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddFriendView implements OnClickListener{
	
	private BaseActivity base;
	public View mView;
	public ImageView iv_image;
	public TextView tv_name;
	public LinearLayout ll_main_layout;
	
	private String name,id,image = null;
	//public String responseMsg;
	
	public AddFriendView(BaseActivity b, String id,String name) {
		base = b;	
		this.name = name;
		this.id = id;
		
		
		mView = View.inflate(base, R.layout.add_friend_row, null);
		iv_image = (ImageView)mView.findViewById(R.id.iv_image);
		tv_name = (TextView)mView.findViewById(R.id.tv_name);
		
		ll_main_layout = (LinearLayout)mView.findViewById(R.id.ll_main_layout);
		ll_main_layout.setOnClickListener(this);
		base.imageLoader.DisplayImage(image = "https://graph.facebook.com/" +id+ "/picture", iv_image);
		tv_name.setText(name);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_main_layout:
			final Dialog dialog = new Dialog(base);				
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.setContentView(R.layout.dialog_add_friend);
			
			TextView tv_name = (TextView)dialog.findViewById(R.id.tv_req_friend_name);
			ImageView iv_img = (ImageView)dialog.findViewById(R.id.iv_req_friend_img);
			final CheckBox isTrack = (CheckBox)dialog.findViewById(R.id.check_track);
			Button yes = (Button)dialog.findViewById(R.id.btn_yes);
			yes.setText(Html.fromHtml("<font color=\"#ffffff\">YE</font><font color=\"#28b6ff\">S</font>"));
			Button no = (Button)dialog.findViewById(R.id.btn_no);
			no.setText(Html.fromHtml("<font color=\"#ffffff\">N</font><font color=\"#28b6ff\">O</font>"));
			
			tv_name.setText(this.name);
			isTrack.setChecked(true);
			base.imageLoader.DisplayImage(image,iv_img);
				
			yes.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int status = 0;
					if(isTrack.isChecked()){
						status = 1;
					}else{
						status = 0;
					}
					new AddSnowmadaFriend().execute(id);
					dialog.dismiss();
				}
			});
			
			no.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					dialog.dismiss();
				}
			});
			dialog.show();				
		
			break;
		}
	}	
	public class AddSnowmadaFriend extends AsyncTask<String, Void, Boolean>{		
		protected void onPreExecute() {
			base.showProgressBar();
			
		}
		@Override
		protected Boolean doInBackground(String... params) {	
			boolean flg = false;
		  	try {
		  		JSONObject request = new JSONObject();
		  		request.put("from_id", base.application.getUserinfo().userId);
		  		request.put("to_id", params[0]);
		  		request.put("first_name", base.application.getUserinfo().first_name);
		  		request.put("last_name", base.application.getUserinfo().last_name);
		  		JSONObject response = HttpClient.SendHttpPost(UrlCons.REQUEST_FRIEND.getUrl(), request);
		  		
		       if(response!=null){
		    	   flg = response.getBoolean("status");
		    	   //responseMsg = response.getString("message");
		    	   return flg;
		       }
		  	   	
				
			} catch (Exception e) {
				base.dismissProgressBar();
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Boolean status) {
			base.dismissProgressBar();
			if(status!=null){
				mView.setVisibility(View.GONE);
				if(status){					
					//Toast.makeText(base, responseMsg, Toast.LENGTH_LONG).show();				
				}else{
					//Toast.makeText(base, responseMsg, Toast.LENGTH_LONG).show();
				}
			}
		}	
	}
}
