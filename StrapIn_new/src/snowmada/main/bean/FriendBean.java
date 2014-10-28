package snowmada.main.bean;

public class FriendBean {
	private String userId;
	private String firstName;
	private String lastName;
	private boolean isTrack;
	
	public FriendBean(String userId, String firstName, String lastName,boolean isTrack) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isTrack  = isTrack;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isTrack() {
		return isTrack;
	}

	public void setTrack(boolean isTrack) {
		this.isTrack = isTrack;
	}
	
	
	
	

}
