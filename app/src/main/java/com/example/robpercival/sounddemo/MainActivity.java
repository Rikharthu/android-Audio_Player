package com.example.robpercival.sounddemo;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ArrayList<Track> tracks;

    MediaPlayer mplayer;
    private ListView tracksLv;
    private SeekBar audioSb;
    private TextView textView;

    public void playAudio(View view) {

        mplayer.start();

    }

    public void pauseAudio(View view) {

        mplayer.pause();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tracksLv= (ListView) findViewById(R.id.track_lv);
        audioSb= (SeekBar) findViewById(R.id.audio_seek_bar);
        textView= (TextView) findViewById(R.id.textView);

        mplayer = MediaPlayer.create(this, R.raw.sfagnum);

        tracks=new ArrayList<>();
        // request all songs
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.d(LOG_TAG,"content uri: "+uri.toString());
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        ArrayList<String> titles = new ArrayList<>();

        long songId=0;
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int idArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            do {
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                String thisArtist = cursor.getString(idArtist);
                titles.add(thisTitle);
                tracks.add(new Track(thisId,thisArtist,thisTitle));
            } while (cursor.moveToNext());
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        tracksLv.setAdapter(itemsAdapter);

        audioSb.setMax(mplayer.getDuration());
        audioSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b) // initiated by the user
                    mplayer.seekTo(i);
                int currentTime=mplayer.getCurrentPosition();
                String time = String.format("%d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(currentTime),
                        TimeUnit.MILLISECONDS.toSeconds(currentTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentTime))
                );
                textView.setText(time);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                audioSb.setProgress(mplayer.getCurrentPosition());
            }
        },0,20);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
