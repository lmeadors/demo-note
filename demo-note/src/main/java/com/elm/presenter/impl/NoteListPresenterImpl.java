package com.elm.presenter.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.elm.model.NoteDataSource;
import com.elm.view.NoteListView;
import com.elm.controller.AppController;
import com.elm.bean.Message;
import com.elm.bean.Note;
import com.elm.presenter.NoteListPresenter;
import com.elm.utility.LocalBroadcastUtility;

import java.util.List;

public class NoteListPresenterImpl implements NoteListPresenter {

	private static final String TAG = NoteListPresenterImpl.class.getName();

	private final NoteListView view;
	private final NoteDataSource dataSource;
	private final LocalBroadcastUtility localBroadcastUtility;

	private BroadcastReceiver listLoadedReceiver;

	public NoteListPresenterImpl(AppController appController, NoteListView view, NoteDataSource dataSource, Context context) {
		this.view = view;
		this.dataSource = dataSource;
		this.localBroadcastUtility = appController.getLocalBroadcastUtility(context);
	}

	public void viewReady() {

		Log.d(TAG, "getting data for view");

		Log.d(TAG, "creating receiver");
		listLoadedReceiver = new BroadcastReceiver() {
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
		localBroadcastUtility.registerReceiver(listLoadedReceiver, NoteDataSource.LIST_ACTION);

		Log.d(TAG, "building list (async)");
		dataSource.init();
		dataSource.listAsync();

	}

	@Override
	public void release() {
		localBroadcastUtility.unregisterReceiver(listLoadedReceiver);
		dataSource.release();
	}

	private void onNoteListLoaded(Message<List<Note>> message) {
		Log.d(TAG, "notes loaded");
		view.setNoteList(message.getPayload());
	}


}
