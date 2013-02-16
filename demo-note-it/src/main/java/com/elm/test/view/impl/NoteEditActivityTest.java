package com.elm.test.view.impl;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ShareActionProvider;
import com.elm.R;
import com.elm.bean.Note;
import com.elm.controller.AppController;
import com.elm.mock.MockAppController;
import com.elm.mock.WaitingBroadcastReceiver;
import com.elm.presenter.NoteEditPresenter;
import com.elm.view.NoteEditView;
import com.elm.view.impl.NoteEditActivity;

public class NoteEditActivityTest extends ActivityInstrumentationTestCase2<NoteEditActivity> {

	private static final String TAG = NoteEditActivityTest.class.getName();
	public static final String VIEW_READY_ACTION = TAG + ":viewReady";
	public static final String RELEASE_ACTION = TAG + ":release";
	public static final String SHARE_ACTION = TAG + ":share";


	public NoteEditActivityTest() {
		super(NoteEditActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
	}

	public void test_should_call_view_ready_and_release() throws InterruptedException {

		final Instrumentation instrumentation = getInstrumentation();
		final Context context = instrumentation.getTargetContext();

		// need to add the note to edit and the application controller to the intent
		final Intent activityIntent = new Intent();
		activityIntent.putExtra(AppController.class.getName(), new TestAppController());
		activityIntent.putExtra(Note.class.getName(), new Note());
		setActivityIntent(activityIntent);

		// we need to wait (later) to see that the view notified the presenter that it was ready
		WaitingBroadcastReceiver viewReadyReceiver = new WaitingBroadcastReceiver();
		final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
		manager.registerReceiver(viewReadyReceiver, new IntentFilter(VIEW_READY_ACTION));

		// create the view
		final NoteEditActivity activity = getActivity();

		// wait for notification that the view is ready
		viewReadyReceiver.waitForCompletion();

		// we need to know that the presenter is released when the activity pauses
		WaitingBroadcastReceiver releaseReceiver = new WaitingBroadcastReceiver();
		manager.registerReceiver(releaseReceiver, new IntentFilter(RELEASE_ACTION));

		// pause the activity
		instrumentation.callActivityOnPause(activity);

		// wait for notification that the activity was paused
		releaseReceiver.waitForCompletion();

		// all done
		activity.finish();

	}

	public void test_should_prepare_share_intent_on_share() throws InterruptedException {

		final Instrumentation instrumentation = getInstrumentation();
		final Context context = instrumentation.getTargetContext();

		// need to add the note to edit and the application controller to the intent
		final Intent activityIntent = new Intent();
		activityIntent.putExtra(AppController.class.getName(), new TestAppController());
		activityIntent.putExtra(Note.class.getName(), new Note());
		setActivityIntent(activityIntent);

		// we need to wait (later) to see that the view notified the presenter that it was ready
		final WaitingBroadcastReceiver shareReceiver = new WaitingBroadcastReceiver();
		final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
		manager.registerReceiver(shareReceiver, new IntentFilter(SHARE_ACTION));

		// create the view
		final NoteEditActivity activity = getActivity();

		// wait for the ui to settle, then touch the menu
		instrumentation.waitForIdleSync();
		instrumentation.invokeMenuActionSync(activity, R.id.share_note, 0);

		// wait for notification that the share info has been updated
		shareReceiver.waitForCompletion();

		// all done
		activity.finish();

	}


	static class TestAppController extends MockAppController {
		@Override
		public NoteEditPresenter getNoteEditPresenter(Context context, NoteEditView noteEditView, Note note) {
			return new TestNoteEditPresenter(context);
		}
	}

	static class TestNoteEditPresenter implements NoteEditPresenter {

		private final LocalBroadcastManager manager;

		public TestNoteEditPresenter(Context context) {
			manager = LocalBroadcastManager.getInstance(context);
		}

		@Override
		public void release() {
			Log.d(TAG, "release called");
			manager.sendBroadcast(new Intent(RELEASE_ACTION));
		}

		@Override
		public void viewReady() {
			Log.d(TAG, "view is ready");
			manager.sendBroadcast(new Intent(VIEW_READY_ACTION));
		}

		@Override
		public void delete(Note note) {
			Log.d(TAG, "delete note: " + note);
		}

		@Override
		public void save(Note note) {
			Log.d(TAG, "save note: " + note);
		}

		@Override
		public void prepareShareIntent(ShareActionProvider provider, String subject, String text) {
			Log.d(TAG, "preparing to share: " + subject + " / " + text);
			manager.sendBroadcast(new Intent(SHARE_ACTION));
		}

	}

}
