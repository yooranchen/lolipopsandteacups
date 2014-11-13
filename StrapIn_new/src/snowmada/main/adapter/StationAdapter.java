package snowmada.main.adapter;

import java.util.ArrayList;

import snowmada.main.dialog.WeatherDialog;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StationAdapter extends ArrayAdapter<String> {
	
	
	public interface OnStationSelect{
		public void onStationSelect(String name, String lat, String lng);
	}
	private BaseActivity activity;
	private ArrayList<String> mItems = new ArrayList<String>();
	private ViewHolder mHolder;
	String st[],st1[];
	WeatherDialog dlg;
	public OnStationSelect listener;
	

	public StationAdapter(BaseActivity base, WeatherDialog dlg,int textViewResourceId, ArrayList<String> items) {
		super(base, textViewResourceId, items);
		this.activity = base;
		this.mItems = items;
		this.dlg = dlg;
		listener = (OnStationSelect)dlg;
		
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
			v = vi.inflate(R.layout.station_row, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);
			mHolder.tv_station = (TextView) v.findViewById(R.id.tv_station_name);
		} else {
			mHolder = (ViewHolder) v.getTag();
		}
		st = mItems.get(position).split("~");
		mHolder.tv_station.setText(st[2]);
		if(position%2 == 0){
			mHolder.tv_station.setBackgroundColor(Color.parseColor("#616161"));
		}else{
			mHolder.tv_station.setBackgroundColor(Color.parseColor("#505050"));
		}
		mHolder.tv_station.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				st1 = mItems.get(position).split("~");
				listener.onStationSelect(st1[2], st1[0], st1[1]);
				
			}
		});

		return v;
	}

	class ViewHolder {

		public TextView tv_station;
	}
}
