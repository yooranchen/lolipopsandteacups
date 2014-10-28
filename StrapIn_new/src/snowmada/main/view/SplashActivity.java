package snowmada.main.view;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

public class SplashActivity extends BaseActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "snowmada.main.view", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.e("Your Tag", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }
		if (!application.getUserinfo().session) {
			doWait();
		}

	}

	public void doWait() {
		Thread t = new Thread() {
			public void run() {
				try {
					sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					startActivity(new Intent(SplashActivity.this, SigninView.class));
					SplashActivity.this.finish();
				}
			}
		};
		t.start();
	}

	protected void onResume() {
		super.onResume();
		if (application.getUserinfo().session) {
			startActivity(new Intent(SplashActivity.this, HomeView.class));
			SplashActivity.this.finish();
		}
	}

}