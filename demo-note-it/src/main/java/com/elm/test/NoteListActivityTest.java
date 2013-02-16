package com.elm.test;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import com.elm.R;
import com.elm.bean.Note;
import com.elm.controller.AppController;
import com.elm.controller.impl.AppControllerImpl;
import com.elm.presenter.NoteListPresenter;
import com.elm.test.mock.MockAppController;
import com.elm.test.mock.WaitingBroadcastReceiver;
import com.elm.view.impl.NoteListActivity;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

public class NoteListActivityTest extends ActivityInstrumentationTestCase2<NoteListActivity> implements Serializable {

	private static final String TAG = NoteListActivityTest.class.getName();
	public static final String EDIT_ACTION = TAG + ":editReceiver";

	public NoteListActivityTest() {
		super(NoteListActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(true);
	}

	public void test_should_call_view_ready_and_release() {

		final Instrumentation instrumentation = getInstrumentation();

		AppController mockController = prepareController();

		final Intent intent = new Intent().putExtra(AppController.class.getName(), mockController);

		setActivityIntent(intent);

		NoteListActivity activity = getActivity();
		assertNotNull(activity);

		// todo: verify viewReady was called

		instrumentation.callActivityOnPause(activity);

		// todo: verify release was called

		activity.finish();

	}

	public void test_should_edit_new_note_from_add_button() {

		final Instrumentation instrumentation = getInstrumentation();

		AppController mockController = prepareController();

		final Intent intent = new Intent().putExtra(AppController.class.getName(), mockController);

		setActivityIntent(intent);

		NoteListActivity activity = getActivity();
		assertNotNull(activity);

		instrumentation.invokeMenuActionSync(activity, R.id.add_new, 0);

		// todo: verify add was called

		activity.finish();

	}

	public void test_should_bootstrap_controller() {

		final Instrumentation instrumentation = getInstrumentation();

		AppController mockController = prepareController();
		AppControllerImpl.setInstance(mockController);

		NoteListActivity activity = getActivity();
		assertNotNull(activity);

		// todo: verify controller retrieved from default impl

		activity.finish();

		// clear it to make other tests behave properly
		AppControllerImpl.setInstance(null);

	}

	public void test_should_render_note_list_with_items_that_respond_to_click() throws Throwable {

		final Instrumentation instrumentation = getInstrumentation();
		final Context context = instrumentation.getTargetContext();

		AppController mockController = prepareController();

		final Intent intent = new Intent().putExtra(AppController.class.getName(), mockController);

		setActivityIntent(intent);

		final NoteListActivity activity = getActivity();
		assertNotNull(activity);

		// this just needs to be a unique value - not a magic number...
		final long noteId = System.currentTimeMillis();

		final LinkedList<Note> noteList = new LinkedList<Note>();
		noteList.add(new Note(noteId, new Date(), "test note", ""));

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				activity.setNoteList(noteList);
			}
		});

		// verify list was created
		instrumentation.waitForIdleSync();
		final ListView list = (ListView) activity.findViewById(R.id.note_list);

		// make sure that item #0 has the expected id
		assertEquals(noteId, list.getAdapter().getItemId(0));

		// wait for the touch event - it'll broadcast a message when it's touched
		final WaitingBroadcastReceiver editReceiver = new WaitingBroadcastReceiver();
		final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
		manager.registerReceiver(editReceiver, new IntentFilter(EDIT_ACTION));

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				list.performItemClick(list, 0, noteId);
			}
		});

		// this will be notified when the click happens or the test will fail
		editReceiver.waitForCompletion();

		// whew. Done.
		activity.finish();

	}

	private AppController prepareController() {
		return new TestAppController();
	}

	static class TestAppController extends MockAppController {

		@Override
		public void editNote(Note note, Context context) {
			final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
			manager.sendBroadcast(new Intent(EDIT_ACTION));
		}

		@Override
		public NoteListPresenter getNoteListPresenter(NoteListActivity noteListActivity) {
			return new MockNoteListPresenter();
		}

	}

	static class MockNoteListPresenter implements NoteListPresenter, Serializable {

		@Override
		public void viewReady() {

		}

		@Override
		public void release() {

		}
	}

}

