package com.elm.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.elm.view.NoteListView;
import com.elm.R;
import com.elm.controller.AppController;
import com.elm.controller.impl.AppControllerImpl;
import com.elm.bean.Note;
import com.elm.presenter.NoteListPresenter;
import com.elm.adapter.NoteListAdapter;

import java.util.List;

public class NoteListActivity extends Activity implements NoteListView {

	private static final String TAG = NoteListActivity.class.getName();

	private NoteListPresenter presenter;
	private AppController controller;

	private ListView noteListing;

	public NoteListActivity() {
		Log.d(TAG, "creating activity...");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.d(TAG, "creating activity");
		super.onCreate(savedInstanceState);

		controller = findAppController();

		presenter = findPresenter();

		Log.d(TAG, "setting view");
		setContentView(R.layout.note_list);

		Log.d(TAG, "getting ui components");
		noteListing = (ListView) findViewById(R.id.note_list);

	}

	@Override
	protected void onPause() {
		super.onPause();
		presenter.release();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(TAG, "creating menu for list activity");
		getMenuInflater().inflate(R.menu.note_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// only one menu option here...
		controller.editNote(new Note(), this);
		return true;
	}

	@Override
	protected void onResume() {
		presenter.viewReady();
		Log.d(TAG, "view is ready");
		super.onResume();
	}

	public void setNoteList(List<Note> noteList) {

		Log.d(TAG, "adding " + noteList.size() + " notes");

		noteListing.setAdapter(new NoteListAdapter(this, R.layout.note_list_item, noteList));
		noteListing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> list, View view, int index, long noteId) {
				Log.d(TAG, "you touched item " + index + " / " + noteId);
				controller.editNote((Note) list.getAdapter().getItem(index), NoteListActivity.this);
			}
		});

	}

	private NoteListPresenter findPresenter() {

		Log.d(TAG, "getting presenter from intent");
		final NoteListPresenter noteListPresenter;

		if (getIntent().hasExtra(NoteListPresenter.class.getName())) {
			Log.d(TAG, "presenter was injected, use it");
			noteListPresenter = (NoteListPresenter) getIntent().getSerializableExtra(NoteListPresenter.class.getName());
		} else {
			Log.d(TAG, "no presenter provided, get one from the controller");
			noteListPresenter = controller.getNoteListPresenter(this);
		}

		return noteListPresenter;

	}

	private AppController findAppController() {

		Log.d(TAG, "get or create app controller");
		final AppController appController;

		final Intent intent = getIntent();

		if (intent.hasExtra(AppController.class.getName())) {
			Log.d(TAG, "app controller was injected, use it");
			appController = (AppController) intent.getSerializableExtra(AppController.class.getName());
		} else {
			Log.d(TAG, "app controller not injected - bootstrap one");
			appController = new AppControllerImpl();
		}

		return appController;

	}

	public NoteListPresenter getPresenter() {
		return presenter;
	}

}

