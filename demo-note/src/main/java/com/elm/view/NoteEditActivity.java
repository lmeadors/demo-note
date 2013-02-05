package com.elm.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import com.elm.R;
import com.elm.bean.Note;
import com.elm.utility.DateUtility;

public class NoteEditActivity extends Activity {

	private static final String TAG = NoteEditActivity.class.getName();

	private final DateUtility dateUtility = new DateUtility();

	private Note note;
	private TextView id;
	private TextView date;
	private EditText title;
	private EditText text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Log.d(TAG, "get note from calling intent");
		note = (Note) getIntent().getSerializableExtra(Note.class.getName());

		Log.d(TAG, "get view components");
		setContentView(R.layout.note_edit);
		id = (TextView) findViewById(R.id.note_id);
		date = (TextView) findViewById(R.id.note_date);
		title = (EditText) findViewById(R.id.note_title);
		text = (EditText) findViewById(R.id.note_text);

		Log.d(TAG, "setting values on view components");
		if(null != note.getId())
			id.setText(note.getId().toString());
		date.setText(dateUtility.dateString(note.getDate()));
		title.setText(note.getTitle());
		text.setText(note.getNote());

		Log.d(TAG, "let's edit " + note);

	}

}
