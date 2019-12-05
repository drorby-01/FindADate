package com.example.myapplication.Connected.AppFragments.Home;

import java.util.Calendar;

public class Birthdate {

    public static int AgeCalculate(String BirthDate)
    {

        int age;
        int userYear =Integer.parseInt(BirthDate.split("/")[2]);
        int userMonth =Integer.parseInt(BirthDate.split("/")[1]);
        int userDay =Integer.parseInt(BirthDate.split("/")[0]);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH)+1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        age = year - userYear -1;
        if(month > userMonth)
            age++;
        else if(month == userMonth && userDay <= day )
            age++;
        return age;

    }
}
