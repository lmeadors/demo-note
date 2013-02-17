package com.elm.test.presenter.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.test.InstrumentationTestCase;
import android.widget.ShareActionProvider;
import com.elm.bean.Message;
import com.elm.bean.Note;
import com.elm.controller.AppController;
import com.elm.mock.MockAppController;
import com.elm.mock.MockNoteDataSource;
import com.elm.mock.MockNoteEditView;
import com.elm.mock.WaitingBroadcastReceiver;
import com.elm.model.NoteDataSource;
import com.elm.presenter.NoteEditPresenter;
import com.elm.presenter.impl.NoteEditPresenterImpl;
import com.elm.utility.LocalBroadcastUtility;
import com.elm.utility.NullUtility;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class NoteEditPresenterImplTest extends InstrumentationTestCase {

	public static final Note NEW_ADDED_NOTE = new Note();
	public static final Note EXISTING_NOTE = new Note(1l, new Date(), "test title", "test note");

	private Context context;
	private ViewState viewState;
	private NoteEditPresenter presenter;
	private List<Long> deletedNoteIdList;
	private List<Long> insertedNoteIdList;
	private List<Long> updatedNoteIdList;
	private List<BroadcastReceiver> unregisteredReceiverList;

	@Override
	public void setUp() throws Exception {

		super.setUp();

		insertedNoteIdList = new LinkedList<Long>();
		updatedNoteIdList = new LinkedList<Long>();
		deletedNoteIdList = new LinkedList<Long>();

		unregisteredReceiverList = new LinkedList<BroadcastReceiver>();

		viewState = new ViewState();

		context = getInstrumentation().getTargetContext();
		assertNotNull(context);

		final AppController controller = getMockAppController();
		final MockNoteEditView view = getMockNoteEditView(viewState);
		final MockNoteDataSource dataSource = getMockNoteDataSource(context);

		presenter = new NoteEditPresenterImpl(controller, view, dataSource, context);

	}

	public void test_should_hide_delete_on_new_note() {

		// make sure that the state is as we expect it before
		assertFalse(viewState.showDelete);
		assertFalse(viewState.hideDelete);
		assertNull(viewState.note);

		// run the test
		presenter.viewReady(NEW_ADDED_NOTE);

		// verify the state
		assertFalse(viewState.showDelete);
		assertTrue(viewState.hideDelete);
		assertNotNull(viewState.note);

		viewState.note = null;
		presenter.viewReady(new Note(1l, new Date(), "test title", "test note"));

	}

	public void test_should_show_delete_on_existing_note() {

		// make sure that the state is as we expect it before
		assertFalse(viewState.showDelete);
		assertFalse(viewState.hideDelete);
		assertNull(viewState.note);

		// run the test
		presenter.viewReady(EXISTING_NOTE);

		// verify the state
		assertTrue(viewState.showDelete);
		assertFalse(viewState.hideDelete);
		assertNotNull(viewState.note);

	}

	public void test_should_not_delete_note_without_id() {

		assertTrue(deletedNoteIdList.isEmpty());

		presenter.delete(NEW_ADDED_NOTE);

		assertFalse(deletedNoteIdList.isEmpty());
		assertNull(deletedNoteIdList.get(0));

	}

	public void test_should_delete_note_with_id() throws InterruptedException {

		final WaitingBroadcastReceiver deleteReceiver = new WaitingBroadcastReceiver();
		final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
		manager.registerReceiver(deleteReceiver, new IntentFilter(NoteDataSource.DELETE_ACTION));

		assertTrue(deletedNoteIdList.isEmpty());

		presenter.delete(EXISTING_NOTE);
		deleteReceiver.waitForCompletion();

		assertFalse(deletedNoteIdList.isEmpty());

	}

	public void test_should_insert_new_note_on_save() {

		final WaitingBroadcastReceiver insertReceiver = new WaitingBroadcastReceiver();
		final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
		manager.registerReceiver(insertReceiver, new IntentFilter(NoteDataSource.INSERT_ACTION));

		assertTrue(insertedNoteIdList.isEmpty());
		assertTrue(updatedNoteIdList.isEmpty());

		presenter.save(NEW_ADDED_NOTE);

		assertFalse(insertedNoteIdList.isEmpty());
		assertTrue(updatedNoteIdList.isEmpty());

	}

	public void test_should_update_existing_note_on_save() {

		final WaitingBroadcastReceiver updateReceiver = new WaitingBroadcastReceiver();
		final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
		manager.registerReceiver(updateReceiver, new IntentFilter(NoteDataSource.UPDATE_ACTION));

		assertTrue(insertedNoteIdList.isEmpty());
		assertTrue(updatedNoteIdList.isEmpty());

		presenter.save(EXISTING_NOTE);

		assertTrue(insertedNoteIdList.isEmpty());
		assertFalse(updatedNoteIdList.isEmpty());

	}

	public void test_share_intent_should_get_populated_with_title_and_text() {

		final String expectedTitle = "title";
		final String expectedText = "text here";

		final ShareActionProvider provider = new ShareActionProvider(context) {
			@Override
			public void setShareIntent(Intent intent) {
				assertEquals(expectedTitle, intent.getStringExtra(Intent.EXTRA_SUBJECT));
				assertEquals(expectedText, intent.getStringExtra(Intent.EXTRA_TEXT));
			}
		};

		presenter.prepareShareIntent(provider, expectedTitle, expectedText);

	}

	public void test_release_should_unregister_receivers() {

		assertTrue(unregisteredReceiverList.isEmpty());
		presenter.release();
		assertFalse(unregisteredReceiverList.isEmpty());
		assertEquals(2, unregisteredReceiverList.size());

	}

	private MockNoteDataSource getMockNoteDataSource(final Context context) {
		return new MockNoteDataSource() {
			final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);

			@Override
			public void deleteAsync(Long id) {
				manager.sendBroadcast(new Intent(NoteDataSource.DELETE_ACTION));
			}

			@Override
			public void insertAsync(Note note) {
				final Intent intent = new Intent(NoteDataSource.INSERT_ACTION);
				intent.putExtra(Message.class.getName(), new Message<Note>(note));
				manager.sendBroadcast(intent);
			}

			@Override
			public void updateAsync(Note note) {
				final Intent intent = new Intent(NoteDataSource.UPDATE_ACTION);
				intent.putExtra(Message.class.getName(), new Message<Note>(note));
				manager.sendBroadcast(intent);
			}
		};
	}

	private MockNoteEditView getMockNoteEditView(final ViewState viewState) {

		return new MockNoteEditView() {

			NullUtility nullUtility = new NullUtility();

			@Override
			public boolean hideDelete() {
				viewState.hideDelete = true;
				return false;
			}

			@Override
			public boolean showDelete() {
				viewState.showDelete = true;
				return true;
			}

			@Override
			public void setNote(Note note) {
				viewState.note = note;
			}

			@Override
			public void noteDeleted(Long noteId) {
				deletedNoteIdList.add(noteId);
			}

			@Override
			public void noteSaved(Note note) {
				if (nullUtility.nullOrZero(note.getId())) {
					insertedNoteIdList.add(note.getId());
				} else {
					updatedNoteIdList.add(note.getId());
				}
			}
		};

	}

	private MockAppController getMockAppController() {
		return new MockAppController() {
			@Override
			public LocalBroadcastUtility getLocalBroadcastUtility(Context context) {
				return new LocalBroadcastUtility(context) {
					@Override
					public void unregisterReceiver(BroadcastReceiver receiver) {
						super.unregisterReceiver(receiver);
						unregisteredReceiverList.add(receiver);
					}
				};
			}
		};
	}

	class ViewState {
		private boolean hideDelete = false;
		private boolean showDelete = false;
		private Note note = null;
	}

}
