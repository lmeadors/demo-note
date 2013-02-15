package com.elm.test.controller;

import android.content.Context;
import com.elm.bean.Note;
import com.elm.controller.AppController;
import com.elm.presenter.NoteEditPresenter;
import com.elm.presenter.NoteListPresenter;
import com.elm.utility.LocalBroadcastUtility;
import com.elm.view.NoteEditView;
import com.elm.view.impl.NoteListActivity;

public class MockAppController implements AppController{

	@Override
	public void editNote(Note note, Context context) {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

	@Override
	public NoteListPresenter getNoteListPresenter(NoteListActivity noteListActivity) {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

	@Override
	public NoteEditPresenter getNoteEditPresenter(Context context, NoteEditView noteEditView, Note note) {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

	@Override
	public LocalBroadcastUtility getLocalBroadcastUtility(Context context) {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

}
