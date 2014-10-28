package snowmada.main.bean;

import java.util.ArrayList;

public class ImageBean{

	public String imageId;
	public String imageLink;
	public ArrayList<CommentBean> commentArr = new ArrayList<CommentBean>();
	
	public ImageBean(String imageId, String imagelink){
		this.imageId = imageId;
		this.imageLink = imagelink;
	}
	public ImageBean(){
	    
	}

	public String getImageId(){
		return imageId;
	}

	public void setImageId(String imageId){
		this.imageId = imageId;
	}

	public String getImageLink(){
		return imageLink;
	}

	public void setImageLink(String imageLink){
		this.imageLink = imageLink;
	}
	public ArrayList<CommentBean> getCommentArr() {
	    return commentArr;
	}
	public void setCommentArr(ArrayList<CommentBean> commentArr) {
	    this.commentArr = commentArr;
	}
	
	
}
