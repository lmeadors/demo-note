package com.elm.test.view.impl;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import com.elm.R;
import com.elm.bean.Note;
import com.elm.controller.AppController;
import com.elm.mock.MockAppController;
import com.elm.mock.WaitingBroadcastReceiver;
import com.elm.presenter.NoteEditPresenter;
import com.elm.utility.DateUtility;
import com.elm.view.NoteEditView;
import com.elm.view.impl.NoteEditActivity;

import java.util.Date;

public class NoteEditActivityTest extends ActivityInstrumentationTestCase2<NoteEditActivity> {

	private static final String TAG = NoteEditActivityTest.class.getName();
	public static final String VIEW_READY_ACTION = TAG + ":viewReady";
	public static final String RELEASE_ACTION = TAG + ":release";
	public static final String SHARE_ACTION = TAG + ":share";
	public static final String DELETE_ACTION = TAG + ":delete";
	public static final String SAVE_ACTION = TAG + ":save";

	private final DateUtility dateUtility = new DateUtility();

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

	public void test_should_delete_note() throws InterruptedException {

		final Instrumentation instrumentation = getInstrumentation();
		final Context context = instrumentation.getTargetContext();

		// need to add the note to edit and the application controller to the intent
		final Intent activityIntent = new Intent();
		activityIntent.putExtra(AppController.class.getName(), new TestAppController());
		activityIntent.putExtra(Note.class.getName(), new Note());
		setActivityIntent(activityIntent);

		// we need to wait (later) to see that the view requested a delete
		final WaitingBroadcastReceiver deleteReceiver = new WaitingBroadcastReceiver();
		final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
		manager.registerReceiver(deleteReceiver, new IntentFilter(DELETE_ACTION));

		// create the view
		final NoteEditActivity activity = getActivity();

		// wait for the ui to settle, then touch the menu
		instrumentation.waitForIdleSync();
		instrumentation.invokeMenuActionSync(activity, R.id.delete_note, 0);

		// wait for notification that the delete has been requested
		deleteReceiver.waitForCompletion();

		// all done
		activity.finish();

	}

	public void test_should_save_note() throws InterruptedException {

		final Instrumentation instrumentation = getInstrumentation();
		final Context context = instrumentation.getTargetContext();

		// need to add the note to edit and the application controller to the intent
		final Intent activityIntent = new Intent();
		activityIntent.putExtra(AppController.class.getName(), new TestAppController());
		activityIntent.putExtra(Note.class.getName(), new Note());
		setActivityIntent(activityIntent);

		// we need to wait (later) to see that the view requested a save
		final WaitingBroadcastReceiver saveReceiver = new WaitingBroadcastReceiver();
		final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
		manager.registerReceiver(saveReceiver, new IntentFilter(SAVE_ACTION));

		// create the view
		final NoteEditActivity activity = getActivity();

		// wait for the ui to settle, then touch the menu
		instrumentation.waitForIdleSync();
		instrumentation.invokeMenuActionSync(activity, R.id.save_note, 0);

		// wait for notification that the delete has been requested
		saveReceiver.waitForCompletion();

		// all done
		activity.finish();

	}

