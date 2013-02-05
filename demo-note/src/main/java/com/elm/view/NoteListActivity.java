package com.elm.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.elm.NoteListView;
import com.elm.R;
import com.elm.bean.Note;
import com.elm.model.NoteDataSourceImpl;
import com.elm.presenter.NoteListPresenter;

import java.util.Date;
import java.util.List;

public class NoteListActivity extends Activity implements NoteListView {

	private static final String TAG = NoteListActivity.class.getName();
	private NoteListPresenter presenter;

	private ListView noteListing;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.d(TAG, "creating activity");
		super.onCreate(savedInstanceState);

		Log.d(TAG, "creating presenter");
		presenter = new NoteListPresenter(this, new NoteDataSourceImpl(this), this);

		Log.d(TAG, "setting view");
		setContentView(R.layout.note_list);

		Log.d(TAG, "getting ui components");
		noteListing = (ListView) findViewById(R.id.note_list);

		Log.d(TAG, "view is ready for data");
		presenter.viewReady();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// only one menu option here...
		editNote(new Note());
		return true;
	}

	public void setNoteList(List<Note> noteList) {

		// todo: remove these 50 dummy notes
		for (long i = 1; i <= 50; i++) {
			noteList.add(new Note(i * 10, new Date(), "test note " + i, "whatever"));
		}

		Log.d(TAG, "adding " + noteList.size() + " notes");

		noteListing.setAdapter(new NoteListAdapter(this, R.layout.note_list_item, noteList));
		noteListing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> list, View view, int index, long noteId) {
				Log.d(TAG, "you touched item " + index + " / " + noteId);
				editNote((Note) list.getAdapter().getItem(index));
			}
		});

	}

	private void editNote(final Note note) {
		startActivity(new Intent(this, NoteEditActivity.class).putExtra(Note.class.getName(), note));
	}

}

