package com.example.myapplication.SignUp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;

public class LookingForActivity extends AppCompatActivity implements View.OnClickListener {

    Button [] arr =new Button[4];
    public static ArrayList<Integer> arrayLastOption = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.looking_for_activity);

        arr[0] = findViewById(R.id.btn_manNman);  ////man looking for man
        arr[1] = findViewById(R.id.btn_manNwomen); ///man looking for woman
        arr[2] = findViewById(R.id.btn_womanNwoman); /// woman looking for woman
        arr[3] = findViewById(R.id.btn_womanNman);/// woman looking for man

        arr[0].setTag('0');
        arr[1].setTag('1');
        arr[2].setTag('2');
        arr[3].setTag('3');

        arr[0].setOnClickListener(this);
        arr[1].setOnClickListener(this);
        arr[2].setOnClickListener(this);
        arr[3].setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if(v.getTag().equals('0'))
        {
            arr[0].setTextColor(Color.BLACK);
            arr[0].setBackgroundColor(getResources().getColor(R.color.colorRed));
            grayButton((Character) v.getTag());
            Intent intent = new Intent(LookingForActivity.this, UserFinalDetails.class);
            startActivity(intent);
            Files files = new Files("genderlook",LookingForActivity.this);
            files.WriteToFile("genderpriority","man looking for men");
        }
        if(v.getTag().equals('1'))
        {
            arr[1].setTextColor(Color.BLACK);
            arr[1].setBackgroundColor(getResources().getColor(R.color.colorRed));
            grayButton((Character) v.getTag());
            Intent intent = new Intent(LookingForActivity.this,UserFinalDetails.class);
            startActivity(intent);
            Files files = new Files("genderlook",LookingForActivity.this);
            files.WriteToFile("genderpriority","man looking for women");
        }
        if(v.getTag().equals('2'))
        {
            arr[2].setTextColor(Color.BLACK);
            arr[2].setBackgroundColor(getResources().getColor(R.color.colorRed));
            grayButton((Character) v.getTag());
            Intent intent = new Intent(LookingForActivity.this,UserFinalDetails.class);
            startActivity(intent);
            Files files = new Files("genderlook",LookingForActivity.this);
            files.WriteToFile("genderpriority","woman looking for women");
        }
        if(v.getTag().equals('3'))
        {
            arr[3].setTextColor(Color.BLACK);
            arr[3].setBackgroundColor(getResources().getColor(R.color.colorRed));
            grayButton((Character) v.getTag());
            Intent intent = new Intent(LookingForActivity.this,UserFinalDetails.class);
            startActivity(intent);
            Files files = new Files("genderlook",LookingForActivity.this);
            files.WriteToFile("genderpriority","woman looking for men");
        }
    }
    public void grayButton(char Tag)
    {
        for(char i = '0'; i <= '3' ;i++)
        {
            if(i != Tag)
            {
            arr[Integer.parseInt(i+"")].setTextColor(Color.DKGRAY);
            arr[Integer.parseInt(i+"")].setBackgroundColor(Color.WHITE);
            }
        }
    }
}
