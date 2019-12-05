package com.example.myapplication.Connected.AppFragments.Profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.SignUp.Files;
import com.example.myapplication.SignUp.User;
import com.example.myapplication.SignUp.UserDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProfileUserFragment extends Fragment implements View.OnClickListener {

    public ProfileUserFragment() {

    }
    ArrayList<User> users = new ArrayList<>();

    ImageView mainImage,firstImage,secondImage,thirdImage;
    FloatingActionButton btn_show_last,btn_show_more;
    GridLayout gridLayout;
    int choser;
    static final int GALLERY_INTENT = 1;
    static final int CAMERA_INTENT = 2;

    FloatingActionButton btn_edit;
    File file;
    View view;
    ListView listView;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseUser;
    StorageReference mStorage;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_user_fragment,container,false);
        mainImage = view.findViewById(R.id.image_main);
        firstImage = view.findViewById(R.id.first_image);
        secondImage = view.findViewById(R.id.second_image);
        thirdImage = view.findViewById(R.id.third_image);
        listView = view.findViewById(R.id.list_item);
        mainImage.setOnClickListener(this);
        //mainImage.setTag("mainimage");
        firstImage.setOnClickListener(this);
        //firstImage.setTag("first");
        secondImage.setOnClickListener(this);
        //secondImage.setTag("second");
        thirdImage.setOnClickListener(this);
        //thirdImage.setTag("third");
        btn_edit=view.findViewById(R.id.btn_edit);
        btn_show_last = view.findViewById(R.id.btn_showLastProfile);
        btn_show_more = view.findViewById(R.id.btn_showMoreProfile);
        gridLayout = view.findViewById(R.id.grid_more_picture_layout);
        mStorage = FirebaseStorage.getInstance().getReference();
        databaseUser =FirebaseDatabase.getInstance().getReference("Users");
        btn_show_more.hide();
        btn_show_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridLayout.setVisibility(View.GONE);
                btn_show_more.show();
                btn_show_last.hide();
            }
        });
        btn_show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridLayout.setVisibility(View.VISIBLE);;
                btn_show_more.hide();
                btn_show_last.show();

            }
        });
        downloadImage();
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
                    //adding user to the list
                    users.add(user);
                }
                FirebaseUser userEmail = firebaseAuth.getCurrentUser();

                String email;
                try {
                    email = userEmail.getEmail();
                }catch (Exception e){
                    Files files = new Files("emailAndPassword",view.getContext());
                    email = files.ReadFromFile("emailpassword").split(",")[0];
                }

                final User user = UserDetails.details(users, email);
                if(user != null) {
                    String[] strings = {user.getM_firstName(), user.getM_lastName(), user.getM_birthDate(), user.getM_city(), user.getM_email(), user.getM_aboutYourSelf()};
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, strings);

                    listView.setAdapter(arrayAdapter);
                }
                btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                        intent.putExtra("userinformation",user);
                        startActivity(intent);
                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mainImage.getId())
            choser =1;
        else if(v.getId() == firstImage.getId())
            choser =2;
        else if(v.getId() == secondImage.getId())
            choser =3;
        else if(v.getId() == thirdImage.getId())
            choser =4;

        AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.choose_source).
                        setMessage(R.string.choose_source_message).setPositiveButton(R.string.camera, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        file = new File(Environment.getExternalStorageDirectory(),"piccc.jpg");
                        Uri fileUri = Uri.fromFile(file);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,CAMERA_INTENT);

                    }
                }).setNegativeButton(R.string.gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, GALLERY_INTENT);
                    }
                }).show();
    }

    public void prograssDialog()
    {
        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setTitle("Plaese wait");
        progressDialog.setMessage("Downloading files it may take a few minutes...");

        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(view.getContext(), "Proccess cancelled", Toast.LENGTH_SHORT).show();
            }
        });


        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        },5000);

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void UploadImage(String path,Uri imageUri){

        StorageReference filepath = mStorage.child(path).child("image");
        filepath.putFile(imageUri);

    }


    public void downloadImage() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        try {
            StorageReference storageRef = mStorage.child("Photos/" + user.getEmail() + "/mainImage/image");
            downloadImage2(storageRef, mainImage);
            StorageReference storageRef1 = mStorage.child("Photos/" + user.getEmail() + "/firstImage/image");
            downloadImage2(storageRef1, firstImage);
            StorageReference storageRef2 = mStorage.child("Photos/" + user.getEmail() + "/secondImage/image");
            downloadImage2(storageRef2, secondImage);
            StorageReference storageRef3 = mStorage.child("Photos/" + user.getEmail() + "/thirdImage/image");
            downloadImage2(storageRef3, thirdImage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void downloadImage2(StorageReference storageRef, final ImageView imageView){
        final Bitmap[] bitmap1 = new Bitmap[1];
        try {
            final File localFile = File.createTempFile("Images", "jpeg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bitmap1[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap1[0]);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_INTENT && resultCode == getActivity().RESULT_OK)
        {

            String path1,path2,path3,path4;

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Uri imageUri = getImageUri(getActivity(),bitmap);
            FirebaseUser user =firebaseAuth.getCurrentUser();
            path1 ="Photos/"+user.getEmail()+"/"+"mainImage";
            path2="Photos/"+user.getEmail()+"/"+"firstImage";
            path3 ="Photos/"+user.getEmail()+"/"+"secondImage";
            path4 ="Photos/"+user.getEmail()+"/"+"thirdImage";

            switch (choser)
            {
                case 1:{
                    //mainImage.setImageBitmap(bitmap);
                    Glide.with(view.getContext()).load(file.getAbsoluteFile()).into(mainImage);
                    //mainImage.setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                    UploadImage(path1,imageUri);

                    break;}
                case 2: {//firstImage.setImageBitmap(bitmap);
                    Glide.with(view.getContext()).load(file.getAbsoluteFile()).into(firstImage);
                    //firstImage.setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                    UploadImage(path2,imageUri);
                    break;}
                case 3: {//secondImage.setImageBitmap(bitmap);
                    Glide.with(view.getContext()).load(file.getAbsoluteFile()).into(secondImage);
                    UploadImage(path3,imageUri);
                    break;}
                case 4: {//thirdImage.setImageBitmap(bitmap);
                    Glide.with(view.getContext()).load(file.getAbsoluteFile()).into(thirdImage);
                    UploadImage(path4,imageUri);
                    break;}
            }
        }
        else if( requestCode == GALLERY_INTENT && resultCode == getActivity().RESULT_OK)
        {

            Uri imageUri = data.getData();
            FirebaseUser user =firebaseAuth.getCurrentUser();
            String path1 ="Photos/"+user.getEmail()+"/"+"mainImage";
            String path2="Photos/"+user.getEmail()+"/"+"firstImage";
            String path3 ="Photos/"+user.getEmail()+"/"+"secondImage";
            String path4 ="Photos/"+user.getEmail()+"/"+"thirdImage";


            switch (choser)
            {
                case 1: {
                    //Picasso.get().load(imageUri).into(mainImage);
                    Glide.with(view.getContext()).load(imageUri).into(mainImage);
                    //mainImage.setImageURI(imageUri);
                    UploadImage(path1,imageUri);
                    break;}
                case 2: {
                    //Picasso.get().load(imageUri).into(firstImage);
                    Glide.with(view.getContext()).load(imageUri).into(firstImage);
                    //firstImage.setImageURI(imageUri);
                    UploadImage(path2,imageUri);
                    break;}
                case 3: {
                    //Picasso.get().load(imageUri).into(secondImage);
                    Glide.with(view.getContext()).load(imageUri).into(secondImage);
                    UploadImage(path3,imageUri);
                    break;}
                case 4: {
                    //Picasso.get().load(imageUri).into(thirdImage);
                    Glide.with(view.getContext()).load(imageUri).into(thirdImage);
                    //thirdImage.setImageURI(imageUri);
                    UploadImage(path4,imageUri);
                    break;}
            }
        }

    }
}

