package snowmada.main.model;

public interface OnMeetupSubmitListener{
	public void onMeetUpSubmit(String id,String name,String location ,String description, String time, String lat, String lng, String meetupdate, String action);
}
