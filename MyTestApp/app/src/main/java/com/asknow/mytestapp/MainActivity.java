package com.asknow.mytestapp;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.asknow.mytestapp.DrawView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity {

    TextView textView;
    boolean isBackGroundSelected = true;
    DrawView drawView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   drawView = (DrawView) findViewById(this, "");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void paintClicked(View view){

        drawView = (DrawView) findViewById(R.id.drawing);


       if(this.isBackGroundSelected) {
            System.out.println("DrawView =" + view.getTag().toString());
            drawView.setBackgroundColor(Color.parseColor(view.getTag().toString()));
        }
        else {

            drawView.setColor(view.getTag().toString());

        }

    }

    public void selectedState(View view){

        if(R.id.save_btn == view.getId())
            this.save(view);
        else {
            switch (view.getId()) {
                case R.id.new_btn:
                    this.isBackGroundSelected = true;
                    break;
                case R.id.draw_btn:
                    this.isBackGroundSelected = false;
                    break;

            }
        }
    }


    public  void  save(View view){

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
        String curDate =dateFormat.format(date);

        drawView = (DrawView) findViewById(R.id.drawing);
        View content = drawView;
        content.setDrawingCacheEnabled(true);
        content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = content.getDrawingCache();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String imageName = "/"+curDate+"image.png";
        File file = new File(path+imageName);
        FileOutputStream ostream;
        try {
            file.createNewFile();
            ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.flush();
            ostream.close();
            Toast.makeText(getApplicationContext(), "image saved", 5000).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "error", 5000).show();
        }
    }


    public void  setWallpaper(View view){

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
        String curDate =dateFormat.format(date);

        drawView = (DrawView) findViewById(R.id.drawing);
        View content = drawView;
        content.setDrawingCacheEnabled(true);
        content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String imageName = "/"+curDate+"image.png";
        File file = new File(path+imageName);

        try {
            WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());
            file.createNewFile();
            Bitmap bitmap = BitmapFactory.decodeFile(path+imageName);
            wallManager.setBitmap(bitmap);
            Toast.makeText(MainActivity.this, "Wallpaper Set Successfully!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Setting WallPaper Failed!!", Toast.LENGTH_SHORT).show();
        }
    }

}
