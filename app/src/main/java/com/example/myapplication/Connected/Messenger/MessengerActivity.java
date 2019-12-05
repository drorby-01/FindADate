package com.example.myapplication.Connected.Messenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.SignUp.Files;
import com.example.myapplication.SignUp.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessengerActivity extends AppCompatActivity implements View.OnClickListener {
    CircleImageView profile;
    TextView username;
    DatabaseReference databaseReference;
    EditText text_send;
    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;
    ImageButton btn_send;
    ImageButton im_sound;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
        profile = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        text_send = findViewById(R.id.text_send);
        recyclerView = findViewById(R.id.recycler_view);
        btn_send = findViewById(R.id.btn_send);
        im_sound = findViewById(R.id.sound);
        Files files = new Files("chat", MessengerActivity.this);
        files.WriteToFile("chat","1");
        im_sound.setTag("volume_up");
        startService(new Intent(this, BackgroundMusicService.class).putExtra("start",0));
        im_sound.setOnClickListener(this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final String currentUserEmail =FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final String reciverEmail = getIntent().getStringExtra("reciverEmail");
        final String userId = getIntent().getStringExtra("userid");//recieve to id
        final String imageUrl = getIntent().getStringExtra("url");//img reciver for toolbar

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(currentUserEmail,reciverEmail,msg);
                }else {
                    Snackbar.make(findViewById(R.id.message_layout),getString(R.string.cant_send_empty_msg),Snackbar.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user =dataSnapshot.getValue(User.class);
                username.setText(user.getM_firstName()+" "+user.getM_lastName());
                Glide.with(MessengerActivity.this).load(imageUrl).fitCenter().error(R.drawable.blankprofile).into(profile);
                readMessagges(currentUserEmail,reciverEmail);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String sender_email , String receiver_email, String message)
    {
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender_email);
        hashMap.put("receiver",receiver_email);
        hashMap.put("message",message);
        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessagges(final String myEmail, final String userEmail)
    {
        mchat=new ArrayList<>();
        databaseReference =FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat =snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myEmail) && chat.getSender().equals(userEmail)||
                    chat.getReceiver().equals(userEmail) && chat.getSender().equals(myEmail))
                    {
                        mchat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessengerActivity.this,mchat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this,BackgroundMusicService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(im_sound.getTag().equals("volume_up"))
        {
            startService(new Intent(this,BackgroundMusicService.class));
        }
    }



    @Override
    public void onClick(View v) {
        if(v==im_sound)
        {
            if(im_sound.getTag().equals("volume_up"))
            {
                stopService(new Intent(this,BackgroundMusicService.class));
                im_sound.setBackgroundResource(R.drawable.play_arrow);
                im_sound.setTag("volume_off");
            }
            else if(im_sound.getTag().equals("volume_off"))
            {
                startService(new Intent(this,BackgroundMusicService.class));
                im_sound.setBackgroundResource(R.drawable.pause_black);
                im_sound.setTag("volume_up");
            }
        }
    }
}
