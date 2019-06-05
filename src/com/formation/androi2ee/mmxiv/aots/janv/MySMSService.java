package com.formation.androi2ee.mmxiv.aots.janv;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.util.Log;

public class MySMSService extends Service {
	// Define the SMS_RECEIVE's INTENT NAME
	final String SMS_RECEIVE_INTENT_NAME = "android.provider.Telephony.SMS_RECEIVED";
	final int UniqueNotificationId = 131274;

	public MySMSService() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("MySMSService", "Service received an event from Broadcast " + intent.getAction() + "?="
				+ SMS_RECEIVE_INTENT_NAME);
		if (intent.getAction().equals(SMS_RECEIVE_INTENT_NAME)) {
			// Retrieve the bundle that handles the Messages
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				// Retrieve the data store in the SMS
				Object[] pdus = (Object[]) bundle.get("pdus");
				// Declare the associated SMS Messages
				SmsMessage[] smsMessages = new SmsMessage[pdus.length];
				// Rebuild your SMS Messages
				for (int i = 0; i < pdus.length; i++) {
					smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				// Parse your SMS Message
				SmsMessage currentMessage;
				String body, from;
				long when;
				for (int i = 0; i < smsMessages.length; i++) {
					currentMessage = smsMessages[i];
					body = currentMessage.getDisplayMessageBody();
					from = currentMessage.getDisplayOriginatingAddress();
					when = currentMessage.getTimestampMillis();
					buildANotif(body, from, when);
					Log.e("MySMSService", "Service parse the sms [" + from + "," + body + "]");
				}
			}
		}

		// never ever forget to kill your service or it will kill you :)
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	private void buildANotif(String body, String from, long when) {
		PendingIntent pdIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setAutoCancel(true)
				.setContentIntent(pdIntent)
				.setContentText(body)
				.setContentTitle("New SMS de :" + from)
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
				.setLights(0x99FF0000, 0, 1000)// don't work
				.setNumber(41108)
				.setOngoing(false)
				.setPriority(NotificationCompat.PRIORITY_MIN)
				.setProgress(100, 0, true) // don't work
				.setSmallIcon(R.drawable.ic_notif_small)
				.setSubText("SubText")
				.setTicker("You received a new SMS from " + from)
				.setVibrate(new long[] { 100, 200, 100, 200, 100 }) // don't work
				.setWhen(System.currentTimeMillis());

		NotificationManagerCompat notifCompat=NotificationManagerCompat.from(this);
		notifCompat.notify(UniqueNotificationId, builder.build());
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
