package com.example.myapplication.SignUp;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private  String m_userId;
    private String m_email;
    private String m_genderLookFor;
    private String m_firstName;
    private String m_lastName;
    private String m_city;
    private String m_birthDate;
    private String m_aboutYourSelf;
    private  String m_password;

    public User(){

    }


    public String getM_password() {
        return m_password;
    }

    public void setM_password(String m_password) {
        this.m_password = m_password;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        boolean flag = false;
        if ( obj instanceof  User)
         flag = ((User) obj).getM_email().equals(this.getM_email());
        return flag;
    }

    public User(String m_userId, String m_email, String m_genderLookFor, String m_firstName, String m_lastName, String m_city, String m_birthDate, String m_aboutYourSelf , String password) {
        this.m_userId = m_userId;
        this.m_email = m_email;
        this.m_genderLookFor = m_genderLookFor;
        this.m_firstName = m_firstName;
        this.m_lastName = m_lastName;
        this.m_city = m_city;
        this.m_birthDate = m_birthDate;
        this.m_aboutYourSelf = m_aboutYourSelf;
        this.m_password = password;
    }

    public String getM_userId()
    {
        return this.m_userId;
    }

    public void setM_userId(String m_userId)
    {
        this.m_userId = m_userId;
    }
    public String getM_email() {
        return m_email;
    }

    public void setM_email(String m_email) {
        this.m_email = m_email;
    }

    public String getM_genderLookFor() {
        return m_genderLookFor;
    }

    public void setM_genderLookFor(String m_genderLookFor) {
        this.m_genderLookFor = m_genderLookFor;
    }

    public String getM_firstName() {
        return m_firstName;
    }

    public void setM_firstName(String m_firstName) {
        this.m_firstName = m_firstName;
    }

    public String getM_lastName() {
        return m_lastName;
    }

    public void setM_lastName(String m_lastName) {
        this.m_lastName = m_lastName;
    }

    public String getM_city() {
        return m_city;
    }

    public void setM_city(String m_city) {
        this.m_city = m_city;
    }

    public String getM_birthDate() {
        return m_birthDate;
    }

    public void setM_birthDate(String m_birthDate) {
        this.m_birthDate = m_birthDate;
    }

    public String getM_aboutYourSelf() {
        return m_aboutYourSelf;
    }

    public void setM_aboutYourSelf(String m_aboutYourSelf) {
        this.m_aboutYourSelf = m_aboutYourSelf;
    }
    public static boolean lookfor(ArrayList<User> users ,User user)
    {
        for(User user1 :users)
        {
            if(user1.getM_email().equals(user.getM_email()))
                return true;
        }
        return false;
    }
}
