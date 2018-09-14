package app.profile.sweeky.com.sweeky.Data;

import java.io.Serializable;

public class UserDetails implements Serializable {

    private String photo;
    private String userName;
    private String status;
    private String userId;

    public UserDetails() {}

    public UserDetails(String photo, String userName, String status, String userId) {
        this.photo = photo;
        this.userName = userName;
        this.status = status;
        this.userId = userId;
    }


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
