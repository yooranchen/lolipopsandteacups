package snowmada.main.adapter;

import java.util.ArrayList;
import java.util.List;

import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactFriendAdapter extends ArrayAdapter<String> {

	private ArrayList<String> mItems = new ArrayList<String>();
	private ViewHolder mHolder;
	private BaseActivity activity;
	public String responseMsg;
	public int pos;

	public ContactFriendAdapter(BaseActivity activity, int textViewResourceId, ArrayList<String> items) {
		super(activity, textViewResourceId, items);
		this.mItems = items;
		this.activity = activity;

	}

	public List<String> getData() {
		return mItems;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.contact_friend_row, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);
			mHolder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			mHolder.tv_ph = (TextView) v.findViewById(R.id.tv_phone);

			mHolder.ll_main_layout = (LinearLayout) v.findViewById(R.id.ll_main_layout);

		} else {
			mHolder = (ViewHolder) v.getTag();
		}
		if (mItems != null) {

			final String st[] = mItems.get(position).split("~");
			// activity.imageLoader.DisplayImage("https://graph.facebook.com/" +
			// st[2] + "/picture", mHolder.iv_image);
			// mHolder.tv_name.setText(st[0] + " " + st[1]);
			Log.e("value", st[0]+" "+st[1]);
			mHolder.tv_name.setText(st[0]);
			mHolder.tv_ph.setText(st[1]);

		}

		mHolder.ll_main_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String st_[] = mItems.get(position).split("~");
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				smsIntent.setData(Uri.parse("smsto:"));
				smsIntent.setType("vnd.android-dir/mms-sms");
				smsIntent.putExtra("address", new String(st_[1]));
				smsIntent.putExtra("sms_body", "Hey, I just downloaded this awesome app for keeping track of your friends and letting them know if you get hurt on the mountain check it out here: http://goo.gl/XeVQqF");
				try {
					activity.startActivity(smsIntent);
					Log.i("Finished sending SMS...", "");
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(activity, "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
				}
			}
		});

		return v;
	}

	class ViewHolder {
		public TextView tv_name;
		public TextView tv_ph;
		public LinearLayout ll_main_layout;

	}

}
