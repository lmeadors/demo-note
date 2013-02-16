package com.elm.view;

import android.view.MenuItem;
import com.elm.bean.Note;

public interface NoteEditView {

	void setNote(Note note);

	void noteDeleted(Long noteId);

	boolean hideDelete();

	boolean showDelete();

	void noteSaved(Note note);

}
