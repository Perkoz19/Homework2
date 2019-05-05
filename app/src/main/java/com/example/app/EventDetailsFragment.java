package com.example.app;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app.events.EventContent;


public class EventDetailsFragment extends Fragment{

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if(intent != null){
            EventContent.Event receivedEvent = intent.getParcelableExtra("eventExtra");
            if(receivedEvent != null){
                displayEventDetails(receivedEvent);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_details, container, false);
    }

    public void displayEventDetails(EventContent.Event event){
        FragmentActivity activity = getActivity();

        TextView name = activity.findViewById(R.id.event_d_name);
        TextView details = activity.findViewById(R.id.event_d_details);
        TextView date = activity.findViewById(R.id.event_d_date);
        ImageView img = activity.findViewById(R.id.event_d_image);

        name.setText(event.name);
        details.setText(event.details);
        date.setText(event.date);

        String picPath = event.picPath;

        Bitmap image = BitmapFactory.decodeFile(picPath);
        if(image != null){
            img.setImageBitmap(image);
        }else{
            img.setImageResource(getResources().getIdentifier(picPath, "drawable", getActivity().getPackageName()));
        }
    }
}
