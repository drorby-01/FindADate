package com.example.myapplication.Connected.AppFragments.Settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

public class SettingsAdapter extends BaseAdapter {
    List<Settings>settings;

    public SettingsAdapter(List<Settings> settings) {
        this.settings = settings;
    }

    @Override
    public int getCount() {
        return settings.size();
    }

    @Override
    public Object getItem(int position) {
        return settings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
       if(view == null)
       {
           view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settingadapter,null);
       }
        Settings settings2 =settings.get(position);
        TextView textMain = view.findViewById(R.id.text_main);
        TextView textSupportDetails = view.findViewById(R.id.text_detail);
        textMain.setText(settings2.getText());
        textSupportDetails.setText(settings2.getTextinformation());
        return view;
    }
}
