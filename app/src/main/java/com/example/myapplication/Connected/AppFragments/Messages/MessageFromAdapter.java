package com.example.myapplication.Connected.AppFragments.Messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Connected.Messenger.UserChatDetails;
import com.example.myapplication.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageFromAdapter extends RecyclerView.Adapter<MessageFromAdapter.ViewHolder> {

    List<UserChatDetails> users;
    Context context;

    public MessageFromAdapter(Context context,List<UserChatDetails> users) {
        this.context =context;
        this.users = users;

    }

    public interface MessageFromListner
    {
         void onProfileClick(int position, View view);
    }

    public void setLisner(MessageFromListner messageListner) {
        this.messageListner = messageListner;
    }

    MessageFromListner messageListner;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.adapter_message_from, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserChatDetails userChatDetails = users.get(position);
        holder.textView.setText(userChatDetails.getUsername());
        Glide.with(context).load(userChatDetails.getImageUrl()).error(R.drawable.blankprofile).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
         imageView = itemView.findViewById(R.id.circle_user_search_for);
         textView = itemView.findViewById(R.id.text_username);

         itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(messageListner != null)
                messageListner.onProfileClick(getAdapterPosition(),v);
             }
         });
        }

    }
}
