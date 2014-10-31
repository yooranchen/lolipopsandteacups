package snowmada.main.view;

import static snowmada.main.view.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static snowmada.main.view.CommonUtilities.EXTRA_MESSAGE;
import static snowmada.main.view.CommonUtilities.SENDER_ID;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.Enum.UrlCons;
import snowmada.main.adapter.CustomInfoWindowAdapter;
import snowmada.main.bean.DlgSkiPatrolConfirmation;
import snowmada.main.bean.FriendBean;
import snowmada.main.bean.GoodDeals;
import snowmada.main.bean.MeetUpBean;
import snowmada.main.constant.Constant;
import snowmada.main.dialog.DlgFriend.OnFriendDialogListener;
import snowmada.main.dialog.DlgMeetup;
import snowmada.main.fragment.ChatFragment;
import snowmada.main.fragment.DealFragment;
import snowmada.main.fragment.FriendFragment;
import snowmada.main.model.FriendView;
import snowmada.main.network.HttpClient;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;

public class HomeView extends BaseActivity implements OnFriendDialogListener {

	private final String TAG = "snomada";
	public GoogleMap map;
	private ImageView iv_profile_pic, iv_slider_left, iv_slider_right;
	private TextView tv_user_name, tv_page_title;
	private ImageView iv_ski_patrol;
	private boolean UserType_flag = true;
	private LinearLayout ll_friends;
	private ProgressBar progressBar, progressrar_friend_list;
	private LinearLayout ll_meet_up, ll_chat, ll_deals, ll_track, ll_add_friends, ll_profile;
	public ArrayList<FriendBean> friendArr = new ArrayList<FriendBean>();
	public ArrayList<MeetUpBean> meetUpArr = new ArrayList<MeetUpBean>();
	public List<String> expireMarkerIds = new ArrayList<String>();
	public HashMap<Marker, String> markerHas = new HashMap<Marker, String>();

	public boolean isSliderToggle = false;
	public Runnable runnable;
	private Handler handler = new Handler();
	private int counter = 0;
	private static final int TRACK_INTERVAL = 60000;
	private static final int TIME_SPAN = 3000;
	public boolean isTackingOpen = false;
	public boolean isLongTouchEnableOnMap = false;
	public static final int TIME_DIALOG_ID = 999;
	public static final int DATE_DIALOG_ID = 998;
	public int hour;
	public int minute;
	public int year;
	public int month;
	public int day;
	private DlgMeetup dlgmeetup;
	private boolean isMeetUpCall = true;
	public String profile_id = null;
	private AsyncTask<Void, Void, Void> mRegisterTask;
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;
	public ArrayList<GoodDeals> mDealsArr = new ArrayList<GoodDeals>();
	public boolean pushSendOnFirstRequestTrack = true;
	public String ski_patroler_id, name;
	private RadioGroup rgViews;
	private Bitmap ic_bitmap = null;
	Bitmap bhalfsize;
	private boolean isZoomWhenTracking = false;

