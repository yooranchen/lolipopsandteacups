package snowmada.main.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.dialog.WeatherDialog;
import snowmada.main.dialog.WeatherDialog.OnStationSelectListener;
import snowmada.main.network.HttpClientGet;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class WeatherFragment extends Fragment implements OnClickListener, OnStationSelectListener {
	public BaseActivity base;
	private Button btn_select_station;
	private WeatherDialog wDlg;
	private TextView tv_selected_station;
	private Double dlat, dlng;
	private TextView tv_current_temp, tv_max_temp, tv_min_temp, tv_humidity, tv_wind_speed, tv_weather, tv_snow;
	private LinearLayout ll_weather_layout;
	private ImageView btn_refresh;
	private Animation animation;
	private boolean flag = false;
	
	private Handler handle = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				btn_refresh.clearAnimation();
				break;

		
			}
		}
	};

	@SuppressLint(value = { "ValidFragment" })
	public WeatherFragment(BaseActivity b) {
		base = b;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_weather, null, false);
		btn_select_station = (Button) mView.findViewById(R.id.btn_select_station);
		tv_selected_station = (TextView) mView.findViewById(R.id.tv_selected_station);
		tv_current_temp = (TextView) mView.findViewById(R.id.tv_current_temp);
		tv_max_temp = (TextView) mView.findViewById(R.id.tv_max_temp);
		tv_min_temp = (TextView) mView.findViewById(R.id.tv_min_temp);
		tv_humidity = (TextView) mView.findViewById(R.id.tv_humidity);
		tv_wind_speed = (TextView) mView.findViewById(R.id.tv_wind_speed);
		tv_weather = (TextView) mView.findViewById(R.id.tv_weather);
		ll_weather_layout = (LinearLayout) mView.findViewById(R.id.ll_weather_layout);
		tv_snow = (TextView) mView.findViewById(R.id.tv_snow);
		btn_refresh = (ImageView) mView.findViewById(R.id.btn_refresh);
		ll_weather_layout.setVisibility(View.GONE);
		btn_select_station.setOnClickListener(this);
		tv_selected_station.setOnClickListener(this);
		btn_refresh.setOnClickListener(this);
		animation = AnimationUtils.loadAnimation(base, R.anim.rotate_around_center_point);

		return mView;
	}

	@Override
	public void onSelectStation(String name, String lat, String lng) {

		tv_selected_station.setText("STATION: " + name);
		dlat = Double.parseDouble(lat);
		dlng = Double.parseDouble(lng);
		ll_weather_layout.setVisibility(View.VISIBLE);
		new WeatherAsync().execute();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_select_station:
			wDlg = new WeatherDialog(base, this);
			wDlg.show();
			break;
		case R.id.btn_refresh:
			if ((dlat != null) && (dlng != null)) {
				flag = true;
				btn_refresh.startAnimation(animation);
				new WeatherAsync().execute();
			} else {
				Toast.makeText(base, "Please select a station", Toast.LENGTH_LONG).show();
			}
			break;

		}
	}

	public class WeatherAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			final JSONObject json = HttpClientGet.SendHttpPost("http://api.openweathermap.org/data/2.5/weather?lat=" + dlat + "&lon=" + dlng);
			if (json != null) {
				Log.e("Wether", json.toString());

				base.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {

							String st;

							st = String.format("%1$,.1f", ((json.getJSONObject("main").getDouble("temp") - 273.0d) * 1.8d + 32.0d));
							tv_current_temp.setText("" + st + "\u00B0F");

							st = String.format("%1$,.1f", ((json.getJSONObject("main").getDouble("temp_max") - 273.0d) * 1.8d + 32.0d));
							tv_max_temp.setText("" + st + "\u00B0F");

							st = String.format("%1$,.1f", ((json.getJSONObject("main").getDouble("temp_min") - 273.0d) * 1.8d + 32.0d));
							tv_min_temp.setText("" + st + "\u00B0F");

							tv_humidity.setText("" + (json.getJSONObject("main").getInt("humidity")));

							st = String.format("%1$,.1f", json.getJSONObject("wind").getDouble("speed") * 2.2369d);
							tv_wind_speed.setText("" + st + " mph");

							JSONArray jArr = json.getJSONArray("weather");
							String w = "";
							for (int i = 0; i < jArr.length(); i++) {
								JSONObject jObj = jArr.getJSONObject(i);
								if (i == 0) {
									w = jObj.getString("description");
								}
								if (i == 1) {
									w = w + " , " + jObj.getString("description");
								}
								tv_weather.setText(jObj.getString("description"));
							}

							if (json.has("snow")) {

								tv_snow.setText("" + String.format("%1$,.2f", json.getJSONObject("snow").getDouble("3h")) + " Inch");
							} else {
								tv_snow.setText("0.00 Inch");
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (flag) {
							flag = false;
							handle.sendEmptyMessageDelayed(1, 3000);
						}
					}
				});
			}

			return null;
		}

	}

}
