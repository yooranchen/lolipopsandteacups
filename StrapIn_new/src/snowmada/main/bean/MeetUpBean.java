package snowmada.main.bean;

public class MeetUpBean {
	public String id;
	public String name;
	public String location;
	public String description;
	public String date1;
	public String time;
	public String owner;
	public double lat;
	public double lng;
	
	public MeetUpBean(String id,String name, String location,String desc,String date1,String time,String owner,double lat, double lng){
		this.id = id;
		this.name = name;
		this.location = location;
		this.description = desc;
		this.date1  = date1;
		this.time = time;
		this.owner = owner;
		this.lat  = lat;
		this.lng = lng;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}
	
	

}
