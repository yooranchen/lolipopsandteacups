package snowmada.main.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import snowmada.main.Util.ImageLoader;
import snowmada.main.application.MyApplication;
import snowmada.main.application.UserInfo;
import snowmada.main.bean.CustomDialogInterface;
import snowmada.main.bean.GoodDeals;
import snowmada.main.dialog.DlgFriend.OnFriendDialogListener;
import snowmada.main.fragment.DealFragment.setDealsOnMapListener;
import snowmada.main.model.OnMeetupSubmitListener;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class BaseActivity extends FragmentActivity implements OnClickListener, OnFriendDialogListener, OnMapLongClickListener, OnMeetupSubmitListener, setDealsOnMapListener, CustomDialogInterface , OnInfoWindowClickListener,OnMarkerClickListener, OnMapClickListener{
	public MyApplication application;
	public ImageLoader imageLoader;
	public SlidingMenu slidingmenu;
	public Marker marker;
	private ProgressDialog mProgressDialog = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (MyApplication) getApplication();
		application.setUserinfo(new UserInfo(this));
		imageLoader = new ImageLoader(this);
		slidingmenu = new SlidingMenu(this);
	}

	public Spanned getCustomText(String s1, String s2) {
		return Html.fromHtml("<font color=\"#ffffff\">" + s1 + "</font>&nbsp;&nbsp;<font color=\"#ff0000\">" + s2 + "</font>");
	}

	public void onClick(View v) {
	}

	public boolean isNetworkConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public void showMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	public void showProgressBar() {
		runOnUiThread(new Runnable() {
			public void run() {
				mProgressDialog = new ProgressDialog(BaseActivity.this);
				mProgressDialog.setMessage("Please wait...");
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.setIndeterminate(true);
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
			}
		});
	}

	public void dismissProgressBar() {
		runOnUiThread(new Runnable() {
			public void run() {
				mProgressDialog.cancel();
			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	public long getCurrentDate() {
		try {
			return getSDF().parse(getSDF().format(new Date())).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public SimpleDateFormat getSDF() {
		return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	}

	public long getExpireTime(String date, String time) {
		try {
			return ((getSDF().parse(date + " " + time).getTime()) + 3600000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public long setMeetUpTime(String date, String time) {
		try {
			return ((getSDF().parse(date + " " + time).getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Typeface setFont() {
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/aircruiser.ttf");
		return custom_font;
	}

	public void onTack(String id) {
	}

	public void onViewProfile(String id) {
	}

	public void onChat(String id) {
	}

	public void onDelete(String id) {
	}

	public void onMapLongClick(LatLng arg0) {
	}

	public void onMeetUpSubmit(String id, String name, String location, String description, String time, String lat, String lng, String meetupdate) {
	}

	public void loadDealsonMap(ArrayList<GoodDeals> mDealsArr) {
	}

	public void OnEmergencyConformDlg() {
	}

	
	public void onInfoWindowClick(Marker arg0) {	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		return false;
	}

	@Override
	public void onMeetUpSubmit(String id, String name, String location, String description, String time, String lat, String lng, String meetupdate, String action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapClick(LatLng arg0) {
		
		
	}
}
