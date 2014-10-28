package snowmada.main.application;

import snowmada.main.Enum.PrefCons;
import snowmada.main.view.BaseActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserInfo {
	
	public SharedPreferences sharedPreferences;
	public String first_name = null;
	public String last_name = null;
	public String userId = null;
	public String skiId = null;
	public boolean ski_launch = false;
	public String ski_name = null;
	public String profile_image = null;
	public boolean session = false;
	public boolean isZoom = false;
	public String dob = null;
	
	public UserInfo(BaseActivity base) {
		sharedPreferences = base.getSharedPreferences(PrefCons.GLOBAL_SETTINGS.getPrefName(), Context.MODE_PRIVATE);
		first_name = sharedPreferences.getString(PrefCons.FIRST_NAME.getPrefName(), first_name);
		last_name = sharedPreferences.getString(PrefCons.LAST_NAME.getPrefName(), last_name);
		userId = sharedPreferences.getString(PrefCons.USER_ID.getPrefName(), userId);
		skiId = sharedPreferences.getString(PrefCons.SKIID.getPrefName(), skiId);
		ski_launch = sharedPreferences.getBoolean(PrefCons.SKILAUNCH.getPrefName(), ski_launch);
		ski_name = sharedPreferences.getString(PrefCons.SKINAME.getPrefName(), ski_name);
		profile_image = sharedPreferences.getString(PrefCons.PROFILE_IMAGE.getPrefName(), profile_image);
		session = sharedPreferences.getBoolean(PrefCons.SESSION.getPrefName(), session);
		isZoom = sharedPreferences.getBoolean(PrefCons.ZOOM.getPrefName(), isZoom);
		dob = sharedPreferences.getString(PrefCons.DOB.getPrefName(), dob);
	}
	public void setUser(String first_name, String last_name, String userId, String profile_image,String dob){
		this.first_name = first_name;
		this.last_name = last_name;
		this.userId = userId;
		this.profile_image = profile_image;
		this.dob = dob;
		Editor edit = sharedPreferences.edit();
		edit.putString(PrefCons.FIRST_NAME.getPrefName(), first_name);
		edit.putString(PrefCons.LAST_NAME.getPrefName(), last_name);
		edit.putString(PrefCons.USER_ID.getPrefName(), userId);
		edit.putString(PrefCons.PROFILE_IMAGE.getPrefName(), profile_image);
		edit.putString(PrefCons.DOB.getPrefName(), dob);
		edit.commit();
	}
	
	public void setSession(boolean flg){
		session = flg;
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(PrefCons.SESSION.getPrefName(), session);
		edit.commit();
	}
	
	public void setZoom(boolean flg){
		isZoom = flg;
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(PrefCons.ZOOM.getPrefName(), flg);
		edit.commit();
	}
	
	public void setSkiName(String skiname){
		ski_name = skiname;
		Editor edit = sharedPreferences.edit();
		edit.putString(PrefCons.SKINAME.getPrefName(), ski_name);
		edit.commit();
	}
	
	public void setSKiLaunch(boolean flg){
		ski_launch = flg;
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(PrefCons.SKILAUNCH.getPrefName(), ski_launch);
		edit.commit();
	}
	
	public void setSkiId(String ski_Id){
		skiId = ski_Id;
		Editor edit = sharedPreferences.edit();
		edit.putString(PrefCons.SKIID.getPrefName(), skiId);
		edit.commit();
	}
	
	
	
}
