package com.charlesdrews.herecomethehawks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Receive music player actions and rebroadcast them to the music service
 * Created by charlie on 3/2/16.
 */
public class MusicActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String musicActionKey = context.getString(R.string.music_action_key);
        MusicService.MusicAction action = (MusicService.MusicAction) intent.getSerializableExtra(musicActionKey);

        Intent musicServiceIntent = new Intent(context, MusicService.class);
        musicServiceIntent.putExtra(musicActionKey, action);
        context.startService(musicServiceIntent);

        if (action == MusicService.MusicAction.STOP) {
            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
        }
    }
}
