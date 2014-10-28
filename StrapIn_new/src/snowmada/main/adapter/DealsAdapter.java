package snowmada.main.adapter;

import java.util.ArrayList;

import snowmada.main.bean.GoodDeals;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.DealsView;
import snowmada.main.view.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DealsAdapter extends ArrayAdapter<GoodDeals>{
    private BaseActivity activity;
	private ArrayList<GoodDeals> mItems = new ArrayList<GoodDeals>();
	private ViewHolder mHolder;
	int size = 0;
	
	public DealsAdapter(BaseActivity base, int textViewResourceId,	ArrayList<GoodDeals> items) {
		super(base, textViewResourceId, items);
		this.activity = base;
		this.mItems = items;
		size = mItems.size();
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
			v = vi.inflate(R.layout.deals_row, null);
			mHolder = new ViewHolder();
			mHolder.mMainBg = (LinearLayout)v.findViewById(R.id.main_bg);
			v.setTag(mHolder);
			mHolder.user_image = (ImageView)v.findViewById(R.id.user_image);
			mHolder.lbl_product_name = (TextView)v.findViewById(R.id.lbl_product_name);
			mHolder.lbl_address =(TextView)v.findViewById(R.id.lbl_address);
			mHolder.lbl_description =(TextView)v.findViewById(R.id.lbl_description);
			mHolder.lbl_price = (TextView)v.findViewById(R.id.lbl_price);
			mHolder.lbl_discount = (TextView)v.findViewById(R.id.lbl_discount);
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}	
		
		/*if(position%2 ==0){
			mHolder.mMainBg.setBackgroundResource(R.drawable.black_1);
			
		}else{
			mHolder.mMainBg.setBackgroundResource(R.drawable.black_2);
		}*/
		mHolder.mMainBg.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
			Intent i = new Intent(activity, DealsView.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
				Bundle bundle = new Bundle();
				bundle.putString("advt_id", mItems.get(position).getId());
				bundle.putString("name", mItems.get(position).getName());
				bundle.putString("AdvtName", mItems.get(position).getAdvtName());
				bundle.putString("address", mItems.get(position).getAddress());
				bundle.putString("descriptin", mItems.get(position).getDescription());
				bundle.putString("advt_image", mItems.get(position).getImageUrl());
				
			    i.putExtras(bundle);
			    activity.startActivity(i);
		    }
		});
			
		final GoodDeals bean = mItems.get(position);
		if(bean != null){
			activity.imageLoader.DisplayImage(bean.getImageUrl(), mHolder.user_image);
		    mHolder.lbl_product_name.setText(bean.getAdvtName());
		    mHolder.lbl_address.setText(bean.getAddress());
		    mHolder.lbl_description.setText(bean.getDescription());
		    mHolder.lbl_price.setText("$100");
		    mHolder.lbl_discount.setText("$5");
		}		
		return v;
	}
	class ViewHolder {	
			
		public LinearLayout mMainBg;
		public ImageView user_image;
		public TextView lbl_product_name;
		public TextView lbl_address;
		public TextView lbl_description;
		public TextView lbl_price;
		public TextView lbl_discount;
	}
}
