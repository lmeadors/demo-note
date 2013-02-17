package com.elm.test.controller.impl;

import android.content.Context;
import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import com.elm.bean.Note;
import com.elm.controller.AppController;
import com.elm.controller.impl.AppControllerImpl;
import com.elm.mock.MockNoteEditView;
import com.elm.mock.MockNoteListView;
import com.elm.view.impl.NoteEditActivity;

import java.util.LinkedList;
import java.util.List;

public class AppControllerImplTest extends InstrumentationTestCase {

	private Context context;
	private List<Intent> intentList;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		AppControllerImpl.setInstance(null);
		context = getInstrumentation().getTargetContext();
		intentList = new LinkedList<Intent>();
	}

	public void test_should_bootstrap_new_instance_if_needed() {

		final AppController instance = AppControllerImpl.getInstance();

		assertNotNull(instance);
		assertEquals(AppControllerImpl.class, instance.getClass());

	}

	public void test_should_create_local_broadcast_utility() {

		assertNotNull(AppControllerImpl.getInstance().getLocalBroadcastUtility(context));

	}

	public void test_should_create_note_list_presenter() {

		assertNotNull(AppControllerImpl.getInstance().getNoteListPresenter(context, new MockNoteListView()));

	}

	public void test_should_create_note_edit_presenter() {

		assertNotNull(AppControllerImpl.getInstance().getNoteEditPresenter(context, new MockNoteEditView()));

	}

	public void test_should_start_activity_to_edit_note() {

		final AppController controller = AppControllerImpl.getInstance();

		Context mockContext = getMockContext(context);

		assertTrue(intentList.isEmpty());
		controller.editNote(mockContext, new Note());
		assertFalse(intentList.isEmpty());
		final Intent intent = intentList.get(0);
		assertNotNull(intent);
		assertNotNull(intent.getSerializableExtra(Note.class.getName()));
		assertEquals(NoteEditActivity.class.getName(), intent.getComponent().getClassName());

	}

	private MockContext getMockContext(final Context context) {
		return new MockContext(){
			@Override
			public String getPackageName() {
				return context.getPackageName();
			}

			@Override
			public void startActivity(Intent intent) {
				intentList.add(intent);
			}

		};
	}

}
