package com.elm.mock;

import com.elm.bean.Note;
import com.elm.view.NoteEditView;

public class MockNoteEditView implements NoteEditView {

	@Override
	public void setNote(Note note) {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

	@Override
	public void noteDeleted(Long noteId) {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

	@Override
	public boolean hideDelete() {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

	@Override
	public boolean showDelete() {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

	@Override
	public void noteSaved(Note note) {
		throw new UnsupportedOperationException("not implemented - " + this);
	}

}