	/*
	 * private Button btn_text; boolean flag = false;
	 */

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		application.getUserinfo().setFriendWeb(false);
		getViewbyId();
		if (application.getUserinfo().ski_launch) {
			handler2.sendEmptyMessageDelayed(1, 2000);
		}
		if (isNetworkConnected())
			new SignInAsyncTask().execute();
		getPushNotificationDeviceID();

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		setCurrentDateOnView();
		switch (id) {

		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timePickerListener, hour, minute, true);
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, datePickerListener, year, month, day);
		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;
			int sec = 00;
			dlgmeetup.tv_time.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)).append(":").append(0).append(0));
		}
	};

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			if ((month + 1) < 10 && day < 10) {
				dlgmeetup.tv_date.setText(new StringBuilder().append(year).append("-").append(0).append(month + 1).append("-").append(0).append(day));
			} else if ((month + 1) < 10) {
				dlgmeetup.tv_date.setText(new StringBuilder().append(year).append("-").append(0).append(month + 1).append("-").append(day));
			} else if (day < 10) {
				dlgmeetup.tv_date.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(0).append(day));
			} else {
				dlgmeetup.tv_date.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day));
			}
		}
	};

	public void setCurrentDateOnView() {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
	}

	public void getViewbyId() {
		// btn_text = (Button)findViewById(R.id.button1_test);
		slidingmenu.setMode(SlidingMenu.LEFT_RIGHT);
		slidingmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		slidingmenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingmenu.setShadowDrawable(R.drawable.shadow);
		slidingmenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingmenu.setFadeDegree(0.35f);
		slidingmenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingmenu.setMenu(R.layout.slider_friend_list);
		slidingmenu.setSecondaryMenu(R.layout.slider_menu);
		slidingmenu.setSlidingEnabled(true);

		iv_profile_pic = (ImageView) findViewById(R.id.iv_profile_pic);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		iv_slider_left = (ImageView) findViewById(R.id.iv_slider_left);
		iv_slider_right = (ImageView) findViewById(R.id.iv_slider_right);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressrar_friend_list = (ProgressBar) findViewById(R.id.progressrar_friend_list);

		ll_meet_up = (LinearLayout) findViewById(R.id.ll_meet_up);
		ll_chat = (LinearLayout) findViewById(R.id.ll_chat);
		ll_deals = (LinearLayout) findViewById(R.id.ll_deals);
		ll_track = (LinearLayout) findViewById(R.id.ll_track);
		ll_add_friends = (LinearLayout) findViewById(R.id.ll_add_friends);
		ll_profile = (LinearLayout) findViewById(R.id.ll_profile);
		tv_page_title = (TextView) findViewById(R.id.tv_page_title);

		iv_ski_patrol = (ImageView) findViewById(R.id.iv_ski_patrol);
		rgViews = (RadioGroup) findViewById(R.id.rg_views);
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		map.getUiSettings().setCompassEnabled(true);
		map.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
		application.setMap(map);

		ll_friends = (LinearLayout) findViewById(R.id.ll_friends);
		iv_slider_left.setOnClickListener(this);
		iv_slider_right.setOnClickListener(this);
		ll_meet_up.setOnClickListener(this);
		ll_chat.setOnClickListener(this);
		ll_deals.setOnClickListener(this);
		ll_track.setOnClickListener(this);
		ll_add_friends.setOnClickListener(this);
		ll_profile.setOnClickListener(this);
		map.setOnMapLongClickListener(this);
		iv_profile_pic.setOnClickListener(this);
		displayView(3);
		TrackLocation.createInstance(this);

		iv_ski_patrol.setOnClickListener(this);
		slidingmenu.setOnOpenedListener(new OnOpenedListener() {

			@Override
			public void onOpened() {
				if (isNetworkConnected()) {
					if(application.getUserinfo().isFriendWebCall){
						application.getUserinfo().setFriendWeb(false);
						new FriendListAsynctask().execute();
					}
					
				}

			}
		});

		rgViews.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_normal) {
					map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				} else if (checkedId == R.id.rb_satellite) {
					map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				} else if (checkedId == R.id.rb_terrain) {
					map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				}
			}
		});

	}

	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_slider_left:
			slidingmenu.showMenu(true);
			// new FriendListAsynctask().execute();
			break;
		case R.id.iv_slider_right:
			slidingmenu.showSecondaryMenu(true);
			break;
		case R.id.ll_meet_up:
			displayView(0);

			break;
		case R.id.ll_chat:
			displayView(1);
			break;
		case R.id.ll_deals:
			displayView(2);
			break;
		case R.id.ll_track:
			displayView(3);
			break;
		case R.id.ll_add_friends:
			displayView(4);
			break;
		case R.id.ll_profile:
			displayView(5);
			break;
		case R.id.iv_profile_pic:
			isSliderToggle = false;
			displayView(5);
			break;
		case R.id.iv_ski_patrol:
			new DlgSkiPatrolConfirmation(this, this).show();
			break;
		}
	}

	private void displayView(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			findViewById(R.id.fragment_layout).setVisibility(View.INVISIBLE);
			break;
		case 1:
			fragment = new ChatFragment(this);
			break;
		case 2:
			fragment = new DealFragment(this, mDealsArr);
			break;
		case 3:
			findViewById(R.id.fragment_layout).setVisibility(View.INVISIBLE);
			break;
		case 4:
			fragment = new FriendFragment(this);
			break;
		case 5:
			/*
			 * fragment = new ProfileFragment(this, UserType_flag, profile_id);
			 * UserType_flag = true;
			 */
			break;
		}

		switch (position) {

		case 0:
			isLongTouchEnableOnMap = true;
			tv_page_title.setText(getCustomText("MEETUP", "LOCATION"));
			rgViews.setVisibility(View.VISIBLE);
			break;
		case 1:
			tv_page_title.setText(getCustomText("LIVE", "CHAT"));
			rgViews.setVisibility(View.GONE);
			break;
		case 2:
			tv_page_title.setText(getCustomText("GOOD", "DEALS"));
			rgViews.setVisibility(View.GONE);
			break;
		case 3:
			isLongTouchEnableOnMap = false;
			tv_page_title.setText(getCustomText("TRACK", "FRIENDS"));
			rgViews.setVisibility(View.VISIBLE);
			break;
		case 4:
			tv_page_title.setText(getCustomText("ADD", "FRIENDS"));
			rgViews.setVisibility(View.GONE);
			break;
		case 5:
			tv_page_title.setText(getCustomText("VIEW", "PROFILE"));
			rgViews.setVisibility(View.GONE);
			break;
		}

		if (isSliderToggle) {
			slidingmenu.toggle();
		} else {
			isSliderToggle = true;
		}

		if (position == 0 || position == 3) {

		} else {
			if (fragment != null) {

				findViewById(R.id.fragment_layout).setVisibility(View.VISIBLE);
				FragmentManager fragmentManager = getSupportFragmentManager();
				if (fragmentManager.findFragmentById(R.id.fragment_layout) != null)
					fragmentManager.beginTransaction().replace(R.id.fragment_layout, fragment).commit();
				else
					fragmentManager.beginTransaction().add(R.id.fragment_layout, fragment).commit();
			}
		}
	}

	public void onTack(String id) {

		if (isTackingOpen) {

			showMsg("Already One Tracking Open");
			return;
		}
		isZoomWhenTracking = true;
		progressBar.setVisibility(View.VISIBLE);
		isSliderToggle = false;
		displayView(3);

		int pos = 0;
		for (int i = 0; i < friendArr.size(); i++) {
			if (friendArr.get(i).getUserId().equalsIgnoreCase(id)) {
				pos = i;
				isTackingOpen = true;
				break;
			}
			
			
			
		}
		doTrack(id, friendArr.get(pos).getFirstName() + " " + friendArr.get(pos).getLastName());
	}

	private void doTrack(final String id, final String name) {
		runnable = new Runnable() {
			public void run() {
				handler.postDelayed(runnable, TIME_SPAN);
				if (counter > (int) (TRACK_INTERVAL / TIME_SPAN)) {
					ic_bitmap = null;
					counter = 0;
					isTackingOpen = false;

					if (marker != null) {
						marker.remove();
						pushSendOnFirstRequestTrack = true;
					}
					handler.removeCallbacks(runnable);

				} else {
					counter++;
					getTrackLocation(id, name);
				}
			}
		};
		handler.postDelayed(runnable, 0);
	}

	public void getTrackLocation(final String trackuserId, final String name) {
		Thread t = new Thread() {
			public void run() {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("track_user_id", trackuserId);
					jsonObject.put("userid", application.getUserinfo().userId);
					jsonObject.put("ispush", pushSendOnFirstRequestTrack);
					pushSendOnFirstRequestTrack = false;
					JSONObject json = HttpClient.SendHttpPost(UrlCons.GET_LOCATION.getUrl(), jsonObject);

					System.out.println("!!! " + json.toString());
					if (json != null) {
						if (ic_bitmap != null) {

							updateMap(Double.valueOf(json.getString("lat")), Double.valueOf(json.getString("lng")), name, ic_bitmap);
						} else {
							ic_bitmap = getBitmapFromURL("https://graph.facebook.com/" + trackuserId + "/picture?type=large");
							updateMap(Double.valueOf(json.getString("lat")), Double.valueOf(json.getString("lng")), name, ic_bitmap);
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

	public void updateMap(final Double lat, final Double lng, final String name, final Bitmap b) {
		runOnUiThread(new Runnable() {

			public void run() {
				// Toast.makeText(getApplicationContext(), "Here", 1000).show();

				if (marker != null) {
					marker.remove();
				}
				/* bhalfsize=Bitmap.createScaledBitmap(b, b.getWidth()*4,b.getHeight()*4, false);*/
				progressBar.setVisibility(View.GONE);
				marker = map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Name:" + name).snippet("Time:" + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds()).snippet("Time:" + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds()).icon(BitmapDescriptorFactory.fromBitmap(b)));
				marker.showInfoWindow();
				if(isZoomWhenTracking){
					isZoomWhenTracking = false;
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16));
				}
				
				
			}
		});
	}

	public void deleteOldMarker() {
		Thread t = new Thread() {
			String st;

			public void run() {
				try {
					for (int i = 0; i < expireMarkerIds.size(); i++) {
						st = i == 0 ? expireMarkerIds.get(i) : st + "," + expireMarkerIds.get(i);
					}
					JSONObject json = new JSONObject();
					json.put("deleted_ids", st);
					HttpClient.SendHttpPost(UrlCons.MEET_UP_DETELE.getUrl(), json);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	public void onMapLongClick(final LatLng arg0) {
		if (isLongTouchEnableOnMap) {
			//Toast.makeText(getApplicationContext(), "Test", 4000).show();
			AlertDialog.Builder builder = new AlertDialog.Builder(HomeView.this);
			builder.setCancelable(true);
			builder.setTitle("Craete meet up location?");
			builder.setInverseBackgroundForced(true);
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					dlgmeetup = new DlgMeetup(HomeView.this, arg0);
					dlgmeetup.show();
				}
			});
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Constant.isEditedProfile) {
			Constant.isEditedProfile = false;
			isSliderToggle = false;
			displayView(5);
		} else if (Constant.isAcceptFriendReq) {

		}
	}

	public void onViewProfile(String id) {
		UserType_flag = false;
		profile_id = id;
		isSliderToggle = false;
		displayView(5);
	}

	public void onChat(String id) {
	}

	public void onDelete(String id) {
		new DeleteFriendAsyncTask().execute(id);
	}

	public void onMeetUpSubmit(final String id, final String name, final String location, final String description, final String time, final String lat, final String lng, final String meetupdate) {
		Thread t = new Thread() {
			public void run() {
				boolean flag = false;
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("fbid", application.getUserinfo().userId);
					jsonObject.put("fname", application.getUserinfo().first_name);
					jsonObject.put("marker_id", id);
					jsonObject.put("person_name", name);
					jsonObject.put("loc_name", location);
					jsonObject.put("loc_desc", description);
					jsonObject.put("meet_time", time);
					jsonObject.put("lat", lat);
					jsonObject.put("lng", lng);
					jsonObject.put("meet_date", meetupdate);
					jsonObject.put("actn", "add");
					JSONObject json = HttpClient.SendHttpPost(UrlCons.ADD_MEET_UP_LOCATION.getUrl(), jsonObject);
					if (json != null) {
						flag = json.getBoolean("status");
						if (flag) {
							syncMeetUpLocation(true);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	public class SignInAsyncTask extends AsyncTask<String, Void, Void> {
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		protected Void doInBackground(String... params) {
			try {
				JSONObject request = new JSONObject();
				request.put("fbid", application.getUserinfo().userId);
				request.put("fname", application.getUserinfo().first_name);
				request.put("lname", application.getUserinfo().last_name);
				request.put("birthday", application.getUserinfo().dob);
				request.put("usertype", "F");
				System.out.println("!-->>>>>>>>>>>>>>>> ");
				System.out.println("!-->>>>>>>>>>>>>>>> " + request.toString());
				final JSONObject response = HttpClient.SendHttpPost(UrlCons.LOGIN.getUrl(), request);
				// Log.e(TAG, "Signin response:" + response.toString());
				// System.out.println("!-- "+ response.toString());
				if (response != null) {
					if (response.getBoolean("status")) {
						String imgurl = response.isNull("image") ? "https://graph.facebook.com/" + response.getString("user_id") + "/picture" : UrlCons.IMAGE_PATH.getUrl() + response.getString("image");
						application.getUserinfo().setUser(response.getString("first_name"), response.getString("last_name"), response.getString("user_id"), imgurl, application.getUserinfo().dob);
						application.getUserinfo().setSession(true);
						application.getUserinfo().setZoom(true);
						runOnUiThread(new Runnable() {

							public void run() {
								try {
									imageLoader.DisplayImage("https://graph.facebook.com/" + application.getUserinfo().userId + "/picture?type=large", iv_profile_pic);
									tv_user_name.setText(response.getString("first_name") + " " + response.getString("last_name"));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
				progressBar.setVisibility(View.GONE);
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressBar.setVisibility(View.GONE);
			if (isNetworkConnected())
				new FriendListAsynctask().execute();
		}
	}

	public class FriendListAsynctask extends AsyncTask<String, Void, ArrayList<FriendBean>> {

		protected void onPreExecute() {
			progressrar_friend_list.setVisibility(View.VISIBLE);
		}

		protected ArrayList<FriendBean> doInBackground(String... params) {
			try {
				JSONObject request = new JSONObject();
				request.put("userid", application.getUserinfo().userId);
				JSONObject response = HttpClient.SendHttpPost(UrlCons.FRIEND_LIST.getUrl(), request);
				if (response.getBoolean("status")) {
					friendArr.clear();
					JSONArray arr = response.getJSONArray("friends");
					for (int j = 0; j < arr.length(); j++) {
						JSONObject c = arr.getJSONObject(j);
						friendArr.add(new FriendBean(c.getString("userid"), c.getString("first_name"), c.getString("last_name"), Integer.parseInt(c.getString("track")) == 1 ? true : false));
					}
				} else {
					friendArr.clear();
				}
			} catch (Exception e) {
				e.printStackTrace();
				progressrar_friend_list.setVisibility(View.INVISIBLE);
			}
			return friendArr;
		}

		protected void onPostExecute(final ArrayList<FriendBean> result) {
			progressrar_friend_list.setVisibility(View.INVISIBLE);

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ll_friends.removeAllViews();

					if (result != null) {
						application.setFriendArr(result);
						for (int i = 0; i < result.size(); i++) {
							ll_friends.addView(new FriendView(HomeView.this, result.get(i).getUserId(), result.get(i).getFirstName(), result.get(i).getLastName(), result.get(i).isTrack()).mView);
						}
					}

				}
			});

			if (isMeetUpCall) {
				syncMeetUpLocation(isMeetUpCall);
				isMeetUpCall = false;
				// new GoodDealsWeb().execute();

			}
		}
	}

	public void syncMeetUpLocation(boolean flag) {
		if (flag) {
			// progressBar.setVisibility(View.VISIBLE);
			Thread t = new Thread() {
				public void run() {
					try {

						JSONObject jsonObject = new JSONObject();
						jsonObject.put("fbid", application.getUserinfo().userId);
						Log.e("Meetup req", jsonObject.toString());
						JSONObject json = HttpClient.SendHttpPost(UrlCons.MEET_UP_MERKER_LIST.getUrl(), jsonObject);
						Log.e("Meetup response", json.toString());
						meetUpArr.clear();
						expireMarkerIds.clear();
						if (json.getBoolean("status")) {
							JSONArray jArr = json.getJSONArray("MeetList");
							for (int i = 0; i < jArr.length(); i++) {
								JSONObject c = jArr.getJSONObject(i);
								String _marker_id = c.getString("marker_id");
								String _name = c.getString("person_name");
								String _loc = c.getString("loc_name");
								String _desc = c.getString("loc_desc");
								String _identifier = c.getString("identifier");
								String _date = c.getString("meet_date");
								String time = c.getString("meet_time");
								double _lat = Double.parseDouble(c.getString("lat"));
								double _lng = Double.parseDouble(c.getString("lng"));
								if (getCurrentDate() < getExpireTime(_date, time)) {
									meetUpArr.add(new MeetUpBean(_marker_id, _name, _loc, _desc, _date, time, _identifier, _lat, _lng));
								} else {
									expireMarkerIds.add("" + _marker_id);
								}
								if (meetUpArr != null) {
									dropMarker(meetUpArr);
								}
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			t.start();
		}
	}

	public void dropMarker(final ArrayList<MeetUpBean> result) {
		runOnUiThread(new Runnable() {
			public void run() {
				progressBar.setVisibility(View.GONE);
				if (result != null) {
					if (marker != null) {
						marker.remove();
					}

					for (int i = 0; i < result.size(); i++) {

						if (result.get(i).getOwner().equalsIgnoreCase("ME")) {
							marker = map.addMarker(new MarkerOptions().position(new LatLng(result.get(i).getLat(), result.get(i).getLng())).title("Name:" + result.get(i).getName()).snippet("Location:" + result.get(i).getLocation() + "\nDescription: " + result.get(i).getDescription() + "\nDate: " + result.get(i).getDate1() + "\nTime: " + result.get(i).getTime()).icon(BitmapDescriptorFactory.fromResource(R.drawable.meet_up_icon)));
							marker.setDraggable(true);

							markerHas.put(marker, result.get(i).getId());
						} else {
							marker = map.addMarker(new MarkerOptions().position(new LatLng(result.get(i).getLat(), result.get(i).getLng())).title("Name:" + result.get(i).getName()).snippet("Location:" + result.get(i).getLocation() + "\nDescription: " + result.get(i).getDescription() + "\nDate: " + result.get(i).getDate1() + "\nTime: " + result.get(i).getTime()).icon(BitmapDescriptorFactory.fromResource(R.drawable.meet_up_icon)));
							marker.setDraggable(false);

							markerHas.put(marker, result.get(i).getId());
						}
					}
					if (expireMarkerIds != null) {
						if (expireMarkerIds.size() > 0) {
							deleteOldMarker();
						}
					}
				}
			}
		});
	}

	@Override
	public void loadDealsonMap(ArrayList<GoodDeals> mDealsArr) {
		for (int i = 0; i < mDealsArr.size(); i++) {
			marker = map.addMarker(new MarkerOptions().position(new LatLng(mDealsArr.get(i).getLat(), mDealsArr.get(i).getLng())).title("Name:" + mDealsArr.get(i).getAdvtName()).snippet("Price :" + "100$\n" + "Description : " + mDealsArr.get(i).getDescription() + "\n" + "BUY it now?").icon(BitmapDescriptorFactory.fromBitmap(mDealsArr.get(i).getImage())));
			markerHas.put(marker, mDealsArr.get(i).getMarkerId());
		}
	}

	public void getPushNotificationDeviceID() {
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(HomeView.this, "Internet Connection Error", "Please connect to working Internet connection", false);
			return;
		}

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));

		final String regId = GCMRegistrar.getRegistrationId(this);

		if (regId.equals("")) {
			GCMRegistrar.register(getApplicationContext(), SENDER_ID);
		} else {
			GCMRegistrar.register(getApplicationContext(), SENDER_ID);
			if (GCMRegistrar.isRegisteredOnServer(this)) {

			} else {
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						ServerUtilities.register(context, regId, "" + application.getUserinfo().userId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}

	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);

			if (newMessage != null && newMessage.startsWith("{") && newMessage.endsWith("}")) {
				try {
					JSONObject json = new JSONObject(newMessage);
					if (json.getInt("status") == 11) {
						new FriendListAsynctask().execute();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {

		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
			handler.removeCallbacks(runnable);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	/*
	 * public class GoodDealsWeb extends AsyncTask<Boolean, Void, Boolean> {
	 * 
	 * @Override protected Boolean doInBackground(Boolean... params) { boolean
	 * flag = false; try { JSONObject jsonObject = new JSONObject();
	 * jsonObject.put("user_id", application.getUserinfo().userId); JSONObject
	 * json = HttpClient.SendHttpPost(UrlCons.GOOD_DEALS.getUrl(), jsonObject);
	 * 
	 * if (json != null) { flag = json.getBoolean("status"); if (flag) {
	 * JSONArray arr = json.getJSONArray("data"); for (int i = 0; i <
	 * arr.length(); i++) { JSONObject c = arr.getJSONObject(i); long markerid =
	 * System.currentTimeMillis() + new Random().nextInt(1000); String id =
	 * "11"; String name = c.getString("name"); String advt_name =
	 * c.getString("advt_name"); String address = c.getString("address"); Double
	 * lat = Double.valueOf(c.getString("lat")); Double lng =
	 * Double.valueOf(c.getString("lng"));
	 * 
	 * String url = UrlCons.BANNER_ADD.getUrl() + c.getString("advt_image");
	 * Bitmap bitmap; bitmap = getBitmapFromURL(url);
	 * 
	 * String desc = c.getString("description"); mDealsArr.add(new GoodDeals(""
	 * + markerid, id, name, advt_name, address, lat, lng, bitmap, url, desc));
	 * } } }
	 * 
	 * } catch (JSONException e) { e.printStackTrace(); return flag; } catch
	 * (NumberFormatException e) { e.printStackTrace(); return flag; } return
	 * flag; }
	 * 
	 * @Override protected void onPostExecute(Boolean status) { if (status) {
	 * for (int i = 0; i < mDealsArr.size(); i++) { marker = map.addMarker(new
	 * MarkerOptions().position(new LatLng(mDealsArr.get(i).getLat(),
	 * mDealsArr.get(i).getLng())).title("Name:" +
	 * mDealsArr.get(i).getAdvtName()).snippet("Price :" + "100$\n" +
	 * "Description : " + mDealsArr.get(i).getDescription() + "\n" +
	 * "BUY it now?"
	 * ).icon(BitmapDescriptorFactory.fromBitmap(mDealsArr.get(i).getImage())));
	 * markerHas.put(marker, mDealsArr.get(i).getMarkerId()); } } } }
	 */

	public  Bitmap getBitmapFromURL(String src) {
		try {
			InputStream in = new java.net.URL(src).openStream();
			//Bitmap bi =  getCircularBitmap(BitmapFactory.decodeStream(in));
			Bitmap bi = getCircularBitmapWithWhiteBorder(BitmapFactory.decodeStream(in),5);
			 bhalfsize=Bitmap.createScaledBitmap(bi,(int) (bi.getWidth()/1.40),(int)(bi.getHeight()/1.40), false);
			 return bhalfsize;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap,
	        int borderWidth) {
	    if (bitmap == null || bitmap.isRecycled()) {
	        return null;
	    }

	    final int width = bitmap.getWidth() + borderWidth;
	    final int height = bitmap.getHeight() + borderWidth;

	    Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	    BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
	    Paint paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setShader(shader);

	    Canvas canvas = new Canvas(canvasBitmap);
	    float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
	    canvas.drawCircle(width / 2, height / 2, radius, paint);
	    paint.setShader(null);
	    paint.setStyle(Paint.Style.STROKE);
	    paint.setColor(Color.WHITE);
	    paint.setStrokeWidth(borderWidth);
	    canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
	    return canvasBitmap;
	}

	public void OnEmergencyConformDlg() {
		new SKIEmergencyButtonPressWeb().execute();
	}

	public class SKIEmergencyButtonPressWeb extends AsyncTask<String, Void, Boolean> {

		protected void onPreExecute() {
			showProgressBar();
		}

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				JSONObject request = new JSONObject();
				request.put("userid", application.getUserinfo().userId);
				JSONObject response = HttpClient.SendHttpPost(UrlCons.SKI_PATROL.getUrl(), request);
				if (response != null) {
					return response.getBoolean("status");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dismissProgressBar();
			if (result) {

				final Dialog dialog1 = new Dialog(HomeView.this);
				dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog1.setContentView(R.layout.skipetrol_btn_press_dialog);
				dialog1.setCancelable(false);
				Button ok = (Button) dialog1.findViewById(R.id.iv_dlg_ok);
				ok.setTypeface(setFont());
				TextView tv_dialog = (TextView) dialog1.findViewById(R.id.tv_alert_dialog_text);
				tv_dialog.setTypeface(setFont());

				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog1.dismiss();
					}
				});
				dialog1.show();
			}
		}
	}

	Handler handler2 = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
			case 1:
				application.getUserinfo().setSKiLaunch(false);
				application.getUserinfo().setZoom(false);
				doTrack(application.getUserinfo().skiId, application.getUserinfo().ski_name);
				break;

			}
		}
	};

	public class DeleteFriendAsyncTask extends AsyncTask<String, Void, Boolean> {
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		protected Boolean doInBackground(String... params) {
			boolean status = false;
			try {
				JSONObject request = new JSONObject();
				request.put("from_id", application.getUserinfo().userId);
				request.put("to_id", params[0]);
				final JSONObject response = HttpClient.SendHttpPost(UrlCons.DELETE_FRIEND.getUrl(), request);
				if (response != null) {
					status = response.getBoolean("status");
				}

			} catch (JSONException e) {
				e.printStackTrace();
				progressBar.setVisibility(View.GONE);
			}
			return status;
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			progressBar.setVisibility(View.GONE);

			if (result) {

				new FriendListAsynctask().execute();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_logout:
			application.getUserinfo().setSession(false);
			Intent i = new Intent(HomeView.this, SigninView.class);
			startActivity(i);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
