package snowmada.main.model;

import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactFriendView {

	private BaseActivity base;
	public View mView;
	public ImageView iv_image;
	public TextView tv_name, tv_phone;
	public LinearLayout ll_main_layout;

	private String name, phone = null;

	public String st[];

	public ContactFriendView(BaseActivity b, String value) {
		base = b;

		st = value.split("~");

		name = st[0];
		phone = st[1];

		mView = View.inflate(base, R.layout.contact_friend_row, null);
		tv_name = (TextView) mView.findViewById(R.id.tv_name);
		tv_phone = (TextView) mView.findViewById(R.id.tv_phone);
		ll_main_layout = (LinearLayout) mView.findViewById(R.id.ll_main_layout);

		tv_name.setText(name);
		tv_phone.setText(phone);

		ll_main_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				smsIntent.setData(Uri.parse("smsto:"));
				smsIntent.setType("vnd.android-dir/mms-sms");

				smsIntent.putExtra("address", new String(phone));
				smsIntent.putExtra("sms_body", "Hey, I just downloaded this awesome app for keeping track of your friends and letting them know if you get hurt on the mountain check it out here: http://goo.gl/XeVQqF");
				try {
					base.startActivity(smsIntent);

					Log.i("Finished sending SMS...", "");
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(base, "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

}
