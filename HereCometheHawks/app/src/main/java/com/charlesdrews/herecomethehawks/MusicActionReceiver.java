package com.charlesdrews.herecomethehawks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by charlie on 3/2/16.
 */
public class MusicActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "broadcast received", Toast.LENGTH_SHORT).show();
    }
}
