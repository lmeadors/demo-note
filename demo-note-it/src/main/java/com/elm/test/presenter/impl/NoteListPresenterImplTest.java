package com.elm.test.presenter.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.test.InstrumentationTestCase;
import com.elm.bean.Message;
import com.elm.bean.Note;
import com.elm.controller.AppController;
import com.elm.mock.MockAppController;
import com.elm.mock.MockNoteDataSource;
import com.elm.mock.MockNoteListView;
import com.elm.mock.WaitingBroadcastReceiver;
import com.elm.model.NoteDataSource;
import com.elm.presenter.NoteListPresenter;
import com.elm.presenter.impl.NoteListPresenterImpl;
import com.elm.utility.LocalBroadcastUtility;
import com.elm.view.NoteListView;

import java.util.LinkedList;
import java.util.List;

public class NoteListPresenterImplTest extends InstrumentationTestCase {

	private LocalBroadcastUtility localBroadcastUtility;

	public void test_should_send_view_note_list_when_ready() throws InterruptedException {

		final Context context = getInstrumentation().getTargetContext();

		// this is used for this test as is
		localBroadcastUtility = new LocalBroadcastUtility(context);

		final AppController controller = getTestAppController();
		final NoteListView view = getTestNoteListView();
		final List<Note> expectedNoteList = new LinkedList<Note>();
		final NoteDataSource dataSource = getTestNoteDataSource(context, expectedNoteList);

		final NoteListPresenter presenter = new NoteListPresenterImpl(controller, view, dataSource, context);

		final WaitingBroadcastReceiver noteListReceiver = new WaitingBroadcastReceiver();
		final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
		manager.registerReceiver(noteListReceiver, new IntentFilter(NoteDataSource.LIST_ACTION));

		// tell the presenter the view is ready
		presenter.viewReady();

		final Intent intent = noteListReceiver.waitForCompletion();
		assertNotNull(intent);

		@SuppressWarnings("unchecked")
		List<Note> actualNoteList = ((Message<List<Note>>) intent.getSerializableExtra(Message.class.getName())).getPayload();
		assertNotNull(actualNoteList);
		assertTrue(actualNoteList == expectedNoteList);

	}

	public void test_should_unregister_reciever_when_released() {

		final Context context = getInstrumentation().getTargetContext();

		final List<BroadcastReceiver> unregisteredReceivers = new LinkedList<BroadcastReceiver>();
		localBroadcastUtility = new LocalBroadcastUtility(context) {
			@Override
			public void unregisterReceiver(BroadcastReceiver receiver) {
				unregisteredReceivers.add(receiver);
				super.unregisterReceiver(receiver);
			}
		};

		final AppController controller = getTestAppController();
		final NoteListView view = getTestNoteListView();
		final List<Note> expectedNoteList = new LinkedList<Note>();
		final NoteDataSource dataSource = getTestNoteDataSource(context, expectedNoteList);

		final NoteListPresenter presenter = new NoteListPresenterImpl(controller, view, dataSource, context);

		assertEquals(0, unregisteredReceivers.size());
		presenter.release();
		assertEquals(1, unregisteredReceivers.size());

	}

	private NoteDataSource getTestNoteDataSource(final Context context, List<Note> noteList) {

		final List<Note> expectedNoteList = noteList;

		return new MockNoteDataSource() {

			@Override
			public void listAsync() {
				final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
				final Intent intent = new Intent(NoteDataSource.LIST_ACTION);
				intent.putExtra(Message.class.getName(), new Message<List<Note>>(expectedNoteList));
				manager.sendBroadcast(intent);
			}

			@Override
			public void release() {
				// todo: nothing here yet
			}

			@Override
			public void init() {
				// todo: nothing here yet
			}

		};
	}

	private NoteListView getTestNoteListView() {
		return new MockNoteListView() {
			@Override
			public void setNoteList(List<Note> noteList) {
				// nothing here - we just
			}
		};
	}

	private AppController getTestAppController() {

		return new MockAppController() {

			@Override
			public LocalBroadcastUtility getLocalBroadcastUtility(Context context) {
				return localBroadcastUtility;
			}

		};

	}

}
