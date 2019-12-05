package com.example.myapplication.Connected.AppFragments.Messages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Connected.Messenger.UserChatDetails;
import com.example.myapplication.Connected.Messenger.Chat;
import com.example.myapplication.Connected.Messenger.MessengerActivity;
import com.example.myapplication.R;
import com.example.myapplication.SignUp.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MessageFromFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    ArrayList<User> mUsers;
    FirebaseUser fuser;
    DatabaseReference reference;
    int count =0;
    private List<String>usersList;
    MessageFromAdapter m_messageFromAdapter;
    List<UserChatDetails> m_userChatDetails;
    StorageReference storageReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.messge_from_fragment,null);
        recyclerView =view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        usersList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");//get the current user
        reference.addValueEventListener(new ValueEventListener() {//get all the chats from the firebase built from reciver and sender text
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    Chat chat = Snapshot.getValue(Chat.class);
                    if(chat.getSender().equals(fuser.getEmail()))//check if the sender's email is mine
                    {
                        usersList.add(chat.getReceiver());//save in list the receiver email
                    }
                    if(chat.getReceiver().equals(fuser.getEmail()))//check if the receiver's email is mine
                    {
                        usersList.add(chat.getSender());//save in list the sender email
                    }
                }
                readChats();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }


    public void readChats() {
        mUsers = new ArrayList<>();//built array list of users that the i (the user) have "Interaction" with
        m_userChatDetails = new ArrayList<>();
        reference =FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                m_userChatDetails.clear();
                m_messageFromAdapter = new MessageFromAdapter(getContext(),m_userChatDetails);
                User user1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);//get user from firebase
                    //display 1 user from chats
                    for( int j=0;j<usersList.size();j++)//list of users emails
                    {
                        String email = usersList.get(j);
                        if(user != null && user.getM_email().equals(email))
                        {
                            if(mUsers.size() != 0)
                            {
                                for(int i=0; i< mUsers.size();i++)//avoid duplicate user list
                                {
                                    user1 =mUsers.get(i);
                                    if(!user.getM_email().equals(user1.getM_email())&&!User.lookfor(mUsers,user))
                                    {
                                        mUsers.add(user);
                                        m_userChatDetails.add(new UserChatDetails(user.getM_firstName() +" " +user.getM_lastName(),null));
                                        downloadImage(email,m_userChatDetails.size()-1);
                                        //we must add a picture in position
                                    }
                                }
                            }else if(!User.lookfor(mUsers,user))
                            {
                                mUsers.add(user);
                                m_userChatDetails.add(new UserChatDetails(user.getM_firstName() + " " + user.getM_lastName(), null));
                                downloadImage(email,m_userChatDetails.size()-1);
                            }
                        }
                    }
                }
                m_messageFromAdapter.setLisner(new MessageFromAdapter.MessageFromListner() {
                    @Override
                    public void onProfileClick(int position, View view) {
                        Intent intent = new Intent(view.getContext(), MessengerActivity.class);
                        intent.putExtra("userid",mUsers.get(position).getM_userId());
                        intent.putExtra("reciverEmail",mUsers.get(position).getM_email());
                        intent.putExtra("url",m_userChatDetails.get(position).getImageUrl());
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(m_messageFromAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void downloadImage(String email,final int position) {

        final String[] profileImageUrl = new String[1];
        StorageReference storageRef = storageReference.child("Photos/" + email + "/mainImage/image");
        storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                try {
                    profileImageUrl[0] = task.getResult().toString();
                    m_userChatDetails.get(position).setImageUrl(profileImageUrl[0]);
                    m_messageFromAdapter.notifyDataSetChanged();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

}
