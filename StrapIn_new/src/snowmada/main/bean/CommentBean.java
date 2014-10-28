package snowmada.main.bean;

public class CommentBean {

    
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String comments;

    public CommentBean(String firstName, String lastName,String profilePicture, String comments) {
	this.firstName = firstName;
	this.lastName = lastName;
	this.profilePicture = profilePicture;
	this.comments = comments;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    

}
