package snowmada.main.dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import snowmada.main.model.OnMeetupSubmitListener;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DlgMeetupEdit extends Dialog implements android.view.View.OnClickListener {

	private BaseActivity base;
	private LatLng latlng;
	private Button btn_submit;
	private Button btn_cancel;
	private ImageView iv_date_picker_icon, iv_time_picker_icon;
	public TextView tv_time, tv_date, tv_name;
	private EditText et_location, et_description;
	public static final int TIME_DIALOG_ID = 999;
	public static final int DATE_DIALOG_ID = 998;
	public OnMeetupSubmitListener listener;
	public String current_selected_marker_id, name, location, desc, date1, time1;
	public Marker marker;

	public DlgMeetupEdit(BaseActivity b, Marker marker ,String current_selected_marker_id, String name, String location, String desc, String date1, String time1) {
		super(b);
		base = b;
		listener = (OnMeetupSubmitListener) base;
		this.current_selected_marker_id = current_selected_marker_id;
		this.name = name;
		this.location = location;
		this.desc = desc;
		this.date1 = date1;
		this.time1 = time1;
		this.marker = marker;

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.meetup_info_dialog);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_submit.setTypeface(base.setFont());
		btn_cancel.setTypeface(base.setFont());
		// btn_submit.setText(base.getCustomText("SUB","MIT"));
		// btn_cancel.setText(base.getCustomText("CAN", "CEL"));
		iv_date_picker_icon = (ImageView) findViewById(R.id.iv_date_picker_icon);
		iv_time_picker_icon = (ImageView) findViewById(R.id.iv_time_picker_icon);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_time.setText(time1);
		tv_date.setText(date1);

		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_name.setText(name);
		tv_name.setText(base.application.getUserinfo().first_name + " " + base.application.getUserinfo().last_name);
		et_location = (EditText) findViewById(R.id.et_location);
		et_location.setText(location);

		et_description = (EditText) findViewById(R.id.et_description);
		et_description.setText(desc);
		btn_submit.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		iv_date_picker_icon.setOnClickListener(this);
		iv_time_picker_icon.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			doSubmit();
			break;
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.iv_date_picker_icon:
			base.showDialog(DATE_DIALOG_ID);
			break;
		case R.id.iv_time_picker_icon:
			base.showDialog(TIME_DIALOG_ID);

			break;

		}

	}

	public void doSubmit() {
		dismiss();

		String _name = tv_name.getText().toString().trim();
		String _location = et_location.getText().toString().trim();
		String _description = et_description.getText().toString().trim();
		String _date = tv_date.getText().toString().trim();
		String _time = tv_time.getText().toString().trim();
		String _id = "" + current_selected_marker_id;
		String _lat = "" + marker.getPosition().latitude;
		String _lng = "" + marker.getPosition().longitude;
		if (_location.length() == 0) {
			et_location.setError("Please enter Location Name");
		} else if (_description.length() == 0) {
			et_description.setError("Please enter Description");
		} else if (_date.length() == 0) {
			tv_date.setError("Please enter Date");
		} else if (_time.length() == 0) {
			tv_time.setError("Please enter Time");
		} else {

			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date date = new Date();
				String _currentdate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
				Date currentdate = sdf.parse(_currentdate);
				Date scheduledate = sdf.parse(_date + " " + _time);
				if (scheduledate.compareTo(currentdate) < 0) {
					Toast.makeText(base, "Plesase insert a valid Date&TIme", Toast.LENGTH_LONG).show();
				} else {
					listener.onMeetUpSubmit(_id, _name, _location, _description, _time, _lat, _lng, _date, "Edit");

				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

	}
}
