package com.example.myapplication.Connected.AppFragments.Settings;

public class Settings {
    String text;
    String textinformation;

    public Settings(String text, String textinformation) {
        this.text = text;
        this.textinformation = textinformation;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextinformation() {
        return textinformation;
    }

    public void setTextinformation(String textinformation) {
        this.textinformation = textinformation;
    }
}
