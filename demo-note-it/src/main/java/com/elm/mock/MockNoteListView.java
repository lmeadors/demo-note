package com.elm.mock;

import com.elm.bean.Note;
import com.elm.view.NoteListView;

import java.util.List;

public class MockNoteListView implements NoteListView {

	@Override
	public void setNoteList(List<Note> noteList) {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

}
