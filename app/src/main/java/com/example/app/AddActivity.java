package com.example.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    String picPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File image = null;
                try {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = timeStamp + "_";
                    File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    image = File.createTempFile(
                            imageFileName,
                            ".jpg",
                            storageDir
                    );
                    picPath = image.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri photoURI = FileProvider.getUriForFile(this, getString(R.string.myFileprovider), image);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
        setButtonClickListener();
    }

    public void setButtonClickListener(){
        final Button add = (Button) findViewById(R.id.add);

        View.OnClickListener myClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = findViewById(R.id.add_name);
                TextView details = findViewById(R.id.add_details);
                TextView date = findViewById(R.id.add_date);

                String nameString = name.getText().toString();
                String detailsString = details.getText().toString();
                String dateString = date.getText().toString();

                if(!TextUtils.isEmpty(nameString) && !TextUtils.isEmpty(nameString) && !TextUtils.isEmpty(dateString)){
                    finishActivity(nameString, detailsString, dateString);
                }
                else{
                    if(TextUtils.isEmpty(nameString)) name.setError("Add event name:");
                    if(TextUtils.isEmpty(detailsString)) details.setError("Add event details:");
                    if(TextUtils.isEmpty(dateString)) date.setError("Add event date:");
                }
            }
        };

        add.setOnClickListener(myClickListener);
    }

    void finishActivity(String name, String details, String date){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("nameRet", name);
        returnIntent.putExtra("detailsRet", details);
        returnIntent.putExtra("dateRet", date);
        if(picPath != null){
            returnIntent.putExtra("picPath", picPath);
        }
        setResult(AddActivity.RESULT_OK,returnIntent);
        finish();
    }
}
