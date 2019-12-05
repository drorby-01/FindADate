package com.example.myapplication.Connected.AppFragments.Profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Connected.AppFragments.SearchDateActivity;
import com.example.myapplication.R;
import com.example.myapplication.SignUp.Cities;
import com.example.myapplication.SignUp.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText et_firstname,et_lastname,et_birthdate,et_about_yourself;
    AutoCompleteTextView et_city;
    Button btn_save;
    Cities cities;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;
    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseUser;
    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_activity);
        et_firstname = findViewById(R.id.et_firstName);
        et_lastname = findViewById(R.id.et_lastName);
        et_city = findViewById(R.id.city);
        et_birthdate = findViewById(R.id.et_birthdate);
        et_about_yourself = findViewById(R.id.et_aboutYourSelf);
        btn_save = findViewById(R.id.btn_save);
        User user = (User) getIntent().getSerializableExtra("userinformation");
        ArrayList<String>cities1 = new ArrayList<>();
        cities = new Cities(UpdateProfileActivity.this,cities1);
        cities.loadCities();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cities.getCities());
        et_city.setAdapter(adapter);
        et_firstname.setText(user.getM_firstName());
        et_lastname.setText(user.getM_lastName());
        et_city.setText(user.getM_city());
        et_birthdate.setText(user.getM_birthDate());
        et_about_yourself.setText(user.getM_aboutYourSelf());
        databaseUser = FirebaseDatabase.getInstance().getReference("Users").child(user.getM_userId());


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkField()) {
                    User user1 = (User) getIntent().getSerializableExtra("userinformation");
                    User user2 = new User(user1.getM_userId(), user1.getM_email(), user1.getM_genderLookFor(), et_firstname.getText().toString(), et_lastname.getText().toString(), et_city.getText().toString(), et_birthdate.getText().toString(), et_about_yourself.getText().toString(), user1.getM_password());
                    databaseUser.setValue(user2);
                    Intent intent = new Intent(UpdateProfileActivity.this, SearchDateActivity.class);
                    startActivity(intent);
                }
            }
        });


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

    }

    public void birthdate(View view) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR)-18;
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                et_birthdate.setText(dayOfMonth+ "/" + (month+1)+ "/" +year);
            }
        },year,month,day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public boolean checkField()
    {
        boolean flag = true;
        if(!cities.getCities().contains(et_city.getText().toString()))
        {
            et_city.setError(getString(R.string.cityerror));
            flag = false;
        }
        if(et_about_yourself.getText().toString().isEmpty())
        {
            et_about_yourself.setError(getString(R.string.yourselferror));
            flag = false;
        }
        if(et_firstname.getText().toString().isEmpty())
        {
            et_firstname.setError(getString(R.string.firstnameerror));
            flag = false;
        }
        if(et_lastname.getText().toString().isEmpty())
        {
            et_lastname.setError(getString(R.string.lastnameerror));
            flag = false;
        }
        if(et_birthdate.getText().toString().isEmpty())
        {
            et_birthdate.setError(getString(R.string.birthdate));
            flag = false;
        }
        return flag;
    }

}