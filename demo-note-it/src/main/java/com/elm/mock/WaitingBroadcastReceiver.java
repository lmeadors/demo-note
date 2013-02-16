package com.elm.mock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WaitingBroadcastReceiver extends BroadcastReceiver {

	protected boolean complete = false;
	private Intent intent;
	private final Object lock = new Object();

	@Override
	public void onReceive(Context context, Intent intent) {
		synchronized (lock) {
			this.intent = intent;
			this.complete = true;
		}
	}

	public Intent waitForCompletion(long timeout) throws InterruptedException {
		final long endTime = System.currentTimeMillis() + timeout;
		while (!complete) {
			if (System.currentTimeMillis() < endTime) {
				// keep waiting
				Thread.sleep(100);
			} else {
				throw new InterruptedException("timed out (over " + timeout + "ms elapsed waiting for completion)");
			}
		}
		return intent;
	}

	public Intent waitForCompletion() throws InterruptedException {
		return waitForCompletion(10000);
	}

	public Intent getIntent() {
		return intent;
	}

}
