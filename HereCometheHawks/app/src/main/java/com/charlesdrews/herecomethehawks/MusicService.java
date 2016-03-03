package com.charlesdrews.herecomethehawks;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Play some jamzzzz!!!
 * Created by charlie on 3/2/16.
 */
public class MusicService extends Service {
    private static MediaPlayer mPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            String url = "http://downloads.blackhawks.nhl.com/audio/herecomethehawks.mp3";
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(url);
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    startPlayer();
                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopSelf();
                }
            });
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            startPlayer();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
        mPlayer.release();
    }

    private void startPlayer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPlayer.start();
            }
        }).start();
    }
}
