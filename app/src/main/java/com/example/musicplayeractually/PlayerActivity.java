package com.example.musicplayeractually;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    ListView listView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        listView = findViewById(R.id.songList);

        runtimePermission();
    }

    public void runtimePermission() {
        Dexter.withContext(this).withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<File> findSong(File file) {
        ArrayList<File> arraylist = new ArrayList<>();

        File f = file;

        File[] files = null;

        try {
            files = f.listFiles();

            if (files.length > 0) {
                for (File singleFile : files) {

                    if (singleFile.isDirectory() && !singleFile.isHidden()) {
                        arraylist.addAll(findSong(singleFile));
                    } else {
                        if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {

                            arraylist.add(singleFile);
                        }
                    }
                }
            } else {
                Path path = Paths.get("/storage/emulated/0/Music");
                File filePath = path.toFile();
                arraylist.add(filePath);
            }
        } catch (NullPointerException e) {
            //toast.show("Error : " + e + " " + e.getStackTrace()[0].getLineNumber());
            //val toast = Toast.makeText(this, ("Error : " + e + " " + e.getStackTrace()[0].getLineNumber()), Toast.LENGTH_SHORT)
            Toast.makeText(getApplicationContext(),("Error : " + e + " " + e.getStackTrace()[0].getLineNumber()),Toast.LENGTH_LONG).show();
        }

        return arraylist;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void displaySongs() {
        ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        //ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        //listView.setAdapter(myAdapter);

        adapterCustom myAdapter = new adapterCustom();
        listView.setAdapter(myAdapter);
    }


    class adapterCustom extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.list_itm,null);
            TextView textSong = view1.findViewById(R.id.song_name_txt);
            textSong.setSelected(true);
            textSong.setText(items[i]);

            return view1;
        }
    }




}