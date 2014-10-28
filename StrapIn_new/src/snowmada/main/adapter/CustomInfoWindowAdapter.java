package snowmada.main.adapter;

import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements InfoWindowAdapter {
	private View view;
	private BaseActivity base;
	private TextView tv_name,tv_snippet;

	public CustomInfoWindowAdapter(BaseActivity b) {
		base = b;
	    view = base.getLayoutInflater().inflate(R.layout.custom_info_window,   null);
	}	
	public View getInfoContents(Marker marker) {
	    if (base.marker != null && base.marker.isInfoWindowShown()) {
	    	base.marker.hideInfoWindow();
	    	base.marker.showInfoWindow();
	    }
	    return null;
	}
	public View getInfoWindow(final Marker marker) {
		base.marker = marker;
	    tv_name = ((TextView) view  .findViewById(R.id.tv_name));	    
	    tv_snippet = ((TextView) view  .findViewById(R.id.tv_snippet));
	    tv_name.setText(marker.getTitle());
	    tv_snippet.setText(marker.getSnippet());
	    
	    return view;
	}
   }