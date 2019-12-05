package com.example.myapplication.Connected.AppFragments.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Connected.AppFragments.Profile.Profile;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private List<Profile>listOfProfile;
    private Context context;
    public ProfileAdapter(List<Profile> list,Context context)
    {
        this.listOfProfile = list;
        this.context = context;
    }


    interface MyProfileListener
    {
        void onProfileClicked(int position, View view);
        void onButtonClick(int position, View view);
    }

    MyProfileListener profileListener;

    public void setListener(MyProfileListener listner)
    {
        this.profileListener=listner;
    }
    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_card,parent,false);
        ProfileViewHolder profileViewHolder = new ProfileViewHolder(view);
        return profileViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {

        Profile profile = listOfProfile.get(position);
        Glide.with(context).load(profile.getPhoto()).error(R.drawable.blankprofile).into(holder.m_imageuser);
        holder.m_about_her.setText(profile.getAbout_user_details());
        holder.m_name_age_city.setText(profile.getName_age_city());
    }

    @Override
    public int getItemCount() {
        return listOfProfile.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder
    {
        ImageView m_imageuser;
        TextView m_name_age_city;
        TextView m_about_her;
        FloatingActionButton m_messenger;
        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);

            m_imageuser = itemView.findViewById(R.id.image_card);
            m_name_age_city=itemView.findViewById(R.id.name_age_city);
            m_about_her = itemView.findViewById(R.id.about_her);
            m_messenger = itemView.findViewById(R.id.btn_message);

            m_messenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(profileListener != null)
                    profileListener.onButtonClick(getAdapterPosition(),view);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(profileListener != null)
                    {
                        profileListener.onProfileClicked(getAdapterPosition(),view);
                    }
                }
            });
        }
    }
}
