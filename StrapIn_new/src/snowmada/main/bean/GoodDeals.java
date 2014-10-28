package snowmada.main.bean;

import android.graphics.Bitmap;

public class GoodDeals {
    public String markerId; 
    public String id;
    public String name;
    public String advtName;
    public String address;
    public double lat;
    public double lng;
    public Bitmap image;
    public String imageUrl;
    public String description;
    
    public GoodDeals(String markerId,String id,String name,String advtName,String address,double lat,double lng,Bitmap image,String imageUrl,String description){
	this.markerId = markerId;
	this.id  = id;
	this.name  = name;
	this.advtName = advtName;
	this.address  = address;
	this.lat  = lat;
	this.lng  = lng;
	this.image = image;
	this.imageUrl = imageUrl;
	this.description = description;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAdvtName() {
        return advtName;
    }

    public void setAdvtName(String advtName) {
        this.advtName = advtName;
    }

    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    

}
