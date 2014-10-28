package snowmada.main.application;

import java.util.ArrayList;

import snowmada.main.bean.FriendBean;
import snowmada.main.view.TrackLocation;

import com.google.android.gms.maps.GoogleMap;

import android.app.Application;

public class MyApplication extends Application {

	public UserInfo userinfo;
	public GoogleMap map;
	public ArrayList<FriendBean> friendArr;
	
	public TrackLocation track;
	
	
	public UserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
	}

	public GoogleMap getMap() {
		return map;
	}

	public void setMap(GoogleMap map) {
		this.map = map;
	}

	public ArrayList<FriendBean> getFriendArr() {
		return friendArr;
	}

	public void setFriendArr(ArrayList<FriendBean> friendArr) {
		this.friendArr = friendArr;
	}

	public TrackLocation getTrack() {
		return track;
	}

	public void setTrack(TrackLocation track) {
		this.track = track;
	}
	
	
	
	

}
