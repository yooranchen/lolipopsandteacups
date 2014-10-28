package snowmada.main.adapter;

import java.util.ArrayList;

import snowmada.main.bean.ImageBean;
import snowmada.main.constant.Constant;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.ImagePagerActivity;
import snowmada.main.view.R;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class GalleryAdapter extends ArrayAdapter<ImageBean> {

	private ArrayList<ImageBean> mItems = new ArrayList<ImageBean>();
	private ViewHolder mHolder;
	private BaseActivity activity;
	public String responseMsg;

	public GalleryAdapter(BaseActivity activity, int textViewResourceId, ArrayList<ImageBean> items) {
		super(activity, textViewResourceId, items);
		this.mItems = items;
		this.activity = activity;

		for (int i = 0; i < mItems.size(); i++) {
			Log.e("TAG4", mItems.get(i).getImageLink());
		}
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
			v = vi.inflate(R.layout.row_image_gallery, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);
			mHolder.image = (ImageView) v.findViewById(R.id.iv_image_gallery);
		} else {
			mHolder = (ViewHolder) v.getTag();
		}

		mHolder.image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(activity, ImagePagerActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("position", "" + position);
				Constant.mItemsList = mItems;
				activity.startActivity(i);
			}
		});
		final ImageBean bean = mItems.get(position);
		if (bean != null) {
			activity.imageLoader.DisplayImage(bean.getImageLink(), mHolder.image);
		}
		return v;
	}

	class ViewHolder {
		public ImageView image;
	}
}
