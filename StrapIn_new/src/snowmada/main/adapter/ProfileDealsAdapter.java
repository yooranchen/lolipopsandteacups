package snowmada.main.adapter;

import java.util.ArrayList;

import snowmada.main.bean.ProfileDealsBean;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ProfileDealsAdapter extends ArrayAdapter<ProfileDealsBean>{
	
	private ArrayList<ProfileDealsBean> mItems = new ArrayList<ProfileDealsBean>();
	private ViewHolder mHolder;
	private BaseActivity activity;
	public String responseMsg;
	
	public ProfileDealsAdapter(BaseActivity activity, int textViewResourceId,	ArrayList<ProfileDealsBean> items) {
		super(activity, textViewResourceId, items);
		this.mItems = items;
		this.activity =activity;	
		Log.e("Gallery", "Gallery");
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
	public View getView( final int position,  View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_deals, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);	
			mHolder.image = (ImageView)v.findViewById(R.id.iv_deals_image);
			
				
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}			
		final ProfileDealsBean bean = mItems.get(position);
		if(bean != null){
			activity.imageLoader.DisplayImage(bean.getDealsImage(),mHolder.image);
			
			
		}		
		return v;
	}
	class ViewHolder {	
		public ImageView image;
							
	}
	 
}
