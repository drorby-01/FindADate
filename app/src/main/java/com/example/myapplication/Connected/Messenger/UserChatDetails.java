package com.example.myapplication.Connected.Messenger;

public class UserChatDetails {

    private String username;
    private String imageUrl;

    public UserChatDetails(String username, String imageUrl) {
        this.username = username;
        this.imageUrl = imageUrl;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
