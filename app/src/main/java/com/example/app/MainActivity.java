package com.example.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app.events.EventContent;

import static com.example.app.events.EventContent.addItem;
import static com.example.app.events.EventContent.createEvent;


public class MainActivity extends AppCompatActivity implements EventFragment.OnListFragmentInteractionListener {

    public static final String eventExtra = "eventExtra";
    public SharedPreferences preferences;
    String PREFERENCES_NAME = "myPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(PREFERENCES_NAME, MainActivity.MODE_PRIVATE);
        loadData();
        FloatingActionButton fabPlus = findViewById(R.id.fabPlus);
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(view.getContext(), AddActivity.class);
                startActivityForResult(intentAdd, 1);
            }
        });
        FloatingActionButton fabCamera = findViewById(R.id.fabCamera);
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(view.getContext(), AddActivity.class);
                intentAdd.putExtra("camera", 1);
                startActivityForResult(intentAdd, 1);
            }
        });

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            onListFragmentClickInteraction(null, 0);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == AddActivity.RESULT_OK) {
                String name = data.getStringExtra("nameRet");
                String details = data.getStringExtra("detailsRet");
                String date = data.getStringExtra("dateRet");
                String picPath = data.getStringExtra("picPath");

                if(picPath == null){
                    saveData(name, details, date, "pic");
                }else{
                    saveData(name, details, date, picPath);
                }
                loadData();
                ((EventFragment) getSupportFragmentManager().findFragmentById(R.id.eventFragment)).notifyDataChange();
            } else if (resultCode == AddActivity.RESULT_CANCELED) {
            }
        }
    }

    private void saveData(String name, String details, String date, String picPath) {
        int index = 0;
        while (preferences.getString("name" + index, "") != "")
            index++;

        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString("name" + index, name);
        preferencesEditor.putString("details" + index, details);
        preferencesEditor.putString("date" + index, date);
        preferencesEditor.putString("picPath" + index, picPath);
        preferencesEditor.apply();
    }

    private void loadData() {
        EventContent.deleteItems();
        int positionItem = 0;
        while (preferences.getString("name" + positionItem, "") != "") {
            String prefsName = preferences.getString("name" + positionItem, "");
            String prefsDetails = preferences.getString("details" + positionItem, "");
            String prefsDate = preferences.getString("date" + positionItem, "");
            String prefsPic = preferences.getString("picPath" + positionItem, "");

            addItem(createEvent(positionItem++, prefsName, prefsDetails, prefsDate, prefsPic));
            ((EventFragment) getSupportFragmentManager().findFragmentById(R.id.eventFragment)).notifyDataChange();
        }
    }

    @Override
    public void onListFragmentClickInteraction(EventContent.Event event, int position) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            TextView name = findViewById(R.id.event_d_name);
            TextView details = findViewById(R.id.event_d_details);
            TextView date = findViewById(R.id.event_d_date);
            ImageView pic = findViewById(R.id.event_d_image);

            String prefsName = preferences.getString("name" + position, "");
            String prefsDetails = preferences.getString("details" + position, "");
            String prefsDate = preferences.getString("date" + position, "");
            String prefsPic = preferences.getString("picPath" + position, "");

            name.setText(prefsName);
            details.setText(prefsDetails);
            date.setText(prefsDate);
            Bitmap image = BitmapFactory.decodeFile(prefsPic);

            if(image != null) pic.setImageBitmap(image);
            else pic.setImageResource(getResources().getIdentifier(prefsPic, "drawable", getPackageName()));

        }else{
            Intent intent = new Intent(this, EventDetailsActivity.class);
            intent.putExtra(eventExtra, event);
            startActivity(intent);
        }
    }

    public void OnDeleteButtonClickListener(int position) {
        deleteItem(position);
    }

    private void deleteItem(final int position) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Do you really want to delete this event?")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        preferences.edit().remove("name" + position).apply();
                        preferences.edit().remove("details" + position).apply();
                        preferences.edit().remove("date" + position).apply();
                        preferences.edit().remove("picPath" + position).apply();
                        repairId(position);
                        loadData();
                        ((EventFragment) getSupportFragmentManager().findFragmentById(R.id.eventFragment)).notifyDataChange();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void repairId(int deletedPosition) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        int nextPosition = deletedPosition + 1;
        int previewPosition = deletedPosition;

        while (preferences.getString("name" + nextPosition, "") != "") {
            String prefsName = preferences.getString("name" + nextPosition, "");
            String prefsDetails = preferences.getString("details" + nextPosition, "");
            String prefsdate = preferences.getString("date" + nextPosition, "");
            String prefsPic = preferences.getString("picPath" + nextPosition, "");
            preferencesEditor.putString("name" + previewPosition, prefsName);
            preferencesEditor.putString("details" + previewPosition, prefsDetails);
            preferencesEditor.putString("date" + previewPosition, prefsdate);
            preferencesEditor.putString("picPath" + previewPosition, prefsPic);
            previewPosition++;
            nextPosition++;
        }
        preferencesEditor.apply();
        preferences.edit().remove("name" + previewPosition).apply();
        preferences.edit().remove("details" + previewPosition).apply();
        preferences.edit().remove("date" + previewPosition).apply();
        preferences.edit().remove("picPath" + previewPosition).apply();
    }
}
