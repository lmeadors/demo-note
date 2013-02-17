package com.elm.presenter;

import android.widget.ShareActionProvider;
import com.elm.bean.Note;

public interface NoteEditPresenter {

	public void release();

	public void viewReady(Note note);

	public void delete(Note note);

	public void save(Note note);

	void prepareShareIntent(ShareActionProvider provider, String subject, String text);

}
