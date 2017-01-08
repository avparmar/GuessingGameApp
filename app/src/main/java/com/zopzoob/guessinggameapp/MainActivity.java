package com.zopzoob.guessinggameapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> charUrls = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    int selected = 0;

    ImageView currImg;
    Button currB;

    int ansNum = 0;

    public void celebChosen(View view) {
        try {

            Button bb = (Button) view;
            if (bb.getText().toString().equals(names.get(selected))) Toast.makeText(getApplicationContext(),"You are correct!",Toast.LENGTH_SHORT).show();
            else Toast.makeText(getApplicationContext(),"Incorrect! that was " + names.get(selected), Toast.LENGTH_SHORT).show();

            selected = (int) (Math.random() * 50);

            ImageDownloader imgtask = new ImageDownloader();

            Bitmap charImg = imgtask.execute(charUrls.get(selected)).get();

            currImg.setImageBitmap(charImg);

            ansNum = (int) (Math.random() * 4);
            for (int i = 0; i < 4; i++) {
                if (i == 0) currB = (Button) findViewById(R.id.button1);
                else if (i == 1) currB = (Button) findViewById(R.id.button2);
                else if (i == 2) currB = (Button) findViewById(R.id.button3);
                else currB = (Button) findViewById(R.id.button4);

                if (ansNum == i) currB.setText(names.get(selected));
                else currB.setText(names.get((int) (Math.random() * 50)));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);

                return mBitmap;


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String res = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1) {
                    char current = (char) data;
                    res += current;
                    data = reader.read();
                }
                return res;

            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currImg = (ImageView) findViewById(R.id.imageView);

        DownloadTask task = new DownloadTask();

        String result = null;

        try {
            result = task.execute("http://www.empireonline.com/movies/features/50-greatest-video-game-characters/").get();

            Pattern p = Pattern.compile("l\"><img src=\"(.*?)\"");
            Matcher m = p.matcher(result);

            while(m.find()) {

                charUrls.add(m.group(1));

            }

            p = Pattern.compile("h2 id=\".*? (.*?)<");
            m = p.matcher(result);

            while(m.find()) {

                names.add(m.group(1));

            }

            selected =(int) (Math.random() * 50);

            ImageDownloader imgtask = new ImageDownloader();

            Bitmap charImg = imgtask.execute(charUrls.get(selected)).get();

            currImg.setImageBitmap(charImg);

            ansNum = (int) (Math.random() * 4);
            for (int i = 0; i < 4; i++) {
                if (i == 0) currB = (Button) findViewById(R.id.button1);
                else if (i == 1) currB = (Button) findViewById(R.id.button2);
                else if (i == 2) currB = (Button) findViewById(R.id.button3);
                else currB = (Button) findViewById(R.id.button4);

                if (ansNum == i) currB.setText(names.get(selected));
                else currB.setText(names.get((int)(Math.random()*50)));
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }




    }
}
