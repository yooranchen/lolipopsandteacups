package snowmada.main.dialog;

import java.util.ArrayList;

import snowmada.main.adapter.StationAdapter;
import snowmada.main.adapter.StationAdapter.OnStationSelect;
import snowmada.main.fragment.WeatherFragment;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

public class WeatherDialog extends Dialog implements OnStationSelect {

	public interface OnStationSelectListener {
		public void onSelectStation(String name, String lat, String lng);
	}

	public BaseActivity base;
	public ArrayList<String> Arr = new ArrayList<String>();
	public WeatherFragment frag;
	public ListView listView1;
	private StationAdapter adapter;

	public OnStationSelectListener listenr;

	public WeatherDialog(BaseActivity base, WeatherFragment frag) {
		super(base);
		this.base = base;
		this.frag = frag;
		Arr.add("39.5965645~-105.9320236~Keystone");
		Arr.add("39.480466~-106.067124~Breckenridge");
		Arr.add("39.6334609~-106.3602599~Vail");
		Arr.add("39.642312~-105.871685~Arapahoe Basin");
		Arr.add("39.5965645~-106.5146821~Beaver Creak");
		Arr.add("39.223915~-106.9432269~Snowmass");
		Arr.add("39.1984814~-106.8367246~Aspen");
		Arr.add("37.630872~-107.814355~Purgatory");
		Arr.add("38.8870945~-106.9310609~Crested Butte");
		Arr.add("37.8095047~-107.6454166~Silverton");
		Arr.add("39.478525~-106.160554~Copper Mountain");
		Arr.add("39.8808022~-105.7548091~Winter Park");
		Arr.add("40.4840386~-106.8335065~Steamboat Springs");
		listenr = (OnStationSelectListener) frag;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dlg_weather);
		listView1 = (ListView)findViewById(R.id.listView1);
		adapter = new StationAdapter(base, this, R.layout.station_row, Arr);
		listView1.setAdapter(adapter);

	}

	@Override
	public void onStationSelect(String name, String lat, String lng) {
		//Toast.makeText(base, name, 1000).show();

		listenr.onSelectStation(name, lat, lng);
		dismiss();
	}

}
