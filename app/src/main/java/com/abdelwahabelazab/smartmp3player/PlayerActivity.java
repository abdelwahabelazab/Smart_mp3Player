package com.abdelwahabelazab.smartmp3player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener{

    static SeekBar seekBar;
    ImageButton btnplay,btnForward,btnBackward,btnPrevious,btnNext;
    TextView txtTitle,txtTotalDuration,txtCurrentDuration;
    static MediaPlayer mp;
    ArrayList<File> mysongs;
    int position;
    long totalDuration;
    int currentPosition;
    Uri u;
    String title;
    Utilities utilities;
     Thread updateSeekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        seekBar=(SeekBar) findViewById(R.id.songProgressBar);
        btnplay=(ImageButton) findViewById(R.id.btnPlay);
        btnForward=(ImageButton) findViewById(R.id.btnForward);
        btnBackward=(ImageButton) findViewById(R.id.btnBackward);
        btnPrevious=(ImageButton) findViewById(R.id.btnPrevious);
        btnNext=(ImageButton) findViewById(R.id.btnNext);

        btnplay.setImageResource( R.drawable.btn_pause);
        txtTitle=(TextView) findViewById(R.id.songTitle);
        txtTotalDuration=(TextView) findViewById(R.id.songTotalDurationLabel);
        txtCurrentDuration=(TextView) findViewById(R.id.songCurrentDurationLabel);
        utilities=new Utilities();

        if(mp != null){
            mp.stop();
            mp.release();
        }
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        mysongs=(ArrayList)b.getParcelableArrayList("songList");
        position=b.getInt("pos");
        u=Uri.parse(mysongs.get(position).toString());
        title=mysongs.get(position).getName().toString().replace(".mp3","").replace(".wav","").replace(".MP3","");
        txtTitle.setText(title);
        mp=MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        seekBar.setMax(mp.getDuration());


        SeekbarWork();

       onSeekBarComplete();


    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btnPlay:
                if (mp.isPlaying()){

                    mp.pause();
                    btnplay.setImageResource( R.drawable.btn_play);
                }else {

                    mp.start();
                    btnplay.setImageResource( R.drawable.btn_pause);
                }
                break;
            case R.id.btnForward:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;

            case R.id.btnBackward:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btnNext:
                btnplay.setImageResource( R.drawable.btn_pause);
                 mp.stop();
                mp.release();
                position=(position+1)%mysongs.size();
                u=Uri.parse(mysongs.get(position).toString());
                title=mysongs.get(position).getName().toString().replace(".mp3","").replace(".wav","");
                txtTitle.setText(title);
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                seekBar.setMax(mp.getDuration());
                SeekbarWork();
                onSeekBarComplete();
                break;
            case R.id.btnPrevious:
                btnplay.setImageResource( R.drawable.btn_pause);
                mp.stop();
                mp.release();
                position=(position -1 <0)? mysongs.size()-1:position-1;
                u=Uri.parse(mysongs.get(position).toString());
                title=mysongs.get(position).getName().toString().replace(".mp3","").replace(".wav","").replace(".MP3","");
                txtTitle.setText(title);
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                seekBar.setMax(mp.getDuration());
                SeekbarWork();
                onSeekBarComplete();
                break;
        }
    }

   public void SeekbarWork(){


       updateSeekbar=new Thread(){
           @Override
           public void run() {


               totalDuration=mp.getDuration();
               currentPosition= 0;




               while (currentPosition < totalDuration){
                   try {

                       currentPosition=mp.getCurrentPosition();
                       seekBar.setProgress(currentPosition);
                       // Displaying Total Duration time
                       String fullDuration=""+utilities.milliSecondsToTimer(totalDuration);
                       txtTotalDuration.setText(fullDuration);
                       // Displaying time completed playing
                       String currentTime=""+utilities.milliSecondsToTimer(currentPosition);
                       txtCurrentDuration.setText(currentTime);
                       sleep(500);


                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
               // super.run();

           }

       };


       updateSeekbar.start();


       seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
               mp.seekTo(seekBar.getProgress());

           }
       });


   }

    public void onSeekBarComplete(){
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.seekTo(0);
                mp.start();
            }
        });
    }

}
