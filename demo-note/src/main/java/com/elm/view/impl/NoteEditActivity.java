package com.elm.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import com.elm.view.NoteEditView;
import com.elm.R;
import com.elm.controller.AppController;
import com.elm.bean.Note;
import com.elm.presenter.NoteEditPresenter;
import com.elm.utility.DateUtility;

public class NoteEditActivity extends Activity implements NoteEditView {

	private static final String TAG = NoteEditActivity.class.getName();

	private final DateUtility dateUtility = new DateUtility();

	private NoteEditPresenter noteEditPresenter;
	private Note note;

	private ShareActionProvider shareActionProvider;

	private TextView id;
	private TextView date;
	private EditText title;
	private EditText text;
	private MenuItem deleteMenuItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Log.d(TAG, "get note from calling intent");
		note = (Note) getIntent().getSerializableExtra(Note.class.getName());

		Log.d(TAG, "get app controller from intent");
		final AppController appController = (AppController) getIntent().getSerializableExtra(AppController.class.getName());

		Log.d(TAG, "get presenter from controller");
		noteEditPresenter = appController.getNoteEditPresenter(this, this, note);

		Log.d(TAG, "prepare view components");
		setContentView(R.layout.note_edit);
		id = (TextView) findViewById(R.id.note_id);
		date = (TextView) findViewById(R.id.note_date);
		title = (EditText) findViewById(R.id.note_title);
		text = (EditText) findViewById(R.id.note_text);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		Log.d(TAG, "create menu");
		getMenuInflater().inflate(R.menu.note_edit, menu);

		// we need to hide the delete menu item in the case of a NEW note
		deleteMenuItem = menu.findItem(R.id.delete_note);

		// get our share provider
		shareActionProvider = (ShareActionProvider) menu.findItem(R.id.share_note).getActionProvider();

		noteEditPresenter.viewReady();

		return true;

	}

	@Override
	protected void onPause() {
		Log.d(TAG, "releasing presenter");
		noteEditPresenter.release();
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

			case R.id.save_note: {
				Note noteToSave = new Note(note.getId(), note.getDate(), title.getText().toString(), text.getText().toString());
				noteEditPresenter.save(noteToSave);
				break;
			}

			case R.id.delete_note: {
				noteEditPresenter.delete(note);
				break;
			}
		}

		return true;

	}

	private void prepareShareIntent() {

		if (shareActionProvider != null) {

			final Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

			// Add data to the intent, the receiving app will decide what to do with it.
			intent.putExtra(Intent.EXTRA_SUBJECT, note.getTitle());
			intent.putExtra(Intent.EXTRA_TEXT, note.getText());

			shareActionProvider.setShareIntent(intent);

		}
	}

	public void setNote(Note note) {

		this.note = note;

		Log.d(TAG, "setting values on view components");
		if (null != note.getId()) {
			id.setText(note.getId().toString());
		}
		date.setText(dateUtility.dateString(note.getDate()));
		title.setText(note.getTitle());
		text.setText(note.getText());

		// todo: add call to this to change listeners?
		prepareShareIntent();

	}

	public void noteDeleted(Long noteId) {
		// go back to previous activity
		finish();
	}

	public void hideDelete() {
		deleteMenuItem.setEnabled(false);
	}

	public void showDelete() {
		deleteMenuItem.setEnabled(true);
	}

}
