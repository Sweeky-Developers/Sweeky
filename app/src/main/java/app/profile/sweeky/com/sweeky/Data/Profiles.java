package app.profile.sweeky.com.sweeky.Data;

public class Profiles {

    private String photoUrl;
    private String userName;

    //Constructors
    public Profiles() {}

    public Profiles(String photoUrl, String userName) {
        this.photoUrl = photoUrl;
        this.userName = userName;
    }


    //Getters and Setters
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
