package com.elm.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.Serializable;

public class LocalBroadcastUtility {

	private static final String TAG = LocalBroadcastUtility.class.getName();

	private final LocalBroadcastManager broadcastManager;

	public LocalBroadcastUtility(Context context) {
		broadcastManager = LocalBroadcastManager.getInstance(context);
	}

	public IntentFilter getSimpleFilter(Class sendingClass) {
		return new IntentFilter(sendingClass.getName());
	}

	public boolean sendBroadcast(Class sendingClass) {
		return broadcastManager.sendBroadcast(getIntent(sendingClass));
	}

	public boolean sendBroadcast(Class sendingClass, Parcelable payload) {
		final Intent intent = getIntent(sendingClass);
		addPayload(intent, payload);
		return broadcastManager.sendBroadcast(intent);
	}

	public boolean sendBroadcast(String action, Serializable payload) {
		Log.d(TAG, "sending " + payload + " using action " + action);
		final Intent intent = new Intent(action);
		addPayload(intent, payload);
		return broadcastManager.sendBroadcast(intent);
	}

	public boolean sendBroadcast(Object sender, Serializable payload) {
		Log.d(TAG, "sending " + payload + " for object " + sender);
		return sendBroadcast(sender.getClass(), payload);
	}

	public boolean sendBroadcast(Class sendingClass, Serializable payload) {
		Log.d(TAG, "sending " + payload + " for class " + sendingClass);
		final Intent intent = getIntent(sendingClass);
		addPayload(intent, payload);
		return broadcastManager.sendBroadcast(intent);
	}

	public void addPayload(Intent intent, Parcelable payload) {
		intent.putExtra(payload.getClass().getName(), payload);
	}

	public void addPayload(Intent intent, Serializable payload) {
		intent.putExtra(payload.getClass().getName(), payload);
	}

	public Intent getIntent(Class sendingClass) {
		return new Intent(sendingClass.getName());
	}

	@SuppressWarnings("unchecked")
	public <PayloadType> PayloadType getPayload(Intent intent, Class<PayloadType> type) {
		return (PayloadType) intent.getExtras().get(type.getName());
	}

	public void registerReceiver(BroadcastReceiver receiver, String action) {
		Log.d(TAG, "register receiver for action " + action);
		broadcastManager.registerReceiver(receiver, new IntentFilter(action));
	}

	public void unregisterReceiver(BroadcastReceiver receiver) {
		Log.d(TAG, "unregister receiver " + receiver);
		broadcastManager.unregisterReceiver(receiver);
	}

}
