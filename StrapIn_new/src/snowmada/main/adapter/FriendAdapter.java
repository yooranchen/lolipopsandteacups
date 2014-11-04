package snowmada.main.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import snowmada.main.Enum.UrlCons;
import snowmada.main.fragment.FriendFragment;
import snowmada.main.network.HttpClient;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FriendAdapter extends ArrayAdapter<String> {
	
	public interface onFriendRequestSend{
		public void onFiendReqSend(int val);
	}

	private List<String> mItems = new ArrayList<String>();
	private ViewHolder mHolder;
	private BaseActivity activity;
	public String responseMsg;
	private onFriendRequestSend listener;
	private FriendFragment friendfragment;
	public int pos;

	public FriendAdapter(BaseActivity activity,FriendFragment friendfragment, int textViewResourceId, List<String> items) {
		super(activity, textViewResourceId, items);
		this.mItems = items;
		this.activity = activity;
		this.friendfragment = friendfragment;
		
		listener = (onFriendRequestSend) friendfragment;
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
			v = vi.inflate(R.layout.add_friend_row, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);
			mHolder.iv_image = (ImageView) v.findViewById(R.id.iv_image);
			mHolder.tv_name = (TextView) v.findViewById(R.id.tv_name);

			mHolder.ll_main_layout = (LinearLayout) v.findViewById(R.id.ll_main_layout);

		} else {
			mHolder = (ViewHolder) v.getTag();
		}
		if (mItems != null) {

			final String st[] = mItems.get(position).split("~");
			activity.imageLoader.DisplayImage("https://graph.facebook.com/" + st[2] + "/picture", mHolder.iv_image);
			mHolder.tv_name.setText(st[0] + " " + st[1]);

			mHolder.ll_main_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					pos = position;
					final Dialog dialog = new Dialog(activity);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
					dialog.setContentView(R.layout.dialog_add_friend);

					TextView tv_name = (TextView) dialog.findViewById(R.id.tv_req_friend_name);
					ImageView iv_img = (ImageView) dialog.findViewById(R.id.iv_req_friend_img);
					final CheckBox isTrack = (CheckBox) dialog.findViewById(R.id.check_track);
					Button yes = (Button) dialog.findViewById(R.id.btn_yes);
					yes.setTypeface(activity.setFont());
					Button no = (Button) dialog.findViewById(R.id.btn_no);
					no.setTypeface(activity.setFont());

					tv_name.setText(st[0] + " " + st[1]);
					isTrack.setChecked(true);
					activity.imageLoader.DisplayImage("https://graph.facebook.com/" + st[2] + "/picture", iv_img);

					yes.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							int status = 0;
							if (isTrack.isChecked()) {
								status = 1;
							} else {
								status = 0;
							}
							new AddSnowmadaFriend().execute(st[2]);
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

				}
			});
		}

		return v;
	}

	class ViewHolder {
		public ImageView iv_image;
		public TextView tv_name;
		public LinearLayout ll_main_layout;

	}

	public class AddSnowmadaFriend extends AsyncTask<String, Void, Boolean> {
		protected void onPreExecute() {
			activity.showProgressBar();

		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean flg = false;
			try {
				JSONObject request = new JSONObject();
				request.put("from_id", activity.application.getUserinfo().userId);
				request.put("to_id", params[0]);
				request.put("first_name", activity.application.getUserinfo().first_name);
				request.put("last_name", activity.application.getUserinfo().last_name);
				JSONObject response = HttpClient.SendHttpPost(UrlCons.REQUEST_FRIEND.getUrl(), request);
				Log.e("DDDDDD", request.toString());

				if (response != null) {
					flg = response.getBoolean("status");
					// responseMsg = response.getString("message");
					return flg;
				}

			} catch (Exception e) {
				activity.dismissProgressBar();
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean status) {
			activity.dismissProgressBar();
			if (status != null) {
				listener.onFiendReqSend(pos);
				
			}
		}
	}
}
