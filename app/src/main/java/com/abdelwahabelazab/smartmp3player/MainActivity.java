package com.abdelwahabelazab.smartmp3player;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvPlayList;
    String [] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList<File> songs=findSongs(Environment.getExternalStorageDirectory());
        lvPlayList=(ListView) findViewById(R.id.lvPlaylist);
        items=new String[songs.size()];

        for (int i=0;i<songs.size();i++){

            items[i]=songs.get(i).getName().toString().replace(".mp3","").replace(".wav","").replace(".MP3","");
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.song_layout,R.id.textView,items);
        lvPlayList.setAdapter(adapter);

        lvPlayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),PlayerActivity.class).putExtra("pos",position).putExtra("songList",songs));
            }
        });

    }

    //get songs from sdCard
    public ArrayList<File> findSongs(File root){
        ArrayList<File> songs=new ArrayList<File>();
        File files []=root.listFiles();
        for (File singleFile :files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                  songs.addAll(findSongs(singleFile));
            }
            else{
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav") || singleFile.getName().endsWith(".MP3")){
                    songs.add(singleFile);
                }
            }
        }

        return songs;
    }


}
