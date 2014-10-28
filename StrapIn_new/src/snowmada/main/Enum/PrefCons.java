package snowmada.main.Enum;

public enum PrefCons {

	GLOBAL_SETTINGS("GLOBAL_SETTINGS"),
	FIRST_NAME("FIRST_NAME"),
	LAST_NAME("LAST_NAME"),
	USER_ID("USER_ID"),
	PROFILE_IMAGE("PROFILE_IMAGE"),
	SESSION("SESSION"),
	ZOOM("ZOOM"),
	DOB("DOB"),
	SKILAUNCH("SKYLAUNCH"),
	SKINAME("SKINAME"),
	SKIID("SKIID");

	public String url;

	PrefCons(String ob) {
		this.url = ob;
	}

	public String getPrefName() {
		return url;
	}

}
