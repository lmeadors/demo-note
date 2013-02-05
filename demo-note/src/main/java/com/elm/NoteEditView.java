package com.elm;

import com.elm.bean.Note;

public interface NoteEditView {

	void setNote(Note note);

	void noteDeleted(Long noteId);

	void hideDelete();

	void showDelete();

}
