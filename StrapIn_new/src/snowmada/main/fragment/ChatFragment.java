package snowmada.main.fragment;

import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class ChatFragment extends Fragment implements OnTouchListener {
	public BaseActivity base;

	public ChatFragment(BaseActivity b) {
		base = b;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_chat, null, false);
		mView.setOnTouchListener(this);
		return mView;
	}

	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

}
