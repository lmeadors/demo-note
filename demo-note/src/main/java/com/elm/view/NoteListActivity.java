package com.elm.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.elm.NoteListPresenter;
import com.elm.NoteListView;
import com.elm.R;
import com.elm.bean.Note;
import com.elm.model.NoteDataSourceImpl;
import com.elm.utility.DateUtility;

import java.util.List;

public class NoteListActivity extends Activity implements NoteListView {

	private static final String TAG = NoteListActivity.class.getName();
	private final DateUtility dateUtility = new DateUtility();
	private NoteListPresenter presenter;

	private LinearLayout noteListing;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.d(TAG, "creating activity");
		super.onCreate(savedInstanceState);

		Log.d(TAG, "creating presenter");
		presenter = new NoteListPresenter(this, new NoteDataSourceImpl(this), this);

		Log.d(TAG, "setting view");
		setContentView(R.layout.main);

		Log.d(TAG, "getting ui components");
		noteListing = (LinearLayout) findViewById(R.id.note_list);

		Log.d(TAG, "view is ready for data");
		presenter.viewReady();

	}

	public void setNoteList(List<Note> noteList) {

		Log.d(TAG, "adding " + noteList.size() + " notes");

		final LayoutInflater inflater = getLayoutInflater();

		for (Note note : noteList) {

			final LinearLayout noteView = (LinearLayout) inflater.inflate(R.layout.note_list_item, null, false);

			final TextView dateView = (TextView) noteView.findViewById(R.id.date);
			final TextView titleView = (TextView) noteView.findViewById(R.id.title);

			dateView.setText(dateUtility.dateString(note.getDate()));
			titleView.setText(note.getTitle());

			Log.d(TAG, "adding note "+note);
			noteListing.addView(noteView);

		}

	}

}

