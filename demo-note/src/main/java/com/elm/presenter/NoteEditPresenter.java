package com.elm.presenter;

import com.elm.bean.Note;

public interface NoteEditPresenter {

	public void release();

	public void viewReady();

	public void delete(Note note);

	public void save(Note note);

}
