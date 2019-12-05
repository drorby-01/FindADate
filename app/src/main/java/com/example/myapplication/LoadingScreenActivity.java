package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.Connected.AppFragments.SearchDateActivity;
import com.example.myapplication.SignUp.Files;
import com.example.myapplication.SignUp.NewUserAccountActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoadingScreenActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;
    Button emailBtn;
    TextView userExsitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        emailBtn = findViewById(R.id.btnemail);
        userExsitText = findViewById(R.id.exist_text);

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoadingScreenActivity.this, NewUserAccountActivity.class);
                startActivity(intent);
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((mobile != null && !mobile.isConnected()) && (wifi != null && !wifi.isConnected())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoadingScreenActivity.this);
            View view = getLayoutInflater().inflate(R.layout.alertdialog_network_issue, null);
            builder.setView(view);
            builder.setCancelable(false);
            final AlertDialog dialog = builder.create();
            Button button = view.findViewById(R.id.btn_network);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);

    }


    public void OpenSignInDialog(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoadingScreenActivity.this);
        View dialogView= getLayoutInflater().inflate(R.layout.alert_dialg_exsit_user,null);
        alertDialog.setView(dialogView);
        final AlertDialog dialog = alertDialog.create();
        Button BtnOk = dialogView.findViewById(R.id.btn_ok);
        final CheckBox checkBox = dialogView.findViewById(R.id.checkbox_always_connected);
        final EditText email_edit = dialogView.findViewById(R.id.edit_email);
        final EditText password_edit = dialogView.findViewById(R.id.edit_password);
        BtnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                firebaseAuth.signInWithEmailAndPassword(email_edit.getText().toString(),password_edit.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            if(checkBox.isChecked())
                            {
                                Files files = new Files("always connected",LoadingScreenActivity.this);
                                files.WriteToFile("EmailPlusPassword",email_edit.getText().toString()+","+password_edit.getText().toString());
                            }
                            Snackbar.make(findViewById(R.id.relative), "Sign in successful", Snackbar.LENGTH_SHORT).show();
                             Intent intent = new Intent(LoadingScreenActivity.this, SearchDateActivity.class);
                             startActivity(intent);
                        }else
                            Snackbar.make(findViewById(R.id.relative),"Sign in failed",Snackbar.LENGTH_SHORT).show();
                    }
                });
               dialog.dismiss();
            }
        });
          dialog.show();
    }


}
