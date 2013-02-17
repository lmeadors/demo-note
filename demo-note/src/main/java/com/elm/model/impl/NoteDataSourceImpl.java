package com.elm.model.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import com.elm.bean.Message;
import com.elm.bean.Note;
import com.elm.model.NoteDataSource;
import com.elm.utility.DateUtility;
import com.elm.utility.LocalBroadcastUtility;
import com.elm.utility.SimpleAsyncTask;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class NoteDataSourceImpl extends SQLiteOpenHelper implements NoteDataSource, Serializable {

	private static final String TAG = NoteDataSourceImpl.class.getName();

	private final DateUtility dateUtility = new DateUtility();

	public static final String DB_NAME = "DEMO_NOTES";
	private static final String TABLE_NAME = "NOTES";
	private static final String NOTE_ID = "NOTE_ID";
	private static final String NOTE_TITLE = "NOTE_TITLE";
	private static final String NOTE_TEXT = "NOTE_TEXT";
	private static final String NOTE_DATE = "NOTE_DATE";
	private static final String[] ALL_COLS = {NOTE_ID, NOTE_TITLE, NOTE_TEXT, NOTE_DATE};

	private final LocalBroadcastUtility localBroadcastUtility;
	private SQLiteDatabase database;

	public NoteDataSourceImpl(Context context) {
		super(context, DB_NAME, null, 1);
		localBroadcastUtility = new LocalBroadcastUtility(context);
		init();
	}
/*
	public Note fetch(Long id) {
		Log.d(TAG, "fetching note " + id);
		SQLiteDatabase database = getReadableDatabase();
		final Cursor cursor = database.query(TABLE_NAME, ALL_COLS, NOTE_ID + " = ?", new String[]{id.toString()}, null, null, null);

		if (null != cursor) {
			try {
				if (cursor.moveToFirst()) {
					Log.d(TAG, "note " + id + " found");
					return new Note(
							cursor.getLong(0),
							Timestamp.valueOf(cursor.getString(3)),
							cursor.getString(1),
							cursor.getString(2)
					);
				}
			} finally {
				cursor.close();
			}
		}

		Log.d(TAG, "note " + id + " not found");
		return null;

	}
*/
	public Note insert(Note note) {

		Log.d(TAG, "insert note " + note.getId());

		ContentValues values = new ContentValues();
		values.put(NOTE_ID, (Long) null);
		values.put(NOTE_TITLE, note.getTitle());
		values.put(NOTE_DATE, dateUtility.toSqlLiteDateString(note.getDate()));
		values.put(NOTE_TEXT, note.getText());

//		SQLiteDatabase database = getWritableDatabase();
		final Long newId = database.insert(TABLE_NAME, null, values);

		return new Note(newId, note);

	}

	public Note update(Note note) {
		Log.d(TAG, "update note " + note.getId());

		final ContentValues values = new ContentValues();

		values.put(NOTE_ID, note.getId());
		values.put(NOTE_TITLE, note.getTitle());
		values.put(NOTE_DATE, dateUtility.toSqlLiteDateString(note.getDate()));
		values.put(NOTE_TEXT, note.getText());

//		SQLiteDatabase database = getWritableDatabase();
		database.update(TABLE_NAME, values, NOTE_ID + " = ?", new String[]{note.getId().toString()});

		return note;
	}

	public Long delete(Long id) {

		Log.d(TAG, "delete note " + id);

//		SQLiteDatabase database = getWritableDatabase();
		database.delete(TABLE_NAME, NOTE_ID + " = ?", new String[]{id.toString()});

		return id;

	}

	public List<Note> list() {

		Log.d(TAG, "getting list");
		final List<Note> noteList = new LinkedList<Note>();
//		SQLiteDatabase database = getReadableDatabase();
		final Cursor cursor = database.query(TABLE_NAME, ALL_COLS, null, null, null, null, null);

		if (null != cursor) {
			try {
				if (cursor.moveToFirst()) {
					do {
						final String dateString = cursor.getString(3);
						noteList.add(new Note(
								cursor.getLong(0),
								Timestamp.valueOf(dateString == null ? "1999-12-31 00:00:00" : dateString),
								cursor.getString(1),
								cursor.getString(2)));
					} while (cursor.moveToNext());
				}
			} finally {
				cursor.close();
			}
		}

		return noteList;

	}

	public void listAsync() {

		final AsyncTask<Void, Void, Message<List<Note>>> task;

		Log.d(TAG, "creating task to list notes");
		task = new SimpleAsyncTask<Void, Void, Message<List<Note>>>(localBroadcastUtility, LIST_ACTION) {
			@Override
			protected Message<List<Note>> doInBackground(Void... voids) {
				Log.d(TAG, "returning list of notes");
				return new Message<List<Note>>(list());
			}
		};

		Log.d(TAG, "executing task to list notes");
		task.execute();

	}

//	public void fetchAsync(Long id) {
//		final AsyncTask<Long, Void, Message<Note>> task;
//		task = new SimpleAsyncTask<Long, Void, Message<Note>>(localBroadcastUtility, FETCH_ACTION) {
//			@Override
//			protected Message<Note> doInBackground(Long... ids) {
//				return new Message<Note>(fetch(ids[0]));
//			}
//		};
//		task.execute(id);
//	}

	public void insertAsync(Note note) {

		final SimpleAsyncTask<Note, Void, Message<Note>> task;

		task = new SimpleAsyncTask<Note, Void, Message<Note>>(localBroadcastUtility, INSERT_ACTION) {
			@Override
			protected Message<Note> doInBackground(Note... notes) {
				return new Message<Note>(insert(notes[0]));
			}
		};

		task.execute(note);

	}

	public void updateAsync(Note note) {

		final SimpleAsyncTask<Note, Void, Message<Note>> task;

		task = new SimpleAsyncTask<Note, Void, Message<Note>>(localBroadcastUtility, UPDATE_ACTION) {
			@Override
			protected Message<Note> doInBackground(Note... notes) {
				return new Message<Note>(update(notes[0]));
			}
		};

		task.execute(note);

	}

	public void deleteAsync(Long id) {

		final SimpleAsyncTask<Long, Void, Message<Long>> task;

		task = new SimpleAsyncTask<Long, Void, Message<Long>>(localBroadcastUtility, DELETE_ACTION) {
			@Override
			protected Message<Long> doInBackground(Long... ids) {
				return new Message<Long>(delete(ids[0]));
			}
		};

		task.execute(id);

	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NAME
				+ "("
				+ NOTE_ID + " INTEGER PRIMARY KEY,"
				+ NOTE_TITLE + " VARCHAR(100),"
				+ NOTE_TEXT + " TEXT,"
				+ NOTE_DATE + " TIMESTAMP"
				+ ")";
		sqLiteDatabase.execSQL(CREATE_NOTES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
		// no upgrade
	}

	@Override
	public void release() {
		database.close();
	}

	@Override
	public void init() {
		database = getWritableDatabase();
	}

}
