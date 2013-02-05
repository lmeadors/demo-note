package com.elm.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.elm.NoteDataSource;
import com.elm.NoteEditView;
import com.elm.bean.Message;
import com.elm.bean.Note;
import com.elm.utility.LocalBroadcastUtility;

public class NoteEditPresenter {

	private static final String TAG = NoteEditPresenter.class.getName();

	private final NoteEditView view;
	private final NoteDataSource dataSource;
	private final LocalBroadcastUtility localBroadcastUtility;
	private Note note;

	public NoteEditPresenter(NoteEditView view, NoteDataSource dataSource, Context context) {
		this.view = view;
		this.dataSource = dataSource;
		localBroadcastUtility = new LocalBroadcastUtility(context);
	}

	public void viewReady() {
		final Long noteId = note.getId();
		if(nullOrZero(noteId)){
			view.hideDelete();
		}
		view.setNote(note);
	}

	public void setNote(Note note) {
		this.note = note;
	}

	private boolean nullOrZero(Long noteId) {
		return noteId==null || noteId.equals(0l);
	}

	public void delete(Note note) {
		final Long noteId = note.getId();
		if (nullOrZero(noteId)) {
			// nothing to do - this is a new note
			noteDeleted(null);
		} else {
			BroadcastReceiver deleteReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					localBroadcastUtility.unregisterReceiver(this);
					noteDeleted(noteId);
				}
			};
			localBroadcastUtility.registerReceiver(deleteReceiver, NoteDataSource.DELETE_ACTION);
			dataSource.deleteAsync(noteId);
		}

	}

	private void noteDeleted(Long noteId) {
		view.noteDeleted(noteId);
	}

	public void save(Note note) {

		final Long noteId = note.getId();

		BroadcastReceiver saveReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				localBroadcastUtility.unregisterReceiver(this);
				//noinspection unchecked
				noteSaved(localBroadcastUtility.getPayload(intent, Message.class));
			}
		};

		if (nullOrZero(noteId)) {
			localBroadcastUtility.registerReceiver(saveReceiver, NoteDataSource.INSERT_ACTION);
			dataSource.insertAsync(note);
		} else {
			localBroadcastUtility.registerReceiver(saveReceiver, NoteDataSource.UPDATE_ACTION);
			dataSource.updateAsync(note);
		}

	}

	private void noteSaved(Message<Note> message) {
		view.setNote(message.getPayload());
		view.showDelete();
	}

}
