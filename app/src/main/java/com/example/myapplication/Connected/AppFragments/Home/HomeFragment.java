package com.example.myapplication.Connected.AppFragments.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Connected.AppFragments.Profile.Profile;
import com.example.myapplication.R;
import com.example.myapplication.SignUp.Files;
import com.example.myapplication.SignUp.User;
import com.example.myapplication.SignUp.UserDetails;
import com.example.myapplication.Connected.Messenger.MessengerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class HomeFragment extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseUser;
    RecyclerView recyclerView;
    ArrayList<User> users = new ArrayList<>();
    List<Profile> profiles = new ArrayList<>();
    ProfileAdapter profileAdapter;
    StorageReference mStorage;
    ArrayList<User> genderSearchForArray;
    User user;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.id_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseUser = FirebaseDatabase.getInstance().getReference("Users");
        mStorage = FirebaseStorage.getInstance().getReference();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting Users
                    User user = postSnapshot.getValue(User.class);
                    //geting all users
                    users.add(user);
                }

                FirebaseUser currentUser = firebaseAuth.getCurrentUser();// get he current user
                String emailCurrentUser = null;//get the email of the current user
                if (currentUser != null) {
                    emailCurrentUser = currentUser.getEmail();
                }
                else
                {
                    Files files = new Files("always connected",getActivity());
                    emailCurrentUser = files.ReadFromFile("EmailPlusPassword");

                }
                user = UserDetails.details(users, emailCurrentUser);// get all the details of the user
                ArrayList<User> usersWithoutCurrentUser = new ArrayList<>(users);
                usersWithoutCurrentUser.remove(user);

                genderSearchForArray = new ArrayList<>(UserDetails.lookForCollection(usersWithoutCurrentUser, user.getM_genderLookFor()));

                profileAdapter = new ProfileAdapter(profiles, getActivity());
                recyclerView.setAdapter(profileAdapter);
                for (int i = 0; i < genderSearchForArray.size(); i++) {
                    StringBuilder stringBuilder = new StringBuilder();
                    profiles.add(new Profile(stringBuilder.append(genderSearchForArray.get(i).getM_firstName())
                            .append(" ").append(genderSearchForArray.get(i).getM_lastName())
                            .append(",  ").append(genderSearchForArray.get(i).getM_city())
                            .append(",  ").append(Birthdate.AgeCalculate(genderSearchForArray.get(i).getM_birthDate())).toString(), genderSearchForArray.get(i).getM_aboutYourSelf(),
                            genderSearchForArray.get(i).getM_email(),null));
                            downloadImage(genderSearchForArray.get(i).getM_email(),i);
                }

                profileAdapter.setListener(new ProfileAdapter.MyProfileListener() {
                    @Override
                    public void onProfileClicked(int position, View view) {
                        Intent intent = new Intent(getActivity(), UserExtension.class);
                        intent.putExtra("userinfo", profiles.get(position));//for photo
                        intent.putExtra("firstname",genderSearchForArray.get(position).getM_firstName());// for first name
                        intent.putExtra("lastname",genderSearchForArray.get(position).getM_lastName());// for last name
                        startActivity(intent);
                    }

                    @Override
                    public void onButtonClick(int position, View view) {
                        Intent intent = new Intent(getActivity(), MessengerActivity.class);
                        intent.putExtra("userid",genderSearchForArray.get(position).getM_userId());//get the id of the user that clicked
                        intent.putExtra("reciverEmail",genderSearchForArray.get(position).getM_email());//get the email
                        intent.putExtra("url",profiles.get(position).getPhoto());//get photo
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void downloadImage(String email, final int position) {

        final String[] profileImageUrl = new String[1];
        StorageReference storageRef = mStorage.child("Photos/" + email + "/mainImage/image");
        Task<Uri> url = storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                try {
                    profileImageUrl[0] = task.getResult().toString();
                    profiles.get(position).setPhoto(profileImageUrl[0]);
                    profileAdapter.notifyDataSetChanged();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

}
