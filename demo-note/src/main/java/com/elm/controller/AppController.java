package com.elm.controller;

import android.content.Context;
import com.elm.bean.Note;
import com.elm.presenter.NoteEditPresenter;
import com.elm.presenter.NoteListPresenter;
import com.elm.utility.LocalBroadcastUtility;
import com.elm.view.NoteEditView;
import com.elm.view.NoteListView;

import java.io.Serializable;

public interface AppController extends Serializable {

	public void editNote(Context context, Note note);

	NoteListPresenter getNoteListPresenter(Context context, NoteListView noteListView);

	NoteEditPresenter getNoteEditPresenter(Context context, NoteEditView noteEditView, Note note);

	LocalBroadcastUtility getLocalBroadcastUtility(Context context);

}
