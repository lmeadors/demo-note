package com.elm.presenter.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ShareActionProvider;
import com.elm.bean.Message;
import com.elm.bean.Note;
import com.elm.controller.AppController;
import com.elm.model.NoteDataSource;
import com.elm.presenter.NoteEditPresenter;
import com.elm.utility.LocalBroadcastUtility;
import com.elm.view.NoteEditView;

public class NoteEditPresenterImpl implements NoteEditPresenter {

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

	public void release() {
		localBroadcastUtility.unregisterReceiver(deleteReceiver);
		localBroadcastUtility.unregisterReceiver(saveReceiver);
	}

	public void viewReady(Note note) {
		final Long noteId = note.getId();
		if (nullOrZero(noteId)) {
			view.hideDelete();
		}
		view.setNote(note);
	}

	private boolean nullOrZero(Long noteId) {
		return noteId == null || noteId.equals(0l);
	}

	public void delete(Note note) {
		final Long noteId = note.getId();
		if (nullOrZero(noteId)) {
			// nothing to do - this is a new note
			view.noteDeleted(null);
		} else {
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

		if (nullOrZero(noteId)) {
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

		if (provider != null) {

			final Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

			// Add data to the intent, the receiving app will decide what to do with it.
			intent.putExtra(Intent.EXTRA_SUBJECT, subject);
			intent.putExtra(Intent.EXTRA_TEXT, text);

			provider.setShareIntent(intent);

		}

	}
}
