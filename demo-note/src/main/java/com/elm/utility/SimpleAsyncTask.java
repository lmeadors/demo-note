package com.elm.utility;

import android.os.AsyncTask;
import android.util.Log;
import com.elm.bean.Message;

public abstract class SimpleAsyncTask<T, U, V extends Message> extends AsyncTask<T, U, V> {

	private static final String TAG = SimpleAsyncTask.class.getName();
	private final LocalBroadcastUtility localBroadcastUtility;
	private final String sendingAction;

	protected SimpleAsyncTask(LocalBroadcastUtility localBroadcastUtility, String sendingAction) {
		Log.d(TAG, "creating task for action " + sendingAction);
		this.localBroadcastUtility = localBroadcastUtility;
		this.sendingAction = sendingAction;

	}

	@Override
	protected void onPostExecute(V v) {
		Log.d(TAG, "sending " + v + " as action " + sendingAction);
		localBroadcastUtility.sendBroadcast(sendingAction, v);
	}

}
