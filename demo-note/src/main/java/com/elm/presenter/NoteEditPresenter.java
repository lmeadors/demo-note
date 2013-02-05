package com.elm.presenter;

import android.content.Context;
import com.elm.NoteDataSource;
import com.elm.NoteEditView;
import com.elm.utility.LocalBroadcastUtility;

public class NoteEditPresenter {

	private static final String TAG = NoteEditPresenter.class.getName();

	private final NoteEditView view;
	private final NoteDataSource dataSource;
	private final LocalBroadcastUtility localBroadcastUtility;

	public NoteEditPresenter(NoteEditView view, NoteDataSource dataSource, Context context) {
		this.view = view;
		this.dataSource = dataSource;
		localBroadcastUtility = new LocalBroadcastUtility(context);
	}

	public void viewReady() {

	}

}
