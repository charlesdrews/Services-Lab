package com.charlesdrews.herecomethehawks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final int PLAY_ICON_RES = R.drawable.ic_play_arrow_white_18dp;
    private static final int PAUSE_ICON_RES = R.drawable.ic_pause_white_18dp;

    private FloatingActionButton mPlayPauseButton;
    private MusicService.MusicAction mPlayPauseButtonAction;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPlayPauseButton = (FloatingActionButton) findViewById(R.id.play_pause_button);
        FloatingActionButton stopButton = (FloatingActionButton) findViewById(R.id.stop_button);

        // initialize action for play/pause button to PLAY
        mPlayPauseButtonAction = MusicService.MusicAction.PLAY;
        mPlayPauseButton.setImageResource(PLAY_ICON_RES);

        mPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActionToMusicService(mPlayPauseButtonAction);
                resetPlayPauseButtonAction(mPlayPauseButtonAction);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActionToMusicService(MusicService.MusicAction.STOP);
                resetPlayPauseButtonAction(MusicService.MusicAction.STOP);
            }
        });
    }

    private void sendActionToMusicService(MusicService.MusicAction action) {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        intent.putExtra(getString(R.string.music_action_key), action);
        startService(intent);
    }

    private void resetPlayPauseButtonAction(MusicService.MusicAction lastActionSent) {
        switch (lastActionSent) {

            case PLAY:
                // reset to PAUSE
                mPlayPauseButtonAction = MusicService.MusicAction.PAUSE;
                mPlayPauseButton.setImageResource(PAUSE_ICON_RES);
                break;

            case PAUSE:
                // reset to RESUME
                mPlayPauseButtonAction = MusicService.MusicAction.RESUME;
                mPlayPauseButton.setImageResource(PLAY_ICON_RES);
                break;

            case RESUME:
                // reset to PAUSE
                mPlayPauseButtonAction = MusicService.MusicAction.PAUSE;
                mPlayPauseButton.setImageResource(PAUSE_ICON_RES);
                break;

            case STOP:
                // reset to PLAY
                mPlayPauseButtonAction = MusicService.MusicAction.PLAY;
                mPlayPauseButton.setImageResource(PLAY_ICON_RES);
                break;
        }
    }
}
