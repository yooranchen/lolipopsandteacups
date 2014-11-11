package snowmada.main.view;

import android.os.Bundle;

public class TempActivity extends BaseActivity{

	boolean flag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle bundle  = getIntent().getExtras();
		if(bundle!=null){
			try{
			flag = bundle.getBoolean("value");
			if(flag){
				TrackLocation.createInstance(TempActivity.this);
			}else{
				TrackLocation.createInstance(TempActivity.this).removeLocationUpdate();
			}
			}catch (Exception e){
				e.printStackTrace();
			}
			
			finish();
		}
	}
	
	

}
