package com.elm.controller.impl;

import android.content.Context;
import android.content.Intent;
import com.elm.bean.Note;
import com.elm.controller.AppController;
import com.elm.model.impl.NoteDataSourceImpl;
import com.elm.presenter.NoteEditPresenter;
import com.elm.presenter.NoteListPresenter;
import com.elm.presenter.impl.NoteEditPresenterImpl;
import com.elm.presenter.impl.NoteListPresenterImpl;
import com.elm.utility.LocalBroadcastUtility;
import com.elm.view.NoteEditView;
import com.elm.view.impl.NoteEditActivity;
import com.elm.view.impl.NoteListActivity;

public class AppControllerImpl implements AppController {

	@Override
	public void editNote(Note note, Context context) {

		final Intent intent = new Intent(context, NoteEditActivity.class);

		// add the app controller to the intent
		intent.putExtra(AppController.class.getName(), this);

		// add the note to the intent
		intent.putExtra(Note.class.getName(), note);

		context.startActivity(intent);

	}

	@Override
	public NoteListPresenter getNoteListPresenter(NoteListActivity noteListActivity) {
		return new NoteListPresenterImpl(this, noteListActivity, new NoteDataSourceImpl(noteListActivity), noteListActivity);
	}

	@Override
	public NoteEditPresenter getNoteEditPresenter(Context context, NoteEditView noteEditView, Note note) {
		return new NoteEditPresenterImpl(this, noteEditView, new NoteDataSourceImpl(context), context, note);
	}

	@Override
	public LocalBroadcastUtility getLocalBroadcastUtility(Context context) {
		return new LocalBroadcastUtility(context);
	}

}
