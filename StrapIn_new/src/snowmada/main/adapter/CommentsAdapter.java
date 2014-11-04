package snowmada.main.adapter;

import java.util.ArrayList;

import snowmada.main.bean.CommentBean;
import snowmada.main.view.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentsAdapter extends ArrayAdapter<CommentBean> {

	private ArrayList<CommentBean> mItems = new ArrayList<CommentBean>();
	private ViewHolder mHolder;
	private Activity activity;
	public String responseMsg;

	public CommentsAdapter(Activity activity, int textViewResourceId, ArrayList<CommentBean> items) {
		super(activity, textViewResourceId, items);
		this.mItems = items;
		this.activity = activity;
	}

	public ArrayList<CommentBean> getData() {
		return mItems;
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
			v = vi.inflate(R.layout.row_comments, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);
			mHolder.name = (TextView) v.findViewById(R.id.lbl_name);
			mHolder.comments = (TextView) v.findViewById(R.id.lbl_comments);
			mHolder.main_row_bg = (LinearLayout) v.findViewById(R.id.ll_main_row_bg);

		} else {
			mHolder = (ViewHolder) v.getTag();
		}

		if (position % 2 == 0) {
			mHolder.main_row_bg.setBackgroundColor(Color.parseColor("#3A3839"));
		} else {
			mHolder.main_row_bg.setBackgroundColor(Color.parseColor("#515151"));
		}

		final CommentBean bean = mItems.get(position);
		if (bean != null) {

			mHolder.name.setText(bean.getFirstName() + " " + bean.getLastName() + "  ");
			mHolder.comments.setText(bean.getComments());

		}
		return v;
	}

	class ViewHolder {
		public TextView name;
		public TextView comments;
		public LinearLayout main_row_bg;

	}

}
