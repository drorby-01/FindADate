package com.example.myapplication.Connected.AppFragments.Home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Connected.AppFragments.Profile.Profile;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;

public class UserExtension extends AppCompatActivity {
    ListView listView;
    ImageView img_main,img_first,img_second,img_third;
    FloatingActionButton btn_edit,btn_up,btn_down;
    Profile profileCurrent;
    StorageReference m_storage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user_fragment);
        listView = findViewById(R.id.list_item);
        btn_edit = findViewById(R.id.btn_edit);
        btn_down = findViewById(R.id.btn_showMoreProfile);
        //btn_down.setVisibility(View.GONE);
        btn_down.hide();
        btn_up = findViewById(R.id.btn_showLastProfile);
        final GridLayout gridView = findViewById(R.id.grid_more_picture_layout);
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.setVisibility(View.GONE);
                btn_down.show();
                btn_up.hide();
            }
        });
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gridView.setVisibility(View.VISIBLE);
                btn_down.hide();
                btn_up.show();
            }
        });
        btn_edit.hide();
        img_main = findViewById(R.id.image_main);
        img_first = findViewById(R.id.first_image);
        img_second = findViewById(R.id.second_image);
        img_third = findViewById(R.id.third_image);
        img_first.setVisibility(View.GONE);
        img_second.setVisibility(View.GONE);
        img_third.setVisibility(View.GONE);
        m_storage = FirebaseStorage.getInstance().getReference();
        profileCurrent = (Profile) getIntent().getSerializableExtra("userinfo");
        String firstName = getIntent().getStringExtra("firstname");
        String lastName = getIntent().getStringExtra("lastname");
        String[]strings ={

                getString(R.string.firstname)+" "+firstName,
                getString(R.string.lastname)+" "+lastName,
                getString(R.string.user_age)+" "+profileCurrent.getName_age_city().split(",  ")[2],
                getString(R.string.usercity)+" "+profileCurrent.getName_age_city().split(",  ")[1],
                getString(R.string.email1) +" " + profileCurrent.getEmail(),
                getString(R.string.aboutuser)+" "+profileCurrent.getAbout_user_details()
        };
        ArrayAdapter<String> profileAdapter= new ArrayAdapter<>(UserExtension.this,android.R.layout.simple_list_item_1,strings);
        listView.setAdapter(profileAdapter);
        downloadImage();

    }


    public void downloadImage() {

         String userEmail =profileCurrent.getEmail();

        try {
            StorageReference storageRef = m_storage.child("Photos/" + userEmail + "/mainImage/image");
            downloadImage2(storageRef, img_main);
            StorageReference storageRef1 = m_storage.child("Photos/" + userEmail + "/firstImage/image");
            downloadImage2(storageRef1, img_first);
            StorageReference storageRef2 = m_storage.child("Photos/" + userEmail + "/secondImage/image");
            downloadImage2(storageRef2, img_second);
            StorageReference storageRef3 = m_storage.child("Photos/" + userEmail + "/thirdImage/image");
            downloadImage2(storageRef3, img_third);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void downloadImage2(final StorageReference storageRef, final ImageView imageView){
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                final Bitmap[] bitmap1 = new Bitmap[1];
                try {
                    final File localFile = File.createTempFile("Images", "jpeg");
                    storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap1[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            imageView.setImageBitmap(bitmap1[0]);
                            imageView.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    if(imageView.getId() == img_main.getId())
                        imageView.setImageResource(R.drawable.blankprofile);
                    else
                    imageView.setVisibility(View.GONE);
            }
        });


    }
}
