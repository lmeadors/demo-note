package com.elm.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.elm.NoteDataSource;
import com.elm.NoteListView;
import com.elm.bean.Message;
import com.elm.bean.Note;
import com.elm.utility.LocalBroadcastUtility;

import java.util.List;

public class NoteListPresenter {

	private static final String TAG = NoteListPresenter.class.getName();

	private final NoteListView view;
	private final NoteDataSource dataSource;
	private final LocalBroadcastUtility localBroadcastUtility;

	public NoteListPresenter(NoteListView view, NoteDataSource dataSource, Context context) {
		this.view = view;
		this.dataSource = dataSource;
		localBroadcastUtility = new LocalBroadcastUtility(context);
	}

	public void viewReady() {

		Log.d(TAG, "getting data for view");

		Log.d(TAG, "creating receiver");
		BroadcastReceiver receiver = new BroadcastReceiver() {
			@SuppressWarnings("unchecked")
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d(TAG, "message received");
				localBroadcastUtility.unregisterReceiver(this);
				final Message message = localBroadcastUtility.getPayload(intent, Message.class);
				onNoteListLoaded(message);
			}
		};

		Log.d(TAG, "registering receiver for " + NoteDataSource.LIST_ACTION);
		localBroadcastUtility.registerReceiver(receiver, NoteDataSource.LIST_ACTION);

		Log.d(TAG, "building list (async)");
		dataSource.listAsync();

	}

	private void onNoteListLoaded(Message<List<Note>> message) {
		Log.d(TAG, "notes loaded");
		view.setNoteList(message.getPayload());
	}

}
