package snowmada.main.model;

import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UserPendingFriendRequestView {
	
	
	public View mView;
	public ImageView iv_image;
	public TextView tv_name;
	
	
	public UserPendingFriendRequestView(BaseActivity b, String id, String fanme, String lname) {
		
		
		mView = View.inflate(b, R.layout.user_pending_req_row, null);
		iv_image = (ImageView)mView.findViewById(R.id.user_friend_image);
		tv_name = (TextView)mView.findViewById(R.id.facebook_friend_name);		
		
		b.imageLoader.DisplayImage("https://graph.facebook.com/" +id + "/picture", iv_image);
		tv_name.setText("You have sent "+fanme+" a friend request");
		//You've sent username a friend request
		
	}

}
