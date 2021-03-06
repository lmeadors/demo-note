package com.elm.presenter.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ShareActionProvider;
import com.elm.bean.Message;
import com.elm.bean.Note;
import com.elm.controller.AppController;
import com.elm.model.NoteDataSource;
import com.elm.presenter.NoteEditPresenter;
import com.elm.utility.LocalBroadcastUtility;
import com.elm.utility.NullUtility;
import com.elm.view.NoteEditView;

public class NoteEditPresenterImpl implements NoteEditPresenter {

	private static final String TAG = NoteEditPresenterImpl.class.getName();

	private final NullUtility nullUtility = new NullUtility();

	private final NoteEditView view;
	private final NoteDataSource dataSource;
	private final LocalBroadcastUtility localBroadcastUtility;

	private BroadcastReceiver deleteReceiver;
	private BroadcastReceiver saveReceiver;

	public NoteEditPresenterImpl(AppController appController, NoteEditView noteEditView, NoteDataSource dataSource, Context context) {
		this.dataSource = dataSource;
		this.localBroadcastUtility = appController.getLocalBroadcastUtility(context);
		this.view = noteEditView;
	}

	@Override
	public void viewReady(Note note) {

		if (nullUtility.nullOrZero(note.getId())) {
			view.hideDelete();
		} else {
			view.showDelete();
		}

		view.setNote(note);

	}

	@Override
	public void delete(Note note) {

		final Long noteId = note.getId();

		if (nullUtility.nullOrZero(noteId)) {

			Log.d(TAG, "cannot delete note with id: " + noteId);
			view.noteDeleted(null);

		} else {

			Log.d(TAG, "delete note with id: " + noteId);

			deleteReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					localBroadcastUtility.unregisterReceiver(this);
					view.noteDeleted(noteId);
				}
			};

			localBroadcastUtility.registerReceiver(deleteReceiver, NoteDataSource.DELETE_ACTION);

			dataSource.deleteAsync(noteId);

		}

	}

	@Override
	public void save(Note note) {

		final Long noteId = note.getId();

		saveReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				localBroadcastUtility.unregisterReceiver(this);

				@SuppressWarnings("unchecked")
				final Message<Note> payload = localBroadcastUtility.getPayload(intent, Message.class);

				view.setNote(payload.getPayload());
				view.showDelete();

			}
		};

		if (nullUtility.nullOrZero(noteId)) {
			localBroadcastUtility.registerReceiver(saveReceiver, NoteDataSource.INSERT_ACTION);
			dataSource.insertAsync(note);
		} else {
			localBroadcastUtility.registerReceiver(saveReceiver, NoteDataSource.UPDATE_ACTION);
			dataSource.updateAsync(note);
		}

		view.noteSaved(note);

	}

	@Override
	public void prepareShareIntent(ShareActionProvider provider, String subject, String text) {

		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		// Add data to the intent, the receiving app will decide what to do with it.
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);

		provider.setShareIntent(intent);

	}

	@Override
	public void release() {
		localBroadcastUtility.unregisterReceiver(deleteReceiver);
		localBroadcastUtility.unregisterReceiver(saveReceiver);
		dataSource.release();
	}

}
