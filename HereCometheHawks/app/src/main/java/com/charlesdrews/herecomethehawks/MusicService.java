package com.charlesdrews.herecomethehawks;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/**
 * Play some jamzzzz!!!
 * Created by charlie on 3/2/16.
 */
public class MusicService extends Service {
    private static final String URL = "http://downloads.blackhawks.nhl.com/audio/herecomethehawks.mp3";
    private static final String CUSTOM_INTENT_ACTION = "com.charlesdrews.herecomethehawks.CUSTOM_INTENT";
    private static final int NOTIFICATION_ID = 12345;

    public enum MusicAction { PLAY, PAUSE, RESUME, STOP, TOGGLE_PLAY_PAUSE }

    private static MediaPlayer mPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MusicAction action = (MusicAction) intent.getSerializableExtra(getString(R.string.music_action_key));

        switch (action) {
            case PLAY:
                initPlayer();
                break;
            case PAUSE:
                mPlayer.pause();
                break;
            case RESUME:
                mPlayer.start();
                break;
            case STOP:
                shutDownPlayer();
                break;
            case TOGGLE_PLAY_PAUSE:
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                } else {
                    mPlayer.start();
                }
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
        }
    }

    private void initPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setLooping(false);

        try {
            mPlayer.setDataSource(URL);
        } catch (Throwable thr) {
            thr.printStackTrace();
        }

        mPlayer.prepareAsync();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.start();
                goForegroundWithNotification();
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                shutDownPlayer();
            }
        });
    }

    private void shutDownPlayer() {
        mPlayer.stop();
        mPlayer.release();
        stopForeground(true);
        stopSelf();
    }

    /**
     * Give the service Foreground status to prevent cancellation. This requires a notification;
     * use it to control play/pause/stop via a broadcast receiver.
     */
    private void goForegroundWithNotification() {
        String action_key = getString(R.string.music_action_key);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_music_note_white_18dp);
        builder.setContentTitle("Here Come the Hawks");
        builder.setContentText("Great song, eh?");

        Intent playPauseIntent = new Intent(CUSTOM_INTENT_ACTION);
        playPauseIntent.putExtra(action_key, MusicAction.TOGGLE_PLAY_PAUSE);
        PendingIntent playPausePendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                (int) System.currentTimeMillis(), playPauseIntent, 0);
                // use milliseconds as request code so pending intents don't overwrite each other
        builder.addAction(R.drawable.ic_refresh_white_18dp, "Play/Pause", playPausePendingIntent);

        Intent stopIntent = new Intent(CUSTOM_INTENT_ACTION);
        stopIntent.putExtra(action_key, MusicAction.STOP);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                (int) System.currentTimeMillis(), stopIntent, 0);
        builder.addAction(R.drawable.ic_stop_white_18dp, "Stop", stopPendingIntent);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;

        startForeground(NOTIFICATION_ID, notification);
    }
}
