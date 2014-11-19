package snowmada.main.fragment;

import static snowmada.main.view.CommonUtilities.CHAT_MESSAGE;
import static snowmada.main.view.CommonUtilities.DISPLAY_CHAT_MESSAGE_ACTION;

import java.util.ArrayList;
import java.util.StringTokenizer;

import snowmada.main.adapter.ChatAdapter;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.HomeView;
import snowmada.main.view.R;
import snowmada.main.view.WakeLocker;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint("ValidFragment")
public class ChatFragment extends Fragment implements OnClickListener{
	
	public interface OnSendMessageListener{
		public void OnSendMessage(String val);
	}
	public BaseActivity base;
	public ListView listView1;
	public Button btn_send;
	private EditText et_input_box;
	private LinearLayout ll_emoji;
	private ArrayList<String> arr = new ArrayList<String>();
	String str;
	private String receiver_id;
	private String receiver_name;
	private HomeView home;
	public OnSendMessageListener listener;
	private StringTokenizer tokens;
	private ChatAdapter adapter;
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		activity.registerReceiver(mHandleChatMessageReceiver, new IntentFilter(DISPLAY_CHAT_MESSAGE_ACTION));
	}

	public ChatFragment(BaseActivity b,HomeView home, String receiver_id, String receiver_name) {
		base = b;
		this.receiver_id  = receiver_id;
		this.receiver_name = receiver_name; // Sender Name type  FirstName~LastName
		this.home = home;
		listener = (OnSendMessageListener) home;
		arr.clear();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_chat, null, false);
		listView1 = (ListView) mView.findViewById(R.id.listView1);
		btn_send = (Button) mView.findViewById(R.id.btn_send);
		et_input_box = (EditText) mView.findViewById(R.id.et_input_box);
		ll_emoji = (LinearLayout) mView.findViewById(R.id.ll_emoji);
		return mView;
	}
	
	private final BroadcastReceiver mHandleChatMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(CHAT_MESSAGE);
			 tokens = new StringTokenizer(newMessage, ":");
			String firstToken = tokens.nextToken();
			/*if (firstToken.equals("w")) {
				refrteshOnlineUserList(tokens);
			}*/ /*else if (firstToken.equals("c")) {
				addOnlineMember(tokens);
			} else if (firstToken.equals("q")) {
				removeOnlineMember(tokens);
			} *//*else*/ if (firstToken.equals("$")) {
				receiveMsg(tokens);
			}
			WakeLocker.release();
			

			WakeLocker.release();
		}
	};
	
	public void onDetach() {
		getActivity().unregisterReceiver(mHandleChatMessageReceiver);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			
			String textMsg = et_input_box.getText().toString();
			if(textMsg.length()>0){
				
				String lastmesg = "$" + ":" + receiver_id+"~"+receiver_name + ":" + textMsg;
				listener.OnSendMessage(lastmesg);
				str = base.application.getUserinfo().userId+"~"+receiver_id+"~"+receiver_name+"~"+textMsg+"~"+System.currentTimeMillis();
				arr.add(str);
				adapter = new ChatAdapter(base, R.layout.chat_row, arr);
				listView1.setAdapter(adapter);
				et_input_box.setText("");
			}
			
			break;

		}
		
	};
	
	public void receiveMsg(StringTokenizer st) {
		String ss = "";
		final String sendername = new String(tokens.nextToken());
		final String message = new String(tokens.nextToken());
		String[] s1 = sendername.split("~");
		
		str = s1[0]+"~"+ base.application.getUserinfo().userId+"~"+receiver_name+"~"+message+"~"+System.currentTimeMillis();
		
		arr.add(str);
		adapter = new ChatAdapter(base, R.layout.chat_row, arr);
		listView1.setAdapter(adapter);

	}

}
