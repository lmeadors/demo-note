package com.elm.test;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import com.elm.controller.AppController;
import com.elm.presenter.NoteListPresenter;
import com.elm.test.controller.MockAppController;
import com.elm.view.impl.NoteListActivity;

import java.io.Serializable;

public class NoteListActivityTest extends ActivityInstrumentationTestCase2<NoteListActivity> implements Serializable {


	public NoteListActivityTest() {
		super(NoteListActivity.class);
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

	private AppController prepareController() {
		return new TestAppController();
	}

	static class TestAppController extends MockAppController {

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

