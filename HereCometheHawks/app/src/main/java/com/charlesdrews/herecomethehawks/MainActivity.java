package com.charlesdrews.herecomethehawks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private boolean mPlaying = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton playPause = (FloatingActionButton) findViewById(R.id.play_pause_button);
        FloatingActionButton stop = (FloatingActionButton) findViewById(R.id.stop_button);

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // icon is initialized to the play icon
                Intent intent = new Intent(MainActivity.this, MusicService.class);
                startService(intent);
                mPlaying = !mPlaying;
                if (mPlaying) {
                    playPause.setImageResource(R.drawable.ic_pause_white_18dp);
                } else {
                    playPause.setImageResource(R.drawable.ic_play_arrow_white_18dp);
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MusicService.class);
                stopService(intent);
                playPause.setImageResource(R.drawable.ic_play_arrow_white_18dp);
            }
        });
    }
}
