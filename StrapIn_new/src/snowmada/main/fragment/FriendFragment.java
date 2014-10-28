package snowmada.main.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.Enum.UrlCons;
import snowmada.main.adapter.FriendAdapter;
import snowmada.main.adapter.FriendAdapter.onFriendRequestSend;
import snowmada.main.bean.FriendRequestBean;
import snowmada.main.model.AddFriendView;
import snowmada.main.model.ContactFriendView;
import snowmada.main.model.FriendRequestView;
import snowmada.main.model.UserPendingFriendRequestView;
import snowmada.main.network.HttpClient;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;


public class FriendFragment extends Fragment implements OnClickListener ,onFriendRequestSend{
	public BaseActivity base;
	private Button btn_add_friend, btn_invite_friend, btn_request, btn_contactlist;
	private ArrayList<FriendRequestBean> mRequestArr = new ArrayList<FriendRequestBean>();
	private EditText et_input_key_for_search_add_friends;
	private ListView listView1;
	private LinearLayout ll_list;
	private ArrayList<String> allArr = new ArrayList<String>();
	private ArrayList<String> searchallArr = new ArrayList<String>();
	
	private RelativeLayout add_friend_search_layout;
	private ScrollView sc_list;
	
	
	
	private FriendAdapter adapter1;


	private int pos = 1;
	JSONArray jsonArray ;

	
	@SuppressLint("ValidFragment")
	public FriendFragment(BaseActivity b) {
		base = b;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_friend_list, null, false);
		btn_add_friend = (Button) mView.findViewById(R.id.btn_add_friend);
		btn_invite_friend = (Button) mView.findViewById(R.id.btn_invite_friend);
		btn_request = (Button) mView.findViewById(R.id.btn_request);
		btn_contactlist = (Button) mView.findViewById(R.id.btn_contactlist);
		listView1 = (ListView)mView.findViewById(R.id.listView1);
		ll_list = (LinearLayout)mView.findViewById(R.id.ll_list);
		add_friend_search_layout = (RelativeLayout)mView.findViewById(R.id.add_friend_search_layout);
		et_input_key_for_search_add_friends = (EditText)mView.findViewById(R.id.et_input_key_for_search_add_friends);
		sc_list = (ScrollView)mView.findViewById(R.id.sc_list);
		btn_add_friend.setOnClickListener(this);
		btn_invite_friend.setOnClickListener(this);
		btn_request.setOnClickListener(this);
		btn_contactlist.setOnClickListener(this);
		selectButton(1);
		searchRetailer();
		
		return mView;
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_add_friend:
			pos = 1;
			
			selectButton(1);
			break;
		case R.id.btn_invite_friend:
			pos = 2;
			selectButton(2);
			new RequestToSendWeb().execute();
			break;
		case R.id.btn_request:
			pos = 3;
			selectButton(3);
			new RequestToAcceptWeb().execute();
			break;

		case R.id.btn_contactlist:
			pos = 4;
			selectButton(4);
			new ContactListAsynctask().execute();
			
