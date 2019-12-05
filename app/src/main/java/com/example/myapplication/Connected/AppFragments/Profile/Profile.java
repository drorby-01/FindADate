package com.example.myapplication.Connected.AppFragments.Profile;

import java.io.Serializable;

public class Profile implements Serializable {

    private String name_age_city;
    private String about_user_details;
    private String email;
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String profile) {
        this.photo = profile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Profile(String name_age_city, String about_user_details, String email, String photo) {
        this.name_age_city = name_age_city;
        this.about_user_details = about_user_details;
        this.email = email;
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public String getName_age_city() {
        return name_age_city;
    }

    public void setName_age_city(String name_age_city) {
        this.name_age_city = name_age_city;
    }

    public String getAbout_user_details() {
        return about_user_details;
    }

    public void setAbout_user_details(String about_user_details) {
        this.about_user_details = about_user_details;
    }



}
