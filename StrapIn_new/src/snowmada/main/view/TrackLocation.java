package snowmada.main.view;

import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.Enum.UrlCons;
import snowmada.main.network.HttpClient;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

public class TrackLocation implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
	private static TrackLocation sInstance;
	private static LocationClient mLocationClient;
	private static LocationRequest mLocationRequest;
	private boolean isUploadFinished = true;
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	public static final int MILLISECONDS_PER_SECOND = 1000;
	public static final int UPDATE_INTERVAL_IN_SECONDS = 2;
	public static final int FAST_CEILING_IN_SECONDS = 1;
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 500;/*MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS*/;
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;
	private static BaseActivity base;
	public static String TAG = "snomada";
	private Double lat, lng;

	private TrackLocation(BaseActivity b) {
		base = b;

	}

	public static TrackLocation createInstance(BaseActivity b) {

		if (sInstance == null) {
			sInstance = new TrackLocation(b);
			open();
		}

		return sInstance;

	}

	private static void open() {
		
		try{

		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		mLocationClient = new LocationClient(base, sInstance, sInstance);
		mLocationClient.connect();
		if (mLocationClient.isConnected()) {
			mLocationClient.removeLocationUpdates(sInstance);
			mLocationClient.requestLocationUpdates(mLocationRequest, sInstance);

		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void removeLocationUpdate() {
		if(mLocationClient!=null){
			mLocationClient.removeLocationUpdates(this);
			sInstance = null;
		}
		
	}

	public void requestUpdate() {
		mLocationClient.requestLocationUpdates(mLocationRequest, sInstance);
	}

	public void onConnectionFailed(ConnectionResult result) {
	}

	public void onConnected(Bundle connectionHint) {
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	public void onDisconnected() {
	}

	public void onLocationChanged(Location location) {
		lat = location.getLatitude();
		lng = location.getLongitude();
		//Toast.makeText(base, "" + lat, 1000).show();

		if (base.application.getUserinfo().isZoom) {
			base.application.getUserinfo().setZoom(false);
			base.application.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
			removeLocationUpdate();
		}
		if (isUploadFinished) {
			if (base.isNetworkConnected()) {
				isUploadFinished = false;
				sendLocation();
			}
		}

	}

	public void sendLocation() {
		Thread t = new Thread() {
			public void run() {
				JSONObject req = new JSONObject();
				try {
					req.put("user_id", base.application.getUserinfo().userId);
					req.put("lat", lat);
					req.put("lng", lng);
					Log.i(TAG, "Location Request==>" + req.toString());

					JSONObject response = HttpClient.SendHttpPost(UrlCons.SEND_CURRENT_LOCATION.getUrl(), req);
					if (response != null) {
						Log.i(TAG, "Location response==>" + response.toString());
						if (response.getBoolean("status")) {
							isUploadFinished = true;
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		};
		t.start();
	}

}