	public void test_should_update_ui_to_show_note() throws Throwable {

		final Instrumentation instrumentation = getInstrumentation();
		final Context context = instrumentation.getTargetContext();

		// need to add the note to edit and the application controller to the intent
		final Intent activityIntent = new Intent();
		activityIntent.putExtra(AppController.class.getName(), new TestAppController());
		activityIntent.putExtra(Note.class.getName(), new Note());
		setActivityIntent(activityIntent);

		// we need to wait (later) to see that the view requested a save
		final WaitingBroadcastReceiver saveReceiver = new WaitingBroadcastReceiver();
		final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
		manager.registerReceiver(saveReceiver, new IntentFilter(SAVE_ACTION));

		// create the view
		final NoteEditActivity activity = getActivity();

		// create the new note
		final Note note = new Note(System.currentTimeMillis(), new Date(), "test title", "test text");

		// tell it there's a new note to show
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				activity.setNote(note);
			}
		});

		// wait for the UI thread to settle
		instrumentation.waitForIdleSync();

		assertEquals(note.getId().toString(), ((TextView) activity.findViewById(R.id.note_id)).getText().toString());
		assertEquals(dateUtility.dateString(note.getDate()), ((TextView) activity.findViewById(R.id.note_date)).getText().toString());
		assertEquals(note.getTitle(), ((TextView) activity.findViewById(R.id.note_title)).getText().toString());
		assertEquals(note.getText(), ((TextView) activity.findViewById(R.id.note_text)).getText().toString());

	}

	public void test_should_hide_and_show_delete() throws Throwable {

		final Instrumentation instrumentation = getInstrumentation();
		final Context context = instrumentation.getTargetContext();

		// need to add the note to edit and the application controller to the intent
		final Intent activityIntent = new Intent();
		activityIntent.putExtra(AppController.class.getName(), new TestAppController());
		activityIntent.putExtra(Note.class.getName(), new Note());
		setActivityIntent(activityIntent);

		// we need to wait (later) to see that the view requested a save
		final WaitingBroadcastReceiver saveReceiver = new WaitingBroadcastReceiver();
		final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
		manager.registerReceiver(saveReceiver, new IntentFilter(SAVE_ACTION));

		// create the view
		final NoteEditActivity activity = getActivity();

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				assertFalse(activity.hideDelete());
				assertTrue(activity.showDelete());
			}
		});

		activity.finish();

	}

	public void test_should_finish_after_delete() throws Throwable {

		final Instrumentation instrumentation = getInstrumentation();
		final Context context = instrumentation.getTargetContext();

		// need to add the note to edit and the application controller to the intent
		final Intent activityIntent = new Intent();
		activityIntent.putExtra(AppController.class.getName(), new TestAppController());
		activityIntent.putExtra(Note.class.getName(), new Note());
		setActivityIntent(activityIntent);

		// we need to wait (later) to see that the view requested a save
		final WaitingBroadcastReceiver saveReceiver = new WaitingBroadcastReceiver();
		final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
		manager.registerReceiver(saveReceiver, new IntentFilter(SAVE_ACTION));

		// create the view
		final NoteEditActivity activity = getActivity();

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				activity.noteDeleted(1l);
			}
		});

		//activity.finish();
		instrumentation.waitForIdleSync();
		assertTrue(activity.isFinishing());

	}

	public void test_should_notify_after_save() throws Throwable {

		final Instrumentation instrumentation = getInstrumentation();
		final Context context = instrumentation.getTargetContext();

		// need to add the note to edit and the application controller to the intent
		final Intent activityIntent = new Intent();
		activityIntent.putExtra(AppController.class.getName(), new TestAppController());
		activityIntent.putExtra(Note.class.getName(), new Note());
		setActivityIntent(activityIntent);

		// we need to wait (later) to see that the view requested a save
		final WaitingBroadcastReceiver saveReceiver = new WaitingBroadcastReceiver();
		final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
		manager.registerReceiver(saveReceiver, new IntentFilter(SAVE_ACTION));

		// create the view
		final NoteEditActivity activity = getActivity();

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				activity.noteSaved(new Note());
			}
		});

		// nothing to verify here - just want to get here and know nothing exploded
		instrumentation.waitForIdleSync();
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
			manager.sendBroadcast(new Intent(DELETE_ACTION));
		}

		@Override
		public void save(Note note) {
			Log.d(TAG, "save note: " + note);
			manager.sendBroadcast(new Intent(SAVE_ACTION));
		}

		@Override
		public void prepareShareIntent(ShareActionProvider provider, String subject, String text) {
			Log.d(TAG, "preparing to share: " + subject + " / " + text);
			manager.sendBroadcast(new Intent(SHARE_ACTION));
		}

	}

}
