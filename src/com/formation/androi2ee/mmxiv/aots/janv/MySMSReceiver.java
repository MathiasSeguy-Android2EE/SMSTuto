package com.formation.androi2ee.mmxiv.aots.janv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MySMSReceiver extends BroadcastReceiver {
	public MySMSReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("MySMSReceiver","SMS event received in Broadcast");
		Intent launchService=new Intent(context,MySMSService.class);
		//copy paste the data
		launchService.putExtras(intent);
		//copy paste the Action
		launchService.setAction(intent.getAction());
		//Launch the service
		context.startService(launchService);
		
	}
}
