package snowmada.main.adapter;

import java.util.ArrayList;

import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatAdapter extends ArrayAdapter<String> {
	
	/*
	 *  Message format :   senderid~receiverid~name~message~time
	 */

	private BaseActivity activity;
	private ArrayList<String> mItems = new ArrayList<String>();
	private ViewHolder mHolder;
	private String st[];
	

	public ChatAdapter(BaseActivity activity, int textViewResourceId, ArrayList<String> mChat) {
		super(activity, textViewResourceId, mChat);
		this.activity = activity;
		mItems = mChat;

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
			v = vi.inflate(R.layout.chat_row, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);			
			
			mHolder.iv_receiver = (ImageView) v.findViewById(R.id.iv_receiver);
			mHolder.iv_sender = (ImageView) v.findViewById(R.id.iv_sender);

			mHolder.tv_receive_msg = (TextView) v.findViewById(R.id.tv_receiver_msg);
			mHolder.tv_send_msg = (TextView) v.findViewById(R.id.tv_sender_msg);
			
			mHolder.tv_receiver_name = (TextView) v.findViewById(R.id.tv_receiver_name);
			mHolder.tv_sender_name= (TextView) v.findViewById(R.id.tv_sender_name);
			
			mHolder.tv_receive_mag_time = (TextView) v.findViewById(R.id.tv_receive_mag_time);
			mHolder.tv_send_mag_time= (TextView) v.findViewById(R.id.tv_send_mag_time);

			mHolder.rl_receiver_layout = (RelativeLayout) v.findViewById(R.id.rl_receiver_layout);
			mHolder.rl_sender_layout = (RelativeLayout) v.findViewById(R.id.rl_sender_layout);

		} else {
			mHolder = (ViewHolder) v.getTag();
		}
		
		  st = mItems.get(position).split("~");
		 
		 if(st[0].equalsIgnoreCase(activity.application.getUserinfo().userId)){
			 
			 
			 
			 mHolder.rl_receiver_layout.setVisibility(View.VISIBLE);
			 mHolder.rl_sender_layout.setVisibility(View.GONE);
			 
			 activity.imageLoader.DisplayImage("https://graph.facebook.com/" + st[0] + "/picture", mHolder.iv_receiver);
			 mHolder.tv_receiver_name.setText(st[2]);
			 mHolder.tv_receive_mag_time.setText(st[3]);
			 mHolder.tv_receiver_name.setText(st[4]);
			 
			 
			
		 }else{	
			 mHolder.rl_receiver_layout.setVisibility(View.GONE);
			 mHolder.rl_sender_layout.setVisibility(View.VISIBLE);
			 
			 activity.imageLoader.DisplayImage("https://graph.facebook.com/" + st[0] + "/picture", mHolder.iv_sender);
			 mHolder.tv_sender_name.setText(st[2]);			 
			 mHolder.tv_send_mag_time.setText(st[3]);			 
			 mHolder.tv_sender_name.setText(st[4]);
			/* mHolder.rl_receiver_layout.setVisibility(View.VISIBLE);
			 mHolder.rl_sender_layout.setVisibility(View.GONE);
			 
			 activity.imageLoader.DisplayImage("https://graph.facebook.com/" + st[0] + "/picture", mHolder.iv_receiver);
			 mHolder.tv_receiver_name.setText(st[1]);
			 mHolder.tv_receive_mag_time.setText(st[2]);
			 mHolder.tv_receiver_name.setText(st[3]);*/
		 }
		

		return v;
	}

	class ViewHolder {
		public ImageView iv_receiver;
		public ImageView iv_sender;
		public TextView tv_receive_msg;
		public TextView tv_send_msg;
		public TextView tv_receiver_name;
		public TextView tv_sender_name;
		public TextView tv_receive_mag_time;
		public TextView tv_send_mag_time;
		public RelativeLayout rl_receiver_layout;
		public RelativeLayout rl_sender_layout;
	}

}
