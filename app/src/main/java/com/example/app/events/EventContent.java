package com.example.app.events;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventContent {

    public static final List<Event> ITEMS = new ArrayList<Event>();
    public static final Map<String, Event> ITEM_MAP = new HashMap<String, Event>();

    public static Event createEvent(int position, String name, String details, String date, String picPath) {
        return new Event(String.valueOf(position), name ,details, date, picPath);
    }

    public static void addItem(Event item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void deleteItems(){
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    public static class Event implements Parcelable {
        public final String id;
        public final String name;
        public final String details;
        public final String date;
        public String picPath;

        public Event(String id, String name, String details, String date, String picPath) {
            this.id = id;
            this.name = name;
            this.details = details;
            this.date = date;
            this.picPath = picPath;
        }

        protected Event(Parcel in) {
            id = in.readString();
            name = in.readString();
            details = in.readString();
            date = in.readString();
            picPath = in.readString();
        }

        public void setPicPath(String path){
            this.picPath = path;
        }

        public static final Creator<Event> CREATOR = new Creator<Event>() {
            @Override
            public Event createFromParcel(Parcel in) {
                return new Event(in);
            }

            @Override
            public Event[] newArray(int size) {
                return new Event[size];
            }
        };

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
            dest.writeString(details);
            dest.writeString(date);
            dest.writeString(picPath);
        }
    }
}
