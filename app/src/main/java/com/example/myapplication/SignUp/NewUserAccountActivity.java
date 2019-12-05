package com.example.myapplication.SignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewUserAccountActivity extends AppCompatActivity {

    EditText e_password, e_username, e_email;
    Button b_next, b_back;
    DatabaseReference database;
    ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_account);
        e_email = findViewById(R.id.email_edit);
        e_username = findViewById(R.id.user_edit);
        e_password = findViewById(R.id.password_edit);
        b_next = findViewById(R.id.next_btn);
        b_back = findViewById(R.id.back_btn);
        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        b_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e_email.getText().toString();
                String username = e_username.getText().toString();
                String password = e_password.getText().toString();
                if (CheckField()) {
                    Files files = new Files("emailAndPassword", NewUserAccountActivity.this);
                    files.WriteToFile("emailpassword", email + "," + username + "," + password);
                    Intent intent = new Intent(NewUserAccountActivity.this, LookingForActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
         users = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference("Users");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting Users
                    User user = postSnapshot.getValue(User.class);
                    //geting all users
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public boolean emailCorrect(String email) {
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean CheckField() {
        boolean flag = true;
        String email = e_email.getText().toString();
        String username = e_username.getText().toString();
        String password = e_password.getText().toString();
        if (!emailCorrect(email)) {
            e_email.setError(getString(R.string.properemail));
            flag = false;
        }
        for(User user:users)
        {
            if(email.equals(user.getM_email())) {
                e_email.setError(getString(R.string.email_already_exist));
                flag = false;
                break;
            }
        }
        if (username.isEmpty()) {
            e_username.setError(getString(R.string.nameerror));
            flag = false;
        }
        if (password.length() < 8) {
            e_password.setError(getString(R.string.passworderror));
            flag = false;
        }

        return flag;
    }
}


