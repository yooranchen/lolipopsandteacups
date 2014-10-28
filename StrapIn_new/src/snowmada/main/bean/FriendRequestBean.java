package snowmada.main.bean;

public class FriendRequestBean {

	private String fromId;
	private String toId;
	private String name;

	public FriendRequestBean(String fromId, String toId, String name) {
		super();
		this.fromId = fromId;
		this.toId = toId;
		this.name = name;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
