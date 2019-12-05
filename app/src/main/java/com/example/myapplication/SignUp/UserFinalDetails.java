package com.example.myapplication.SignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.myapplication.LoadingScreenActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class UserFinalDetails extends AppCompatActivity {


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;
    ArrayList<String>city = new ArrayList<>();
    EditText et_firstname,et_lastname,et_birthdate,et_about_your_self;
    AutoCompleteTextView ac_city;
    Button btn_next;
    DatabaseReference databaseUser;
    FirebaseDatabase database;
    LinearLayout linearLayout;
    Cities cities;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_final_details);
        et_firstname = findViewById(R.id.et_firstName);
        et_lastname = findViewById(R.id.et_lastName);
        et_birthdate = findViewById(R.id.et_birthdate);
        et_about_your_self = findViewById(R.id.et_aboutYourSelf);
        ac_city = findViewById(R.id.city);
        btn_next = findViewById(R.id.btn_next);
        database = FirebaseDatabase.getInstance();
        databaseUser = database.getReference("Users");
        linearLayout = findViewById(R.id.line1);
        cities = new Cities(UserFinalDetails.this,city);
        cities.loadCities();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cities.getCities());
        ac_city.setAdapter(adapter);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    Files files = new Files("emailAndPassword",UserFinalDetails.this);
                    String emailAndpassword=files.ReadFromFile("emailpassword");
                    String email= emailAndpassword.split(",")[0];
                    String password = emailAndpassword.split(",")[2];

                    if(checkField()) {
                        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful())
                                {
                                    Snackbar.make(linearLayout,"The data was successfully saved",Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                        AddUser();
                        AlertDialog.Builder builder=new AlertDialog.Builder(UserFinalDetails.this);
                        View view = LayoutInflater.from(UserFinalDetails.this).inflate(R.layout.alert_dialog_sign_up_finished,null);
                        builder.setView(view);
                        builder.setCancelable(false);
                        final AlertDialog dialog = builder.create();
                        Button btn_continue = view.findViewById(R.id.btn_continue);
                        btn_continue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(UserFinalDetails.this, LoadingScreenActivity.class);
                                startActivity(intent);
                                finishAffinity();
                                dialog.dismiss();
                            }
                        });dialog.show();

                    }
                }
            }
        });
    }

    public void birthdate(View view) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR)-18;
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(UserFinalDetails.this, new DatePickerDialog.OnDateSetListener() {
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

        if(et_about_your_self.getText().toString().isEmpty()) {
            et_about_your_self.setError(getString(R.string.yourselferror));
            flag = false;
        }
        if(et_birthdate.getText().toString().isEmpty())
        {
            et_birthdate.setError(getString(R.string.birthdateerror));
            flag =false;
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
        if(!cities.getCities().contains(ac_city.getText().toString()))
        {
            ac_city.setError(getString(R.string.cityerror));
            flag = false;
        }
        return flag;
    }

    public void AddUser()
    {
        String city = ac_city.getText().toString();
        String lastName = et_lastname.getText().toString();
        String firstName = et_firstname.getText().toString();
        String birthDate = et_birthdate.getText().toString();
        String aboutYourself = et_about_your_self.getText().toString();
        Files files = new Files("emailAndPassword",UserFinalDetails.this);
        String email=files.ReadFromFile("emailpassword").split(",")[0];
        String password = files.ReadFromFile("emailpassword").split(",")[2];
        Files files1 = new Files("genderlook",UserFinalDetails.this);
        String genderpriority = files1.ReadFromFile("genderpriority");
        String id = databaseUser.push().getKey();
        User user = new User(id,email,genderpriority,firstName,lastName,city,birthDate,aboutYourself,password);
        assert id != null;
        databaseUser.child(id).setValue(user);

    }
}
