package snowmada.main.bean;

import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class DlgSkiPatrolConfirmation extends Dialog implements android.view.View.OnClickListener{

    private BaseActivity base = null;
    private Button btn_ok = null, btn_cancel = null;
    private CustomDialogInterface listener;
    
    public DlgSkiPatrolConfirmation(BaseActivity b,CustomDialogInterface l) {
	super(b);
	base = b;
      listener = l;
    }   
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	setContentView(R.layout.emergency_conf_dialog);
	setCancelable(true);
	btn_ok = (Button) findViewById(R.id.btn_ok);
	btn_cancel = (Button)findViewById(R.id.btn_cancel);
	btn_ok.setTypeface(base.setFont());
	btn_cancel.setTypeface(base.setFont());
	btn_ok.setOnClickListener(this);
	btn_cancel.setOnClickListener(this);
    }
   
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.btn_ok:
	    dismiss();
	    listener.OnEmergencyConformDlg();
	    break;	    
	case R.id.btn_cancel:	
	    dismiss();
	    break;
	}
	
    }

}
