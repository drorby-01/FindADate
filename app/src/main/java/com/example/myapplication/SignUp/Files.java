package com.example.myapplication.SignUp;

import android.content.Context;
import android.content.SharedPreferences;

public class Files {

     String openfile;
     Context context;


    public Files(String openFile,Context context) {

        this.context = context;
        this.openfile = openFile;

    }

    public  void WriteToFile(String tag,String information)
    {
        SharedPreferences sp = context.getSharedPreferences(openfile,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString(tag,information);
        editor.commit();
    }

    public  String ReadFromFile(String TAG)
    {
        SharedPreferences sp = context.getSharedPreferences(openfile,Context.MODE_PRIVATE);
        return sp.getString(TAG,"");
    }


}
