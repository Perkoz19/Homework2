package com.example.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app.EventFragment.OnListFragmentInteractionListener;
import com.example.app.events.EventContent;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link EventContent.Event} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyEventRecyclerViewAdapter extends RecyclerView.Adapter<MyEventRecyclerViewAdapter.ViewHolder> {

    private final List<EventContent.Event> mValues;
    private final OnListFragmentInteractionListener mListener;
    public SharedPreferences preferences;
    String PREFERENCES_NAME = "myPreferences";

    public MyEventRecyclerViewAdapter(List<EventContent.Event> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void setButtonClickListener(View view, final ViewHolder holder){
        final Button deleteButton = (Button) view.findViewById(R.id.deleteButton);
        View.OnClickListener myClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                mListener.OnDeleteButtonClickListener(position);
            }
        };
        deleteButton.setOnClickListener(myClickListener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event, parent, false);
        ViewHolder holder = new ViewHolder(view);
        preferences = parent.getContext().getSharedPreferences(PREFERENCES_NAME, MainActivity.MODE_PRIVATE);
        setButtonClickListener(view, holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        EventContent.Event event = mValues.get(position);
        holder.mItem = event;
        holder.mContentView.setText(event.name);

        final String picPath = event.picPath;
        Context context = holder.mView.getContext();
        if(picPath != null && !picPath.isEmpty()){
            Bitmap image = null;
            image = BitmapFactory.decodeFile(picPath);
            if(image != null) holder.mItemImageView.setImageBitmap(image);
            else holder.mItemImageView.setImageResource(context.getResources().getIdentifier(picPath, "drawable", context.getPackageName()));
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(null != mListener){
                    mListener.onListFragmentClickInteraction(holder.mItem, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageView mItemImageView;
        public EventContent.Event mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mItemImageView = view.findViewById(R.id.event_image);
            mContentView =  view.findViewById(R.id.event_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