			break;
		}
	}
	


	public void selectButton(int postion) {
		btn_add_friend.setBackgroundResource(postion == 1 ? R.drawable.tab_select : R.drawable.tab_unselect);
		btn_invite_friend.setBackgroundResource(postion == 2 ? R.drawable.tab_select : R.drawable.tab_unselect);
		btn_request.setBackgroundResource(postion == 3 ? R.drawable.tab_select : R.drawable.tab_unselect);
		btn_contactlist.setBackgroundResource(postion == 4 ? R.drawable.tab_select : R.drawable.tab_unselect);

		btn_add_friend.setTextColor(postion == 1 ? Color.parseColor("#00ccff") : Color.parseColor("#ffffff"));
		btn_invite_friend.setTextColor(postion == 2 ? Color.parseColor("#00ccff") : Color.parseColor("#ffffff"));
		btn_request.setTextColor(postion == 3 ? Color.parseColor("#00ccff") : Color.parseColor("#ffffff"));
		btn_contactlist.setTextColor(postion == 4 ? Color.parseColor("#00ccff") : Color.parseColor("#ffffff"));
		
		
		listView1.setVisibility(postion == 1 ? View.VISIBLE: View.GONE);
		ll_list.setVisibility(postion != 1? View.VISIBLE:View.GONE);
		sc_list.setVisibility(postion != 1? View.VISIBLE:View.GONE);
		add_friend_search_layout.setVisibility(pos == 1? View.VISIBLE:View.GONE);
		if (pos == 1) {
			new AllUserAsynctask().execute();
		}
	}

	public class AllUserAsynctask extends AsyncTask<Void, View, Void> {

		protected void onPreExecute() {
			super.onPreExecute();
			base.showProgressBar();
			
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			base.dismissProgressBar();
			searchallArr = allArr;
			adapter1 = new FriendAdapter(base,FriendFragment.this, R.id.add_friend_layout, searchallArr);
			listView1.setAdapter(adapter1);
			
		}

		protected Void doInBackground(Void... params) {

			try {
				JSONObject request = new JSONObject();
				request.put("user_id", base.application.getUserinfo().userId);
				JSONObject response = HttpClient.SendHttpPost(UrlCons.GET_USER_EXCEPT_FRIEND.getUrl(), request);
				Log.e("Response ", response.toString());
				if (response.getBoolean("status")) {
					 jsonArray = response.getJSONArray("app_users");
					 allArr.clear();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c = jsonArray.getJSONObject(i);
						if (!isFriend(c.getString("user_id"))) {
							allArr.add(c.getString("first_name")+"~"+c.getString("last_name")+"~"+c.getString("user_id"));
							
							//publishProgress(new AddFriendView(base, c.getString("user_id"), c.getString("first_name") + " " + c.getString("last_name")).mView);
						
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}
	}

	public boolean isFriend(String id) {
		boolean flag = false;
		for (int j = 0; j < base.application.getFriendArr().size(); j++) {
			if (id.equalsIgnoreCase(base.application.getFriendArr().get(j).getUserId())) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public class RequestToAcceptWeb extends AsyncTask<String, Void, ArrayList<FriendRequestBean>> {
		protected void onPreExecute() {
			base.showProgressBar();
		}

		@Override
		protected ArrayList<FriendRequestBean> doInBackground(String... params) {
			JSONObject json;
			boolean flag = false;

			try {
				JSONObject mJsonObject = new JSONObject();
				mJsonObject.put("user_id", base.application.getUserinfo().userId);
				json = HttpClient.SendHttpPost(UrlCons.PENDING_FRIEND_REQUEST_TO_ME.getUrl(), mJsonObject);
				System.out.println("!-- " + json.toString());

				if (json != null) {
					flag = json.getBoolean("status");
					if (flag) {
						JSONArray jarray = json.getJSONArray("requests");

						mRequestArr.clear();
						for (int i = 0; i < jarray.length(); i++) {
							JSONObject obj = jarray.getJSONObject(i);
							String from_id = obj.getString("from_id");
							String to_id = obj.getString("to_id");
							String fromName = obj.getString("from_name");
							mRequestArr.add(new FriendRequestBean(from_id, to_id, fromName));

						}
					} else {
						return null;
					}
				}
			} catch (Exception e) {
				return null;
			}
			return mRequestArr;
		}

		@Override
		protected void onPostExecute(ArrayList<FriendRequestBean> mRequestArr) {
			base.dismissProgressBar();
			ll_list.removeAllViews();
			if (mRequestArr != null) {
				for (int i = 0; i < mRequestArr.size(); i++) {
					ll_list.addView(new FriendRequestView(base, mRequestArr.get(i).getFromId(), mRequestArr.get(i).getToId(), mRequestArr.get(i).getName()).mView);

				}
			}
		}
	}

	public class RequestToSendWeb extends AsyncTask<String, Void, JSONArray> {
		protected void onPreExecute() {
			base.showProgressBar();
		}

		@Override
		protected JSONArray doInBackground(String... params) {
			JSONObject json = null;
			JSONArray jarray = null;
			boolean flag = false;

			try {
				JSONObject mJsonObject = new JSONObject();
				mJsonObject.put("userid", base.application.getUserinfo().userId);
				json = HttpClient.SendHttpPost(UrlCons.PENDING_FRIEND_REQUEST_FROM_ME.getUrl(), mJsonObject);
				System.out.println("!-- " + json.toString());

				if (json != null) {
					flag = json.getBoolean("status");
					if (flag) {
						jarray = json.getJSONArray("pending");

					} else {
						return jarray;
					}
				}
			} catch (Exception e) {
				return jarray;
			}
			return jarray;
		}

		@Override
		protected void onPostExecute(JSONArray jarray) {
			base.dismissProgressBar();
			ll_list.removeAllViews();
			if (jarray != null) {
				for (int i = 0; i < jarray.length(); i++) {
					try {
						JSONObject c = jarray.getJSONObject(i);
						ll_list.addView(new UserPendingFriendRequestView(base, c.getString("userid"), c.getString("first_name"), c.getString("last_name")).mView);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					

				}
			}
		}
	}

	public ArrayList<String> getNumber() {
		Cursor phones = base.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		ArrayList<String> arr = new ArrayList<String>();
		arr.clear();
		while (phones.moveToNext()) {
			String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			arr.add(name+"~"+phoneNumber);
		}
		return arr;

	}
	
	public class ContactListAsynctask extends AsyncTask<Void, View, Void> {

		protected void onPreExecute() {
			super.onPreExecute();
			base.showProgressBar();
			ll_list.removeAllViews();
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			base.dismissProgressBar();
			
		}

		@Override
		protected void onProgressUpdate(View... values) {
			super.onProgressUpdate(values);
			ll_list.addView(values[0]);
		}

		@Override
		protected Void doInBackground(Void... params) {
			ArrayList<String> arr = getNumber();
			
			for(int i=0; i<arr.size(); i++){
				publishProgress(new ContactFriendView(base, arr.get(i)).mView);
			}
			return null;
		}

	}
	
	
	public void searchRetailer() {
		et_input_key_for_search_add_friends.addTextChangedListener(
				new TextWatcher() {

					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						String searchString = et_input_key_for_search_add_friends
								.getText().toString().trim();
						int textLength = searchString.length();
						
						searchallArr.clear();
						Toast.makeText(base, ""+allArr.size(), 1000).show();
						for (int i = 0; i < allArr.size(); i++) {
							String retailerName = allArr.get(i);
							
							if (textLength <= retailerName.length()) {
								
								if (searchString.equalsIgnoreCase(retailerName
										.substring(0, textLength))) {
									

									searchallArr.add(allArr.get(i));
								}

							}
						}
						
						
						adapter1 = new FriendAdapter(base,FriendFragment.this, R.id.add_friend_layout, searchallArr);
						listView1.setAdapter(adapter1);
					}

					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {

					}

					public void afterTextChanged(Editable s) {

					}
				});

	}


	@Override
	public void onFiendReqSend(int val) {
		searchallArr.remove(val);
		adapter1 = new FriendAdapter(base,this, R.id.add_friend_layout, searchallArr);
		listView1.setAdapter(adapter1);
		
	}

}
