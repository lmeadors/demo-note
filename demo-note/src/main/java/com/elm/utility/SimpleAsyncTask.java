package com.elm.utility;

import android.os.AsyncTask;
import android.util.Log;
import com.elm.bean.Message;

public abstract class SimpleAsyncTask<ParameterType, ProgressType, ReturnType extends Message>
		extends AsyncTask<ParameterType, ProgressType, ReturnType>
{

	private static final String TAG = SimpleAsyncTask.class.getName();
	private final LocalBroadcastUtility localBroadcastUtility;
	private final String sendingAction;

	protected SimpleAsyncTask(LocalBroadcastUtility localBroadcastUtility, String sendingAction) {
		Log.d(TAG, "creating task for action " + sendingAction);
		this.localBroadcastUtility = localBroadcastUtility;
		this.sendingAction = sendingAction;

	}

	@Override
	protected void onPostExecute(ReturnType returnValue) {
		Log.d(TAG, "sending " + returnValue + " as action " + sendingAction);
		localBroadcastUtility.sendBroadcast(sendingAction, returnValue);
	}

}
