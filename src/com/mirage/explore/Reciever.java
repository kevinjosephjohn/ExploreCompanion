package com.mirage.explore;

import java.util.UUID;

import org.json.JSONException;

import com.getpebble.android.kit.Constants;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.getpebble.android.kit.util.PebbleDictionary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class Reciever extends BroadcastReceiver {
	public static final UUID APP_UUID = UUID
			.fromString("144148b3-c282-49e1-968b-646aeb57da2f");
@Override
public void onReceive(Context context, Intent intent) {
   if (intent.getAction().equals(Constants.INTENT_APP_RECEIVE)) {
        final UUID receivedUuid = (UUID) intent.getSerializableExtra(Constants.APP_UUID);
            
        // Pebble-enabled apps are expected to be good citizens and only inspect broadcasts containing their UUID
        if (!APP_UUID.equals(receivedUuid)) {
            Log.i("no","no");
            return;
        }
            
        final int transactionId = intent.getIntExtra(Constants.TRANSACTION_ID, -1);
        final String jsonData = intent.getStringExtra(Constants.MSG_DATA);
        if (jsonData == null || jsonData.isEmpty()) {
        	Log.i("yes","yes");
            return;
        }
            
        try {
            final PebbleDictionary data = PebbleDictionary.fromJson(jsonData);
            // do what you need with the data
            String text = data.getString(300);
            PebbleKit.sendAckToPebble(context, transactionId);
            if (text.equals("PebbleKitAndroid")) {
				String lat = data.getString(200);
				String longi = data.getString(201);
				Log.i(lat, longi);

				String uri = "https://maps.google.com/maps?f=d&daddr="
						+ lat + "," + longi;
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				i.addFlags(
					      Intent.FLAG_ACTIVITY_NEW_TASK
					    | Intent.FLAG_ACTIVITY_CLEAR_TOP
					    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				context.getApplicationContext().startActivity(i);
//				MediaPlayer mp = MediaPlayer.create (context, R.raw.mpthreetest);  
//				  mp.start();
				

			}
			if (text.equals("PebbleKitJS")) {
				String cat = data.getString(100);

				Log.i(cat, cat);
				

			}
			Log.i("data",text);
            
        } catch (JSONException e) {
            
            return;
        }
    }
}

}
