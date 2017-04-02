package com.example.dobit.recall;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VisualExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    private Uri picUri;
    private final static int cameraRequestCode = 1;
    private ImageView imageView;
    private Button take;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_mode);
        imageView = (ImageView) findViewById(R.id.ivPhoto);
        take = (Button) findViewById(R.id.btnTake);
        take.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, cameraRequestCode);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == cameraRequestCode)
        {
            if(resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
                Toast.makeText(this, "You are correct!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
