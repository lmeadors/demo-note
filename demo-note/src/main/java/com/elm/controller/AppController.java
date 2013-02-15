package com.elm.controller;

import android.content.Context;
import com.elm.utility.LocalBroadcastUtility;
import com.elm.view.NoteEditView;
import com.elm.bean.Note;
import com.elm.presenter.NoteEditPresenter;
import com.elm.presenter.NoteListPresenter;
import com.elm.view.impl.NoteListActivity;

import java.io.Serializable;

public interface AppController extends Serializable {

	public void editNote(Note note, Context context);

	NoteListPresenter getNoteListPresenter(NoteListActivity noteListActivity);

	NoteEditPresenter getNoteEditPresenter(Context context, NoteEditView noteEditView, Note note);

	LocalBroadcastUtility getLocalBroadcastUtility(Context context);

}
