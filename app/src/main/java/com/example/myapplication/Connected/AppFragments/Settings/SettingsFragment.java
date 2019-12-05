package com.example.myapplication.Connected.AppFragments.Settings;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.LoadingScreenActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {
    View view;
    ListView listView;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener firebaseAuthlistner;
    List<Settings>settings=new ArrayList<>();
    SettingsAdapter settingsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuthlistner= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting,null);
        listView = view.findViewById(R.id.list_item);
        settings.add(new Settings(getString(R.string.alert), null));
        settings.add(new Settings(getString(R.string.about),getString(R.string.aboutapp)));
        settings.add(new Settings(getString(R.string.logout),getString(R.string.logoutfrom)));
        settingsAdapter = new SettingsAdapter(settings);
        listView.setAdapter(settingsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {
                Settings settings = (Settings) listView.getItemAtPosition(position);
                if(settings.getText().equals(getString(R.string.logout)))
                {
                    firebaseAuth.signOut();
                    Intent intent =new Intent(view.getContext(), LoadingScreenActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finishAffinity();
                    SharedPreferences sharedPreferences =getActivity().getSharedPreferences("always connected",MODE_PRIVATE);
                    if(sharedPreferences.contains("EmailPlusPassword"))
                    sharedPreferences.edit().remove("EmailPlusPassword").commit();
                }
                if(settings.getText().equals(getString(R.string.alert)))
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                    alert.setCancelable(false);
                    View view3 = LayoutInflater.from(view.getContext()).inflate(R.layout.activity_alert,null);
                    Button btn_ok = view3.findViewById(R.id.btn_ok);
                    Button btn_cancel = view3.findViewById(R.id.cancel);
                    final Switch switch1 = view3.findViewById(R.id.switch1);
                    final AlertDialog dialog = alert.create();
                    dialog.setView(view3);
                    final AlarmManager alarmManager =(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Switchon(switch1)) {
                                Intent intent = new Intent(view.getContext(), AlertNotification.class);
                                //intent.putExtra("time", 1000 * 60 * 60 * 24);
                                intent.putExtra("time", 1000 * 5);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + /*1000*60*60*24*/1000*5, pendingIntent);
                                dialog.dismiss();
                            }
                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(view.getContext(), AlertNotification.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(),0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                            alarmManager.cancel(pendingIntent);
                            dialog.dismiss();
                        }
                    });
                        dialog.show();
                }
                if(settings.getText().equals(getString(R.string.about)))
                {
                    Intent intent = new Intent(view.getContext(), AboutAppActivity.class);
                    startActivity(intent);
                }

            }
        });
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthlistner);
    }
    public boolean Switchon(Switch switch1)
    {
        return switch1.isChecked();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthlistner);
    }
}
