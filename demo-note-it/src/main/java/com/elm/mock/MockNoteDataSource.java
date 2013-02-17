package com.elm.mock;

import com.elm.bean.Note;
import com.elm.model.NoteDataSource;

import java.util.List;

public class MockNoteDataSource implements NoteDataSource {

	@Override
	public List<Note> list() {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

	@Override
	public Note insert(Note note) {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

	@Override
	public Note update(Note note) {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

	@Override
	public Long delete(Long id) {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

	@Override
	public void listAsync() {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

	@Override
	public void insertAsync(Note note) {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

	@Override
	public void updateAsync(Note note) {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

	@Override
	public void deleteAsync(Long id) {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

}
