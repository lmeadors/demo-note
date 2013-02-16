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
import com.elm.view.NoteListView;
import com.elm.view.impl.NoteEditActivity;

public class AppControllerImpl implements AppController {

	private static AppController instance;

	@Override
	public void editNote(Context context, Note note) {

		final Intent intent = new Intent(context, NoteEditActivity.class);

		// add the app controller to the intent
		intent.putExtra(AppController.class.getName(), this);

		// add the note to the intent
		intent.putExtra(Note.class.getName(), note);

		context.startActivity(intent);

	}

	@Override
	public NoteListPresenter getNoteListPresenter(Context context, NoteListView view) {
		return new NoteListPresenterImpl(this, view, new NoteDataSourceImpl(context), context);
	}

	@Override
	public NoteEditPresenter getNoteEditPresenter(Context context, NoteEditView noteEditView, Note note) {
		return new NoteEditPresenterImpl(this, noteEditView, new NoteDataSourceImpl(context), context, note);
	}

	@Override
	public LocalBroadcastUtility getLocalBroadcastUtility(Context context) {
		return new LocalBroadcastUtility(context);
	}

	public static AppController getInstance(){
		if(null == instance){
			instance = new AppControllerImpl();
		}
		return instance;
	}

	public static void setInstance(AppController instance) {
		AppControllerImpl.instance = instance;
	}

}
